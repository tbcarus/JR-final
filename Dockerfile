FROM openjdk:17-slim
ARG APP_VERSION
COPY build/libs/jr-final-${APP_VERSION}.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]