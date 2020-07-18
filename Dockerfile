FROM openjdk:14
COPY . .
ENTRYPOINT ["java", "-jar", "./target/movies-0.0.1-SNAPSHOT.jar"]