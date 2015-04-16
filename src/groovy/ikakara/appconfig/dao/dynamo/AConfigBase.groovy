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
import groovy.util.logging.Slf4j

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.model.TableDescription

import ikakara.awsinstance.aws.AWSInstance
import ikakara.awsinstance.aws.DynamoHelper
import ikakara.awsinstance.dao.ITypeObject
import ikakara.awsinstance.dao.dynamo.ADynamoObject

@CompileStatic
@DynamoDBTable(tableName = "ConfigVersions")
@Slf4j("LOG")
abstract class AConfigBase extends AClassVersionShardObject implements ITypeObject {

  private static String TABLE_NAME

  @Override
  synchronized String tableName() {
    if (TABLE_NAME == null) {
      TABLE_NAME = DynamoHelper.getTableName(AConfigBase.class, "grails.plugin.awsappconfig.dataSource")
      DynamoHelper.initTable(TABLE_NAME, this, "grails.plugin.awsappconfig.dataSource")
    }
    return TABLE_NAME
  }

  Map initTable() {
    Map map = DynamoHelper.getTableInformation(tableName())
    if (map == null) {
      // Table doesn't exist.  Let's create it.
      ProvisionedThroughput THRUPUT = new ProvisionedThroughput(1L, 1L)

      CreateTableRequest req = new CreateTableRequest()
      .withTableName(tableName())
      .withAttributeDefinitions(
        new AttributeDefinition(nameHashKey(), ScalarAttributeType.S),
        new AttributeDefinition(nameRangeKey(), ScalarAttributeType.S))
      .withKeySchema(
        new KeySchemaElement(nameHashKey(), KeyType.HASH),
        new KeySchemaElement(nameRangeKey(), KeyType.RANGE))
      .withProvisionedThroughput(THRUPUT)

      Table table = AWSInstance.DYNAMO_DB().createTable(req)

      try {
        // Wait for the table to become active
        TableDescription desc = table.waitForActive()
        map = DynamoHelper.tableDescriptionToMap(desc)
      } catch (InterruptedException ie) {
        LOG.error(ie.message, ie)
        map = DynamoHelper.getTableInformation(tableName())
      }

    }
    return map
  }

  @Override
  Item marshalItemOUT(boolean removeAttributeNull) {
    Item outItem = super.marshalItemOUT(removeAttributeNull)
    if (outItem == null) {
      outItem = new Item()
    }

    if (getType()) {
      outItem = outItem.withString("ConfigType", getType())
    } else if (removeAttributeNull) {
      outItem = outItem.removeAttribute("ConfigType")
    }

    return outItem
  }

  @DynamoDBAttribute(attributeName = "ConfigType")
  abstract String getType()

  ADynamoObject newInstance(Item item) {
    ADynamoObject obj

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

  AConfigBase() {
    super()
  }

  AConfigBase(Map params) {
    super()
    initParameters(params)
  }

  List<AConfigBase> findByType() {
    // Scan items for ConfigType
    String where = "ConfigType = :myConfigType"
    ValueMap valueMap = new ValueMap().withString(":myConfigType", getType())
    return super.scan(where, valueMap)
  }

  List<AConfigBase> findByTypeAndVersionStatus(String status) {
    // Scan items for ConfigType and VersionStatus
    String where = "ConfigType = :myConfigType AND VersionStatus = :myVersionStatus"
    ValueMap valueMap = new ValueMap()
    .withString(":myConfigType", getType())
    .withString(":myVersionStatus", status)
    return super.scan(where, valueMap)
  }
}
