<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityNameH" value="${message(code: 'ConfigHost.label', default: 'ConfigHost')}" />
    <g:set var="entityNameC" value="${message(code: 'VersionRDS.label', default: 'ConfigRDS')}" />
    <title><g:message code="default.list.label" args="[entityNameH]" /></title>
  </head>
  <body>
    <a href="#list-platformConfigRDS" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="navmenu" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <!--li><g:link class="list" controller="platformConfigRDS" action="indexConfig"><g:message code="default.list.label" args="[entityNameC]" /></g:link></li-->
        <li><g:link class="create" action="createConfig"><g:message code="default.new.label" args="[entityNameH]" /></g:link></li>
        </ul>
      </div>
      <div id="list-adminConfigHost" class="content scaffold-list" role="main">
        <h1><g:message code="default.list.label" args="[entityNameH]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
        <thead>
          <tr>
            <g:sortableColumn property="name" title="${message(code: 'adminConfigHost.name.label', default: 'Class<br/>Name')}" />
            <g:sortableColumn property="version" title="${message(code: 'adminConfigHost.version.label', default: 'Version')}" />
              <g:sortableColumn property="versionStatus" title="${message(code: 'adminConfigHost.versionStatus.label', default: 'Version<br/>Status')}" />
              <g:sortableColumn property="versionNote" title="${message(code: 'adminConfigHost.versionNote.label', default: 'Version<br/>Note')}" />
                <g:sortableColumn property="shardCount" title="${message(code: 'adminConfigHost.shardCount.label', default: 'Shard<br/>Count')}" />
                  <g:sortableColumn property="shardMap" title="${message(code: 'adminConfigHost.shardMap.label', default: 'Shards')}" />
                  </tr>
                </thead>
                <tbody>
                  <g:each in="${configHostInstanceList}" status="i" var="configHostInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                      <td>${fieldValue(bean: configHostInstance, field: "name")}</td>
                      <td><g:link action="showConfig" id="${configHostInstance.id}">${fieldValue(bean: configHostInstance, field: "version")}</g:link></td>
                      <td>${fieldValue(bean: configHostInstance, field: "versionStatus")}</td>
                      <td>${fieldValue(bean: configHostInstance, field: "versionNote")}</td>
                      <td>${fieldValue(bean: configHostInstance, field: "shardCount")}</td>
                      <td>${fieldValue(bean: configHostInstance, field: "shardMapStr")}</td>
                    </tr>
                  </g:each>
                </tbody>
              </table>
              <div class="pagination">
                <g:paginate total="${configHostInstanceCount ?: 0}" />
              </div>
            </div>
          </body>
        </html>
