<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityNameS" value="${message(code: 'adminConfigHost.ShardHost.label', default: 'ShardHost')}" />
    <g:set var="entityNameH" value="${message(code: 'adminConfigHost.ConfigHost.label', default: 'ConfigHost')}" />
    <title><g:message code="default.edit.label" args="[entityNameS]" /></title>
  </head>
  <body>
    <a href="#edit-adminConfigHost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="navmenu" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="indexConfig"><g:message code="default.list.label" args="[entityNameH]" /></g:link></li>
        <li><g:link class="create" action="createShard"><g:message code="default.new.label" args="[entityNameS]" /></g:link></li>
        </ul>
      </div>
      <div id="edit-adminConfigHost" class="content scaffold-edit" role="main">
        <h1><g:message code="default.edit.label" args="[entityNameS]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${shardHostInstance}">
        <ul class="errors" role="alert">
          <g:eachError bean="${shardHostInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
      </g:hasErrors>
      <g:form url="[action:'updateShard']" method="PUT" >
        <g:hiddenField name="id" value="${shardHostInstance?.id}" />
        <fieldset class="formShard">
          <g:render template="formShard"/>
        </fieldset>
        <fieldset class="buttons">
          <g:actionSubmit class="save" action="updateShard" value="${message(code: 'default.button.update.label', default: 'Update')}" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
