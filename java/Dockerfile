FROM maven:3.8.4-openjdk-17-slim as builder
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn dependency:go-offline -B
RUN mvn package

FROM openjdk:17.0.2
WORKDIR /app
COPY ./lorem-ipsum.txt ./
COPY --from=builder target/PerformanceServer.jar ./PerformanceServer.jar
CMD ["java", "-jar", "./PerformanceServer.jar"]