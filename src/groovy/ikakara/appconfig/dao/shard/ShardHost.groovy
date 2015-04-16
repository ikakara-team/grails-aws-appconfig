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

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import grails.validation.ValidationErrors
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility

@GrailsCompileStatic
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE)
@Slf4j("LOG")
@ToString(includePackage=false, ignoreNulls=true)
@Validateable
class ShardHost extends NameVersionShard {
  // id_shard
  @JsonProperty String host
  @JsonProperty String port
  @JsonProperty String options

  static constraints = {
    shard size: 0..99, blank: false
    host blank: false
    port blank: false
    options blank: true
  }

  @Override
  void initParameters(Map params) {
    if (params) {
      super.initParameters(params)
      host = (String) params.host
      port = (String) params.port
      options = (String) params.options
    }
  }

  ShardHost() {

  }

  ShardHost(Map params) {
    initParameters(params)
  }

  String prettyPrint() {
    return shard + " - " + host + ":" + port
  }
}
