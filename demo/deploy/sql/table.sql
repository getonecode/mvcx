
DROP TABLE IF EXISTS `b_blog`;
CREATE TABLE `b_blog` (
  `blog_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `blog_title` varchar(128) NOT NULL COMMENT '博客标题',
  `blog_content` text NOT NULL COMMENT '博客内容',
  `blog_status` tinyint(1) DEFAULT '0' COMMENT '用状态',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_update` datetime NOT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `b_abc_desc`;
CREATE TABLE `b_abc_desc` (
  `desc_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `desc_title` varchar(128) NOT NULL COMMENT '博客标题',
  `desc_content` text NOT NULL COMMENT '博客内容',
  `desc_status` tinyint(1) DEFAULT '0' COMMENT '用状态',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_update` datetime NOT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`desc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

