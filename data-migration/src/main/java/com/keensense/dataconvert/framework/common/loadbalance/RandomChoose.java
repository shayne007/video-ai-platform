package com.keensense.dataconvert.framework.common.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.common.loadbalance
 * @Description： <p> RandomChoose 随机选择 </p>
 * @Author： - Jason
 * @CreatTime：2019/8/7 - 16:41
 * @Modify By：
 * @ModifyTime： 2019/8/7
 * @Modify marker：
 */
public class RandomChoose {

    /**
     * 随机选择
     * @return
     */
    public static String getServer(){
        Map<String, Integer> serverMap = new HashMap<>(5);
        serverMap.putAll(AlgServerMap.serverWeightMap);
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<>();
        keyList.addAll(keySet);
        java.util.Random random = new java.util.Random();
        int randomPos = random.nextInt(keyList.size());
        return keyList.get(randomPos);
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(getServer());
        }
    }
}
