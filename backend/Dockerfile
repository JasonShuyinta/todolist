FROM openjdk:11 as build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} youtorial-backend.jar
ENTRYPOINT ["java","-jar","/youtorial-backend.jar"]