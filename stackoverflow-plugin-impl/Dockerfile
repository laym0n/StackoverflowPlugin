FROM eclipse-temurin:17-jdk-alpine
ARG jar_name
COPY ./target/${jar_name}.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]