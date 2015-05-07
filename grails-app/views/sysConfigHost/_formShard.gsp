<%@ page import="ikakara.appconfig.dao.shard.ShardHost" %>

<div class="fieldcontain ${hasErrors(bean: shardHostInstance, field: 'version', 'error')} required">
  <label for="version">
    <g:message code="configHost.id.label" default="Version Class" />
    <span class="required-indicator">*</span>
  </label>
  <g:select name="id" from="${configHostInstanceList}" optionKey="id" optionValue="id" value="${shardHostInstance?.getNameVersionId()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardHostInstance, field: 'shard', 'error')} required">
  <label for="shard">
    <g:message code="sysConfigHost.shard.label" default="Shard" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="shard" type="number" min="0" max="99" value="${shardHostInstance.shard}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardHostInstance, field: 'host', 'error')} required">
  <label for="host">
    <g:message code="sysConfigHost.host.label" default="Host" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField type="url" name="host" required="" value="${shardHostInstance?.host}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardHostInstance, field: 'port', 'error')} required">
  <label for="port">
    <g:message code="sysConfigHost.port.label" default="Port" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="port" required="" value="${shardHostInstance?.port}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardHostInstance, field: 'options', 'error')}">
  <label for="options">
    <g:message code="sysConfigHost.options.label" default="Options" />
    <!--span class="required-indicator">*</span-->
  </label>
  <g:textField name="options" required="" value="${shardHostInstance?.options}"/>
</div>
