FROM openjdk:7u111-jdk-alpine
COPY KTE.jar /KTE.jar
EXPOSE 8080/tcp
CMD ["java", "-jar", "/FBP.jar"]