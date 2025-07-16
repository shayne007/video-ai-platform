package com.keensense.commonlib.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("lib_common_info")
public class CommonInfo {
    private String id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Integer type;

    private CommonInfo(){

    }

    public CommonInfo(String id,String name,Date createTime,Integer type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = createTime;
    }

}
