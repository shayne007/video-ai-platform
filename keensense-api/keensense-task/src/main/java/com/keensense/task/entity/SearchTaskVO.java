package com.keensense.task.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description: 搜图对象
 * @Author: wujw
 * @CreateDate: 2019/5/23 9:27
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class SearchTaskVO {

    private String id;

    @JSONField(name = "start_at")
    private Timestamp startAt;

    private String taskId;

    public SearchTaskVO(String id, Timestamp startAt, String taskId){
        this.id = id;
        this.startAt = startAt;
        this.taskId = taskId;
    }

    public SearchTaskVO(String id, String taskId){
        this.id = id;
        this.taskId = taskId;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public boolean equals(Object searchVO){
        if(this == searchVO){
            return true;
        }
        if(searchVO instanceof SearchTaskVO){
            SearchTaskVO tmp = (SearchTaskVO)searchVO;
            return this.id.equals(tmp.getId());
        }
        return false;
    }
}
