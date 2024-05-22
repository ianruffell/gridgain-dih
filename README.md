# gridgain-dih
GridGain Digital Integration Hub is intended to be used as an example of how to implement a Digital Integration Hub connecting to various 3rd party data sources.

1. Start DB's in Docker & Load Data

```sh
cd mysql
./docker.sh

cd postgres
./docker.sh

cd cassandra
./docker.sh

cd mongodb
./docker.sh
```

2. Run the class com.gridgain.dih.replicate.GenerateAll

	This will generate POJOs, CacheConfiguration and IgniteClientHelper classes in com.gridgain.dih.gen package

3. Build Kafka Connect GridGain image

```sh
cd kafka
export KAFKA_VERSION=latest
docker build -t kafka-connect-gridgain .
```

4. Deploy Kafka to Docker

```sh
docker-compose -f docker-compose.yml up
```

Kafka Control Center is enabled so you can monitor Kafka cluster with http://localhost:9021/

5. Start a server node

	com.gridgain.dih.app.IgniteServer

6. Start the Client

	com.gridgain.dih.gen.IgniteClientHelper
