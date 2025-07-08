DROP PROCEDURE IF EXISTS sp_MonitorSlaveStatus;  
delimiter // 
CREATE PROCEDURE sp_MonitorSlaveStatus()
exit_label:
BEGIN
	update vsd_slavestatus s set s.valid = 0 where UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(s.lastupdate_time) > 60;
	update vsd_slavestatus s,vsd_task t set t.`status` = 0,t.slaveip = '',t.progress = 0 where t.slaveip = s.slave_ip and t.`status` = 1 and s.valid = 0 and t.type != 'summary';
	update vsd_summarystatus s set s.valid = 0 where UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(s.lastupdate_time) > 60;
	update vsd_summarystatus s,vsd_task t set t.`status` = 0,t.slaveip = '',t.progress = 0 where t.slaveip = s.slave_ip and t.`status` = 1 and s.valid = 0 and t.type = 'summary';
END; 

CREATE EVENT IF NOT EXISTS E_MonitorSlaveStatus
ON schedule EVERY 30 SECOND 
ON COMPLETION PRESERVE
DO CALL sp_MonitorSlaveStatus();

DROP TABLE if EXISTS vsd_summarystatus;
CREATE TABLE vsd_summarystatus (
   id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `slave_ip` varchar(16) NOT NULL DEFAULT '' COMMENT '从服务器ip',
  `valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否可用',
  `summary_capability` smallint(6) DEFAULT '0' COMMENT '浓缩的能力',
  `payload` text COMMENT '当前负荷',
  `reserve` varchar(32) NOT NULL DEFAULT '' COMMENT '保留参数',
  `lastupdate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `slave_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY ` slave_ip` (`slave_ip`),
  KEY ` lastupdate_time` (`lastupdate_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF not EXISTS  `vlpr_picture_recog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `taskId` varchar(64) NOT NULL COMMENT 'vlpr_task关联id,对应serialnumber',
  `cameraId` int(11) NOT NULL DEFAULT '0' COMMENT '监控点id',
  `imageUrl` varchar(200) NOT NULL COMMENT '图片相对url',
  `downTime` int(11) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '识别状态 0-等待处理 1-成功 2-下载失败 3-结构化失败 4-未提取到任务车辆特征 5-保存或者推送至Kafka失败 9-处理中',
  `realTime` datetime NULL COMMENT '过车时间',
  `extendId` varchar(26) NULL COMMENT '第三方关联key,对应id',
  `extendInfo` varchar(500) NULL COMMENT '第三方记录信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆卡口图片识别表';


INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'vlpr.switch', '车辆卡口图片定时任务开关', 'true', '启动时有效，默认未fa
lse,true|false', '2018-09-03 16:04:13');

ALTER TABLE ctrl_unit_file ADD COLUMN auto_analysis_flag TINYINT DEFAULT 1 COMMENT '是否自动分析,0:否,1:是',
ADD COLUMN interest_param VARCHAR (255) DEFAULT NULL COMMENT '感兴趣区域参数',
ADD COLUMN uninterest_param VARCHAR (255) DEFAULT NULL COMMENT '不感兴趣区域参数';

DROP TABLE IF EXISTS `vsd_bit_machine`;
CREATE TABLE `vsd_bit_machine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bit_name` varchar(20) DEFAULT '比特盒子-1' COMMENT '名称',
  `bit_status` int(1) NOT NULL DEFAULT '1' COMMENT '状态 1-是 0-否',
  `url` varchar(256) NOT NULL COMMENT '访问地址',
  `number_road` int(3) NOT NULL DEFAULT '1' COMMENT '路数',
  `user_road` int(3) DEFAULT '0' COMMENT '使用路数',
  `execute_status` int(11) DEFAULT '0' COMMENT '执行任务状态：1-路数已满 0-路数未满',
  `retry_count` int(2) DEFAULT '0' COMMENT '重试次数',
  `last_fail_time` datetime DEFAULT NULL COMMENT '最后失败时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vsd_task_bit
-- ----------------------------
DROP TABLE IF EXISTS `vsd_task_bit`;
CREATE TABLE `vsd_task_bit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serialnumber` varchar(64) NOT NULL COMMENT 'serialnumber唯一编号',
  `bit_machine_id` bigint(20) NOT NULL COMMENT 'vsd_bit_machine表ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int(1) DEFAULT '0' COMMENT '状态：0-等待执行，1-正在执行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'bit.machine.switch', '比特盒子定时任务开关', 'false', '默认为false,true
|false', '2018-10-10 02:46:13');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'beijing.exhibition.switch', '北京展会开关', 'false', '北京展会开关,默认
false;开启后默认所有上下衣款式为长袖，长裤', '2018-10-16 17:34:24');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'delay.play.milliseconds', '延时播放毫秒数', '0', '延时播放毫秒数，默认是0
', '2018-10-21 17:03:08');

alter table tb_analysis_task add device_id varchar(128) COMMENT "设备ID";

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'huawei.push.switch', '向华为大数据推送数据开关', 'false', '默认未false,
true|false', '2018-10-27 14:23:55');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'wuxi.operation.count.switch', '无锡运维接口推送当日数据总数开关', 'fals
e', '默认未false,true|false', '2018-11-01 11:10:16');
INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'wuxi.subscribes.data', '无锡订阅数据JSON', '', '无锡订阅数据JSON', '201
8-10-31 10:32:13');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('_control', 'wuxi.subscribes.id', '无锡订阅数据ID', NULL, '无锡订阅数据ID', '2018-10-
31 10:35:18');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'wuxi.subscribes.switch', '无锡网关订阅开关', 'false', '默认未false,true
|false', '2018-11-01 11:10:37');

-- -----------------------
-- add by dufy 2018-11-17
-- -----------------------
CREATE TABLE `tb_cron` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cron` varchar(50) NOT NULL COMMENT 'cron表达式',
  `enable` tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否启用（0：停用；1：启用）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_vas_task_log` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` int(4) NOT NULL DEFAULT '10' COMMENT '状态(10:成功；-10：失败)',
  `msg` varchar(255) DEFAULT NULL COMMENT '返回信息',
  `vas_cnt` int(12) NOT NULL DEFAULT '0' COMMENT 'vas数量',
  `vas_insert_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '新增vas数量',
  `vas_update_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '更新vas数量',
  `vas_delete_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '删除vas数量',
  `org_cnt` int(12) NOT NULL DEFAULT '0' COMMENT 'org数量',
  `org_insert_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '新增org数量',
  `org_update_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '更新org数量',
  `org_delete_cnt` int(12) NOT NULL DEFAULT '0' COMMENT '删除org数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tb_cron` (`id`, `cron`, `enable`, `create_time`) VALUES (1, '0 10 3 * * ?', 1, '2018-11-6 11:19:41');

ALTER TABLE camera MODIFY extcameraid varchar(50) DEFAULT NULL COMMENT '设备编号';

-- ----------------------------
-- 修改ctrl_unit_file 中感兴趣参数的长度
-- ----------------------------
ALTER TABLE ctrl_unit_file MODIFY column interest_param varchar(1024);
ALTER TABLE ctrl_unit_file MODIFY column uninterest_param varchar(1024);


-- -------------------
-- FTP类别@
-- 5
-- -------------------
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='FTP类别@ftp 服务器的 http 访问方式' WHERE (`module_name`='qst_u2s') AND (`prop_key`='ftp-server-httpurl');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='FTP类别@ftp 服务器 ip' WHERE (`module_name`='qst_u2s') AND (`prop_key`='ftp-server-ip');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='FTP类别@ftp 服务器端口' WHERE (`module_name`='qst_u2s') AND (`prop_key`='ftp-server-port');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='FTP类别@ftp 服务器密码' WHERE (`module_name`='qst_u2s') AND (`prop_key`='ftp-server-pwd');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='FTP类别@ftp 服务器用户名' WHERE (`module_name`='qst_u2s') AND (`prop_key`='ftp-server-user');

-- -------------------
-- VAS类别@
-- 5
-- -------------------
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='VAS类别@vas服务器 ip' WHERE (`module_name`='qst_u2s') AND (`prop_key`='vas-download-ip');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='VAS类别@vas点播服务器 ip' WHERE (`module_name`='qst_u2s') AND (`prop_key`='vas-play-ip');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='VAS类别@vas点播服务器端口' WHERE (`module_name`='qst_u2s') AND (`prop_key`='vas-play-port');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='VAS类别@vas点播服务器密码'WHERE (`module_name`='qst_u2s') AND (`prop_key`='vas-play-pwd');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='VAS类别@vas点播服务器用户名' WHERE (`module_name`='qst_u2s') AND (`prop_key`='vas-play-user');

-- -------------------
-- 系统名称类别@
-- 2
-- -------------------
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='系统名称类别@系统展示名称' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-title');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='系统名称类别@系统登录页名称' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-login-title');


