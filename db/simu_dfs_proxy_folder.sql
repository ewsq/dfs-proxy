-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: simu_dfs_proxy
-- ------------------------------------------------------
-- Server version	5.7.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `path` varchar(255) NOT NULL DEFAULT '' COMMENT '路径: a/b 或者 null(根目录)',
  `parent_id` int(11) unsigned NOT NULL DEFAULT '0',
  `bucket_id` int(11) unsigned NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
INSERT INTO `folder` VALUES (1,'a','',0,1,'2018-02-11 12:59:44','2018-02-11 12:59:44'),(2,'b','a',1,1,'2018-02-11 13:00:25','2018-02-11 13:00:25'),(3,'c','a/b',2,1,'2018-02-11 13:01:35','2018-02-11 13:01:35'),(4,'affair','',0,2,'2018-02-12 05:07:51','2018-02-12 05:07:51'),(5,'110904','affair',4,2,'2018-02-12 05:07:51','2018-02-12 05:07:51'),(6,'files','affair/110904',5,2,'2018-02-12 05:07:51','2018-02-12 05:07:51'),(7,'WogFp5FDC','affair/110904/files',6,2,'2018-02-12 05:07:51','2018-02-12 05:07:51'),(8,'affair','',0,1,'2018-02-12 05:41:14','2018-02-12 05:41:14'),(9,'110904','affair',8,1,'2018-02-12 05:41:14','2018-02-12 05:41:14'),(10,'files','affair/110904',9,1,'2018-02-12 05:41:14','2018-02-12 05:41:14'),(11,'WogFp5FDC','affair/110904/files',10,1,'2018-02-12 05:41:14','2018-02-12 05:41:14');
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-12 13:54:11
