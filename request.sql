/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : request

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 22/11/2022 20:18:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for request
-- ----------------------------
DROP TABLE IF EXISTS `request`;
CREATE TABLE `request` (
  `id` bigint NOT NULL COMMENT '主键',
  `factory` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '工厂',
  `system_use` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '系统',
  `create_time` datetime NOT NULL COMMENT '接收时间',
  `background` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '背景现状',
  `description` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '需求描述',
  `request_value` varchar(8) COLLATE utf8mb4_general_ci NOT NULL COMMENT '价值体现',
  `department` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '提出部门',
  `request_person` varchar(8) COLLATE utf8mb4_general_ci NOT NULL COMMENT '提出人',
  `create_user` bigint NOT NULL COMMENT '检讨人',
  `request_time` datetime NOT NULL COMMENT '期望日期',
  `urgent` int NOT NULL COMMENT '紧急程度',
  `progress` int DEFAULT NULL COMMENT '进度',
  `start_time` datetime DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime DEFAULT NULL COMMENT '结束日期',
  `complete_status` int NOT NULL DEFAULT '0' COMMENT '状态 0未完成 1完成',
  `take_status` int NOT NULL DEFAULT '0' COMMENT '领取状态 0未领取 1已经领取',
  `status_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of request
-- ----------------------------
BEGIN;
INSERT INTO `request` VALUES (1, 'mtest', 'metest', '2022-01-01 00:00:00', 'test', 'test', 'test', 'mod', '袁', 1, '2022-01-01 00:00:00', 10, NULL, NULL, NULL, 1, 0, 0);
INSERT INTO `request` VALUES (2, 'mtest2', 'mtest2', '2022-01-02 00:00:00', 'test2', 'test2', 'test2', 'mod2', '李', 1, '2022-01-02 00:00:00', 5, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `request` VALUES (3, 'mtest333', 'mtest333', '2022-01-03 00:00:00', 'test333', 'test333', 'test333', 'mod333', '何', 1, '2022-01-03 00:00:00', 4, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `request` VALUES (4, 'mtest4', 'mtest4', '2022-01-04 00:00:00', 'test4', 'test4', 'test4', 'mod4', '凌', 1, '2022-01-04 00:00:00', 9, NULL, NULL, NULL, 1, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='员工信息';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13812312312', '1', 1, '2021-05-06 17:20:07', '2021-05-10 02:24:09');
INSERT INTO `user` VALUES (1580183395554672641, '张三', 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '13212345678', '1', 1, '2022-10-12 21:07:35', '2022-10-17 22:25:27');
INSERT INTO `user` VALUES (1582015145145434114, '李四t2', 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '17366977223', '1', 1, '2022-10-17 22:26:18', '2022-10-17 23:19:04');
INSERT INTO `user` VALUES (1582373886270492674, 'test006', 'test006', 'e10adc3949ba59abbe56e057f20f883e', '13609876542', '1', 1, '2022-10-18 22:11:48', '2022-10-18 22:16:03');
INSERT INTO `user` VALUES (1582376257524457473, 'test08', 'test08', 'e10adc3949ba59abbe56e057f20f883e', '13678906543', '1', 1, '2022-10-18 22:21:14', '2022-10-18 22:21:14');
INSERT INTO `user` VALUES (1582387020955766786, 'test10', 'test10', 'e10adc3949ba59abbe56e057f20f883e', '13209876789', '1', 1, '2022-10-18 23:04:00', '2022-10-18 23:04:00');
INSERT INTO `user` VALUES (1582747364823691266, 'test12', 'test12', 'e10adc3949ba59abbe56e057f20f883e', '13309876789', '1', 1, '2022-10-19 22:55:52', '2022-10-19 23:14:50');
INSERT INTO `user` VALUES (1594536312109576194, 'test2', '10869077', 'e10adc3949ba59abbe56e057f20f883e', '', '', 1, '2022-11-21 11:40:56', '2022-11-21 11:40:56');
INSERT INTO `user` VALUES (1594589568957435905, 'test4', '11111111', 'e10adc3949ba59abbe56e057f20f883e', '', '', 1, '2022-11-21 15:12:34', '2022-11-21 15:12:34');
INSERT INTO `user` VALUES (1594628588282327041, 'test66', '12345678', 'e10adc3949ba59abbe56e057f20f883e', '', '', 1, '2022-11-21 17:47:37', '2022-11-21 17:47:37');
INSERT INTO `user` VALUES (1594628759808389122, 'test88', '1234567890', 'e10adc3949ba59abbe56e057f20f883e', '', '', 1, '2022-11-21 17:48:18', '2022-11-21 17:48:18');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
