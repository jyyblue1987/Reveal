/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 10.1.16-MariaDB : Database - reveal
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`reveal` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `reveal`;

/*Table structure for table `block` */

DROP TABLE IF EXISTS `block`;

CREATE TABLE `block` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(255) NOT NULL,
  `blockfacebookid` varchar(255) NOT NULL,
  `blocksort` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `block` */

/*Table structure for table `chat` */

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `receiver` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `time` int(4) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `chat` */

/*Table structure for table `friend` */

DROP TABLE IF EXISTS `friend`;

CREATE TABLE `friend` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid1` varchar(255) NOT NULL,
  `facebookid2` varchar(255) NOT NULL,
  `name1` varchar(255) NOT NULL,
  `name2` varchar(255) NOT NULL,
  `sendfeed1` varchar(23) NOT NULL DEFAULT 'no',
  `sendfeed2` varchar(23) NOT NULL DEFAULT 'no',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `friend` */

/*Table structure for table `matching` */

DROP TABLE IF EXISTS `matching`;

CREATE TABLE `matching` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid1` varchar(255) NOT NULL,
  `facebookid2` varchar(255) NOT NULL,
  `name1` varchar(255) NOT NULL,
  `name2` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `matching` */

/*Table structure for table `notification` */

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(255) NOT NULL,
  `destination` varchar(255) NOT NULL,
  `notekind` varchar(255) NOT NULL,
  `sendtime` varchar(255) NOT NULL,
  `feedval` text NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0',
  `sender_name` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `notification` */

/*Table structure for table `photo` */

DROP TABLE IF EXISTS `photo`;

CREATE TABLE `photo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid` varchar(255) NOT NULL,
  `photopath` varchar(255) NOT NULL,
  `ratesum` int(11) NOT NULL DEFAULT '0',
  `ratenumber` int(11) NOT NULL DEFAULT '0',
  `reportgp` int(11) NOT NULL DEFAULT '0',
  `reportom` int(11) NOT NULL DEFAULT '0',
  `reportnap` int(11) NOT NULL DEFAULT '0',
  `reportwg` int(11) NOT NULL DEFAULT '0',
  `reportfls` int(11) NOT NULL DEFAULT '0',
  `commentnum` int(11) NOT NULL DEFAULT '0',
  `commentcon` text NOT NULL,
  `likenum` int(11) NOT NULL DEFAULT '0',
  `likefacebookid` text NOT NULL COMMENT 'the users that like this photo. seperate with "^".',
  `mycomment` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `rate` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;

/*Data for the table `photo` */

/*Table structure for table `rate` */

DROP TABLE IF EXISTS `rate`;

CREATE TABLE `rate` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sender` varchar(255) NOT NULL COMMENT 'rated man facebookid',
  `receiver` varchar(255) NOT NULL COMMENT 'photo owner facebookid',
  `send_name` varchar(255) NOT NULL COMMENT 'rated man name',
  `receiver_name` varchar(255) NOT NULL COMMENT 'photo owner name',
  `kind` varchar(20) NOT NULL COMMENT 'rate kind',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT 'teh state of rate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `rate` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `iid` int(11) NOT NULL AUTO_INCREMENT,
  `facebookid` varchar(255) NOT NULL,
  `profilephoto` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `age` int(11) NOT NULL DEFAULT '0',
  `locationx` double NOT NULL DEFAULT '0',
  `locationy` double NOT NULL DEFAULT '0',
  `showme` varchar(255) NOT NULL DEFAULT 'both',
  `showmatch` varchar(255) NOT NULL DEFAULT 'yes',
  `maxage` int(11) NOT NULL DEFAULT '100',
  `minage` int(11) NOT NULL DEFAULT '18',
  `distance` int(11) NOT NULL DEFAULT '20',
  `maxrate` int(11) NOT NULL DEFAULT '10',
  `minrate` int(11) NOT NULL DEFAULT '0',
  `not_match` varchar(20) NOT NULL DEFAULT 'yes',
  `not_message` varchar(20) NOT NULL DEFAULT 'yes',
  `not_friend` varchar(20) NOT NULL DEFAULT 'yes',
  `not_comment` varchar(20) NOT NULL DEFAULT 'yes',
  `not_like` varchar(20) NOT NULL DEFAULT 'yes',
  `showfullname` varchar(255) NOT NULL DEFAULT 'yes' COMMENT 'yes: full name, no: first name',
  `searchbyname` varchar(255) NOT NULL DEFAULT 'yes' COMMENT 'yes: search by name, no: refuse searching by name.',
  `autoaccept` varchar(20) NOT NULL DEFAULT 'yes',
  `totalrate` double NOT NULL DEFAULT '0',
  `lastname` varchar(255) NOT NULL,
  `suberb` varchar(255) NOT NULL,
  PRIMARY KEY (`iid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `users` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
