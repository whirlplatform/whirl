[![Build Status](https://scrutinizer-ci.com/g/whirlplatform/whirl/badges/build.png?b=master)](https://scrutinizer-ci.com/g/whirlplatform/whirl/build-status/master)

# Whrl Platform

The Whirl Platform is application builder software for database developers that simplifying of creating web applications. It provides WYSIWYG tools for building UI that tightly binds to database data and business logic. Building application didn't require to write application server or client side logic, all logic can be done database side.

Platform is on production-ready state and used in more than twenty closed source commercial applications.

## Demo

You can try Whirl Platform on the our demo server.

#### Application

Server: http://whirlplatform.cloudjiffy.net/app=whirl-showcase

Username: whirl-showcase-user

Password: password

#### Editor

Server: http://whirlplatform.cloudjiffy.net/editor/

Username: whirl-admin

Password: password

## License

Since the Whirl Platform client side code mostly based on the Sencha GXT library, it's deriving GPL v3 license.

[License text](LICENSE)

## Developing

First we should create database to store platform data.

For PostgreSQL you should have local configured RDBMS on 5432 port. SQL script for creating metadata database is:

```sql
CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
CREATE DATABASE whirl OWNER whirl;
GRANT ALL PRIVILEGES ON DATABASE whirl TO whirl;
-- connect to whirl database as superuser and run next commands
CREATE SCHEMA whirl AUTHORIZATION whirl;
CREATE EXTENSION IF NOT EXISTS hstore;
```

We are using GWT for developing frontend side
with [tbroyer Maven GWT plugin](https://tbroyer.github.io/gwt-maven-plugin/index.html) to manage GWT modules.

### Main platform - whirl-app

#### Server

Command to start backend server is:

    cd whirl-app
    mvn tomcat7:run -pl whirl-app-server -am -P jdbc-postgresql,config-postgresql

#### Client

Command to start frontend in dev mode is:

    cd whirl-app
    mvn gwt:codeserver -pl whirl-app-client -am

After command execution application will be accessible at http://localhost:8090/app. Frontend part will be compiled on
demand.

**whirl-editor**

- Server

        mvn tomcat7:run -pl whirl-editor-server -am

- Client

        mvn gwt:codeserver -pl whirl-editor-client -am
