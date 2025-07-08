package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarBrandVo implements Serializable{

	private Integer id;

	private String brandId;

	private String brandName;

	private String carSeriesId;

	private String carSeriesName;

	private String carYearId;

	private String carYearName;

	private String carKindId;

	private String carKindName;

	private String carFullId;

	private String carFullName;

	private String c1;

	private String c2;

	private String c3;

}
