package com.keensense.admin.base;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * 复杂业务对象实体抽象类
 * 一个业务实体涉及多个数据库模型实体组合
 *
 * @author Administrator
 */
public class BaseBo implements Serializable {
    private static final long serialVersionUID = 1L;
    //日志对象
    protected transient Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取日志对象
     **/
    protected Logger getLog() {
        return log;
    }

    /**
     * 两个实例是否相等
     **/
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * 将DTO对象转换成字符串
     **/
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 重载hashCode()
     */
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
