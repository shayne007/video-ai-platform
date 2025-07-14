package com.keensense.search.schedule;

import com.keensense.common.util.HttpClientUtil2;
import java.util.ArrayList;
import java.util.Base64;
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
 * Created by zhanx xiaohui on 2019-10-14.
 */
@Component
@Slf4j
public class ElasticSearchClusterIpSchedule {

    @Value("${origin.es.datasource.host}")
    String host;
    @Value("${origin.es.datasource.username}")
    String username;

    private String auth;
    private List<String> list = new ArrayList<>();

    @PostConstruct
    private void setInitailList() {
        list.add(host);
        String[] user = username.split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(
            Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));
    }

    @Scheduled(fixedDelay = 5000)//上一次执行完毕时间点之后5秒再执行
    private void getElasticSearchClusterIpSchedule() {
        for (String esIp : list) {
            String url = "http://" + esIp + ":9200/_cat/nodes";
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", auth);
            String result = HttpClientUtil2.get(url, header);
            if (StringUtils.isEmpty(result)) {
                continue;
            }
            String[] lines = result.split("\n");
            List<String> newList = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String[] splits = line.split(" ");
                String ip = splits[0];
                if (isIp(ip)) {
                    newList.add(ip);
                }
            }
            log.info("the es ip list is {}.", newList);
            list = newList;
            return;
        }
    }

    public String getEsIp() {
        List<String> tmpList = list;
        return tmpList.get(new Random().nextInt(tmpList.size()));
    }

    private boolean isIp(String IP) {//判断是否是一个IP 
        boolean isIp = false;
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255) {
                if (Integer.parseInt(s[1]) < 255) {
                    if (Integer.parseInt(s[2]) < 255) {
                        if (Integer.parseInt(s[3]) < 255) {
                            isIp = true;
                        }
                    }
                }
            }
        }
        return isIp;
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-10-14 17:35
 **/
