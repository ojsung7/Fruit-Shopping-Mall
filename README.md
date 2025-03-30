# Fruit-Shopping-Mall

1. 개요
    - 이 프로젝트는 과일 쇼핑몰 홈페이지 개발 프로젝트 입니다.
    - ERD, BE, FE 순으로 Full Stack으로 개발할 예정입니다.
    - 혼자서 모든 개발단계를 진행해보려고 합니다.
    - claude AI를 활용하였습니다.
---
## DATA

1. ERD
    - https://mermaid.live/ 에서 ERD 그림 캡처함
    - ![Alt text](/DATA/ERD.png)
2. DB 설치
    - Docker mariadb 설치
        - ```docker run -d --name mariadb-container -e MARIADB_ROOT_PASSWORD=root -e MARIADB_DATABASE=mydb -e MARIADB_USER=myuser -e MARIADB_PASSWORD=myuser -p 3306:3306  mariadb:latest```
    - 컨테이너 내부에 접속
        - ```docker exec -it mariadb-container mariadb -u root -p```
3. Table 생성
    - /DATA/table.sql에 있는 쿼리문으로 테이블 생성
---
## BE

1. 환경
    - java 17
    - spring boot 3.4.4
2. 설계 방식
    - DDD - Domain Driven Design