#!/usr/bin/env bash

# tear down database, reset it
docker-compose -f docker-compose.dev.yml down
docker volume rm backend_pgadmin_data backend_postgres_data -f
docker-compose -f docker-compose.dev.yml up -d

# generate and migrate prisma
yarn prisma:save
yarn prisma:up
yarn prisma:generate
