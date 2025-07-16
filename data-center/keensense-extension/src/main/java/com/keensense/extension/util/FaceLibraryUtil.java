package com.keensense.extension.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.extension.config.NacosConfig;
import com.keensense.extension.constants.LibraryConstant;
import com.keensense.extension.entity.ArchivesTaskLib;
import com.keensense.extension.entity.LibraryInfo;
import com.keensense.extension.mapper.LibraryMapper;
import com.keensense.extension.service.IArchivesTaskLibService;
import com.keensense.extension.service.impl.ArchivesTaskLibServiceImpl;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.algorithm.impl.GlstFaceSdkInvokeImpl;
import com.keensense.sdk.constants.FaceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


/**
 * @Description: 人脸底库工具类
 * @Author: kellen
 * @CreateDate: 2019/2/27 10:45
 * @Version: 1.0
 */
@Slf4j
public class FaceLibraryUtil {

    private static final int CODE_FAIL = -1;

    private FaceLibraryUtil(){}
    /**
     * 判断人脸底库是否存在，不存在则创建
     * */
    public static void isExistFaceLibrary(){
    	isExistFaceLibrary(LibraryConstant.LIBRARY_TYPE_FACE);
//    	isExistFaceLibrary(LibraryConstant.LIBRARY_TYPE_FACE_EXT);
    }

    private static void isExistFaceLibrary(int faceType) {
    	 List<LibraryInfo> faceLibraryList = LibraryConstant.getLibraryByType(faceType);
        LibraryMapper libraryMapper = SpringContext.getBean(LibraryMapper.class);
        if(CollectionUtils.isEmpty(faceLibraryList)){ //未查找到数据则创建记录
            createLib(faceType,libraryMapper);
         }else{
             for(LibraryInfo faceInfo :faceLibraryList){
                String libId = faceInfo.getId();
                 try {
                     String id = FaceConstant.getFaceSdkInvoke().getRegistLib(libId);
                     if(StringUtils.isBlank(id) || !id.equals(libId)){
                         libraryMapper.deleteById(libId);
                         createLib(faceType,libraryMapper);
                     }
                 }catch (Exception e){
                     log.error("isExistFaceLibrary error",e);
                     if(StringUtils.isNotBlank(libId)){
                         libraryMapper.deleteById(libId);
                         createLib(faceType,libraryMapper);
                     }
                 }
             }
         }
    }

    private static  void createLib(int faceType,LibraryMapper libraryMapper){
        String regId = createFaceLibrary();
        if(StringUtils.isNotEmpty(regId)){
            Date date = new Date();
//            LibraryInfo libraryInfo = new LibraryInfo(regId,faceType
//                ,DateUtil.getFirstSecondInDay(date),LibraryConstant.getInfintyDate(),date);
//            if(libraryMapper.insert(libraryInfo)<=0){
//                log.error(" add face library to mysql error !faceType="+faceType);
//            }
        }else{
            log.error(" create face library error !faceType="+faceType);
        }
    }

    /**
     * 更新人脸底库缓存
     * */
    public static void updateFaceLibraryCache(){

    	updateFaceLibraryCache(LibraryConstant.LIBRARY_TYPE_FACE);
//    	updateFaceLibraryCache(LibraryConstant.LIBRARY_TYPE_FACE_EXT);
    }

    public static void updateFaceLibraryCache(int faceType){

        List<LibraryInfo> faceLibraryList = LibraryConstant.getLibraryByType(faceType);
        //只有一个人脸库时
        if(faceLibraryList.size() == 1){

            LibraryConstant.setFaceLibraryCache(faceLibraryList.get(0));
            //多个人脸库时
        }else if(faceLibraryList.size() > 1){

            //缓存中有人脸库A，则判断查询结果中是否包含A,则缓存继续使用A，如果不包含随机返回一个人脸库，删除其他记录
            if(LibraryConstant.getFaceLibraryCache() != null){
                LibraryInfo libraryCache = LibraryConstant.getFaceLibraryCache();
                LibraryConstant.setFaceLibraryCache(clearFaceLibrary(faceLibraryList,libraryCache.getId()));
            }else {  //缓存中不存在人脸底库，随机获取一个底库存入缓存，其他删除
                LibraryConstant.setFaceLibraryCache(clearFaceLibrary(faceLibraryList));
            }
        }else{
            log.error("can not get face library from mysql!faceType="+faceType);
        }



    }

