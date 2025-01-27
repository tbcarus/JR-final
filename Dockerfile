FROM openjdk:17-jdk-slim
COPY build/libs/JR-final-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]