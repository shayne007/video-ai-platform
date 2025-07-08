#!/bin/bash

dir=`cd $(dirname $0) && pwd`
es_path=/u2s/master/elasticsearch-6.3.0
IP=$1
if [ -z $IP ];then
        IP=127.0.0.1
fi
indexs=`curl "http://$IP:9200/_cat/indices?" -s | awk '{print $3}'`

###新建es临时索引,索引结构与新版本一致
if [[ $indexs != *objext_result_temp* ]];then
        echo -e "====创建临时索引开始··· "
        bash $dir/index/createIndex_temp.sh $IP
        echo -e "\n====创建临时索引结束."
fi

###备份旧版本es数据目录
echo "====备份es数据文件开始····"
tar czPf $es_path/data.tar.gz $es_path/data
echo "====备份es数据文件结束."

exit 0
