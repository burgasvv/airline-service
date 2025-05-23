
FROM maven:3.9.9 AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package

FROM openjdk:17 AS prod
COPY --from=build target/gateway-server-0.0.1-SNAPSHOT.jar gateway-server.jar
EXPOSE 9050

ENTRYPOINT ["java", "-jar", "gateway-server.jar"]