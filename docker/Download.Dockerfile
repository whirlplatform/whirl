FROM maven:3.8.6-openjdk-8-slim AS builder

ENV TAG="v0.1.0"

WORKDIR /home/app/

RUN apt-get update
RUN apt-get install -y wget

RUN wget -O /home/app/whirl-application.war https://github.com/whirlplatform/whirl/releases/download/${TAG}/whirl-application-${TAG}.war
RUN wget -O /home/app/whirl-editor.war https://github.com/whirlplatform/whirl/releases/download/${TAG}/whirl-editor-${TAG}.war

FROM tomcat:9-jdk8-openjdk AS app

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-application.war /usr/local/tomcat/webapps/ROOT.war

FROM tomcat:9-jdk8-openjdk AS all

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-application.war /usr/local/tomcat/webapps/ROOT.war
COPY --from=builder /home/app/whirl-editor.war /usr/local/tomcat/webapps/editor.war