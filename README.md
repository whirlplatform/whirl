[![Build Status](https://scrutinizer-ci.com/g/whirlplatform/whirl/badges/build.png?b=master)](https://scrutinizer-ci.com/g/whirlplatform/whirl/build-status/master)

<p style="text-align:center;">
  <img src="logo.png" /><br>
</p>

# Whirl Platform

The Whirl Platform is an application builder software for database developers that simplifies the creation of web applications.
It provides WYSIWYG tools for building user interfaces that are tightly coupled to database data and business logic. Building
application doesn't require writing application server or client side logic, all logic can be done database side.

Platform is in production state and used in more than twenty closed source commercial applications.

## Demo

You can try Whirl Platform on our demo server.

#### Application

Server: [Admin Application](http://demo.whirl-platform.ru/app?application=whirl-admin)

Username: whirl-admin

Password: password

#### Editor

Server: [Editor](http://demo.whirl-platform.ru/editor/)

Username: whirl-admin

Password: password


## Installation

### Docker Compose

Fastest way to run platform is to use docker compose located in `docker` folder.

**Linux:**
```shell
cd docker
TAG=v0.3.0 docker compose --profile image --project-name whirl up
```

**Windows:**
```shell
cd docker
set TAG=v0.3.0
docker compose --profile image --project-name whirl up
```

This will run platform with default configuration and default database.

You can open application in browser by url [http://localhost:8090/app](http://localhost:8080/app) 
and [http://localhost:8090/editor](http://localhost:8080/editor) for editor.


## Contributing

### Database Preparation

First, the database to store the platform data should be created.

PostgreSQL should be configured as the local RDBMS on port 5432. SQL scripts for creating the metadata database are

```sql
CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
CREATE DATABASE whirl OWNER whirl;
GRANT whirl ALL PRIVILEGES ON DATABASE whirl;
\c whirl -- connect to the whirl database as superuser and run the following commands
CREATE SCHEMA whirl AUTHORIZE whirl;
```


**NOTE: If you are using PostgreSQL 12 or lower, you can manually install the ['hstore'](https://www.postgresql.org/docs/current/hstore.html) extension first.**.


### Building and Running

**Project requires Java 8, higher versions are not yet supported.** ### ###

To prepare dependencies for running the platform in development mode, you should build prerequisites:

```shell
mvn clean install "-Dgwt.skipCompilation=true"
```

#### Main platform - whirl-app

Command to start the backend on the Tomcat server is:

```shell
cd whirl-app
mvn compile war:exploded cargo:run -pl whirl-app-server -am -P jdbc-postgresql,config-postgresql,local-store
```

We use GWT for frontend development
with [tbroyer Maven GWT plugin](https://tbroyer.github.io/gwt-maven-plugin/index.html) to manage GWT modules.

The command to start the frontend in dev mode is

```shell
cd whirl-app
mvn gwt:codeserver -pl whirl-app-client -am
```

After running the command, the application will be available at http://localhost:8090/app. The frontend part is compiled
on demand.

### Application editor - whirl-editor

Commands to start the backend:

```shell
cd whirl-editor
mvn compile war:exploded cargo:run -pl whirl-editor-server -am -P jdbc-postgresql,config-postgresql,local-store
```

Commands to start the frontend:

```shell
cd whirl-editor
mvn gwt:codeserver -pl whirl-editor-client -am
```

The editor will be available at http://localhost:8091/editor/.

### Database naming conventions

Example: Function that takes two parameters (message and window type) and displays a window depending on those
parameters.

```sql 
CREATE OR REPLACE FUNCTION whirl_admin.show_message(p_message_text text, p_message_type text)
 RETURN text
 LANGUAGE plpgsql
AS $function$.
declare 
		v_version varchar(2048);
		v_result whirl.function_result;
	BEGIN
		select version()
		in v_version;
		v_result.title := 'Message
		v_result.message := p_message_text;
		v_result.message_type := p_message_type;
		return whirl.as_result(v_result);
	END;
function
;
```

Incoming function parameters

```sql
p_message_text text
```

Variables in the function body

```sql
v_parameter_type varchar(4000);
```

References to the other tables (name of the column in the other table)

```sql
r_whirl_users
```

## License

Since the client-side code of the Whirl platform is mostly based on the Sencha GXT library, it's licensed under the [GPL v3](LICENSE)
license.

[GPL v3 license text](LICENSE)
