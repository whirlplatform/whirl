CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
CREATE DATABASE whirl OWNER whirl;
GRANT ALL PRIVILEGES ON DATABASE whirl TO whirl;
-- connect to whirl database as superuser and run next commands
CREATE SCHEMA whirl AUTHORIZATION whirl;