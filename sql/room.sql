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

 Date: 01/07/2020 00:10:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `roomnumber` int(0) NOT NULL COMMENT '如：101（第一个1代表楼层，后两位代表房间编号用）',
  `capacity` int(0) NOT NULL DEFAULT 1 COMMENT '房间的容量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (5, 1, 1);
INSERT INTO `room` VALUES (6, 2, 2);
INSERT INTO `room` VALUES (7, 3, 1);

SET FOREIGN_KEY_CHECKS = 1;
