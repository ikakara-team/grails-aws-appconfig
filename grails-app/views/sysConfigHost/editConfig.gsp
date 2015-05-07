<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="platform">
    <g:set var="entityName" value="${message(code: 'sysConfigHost.label', default: 'ConfigHost')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#edit-sysConfigHost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
        <li><a class="home" href="${request.contextPath}${grailsApplication.mergedConfig.grails.plugin.ikakara.appconfig.homePath}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="indexConfig"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="createConfig"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        </ul>
      </div>
      <div id="edit-sysConfigHost" class="content scaffold-edit" role="main">
        <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${configHostInstance}">
        <ul class="errors" role="alert">
          <g:eachError bean="${configHostInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
      </g:hasErrors>
      <g:form url="[action:'updateConfig']" method="PUT" >
        <g:hiddenField name="id" value="${configHostInstance?.id}" />
        <g:hiddenField name="shardMapStr" value="${configHostInstance?.shardMapStr}" />
        <fieldset class="formConfig">
          <g:render template="formConfig"/>
        </fieldset>
        <fieldset class="buttons">
          <g:actionSubmit class="save" action="updateConfig" value="${message(code: 'default.button.update.label', default: 'Update')}" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
