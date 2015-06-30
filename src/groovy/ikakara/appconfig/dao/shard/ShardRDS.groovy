/* Copyright 2014-2015 the original author or authors.
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

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import grails.validation.ValidationErrors
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility

import ikakara.appconfig.dao.rdb.SQLDescriptor

@GrailsCompileStatic
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE)
@Slf4j("LOG")
@ToString(includePackage=false, includeNames=true, ignoreNulls=true, excludes="username,password")
@Validateable
class ShardRDS extends NameVersionShard {
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
  void initParameters(Map params) {
    if (params) {
      super.initParameters(params)
      driverClassName = (String) params.driverClassName
      url = (String) params.url
      db = (String) params.db
      username = (String) params.username
      password = (String) params.password
      options = (String) params.options
    }
  }

  ShardRDS() {

  }

  ShardRDS(Map params) {
    initParameters(params)
  }

  String prettyPrint() {
    return shard + " - " + url_db_options()
  }

  String url_db() {
    return ShardRDS.URL_DB(url, db)
  }

  String url_db_options() {
    def sb = url_db()
    if(options) {
      sb += "?" + options
    }
    return sb
  }

  static String URL_DB(String url, String db) {
    return url + "/" + db
  }

  // Rewrite to be @Immutable, tuple

  static SQLDescriptor sql_descriptor(HashMap<String, String> shard) {
    SQLDescriptor des = new SQLDescriptor(
      shard.driverClassName,
      ShardRDS.URL_DB(shard.url, shard.db),
      shard.username,
      shard.password,
      shard.options ?: null
    )
    return des
  }

  static SQLDescriptor sql_descriptor(ShardRDS shard) {
    return new SQLDescriptor(
      shard.driverClassName,
      shard.url_db(),
      shard.username,
      shard.password,
      shard.options ?: null
    )
  }
}
