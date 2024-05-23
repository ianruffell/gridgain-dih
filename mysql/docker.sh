docker run -v $PWD:/opt/data -p3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=password -d mysql
sleep 5
docker exec -it mysql bash -c "/usr/bin/mysqladmin -uroot -ppassword create world"
docker exec -it mysql bash -c "/usr/bin/mysql -uroot -ppassword world < /opt/data/world.sql"
