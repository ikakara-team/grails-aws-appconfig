grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

  inherits("global")
  log "warn"

  repositories {
    grailsCentral()
    mavenLocal()
    mavenCentral()
  }

  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    compile ('com.amazonaws:aws-java-sdk:1.9.24') { // http://aws.amazon.com/releasenotes/Java?browse=1
      export = false
    }
  }

  plugins {
    // needed for testing
    build (":tomcat:8.0.20" ){ // 8.0.18 has issues - https://jira.grails.org/browse/GPTOMCAT-29
      export = false
    }

    // needed for config management
    compile ':plugin-config:0.2.0'

    // needed for aws-appconfig
    compile (':aws-instance:0.3.7') {
      export = true
    }

    build(":release:3.1.0",
          ":rest-client-builder:1.0.3") {
      export = false
    }
  }
}
