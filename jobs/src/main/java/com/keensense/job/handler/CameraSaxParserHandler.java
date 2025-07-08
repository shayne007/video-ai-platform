package com.keensense.job.handler;

import com.keensense.job.config.VasUrlConfig;
import com.keensense.job.entity.Camera;
import com.keensense.job.util.ChinaMapUtils;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义sax解析器，处理Camera xml数据！
 *
 * @author:duf
 * @version:1.0.0
 * @date 2018/11/30
 */
@Slf4j
public class CameraSaxParserHandler extends DefaultHandler {
    /**
     * vas 配置
     */
    private VasUrlConfig vasUrlConfig;

    /**
     * 保存解析转换后的Camera对象
     */
    private List<Camera> cameraList = new ArrayList<Camera>();
    /**
     * 临时camera对象
     */
    private Camera tempCamera = null;
    /**
     * 记录解析时节点名称
     */
    private String tagName = null;
    /**
     * 记录解析时节点的值
     */
    private String tagValue = null;

    public CameraSaxParserHandler(){
    }

    public CameraSaxParserHandler(VasUrlConfig vasUrlConfig) {
        this.vasUrlConfig = vasUrlConfig;
    }

    public List<Camera> getCameraList() {
        return cameraList;
    }

    @Override
    public void startDocument() throws SAXException {
        log.info("-----解析Camera xml文档开始--------------");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tagName = qName;
        if("Device".equals(qName)){
            tempCamera = new Camera();
       }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(tempCamera != null){
            for (int i = 0; i < ch.length; i++) {

                if(ch[i] == '&'){
                    ch[i] = '-';
                }
            }

            tagValue =  new String(ch, start, length);

            if(StringUtil.isNotNull(tagName) && StringUtil.isNotNull(tagValue) ){
                this.packageCameraData(tagName,tagValue);
            }
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("Device".equals(qName)){
            double longitude = Double.parseDouble(tempCamera.getLongitude());
            double latitude = Double.parseDouble(tempCamera.getLatitude());
            if(!ChinaMapUtils.IsInsideChina(latitude,longitude)){
                tempCamera.setLongitude("0");
                tempCamera.setLatitude("0");
                log.info("此监控点经纬度可能存在异常，camera Extcameraid:{}, longitude：{},latitude：{}",tempCamera.getExtcameraid(), longitude,latitude);
            }
            cameraList.add(tempCamera);
        }
    }
    @Override
    public void endDocument() throws SAXException {
        log.info("-----解析Camera xml文档结束--------------");
    }

    /**
     * 将解析的xml数据封装到tempCamera对象中
     * @param key 标签名
     * @param value 标签值
     */
    private void packageCameraData(String key, String value) {

        if(tempCamera == null){
            return;
        }

        switch (key) {
            case "Name":
                tempCamera.setName(value);
                break;
            case "CodeId":
                tempCamera.setExtcameraid(value);
                //补充url信息,可以使用xmlUtils中方法
                tempCamera.setUrl(this.generateVasUrl(vasUrlConfig,value));
                break;
            case "ParentId":
                tempCamera.setRegion(value);
                break;
            case "Status":
                tempCamera.setStatus(Long.parseLong(value));
                break;
            case "Longitude":
                tempCamera.setLongitude(value);
                break;
            case "latitude":
                tempCamera.setLatitude(value);
                break;
            case "Type":
                tempCamera.setType(Long.parseLong(value));
                break;
            default:
                //...
                break;
        }
    }

    /**
     * 例子 vas地址 ： vas://name=admin&psw=1234&srvip=192.168.0.75&srvport=8350&devid=43010000001320000009&
     * @param vasUrlConfig
     * @param deviceId
     * @return
     */
    private String generateVasUrl(VasUrlConfig vasUrlConfig, String deviceId){
        StringBuffer stringBuffer = new StringBuffer("vas://");
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
