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
DELETE from sys_module_dev;
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518014, 0, '', '1', NULL, NULL, NULL, 1, '实时感知', NULL, NULL, '实时感知', 'person_density_perception', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-renqunmidu', '1', 1, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518015, 0, '', '1', NULL, NULL, NULL, 1, '历史告警', NULL, NULL, '历史告警', 'target_retrieval', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-baojing', '1', 2, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518016, 0, NULL, '1', NULL, NULL, NULL, 1, '告警配置', NULL, NULL, '告警配置', 'manage_config', NULL, NULL, NULL, NULL, NULL, '', 'icon-shezhi', '1', 5, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518017, 0, NULL, '1', NULL, NULL, NULL, 1, '行政区域', NULL, NULL, '行政区域', 'area_mgmt', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-zuzhijiagou', '1', 4, NULL, NULL, NULL, NULL, 'meun');
INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518018, 0, NULL, '1', NULL, NULL, NULL, 1, '点位管理', NULL, NULL, '点位管理', 'camera_mgmt', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-wulianwang', '1', 3, NULL, NULL, NULL, NULL, 'meun');


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

ALTER TABLE `camera` ADD COLUMN `alarm_threshold` int(11) DEFAULT 0 COMMENT '布控告警阈值';