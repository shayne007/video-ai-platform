package com.keensense.extension.constants;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.common.util.DateUtil;
import com.keensense.extension.entity.LibraryInfo;
import com.keensense.extension.mapper.LibraryMapper;

import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * @Description: 底库常量类 主要提供底库枚举值、数据库操作、缓存底库对象操作
 * @Author: kellen
 * @CreateDate: 2019/2/26 11:43
 * @Version: 1.0
 */
public class LibraryConstant {

    /** 底库类型 1-人脸 2-人形(正面) 3-人形(侧面) 4-人形(背面)5-人脸（第三方档案）  */
    public static final int LIBRARY_TYPE_FACE = 1;
    private static final int LIBRARY_TYPE_BODY_FRONT = 2;
    private static final int LIBRARY_TYPE_BODY_SIDE = 3;
    private static final int LIBRARY_TYPE_BODY_BACK = 4;
    public static final int LIBRARY_TYPE_FACE_EXT = 5;

    /**人形角度类型 0-未知 128-正面 256-侧面 512-背面*/
    private static final int BOYD_ANGLE_UNKOWN = 0;
    public static final int BOYD_ANGLE_FRONT = 128;
    public static final int BOYD_ANGLE_SIDE = 256;
    public static final int BOYD_ANGLE_BACK = 512;
    /**搜图返回结果最大值*/
    public static final int SEARCH_MAX_RESULT_COUNT = 3000;

    /**人形角度类型字符串*/
    private static final String BOYD_ANGLE_UNKOWN_STR = "unkown";
    private static final String BOYD_ANGLE_FRONT_STR = "front";
    private static final String BOYD_ANGLE_SIDE_STR = "side";
    private static final String BOYD_ANGLE_BACK_STR = "back";

    private static final String LIBRARY_TYPE = "library_type";

    private static final Integer[] LIBRARY_TYPE_BODY = {LIBRARY_TYPE_BODY_FRONT,LIBRARY_TYPE_BODY_SIDE,LIBRARY_TYPE_BODY_BACK};
    /**人脸底库缓存*/
    private static LibraryInfo faceLibraryCache = null;
    /**人脸ext底库缓存*/
    private static LibraryInfo faceExtLibraryCache = null;
    /**人形底库缓存*/
    private static Map<String,LibraryInfo> bodyLibraryCache = new HashMap<>();

    private static LibraryMapper libraryMapper = SpringContext.getBean(LibraryMapper.class);

    public static final int KS_TYPE = 1;
    public static final int GL_TYPE = 2;
    public static final int GLQST_TYPE = 3;
    public static final int STQST_TYPE = 4;

    private LibraryConstant(){}

    /***
     * @description: 无穷大时间 253401407999000对应 9999-12-59 23:59:59
     * @return:
     */
    public static Date getInfintyDate(){
        return new Date(253401407999000L);
    }
    /***
     * @description: 无穷大时间 253401407999000对应 9999-12-59 23:59:59
     * @return:
     */
    public static Integer[] getLibraryTypeBody(){
        return LIBRARY_TYPE_BODY;
    }
    /**
     * 根据底库类型、时间获取使用中的人形底库
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param type 底库类型
     * @return 过期底库
     * */
    public static List<LibraryInfo> getBodyLibraryByDate(int type, Date startDate,Date endDate){
        List<LibraryInfo> list = libraryMapper.selectList(new QueryWrapper<LibraryInfo>().eq(LIBRARY_TYPE,type)
        .le("preset_start_time",startDate).ge("preset_end_time",endDate));
        return CollectionUtils.isNotEmpty(list)?list:null;
    }

    /**
     * 删除过期的人形底库
     * @param invalidDate 过期时间
     * @return 过期底库
     * */
    public static List<LibraryInfo> getInvalidBodyLibraryByType(Date invalidDate){
        List<LibraryInfo> list = libraryMapper.selectList(new QueryWrapper<LibraryInfo>()
            .in(LIBRARY_TYPE,LIBRARY_TYPE_BODY)
            .le("preset_end_time",invalidDate));
        return CollectionUtils.isNotEmpty(list)?list:null;
    }

