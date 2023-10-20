FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/eureka_service*.jar app.jar
EXPOSE ${EUREKA_PORT}
ENTRYPOINT ["java", "-jar", "/app.jar"]
