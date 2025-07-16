package com.keensense.glstsdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import sun.misc.BASE64Encoder;

/**
 * glst算法性能测试 Created by memory_fu on 2019/9/5.
 */
public class GlAlgoTest {

    public static String host = "http://172.16.1.82";
    // public static String host = "http://10.10.10.218";
    public static String port = "38080";
    public Timer timer = new Timer();
    public static AtomicInteger succsessCount = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        if (args.length == 5) {
            host = "http://" + args[4];
        }
        new GlAlgoTest().glstAlgorithm(args[0], Integer.valueOf(args[1]), Long.valueOf(args[2]), Long.valueOf(args[3]));
    }

    public void glstAlgorithm(String path, int threadCount, long requestCount, long requestSec) throws Exception {

        // 读取图片
        long currentTime = TrackFunctionUtil.getCurrentTime();
        Set<String> sets = readPicBase64(path);
        int number = sets.size();
        long sumTime = TrackFunctionUtil.getSumTime(currentTime);
        System.out.println("====" + number + "张图片读取数据时间：" + sumTime + " 毫秒.");

        // 发送请求调用gl结构化算法
        reqGlstAlgo(Lists.newArrayList(sets), threadCount, requestCount, requestSec);

    }

    /**
     * 调用gl算法返回结果
     *
     * @param threadCount
     *            线程数
     * @param requestCount
     *            总请求数
     * @param requestPerSec
     *            每秒发送请求数
     */
    public List<String> reqGlstAlgo(List<String> lists, int threadCount, long requestCount, long requestPerSec)
        throws Exception {

        long currentTime = TrackFunctionUtil.getCurrentTime();
        String reqUrl = host + ":" + port + "/vse/face/rec/image";

        Map<String,Object> params = new HashMap<>();
        params.put("Context.Functions[0]", 200);
        params.put("Context.Type", 2);
        params.put("Context.Params.detect_mode", 1);
        params.put("Image.Data.BinDataType", 1);

        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        List<Future<String>> futureList = Lists.newArrayList();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < requestPerSec; i++) {

                    int anInt = RandomUtil.getInt(lists.size());
                    params.put("Image.Data.BinData", lists.get(anInt));

                    GlAlgoRequest glAlgoRequest = new GlAlgoRequest(reqUrl, params.toString());

                    Future<String> future = threadPool.submit(glAlgoRequest);
                    futureList.add(future);
                    succsessCount.incrementAndGet();
                }
            }
        }, 0, 1000);

        while (true) {
            if (succsessCount.get() >= requestCount) {
                timer.cancel();
                break;
            }
        }
        threadPool.shutdown();

        Map<String, Integer> map = Maps.newHashMap();
        for (Future<String> future : futureList) {
            try {
                String result = future.get();
                Integer num = map.get(result);
                if (null == num) {
                    map.put(result, 1);
                } else {
                    map.put(result, ++num);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long sumTime = TrackFunctionUtil.getSumTime(currentTime);

        System.out.println("====线程数：" + threadCount);
        System.out.println("====调用总次数：" + requestCount);
        System.out.println("====处理总时间：" + sumTime + " 毫秒.");
        System.out.println("====每秒处理数据条数：" + (float)(requestCount / (sumTime / 1000)));
        System.out
            .println("====调用成功数：" + map.get("true") + "调用失败数：" + (null == map.get("false") ? 0 : (map.get("false"))));

        return Lists.newArrayList();

    }

    /**
     * 读取文件夹下全部图片数据，
     *
     * @return 图片base64编码集合
     */
    public Set<String> readPicBase64(String path) throws IOException {
        Set<String> hashSet = Sets.newHashSet();

        File file = new File(path);
        if (!file.isDirectory()) {
            return hashSet;
        }

        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File(path + File.separator + filelist[i]);
            FileInputStream inputFile = new FileInputStream(readfile);
            byte[] buffer = new byte[(int)readfile.length()];
            inputFile.read(buffer);
            inputFile.close();
            String encode = new BASE64Encoder().encode(buffer);
            hashSet.add(encode);
        }
        return hashSet;
    }

}
