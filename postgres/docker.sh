docker run -v $PWD:/opt/data -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=password -d postgres
docker exec -it postgres bash -c "psql -U postgres -W password -f clubdata_ddl.sql"
docker exec -it postgres bash -c "psql -U postgres -W password -f clubdata_data.sql"