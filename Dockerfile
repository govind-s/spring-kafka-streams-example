FROM java:8

COPY /target/streams-wordcount-example*.jar /opt/streams-wordcount-example.jar

RUN mkdir /tmp/kafka-streams
RUN chmod 777 /tmp/kafka-streams

EXPOSE 8080


CMD [\
    "java",\
    "-Xmx512M",\
#    "-Xbootclasspath/a:/java/lib/tools.jar",\
    "-Djava.security.egd=file:/dev/./urandom",\
    "-Dcom.sun.management.jmxremote=true",\
    "-Dcom.sun.management.jmxremote.port=1099",\
    "-Dcom.sun.management.jmxremote.rmi.port=1099",\
    "-Dcom.sun.management.jmxremote.local.only=false",\
    "-Dcom.sun.management.jmxremote.authenticate=false",\
    "-Dcom.sun.management.jmxremote.ssl=false",\
    "-Dcom.sun.management.jmxremote.registry.ssl=false",\
    "-Duser.timezone=America/Chicago",\
    "-Duser.name=gs",\
    "-jar",\
    "/opt/streams-wordcount-example.jar"]