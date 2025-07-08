package com.keensense.common.platform.enums;

import org.springframework.web.servlet.support.RequestContext;

public enum Age {
    // 注释
    CHILD(4, "小孩", "dic.objType.child"),

    YOUNG(8, "青年", "dic.objType.young"),

    MIDDLE(16, "中年", "dic.objType.middle"),

    OLD(32, "老年", "dic.objType.bike"),

    UNKNOW(0, "未知", "dic.objType.middle");

    private int value;
    private String desc;
    private String label;

    private Age(int value, String desc, String label) {
        this.value = value;
        this.desc = desc;
        this.label = label;
    }

	public void setDesc(int value, String desc) {
		for(Age type: Age.values()) {
			if(type.value == value) {
				type.desc = desc;
				break;
			}
		}
	}

    public static Age get(int value) {
        for (Age type : Age.values()) {
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

    public String getLabel() {
        return label;
    }
}
