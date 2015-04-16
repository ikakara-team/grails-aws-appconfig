<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityNameC" value="${message(code: 'ConfigRDS.label', default: 'ConfigRDS')}" />
    <g:set var="entityNameH" value="${message(code: 'ConfigHost.label', default: 'ConfigHost')}" />
    <title><g:message code="default.list.label" args="[entityNameC]" /></title>
  </head>
  <body>
    <a href="#list-platformConfigRDS" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="navmenu" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <!--li><g:link class="list" controller="platformConfigHost" action="indexConfig"><g:message code="default.list.label" args="[entityNameH]" /></g:link></li-->
        <li><g:link class="create" url="createConfig"><g:message code="default.new.label" args="[entityNameC]" /></g:link></li>
        </ul>
      </div>
      <div id="list-platformConfigRDS" class="content scaffold-list" role="main">
        <h1><g:message code="default.list.label" args="[entityNameC]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
        <thead>
          <tr>
            <g:sortableColumn property="className" title="${message(code: 'platformConfigRDS.className.label', default: 'Class<br/>Name')}" />
            <g:sortableColumn property="version" title="${message(code: 'platformConfigRDS.version.label', default: 'Version')}" />
              <g:sortableColumn property="versionStatus" title="${message(code: 'platformConfigRDS.versionStatus.label', default: 'Version<br/>Status')}" />
              <g:sortableColumn property="versionNote" title="${message(code: 'platformConfigRDS.versionNote.label', default: 'Version<br/>Note')}" />
                <g:sortableColumn property="shardCount" title="${message(code: 'platformConfigRDS.shardCount.label', default: 'Shard<br/>Count')}" />
                  <g:sortableColumn property="shardMap" title="${message(code: 'platformConfigRDS.shardMap.label', default: 'Shards')}" />
                  </tr>
                </thead>
                <tbody>
                  <g:each in="${configRDSInstanceList}" status="i" var="configRDSInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                      <td>${fieldValue(bean: configRDSInstance, field: "className")}</td>
                      <td><g:link action="showConfig" id="${configRDSInstance.id}">${fieldValue(bean: configRDSInstance, field: "version")}</g:link></td>
                      <td>${fieldValue(bean: configRDSInstance, field: "versionStatus")}</td>
                      <td>${fieldValue(bean: configRDSInstance, field: "versionNote")}</td>
                      <td>${fieldValue(bean: configRDSInstance, field: "shardCount")}</td>
                      <td>${fieldValue(bean: configRDSInstance, field: "shardMapStr")}</td>
                    </tr>
                  </g:each>
                </tbody>
              </table>
              <div class="pagination">
                <g:paginate total="${configRDSInstanceCount ?: 0}" />
              </div>
            </div>
          </body>
        </html>
