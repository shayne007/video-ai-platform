package com.keensense.dataconvert.framework.common.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.common.loadbalance
 * @Description： <p>  轮询算法 </p>
 * @Author： - Jason
 * @CreatTime：2019/8/7 - 16:37
 * @Modify By：
 * @ModifyTime： 2019/8/7
 * @Modify marker：
 */
public class RoundRobin {

    private static Integer pos = 0;

    public static String getServer(){
        Map<String, Integer> serverMap = new HashMap<>(5);
        serverMap.putAll(AlgServerMap.serverWeightMap);
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<>();
        keyList.addAll(keySet);
        String server;
        synchronized (pos){
            if (pos > keySet.size()) {
                pos = 0;
            }
            server = keyList.get(pos);
            pos ++;
        }
        return server;
    }


    /**
     * @Description: Just for Test
     * @param args void
     * @Autor: Jason - Jasonandy@hotmail.com
     */
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            System.out.println(getServer());
        }
    }
}
