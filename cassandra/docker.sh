docker network create cassandra
docker run --rm -d -v $PWD:/opt/data -v $PWD/cassandra.yaml:/etc/cassandra/cassandra.yaml -p 9042:9042 --name cassandra --hostname cassandra --network cassandra cassandra
docker exec -it cassandra bash -c "/opt/cassandra/bin/cqlsh -f /opt/data/killrvideo-schema.cql"
docker exec -it cassandra bash -c "/opt/cassandra/bin/cqlsh -f /opt/data/killrvideo-inserts.cql"