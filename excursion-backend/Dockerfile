
FROM postgres:latest
EXPOSE 5432

FROM maven:3.9.9 AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package

FROM openjdk:17 AS prod
COPY --from=build target/excursion-backend-0.0.1-SNAPSHOT.jar excursion-backend.jar
EXPOSE 9010
ENTRYPOINT ["java", "-jar", "excursion-backend.jar"]