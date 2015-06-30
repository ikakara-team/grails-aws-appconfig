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
package ikakara.appconfig.dao.dynamo

import grails.validation.Validateable
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore

import com.fasterxml.jackson.core.type.TypeReference

import ikakara.appconfig.dao.shard.ShardRDS

@CompileStatic
@Slf4j("LOG")
@Validateable(nullable = true)
class ConfigRDS extends AConfigBase {

  public static final String CONFIG_TYPE = "RDS"

  @DynamoDBAttribute(attributeName = "ConfigType")
  String getType() {
    return CONFIG_TYPE
  }

  Map<Integer, ShardRDS> shardMap

  @DynamoDBIgnore
  Map getShardMap() {
    return shardMap
  }

  @DynamoDBIgnore
  TypeReference typeReference() {
    return new TypeReference<Map<Integer, ShardRDS>>(){}
  }

  ConfigRDS() {
    super()
  }

  ConfigRDS(Map params) {
    super()
    initParameters(params)
  }
}
