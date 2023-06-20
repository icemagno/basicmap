#! /bin/sh

docker ps -a | awk '{ print $1,$2 }' | grep magnoabreu/basicmap:1.0 | awk '{print $1 }' | xargs -I {} docker rm -f {}
docker rmi magnoabreu/basicmap:1.0

docker build --tag=magnoabreu/basicmap:1.0 --rm=true .

docker run --name basicmap --hostname=basicmap --network=interna \
--restart=always \
-e RABBITMQ_HOST=172.21.81.48 \
-e RABBITMQ_USER=sisgeodef \
-e RABBITMQ_PASS=sisgeodef \
-e RABBITMQ_PORT=36317 \
-e DB_HOST=basicmap-db \
-e DB_PORT=36432 \
-e DB_USERNAME=postgres \
-e DB_PASSWORD=admin \
-e DB_NAME=basicmap \
-v /srv/basicmap:/var/lib/rabbitmq/mnesia \
-v /etc/localtime:/etc/localtime:ro \
-p 36316:8080 \
-d magnoabreu/basicmap:1.0
