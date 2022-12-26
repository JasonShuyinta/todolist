FROM openjdk:11 as build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} todo-list-backend.jar
ENTRYPOINT ["java","-jar","/todo-list-backend.jar"]