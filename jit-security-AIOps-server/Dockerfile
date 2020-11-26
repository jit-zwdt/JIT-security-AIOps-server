FROM tomcat:8-jre8
MAINTAINER "jit-ggzw"
RUN rm -rf /usr/local/tomcat/webapps/*
ADD ./target/*.war /usr/local/tomcat/webapps/api.war
# CentOS use -> RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
# Ubuntu use -> RUN echo 'Asia/Shanghai' >/etc/timezone
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
EXPOSE 8080
CMD ["catalina.sh","run"]
