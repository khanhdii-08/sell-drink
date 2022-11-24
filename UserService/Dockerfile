FROM openjdk:11-jre-slim
WORKDIR /usr/src/myapp
COPY target/UserService-0.0.1-SNAPSHOT.jar /usr/src/myapp/user.jar
EXPOSE 8001
CMD [ "java", "-jar", "/usr/src/myapp/user.jar" ]
