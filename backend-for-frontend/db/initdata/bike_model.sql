CREATE TABLE `bike_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `kind_id` int(11) DEFAULT NULL COMMENT '类型ID',
  `kind_name` varchar(50) DEFAULT NULL COMMENT '类型名称',
  `model_id` int(11) DEFAULT NULL COMMENT '款式ID',
  `model_name` varchar(50) DEFAULT NULL COMMENT '款式名称',
  `info1` varchar(50) DEFAULT NULL COMMENT '扩展字段1',
  `info2` varchar(50) DEFAULT NULL COMMENT '扩展字段2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of bike_model
-- ----------------------------
INSERT INTO `bike_model` VALUES ('2', '2', '二轮摩托车', null, '', null, null);
INSERT INTO `bike_model` VALUES ('3', '3', '自行车', null, '', null, null);
INSERT INTO `bike_model` VALUES ('4', '5', '三轮车', null, '', null, null);
INSERT INTO `bike_model` VALUES ('5', '0', '未知', null, null, null, null);