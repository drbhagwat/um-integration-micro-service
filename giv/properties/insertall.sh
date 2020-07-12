echo "Creating users, role, and users_role tables and populating them with data..."
export PGPASSWORD=root
docker cp ./insertall.sql givdeploy_giv-pg_1:tmp/insertall.sql
docker exec givdeploy_giv-pg_1 psql -U postgres -d postgres -f tmp/insertall.sql
