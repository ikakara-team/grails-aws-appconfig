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
import java.util.HashMap

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.apache.commons.lang.builder.ToStringBuilder

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.document.Item

import com.fasterxml.jackson.core.type.TypeReference

import ikakara.appconfig.dao.IShardObject
import ikakara.appconfig.dao.shard.NameVersionShard
import ikakara.awsinstance.json.FasterXMLInstance
import ikakara.awsinstance.dao.dynamo.ADynamoObject

@Slf4j("LOG")
@CompileStatic
abstract public class AClassVersionShardObject extends AClassVersionObject implements IShardObject {

  protected Integer shardCount = 0
  protected String shardMapStr

  @Override
  abstract public ADynamoObject newInstance(Item item)

  @Override
  abstract public Map getShardMap()

  @Override
  abstract public void setShardMap(Map sm)

  @Override
  abstract public TypeReference typeReference()

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("class", className).append("version", version)
    .append("status", versionStatus)
    .append("note", versionNote)
    .append("count", shardCount)
    .toString()
  }

  @Override
  public void marshalAttributesIN(Item item) {
    if (item != null) {
      super.marshalAttributesIN(item)
      if (item.isPresent("ShardCount")) {
        shardCount = item.getInt("ShardCount")
      }
      if (item.isPresent("ShardMap")) {
        shardMapStr = item.getString("ShardMap")
      }
    }
  }

  @Override
  public Item marshalItemOUT(boolean bRemoveAttributeNull) {
    Item outItem = super.marshalItemOUT(bRemoveAttributeNull)
    if (outItem == null) {
      outItem = new Item()
    }

    if (shardCount != null) {
      outItem = outItem.withNumber("ShardCount", shardCount)
    } else if (bRemoveAttributeNull) {
      outItem = outItem.removeAttribute("ShardCount")
    }
    if (shardMapStr != null && !"".equals(shardMapStr)) {
      outItem = outItem.withString("ShardMap", shardMapStr)
    } else if (bRemoveAttributeNull) {
      outItem = outItem.removeAttribute("ShardMap")
    }

    return outItem
  }

  @Override
  public void initParameters(Map params) {
    if (params != null && !params.isEmpty()) {
      super.initParameters(params)
      setShardCount((String) params.get("shardCount"))
      shardMapStr = (String) params.get("shardMapStr")
    }
  }

  public void setShardCount(String s) {
    if (s != null && !"".equals(s)) {
      try {
        shardCount = Integer.parseInt(s)
      } catch (NumberFormatException e) {
        LOG.error("setShardCount:" + e.getMessage())
      }
    }
  }

  public String toJSON(Map sm) {
    if (sm != null) {
      setShardMap(sm)
    }

    if (getShardMap() != null) {
      try {
        shardMapStr = FasterXMLInstance.objectMapper.writeValueAsString(getShardMap())
      } catch (Exception e) {
        LOG.error("toJSON" + e.getMessage())
      }
    }
    return shardMapStr
  }

  public Map fromJSON() {
    if (shardMapStr != null) {
      try {
        Map map = (Map)FasterXMLInstance.objectMapper.readValue(shardMapStr, typeReference())
        setShardMap(map)
      } catch (Exception e) {
        LOG.error("fromJSON" + e.getMessage())
      }
    }
    return getShardMap()
  }

  public void replaceShard(Integer key, NameVersionShard shard) {
    if (getShardMap() == null) {
      fromJSON()
      if (getShardMap() == null) {
        setShardMap(new HashMap<>())
      }
    }

    Map sm = getShardMap()

    NameVersionShard s = (NameVersionShard) sm.remove(key)
    if (s != null) {
      LOG.info("Removing shard:" + s)
    }

    if (shard != null) {
      sm.put(key, shard)
      setShardMap(sm)
    }

    toJSON(null)
  }

  public NameVersionShard findShard(Integer shardId) {
    NameVersionShard shard = null
    if (getShardMap() == null) {
      fromJSON()
    }

    if (getShardMap() != null) {
      shard = (NameVersionShard) getShardMap().get(shardId)
      if (shard != null) {
        // init
        shard.setId_version(version)
        shard.setId_name(className)
        shard.setShard(shardId)
      }
    }

    return shard
  }

  @Override
  @DynamoDBAttribute(attributeName = "ShardCount")
  public Integer getShardCount() {
    return shardCount
  }

  @Override
  public void setShardCount(Integer count) {
    shardCount = count
  }

  @Override
  @DynamoDBAttribute(attributeName = "ShardMap")
  public String getShardMapStr() {
    return shardMapStr
  }

  @Override
  public void setShardMapStr(String name) {
    shardMapStr = name
  }

}
