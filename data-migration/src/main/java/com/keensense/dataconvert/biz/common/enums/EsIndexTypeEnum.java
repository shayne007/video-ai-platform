package com.keensense.dataconvert.biz.common.enums;

import com.keensense.dataconvert.biz.common.consts.EsConst;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.common.enums
 * @Description： <p> EsIndexTypeEnum - es库- 类型枚举 1 ＝ ⼈人 ｜ 2 ＝⾞車車 ｜ 3 ＝⼈人臉 ｜ 4 ＝⼈人騎⾞車車 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/29 - 15:52
 * @Modify By：
 * @ModifyTime： 2019/7/29
 * @Modify marker：
 */
public enum EsIndexTypeEnum {


    /**
     * vlpr 车辆类型
     */
    VLPR(2, EsConst.ES_VLPR_RESULT_INDEX_NAME,EsConst.ES_VLPR_RESULT_INDEX_TYPE),

    /**
     * OBJEXT
     */
    OBJEXT(1, EsConst.ES_OBJEXT_RESULT_INDEX_NAME,EsConst.ES_OBJEXT_RESULT_INDEX_TYPE),

    /**
     * BIKE
     */
    BIKE(4, EsConst.ES_BIKE_RESULT_INDEX_NAME,EsConst.ES_BIKE_RESULT_INDEX_TYPE);


    /**
     * 类型   bike:4 vlpr:2 objext:1
     */
    private int objType;

    /**
     * 索引name
     */
    private String indexName;

    /**
     * 索引type
     */
    private String indexType;

    /**
     * 枚举构造
     * @param objType
     * @param indexName
     * @param indexType
     */
    EsIndexTypeEnum(int objType, String indexName, String indexType) {
        this.objType = objType;
        this.indexName = indexName;
        this.indexType = indexType;
    }


    /**
     * 通过indexName 查询code
     * @param indexName
     * @return
     */
    public static int getTypeByIndexName(String indexName){
        for (EsIndexTypeEnum ele : values()) {
            if(ele.getIndexName().equals(indexName)) {
                return ele.getObjType();
            }
        }
        return -1;
    }

    /**
     * 通过类型查询indexName
     * @param objType
     * @return
     */
    public static String getIndexNameByObjType(int objType){
        for (EsIndexTypeEnum ele : values()) {
            if(ele.getObjType() == objType) {
                return ele.getIndexName();
            }
        }
        return null;
    }

    /**
     * 通过类型 查询indexType
     * @param objType
     * @return
     */
    public static String getIndexTypeByObjType(int objType){
        for (EsIndexTypeEnum ele : values()) {
            if(ele.getObjType() == objType) {
                return ele.getIndexType();
            }
        }
        return null;
    }



    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

}
