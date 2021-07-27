FROM openjdk:8-jre-alpine
COPY target/api-gateway-0.0.1-SNAPSHOT.jar .
EXPOSE $PORT
EXPOSE $DEBUG_PORT
CMD /usr/bin/java -Dlogging.path=/log/ -DLOG_LEVEL=DEBUG -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT -Xmx400m -Xms400m -jar api-gateway-0.0.1-SNAPSHOT.jar