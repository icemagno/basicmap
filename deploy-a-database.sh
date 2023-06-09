#! /bin/bash

docker run --name solon-db --hostname=solon-db --network=interna \
--restart=always \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASS=admin \
-e POSTGRES_DBNAME=solon \
-e ALLOW_IP_RANGE='0.0.0.0/0' \
-p 36432:5432 \
-v /etc/localtime:/etc/localtime:ro \
-v /srv/solon-db/:/var/lib/postgresql/ \
-d kartoza/postgis:14-3.3


