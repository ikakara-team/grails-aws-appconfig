# grails-aws-appconfig

Description:
--------------
Grails plugin, to store application configuration.

Installation:
--------------
```
  plugins {
...
    compile ':aws-instance:0.5.3'
    compile ':aws-appconfig:0.2'
...
  }
```

Configuration:
--------------
Add the following to grails-app/conf/Config.groovy:
```
grails {
  plugin {
    awsinstance {
      accessKey='AWS_ACCESSKEY'
      secretKey='AWS_SECRETKEY'
      s3.bucketName='AWS_S3_BUCKETNAME'
      ses.mailFrom='AWS_SES_MAILFROM'
    }
  }
}
```
See <a href="https://github.com/ikakara-team/grails-aws-instance">aws-instance README</a>

By default, automatically creates DynamoDB tables w/ "DEV" prefix.  Platform (admin) controllers to manage
config hosts and RDS (sql) use the homePath of "/".
```
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
```

Usage:
--------------


Copyright & License:
--------------
Copyright 2014-2015 Allen Arakaki.  All Rights Reserved.

```
Apache 2 License - http://www.apache.org/licenses/LICENSE-2.0
```

History:
--------------
```
0.1 - initial checkin
```