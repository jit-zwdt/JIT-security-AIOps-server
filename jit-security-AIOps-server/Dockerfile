FROM tomcat:8-jre8
MAINTAINER "jit-ggzw"
RUN rm -rf /usr/local/tomcat/webapps/*
ADD ./target/*.war /usr/local/tomcat/webapps/api.war
EXPOSE 8080
CMD ["catalina.sh","run"]
