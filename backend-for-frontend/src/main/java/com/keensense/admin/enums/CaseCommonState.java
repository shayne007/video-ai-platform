package com.keensense.admin.enums;

import org.springframework.web.servlet.support.RequestContext;

public enum CaseCommonState {
	PROCESS(1, "进行中", "dic.casestate.process"),
	FINISH(2, "已结案", "dic.casestate.finish");
 
	public int value;
	public String desc;
	public String label;
	
	private CaseCommonState(int value, String desc, String label) {
		this.value = value;
		this.desc = desc;
		this.label = label;
	}
	
	public void setDesc(int value, String desc) {
		for(CaseCommonState type: CaseCommonState.values()) {
			if(type.value == value) {
				type.desc = desc;
				break;
			}
		}
	}
	
	public static CaseCommonState get(int value) {
		for(CaseCommonState type: CaseCommonState.values()) {
			if(type.value == value) {
				return type;
			}
		}
		return null;
	}	
	
	public String getDesc(RequestContext context) {
		return context.getMessage(label, desc);
	}	
}
