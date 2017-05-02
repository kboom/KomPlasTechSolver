FROM hazelcast/hazelcast:latest
COPY hazelcast.xml $HZ_HOME
COPY kpts-solver.jar /libs/
COPY entrypoint.sh ./
RUN chmod +x entrypoint.sh
ENV CLASSPATH /libs
ENTRYPOINT ./entrypoint.sh