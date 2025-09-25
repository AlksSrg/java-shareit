FROM eclipse-temurin:21.0.4_9-jre-jammy
VOLUME /tmp
ARG JAR_FILE=target/server-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]