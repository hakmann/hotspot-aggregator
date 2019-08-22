#!/bin/sh

echo 'Starting JAVA App'

exec /usr/bin/java $JAVA_OPTS -jar /home/java/app.jar $APP_ARGS --spring.profiles.active=prod