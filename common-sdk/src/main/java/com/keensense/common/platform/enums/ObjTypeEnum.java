package com.keensense.common.platform.enums;

import org.springframework.web.servlet.support.RequestContext;

public enum ObjTypeEnum {
    // 注释
    PERSON(1, "人行"),

    CAR(2, "车辆"),

    FACES(3, "人脸"),

    BIKE(4, "人骑车");

    private int value;
    private String desc;
    private String label;

    ObjTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

	/*public void setDesc(int value, String desc) {
        for(ObjTypeEnum type: ObjTypeEnum.values()) {
			if(type.value == value) {
				type.desc = desc;
				break;
			}
		}
	}*/

    public static ObjTypeEnum get(int value) {
        for (ObjTypeEnum type : ObjTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    public String getDesc(RequestContext context) {
        return context.getMessage(label, desc);
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
