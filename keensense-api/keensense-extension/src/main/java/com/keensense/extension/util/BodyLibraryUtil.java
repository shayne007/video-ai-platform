package com.keensense.extension.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.extension.constants.LibraryConstant;
import com.keensense.extension.entity.ArchivesBodyInfo;
import com.keensense.extension.entity.LibraryInfo;
import com.keensense.extension.mapper.ArchivesBodyInfoMapper;
import com.keensense.extension.mapper.LibraryMapper;
import com.keensense.sdk.constants.BodyConstant;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

/**
 * @Description: 人形底库工具类
 * @Author: kellen
 * @CreateDate: 2019/2/27 10:46
 * @Version: 1.0
 */
@Slf4j
public class BodyLibraryUtil {

    private static final int CODE_FAIL = -1;
    private static LibraryMapper libraryMapper = SpringContext.getBean(LibraryMapper.class);
    private static ArchivesBodyInfoMapper archivesBodyInfoMapper = SpringContext.getBean(ArchivesBodyInfoMapper.class);
    private static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private BodyLibraryUtil(){}
    /**
     * 判断人形底库是否存在，不存在则创建
     * */
    public static void isExistBodyLibrary(){
        isExistBodyLibrarybyDate(new Date());
        isExistBodyLibrarybyDate(DateUtil.getNextDay());
    }

    /**
     * 更新人形底库缓存
     * */
    public static void updateBodyLibraryCache(){
        Map<String,LibraryInfo> bodyLibraryCache = getBodyLibraryForTodayTomorrow();
        LibraryConstant.setBodyLibraryCache(bodyLibraryCache);
    }

    /**
     * 删除过期的人形底库
     * */
    public static void deleteInvalidBodyLibrary(){
        //获取今天00:00:00的时间点
        Date date = DateUtil.getFirstSecondInDay(new Date());
        List<LibraryInfo> libraryInfoList = LibraryConstant.getInvalidBodyLibraryByType(date);
        if(CollectionUtils.isNotEmpty(libraryInfoList)){
            for(LibraryInfo libraryInfo : libraryInfoList){
                deleteBodyLibraryDateType(libraryInfo.getId(),date,libraryInfo.getLibraryType());
            }
        }
    }

    /**
     * 根据时间和类型判断人形底库是否存在，不存在则创建
     * @param date 时间
     * */
    private static void isExistBodyLibrarybyDate(Date date){
        Date startDate = DateUtil.getFirstSecondInDay(date);
        Date endDate = DateUtil.getLastSecondInDay(date);
        List<LibraryInfo> list;
        for(Integer type : LibraryConstant.getLibraryTypeBody()){
            list = LibraryConstant.getBodyLibraryByDate(type, startDate, endDate);
            if(CollectionUtils.isEmpty(list)){
                createBodyLibrary(type,startDate,endDate,date);
            }
        }
    }

    /**
     * 获取第二天的人形库缓存
     * @param bodyLibraryCache 人形底库缓存Map
     * @return 人形库缓存
     * */
    private static Map<String,LibraryInfo> getBodyLibraryForTomorrow(Map<String,LibraryInfo> bodyLibraryCache){
        Date date = DateUtil.getNextDay();
        Date beginTime = DateUtil.getFirstSecondInDay(date);
        Date endTime = DateUtil.getLastSecondInDay(date);
        List<LibraryInfo> bodyLibrarylist;
        for(Integer type : LibraryConstant.getLibraryTypeBody()){
            bodyLibrarylist = LibraryConstant.getBodyLibraryByDate(type, beginTime, endTime);
            String key = LibraryConstant.getBodyLibraryCacheKey(type, date);
            /*获取到数据随机获取一个底库作为缓存，其余删除*/
            if(bodyLibrarylist.size() > 0){
                bodyLibraryCache.put(key, clearBodyLibrary(bodyLibrarylist));
            }else{
                log.error("can not get body library from mysql with type = " + type
                        + " and presetStartTime = "+ DateUtil.getFormat(beginTime, DATA_FORMAT)
                        + " and presetEndTime = " + DateUtil.getFormat(endTime, DATA_FORMAT));
            }
        }
        return bodyLibraryCache;
    }