    /**
     * 根据底库类型获取底库
     * @param type 底库类型
     * @return 过期底库
     * */
    public static List<LibraryInfo> getLibraryByType(int type){
        List<LibraryInfo> list = libraryMapper.selectList(new QueryWrapper<LibraryInfo>()
            .eq(LIBRARY_TYPE,type));
        return CollectionUtils.isNotEmpty(list)?list:null;
    }

    /**
     * 根据ID删除底库数据
     * @param id id
     * @return 删除数量
     * */
    public static boolean deleteLibraryById(String id){
        int rslt = libraryMapper.deleteById(id);
        return rslt>0;
    }

    /**
     * 获取任性库缓存key值
     * @param type 人形库类型
     * @param date 时间(哪一天的缓存)
     * @return 缓存key值
     * */
    public static String getBodyLibraryCacheKey(int type,Date date){
        return type+"_"+ DateUtil.formatDate(date, "yyyyMMdd");
    }

    /**
     * 根据ID获取人形库的缓存对象
     * @param key key
     * @return 底库对象，可能为null
     * */
    public static LibraryInfo getBodyLibraryByKey(String key){
        return LibraryConstant.bodyLibraryCache.get(key);
    }

    /**
     * 根据angle获取当天的缓存对象
     * @param angle 角度
     * @return 底库对象，可能为null
     * */
    public static LibraryInfo getBodyLibraryByAngle(int angle,Date date){
        return getBodyLibraryByKey(getBodyLibraryCacheKey(getBodyTypeByAngle(angle),date));
    }

    /**
     * 根据人形角度获取地库ID
     * @param angle 人形角度
     * @return 缓存底库的key
     * */
    public static String getLibraryIdByAngle(int angle){
        return getBodyLibraryCacheKey(getBodyTypeByAngle(angle), new Date());
    }

    /**
     * 根据人形角度获取类型
     * @param angle 人形角度
     * @return 人形类型
     * */
    private static int getBodyTypeByAngle(int angle){
        int rslt;
        switch (angle){
            case BOYD_ANGLE_FRONT :
                rslt = LIBRARY_TYPE_BODY_FRONT;
                break;
            case BOYD_ANGLE_SIDE :
                rslt = LIBRARY_TYPE_BODY_SIDE;
                break;
            case BOYD_ANGLE_BACK :
                rslt = LIBRARY_TYPE_BODY_BACK;
                break;
            default:
                rslt = BOYD_ANGLE_UNKOWN;
                break;
        }
        return rslt;
    }

    /**
     * 根据人形角度获取类型字符串
     * @param angle 人形角度
     * @return 人形类型
     * */
    public static String getBodyTypeStrByAngle(int angle){
        switch (angle){
            case BOYD_ANGLE_FRONT :
                return BOYD_ANGLE_FRONT_STR;
            case BOYD_ANGLE_SIDE :
                return BOYD_ANGLE_SIDE_STR;
            case BOYD_ANGLE_BACK :
                return BOYD_ANGLE_BACK_STR;
            default:
                return BOYD_ANGLE_UNKOWN_STR;
        }
    }

    /**
     * 根据库类型获取人形角度
     * @param type 人形类型
     * @return 人形类型
     * */
    public static int getAngleByBodyType(int type){
        switch (type){
            case LIBRARY_TYPE_BODY_FRONT :
                return BOYD_ANGLE_FRONT;
            case LIBRARY_TYPE_BODY_SIDE :
                return BOYD_ANGLE_SIDE;
            case LIBRARY_TYPE_BODY_BACK :
                return BOYD_ANGLE_BACK;
            default:
                return BOYD_ANGLE_UNKOWN;
        }
    }

    public static void setBodyLibraryCache(Map<String, LibraryInfo> bodyLibraryCache) {
        LibraryConstant.bodyLibraryCache = bodyLibraryCache;
    }

    public static Map<String, LibraryInfo> getBodyLibraryCache() {
        return bodyLibraryCache;
    }

    public static LibraryInfo getFaceLibraryCache() {
        return faceLibraryCache;
    }
    
    public static LibraryInfo getFaceExtLibraryCache() {
    	return faceExtLibraryCache;
    }

    public static void setFaceLibraryCache(LibraryInfo faceLibraryCache) {
        LibraryConstant.faceLibraryCache = faceLibraryCache;
    }
    
    public static void setFaceExtLibraryCache(LibraryInfo faceExtLibraryCache) {
    	LibraryConstant.faceExtLibraryCache = faceExtLibraryCache;
    }

}
