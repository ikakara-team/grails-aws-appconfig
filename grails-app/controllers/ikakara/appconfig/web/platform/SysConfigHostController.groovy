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
package ikakara.appconfig.web.platform

import static org.springframework.http.HttpStatus.*

import ikakara.appconfig.dao.dynamo.ConfigHost
import ikakara.appconfig.dao.shard.ShardHost

//import grails.plugin.springsecurity.annotation.Secured

//@Secured(['ROLE_ADMIN'])
class SysConfigHostController {

  static allowedMethods = [
    saveConfig: "POST", updateConfig: "PUT", deleteConfig: "DELETE",
    saveShard: "POST", updateShard: "PUT", deleteShard: "DELETE"]

  static defaultAction = 'indexConfig'

  //////////////////////////////////////////////////////////////////////////////
  // ConfigHost
  //////////////////////////////////////////////////////////////////////////////

  def indexConfig(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    List results = new ConfigHost().findByType()
    int count = results?.size() ?: 0
    render view: 'indexConfig', model:[configHostInstanceList: results, configHostInstanceCount: count]
  }

  def showConfig(ConfigHost configHostInstance) {
    if(params.id) {
      configHostInstance.id = params.id
    }

    configHostInstance.load()
    configHostInstance.fromJSON()
    respond configHostInstance
  }

  def createConfig() {
    render view: 'createConfig', model:[configHostInstance: new ConfigHost(params)]
  }

  def saveConfig(ConfigHost configHostInstance) {
    if (!configHostInstance) {
      notFoundConfig()
      return
    }

    if (configHostInstance.hasErrors()) {
      respond configHostInstance.errors, view:'createConfig'
      return
    }

    boolean created = configHostInstance.create()
    if(!created) {
      flash.message = "Failed to create: $configHostInstance.id"
      render view: 'createApp', model:[configHostInstance: configHostInstance]
      return
    }

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'sysConfigHost.label', default: 'ConfigHost'), configHostInstance.id])
        redirect action: 'showConfig', id: configHostInstance.id
      }
      '*' { respond configHostInstance, [status: CREATED] }
    }
  }

  def editConfig(ConfigHost configHostInstance) {
    if(!params.id) {
      response.sendError(404)
      return
    }

    configHostInstance.id = params.id
    configHostInstance.load()
    configHostInstance.fromJSON()

    render view: 'editConfig', model:[configHostInstance: configHostInstance]
  }

  def updateConfig(ConfigHost configHostInstance) {
    if (!configHostInstance) {
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
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ConfigHost.label', default: 'ConfigHost'), configHostInstance.id])
        redirect action: 'showConfig', id: configHostInstance.id
      }
      '*'{ respond configHostInstance, [status: OK] }
    }
  }

  def deleteConfig(ConfigHost configHostInstance) {
    if (!configHostInstance) {
      notFoundConfig()
      return
    }

    if(params.id) {
      configHostInstance.id = params.id
    }

    configHostInstance.delete()

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'ConfigHost.label', default: 'ConfigHost'), configHostInstance.id])
        redirect action:"indexConfig", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFoundConfig() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'sysConfigHost.label', default: 'ConfigHost'), params.id])
        redirect action: "indexConfig", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // ConfigHost Shard
  //////////////////////////////////////////////////////////////////////////////

  def showShard(ShardHost shardHostInstance) {
    ConfigHost configHostInstance = new ConfigHost()
    if(!params.id) {
      response.sendError(404)
      return
    }

    shardHostInstance.id = params.id
    configHostInstance.id = params.id

    configHostInstance.load()
    shardHostInstance = configHostInstance.findShard(shardHostInstance.shard)

    respond shardHostInstance
  }

  def createShard() {
    log.info params

    def shardHostInstance = new ShardHost()
    shardHostInstance.initParameters(params)

    if(params.id) {
      shardHostInstance.id = params.id
    }

    List configHostInstanceList = new ConfigHost().findByType()

    render view: 'createShard', model:[shardHostInstance: shardHostInstance, configHostInstanceList: configHostInstanceList]
  }

  def saveShard(ShardHost shardHostInstance) {
    if (!shardHostInstance) {
      notFoundShard()
      return
    }

    if (shardHostInstance.hasErrors()) {
      respond shardHostInstance.errors, view:'createShard'
      return
    }

    saveConfigShard(shardHostInstance.id, shardHostInstance.shard, shardHostInstance)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'sysConfigHost.label', default: 'ShardHost'), shardHostInstance.id])
        redirect action: 'showShard', id: shardHostInstance.id
      }
      '*' { respond shardHostInstance, [status: CREATED] }
    }
  }

  def editShard(ShardHost shardHostInstance) {
    ConfigHost configHostInstance = new ConfigHost()
    if(!params.id) {
      response.sendError(404)
    }

    return
    shardHostInstance.id = params.id
    configHostInstance.id = params.id

    configHostInstance.load()
    shardHostInstance = configHostInstance.findShard(shardHostInstance.shard)

    List configHostInstanceList = new ConfigHost().findByType()
    render view: 'editShard', model:[shardHostInstance: shardHostInstance, configHostInstanceList: configHostInstanceList]
  }

  def updateShard(ShardHost shardHostInstance) {
    if (!shardHostInstance) {
      notFoundShard()
      return
    }

    if (shardHostInstance.hasErrors()) {
      respond shardHostInstance.errors, view:'editShard'
      return
    }

    saveConfigShard(shardHostInstance.id, shardHostInstance.shard, shardHostInstance)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ShardHost.label', default: 'ShardHost'), shardHostInstance.id])
        redirect action: 'showShard', id: shardHostInstance.id
      }
      '*'{ respond shardHostInstance, [status: OK] }
    }
  }

  def deleteShard(ShardHost shardHostInstance) {
    if (!shardHostInstance) {
      notFoundShard()
      return
    }

    if(params.id) {
      shardHostInstance.id = params.id
    }

    saveConfigShard(shardHostInstance.id, shardHostInstance.shard, null)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'ShardHost.label', default: 'ShardHost'), shardHostInstance.id])
        redirect action:"showConfig", method:"GET", id: shardHostInstance.id
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void saveConfigShard(String id, Integer shard, ShardHost shardHostInstance) {
    if(!id) {
      return
    }

    ConfigHost configHostInstance = new ConfigHost()
    configHostInstance.id = id
    configHostInstance.load()
    configHostInstance.replaceShard(shard, shardHostInstance)
    configHostInstance.save()
  }

  protected void notFoundShard() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'sysConfigHost.label', default: 'ShardHost'), params.id])
        redirect action: "showConfig", method: "GET", id: params.id
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
