<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityName" value="${message(code: 'sysConfigHost.label', default: 'ConfigHost')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sysConfigHost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="indexConfig"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="createConfig"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        </ul>
      </div>
      <div id="show-sysConfigHost" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sysConfigHost">
        <g:if test="${configHostInstance?.name}">
          <li class="fieldcontain">
            <span id="name-label" class="property-label"><g:message code="sysConfigHost.name.label" default="Name" /></span>
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${configHostInstance}" field="name"/></span>
          </li>
        </g:if>
        <g:if test="${configHostInstance?.version}">
          <li class="fieldcontain">
            <span id="version-label" class="property-label"><g:message code="sysConfigHost.version.label" default="Version" /></span>
            <span class="property-value" aria-labelledby="version-label"><g:fieldValue bean="${configHostInstance}" field="version"/></span>
          </li>
        </g:if>
        <g:if test="${configHostInstance?.versionStatus}">
          <li class="fieldcontain">
            <span id="versionStatus-label" class="property-label"><g:message code="sysConfigHost.versionStatus.label" default="Version Status" /></span>
            <span class="property-value" aria-labelledby="versionStatus-label"><g:fieldValue bean="${configHostInstance}" field="versionStatus"/></span>
          </li>
        </g:if>
        <g:if test="${configHostInstance?.versionNote}">
          <li class="fieldcontain">
            <span id="versionNote-label" class="property-label"><g:message code="sysConfigHost.versionNote.label" default="Version Note" /></span>
            <span class="property-value" aria-labelledby="versionNote-label"><g:fieldValue bean="${configHostInstance}" field="versionNote"/></span>
          </li>
        </g:if>
        <g:if test="${configHostInstance?.shardCount}">
          <li class="fieldcontain">
            <span id="shardCount-label" class="property-label"><g:message code="sysConfigHost.shardCount.label" default="Shard Count" /></span>
            <span class="property-value" aria-labelledby="shardCount-label"><g:fieldValue bean="${configHostInstance}" field="shardCount"/></span>
          </li>
        </g:if>
<%--
        <g:if test="${configHostInstance?.shardMapStr}">
          <li class="fieldcontain">
            <span id="shardMapStr-label" class="property-label"><g:message code="sysConfigHost.shardMapStr.label" default="Shard Map" /></span>
            <span class="property-value" aria-labelledby="shardMapStr-label"><g:fieldValue bean="${configHostInstance}" field="shardMapStr"/></span>
          </li>
        </g:if>
--%>
        <g:if test="${configHostInstance?.shardMap}">
          <li class="fieldcontain">
            <span id="shardMap-label" class="property-label"><g:message code="sysConfigHost.shardMap.label" default="Shards" /></span>
            <g:each in="${configHostInstance.shardMap}" var="s">
              <span class="property-value" aria-labelledby="shardMap-label"><g:link action="showShard" id="${configHostInstance.id}_${s.key}">${s?.value.prettyPrint().encodeAsHTML()}</g:link></span>
            </g:each>
          </li>
        </g:if>
      </ol>
      <g:form action="deleteConfig" id="${configHostInstance?.id}" method="DELETE">
        <fieldset class="buttons">
          <g:link class="edit" action="editConfig" id="${configHostInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="deleteConfig" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
