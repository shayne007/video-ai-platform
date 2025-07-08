package com.keensense.dataconvert.api.util.db;

import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util.db
 * @Description： <p> DbUtils  </p>
 * @Author： - Jason
 * @CreatTime：2019/8/15 - 16:29
 * @Modify By：
 * @ModifyTime： 2019/8/15
 * @Modify marker：
 */
public class DbUtils {

    private static Logger logger = LoggerFactory.getLogger(DbUtils.class);

    /**
     * 数据库驱动
     */
    public static String MYSQL_DB_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * 声明三个核心接口对象
     * {
     *     1.Connection 数据库连接的对象
     *     2.PreparedStatement SQL命令预处理并执行操作的对象
     *     3.ResultSet 查询后返回的结果集对象
     * }
     */
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    protected static ResultSet rs = null;

    /**
     * 编写创建数据库连接对象的方法(DriverManager)
     * @return Connection 连接对象
     * @throws ClassNotFoundException 驱动类未找到
     * @throws SQLException sql异常
     */
    public static Connection getConn() throws ClassNotFoundException, SQLException {
        return getConn( CommonConst.BIZ_SOURCE_MYSQL_URL_TEMP, CommonConst.BIZ_SOURCE_MYSQL_USER_NAME,
                CommonConst.BIZ_SOURCE_MYSQL_USER_PASS_WORD);
    }

    /**
     * getConn
     * @param url
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConn(String url) throws ClassNotFoundException, SQLException {
        return getConn(url, CommonConst.BIZ_SOURCE_MYSQL_USER_NAME,
                CommonConst.BIZ_SOURCE_MYSQL_USER_PASS_WORD);
    }

    /**
     * getConn
     * @param url
     * @param userName
     * @param password
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConn(String url,String userName,String password) throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DB_DRIVER);
        conn = DriverManager.getConnection(url,userName,password);
        logger.debug("=== 数据库配置信息如下: \n*** url:{} \n*** userName:{}",
                CommonConst.BIZ_SOURCE_MYSQL_URL,CommonConst.BIZ_SOURCE_MYSQL_USER_NAME);
        if (conn != null){
            logger.info(">>> DbUtils:getConn() 连接获取成功 conn!=null ...");
        }else{
            logger.info(">>> DbUtils:getConn() 连接获取失败 conn==null ...");
        }
        return conn;
    }

    /**
     * 编写关闭数据库释放资源的方法
     * @throws SQLException sql异常
     */
    public static void closeAll() throws SQLException {
        if (null != rs) {
            rs.close();
        }
        if (null != preparedStatement) {
            preparedStatement.close();
        }
        if (null != conn) {
            conn.close();
        }
    }

    /**
     * 编写数据库的查询方法
     * @param sql sql语句
     * @param params 传入的参数
     * @return
     * @throws SQLException sql异常
     */
    public static ResultSet executeSelect(String sql, Object[] params) throws SQLException {
        if (null != conn) {
            preparedStatement = conn.prepareStatement(sql);
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            rs = preparedStatement.executeQuery();
            return rs;
        } else {
            logger.warn("=== executeSelect conn is null,time= {} ===",new Date().toString());
            return null;
        }
    }

    /**
     * 执行语句是否异常
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int executeSql(String sql) throws SQLException {
        if (null != conn) {
            preparedStatement = conn.prepareStatement(sql);
            return preparedStatement.executeUpdate();
        } else {
            return 0;
        }
    }

    /**
     * 查询结果转换为list
     * @param resultSet
     * @return
     */
    public static List<Map<String, Object>> resultSetConvertList(ResultSet resultSet){
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            if ( null != resultSet){
                while (resultSet.next()){
                    Map<String,Object> rowData = new HashMap<>(2*columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(md.getColumnName(i), resultSet.getObject(i));
                    }
                    resultList.add(rowData);
                }
            }
        } catch (SQLException e) {
            logger.error("=== 转换异常:error:{} ===",e.getMessage());
        }
        return resultList;
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DbUtils.getConn();
        String sql ="select * from vsd_task ";
        ResultSet resultSet = DbUtils.executeSelect(sql, null);
        List<Map<String, Object>> mapList = resultSetConvertList(resultSet);
        logger.info(JSON.toJSONString(mapList));
        DbUtils.closeAll();
    }

}
