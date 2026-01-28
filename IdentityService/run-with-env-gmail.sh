#!/usr/bin/env bash
set -e

if [ ! -f ".env.gmail" ]; then
  echo "Falta .env.gmail. Completá los valores."
  exit 1
fi

set -a
source ./.env.gmail
set +a

mvn spring-boot:run
