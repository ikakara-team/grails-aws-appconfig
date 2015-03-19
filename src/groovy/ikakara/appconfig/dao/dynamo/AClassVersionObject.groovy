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

import java.util.Date
import java.util.List
import java.util.Map

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

/**
 *
 * @author Allen
 */
@Slf4j("LOG")
@CompileStatic
abstract public class AClassVersionObject extends ADynamoObject implements ICommandObject {

  static public final String STATUS_UNKNOWN = "UNKNOWN"
  static public final String STATUS_ACTIVE = "ACTIVE"
  static public final String STATUS_INACTIVE = "INACTIVE"

  protected String className
  protected String version // YYMMddHHmmss
  protected String versionStatus
  protected String versionNote
  // transient
  protected Date versionDate = null

  @Override
  abstract public ADynamoObject newInstance(Item item)

  @Override
  abstract public String tableName()

  @Override
  abstract public Map initTable()

  @Override
  public Object valueHashKey() {
    return className
  }

  @Override
  public String nameHashKey() {
    return "ClassName"
  }

  @Override
  public Object valueRangeKey() {
    return version
  }

  @Override
  public String nameRangeKey() {
    return "Version"
  }

  @Override
  public void marshalAttributesIN(Item item) {
    //if (map != null && !map.isEmpty()) {
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
  public Item marshalItemOUT(boolean bRemoveAttributeNull) {
    Item outItem = new Item()

    if (versionStatus != null && !"".equals(versionStatus)) {
      outItem = outItem.withString("VersionStatus", versionStatus)
    } else if (bRemoveAttributeNull) {
      outItem = outItem.removeAttribute("VersionStatus")
    }
    if (versionNote != null && !"".equals(versionNote)) {
      outItem = outItem.withString("VersionNote", versionNote)
    } else if (bRemoveAttributeNull) {
      outItem = outItem.removeAttribute("VersionNote")
    }

    return outItem
  }

  @DynamoDBIgnore
  @Override
  public String getId() {
    return getClassName() + "_" + getVersion()
  }

  @Override
  public void setId(String id) {
    if (id != null) {
      String[] ids = id.split("_")
      if (ids.length > 1) {
        setClassName(ids[0])
        setVersion(ids[1])
      }
    }
  }

  @Override
  public void initParameters(Map params) {
    //if (params != null && !params.isEmpty()) {
    className = (String) params.get("className")
    version = (String) params.get("version")
    versionStatus = (String) params.get("versionStatus")
    versionNote = (String) params.get("versionNote")
    //}
  }

  @Override
  public boolean validate() {
    return true // needed to be used as "command object"
  }

  @DynamoDBIgnore
  public Date getVersionDate() {
    if (versionDate == null) {
      if (version != null) {
        versionDate = CalendarUtil.getDateFromString_CONCISE(version)
      } else {
        setVersionDate(new Date())
      }
    }
    return versionDate
  }

  public void setVersionDate(Date d) {
    versionDate = d
    version = CalendarUtil.getStringFromDate_CONCISE(d)
  }

  @DynamoDBHashKey(attributeName = "ClassName")
  public String getClassName() {
    return className
  }

  public void setClassName(String name) {
    className = name
  }

  @DynamoDBRangeKey(attributeName = "Version")
  public String getVersion() {
    return version
  }

  public void setVersion(String ver) {
    version = ver
  }

  @DynamoDBAttribute(attributeName = "VersionStatus")
  public String getVersionStatus() {
    return versionStatus
  }

  public void setVersionStatus(String s) {
    versionStatus = s
  }

  @DynamoDBAttribute(attributeName = "VersionNote")
  public String getVersionNote() {
    return versionNote
  }

  public void setVersionNote(String d) {
    versionNote = d
  }

  public List<AClassVersionObject> findByClassAndStatus(String status) {
    String where = "VersionStatus = :myStatus"
    ValueMap valueMap = new ValueMap()
    .withString(":myStatus", status)

    List list = super.query(nameHashKey(), valueHashKey(), null, where, valueMap)
    return list
  }
}
