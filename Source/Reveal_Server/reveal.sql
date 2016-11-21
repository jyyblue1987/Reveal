# SQL-Front 5.1  (Build 4.16)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: reveal
# ------------------------------------------------------
# Server version 5.5.5-10.1.16-MariaDB

DROP DATABASE IF EXISTS `reveal`;
CREATE DATABASE `reveal` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `reveal`;

#
# Source for table chat
#

DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `receiver` varchar(255) DEFAULT NULL,
  `message` text,
  `time` int(4) DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;

#
# Dumping data for table chat
#

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,'333379007041276','1','asdasdasd',0);
INSERT INTO `chat` VALUES (2,'333379007041276','1','asdfgsdfgsdfg',0);
INSERT INTO `chat` VALUES (4,'1','333379007041276','aadsaaaaaaaaaadfa',0);
INSERT INTO `chat` VALUES (5,'333379007041276','1','adfadf',0);
INSERT INTO `chat` VALUES (6,'333379007041276','1','sfdgsdfg',0);
INSERT INTO `chat` VALUES (7,'333379007041276','1','sfgsdfg',0);
INSERT INTO `chat` VALUES (8,'333379007041276','1','sfg',0);
INSERT INTO `chat` VALUES (9,'333379007041276','1','my',0);
INSERT INTO `chat` VALUES (10,'333379007041276','1','asdf',0);
INSERT INTO `chat` VALUES (11,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (12,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (13,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (14,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (15,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (16,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (17,'3','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (18,'3','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (19,'3','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (20,'3','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (21,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (22,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (23,'23','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (24,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (25,'23','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (26,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (27,'23','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (28,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (29,'23','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (30,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (31,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (32,'1','333379007041276','dfghdfgh',0);
INSERT INTO `chat` VALUES (33,'333379007041276','1',' WHAT ARE YOU DOING',0);
INSERT INTO `chat` VALUES (34,'333379007041276','1','Hi i am tom who are you ',0);
INSERT INTO `chat` VALUES (35,'333379007041276','3','how are you',0);
INSERT INTO `chat` VALUES (36,'333379007041276','3','hello',0);
INSERT INTO `chat` VALUES (37,'333379007041276','3','what are you doing',0);
INSERT INTO `chat` VALUES (38,'333379007041276','3','aaaaaaaaaa',0);
INSERT INTO `chat` VALUES (39,'333379007041276','1','HEIIO',0);
INSERT INTO `chat` VALUES (40,'333379007041276','1','ok',0);
INSERT INTO `chat` VALUES (41,'333379007041276','23','how are you',0);
INSERT INTO `chat` VALUES (42,'333379007041276','1','dkdklldkjfaf',0);
INSERT INTO `chat` VALUES (43,'333379007041276','23','ddddddddddd',0);
INSERT INTO `chat` VALUES (44,'333379007041276','23','ssssssssssssss',0);
INSERT INTO `chat` VALUES (45,'333379007041276','23','aaaaaaaaaaaaaa',0);
INSERT INTO `chat` VALUES (46,'333379007041276','23','sssssssssssssssssss',0);
INSERT INTO `chat` VALUES (47,'333379007041276','1','complicated',0);
INSERT INTO `chat` VALUES (48,'333379007041276','1','Hello',10);
INSERT INTO `chat` VALUES (49,'333379007041276','1','Nice day.',10);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table friend
#

DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid1` varchar(255) DEFAULT NULL,
  `facebookid2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

#
# Dumping data for table friend
#

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
INSERT INTO `friend` VALUES (3,'2','6');
INSERT INTO `friend` VALUES (6,'1','3');
INSERT INTO `friend` VALUES (7,'3','1');
INSERT INTO `friend` VALUES (8,'333379007041276','1');
INSERT INTO `friend` VALUES (10,'3','333379007041276');
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table matching
#

DROP TABLE IF EXISTS `matching`;
CREATE TABLE `matching` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid1` varchar(255) DEFAULT NULL,
  `facebookid2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

#
# Dumping data for table matching
#

LOCK TABLES `matching` WRITE;
/*!40000 ALTER TABLE `matching` DISABLE KEYS */;
INSERT INTO `matching` VALUES (1,'1','2');
INSERT INTO `matching` VALUES (2,'1','3');
INSERT INTO `matching` VALUES (3,'2','3');
INSERT INTO `matching` VALUES (19,'333379007041276','23');
/*!40000 ALTER TABLE `matching` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table notification
#

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `notekind` varchar(255) DEFAULT NULL,
  `sendtime` bigint(20) DEFAULT NULL,
  `feedval` text,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=590 DEFAULT CHARSET=latin1;

#
# Dumping data for table notification
#

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'a','g','matchRequest',0,NULL);
INSERT INTO `notification` VALUES (231,'333379007041276','2','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (233,'333379007041276','2','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (311,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (332,'333379007041276','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (334,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (336,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (338,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (340,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (345,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (528,'333379007041276','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (549,'333379007041276','1','newfeed',11,'photo2.jpg');
INSERT INTO `notification` VALUES (551,'333379007041276','1','newfeed',11,'photo8.jpg');
INSERT INTO `notification` VALUES (553,'333379007041276','1','newfeed',11,'photo4.jpg');
INSERT INTO `notification` VALUES (555,'333379007041276','1','newfeed',11,'photo7.jpg');
INSERT INTO `notification` VALUES (557,'333379007041276','1','newfeed',11,'photo3.jpg');
INSERT INTO `notification` VALUES (563,'333379007041276','1','newfeed',11,'photo1.jpg');
INSERT INTO `notification` VALUES (564,'333379007041276','1','newfeed',11,'photo1.jpg');
INSERT INTO `notification` VALUES (568,'333379007041276','1','newfeed',11,'face6.png');
INSERT INTO `notification` VALUES (587,'333379007041276','1','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (589,'333379007041276','3','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (590,'1','11','requestfriend',0,'photo2.jpg');
INSERT INTO `notification` VALUES (591,'1','11','acceptfriend',0,'photo2.jpg');
INSERT INTO `notification` VALUES (592,'1','11','newmatch',0,'photo8.jpg');
INSERT INTO `notification` VALUES (593,'1','11','comment',0,'photo4.jpg');
INSERT INTO `notification` VALUES (594,'1','11','matchRequest',0,'photo7.jpg');
INSERT INTO `notification` VALUES (595,'1','11','newfeed',10,'photo1.jpg');
INSERT INTO `notification` VALUES (596,'1','11','message',10,'aaaa');
INSERT INTO `notification` VALUES (597,'1','11','like',10,'photo3.jpg');
INSERT INTO `notification` VALUES (606,'1','11','newfeed',11,'photo2.jpg');
INSERT INTO `notification` VALUES (650,'1','333379007041276','requestfriend',0,'photo2.jpg');
INSERT INTO `notification` VALUES (651,'1','333379007041276','acceptfriend',0,'photo2.jpg');
INSERT INTO `notification` VALUES (652,'1','333379007041276','newmatch',0,'photo8.jpg');
INSERT INTO `notification` VALUES (653,'1','333379007041276','comment',0,'photo4.jpg');
INSERT INTO `notification` VALUES (654,'1','333379007041276','matchRequest',0,'photo7.jpg');
INSERT INTO `notification` VALUES (655,'1','333379007041276','newfeed',10,'photo1.jpg');
INSERT INTO `notification` VALUES (656,'1','333379007041276','message',10,'aaaa');
INSERT INTO `notification` VALUES (657,'1','333379007041276','like',10,'photo3.jpg');
INSERT INTO `notification` VALUES (658,'1','333379007041276','newfeed',11,'photo2.jpg');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table photo
#

DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid` varchar(255) DEFAULT NULL,
  `photopath` varchar(255) DEFAULT NULL,
  `ratesum` int(11) DEFAULT '0',
  `ratenumber` int(11) DEFAULT '0',
  `reportgp` int(11) DEFAULT '0',
  `reportom` int(11) DEFAULT '0',
  `reportnap` int(11) DEFAULT '0',
  `reportwg` int(11) DEFAULT '0',
  `reportfls` int(11) DEFAULT '0',
  `commentnum` int(11) DEFAULT '0',
  `commentcon` text,
  `likenum` int(11) DEFAULT '0',
  `likefacebookid` text COMMENT 'the users that like this photo. seperate with "^".',
  `mycomment` text,
  `name` varchar(255) DEFAULT '',
  `rate` int(11) DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;

#
# Dumping data for table photo
#

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
INSERT INTO `photo` VALUES (40,'1','photo1.jpg',0,0,0,0,0,0,0,3,'YakYong Jong&who are you&333379007041276^YakYong Jong&aldkf&333379007041276^YakYong Jong&i have something another things.&333379007041276',8,'333379007041276&YakYong Jong^333379007041276&YakYong Jong^333379007041276&Pyon^333379007041276&Pyon^333379007041276&Pyon^333379007041276&Pyon^333379007041276&YakYong Jong^333379007041276&YakYong Jong','this is my comment','Alexsandria',0);
INSERT INTO `photo` VALUES (41,'1','photo2.jpg',0,0,0,0,0,0,0,3,'YakYong Jong&who are you&333379007041276^YakYong Jong&very good.&333379007041276^YakYong Jong& another things.&333379007041276',1,'333379007041276&YakYong Jong','this is my second comment','Alexsandria',0);
INSERT INTO `photo` VALUES (42,'1','photo3.jpg',0,0,0,0,0,0,0,0,'',2,'333379007041276&YakYong Jong^333379007041276&YakYong Jong','aboutphoto','Alexsandria',0);
INSERT INTO `photo` VALUES (43,'1','photo4.jpg',0,0,0,0,0,0,0,0,'',2,'333379007041276&YakYong Jong^333379007041276&YakYong Jong','this is my fourth comment','Alexsandria',0);
INSERT INTO `photo` VALUES (44,'1','photo5.jpg',0,0,0,0,0,0,0,0,'',2,'333379007041276&YakYong Jong^333379007041276&YakYong Jong','this is my fifth comment','Alexsandria',0);
INSERT INTO `photo` VALUES (45,'1','photo6.jpg',0,0,0,0,0,0,0,0,'',2,'333379007041276&YakYong Jong^333379007041276&YakYong Jong','this is my sixth comment','Alexsandria',0);
INSERT INTO `photo` VALUES (52,'333379007041276','photo7.jpg',0,0,0,0,0,0,0,0,'',0,'','Hello','YYj',0);
INSERT INTO `photo` VALUES (53,'333379007041276','photo4.jpg',0,0,0,0,0,0,0,1,'Jane&who are you&1',0,'','Hello','YYj',0);
INSERT INTO `photo` VALUES (54,'333379007041276','photo3.jpg',0,0,0,0,0,0,0,0,'',1,'1&Jane','gggggggggggggg','YYj',0);
INSERT INTO `photo` VALUES (55,'333379007041276','photo2.jpg',0,0,0,0,0,0,0,0,'',0,'','Hello','YYj',0);
INSERT INTO `photo` VALUES (56,'333379007041276','photo1.jpg',0,0,0,0,0,0,0,3,'YakYong Jong&who are you&333379007041276^YakYong Jong&aldkf&333379007041276^YakYong Jong&i have something another things.&333379007041276',0,'','My photo','YYj',0);
INSERT INTO `photo` VALUES (94,'3','photo1.jpg',44,53,0,0,0,0,0,0,'',0,'',' i ma 3','Jane',0);
INSERT INTO `photo` VALUES (97,'3','photo4.jpg',19,16,0,0,0,0,0,0,'',0,' ',' you a 333','Jane',0);
INSERT INTO `photo` VALUES (98,'3','photo5.jpg',7,18,0,0,0,0,0,0,'',0,' ','hello','Jane',0);
INSERT INTO `photo` VALUES (99,'3','photo6.jpg',14,27,0,0,0,1,0,0,'',0,' ',' dok','Jane',0);
INSERT INTO `photo` VALUES (100,'3','photo7.jpg',24,25,0,0,0,1,0,0,'',0,' ',' missing pot','Jane',0);
INSERT INTO `photo` VALUES (101,'3','photo8.jpg',35,23,0,0,0,0,0,0,'',0,' ','probable','Jane',0);
INSERT INTO `photo` VALUES (102,'3','photo2.jpg',37,22,0,0,0,0,0,0,'',3,'333379007041276&tom^333379007041276&tom^333379007041276&tom','Hello','Jane',0);
INSERT INTO `photo` VALUES (103,'3','photo3.jpg',20,23,0,0,0,0,0,0,'',0,' ','Hello','Jane',0);
INSERT INTO `photo` VALUES (115,'333379007041276','photo8.jpg',0,0,0,0,0,0,0,0,'',0,'','Beautiful?','YYj',0);
INSERT INTO `photo` VALUES (116,'333379007041276','photo8.jpg',0,0,0,0,0,0,0,0,'',0,'','Very Gook!','YYj',0);
INSERT INTO `photo` VALUES (117,'333379007041276','face6.png',0,0,0,0,0,0,0,0,'',0,'','Hello','YYj',0);
INSERT INTO `photo` VALUES (118,'333379007041276','face5.png',0,0,0,0,0,0,0,0,'',0,'','Very Gook!','YYj',0);
INSERT INTO `photo` VALUES (119,'333379007041276','face4.png',0,0,0,0,0,0,0,0,'',0,'','Nice!','YYj',0);
INSERT INTO `photo` VALUES (120,'333379007041276','face3.png',0,0,0,0,0,0,0,0,'',0,'','Glance!','YYj',0);
INSERT INTO `photo` VALUES (121,'333379007041276','face2.png',0,0,0,0,0,0,0,0,'',0,'','Glance!','YYj',0);
INSERT INTO `photo` VALUES (122,'333379007041276','face1.png',0,0,0,0,0,0,0,0,'',0,'','See Me!','YYj',0);
INSERT INTO `photo` VALUES (123,'333379007041276','photo8.jpg',0,0,0,0,0,0,0,0,'',0,'','See me!','YYj',0);
INSERT INTO `photo` VALUES (127,'44','photo3.jpg',73,11,0,0,0,0,0,0,'',0,'','hello','jim',7);
INSERT INTO `photo` VALUES (128,'44','photo4.jpg',73,11,0,0,0,0,0,0,'',0,'','My Photo!','jim',7);
/*!40000 ALTER TABLE `photo` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table users
#

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `totalrate` varchar(255) DEFAULT NULL,
  `profilephoto` varchar(255) DEFAULT '',
  `email` varchar(255) DEFAULT NULL,
  `locationx` float DEFAULT NULL,
  `locationy` float DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

#
# Dumping data for table users
#

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'1','a','aa','man',18,'10','photo1.jpg','aemail',0,0);
INSERT INTO `users` VALUES (2,'2','b','bb','man',19,'5','photo2.jpg','aemail',1,1);
INSERT INTO `users` VALUES (6,'23','f',NULL,NULL,19,NULL,'photo3.jpg','aemail',0,0);
INSERT INTO `users` VALUES (7,'333379007041276','YakYong Jong',NULL,'male',18,'0','photo8.jpg','jyyblue1987@outlook.com',0,0);
INSERT INTO `users` VALUES (8,'44','tom',NULL,'male',23,NULL,'photo5.jpg',NULL,NULL,NULL);
INSERT INTO `users` VALUES (9,'3','d','dd','man',52,'0','photo6.jpg','fnbvgh',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
