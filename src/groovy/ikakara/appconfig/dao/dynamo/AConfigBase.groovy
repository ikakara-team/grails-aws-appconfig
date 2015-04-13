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

import java.util.List
import java.util.Map

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.model.TableDescription
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap

import com.fasterxml.jackson.core.type.TypeReference

import ikakara.awsinstance.aws.AWSInstance
import ikakara.awsinstance.aws.DynamoHelper
import ikakara.awsinstance.dao.ITypeObject
import ikakara.awsinstance.dao.dynamo.ADynamoObject

@DynamoDBTable(tableName = "ConfigVersions")
@Slf4j("LOG")
@CompileStatic
abstract public class AConfigBase extends AClassVersionShardObject implements ITypeObject {

  private static String TABLE_NAME = null

  @Override
  synchronized public String tableName() {
    if (TABLE_NAME == null) {
      TABLE_NAME = DynamoHelper.getTableName(AConfigBase.class, "grails.plugin.awsappconfig.dataSource")
      DynamoHelper.initTable(TABLE_NAME, this, "grails.plugin.awsappconfig.dataSource")
    }
    return TABLE_NAME
  }

  @Override
  public Map initTable() {
    Map map = DynamoHelper.getTableInformation(tableName())
    if (map == null) {
      // Table doesn't exist.  Let's create it.
      ProvisionedThroughput THRUPUT = new ProvisionedThroughput(1L, 1L)

      CreateTableRequest req = new CreateTableRequest()
      .withTableName(tableName())
      .withAttributeDefinitions(
        new AttributeDefinition(this.nameHashKey(), ScalarAttributeType.S),
        new AttributeDefinition(this.nameRangeKey(), ScalarAttributeType.S))
      .withKeySchema(
        new KeySchemaElement(this.nameHashKey(), KeyType.HASH),
        new KeySchemaElement(this.nameRangeKey(), KeyType.RANGE))
      .withProvisionedThroughput(THRUPUT)

      Table table = AWSInstance.DYNAMO_DB().createTable(req)

      try {
        // Wait for the table to become active
        TableDescription desc = table.waitForActive()
        map = DynamoHelper.tableDescriptionToMap(desc)
      } catch (InterruptedException ie) {
        System.out.println(ie)
        map = DynamoHelper.getTableInformation(tableName())
      }

    }
    return map
  }

  @Override
  public Item marshalItemOUT(boolean bRemoveAttributeNull) {
    Item outItem = super.marshalItemOUT(bRemoveAttributeNull)
    if (outItem == null) {
      outItem = new Item()
    }

    if (getType() != null && !"".equals(getType())) {
      outItem = outItem.withString("ConfigType", getType())
    } else if (bRemoveAttributeNull) {
      outItem = outItem.removeAttribute("ConfigType")
    }

    return outItem
  }

  @Override
  @DynamoDBAttribute(attributeName = "ConfigType")
  abstract public String getType()

  @Override
  abstract public Map getShardMap()

  @Override
  abstract public void setShardMap(Map sm)

  @Override
  abstract public TypeReference typeReference()

  @Override
  public ADynamoObject newInstance(Item item) {
    ADynamoObject obj = null

    if (item != null) {
      // this is hacky to store different configs into one table
      if (item.isPresent("ConfigType")) {
        String type = item.getString("ConfigType")
        switch (type) {
        case ConfigRDS.CONFIG_TYPE: obj = new ConfigRDS(); break
        case ConfigHost.CONFIG_TYPE: obj = new ConfigHost(); break
        }
      }
    }
    
    obj?.marshalAttributesIN(item)

    return obj
  }

  public AConfigBase() {
    super()
  }

  public AConfigBase(Map params) {
    super()
    initParameters(params)
  }

  public List<AConfigBase> findByType() {
    // Scan items for ConfigType
    String where = "ConfigType = :myConfigType"
    ValueMap valueMap = new ValueMap()
    .withString(":myConfigType", getType())
    List list = super.scan(where, valueMap)

    return list
  }

  public List<AConfigBase> findByTypeAndVersionStatus(String status) {
    // Scan items for ConfigType and VersionStatus
    String where = "ConfigType = :myConfigType AND VersionStatus = :myVersionStatus"
    ValueMap valueMap = new ValueMap()
    .withString(":myConfigType", getType())
    .withString(":myVersionStatus", status)
    List list = super.scan(where, valueMap)

    return list
  }

}
