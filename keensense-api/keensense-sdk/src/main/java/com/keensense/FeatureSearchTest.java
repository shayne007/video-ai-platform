package com.keensense;

import com.google.common.collect.Maps;
import com.keensense.glstsdk.TrackFunctionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class FeatureSearchTest {

    public static String personFeature = "/f39//39/v3+/v3+//38/fz8//3+AAMC//7+/vz8/v4AAQYE/v39/v7+CwwJCxQJAwcPDAwHBQYEBQUCAAH//v39/wD+/vr8/gMHAP79/vz9/v3+/v8BAQEA/f39/fz+Af/+//7/AP78/fr7AAD/AAD//Pv7/fv8AAMDAAAAAAAAAAD//Pz+AAQF///+///+/f7+/fr7/f79/v3+AP78/gECBgL//wD+AP/9/f3+/P39/v7+///+/v3+AAEKA/79/Pz8/v0AAAD+/vz8AP7+//7+/v8AAAAA/v3+///+//79/fv++/r7/Pz9/vz9/vz9AAIAAP7+/f38/Pv/AAAA/wAAAAEFBAMA/f3///3+AP79/v79AAAAAQkH/f38/fr8//8AAP/9Dwj+/v0A/v7/AP8AAwYAAAAA///+/wAA+vr+///9AwMLCQwI/wABAP7+CQoEAwYGBAL+/wAA/Pr6/f39//79/v39/v7+//3++/z8/vz9/v39/vv9AP8AAAAA/v8AAQMF/f3+/v0A//7///3+/f39/v39/f39/fz7//79/v8ABAYBAP8A/f7+/wAAAQH+/vz9/vz5+vf7+/r7/Pz+AP/+/v39/v7///8B/f3+AP39///+//z//Pz9/f8H/vz9/vr8/Pv7/PsA/v7///0ABAMBAgIBBAIAAP4A/v3+/v79AAD+/wIEAAYB/vz9/P37/Pr7/f4AAP3+/f3///7/+/z7+/j9/v79//7//fv8/fv8/f4ABhYJ/v/+//3+/v7/AAEA/fz9/vz9AAEA/wQJ/f38/v7+//79/vv9/gD//vv+/v3+/vv9AAAAAP//AAD/AAL//P3+/vv8///+/v7+AAAAAP//BAIA//7+/v3+/v4AAwD+/v4A+fn4+/f6/v39/wAA//8A//7//f7+//8A/v7+//4A/f39/fv7AP/+/wQG/wACAwD8/Pv7/Pj8/v8BAgUDAwMA//79AAAAAQQD/Pz6/f8B/v4CAfv+AP/9/v0B/wD///79/fv6/Pr7/f3+///+AAD+/v8AAAD//v3+/fz9/vz9AP/////9/f///v0AAP77/fv9/v7+//7//v7+//39/v79/fz9/v7+//8ADQkBAP8AAP/+/v38/Pz7/f3+/v7+/wIH/v38/fz+/v39/v3/AP/9/fv9AP7+/v3+///+//38/f39/vz9//////4A/f3+//7+AP79/v39AwQA//39/f39/wgMAwMBAAABAAD+/wAD/v8EBwoAAAD//wAA//////79/f39/vv8AQMBAAIIAgIBAQD//v////7/AP//AP//AP//AP3+/v39/vz9//8AAP78AQD///7+AQD+/v7+/fz8/gADAP/9/v4ABggEAAD//v4AAQD/A//9/v7/AwYAAP/+/v3/AQQDBAD8/v///fv6/Pz8AwD///4AAf/+//39//79/v7+/v37/Pv+//7+/v7/AQEA//39/f79/vwA/fz7/Pr8AQD+/v0BAwH+//8AAP/+/vv8/Pz+/v3+AAD+/v3//fz8/vz7//78/Pv9/v38/fr8+fj4+/r6/v7+/vz9//38/fz/AAD/AAD/AP79/vz+//7+//39/v39/fv9/v8AAwUD/f3+/vz8AP78/fz9/f7///4A/v8CAgEAAQD+////AAYSDQUA/fz8/fr7BAcGAQD/AAEFBQIA/v3/AP7+///+/wAABAL//vv8/Pz9/vv9AAIEAP3+AAADAwkN/v0BBgsI/vz7/Pr+/v3+/wH//Pz9/v0A/v39/vz9/f79/fv8/f37/Pn7//7+/v3+/wD9/v3/AAQBAP7+/v38/fz8/v3+/v//AgMA//8C/v4AAP/+AAABAgMB//8AAP0A/wADAgL///7+/v3+AwH/AAABBgT////9AP79//8A/Pv8/v7+/v79//8A/v37/Pr7/P3//wAAAP//AP38/wD+/v39/f39/vz9AP8AAAD/AQoLAwAA/v3+/wAC/v3+/wMD/vz8/fv7AwIA//4AAAACAgIAAP//AP/+AP/9/fz9/f38+/r+//78/fn7";
    public Timer timer = new Timer();
    public static AtomicInteger succsessCount = new AtomicInteger(0);

    public static void main(String[] args) {

        new FeatureSearchTest().requestFeature(10, 100000, 500);
//		new FeatureSearchTest().requestFeature(Integer.valueOf(args[0]), Long.valueOf(args[1]), Long.valueOf(args[2]));
    }

    public static String getGeoJson(String fileName) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    /**
     * 请求搜图模块  任务抓拍库
     *
     * @param threadCount  线程数
     * @param requestCount 发送总数
     * @param requestSec   每秒发送数
     */
    public void requestFeature(int threadCount, long requestCount, long requestSec) {
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

        List<Future<String>> futureList = new ArrayList();
        long currentTime = TrackFunctionUtil.getCurrentTime();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < requestSec; i++) {
                    succsessCount.incrementAndGet();
                    Future<String> future = threadPool.submit(new FeatureRequest(personFeature));
                    futureList.add(future);
                }
            }
        }, 2, 100);

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
        System.out.println("====每秒处理数据条数：" + (float) (requestCount / (sumTime / 1000)));
        System.out.println("====调用成功数：" + map.get("true") + "调用失败数：" + (null == map.get("false") ? 0
                : (map.get("false"))));
    }

}
