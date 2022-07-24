FROM maven:3.8.6-openjdk-8-slim AS builder

WORKDIR /home/app/
ADD . ./

RUN apt-get update
RUN apt-get install -y libfreetype6 libfontconfig1 nodejs

RUN mvn clean package -DskipTests -Dmaven.artifact.threads=5

FROM tomcat:9-jdk8-openjdk AS app

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-app/whirl-app-server/target/war/ /usr/local/tomcat/webapps/ROOT

FROM tomcat:9-jdk8-openjdk AS editor

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-editor/whirl-editor-server/target/war/ /usr/local/tomcat/webapps/ROOT

FROM tomcat:9-jdk8-openjdk AS all

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-app/whirl-app-server/target/war/ /usr/local/tomcat/webapps/ROOT
COPY --from=builder /home/app/whirl-editor/whirl-editor-server/target/war/ /usr/local/tomcat/webapps/editor