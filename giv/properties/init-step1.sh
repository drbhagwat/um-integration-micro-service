#!/bin/bash

printf "\nCreating First Warehouse\n"
curl -u givadmin:password -d "@seed-data/warehouse.json" -X POST -H "Content-Type: application/json" http://localhost:8080/api/warehouses
printf "\n\n"

printf "\nCreating Catalog Channel\n"
curl -u givadmin:password -d "@seed-data/channel-catalog.json" -X POST -H "Content-Type: application/json" http://localhost:8080/api/channels
printf "\n\n"

printf "\nCreating Retail Channel \n"
curl -u givadmin:password -d "@seed-data/channel-retail.json" -X POST -H "Content-Type: application/json" http://localhost:8080/api/channels
printf "\n\n"

printf "\nCreating Web Channel \n"
curl -u givadmin:password -d "@seed-data/channel-web.json" -X POST -H "Content-Type: application/json" http://localhost:8080/api/channels
printf "\n\n"