    /**
     * 获取当天的人形库缓存
     * @return 今天的人形缓存库
     * */
    private static Map<String,LibraryInfo> getBodyLibraryForTodayTomorrow(){
        Map<String,LibraryInfo> bodyLibraryToday = new HashMap<>();
        Date date = new Date();
        Date beginTime = DateUtil.getFirstSecondInDay(date);
        Date endTime = DateUtil.getLastSecondInDay(date);
        List<LibraryInfo> bodyLibrarylist;
        for(Integer type : LibraryConstant.getLibraryTypeBody()){
            bodyLibrarylist = LibraryConstant.getBodyLibraryByDate(type, beginTime, endTime);
            String key = LibraryConstant.getBodyLibraryCacheKey(type, date);
            if(bodyLibrarylist.size() == 1){        //只有一个人形库时
                LibraryInfo libraryInfo = bodyLibrarylist.get(0);
                bodyLibraryToday.put(key, libraryInfo);
            }else if(bodyLibrarylist.size() > 1){       //多个人形库时
                //缓存中有人形库A，则判断查询结果中是否包含A,则缓存继续使用A，如果不包含随机返回一个人形库，删除其他记录
                if(LibraryConstant.getBodyLibraryByKey(key) != null){
                    LibraryInfo libraryCache = LibraryConstant.getBodyLibraryByKey(key);
                    bodyLibraryToday.put(key, clearBodyLibrary(bodyLibrarylist,libraryCache.getId()));
                }else{  //缓存中不存在人形底库，随机获取一个底库存入缓存，其他删除
                    bodyLibraryToday.put(key, clearBodyLibrary(bodyLibrarylist));
                }
            }else{
                log.error("can not get body library from mysql with type = " + type
                        + " and presetStartTime = "+ DateUtil.getFormat(beginTime, DATA_FORMAT)
                        + " and presetEndTime = " + DateUtil.getFormat(endTime, DATA_FORMAT));
            }
        }
        getBodyLibraryForTomorrow(bodyLibraryToday);
        return bodyLibraryToday;
    }

    /**
     * 清理人形库数据，匹配制定ID的缓存库，返回缓存对象
     * @param bodyLibraryList 数据库中人脸库列表
     * @param id 人脸库ID
     * @return 缓存库对象
     * */
    private static LibraryInfo clearBodyLibrary(List<LibraryInfo> bodyLibraryList,String id){
        for(int i = 0 ; i < bodyLibraryList.size() ; i++){
            if(bodyLibraryList.get(i).getId().equals(id)){
                LibraryInfo libraryInfo = bodyLibraryList.get(i);
                bodyLibraryList.remove(i);
                clearBodyLibrary(bodyLibraryList);
                return libraryInfo;
            }
        }
        return clearBodyLibrary(bodyLibraryList);
    }

    /**
     * 清理人形库数据，随机返回一个人脸库
     * @param bodyLibraryList 数据库中人脸库列表
     * @return 缓存库对象
     * */
    private static LibraryInfo clearBodyLibrary(List<LibraryInfo> bodyLibraryList){
        LibraryInfo libraryInfo = bodyLibraryList.get(0);
        bodyLibraryList.remove(0);
        deletebodyLibrary(bodyLibraryList);
        return libraryInfo;
    }

    /**
     * 删除人形库列表
     * @param faceLibraryList 需要清理的列表
     * @return 缓存库对象
     * */
    private static void deletebodyLibrary(List<LibraryInfo> faceLibraryList){
        for (LibraryInfo libraryInfo: faceLibraryList) {
            deleteBodyLibraryById(libraryInfo.getId());
        }
    }

    /**
     * 删除人形底库
     * @param repoId 底库ID
     * @param type 
     * */
    private static void deleteBodyLibraryDateType(String repoId,Date date, Integer type){
        LibraryUtil.deleteLibraryById(repoId);
        archivesBodyInfoMapper.delete(new QueryWrapper<ArchivesBodyInfo>()
            .eq("angle",LibraryConstant.getAngleByBodyType(type))
            .le(false,"create_time",date));
    }
    
    /**
     * 删除人形底库
     * @param repoId 底库ID
     * */
    private static void deleteBodyLibraryById(String repoId){
    	Var resultVar = Var.fromJson(BodyConstant.getBodySdkInvoke().deleteRegistLib(repoId));
    	
    	if(resultVar != null && resultVar.getInt("code") == CODE_FAIL){
    		log.error("library SDK error! delete body library failed, id = " + repoId);
    	}else{
    		LibraryUtil.deleteLibraryById(repoId);
    	}
    }

    /**
     * 创建人形底库
     * */
    private static void createBodyLibrary(int type, Date startDate, Date endDate, Date date){
        String regId = createBodyLibrary();
        if(StringUtil.isNotNull(regId)){
            LibraryInfo libraryInfo = new LibraryInfo(regId,type,startDate,endDate,date);
            if(libraryMapper.insert(libraryInfo)<=0){
                log.error(" add body library to mysql error !");
            }
        }else{
            log.error(" create body library error with type = " + type);
        }
    }

    /**
     * 创建人形底库
     * @return 底库ID
     * */
    public static String createBodyLibrary(){
        return BodyConstant.getBodySdkInvoke().createRegistLib();
    }

    /**
     * 创建人形底库
     * @param repId 底库ID
     * */
    public static void createBodyLibrary(String repId){
        BodyConstant.getBodySdkInvoke().createRegistLib(repId);
    }

    /**
     * 删除人形底库
     * @param repId 底库ID
     * */
    public static boolean deleteLibrary(String repId){
    	Var resultVar = Var.fromJson(BodyConstant.getBodySdkInvoke().deleteRegistLib(repId));
        if(resultVar!= null &&resultVar.getInt("code") == CODE_FAIL){
            log.error("delete library failed, id = " + repId + " , sdkResult code = " + resultVar.getInt("code"));
            return false;
        }
        return true;
    }

}
