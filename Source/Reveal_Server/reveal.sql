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
# Source for table block
#

DROP TABLE IF EXISTS `block`;
CREATE TABLE `block` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(255) DEFAULT '',
  `blockfacebookid` varchar(255) DEFAULT '',
  `blocksort` varchar(255) DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

#
# Dumping data for table block
#

LOCK TABLES `block` WRITE;
/*!40000 ALTER TABLE `block` DISABLE KEYS */;
INSERT INTO `block` VALUES (6,'333379007041276','337825819928904','Feels Like Spam');
/*!40000 ALTER TABLE `block` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

#
# Dumping data for table chat
#

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (2,'1','333379007041276','Hello!',0);
INSERT INTO `chat` VALUES (4,'333379007041276','1','Hi',0);
INSERT INTO `chat` VALUES (7,'337825819928904','1','hello',0);
INSERT INTO `chat` VALUES (8,'337825819928904','333379007041276','hello',0);
INSERT INTO `chat` VALUES (9,'333379007041276','337825819928904','HI',0);
INSERT INTO `chat` VALUES (10,'333379007041276','337825819928904','AAA',0);
INSERT INTO `chat` VALUES (11,'337825819928904','333379007041276','BBB',0);
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
  `name1` varchar(255) DEFAULT '',
  `name2` varchar(255) DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

#
# Dumping data for table friend
#

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
INSERT INTO `friend` VALUES (6,'1','3','Jane','Pumbba');
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
  `name1` varchar(255) DEFAULT '',
  `name2` varchar(255) DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

#
# Dumping data for table matching
#

LOCK TABLES `matching` WRITE;
/*!40000 ALTER TABLE `matching` DISABLE KEYS */;
INSERT INTO `matching` VALUES (1,'1','2','Jane','Alexandria');
INSERT INTO `matching` VALUES (2,'1','3','Jane','Pumbba');
INSERT INTO `matching` VALUES (34,'337825819928904','1','simmon','Jane');
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
  `state` int(11) DEFAULT '0',
  `sender_name` varchar(255) DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=932 DEFAULT CHARSET=latin1;

