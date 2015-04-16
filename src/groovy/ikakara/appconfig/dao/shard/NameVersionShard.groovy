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

import groovy.transform.CompileStatic

import com.fasterxml.jackson.annotation.JsonIgnore

import ikakara.awsinstance.dao.ICommandObject

@CompileStatic
class NameVersionShard extends BaseShard implements ICommandObject {
  // Transient
  @JsonIgnore String id_name
  @JsonIgnore Long   id_version

  String getNameVersionId() {
    return id_name + "_" + id_version
  }

  String getId() {
    return id_name + "_" + id_version + "_" + shard
  }

  void setId(String id) {
    if (id == null) {
      return
    }

    String[] ids = id.split("_")
    if (ids.length < 2) {
      return
    }

    id_name = ids[0]
    setId_version(ids[1])
    if (ids.length > 2 && "null" != ids[2]) {
      setIdShard(ids[2])
    }
  }

  void setId_version(String s) {
    if (s) {
      try {
        id_version = s as Long
      } catch (NumberFormatException e) {
        LOG.error("setId_version: $e.message")
      }
    }
  }

  void initParameters(Map params) {
    //if (params) {
    setIdShard((String) params.shard)
    //}
  }

  boolean validate() {
    return true // needed to be used as "command object"
  }
}
