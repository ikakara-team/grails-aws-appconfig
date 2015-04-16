<div class="fieldcontain ${hasErrors(bean: configHostInstance, field: 'name', 'error')} required">
  <label for="name">
    <g:message code="configHost.name.label" default="Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="name" required="" value="${configHostInstance?.name}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: configHostInstance, field: 'version', 'error')} required">
  <label for="version">
    <g:message code="versionRDS.version.label" default="Version" />
    <span class="required-indicator">*</span>
  </label>
  <g:datePicker name="versionDate" precision="day"  value="${configHostInstance?.versionDate}"  />
</div>
<div class="fieldcontain ${hasErrors(bean: configHostInstance, field: 'versionStatus', 'error')} required">
  <label for="versionStatus">
    <g:message code="versionRDS.versionStatus.label" default="Version Status" />
    <span class="required-indicator">*</span>
  </label>
  <g:select name="versionStatus" from="${['UNKNOWN', 'ACTIVE', 'INACTIVE']}" value="${configHostInstance?.versionStatus}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: configHostInstance, field: 'versionNote', 'error')} required">
  <label for="versionNote">
    <g:message code="versionRDS.versionNote.label" default="Version Note" />
    <span class="required-indicator">*</span>
  </label>
  <g:textArea name="versionNote" value="${configHostInstance.versionNote}" style="width:250px;height:50px" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: configHostInstance, field: 'shardCount', 'error')} required">
  <label for="shardCount">
    <g:message code="configHost.shardCount.label" default="Shard Count" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="shardCount" type="number" min="1" max="100" value="${configHostInstance.shardCount}" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: configHostInstance, field: 'shardMap', 'error')} ">
  <label for="shardMap">
    <g:message code="configHost.shardMap.label" default="Shards" />
  </label>
  <ul class="one-to-many">
    <g:each in="${configHostInstance?.shardMap?}" var="s">
      <li><g:link action="showShard" id="${configHostInstance?.id}_${s.key}">${s?.value.prettyPrint().encodeAsHTML()}</g:link></li>
      </g:each>
    <li class="add">
      <g:link action="createShard" params="['id': configHostInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'adminConfigHost.label', default: 'Shard')])}</g:link>
      </li>
    </ul>
  </div>

