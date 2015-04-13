/* Copyright 2014-2015 Allen Arakaki.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ikakara.appconfig.dao.rdb

import java.sql.SQLException
import java.util.ArrayList
import java.util.HashMap
import java.util.Map

import groovy.transform.ToString
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import grails.util.Holders

import org.apache.tomcat.jdbc.pool.DataSource
import org.apache.tomcat.jdbc.pool.PoolProperties
import org.apache.commons.lang.builder.ToStringBuilder

import ikakara.appconfig.dao.rdb.SQLDescriptor
import ikakara.appconfig.util.Stats

@ToString(includePackage=false, excludes="_username,_password")
@Slf4j("LOG")
@CompileStatic
public class SQLHelper {

  // config requires a boolean true, (String) "true" will be false
  private static Boolean _pooled = Holders.getFlatConfig().get("dataSource.pooled") instanceof Boolean ? (Boolean) Holders.getFlatConfig().get("dataSource.pooled") : false
  private static Map<String, DataSource> _ds_map = new HashMap<String, DataSource>()
  //
  private final int MAX_RETRIES = 5
  private final int WAIT_TIME = 200 // milliseconds
  private final int WAIT_TIME_COMMUNICATION_ERROR = 30000 // milliseconds
  //
  private String _url = null
  private String _bare_url = null
  private String _username = null
  private String _password = null
  private String _driver = null
  private Sql _sql = null
  private DataSource _ds = null
  private Stats _stats = null
  static Map<String, Stats> _conn_stats = new HashMap<String, Stats>()

  // this can be removed later
  static public synchronized HashMap<String, Stats.Info> getAllStats() {
    HashMap<String, Stats.Info> ret = new HashMap<String, Stats.Info>()
    for (Map.Entry<String, Stats> e : _conn_stats.entrySet()) {
      ret.put(e.getKey(), e.getValue().get())
    }
    return ret
  }

  static public boolean isPooled() {
    return _pooled
  }

  SQLHelper(SQLDescriptor des) {
    LOG.debug("SQLHelper:" + Holders.getFlatConfig().get("dataSource.pooled") + " pooled:" + _pooled)
    if (des != null) {
      _url = des.getUrl()
      _bare_url = des.url
      _username = des.username
      _password = des.password
      _driver = des.driverClassName

      // Append options
      if (des.getOptions() != null && !"".equals(des.getOptions())) {
        _url = _url + "?" + des.getOptions()
      }

      // Use dataSource pool
      if (_pooled) {
        _ds = _getDataSource()
      }
      _stats = getStats(_bare_url)
    }
  }

  static public synchronized Stats getStats(String url) {
    Stats ret = _conn_stats.get(url)
    if (ret == null) {
      ret = new Stats(url.substring(url.lastIndexOf('/') + 1))
      _conn_stats.put(url, ret)
    }
    return ret
  }

  synchronized DataSource _getDataSource() {
    DataSource ds = _ds_map.get(_url)
    if (ds == null) {
      // TBD: many of these properties need to be in a config
      PoolProperties p = new PoolProperties()
      p.setUrl(_url)
      p.setDriverClassName(_driver) //"com.mysql.jdbc.Driver")
      p.setUsername(_username)
      p.setPassword(_password)
      p.setJmxEnabled(true)
      p.setTestWhileIdle(false)
      p.setTestOnBorrow(true)
      p.setValidationQuery("SELECT 1")
      p.setTestOnReturn(false)
      p.setValidationInterval(30000)
      p.setTimeBetweenEvictionRunsMillis(30000)
      p.setMaxActive(20)
      p.setMaxIdle(4)
      p.setInitialSize(0)
      p.setMaxWait(600000) // 10 mins
      //p.setRemoveAbandonedTimeout(60)
      p.setMinEvictableIdleTimeMillis(30000)
      p.setMinIdle(0)
      p.setLogAbandoned(true)
      p.setRemoveAbandoned(false)
      //p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
      ds = new DataSource()
      ds.setPoolProperties(p)
      _ds_map.put(_url, ds)
    }
    return ds
  }

  @Override
  protected void finalize() {
    close()
    try {
      super.finalize()
    } catch (Throwable t) {
      LOG.error("finalize:", t)
    }
  }

  public void close() {
    if (_sql != null) {
      _sql.close()
      _sql = null
    }
  }

  Sql sql_instance() {
    long start = System.currentTimeMillis()
    int retries = 0
    int wait = WAIT_TIME
    while (_sql == null) {
      try {
        if (_ds != null) {
          _sql = new Sql(_ds)
        } else {
          _sql = Sql.newInstance(_url, _username, _password, _driver)
        }
      } catch (SQLException e) {
        LOG.error("sql_instance " + _url + ": " + e.getMessage())
      } catch (ClassNotFoundException cnfe) {
        LOG.error("sql_instance:", cnfe)
      } finally {
        if (_sql == null) {
          retries++
          if (retries > MAX_RETRIES) {
            LOG.error("sql_instance: COMMUNICATION RETRY FAILED!!!")
            break
          }
          try {
            LOG.error("sql_instance: COMMUNICATION RETRY wait=" + wait)
            Thread.sleep(wait)
            // exponential backoff
            wait = wait * 2
          } catch (InterruptedException ie) {
            LOG.error("sql_instance:", ie)
            break
          }
        }
      }
    }
    _stats.add(System.currentTimeMillis() - start)
    return _sql
  }

  GroovyRowResult retry_firstRow(String q, ArrayList param_array) throws SQLException {
    if (sql_instance() == null) {
      throw new SQLException("sql_instance() failed!!!")
    }

    int retries = 0
    int wait = WAIT_TIME
    GroovyRowResult row = null
    while (row == null) {
      try {
        row = (GroovyRowResult) _sql.firstRow(q, param_array)
      } catch (SQLException e) {
        LOG.error("retry_firstRow " + _url + ": " + e.getMessage())
      } catch (Exception unknown) {
        LOG.error("retry_firstRow:", unknown)
      } finally {
        if (row == null) {
          retries++
          if (retries > MAX_RETRIES) {
            throw new SQLException("retry_firstRow: RETRY FAILED!!!")
          }
          try {
            LOG.error("retry_firstRow: RETRY wait=" + wait)
            Thread.sleep(wait)
            // exponential backoff
            wait = wait * 2
          } catch (InterruptedException ie) {
            LOG.error("retry_firstRow:", ie)
            break
          }
        }
      }
    }
    return row
  }

}
