#!/bin/bash

echo "Bringing GIV down..."
docker-compose down

echo "Bringing postgres up..."
docker-compose up -d giv-pg

echo "Bringing GIV up..."
docker-compose up -d
