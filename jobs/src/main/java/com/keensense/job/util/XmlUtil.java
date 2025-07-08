package com.keensense.job.util;

import com.keensense.job.config.VasUrlConfig;
import com.keensense.job.entity.Camera;
import com.keensense.job.entity.CtrlUnit;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;

/**
 * xml工具类  XmlUtil <br/>
 * 当解析xml data 数据较少（<2万）的时候，可以使用DOM方式进行解析！
 * 相比于SAX特点：占用内存大，处理速度快
 * @Author cuiss
 * @Description
 * @Date 2018/11/6
 */
@Slf4j
@Component
public class XmlUtil {

    private static DocumentBuilderFactory dbFactory = null;
    private static DocumentBuilder db = null;
    private static Document document = null;
    private static List<Camera> cameras = null;
    private static List<CtrlUnit> ctrlUnits = null;

    static {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析点位xml
     *
     * @param xmlString
     * @return
     * @throws Exception
     */
    public static List<Camera> getCameras(String xmlString, VasUrlConfig vasUrlConfig) throws Exception {
        //处理xml报文中的特殊字符
        if(xmlString.contains("&")){
            xmlString = init(xmlString);
        }
        //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
        document = db.parse(new java.io.ByteArrayInputStream(xmlString.getBytes("UTF-8")));
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        NodeList cameraList = document.getElementsByTagName("Device");
        cameras = new ArrayList<>();
        //遍历Device
        for (int i = 0; i < cameraList.getLength(); i++) {
            Camera camera = new Camera();
            //获取第i个camera结点
            org.w3c.dom.Node node = cameraList.item(i);

            //获取book结点的子节点,包含了Test类型的换行
            NodeList cList = node.getChildNodes();//System.out.println(cList.getLength());9

            //将一个book里面的属性加入数组
            ArrayList<String> contents = new ArrayList<>();
            for (int j = 1; j < cList.getLength(); j += 2) {

                org.w3c.dom.Node cNode = cList.item(j);
                String content = cNode.getFirstChild() == null ? "" : cNode.getFirstChild().getTextContent();
                contents.add(content);

            }

            camera.setName(contents.get(0));
            camera.setExtcameraid(contents.get(1));
            camera.setRegion(contents.get(2));
            camera.setStatus(Long.parseLong(contents.get(3)));
            double longitude = Double.parseDouble(StringUtils.isEmpty(contents.get(4)) ? "0" : contents.get(4));
            double latitude = Double.parseDouble(StringUtils.isEmpty(contents.get(5))  ? "0" : contents.get(5));
            if(ChinaMapUtils.IsInsideChina(latitude,longitude)){
                camera.setLongitude(contents.get(4));
                camera.setLatitude(contents.get(5));
            }else{
                camera.setLongitude("0");
                camera.setLatitude("0");
                log.info("此监控点经纬度可能存在异常，camera Extcameraid:{}, longitude：{},latitude：{}",contents.get(1), longitude,latitude);
            }
            camera.setType(Long.parseLong(contents.get(6)));
            //补充url信息
            camera.setUrl(generateUrl(vasUrlConfig,contents.get(1)));
            // VAS 类型为 1
            camera.setCameratype(Camera.CAMERA_TYPE_VAS);
            cameras.add(camera);
        }
        return cameras;
    }

    /**
     * 因解析xml时对于>、<、&、'，"时会出现异常
     * 故对xml中特殊字符进行处理
     * @param xmlString
     */
    private static String init(String xmlString) {
       String replaceString = xmlString.
              // replaceAll(">","&lt;").replaceAll("<","&gt;").
               replaceAll("&","&amp;");
              // .replaceAll("\'","&apos;").
               //replaceAll("\"","&quot;");
       log.info("=========="+replaceString);
       return replaceString;
    }

    /**
     * 解析区域xml
     *
     * @param xmlString
     * @return
     * @throws Exception
     */
    public static List<CtrlUnit> getCtrlUnits(String xmlString,VasUrlConfig vasUrlConfig) throws Exception {
        //处理xml报文中的特殊字符
        if(xmlString.contains("&")){
            xmlString = init(xmlString);
        }
        //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
        document = db.parse(new java.io.ByteArrayInputStream(xmlString.getBytes("UTF-8")));
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        NodeList organList = document.getElementsByTagName("Organ");
        ctrlUnits = new ArrayList<>();
        //遍历Organ
        for (int i = 0; i < organList.getLength(); i++) {
            CtrlUnit ctrlUnit = new CtrlUnit();
            //获取第i个unit结点
            org.w3c.dom.Node node = organList.item(i);

            //获取book结点的子节点,包含了Test类型的换行
            NodeList cList = node.getChildNodes();//System.out.println(cList.getLength());9

            //将一个book里面的属性加入数组
            ArrayList<String> contents = new ArrayList<>();
            for (int j = 1; j < cList.getLength(); j += 2) {

                org.w3c.dom.Node cNode = cList.item(j);
                String content = cNode.getFirstChild() == null ? "" : cNode.getFirstChild().getTextContent();
                contents.add(content);
            }
            ctrlUnit.setDisplayName(contents.get(0));
            ctrlUnit.setUnitName(contents.get(0));
            ctrlUnit.setUnitNumber(contents.get(1));
            ctrlUnit.setUnitIdentity(contents.get(1));
            ctrlUnit.setUnitParentId(contents.get(2));
            ctrlUnit.setUnitState(1L);
            ctrlUnit.setUnitLevel(2L);//初始化为根节点
            ctrlUnit.setIsLeaf(0L);//初始化为非叶子节点
            String parentId = contents.get(2);
            if (StringUtil.isNotNull(parentId)){
                ctrlUnit.setLongNumber(parentId + "!" + contents.get(1));
            }else{
                ctrlUnit.setLongNumber(contents.get(1));
            }
            ctrlUnits.add(ctrlUnit);
        }
        return ctrlUnits;
    }

    private static String generateUrl(VasUrlConfig vasUrlConfig,String deviceId){
        StringBuffer stringBuffer = new StringBuffer("vas://");//vas://name=admin&psw=1234&srvip=192.168.0.75&srvport=8350&devid=43010000001320000009&
        if(vasUrlConfig != null){
            stringBuffer.append("name=").append(vasUrlConfig.getName()).append("&");
            stringBuffer.append("psw=").append(vasUrlConfig.getPsw()).append("&");
            stringBuffer.append("srvip=").append(vasUrlConfig.getSrvip()).append("&");
            stringBuffer.append("srvport=").append(vasUrlConfig.getSrvport()).append("&");
            stringBuffer.append("devid=").append(deviceId).append("&");
        }
        return stringBuffer.toString();
    }

}
