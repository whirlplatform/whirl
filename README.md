[![Build Status](https://scrutinizer-ci.com/g/whirlplatform/whirl/badges/build.png?b=master)](https://scrutinizer-ci.com/g/whirlplatform/whirl/build-status/master)

<p align="center">
  <img src="logo.png" />
</p>

# Whrl Platform

The Whirl Platform is application builder software for database developers that simplifying of creating web
applications. It provides WYSIWYG tools for building UI that tightly binds to database data and business logic. Building
application didn't require to write application server or client side logic, all logic can be done database side.

Platform is on production-ready state and used in more than twenty closed source commercial applications.

## Demo

You can try Whirl Platform on the our demo server.

#### Application

Server: http://whirlplatform.cloudjiffy.net/app?application=whirl-showcase

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

For PostgreSQL you should have local configured RDBMS on 5432 port. SQL scripts for creating metadata database are:

- PostgreSQL:

    ```sql
    CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
    CREATE DATABASE whirl OWNER whirl;
    GRANT ALL PRIVILEGES ON DATABASE whirl TO whirl;
    -- connect to whirl database as superuser and run next commands
    CREATE SCHEMA whirl AUTHORIZATION whirl;
    CREATE EXTENSION IF NOT EXISTS hstore;
    ```

- MySQL:
    ```sql
    CREATE USER whirl IDENTIFIED BY 'password';
    CREATE DATABASE whirl;
    GRANT ALL ON whirl.* TO whirl;
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

### Application editor - whirl-editor

#### Server

        mvn tomcat7:run -pl whirl-editor-server -am

#### Client

        mvn gwt:codeserver -pl whirl-editor-client -am

### Troubleshooting

If you experience the following issue while initializing maven project:

    Blocked mirror for repositories: [jboss-central (http://repository.jboss.org/nexus/content/groups/public-jboss, default, releases...

Add following to your maven settings.xml:

```xml

<mirrors>
    <mirror>
        <id>imagej-repository-mirror</id>
        <name>ImageJ repository mirror</name>
        <url>https://maven.imagej.net/content/repositories/public</url>
        <mirrorOf>imagej-repository</mirrorOf>
    </mirror>
    <mirror>
        <id>jboss-repository-mirror</id>
        <name>JBoss Public Nexus Repository</name>
        <url>https://repository.jboss.org/nexus/content/groups/public/</url>
        <mirrorOf>jboss-central</mirrorOf>
    </mirror>
</mirrors>
```