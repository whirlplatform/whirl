#!/usr/bin/env bash
set -e

echo "Creating database and user 'whirl' with password 'password'"
psql -d whirl <<EOSQL
CREATE SCHEMA whirl AUTHORIZATION whirl;
SET search_path TO whirl;
IF (TO_NUMBER(current_setting('server_version'), '999999.99') < 13) then
    CREATE EXTENSION IF NOT EXISTS hstore;
end IF;
EOSQL