#!/bin/bash

dir=`cd $(dirname $0) && pwd`
es_path=/u2s/master/elasticsearch-6.3.0
IP=$1
if [ -z $IP ];then
        IP=127.0.0.1
fi
indexs=`curl "http://$IP:9200/_cat/indices?" -s | awk '{print $3}'`
indexNameTemp=("vlpr_result_temp" "summary_result_temp" "objext_result_temp" "face_result_temp" "bike_result_temp")

###删除临时索引
function deleteTempIndex(){
	for index in ${indexNameTemp[@]}
	do
		echo -e "====删除临时索引$index 开始···"
		curl -X DELETE "http://$IP:9200/$index"
		echo -e "\n====删除临时索引$index 结束. \n"
	done	
}
if [[ $indexs == *objext_result_temp* ]];then
	deleteTempIndex
fi

###删除备份文件
rm -rf $es_path/data.tar.gz
