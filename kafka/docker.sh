export KAFKA_VERSION=latest

docker build -t kafka-connect-gridgain .

docker-compose -f docker-compose.yml up
