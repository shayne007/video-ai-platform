package com.keensense.admin.util;

import java.util.List;
import java.util.Set;

/**
 * 校验工具类
 * @author admin
 *
 */
public final class ValidateHelper
{
    private ValidateHelper()
    {
        
    }
    
    /**
     * 判断List是否为空
     * @param list
     * @return
     */
    public static boolean isNotEmptyList(List<?> list)
    {
        if (null != list) 
        {
            if ((list.size() > 0) && !list.isEmpty()) return true;
        } 
        return false;
    }
    
    /**
     * 判断Set是否为空
     * @param list
     * @return
     */
    public static boolean isNotEmptySet(Set<?> set)
    {
        if (null != set) 
        {
            if ((set.size() > 0) && !set.isEmpty()) return true;
        } 
        return false;
    }
    
    /**
     * 判断List是否为空
     * @param list
     * @return
     */
    public static boolean isEmptyList(List<?> list)
    {
        return !isNotEmptyList(list);
    }
    
}
