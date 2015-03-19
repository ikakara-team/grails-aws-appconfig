// configuration for plugin testing - will not be included in the plugin zip

log4j = {

  error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
         'org.codehaus.groovy.grails.web.pages', //  GSP
         'org.codehaus.groovy.grails.web.sitemesh', //  layouts
         'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
         'org.codehaus.groovy.grails.web.mapping', // URL mapping
         'org.codehaus.groovy.grails.commons', // core / classloading
         'org.codehaus.groovy.grails.plugins', // plugins
         'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
         'org.springframework',
         'org.hibernate',
         'net.sf.ehcache.hibernate'
}

// for testing
grails.validateable.classes = [
  ikakara.appconfig.dao.shard.ShardRDS,
  ikakara.appconfig.dao.shard.ShardHost,
  ikakara.appconfig.dao.dynamo.ConfigRDS,
  ikakara.appconfig.dao.dynamo.ConfigHost,
]
