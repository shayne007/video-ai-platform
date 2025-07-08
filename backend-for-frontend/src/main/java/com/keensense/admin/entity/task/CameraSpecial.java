package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("camera_special")
public class CameraSpecial extends Model<CameraSpecial> {
	private static final long serialVersionUID = 1L;

	/**
	 * 监控点编号
	 */
	@TableId
	private Long cameraId;
	/**
	 * 场景: 100-室内场景
	 */
	private String sence;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.cameraId;
  }
}
