#!/usr/bin/env bash
set -e

if [ ! -f ".env.mailtrap" ]; then
  echo "Falta .env.mailtrap. Completá los valores."
  exit 1
fi

set -a
source ./.env.mailtrap
set +a

mvn spring-boot:run
