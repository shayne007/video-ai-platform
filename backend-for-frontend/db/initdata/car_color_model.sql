CREATE TABLE `car_color_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `color_id` int(11) DEFAULT NULL COMMENT '颜色ID',
  `color_rgb_tag` int(8) NOT NULL DEFAULT '0' COMMENT 'RGB颜色标签',
  `color_bgr_tag` int(8) NOT NULL DEFAULT '0' COMMENT 'BGR颜色标签',
  `color_hex_tag` varchar(8) DEFAULT NULL COMMENT '十六进制颜色标签',
  `color_name` varchar(32) DEFAULT NULL COMMENT '颜色名',
  `fuzzy_color` varchar(32) DEFAULT NULL COMMENT '模糊颜色名',
  `color_code` varchar(32) DEFAULT NULL COMMENT '颜色code',
  `color_number` varchar(32) DEFAULT NULL COMMENT '前端插件颜色type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of car_color_model
-- ----------------------------
INSERT INTO `car_color_model` VALUES ('1', '20', '0', '16777215', null, '白', '白', '2', 'color1');
INSERT INTO `car_color_model` VALUES ('2', '2', '0', '6579300', null, '灰', '灰', '3', 'color8');
INSERT INTO `car_color_model` VALUES ('3', '3', '0', '65535', null, '黄', '黄', '6', 'color4');
INSERT INTO `car_color_model` VALUES ('4', '4', '0', '11306222', null, '粉', '粉红', '12', 'color11');
INSERT INTO `car_color_model` VALUES ('5', '5', '0', '9576596', null, '紫', '紫', '10', 'color7');
INSERT INTO `car_color_model` VALUES ('6', '6', '0', '5287936', null, '绿', '绿', '9', 'color5');
INSERT INTO `car_color_model` VALUES ('7', '7', '0', '16724484', null, '蓝', '蓝', '5', 'color6');
INSERT INTO `car_color_model` VALUES ('8', '8', '0', '9983', null, '红', '红', '4', 'color2');
INSERT INTO `car_color_model` VALUES ('10', '10', '0', '2111058', null, '棕', '棕', '8', 'color3');
INSERT INTO `car_color_model` VALUES ('11', '11', '0', '0', null, '黑', '黑', '1', 'color9');
INSERT INTO `car_color_model` VALUES ('12', '12', '0', '0', null, '橙', '橙', '7', 'color12');