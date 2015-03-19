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
package ikakara.appconfig.web.platform

import static org.springframework.http.HttpStatus.*

import ikakara.appconfig.dao.dynamo.ConfigHost
import ikakara.appconfig.dao.shard.ShardHost

//import grails.plugin.springsecurity.annotation.Secured

//@Secured(['ROLE_ADMIN'])
class PlatformConfigHostController {

  static allowedMethods = [
    saveConfig: "POST", updateConfig: "PUT", deleteConfig: "DELETE",
    saveShard: "POST", updateShard: "PUT", deleteShard: "DELETE"]

  def index(Integer max) {
    redirect action: 'indexConfig'
  }

  //////////////////////////////////////////////////////////////////////////////
  // ConfigHost
  //////////////////////////////////////////////////////////////////////////////

  def indexConfig(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    List results = new ConfigHost().findByType();
    int count = results != null ? results.size() : 0;
    render view: 'indexConfig', model:[configHostInstanceList: results, configHostInstanceCount: count]
  }

  def showConfig(ConfigHost configHostInstance) {
    if(params.id) {
      configHostInstance.setId(params.id);
    }

    configHostInstance.load();
    configHostInstance.fromJSON();
    respond configHostInstance
  }

  def createConfig() {
    def config = new ConfigHost(params);
    List results = null;

    render view: 'createConfig', model:[configHostInstance: config, versionRDSInstanceList: results]
  }

  def saveConfig(ConfigHost configHostInstance) {
    if (configHostInstance == null) {
      notFoundConfig()
      return
    }

    if (configHostInstance.hasErrors()) {
      respond configHostInstance.errors, view:'createConfig'
      return
    }

    boolean bcreated = configHostInstance.create()
    if(!bcreated) {
      flash.message = "Failed to create: ${configHostInstance.getId()}"
      render view: 'createApp', model:[configHostInstance: configHostInstance]
      return
    }

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'adminConfig.label', default: 'ConfigHost'), configHostInstance.getId()])
        redirect action: 'showConfig', id: configHostInstance.getId()
      }
      '*' { respond configHostInstance, [status: CREATED] }
    }
  }

  def editConfig(ConfigHost configHostInstance) {
    if(params.id) {
      configHostInstance.setId(params.id);
    } else {
      response.sendError(404);
      return;
    }

    configHostInstance.load();
    configHostInstance.fromJSON();

    List results = null;

    render view: 'editConfig', model:[configHostInstance: configHostInstance, versionRDSInstanceList: results]
  }

  def updateConfig(ConfigHost configHostInstance) {
    if (configHostInstance == null) {
      notFoundConfig()
      return
    }

    if (configHostInstance.hasErrors()) {
      respond configHostInstance.errors, view:'editConfig'
      return
    }

    configHostInstance.save()

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ConfigHost.label', default: 'ConfigHost'), configHostInstance.getId()])
        redirect action: 'showConfig', id: configHostInstance.getId()
      }
      '*'{ respond configHostInstance, [status: OK] }
    }
  }

  def deleteConfig(ConfigHost configHostInstance) {
    if (configHostInstance == null) {
      notFoundConfig()
      return
    }

    if(params.id) {
      configHostInstance.setId(params.id);
    }

    configHostInstance.delete()

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'ConfigHost.label', default: 'ConfigHost'), configHostInstance.getId()])
        redirect action:"indexConfig", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFoundConfig() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminConfig.label', default: 'ConfigHost'), params.id])
        redirect action: "indexConfig", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // ConfigHost Shard
  //////////////////////////////////////////////////////////////////////////////

  def showShard(ShardHost shardHostInstance) {
    ConfigHost configHostInstance = new ConfigHost();
    if(params.id) {
      shardHostInstance.setId(params.id);
      configHostInstance.setId(params.id);
    } else {
      response.sendError(404);
      return;
    }

    configHostInstance.load();
    shardHostInstance = configHostInstance.findShard(shardHostInstance.shard)

    respond shardHostInstance
  }

  def createShard() {
    log.info params

    def shardHostInstance = new ShardHost();
    shardHostInstance.initParameters(params);

    if(params.id) {
      shardHostInstance.setId(params.id);
    }

    List configHostInstanceList = new ConfigHost().findByType();

    render view: 'createShard', model:[shardHostInstance: shardHostInstance, configHostInstanceList: configHostInstanceList]
  }

  def saveShard(ShardHost shardHostInstance) {
    if (shardHostInstance == null) {
      notFoundShard()
      return
    }

    if (shardHostInstance.hasErrors()) {
      respond shardHostInstance.errors, view:'createShard'
      return
    }

    saveConfigShard(shardHostInstance.getId(), shardHostInstance.shard, shardHostInstance);

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'adminConfig.label', default: 'ShardHost'), shardHostInstance.getId()])
        redirect action: 'showShard', id: shardHostInstance.getId()
      }
      '*' { respond shardHostInstance, [status: CREATED] }
    }
  }

  def editShard(ShardHost shardHostInstance) {
    ConfigHost configHostInstance = new ConfigHost();
    if(params.id) {
      shardHostInstance.setId(params.id);
      configHostInstance.setId(params.id);
    } else {
      response.sendError(404);
      return;
    }

    configHostInstance.load();
    shardHostInstance = configHostInstance.findShard(shardHostInstance.shard)

    List configHostInstanceList = new ConfigHost().findByType();
    render view: 'editShard', model:[shardHostInstance: shardHostInstance, configHostInstanceList: configHostInstanceList]
  }

  def updateShard(ShardHost shardHostInstance) {
    if (shardHostInstance == null) {
      notFoundShard()
      return
    }

    if (shardHostInstance.hasErrors()) {
      respond shardHostInstance.errors, view:'editShard'
      return
    }

    saveConfigShard(shardHostInstance.getId(), shardHostInstance.shard, shardHostInstance);

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ShardHost.label', default: 'ShardHost'), shardHostInstance.getId()])
        redirect action: 'showShard', id: shardHostInstance.getId()
      }
      '*'{ respond shardHostInstance, [status: OK] }
    }
  }

  def deleteShard(ShardHost shardHostInstance) {
    if (shardHostInstance == null) {
      notFoundShard()
      return
    }

    if(params.id) {
      shardHostInstance.setId(params.id);
    }

    saveConfigShard(shardHostInstance.getId(), shardHostInstance.shard, null);

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'ShardHost.label', default: 'ShardHost'), shardHostInstance.getId()])
        redirect action:"showConfig", method:"GET", id: shardHostInstance.getId()
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void saveConfigShard(String id, Integer shard, ShardHost shardHostInstance) {
    if(id) {
      ConfigHost configHostInstance = new ConfigHost();
      configHostInstance.setId(id);
      configHostInstance.load();
      configHostInstance.replaceShard(shard, shardHostInstance);
      configHostInstance.save();
    }
  }

  protected void notFoundShard() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminConfig.label', default: 'ShardHost'), params.id])
        redirect action: "showConfig", method: "GET", id: params.id
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
