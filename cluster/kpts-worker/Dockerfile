FROM hazelcast/hazelcast:latest
COPY hazelcast.xml $HZ_HOME
COPY komplastechsolver-0.1.0.jar /libs/
COPY entrypoint.sh ./
RUN chmod +x entrypoint.sh
ENV CLASSPATH /libs
ENTRYPOINT ./entrypoint.sh