FROM openjdk:20
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} backendprestabanco.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "backendprestabanco.jar"]
