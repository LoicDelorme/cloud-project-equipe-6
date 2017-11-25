#!/bin/bash
sudo docker-compose up -d
sudo docker exec -i mongo-db-container mongo < mongoConfiguration.js