export KAFKA_VERSION=7.5.0

docker build -t kafka-connect-gridgain:$KAFKA_VERSION .

docker-compose -f docker-compose.yml up
