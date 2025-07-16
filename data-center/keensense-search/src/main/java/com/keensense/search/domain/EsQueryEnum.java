package com.keensense.search.domain;

public enum EsQueryEnum {
	
	TERM("term"),
	TERMS("terms"),
	RANGE("range"),
	GTE("gte"),
	LTE("lte"),
	LIKE("wildcard"),
	GROUPBY("groupBy"),
	HAVING("having"),
	LIMIT("limit"),
	SIZE("size"),
	SORT("sort"),
	GROUPBYDATE("groupByDate"),
	TOP_HITS("top_hits"),
	FIELD("field"),
	VALUE("value");
	
	private String opt;

	private EsQueryEnum(String opt) {
		this.opt = opt;
	}

	public String getOpt(){
		return this.opt;
	}

}
