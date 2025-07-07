package com.keensense.picturestream.util.kafka;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

/**
 * 从所有分区中找出那些 Leader 副本在南方的所有分区，然后随机挑选一个进行消息发送。
 * 
 * @author fengsy
 * @date 3/21/21
 * @Description
 */

public class RegionPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        return partitions.stream().filter(p -> isSouth(p.leader().host())).map(PartitionInfo::partition).findAny()
            .get();
    }

    private boolean isSouth(String host) {
        if (host.contains("south")) {
            return true;
        }
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
