-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: blog
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'기술'),(2,'백엔드'),(3,'딥러닝'),(4,'머신러닝'),(5,'그래픽스'),(6,'프론트엔드'),(7,'기타');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `author` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `posts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `udesc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `date` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `category` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hashtag` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (1,'이태원','임시','테스트입니다','<p>테스트입니다.</p>\r\n','테스트입니다.\r\n','2020-07-07 10:07:33','2020-07-07 10:07:33','Tech',NULL),(2,'이태원','임시','테스트20200707','<h1><big>테스트20200707</big></h1>\r\n','테스트20200707\r\n','2020-07-07 10:21:38','2020-07-07 10:21:38','Tech',NULL),(3,'이태원','풀스택, 기계학습, 심층학습담당','테스트중이에요 너무 힘들어요 ㅠㅠㅠㅠㅠㅠㅠㅠㅠ','<h1>테스트중이에요 너무 힘들어요 ㅠㅠㅠㅠㅠㅠㅠ</h1>\r\n\r\n<p>이거 언제쯤 기술블로그로될까 ㅠㅠㅠㅠ</p>\r\n\r\n<p><img alt=\"\" src=\"/public/uploads/1595129120.png\" style=\"height:281px; width:500px\" /></p>\r\n','테스트중이에요 너무 힘들어요 ㅠㅠㅠㅠㅠㅠㅠ\r\n\r\n이거 언제쯤 기술블로그로될까 ㅠㅠㅠㅠ\r\n','2020-07-19 02:40:23','2020-07-19 03:25:44','Tech',NULL),(4,'이태원','풀스택, 기계학습, 심층학습담당','이미지 테스트','<h1>과연 이미지가 요약에 나올까???</h1>\r\n\r\n<p><img alt=\"\" src=\"/public/uploads/1595129183.png\" style=\"height:281px; width:500px\" /></p>\r\n','과연 이미지가 요약에 나올까???\r\n\r\n\r\n','2020-07-19 03:27:10','2020-07-19 03:27:10','Tech',NULL),(5,'이태원','풀스택, 기계학습, 심층학습담당','해쉬태그 테스트','<p>해쉬태그 테스트중입니다.</p>\r\n','해쉬태그 테스트중입니다.\r\n','2020-07-20 03:18:56','2020-07-20 03:18:56','기술',NULL),(6,'이태원','풀스택, 기계학습, 심층학습담당','해쉬태그테스트2','<p>해쉬태그 테스트2</p>\r\n','해쉬태그 테스트2\r\n','2020-07-20 03:19:36','2020-07-20 03:19:36','기술',NULL),(7,'이태원','풀스택, 기계학습, 심층학습담당','해쉬태그 테스트 3','<p>해쉬태그 테스트3</p>\r\n','해쉬태그 테스트3\r\n','2020-07-20 03:51:52','2020-07-20 03:51:52','기술','#해쉬태그_테스트 #백엔드 #Golang'),(9,'이태원','풀스택, 기계학습, 심층학습담당','모바일환경 테스트','<div style=\"text-align:center\">\r\n<figure class=\"image\" style=\"display:inline-block\"><img alt=\"코비\" height=\"300\" src=\"/public/uploads/1595417134.jpg\" width=\"400\" />\r\n<figcaption>맘바 멘탈리티</figcaption>\r\n</figure>\r\n</div>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>코비브라이언트의 자서전 맘바멘탈리티</p>\r\n\r\n<p>코비브라이언트의 경기를 직접 볼 수 있었던것에 감사한다</p>\r\n','\r\n\r\n맘바 멘탈리티\r\n\r\n\r\n\r\n','2020-07-22 11:26:56','2020-07-22 11:26:56','기타','#백엔드 #모바일환경 #언제배포해');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sessions` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiresAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES ('9e1212a9-a1bd-4361-85ad-9f920177b33a','elixter','2020-07-22 12:23:46');
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `uid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `upw` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uemail` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uname` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `udesc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `admin` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('elixter','21b630b3624a6fba2dae0e80d6dd734976f52b9c834854f7dca37e869d402583','ltw971@naver.com','이태원','풀스택, 기계학습, 심층학습담당',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-22 20:39:19
