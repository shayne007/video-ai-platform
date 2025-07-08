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
@TableName("app_lawcase_basicoption")
public class AppLawcaseBasicoption extends Model<AppLawcaseBasicoption> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * conf_id
	 */
	private String basicId;
	/**
	 * 每个列表值，都与对应的基本信息的列名一致，例如：LAWCASE_TYPE的代表案件类型，则COL_NAME就是LAWCASE_TYPE
	 */
	private String colName;
	/**
	 * 
	 */
	private String tableName;
	/**
	 * 下拉列表中的选项名称
	 */
	private String optionsName;
	/**
	 * 在同一个COL_NAME下，OPTIONS_VALUE唯一
	 */
	private String optionsValue;
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
	 * 可利用排序码对 特征进行手工调序，建议排序码按照 1/2/3/4/5的方式升序排列
	 */
	private String orderbyCode;
	/**
	 * 
	 */
	private String isValid;
	/**
	 * 
	 */
	private String busiCode;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
