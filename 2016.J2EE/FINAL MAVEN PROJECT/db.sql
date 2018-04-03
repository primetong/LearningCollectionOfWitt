CREATE DATABASE IF NOT EXISTS `fx` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `fx`;

-------------
-- 用户信息
-------------

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
	`uid` 				int(11) 	NOT NULL AUTO_INCREMENT,
	`username` 			varchar(32) NOT NULL UNIQUE,
    `mail` 				varchar(32) NOT NULL UNIQUE,
	`password` 			varchar(32) NOT NULL,
	`registrationDate`	date		NOT NULL,	
	PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=100000000 DEFAULT CHARSET=utf8;

INSERT INTO `user_info` VALUES(0, 'admin', 'admin@test.com', '123456', "1970-01-01");

select * from user_info;

-------------
-- 文章
-------------

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
	`aid` 			int(11) 		NOT NULL AUTO_INCREMENT,
	`uid`			int(11) 		NOT NULL,
	`title` 		varchar(128) 	NOT NULL,
	`content`		text			BINARY NOT NULL,
	`creationDate`	datetime		NOT NULL,
    `isPrivate`		bool			NOT NULL DEFAULT FALSE,
	PRIMARY KEY (`aid`),
	foreign key(`uid`) references `user_info`(`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=100000000 DEFAULT CHARSET=utf8;

