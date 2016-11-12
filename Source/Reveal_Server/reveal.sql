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
  `sender` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `message` text,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table chat
#

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
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
INSERT INTO `friend` VALUES (1,'3','4');
INSERT INTO `friend` VALUES (2,'2','5');
INSERT INTO `friend` VALUES (3,'2','6');
INSERT INTO `friend` VALUES (4,'1','2');
INSERT INTO `friend` VALUES (5,'5','1');
INSERT INTO `friend` VALUES (6,'1','3');
INSERT INTO `friend` VALUES (7,'3','1');
INSERT INTO `friend` VALUES (8,'333379007041276','1');
INSERT INTO `friend` VALUES (10,'44','333379007041276');
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

#
# Dumping data for table matching
#

LOCK TABLES `matching` WRITE;
/*!40000 ALTER TABLE `matching` DISABLE KEYS */;
INSERT INTO `matching` VALUES (1,'1','2');
INSERT INTO `matching` VALUES (2,'1','3');
INSERT INTO `matching` VALUES (3,'2','3');
INSERT INTO `matching` VALUES (4,'4','1');
INSERT INTO `matching` VALUES (5,'4','1');
INSERT INTO `matching` VALUES (6,'4','1');
INSERT INTO `matching` VALUES (7,'4','1');
INSERT INTO `matching` VALUES (8,'4','1');
INSERT INTO `matching` VALUES (9,'4','1');
INSERT INTO `matching` VALUES (10,'4','1');
INSERT INTO `matching` VALUES (11,'4','1');
INSERT INTO `matching` VALUES (12,'4','1');
INSERT INTO `matching` VALUES (13,'4','1');
INSERT INTO `matching` VALUES (14,'4','1');
INSERT INTO `matching` VALUES (15,'4','1');
INSERT INTO `matching` VALUES (16,'4','1');
INSERT INTO `matching` VALUES (17,'2','333379007041276');
INSERT INTO `matching` VALUES (18,'333379007041276','3');
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
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;

