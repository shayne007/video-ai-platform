package com.keensense.common.platform.constant;
/**
 * 1400 Url常量类
 * @author wangsj
 *
 */
public class StandardUrlConstant {
	/**
     * 所有类型查询接口
     * get  
     *  /<ResultList> 在所有类型中进行查询
     */
	public static final String RESULT = "/VIID/Result";
	/**
	 * 人员相关接口
	 * get  批量查询人员
	 * post 批量人员新增
	 *  /<PersonID>单个人员查询
	 */
	public static final String PERSONS = "/VIID/Persons";
	/**
	 * 人员以图搜图
     */
	public static final String PERSONS_SEARCH = "/VIID/Persons/Search";
	/**
     * 机动车相关接口
     * get  批量查询机动车
     * post 批量机动车新增
     *  /<PersonID>单个机动车查询 
     */
	public static final String MOTORVEHICLES = "/VIID/MotorVehicles";
	/**
	 * 机动车以图搜图
     */
	public static final String MOTORVEHICLES_SEARCH = "/VIID/MotorVehicles/Search";
	/**
     * 非机动车相关接口
     * get  批量查询非机动车
     * post 批量非机动车新增
     *  /<PersonID>单个机动车查询 
     */
	public static final String NONMOTORVEHICLES = "/VIID/NonMotorVehicles";
	/**
	 * 非机动车以图搜图
     */
	public static final String NONMOTORVEHICLES_SEARCH = "/VIID/NonMotorVehicles/Search";
	/**
     * 人脸相关接口
     * get  批量查询人脸
     * post 批量人脸新增
     *  /<PersonID>单个人脸查询 
     */
	public static final String FACES = "/VIID/Faces";
	/**
	 * 人脸以图搜图
     */
	public static final String FACES_SEARCH = "/VIID/Faces/Search";
	
	/**
	 * 图片特征提取
     */
	public static final String EXTRACT_FROM_PICTURE = "/rest/feature/extractFromPicture";
	/**
	 * 图片特征提取
     */
	public static final String STRUCT_PICTURE = "/rest/feature/structPicture";
	
	/**
	 * 语义搜时先按监控点分组返回单个监控点下某单张详情
	 * 可扩展到整个es查询
     */
	public static final String PERSON_GROUPBY = "/VIID/GroupByQuery/Persons";
	/**
	 * 语义搜时先按监控点分组返回单个监控点下某单张详情
	 * 可扩展到整个es查询
     */
	public static final String MOTORVEHICLES_GROUPBY = "/VIID/GroupByQuery/MotorVehicles";
	/**
	 * 语义搜时先按监控点分组返回单个监控点下某单张详情
	 * 可扩展到整个es查询
     */
	public static final String NONMOTORVEHICLES_GROUPBY = "/VIID/GroupByQuery/NonMotorVehicles";
	/**
	 * 语义搜时先按监控点分组返回单个监控点下某单张详情
	 * 可扩展到整个es查询
     */
	public static final String FACES_GROUPBY = "/VIID/GroupByQuery/Faces";

	/**
	 * 3.5.1获取集群列表信息
	 */
	public static final String VSD_SLAVE = "/rest/slave/getVsdSlaveList";

	/**
	 * 根据ID获取集群节点信息
	 */
	public static final String VSD_SLAVE_BY_ID = "/rest/slave/getVsdSlaveById";

	/**
	 * 删除es数据
	 */
	public static final String DELETE_RESULT_DATA = "/VIID/Result/Single/Delete";

	/**
	 * 更新人es数据
	 */
	public static final String PERSONS_UPDATE = "/VIID/Persons/Update";

	/**
	 * 更新车es数据
	 */
	public static final String MOTORVEHICLES_UPDATE = "/VIID/MotorVehicles/Update";

	/**
	 * 更新人骑车es数据
	 */
	public static final String NONMOTORVEHICLES_UPDATE = "/VIID/NonMotorVehicles/Update";

	public static final String EXTRACT_FROM_PICTURE_GL_FACE = "/picturestream/face/struct";


}
