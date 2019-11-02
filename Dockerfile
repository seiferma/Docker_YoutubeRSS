FROM maven:latest AS app-builder
COPY pom.xml /tmp/pom.xml
COPY src /tmp/src
RUN mvn -f /tmp/pom.xml clean verify

FROM alpine:latest AS dependency-downloader
RUN apk --no-cache add wget
RUN wget https://yt-dl.org/downloads/latest/youtube-dl -O /tmp/youtube-dl
RUN chmod +x /tmp/youtube-dl


FROM openjdk:11-jre-slim

RUN apt-get update && \
    apt-get install -y python3 gosu && \
    rm -rf /var/lib/apt/lists/* && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    gosu nobody true

WORKDIR /opt

COPY ./entrypoint.sh ./entrypoint.sh
COPY --from=app-builder /tmp/target/youtube-rss-*.jar ./app.jar
COPY --from=dependency-downloader /tmp/youtube-dl /usr/local/bin/youtube-dl

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "./app.jar"]
