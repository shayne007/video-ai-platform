package com.keensense.admin.vo;


import com.keensense.admin.util.StringUtils;

/**
 * 查询条件通用BO
 * @author admin
 *
 */
public class CondVo implements Comparable<CondVo> {
    public CondVo(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    private String id;
    
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    
    private String name;

    @Override
    public int compareTo(CondVo condBo){
        if(StringUtils.isNumeric(condBo.getId()) && StringUtils.isNumeric(this.id))
        {
            return Integer.valueOf(this.id).compareTo(Integer.valueOf(condBo.getId()));
        }
        
        return this.id.compareTo(condBo.getId());
    }
}
