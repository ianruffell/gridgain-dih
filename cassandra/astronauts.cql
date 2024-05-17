DROP KEYSPACE IF EXISTS packt;
CREATE KEYSPACE packt WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE packt.astronauts (
id int,
name text,
year int,
group int,
status text,
dob text,
birthplace text,
gender text,
alma_mater text,
spaceflights int,
spaceflight_hours int,
spacewalks int,
spacewalk_hours int,
missions text,
PRIMARY KEY (id));

COPY packt.astronauts (id, name,year,group,status,dob,birthplace,gender,alma_mater,spaceflights,spaceflight_hours,spacewalks,spacewalk_hours,missions) FROM '/opt/data/astronauts.csv' WITH HEADER=true;
