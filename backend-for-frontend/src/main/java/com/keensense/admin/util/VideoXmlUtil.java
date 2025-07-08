package com.keensense.admin.util;

import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.common.config.SpringContext;
import com.keensense.common.util.DateUtil;
import com.keensense.common.ws.WebsocketClient;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.PatternUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.java_websocket.drafts.Draft_6455;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * xml工具类  XmlUtil <br/>
 * 当解析xml data 数据较少（<2万）的时候，可以使用DOM方式进行解析！
 * 相比于SAX特点：占用内存大，处理速度快
 *
 * @Author cuiss
 * @Description
 * @Date 2018/11/6
 */
@Slf4j
public class VideoXmlUtil {

    @Resource
    private static ICfgMemPropsService cfgMemPropsService = SpringContext.getBean(ICfgMemPropsService.class);

    private static DocumentBuilderFactory dbFactory = null;
    private static DocumentBuilder db = null;
    private static Document document = null;

    static {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean hasVideo(Map<String, Object> paramMap) {
        //String url = "vas://name=name&psw=psw&srvip=172.16.0.80&srvport=9080&devid=43010000001320000001&";
        String url = MapUtil.getString(paramMap, "originUrl");
        String startTime = MapUtil.getString(paramMap, "startTime");
        String endTime = MapUtil.getString(paramMap, "endTime");
        String[] masInfos = PatternUtil.getMatch(url, "vas://name=([^&]*)&psw=([^&]*)&srvip=([^&]*)&srvport=([^&]*)&devid=([^&]*)&");
        String wsAddr = "ws://" + cfgMemPropsService.getWs2ServerIp() + ":" + cfgMemPropsService.getWs2ServerPort();
        JSONObject param = new JSONObject();
        param.put("request", "getRecordList");
        param.put("seq", System.currentTimeMillis());
        param.put("channel", masInfos[5]);
        WebsocketClient websocketClient = null;
        try {
            param.put("startTime", new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.parseDate(startTime, "yyyy-MM-dd HH:mm:ss")));
            param.put("stopTime", new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.parseDate(endTime, "yyyy-MM-dd HH:mm:ss")));
            websocketClient = new WebsocketClient(new URI(wsAddr), new Draft_6455());
            websocketClient.connectBlocking();
            websocketClient.send(param.toString());
            String msg = websocketClient.getResponse();
            long openTime = System.currentTimeMillis();
            while (StringUtils.isEmpty(msg)) {
                msg = websocketClient.getResponse();
                Thread.sleep(100);
                long costTime = System.currentTimeMillis() - openTime;
                if (costTime > 90 * 1000) {
                    break;
                }
            }
            log.info("hasVideo msg:" + msg);
            if (StringUtils.isEmpty(msg)) {
                log.info("调用websocket接口返回为空msg:" + msg);
                return false;
            }
            JSONObject jsonResp = JSONObject.fromObject(msg);
            String errCode = jsonResp.getString("errCode");
            String info = jsonResp.getString("info");
            //{"response":"getRecordList","seq":53011,"errMsg":"wait timeout","errCode":-4300,"info":""}
            if (StringUtils.isEmpty(info)) {
                log.info("调用websocket接口返回msg:" + msg);
                return false;
            } else {
                String sumNumStr = VideoXmlUtil.getSumNumString(info);
                if (StringUtils.isNotEmptyString(sumNumStr)) {
                    Integer sumNum = Integer.valueOf(sumNumStr);
                    if ("0".equals(errCode) && sumNum > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("获取录像失败:" + e);
            return false;
            //throw new VideoException("监控点URL地址错误");
        }
    }


    /**
     * 解析点位xml
     *
     * @param xmlString
     * @return
     * @throws Exception
     */
    public static String getSumNum(String xmlString) throws Exception {
        if (xmlString.contains("Num=1")) {
            xmlString = init(xmlString);
        }
        //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
        document = db.parse(new java.io.ByteArrayInputStream(xmlString.getBytes("UTF-8")));
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        NodeList responseList = document.getElementsByTagName("SumNum");
        org.w3c.dom.Node node = responseList.item(0);
        String sumNum = node.getFirstChild() == null ? "" : node.getFirstChild().getTextContent();
        return sumNum;
    }


    /**
     * 因解析xml时对于>、<、&、'，"时会出现异常
     * 故对xml中特殊字符进行处理
     *
     * @param xmlString
     */

    private static String init(String xmlString) {
        String replaceString = xmlString.
                // replaceAll(">","&lt;").replaceAll("<","&gt;").
                        replaceAll("Num=1", "");
        // .replaceAll("\'","&apos;").
        //replaceAll("\"","&quot;");
        log.info("==========" + replaceString);
        return replaceString;
    }

    public static String getSumNumString(String xmlString) {
        int start = xmlString.indexOf("<SumNum>");
        int end = xmlString.indexOf("</SumNum>");
        String sumNum = xmlString.substring(start + "<SumNum>".length(), end);
        return sumNum;
    }

}
