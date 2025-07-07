package com.keensense.task.util;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.util.Enumeration;

/**
 * @Description: IP获取工具类
 * @Author: wujw
 * @CreateDate: 2019/10/22 15:14
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class IpUtils {

    private IpUtils() {
    }

    public static String getLocalIp() {
        String ip = "";
        try {
            Enumeration<?> e1 = NetworkInterface.getNetworkInterfaces();//获取多个网卡
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if(ni.getName().startsWith("en")){
                    //取“eth0”和“ens33”两个网卡
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address) {//排除IPv6地址
                            continue;
                        }
                        ip = ia.getHostAddress();
                    }
                    break;
                }
//                if (("eth0").equals(ni.getName()) || ("ens33").equals(ni.getName())) {
//                    //取“eth0”和“ens33”两个网卡
//                    Enumeration<?> e2 = ni.getInetAddresses();
//                    while (e2.hasMoreElements()) {
//                        InetAddress ia = (InetAddress) e2.nextElement();
//                        if (ia instanceof Inet6Address) {//排除IPv6地址
//                            continue;
//                            }
//                            ip = ia.getHostAddress();
//                        }
//                        break;
//                }
            }
        } catch (SocketException e) {
            log.error("get ip error!",e);
        }
        return ip;
    }

    /***
     * @description: 根据url获取
     * @param url url地址
     * @return: java.lang.String
     */
    public static String getIpByUrl(String url){
        URI resultUrl = URI.create(url);
        return resultUrl.getHost();
    }

//    public static void main(String[] args){
//
//        String a ="010111100001111111111111111111111111100000000000000000000000";
//        String b ="011111111111111111111111111111111111111111111111111111111111";
//
//        System.out.println(Math.pow(2,63)-1);
//        System.out.println(Long.MAX_VALUE);
//
//        long a1 = Long.valueOf(a,2);
//        long b1 = Long.valueOf(b,2);
//        System.out.println(a1);
//        System.out.println(b1);
//        long c1 = a1 & b1;
//        System.out.println(c1);
//        String c = Long.toBinaryString(c1);
//        System.out.println(c);
//
//    }
}
