FROM openjdk:11-jre-slim
WORKDIR /usr/src/myapp
COPY target/ProductService-0.0.1-SNAPSHOT.jar /usr/src/myapp/product.jar
EXPOSE 4000
CMD [ "java", "-jar", "/usr/src/myapp/product.jar" ]
