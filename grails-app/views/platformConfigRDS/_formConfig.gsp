<%@ page import="ikakara.appconfig.dao.dynamo.ConfigRDS" %>
<div class="fieldcontain ${hasErrors(bean: configRDSInstance, field: 'className', 'error')} required">
  <label for="className">
    <g:message code="configRDS.className.label" default="Class Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="className" required="" value="${configRDSInstance?.className}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: configRDSInstance, field: 'version', 'error')} required">
  <label for="version">
    <g:message code="versionRDS.version.label" default="Version" />
    <span class="required-indicator">*</span>
  </label>
  <g:datePicker name="versionDate" precision="day"  value="${configRDSInstance?.versionDate}"  />
</div>
<div class="fieldcontain ${hasErrors(bean: configRDSInstance, field: 'versionStatus', 'error')} required">
  <label for="versionStatus">
    <g:message code="versionRDS.versionStatus.label" default="Version Status" />
    <span class="required-indicator">*</span>
  </label>
  <g:select name="versionStatus" from="${['UNKNOWN', 'ACTIVE', 'INACTIVE']}" value="${configRDSInstance?.versionStatus}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: configRDSInstance, field: 'versionNote', 'error')} required">
  <label for="versionNote">
    <g:message code="versionRDS.versionNote.label" default="Version Note" />
    <span class="required-indicator">*</span>
  </label>
  <g:textArea name="versionNote" value="${configRDSInstance.versionNote}" style="width:250px;height:50px" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: configRDSInstance, field: 'shardCount', 'error')} required">
  <label for="shardCount">
    <g:message code="configRDS.shardCount.label" default="Shard Count" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="shardCount" type="number" min="1" max="100" value="${configRDSInstance.shardCount}" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: configRDSInstance, field: 'shardMap', 'error')} ">
  <label for="shardMap">
    <g:message code="configRDS.shardMap.label" default="Shards" />
  </label>
  <ul class="one-to-many">
    <g:each in="${configRDSInstance?.shardMap?}" var="s">
      <li><g:link action="showShard" id="${configRDSInstance?.id}_${s.key}">${s?.value.prettyPrint().encodeAsHTML()}</g:link></li>
      </g:each>
    <li class="add">
      <g:link action="createShard" params="['id': configRDSInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'platformConfigRDS.label', default: 'Shard')])}</g:link>
      </li>
    </ul>
  </div>

