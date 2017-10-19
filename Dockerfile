# Use the official Java image. If Ubuntu needed as based image, 
# should write java installation in the Dockerfile

FROM openjdk:8

WORKDIR /opt/package-manager/

COPY target/package-indexer-0.0.1-SNAPSHOT.jar ./

CMD ["java", "-jar", "package-indexer-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
