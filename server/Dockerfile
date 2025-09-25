FROM openjdk:21-jre-slim
VOLUME /tmp
ARG JAR_FILE=target/server-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]