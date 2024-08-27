for /F "tokens=*" %i IN ('docker ps -q') DO docker stop %i
for /F "tokens=*" %i IN ('docker ps -aq') DO docker rm %i
for /F "tokens=*" %i IN ('docker images -q') DO docker rmi -f %i
for /F "tokens=*" %i IN ('docker volume ls -q') DO docker volume rm %i
for /F "tokens=*" %i IN ('docker network ls -q ^| findstr /v "bridge host none"') DO docker network rm %i
docker system prune -a --volumes
