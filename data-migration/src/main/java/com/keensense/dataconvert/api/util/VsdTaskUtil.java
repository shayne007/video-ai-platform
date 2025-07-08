package com.keensense.dataconvert.api.util;

import com.keensense.dataconvert.api.util.db.DbUtils;
import com.keensense.dataconvert.api.util.http.HttpConnectPoolUtil;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.service.AppSysService;
import com.keensense.dataconvert.biz.service.ObjextResultService;
import com.keensense.dataconvert.biz.service.VlprResultService;
import com.keensense.dataconvert.framework.common.exception.SystemException;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.keensense.dataconvert.framework.common.utils.date.DateHelper;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.MapUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> VsdTaskUtil 任务迁移处理 </p>
 * @Author： - Jason
 * @CreatTime：2019/8/15 - 17:01
 * @Modify By：
 * @ModifyTime： 2019/8/15
 * @Modify marker：
 */
public class VsdTaskUtil {

    private static final Logger logger = LoggerFactory.getLogger(VsdTaskUtil.class);

    /**
     * redis 存入 serialNumber的 k,v关系
     */
    private static  RedisService redisService = SpringContextHolder.getBean(RedisService.class);

    /**
     * 查询出所有需要处理的表 
     */
    private static AppSysService appSysService = SpringContextHolder.getBean(AppSysService.class);

    /**
     * 更新数据
     */
    private static ObjextResultService objextResultService = SpringContextHolder.getBean(ObjextResultService.class);
    private static VlprResultService vlprResultService = SpringContextHolder.getBean(VlprResultService.class);

    /**
     * 一分钟后处理
     */
    private static long DELAY_DEAL_TIME = 3 * 1000L;

    /**
     * 开启任务迁移调用
     */
    public static void startDealVsdTask() {
        if (!CommonConst.SYS_IS_NEED_RE_ADD_TASK){
            logger.warn("[Mtd][startDealVsdTask]添加任务,修改状态功能已关闭 ...");
            return;
        }
        logger.info("[startDealVsdTask]开始初始化任务相关数据库数据...");
        // 加载serialNumber 和 userSerialNumber之间的关系 然后将关系update到老版本数据中
        loadSerialNumberRelation();
        // 调接口的形式添加任务到vsd_task表
        addVsdTask();
    }

    /**
     *  1.加载 数据库 serialNumber之间的关系
     */
    public static void loadSerialNumberRelation(){
        try {
            DbUtils.getConn(CommonConst.BIZ_SOURCE_MYSQL_URL);
            String querySql = "SELECT serialnumber,userserialnumber FROM ".concat(CommonConst.BIZ_SOURCE_MYSQL_TABLE_TEMP);
            ResultSet resultSet = DbUtils.executeSelect(querySql, null);
            redisService.del(RedisConstant.REDIS_SERIAL_NUMBER_MAP);
            if (null != resultSet) {
                HashMap<String, String> serialNumberMap = new HashMap<>(100);
                while (resultSet.next()) {
                    String serialNumber = resultSet.getString("serialnumber");
                    String userSerialNumber = resultSet.getString("userserialnumber");
                    if (!serialNumber.equalsIgnoreCase(userSerialNumber)){
                        serialNumberMap.put(userSerialNumber,serialNumber);
                    }
                }
                redisService.hmset(RedisConstant.REDIS_SERIAL_NUMBER_MAP,serialNumberMap);
                serialNumberMap.clear();
            }
            DbUtils.closeAll();
            //更新关系到原始数据里面
            updateSerialNumberRelation();
        }catch (Exception e) {
            logger.error("[退出程序]Please check your code,Restart dataConvert,error:{} ...",e);
            System.exit(0);
        }
    }


    /**
     * 2.更新所有数据 - 数据量较大
     * 将数据库里面所有的表的数据都全部更新一遍,升级到新的版本后,这些不需要在跑
     * [4.0.3.7 ---> 4.0.7.15]
     */
    public static void updateSerialNumberRelation(){
        logger.info("[Mtd]开始批量更新原始数据的SerialNumber间的绑定关系 ...");
        /**
         * 将K,V对应不起来的SerialNumber,更新到需要更新的表里面
         */
        try {
            Map<String,String> serialNumberMap = redisService.hgetAll(RedisConstant.REDIS_SERIAL_NUMBER_MAP);
            List<String> vlprTableNameList = appSysService.selectAllDataTables("vlpr_result");
            List<String> objextTableNameList = appSysService.selectAllDataTables("objext_result");

            if (ListUtil.isNotNull(objextTableNameList) && MapUtil.isNotNull(serialNumberMap)){
                HashMap<Object, Object> params;
                for (String objextTableStr : objextTableNameList) {
                    for (Map.Entry<String,String> entry : serialNumberMap.entrySet()) {
                        params = new HashMap<>();
                        params.put("ymdTableName",objextTableStr);
                        params.put("serialKey",entry.getKey());
                        params.put("serialValue",entry.getValue());
                        logger.info("[Update objext_result data]:{} ",params);
                        objextResultService.updateSerialNumberByMap(params);
                    }
                }
            }

            if (ListUtil.isNotNull(vlprTableNameList) && MapUtil.isNotNull(serialNumberMap)){
                HashMap<Object, Object> params;
                for (String vlprTableStr : vlprTableNameList) {
                    for (Map.Entry<String,String> entry : serialNumberMap.entrySet()) {
                        params = new HashMap<>();
                        params.put("ymdTableName",vlprTableStr);
                        params.put("serialKey",entry.getKey());
                        params.put("serialValue",entry.getValue());
                        logger.info("[Update vlpr_result data]:{} ",params);
                        vlprResultService.updateSerialNumberByMap(params);
                    }
                }
            }
        } catch (SystemException e) {
            throw  new SystemException(-1,"全部数据serialNumber转换异常,立即停止程序数据转换.");
        }
    }


