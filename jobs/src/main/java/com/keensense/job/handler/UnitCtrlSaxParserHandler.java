package com.keensense.job.handler;

import com.keensense.job.entity.CtrlUnit;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义sax解析器，处理CtrlUnit xml数据！
 *
 * @author:duf
 * @version:1.0.0
 * @date 2018/11/30
 */
@Slf4j
public class UnitCtrlSaxParserHandler extends DefaultHandler {

    /**
     * 保存解析转换后的CtrlUnit对象
     */
    private List<CtrlUnit> ctrlUnitList = new ArrayList<CtrlUnit>();
    /**
     * 临时CtrlUnit对象
     */
    private CtrlUnit tempCtrlUnit = null;
    /**
     * 记录解析时节点名称
     */
    private String tagName = null;
    /**
     * 记录解析时节点的值
     */
    private String tagValue = null;

    public UnitCtrlSaxParserHandler(){
    }


    public List<CtrlUnit> getCtrlUnitList() {
        return ctrlUnitList;
    }

    @Override
    public void startDocument() throws SAXException {
        log.info("-----解析CtrlUnit xml文档开始--------------");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tagName = qName;
        if("Organ".equals(qName)){
            tempCtrlUnit = new CtrlUnit();
       }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(tempCtrlUnit != null){
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
        if("Organ".equals(qName)){
            tempCtrlUnit.setUnitState(1L);
            tempCtrlUnit.setUnitLevel(2L);//初始化为根节点
            tempCtrlUnit.setIsLeaf(0L);//初始化为非叶子节点
            ctrlUnitList.add(tempCtrlUnit);
        }
    }
    @Override
    public void endDocument() throws SAXException {
        log.info("-----解析CtrlUnit xml文档结束--------------");
    }

    /**
     * 将解析的xml数据封装到tempCtrlUnit对象中
     * @param key 标签名
     * @param value 标签值
     */
    private void packageCameraData(String key, String value) {

        if(tempCtrlUnit == null){
            return;
        }
        switch (key) {
            case "Name":
                tempCtrlUnit.setDisplayName(value);
                tempCtrlUnit.setUnitName(value);
                break;
            case "CodeId":
                tempCtrlUnit.setUnitNumber(value);
                tempCtrlUnit.setUnitIdentity(value);
                break;
            case "ParentId":
                tempCtrlUnit.setUnitParentId(value);
                break;
            default:
                //...
                break;
        }
    }

}
