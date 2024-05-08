# gridgain-dih
GridGain Digital Integration Hub

1. Start DB's in Docker & Load Data

```sh
cd mysql
./docker.sh

cd postgres
./docker.sh

cd cassandra
./docker.sh
```

2. Run the class com.gridgain.dih.replicate.GenerateAll

	This will generate POJOs, CacheConfiguration and IgniteClientHelper classes in com.gridgain.dih.gen package

3. Start a server node

	com.gridgain.dih.app.IgniteServer

4. Start the Client

	com.gridgain.dih.gen.IgniteClientHelper
