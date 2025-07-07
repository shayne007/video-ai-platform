#!/bin/bash
BASE_PATH=/home/docker/task/
Version=$1

#curl 172.16.1.51:6100/keensense-shituku/jars.tar.gz -o ../jars/jars.tar.gz --progress
cd ${BASE_PATH}/jars
#tar -xzvf jars.tar.gz

cp -rf keensense-task-1.0.1.jar ../package/task/keensense-task.jar
cp -rf keensense-picturestream-1.0.1.jar ../package/picturestream/keensense-picturestream.jar
echo "copy jar success "

#更新安装版本号
cd ${BASE_PATH}/package
sed -i "s/^.*    image: task-base.*$/    image: task-base:${Version}/" base/docker-compose.yml
sed -i "s/^.*    image: task-base.*$/    image: task-base:${Version}/" task/docker-compose.yml
#sed -i "s/^.*    image: task-base.*$/    image: task-base:${Version}/" picturestream/docker-compose.yml

#更新删除镜像版本号
sed -i "s/^.*docker rmi task-base:.*$/docker rmi task-base:${Version}/" task_un_install.sh

echo "replace version success "
docker rmi -f task-base:${Version}

#构建
docker-compose -f base/docker-compose.yml build
rm -rf images/task-base.tar
docker save task-base:${Version}> images/task-base.tar
echo "save keensense-base"

rm -rf ../build/task/*
mkdir -p ../build/task/
cp -rf ../package/* ../build/task
mv -f ../build/task/install.sh ../build/

cd ../build/

rm -rf Module_TASK_${Version}.tar.gz
echo "tar task start"
tar -zcf Module_TASK_${Version}.tar.gz task install.sh
echo "version success"