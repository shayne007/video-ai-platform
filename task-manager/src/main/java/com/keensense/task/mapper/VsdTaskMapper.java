package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.VsdTask;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: vsdTask表jdbc接口
 * @Author: wujw
 * @CreateDate: 2019/5/14 17:25
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface VsdTaskMapper extends BaseMapper<VsdTask> {

    /***
     * 获取未分析完的vsdTask对象
     * @return: java.util.List<com.keensense.task.entity.VsdTask>
     */
    List<VsdTask> selectByRunningDetail();

    /***
     * 批量写入
     * @param list vsdTask对象集合
     * @return: int
     */
    int insertBatch(List<VsdTask> list);

    /***
     * 更新感兴趣区域
     * @param userSerialnumber 任务ID
     * @param param 新的param参数
     * @return: int
     */
    int updateTask(@Param("userSerialnumber") String userSerialnumber, @Param("param") String param);

    /***
     * 更新进度
     * @param serialnumber 任务ID
     * @param progress 进度
     * @return: int
     */
    int updateProgress(@Param("serialnumber") String serialnumber, @Param("progress") Integer progress);

    /****
     * 根据ID重置状态
     * @param ids id数组
     * @param status 状态
     * @return: int
     */
    int updateStatusByIds(@Param("ids") Long[] ids, @Param("status") int status);

    /****
     * 根据serialnumber重置状态
     * @param serialnumber 任务号
     * @param status 状态
     * @return: int
     */
    int updateStatusBySerialnumber(@Param("serialnumber") String serialnumber, @Param("status") int status);

    /****
     * 根据ID重置状态
     * @param id id
     * @param status 状态
     * @return: int
     */
    int updateStatusById(@Param("id") Long id, @Param("status") int status);

    /****
     * 根据Iserialnumber重置状态
     * @param serialnumber ser
     * @return: int
     */
    int updateStatusProgressBySer(@Param("serialnumber") String serialnumber, @Param("progress") Integer progress,@Param("param") String param,
                                  @Param("errcode") Integer errcode,@Param("errmsg") String errmsg);

    int updateDeadTaskByVersion(@Param("id") Long id,@Param("version")int version,@Param("status") int status);
    
    /***
     * @description: 重置任务状态
     * @param serialnumber 任务ID
     * @return: int
     */
    int updateResetStatus(String serialnumber);

    /***
     * @description: 更新超时后的任务状态，mysql版本使用
     * @return: void
     */
    void updateTimeOutTask();

    void updateResetList(List<Long> list);

    List<String> getSerialnumberByTaskIds(List<String> taskIds);
    
    int deleteVsdTask(String userserialnumber);


}
