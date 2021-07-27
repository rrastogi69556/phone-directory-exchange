FROM openjdk:8-jre-alpine
COPY target/discovery-service-0.0.1-SNAPSHOT.jar .
EXPOSE $PORT
CMD /usr/bin/java -Dlogging.path=/log/ -jar discovery-service-0.0.1-SNAPSHOT.jar