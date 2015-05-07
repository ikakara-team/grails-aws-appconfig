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

import ikakara.appconfig.dao.dynamo.ConfigRDS
import ikakara.appconfig.dao.shard.ShardRDS

//import grails.plugin.springsecurity.annotation.Secured

//@Secured(['ROLE_ADMIN'])
class SysConfigRDSController {

  static allowedMethods = [
    saveConfig: "POST", updateConfig: "PUT", deleteConfig: "DELETE",
    saveShard: "POST", updateShard: "PUT", deleteShard: "DELETE"]

  static defaultAction = 'indexConfig'

  //////////////////////////////////////////////////////////////////////////////
  // ConfigRDS
  //////////////////////////////////////////////////////////////////////////////

  def indexConfig(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    List results = new ConfigRDS().findByType()
    int count = results?.size() ?: 0
    //render view: 'indexConfig', model:[configRDSInstanceList: results, configRDSInstanceCount: count]
    respond results, model:[configRDSInstanceCount: count]
  }

  def showConfig(ConfigRDS configRDSInstance) {
    if(params.id) {
      configRDSInstance.id = params.id
    }

    configRDSInstance.load()
    configRDSInstance.fromJSON()
    respond configRDSInstance
  }

  def createConfig() {
    render view: 'createConfig', model:[configRDSInstance: new ConfigRDS(params)]
  }

  def saveConfig(ConfigRDS configRDSInstance) {
    if (!configRDSInstance) {
      notFoundConfig()
      return
    }

    if (configRDSInstance.hasErrors()) {
      respond configRDSInstance.errors, view:'createConfig'
      return
    }

    boolean created = configRDSInstance.create()
    if(!created) {
      flash.message = "Failed to create: $configRDSInstance.id"
      render view: 'createApp', model:[configRDSInstance: configRDSInstance]
      return
    }

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'sysConfigRDS.label', default: 'ConfigRDS'), configRDSInstance.id])
        redirect action: 'showConfig', id: configRDSInstance.id
      }
      '*' { respond configRDSInstance, [status: CREATED] }
    }
  }

  def editConfig(ConfigRDS configRDSInstance) {
    if(!params.id) {
      response.sendError(404)
      return
    }

    configRDSInstance.id = params.id
    configRDSInstance.load()
    configRDSInstance.fromJSON()

    render view: 'editConfig', model:[configRDSInstance: configRDSInstance]
  }

  def updateConfig(ConfigRDS configRDSInstance) {
    if (!configRDSInstance) {
      notFoundConfig()
      return
    }

    if (configRDSInstance.hasErrors()) {
      respond configRDSInstance.errors, view:'editConfig'
      return
    }

    configRDSInstance.save()

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ConfigRDS.label', default: 'ConfigRDS'), configRDSInstance.id])
        redirect action: 'showConfig', id: configRDSInstance.id
      }
      '*'{ respond configRDSInstance, [status: OK] }
    }
  }

  def deleteConfig(ConfigRDS configRDSInstance) {
    if (!configRDSInstance) {
      notFoundConfig()
      return
    }

    if(params.id) {
      configRDSInstance.id = params.id
    }

    configRDSInstance.delete()

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'ConfigRDS.label', default: 'ConfigRDS'), configRDSInstance.id])
        redirect action:"indexConfig", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFoundConfig() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'sysConfigRDS.label', default: 'ConfigRDS'), params.id])
        redirect action: "indexConfig", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // ConfigRDS Shard
  //////////////////////////////////////////////////////////////////////////////

  def showShard(ShardRDS shardRDSInstance) {
    if(!params.id) {
      response.sendError(404)
      return
    }

    ConfigRDS configRDSInstance = new ConfigRDS()
    shardRDSInstance.id = params.id
    configRDSInstance.id = params.id

    configRDSInstance.load()
    shardRDSInstance = configRDSInstance.findShard(shardRDSInstance.shard)

    respond shardRDSInstance
  }

  def createShard() {
    log.info params

    def shardRDSInstance = new ShardRDS()
    shardRDSInstance.initParameters(params)

    if(params.id) {
      shardRDSInstance.id = params.id
    }

    List configRDSInstanceList = new ConfigRDS().findByType()

    render view: 'createShard', model:[shardRDSInstance: shardRDSInstance, configRDSInstanceList: configRDSInstanceList]
  }

  def saveShard(ShardRDS shardRDSInstance) {
    if (!shardRDSInstance) {
      notFoundShard()
      return
    }

    if (shardRDSInstance.hasErrors()) {
      respond shardRDSInstance.errors, view:'createShard'
      return
    }

    saveConfigShard(shardRDSInstance.id, shardRDSInstance.shard, shardRDSInstance)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'sysConfigRDS.label', default: 'ShardRDS'), shardRDSInstance.id])
        redirect action: 'showShard', id: shardRDSInstance.id
      }
      '*' { respond shardRDSInstance, [status: CREATED] }
    }
  }

  def editShard(ShardRDS shardRDSInstance) {
    if(!params.id) {
      response.sendError(404)
      return
    }

    ConfigRDS configRDSInstance = new ConfigRDS()
    shardRDSInstance.id = params.id
    configRDSInstance.id = params.id

    configRDSInstance.load()
    shardRDSInstance = configRDSInstance.findShard(shardRDSInstance.shard)

    List configRDSInstanceList = new ConfigRDS().findByType()
    render view: 'editShard', model:[shardRDSInstance: shardRDSInstance, configRDSInstanceList: configRDSInstanceList]
  }

  def updateShard(ShardRDS shardRDSInstance) {
    if (!shardRDSInstance) {
      notFoundShard()
      return
    }

    if (shardRDSInstance.hasErrors()) {
      respond shardRDSInstance.errors, view:'editShard'
      return
    }

    saveConfigShard(shardRDSInstance.id, shardRDSInstance.shard, shardRDSInstance)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ShardRDS.label', default: 'ShardRDS'), shardRDSInstance.id])
        redirect action: 'showShard', id: shardRDSInstance.id
      }
      '*'{ respond shardRDSInstance, [status: OK] }
    }
  }

  def deleteShard(ShardRDS shardRDSInstance) {
    if (!shardRDSInstance) {
      notFoundShard()
      return
    }

    if(params.id) {
      shardRDSInstance.id = params.id
    }

    saveConfigShard(shardRDSInstance.id, shardRDSInstance.shard, null)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'ShardRDS.label', default: 'ShardRDS'), shardRDSInstance.id])
        redirect action:"showConfig", method:"GET", id: shardRDSInstance.id
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void saveConfigShard(String id, Integer shard, ShardRDS shardRDSInstance) {
    if(!id) {
      return
    }

    ConfigRDS configRDSInstance = new ConfigRDS()
    configRDSInstance.id = id
    configRDSInstance.load()
    configRDSInstance.replaceShard(shard, shardRDSInstance)
    configRDSInstance.save()
  }

  protected void notFoundShard() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'sysConfigRDS.label', default: 'ShardRDS'), params.id])
        redirect action: "showConfig", method: "GET", id: params.id
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
