FROM maven:3.8.4-eclipse-temurin-16-alpine AS build-stage

RUN mkdir /todolist
WORKDIR /todolist

ADD pom.xml pom.xml
RUN mvn dependency:go-offline

COPY . .
RUN mvn package -Dmaven.test.skip


FROM openjdk:16-jdk-alpine3.13
RUN addgroup spring && adduser -S -G spring spring
RUN mkdir /todolist && chown -R spring:spring /todolist
USER spring
WORKDIR /todolist
COPY --from=build-stage /todolist/target/*.jar app.jar

EXPOSE 9090
CMD ["java","-jar", "-Dspring.profiles.active=prod", "app.jar"]
