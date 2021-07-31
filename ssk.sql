/*
 Navicat MySQL Data Transfer

 Source Server         : ssk
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 47.101.196.193:3306
 Source Schema         : ssk

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 31/07/2021 17:07:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for calendar
-- ----------------------------
DROP TABLE IF EXISTS `calendar`;
CREATE TABLE `calendar` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `datelist` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for datelist1
-- ----------------------------
DROP TABLE IF EXISTS `datelist1`;
CREATE TABLE `datelist1` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `datelist` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for num
-- ----------------------------
DROP TABLE IF EXISTS `num`;
CREATE TABLE `num` (
  `i` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_access
-- ----------------------------
DROP TABLE IF EXISTS `t_access`;
CREATE TABLE `t_access` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `from_id` int(20) DEFAULT NULL,
  `to_id` int(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `day` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_attention
-- ----------------------------
DROP TABLE IF EXISTS `t_attention`;
CREATE TABLE `t_attention` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `from_id` int(50) NOT NULL,
  `to_id` int(50) NOT NULL,
  `firend_name` varchar(50) DEFAULT NULL,
  `firend_type` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `status` bigint(1) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_chat_list
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_list`;
CREATE TABLE `t_chat_list` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `from_id` int(20) DEFAULT NULL COMMENT '自己的id',
  `to_id` int(20) DEFAULT NULL COMMENT '朋友id',
  `firend_name` varchar(30) DEFAULT '' COMMENT '好友备注',
  `after_time` datetime DEFAULT NULL,
  `firend_type` int(20) DEFAULT NULL COMMENT '好友类型',
  `status` bigint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_collect
-- ----------------------------
DROP TABLE IF EXISTS `t_collect`;
CREATE TABLE `t_collect` (
  `id` int(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `c_id` int(20) DEFAULT NULL,
  `t_id` int(20) DEFAULT NULL,
  `u_id` int(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `userpic` varchar(255) DEFAULT NULL,
  `title` varchar(555) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `u_id` int(20) NOT NULL,
  `parent_id` int(20) DEFAULT '0',
  `t_id` int(20) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `dalete_time` datetime DEFAULT NULL,
  `child` bigint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_history
-- ----------------------------
DROP TABLE IF EXISTS `t_history`;
CREATE TABLE `t_history` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `t_id` int(20) NOT NULL,
  `u_id` int(20) DEFAULT NULL,
  `c_id` int(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `userpic` varchar(255) DEFAULT NULL,
  `title` varchar(555) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `u_id` int(50) DEFAULT NULL,
  `kind` varchar(50) DEFAULT NULL COMMENT '监控指标的大类',
  `type` varchar(50) DEFAULT NULL COMMENT 'error //小类型 这是一个错误',
  `error_type` varchar(50) DEFAULT NULL COMMENT ' ''resourceError'',//js或css资源加载错误',
  `filename` varchar(255) DEFAULT NULL COMMENT '//哪个文件报错了',
  `tag_name` varchar(255) DEFAULT NULL COMMENT '/SCRIPT',
  `selector` varchar(555) DEFAULT NULL COMMENT '代表最后一个操作的元素',
  `position` varchar(255) DEFAULT NULL COMMENT '保存定位',
  `message` varchar(1024) DEFAULT NULL COMMENT '报错信息',
  `stack` varchar(2024) DEFAULT NULL COMMENT '报错堆栈信息',
  `pathname` varchar(255) DEFAULT NULL COMMENT '请求路径',
  `response` varchar(2024) DEFAULT NULL COMMENT '响应体',
  `params` varchar(2024) DEFAULT NULL COMMENT '请求参数',
  `status` varchar(20) DEFAULT NULL COMMENT 'qingqiu zhaugntaim',
  `user_agent` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '报错url',
  `title` varchar(255) DEFAULT NULL COMMENT '网页标题',
  `timestamp` varchar(255) DEFAULT NULL COMMENT '时间戳',
  `largest_contentful_paint` varchar(255) DEFAULT NULL,
  `first_contentful_paint` varchar(255) DEFAULT NULL,
  `first_meaningful_paint` varchar(255) DEFAULT NULL,
  `first_paint` varchar(255) DEFAULT NULL,
  `input_delay` varchar(255) DEFAULT NULL COMMENT '延时的时间',
  `duration` varchar(255) DEFAULT NULL COMMENT '处理的时间',
  `start_time` varchar(255) DEFAULT NULL COMMENT '开始时间',
  `connect_time` varchar(255) DEFAULT NULL COMMENT '连接时间',
  `ttfb_time` varchar(60) DEFAULT NULL COMMENT '首字节到达时间',
  `response_time` varchar(60) DEFAULT NULL,
  `parse_dom_time` varchar(60) DEFAULT NULL,
  `dom_content_loaded_time` varchar(60) DEFAULT NULL,
  `dns_time` varchar(60) DEFAULT NULL,
  `time_to_interactive` varchar(60) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `load_time` varchar(60) DEFAULT NULL,
  `user_ip` varchar(255) DEFAULT NULL,
  `user_adress` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17577 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(20) DEFAULT '0',
  `ip` varchar(255) DEFAULT NULL,
  `adress` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` bigint(1) DEFAULT NULL COMMENT '0未读，1已读',
  `delete_time` datetime DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  `from_id` int(30) DEFAULT NULL,
  `to_id` int(30) DEFAULT NULL,
  `c_id` int(20) DEFAULT NULL COMMENT '聊天室id',
  `belong` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=917 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `t_sign_in`;
CREATE TABLE `t_sign_in` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `u_id` int(30) DEFAULT NULL,
  `sign_In` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `adress` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_title_class
-- ----------------------------
DROP TABLE IF EXISTS `t_title_class`;
CREATE TABLE `t_title_class` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `t_id` int(30) DEFAULT NULL COMMENT 'topic id',
  `c_id` int(30) DEFAULT NULL COMMENT '标签id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_topic
-- ----------------------------
DROP TABLE IF EXISTS `t_topic`;
CREATE TABLE `t_topic` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `c_id` int(20) NOT NULL,
  `u_id` int(20) DEFAULT NULL,
  `title` varchar(255) NOT NULL COMMENT '话题# xx #',
  `url_type` varchar(255) DEFAULT NULL COMMENT '图片还是视频',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `images` varchar(1000) DEFAULT NULL,
  `display` tinyint(1) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_topic_active
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_active`;
CREATE TABLE `t_topic_active` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `t_id` int(50) NOT NULL,
  `u_id` int(50) NOT NULL,
  `t_active` int(10) NOT NULL,
  `t_uid` int(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_topic_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_detail`;
CREATE TABLE `t_topic_detail` (
  `t_id` int(20) NOT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `hits` int(20) DEFAULT NULL COMMENT '热度',
  `digest` bigint(1) DEFAULT NULL,
  `like_num` int(20) DEFAULT NULL COMMENT '点赞数',
  `tread_num` int(20) DEFAULT NULL COMMENT '踩数',
  `comment_num` int(20) DEFAULT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_topic_title
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_title`;
CREATE TABLE `t_topic_title` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `c_id` int(30) DEFAULT NULL COMMENT '所属分类的id',
  `display` bigint(1) DEFAULT NULL,
  `title` varchar(55) NOT NULL,
  `u_id` int(30) NOT NULL,
  `title_pic` varchar(255) DEFAULT NULL,
  `total` int(30) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_topic_title_class
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_title_class`;
CREATE TABLE `t_topic_title_class` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `u_id` int(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(60) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `phone_number` varchar(30) DEFAULT NULL,
  `status` bigint(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `author_url` varchar(555) DEFAULT 'http://image.xquery.cn/2020eb384401aa616ba134126357_th.jpg',
  `description` varchar(255) DEFAULT NULL,
  `gender` int(10) DEFAULT '0' COMMENT '0男1女',
  `birthday` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `occupation` varchar(65) DEFAULT NULL COMMENT '职业',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10069 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
