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

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap

import ikakara.awsinstance.dao.ICommandObject
import ikakara.awsinstance.dao.dynamo.ADynamoObject
import ikakara.awsinstance.util.CalendarUtil

@CompileStatic
@ToString(includePackage=false, includeNames=true, ignoreNulls=true)
@Slf4j("LOG")
abstract class ANameVersionObject extends ADynamoObject implements ICommandObject {

  public static final String STATUS_UNKNOWN = "UNKNOWN"
  public static final String STATUS_ACTIVE = "ACTIVE"
  public static final String STATUS_INACTIVE = "INACTIVE"

  @DynamoDBHashKey(attributeName = "NameId")
  String name
  @DynamoDBRangeKey(attributeName = "Version")
  Long version // YYMMddHHmmss
  @DynamoDBAttribute(attributeName = "VersionStatus")
  String versionStatus
  @DynamoDBAttribute(attributeName = "VersionNote")
  String versionNote

  // transient
  protected Date versionDate

  @Override
  def valueHashKey() {
    return name
  }

  @Override
  String nameHashKey() {
    return "NameId"
  }

  @Override
  def valueRangeKey() {
    return version
  }

  @Override
  String nameRangeKey() {
    return "Version"
  }

  @Override
  void marshalAttributesIN(Item item) {
    //if (map) {
    if (item.isPresent("NameId")) {
      name = item.getString("NameId")
    }
    if (item.isPresent("Version")) {
      version = item.getLong("Version")
    }
    if (item.isPresent("VersionStatus")) {
      versionStatus = item.getString("VersionStatus")
    }
    if (item.isPresent("VersionNote")) {
      versionNote = item.getString("VersionNote")
    }
    //}
  }

  @Override
  Item marshalItemOUT(List removeAttributeNull) {
    Item outItem = new Item()

    if (versionStatus) {
      outItem = outItem.withString("VersionStatus", versionStatus)
    } else if (removeAttributeNull != null) {
      removeAttributeNull.add("VersionStatus")
    }
    if (versionNote) {
      outItem = outItem.withString("VersionNote", versionNote)
    } else if (removeAttributeNull != null) {
      removeAttributeNull.add("VersionNote")
    }

    return outItem
  }

  @DynamoDBIgnore
  @Override
  String getId() {
    return name + "_" + version
  }

  @Override
  void setId(String id) {
    if (!id) {
      return
    }

    String[] ids = id.split("_")
    if (ids.length > 1) {
      setName(ids[0])
      setVersion(ids[1])
    }
  }

  @Override
  void initParameters(Map params) {
    //if (params) {
    name = (String) params.name
    version = (Long) params.version
    versionStatus = (String) params.versionStatus
    versionNote = (String) params.versionNote
    //}
  }

  @Override
  boolean validate() {
    return true // needed to be used as "command object"
  }

  ANameVersionObject withName(String str) {
    name = str
    return this
  }

  ANameVersionObject withVersion(Long ver) {
    version = ver
    return this
  }

  ANameVersionObject withVersion(Date date) {
    setVersionDate(date)
    return this
  }

  ANameVersionObject withVersion(String str) {
    setVersion(str)
    return this
  }

  ANameVersionObject withVersionStatus(String str) {
    versionStatus = str
    return this
  }

  ANameVersionObject withVersionNote(String str) {
    versionNote = str
    return this
  }

  void setVersion(String ver) {
    try {
      version = Long.parseLong(ver)
    } catch (e) {

    }
  }

  @DynamoDBIgnore
  Date getVersionDate() {
    if (!versionDate) {
      if (version != null) {
        versionDate = CalendarUtil.getDateFromString_CONCISE(version?.toString())
      } else {
        setVersionDate(new Date())
      }
    }
    return versionDate
  }

  void setVersionDate(Date d) {
    versionDate = d
    setVersion(CalendarUtil.getStringFromDate_CONCISE(d))
  }

  List<ANameVersionObject> findByNameAndStatus(String status) {
    String where = "VersionStatus = :myStatus"
    ValueMap valueMap = new ValueMap().withString(":myStatus", status)

    return super.query(nameHashKey(), valueHashKey(), null, where, valueMap)
  }
}
