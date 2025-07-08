package com.keensense.dataconvert.biz.task.callable;

import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.common.consts.EsConst;
import com.keensense.dataconvert.biz.common.enums.ResultEnum;
import com.keensense.dataconvert.biz.entity.ObjextResult;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.biz.task.vo.TaskResultInfo;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.task.callable
 * @Description： <p> ObjextResultCollectCall </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 17:06
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class ObjextResultCollectCall implements Callable<TaskResultInfo> {

    private static Logger logger = LoggerFactory.getLogger(ObjextResultCollectCall.class);

    private ObjextResult objextResult;

    /**
     * 当前处理的数据归属的 table表
     */
    private String collectTableYmdStr;

    TaskResultInfo taskResultInfo;

    private static ElasticSearchService refreshEsService = SpringContextHolder.getBean("refreshEsService");

    /**
     * 多线程处理...  T submit 结果集返回
     * 异常的放到队列处理 -- 超过次数不再处理
     * Biz: 1.将数据推送到 es
     *      2.将url调用识别接口
     *      3.将特征值推送到kafka
     * @return
     * @throws Exception
     */
    @Override
    public TaskResultInfo call() throws Exception {
        Short type = objextResult.getObjtype();
        taskResultInfo = new TaskResultInfo();
        logger.info("=== 同步ObjextResult:[{}]到ElasticSearch中 ...",JSON.toJSONString(objextResult));
        if (CommonConst.OBJECT_TYPE_BIKE == type){
            try {
                refreshEsService.documentCreate(EsConst.ES_BIKE_RESULT_INDEX_NAME,EsConst.ES_BIKE_RESULT_INDEX_TYPE,
                        objextResult.getSerialnumber(),JSON.toJSONString(objextResult));
            } catch (Exception e) {
                logger.error("== mysql[BIKE-OBJECT]存入es异常:{} ==",e.getMessage());
                taskResultInfo.setCollectCallCode(ResultEnum.BIZ_MYSQL_PUSH_ES_ERROR.getResultCode());
            }
        }else if (CommonConst.OBJECT_TYPE_OBJEXT == type ){
            try {
                refreshEsService.documentCreate(EsConst.ES_OBJEXT_RESULT_INDEX_NAME,EsConst.ES_OBJEXT_RESULT_INDEX_TYPE,
                        objextResult.getSerialnumber(),JSON.toJSONString(objextResult));
            } catch (Exception e) {
                logger.error("== mysql[Objext-OBJECT]存入es异常:{} ==",e.getMessage());
                taskResultInfo.setCollectCallCode(ResultEnum.BIZ_MYSQL_PUSH_ES_ERROR.getResultCode());
            }
        }
        taskResultInfo.setTaskResult(objextResult);
        taskResultInfo.setCollectTableYmdStr(collectTableYmdStr);
        return taskResultInfo;
    }


    /**
     * 当前数据归属的 tableYmd
     * @param objextResult
     * @param collectTableYmdStr
     */
    public ObjextResultCollectCall(ObjextResult objextResult, String collectTableYmdStr) {
        this.objextResult = objextResult;
        this.collectTableYmdStr = collectTableYmdStr;
    }
}
