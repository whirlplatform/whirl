#!/usr/bin/env bash
set -e

echo "Creating database and user 'whirl' with password 'password'"
psql <<EOSQL
CREATE ROLE whirl WITH LOGIN PASSWORD 'password';
CREATE DATABASE whirl OWNER whirl;
GRANT ALL PRIVILEGES ON DATABASE whirl TO whirl;
EOSQL