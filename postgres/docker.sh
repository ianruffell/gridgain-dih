docker run -v $PWD:/opt/data -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=password -d postgres
sleep 5
docker exec -it postgres bash -c "/usr/bin/psql -U postgres -f /opt/data/clubdata_ddl.sql"
docker exec -it postgres bash -c "/usr/bin/psql -U postgres -f /opt/data/clubdata_data.sql"
