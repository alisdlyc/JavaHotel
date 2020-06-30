/*
 Navicat Premium Data Transfer

 Source Server         : hotel
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : hotel

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 30/06/2020 14:27:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for usr
-- ----------------------------
DROP TABLE IF EXISTS `usr`;
CREATE TABLE `usr`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_520_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_520_ci NOT NULL COMMENT '密码',
  `authority` int(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '权限值，0为普通用户,1为管理员,2为root用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usr
-- ----------------------------
INSERT INTO `usr` VALUES (1, 'qwq', '5252634', 0);
INSERT INTO `usr` VALUES (2, 'alisdlyc', '123456', 0);
INSERT INTO `usr` VALUES (6, 'alisdlsyc', '123456', 0);
INSERT INTO `usr` VALUES (7, 'username', 'password', 0);
INSERT INTO `usr` VALUES (8, 'username2', 'password', 0);
INSERT INTO `usr` VALUES (9, 'user', 'qwq', 0);
INSERT INTO `usr` VALUES (10, 'root', 'root', 2);
INSERT INTO `usr` VALUES (11, 'admin', '2323', 1);

SET FOREIGN_KEY_CHECKS = 1;
