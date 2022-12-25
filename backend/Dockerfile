FROM openjdk:11 as build
RUN mkdir -p /app
WORKDIR /app
COPY src/main/resources/application.properties /app/
ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} todo-list-backend.jar
COPY ${JAR_FILE} /app/todo-list-backend.jar
CMD ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-jar","/todo-list-backend.jar"]