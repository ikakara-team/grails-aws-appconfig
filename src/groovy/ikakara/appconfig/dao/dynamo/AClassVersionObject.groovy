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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap

import ikakara.awsinstance.dao.ICommandObject
import ikakara.awsinstance.dao.dynamo.ADynamoObject
import ikakara.awsinstance.util.CalendarUtil

@CompileStatic
@Slf4j("LOG")
abstract class AClassVersionObject extends ADynamoObject implements ICommandObject {

  public static final String STATUS_UNKNOWN = "UNKNOWN"
  public static final String STATUS_ACTIVE = "ACTIVE"
  public static final String STATUS_INACTIVE = "INACTIVE"

  String className
  String version // YYMMddHHmmss
  String versionStatus
  String versionNote

  // transient
  protected Date versionDate

  def valueHashKey() {
    return className
  }

  String nameHashKey() {
    return "ClassName"
  }

  def valueRangeKey() {
    return version
  }

  String nameRangeKey() {
    return "Version"
  }

  void marshalAttributesIN(Item item) {
    //if (map) {
    if (item.isPresent("ClassName")) {
      className = item.getString("ClassName")
    }
    if (item.isPresent("Version")) {
      version = item.getString("Version")
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
  Item marshalItemOUT(boolean removeAttributeNull) {
    Item outItem = new Item()

    if (versionStatus) {
      outItem = outItem.withString("VersionStatus", versionStatus)
    } else if (removeAttributeNull) {
      outItem = outItem.removeAttribute("VersionStatus")
    }
    if (versionNote) {
      outItem = outItem.withString("VersionNote", versionNote)
    } else if (removeAttributeNull) {
      outItem = outItem.removeAttribute("VersionNote")
    }

    return outItem
  }

  @DynamoDBIgnore
  @Override
  String getId() {
    return getClassName() + "_" + getVersion()
  }

  @Override
  void setId(String id) {
    if (!id) {
      return
    }

    String[] ids = id.split("_")
    if (ids.length > 1) {
      setClassName(ids[0])
      setVersion(ids[1])
    }
  }

  @Override
  void initParameters(Map params) {
    //if (params) {
    className = (String) params.className
    version = (String) params.version
    versionStatus = (String) params.versionStatus
    versionNote = (String) params.versionNote
    //}
  }

  @Override
  boolean validate() {
    return true // needed to be used as "command object"
  }

  @DynamoDBIgnore
  Date getVersionDate() {
    if (!versionDate) {
      if (version != null) {
        versionDate = CalendarUtil.getDateFromString_CONCISE(version)
      } else {
        setVersionDate(new Date())
      }
    }
    return versionDate
  }

  void setVersionDate(Date d) {
    versionDate = d
    version = CalendarUtil.getStringFromDate_CONCISE(d)
  }

  @DynamoDBHashKey(attributeName = "ClassName")
  String getClassName() {
    return className
  }

  @DynamoDBRangeKey(attributeName = "Version")
  String getVersion() {
    return version
  }

  @DynamoDBAttribute(attributeName = "VersionStatus")
  String getVersionStatus() {
    return versionStatus
  }

  @DynamoDBAttribute(attributeName = "VersionNote")
  String getVersionNote() {
    return versionNote
  }

  List<AClassVersionObject> findByClassAndStatus(String status) {
    String where = "VersionStatus = :myStatus"
    ValueMap valueMap = new ValueMap().withString(":myStatus", status)

    return super.query(nameHashKey(), valueHashKey(), null, where, valueMap)
  }
}
