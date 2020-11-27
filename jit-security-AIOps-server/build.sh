imagesid=`docker images | grep -i jit/jitaiops-server | awk '{print $3}'| head -1`
project=/var/lib/jenkins/workspace/JIT-security-AIOps-server/jit-security-AIOps-server
IMAGENAME=jit/jitaiops-server
HARBORURI=www.harbor.mobi
HARBORREPONAME=harbor
if [ -z "$VERSION" ];then
	VERSION=`date +%Y%m%d%H%M`
fi

echo $VERSION

if docker ps -a|grep -i jit-aiops-server;then
   docker rm -f jit-aiops-server
fi

if [ -z "$imagesid" ];then
	echo $imagesid "is null"
else
	docker rmi -f $imagesid 
fi

docker build -t $IMAGENAME:$VERSION $project

docker run -e TZ="Asia/Shanghai" -p 8081:8080 -idt --name jit-aiops-server --network zabbix_net  --ip=172.19.0.6  -v /var/log/tomcat:/usr/local/tomcat/logs  $IMAGENAME:$VERSION


# docker tag $IMAGENAME:$VERSION $HARBORURI/$HARBORREPONAME/$IMAGENAME:$VERSION
# docker login -u ggzw -p Dotacsjit3368 $HARBORURI
# docker push $HARBORURI/$HARBORREPONAME/$IMAGENAME:$VERSION
# docker rmi $HARBORURI/$HARBORREPONAME/$IMAGENAME:$VERSION