    /**
     * 清理人脸库数据，匹配制定ID的缓存库，返回缓存对象
     * @param faceLibraryList 数据库中人脸库列表
     * @param id 人脸库ID
     * @return 缓存库对象
     * */
    private static LibraryInfo clearFaceLibrary(List<LibraryInfo> faceLibraryList,String id){
        for(int i = 0 ; i < faceLibraryList.size() ; i++){
            if(faceLibraryList.get(i).getId().equals(id)){
                LibraryInfo LibraryInfo = faceLibraryList.get(i);
                faceLibraryList.remove(i);
                deleteFaceLibrary(faceLibraryList);
                return LibraryInfo;
            }
        }
        return clearFaceLibrary(faceLibraryList);
    }

    /**
     * 清理人脸库数据，随机返回一个人脸库
     * @param faceLibraryList 数据库中人脸库列表
     * @return 缓存库对象
     * */
    private static LibraryInfo clearFaceLibrary(List<LibraryInfo> faceLibraryList){
        LibraryInfo LibraryInfo = faceLibraryList.get(0);
        faceLibraryList.remove(0);
        deleteFaceLibrary(faceLibraryList);
        return LibraryInfo;
    }

    /**
     * 删除人脸库列表
     * @param faceLibraryList 需要清理的列表
     * @return 缓存库对象
     * */
    private static void deleteFaceLibrary(List<LibraryInfo> faceLibraryList){
        for (LibraryInfo libraryInfo: faceLibraryList) {
            deleteFaceLibrary(libraryInfo.getId());
        }
    }

    /**
     * 删除人脸底库
     * @param repoId 底库ID
     * */
    public static boolean deleteFaceLibrary(String repoId){
        JSONObject resultVar = JSONObject.parseObject(FaceConstant.getFaceSdkInvoke().deleteRegistLib(repoId));
        if(resultVar.getInteger("code") == CODE_FAIL){
            log.error("delete face library failed, id = " + repoId + " , sdkResult code = " + resultVar.getInteger("code"));
            return false;
        }
        return true;
    }

    /**
     * 创建人脸底库
     *  @return 底库ID
     * */
    public static String createFaceLibrary(){
        return FaceConstant.getFaceSdkInvoke().createRegistLib();
    }


    /**
     * 创建每日GL人脸抓拍库底库
     * */
    public static void createGLCaptureFaceLibrary(){

        IFaceSdkInvoke faceSdkInvoke = FaceConstant.getFaceSdkInvoke();
        int libHours = SpringContext.getBean(NacosConfig.class).getLibHours();
        if (libHours == 0) {
            return;
        }
        if (faceSdkInvoke instanceof GlstFaceSdkInvokeImpl) {
            //查看底库是否创建
            IArchivesTaskLibService taskLibService = SpringContext.getBean(ArchivesTaskLibServiceImpl.class);
            LocalDateTime now = LocalDateTime.now();
            List<ArchivesTaskLib> taskLibs = taskLibService.list(new LambdaQueryWrapper<ArchivesTaskLib>()
                    .le(ArchivesTaskLib::getIntervalBeginTime, now)
                    .ge(ArchivesTaskLib::getIntervalEndTime, now));
            if (CollectionUtils.isEmpty(taskLibs)) {
                //创建当前时间之后当日库
                int nowHour = LocalDateTime.now().getHour();
                int count = (24 - nowHour) / libHours;
                int remain = (24 - nowHour) % libHours;

                LocalDateTime localDateTime = LocalDate.now().atStartOfDay().plusHours(nowHour);
                for (int i = 0; i < count; i++) {
                    LocalDateTime begin = localDateTime;
                    localDateTime = localDateTime.plusHours(libHours);
                    createGLCaptureFaceLibrary(begin, localDateTime);
                }
                if (remain > 0) {
                    LocalDateTime begin = localDateTime;
                    localDateTime = localDateTime.plusHours(remain);
                    createGLCaptureFaceLibrary(begin, localDateTime);
                }

            }

        }
    }

    public static void createGLCaptureFaceLibrary(LocalDateTime start,LocalDateTime end){
        ArchivesTaskLib taskLib = new ArchivesTaskLib();
        String id = FaceConstant.getFaceSdkInvoke().createRegistLib();
        taskLib.setIntervalBeginTime(start).setIntervalEndTime(end).setLibId(id).setLibType(LibraryConstant.GL_TYPE);
        IArchivesTaskLibService taskLibService = SpringContext.getBean(ArchivesTaskLibServiceImpl.class);
        boolean f = taskLibService.save(taskLib);
        log.info("save glst lib {} is {}" ,id,f);
    }

}
