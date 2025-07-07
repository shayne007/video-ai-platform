package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.repository.AlarmRepository;
import com.keensense.search.repository.DocumentRepository;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.utils.JsonConvertUtil;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConvertServiceImpl {

    @Resource
    private MotorVehiclesServiceImpl motorVehiclesServiceImpl;
    @Resource
    private NonMotorVehiclesServiceImpl nonMotorVehiclesServiceImpl;
    @Resource
    private PersonsServiceImpl personsServiceImpl;
    @Resource
    private FaceServiceImpl faceServiceImpl;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    protected JsonConvertUtil jsonConvertUtil;
    @Resource(name = "${structuringData.repository}")
    protected StructuringDataRepository structuringDataRepository;
    @Autowired
    protected AlarmRepository alarmRepository;

    @Value("${docuemnt.service.exist}")
    private String isExist;
    @Value("${face.faceClassPath}")
    private String faceClassPath;

    /**
     * 处理有1400插件转换kafka中数据的那个数据
     *
     * @param jsonObject 请求json
     */
    public void data(JSONObject jsonObject) {
        // 判断是否为机动车数据
        //log.debug("the input json object is c{}", jsonObject);
        JSONObject motorVehicleObject = jsonObject.getJSONObject("MotorVehicleListObject");
        if (!Objects.isNull(motorVehicleObject)) {
            motorVehiclesServiceImpl.batchInsert(jsonObject);
        }

        // 判断是否为非机动车数据
        JSONObject nonMotorVehicleObject = jsonObject.getJSONObject("NonMotorVehicleListObject");
        if (!Objects.isNull(nonMotorVehicleObject)) {
            nonMotorVehiclesServiceImpl.batchInsert(jsonObject);
        }

        // 其他一定是人形和人脸数据
        JSONObject personObject = jsonObject.getJSONObject("PersonListObject");
        JSONObject faceObject = jsonObject.getJSONObject("FaceListObject");

        // 人形数据存储
        personBatchInsert(personObject, jsonObject);

        // face数据单独处理
        faceBatchInsert(faceObject, jsonObject);
    }

    private void faceBatchInsert(JSONObject faceObject, JSONObject jsonObject) {
        if (!Objects.isNull(faceObject)) {
            faceServiceImpl.batchInsert(jsonObject);
        }
    }

    private void personBatchInsert(JSONObject personObject, JSONObject jsonObject) {
        if (!Objects.isNull(personObject)) {
            personsServiceImpl.batchInsert(jsonObject);
        }
    }

}
