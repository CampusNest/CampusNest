FROM maven:3.9.4-eclipse-temurin-21-alpine as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
COPY --from=build /target/CampusNestProject-1.0-SNAPSHOT.jar CampusNestProject.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "CampusNestProject.jar"]