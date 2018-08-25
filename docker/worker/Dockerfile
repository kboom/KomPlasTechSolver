FROM hazelcast/hazelcast:latest

ADD hazelcast.xml /opt/hazelcast
COPY *.jar /opt/libs
ENV CLASSPATH /opt/libs
ENV JAVA_OPTS -Dhazelcast.config=/opt/hazelcast/hazelcast.xml
