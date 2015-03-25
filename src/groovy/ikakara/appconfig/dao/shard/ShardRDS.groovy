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
package ikakara.appconfig.dao.shard

import groovy.transform.ToString
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import grails.validation.Validateable
import grails.validation.ValidationErrors
import org.springframework.validation.DefaultMessageCodesResolver

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnore

import ikakara.appconfig.dao.rdb.SQLDescriptor

/**
 *
 * @author Allen
 */
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE)
@ToString(includePackage=false, ignoreNulls=true, excludes="username,password")
@Validateable
@Slf4j("LOG")
//@CompileStatic
public class ShardRDS extends NameVersionShard {
  // http://www.sql-workbench.net/manual/jdbc-setup.html
  // id_shard
  @JsonProperty String driverClassName
  @JsonProperty String url
  @JsonProperty String db
  @JsonProperty String username
  @JsonProperty String password
  @JsonProperty String options

  static constraints = {
    shard size: 0..99, blank: false
    driverClassName blank: false
    url blank: false
    db blank: false
    username blank: true
    password blank: true
    options blank: true
  }

  @Override
  public void initParameters(Map params) {
    if (params != null && !params.isEmpty()) {
      super.initParameters(params)
      driverClassName = (String) params.get("driverClassName")
      url = (String) params.get("url")
      db = (String) params.get("db")
      username = (String) params.get("username")
      password = (String) params.get("password")
      options = (String) params.get("options")
    }
  }

  public ShardRDS() {

  }

  public ShardRDS(Map params) {
    initParameters(params)
  }

  public String prettyPrint() {
    return shard + " - " + url_db_options()
  }

  public String url_db() {
    return ShardRDS.URL_DB(url, db)
  }

  public String url_db_options() {
    def sb = url_db()
    if(options) {
      sb = sb + "?" + options
    }
    return sb
  }

  static public String URL_DB(url, db) {
    return url + "/" + db
  }

  // Rewrite to be @Immutable, tuple

  static public SQLDescriptor sql_descriptor(HashMap shard) {
    SQLDescriptor des = new SQLDescriptor(
      shard.driverClassName,
      ShardRDS.URL_DB(shard.url, shard.db),
      shard.username,
      shard.password,
      shard.options ? shard.options : null
    )
    return des
  }

  static public SQLDescriptor sql_descriptor(ShardRDS shard) {
    SQLDescriptor des = new SQLDescriptor(
      shard.driverClassName,
      shard.url_db(),
      shard.username,
      shard.password,
      shard.options ? shard.options : null
    )
    return des
  }

}
