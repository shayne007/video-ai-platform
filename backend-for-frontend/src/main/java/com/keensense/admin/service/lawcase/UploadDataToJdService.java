package com.keensense.admin.service.lawcase;

import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.vo.UploadDataToJdVo;

/**
 * 上传数据到佳都平台
 *
 */
public interface UploadDataToJdService {

    /**
     * 上传数据到佳都
     * @param type 上传类型
     * @param uploadDataToJdBo 封装上传数据
     * @return
     */
   boolean uploadData(String type, UploadDataToJdVo uploadDataToJdBo, SysUser sysUser);

    /**
     * 保存案件管理的图片数据
     *
     * @param caseCode 案件编号
     * @param imageUrl 图片的url地址
     * @param pathType 路径类型
     * @return
     */
    String saveImage(String caseCode, String imageUrl, String pathType);

}
