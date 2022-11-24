FROM openjdk:11-jre-slim
WORKDIR /usr/src/myapp
COPY target/DiscoveryService-0.0.1-SNAPSHOT.jar /usr/src/myapp/discover.jar
EXPOSE 8761
CMD [ "java", "-jar", "/usr/src/myapp/discover.jar" ]
