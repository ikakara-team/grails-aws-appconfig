<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityNameC" value="${message(code: 'platformConfigRDS.ConfigRDS.label', default: 'ConfigRDS')}" />
    <g:set var="entityNameS" value="${message(code: 'platformConfigRDS.ShardRDS.label', default: 'ShardRDS')}" />
    <title><g:message code="default.show.label" args="[entityNameS]" /></title>
  </head>
  <body>
    <a href="#show-platformConfigRDS" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="navmenu" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="indexConfig"><g:message code="default.list.label" args="[entityNameC]" /></g:link></li>
        <li><g:link class="create" action="createShard"><g:message code="default.new.label" args="[entityNameS]" /></g:link></li>
        </ul>
      </div>
      <div id="show-platformConfigRDS" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityNameS]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list platformConfigRDS">
        <g:if test="${shardRDSInstance?.id_name}">
          <li class="fieldcontain" style="background-color: #FFAAAA;">
            <span id="name-label" class="property-label"><g:message code="platformConfigRDS.name.label" default="Name" /></span>
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${shardRDSInstance}" field="id_name"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.id_version}">
          <li class="fieldcontain" style="background-color: #FFAAAA;">
            <span id="version-label" class="property-label"><g:message code="platformConfigRDS.version.label" default="Version" /></span>
            <span class="property-value" aria-labelledby="version-label"><g:fieldValue bean="${shardRDSInstance}" field="id_version"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.shard || shardRDSInstance?.shard == 0}">
          <li class="fieldcontain">
            <span id="shard-label" class="property-label"><g:message code="platformConfigRDS.shard.label" default="Shard" /></span>
            <span class="property-value" aria-labelledby="shard-label"><g:fieldValue bean="${shardRDSInstance}" field="shard"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.driverClassName}">
          <li class="fieldcontain">
            <span id="driverClassName-label" class="property-label"><g:message code="platformConfigRDS.driverClassName.label" default="Driver Name" /></span>
            <span class="property-value" aria-labelledby="driverClassName-label"><g:fieldValue bean="${shardRDSInstance}" field="driverClassName"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.url}">
          <li class="fieldcontain">
            <span id="url-label" class="property-label"><g:message code="platformConfigRDS.url.label" default="Url" /></span>
            <span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${shardRDSInstance}" field="url"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.db}">
          <li class="fieldcontain">
            <span id="db-label" class="property-label"><g:message code="platformConfigRDS.db.label" default="Db" /></span>
            <span class="property-value" aria-labelledby="db-label"><g:fieldValue bean="${shardRDSInstance}" field="db"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.username}">
          <li class="fieldcontain">
            <span id="username-label" class="property-label"><g:message code="platformConfigRDS.username.label" default="Username" /></span>
            <span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${shardRDSInstance}" field="username"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.password}">
          <li class="fieldcontain">
            <span id="password-label" class="property-label"><g:message code="platformConfigRDS.password.label" default="Password" /></span>
            <span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${shardRDSInstance}" field="password"/></span>
          </li>
        </g:if>
        <g:if test="${shardRDSInstance?.options}">
          <li class="fieldcontain">
            <span id="options-label" class="property-label"><g:message code="platformConfigRDS.options.label" default="Options" /></span>
            <span class="property-value" aria-labelledby="options-label"><g:fieldValue bean="${shardRDSInstance}" field="options"/></span>
          </li>
        </g:if>
      </ol>
      <g:form action="deleteShard" id="${shardRDSInstance?.id}"  method="DELETE">
        <fieldset class="buttons">
          <g:link class="edit" action="editShard" id="${shardRDSInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="deleteShard" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
