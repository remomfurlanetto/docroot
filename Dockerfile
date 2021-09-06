FROM openjdk:8-jre-alpine

EXPOSE 27117

COPY ./target/docroot-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar docroot-*.jar