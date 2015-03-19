<%@ page import="ikakara.appconfig.dao.shard.ShardRDS" %>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'version', 'error')} required">
  <label for="version">
    <g:message code="configRDS.id.label" default="Version Class" />
    <span class="required-indicator">*</span>
  </label>
  <g:select name="id" from="${configRDSInstanceList}" optionKey="id" optionValue="id" value="${shardRDSInstance?.getNameVersionId()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'shard', 'error')} required">
  <label for="shard">
    <g:message code="platformConfigRDS.shard.label" default="Shard" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="shard" type="number" min="0" max="99" value="${shardRDSInstance.shard}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'driverClassName', 'error')} required">
  <label for="driverClassName">
    <g:message code="platformConfigRDS.driverClassName.label" default="Driver Class Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="driverClassName" required="" value="${shardRDSInstance?.driverClassName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'url', 'error')} required">
  <label for="url">
    <g:message code="platformConfigRDS.url.label" default="Url" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField type="url" name="url" required="" value="${shardRDSInstance?.url}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'db', 'error')} required">
  <label for="db">
    <g:message code="platformConfigRDS.db.label" default="Db" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="db" required="" value="${shardRDSInstance?.db}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'username', 'error')} required">
  <label for="username">
    <g:message code="platformConfigRDS.username.label" default="Username" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="username" required="" value="${shardRDSInstance?.username}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'password', 'error')} required">
  <label for="password">
    <g:message code="platformConfigRDS.password.label" default="Password" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField type="password" name="password" required="" value="${shardRDSInstance?.password}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: shardRDSInstance, field: 'options', 'error')}">
  <label for="options">
    <g:message code="platformConfigRDS.options.label" default="Options" />
    <!--span class="required-indicator">*</span-->
  </label>
  <g:textField name="options" required="" value="${shardRDSInstance?.options}"/>
</div>

