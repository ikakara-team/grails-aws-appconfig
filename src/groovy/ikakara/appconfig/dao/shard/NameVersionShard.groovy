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

import groovy.transform.ToString
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnore

import ikakara.awsinstance.dao.ICommandObject

/**
 *
 * @author Allen
 */
@Slf4j("LOG")
@CompileStatic
public class NameVersionShard extends BaseShard implements ICommandObject {
  // Transient
  @JsonIgnore String id_name
  @JsonIgnore String id_version

  public String getNameVersionId() {
    return id_name + "_" + id_version
  }

  @Override
  public String getId() {
    return id_name + "_" + id_version + "_" + shard
  }

  @Override
  public void setId(String id) {
    if (id != null) {
      String[] ids = id.split("_")
      if (ids.length > 1) {
        id_name = ids[0]
        id_version = ids[1]
        if (ids.length > 2 && !"null".equals(ids[2])) {
          setIdShard(ids[2])
        }
      }
    }
  }

  @Override
  public void initParameters(Map params) {
    //if (params != null && !params.isEmpty()) {
    setIdShard((String) params.get("shard"))
    //}
  }

  @Override
  public boolean validate() {
    return true // needed to be used as "command object"
  }

}
