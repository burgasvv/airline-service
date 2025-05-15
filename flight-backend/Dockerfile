
FROM postgres:latest
EXPOSE 5432

FROM maven:3.9.9 AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package -DskipTests

FROM openjdk:17 AS prod
COPY --from=build target/flight-backend-0.0.1-SNAPSHOT.jar flight-backend.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "flight-backend.jar"]