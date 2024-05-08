docker run -v $PWD:/opt/data -p3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=password -d mysql
docker exec -it mysql bash -c "mysqladmin -uroot -ppassword create world"
docker exec -it mysql bash -c "mysql -uroot -ppassword world < world.sql"