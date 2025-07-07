package com.keensense.task.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.task.entity.SearchTaskEntity;
import com.keensense.task.entity.TaskVO;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.entity.VideoTaskMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
@Mapper
public interface TbAnalysisTaskMapper extends BaseMapper<TbAnalysisTask> {

    /***
     * 分页查询任务列表
     * @param page 分页对象
     * @param json 查询参数
     * @return: int
     */
	Page<TaskVO> selectListByParam(Page<TaskVO> page, @Param("param") JSONObject json);

    /***
     * 停止任务
     * @param id 任务号
     * @return: int
     */
    int stopTask(String id);

    /***
     * 停止抓拍机任务
     * @param id 任务号
     * @return: int
     */
    int stopPictureTask(String id);

    /***
     * 继续任务
     * @param id 任务号
     * @return: int
     */
    int continueTask(String id);

    /***
     * 继续抓拍机任务
     * @param id 任务号
     * @return: int
     */
    int continuePictureTask(String id);

    /***
     * 删除任务
     * @param id 任务号
     * @return: int
     */
    int deleteTask(String id);

    /***
     * 删除抓拍机任务
     * @param id 任务号
     * @return: int
     */
    int deletePictureTask(String id);

    /***
     * 批量停止任务
     * @param ids 任务对象
     * @return: int
     */
    int stopBatchTask(String[] ids);

    /***
     * 批量删除任务
     * @param ids 任务对象
     * @return: int
     */
    int deleteBatchTask(String[] ids);

    /***
     * 获取搜图任务
     * @param param 请求参数
     * @return: int
     */
    List<SearchTaskEntity> getTaskForSearch(Map param);

    /***
     * 查询需要清理的非实时任务
     * @param endTime 截止时间
     * @return: JSONObject
     */
    List<TbAnalysisTask> selectOfficeForDelete(Timestamp endTime);

    /***
     * 查询需要清理的非实时任务
     * @param ids 任务ID集合
     * @return: JSONObject
     */
    List<VideoTaskMap> getVideoObjectTaskList(String[] ids);

    /***
     * 获取最早的任务
     * @return: JSONObject
     */
    TbAnalysisTask selectEarliestOfficeTask();

    List<TbAnalysisTask> getSerialnumberByCameras(String[] cameraIds);
}
