package com.keensense.densecrowd.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestUtil {
    private RequestUtil() {
    }

    public static String getLocalIp() {
        String currentRequestHost = CommonConstants.LOCAL_IP;
        try {
            if (null != Request.getRequest()) {
                //原读取真实路径方式，当以localhost方式访问的如果有双网卡，会优先读取IPV6路径0:0:0:0:0:0:0:1
//                request.getRemoteAddr() 获取的值为0:0:0:0:0:0:0:1的原因及解决办法
//
//                最近在进行web开发时，在jsp页面获取服务器ip时，遇到了request.getRemoteAddr()获取的值为0:0:0:0:0:0:0:1，这是为什么呢，照道理讲，应该是127.0.0.1才对，为什么这个获取的值变成了ipv6了呢，而且我发现这种情况只有在服务器和客户端都在同一台电脑上才会出现（例如用localhost访问的时候才会出现），后来上网查了查原因，原来是/etc/hosts这个东西作怪(在windows上应该是C:\Windows\System32\drivers\etc\hosts这个文件)，只需要注释掉文件中的 # ::1 localhost 这一行即可解决问题。另外localhost这个文件很有用，这里你可以添加自己的条目，例如添加 192.168.0.212 myweb 这样子，在浏览器中原来只能使用192.168.0.212来访问的，并可以使用myweb来进行替换。
//
//                如果还不能解决，本机访问的时候用127.0.0.1或本机ip代替localhost即可解决
                currentRequestHost = Request.getRequest().getLocalAddr();
                if (CommonConstants.LOCAL_IP.equals(currentRequestHost)) {
                    currentRequestHost = getIpAddress(Request.getRequest());
                }
                if (currentRequestHost.equals("0:0:0:0:0:0:0:1")) {
                    currentRequestHost = CommonConstants.LOCAL_IP;
                }
            }
        } catch (RequestException e) {
            log.error(e.getMessage(), e);
        }
        return currentRequestHost;
    }

    private static Map<String, String> replaceIps = null;

    public static String getResultUrl(String url) {
        if (StringUtil.isNull(url)) return url;

        if (null == replaceIps) {
            String replaceIpsStr = PropertiesUtil
                    .getParameterKey("converse.network.req.config");
            if (StringUtil.isNull(replaceIpsStr)) {
                replaceIpsStr = DbPropUtil
                        .getString("converse-network-req-config");
            }
            Map<String, String> replaceIpsMap = new HashMap<>();
            if (StringUtil.isNotNull(replaceIpsStr)) {
                List<String[]> replaceIpList = PatternUtil.getMatches(
                        replaceIpsStr,
                        "(\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)_(\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)");
                if (ListUtil.isNotNull(replaceIpList)) {
                    for (int i = 0; i < replaceIpList.size(); i++) {
                        replaceIpsMap.put(replaceIpList.get(i)[1],
                                replaceIpList.get(i)[2]);
                        replaceIpsMap.put(replaceIpList.get(i)[2], replaceIpList.get(i)[1]);
                    }
                }
            }
            replaceIps = replaceIpsMap;
        }
        if (!replaceIps.containsKey("127.0.0.1")) {
            String localIp = getLocalIp();
            if (!"127.0.0.1".equals(localIp)) {
                replaceIps.put(CommonConstants.LOCAL_IP + ":8088", localIp + ":8088");
                replaceIps.put(CommonConstants.LOCAL_IP + ":8081", localIp + ":8081");
                replaceIps.put(CommonConstants.LOCAL_IP + ":8082", localIp + ":8082");
            }
        }
        if (null == replaceIps) return url;

        String ipp = PatternUtil.getMatch(url,
                "^((http|https)://)?(\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)(/)?.*$", 0,
                3);
        if (StringUtil.isNull(ipp)) return url;

        if (!replaceIps.containsKey(ipp)) return url;

        return url.replace(ipp, replaceIps.get(ipp));
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
