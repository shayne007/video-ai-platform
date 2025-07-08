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
) ENGINE=InnoDB AUTO_INCREMENT=1495011518006 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='模块表';

-- ----------------------------
-- Records of sys_module_dev
-- ----------------------------
INSERT INTO `sys_module_dev` VALUES ('1477447508878', '1495011517968', '', '1', '1', '', '1477447508878', '2', '系统管理', '2', '7', '系统管理', 'sysMgt', '', null, '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', 'menu_5', '1', null, '', '', '', null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1477447557140', '1477447508878', null, '1', '1', '', '1477447508878!1477447557140', '3', '系统管理!用户管理', '3', null, '用户管理', 'user_mgt', '', '1', '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', '', '1', '1', '', '', '', null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1477447587460', '1477447508878', null, '1', '1', '', '1477447508878!1477447587460', '3', '系统管理!组织结构管理', '3', null, '组织结构管理', 'organiz_mgt', '', '1', '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', '', '1', '3', '', '', '', null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1477447627233', '1477447508878', null, '1', '1', '', '1477447508878!1477447627233', '3', '系统管理!行政区域管理', '3', null, '行政区域管理', 'administrative_area_mgt', '', '1', '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', '', '1', '4', '', '', '', null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1490002273833', '1477447508878', null, '1', '1', '', '1477447508878!1490002273833', '3', '系统管理!集群管理', '3', null, '集群管理', 'group_mgt', '', '1', '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', '', '1', '5', '', '', '', null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1490002273834', '1477447508878', null, '1', '1', '', '1477447508878!1490002273834', '3', '系统管理!日志管理', '3', null, ' 日志管理', 'log_mgt', '', '1', '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', '', '1', '6', '', '', '', null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517961', '0', null, '1', null, null, null, '1', '接入分析', null, null, '接入分析', null, null, null, null, null, null, null, 'icon-renwuguanli', '1', '7', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517962', '1495011517961', null, '1', null, null, null, '2', '接入分析!实时分析', null, null, '实时分析', 'real_time_analysis', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517963', '1495011517961', null, '1', null, null, null, '2', '接入分析!录像分析', null, null, '录像分析', 'video_analysis', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517964', '1495011517961', null, '1', null, null, null, '2', '分析记录', null, null, '分析记录', 'analysis_record', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517965', '1495011517961', null, '1', null, null, null, '2', '专项监控组', null, null, '专项监控组', 'monitoring_group', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517966', '1495011517963', null, '1', null, null, null, '3', '离线视频分析', null, null, '离线视频分析', 'offline_analysis', null, null, null, null, null, null, 'icon-shipin', '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517967', '1495011517963', null, '1', null, null, null, '3', '联网录像分析', null, null, '联网录像分析', 'net_video_analysis', null, null, null, null, null, null, 'icon-videotape', '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517968', '0', null, '1', null, null, null, '1', '便捷管理', '0', '10012', '便捷管理', '1', null, null, null, null, null, null, 'icon-shijianzhoushezhi', '1', '8', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517969', '1495011517968', null, '1', null, null, null, '2', '便捷管理!菜单管理', '1', null, '菜单管理', 'menu', 's', null, null, null, null, null, null, '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517970', '1495011517962', null, '1', null, null, null, '3', '接入源!实时分析!联网实时视频分析', '3', null, '联网实时视频分析', 'online_analysis', null, null, null, null, null, null, 'icon-jiankong1', '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517971', '0', null, '1', null, null, null, '1', '以图搜图', '0', null, '以图搜图', 'image_search', null, null, null, null, null, null, 'icon-yitusoutu', '1', '3', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517972', '0', null, '1', null, null, null, '1', '目标检索', '0', null, '目标检索', 'target_retrieval', null, null, null, null, null, null, 'icon-jiansuo', '1', '2', null, null, null, null, 'menu');
-- INSERT INTO `sys_module_dev` VALUES ('1495011517973', '0', null, '1', null, null, null, '1', '一键图踪', '0', null, '一键图踪', 'key_trace', null, null, null, null, null, null, 'icon-track-one', '0', '4', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517974', '0', null, '1', null, null, null, '1', '接力追踪', '0', null, '接力追踪', 'relay_tracking', null, null, null, null, null, null, 'icon-zhuizong', '1', '5', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517975', '0', null, '1', null, null, null, '1', '案件管理', '0', null, '案件管理', 'case', null, null, null, null, null, null, 'icon-zhubananjian', '1', '6', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517976', '1495011517962', null, '1', null, null, null, '3', '接入源!实时分析!ipc直连', null, null, 'ipc直连', 'ipc_analysis', null, null, null, null, null, null, 'icon-IPC', '1', null, null, null, null, null, 'menu');
-- INSERT INTO `sys_module_dev` VALUES ('1495011517977', '1495011517962', '', '1', '', '', '', '3', '接入源!实时分析!抓拍机接入', null, null, '抓拍机接入', 'capture_analysis', '', null, '2017-08-26 11:13:30', null, '2017-08-26 11:13:30', '', 'icon-snap', '0', null, '', '', '', '', 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517978', '0', null, '1', null, null, null, '1', '实时感知', null, null, '实时感知', 'real_time_perception', null, null, null, null, null, null, 'icon-shishi', '1', '1', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517979', '1495011517968', '', '1', '', '', '', '2', '便捷管理!管理服务配置', '2', null, '管理服务配置', 'manage_config', '', null, '2019-07-15 10:30:34', null, '2019-07-15 10:30:30', '', '', '1', null, '', '', '', '', 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517980', '1495011517979', null, '1', null, null, null, '3', null, '3', '1', 'FTP服务器配置', 'ftp_server_setting', 'FTP类别', null, null, null, null, null, 'icon-FTP', '1', null, '1', null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517981', '1495011517979', null, '1', null, null, null, '3', null, '3', '2', '基础服务设置', 'vas_server_setting', 'VAS类别', null, null, null, null, null, 'icon-fuwuqi', '1', null, null, null, null, null, 'menu');
-- INSERT INTO `sys_module_dev` VALUES ('1495011517982', '1495011517979', null, '1', null, null, null, '3', null, '3', '3', '原图质量设置', 'picture_setting', '原图质量类别', null, null, null, null, null, 'icon-tupian1', '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517986', '1495011517979', null, '1', null, null, null, '3', null, null, '4', '其他', 'other_setting', '其他类别', null, null, null, null, null, null, '1', null, null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517987', '1477447508878', null, '1', null, null, null, '3', null, '3', null, '使用排行榜', 'use_rank_list', null, null, null, null, null, null, null, '1', '7', null, null, null, null, 'menu');
INSERT INTO `sys_module_dev` VALUES ('1495011517988', '1477447508878', null, '1', null, null, null, '3', null, '3', null, '角色管理', 'role_mgt', null, null, null, null, null, null, null, '1', '2', null, null, null, null, 'menu');
-- INSERT INTO `sys_module_dev` VALUES ('1495011517991', '1495011517970', null, '1', null, null, null, '4', null, null, null, '点位同步', 'sync_point', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011517992', '1495011517970', null, '1', null, null, null, '4', null, null, null, '批量删除', 'batch_delete', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011517997', '1495011517971', null, '1', null, null, null, '2', null, '1', '1', '人形', 'image_search_person', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
-- INSERT INTO `sys_module_dev` VALUES ('1495011517998', '0', null, null, null, null, null, '1', null, null, null, '123', '123', null, null, null, null, null, null, null, null, null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518000', '1495011517978', null, '1', null, null, null, '2', null, null, null, '转码', 'transcode', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518002', '1495011517972', null, '1', null, null, null, '2', null, null, '1', '人形', 'target_retrieval_person', null, null, null, null, null, null, 'icon-599kuaizhuangzoulu', '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518003', '1495011517972', null, '1', null, null, null, '2', null, null, '2', '骑行', 'target_retrieval_bike', null, null, null, null, null, null, 'icon-cycle-qiche', '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518004', '1495011517972', null, '1', null, null, null, '2', null, null, '3', '车辆', 'target_retrieval_car', null, null, null, null, null, null, 'icon-che', '1', null, null, null, null, null, 'button');

INSERT INTO `sys_module_dev` VALUES ('1495011518006', '1495011517971', null, '1', null, null, null, '2', null, null, '2', '骑行', 'image_search_bike', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518007', '1495011517971', null, '1', null, null, null, '2', null, null, '3', '车辆', 'image_search_car', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');

INSERT INTO `sys_module_dev` VALUES ('1495011518008', '0', null, '1', null, null, null, '1', null, null, null, '结果分析页查询按钮组', 'result_btn_group', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'tab');
INSERT INTO `sys_module_dev` VALUES ('1495011518009', '1495011518008', null, '1', null, null, null, '2', null, null, '2', '人形', 'result_btn_group_person', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518010', '1495011518008', null, '1', null, null, null, '2', null, null, '3', '骑行', 'result_btn_group_bike', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518011', '1495011518008', null, '1', null, null, null, '2', null, null, '4', '车辆', 'result_btn_group_car', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');

INSERT INTO `sys_module_dev` VALUES ('1495011518013', '1495011518008', null, '1', null, null, null, '2', null, null, '1', '全部', 'result_btn_group_all', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518005', '1495011517972', null, '1', null, null, null, '2', null, null, '4', '人脸', 'target_retrieval_face', null, null, null, null, null, null, 'icon-renlian', '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518001', '1495011517971', null, '1', null, null, null, '2', null, null, '4', '人脸', 'image_search_face', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
INSERT INTO `sys_module_dev` VALUES ('1495011518012', '1495011518008', null, '1', null, null, null, '2', null, null, '5', '人脸', 'result_btn_group_face', null, null, null, null, null, null, null, '1', null, null, null, null, null, 'button');
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名',
  `role_remark` varchar(512) DEFAULT NULL COMMENT '角色描述',
  `state` bigint(8) DEFAULT NULL COMMENT '状态',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `role_sign` varchar(128) DEFAULT NULL COMMENT '角色标识,程序中判断使用,如"admin"',
  `default_perm_ids` varchar(2000) DEFAULT '' COMMENT '默认角色参数,以逗号区分id',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', '管理员', null, null, null, null, 'admin', '1477447178527,1477447456246,1477447508878,1494399173729,1495011517915,1495011517916,1495011517917,1495011517918,1495011517919,1495011517921,1495011517926,1495011517927,1495011517928,1495011517930,1495011517931,1495011517932,1495011517934,1495011517936,1495011517937,1495011517938,1495011517941,1495011517942,1495011517943,1495011517944,1495011517945,1495011517920,1495011517947,1495011517948,1495011517949,1495011517950,1495011517951,1495011517952,1495011517953,1495011517956,1495011517957,1495011517958,1495011517959,1495011517925');
INSERT INTO `sys_role` VALUES ('2', '操作员', '普通用户', null, null, null, null, 'normal', '1477447178527,1477447456246,1477447508878,1494399173729,1495011517915,1495011517916,1495011517917,1495011517918,1495011517919,1495011517921,1495011517926,1495011517927,1495011517928,1495011517930,1495011517920,1495011517951,1495011517952,1495011517953,1495011517931,1495011517936,1495011517925');



-- ----------------------------
-- Table structure for `sys_role_permission_dev`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission_dev`;
CREATE TABLE `sys_role_permission_dev` (
  `role_permission_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=99794879 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色与权限关联表';

-- ----------------------------
-- Records of sys_role_permission_dev
-- ----------------------------
INSERT INTO `sys_role_permission_dev` VALUES ('11145321', '2', '1495011517966');
INSERT INTO `sys_role_permission_dev` VALUES ('11641465', '2', '1495011517962');
INSERT INTO `sys_role_permission_dev` VALUES ('12769631', '2', '1495011517970');
INSERT INTO `sys_role_permission_dev` VALUES ('13143645', '2', '1495011517971');
INSERT INTO `sys_role_permission_dev` VALUES ('13225239', '1', '1495011517992');
INSERT INTO `sys_role_permission_dev` VALUES ('15618227', '2', '1495011517963');
INSERT INTO `sys_role_permission_dev` VALUES ('15636241', '1', '1495011517964');
INSERT INTO `sys_role_permission_dev` VALUES ('15968667', '1', '1495011517980');
INSERT INTO `sys_role_permission_dev` VALUES ('19127997', '1', '1495011517975');
INSERT INTO `sys_role_permission_dev` VALUES ('19156999', '7', '1477447557140');
INSERT INTO `sys_role_permission_dev` VALUES ('19891531', '1', '1495011517966');
INSERT INTO `sys_role_permission_dev` VALUES ('21474866', '2', '1495011517964');
INSERT INTO `sys_role_permission_dev` VALUES ('21632424', '2', '1495011517967');
INSERT INTO `sys_role_permission_dev` VALUES ('21651193', '1', '1495011518006');
INSERT INTO `sys_role_permission_dev` VALUES ('21728199', '7', '1495011517988');
INSERT INTO `sys_role_permission_dev` VALUES ('22736459', '1', '1495011517997');
INSERT INTO `sys_role_permission_dev` VALUES ('22883277', '2', '1495011517972');
INSERT INTO `sys_role_permission_dev` VALUES ('24265718', '2', '1495011517963');
INSERT INTO `sys_role_permission_dev` VALUES ('24553475', '1', '1495011518011');
INSERT INTO `sys_role_permission_dev` VALUES ('25165129', '1', '1495011517974');
INSERT INTO `sys_role_permission_dev` VALUES ('25553481', '2', '1495011517967');
INSERT INTO `sys_role_permission_dev` VALUES ('27367647', '1', '1495011518007');
INSERT INTO `sys_role_permission_dev` VALUES ('29412476', '7', '1495011517968');
INSERT INTO `sys_role_permission_dev` VALUES ('29631611', '2', '1495011517966');
INSERT INTO `sys_role_permission_dev` VALUES ('29673863', '1', '1495011517985');
INSERT INTO `sys_role_permission_dev` VALUES ('31643533', '2', '1495011517978');
INSERT INTO `sys_role_permission_dev` VALUES ('33543833', '2', '1495011517974');
INSERT INTO `sys_role_permission_dev` VALUES ('34922555', '7', '1477447508878');
INSERT INTO `sys_role_permission_dev` VALUES ('35512968', '7', '1490002273834');
INSERT INTO `sys_role_permission_dev` VALUES ('35677421', '2', '1495011517964');
INSERT INTO `sys_role_permission_dev` VALUES ('36172623', '12', '1490002273833');
INSERT INTO `sys_role_permission_dev` VALUES ('36258623', '2', '1495011517971');
INSERT INTO `sys_role_permission_dev` VALUES ('37252594', '2', '1495011517974');
INSERT INTO `sys_role_permission_dev` VALUES ('37422434', '1', '1495011517961');
INSERT INTO `sys_role_permission_dev` VALUES ('38515594', '1', '1495011517983');
INSERT INTO `sys_role_permission_dev` VALUES ('39438498', '2', '1495011517966');
INSERT INTO `sys_role_permission_dev` VALUES ('39737754', '12', '1495011517962');
INSERT INTO `sys_role_permission_dev` VALUES ('39875532', '12', '1495011517971');
INSERT INTO `sys_role_permission_dev` VALUES ('41285291', '2', '1495011517966');
INSERT INTO `sys_role_permission_dev` VALUES ('41528167', '1', '1477447627233');
INSERT INTO `sys_role_permission_dev` VALUES ('41555155', '2', '1495011517974');
INSERT INTO `sys_role_permission_dev` VALUES ('43294279', '2', '1495011517962');
INSERT INTO `sys_role_permission_dev` VALUES ('43638545', '1', '1495011518000');
INSERT INTO `sys_role_permission_dev` VALUES ('43719111', '1', '1495011517967');
INSERT INTO `sys_role_permission_dev` VALUES ('43947943', '2', '1495011517964');
INSERT INTO `sys_role_permission_dev` VALUES ('45642373', '7', '1477447587460');
INSERT INTO `sys_role_permission_dev` VALUES ('46624518', '2', '1495011517978');
INSERT INTO `sys_role_permission_dev` VALUES ('46763799', '1', '1495011517963');
INSERT INTO `sys_role_permission_dev` VALUES ('47227361', '2', '1495011517967');
INSERT INTO `sys_role_permission_dev` VALUES ('48894973', '1', '1490002273833');
INSERT INTO `sys_role_permission_dev` VALUES ('49396867', '1', '1477447557140');
INSERT INTO `sys_role_permission_dev` VALUES ('51739843', '1', '1495011518002');
INSERT INTO `sys_role_permission_dev` VALUES ('54491194', '1', '1495011518010');
INSERT INTO `sys_role_permission_dev` VALUES ('54498169', '2', '1495011517967');
INSERT INTO `sys_role_permission_dev` VALUES ('54598652', '2', '1495011517961');
INSERT INTO `sys_role_permission_dev` VALUES ('54844872', '2', '1495011517972');
INSERT INTO `sys_role_permission_dev` VALUES ('56147782', '7', '1495011517987');
INSERT INTO `sys_role_permission_dev` VALUES ('57988853', '1', '1495011517969');
INSERT INTO `sys_role_permission_dev` VALUES ('58559517', '12', '1495011517968');
INSERT INTO `sys_role_permission_dev` VALUES ('58731726', '2', '1495011517971');
INSERT INTO `sys_role_permission_dev` VALUES ('59218821', '2', '1495011517961');
INSERT INTO `sys_role_permission_dev` VALUES ('59354219', '12', '1490002273834');
INSERT INTO `sys_role_permission_dev` VALUES ('59423678', '12', '1495011517970');
INSERT INTO `sys_role_permission_dev` VALUES ('61979298', '2', '1495011517970');
INSERT INTO `sys_role_permission_dev` VALUES ('62856382', '1', '1495011517972');
INSERT INTO `sys_role_permission_dev` VALUES ('63234295', '2', '1495011517963');
INSERT INTO `sys_role_permission_dev` VALUES ('63637947', '7', '1490002273833');
INSERT INTO `sys_role_permission_dev` VALUES ('63991831', '2', '1495011517976');
INSERT INTO `sys_role_permission_dev` VALUES ('65123111', '2', '1495011517964');
INSERT INTO `sys_role_permission_dev` VALUES ('65933941', '12', '1477447508878');
INSERT INTO `sys_role_permission_dev` VALUES ('66565415', '1', '1495011518004');
INSERT INTO `sys_role_permission_dev` VALUES ('66615811', '1', '1495011518009');
INSERT INTO `sys_role_permission_dev` VALUES ('67239145', '12', '1495011517961');
INSERT INTO `sys_role_permission_dev` VALUES ('67245178', '2', '1495011517970');
INSERT INTO `sys_role_permission_dev` VALUES ('67399726', '1', '1495011518013');
INSERT INTO `sys_role_permission_dev` VALUES ('67625787', '2', '1495011517976');
INSERT INTO `sys_role_permission_dev` VALUES ('67815357', '12', '1495011517972');
INSERT INTO `sys_role_permission_dev` VALUES ('68328235', '1', '1495011517976');
INSERT INTO `sys_role_permission_dev` VALUES ('68952518', '2', '1495011517978');
INSERT INTO `sys_role_permission_dev` VALUES ('73811964', '1', '1495011517962');
INSERT INTO `sys_role_permission_dev` VALUES ('73852372', '2', '1495011517962');
INSERT INTO `sys_role_permission_dev` VALUES ('74515176', '1', '1495011517982');
INSERT INTO `sys_role_permission_dev` VALUES ('75573268', '2', '1495011517963');
INSERT INTO `sys_role_permission_dev` VALUES ('76938978', '12', '1495011517974');
INSERT INTO `sys_role_permission_dev` VALUES ('77748856', '1', '1490002273834');
INSERT INTO `sys_role_permission_dev` VALUES ('77984477', '1', '1495011517971');
INSERT INTO `sys_role_permission_dev` VALUES ('78344439', '1', '1477447508878');
INSERT INTO `sys_role_permission_dev` VALUES ('79728516', '2', '1495011517961');
INSERT INTO `sys_role_permission_dev` VALUES ('82499912', '1', '1495011517979');
INSERT INTO `sys_role_permission_dev` VALUES ('82748155', '2', '1495011517978');
INSERT INTO `sys_role_permission_dev` VALUES ('82948386', '1', '1495011517986');
INSERT INTO `sys_role_permission_dev` VALUES ('85318761', '1', '1495011517978');
INSERT INTO `sys_role_permission_dev` VALUES ('85764454', '2', '1495011517976');
INSERT INTO `sys_role_permission_dev` VALUES ('85766271', '7', '1477447627233');
INSERT INTO `sys_role_permission_dev` VALUES ('85938263', '2', '1495011517976');
INSERT INTO `sys_role_permission_dev` VALUES ('86848221', '12', '1495011517976');
INSERT INTO `sys_role_permission_dev` VALUES ('87886932', '1', '1495011518008');
INSERT INTO `sys_role_permission_dev` VALUES ('88164397', '1', '1495011517965');
INSERT INTO `sys_role_permission_dev` VALUES ('88789473', '2', '1495011517962');
INSERT INTO `sys_role_permission_dev` VALUES ('89238998', '1', '1495011517981');
INSERT INTO `sys_role_permission_dev` VALUES ('89925837', '2', '1495011517970');
INSERT INTO `sys_role_permission_dev` VALUES ('91866888', '1', '1495011517970');
INSERT INTO `sys_role_permission_dev` VALUES ('91896487', '2', '1495011517961');
INSERT INTO `sys_role_permission_dev` VALUES ('93458864', '2', '1495011517972');
INSERT INTO `sys_role_permission_dev` VALUES ('94733713', '1', '1495011517988');
INSERT INTO `sys_role_permission_dev` VALUES ('95745776', '2', '1495011517972');
INSERT INTO `sys_role_permission_dev` VALUES ('96242265', '2', '1495011517974');
INSERT INTO `sys_role_permission_dev` VALUES ('97173679', '1', '1495011517968');
INSERT INTO `sys_role_permission_dev` VALUES ('97491451', '1', '1495011518003');
INSERT INTO `sys_role_permission_dev` VALUES ('97942416', '12', '1495011517978');
INSERT INTO `sys_role_permission_dev` VALUES ('98196562', '1', '1477447587460');
INSERT INTO `sys_role_permission_dev` VALUES ('98332534', '1', '1495011517987');
INSERT INTO `sys_role_permission_dev` VALUES ('99794878', '2', '1495011517971');
INSERT INTO `sys_role_permission_dev` VALUES ('99868239', '1', '1495011517991');
INSERT INTO `sys_role_permission_dev` VALUES (33134341, 1, 1495011518005);
INSERT INTO `sys_role_permission_dev` VALUES (14265312, 1, 1495011518001);
INSERT INTO `sys_role_permission_dev` VALUES (31245323, 1, 1495011518012);
INSERT INTO `sys_role_permission_dev` VALUES (33134342, 2, 1495011518005);
INSERT INTO `sys_role_permission_dev` VALUES (14265313, 2, 1495011518001);
INSERT INTO `sys_role_permission_dev` VALUES (31245324, 3, 1495011518012);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户表';
-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '系统管理员', null, null, null, '1', '1', null, null, null, '2014-07-17 12:59:08');
INSERT INTO `sys_user` VALUES ('2', 'admintest', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '系统测试员', null, null, null, '1', '1', null, null, null, '2016-12-17 12:59:08');

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43187236 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户与角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2', '1');
