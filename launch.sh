#!/bin/bash
docker-compose up -d
docker exec -i mongo-db-container mongo < mongoConfiguration.js