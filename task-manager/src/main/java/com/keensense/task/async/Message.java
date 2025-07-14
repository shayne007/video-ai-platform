package com.keensense.task.async;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: Message
 * @Description: 消息类
 * @Author: cuiss
 * @CreateDate: 2019/8/10 14:20
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public final class Message implements Serializable {
    private static final long serialVersionUID = -3351206321279291834L;

    @Getter @Setter private String taskId;

    @Getter @Setter private String taskLogId;

    @Getter @Setter private long sendtime;

    @Getter @Setter private long ttl;

    @Getter @Setter private long timeoutSecond;

    @Getter @Setter private int retrycount;

    @Getter @Setter private Map<String, Object> param;

    @Override
    public boolean equals(Object object){
        if(object == null){
            return false;
        }
        if(object == this){
                return true;
            }else{
             if(object instanceof Message){
                 Message msg = (Message)object;
                 return msg.getTaskId().equals(this.taskId);
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return taskId.hashCode();
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ taskId=" + this.taskId);
        stringBuilder.append(", taskLogId=" + this.taskLogId);
        stringBuilder.append(", sendtime=" + this.sendtime);
        stringBuilder.append(", ttl=" + this.ttl);
        stringBuilder.append(", retrycount=" + this.retrycount);
        stringBuilder.append(", timeoutSecond=" + this.timeoutSecond);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
