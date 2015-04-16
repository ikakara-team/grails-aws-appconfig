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
import grails.util.Holders
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import org.apache.tomcat.jdbc.pool.DataSource
import org.apache.tomcat.jdbc.pool.PoolProperties

import ikakara.appconfig.util.Stats

@CompileStatic
@Slf4j("LOG")
@ToString(includePackage=false, excludes="_username,_password")
class SQLHelper {

  // config requires a boolean true, (String) "true" will be false
  private static Boolean _pooled = Holders.flatConfig["dataSource.pooled"] instanceof Boolean ? (Boolean) Holders.flatConfig["dataSource.pooled"] : false
  private static Map<String, DataSource> _ds_map = [:]
  //
  private static final int MAX_RETRIES = 5
  private static final int WAIT_TIME = 200 // milliseconds
  private static final int WAIT_TIME_COMMUNICATION_ERROR = 30000 // milliseconds
  //
  private String _url
  private String _bare_url
  private String _username
  private String _password
  private String _driver
  private Sql _sql
  private DataSource _ds
  private Stats _stats
  static Map<String, Stats> _conn_stats = new HashMap<String, Stats>()

  // this can be removed later
  static synchronized HashMap<String, Stats.Info> getAllStats() {
    Map<String, Stats.Info> ret = [:]
    _conn_stats.each { String key, Stats value ->
      ret[key] = value.get()
    }
    return ret
  }

  static boolean isPooled() {
    return _pooled
  }

  SQLHelper(SQLDescriptor des) {
    LOG.debug("SQLHelper:${Holders.flatConfig["dataSource.pooled"]} pooled:$_pooled")
    if (des) {
      _url = des.url
      _bare_url = des.url
      _username = des.username
      _password = des.password
      _driver = des.driverClassName

      // Append options
      if (des.options) {
        _url += "?" + des.options
      }

      // Use dataSource pool
      if (_pooled) {
        _ds = _getDataSource()
      }
      _stats = getStats(_bare_url)
    }
  }

  static synchronized Stats getStats(String url) {
    Stats ret = _conn_stats[url]
    if (!ret) {
      ret = new Stats(url.substring(url.lastIndexOf('/') + 1))
      _conn_stats[url] = ret
    }
    return ret
  }

  synchronized DataSource _getDataSource() {
    DataSource ds = _ds_map[_url]
    if (!ds) {
      // TBD: many of these properties need to be in a config
      ds = new DataSource(poolProperties: new PoolProperties(
        url: _url, driverClassName: _driver, //"com.mysql.jdbc.Driver")
        username: _username, password: _password, jmxEnabled: true,
        testWhileIdle: false, testOnBorrow: true, validationQuery: "SELECT 1",
        testOnReturn: false, validationInterval: 30000, timeBetweenEvictionRunsMillis: 30000,
        maxActive: 20, maxIdle: 4, initialSize: 0, maxWait: 600000, // 10 mins
        //removeAbandonedTimeout: 60,
        minEvictableIdleTimeMillis: 30000, minIdle: 0, logAbandoned: true,
        //jdbcInterceptors: "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer",
        removeAbandoned: false
      ))
      _ds_map[_url] = ds
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

  void close() {
    if (_sql) {
      _sql.close()
      _sql = null
    }
  }

  Sql sql_instance() {
    long start = System.currentTimeMillis()
    int retries = 0
    int wait = WAIT_TIME
    while (!_sql) {
      try {
        if (_ds) {
          _sql = new Sql(_ds)
        } else {
          _sql = Sql.newInstance(_url, _username, _password, _driver)
        }
      } catch (SQLException e) {
        LOG.error("sql_instance $_url: $e.message")
      } catch (ClassNotFoundException cnfe) {
        LOG.error("sql_instance:", cnfe)
      } finally {
        if (!_sql) {
          retries++
          if (retries > MAX_RETRIES) {
            LOG.error("sql_instance: COMMUNICATION RETRY FAILED!!!")
            break
          }
          try {
            LOG.error("sql_instance: COMMUNICATION RETRY wait=$wait")
            sleep(wait)
            // exponential backoff
            wait *= 2
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

  GroovyRowResult retry_firstRow(String q, List param_array) throws SQLException {
    if (!sql_instance()) {
      throw new SQLException("sql_instance() failed!!!")
    }

    int retries = 0
    int wait = WAIT_TIME
    GroovyRowResult row
    while (!row) {
      try {
        row = _sql.firstRow(q, param_array)
      } catch (SQLException e) {
        LOG.error("retry_firstRow $_url: $e.message")
      } catch (unknown) {
        LOG.error("retry_firstRow:", unknown)
      } finally {
        if (!row) {
          retries++
          if (retries > MAX_RETRIES) {
            throw new SQLException("retry_firstRow: RETRY FAILED!!!")
          }
          try {
            LOG.error("retry_firstRow: RETRY wait=$wait")
            sleep(wait)
            // exponential backoff
            wait *= 2
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
