package com.keensense.dataconvert.biz.task.vo;

import com.keensense.dataconvert.biz.common.enums.ResultEnum;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.task.vo
 * @Description： <p> TaskResultInfo </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 11:38
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 */
public class TaskResultInfo {

    /**
     * 任务结果对象
     */
    private Object taskResult;

    /**
     * 信息描述
     */
    private String collectCallMsg;

    /**
     * 当前处理的数据归属的 table表
     */
    private String collectTableYmdStr;

    /**
     * 状态码
     *      1.默认成功 200
     *      2.mysql --> es error 300
     *      3.推到kafka error 400
     */
    private int collectCallCode = ResultEnum.BIZ_DEAL_SUCCESS.getResultCode();


    public Object getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(Object taskResult) {
        this.taskResult = taskResult;
    }

    public String getCollectCallMsg() {
        return collectCallMsg;
    }

    public void setCollectCallMsg(String collectCallMsg) {
        this.collectCallMsg = collectCallMsg;
    }

    public int getCollectCallCode() {
        return collectCallCode;
    }

    public void setCollectCallCode(int collectCallCode) {
        this.collectCallCode = collectCallCode;
    }

    public String getCollectTableYmdStr() {
        return collectTableYmdStr;
    }

    public void setCollectTableYmdStr(String collectTableYmdStr) {
        this.collectTableYmdStr = collectTableYmdStr;
    }

    /**
     * TaskResultInfo.
     */
    public TaskResultInfo() {
        super();
    }

}
