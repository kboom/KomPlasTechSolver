FROM frolvlad/alpine-oraclejre8:slim

ARG PUID=666
ARG APP_GROUP=app
ARG APP_USER=app
ARG APP_HOME=/home/${APP_USER}
ARG LOGS_DIR=${APP_HOME}/logs

ENV CLUSTER_ADDRESS localhost

RUN adduser -D -u ${PUID} -g ${APP_GROUP} -h ${APP_HOME} ${APP_USER}
RUN mkdir -p ${LOGS_DIR} && chown -R ${APP_USER} ${LOGS_DIR}

WORKDIR ${APP_HOME}
COPY --chown=app:app *.jar .
COPY --chown=app:app entrypoint.sh .
RUN chmod ug+x entrypoint.sh

USER ${PUID}
VOLUME ${LOGS_DIR}
ENTRYPOINT ["./entrypoint.sh"]