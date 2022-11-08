# DB 세팅

docker-compose 로 실습진행 예정.

docker-compose 파일은 아래와 같다.

```yaml
version: '3'
services:
  mysql-for-test:
    image: mysql:5.7.39-debian
    restart: always
    #    command: --lower_case_table_names=1
    container_name: mysql-temp
    networks:
      - default
      # - network-test
    ports:
      - "33306:3306"
    environment:
      - MYSQL_USER=collector
      - MYSQL_PASSWORD=1111
      - MYSQL_DATABASE=collector
      - MYSQL_ROOT_PASSWORD=1111
      - TZ=Asia/Seoul
    command:
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
    volumes:
      - ./init/:/docker-entrypoint-initdb.d/
```

<br>

데이터베이스 생성 SQL

```
create database product_stock;
use product_stock;
```

<br>

