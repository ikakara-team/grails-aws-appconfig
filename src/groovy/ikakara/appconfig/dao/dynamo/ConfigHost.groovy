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
package ikakara.appconfig.dao.dynamo

import java.util.Map

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import grails.validation.Validateable

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore

import com.fasterxml.jackson.core.type.TypeReference

import ikakara.appconfig.dao.shard.ShardHost

@Validateable(nullable = true)
@Slf4j("LOG")
@CompileStatic
public class ConfigHost extends AConfigBase {

  static public final String CONFIG_TYPE = "Host"

  @Override
  @DynamoDBAttribute(attributeName = "ConfigType")
  public String getType() {
    return CONFIG_TYPE
  }

  Map<Integer, ShardHost> shardMap

  @Override
  @DynamoDBIgnore
  public Map getShardMap() {
    return shardMap
  }

  @Override
  public void setShardMap(Map sm) {
    shardMap = sm
  }

  @Override
  @DynamoDBIgnore
  public TypeReference typeReference() {
    return new TypeReference<Map<Integer, ShardHost>>(){}
  }

  public ConfigHost() {
    super()
  }

  public ConfigHost(Map params) {
    super()
    initParameters(params)
  }

}
