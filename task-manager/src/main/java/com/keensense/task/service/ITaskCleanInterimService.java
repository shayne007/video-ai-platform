package com.keensense.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.task.entity.TaskCleanInterim;

import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/9/28 10:24
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface ITaskCleanInterimService {

    /***
     * 批量插入清理数据
     * @param list 清理对象列表
     * @return: int
     */
    boolean insertBatch(List<TaskCleanInterim> list);

    boolean insert(TaskCleanInterim taskCleanInterim);

    /***
     * 重置所有正在运行中的任务状态
     */
    void resetRunningStatus();

    /***
     * 查询需要执行的记录
     * @param slaveId 节点信息Id
     * @return: int
     */
    List<TaskCleanInterim> selectListBySlave(String slaveId);

    /***
     * 获取需要执行的记录数
     * @return: int
     */
    int selectListForExce();

    /***
     * 修改记录状态
     * @param taskCleanInterim 删除记录
     * @return: int
     */
    boolean updateById(TaskCleanInterim taskCleanInterim);

    /***
     * 修改记录状态
     * @param serialnumber 任务号
     * @param status 是否成功
     * @param ymd   时间
     * @return: boolean
     */
    boolean updateStatus(String serialnumber, boolean status, String ymd);
}