#
# Dumping data for table notification
#

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'a','g','matchRequest',0,NULL);
INSERT INTO `notification` VALUES (7,'1','2','newfeed',11,'pp1');
INSERT INTO `notification` VALUES (8,'1','2','newfeed',11,'pp1');
INSERT INTO `notification` VALUES (9,'1','2','newfeed',11,'pp1');
INSERT INTO `notification` VALUES (10,'1','2','newfeed',11,'pp1');
INSERT INTO `notification` VALUES (11,'1','2','newfeed',11,'pp1');
INSERT INTO `notification` VALUES (13,'1','2','newfeed',11,'pp1');
INSERT INTO `notification` VALUES (15,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (16,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (17,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (18,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (19,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (20,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (21,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (24,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (25,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (26,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (27,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (28,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (29,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (30,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (31,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (32,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (33,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (34,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (35,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (36,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (37,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (38,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (39,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (40,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (41,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (42,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (43,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (44,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (45,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (46,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (47,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (48,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (49,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (50,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (51,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (52,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (53,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (54,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (55,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (56,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (57,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (58,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (59,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (60,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (61,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (62,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (63,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (64,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (65,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (66,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (67,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (68,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (69,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (70,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (71,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (72,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (73,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (74,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (75,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (76,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (77,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (78,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (79,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (80,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (81,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (82,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (83,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (84,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (85,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (86,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (93,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (95,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (98,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (99,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (103,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (113,'333379007041276','333379007041276','matchRequest',10,NULL);
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
  `ratesum` int(11) DEFAULT NULL,
  `ratenumber` int(11) DEFAULT NULL,
  `reportgp` int(11) DEFAULT NULL,
  `reportom` int(11) DEFAULT NULL,
  `reportnap` int(11) DEFAULT NULL,
  `reportwg` int(11) DEFAULT NULL,
  `reportfls` int(11) DEFAULT NULL,
  `commentnum` int(11) DEFAULT NULL,
  `commentcon` text,
  `likenum` int(11) DEFAULT NULL,
  `likefacebookid` text,
  `mycomment` text,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

#
# Dumping data for table photo
#

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
INSERT INTO `photo` VALUES (3,'2','338a9416367237a9c0568778d83e1831.jpg',35,15,0,0,0,0,0,2,'www&8^ddd&3',NULL,NULL,NULL,'a');
INSERT INTO `photo` VALUES (5,'1','312613898fcf7cc9bda2bb47988b9a41.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (6,'1','42a9824b9e31999c385479d6a65d985c.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (7,'1','312613898fcf7cc9bda2bb47988b9a41.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (8,'1','42a9824b9e31999c385479d6a65d985c.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (9,'44','312613898fcf7cc9bda2bb47988b9a41.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (10,'44','42a9824b9e31999c385479d6a65d985c.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (11,'44','312613898fcf7cc9bda2bb47988b9a41.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (12,'44','42a9824b9e31999c385479d6a65d985c.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (13,'44','312613898fcf7cc9bda2bb47988b9a41.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (14,'44','42a9824b9e31999c385479d6a65d985c.jpg',152,19,1,0,0,0,0,7,'ttt&1^ass&2^fhhk&1^hjjg&1^ggg&1^tying&1^Tgj.l.uvih&1',7,'4^4^333379007041276^333379007041276^333379007041276^333379007041276^333379007041276','this is great','a');
INSERT INTO `photo` VALUES (15,'333379007041276','0d268937709f3e6e2b79066313e72b6c.jpg',0,9,0,0,0,1,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (16,'333379007041276','312613898fcf7cc9bda2bb47988b9a41.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (17,'333379007041276','42a9824b9e31999c385479d6a65d985c.jpg',7,2,0,0,0,0,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (18,'333379007041276','312613898fcf7cc9bda2bb47988b9a41.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (19,'333379007041276','338a9416367237a9c0568778d83e1831.jpg',0,1,0,0,0,0,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (20,'333379007041276','0835c12d5d134e73930ebe0e80244a21.jpg',0,1,0,0,0,0,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (21,'333379007041276','312613898fcf7cc9bda2bb47988b9a41.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"');
INSERT INTO `photo` VALUES (22,'2','0d268937709f3e6e2b79066313e72b6c.jpg',146,54,1,0,2,0,0,2,'asd&1^ddd&3',NULL,NULL,NULL,'a');
INSERT INTO `photo` VALUES (23,'2','42a9824b9e31999c385479d6a65d985c.jpg',146,55,1,0,2,0,0,2,'asd&1^ddd&3',NULL,NULL,NULL,'a');
INSERT INTO `photo` VALUES (24,'3','338a9416367237a9c0568778d83e1831.jpg',35,14,1,0,2,0,0,2,'asd&1^ddd&3',NULL,NULL,NULL,'a');
INSERT INTO `photo` VALUES (25,'3','0835c12d5d134e73930ebe0e80244a21.jpg',146,54,1,0,2,0,0,2,'asd&1^ddd&3',NULL,NULL,NULL,'a');
INSERT INTO `photo` VALUES (26,'3','312613898fcf7cc9bda2bb47988b9a41.jpg',146,54,1,0,2,0,0,2,'asd&1^ddd&3',NULL,NULL,NULL,'a');
INSERT INTO `photo` VALUES (27,'333379007041276','0f3e3b17457e57b4fd5bf25479d020df.jpg',6,3,0,0,0,0,0,0,'',0,'','','');
INSERT INTO `photo` VALUES (28,'333379007041276','686d2cb43a9e2e3f5f6f999efa57737f.jpg',0,0,0,0,0,0,0,0,'',0,'','','');
INSERT INTO `photo` VALUES (29,'333379007041276','360ab50ff6218fc82de5704145df36c7.jpg',0,0,0,0,0,0,0,0,'',0,'','','');
INSERT INTO `photo` VALUES (30,'333379007041276','20da79a5bb8be3252d09a297e2fe2b5a.jpg',0,1,0,0,0,0,0,0,'',0,'','','');
INSERT INTO `photo` VALUES (31,'333379007041276','59e49b8d5c62a12b13a0484cbb9eff4c.jpg',0,0,0,0,0,0,0,0,'',0,'','','');
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
  `profilephoto` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `locationx` float DEFAULT NULL,
  `locationy` float DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

#
# Dumping data for table users
#

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'1','a','aa','man',18,'10','pp','aemail',0,0);
INSERT INTO `users` VALUES (2,'2','b','bb','man',19,'5','bpp','aemail',1,1);
INSERT INTO `users` VALUES (6,'23','f',NULL,NULL,19,NULL,NULL,'aemail',NULL,NULL);
INSERT INTO `users` VALUES (7,'333379007041276','YakYong Jong',NULL,'male',10,'0',NULL,'jyyblue1987@outlook.com',0,0);
INSERT INTO `users` VALUES (8,'44',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
