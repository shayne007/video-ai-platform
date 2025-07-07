package com.keensense.extension.util;

import com.keensense.extension.constants.LibraryConstant;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description: 底库工具类
 * @Author: kellen
 * @CreateDate: 2019/2/26 15:45
 * @Version: 1.0
 */
@Slf4j
public class LibraryUtil {

    private LibraryUtil(){}
    /**
     * 判断底库是否存在，不存在则创建
     * */
    public static void isExistLibrary(){
        FaceLibraryUtil.isExistFaceLibrary();
        BodyLibraryUtil.isExistBodyLibrary();
    }

    /**
     * 更新底库缓存信息
     * */
    public static void updateLibraryCache(){
        FaceLibraryUtil.updateFaceLibraryCache();
        BodyLibraryUtil.updateBodyLibraryCache();
    }

    /**
     * 删除底库记录
     * @param id 底库ID
     * */
    public static void deleteLibraryById(String id){
        if(!LibraryConstant.deleteLibraryById(id)){
            log.error(" delete library for mysql failed, id = "+id);
        }
    }

}
