import ikakara.appconfig.dao.shard.ShardRDS
import ikakara.appconfig.dao.shard.ShardHost
import ikakara.appconfig.dao.dynamo.ConfigRDS
import ikakara.appconfig.dao.dynamo.ConfigHost

log4j = {
  error 'org.codehaus.groovy.grails',
        'org.springframework'
}

// for testing
grails.validateable.classes = [ShardRDS, ShardHost, ConfigRDS, ConfigHost]
