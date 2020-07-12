echo "Truncating all tables in GIV..."
export PGPASSWORD=root
docker cp ./dropall.sql givdeploy_giv-pg_1:tmp/dropall.sql
docker exec givdeploy_giv-pg_1 psql -U postgres -d postgres -f tmp/dropall.sql
