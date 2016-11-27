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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

#
# Dumping data for table block
#

LOCK TABLES `block` WRITE;
/*!40000 ALTER TABLE `block` DISABLE KEYS */;
INSERT INTO `block` VALUES (2,'337825819928904','2','Inappropriate Messages');
INSERT INTO `block` VALUES (3,'333379007041276','23','Block without reporting');
INSERT INTO `block` VALUES (4,'333379007041276','1','Block without reporting');
INSERT INTO `block` VALUES (5,'337825819928904','1','Inappropriate Messages');
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
INSERT INTO `chat` VALUES (5,'333379007041276','337825819928904','HELLO',0);
INSERT INTO `chat` VALUES (6,'337825819928904','333379007041276','hi',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

#
# Dumping data for table friend
#

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
INSERT INTO `friend` VALUES (6,'1','3','Jane','Pumbba');
INSERT INTO `friend` VALUES (7,'3','1','Pumbba','Jane');
INSERT INTO `friend` VALUES (26,'1','333379007041276','Jane','yyj');
INSERT INTO `friend` VALUES (27,'337825819928904','333379007041276','Sin Mi Rae','YakYong Jong');
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
INSERT INTO `matching` VALUES (27,'333379007041276','23','yyj','Moppy');
INSERT INTO `matching` VALUES (32,'337825819928904','2','Sinnon','Alexandria');
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
) ENGINE=InnoDB AUTO_INCREMENT=894 DEFAULT CHARSET=latin1;

