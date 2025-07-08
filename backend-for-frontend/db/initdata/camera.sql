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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1567492694408 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='视频点位表';

-- ----------------------------
-- Records of camera
-- ----------------------------
INSERT INTO `camera` VALUES ('1234567891011', '默认监控点', '2', null, '1', '42407631', '112.97601699829102', '28.195033121678538', null, null, '1', null, '2', null, null, null, null, null, null, null, null, null, null, null, null, null, 'rtsp://username:password@ip:554/h264/ch1/main/av_stream', null, null, null, '2019-09-01 14:29:39');