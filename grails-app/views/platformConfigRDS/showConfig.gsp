<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityName" value="${message(code: 'platformConfigRDS.label', default: 'ConfigRDS')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-platformConfigRDS" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="navmenu" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="indexConfig"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="createConfig"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        </ul>
      </div>
      <div id="show-platformConfigRDS" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list platformConfigRDS">
        <g:if test="${configRDSInstance?.name}">
          <li class="fieldcontain">
            <span id="name-label" class="property-label"><g:message code="platformConfigRDS.name.label" default="Name" /></span>
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${configRDSInstance}" field="name"/></span>
          </li>
        </g:if>
        <g:if test="${configRDSInstance?.version}">
          <li class="fieldcontain">
            <span id="version-label" class="property-label"><g:message code="platformConfigRDS.version.label" default="Version" /></span>
            <span class="property-value" aria-labelledby="version-label"><g:fieldValue bean="${configRDSInstance}" field="version"/></span>
          </li>
        </g:if>
        <g:if test="${configRDSInstance?.versionStatus}">
          <li class="fieldcontain">
            <span id="versionStatus-label" class="property-label"><g:message code="platformConfigRDS.versionStatus.label" default="Version Status" /></span>
            <span class="property-value" aria-labelledby="versionStatus-label"><g:fieldValue bean="${configRDSInstance}" field="versionStatus"/></span>
          </li>
        </g:if>
        <g:if test="${configRDSInstance?.versionNote}">
          <li class="fieldcontain">
            <span id="versionNote-label" class="property-label"><g:message code="platformConfigRDS.versionNote.label" default="Version Note" /></span>
            <span class="property-value" aria-labelledby="versionNote-label"><g:fieldValue bean="${configRDSInstance}" field="versionNote"/></span>
          </li>
        </g:if>
        <g:if test="${configRDSInstance?.shardCount}">
          <li class="fieldcontain">
            <span id="shardCount-label" class="property-label"><g:message code="platformConfigRDS.shardCount.label" default="Shard Count" /></span>
            <span class="property-value" aria-labelledby="shardCount-label"><g:fieldValue bean="${configRDSInstance}" field="shardCount"/></span>
          </li>
        </g:if>
<%--
        <g:if test="${configRDSInstance?.shardMapStr}">
          <li class="fieldcontain">
            <span id="shardMapStr-label" class="property-label"><g:message code="platformConfigRDS.shardMapStr.label" default="Shard Map" /></span>
            <span class="property-value" aria-labelledby="shardMapStr-label"><g:fieldValue bean="${configRDSInstance}" field="shardMapStr"/></span>
          </li>
        </g:if>
--%>
        <g:if test="${configRDSInstance?.shardMap}">
          <li class="fieldcontain">
            <span id="shardMap-label" class="property-label"><g:message code="platformConfigRDS.shardMap.label" default="Shards" /></span>
            <g:each in="${configRDSInstance.shardMap}" var="s">
              <span class="property-value" aria-labelledby="shardMap-label"><g:link action="showShard" id="${configRDSInstance.id}_${s.key}">${s?.value.prettyPrint().encodeAsHTML()}</g:link></span>
            </g:each>
          </li>
        </g:if>
      </ol>
      <g:form action="deleteConfig" id="${configRDSInstance?.id}" method="DELETE">
        <fieldset class="buttons">
          <g:link class="edit" action="editConfig" id="${configRDSInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="deleteConfig" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
