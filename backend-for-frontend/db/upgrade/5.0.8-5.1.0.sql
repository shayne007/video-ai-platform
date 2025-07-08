ALTER TABLE ctrl_unit_file ADD COLUMN video_type  smallint(4) NULL DEFAULT NULL COMMENT '';

ALTER TABLE vsd_task_relation ADD COLUMN overline_type  smallint(6) NULL DEFAULT NULL COMMENT '异常行为检测标志';

ALTER TABLE ctrl_unit_file ADD COLUMN overline_type  smallint(6) NULL DEFAULT NULL COMMENT '异常行为检测标志';

UPDATE `sys_module_dev` SET `module_id`='1495011517968', `parent_id`='0', `module_url`=NULL, `state`='1',`is_visible`=NULL, `actions`=NULL, `long_number`=NULL, `module_level`='1', `display_name`='便捷管理', `leaf`='0', `seq`='10012', `module_name`='便捷管理', `module_number`='1', `module_description`=NULL, `creator_id`=NULL, `create_time`=NULL, `last_update_user_id`=NULL, `last_updated_time`=NULL, `ctrl_unit_id`=NULL, `module_icon`='icon-shijianzhoushezhi', `is_dsiplay`='1', `display_order`='9', `info1`=NULL, `info2`=NULL, `info3`=NULL, `perms`=NULL, `type`='menu' WHERE (`module_id`='1495011517968');

-- 异常行为
INSERT INTO `sys_module_dev` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518014, 0, NULL, '1', NULL, NULL, NULL, 1, NULL, NULL, NULL, '异常行为', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'icon-yichangguanli', '1', 8, NULL, NULL, NULL, NULL, 'menu');
INSERT INTO `sys_module_dev` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES ('1495011518015', '1495011518014', NULL, '1', NULL, NULL, NULL, '2', NULL, NULL, NULL, '异常行为感知', 'event_alarm_perception', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, 'menu');
INSERT INTO `sys_module_dev` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES ('1495011518016', '1495011518014', NULL, '1', NULL, NULL, NULL, '2', NULL, NULL, NULL, '入侵越界检测', 'overline_alarm_perception', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, 'menu');


ALTER TABLE `vsd_task_relation` ADD COLUMN `entry_time`  datetime NULL DEFAULT NULL COMMENT '视频录入时间';

ALTER TABLE `vsd_task_relation` ADD COLUMN `enable_partial`  smallint NULL DEFAULT 0 COMMENT '是否开标注';

-- 卡口菜单, 以及配置
INSERT INTO `sys_module_dev` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES ('1495011518017', '1495011517962', NULL, '1', NULL, NULL, NULL, '3', NULL, NULL, NULL, '卡口实时图片', 'bayonet_analysis', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, 'menu');

INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'kafka.url', '其他类别@卡口任务对接kafka数据', '127.0.0.1:39092', '其他类别@卡口任务对接kafka数据', '2019-08-08 11:37:59');

INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'picturestream.url', '其他类别@调用picturestream服务', '127.0.0.1:8890', '其他类别@调用picturestream服务', '2019-08-08 11:37:59');

INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'kakou.analysis', '其他类别@卡口任务分析启动,0不启动, 1启动分析', '0', '其他类别@卡口任务分析启动,0不启动, 1启动分析', '2019-08-08 11:37:59');

ALTER TABLE `objext_track` ADD COLUMN `search_status` tinyint(1) DEFAULT 0 COMMENT '是否搜图过';
ALTER TABLE `objext_track` ADD COLUMN `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0);



ALTER TABLE `ctrl_unit_file`
ADD COLUMN `interest_param_save`  varchar(1024) NULL DEFAULT NULL COMMENT '前端感兴趣区域保存' AFTER `uninterest_param`,
ADD COLUMN `interest_flag`  smallint(8) NULL DEFAULT NULL COMMENT '感兴趣区域/不感兴趣标志:0为不感兴趣区域,1为感兴趣区域' AFTER `interest_param_save`;

-- 聚类检索
DROP TABLE IF EXISTS `tb_cluster_task`;
CREATE TABLE `tb_cluster_task`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务名称',
  `start_time` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '聚类起始时间',
  `end_time` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '聚类结束时间',
  `create_time` datetime(0) DEFAULT NULL COMMENT '提交时间',
  `obj_type` smallint(1) DEFAULT NULL COMMENT '目标类型：1-人；2-骑；4-车；3-人脸',
  `feature` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '搜图图片特征base64',
  `status` smallint(6) DEFAULT NULL COMMENT '任务状态 0:等待聚类，1：聚类中，2：聚类完成，3聚类失败',
  `qcamera_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '聚类过滤条件',
  `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `camera_ids` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '聚类监控点id,逗号分隔',
  `camera_names` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '聚类监控点名称,逗号分隔',
  `threshold` float DEFAULT NULL COMMENT '阈值(0-1之间)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '聚类检索' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `tb_cluster_task_detail`;
CREATE TABLE `tb_cluster_task_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `pid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '聚类任务id',
  `count_num` int(11) DEFAULT NULL COMMENT '数量统计',
  `create_time` datetime(0) DEFAULT NULL COMMENT '提交时间',
  `result` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '聚类结果集',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '聚类检索结果明细' ROW_FORMAT = Dynamic;

INSERT INTO `sys_module_dev`(`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011518018, 0, '', '1', NULL, NULL, NULL, 1, NULL, NULL, NULL, '聚类', 'cluster_retrieval', NULL, NULL, NULL, NULL, NULL, NULL, 'icon-julei', '1', 6, NULL, NULL, NULL, NULL, 'menu');

ALTER TABLE `objext_track` ADD COLUMN `search_status` tinyint(1) DEFAULT 0 COMMENT '是否搜图过';
ALTER TABLE `objext_track` ADD COLUMN `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0);

-- 增大字段容量
ALTER TABLE `case_camera_media` MODIFY COLUMN `pic_info` mediumtext COMMENT '图片的标签数据';

ALTER TABLE `ctrl_unit_file` ADD COLUMN `enable_independent_face_snap`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'false' COMMENT '独立输出人脸' AFTER `overline_type`;
ALTER TABLE `ctrl_unit_file` ADD COLUMN `scene`  smallint(4) NULL DEFAULT 0 COMMENT '默认场景' AFTER `overline_type`;

INSERT INTO `car_color_model` (`id`, `color_id`, `color_rgb_tag`, `color_bgr_tag`, `color_hex_tag`, `color_name`, `fuzzy_color`, `color_code`, `color_number`) VALUES ('14', '14', '0', '0', NULL, '银', '银', '14', 'color14');
INSERT INTO `car_color_model` (`id`, `color_id`, `color_rgb_tag`, `color_bgr_tag`, `color_hex_tag`, `color_name`, `fuzzy_color`, `color_code`, `color_number`) VALUES ('15', '15', '0', '0', NULL, '金', '金', '15', 'color15');

