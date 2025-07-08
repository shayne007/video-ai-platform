#!/bin/bash

dir=`cd $(dirname $0) && pwd`
es_path=/u2s/master/elasticsearch-6.3.0
IP=$1
if [ -z $IP ];then
        IP=127.0.0.1
fi
indexName=("vlpr_result" "summary_result" "objext_result" "face_result" "bike_result")
indexNameTemp=("vlpr_result_temp" "summary_result_temp" "objext_result_temp" "face_result_temp" "bike_result_temp")

function deleteOldIndex(){
	for index in ${indexName[@]}
	do
		echo -e "====删除旧索引$index 开始······"
			curl -X DELETE "http://$IP:9200/$index"
		echo -e "\n====删除旧索引$index 结束.\n"
	done
}

function createNewIndex(){
	echo -e "====创建新索引开始······"
	bash $dir/index/createIndex.sh $IP
	echo -e "\n====创建新索引结束.\n"
}

function transferData(){
	for index in ${indexName[@]}
	do
		echo -e "====开始转移$index数据······"
		curl -X POST "http://$IP:9200/_reindex" -H 'Content-Type: application/json' -d'
		{
			"source": {
				"index": "'$index\_temp'"
			},
			"dest": {
				"index": "'$index'"
			}
		}'
		echo -e "\n====转移$index数据结束.\n"
	done	
}

deleteOldIndex
createNewIndex
transferData

exit 0
