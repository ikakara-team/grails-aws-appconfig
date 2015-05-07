class AwsAppconfigGrailsPlugin {
  def version = "0.5.1"
  def grailsVersion = "2.2 > *"
  def pluginExcludes = [
    "grails-app/i18n/*",          // needed to test plugin
    "grails-app/views/error.gsp", // needed to test plugin
    "grails-app/views/index.gsp", // needed to test plugin
    "web-app/**"
  ]
  List loadAfter = ['aws-instance']
  def title = "AWS App Config Plugin"
  def author = "Allen Arakaki"
  def description = 'Uses AWS DynamoDB to store (versioned) application configuration.'
  def documentation = "http://grails.org/plugin/aws-appconfig"
  def license = "APACHE"
  def issueManagement = [url: 'https://github.com/ikakara-team/grails-aws-appconfig/issues']
  def scm = [url: 'https://github.com/ikakara-team/grails-aws-appconfig']

  // merge config ...
  def doWithApplicationContext = { appCtx ->
    println 'Configuring AwsAppConfig config ...' + application.mergedConfig.conf.grails.plugin.awsappconfig

    println '... finished configuring AwsAppConfig config'
  }

  def afterConfigMerge = {config, ctx ->
    // let's put the mergedConfig in ctx
    ctx.appConfig.grails.plugin.awsappconfig.putAll(config.grails.plugin.awsappconfig)
  }
}