-- -------------------
-- 菜单类别@
-- 16
-- -------------------
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统一级菜单01' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-01');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单01-01' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-01-01-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单01-02' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-01-02-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单01-03' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-01-03-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统一级菜单02' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-02');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单02-01' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-02-01-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单02-02' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-02-02-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统一级菜单03' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-03');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单03-01' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-03-01-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单03-02' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-03-02-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单03-03' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-03-03-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统一级菜单04' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-04');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单04-01' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-04-01-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单04-02' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-04-02-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单04-03' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-04-03-text');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='菜单类别@系统二级菜单04-04' WHERE (`module_name`='qst_u2s') AND (`prop_key`='text-page-index-menu-04-04-text');


-- -------------------
-- 其他类别@
-- 12
-- -------------------
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@地图坐标类型' WHERE (`module_name`='qst_u2s') AND (`prop_key`='google-map-from');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@是否为开发模式' WHERE (`module_name`='qst_u2s') AND (`prop_key`='is-develop-status');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@磁盘空间不足时是否限制离线视频上传' WHERE (`module_name`='qst_u2s') AND (`prop_key`='is_limit_upload');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@登陆权限区分' WHERE (`module_name`='qst_u2s') AND (`prop_key`='login-access');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@持久化图片表名' WHERE (`module_name`='qst_u2s') AND (`prop_key`='persist-tablename');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@同步监控点快照' WHERE (`module_name`='qst_u2s') AND (`prop_key`='sync-camera-thumbNail');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@同步ES等待插入MySQL时间' WHERE (`module_name`='qst_u2s') AND (`prop_key`='sync-wait-time-second');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@视频结构化分析场景类型' WHERE (`module_name`='qst_u2s') AND (`prop_key`='analysis.scene.type');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@上传离线视频时检测磁盘空间的限制值' WHERE (`module_name`='qst_u2s') AND (`prop_key`='check_upload_size');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@网络ip端口转换' WHERE (`module_name`='qst_u2s') AND (`prop_key`='converse-network-req-config');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@延时播放毫秒数' WHERE (`module_name`='qst_u2s') AND (`prop_key`='delay.play.milliseconds');
UPDATE `u2s`.`cfg_mem_props` SET `prop_name`='其他类别@以图搜脸按钮展示开关' WHERE (`module_name`='qst_u2s') AND (`prop_key`='face.search.switch');


