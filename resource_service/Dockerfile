FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/resource_service*.jar app.jar
EXPOSE ${SC_RESOURCE_PORT}
ENTRYPOINT ["java", "-jar", "/app.jar"]
