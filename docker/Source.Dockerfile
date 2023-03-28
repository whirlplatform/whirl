# This dockerfile builds image by compiling current project from source.
FROM maven:3.8.6-openjdk-8-slim AS builder

WORKDIR /home/app/

ADD . ./

RUN apt-get update
RUN apt-get install -y libfreetype6 libfontconfig1 nodejs

RUN mvn clean package -DskipTests -Dmaven.artifact.threads=5 --activate-profiles full-package


FROM tomcat:9-jdk8-openjdk AS app

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-app/whirl-app-server/target/whirl-app-server.war /usr/local/tomcat/webapps/ROOT.war

FROM tomcat:9-jdk8-openjdk AS editor

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-editor/whirl-editor-server/target/whirl-editor-server.war /usr/local/tomcat/webapps/ROOT.war

FROM tomcat:9-jdk8-openjdk AS all

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-app/whirl-app-server/target/whirl-app-server.war /usr/local/tomcat/webapps/ROOT.war
COPY --from=builder /home/app/whirl-editor/whirl-editor-server/target/whirl-editor-server.war /usr/local/tomcat/webapps/editor.war