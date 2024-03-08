FROM openjdk:11-jre-slim
WORKDIR /usr/src/myapp
COPY target/CoreUtil-1.0-SNAPSHOT.jar /usr/src/myapp/core.jar
CMD [ "java", "-jar", "/usr/src/myapp/core.jar" ]
