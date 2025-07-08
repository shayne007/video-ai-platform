-- set global time_zone = '+8:00';  ##修改mysql全局时区为北京时间，即我们所在的东8区
-- set time_zone = '+8:00';  ##修改当前会话时区
-- flush privileges;  #立即生效

CREATE TABLE `app_image_search` (
  `SEARCH_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OBJEXT_TYPE` int(2) DEFAULT '1' COMMENT '1：person 2：car 4：bike',
  `IMAGE_URL` longtext COMMENT '图片路径',
  `CAMERA_ADDRESS` longtext COMMENT '支持多监控点查询,例如：cameraId1,cameraId2，.....，以英文状态下“,”逗号分隔',
  `START_TIME` datetime DEFAULT NULL COMMENT '开始时间',
  `END_TIME` datetime DEFAULT NULL COMMENT '结束时间',
  `STREAM_ANS_SERIALNUMBERS` longtext,
  `SERIALNUMBER` varchar(64) DEFAULT NULL COMMENT '以图搜图任务id',
  `PROGRESS` varchar(10) DEFAULT NULL COMMENT '搜图进度',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `RET` varchar(10) DEFAULT NULL COMMENT '处理的状态 0：成功，其他：失败',
  `RESPONSE_MSG` longtext COMMENT '返回信息',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `BATCH_ID` varchar(32) DEFAULT NULL COMMENT '以图搜图批次id',
  `TYPE` varchar(2) DEFAULT NULL COMMENT '0 接力追踪以图搜图 1 视频分析以图搜图',
  `c1` varchar(100) DEFAULT NULL COMMENT '图片特征',
  `c2` longtext COMMENT '图片id',
  `C3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`SEARCH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `app_police_comprehensive` (
  `LAWCASE_ID` varchar(32) NOT NULL,
  `CASE_NAME` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '案件名称',
  `CASE_CODE` varchar(128) DEFAULT NULL COMMENT '案件编号',
  `CATEGORY_VALUE` varchar(32) DEFAULT NULL COMMENT '案件类别',
  `property_value` varchar(32) DEFAULT NULL COMMENT '案件性质',
  `CASE_STATE` varchar(32) DEFAULT NULL,
  `START_TIME` datetime DEFAULT NULL COMMENT '发案起始时间',
  `END_TIME` datetime DEFAULT NULL COMMENT '发案终止时间',
  `RECIEVE_TIME` datetime DEFAULT NULL COMMENT '接警时间',
  `RESOLVE_TIME` datetime DEFAULT NULL,
  `ENGAGE_ORG` varchar(50) DEFAULT NULL COMMENT '责任单位',
  `reporter_name` varchar(50) DEFAULT NULL COMMENT '报案人姓名',
  `reporter_phone` varchar(20) DEFAULT NULL COMMENT '报案人电话',
  `LOCATION_ID` varchar(32) DEFAULT NULL COMMENT '案发区划',
  `POLICE_STATION` varchar(32) DEFAULT NULL COMMENT '案发派出所',
  `CASE_COMMUNITY` varchar(512) DEFAULT NULL COMMENT '案发社区',
  `LOCATION_TYPE` varchar(32) DEFAULT NULL COMMENT '案发地点类型',
  `LOCATION_NAME` varchar(255) DEFAULT NULL COMMENT '案发地点名称',
  `LOCATION_DETAIL` varchar(512) DEFAULT NULL COMMENT '案发详址',
  `LONTITUDE` varchar(32) DEFAULT NULL COMMENT '发案经度',
  `LATITUDE` varchar(32) DEFAULT NULL COMMENT '发案纬度',
  `CASE_AREA` varchar(32) DEFAULT NULL COMMENT '案发区域',
  `CASE_PLACES` varchar(32) DEFAULT NULL COMMENT '案发场所',
  `PLACES_TYPE` varchar(32) DEFAULT NULL COMMENT '地点类别',
  `CASE_DESC` varchar(2048) DEFAULT NULL COMMENT '案情简介',
  `CASE_MONEY` varchar(20) DEFAULT NULL COMMENT '涉案金额',
  `MONEY_LOSE` varchar(20) DEFAULT NULL COMMENT '经济损失',
  `CHOOSE_PLACE` varchar(32) DEFAULT NULL COMMENT '选择处所',
  `CHOOSE_PERSON` varchar(32) DEFAULT NULL COMMENT '选择对象',
  `CHOOSE_ITEM` varchar(32) DEFAULT NULL COMMENT '选择物品',
  `CHOOSE_DATE` varchar(32) DEFAULT NULL COMMENT '选择日期',
  `CHOOSE_TIME` varchar(32) DEFAULT NULL COMMENT '选择时间',
  `CHOOSE_CHANCE` varchar(32) DEFAULT NULL COMMENT '选择时机',
  `CHOOSE_WEATHER` varchar(32) DEFAULT NULL COMMENT '选择天气 ',
  `CASE_METHOD_FEATURES_VALUE` varchar(32) DEFAULT NULL COMMENT '侵入手段',
  `STEAL_METHOD` varchar(32) DEFAULT NULL COMMENT '窃入手段',
  `DISGUISE_METHOD` varchar(32) DEFAULT NULL COMMENT '伪装灭迹',
  `PREPARE_METHOD` varchar(32) DEFAULT NULL COMMENT '预备手段',
  `ORG_WAY` varchar(32) DEFAULT NULL COMMENT '组织形式',
  `COLLUDE_WAY` varchar(32) DEFAULT NULL COMMENT '勾结形式',
  `CASE_WAY` varchar(32) DEFAULT NULL COMMENT '作案范围',
  `EXPLORE_WAY` varchar(32) DEFAULT NULL COMMENT '试探方式',
  `ACTION_WAY` varchar(32) DEFAULT NULL COMMENT '行为特点',
  `IS_CROSS` varchar(20) DEFAULT NULL COMMENT '是否抢渡',
  `BUS_WAY` varchar(255) DEFAULT NULL COMMENT '公交路线',
  `UP_STATION` varchar(255) DEFAULT NULL COMMENT '上车站点',
  `DOWN_STATION` varchar(255) DEFAULT NULL COMMENT '下车站点',
  `CASE_TOOL` varchar(255) DEFAULT NULL COMMENT '作案工具',
  `OPRATE_RESULT` varchar(255) DEFAULT NULL COMMENT '处理结果',
  `KEY_WORDS` varchar(512) DEFAULT NULL COMMENT '关键字',
  `REMARK_ADVICE` varchar(2048) DEFAULT NULL COMMENT '备注及民警意见',
  `HAS_SUPECT` varchar(50) DEFAULT NULL COMMENT '是否有嫌疑人',
  `HAS_SURVEY` varchar(50) DEFAULT NULL COMMENT '是否有现场勘查',
  `IS_OLD_DATA` decimal(5,0) DEFAULT NULL COMMENT '是否是老数据',
  `ELECTRONIC_FILE_ID` varchar(50) DEFAULT NULL COMMENT '电子归档id',
  `CASE_CLASS` varchar(32) DEFAULT NULL COMMENT '案件分类： 案件、疑情',
  `CASE_THUMBNAIL` varchar(256) DEFAULT NULL COMMENT '案件封面',
  `CREATOR_ID` decimal(16,0) DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_USER_ID` decimal(16,0) DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `IS_DELETED` decimal(5,0) DEFAULT NULL COMMENT '删除标识',
  `DELETE_TIME` datetime DEFAULT NULL COMMENT '删除时间',
  `DELETE_REASON` varchar(256) DEFAULT NULL COMMENT '删除原因',
  `ARCHIVE_INFO` varchar(2048) DEFAULT NULL COMMENT '归档信息',
  `ARCHIVE_REPORT` varchar(2048) DEFAULT NULL COMMENT '归档报告',
  `PRINCIPAL` decimal(16,0) DEFAULT NULL COMMENT '责任人',
  `IS_CLASSIC_CASE` decimal(5,0) DEFAULT NULL COMMENT '是否是经典案例',
  `HAPPEN_PLACE_LEVEL` decimal(16,0) DEFAULT NULL COMMENT '案发地图层级',
  `REPORT_PERSON` varchar(256) DEFAULT NULL COMMENT '报案人',
  `ID_CARD` varchar(256) DEFAULT NULL COMMENT '报案人证件号码',
  `CONTACT_PHONE` varchar(256) DEFAULT NULL COMMENT '报案人电话',
  `ISQUALIFIED` decimal(5,0) DEFAULT NULL COMMENT '是否是合格案件',
  `SOURCEID` decimal(16,0) DEFAULT NULL COMMENT '专网id',
  `ISACROSSBORDER` varchar(256) DEFAULT '' COMMENT '专网网闸',
  `VERIFYING_STATUS` decimal(5,0) DEFAULT NULL COMMENT ' 审批状态',
  `WITHWAITVERIFYRES` decimal(16,0) DEFAULT NULL COMMENT '审批关联的附件id',
  `ORGIN_SYSTEM` varchar(32) DEFAULT NULL COMMENT '来源系统',
  `ORGIN_CODE` varchar(256) DEFAULT NULL COMMENT '系统编码',
  `RECIEVE_CODE` varchar(32) DEFAULT NULL COMMENT '接触警编号',
  `TRANSPORT` varchar(32) DEFAULT NULL COMMENT '交通工具',
  `SYNC_STATUS` decimal(5,0) DEFAULT '0',
  `TRACK_IMAGE_URL` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`LAWCASE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='警综表';






CREATE TABLE `camera_dept` (
  `dept_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `Long_number` varchar(256) DEFAULT NULL COMMENT '业务行政部门代码',
  `dept_name` varchar(256) DEFAULT NULL COMMENT '名称',
  `dept_number` varchar(256) DEFAULT NULL COMMENT '部门编码',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父节点id',
  `dept_level` bigint(8) DEFAULT NULL COMMENT '层级',
  `display_name` varchar(256) DEFAULT NULL COMMENT '显示名称',
  `is_leaf` bigint(8) DEFAULT NULL COMMENT '是否叶子节点',
  `dept_state` bigint(8) DEFAULT NULL COMMENT '状态 0 禁用1启用',
  `admin_identity` bigint(8) DEFAULT NULL COMMENT '行政组织代码',
  `address` varchar(512) DEFAULT NULL COMMENT '地址',
  `tel` bigint(20) DEFAULT NULL COMMENT '联系电话',
  `is_biz_org` bigint(8) DEFAULT NULL COMMENT '是否业务行政部门 0 否 1 是',
  `biz_code` varchar(128) DEFAULT NULL COMMENT '行政部门id',
  `seq` bigint(8) DEFAULT NULL COMMENT '序号',
  `description` bigint(8) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '端口2',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_user_id` bigint(20) DEFAULT NULL COMMENT '最新更新用户id',
  `last_updated_time` datetime DEFAULT NULL COMMENT '最新更新时间',
  `ctrl_unit_id` varchar(32) DEFAULT NULL COMMENT '行政区域id',
  `allCamera` int(16) DEFAULT NULL,
  `onlineCamera` int(16) DEFAULT NULL,
  `longitude` varchar(32) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`dept_id`),
  KEY `INDEX_LONG_NUMBER` (`Long_number`(255)) USING BTREE,
  KEY `INDEX_DEPT_NUMBER` (`dept_number`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

CREATE TABLE `camera_special` (
  `camera_id` bigint(32) NOT NULL COMMENT '监控点编号',
  `sence` varchar(10) NOT NULL DEFAULT '100' COMMENT '场景: 100-室内场景',
  PRIMARY KEY (`camera_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='特殊点位表';





CREATE TABLE `case_camera_media` (
  `ID` varchar(32) NOT NULL,
  `FILE_TYPE` varchar(2) DEFAULT NULL COMMENT '1 img,2 video',
  `FILE_SUFFIX` varchar(32) DEFAULT NULL COMMENT '不包含小数点，例如：avi',
  `FILE_NAME` varchar(200) DEFAULT NULL COMMENT '例如：五一路卡口视频.avi',
  `FILE_NAMEAFTERUPLOAD` varchar(256) DEFAULT NULL COMMENT '上传后的文件名',
  `FILE_PATHAFTERUPLOAD` varchar(256) DEFAULT NULL COMMENT '上传后文件详细路径',
  `FILE_BIG_IMAGE` varchar(256) DEFAULT NULL COMMENT '上传后的大图文件名',
  `VIDEOBIT_ID` bigint(32) DEFAULT NULL,
  `TARGET_APPEARTIME` varchar(128) DEFAULT NULL,
  `FILE_DESCRIPTION` varchar(2000) DEFAULT NULL,
  `CREATE_TIME` varchar(128) DEFAULT NULL,
  `CREATE_USERID` varchar(32) DEFAULT NULL,
  `THUMB_NAIL` varchar(256) DEFAULT NULL COMMENT '在上传一个文件后，对应的应有一个缩略图命名规则为：上传后文件名+thumbnail例如：上传后文件名为123456，则缩略图命名为：123456thumbnail',
  `FILE_SIZE` varchar(32) DEFAULT NULL COMMENT '文件大小',
  `USERUPLOADVIDEOID` varchar(256) DEFAULT NULL COMMENT '提交ocx转码后id',
  `USERUPLOAD_PATH` varchar(256) DEFAULT NULL COMMENT '提交ocx转码后视频路径',
  `USERUPLOAD_STATUS` varchar(32) DEFAULT NULL COMMENT '提交ocx转码后的状态',
  `C1` varchar(32) DEFAULT NULL COMMENT '素材关联案件id',
  `C2` varchar(32) DEFAULT NULL COMMENT 'clue：素材，source:原始视频 【该字段 区分 原始视频和 手动上传的 图片或短视频素材】',
  `C3` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `C4` varchar(32) DEFAULT NULL COMMENT '任务ID',
  `C5` varchar(32) DEFAULT NULL COMMENT '图片类型objType(1:人,2:车辆,4:人骑车 )',
  `clue_type` varchar(2) DEFAULT NULL COMMENT '线索采集分类 1：中心现场，2：清晰图像，3：活动轨迹，4：落脚点信息，5：破案信息',
  `C6` varchar(32) DEFAULT NULL,
  `C7` varchar(32) DEFAULT NULL,
  `C8` varchar(128) DEFAULT NULL,
  `VERIFYING_STATUS` varchar(2) DEFAULT NULL,
  `sync_status` varchar(2) DEFAULT NULL,
  `lawcaseId` varchar(32) DEFAULT NULL,
  `pic_info` varchar(2000) NOT NULL COMMENT '图片的标签数据',
  `longitude` varchar(32) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='与相关的附件表，视频，前期我们通过上传本地文件到服务器，服务器将文件放到FTP服务器，如果以后的文件类型，上传方式客户有要求，可扩充此字段';





CREATE TABLE `ctrl_unit_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `file_type` varchar(2) DEFAULT NULL COMMENT '文件类型2视频，1图片,3表示文件夹',
  `file_suffix` varchar(64) DEFAULT NULL COMMENT '文件后缀',
  `file_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文件名',
  `file_nameafterupload` varchar(512) DEFAULT NULL COMMENT '上传后的文件名',
  `file_pathafterupload` varchar(512) DEFAULT NULL COMMENT '上传后文件的ftp路径',
  `file_local_path` varchar(512) DEFAULT NULL COMMENT '上传的本地路径',
  `ctrl_unit_id` varchar(256) DEFAULT NULL COMMENT '行政区域id',
  `camera_id` bigint(20) DEFAULT NULL COMMENT '点位id',
  `file_description` varchar(1024) DEFAULT NULL COMMENT '文件描述',
  `thumb_nail` varchar(512) DEFAULT NULL COMMENT '缩略图',
  `file_size` varchar(32) DEFAULT NULL COMMENT '文件大小',
  `create_uerid` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `entry_time` datetime DEFAULT NULL COMMENT '视频录入时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `serialnumber` varchar(64) DEFAULT NULL COMMENT '系列Id',
  `transcoding_id` varchar(64) DEFAULT NULL COMMENT '转码文件ID',
  `transcoding_progress` varchar(64) DEFAULT NULL COMMENT '转码进度',
  `frame_count` bigint(20) DEFAULT NULL COMMENT '视频帧数',
  `framerate` bigint(20) DEFAULT NULL COMMENT '每秒帧数',
  `resolution` varchar(20) DEFAULT NULL COMMENT '分辨率',
  `transcode_status` smallint(6) DEFAULT NULL COMMENT '转码状态',
  `tasticsentityid` varchar(32) DEFAULT NULL,
  `file_ftp_path` varchar(521) DEFAULT NULL COMMENT 'ftp文件路径',
  `slave_ip` varchar(256) DEFAULT NULL,
  `auto_analysis_flag` tinyint(4) DEFAULT NULL COMMENT '是否自动分析,0:否,1:是',
  `interest_param` varchar(1024) DEFAULT NULL,
  `uninterest_param` varchar(1024) DEFAULT NULL,
  `tripwires` varchar(255) DEFAULT NULL COMMENT '跨线参数',
  `create_user_id` smallint(8) DEFAULT NULL COMMENT '用户id',
  `del_flag` smallint(8) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9985136759 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='文件表';




CREATE TABLE `image_search_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serialnumber` varchar(252) DEFAULT NULL COMMENT '任务关联序列号',
  `objext_id` varchar(40) DEFAULT NULL COMMENT '关联目标id',
  `imgurl` varchar(252) DEFAULT NULL,
  `seqnumber` int(4) DEFAULT NULL COMMENT '当前任务排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `image_search_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serialnumber` varchar(256) NOT NULL COMMENT '序列号，关联si_task的serialNum',
  `target_image_url` text COMMENT '以图搜图目标图片 http:// 或者 file://',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理状态 0 等待处理 1 处理成功 2 处理失败',
  `progress` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理进度',
  `taskname` varchar(256) NOT NULL DEFAULT '' COMMENT '任务名称',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '任务描述',
  `taskType` smallint(8) NOT NULL DEFAULT '0' COMMENT '目标类型,1:人,2:汽车,4：人骑车',
  `algorithm_type` smallint(8) NOT NULL DEFAULT '0' COMMENT '算法类别：0 综合特征(需要 cuda 设备支持) 1 颜色优先特征',
  `result_tbl_name` varchar(64) NOT NULL DEFAULT '' COMMENT '结果分表的表名，为空时，结果输出到si_task',
  `total_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '备选集的总数',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  `output` text COMMENT '添加以图搜图任务接口output参数(定义以图搜图比对结果的输出方式和格式)',
  `qstartTime` varchar(20) DEFAULT NULL COMMENT '搜图范围：起始时间',
  `qendTime` varchar(20) DEFAULT NULL COMMENT '搜图范围：结束时间',
  `qcameraIds` text COMMENT '搜图范围：监控点列表',
  PRIMARY KEY (`id`),
  KEY `serialnumber` (`serialnumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `objext_result` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serialnumber` varchar(128) NOT NULL DEFAULT '' COMMENT '序列号，对应哪个任务的结果',
  `cameraid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '点位id',
  `vlpr_result_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '与vlpr_result关联的结果，-1:未关联,0：已关联但无结果,>1 已关联且有结果',
  `imgurl` varchar(512) NOT NULL DEFAULT '' COMMENT '图片的链接',
  `bigimgurl` varchar(512) NOT NULL DEFAULT '' COMMENT '大图的链接',
  `objtype` smallint(8) NOT NULL DEFAULT '0' COMMENT '目标类型,-1:未知,1:人,2:汽车,4：人骑车',
  `maincolor_1` int(8) DEFAULT NULL COMMENT '主颜色1',
  `maincolor_2` int(8) DEFAULT NULL COMMENT '主颜色2',
  `maincolor_3` int(8) DEFAULT NULL COMMENT '主颜色3',
  `upcolor_1` int(8) DEFAULT NULL COMMENT '上半身颜色1,objtype为1,此值有效',
  `upcolor_2` int(8) DEFAULT NULL COMMENT '上半身颜色2,objtype为1,此值有效',
  `upcolor_3` int(8) DEFAULT NULL COMMENT '上半身颜色3,objtype为1,此值有效',
  `lowcolor_1` int(8) DEFAULT NULL COMMENT '下半身颜色1,objtype为1时,此值有效',
  `lowcolor_2` int(8) DEFAULT NULL COMMENT '下半身颜色2,objtype为1时,此值有效',
  `lowcolor_3` int(8) DEFAULT NULL COMMENT '下半身颜色3,objtype为1时,此值有效',
  `maincolor_tag_1` int(8) DEFAULT NULL COMMENT '主颜色标签1',
  `maincolor_tag_2` int(8) DEFAULT NULL COMMENT '主颜色标签2',
  `maincolor_tag_3` int(8) DEFAULT NULL COMMENT '主颜色标签3',
  `upcolor_tag_1` int(8) DEFAULT NULL COMMENT '上半身颜色标签1,objtype为1,此值有效1',
  `upcolor_tag_2` int(8) DEFAULT NULL COMMENT '上半身颜色标签2,objtype为1,此值有效2',
  `upcolor_tag_3` int(8) DEFAULT NULL COMMENT '上半身颜色标签3,objtype为1,此值有效3',
  `lowcolor_tag_1` int(8) DEFAULT NULL COMMENT '下半身颜色标签1,objtype为1时,此值有效1',
  `lowcolor_tag_2` int(8) DEFAULT NULL COMMENT '下半身颜色标签2,objtype为1时,此值有效2',
  `lowcolor_tag_3` int(8) DEFAULT NULL COMMENT '下半身颜色标签3,objtype为1时,此值有效3',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别,0:未知,1:男性 2:女性 ，objtype为1时,此值有效',
  `age` tinyint(4) NOT NULL DEFAULT '0' COMMENT '年龄,0:未知,4:小朋友,8:青年,16:中年,32:老年,64:备用，objtype为1时,此值有效',
  `wheels` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:未知,1:自行车，2:2轮车，3:3轮车,objtype为4时,此值有效',
  `size` tinyint(4) NOT NULL DEFAULT '0' COMMENT '车辆类型,0:未知,1:大型车、2:中型车、4:小型车,objtype为2时,此值有效',
  `bag` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '是否背包,-1:未知,1:背包 0:不背包，objtype为1时,此值有效',
  `glasses` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '是否戴眼镜,-1:未知,1:戴眼镜 0:不戴眼镜，objtype为1时,此值有效',
  `umbrella` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '【2.0版本，非机动车带此属性】是否打伞,-1:未知,1:打伞 0:不打伞，objtype为1时,此值有效',
  `umbrella_color_tag` int(8) DEFAULT NULL COMMENT '【2.0版本，非机动车带此属性】遮阳伞颜色标签,',
  `angle` smallint(6) NOT NULL DEFAULT '0' COMMENT '角度,0:未知,128:正面 256:侧面，512:背面,objtype为1时,此值有效',
  `handbag` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '是否有手提包,-1:未知,1:有手提包 0:没有手提包，objtype为1时,此值有效',
  `tubeid` int(11) NOT NULL DEFAULT '0' COMMENT 'tubeid',
  `objid` int(11) NOT NULL DEFAULT '0' COMMENT 'objid',
  `startframeidx` int(11) NOT NULL DEFAULT '0' COMMENT '目标出现的帧序',
  `endframeidx` int(11) NOT NULL DEFAULT '0' COMMENT '目标消失的帧序',
  `startframepts` int(11) NOT NULL DEFAULT '0' COMMENT '目标出现的时间戳',
  `endframepts` int(11) NOT NULL DEFAULT '0' COMMENT '目标消失的时间戳',
  `frameidx` int(11) NOT NULL DEFAULT '0' COMMENT '当前快照图片在视频中的帧序',
  `width` smallint(6) NOT NULL DEFAULT '0' COMMENT '图片宽度',
  `height` smallint(6) NOT NULL DEFAULT '0' COMMENT '图片高度',
  `x` smallint(6) NOT NULL DEFAULT '0' COMMENT '目标在图片中的x坐标',
  `y` smallint(6) NOT NULL DEFAULT '0' COMMENT '目标在图片中的y坐标',
  `w` smallint(6) NOT NULL DEFAULT '0' COMMENT '目标在图片中的宽度',
  `h` smallint(6) NOT NULL DEFAULT '0' COMMENT '目标在图片中的高度',
  `distance` float NOT NULL DEFAULT '0' COMMENT '相似度',
  `featuredataurl` varchar(512) NOT NULL DEFAULT '' COMMENT '特征文件的链接',
  `face_imgurl` varchar(512) NOT NULL DEFAULT '' COMMENT '抓拍的人脸图片',
  `humanfeature` text COMMENT '人特征参数',
  `peccancy` smallint(6) NOT NULL DEFAULT '0' COMMENT '违规类型,0:无违规信息,1:行人闯红绿灯 2:车辆闯红绿灯',
  `bike_color` int(8) NOT NULL DEFAULT '0' COMMENT '非机动车颜色标签，objtype为4时,此值有效',
  `bike_genre` int(8) NOT NULL DEFAULT '0' COMMENT '非机动车类型：0：未知，1：女士摩托车:2：男士摩托车，3：自行车，4：电动车，5：三轮车，objtype为4时,此值有效',
  `seating_count` int(11) NOT NULL DEFAULT '0' COMMENT '载客数量: 0：未知，> 0 载客数，objtype为4时,此值有效',
  `helmet` varchar(64) NOT NULL DEFAULT '2' COMMENT '是否戴头盔,2:未知,1:戴 0:没戴，objtype为4时,此值有效',
  `helmet_color_tag_1` int(11) DEFAULT NULL COMMENT '乘客1头盔颜色标签，objtype为4时,此值有效',
  `helmet_color_tag_2` int(11) DEFAULT NULL COMMENT '乘客2头盔颜色标签，objtype为4时,此值有效',
  `lam_shape` smallint(6) NOT NULL DEFAULT '999' COMMENT '非机动车车灯形状,0:倒三角，1:圆形,2:方形,3:梯形,4:椭圆,999:未知,',
  `bike_has_plate` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '人骑车是否挂牌,-1:未知,1:挂牌 0:不挂牌，objtype为4时,此值有效',
  `bike_feature_json` text COMMENT '非机动车特征原始信息',
  `createtime` datetime DEFAULT NULL COMMENT '经过校正后的时间',
  `inserttime` datetime DEFAULT NULL COMMENT '结果入库时间',
  `sychronized` smallint(1) DEFAULT '0' COMMENT '是否同步建立索引：0否1是',
  `slaveip` varchar(20) NOT NULL DEFAULT '127.0.0.1' COMMENT '记录分析的slave的ip地址',
  `coat_style` varchar(20) DEFAULT NULL COMMENT '上身衣着',
  `trousers_style` varchar(20) DEFAULT NULL COMMENT '下身衣着',
  PRIMARY KEY (`id`),
  KEY `startframeidx` (`startframeidx`),
  KEY `endframeidx` (`endframeidx`),
  KEY `createtime` (`createtime`),
  KEY `serialnumber` (`serialnumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `objext_result_track` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serialnumber` varchar(64) NOT NULL DEFAULT '' COMMENT '序列号，对应哪个任务的结果',
  `tubeid` int(11) NOT NULL DEFAULT '0' COMMENT 'tubeid',
  `objid` int(11) NOT NULL DEFAULT '0' COMMENT 'objid',
  `startframepts` int(11) NOT NULL DEFAULT '0' COMMENT '目标出现的时间戳',
  `trackjson` mediumtext NOT NULL COMMENT '轨迹json，schema:{"frames":[{"idx":224,"pts":8960,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":225,"pts":9000,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":226,"pts":9040,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":227,"pts":9080,"box":{"x":100,"y":120,"w":40,"h":60}},{"idx":248,"pts":9920,"box":{"x":100,"y":120,"w":40,"h":60}}]}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `objext_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serialnumber` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '关联任务id',
  `cameraid` bigint(20) NOT NULL COMMENT '监控点id',
  `tracktime` datetime NOT NULL COMMENT '目标出现时间',
  `objtype` smallint(8) NOT NULL COMMENT '目标类型,-1:未知,1:人,2:汽车,4:人骑车',
  `resultid` varchar(64) NOT NULL COMMENT '结果id',
  `info1` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '扩展字段1',
  `info2` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '扩展字段2',
  `info3` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '扩展字段3',
  PRIMARY KEY (`id`),
  UNIQUE KEY `snum_rid_unique` (`serialnumber`,`resultid`)
) ENGINE=InnoDB AUTO_INCREMENT=537 DEFAULT CHARSET=latin1;

CREATE TABLE `objext_track_pic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serialnumber` varchar(60) NOT NULL,
  `imgurl` varchar(255) NOT NULL,
  `createtime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `persist_picture` (
  `ID` varchar(32) NOT NULL,
  `picture_data` longtext COMMENT '持久化图片详情',
  `picture_type` varchar(2) DEFAULT NULL COMMENT '1案件线索',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='持久化图片表';

CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `Long_number` varchar(256) DEFAULT NULL COMMENT '业务行政部门代码',
  `dept_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `dept_number` varchar(256) DEFAULT NULL COMMENT '部门编码',
  `parent_id` bigint(8) DEFAULT NULL COMMENT '父节点id',
  `dept_level` bigint(8) DEFAULT NULL COMMENT '层级',
  `display_name` varchar(256) DEFAULT NULL COMMENT '显示名称',
  `is_leaf` bigint(8) DEFAULT NULL COMMENT '是否叶子节点',
  `dept_state` bigint(8) DEFAULT NULL COMMENT '状态 0 禁用1启用',
  `admin_identity` bigint(8) DEFAULT NULL COMMENT '行政组织代码',
  `address` varchar(512) DEFAULT NULL COMMENT '地址',
  `tel` bigint(20) DEFAULT NULL COMMENT '联系电话',
  `is_biz_org` bigint(8) DEFAULT NULL COMMENT '是否业务行政部门 0 否 1 是',
  `biz_code` varchar(128) DEFAULT NULL COMMENT '行政部门id',
  `seq` bigint(8) DEFAULT NULL COMMENT '序号',
  `description` bigint(8) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '端口2',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_user_id` bigint(20) DEFAULT NULL COMMENT '最新更新用户id',
  `last_updated_time` datetime DEFAULT NULL COMMENT '最新更新时间',
  `ctrl_unit_id` varchar(32) DEFAULT NULL COMMENT '行政区域id',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76739392 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='部门表';


CREATE TABLE `sys_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `module_name` varchar(100) DEFAULT NULL COMMENT '模块名称',
  `module_url` varchar(200) DEFAULT NULL COMMENT '模块url',
  `action_type` int(1) DEFAULT NULL COMMENT '操作类型 1查询 2新增 3修改 4删除 5登录 6退出 7其他 ',
  `user_name` varchar(20) DEFAULT NULL COMMENT '创建用户用户名',
  `real_name` varchar(20) DEFAULT NULL COMMENT '创建用户',
  `ipaddr` varchar(30) DEFAULT NULL COMMENT 'ip地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `c1` varchar(30) DEFAULT NULL COMMENT '预留字段1',
  `c2` varchar(30) DEFAULT NULL COMMENT '预留字段2',
  `c3` varchar(30) DEFAULT NULL COMMENT '预留字段3',
  `c4` varchar(30) DEFAULT NULL COMMENT '预留字段4',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2125 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='日志表';


CREATE TABLE `sys_module` (
  `module_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '模块id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `module_url` varchar(512) DEFAULT NULL COMMENT '链接',
  `state` varchar(8) DEFAULT NULL COMMENT '状态',
  `is_visible` varchar(8) DEFAULT NULL COMMENT '是否可见',
  `actions` varchar(256) DEFAULT NULL,
  `long_number` varchar(256) DEFAULT NULL COMMENT '备注',
  `module_level` bigint(8) DEFAULT NULL COMMENT '部门id',
  `display_name` varchar(512) DEFAULT NULL COMMENT '显示名称',
  `leaf` bigint(8) DEFAULT NULL COMMENT '是否叶子',
  `seq` bigint(8) DEFAULT NULL COMMENT '排序',
  `module_name` varchar(512) DEFAULT NULL COMMENT '名称',
  `module_number` varchar(512) DEFAULT NULL COMMENT '编号',
  `module_description` varchar(512) DEFAULT NULL COMMENT '描述',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_user_id` bigint(20) DEFAULT NULL COMMENT '最新更新用户id',
  `last_updated_time` datetime DEFAULT NULL COMMENT '最新更新时间',
  `ctrl_unit_id` varchar(32) DEFAULT NULL COMMENT '部门id',
  `module_icon` varchar(512) DEFAULT NULL COMMENT '图标',
  `is_dsiplay` varchar(8) DEFAULT NULL COMMENT '是否首页显示',
  `display_order` bigint(8) DEFAULT NULL COMMENT '显示顺序',
  `info1` varchar(256) DEFAULT NULL COMMENT '扩展字段',
  `info2` varchar(256) DEFAULT NULL COMMENT '扩展字段',
  `info3` varchar(256) DEFAULT NULL COMMENT '扩展字段',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` varchar(10) DEFAULT NULL COMMENT '菜单类型,menu:菜单,button:按钮',
  PRIMARY KEY (`module_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1495011517954 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='模块表';


CREATE TABLE `sys_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `visit_num` int(4) NOT NULL COMMENT '访问次数',
  `operate_ip` varchar(20) NOT NULL COMMENT '操作的IP',
  `operate_module` varchar(255) NOT NULL COMMENT '操作的模块',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户操作日志';

CREATE TABLE `sys_permission` (
  `permission_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `resource_id` bigint(20) DEFAULT NULL COMMENT '资源id',
  `resource_name` varchar(128) DEFAULT NULL COMMENT '资源名称',
  `resource_type` bigint(8) DEFAULT NULL COMMENT '资源类型',
  `action_id` bigint(8) DEFAULT NULL COMMENT '动作',
  `action_name` varchar(128) DEFAULT NULL,
  `function_code` varchar(64) DEFAULT NULL COMMENT '功能编码',
  `permission_name` varchar(128) DEFAULT NULL COMMENT '权限名',
  `permission_sign` varchar(128) DEFAULT NULL COMMENT '权限标识,程序中判断使用,如"user:create"',
  `description` varchar(256) DEFAULT NULL COMMENT '权限描述,UI界面显示使用',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1494399268501 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='权限表';


CREATE TABLE `sys_role_permission` (
  `role_permission_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=96843614 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色与权限关联表';

CREATE TABLE `sys_role_permission_dev` (
  `role_permission_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=99794879 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色与权限关联表';

CREATE TABLE `sys_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户Token';



CREATE TABLE `t_analysetask` (
  `TASK_ID` varchar(255) NOT NULL COMMENT '任务ID',
  `FILE_ID` varchar(255) DEFAULT NULL,
  `TASK_NAME` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `TASK_DES` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `TAST_STATUS` decimal(5,0) DEFAULT NULL COMMENT '任务状态： 1 待提交 2 正在处理 3已完成 4处理失败',
  `ANALYSE_PROGRESS` decimal(5,0) DEFAULT NULL COMMENT '分析进度',
  `ANALYSE_PARAM` longtext COMMENT '分析参数',
  `FLOW_NUMBER` varchar(255) DEFAULT NULL,
  `SUBMIT_TIME` datetime DEFAULT NULL COMMENT '提交时间',
  `FINISH_TIME` datetime DEFAULT NULL COMMENT '完成时间',
  `LASTUPDATE_TIME` datetime DEFAULT NULL COMMENT '最后更改时间',
  `HTTP_URLPARAM` varchar(255) DEFAULT NULL,
  `USER_DATA` varchar(255) DEFAULT NULL COMMENT '用户参数',
  `MAIN_ID` varchar(32) DEFAULT NULL COMMENT '主任务ID',
  `CREATE_USER_ID` varchar(32) DEFAULT NULL COMMENT '创建用户',
  `TASK_TYPE` decimal(5,0) DEFAULT NULL COMMENT '任务类型 1 目标检索  2 视频浓缩',
  `TARGET_TYPE` decimal(5,0) DEFAULT NULL COMMENT '检索类型： 1 检索人 2 检索车 3 人骑车 4 以图搜图 5 浓缩',
  `ANALYSIS_TASK_ID` varchar(255) DEFAULT NULL COMMENT '分析服务器任务ID',
  `C1` varchar(32) DEFAULT NULL,
  `C2` varchar(255) DEFAULT NULL,
  `C3` varchar(32) DEFAULT NULL,
  `C4` varchar(32) DEFAULT NULL,
  `C5` varchar(32) DEFAULT NULL,
  `C6` varchar(32) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `camera_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`TASK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_relaytrack_taskdetail` (
  `taskdetail_id` varchar(32) DEFAULT NULL COMMENT '任务ID',
  `task_id` varchar(32) DEFAULT NULL COMMENT '主任务ID',
  `camera_id` varchar(32) DEFAULT NULL COMMENT '点位',
  `camera_address` varchar(32) DEFAULT NULL COMMENT '点位编码',
  `start_time` datetime DEFAULT NULL COMMENT '视频开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '视频结束时间',
  `file_id` varchar(32) DEFAULT NULL COMMENT '从天网下载的文件ID',
  `video_id` varchar(32) DEFAULT NULL COMMENT '提交转码文件ID',
  `transcode_id` varchar(32) DEFAULT NULL COMMENT '转码ID',
  `transcode_progress` varchar(32) DEFAULT NULL COMMENT '转码进度',
  `analy_task_id` varchar(32) DEFAULT NULL COMMENT '分析任务ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `slave_ip` varchar(32) DEFAULT NULL COMMENT '分析服务器IP',
  `file_name` varchar(255) DEFAULT NULL COMMENT '下载文件名',
  `file_down_name` varchar(255) DEFAULT NULL,
  `file_down_progress` varchar(32) DEFAULT NULL,
  `c1` varchar(255) DEFAULT NULL,
  `c2` varchar(32) DEFAULT NULL,
  `c3` varchar(32) DEFAULT NULL,
  `c4` varchar(255) DEFAULT NULL,
  `c5` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_analysis_detail` (
  `id` varchar(40) NOT NULL COMMENT '任务ID',
  `task_id` varchar(64) NOT NULL COMMENT '关联tb_analysis_task表任务ID',
  `progress` smallint(6) DEFAULT NULL COMMENT '任务进度',
  `entry_time` varchar(20) DEFAULT NULL COMMENT '校准时间',
  `analysis_url` varchar(255) DEFAULT NULL COMMENT '任务分析地址',
  `analysis_id` varchar(255) DEFAULT NULL COMMENT '任务分析serialnumber',
  `analysis_status` smallint(6) DEFAULT NULL COMMENT '任务状态： 1 待提交 2 正在处理 3已完成 4处理失败',
  `analysis_progress` smallint(6) DEFAULT NULL COMMENT '分析进度',
  `download_url` varchar(255) DEFAULT NULL COMMENT '录像下载地址',
  `download_id` varchar(255) DEFAULT NULL COMMENT '录像下载id',
  `download_status` smallint(6) DEFAULT NULL COMMENT '下载状态： 1 待提交 2 正在处理 3已完成 4处理失败',
  `download_progress` smallint(6) DEFAULT NULL COMMENT '下载进度',
  `download_file` varchar(255) DEFAULT NULL COMMENT '录像下载后文件信息',
  `download_retry` smallint(6) DEFAULT NULL COMMENT '录像下载重试次数',
  `transcode_url` varchar(255) DEFAULT NULL COMMENT '提交转码url地址',
  `transcode_id` varchar(255) DEFAULT NULL COMMENT '转码id',
  `transcode_status` smallint(6) DEFAULT NULL COMMENT '任务状态： 1 待提交 2 正在处理 3已完成 4处理失败',
  `transcode_progress` smallint(6) DEFAULT NULL COMMENT '转码进度',
  `transcode_file` varchar(255) DEFAULT NULL COMMENT '转码后文件信息',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `lastupdate_time` datetime DEFAULT NULL COMMENT '最后更改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_analysis_task` (
  `id` varchar(64) NOT NULL COMMENT '任务ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '任务名称',
  `desc` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `analy_type` varchar(20) DEFAULT NULL COMMENT '分析类型 objext vlpr',
  `analy_param` varchar(2000) DEFAULT NULL COMMENT '分析参数',
  `slice_number` smallint(6) DEFAULT NULL COMMENT '分片数量',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `lastupdate_time` datetime DEFAULT NULL COMMENT '最后更改时间',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建用户',
  `task_type` smallint(6) DEFAULT NULL COMMENT '任务类型 1 实时流分析  2 离线文件分析  3 录像分析',
  `camera_id` varchar(20) DEFAULT NULL COMMENT '所属监控点id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` smallint(6) DEFAULT NULL COMMENT '任务状态 0 运行中 1 已停止 2 已删除',
  `device_id` varchar(128) DEFAULT NULL COMMENT '设备ID',
  PRIMARY KEY (`id`),
  KEY `idx_camera` (`camera_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_case` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `case_code` varchar(50) NOT NULL COMMENT '案件编号',
  `case_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '案件名称',
  `case_option_id` varchar(32) DEFAULT NULL COMMENT '案件类别',
  `case_status` int(2) NOT NULL COMMENT '案件状态：0 已处警 1 已受理 2 已立案 3 已破案 4 已结案 5 已销案 6 已不立 7 已移交 8 已破未结 9 撤案转行政处罚 50 不处理 51 已调解 52 已终止 59 已终结 60 已处罚 61 已受理未结 62 当场处罚 20 审查中 21 已审查 99 其他',
  `case_detail` varchar(2048) DEFAULT NULL COMMENT '案件详情',
  `case_start_time` datetime DEFAULT NULL COMMENT '案发时间',
  `case_end_time` datetime DEFAULT NULL COMMENT '结案时间',
  `case_location` varchar(255) DEFAULT NULL COMMENT '案发地点',
  `case_handle_user` varchar(255) DEFAULT NULL COMMENT '办案人员',
  `case_handle_investor` varchar(255) DEFAULT NULL COMMENT '协办侦查员',
  `case_longitude` varchar(32) DEFAULT NULL COMMENT '经度',
  `case_latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
  `case_station_id` varchar(50) DEFAULT NULL COMMENT '所属派出所编码',
  `track_image_url` varchar(255) DEFAULT NULL COMMENT '目标轨迹图',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识  1:已删除  0:正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人编号',
  `version` int(12) NOT NULL DEFAULT '0' COMMENT '版本号',
  `last_update_time` datetime NOT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件表';

CREATE TABLE `tb_case_archive_pic` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(50) NOT NULL COMMENT '图片名称/视频名称',
  `obj_type` int(2) NOT NULL COMMENT '图片类型objType(1:人,2:车辆,4:人骑车 )',
  `pic_id` varchar(32) NOT NULL COMMENT '图片Id',
  `pic_big_path` varchar(255) DEFAULT NULL COMMENT '文件远程路径',
  `pic_info` varchar(2000) NOT NULL COMMENT '图片的标签数据',
  `case_code` varchar(50) NOT NULL COMMENT '关联案件编号',
  `pic_thumb_path` varchar(255) DEFAULT NULL COMMENT '文件本地路径',
  `happen_period` int(2) NOT NULL COMMENT '发生时间段：0 事前 1事中 2事后',
  `serialnumber` varchar(64) NOT NULL COMMENT '分析序列号',
  `camera_id` bigint(32) NOT NULL COMMENT '关联监控点编号',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识  1:已删除  0:正常',
  `version` int(2) NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人编号',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件图片归档表';

CREATE TABLE `tb_case_archive_video` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(50) NOT NULL COMMENT '图片名称/视频名称',
  `file_type` int(2) NOT NULL COMMENT '文件类型：1 实时；2 离线',
  `file_remote_path` varchar(255) DEFAULT NULL COMMENT '文件远程路径',
  `file_local_path` varchar(255) DEFAULT NULL COMMENT '文件本地路径',
  `happen_period` int(2) NOT NULL DEFAULT '1' COMMENT '发生时间段：0事前 1事中 2事后',
  `is_proof` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否证据视频(0 否 1 是)',
  `video_start_time` datetime DEFAULT NULL COMMENT '视频开始时间',
  `video_end_time` datetime DEFAULT NULL COMMENT '视频结束时间',
  `relate_picture_url` varchar(255) NOT NULL COMMENT '视频关联图片url',
  `case_code` varchar(50) NOT NULL COMMENT '关联案件编号',
  `serialnumber` varchar(64) NOT NULL COMMENT '分析序列号',
  `camera_id` bigint(32) NOT NULL COMMENT '关联监控点编号',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识  1:已删除  0:正常',
  `version` int(2) NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人编号',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `video_download_id` varchar(40) DEFAULT NULL COMMENT '视频下载任务Id，关联tb_case_video_download表Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件视频归档表';

CREATE TABLE `tb_case_video_download` (
  `id` varchar(40) NOT NULL COMMENT '主键，下载任务ID',
  `progress` smallint(6) DEFAULT NULL COMMENT '任务进度',
  `entry_time` varchar(20) DEFAULT NULL COMMENT '校准时间',
  `transfer_url` varchar(255) DEFAULT NULL COMMENT '视频转移地址',
  `transfer_id` varchar(255) DEFAULT NULL COMMENT '转移ID',
  `transfer_status` smallint(6) DEFAULT NULL COMMENT '转移状态： 1 等待处理 2 已完成 3 处理失败',
  `transfer_retry` smallint(6) DEFAULT NULL COMMENT '转移重试次数',
  `download_url` varchar(255) DEFAULT NULL COMMENT '录像下载地址',
  `download_id` varchar(255) DEFAULT NULL COMMENT '录像下载id',
  `download_status` smallint(6) DEFAULT NULL COMMENT '下载状态： 1 待提交 2 正在处理 3已完成 4处理失败',
  `download_progress` smallint(6) DEFAULT NULL COMMENT '下载进度',
  `download_file` varchar(255) DEFAULT NULL COMMENT '录像下载后文件信息',
  `download_retry` smallint(6) DEFAULT NULL COMMENT '录像下载重试次数',
  `transcode_url` varchar(255) DEFAULT NULL COMMENT '提交转码url地址',
  `transcode_id` varchar(255) DEFAULT NULL COMMENT '转码id',
  `transcode_status` smallint(6) DEFAULT NULL COMMENT '任务状态： 0等待 1完成 2转码中 3失败',
  `transcode_progress` smallint(6) DEFAULT NULL COMMENT '转码进度',
  `transcode_file` varchar(255) DEFAULT NULL COMMENT '转码后文件信息',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `lastupdate_time` datetime DEFAULT NULL COMMENT '最后更改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件视频下载表';


CREATE TABLE `tb_imagesearch_record` (
  `id` varchar(40) NOT NULL COMMENT '记录id，主键',
  `task_id` varchar(40) DEFAULT NULL COMMENT '任务编号',
  `create_date` varchar(20) DEFAULT NULL COMMENT '结果记录创建日期',
  `record_id` varchar(64) DEFAULT NULL COMMENT '记录uuid',
  `type` smallint(6) DEFAULT NULL COMMENT '类型：1-置顶；2-删除',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `score` varchar(20) DEFAULT NULL COMMENT '相似度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_imagesearch_task` (
  `id` varchar(40) NOT NULL COMMENT '任务ID',
  `name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `desc` varchar(2000) DEFAULT NULL COMMENT '任务描述',
  `case_id` varchar(255) DEFAULT NULL COMMENT '案件编号',
  `start_time` datetime DEFAULT NULL COMMENT '搜图起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '搜图结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `obj_type` smallint(6) DEFAULT NULL COMMENT '目标类型：1-人；2-骑；4-车',
  `picture` mediumtext DEFAULT NULL COMMENT '搜图图片',
  `feature` mediumtext COMMENT '搜图图片特征base64',
  `status` smallint(6) DEFAULT NULL COMMENT '任务状态 0 正常 1 已删除',
  `qcamera_ids` mediumtext COMMENT '搜图过滤条件',
  `remark` mediumtext DEFAULT NULL COMMENT '备注',
  `camera_ids` mediumtext DEFAULT NULL,
  `camera_names` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_monitor_group` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '监控组名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='专项监控组表';

CREATE TABLE `tb_monitor_group_detail` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `monitor_group_id` bigint(32) NOT NULL COMMENT '监控组ID',
  `camera_id` bigint(20) NOT NULL COMMENT '监控点ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人编号',
  PRIMARY KEY (`id`),
  KEY `index_mp_id` (`monitor_group_id`),
  KEY `index_camera_id` (`camera_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='专项监控组详情表';

CREATE TABLE `tb_relaytrack_detail` (
  `id` varchar(40) NOT NULL COMMENT '任务ID',
  `task_id` varchar(40) NOT NULL COMMENT '关联tb_relaytrack_task表任务ID',
  `camera_id` varchar(20) DEFAULT NULL COMMENT '所属监控点id',
  `camera_name` varchar(255) DEFAULT NULL COMMENT '所属监控点名称',
  `analysis_id` varchar(64) DEFAULT NULL COMMENT '任务分析serialnumber',
  `analysis_status` smallint(6) DEFAULT NULL COMMENT '任务状态： 1 分析中 2 分析完成 3 分析失败',
  `analysis_progress` smallint(6) DEFAULT NULL COMMENT '分析进度',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `status` smallint(6) DEFAULT NULL COMMENT '任务状态 0 运行中 1 成功 2 失败',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_relaytrack_task` (
  `id` varchar(40) NOT NULL COMMENT '任务ID',
  `name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `desc` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `case_id` varchar(255) DEFAULT NULL COMMENT '案件编号',
  `start_time` datetime DEFAULT NULL COMMENT '录像起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '录像结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `lastupdate_time` datetime DEFAULT NULL COMMENT '最后更改时间',
  `obj_type` smallint(6) DEFAULT NULL COMMENT '目标类型：1-人；2-骑；4-车',
  `picture` varchar(1024) DEFAULT NULL COMMENT '搜图图片',
  `progress` smallint(6) DEFAULT NULL COMMENT '任务总进度',
  `status` smallint(6) DEFAULT NULL COMMENT '任务状态 0 运行中 1 已完成 2 已删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `feature` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_vas_task_log` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` int(4) NOT NULL DEFAULT '10' COMMENT '状态(10:成功；-10：失败)',
  `msg` varchar(255) DEFAULT NULL COMMENT '返回信息',
  `vas_cnt` int(12) NOT NULL DEFAULT '0' COMMENT 'vas数量',
  `vas_insert_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '新增vas数量',
  `vas_update_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '更新vas数量',
  `vas_delete_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '删除vas数量',
  `org_cnt` int(12) NOT NULL DEFAULT '0' COMMENT 'org数量',
  `org_insert_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '新增org数量',
  `org_update_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '更新org数量',
  `org_delete_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '删除org数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

CREATE TABLE `vlpr_picture_recog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `taskId` varchar(64) NOT NULL COMMENT 'vlpr_task关联id,对应serialnumber',
  `cameraId` int(11) NOT NULL DEFAULT '0' COMMENT '监控点id',
  `imageUrl` varchar(200) NOT NULL COMMENT '图片相对url',
  `downTime` int(11) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '识别状态 0-等待处理 1-成功 2-下载失败 3-结构化失败 4-未提取到任务车辆特征 5-保存或者推送至Kafka失败 9-处理中',
  `realTime` datetime DEFAULT NULL COMMENT '过车时间',
  `extendId` varchar(26) DEFAULT NULL COMMENT '第三方关联key,对应id',
  `extendInfo` varchar(500) DEFAULT NULL COMMENT '第三方记录信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆卡口图片识别表';

CREATE TABLE `vsd_task_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` bigint(20) unsigned NOT NULL COMMENT 'vds_task_id',
  `serialnumber` varchar(64) NOT NULL DEFAULT '' COMMENT '序列号，由外部定义',
  `camera_file_id` bigint(20) unsigned NOT NULL COMMENT '点位文件id',
  `from_type` bigint(8) unsigned DEFAULT '1' COMMENT '视频来源 1 点位 2离线视频',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  `createuser` bigint(20) DEFAULT NULL,
  `c1` varchar(32) DEFAULT NULL,
  `c2` varchar(32) DEFAULT NULL,
  `task_status` smallint(6) DEFAULT NULL COMMENT '分析状态',
  `isvalid` smallint(6) DEFAULT NULL COMMENT '启动状态',
  `task_progress` smallint(6) DEFAULT NULL COMMENT '分析进度',
  `type` varchar(8) DEFAULT NULL COMMENT '类型',
  `del_flag` smallint(8) DEFAULT NULL COMMENT '删除状态',
  `remark` varchar(1000) DEFAULT NULL,
  `task_name` varchar(300) DEFAULT NULL COMMENT '任务名称',
  `task_user_id` varchar(100) DEFAULT NULL COMMENT '任务用户标识',
  `camera_id` varchar(20) DEFAULT NULL COMMENT '监控点id',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `source_id` varchar(255) DEFAULT NULL COMMENT '任务去向,ks为指向视图库',
  `url` varchar(200) DEFAULT NULL,
  `slaveip` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `serilanumber` (`serialnumber`),
  KEY `task_id` (`task_id`),
  KEY `idx_camera` (`camera_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1567502779119 DEFAULT CHARSET=utf8;
