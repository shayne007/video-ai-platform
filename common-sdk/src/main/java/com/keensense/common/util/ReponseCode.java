package com.keensense.common.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum  ReponseCode {
    /*0-OK，正常*/
    CODE_0("0","OK"),
    /*1-OtherError，其他未知错误*/
    CODE_1("1","OtherError"),
    /*2-Device Busy，设备忙*/
    CODE_2("2","Device Busy"),
    /*3-Device Error，设备错*/
    CODE_3("3","Device Error"),
    /*4-Invalid Operation，无效操作*/
    CODE_4("4","Invalid Operation"),
    /*5-Invalid XML Format，XML 格式无效*/
    CODE_5("5","Invalid XML Format"),
    /*6-Invalid XML Content，XML 内容无效*/
    CODE_6("6","Invalid XML Content"),
    /*7-Invalid JSON Format，JSON 格式无效*/
    CODE_7("7","Invalid JSON Format"),
    /*8-Invalid JSON Content，JSON 内容无效*/
    CODE_8("8","Invalid JSON Content"),
    /*9-Reboot，系统重启中，以附录 B 中类型定义 为准*/
    CODE_9("9","Reboot"),
    /*101，已注册*/
    CODE_101("101","已注册"),
    /*102，未注册或已注销*/
    CODE_102("102","未注册或已注销"),
    /*103 请求IP与注册IP不符*/
    CODE_103("103","请求IP与注册IP不符"),
    /*104 必填字段为空*/
    CODE_104("104","必填字段为空") ;

    private String code;
    private String msg;

    ReponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
