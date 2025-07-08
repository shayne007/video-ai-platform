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
) ENGINE=InnoDB AUTO_INCREMENT=1567238666592 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='组织机构表';



-- ----------------------------
-- Records of ctrl_unit
-- ----------------------------
INSERT INTO `ctrl_unit` VALUES ('1506153675192', '1', '80745444', null, null, '80745444', '1', '社会点位列表', '0', null, '社会点位列表', '80745444', '', null, null, null, null, null, null);
INSERT INTO `ctrl_unit` VALUES ('1506153698283', '1', '99394041', null, null, '80745444!99394041', '2', '默认区域', '0', null, '默认区域', '99394041', '', null, null, null, null, null, '80745444');
INSERT INTO `ctrl_unit` VALUES ('1506153718001', '1', '42407631', null, null, '80745444!99394041!42407631', '3', '默认街道', '1', null, '默认街道', '42407631', '', null, null, null, null, null, '99394041');
