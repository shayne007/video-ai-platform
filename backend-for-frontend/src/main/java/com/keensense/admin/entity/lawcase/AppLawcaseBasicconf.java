package com.keensense.admin.entity.lawcase;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_lawcase_basicconf")
public class AppLawcaseBasicconf extends Model<AppLawcaseBasicconf> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * 
	 */
	private String colName;
	/**
	 * 字典大类编号
	 */
	private String colCode;
	/**
	 * 
	 */
	private String colDes;
	/**
	 * 
	 */
	private String createUserid;
	/**
	 * 
	 */
	private String createUsername;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String orderbyCode;
	/**
	 * 
	 */
	private String isValid;
	/**
	 * basic基本信息，area选择范围，method作案手段，features作案特点
	 */
	private String tableName;
	/**
	 * 
	 */
	private String widgetType;
	/**
	 * 用途:0警综字典 1自定义字典
	 */
	private Integer usefor;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
