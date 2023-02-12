#!/usr/bin/env bash
set -e

echo "Creating database and user 'whirl' with password 'password'"
psql -d whirl <<EOSQL
CREATE SCHEMA whirl AUTHORIZATION whirl;
SET search_path TO whirl;
DO '
BEGIN
    IF (TO_NUMBER(current_setting(''server_version''), ''L9G999g999.99'') >= 13) then
        CREATE EXTENSION IF NOT EXISTS hstore;
    end IF;
END;
'
EOSQL