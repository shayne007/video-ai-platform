package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.FdfsCapacityResult;
import com.keensense.search.domain.FeaturesearchCapacityResult;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.utils.HttpClientUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zhanx xiaohui on 2019-08-30.
 */
@Service
@Slf4j
public class CapacityService {
    @Resource(name = "${structuringData.repository}")
    protected StructuringDataRepository structuringDataRepository;

    @Value("${origin.es.datasource.port}")
    String port;
    @Value("${origin.es.datasource.host}")
    String host;
    private String auth;
    @Value("${origin.es.datasource.username}")
    String username;

    @PostConstruct
    private void setInitailList() {
        String[] user = username.split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(
                Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));
    }

    public String queryCapacity() {
        //获取fast dfs空间使用情况
        JSONObject result = new JSONObject();
        Map<Integer, List<FdfsCapacityResult>> resultMap = structuringDataRepository
                .batchQuery(new HashMap<>(), FdfsCapacityResult.class);
        Entry<Integer, List<FdfsCapacityResult>> entry = resultMap.entrySet().iterator().next();
        List<FdfsCapacityResult> results = entry.getValue();
        long fdfsUsage = 0L;
        long fdfsTotal = 0L;
        long maxpercent = 1L;
        for (FdfsCapacityResult fdfsCapacityResult : results) {
            long u = fdfsCapacityResult.getUsage();
            long t = fdfsCapacityResult.getTotal();
            fdfsUsage += u;
            fdfsTotal += t;
            long percent = u * 100 / t;
            if (maxpercent < percent) {
                maxpercent = percent;
            }
        }
        JSONObject fdfsObject = new JSONObject();
        fdfsObject.put("usage", fdfsUsage);
        fdfsObject.put("total", fdfsTotal);
        fdfsObject.put("maxpercent", maxpercent);
        result.put("fastdfs", fdfsObject);

        //获取搜图模块空间使用情况
        Map<Integer, List<FeaturesearchCapacityResult>> r = structuringDataRepository
                .batchQuery(new HashMap<>(), FeaturesearchCapacityResult.class);
        Entry<Integer, List<FeaturesearchCapacityResult>> e = r.entrySet().iterator().next();
        List<FeaturesearchCapacityResult> rs = e.getValue();
        long featuresearchUsage = 0L;
        long featuresearchTotal = 0L;
        long maxpct = 1L;
        for (FeaturesearchCapacityResult obj : rs) {
            long u = obj.getUsage();
            long t = obj.getTotal();
            featuresearchUsage += u;
            featuresearchTotal += t;
            long percent = u * 100 / t;
            if (maxpct < percent) {
                maxpct = percent;
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("usage", featuresearchUsage);
        obj.put("total", featuresearchTotal);
        obj.put("maxpercent", maxpct);
        result.put("featuresearch", obj);

        String url = "http://" + host + ":" + port + "/_cat/allocation";
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", auth);
        String esResult = HttpClientUtil.get(url, header);
        //返回结果示例
        //shards disk.indices disk.used disk.avail disk.total disk.percent host         ip           node
        //     0           0b    18.4gb     60.2gb     78.6gb           23 192.168.96.2 192.168.96.2 es01
        String[] lines = esResult.split("\n");
        long esUsage = 0L;
        long esTotal = 0L;
        long esmaxpct = 1L;
        for (String line : lines) {
            if (StringUtil.isNullOrEmpty(line) || line.contains("UNASSIGNED")) {
                continue;
            }
            String[] parametor = line.split("\\s+");
            //不能按照单空间去分割字符，因为返回的结果中因为采用了列对齐模式，一些特殊情况下里面可能除了空格还有制表符，
            //所以需要用正则匹配，用空白截取字符。
            String usageString = parametor[2];
            String totalString = parametor[4];
            String pecentString = parametor[5];
            long u = (long) transferCapacity(usageString);
            long t = (long) transferCapacity(totalString);
            long percent = (long) transferCapacity(totalString);
            esUsage += u;
            esTotal += t;
            if (esmaxpct < percent) {
                esmaxpct = percent;
            }
        }
        JSONObject esObject = new JSONObject();
        esObject.put("usage", esUsage);
        esObject.put("total", esTotal);
        esObject.put("maxpercent", esmaxpct);
        result.put("es", esObject);

        return result.toJSONString();
    }

    private double transferCapacity(String capacityString) {
        double capacity = 0L;
        if (capacityString.contains("tb")) {
            String number = capacityString.substring(0, capacityString.indexOf("t"));
            capacity = Double.parseDouble(number) * 1000 * 1000 * 1000;
        } else if (capacityString.contains("gb")) {
            String number = capacityString.substring(0, capacityString.indexOf("g"));
            capacity = Double.parseDouble(number) * 1000 * 1000;
        } else if (capacityString.contains("mb")) {
            String number = capacityString.substring(0, capacityString.indexOf("m"));
            capacity = Double.parseDouble(number) * 1000;
        } else if (capacityString.contains("kb")) {
            String number = capacityString.substring(0, capacityString.indexOf("k"));
            capacity = Double.parseDouble(number);
        }

        return capacity;
    }

    public String setCapacity(JSONObject object) {
        FdfsCapacityResult fdfsCapacityResult = object.toJavaObject(FdfsCapacityResult.class);
        fdfsCapacityResult.setDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        structuringDataRepository.save(fdfsCapacityResult);

        return "success";
    }

    public String setFeaturesearchCapacity(JSONObject object) {
        StopWatch watch = new StopWatch();
        watch.start("");
        FeaturesearchCapacityResult result = object.toJavaObject(FeaturesearchCapacityResult.class);
        result.setDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        structuringDataRepository.save(result);

        watch.stop();
        watch.prettyPrint();
        return "success";
    }

}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-08-30 13:56
 **/