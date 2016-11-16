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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

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
INSERT INTO `friend` VALUES (13,'2','333379007041276');
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
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=latin1;

#
# Dumping data for table notification
#

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'a','g','matchRequest',0,NULL);
INSERT INTO `notification` VALUES (13,'1','2','newfeed',11,'312613898fcf7cc9bda2bb47988b9a41.jpg');
INSERT INTO `notification` VALUES (17,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (18,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (19,'3','1','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (20,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (21,'333379007041276','2','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (227,'333379007041276','3','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (228,'333379007041276','2','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (231,'333379007041276','2','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (233,'333379007041276','2','acceptfriend',0,NULL);
INSERT INTO `notification` VALUES (234,'333379007041276','23','requestfriend',10,NULL);
INSERT INTO `notification` VALUES (235,'333379007041276','44','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (236,'333379007041276','44','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (237,'333379007041276','44','matchRequest',10,NULL);
INSERT INTO `notification` VALUES (238,'333379007041276','1','newfeed',11,'photo7.jpg');
INSERT INTO `notification` VALUES (239,'333379007041276','2','newfeed',0,'photo7.jpg');
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
  `rate` int(11) DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=latin1;

#
# Dumping data for table photo
#

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
INSERT INTO `photo` VALUES (37,'1','face1.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (38,'1','face2.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (39,'1','face3.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (40,'1','photo1.jpg',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (41,'1','photo2.jpg',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (42,'1','photo3.jpg',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (43,'1','photo4.jpg',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (44,'1','photo5.jpg',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (45,'1','photo6.jpg',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (46,'1','face1.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (47,'1','face1.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (48,'1','face1.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (49,'1','face1.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (50,'1','face1.png',0,0,0,0,0,0,0,0,'0',0,'0','0','Pyon',0);
INSERT INTO `photo` VALUES (51,'333379007041276','photo8.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"',0);
INSERT INTO `photo` VALUES (52,'333379007041276','photo7.jpg',0,5,0,0,0,0,0,0,'',0,'','Please write about your photo.','\"\"',0);
INSERT INTO `photo` VALUES (53,'333379007041276','photo4.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"',0);
INSERT INTO `photo` VALUES (54,'333379007041276','photo3.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"',0);
INSERT INTO `photo` VALUES (55,'333379007041276','photo2.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"',0);
INSERT INTO `photo` VALUES (56,'333379007041276','photo1.jpg',0,5,0,0,0,0,0,0,'',0,'','','\"\"',0);
INSERT INTO `photo` VALUES (64,'333379007041276','928d7643ad91172561301d90a4099726.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (65,'333379007041276','824ca7ff46d021cb760cee2b3d258660.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (66,'333379007041276','d8def41050ed6f31e4d63dcf452bde4b.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (67,'333379007041276','1ae71dbca174fd7328835054125b7c58.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (68,'333379007041276','4d6dbc85d0e2c0d2b02319abf881be17.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (69,'333379007041276','912c574f4c38d34da2dedd0e7630fb57.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (70,'333379007041276','e556deb378315a9e554c967b73e189a1.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (71,'333379007041276','c62379fae093d8b93828c42d525b14d6.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (72,'333379007041276','2d4ffbb32e0649aaa451cbeb777fee40.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (73,'333379007041276','3635d3cbbd163590c57b6df8e52962aa.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (74,'333379007041276','f492dc4d71939f29aaa65d0d1403d73f.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (75,'333379007041276','db061b5e9044e925784130f642e4ba77.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (76,'333379007041276','bc06e7201c1db3607fa3cdb37d639cb1.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (77,'333379007041276','09463ddea3890ddacfe5f1df433a45b1.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (78,'333379007041276','eb69bca10c827ebe5d6b5a2503f73dcb.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (79,'333379007041276','2a12e6b74a569622ee7227728cf27524.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (80,'333379007041276','3e9abce8c1d68160cd0c83cf02f142dd.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (81,'333379007041276','9a69a4d09d35b2ad75b4103745380ef1.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (82,'333379007041276','8a38046433ce2a2050057f2c1176d71b.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (83,'333379007041276','5ddaef52b3a379b3e26bf1e36e66d734.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (84,'333379007041276','f29158d25fad5385a7b5c40c9f6041e2.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (85,'333379007041276','f95f544f99a1f912b07abb203bc84177.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (86,'333379007041276','45c17544527d62d9280d0e3ce1b3057e.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (87,'333379007041276','ac1f0669a293b9f06a457a9445ea4ef1.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (88,'333379007041276','f84921a79ae896a7305381fba6ce62d5.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (89,'333379007041276','c289224683f9050ffb704c9ddbfa76e0.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (90,'333379007041276','1245c603478c975fffaacfb1c9b56285.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
INSERT INTO `photo` VALUES (91,'333379007041276','e9d4b259eba4820f1779e1ffb95a766f.jpg',0,0,0,0,0,0,0,0,'',0,'','','',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

#
# Dumping data for table users
#

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'1','a','aa','man',18,'10','pp','aemail',0,0);
INSERT INTO `users` VALUES (2,'2','b','bb','man',19,'5','bpp','aemail',1,1);
INSERT INTO `users` VALUES (6,'23','f',NULL,NULL,19,NULL,NULL,'aemail',0,0);
INSERT INTO `users` VALUES (7,'333379007041276','YakYong Jong',NULL,'male',18,'0',NULL,'jyyblue1987@outlook.com',0,0);
INSERT INTO `users` VALUES (8,'44',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `users` VALUES (9,'3','d','dd','man',52,'0','000','fnbvgh',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
