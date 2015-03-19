grails {
  plugin {
    awsappconfig {
      homePath = "/"
      dataSource {
        dbPrefix="DEV"
        dbCreate="create" //'create', 'create-drop',''
      }
    }
  }
}