#
# Dumping data for table notification
#

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'a','g','matchRequest',0,' ',0,'');
INSERT INTO `notification` VALUES (231,'333379007041276','2','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (233,'2','11','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (589,'333379007041276','3','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (590,'1','11','requestfriend',0,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (591,'1','11','acceptfriend',0,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (593,'1','11','comment',0,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (594,'2','11','matchRequest',0,'photo7.jpg',0,'');
INSERT INTO `notification` VALUES (596,'1','11','message',10,'aaaa',0,'');
INSERT INTO `notification` VALUES (597,'1','11','like',10,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (606,'1','333379007041276','newfeed',11,'photo2.jpg',1,'');
INSERT INTO `notification` VALUES (661,'2','111111111111111','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (662,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (663,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (665,'333379007041276','3','comment',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (698,'5','111111111111111','requestfriend',0,'photo2.jpg',1,'');
INSERT INTO `notification` VALUES (699,'1','111111111111111','acceptfriend',0,'photo2.jpg',1,'');
INSERT INTO `notification` VALUES (700,'1','333379007041276','newmatch',0,'photo8.jpg',1,'');
INSERT INTO `notification` VALUES (701,'1','111111111111111','comment',0,'photo4.jpg',1,'');
INSERT INTO `notification` VALUES (704,'1','111111111111111','message',10,'aaaa',1,'');
INSERT INTO `notification` VALUES (705,'1','111111111111111','like',10,'photo3.jpg',1,'');
INSERT INTO `notification` VALUES (706,'1','333379007041276','newfeed',11,'photo2.jpg',1,'');
INSERT INTO `notification` VALUES (707,'1','333379007041276','newfeed',0,'photo2.jpg',1,'');
INSERT INTO `notification` VALUES (708,'1','333379007041276','newfeed',0,'photo2.jpg',1,'');
INSERT INTO `notification` VALUES (709,'1','333379007041276','newfeed',0,'photo3.jpg',1,'');
INSERT INTO `notification` VALUES (714,'333379007041276','23','requestfriend',10,' ',0,'');
INSERT INTO `notification` VALUES (716,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (718,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (720,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (722,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (724,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (726,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (728,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (730,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (732,'5','11','requestfriend',0,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (737,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (738,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (739,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (740,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (741,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (742,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (743,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (744,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (746,'5','111111111111111','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (747,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (748,'333379007041276','5','requestfriend',10,' ',0,'');
INSERT INTO `notification` VALUES (749,'5','111111111111111','acceptfriend',0,'  ',0,'');
INSERT INTO `notification` VALUES (750,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (751,'333379007041276','1','requestfriend',10,' ',0,'');
INSERT INTO `notification` VALUES (752,'333379007041276','5','requestfriend',10,' ',0,'');
INSERT INTO `notification` VALUES (753,'5','11111111111111','acceptfriend',0,' ',1,'');
INSERT INTO `notification` VALUES (754,'333379007041276','5','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (755,'333379007041276','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (756,'333379007041276','111111111111111','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (757,'111111111111111','111111111111111','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (758,'333379007041276','111111111111111','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (759,'111111111111111','111111111111111','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (760,'333379007041276','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (761,'333379007041276','111111111111111','acceptfriend',0,' ',0,'');
INSERT INTO `notification` VALUES (762,'111111111111111','111111111111111','acceptfriend',0,'  ',0,'');
INSERT INTO `notification` VALUES (763,'111111111111111','111111111111111','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (764,'333379007041276','44','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (765,'333379007041276','5','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (766,'333379007041276','5','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (767,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (768,'','111111111111111','friendadd',10,'',0,'');
INSERT INTO `notification` VALUES (769,'1','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (770,'1','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (771,'1','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (772,'1','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (773,'','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (774,'','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (775,'','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (776,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (777,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (778,'333379007041276','5','like',10,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (779,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (780,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (781,'333379007041276','5','like',10,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (782,'333379007041276','5','like',10,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (783,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (784,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (785,'333379007041276','5','like',10,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (786,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (787,'333379007041276','5','like',10,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (788,'333379007041276','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (789,'333379007041276','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (790,'333379007041276','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (791,'333379007041276','111111111111111','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (793,'333379007041276','23','requestfriend',10,' ',0,'');
INSERT INTO `notification` VALUES (794,'333379007041276','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (795,'333379007041276','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (796,'333379007041276','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (797,'333379007041276','5','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (798,'333379007041276','44','friendadd',10,' ',0,'');
INSERT INTO `notification` VALUES (799,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (800,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (801,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (802,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (803,'333379007041276','337825819928904','friendadd',10,' ',1,'');
INSERT INTO `notification` VALUES (804,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (805,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (806,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (807,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (808,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (809,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (810,'333379007041276','2','matchRequest',10,'  ',0,'');
INSERT INTO `notification` VALUES (811,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (812,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (813,'333379007041276','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (814,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (815,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (816,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (817,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (818,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (819,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (820,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (821,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (822,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (823,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (824,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (825,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (826,'333379007041276','3','comment',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (827,'333379007041276','3','comment',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (828,'333379007041276','3','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (829,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (830,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (831,'333379007041276','1','comment',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (832,'333379007041276','1','comment',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (833,'333379007041276','1','like',10,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (834,'333379007041276','1','like',10,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (835,'333379007041276','1','like',10,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (836,'333379007041276','1','like',10,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (837,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (838,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (839,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (840,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (841,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (842,'333379007041276','1','like',10,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (843,'333379007041276','5','like',10,'photo2.jpg',0,'');
INSERT INTO `notification` VALUES (844,'333379007041276','1','like',10,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (845,'333379007041276','5','like',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (846,'333379007041276','5','comment',10,'photo5.jpg',0,'');
INSERT INTO `notification` VALUES (847,'337825819928904','2','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (849,'333379007041276','44','matchRequest',10,' ',0,'');
INSERT INTO `notification` VALUES (850,'333379007041276','337825819928904','acceptfriend',0,' ',1,'');
INSERT INTO `notification` VALUES (851,'337825819928904','333379007041276','acceptfriend',0,' ',1,'');
INSERT INTO `notification` VALUES (852,'337825819928904','333379007041276','like',10,'photo7.jpg',1,'');
INSERT INTO `notification` VALUES (853,'337825819928904','333379007041276','comment',10,'photo7.jpg',1,'');
INSERT INTO `notification` VALUES (854,'337825819928904','333379007041276','comment',10,'photo7.jpg',1,'');
INSERT INTO `notification` VALUES (856,'333379007041276','3','newfeed',0,'photo7.jpg',0,'');
INSERT INTO `notification` VALUES (857,'333379007041276','5','newfeed',0,'photo7.jpg',0,'');
INSERT INTO `notification` VALUES (859,'333379007041276','3','newfeed',0,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (860,'333379007041276','5','newfeed',0,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (862,'333379007041276','3','newfeed',0,'photo7.jpg',0,'');
INSERT INTO `notification` VALUES (863,'333379007041276','5','newfeed',0,'photo7.jpg',0,'');
INSERT INTO `notification` VALUES (865,'333379007041276','3','newfeed',0,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (866,'333379007041276','5','newfeed',0,'photo4.jpg',0,'');
INSERT INTO `notification` VALUES (868,'333379007041276','3','newfeed',0,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (869,'333379007041276','5','newfeed',0,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (871,'333379007041276','3','newfeed',0,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (872,'333379007041276','5','newfeed',0,'photo3.jpg',0,'');
INSERT INTO `notification` VALUES (874,'333379007041276','3','newfeed',0,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (875,'333379007041276','5','newfeed',0,'photo1.jpg',0,'');
INSERT INTO `notification` VALUES (877,'333379007041276','3','newfeed',0,'face2.png',0,'');
INSERT INTO `notification` VALUES (878,'333379007041276','337825819928904','newfeed',0,'face2.png',1,'');
INSERT INTO `notification` VALUES (879,'333379007041276','23','friendadd',10,NULL,0,'');
INSERT INTO `notification` VALUES (880,'333379007041276','337825819928904','like',10,'photo4.jpg',1,'');
INSERT INTO `notification` VALUES (881,'333379007041276','337825819928904','comment',10,'photo4.jpg',1,'');
INSERT INTO `notification` VALUES (882,'333379007041276','3','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (883,'333379007041276','5','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (884,'333379007041276','2','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (885,'333379007041276','2','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (886,'333379007041276','3','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (887,'333379007041276','2','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (888,'333379007041276','44','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (889,'333379007041276','2','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (890,'333379007041276','5','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (891,'333379007041276','3','matchRequest',10,NULL,0,'');
INSERT INTO `notification` VALUES (892,'333379007041276','23','requestfriend',10,NULL,0,'');
INSERT INTO `notification` VALUES (893,'333379007041276','1','requestfriend',10,NULL,0,'');
INSERT INTO `notification` VALUES (894,'337825819928904','333379007041276','like',10,'photo1.jpg',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (895,'337825819928904','333379007041276','like',10,'face2.png',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (896,'337825819928904','333379007041276','like',10,'photo4.jpg',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (897,'337825819928904','333379007041276','like',10,'photo3.jpg',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (898,'337825819928904','333379007041276','like',10,'photo2.jpg',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (899,'337825819928904','333379007041276','like',10,'photo8.jpg',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (900,'333379007041276','1','like',10,'photo2.jpg',0,'YakYong Jong');
INSERT INTO `notification` VALUES (901,'333379007041276','1','like',10,'photo3.jpg',0,'YakYong Jong');
INSERT INTO `notification` VALUES (902,'undefined','1','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (903,'1','undefined','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (904,'undefined','1','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (905,'1','undefined','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (906,'undefined','1','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (907,'1','undefined','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (908,'333379007041276','1','newmatch',10,'',0,'Yan');
INSERT INTO `notification` VALUES (909,'1','333379007041276','newmatch',10,'',1,'Yan');
INSERT INTO `notification` VALUES (910,'1','333379007041276','acceptfriend',0,'',0,'pak');
INSERT INTO `notification` VALUES (911,'333379007041276','1','acceptfriend',0,'',0,'pak');
INSERT INTO `notification` VALUES (912,'333379007041276','337825819928904','newfeed',0,'cdfe450126df128def338b8ea9f661b8.jpg',1,'YakYong Jong');
INSERT INTO `notification` VALUES (913,'337825819928904','1','requestfriend',10,'',0,'Sin Mi Rae');
INSERT INTO `notification` VALUES (915,'337825819928904','333379007041276','friendadd',10,'',1,'Sin Mi Rae');
INSERT INTO `notification` VALUES (916,'337825819928904','1','requestfriend',10,'',0,'Sin Mi Rae');
INSERT INTO `notification` VALUES (917,'337825819928904','2','requestfriend',10,'',0,'Sin Mi Rae');
INSERT INTO `notification` VALUES (918,'333379007041276','23','requestfriend',10,'',0,'YakYong Jong');
INSERT INTO `notification` VALUES (919,'337825819928904','333379007041276','acceptfriend',0,'',0,'YakYong Jong');
INSERT INTO `notification` VALUES (920,'333379007041276','337825819928904','acceptfriend',0,'',0,'YakYong Jong');
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
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=latin1;

#
# Dumping data for table photo
#

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
INSERT INTO `photo` VALUES (41,'1','photo2.jpg',0,0,0,0,0,0,0,3,'YakYong Jong&who are you&333379007041276^YakYong Jong&very good.&333379007041276^YakYong Jong& another things.&333379007041276',1,'','this is my second comment','Alexsandria',0);
INSERT INTO `photo` VALUES (42,'1','photo3.jpg',0,0,0,0,0,0,0,0,'',1,'','aboutphoto','Alexsandria',0);
INSERT INTO `photo` VALUES (43,'337825819928904','photo4.jpg',0,0,0,0,0,0,0,1,'YakYong Jong&fgtrffhhjggghj&333379007041276',1,'333379007041276&YakYong Jong','this is my fourth comment','Alexsandria',0);
INSERT INTO `photo` VALUES (44,'23','photo5.jpg',0,0,0,0,0,0,0,0,'',0,'','this is my fifth comment','Alexsandria',0);
INSERT INTO `photo` VALUES (45,'23','photo6.jpg',0,0,0,0,0,0,0,0,'',0,'','this is my sixth comment','Alexsandria',0);
INSERT INTO `photo` VALUES (99,'3','photo6.jpg',14,27,0,0,0,1,0,0,'',0,' ',' dok','Jane',0);
INSERT INTO `photo` VALUES (100,'3','photo7.jpg',24,25,0,0,0,1,0,0,'',0,' ',' missing pot','Jane',0);
INSERT INTO `photo` VALUES (101,'2','photo8.jpg',35,23,0,0,0,0,0,0,'',0,' ','probable','Jane',0);
INSERT INTO `photo` VALUES (102,'2','photo2.jpg',37,22,0,0,0,0,0,0,'',0,'','Hello','Jane',0);
INSERT INTO `photo` VALUES (119,'333379007041276','face4.png',0,0,0,0,0,0,0,0,'',0,'','Nice!','YakYong Jong',0);
INSERT INTO `photo` VALUES (120,'333379007041276','face3.png',0,0,0,0,0,0,0,0,'',0,'','Glance!','YakYong Jong',0);
INSERT INTO `photo` VALUES (121,'333379007041276','face2.png',0,0,0,0,0,0,0,0,'',1,'337825819928904&Sin Mi Rae','Glance!','YakYong Jong',0);
INSERT INTO `photo` VALUES (122,'333379007041276','face1.png',0,0,0,0,0,0,0,0,'',0,'','See Me!','YakYong Jong',0);
INSERT INTO `photo` VALUES (127,'44','photo3.jpg',73,11,0,0,0,0,0,0,'',0,'','hello','jim',7);
INSERT INTO `photo` VALUES (128,'44','photo4.jpg',73,11,0,0,0,0,0,0,'',0,'','My Photo!','jim',7);
INSERT INTO `photo` VALUES (148,'5','photo5.jpg',19,16,0,0,0,0,0,13,'YakYong Jong&aaaa&333379007041276^YakYong Jong&what&333379007041276^YakYong Jong&hello&333379007041276^YakYong Jong&Sorry&333379007041276^YakYong Jong&who&333379007041276^YakYong Jong&wheree&333379007041276^YakYong Jong&back&333379007041276^YakYong Jong&string&333379007041276^YakYong Jong&nine&333379007041276^YakYong Jong&ten&333379007041276^YakYong Jong&ten&333379007041276^YakYong Jong&12&333379007041276^YakYong Jong&I have simple problem&333379007041276',1,'333379007041276&YakYong Jong',' you a 333','Baddy',0);
INSERT INTO `photo` VALUES (150,'5','photo2.jpg',19,16,0,0,0,0,0,0,'',0,'',' you a 333','Baddy',0);
INSERT INTO `photo` VALUES (151,'333379007041276','cdfe450126df128def338b8ea9f661b8.jpg',0,0,0,0,0,0,0,0,'',0,'','Please write','YakYong Jong',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

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
INSERT INTO `users` VALUES (13,'337825819928904','Sin Mi Rae',' ','female',18,'0','photo5.jpg','future.syg414@yahoo.com',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
