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

 Date: 01/07/2020 00:10:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id 可作为订单编号',
  `usrname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发起订单的用户',
  `roomnumber` int(0) NOT NULL COMMENT '预定的房间号',
  `begintime` date NOT NULL COMMENT '订单起始时间',
  `endtime` date NOT NULL COMMENT '订单结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, 'qwq', 1, '2020-07-20', '2020-07-30');
INSERT INTO `order` VALUES (2, 'qwq', 1, '2020-07-01', '2020-07-10');
INSERT INTO `order` VALUES (3, 'qwq', 2, '2020-07-01', '2020-07-30');
INSERT INTO `order` VALUES (4, 'qwq', 2, '2020-07-15', '2020-07-25');
INSERT INTO `order` VALUES (9, 'root', 2, '2020-03-04', '2020-03-09');
INSERT INTO `order` VALUES (10, 'root', 1, '2020-03-04', '2020-03-09');

SET FOREIGN_KEY_CHECKS = 1;
