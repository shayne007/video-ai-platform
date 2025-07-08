-- ----------------------------
-- Table structure for `camera`
-- ----------------------------
DROP TABLE IF EXISTS `camera`;
CREATE TABLE `camera` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '点位id',
  `name` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '点位名称',
  `cameratype` bigint(8) DEFAULT NULL COMMENT '点位类型',
  `category` bigint(8) DEFAULT NULL COMMENT '点位类别 [1卡口, 2电警, 0其他]',
  `type` bigint(8) DEFAULT NULL COMMENT '机型',
  `region` varchar(32) DEFAULT NULL COMMENT '行政区域',
  `longitude` varchar(32) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
  `direction` varchar(256) DEFAULT NULL COMMENT '方向',
  `location` varchar(256) DEFAULT NULL COMMENT '地点',
  `status` bigint(8) DEFAULT NULL COMMENT '状态',
  `dsc` varchar(256) DEFAULT NULL COMMENT '描述',
  `brandid` bigint(8) DEFAULT '1' COMMENT '品牌id',
  `brandname` varchar(128) DEFAULT NULL COMMENT '品牌名称',
  `model` varchar(128) DEFAULT NULL COMMENT '型号',
  `ip` varchar(128) DEFAULT NULL COMMENT 'ip地址',
  `port1` bigint(8) DEFAULT NULL COMMENT '端口1',
  `port2` bigint(8) DEFAULT NULL COMMENT '端口2',
  `account` varchar(64) DEFAULT NULL COMMENT '登陆账号',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `channel` bigint(8) DEFAULT NULL COMMENT '通道',
  `extcameraid` varchar(50) DEFAULT NULL COMMENT '设备编号',
  `admindept` varchar(128) DEFAULT NULL COMMENT '管理责任单位',
  `admin` varchar(64) DEFAULT NULL COMMENT '责任人',
  `telephone` varchar(64) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(256) DEFAULT NULL COMMENT '联系地址',
  `url` varchar(256) DEFAULT NULL COMMENT 'url',
  `follwarea` varchar(256) DEFAULT NULL COMMENT '允许区域',
  `thumb_nail` varchar(512) DEFAULT NULL COMMENT '缩略图',
  `cameragroupid` bigint(20) DEFAULT NULL COMMENT '监控点组id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_userid` int(11) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `modify_userid` int(11) DEFAULT NULL,
  `alarm_threshold` int(11) DEFAULT NULL COMMENT '布控告警阈值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1569319722113 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='视频点位表';

-- ----------------------------
-- Records of camera
-- ----------------------------
-- INSERT INTO `camera` VALUES ('1234567891011', '默认监控点', '2', null, '1', '42407631', '112.97601699829102', '28.195033121678538', null, null, '1', null, '2', null, null, null, null, null, null, null, null, null, null, null, null, null, '1569319722111', null, null, null, '2019-09-01 14:29:39', null, null, null);

-- ----------------------------
-- Table structure for `cfg_mem_props`
-- ----------------------------
DROP TABLE IF EXISTS `cfg_mem_props`;
CREATE TABLE `cfg_mem_props` (
  `module_name` varchar(32) NOT NULL COMMENT '模块名',
  `prop_key` varchar(64) NOT NULL COMMENT '参数key',
  `prop_name` varchar(128) DEFAULT NULL COMMENT '参数name',
  `prop_value` varchar(256) DEFAULT NULL COMMENT '参数值',
  `prop_desc` varchar(1024) DEFAULT NULL COMMENT '参数描述',
  `update_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`module_name`,`prop_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='属性配置表';


DELETE from cfg_mem_props;
-- ----------------------------
-- Records of cfg_mem_props
-- ----------------------------
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', '1000video.ip', '其他类别@点位同步服务ip', '', '其他类别@点位同步服务ip', '2019-08-08 11:37:59');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', '1000video.port', '其他类别@点位同步服务端口', '8060', '其他类别@点位同步服务端口', '2019-08-08 11:10:59');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'ftp-server-httpurl', 'FTP类别@ftp 服务器的 http 访问方式', '127.0.0.1:8081', '请配置 ftp 服务器的http url 地址', '2019-10-21 11:01:58');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'ftp_server_timeout', 'FTP类别@ftp 服务器请求超时时间毫秒', '30000', '服务器请求超时时间,默认5秒超时', '2019-07-23 12:44:42');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'standard.ip', 'VAS类别@视图库ip', '127.0.0.1', 'VAS类别@视图库地址(默认本机)', '2019-10-18 19:54:11');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'standard.port', 'VAS类别@视图库端口', '9999', 'VAS类别@视图库端口', '2019-10-12 10:46:55');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'sync-camera-thumbNail', '其他类别@是否获取点位快照', 'true', '是否开启获取点位快照true|false', '2019-10-09 14:13:56');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task-authorize-connect-number', '其他类别@系统授权数量', '100', '其他类别@系统授权联网实时视频的路数之和', '2019-09-30 11:41:08');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'warning.alarm.threshold', '其他类别@人群密度告警阈值', '1', '请配置人群密度告警阈值', '2019-10-18 17:37:20');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'warning.busy.threshold', '其他类别@人群密度繁忙阈值', '0', '请配置人群密度繁忙阈值', '2019-10-18 17:48:34');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'warning.crowd.threshold', '其他类别@人群密度拥挤阈值', '1', '请配置人群密度拥挤阈值', '2019-10-18 17:48:36');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'ws2.port', 'VAS类别@MAS2版本国标流端口', '9080', '请配置国标流端口(模拟流，nva流等)MAS-Master服务', '2019-09-26 17:52:13');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'ws2.server', 'VAS类别@MAS2版本国标流ip', '127.0.0.1', '请配置国标流ip(模拟流，nva流等)MAS-Master服务(默认本机)', '2019-10-18 15:46:40');

INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task.detection_frame_skip_interval', '任务参数@任务视频帧分析间距', '150', '视频帧分析间距，在分析单一视频帧后，会忽略此数目的视频帧不作分析', '2019-11-08 11:54:34');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task.detection_sale_factor', '任务参数@任务图像缩放倍数', '2', '取值范围[1,4]，默认为1', '2019-11-08 13:33:44');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task.enable_density_map_output', '任务参数@任务是否输出热力图', 'true', '开启|关闭：true|false', '2019-11-08 13:34:22');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task.heatmap_weight', '任务参数@任务热力图比率', '0', '取值范围[0,1]认0.4', '2019-11-08 13:35:09');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task.push_frame_max_wait_time', '任务参数@任务最大等候时间(ms)', '1', '取值范围[0.10000],默认为0', '2019-11-08 11:59:49');
INSERT INTO `cfg_mem_props` VALUES ('qst_densecrowd', 'task.callback_url', '任务参数@报警数据推送', '127.0.0.1:9801/densecrowd/spider/cloudWalkService/density/alarm/send', '报警数据推送API接口,POST请求', '2019-11-08 11:59:49');
INSERT INTO `cfg_mem_props`(`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_densecrowd', 'task.dynamic_day', '其他类别@动态报警统计天数', '7', '动态报警统计天数，默认7天', '2020-07-27 21:32:44');
INSERT INTO `cfg_mem_props`(`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_densecrowd', 'task.dynamic_rate', '其他类别@动态报警比例', '1.1', '动态报警比例，默认1.1倍', '2020-07-27 21:38:41');
INSERT INTO `cfg_mem_props`(`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_densecrowd', 'task.delete_alarm_record', '其他类别@历史告警保存天数', '30', '历史告警保存天数,填0的时候不删除', '2020-07-27 21:38:41');

-- ----------------------------
-- Table structure for `ctrl_unit`
-- ----------------------------
DROP TABLE IF EXISTS `ctrl_unit`;
CREATE TABLE `ctrl_unit` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '组织机构id',
  `unit_state` bigint(8) DEFAULT NULL COMMENT '状态 枚举字段说明 [0:禁用];[1:启用]',
  `unit_identity` varchar(32) DEFAULT NULL COMMENT '行政区划编码',
  `org_type` varchar(256) DEFAULT NULL COMMENT '组织类型',
  `share_unit_id` bigint(8) DEFAULT NULL COMMENT '数据维护组织',
  `long_number` varchar(256) DEFAULT NULL COMMENT '长编码',
  `unit_level` bigint(8) DEFAULT NULL COMMENT '层级',
  `display_name` varchar(256) DEFAULT NULL COMMENT '状态 0 禁用1启用',
  `is_leaf` bigint(8) DEFAULT NULL COMMENT '是否叶子节点 0否 1是',
  `seq_num` bigint(8) DEFAULT NULL COMMENT '序号',
  `unit_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `unit_number` varchar(256) DEFAULT NULL COMMENT '备注',
  `unit_description` varchar(256) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '端口2',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_user_id` bigint(20) DEFAULT NULL COMMENT '最新更新用户id',
  `last_updated_time` datetime DEFAULT NULL COMMENT '最新更新时间',
  `ctrl_unit_id` varchar(32) DEFAULT NULL COMMENT '行政区域id',
  `unit_parent_id` varchar(32) DEFAULT NULL COMMENT '父行政区域编码，如果是顶级区域则为空',
  PRIMARY KEY (`id`),
  KEY `unit_identity` (`unit_identity`)
) ENGINE=InnoDB AUTO_INCREMENT=1506153718002 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='组织机构表';

-- ----------------------------
-- Records of ctrl_unit
-- ----------------------------
INSERT INTO `ctrl_unit` VALUES ('1506153675192', '1', '80745444', null, null, '80745444', '1', '社会点位列表', '0', null, '社会点位列表', '80745444', '', null, null, null, null, null, null);
INSERT INTO `ctrl_unit` VALUES ('1506153698283', '1', '99394041', null, null, '80745444!99394041', '2', '默认区域', '0', null, '默认区域', '99394041', '', null, null, null, null, null, '80745444');
INSERT INTO `ctrl_unit` VALUES ('1506153718001', '1', '42407631', null, null, '80745444!99394041!42407631', '3', '默认街道', '1', null, '默认街道', '42407631', '', null, null, null, null, null, '99394041');

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
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
) ENGINE=InnoDB AUTO_INCREMENT=2483 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
-- Table structure for `sys_module_dev`
-- ----------------------------
DROP TABLE IF EXISTS `sys_module_dev`;
CREATE TABLE `sys_module_dev` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1495011518017 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='模块表';

-- ----------------------------
DELETE from sys_module_dev;
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518014, 0, '', '1', NULL, NULL, NULL, 1, '实时感知', NULL, NULL, '实时感知', 'person_density_perception', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-renqunmidu', '1', 1, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518015, 0, '', '1', NULL, NULL, NULL, 1, '历史告警', NULL, NULL, '历史告警', 'target_retrieval', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-baojing', '1', 2, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518016, 0, NULL, '1', NULL, NULL, NULL, 1, '告警配置', NULL, NULL, '告警配置', 'manage_config', NULL, NULL, NULL, NULL, NULL, '', 'icon-shezhi', '1', 5, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518017, 0, NULL, '1', NULL, NULL, NULL, 1, '行政区域', NULL, NULL, '行政区域', 'area_mgmt', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-zuzhijiagou', '1', 4, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518018, 0, NULL, '1', NULL, NULL, NULL, 1, '点位管理', NULL, NULL, '点位管理', 'camera_mgmt', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-wulianwang', '1', 3, NULL, NULL, NULL, NULL, 'meun');

-- ----------------------------
-- Table structure for `sys_token`
-- ----------------------------
DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户Token';


-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `password` char(64) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `tel` varchar(64) DEFAULT NULL COMMENT '电话',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `dept_id` varchar(64) DEFAULT NULL COMMENT '部门id',
  `isvalid` varchar(32) DEFAULT NULL COMMENT '状态',
  `is_dept_admin` varchar(32) DEFAULT NULL COMMENT '部门管理员',
  `themes` varchar(64) DEFAULT NULL COMMENT '主题',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', null, null, null, '1', '1', null, null, null, '2014-07-17 12:59:08');
INSERT INTO `sys_user` VALUES ('2', 'admintest', 'e10adc3949ba59abbe56e057f20f883e', '系统测试员', null, null, null, '1', '1', null, null, null, '2016-12-17 12:59:08');

-- ----------------------------
-- Table structure for `vsd_task_relation`
-- ----------------------------
DROP TABLE IF EXISTS `vsd_task_relation`;
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
  `alarm_threshold` int(11) DEFAULT '0' COMMENT 'alarmThreshold',
  `alarm_start_time` datetime DEFAULT NULL COMMENT '布控开始时间（为空则一直布控）',
  `alarm_end_time` datetime DEFAULT NULL,
  `alarm_interval` int(11) DEFAULT NULL COMMENT '报警间隔时间（分钟）',
  PRIMARY KEY (`id`),
  KEY `serilanumber` (`serialnumber`),
  KEY `task_id` (`task_id`),
  KEY `idx_camera` (`camera_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1570795166235 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `densecrowd_warn_result`;
CREATE TABLE `densecrowd_warn_result`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pic_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `camera_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `density_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `head_position` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `serialnumber` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `insert_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `alarm_threshold` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

DROP TABLE IF EXISTS `densecrowd_daily_max_report`;
CREATE TABLE `densecrowd_daily_max_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serialnumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `max_count` int(11) DEFAULT NULL,
  `total_count` int(11) DEFAULT NULL,
  `report_date` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `insert_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
);

