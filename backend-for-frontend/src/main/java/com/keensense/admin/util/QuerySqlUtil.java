package com.keensense.admin.util;

/**
 * Sql语句查询工具类
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2019/3/18
 */
public class QuerySqlUtil {

    private QuerySqlUtil(){}

    /**
     * 如果sql中包含 "%" 或者 "_" 需要进行转义，否则导致查询有问题！
     * @param queryName 查询的名称
     * @return
     */
    public static String replaceQueryName(String queryName){
        String restult = queryName;
        if(StringUtils.isEmpty(queryName)){
            return restult;
        }
        if(queryName.contains("%")){
            restult = queryName.replaceAll("%", "\\\\%");
        }
        if(queryName.contains("_")){
            restult = queryName.replaceAll("_", "\\\\_%");
        }
        return restult;
    }
}
