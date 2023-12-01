[![Статус строительства](https://scrutinizer-ci.com/g/whirlplatform/whirl/badges/build.png?b=master)](https://scrutinizer-ci.com/g/whirlplatform/whirl/build-status/master)

<p style="text-align:center;">
  <img src="logo.png" /> <br>
</p>

# Платформа Whirl

Whirl Platform - это программа для создания приложений для разработчиков баз данных, которая упрощает создание веб-приложений. 
Она предоставляет инструменты WYSIWYG для создания пользовательских интерфейсов, тесно связанных с данными базы данных и бизнес-логикой. Создание
не требует написания логики на стороне сервера приложений или клиента, вся логика может быть выполнена на стороне базы данных.

Платформа находится в состоянии производства и используется в более чем двадцати коммерческих приложениях с закрытым исходным кодом.

## Демо

Вы можете попробовать Whirl Platform на нашем демо-сервере.

#### Приложение

Сервер: [Admin Application](http://demo.whirl-platform.ru/app?application=whirl-admin)

Имя пользователя: whirl-admin

Пароль: password

#### Редактор

Сервер: [Editor](http://demo.whirl-platform.ru/editor/)

Имя пользователя: whirl-admin

Пароль: password


## Установка

### Docker Compose

Самый быстрый способ запустить платформу - использовать docker compose, расположенный в папке `docker`.

**Linux:**
```shell
cd docker
TAG=v0.4.0 docker compose --profile image --project-name whirl up
```

**Windows:**
```shell
cd docker
set TAG=v0.4.0
docker compose --profile image --project-name whirl up
```

Это запустит платформу с конфигурацией по умолчанию и базой данных по умолчанию.

Вы можете открыть приложение в браузере по url [http://localhost:8090/app](http://localhost:8080/app)
и [http://localhost:8090/editor](http://localhost:8080/editor) для редактора.


## Контрибьюторам

### Подготовка базы данных

Сначала необходимо создать базу данных для хранения данных платформы.

PostgreSQL должен быть настроен как локальная СУБД на порт 5432. SQL-скрипты для создания базы метаданных следующие

```sql
CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
CREATE DATABASE whirl OWNER whirl;
GRANT ALL PRIVILEGES ON DATABASE whirl to whirl;
\c whirl -- подключитесь к базе данных whirl как суперпользователь и выполните следующие команды
CREATE SCHEMA whirl AUTHORIZATION whirl;
```


**NOTE: Если вы используете PostgreSQL 12 или ниже, вы можете сначала вручную установить расширение ['hstore'](https://www.postgresql.org/docs/current/hstore.html).**.

### Сборка и запуск

**Проект требует Java 8, более высокие версии пока не поддерживаются.
Также, для корректной работы необходима установка Node.js версии 21.2.0 или выше** ### ### ###

Чтобы подготовить зависимости для запуска платформы в режиме разработки, необходимо собрать предварительные условия:

```shell
mvn clean install "-Dgwt.skipCompilation=true"
```

#### Основная платформа - whirl-app

Команда для запуска бэкенда на сервере Tomcat следующая:

```shell
cd whirl-app
mvn compile war:exploded cargo:run -pl whirl-app-server -am -P jdbc-postgresql,config-postgresql,local-store
```

Мы используем GWT для разработки фронтенда
с [tbroyer Maven GWT plugin](https://tbroyer.github.io/gwt-maven-plugin/index.html) для управления GWT модулями.

Команда для запуска фронтенда в режиме dev следующая

```shell
cd whirl-app
mvn gwt:codeserver -pl whirl-app-client -am
```

После выполнения команды приложение будет доступно по адресу http://localhost:8090/app. Фронтенд-часть компилируется
по требованию.

### Редактор приложений - whirl-editor

Команды для запуска бэкенда:

```shell
cd whirl-editor
mvn compile war:exploded cargo:run -pl whirl-editor-server -am -P jdbc-postgresql,config-postgresql,local-store
```

Команды для запуска фронтенда:

```shell
cd whirl-editor
mvn gwt:codeserver -pl whirl-editor-client -am
```

Редактор будет доступен по адресу http://localhost:8091/editor/.

### Соглашения об именовании баз данных

Пример: Функция, которая принимает два параметра (сообщение и тип окна) и выводит окно в зависимости от этих параметров.

```sql 
CREATE OR REPLACE FUNCTION whirl_admin.show_message(p_message_text text, p_message_type text)
 возврат текста
 LANGUAGE plpgsql
AS $function$.
объявить 
		v_version varchar(2048);
		v_result whirl.function_result;
	НАЧАЛО
		select version()
		в v_version;
		v_result.title := 'Сообщение
		v_result.message := p_message_text;
		v_result.message_type := p_message_type;
		return whirl.as_result(v_result);
	END;
функция
;
```

Входящие параметры функции

```sql
p_message_text текст
```

Переменные в теле функции

```sql
v_parameter_type varchar(4000);
```

Ссылки на другие таблицы (имя столбца в другой таблице)

```sql
r_whirl_users
```

### Возможные проблемы
**Windows**

`Проблема:`
После запуска остаются 2 процесса, занимающие порты `9876` и `9877`.

`Решение проблемы:`
Необходимо завершить процессы под названием `OpenJDK Platform binary`. Так же вы можете запустить `stop_listening.bat`.


## Лицензия

Поскольку клиентский код платформы Whirl в основном основан на библиотеке Sencha GXT, он лицензируется по [GPL v3](LICENSE)
лицензия.

[Текст лицензии GPL v3](LICENSE)