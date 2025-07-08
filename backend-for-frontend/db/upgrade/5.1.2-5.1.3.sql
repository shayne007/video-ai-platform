ALTER TABLE `ctrl_unit_file` ADD COLUMN `enable_bike_to_human`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'false' COMMENT '室内室外' AFTER `enable_independent_face_snap`;
ALTER TABLE `ctrl_unit_file` ADD COLUMN `sensitivity`  smallint(4) NULL DEFAULT 0 COMMENT '白天夜晚场景' AFTER `enable_bike_to_human`;

ALTER TABLE `tb_imagesearch_task` ADD COLUMN `uuid_pictrue_json`  varchar(2000) NULL DEFAULT NULL COMMENT 'uuid和对应图片url的json字段' AFTER `camera_names`;
ALTER TABLE `tb_imagesearch_task` ADD COLUMN `task_id`  varchar(255) NULL DEFAULT NULL COMMENT '任务serialnumber' AFTER `uuid_pictrue_json`;
ALTER TABLE `tb_imagesearch_task` ADD COLUMN `user_serialnumber`  varchar(255) NULL DEFAULT NULL COMMENT '搜图图片对应主任务号' AFTER `task_id`;
ALTER TABLE `tb_relaytrack_task` ADD COLUMN `uuid` varchar(255)  NULL DEFAULT NULL COMMENT '图片uuid' AFTER `feature`;
ALTER TABLE `tb_relaytrack_task` ADD COLUMN `analysis_id` varchar(255)  NULL DEFAULT NULL COMMENT '图片子任务号' AFTER `uuid`;