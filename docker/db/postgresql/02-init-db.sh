#!/usr/bin/env bash
set -e

echo "Creating database and user 'whirl' with password 'password'"
psql -d whirl <<EOSQL
CREATE SCHEMA whirl AUTHORIZATION whirl;
CREATE EXTENSION IF NOT EXISTS hstore;
EOSQL