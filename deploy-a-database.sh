#! /bin/bash

docker run --name basicmap-db --hostname=basicmap-db --network=interna \
--restart=always \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASS=admin \
-e POSTGRES_DBNAME=basicmap \
-e ALLOW_IP_RANGE='0.0.0.0/0' \
-p 36432:5432 \
-v /etc/localtime:/etc/localtime:ro \
-v /srv/basicmap-db/:/var/lib/postgresql/ \
-d kartoza/postgis:14-3.3


