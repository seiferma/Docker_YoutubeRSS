FROM maven:3-eclipse-temurin-17-alpine AS app-builder
COPY pom.xml /tmp/pom.xml
COPY src /tmp/src
RUN mvn -f /tmp/pom.xml clean verify

FROM alpine:latest AS dependency-downloader
RUN apk --no-cache add wget
RUN YTDL_VERSION=2023.07.06 && \
    wget https://github.com/yt-dlp/yt-dlp/releases/download/$YTDL_VERSION/yt-dlp -O /tmp/youtube-dl && \
    chmod +x /tmp/youtube-dl


FROM alpine:latest

RUN apk upgrade --no-cache && \
    apk add --no-cache openjdk17-jre-headless python3 su-exec

WORKDIR /opt

COPY ./entrypoint.sh ./entrypoint.sh
COPY --from=app-builder /tmp/target/youtube-rss-*.jar ./app.jar
COPY --from=dependency-downloader /tmp/youtube-dl /usr/local/bin/youtube-dl

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "./app.jar"]
