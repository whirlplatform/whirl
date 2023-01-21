# This dockerfile builds image by using prepackaged war files.
FROM tomcat:9-jdk8-openjdk AS app

RUN rm -rf /usr/local/tomcat/webapps/ROOT
ADD ./whirl-app/whirl-app-server/target/whirl-app-server.war /usr/local/tomcat/webapps/ROOT.war

FROM tomcat:9-jdk8-openjdk AS editor

RUN rm -rf /usr/local/tomcat/webapps/ROOT
ADD ./whirl-editor/whirl-editor-server/target/whirl-editor-server.war /usr/local/tomcat/webapps/ROOT.war

FROM tomcat:9-jdk8-openjdk AS all

RUN rm -rf /usr/local/tomcat/webapps/ROOT
ADD ./whirl-app/whirl-app-server/target/whirl-app-server.war /usr/local/tomcat/webapps/ROOT.war
ADD ./whirl-editor/whirl-editor-server/target/whirl-editor-server.war /usr/local/tomcat/webapps/editor.war