package com.keensense.admin.dto;

import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RealVideoBo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9006160398435839280L;

    private String serialnumber;
    private String taskName;

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    private Integer taskStatus;
    private Integer progress;
    private String slaveip;
    private String createTime;
    private String taskId;
    private String taskSerinumber;
    private String videoUrl;
    private String remark;
    private List<TaskDetail> taskDetailList;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskSerinumber() {
        return taskSerinumber;
    }

    public void setTaskSerinumber(String taskSerinumber) {
        this.taskSerinumber = taskSerinumber;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getSlaveip() {
        return slaveip;
    }

    public void setSlaveip(String slaveip) {
        this.slaveip = slaveip;
    }

    public List<TaskDetail> getTaskDetailList() {
        return taskDetailList;
    }

    public void setTaskDetailList(List<TaskDetail> taskDetailList) {
        this.taskDetailList = taskDetailList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTaskDetail(String taskId, String taskSerinumber, String videoUrl, String progress
            , String remark, String parentSerialnumber, String status) {
        TaskDetail task = new TaskDetail(taskId, taskSerinumber, videoUrl, progress, remark, parentSerialnumber, status);
        if (StringUtil.isNotNull(task.getVideoUrl())) {
            /**
             * vas://name=admin&psw=1234&srvip=192.168.0.234&srvport=8350&devid=1&starttime=20180911105704&endtime=20180911111504&
             * 从字符串中提取starttime和endtime
             * */
            String[] arrs = task.getVideoUrl().split("&");
            if (arrs.length > 2) {
                task.setStartTime(getDate(arrs[arrs.length - 2].substring(arrs[arrs.length - 2].indexOf("=") + 1)));
                task.setEndTime(getDate(arrs[arrs.length - 1].substring(arrs[arrs.length - 1].indexOf("=") + 1)));
            }
        }
        if (taskDetailList == null) {
            taskDetailList = new ArrayList<>();
        }
        taskDetailList.add(task);
    }

    private String getDate(String dateStr) {
        Date date = DateUtil.getDate(dateStr);
        if (date != null) {
            return DateUtil.getFormat(date, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }

    public class TaskDetail implements Serializable{
        private String taskId;
        private String taskSerinumber;
        private String videoUrl;
        private String startTime;
        private String endTime;
        private String progress;
        private String remark;
        private String parentSerialnumber;
        private String status;

        public TaskDetail(String taskId, String taskSerinumber, String videoUrl, String progress
                , String remark, String parentSerialnumber, String status) {
            this.taskId = taskId;
            this.taskSerinumber = taskSerinumber;
            this.videoUrl = videoUrl;
            this.progress = progress;
            this.remark = remark;
            this.parentSerialnumber = parentSerialnumber;
            this.status = status;
        }

        public String getParentSerialnumber() {
            return parentSerialnumber;
        }

        public void setParentSerialnumber(String parentSerialnumber) {
            this.parentSerialnumber = parentSerialnumber;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskSerinumber() {
            return taskSerinumber;
        }

        public void setTaskSerinumber(String taskSerinumber) {
            this.taskSerinumber = taskSerinumber;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
