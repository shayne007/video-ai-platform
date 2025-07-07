package com.keensense.search.constant;

import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.ByteUtil;

public class VlprCommonConst {
    private VlprCommonConst(){}

    /**车辆识别卡口状态 0-等待处理 1-成功 2-下载失败 3-结构化失败 4-未提取到任务车辆特征 5-保存或者推送至Kafka失败 9-处理中*/
    public static final int VLPR_RECODE_STATUS_WAIT = 0;
    public static final int VLPR_RECODE_STATUS_SUCCESS = 1;
    public static final int VLPR_RECODE_STATUS_DOWNLOAD_FAIL = 2;
    public static final int VLPR_RECODE_STATUS_OBJECT_FAIL = 3;
    public static final int VLPR_RECODE_STATUS_RESULT_FAIL = 4;
    public static final int VLPR_RECODE_STATUS_SAVE_FAIL = 5;
    public static final int VLPR_RECODE_STATUS_PROCESS = 9;
    /**最大下载次数*/
    public static final int MAX_DOWNLOAD_COUNT = 3;
    /**从GSTL获取特征添加固定头*/
    private static final byte[] GSTL = {'G','L','S','T'};
    private static final byte[] timestamp = {0,0,0,0,0,0,0,0};
    private static final byte[] version =  ByteUtil.int2ByteLow(143,4);

    public static String getGlstFeature(String feature){
        byte[] featureByte = Base64.decode(feature.getBytes());
        byte[] result = new byte[featureByte.length+version.length+GSTL.length+timestamp.length];
        System.arraycopy(GSTL,0,result,0,GSTL.length);
        System.arraycopy(version,0,result,GSTL.length,version.length);
        System.arraycopy(timestamp,0,result,GSTL.length+version.length,timestamp.length);
        System.arraycopy(featureByte,0,result,GSTL.length+version.length+timestamp.length,featureByte.length);
        return new String(Base64.encode(result));
    }

    public static final boolean VLPR_SWITCH = true;
    public static final int VLPR_PIC_BATCH = 8;       //一批处理最大图片张数
    public static final int DOWNLOAD_THREAD_CLIENT = 5;       //下载线程数
    public static final int DOWNLOAD_READ_TIMEOUT = 10;       //下载超时时间(单位：秒),最少5秒
    public static final int DOWNLOAD_QUEUE_CAPACITY = 400;    //下载队列数
    public static final int RECOG_RESULT_DEAL_THREAD = 5;     //结果处理线程数
    public static final int RECOG_OFFLINE_IMAGE_THREAD = 5;   //VIP通道线程数
    public static final int DISTRIBUTE_MACHINE_NUMBER = 0;    //集群机器编号

    /**
     *  gstl车辆类型转换成我们自己的类型
     *  @param type gstl车牌类型
     * */
    public static Integer getVehicleTypeByGstl(Integer type){
        switch (type){
            case 0: return 1;
            case 3: return 2;
            case 4: return 3;
            case 11: return 4;
            case 10: return 4;
            case 13: return 6;
            case 12: return 6;
            case 14: return 6;
            case 15: return 6;
            case 5: return 7;
            case 6:  return 8;
            case 7: return 9;
            case 8: return 9;
            case 9: return 9;
            case 1:  return 10;
            case 2:  return 11;
            case 16:  return 99;
            case 17:  return 99;
            case 18:  return 99;
            case 19:  return 99;
            default: return -1;
        }
    }

    public static String getVehicleNameByType(Integer type){
        switch (type){
            case 1: return "轿车";
            case 2: return "越野车";
            case 3: return "商务车";
            case 4: return "小型货车";
            case 5: return "中型货车";
            case 6: return "大型货车";
            case 7: return "轻客";
            case 8: return "中型客车";
            case 9: return "大型客车";
            case 10: return "面包车";
            case 11:  return "皮卡";
            case 12: return "其他（专用车）";
            default: return "未知";
        }
    }

    /**
     *  gstl车牌类型转换成我们自己的类型
     *  @param type gstl车牌类型
     * */
    public static Integer getPlateTypeByGstl(Integer type){
        switch (type){
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 5: return 4;
            case 6: return 5;
            case 8: return 6;
            case 9: return 6;
            case 10: return 7;
            case 11: return 8;
            case 12: return 9;
            case 4:  return 9;
            case 1000: return 10;
            case 13: return 11;
            case 14: return 12;
            case 7:  return 99;
            default: return -1;
        }
    }

    public static String getPlateNameByType(Integer type){
        switch (type){
            case 1: return "普通蓝牌";
            case 2: return "普通黑牌";
            case 3: return "普通黄牌";
            case 4: return "警车车牌";
            case 5: return "武警车牌";
            case 6: return "军队车牌";
            case 7: return "使馆车牌";
            case 8: return "港澳车牌";
            case 9: return "农用车牌（农用绿牌，农用黄牌）";
            case 10: return "驾校车牌";
            case 11:  return "小型新能源汽车";
            case 12: return "大型新能源汽车";
            case 99:  return "其他车牌";
            default: return "未知";
        }
    }
}
