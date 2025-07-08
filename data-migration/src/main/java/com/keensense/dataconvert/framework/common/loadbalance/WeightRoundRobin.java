package com.keensense.dataconvert.framework.common.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.common.loadbalance
 * @Description： <p> WeightRoundRobin - 根据权重轮询  https://www.cnblogs.com/xrq730/p/5154340.html </p>
 * @Author： - Jason
 * @CreatTime：2019/8/8 - 9:37
 * @Modify By：
 * @ModifyTime： 2019/8/8
 * @Modify marker：
 */
public class WeightRoundRobin {

    private static Integer pos = 0;

    /**
     * 根据权重来创建数据量 A:3,B:5 则有8个数据在List 然后去List顺序去拿数据
     * @return    权重比即 3:5
     */
    public static String getServer(){
        Map<String, Integer> serverMap = new HashMap<>(10);
        serverMap.putAll(AlgServerMap.serverWeightMap);
        Set<String> keySet = serverMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        List<String> serverList = new ArrayList<>();
        while (iterator.hasNext()){
            String server = iterator.next();
            int weight = serverMap.get(server);
            for (int i = 0; i < weight; i++) {
                serverList.add(server);
            }
        }
        String server = null;
        synchronized (pos){
            if (pos >= serverList.size()) {
                pos = 0;
            }
            server = serverList.get(pos);
            pos ++;
        }
        return server;
    }
}
