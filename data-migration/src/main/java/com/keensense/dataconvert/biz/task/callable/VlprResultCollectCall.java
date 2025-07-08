package com.keensense.dataconvert.biz.task.callable;

import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.biz.common.consts.EsConst;
import com.keensense.dataconvert.biz.common.enums.ResultEnum;
import com.keensense.dataconvert.biz.entity.VlprResult;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.biz.task.vo.TaskResultInfo;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.task.callable
 * @Description： <p> VlprResultCollectCall  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 16:58
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class VlprResultCollectCall implements Callable<TaskResultInfo> {

    private static Logger logger = LoggerFactory.getLogger(VlprResultCollectCall.class);

    private VlprResult vlprResult;

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
        taskResultInfo = new TaskResultInfo();
        logger.info("=== 同步VlprResult:[{}]到ElasticSearch中 ...",JSON.toJSONString(vlprResult));
        try {
            refreshEsService.documentCreate(EsConst.ES_VLPR_RESULT_INDEX_NAME,EsConst.ES_VLPR_RESULT_INDEX_TYPE,
                    vlprResult.getRecogId(), JSON.toJSONString(vlprResult));
        } catch (Exception e) {
            logger.error("== mysql[Vlpr]存入es异常:{} ==",e.getMessage());
            taskResultInfo.setCollectCallCode(ResultEnum.BIZ_MYSQL_PUSH_ES_ERROR.getResultCode());
        }
        taskResultInfo.setTaskResult(vlprResult);
        taskResultInfo.setCollectTableYmdStr(collectTableYmdStr);
        return taskResultInfo;
    }

    /**
     * 当前数据归属的 tableYmd
     * @param vlprResult
     * @param collectTableYmdStr
     */
    public VlprResultCollectCall(VlprResult vlprResult, String collectTableYmdStr) {
        this.vlprResult = vlprResult;
        this.collectTableYmdStr = collectTableYmdStr;
    }
}
