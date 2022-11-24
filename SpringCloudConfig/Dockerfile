FROM openjdk:11-jre-slim
WORKDIR /usr/src/myapp
COPY target/SpringCloudConfig-0.0.1-SNAPSHOT.jar /usr/src/myapp/cloudconfig.jar
EXPOSE 8888
CMD [ "java", "-jar", "/usr/src/myapp/cloudconfig.jar" ]
