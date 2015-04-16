<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityNameH" value="${message(code: 'adminConfigHost.ConfigHost.label', default: 'ConfigHost')}" />
    <g:set var="entityNameS" value="${message(code: 'adminConfigHost.ShardHost.label', default: 'ShardHost')}" />
    <title><g:message code="default.show.label" args="[entityNameS]" /></title>
  </head>
  <body>
    <a href="#show-adminConfigHost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="navmenu" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="indexConfig"><g:message code="default.list.label" args="[entityNameH]" /></g:link></li>
        <li><g:link class="create" action="createShard"><g:message code="default.new.label" args="[entityNameS]" /></g:link></li>
        </ul>
      </div>
      <div id="show-adminConfigHost" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityNameS]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list adminConfigHost">
        <g:if test="${shardHostInstance?.id_name}">
          <li class="fieldcontain" style="background-color: #FFAAAA;">
            <span id="name-label" class="property-label"><g:message code="adminConfigHost.name.label" default="Name" /></span>
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${shardHostInstance}" field="id_name"/></span>
          </li>
        </g:if>
        <g:if test="${shardHostInstance?.id_version}">
          <li class="fieldcontain" style="background-color: #FFAAAA;">
            <span id="version-label" class="property-label"><g:message code="adminConfigHost.version.label" default="Version" /></span>
            <span class="property-value" aria-labelledby="version-label"><g:fieldValue bean="${shardHostInstance}" field="id_version"/></span>
          </li>
        </g:if>
        <g:if test="${shardHostInstance?.shard || shardHostInstance?.shard == 0}">
          <li class="fieldcontain">
            <span id="shard-label" class="property-label"><g:message code="adminConfigHost.shard.label" default="Shard" /></span>
            <span class="property-value" aria-labelledby="shard-label"><g:fieldValue bean="${shardHostInstance}" field="shard"/></span>
          </li>
        </g:if>

        <g:if test="${shardHostInstance?.host}">
          <li class="fieldcontain">
            <span id="host-label" class="property-label"><g:message code="adminConfigHost.url.label" default="Host" /></span>
            <span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${shardHostInstance}" field="host"/></span>
          </li>
        </g:if>
        <g:if test="${shardHostInstance?.port}">
          <li class="fieldcontain">
            <span id="port-label" class="property-label"><g:message code="adminConfigHost.port.label" default="Port" /></span>
            <span class="property-value" aria-labelledby="port-label"><g:fieldValue bean="${shardHostInstance}" field="port"/></span>
          </li>
        </g:if>
        <g:if test="${shardHostInstance?.options}">
          <li class="fieldcontain">
            <span id="options-label" class="property-label"><g:message code="adminConfigHost.options.label" default="Options" /></span>
            <span class="property-value" aria-labelledby="options-label"><g:fieldValue bean="${shardHostInstance}" field="options"/></span>
          </li>
        </g:if>
      </ol>
      <g:form action="deleteShard" id="${shardHostInstance?.id}"  method="DELETE">
        <fieldset class="buttons">
          <g:link class="edit" action="editShard" id="${shardHostInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="deleteShard" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
