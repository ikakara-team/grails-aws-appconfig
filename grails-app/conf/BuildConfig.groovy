grails.project.work.dir = 'target'

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

  inherits "global"
  log "warn"

  repositories {
    mavenLocal()
    grailsCentral()
    mavenCentral()
  }

  dependencies {
    compile ('com.amazonaws:aws-java-sdk:1.9.36') { // http://aws.amazon.com/releasenotes/Java?browse=1
      export = false // allow user to use another version
    }
  }

  plugins {
    build (":tomcat:8.0.22" ){
      export = false
    }

    // needed for config management
    compile ':plugin-config:0.2.0'

    // needed for aws-appconfig
    compile (':aws-instance:0.6.5') {
      export = false // allow user to use another version
    }

    build(":release:3.1.1",
          ":rest-client-builder:2.1.1") {
      export = false
    }
  }
}
