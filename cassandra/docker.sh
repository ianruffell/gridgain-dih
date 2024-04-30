docker network create cassandra
docker run --rm -d -v $PWD:/opt/data -v $PWD/cassandra.yaml:/etc/cassandra/cassandra.yaml --name cassandra --hostname cassandra --network cassandra cassandra
