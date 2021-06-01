[![Build Status](https://scrutinizer-ci.com/g/whirlplatform/whirl/badges/build.png?b=master)](https://scrutinizer-ci.com/g/whirlplatform/whirl/build-status/master)

# Whrl Platform

The Whirl Platform is application builder software for database developers that simplifying of creating web applications. It provides WYSIWYG tools for building UI that tightly binds to database data and business logic. Building application didn't require to write application server or client side logic, all logic can be done database side.

Platform is on production-ready state and used in more than twenty closed source commercial applications.

## Demo

You can try Whirl Platform on the our demo server.

#### Application

Server: http://whirlplatform.cloudjiffy.net/app=whirl-showcase


#### Editor

Server: http://demo.whirlplatform.org/editor/

Username: whirl-admin

Password: password


## License

Since the Whirl Platform client side code mostly based on the Sencha GXT library, it's deriving GPL v3 license.

[License text](LICENSE)


## Developing

We are using tbroyer [Maven GWT plugin](https://tbroyer.github.io/gwt-maven-plugin/index.html) to manage GWT modules.

Accordingly to this plugin Maven commands to start modules are:

**whirl-app**
- Server

        mvn tomcat7:run -pl whirl-app-server -am
    
- Client

        mvn gwt:codeserver -pl whirl-app-client -am

**whirl-editor**

- Server

        mvn tomcat7:run -pl whirl-editor-server -am
    
- Client

        mvn gwt:codeserver -pl whirl-editor-client -am
