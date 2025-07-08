package com.keensense.dataconvert.biz.entity;

import java.util.Date;

/**
 * @ClassName：VsdTask
 * @Description： <p> VsdTask - 任务数据迁移 </p>
 * @Author： - Jason
 * @CreatTime：2019/8/15 - 16:18
 * @Modify By：
 * @ModifyTime： 2019/8/15
 * @Modify marker：
 * @version V1.0
*/
public class VsdTask {

    /**
     * id
     */
    private Long id;

    /**
     * 任务号
     */
    private String serialnumber;

    /**
     * 类型 object vlpr
     */
    private String type;

    /**
     * 处理进度
     */
    private Short progress;

    /**
     * 是否有效
     */
    private Short isvalid;

    /**
     * 状态
     */
    private Short status;

    /**
     * 保留参数
     */
    private String reserve;

    /**
     * 重试次数
     */
    private Short retrycount;

    /**
     * 处理服务器ip
     */
    private String slaveip;

    private Date endtime;

    private Date createtime;

    private String userserialnumber;

    private Date entrytime;

    private Integer framerate;

    private String param;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber == null ? null : serialnumber.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Short getProgress() {
        return progress;
    }

    public void setProgress(Short progress) {
        this.progress = progress;
    }

    public Short getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Short isvalid) {
        this.isvalid = isvalid;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    public Short getRetrycount() {
        return retrycount;
    }

    public void setRetrycount(Short retrycount) {
        this.retrycount = retrycount;
    }

    public String getSlaveip() {
        return slaveip;
    }

    public void setSlaveip(String slaveip) {
        this.slaveip = slaveip == null ? null : slaveip.trim();
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUserserialnumber() {
        return userserialnumber;
    }

    public void setUserserialnumber(String userserialnumber) {
        this.userserialnumber = userserialnumber == null ? null : userserialnumber.trim();
    }

    public Date getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(Date entrytime) {
        this.entrytime = entrytime;
    }

    public Integer getFramerate() {
        return framerate;
    }

    public void setFramerate(Integer framerate) {
        this.framerate = framerate;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }
}