    /**
     * 3.调用j_manager的添加任务的接口,数据从vsd_task_temp表迁移到vsd_task
     */
    public static  void addVsdTask(){
        List<JSONObject> paramsList = getDealVsdTaskList();
        if (ListUtil.isNotNull(paramsList)) {
            for (JSONObject params : paramsList) {
                try {
                    HttpClientResult httpClientResult = HttpConnectPoolUtil.doPostJson(CommonConst.BIZ_TASK_MANAGER_ADD_TASK_URL, params.toString());
                    logger.info("[接口响应数据]传入参数为:{}\nManager响应数据为:{} ", params.toString(), httpClientResult.getContent());
                    Thread.sleep(new Random().nextInt(10) * 20);
                } catch (Exception e) {
                    logger.error("== 添加任务处理异常:error:{} ==", e.getMessage());
                    System.exit(0);
                }
            }
            // 定时执行更新操作 因为任务进去是异步的所以定时2分钟后将vad_task任务置0，关闭状态，防止影响数据的迁移操作.
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.info("[定时2分钟]将Vsd_Task里面的任务置为关闭状态 ...");
                    // 将所有的任务关闭
                    updateAllVsdTask("0");
                }
            }, DELAY_DEAL_TIME);
        } else {
            logger.info("Please check your db config , retry ...");
            System.exit(0);
        }
    }


    /**
     * 4.获取封装所有的task相关的 请求参数,没有用mybatis形式，任务表变动比较大
     * @return 数据库的任务表信息
     */
    private static List<JSONObject> getDealVsdTaskList() {
        LinkedList<JSONObject> paramsList = new LinkedList<>();
        try {
            DbUtils.getConn();
            String querySql = "SELECT * FROM ".concat(CommonConst.BIZ_SOURCE_MYSQL_TABLE_TEMP);
            ResultSet resultSet = DbUtils.executeSelect(querySql, null);
            if (null != resultSet) {
                while (resultSet.next()) {
                    JSONObject params = new JSONObject();
                    String param = resultSet.getString("param");
                    JSONObject paramUrl = JSONObject.fromObject(param);
                    params.put("serialnumber", resultSet.getString("serialnumber"));
                    String url = paramUrl.getString("url");
                    if (StringUtils.isNotEmpty(url)) {
                        //ftp://chiwailam:abcd1234!@172.16.1.66/orgvideos/2286892221.mp4
                        if (url.startsWith("ftp://")) {
                            params.put("url", "ftp://admin:123456!@127.0.0.1/invalid/invalid.mp4");
                        } else {
                            params.put("url", url);
                        }
                    }
                    params.put("type", resultSet.getString("type"));
                    if (null != resultSet.getDate("entrytime")) {
                        params.put("entryTime", DateHelper.getTimeStampStr(resultSet.getDate("entrytime")));
                    }
                    paramsList.add(params);
                }
                DbUtils.closeAll();
            } else {
                logger.info("=== 暂无需要同步数据 ===");
            }
        } catch (Exception e) {
            logger.error("[mtd][getDealVsdTaskList]:error:{} ===", e.getMessage());
            System.exit(0);
        }
        return paramsList;
    }

    /**
     * 5. 更新定时任务
     *
     * @param status 定时任务状态 0 为无效 1为有效
     */
    public static void updateAllVsdTask(String status) {
        try {
            logger.info("[UpdateAllVsdTask]将所有迁移过去的任务置为关闭状态 ...");
            DbUtils.getConn(CommonConst.BIZ_SOURCE_MYSQL_URL);
            String updateTaskStatusSql = "UPDATE ".concat(CommonConst.BIZ_SOURCE_MYSQL_TABLE).concat(" SET isvalid = ").concat(status);
            String updateRtspSql = "UPDATE `camera` SET `cameratype`='2' WHERE url like 'rtsp://%';";
            String updateVasSql = "UPDATE `camera` SET `cameratype`='1' WHERE url like 'vas://%';";
            // 将离线任务状态置为完成
            String updateOfflineSql = "UPDATE `vsd_task` SET `status` = 2, progress = 100 WHERE param LIKE '%ftp://%';";
            // 停止所有的实时任务
            String updateRealTimeTask = "UPDATE tb_analysis_task SET `status` = 1 WHERE analy_param LIKE '%vas://%' OR analy_param LIKE '%rtsp://%';";
            DbUtils.executeSql(updateRtspSql);
            DbUtils.executeSql(updateVasSql);
            DbUtils.executeSql(updateOfflineSql);
            DbUtils.executeSql(updateRealTimeTask);
            int count = DbUtils.executeSql(updateTaskStatusSql);
            logger.info("[UpdateAllVsdTask]更新数量:[{}],Sql:[{}] ...", count, updateTaskStatusSql);
            DbUtils.closeAll();
        } catch (Exception e) {
            logger.error("[mtd]updateAllVsdTask:将所有的任务置为关闭异常error:{} ==", e);
            System.exit(0);
        }
    }

}
