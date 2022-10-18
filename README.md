[![Build Status](https://scrutinizer-ci.com/g/whirlplatform/whirl/badges/build.png?b=master)](https://scrutinizer-ci.com/g/whirlplatform/whirl/build-status/master)

<p style="text-align:center;">
  <img src="logo.png" />
</p>

# Whirl Platform

The Whirl Platform is application builder software for database developers that simplifying of creating web
applications. It provides WYSIWYG tools for building UI that tightly binds to database data and business logic. Building
application didn't require to write application server or client side logic, all logic can be done database side.

Platform is on production-ready state and used in more than twenty closed source commercial applications.

## Demo

You can try Whirl Platform on our demo server.

#### Application

Server: [Showcase application](http://whirl-demo.jelastic.regruhosting.ru/app?application=whirl-showcase)

Username: whirl-showcase-user

Password: password

#### Editor

Server: [Editor](http://whirl-demo.jelastic.regruhosting.ru/editor/)

Username: whirl-admin

Password: password

## Developing

### Database preparation

First the database to store platform data should be created.

- **PostgreSQL**

  For PostgreSQL you should have configured local RDBMS on 5432 port.
  SQL scripts for creating metadata database are:

    ```sql
    CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
    CREATE DATABASE whirl OWNER whirl;
    GRANT ALL PRIVILEGES ON DATABASE whirl TO whirl;
    -- connect to whirl database as superuser and run next commands
    CREATE SCHEMA whirl AUTHORIZATION whirl;
    ```

- MySQL:
  For MySQL configure it on port 3306.
    ```sql
    CREATE USER whirl IDENTIFIED BY 'password';
    CREATE DATABASE whirl;
    GRANT ALL ON whirl.* TO whirl;
    ```

### Building and running

**Project requires Java 8, upper versions are not supported yet.**

To prepare dependencies for running platform in development mode you should build prerequisites:

```bash
mvn clean install "-Dgwt.skipCompilation=true"
```

#### Main platform - whirl-app

Command to start backend on Tomcat server is:

```bash
cd whirl-app
mvn compile war:exploded cargo:run -pl whirl-app-server -am -P jdbc-postgresql,config-postgresql,local-store
```

We are using GWT for developing frontend side
with [tbroyer Maven GWT plugin](https://tbroyer.github.io/gwt-maven-plugin/index.html) to manage GWT modules.

Command to start frontend in dev mode is:
```bash
cd whirl-app
mvn gwt:codeserver -pl whirl-app-client -am
```

After command execution application will be accessible at http://localhost:8090/app. Frontend part will be compiled on
demand.

### Application editor - whirl-editor

Commands to start backend:
```bash
cd whirl-editor
mvn compile war:exploded cargo:run -pl whirl-editor-server -am -P jdbc-postgresql,config-postgresql,local-store
```

Commands to start frontend:
```bash
cd whirl-editor
mvn gwt:codeserver -pl whirl-editor-client -am
```

Editor will be accessible at http://localhost:8091/editor/.

### Release preparation

Command to set new version in all pom.xml files is:
```bash
mvn versions:set -DnewVersion=X.X.X-SNAPSHOT -DprocessAllModules
```

## Database Naming Conventions
incoming parameters
- p_...
```bash
example idx
```

Variables in the body
- v_....
```bash
example idx
```

Links to the other tables
- r_...
```bash
example idx
```

Indexes
- Idx
```bash
example idx
```

## License

Since the Whirl Platform client side code mostly based on the Sencha GXT library, it's deriving [GPL v3](LICENSE) license.

[GPL v3 license text](LICENSE)
