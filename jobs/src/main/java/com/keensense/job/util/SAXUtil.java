package com.keensense.job.util;


import com.keensense.job.config.VasUrlConfig;
import com.keensense.job.entity.Camera;
import com.keensense.job.entity.CtrlUnit;
import com.keensense.job.handler.CameraSaxParserHandler;
import com.keensense.job.handler.UnitCtrlSaxParserHandler;
import lombok.extern.slf4j.Slf4j;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * SAX方式解析vas xml内容 <br/>
 * 当解析xml data 数据较多(>2万)的时候，可以使用SAX方式进行解析！
 * 相比于Dom特点：占用内存小，处理速度慢
 * @author:duf
 * @version:1.0.0
 * @date 2018/11/30
 */
@Slf4j
public class SAXUtil {

    private SAXUtil(){}

    /**
     * 解析一个CameraDataXml字符串
     * @param resultCameraDataXml
     * @param vasUrlConfig
     * @return
     * @throws Exception
     */
    public static List<Camera> resolveCameraXmlStr(String resultCameraDataXml, VasUrlConfig vasUrlConfig) throws Exception {

        List<Camera> cameraList = new ArrayList<>();

        SAXParserFactory spFactory = SAXParserFactory.newInstance();

        SAXParser saxParser = spFactory.newSAXParser();
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(resultCameraDataXml.getBytes("utf-8"));
            CameraSaxParserHandler handler = new CameraSaxParserHandler(vasUrlConfig);
            //解析xml
            saxParser.parse(is, handler);

            cameraList = handler.getCameraList();

        } catch (Exception e) {
            log.error("解析 CameraDataXml 出现错误！" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        log.info("------> cameraList : " + cameraList.size());
        return cameraList;
    }

    /**
     * 解析一个 CtrlUnitXml 字符串
     * @param resultCtrlUnitDataXml
     * @param vasUrlConfig
     * @return
     * @throws Exception
     */
    public static List<CtrlUnit> resolveCtrlUnitXmlStr(String resultCtrlUnitDataXml, VasUrlConfig vasUrlConfig) throws Exception {

        List<CtrlUnit> ctrlUnitList = new ArrayList<>();

        SAXParserFactory spFactory = SAXParserFactory.newInstance();

        SAXParser saxParser = spFactory.newSAXParser();
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(resultCtrlUnitDataXml.getBytes("utf-8"));
            UnitCtrlSaxParserHandler handler = new UnitCtrlSaxParserHandler();
            //解析xml
            saxParser.parse(is, handler);

            ctrlUnitList = handler.getCtrlUnitList();

        } catch (Exception e) {
            log.error("解析 CtrlUnitDataXml 出现错误！" + e.getMessage());
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        log.info("------> ctrlUnitList : " + ctrlUnitList.size());
        return ctrlUnitList;
    }

}
