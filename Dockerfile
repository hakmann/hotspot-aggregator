FROM dep-dev-registry.cloudzcp.io/library/openjdk:8-jdk-alpine
MAINTAINER SKCNC

EXPOSE 8080/TCP
VOLUME /tmp

COPY ["src/docker/create-user.sh", "src/docker/startup.sh", "/usr/bin/"]

ADD target/aggregator-0.0.1-SNAPSHOT.jar /home/java/app.jar

RUN chmod +x /usr/bin/create-user.sh

RUN /usr/bin/create-user.sh && \
   chown -R java:java /home/java && \
   chmod +x /usr/bin/startup.sh


USER java
CMD ["/usr/bin/startup.sh"]