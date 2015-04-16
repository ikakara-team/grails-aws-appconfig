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

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.document.Item

import ikakara.appconfig.dao.IShardObject
import ikakara.appconfig.dao.shard.NameVersionShard
import ikakara.awsinstance.dao.dynamo.ADynamoObject
import ikakara.awsinstance.json.FasterXMLInstance

@CompileStatic
@Slf4j("LOG")
@ToString(includePackage=false, ignoreNulls=true, excludes="shardMapStr")
abstract class AClassVersionShardObject extends AClassVersionObject implements IShardObject {

  Integer shardCount = 0
  String shardMapStr

  // shouldn't be needed but groovyc complains in concrete subclasses using @CompileStatic that there's no setter
  void setShardCount(Integer count) {
    shardCount = count
  }

  @Override
  void marshalAttributesIN(Item item) {
    if (!item) {
      return
    }

    super.marshalAttributesIN(item)
    if (item.isPresent("ShardCount")) {
      shardCount = item.getInt("ShardCount")
    }
    if (item.isPresent("ShardMap")) {
      shardMapStr = item.getString("ShardMap")
    }
  }

  @Override
  Item marshalItemOUT(boolean removeAttributeNull) {
    Item outItem = super.marshalItemOUT(removeAttributeNull) ?: new Item()

    if (shardCount != null) {
      outItem = outItem.withNumber("ShardCount", shardCount)
    } else if (removeAttributeNull) {
      outItem = outItem.removeAttribute("ShardCount")
    }
    if (shardMapStr) {
      outItem = outItem.withString("ShardMap", shardMapStr)
    } else if (removeAttributeNull) {
      outItem = outItem.removeAttribute("ShardMap")
    }

    return outItem
  }

  @Override
  void initParameters(Map params) {
    if (params) {
      super.initParameters(params)
      setShardCount((String) params.shardCount)
      shardMapStr = (String) params.shardMapStr
    }
  }

  void setShardCount(String s) {
    if (s) {
      try {
        shardCount = s as Integer
      } catch (NumberFormatException e) {
        LOG.error("setShardCount:$e.message")
      }
    }
  }

  String toJSON(Map sm) {
    if (sm != null) {
      setShardMap(sm)
    }

    if (getShardMap() != null) {
      try {
        shardMapStr = FasterXMLInstance.objectMapper.writeValueAsString(getShardMap())
      } catch (e) {
        LOG.error("toJSON $e.message")
      }
    }
    return shardMapStr
  }

  Map fromJSON() {
    if (shardMapStr != null) {
      try {
        Map map = (Map)FasterXMLInstance.objectMapper.readValue(shardMapStr, typeReference())
        setShardMap(map)
      } catch (e) {
        LOG.error("fromJSON $e.message")
      }
    }
    return getShardMap()
  }

  void replaceShard(Integer key, NameVersionShard shard) {
    if (getShardMap() == null) {
      fromJSON()
      if (getShardMap() == null) {
        setShardMap([:])
      }
    }

    Map sm = getShardMap()

    NameVersionShard s = (NameVersionShard) sm.remove(key)
    if (s) {
      LOG.info("Removing shard: $s")
    }

    if (shard) {
      sm[key] = shard
      setShardMap(sm)
    }

    toJSON(null)
  }

  NameVersionShard findShard(Integer shardId) {
    NameVersionShard shard
    if (getShardMap() == null) {
      fromJSON()
    }

    if (getShardMap()) {
      shard = (NameVersionShard) getShardMap()[shardId]
      if (shard) {
        // init
        shard.setId_version(version)
        shard.setId_name(className)
        shard.setShard(shardId)
      }
    }

    return shard
  }

  @DynamoDBAttribute(attributeName = "ShardCount")
  Integer getShardCount() {
    return shardCount
  }

  @DynamoDBAttribute(attributeName = "ShardMap")
  String getShardMapStr() {
    return shardMapStr
  }
}
