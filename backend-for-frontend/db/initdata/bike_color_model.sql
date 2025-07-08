CREATE TABLE `bike_color_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `color_id` int(11) DEFAULT NULL COMMENT '颜色ID',
  `color_rgb_tag` int(8) NOT NULL DEFAULT '0' COMMENT 'RGB颜色标签',
  `color_bgr_tag` int(8) NOT NULL DEFAULT '0' COMMENT 'BGR颜色标签',
  `color_hex_tag` varchar(8) DEFAULT NULL COMMENT '十六进制颜色标签',
  `color_name` varchar(32) DEFAULT NULL COMMENT '颜色名',
  `fuzzy_color` varchar(32) DEFAULT NULL COMMENT '模糊颜色名',
  `color_number` varchar(12) DEFAULT NULL COMMENT '色号',
  `info1` varchar(32) DEFAULT NULL COMMENT '扩展字段1',
  `info2` varchar(32) DEFAULT NULL COMMENT '扩展字段2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bike_color_model
-- ----------------------------
INSERT INTO `bike_color_model` VALUES ('1', '19', '0', '16777215', null, '白', '白', 'color1', null, null);
INSERT INTO `bike_color_model` VALUES ('2', '2', '0', '5263440', null, '黑(深灰)', '黑', 'color9', null, null);
INSERT INTO `bike_color_model` VALUES ('3', '3', '0', '11842740', null, '灰', '灰', 'color8', null, null);
INSERT INTO `bike_color_model` VALUES ('4', '4', '0', '65535', null, '黄', '黄', 'color4', null, null);
INSERT INTO `bike_color_model` VALUES ('5', '5', '0', '16743167', null, '红', '粉红', 'color2', null, null);
INSERT INTO `bike_color_model` VALUES ('6', '6', '0', '9983', null, '红', '红', 'color2', null, null);
INSERT INTO `bike_color_model` VALUES ('7', '7', '0', '9576596', null, '紫', '紫', 'color7', null, null);
INSERT INTO `bike_color_model` VALUES ('8', '8', '0', '8761028', null, '棕(卡其)', '棕', 'color3', null, null);
INSERT INTO `bike_color_model` VALUES ('9', '9', '0', '5287936', null, '绿(青)', '绿', 'color5', null, null);
INSERT INTO `bike_color_model` VALUES ('10', '10', '0', '8327170', null, '蓝', '蓝', 'color6', null, null);
INSERT INTO `bike_color_model` VALUES ('11', '11', '0', '16724484', null, '蓝', '蓝', 'color6', null, null);
INSERT INTO `bike_color_model` VALUES ('12', '12', '0', '12423793', null, '蓝', '蓝', 'color6', null, null);
INSERT INTO `bike_color_model` VALUES ('13', '13', '0', '15311656', null, '蓝', '蓝', 'color6', null, null);
INSERT INTO `bike_color_model` VALUES ('14', '14', '0', '16776448', null, '绿(青)', '青', 'color5', null, null);
INSERT INTO `bike_color_model` VALUES ('15', '15', '0', '343174', null, '棕(卡其)', '棕', 'color3', null, null);
INSERT INTO `bike_color_model` VALUES ('16', '16', '0', '2111058', null, '棕(卡其)', '棕', 'color3', null, null);
INSERT INTO `bike_color_model` VALUES ('17', '17', '0', '0', null, '黑(深灰)', '黑', 'color9', null, null);
INSERT INTO `bike_color_model` VALUES ('18', '18', '0', '11711154', null, '灰', '银', 'color8', null, null);
INSERT INTO `bike_color_model` VALUES ('19', '19', '0', '37887', null, '黄', '橙', 'color4', null, null);