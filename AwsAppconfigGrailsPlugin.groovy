class AwsAppconfigGrailsPlugin {
  def version = "0.4.1"
  def grailsVersion = "2.2 > *"
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
