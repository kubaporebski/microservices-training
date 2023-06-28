FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/song_service*.jar app.jar
EXPOSE ${SC_SONG_PORT}
ENTRYPOINT ["java", "-jar", "/app.jar"]
