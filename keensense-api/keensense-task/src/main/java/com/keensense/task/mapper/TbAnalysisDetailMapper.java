package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.TbAnalysisDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: TbAnalysisDetail jdbc接口
 * @Author: wujw
 * @CreateDate: 2019/5/14 16:46
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface TbAnalysisDetailMapper extends BaseMapper<TbAnalysisDetail> {

    /***
     * 批量写入对象
     * @param list 对象集合
     * @return: int
     */
    int insertBatch(List<TbAnalysisDetail> list);

    /***
     * 获取可重用的任务分片
     * @param list 下载路径集合
     * @return: int
     */
    List<TbAnalysisDetail> getReuseList(List<String> list);

    /***
     * 批量更新对象
     * @param list 对象集合
     * @return: int
     */
    int updateBatch(List<TbAnalysisDetail> list);

    /***
     * 根据主任务ID获取子任务列表
     * @param taskId 对象集合
     * @return: int
     */
    List<TbAnalysisDetail> getDetailByTaskId(String taskId);

    /***
     * 根据ID查询复用次数
     * @param id ID
     * @return: int
     */
    int countByRetryId(String id);
    
    /***
     * @description: 下载或者转码失败更新子任务
     * @param detail 子任务对象
     * @return: void
     */
    void updateFailed(TbAnalysisDetail detail);

    /***
     * @description: 重置任务状态
     * @param serialnumber 任务ID
     * @return: int
     */
    int updateResetStatus(String serialnumber);
}
