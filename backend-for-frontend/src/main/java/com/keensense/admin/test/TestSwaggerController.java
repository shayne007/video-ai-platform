package com.keensense.admin.test;

import com.keensense.admin.mqtt.config.EnumerationConfig;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName: TestSwaggerController
 * @Author: cuiss
 * @CreateDate: 2019/5/7 14:31
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@RestController
@Api(tags = "测试swagger")
@RequestMapping("/test")
public class TestSwaggerController {

    @Autowired
    private EnumerationConfig enumerationConfig;

    @ApiOperation(value = "计算+", notes = "加法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "a", paramType = "path", value = "数字a", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "b", paramType = "path", value = "数字b", required = true, dataType = "Long", example = "1")
    })
    @GetMapping("/{a}/{b}")
    public Integer get(@PathVariable Integer a, @PathVariable Integer b) {
        return a + b;
    }

    @ApiOperation(value = "测试细类属性获取")
    @GetMapping("/testEnumeration")
    public R testEnumeration() {
        R result = R.ok();
        String personsSex = enumerationConfig.getSex();
        Map<String, String> personsSexMap = splitEnumerationCodeForName(personsSex);
        Map<String, String> personsSexMap_2 = splitEnumerationNameForCode(personsSex);

        String personsCoatStyle = enumerationConfig.getCoatStyle();
        Map<String, String> personsCoatStyleMap = splitEnumerationCodeForName(personsCoatStyle);
        Map<String, String> personsCoatStyleMap_2 = splitEnumerationNameForCode(personsCoatStyle);

        String vehicleClass = enumerationConfig.getVehicleClass();
        Map<String, String> vehicleClassMap = splitEnumerationCodeForName(vehicleClass);
        Map<String, String> vehicleClassMap_2 = splitEnumerationNameForCode(vehicleClass);

        String vehicleBrand = enumerationConfig.getVehicleBrand();
        Map<String, String> vehicleBrandMap = splitEnumerationCodeForName(vehicleBrand);
        Map<String, String> vehicleBrandMap_2 = splitEnumerationNameForCode(vehicleBrand);

        String colorGroups = enumerationConfig.getColorGroups();
        Map<String, String> colorGroupsMap = splitEnumerationCodeForName(colorGroups);
        Map<String, String> colorGroupsMap_2 = splitEnumerationNameForCode(colorGroups);

        result.put("personsSexMap", personsSexMap);
        result.put("personsSexMap_2", personsSexMap_2);
        result.put("personsCoatStyleMap", personsCoatStyleMap);
        result.put("personsCoatStyleMap_2", personsCoatStyleMap_2);
        result.put("vehicleClassMap", vehicleClassMap);
        result.put("vehicleClassMap_2", vehicleClassMap_2);
        result.put("vehicleBrandMap", vehicleBrandMap);
        result.put("vehicleBrandMap_2", vehicleBrandMap_2);
        result.put("colorGroupsMap", colorGroupsMap);
        result.put("colorGroupsMap_2", colorGroupsMap_2);
        return result;
    }

    public Map<String, String> splitEnumerationCodeForName(String str) {
        Map<String, String> map = new HashMap<>();
        String[] strArray = str.split("/");
        for (int i=0; i+1 < strArray.length; i+=2) {
            map.put(strArray[i], strArray[i+1]);
        }
        return map;
    }

    public Map<String, String> splitEnumerationNameForCode(String str) {
        Map<String, String> map = new HashMap<>();
        String[] strArray = str.split("/");
        for (int i=0; i+1 < strArray.length; i+=2) {
            map.put(strArray[i+1], strArray[i]);
        }
        return map;
    }
}
