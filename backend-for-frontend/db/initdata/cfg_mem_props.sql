DROP TABLE IF EXISTS cfg_mem_props;
CREATE TABLE `cfg_mem_props` (
  `module_name` varchar(32) NOT NULL COMMENT '模块名',
  `prop_key` varchar(64) NOT NULL COMMENT '参数key',
  `prop_name` varchar(128) DEFAULT NULL COMMENT '参数name',
  `prop_value` varchar(256) DEFAULT NULL COMMENT '参数值',
  `prop_desc` varchar(1024) DEFAULT NULL COMMENT '参数描述',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  PRIMARY KEY (`module_name`,`prop_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='属性配置表';
-- ----------------------------
-- Records of cfg_mem_props
-- ----------------------------
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', '1000video.ip', '其他类别@点位同步服务ip', '127.0.0.1', '其他类别@点位同步服务ip', '2019-08-08 11:37:59');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', '1000video.port', '其他类别@点位同步服务端口', '8060', '其他类别@点位同步服务端口', '2019-08-08 11:10:59');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'analysis.scene.type', '其他类别@视频结构化分析场景类型', '0', '默认为0.1:一般监控视频场景2：卡口、类卡口场景4：交通流量统计场景8：动态人脸场景16：混合行人及动态人脸', '2019-07-22 18:05:44');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'capture.service.password.key', '其他类别@抓拍机密码加密密钥', 'abcdef0123456789', '抓拍机密码加密密钥', '2019-05-25 09:57:16');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'check_upload_size', '其他类别@上传离线视频时检测磁盘空间的限制值', '50', '单位为GB,默认值50GB', '2019-05-25 09:57:14');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'converse-network-req-config', '其他类别@网络ip端口转换', '127.0.0.1:8088_172.16.1.68:8088,127.0.0.1:8082_172.16.1.68:8082', '需带端口，多个映射用逗号隔开，示例：127.0.0.1:8088_192.168.0.142:8088,127.0.0.1:8082_192.168.0.142:8082', '2019-07-23 21:25:37');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp-clean-space', 'FTP类别@FTP存储目录空间检测（单位/G）,', '100', '默认100G,当FTP存储目录的可用空间小于此值时停止上传', '2019-07-29 14:38:55');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp-server-httpurl', 'FTP类别@ftp 服务器的 http 访问方式', '127.0.0.1:8081', '请配置 ftp 服务器的http url 地址', '2019-07-29 14:38:58');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp-server-ip', 'FTP类别@ftp 服务器 ip', '127.0.0.1', '请配置 ftp 服务器的 ip 地址', '2019-07-22 15:22:58');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp-server-port', 'FTP类别@ftp 服务器端口', '21', '请配置 ftp 服务器的端口', '2019-05-25 09:57:13');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp-server-pwd', 'FTP类别@ftp 服务器密码', 'abcd1234!', '请配置 ftp 服务器的密码', '2019-05-25 09:57:13');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp-server-user', 'FTP类别@ftp 服务器用户名', 'chiwailam', '请配置 ftp 服务器的用户名', '2019-05-25 09:57:13');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ftp_server_timeout', 'FTP类别@ftp 服务器请求超时时间毫秒', '30000', '服务器请求超时时间,默认5秒超时', '2019-07-23 12:44:42');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'google-map-from', '其他类别@地图坐标类型', 'google', '取值：google|pgis', '2019-05-25 09:57:14');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'image_empty_mid_pct', '原图质量类别@压缩比', '50', '请配置 压缩比', '2019-05-25 09:57:14');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'image_width_limit', '原图质量类别@图片宽度限制', '1000', '请配置 图片宽度限制', '2019-05-25 09:57:14');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'is-develop-status', '其他类别@是否为开发模式', 'false', 'true|false,默认false用于生产', '2019-05-25 09:57:14');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'is_limit_upload', '其他类别@磁盘空间不足时是否限制离线视频上传', 'false', 'true|false，默认false,单兵为true', '2019-05-25 09:57:14');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'map-latitude', '其他类别@纬度(latitude)', '28.195033121678538', '中心点位地图的维度', '2019-07-23 16:41:00');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'map-longitude', '其他类别@经度(longitude)', '112.97601699829102', '中心点位地图的经度', '2019-07-23 16:40:49');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'monitor.group.limit', '其他类别@监控组大小', '50', '限制一个监控组最大可加入的监控点总数', '2019-05-25 09:57:21');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'nginx-server-port', '其他类别@nginx服务的ip和端口', '', '请配置nginx的ip和端口，配置格式：ip:port,例如192.168.0.70:8082', '2019-05-25 09:57:15');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'project_release_tag', '其他类别@发布版本标识', 'v_main_release', '默认为主版本：v_main_release，佳都定制版本配置：v_jiadu_release', '2019-05-25 09:57:15');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'standard.ip', 'VAS类别@视图库ip', null, 'VAS类别@视图库地址(默认本机)默认端口9999', '2019-08-31 16:29:23');
-- 关闭端口配置
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'standard.port', 'VAS类别@视图库端口', '9999', 'VAS类别@视图库端口', '2019-08-02 20:20:55');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'sync-camera-thumbNail', '其他类别@同步监控点快照', 'false', '开启同步监控点快照,true|false，默认false', '2019-07-23 16:07:26');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ws.port', '隐藏@MAS2版本标注离线视频点播服务器port', '9003', '请配置实时标注离线视频播放服务器的h5_media_server端口', '2019-08-09 18:51:21');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ws.server', '隐藏@MAS2版本标注离线视频点播服务器ip', null, '请配置实时标注离线视频播放服务器的 IP 地址h5_media_server地址(默认本机)默认端口9003', '2019-08-27 10:56:44');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ws2.port', 'VAS类别@MAS2版本国标流端口', '9080', '请配置国标流端口(模拟流，nva流等)MAS-Master服务', '2019-08-02 19:53:01');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ws2.server', 'VAS类别@MAS2版本国标流ip', null , '请配置国标流ip(模拟流，nva流等)MAS-Master服务(默认本机)默认端口9080', '2019-09-02 14:33:54');
-- INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'ws2.tag.port', '隐藏@MAS2版本标注点播服务器port', '9402', '请配置实时标注播放服务器的MASVideoTagServer，固定跟随分析服务端口', '2019-08-02 19:51:17');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'task-authorize-connect-number', '其他类别@系统授权数量', '100', '其他类别@系统授权联网实时视频和IPC直连启动的路数之和', '2019-08-08 11:10:35');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'sys_login', '其他类别@单点登陆', '0', '系统用户单点登陆，0|1，默认0，0：关闭，1：开启', '2019-07-23 16:07:26');
