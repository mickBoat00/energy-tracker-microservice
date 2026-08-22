#!/bin/sh
set -e

ADMIN_TOKEN_FILE=/docker/influxdb/admin-token.json
HOST=http://influxdb3-core:8181
DATABASE=energy-tracker

TOKEN=$(sed -n 's/.*"token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' "$ADMIN_TOKEN_FILE")

if [ -z "$TOKEN" ]; then
  echo "Could not read token from $ADMIN_TOKEN_FILE"
  exit 1
fi

echo "Waiting for InfluxDB at $HOST..."
until influxdb3 show databases --host "$HOST" --token "$TOKEN" >/dev/null 2>&1; do
  sleep 1
done

echo "Creating database $DATABASE..."
if influxdb3 create database --host "$HOST" --token "$TOKEN" "$DATABASE"; then
  echo "Database $DATABASE created."
else
  echo "Database $DATABASE already exists or could not be created."
fi

echo "InfluxDB init complete."
