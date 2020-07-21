#
# Build stage
#
FROM maven:3.6.3-jdk-14 AS build
COPY src ./src
COPY pom.xml ./pom.xml
RUN mvn clean package


#
# Package stage
#
FROM openjdk:14
COPY --from=build ./target/movies-0.0.1-SNAPSHOT.jar ./target/app.jar
ENTRYPOINT ["java", "-jar", "./target/app.jar"]