-- -------------------
-- 原图质量类别@
-- 2
-- -------------------
INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'image_empty_mid_pct', '原图质量类别@压缩比', '50', '请配置 压缩比', '2018
-11-21 15:39:14');
INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'image_width_limit', '原图质量类别@图片宽度限制', '1000', '请配置 图片宽度
限制', '2018-11-21 15:39:14');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'feature.search.config.timeout', '搜图超时时间', '90', '搜图超时时间(单
位：秒)', '2018-12-12 14:59:05');

-- 特殊点位
CREATE TABLE `camera_special` (
`camera_id` bigint(32) NOT NULL COMMENT '监控点编号',
`sence` varchar(10) NOT NULL DEFAULT '100' COMMENT '场景: 100-室内场景',
  PRIMARY KEY (`camera_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='特殊点位表';

-- ---------------------------------------
-- 佳都案件管理
-- ---------------------------------------
drop table if exists tb_case;

/*==============================================================*/
/* Table: tb_case                                               */
/*==============================================================*/
create table tb_case
(
   id                   bigint(32) not null auto_increment,
   case_code            varchar(50) not null comment '案件编号',
   case_name            varchar(255) BINARY not null comment '案件名称',
   case_option_id       varchar(32) comment '案件类别',
   case_status          int(2) not null comment '案件状态：0 已处警 1 已受理 2 已立案 3 已破案 4 已结案 5 已销案 6 已不立 7 已移交 8 已破未结 9 撤案转行政处罚 50 不处理 51 已调解 52 已终止 59 已终结 60 已处罚 6
1 已受理未结 62 当场处罚 20 审查中 21 已审查 99 其他',
   case_detail          varchar(2048) comment '案件详情',
   case_start_time      datetime comment '案发时间',
   case_end_time        datetime comment '结案时间',
   case_location        varchar(255) comment '案发地点',
   case_handle_user     varchar(255) comment '办案人员',
   case_handle_investor varchar(255) comment '协办侦查员',
   case_longitude       varchar(32) comment '经度',
   case_latitude        varchar(32) comment '纬度',
   case_station_id      varchar(50) comment '所属派出所编码',
   track_image_url      varchar(255) comment '目标轨迹图',
   deleted              tinyint(1) not null default 0 comment '删除标识  1:已删除  0:正常',
   create_time          datetime comment '创建时间',
   create_user          varchar(50) not null comment '创建人编号',
   version              int(12) not null default 0 comment '版本号',
   last_update_time     datetime not null comment '最近更新时间',
   primary key (id)
);

alter table tb_case comment '案件表';


drop table if exists tb_case_archive_pic;

/*==============================================================*/
/* Table: tb_case_archive_pic                                   */
/*==============================================================*/
create table tb_case_archive_pic
(
   id                   bigint(32) not null auto_increment comment '主键',
   file_name            varchar(50) not null comment '图片名称/视频名称',
   obj_type             int(2) not null comment '图片类型objType(1:人,2:车辆,4:人骑车 )',
   pic_id               varchar(32) not null comment '图片Id',
   pic_big_path         varchar(255) comment '文件远程路径',
   case_code            varchar(50) not null comment '关联案件编号',
   pic_thumb_path       varchar(255) comment '文件本地路径',
   happen_period        int(2) not null comment '发生时间段：0 事前 1事中 2事后',
   serialnumber         varchar(64) not null comment '分析序列号',
   camera_id            bigint(32) not null comment '关联监控点编号',
   deleted              tinyint(1) not null default 0 comment '删除标识  1:已删除  0:正常',
   version              int(2) not null default 0 comment '版本号',
   create_time          datetime not null comment '创建时间',
   create_user          varchar(50) not null comment '创建人编号',
   last_update_time     datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最近更新时间',
   primary key (id)
);

alter table tb_case_archive_pic comment '案件图片归档表';

drop table if exists tb_case_archive_video;

/*==============================================================*/
/* Table: tb_case_archive_video                                 */
/*==============================================================*/
create table tb_case_archive_video
(
   id                   bigint(32) not null auto_increment comment '主键',
   file_name            varchar(50) not null comment '图片名称/视频名称',
   file_type            int(2) not null comment '文件类型：1 实时；2 离线',
   file_remote_path     varchar(255) comment '文件远程路径',
   file_local_path      varchar(255) comment '文件本地路径',
   happen_period        int(2) not null default 1 comment '发生时间段：0事前 1事中 2事后',
   is_proof             tinyint(1) not null default 0 comment '是否证据视频(0 否 1 是)',
   video_start_time     datetime comment '视频开始时间',
   video_end_time       datetime comment '视频结束时间',
   relate_picture_url   varchar(255) not null comment '视频关联图片url',
   case_code            varchar(50) not null comment '关联案件编号',
   serialnumber         varchar(64) not null comment '分析序列号',
   camera_id            bigint(32) not null comment '关联监控点编号',
   deleted              tinyint(1) not null default 0 comment '删除标识  1:已删除  0:正常',
   version              int(2) not null default 0 comment '版本号',
   create_time          datetime not null comment '创建时间',
   create_user          varchar(50) not null comment '创建人编号',
   last_update_time     datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最近更新时间',
   video_download_id    varchar(40) comment '视频下载任务Id，关联tb_case_video_download表Id',
   primary key (id)
);

alter table tb_case_archive_video comment '案件视频归档表';

drop table if exists tb_case_video_download;

/*==============================================================*/
/* Table: tb_case_video_download                                */
/*==============================================================*/
create table tb_case_video_download
(
   id                   varchar(40) not null comment '主键，下载任务ID',
   progress             smallint(6) comment '任务进度',
   entry_time           varchar(20) comment '校准时间',
   transfer_url         varchar(255) comment '视频转移地址',
   transfer_id          varchar(255) comment '转移ID',
   transfer_status      smallint(6) comment '转移状态： 1 等待处理 2 已完成 3 处理失败',
   transfer_retry       smallint(6) comment '转移重试次数',
   download_url         varchar(255) comment '录像下载地址',
   download_id          varchar(255) comment '录像下载id',
   download_status      smallint(6) comment '下载状态： 1 待提交 2 正在处理 3已完成 4处理失败',
   download_progress    smallint(6) comment '下载进度',
   download_file        varchar(255) comment '录像下载后文件信息',
   download_retry       smallint(6) comment '录像下载重试次数',
   transcode_url        varchar(255) comment '提交转码url地址',
   transcode_id         varchar(255) comment '转码id',
   transcode_status     smallint(6) comment '任务状态： 0等待 1完成 2转码中 3失败',
   transcode_progress   smallint(6) comment '转码进度',
   transcode_file       varchar(255) comment '转码后文件信息',
   create_time          datetime comment '提交时间',
   finish_time          datetime comment '完成时间',
   lastupdate_time      datetime comment '最后更改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table tb_case_video_download comment '案件视频下载表 拷贝 tb_analysis_detail';

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`,`prop_key`,`prop_name`,`prop_value`,`prop_desc`,`update_time`)VALUES('qst_u2s','nginx-server-port','其他类别@nginx服务的ip和端口','','请配置nginx的ip和端口，配置
格式：ip:port,例如192.168.0.70:8082','2019-01-02 12:59:05');
INSERT INTO `u2s`.`cfg_mem_props` (`module_name`,`prop_key`,`prop_name`,`prop_value`,`prop_desc`,`update_time`)VALUES('qst_u2s','project_release_tag','其他类别@发布版本标识','v_main_release','默认为主版本：v_ma
in_release，佳都定制版本配置：v_jiadu_release','2019-01-02 12:59:05');

INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'ftp-clean-space', 'FTP类别@FTP存储目录空间检测（单位/G）,', '100', '默认1
00G,当FTP存储目录的可用空间小于此值时，触发磁盘清理', '2019-03-27 09:24:03');
INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'map-latitude', '其他类别@纬度(latitude)', '28.195033121678538', '中心点位
地图的维度', '2019-04-02 18:00:36');
INSERT INTO `u2s`.`cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'map-longitude', '其他类别@经度(longitude)', '112.97601699829102', '中心点
位地图的经度', '2019-04-02 18:00:36');

INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'capture.service.url', '抓拍机服务接口地址', '127.0.0.1:8080', '抓拍机服务接口
地址,与抓拍机接入模块一致', '2019-4-8 16:11:48');
INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'task-authorize-connect-number', '其他类别@系统授权数量', '100', '系统授权联网实
时视频和IPC直连启动的路数之和', '2019-4-8 16:24:15');
INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('qst_u2s', 'capture.service.password.key', '其他类别@抓拍机密码加密密钥', 'abcdef0123456789
', '抓拍机密码加密密钥', '2019-4-9 11:14:00');
INSERT INTO `cfg_mem_props` (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`) VALUES ('u2s_recog', 'kafka.consumer.face.switch', 'kafka消费是否分析人脸', 'false', '默认未false,t
rue|false', '2019-4-1 19:46:44');

UPDATE `camera` SET `cameratype`='2' WHERE (`id`='1234567891011');

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `visit_num` int(4) NOT NULL COMMENT '访问次数',
  `operate_ip` varchar(20) NOT NULL COMMENT '操作的IP',
  `operate_module` varchar(255) NOT NULL COMMENT '操作的模块',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作日志';

-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1495011517960 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='模块表';

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1477447178527, 0, 'rest/vsdtaskManage/taskManagement', '1', '1', NULL, '1477447178527', 1, '快捷菜单', 1, 4, '快捷菜单', 'taskManage', '', NULL, NULL, NULL, NULL, NULL, 'menu_2', NULL, NULL, NULL, NULL, NULL, 'menu_01_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1477447456246, 0, 'rest/camera/cameraMgr', '1', '1', NULL, '1477447456246', 1, '综合应用', 0, 6, '综合应用', 'cameraMgr', '', NULL, NULL, NULL, NULL, NULL, 'menu_4', NULL, NULL, NULL, NULL, NULL, 'menu_02_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1477447508878, 0, 'sysMgr', '1', '1', NULL, '1477447508878', 1, '视频任务管理', 0, 7, '视频任务管理', 'sysMgr', '', NULL, NULL, NULL, NULL, NULL, 'menu_5', NULL, NULL, NULL, NULL, NULL, 'menu_03_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1494399173729, 0, 'rest/vsdtaskManage/largeDataSearch', '1', '1', NULL, '1494399173729', 1, '便捷管理', 0, 2, '便捷管理', 'largeDataSearch', '', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_04_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517915, 1477447178527, NULL, NULL, NULL, NULL, NULL, NULL, '案件管理', NULL, NULL, '案件管理', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_01_03_text,menu_01_04_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517916, 1477447178527, NULL, NULL, NULL, NULL, NULL, NULL, '目标检索', NULL, NULL, '目标检索', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_01_01_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517917, 1477447178527, NULL, NULL, NULL, NULL, NULL, NULL, '跨镜追踪', NULL, NULL, '跨镜追踪', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_01_02_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517918, 1477447456246, NULL, NULL, NULL, NULL, NULL, NULL, '接力追踪', NULL, NULL, '接力追踪', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_02_02_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517919, 1477447508878, NULL, NULL, NULL, NULL, NULL, NULL, '添加分析任务', NULL, NULL, '添加分析任务', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_03_01_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517920, 1477447508878, NULL, NULL, NULL, NULL, NULL, NULL, '专项监控组', NULL, NULL, '专项监控组', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_03_04_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517921, 1494399173729, NULL, NULL, NULL, NULL, NULL, NULL, '系统管理', NULL, NULL, '系统管理', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_04_01_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517925, 1477447508878, NULL, NULL, NULL, NULL, NULL, NULL, '分析任务进度查询', NULL, NULL, '分析任务进度查询', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'menu_03_02_text', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517926, 1495011517919, NULL, NULL, NULL, NULL, NULL, NULL, '离线视频分析', NULL, NULL, '离线视频分析', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.relinevideo', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517927, 1495011517919, NULL, NULL, NULL, NULL, NULL, NULL, '联网录像分析', NULL, NULL, '联网录像分析', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.onlinecamera', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517928, 1495011517919, NULL, NULL, NULL, NULL, NULL, NULL, '联网实时', NULL, NULL, '联网实时和IPC直连', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.onlinevideo,analysis.ipc', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517931, 1495011517926, NULL, NULL, NULL, NULL, NULL, NULL, '离线视频分析|上传视频', NULL, NULL, '上传', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, 'analysis.relinevideo.uploadvideo', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517932, 1495011517926, NULL, NULL, NULL, NULL, NULL, NULL, '离线视频分析|删除/批量删除', NULL, NULL, '删除/批量删除', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, 'analysis.relinevideo.delete,analysis.relinevideo.batchdelete', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517934, 1495011517927, NULL, NULL, NULL, NULL, NULL, NULL, '联网录像分析|删除/批量删除', NULL, NULL, '删除/批量删除', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.onlinecamera.delete,analysis.onlinecamera.batchdelete', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517936, 1495011517927, NULL, NULL, NULL, NULL, NULL, NULL, '联网录像分析|下载', NULL, NULL, '下载', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.onlinecamera.downloadcamera', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517937, 1495011517928, NULL, NULL, NULL, NULL, NULL, NULL, '联网实时视频分析|新增/编辑/删除/批量删除', NULL, NULL, '新增/编辑/删除/批量删除', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.onlinevideo.add,analysis.onlinevideo.edit,analysis.onlinevideo.delete,analysis.onlinevideo.batchdelete,analysis.ipc.add,analysis.ipc.edit,analysis.ipc.delete,analysis.ipc.batchdelete', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517938, 1495011517928, NULL, NULL, NULL, NULL, NULL, NULL, '联网实时视频分析|启动/停止任务', NULL, NULL, '启动/停止任务', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.onlinevideo.start,analysis.onlinevideo.stop,analysis.ipc.start,analysis.ipc.stop', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517941, 1495011517930, NULL, NULL, NULL, NULL, NULL, NULL, '抓拍机接入|新增/编辑/删除/批量删除', NULL, NULL, '新增/编辑/删除/批量删除', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.snapping.add,analysis.snapping.edit,analysis.snapping.delete,analysis.snapping.batchdelete', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517942, 1495011517930, NULL, NULL, NULL, NULL, NULL, NULL, '抓拍机接入|启动/停止任务', NULL, NULL, '启动/停止任务', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.snapping.start,analysis.snapping.stop', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517943, 1495011517920, NULL, NULL, NULL, NULL, NULL, NULL, '专项监控组|新增/编辑/删除/批量删除', NULL, NULL, '新增/编辑/删除/批量删除', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.monitorgroup.add,analysis.monitorgroup.edit,analysis.monitorgroup.delete,analysis.monitorgroup.batchdelete', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517944, 1495011517920, NULL, NULL, NULL, NULL, NULL, NULL, '专项监控组|启动/停止任务', NULL, NULL, '启动/停止任务', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.monitorgroup.start,analysis.monitorgroup.stop', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517945, 1495011517920, NULL, NULL, NULL, NULL, NULL, NULL, '专项监控组|移入/移出', NULL, NULL, '移入/移出', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.monitorgroup.move,analysis.monitorgroup.remove', 'button');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517947, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '角色管理', NULL, NULL, '角色管理', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.rolemanage', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517948, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '用户管理', NULL, NULL, '用户管理', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.usermanage', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517949, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '组织机构', NULL, NULL, '组织机构', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.deptmanage', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517950, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '行政区域', NULL, NULL, '行政区域', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.unitmanage', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517951, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '集群管理', NULL, NULL, '集群管理', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.groupmanage', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517952, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '日志管理', NULL, NULL, '日志管理', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.logmanage', 'menu');
INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517953, 1495011517921, NULL, NULL, NULL, NULL, NULL, NULL, '使用排行榜', NULL, NULL, '使用排行榜', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system.leaderboard', 'menu');
-- INSERT INTO `sys_module` (`module_id`, `parent_id`, `module_url`, `state`, `is_visible`, `actions`, `long_number`, `module_level`, `display_name`, `leaf`, `seq`, `module_name`, `module_number`, `module_description`, `creator_id`, `create_time`, `last_update_user_id`, `last_updated_time`, `ctrl_unit_id`, `module_icon`, `is_dsiplay`, `display_order`, `info1`, `info2`, `info3`, `perms`, `type`) VALUES (1495011517930, 1495011517919, NULL, NULL, NULL, NULL, NULL, NULL, '抓拍机', NULL, NULL, '抓拍机', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'analysis.snapping', 'menu');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `permission_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `resource_id` bigint(20) DEFAULT NULL COMMENT '资源id',
  `resource_name` varchar(128) DEFAULT NULL COMMENT '资源名称',
  `resource_type` bigint(8) DEFAULT NULL COMMENT '资源类型',
  `action_id` bigint(8) DEFAULT NULL COMMENT '动作',
  `action_name` varchar(128) DEFAULT NULL,
  `function_code` varchar(64) DEFAULT NULL COMMENT '功能编码',
  `permission_name` varchar(128) DEFAULT NULL COMMENT '权限名',
  `permission_sign` varchar(128) DEFAULT NULL COMMENT '权限标识,程序中判断使用,如"user:create"',
  `description` varchar(256) DEFAULT NULL COMMENT '权限描述,UI界面显示使用',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1495011517929 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1477447142887', '1477447142879', '首页', '1', '1', null, 'rest/index', '首页', null, null);
INSERT INTO `sys_permission` VALUES ('1477447178540', '1477447178527', '任务管理', '1', '1', null, 'rest/vsdtaskManage/taskManagement', '任务管理', null, null);
INSERT INTO `sys_permission` VALUES ('1477447456250', '1477447456246', '资源管理', '1', '1', null, 'rest/camera/cameraMgr', '资源管理', null, null);
INSERT INTO `sys_permission` VALUES ('1477447508881', '1477447508878', '系统管理', '1', '1', null, 'systemsettings', '系统管理', null, null);
INSERT INTO `sys_permission` VALUES ('1477447557185', '1477447557140', '用户管理', '2', '1', null, 'rest/user/sysUserMgr', '用户管理', '1477447508878', null);
INSERT INTO `sys_permission` VALUES ('1477447557186', '1490002273834', '日志管理', '2', '1', '', 'rest/log/toLog', '日志管理', '1477447508878', '');
INSERT INTO `sys_permission` VALUES ('1477447587473', '1477447587460', '组织结构管理', '2', '1', null, 'rest/dept/deptManage', '组织结构管理', '1477447508878', null);
INSERT INTO `sys_permission` VALUES ('1477447627240', '1477447627233', '行政区域管理', '2', '1', null, 'rest/dept/ctrlUnitManage', '行政区域管理', '1477447508878', null);
INSERT INTO `sys_permission` VALUES ('1477447656255', '1477447656246', '菜单管理', '2', '1', null, 'rest/module/listModules', '菜单管理', '1477447508878', null);
INSERT INTO `sys_permission` VALUES ('1477447674924', '1477447674917', '角色管理', '2', '1', null, 'rest/role/sysRoleMgr', '角色管理', '1477447508878', null);
INSERT INTO `sys_permission` VALUES ('1490002273836', '1490002273833', '集群管理', '2', '1', null, 'rest/groupManage/initGroupPage', '集群管理', '1477447508878', null);
INSERT INTO `sys_permission` VALUES ('1494399173746', '1494399173729', '综合查询', '1', '1', null, 'rest/vsdtaskManage/largeDataSearch', '综合查询', '', null);
INSERT INTO `sys_permission` VALUES ('1494399268500', '1494399268490', '以图搜图', '1', '1', null, 'rest/imageQuery/toImageQuery', '以图搜图', '', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
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
INSERT INTO `sys_role` VALUES (1, '管理员', '管理员', NULL, NULL, NULL, NULL, 'admin', '1477447178527,1477447456246,1477447508878,1494399173729,1495011517915,1495011517916,1495011517917,1495011517918,1495011517919,1495011517921,1495011517926,1495011517927,1495011517928,1495011517930,1495011517931,1495011517932,1495011517934,1495011517936,1495011517937,1495011517938,1495011517941,1495011517942,1495011517943,1495011517944,1495011517945,1495011517920,1495011517947,1495011517948,1495011517949,1495011517950,1495011517951,1495011517952,1495011517953,1495011517956,1495011517957,1495011517958,1495011517959,1495011517925');
INSERT INTO `sys_role` VALUES (2, '操作员', '普通用户', NULL, NULL, NULL, NULL, 'normal', '1477447178527,1477447456246,1477447508878,1494399173729,1495011517915,1495011517916,1495011517917,1495011517918,1495011517919,1495011517921,1495011517926,1495011517927,1495011517928,1495011517930,1495011517920,1495011517951,1495011517952,1495011517953,1495011517931,1495011517936,1495011517925');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `role_permission_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=677457528 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色与权限关联表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('13326488', '1', '1495011517947');
INSERT INTO `sys_role_permission` VALUES ('13338281', '1', '1495011517920');
INSERT INTO `sys_role_permission` VALUES ('15425438', '1', '1495011517950');
INSERT INTO `sys_role_permission` VALUES ('17648529', '2', '1477447178527');
INSERT INTO `sys_role_permission` VALUES ('17835226', '1', '1495011517916');
INSERT INTO `sys_role_permission` VALUES ('21842683', '1', '1495011517917');
INSERT INTO `sys_role_permission` VALUES ('29817419', '2', '1477447508878');
INSERT INTO `sys_role_permission` VALUES ('31523192', '2', '1495011517951');
INSERT INTO `sys_role_permission` VALUES ('32561162', '1', '1495011517945');
INSERT INTO `sys_role_permission` VALUES ('33159643', '2', '1495011517919');
INSERT INTO `sys_role_permission` VALUES ('33731958', '1', '1495011517918');
INSERT INTO `sys_role_permission` VALUES ('34683393', '1', '1495011517941');
INSERT INTO `sys_role_permission` VALUES ('34826449', '1', '1495011517931');
INSERT INTO `sys_role_permission` VALUES ('35341377', '2', '1495011517917');
INSERT INTO `sys_role_permission` VALUES ('35721853', '2', '1495011517916');
INSERT INTO `sys_role_permission` VALUES ('38185272', '2', '1495011517926');
INSERT INTO `sys_role_permission` VALUES ('38246729', '2', '1495011517927');
INSERT INTO `sys_role_permission` VALUES ('38469136', '1', '1477447456246');
INSERT INTO `sys_role_permission` VALUES ('38864173', '2', '1495011517952');
INSERT INTO `sys_role_permission` VALUES ('38864174', '2', '1495011517953');
INSERT INTO `sys_role_permission` VALUES ('42296762', '1', '1495011517930');
INSERT INTO `sys_role_permission` VALUES ('42486152', '1', '1495011517925');
INSERT INTO `sys_role_permission` VALUES ('46371133', '1', '1495011517919');
INSERT INTO `sys_role_permission` VALUES ('49211134', '2', '1495011517918');
INSERT INTO `sys_role_permission` VALUES ('49357952', '1', '1494399173729');
INSERT INTO `sys_role_permission` VALUES ('52466371', '1', '1495011517942');
INSERT INTO `sys_role_permission` VALUES ('54251936', '1', '1495011517921');
INSERT INTO `sys_role_permission` VALUES ('54787995', '2', '1495011517930');
INSERT INTO `sys_role_permission` VALUES ('55243181', '1', '1495011517936');
INSERT INTO `sys_role_permission` VALUES ('55243182', '2', '1495011517936');
INSERT INTO `sys_role_permission` VALUES ('56317925', '1', '1495011517927');
INSERT INTO `sys_role_permission` VALUES ('56525278', '2', '1495011517925');
INSERT INTO `sys_role_permission` VALUES ('56719658', '1', '1495011517952');
INSERT INTO `sys_role_permission` VALUES ('56719659', '1', '1495011517953');
INSERT INTO `sys_role_permission` VALUES ('57259565', '1', '1477447508878');
INSERT INTO `sys_role_permission` VALUES ('57453717', '2', '1495011517928');
INSERT INTO `sys_role_permission` VALUES ('59982157', '2', '1477447456246');
INSERT INTO `sys_role_permission` VALUES ('61448713', '2', '1495011517920');
INSERT INTO `sys_role_permission` VALUES ('63657519', '1', '1495011517944');
INSERT INTO `sys_role_permission` VALUES ('64248315', '2', '1495011517921');
INSERT INTO `sys_role_permission` VALUES ('64298417', '1', '1495011517937');
INSERT INTO `sys_role_permission` VALUES ('66725358', '1', '1495011517915');
INSERT INTO `sys_role_permission` VALUES ('71519119', '1', '1477447178527');
INSERT INTO `sys_role_permission` VALUES ('73267677', '1', '1495011517938');
INSERT INTO `sys_role_permission` VALUES ('73744815', '1', '1495011517949');
INSERT INTO `sys_role_permission` VALUES ('78413238', '1', '1495011517926');
INSERT INTO `sys_role_permission` VALUES ('82313998', '1', '1495011517948');
INSERT INTO `sys_role_permission` VALUES ('84181478', '1', '1495011517934');
INSERT INTO `sys_role_permission` VALUES ('84537964', '1', '1495011517932');
INSERT INTO `sys_role_permission` VALUES ('87347656', '1', '1495011517928');
INSERT INTO `sys_role_permission` VALUES ('89525736', '2', '1495011517915');
INSERT INTO `sys_role_permission` VALUES ('89924953', '1', '1495011517951');
INSERT INTO `sys_role_permission` VALUES ('94769532', '1', '1495011517943');
INSERT INTO `sys_role_permission` VALUES ('96843613', '2', '1494399173729');
INSERT INTO `sys_role_permission` VALUES ('34826448', '2', '1495011517931');


-- ----------------------------
-- Table structure for tb_monitor_group
-- ----------------------------
DROP TABLE IF EXISTS `tb_monitor_group`;
CREATE TABLE `tb_monitor_group` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '监控组名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='专项监控组表';

-- ----------------------------
-- Records of tb_monitor_group
-- ----------------------------
INSERT INTO `tb_monitor_group` VALUES ('1', '重要监控点', '2019-05-05 18:46:00', '2019-05-05 18:46:11', '1', null);

-- ----------------------------
-- Table structure for tb_monitor_group_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_monitor_group_detail`;
CREATE TABLE `tb_monitor_group_detail` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `monitor_group_id` bigint(32) NOT NULL COMMENT '监控组ID',
  `camera_id` bigint(20) NOT NULL COMMENT '监控点ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人编号',
  PRIMARY KEY (`id`),
  KEY `index_mp_id` (`monitor_group_id`),
  KEY `index_camera_id` (`camera_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专项监控组详情表';

-- ----------------------------
-- Records of tb_monitor_group_detail
-- ----------------------------

-- 增大字段长度
ALTER TABLE `tb_imagesearch_task`
MODIFY COLUMN `desc`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述' AFTER `name`;

INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'monitor.group.limit', '其他类别@监控组大小', '50', '限制一个监控组最大可加入的监控点总数', '2019-05-25 09:57:21');
INSERT INTO `cfg_mem_props` VALUES ('qst_u2s', 'text-page-index-menu-03-04-text', '菜单类别@系统二级菜单03-04', '专项监控组', '快检：专项监控组', '2019-05-25 09:57:21');

-- ----------------------------
-- Records of ctrl_unit_file
-- ----------------------------
ALTER TABLE ctrl_unit_file ADD COLUMN tripwires VARCHAR(255) DEFAULT NULL COMMENT '跨线参数' AFTER uninterest_param;

ALTER TABLE task_clean_log ADD COLUMN slave_id VARCHAR(32) DEFAULT NULL COMMENT '服务器节点ID';
ALTER TABLE task_clean_log ADD COLUMN retry_count int(11) DEFAULT 0 COMMENT '重试次数';
ALTER TABLE task_clean_log ADD COLUMN update_time datetime DEFAULT NULL COMMENT '更新时间';

ALTER TABLE task_clean_ymddata ADD COLUMN retry_count int(11) DEFAULT 0 COMMENT '重试次数';
ALTER TABLE task_clean_ymddata ADD COLUMN update_time datetime DEFAULT NULL COMMENT '更新时间';

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `visit_num` int(4) NOT NULL COMMENT '访问次数',
  `operate_ip` varchar(20) NOT NULL COMMENT '操作的IP',
  `operate_module` varchar(255) NOT NULL COMMENT '操作的模块',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `visit_num` int(4) NOT NULL COMMENT '访问次数',
  `operate_ip` varchar(20) NOT NULL COMMENT '操作的IP',
  `operate_module` varchar(255) NOT NULL COMMENT '操作的模块',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作日志';

alter table vsd_task modify column errmsg varchar(1024);

INSERT INTO cfg_mem_props (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`)
VALUES ('u2s_recog', 'baidu.club.url', '百度行人是否持有棍棒识别地址', 'http://172.16.1.52:9081/images/cudgel/recog', '百度行人是否持有棍棒识别地址', '2019-08-02 09:21:27');

INSERT INTO cfg_mem_props (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`)
VALUES ('u2s_recog', 'baidu.club.knife', '百度行人是否持刀阈值', '0.5', '百度行人是否持刀阈值', '2019-08-02 09:21:27');

INSERT INTO cfg_mem_props (`module_name`, `prop_key`, `prop_name`, `prop_value`, `prop_desc`, `update_time`)
VALUES ('u2s_recog', 'baidu.club.cudgel', '百度行人是否持棍阈值', '0.45', '百度行人是否持棍阈值', '2019-08-02 09:21:27');


CREATE TABLE `vsd_task_retry_log` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serialnumber` varchar(50) NOT NULL COMMENT '任务号',
  `retry_time` datetime NOT NULL COMMENT 'App重试时间',
  `clean_time` datetime DEFAULT NULL COMMENT '数据清理时间',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '清理状态 0：待启动;1:已完成',
  `clean_count` int(12) DEFAULT NULL COMMENT '清理数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `vsd_task_status_trigger`;
CREATE TRIGGER `vsd_task_status_trigger` AFTER UPDATE ON `vsd_task` FOR EACH ROW BEGIN

SET @oldStatus = (
        SELECT
                old.STATUS
        FROM
                vsd_task
        WHERE
                id = new.id
        LIMIT 1
);

SET @taskType=(SELECT task_type from tb_analysis_task WHERE id = new.userserialnumber);


IF @oldStatus = 1
AND new.STATUS = 0 AND @taskType != 1 THEN
        INSERT INTO vsd_task_retry_log (serialnumber, retry_time)
VALUE
        (
                new.serialnumber,
                IFNULL(new.endtime,NOW())
        );

END
IF;

END;

