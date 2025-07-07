package com.keensense.search.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.search.utils.HttpClientUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by zhanx xiaohui on 2019-11-12.
 */
@Slf4j
@Component
public class ImageServiceClusterIpScheduled {

    @Value("${origin.kafka.feature.brokerList}")
    private String brokerList;
    @Value("${send.to.feature.save}")
    private String featureSave;//是否存储搜图模块（是否使用搜图模块）

    private String[] ips;
    private List<String> list = new ArrayList<>();

    @PostConstruct
    private void initProperties() {
        ips = brokerList.split(",");
        list = Arrays.asList(ips);
    }

    @Scheduled(fixedDelay = 5000)//上一次执行完毕时间点之后5秒再执行
    private void getFeatureSearchIpSchedule() {
        if("true".equals(featureSave)){//如果不存储搜图模块，则也不检查搜图可用ip
            List<String> newList = new ArrayList<>();
            for (String imageSearchIp : ips) {
                String url = "http://" + imageSearchIp + ":39081/status";
                String result = HttpClientUtil
                        .post(url, new JSONObject().toJSONString(), new HashMap<>());
                if (!StringUtils.isEmpty(result)) {
                    JSONObject object = JSON.parseObject(result);
                    String errorMsg = object.getString("error_msg");
                    String errorCode = object.getString("error_code");
                    if ("0".equals(errorCode) && "OK".equals(errorMsg)) {
                        newList.add(imageSearchIp);
                    }
                }
            }
            log.info("the FeatureSearch ip list is {}.", newList);
            list = newList;
        }
    }

    /**
     * 随机获取一个可用搜图ip列表
     * @return
     */
    public String getFeatureIp() {
        if(list.size() == 0){
            return null;
        }
        List<String> tmpList = list;
        return tmpList.get(new Random().nextInt(tmpList.size()));
    }

    /**
     * 获取可用搜图模块ip列表，多个ip之间用,分隔
     * @return
     */
    public String getFeatureIps() {
        if(list.size() == 0){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        List<String> tmpList = list;
        for (int i=0; i < tmpList.size(); i++ ) {
            if (i==0) {
                sb.append(tmpList.get(i));
            } else {
                sb.append("," + tmpList.get(i));
            }
        }
        return sb.toString();
    }

}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-11-12 10:07
 **/
