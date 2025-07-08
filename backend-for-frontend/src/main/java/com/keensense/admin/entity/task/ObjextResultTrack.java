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
@TableName("objext_result_track")
public class ObjextResultTrack extends Model<ObjextResultTrack> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 序列号，对应哪个任务的结果
	 */
	private String serialnumber;
	/**
	 * tubeid
	 */
	private Integer tubeid;
	/**
	 * objid
	 */
	private Integer objid;
	/**
	 * 目标出现的时间戳
	 */
	private Integer startframepts;
	/**
	 * 轨迹json，schema:{"frames":[{"idx":224,"pts":8960,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":225,"pts":9000,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":226,"pts":9040,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":227,"pts":9080,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":248,"pts":9920,"box":{"x":100,"y":120,"w":40,"h":60}}]}
	 */
	private String trackjson;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
