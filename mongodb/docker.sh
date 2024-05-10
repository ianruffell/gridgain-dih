docker run -p27017:27017 -v $PWD:/opt/data --name mongo -d mongo
docker exec -it mongo bash -c "mongoimport --db mflix --collection users --file /opt/data/users.json"
docker exec -it mongo bash -c "mongoimport --db mflix --collection theaters --file /opt/data/theaters.json"
docker exec -it mongo bash -c "mongoimport --db mflix --collection movies --file /opt/data/movies.json"
docker exec -it mongo bash -c "mongoimport --db mflix --collection comments --file /opt/data/comments.json"