#
# Dumping data for table notification
#

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (589,'1','2','acceptfriend',0,' ',0,'Jane');
INSERT INTO `notification` VALUES (590,'1','2','requestfriend',0,'photo2.jpg',0,'Jane');
INSERT INTO `notification` VALUES (591,'1','2','acceptfriend',0,'photo2.jpg',0,'Jane');
INSERT INTO `notification` VALUES (593,'1','2','comment',0,'photo4.jpg',0,'Jane');
INSERT INTO `notification` VALUES (596,'1','2','message',10,'aaaa',0,'Jane');
INSERT INTO `notification` VALUES (597,'1','2','like',10,'photo3.jpg',0,'Jane');
INSERT INTO `notification` VALUES (606,'1','2','newfeed',11,'photo2.jpg',0,'Jane');
INSERT INTO `notification` VALUES (972,'1','333379007041276','matchRequest',10,'',0,'Jane');
INSERT INTO `notification` VALUES (974,'337825819928904','333379007041276','newmatch',10,'',1,'simmon');
INSERT INTO `notification` VALUES (975,'1','333379007041276','newmatch',10,'',0,'Jane');
INSERT INTO `notification` VALUES (976,'1','333379007041276','requestfriend',10,'',0,'Jane');
INSERT INTO `notification` VALUES (977,'333379007041276','337825819928904','acceptfriend',0,'',0,'simmon');
INSERT INTO `notification` VALUES (978,'337825819928904','333379007041276','acceptfriend',0,'',1,'simmon');
INSERT INTO `notification` VALUES (979,'1','333379007041276','like',10,'photo4.jpg',0,'Jane');
INSERT INTO `notification` VALUES (980,'333379007041276','337825819928904','comment',10,'photo4.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (981,'333379007041276','337825819928904','newfeed',11,'b531cc999688c822a5ee0cbbafe47537.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (982,'337825819928904','333379007041276','like',10,'b531cc999688c822a5ee0cbbafe47537.jpg',1,'simmon');
INSERT INTO `notification` VALUES (983,'337825819928904','333379007041276','comment',10,'b531cc999688c822a5ee0cbbafe47537.jpg',1,'jimmy');
INSERT INTO `notification` VALUES (984,'337825819928904','333379007041276','comment',10,'b531cc999688c822a5ee0cbbafe47537.jpg',1,'simmon');
INSERT INTO `notification` VALUES (986,'337825819928904','333379007041276','newmatch',10,'',1,'simmon');
INSERT INTO `notification` VALUES (987,'333379007041276','337825819928904','newmatch',10,'',0,'jimmy');
INSERT INTO `notification` VALUES (988,'333379007041276','337825819928904','requestfriend',10,'',1,'jimmy');
INSERT INTO `notification` VALUES (989,'333379007041276','337825819928904','acceptfriend',0,'',0,'simmon');
INSERT INTO `notification` VALUES (990,'337825819928904','333379007041276','acceptfriend',0,'',1,'simmon');
INSERT INTO `notification` VALUES (992,'333379007041276','337825819928904','acceptfriend',0,'',0,'simmon');
INSERT INTO `notification` VALUES (993,'337825819928904','333379007041276','acceptfriend',0,'',1,'simmon');
INSERT INTO `notification` VALUES (994,'337825819928904','333379007041276','newmatch',10,'',1,'simmon');
INSERT INTO `notification` VALUES (995,'333379007041276','337825819928904','newmatch',10,'',0,'jimmy');
INSERT INTO `notification` VALUES (996,'333379007041276','337825819928904','requestfriend',10,'',1,'jimmy');
INSERT INTO `notification` VALUES (997,'333379007041276','337825819928904','like',10,'photo4.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (998,'333379007041276','337825819928904','comment',10,'photo4.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (999,'333379007041276','1','comment',10,'photo2.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (1000,'333379007041276','1','comment',10,'photo2.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (1001,'333379007041276','1','comment',10,'photo2.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (1002,'333379007041276','1','comment',10,'photo2.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (1003,'333379007041276','1','comment',10,'photo2.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (1004,'333379007041276','1','comment',10,'photo2.jpg',0,'jimmy');
INSERT INTO `notification` VALUES (1005,'333379007041276','23','matchRequest',10,'',0,'jimmy');
INSERT INTO `notification` VALUES (1006,'333379007041276','5','matchRequest',10,'',0,'jimmy');
INSERT INTO `notification` VALUES (1007,'333379007041276','5','matchRequest',10,'',0,'jimmy');
INSERT INTO `notification` VALUES (1008,'333379007041276','44','matchRequest',10,'',0,'jimmy');
INSERT INTO `notification` VALUES (1009,'333379007041276','337825819928904','comment',10,'photo4.jpg',0,'jkh sdf');
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
  `rate` float DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=latin1;

#
# Dumping data for table photo
#

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
INSERT INTO `photo` VALUES (41,'1','photo2.jpg',43,5,0,0,0,0,0,6,'jimmy&comment1&333379007041276^jimmy&comment2&333379007041276^jimmy&comment3&333379007041276^jimmy&comment4&333379007041276^jimmy&comment5&333379007041276^jimmy&comment6&333379007041276',0,'','this is my second comment','Jane',8.66667);
INSERT INTO `photo` VALUES (42,'1','photo3.jpg',34,6,0,0,0,0,0,0,'',0,'','aboutphoto','Jane',5.678);
INSERT INTO `photo` VALUES (43,'337825819928904','photo4.jpg',67,67,0,0,0,0,0,2,'jimmy&cccccc&333379007041276^jkh sdf&hell&333379007041276',1,'333379007041276&jimmy','In the pinic','simmon',1);
INSERT INTO `photo` VALUES (44,'23','photo5.jpg',0,0,0,0,0,0,0,0,'',0,'','this is my fifth comment','Moppy',0);
INSERT INTO `photo` VALUES (45,'23','photo6.jpg',0,0,0,0,0,0,0,0,'',0,'','this is my sixth comment','Moppy',0);
INSERT INTO `photo` VALUES (99,'3','photo6.jpg',14,27,0,0,0,1,0,0,'',0,' ',' dok','Pumbba',0.45);
INSERT INTO `photo` VALUES (100,'3','photo7.jpg',24,25,0,0,0,1,0,0,'',0,' ',' missing pot','Pumbba',1);
INSERT INTO `photo` VALUES (101,'2','photo8.jpg',35,23,0,0,0,0,0,0,'',0,' ','probable','Alexandria',1.45345);
INSERT INTO `photo` VALUES (102,'2','photo2.jpg',37,22,0,0,0,0,0,0,'',0,'','Hello','Alexandria',1.88889);
INSERT INTO `photo` VALUES (119,'333379007041276','face4.png',12,2,0,0,0,0,0,3,'Sin Mi Rae&This is your photo&337825819928904^Sin Mi Rae&Good&337825819928904^Sin Mi Rae&Ok&337825819928904',1,'337825819928904&Sin Mi Rae','Hello','jimmy',6);
INSERT INTO `photo` VALUES (120,'333379007041276','face3.png',23,3,0,0,0,0,0,1,'Sin Mi Rae&good&337825819928904',1,'337825819928904&Sin Mi Rae','Glance!','jimmy',7.0888);
INSERT INTO `photo` VALUES (121,'333379007041276','face2.png',56,8,0,0,0,0,0,0,'',1,'337825819928904&Sin Mi Rae','Glance!','jimmy',4.78678);
INSERT INTO `photo` VALUES (122,'333379007041276','face1.png',89,9,0,0,0,0,0,0,'',0,'','See Me!','jimmy',0.975676);
INSERT INTO `photo` VALUES (127,'44','photo3.jpg',83,12,0,0,0,0,0,0,'',0,'','hello','Baddy',6.91667);
INSERT INTO `photo` VALUES (148,'5','photo5.jpg',29,17,0,0,0,0,0,13,'YakYong Jong&aaaa&333379007041276^YakYong Jong&what&333379007041276^YakYong Jong&hello&333379007041276^YakYong Jong&Sorry&333379007041276^YakYong Jong&who&333379007041276^YakYong Jong&wheree&333379007041276^YakYong Jong&back&333379007041276^YakYong Jong&string&333379007041276^YakYong Jong&nine&333379007041276^YakYong Jong&ten&333379007041276^YakYong Jong&ten&333379007041276^YakYong Jong&12&333379007041276^YakYong Jong&I have simple problem&333379007041276',1,'333379007041276&YakYong Jong',' you a 333','Baddy',2);
INSERT INTO `photo` VALUES (150,'5','photo2.jpg',28,17,0,0,0,0,0,0,'',0,'',' you a 333','Baddy',2);
INSERT INTO `photo` VALUES (151,'333379007041276','cdfe450126df128def338b8ea9f661b8.jpg',12,5,0,0,0,0,0,0,'',0,'','Please write','jimmy',5.78678);
INSERT INTO `photo` VALUES (152,'333379007041276','0558996a33d5c77dcf29ea5ae1e122b9.jpg',34,8,0,0,0,0,0,0,'',0,'','','jimmy',7.88889);
INSERT INTO `photo` VALUES (153,'333379007041276','181650eaf16666e577ce0baa40a93752.jpg',89,56,0,0,0,0,0,0,'',0,'','','jimmy',4.11111);
INSERT INTO `photo` VALUES (154,'333379007041276','3834e5cbea18e3134f0d4e3309bd1256.jpg',3,1,0,0,0,0,0,0,'',0,'','','jimmy',3);
INSERT INTO `photo` VALUES (155,'333379007041276','12e7f9654518548f00bf15f4b3e7f439.jpg',24,12,0,0,0,0,0,0,'',0,'','','jimmy',2);
INSERT INTO `photo` VALUES (156,'333379007041276','b531cc999688c822a5ee0cbbafe47537.jpg',111,16,0,0,0,0,0,1,'simmon&good&337825819928904',1,'337825819928904&simmon','Please write aboo.','jimmy',8.65335);
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

#
# Dumping data for table users
#

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'1','Jane','aa','man',18,'10','photo1.jpg','aemail',0,0);
INSERT INTO `users` VALUES (2,'2','Alexandria','bb','man',19,'5','photo2.jpg','aemail',1,1);
INSERT INTO `users` VALUES (6,'23','Moppy',' ',' ',19,' ','photo3.jpg','aemail',0,0);
INSERT INTO `users` VALUES (9,'3','Pumbba','dd','man',52,'0','photo6.jpg','fnbvgh',0,0);
INSERT INTO `users` VALUES (10,'5','Baddy',' ','male',23,'0','photo5.jpg','gmail.com',0,0);
INSERT INTO `users` VALUES (11,'44','Baddy',' ','male',23,'0','photo7.jpg','outlook.com',0,0);
INSERT INTO `users` VALUES (12,'333379007041276','YakYong Jong',' ','male',18,'0','photo7.jpg','jyyblue1987@outlook.com',0,0);
INSERT INTO `users` VALUES (13,'337825819928904','simmon',' ','female',18,'0','photo5.jpg','future.syg414@yahoo.com',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
