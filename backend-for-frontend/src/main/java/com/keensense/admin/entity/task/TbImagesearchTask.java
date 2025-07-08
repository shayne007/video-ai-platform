package com.keensense.admin.entity.task;

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
@TableName("tb_imagesearch_task")
public class TbImagesearchTask extends Model<TbImagesearchTask> {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId
    private String id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务描述
     */
//    private String desc;
    /**
     * 案件编号
     */
    private String caseId;
    /**
     * 搜图起始时间
     */
    private Date startTime;
    /**
     * 搜图结束时间
     */
    private Date endTime;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 目标类型：1-人；2-骑；4-车
     */
    private Integer objType;
    /**
     * 搜图图片
     */
    private String picture;
    /**
     * 搜图图片特征base64
     */
    private String feature;
    /**
     * 任务状态 0 正常 1 已删除
     */
    private Integer status;
    /**
     * 搜图过滤条件
     */
    private String qcameraIds;

    private String cameraIds;

    private String cameraNames;
    /**
     * 备注
     */
    private String remark;

    /**
     * 图片url和对应uuid对应json
     */
    private String uuidPictrueJson;

    /**
     * 对应子任务serilnumber
     */
    private String taskId;

    /**
     * 对应主任务号
     */
    private String userSerialnumber;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public TbImagesearchTask() {
        super();
    }

    public TbImagesearchTask(String id, String name, String caseId,
                             Date startTime, Date endTime, Integer objType, String picture,
                             String feature, String qcameraIds, String cameraIds, String cameraNames) {
        this.id = id;
        this.name = name;
        this.caseId = caseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.objType = objType;
        this.picture = picture;
        this.feature = feature;
        this.qcameraIds = qcameraIds;
        this.createTime = new Date();
        this.status = 0;
        this.cameraIds = cameraIds;
        this.remark = cameraNames;
        this.cameraNames = cameraNames;
    }

    public TbImagesearchTask(String id, String name, String caseId,
                             Date startTime, Date endTime, Integer objType, String picture,
                             String feature, String qcameraIds, String cameraIds, String cameraNames,
                             String uuidPictrueJson, String taskId, String userSerialnumber) {
        this.id = id;
        this.name = name;
        this.caseId = caseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.objType = objType;
        this.picture = picture;
        this.feature = feature;
        this.qcameraIds = qcameraIds;
        this.createTime = new Date();
        this.status = 0;
        this.cameraIds = cameraIds;
        this.remark = cameraNames;
        this.cameraNames = cameraNames;
        this.uuidPictrueJson = uuidPictrueJson;
        this.taskId = taskId;
        this.userSerialnumber = userSerialnumber;
    }

}
