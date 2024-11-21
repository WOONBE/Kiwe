-- MySQL dump 10.13  Distrib 8.0.40, for Linux (x86_64)
--
-- Host: localhost    Database: kiwi
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `black_list`
--

DROP TABLE IF EXISTS `black_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `black_list` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `invalid_refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `black_list`
--

LOCK TABLES `black_list` WRITE;
/*!40000 ALTER TABLE `black_list` DISABLE KEYS */;
INSERT INTO `black_list` VALUES (1,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTA0ODY0NiwiZXhwIjoxNzMxNjUzNDQ2fQ.VHsb7e7_NlBq8vF-UdDc79KI92r7sj921lDJnjxWB4g7-DJ_0UfrHFv71feWlUQaqmUhYsPuTHEQgqM6Z6O5Jg'),(2,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTA0ODc0MSwiZXhwIjoxNzMxNjUzNTQxfQ.lKIQThbDfnPFnZY-QgQxEixLhCaoX1cDPjWMk6KZj1fFDb-xhHr0pTJn0q6Q7qmMBz2eJOuz4TWjFw3WQNPS0w'),(3,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NTk2MSwiZXhwIjoxNzMxOTAwNzYxfQ.hRfV3sl-omVqny3omitPtkdOqpS1-uDC0CAeGD4pntN-lQ8zHVNGqechLZAYGaIj5Gj2STR5oFk66lQW0WGguQ'),(4,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjAwMCwiZXhwIjoxNzMxOTAwODAwfQ.BCHHNfpDtP1a6ybjI5aX-gb-2S2-znHKwj6t2hpz3diDE6-98y0cyH9r9LKccmSD5YfCHxtST-19QyWSBl_rPQ'),(5,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjEwMywiZXhwIjoxNzMxOTAwOTAzfQ.pK-XZjWY7EgEp0yXHK0LXKS_gKxbHejUjyUITYUc8zctPxb766wMqI2qQHmKDhQSviTgMweZDZvhbzQNKIVOeA'),(6,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjEzMywiZXhwIjoxNzMxOTAwOTMzfQ.bLGF2rBBNlEAsiEqpO-XkgAgPanW1kdRoOj5-0TudHkcT8TaWduUJGRZc_a7Wa95UIkVBBhGLBNHZUWvOLVR2w'),(7,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjMwNSwiZXhwIjoxNzMxOTAxMTA1fQ.2a4a8wkUN1VCu6myIcNOxWZn-HAeVW5ZU_j7rp84esPRJcIBvebIrwf1nQVLXEpdcOAtQj0Tt4_BBtzxksdakA'),(8,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjQ1NSwiZXhwIjoxNzMxOTAxMjU1fQ.GCfbsuLxcA894gsORhRNMarXowYj0sPVPNh_XS5aBmgbK1S6iuuTIFcHXM_D6kyGbMNDoA46Tg3AR5pRPeBLmQ'),(9,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjUzMiwiZXhwIjoxNzMxOTAxMzMyfQ.jqigyPFbGndARcgGoql0QOpZNweqih288dw1gST_5mZa-MduLD5W9StrHydAflaef9B-vc4x0tGsaMyafnVKaw'),(10,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjgxOCwiZXhwIjoxNzMxOTAxNjE4fQ.T-rv8avgKsEg62_dPSU4VPgJQB_JSeHTEJwFZTpX5HFlRuuUj2Rg74op1zvIrXgtA3m--ap4iv9hA4aFhk7FHQ'),(11,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NjkwNSwiZXhwIjoxNzMxOTAxNzA1fQ.qGyZUa4y_Nd8objDyCe4NoDU723D782K_NpWVbXqdfEdW2RlNKBj_8oBepeSqhGyz5kfrPnZaRasT7796eg2rQ'),(12,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5Njk4OCwiZXhwIjoxNzMxOTAxNzg4fQ.NfaXVFngK5mb_k86YoRHhIn14F22DxU_rdJp2s-kBBRbKjjx-gAsYXW2pM6ry5EV9_r-p6WVsS5Vy2WB5cQQsQ'),(13,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NzA4NCwiZXhwIjoxNzMxOTAxODg0fQ.Wxt7AqwptzW3PxkJy5pO1svfli18wAM6SroyhCfnfyHoa_xTfaWDKJNF4gA2JIkCQocXBDiwT2QA2ULmBv9m6A'),(14,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NzU0OSwiZXhwIjoxNzMxOTAyMzQ5fQ.uA8flPxYcejL939D3UmWHXPMqpykzz-qUQnhcWLGD7EQRD1TW4yvlySvyWM_LKfTHx125J8eFj61-lPHm4-qIg'),(15,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5NzYwMCwiZXhwIjoxNzMxOTAyNDAwfQ.H5YNrEyWUosjbLGNbu0_8P9JSFp6WBKx6xNVm8Y0aPFGnitYx23GGnFh_hYwVxPsZmobZqjGN0EnMiMUXx5QYw'),(16,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5Nzk2NiwiZXhwIjoxNzMxOTAyNzY2fQ.jSeW3Woi0sT2fzpw3UeeYxzMVNY1G9AXnwTiPyi-bsswsUbpIIGgyc-NfOliwfASf_mZOR2brK268I8SFGb-tA'),(17,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTI5ODA0NSwiZXhwIjoxNzMxOTAyODQ1fQ.jXnFcuE6MDOAZwGqlUe4stTkCjXgmneyVJwFZWMd_8Dnn-d-7swC92daO68dO-97B4OPRgoEAworNPeboiw85w'),(18,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzE5NiwiZXhwIjoxNzMyMDY3OTk2fQ.POBx4cFBij74UCUXPjAXkb-CmJqOaz30TYF4GwW8fvm_bhf-NgfwFcWkMx5bWhNXsEbYJi8XWZzb00sVVtrusw'),(19,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzM2NSwiZXhwIjoxNzMyMDY4MTY1fQ.n0e5NmdfBG7UDQXbWTSu1uip3vYs57hLVjkdqqPy_OdriZWC6P18HuskahCqQm9sdMS5dCAWaKYseBsMwjLdog'),(20,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzQyOCwiZXhwIjoxNzMyMDY4MjI4fQ.XEqDr8-4b0jpQsiywtjJYptQBCGNaut0KKXWCtRtlR2EUexZfqo_6eHB1h4mRxvhRALz472-_EPghPedo6IhEg'),(21,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzUyNiwiZXhwIjoxNzMyMDY4MzI2fQ.STgKegEfYYLss_Iz2fa5nfmbIEtbreEe88oqAR_zduXcDCArUzhjovQ8JOjjKL5613rPdICGfgTyt7PhsAW6SQ'),(22,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzYzMSwiZXhwIjoxNzMyMDY4NDMxfQ.7G6w4-tEpfJGyXez2BJcM2zSZ6KpC1DsL5Mvu2UQjSX3kVP8k9X5LLWQOYAHrBG9MHuQNy5j2YuLG-e-BfDCiQ'),(23,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2Mjg4MywiZXhwIjoxNzMyMDY3NjgzfQ.WFgiUpK9e2E8SWjS8MPFe0us2sGYG3qwkgLJU5Y6pt2fhzCko_usWRJtfQwrYa7k4eCXCJniX6AJZQ2NBw5YKA'),(24,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ3MTA0MSwiZXhwIjoxNzMyMDc1ODQxfQ.2KftqtAApqbVCvaVGj1oTN_1GjIbsAwx0FlCJ_3Pcv9l93F42VJMYIrC9yj9QYMKgKnO-SR1C9IIHs9LxnUZ1Q'),(25,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ3MTA3OSwiZXhwIjoxNzMyMDc1ODc5fQ.qCFI4Ipmy4Zh_4UVDatnxp85ppFVHbOBZDmBro8Ar9wD4ZJWYuShGB9BGKGtN1tGkPJDojJIHK8sp9HlS5xtuQ'),(26,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ3MTUzNCwiZXhwIjoxNzMyMDc2MzM0fQ.ZmDJ9gULyAPW3vU-kJnYSJwN6u7E5x6ovHLVGXbNdU8CWlipDHeUmfNmM_u-TX-QhZqlv7gg18qgJgn-p5BLSw'),(27,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ3MjkzMSwiZXhwIjoxNzMyMDc3NzMxfQ.ahasdlE73o-NaJTIwxAqjL-c6i88aHUCUcpEDH2x6oFVcdLZuTxK3JyOoxpFHxq-q43tDxe8Yo7rNl9H2yqYTQ'),(28,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ3Nzg2MywiZXhwIjoxNzMyMDgyNjYzfQ.iDUOaCkaEGF8jwGd6YGVQXOYRkQy1A2qp9zXgBTRVgl8rtN2jRb72izXMSnbUVDwOX7ilNjIlRcIbCUW07TIDA'),(29,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ3OTE1MywiZXhwIjoxNzMyMDgzOTUzfQ.R03TCh2o2RBccjQrmp9fE2xkivQle-FvTgnkbAneAXkF-0YZ6Lh78Q9KiblL29TKZlZcB69AZ6-t_1wfYUrL6g'),(30,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ4MDY0NywiZXhwIjoxNzMyMDg1NDQ3fQ.NtM8oK1rY1DtUJ4-3YS4G2lOZBmlBo7JDnJCQlYKHtFio1s2IWOShRg5YoVhto6xAUKifSvq_5liaWRv2Earcw');
/*!40000 ALTER TABLE `black_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `choose_option`
--

DROP TABLE IF EXISTS `choose_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `choose_option` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `eng_name` varchar(255) DEFAULT NULL,
  `kor_name` varchar(255) DEFAULT NULL,
  `mega_drink_price` int NOT NULL,
  `normal_drink_price` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `choose_option`
--

LOCK TABLES `choose_option` WRITE;
/*!40000 ALTER TABLE `choose_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `choose_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kiosk`
--

DROP TABLE IF EXISTS `kiosk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kiosk` (
  `kiosk_id` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `kiosk_location` varchar(255) DEFAULT NULL,
  `kiosk_status` varchar(255) DEFAULT NULL,
  `owner_id` int DEFAULT NULL,
  PRIMARY KEY (`kiosk_id`),
  KEY `FK8yomrqbf9prit29wgome7rk58` (`owner_id`),
  CONSTRAINT `FK8yomrqbf9prit29wgome7rk58` FOREIGN KEY (`owner_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kiosk`
--

LOCK TABLES `kiosk` WRITE;
/*!40000 ALTER TABLE `kiosk` DISABLE KEYS */;
INSERT INTO `kiosk` VALUES (15,'2024-11-11 12:24:26.504776','2024-11-11 12:25:03.249247','string','string',6),(16,'2024-11-11 12:24:26.822550','2024-11-11 12:25:03.249468','string','string',6),(17,'2024-11-11 12:24:27.121631','2024-11-11 12:25:03.249658','string','string',6),(18,'2024-11-11 12:24:27.446786','2024-11-11 12:25:03.249860','string','string',6),(19,'2024-11-11 12:24:27.710879','2024-11-11 12:25:03.250069','string','string',6),(20,'2024-11-11 12:24:28.127597','2024-11-11 12:25:03.250277','string','string',6),(21,'2024-11-12 09:31:07.954933','2024-11-12 09:31:07.954933','string','string',6),(22,'2024-11-12 10:06:26.883039','2024-11-12 10:06:26.883039','location1','운영',6),(33,'2024-11-13 15:04:23.823944','2024-11-13 15:04:23.823944','SSAFY','READY',8),(34,'2024-11-13 15:25:54.095673','2024-11-13 15:25:54.095673','SSAFY','READY',8),(35,'2024-11-13 15:40:36.363000','2024-11-13 15:40:36.363000','SSAFY','READY',8),(37,'2024-11-13 16:32:45.010601','2024-11-13 16:32:45.010601','SSAFY','READY',8);
/*!40000 ALTER TABLE `kiosk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kiosk_order`
--

DROP TABLE IF EXISTS `kiosk_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kiosk_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `assigned_time` datetime(6) DEFAULT NULL,
  `kiosk_id` int DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKigpptrt0woexpg1admdnq38cm` (`kiosk_id`),
  KEY `FKrqv1cp0at9r086nhxhr3y630y` (`order_id`),
  CONSTRAINT `FKigpptrt0woexpg1admdnq38cm` FOREIGN KEY (`kiosk_id`) REFERENCES `kiosk` (`kiosk_id`),
  CONSTRAINT `FKrqv1cp0at9r086nhxhr3y630y` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kiosk_order`
--

LOCK TABLES `kiosk_order` WRITE;
/*!40000 ALTER TABLE `kiosk_order` DISABLE KEYS */;
INSERT INTO `kiosk_order` VALUES (21,'2024-11-13 15:41:07.920507',35,21),(22,'2024-11-13 15:42:09.435389',35,22),(23,'2024-11-13 15:43:29.882373',35,23),(24,'2024-11-13 15:44:08.228614',35,24);
/*!40000 ALTER TABLE `kiosk_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `member_email` varchar(255) NOT NULL,
  `member_name` varchar(255) NOT NULL,
  `member_password` varchar(255) NOT NULL,
  `type` enum('ADMIN','USER') DEFAULT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `UK3orqjaukiw2b73e2gw8rer4rq` (`member_email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'2024-11-07 10:48:03.936855','2024-11-11 10:09:27.011137','string','string','$2a$10$0qGiGvwM3GmQpla89lSMduaD.dweCeEqjXMP6p.RIFgcIJy4f3K8e','USER'),(2,'2024-11-08 13:25:32.876129','2024-11-08 13:25:32.876129','1@1.1','1','$2a$10$rRBevk7IvYCFR32j4DsL4ODPxI4rz/Z5kDlWcq5MUbPv1cGr7m4ra','USER'),(3,'2024-11-08 13:28:29.971417','2024-11-11 16:03:46.905873','5','5','$2a$10$vt0nwgtJryfkBPl47qEzsOzZByQk5v9ioOOW51G5.rskkz6Gq8lFu','USER'),(4,'2024-11-08 14:04:13.277332','2024-11-11 15:57:44.401909','3','3','$2a$10$Rqu/rORFLVGJf9peSAF5zO3qRnTkgwct8EUckdnOpndB6It7dDrTO','USER'),(5,'2024-11-08 15:50:35.454862','2024-11-08 15:50:35.454862','4','4','$2a$10$mRpfoLorNA9AMsxnU02VNeKhCbg0ntTfsEsP6qVXnIjBUIiGVnbbq','USER'),(6,'2024-11-11 08:04:57.261606','2024-11-11 08:04:57.261606','kdb','kdb','$2a$10$gntzKUSJLfa0FLCQ0Os5Y.crqJGHTT4MqYS4kLmhLH0UumvpMoQ0G','USER'),(7,'2024-11-11 10:13:11.042981','2024-11-11 10:14:36.449122','9','9','$2a$10$eGwOnHFYmhGYsZD2jwmfiO6tfENjkyHEEWsk3phiD/s2rUHz19zxO','USER'),(8,'2024-11-12 21:38:24.105789','2024-11-12 21:38:24.105789','rodri','로드리','$2a$10$UfWZgsLUr7tiFoZgsO8tQeDqtM/OchSQsSL4ZHSHWv6XtGzsFyn0.','USER');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_refresh_token`
--

DROP TABLE IF EXISTS `member_refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_refresh_token` (
  `member_id` int NOT NULL,
  `refresh_token` varchar(255) NOT NULL,
  `reissue_count` int NOT NULL,
  PRIMARY KEY (`member_id`),
  CONSTRAINT `FK1dyaxdo0v6l312tk7m85njx0h` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_refresh_token`
--

LOCK TABLES `member_refresh_token` WRITE;
/*!40000 ALTER TABLE `member_refresh_token` DISABLE KEYS */;
INSERT INTO `member_refresh_token` VALUES (2,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTA0MDA0MywiZXhwIjoxNzMxNjQ0ODQzfQ.LjWbRvHpNLw8bknahUvt2qdLYPE6pizDj7szD8sXiPfe_CIi0223-AGCdymZnEAOjFKE6iduypHW6TZc1VD7Wg',0),(3,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzM2NSwiZXhwIjoxNzMyMDY4MTY1fQ.n0e5NmdfBG7UDQXbWTSu1uip3vYs57hLVjkdqqPy_OdriZWC6P18HuskahCqQm9sdMS5dCAWaKYseBsMwjLdog',0),(4,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ4NTgwMSwiZXhwIjoxNzMyMDkwNjAxfQ._JJ0InN1Npcose54JdElH_kz6mhfE5pkLDCRRcesEZwOjtzpyX4Hpg-9KXVrDQCCYzBhQSseFBXKcDe0QKJbhQ',0),(5,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTA0ODc0MSwiZXhwIjoxNzMxNjUzNTQxfQ.lKIQThbDfnPFnZY-QgQxEixLhCaoX1cDPjWMk6KZj1fFDb-xhHr0pTJn0q6Q7qmMBz2eJOuz4TWjFw3WQNPS0w',0),(6,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQxMjgwNiwiZXhwIjoxNzMyMDE3NjA2fQ.C8KmJ4ggT-vpHU_GNjr6hOQeoY_CT-DX_PQwky894jSLU0CVt9IWBGVtXeeoLx7nmII42VVRYYd6AJ5uHxQNlg',0),(7,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTQ2MzQyOCwiZXhwIjoxNzMyMDY4MjI4fQ.XEqDr8-4b0jpQsiywtjJYptQBCGNaut0KKXWCtRtlR2EUexZfqo_6eHB1h4mRxvhRALz472-_EPghPedo6IhEg',0),(8,'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb2xhYmVhcjc1NCIsImlhdCI6MTczMTU1NzUyOSwiZXhwIjoxNzMyMTYyMzI5fQ.DkWfoecptJlSYjC9Zw0gnJDJriChx2nlzmPMpb4v_-slfoROTnZ5sjWjrCL8IY-Pj576yCgNrBOiRdyNOcsr5A',0);
/*!40000 ALTER TABLE `member_refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `menu_id` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `menu_category` varchar(255) DEFAULT NULL,
  `menu_category_num` int DEFAULT NULL,
  `menu_desc` varchar(255) DEFAULT NULL,
  `hot_or_ice` varchar(255) DEFAULT NULL,
  `menu_img` varchar(255) DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  `menu_price` int DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=326 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'2024-10-31 16:56:51.897551','2024-10-31 16:56:51.897551','디카페인',1,'향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피','HOT','drinks/HOT_디카페인 아메리카노.jpg','디카페인 아메리카노',2500),(2,'2024-10-31 16:56:51.961440','2024-10-31 16:56:51.961440','디카페인',2,'디카페인 아메리카노의 묵직한 바디감에 달콤한 사양벌꿀이 소프트하게 어우러진 커피','HOT','drinks/HOT_디카페인 꿀아메리카노.jpg','디카페인 꿀아메리카노',3700),(3,'2024-10-31 16:56:51.964680','2024-10-31 16:56:51.964680','디카페인',3,'디카페인 아메리카노에 헤이즐넛의 풍성한 향과 달콤함을 담아 향긋하고 부드럽게 즐기는 커피','HOT','drinks/HOT_디카페인 헤이즐넛 아메리카노.jpg','디카페인 헤이즐넛아메리카노',3700),(4,'2024-10-31 16:56:51.967765','2024-10-31 16:56:51.967765','디카페인',4,'디카페인 아메리카노에 바닐라의 부드러운 향과 달콤함을 조화롭게 담아낸 커피','HOT','drinks/HOT_디카페인 바닐라 아메리카노.jpg','디카페인 바닐라아메리카노',3700),(5,'2024-10-31 16:56:51.970352','2024-10-31 16:56:51.970352','디카페인',5,'디카페인 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼','HOT','drinks/HOT_디카페인 카페라떼.jpg','디카페인 카페라떼',3900),(6,'2024-10-31 16:56:51.972638','2024-10-31 16:56:51.972638','디카페인',6,'디카페인으로 즐기는 바닐라의 짙은 향과 풍부한 폼 밀크의 조화가 인상적인 달콤한 라떼.','HOT','drinks/HOT_디카페인 바닐라라떼.jpg','디카페인 바닐라라떼',4400),(7,'2024-10-31 16:56:51.975111','2024-10-31 16:56:51.975111','디카페인',7,'디카페인 에스프레소 샷, 부드러운 우유 그리고 달콤한 연유가 조화롭게 어우러진 라떼.','HOT','drinks/HOT_디카페인 연유라떼.jpg','디카페인 연유라떼',4900),(8,'2024-10-31 16:56:51.978098','2024-10-31 16:56:51.978098','디카페인',8,'폼 밀크 속에 진한 디카페인 에스프레소와 달콤한 카라멜을 가미해 부드럽게 즐기는 커피','HOT','drinks/HOT_디카페인 카라멜마끼아또.jpg','디카페인 카라멜마끼아또',4700),(9,'2024-10-31 16:56:51.980906','2024-10-31 16:56:51.980906','디카페인',9,'초코를 만나 풍부해진 디카페인 에스프레소와 고소한 우유, 부드러운 휘핑크림까지 더해 달콤하게 즐기는 커피.','HOT','drinks/HOT_디카페인 카페모카.jpg','디카페인 카페모카',4900),(10,'2024-10-31 16:56:51.983403','2024-10-31 16:56:51.983403','디카페인',10,'디카페인 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 카푸치노.','HOT','drinks/HOT_디카페인 카푸치노.jpg','디카페인 카푸치노',3900),(11,'2024-10-31 16:56:51.985706','2024-10-31 16:56:51.985706','디카페인',11,'부드러운 카페라떼에 헤이즐넛의 풍부한 향과 달콤함을 담아 향긋하게 즐길 수 있는 디카페인 라떼.','HOT','drinks/HOT_디카페인 헤이즐넛 라떼.jpg','디카페인 헤이즐넛라떼',4400),(12,'2024-10-31 16:56:51.987900','2024-10-31 16:56:51.987900','디카페인',12,'디카페인 에스프레소와 티라미수 소스 & 우유 그리고 풍미를 더해주는 달달한 크림까지 곁들여 완성한 티라미수 라떼.','HOT','drinks/HOT_디카페인 티라미수라떼.jpg','디카페인 티라미수라떼',4900),(13,'2024-10-31 16:56:51.990320','2024-10-31 16:56:51.990320','디카페인',13,'메가MGC커피 디카페인 아메리카노를 \'960ml\' 더 크고 가볍게 즐길 수 있는 대용량 커피','ICE','drinks/ICE_디카페인 메가리카노.jpg','디카페인 메가리카노',4500),(14,'2024-10-31 16:56:51.992747','2024-10-31 16:56:51.992747','디카페인',14,'향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피','ICE','drinks/ICE_디카페인 아메리카노.jpg','디카페인 아메리카노',2500),(15,'2024-10-31 16:56:51.995255','2024-10-31 16:56:51.995255','디카페인',15,'디카페인 아메리카노의 묵직한 바디감에 달콤한 사양벌꿀이 소프트하게 어우러진 커피.','ICE','drinks/ICE_디카페인 꿀아메리카노.jpg','디카페인 꿀아메리카노',3700),(16,'2024-10-31 16:56:51.999507','2024-10-31 16:56:51.999507','디카페인',16,'디카페인 아메리카노에 헤이즐넛의 풍성한 향과 달콤함을 담아 향긋하고 부드럽게 즐기는 커피.','ICE','drinks/ICE_디카페인 헤이즐넛 아메리카노.jpg','디카페인 헤이즐넛 아메리카노',3700),(17,'2024-10-31 16:56:52.002237','2024-10-31 16:56:52.002237','디카페인',17,'디카페인 아메리카노에 바닐라의 부드러운 향과 달콤함을 조화롭게 담아낸 커피.','ICE','drinks/ICE_디카페인 바닐라 아메리카노.jpg','디카페인 바닐라 아메리카노',3700),(18,'2024-10-31 16:56:52.005092','2024-10-31 16:56:52.005092','디카페인',18,'디카페인 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼.','ICE','drinks/ICE_디카페인 카페라떼.jpg','디카페인 카페라떼',3900),(19,'2024-10-31 16:56:52.007686','2024-10-31 16:56:52.007686','디카페인',19,'디카페인으로 즐기는 바닐라의 짙은 향과 풍부한 폼 밀크의 조화가 인상적인 달콤한 라떼.','ICE','drinks/ICE_디카페인 바닐라라떼.jpg','디카페인 바닐라라떼',4400),(20,'2024-10-31 16:56:52.010103','2024-10-31 16:56:52.010103','디카페인',20,'폼 밀크 속에 진한 디카페인 에스프레소와 달콤한 카라멜을 가미해 부드럽게 즐기는 커피','ICE','drinks/ICE_디카페인 카라멜마끼아또.jpg','디카페인 카라멜마끼아또',4700),(21,'2024-10-31 16:56:52.012423','2024-10-31 16:56:52.012423','디카페인',21,'초코를 만나 풍부해진 디카페인 에스프레소와 고소한 우유, 부드러운 휘핑크림까지 더해 달콤하게 즐기는 커피.','ICE','drinks/ICE_디카페인 카페모카.jpg','디카페인 카페모카',4900),(22,'2024-10-31 16:56:52.014999','2024-10-31 16:56:52.014999','디카페인',22,'디카페인 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 카푸치노.','ICE','drinks/ICE_디카페인 카푸치노.jpg','디카페인 카푸치노',3900),(23,'2024-10-31 16:56:52.017414','2024-10-31 16:56:52.017414','디카페인',23,'부드러운 카페라떼에 헤이즐넛의 풍부한 향과 달콤함을 담아 향긋하게 즐길 수 있는 디카페인 라떼.','ICE','drinks/ICE_디카페인 헤이즐넛 라떼.jpg','디카페인 헤이즐넛라떼',4400),(24,'2024-10-31 16:56:52.019852','2024-10-31 16:56:52.019852','디카페인',24,'디카페인 에스프레소와 티라미수 소스 & 우유 그리고 풍미를 더해주는 달달한 크림까지 곁들여 완성한 티라미수 라떼.','ICE','drinks/ICE_디카페인 티라미수라떼.jpg','디카페인 티라미수라떼',4900),(25,'2024-10-31 16:56:52.029969','2024-10-31 16:56:52.029969','스무디&프라페',1,'달달하고 청량한 수박 스무디 위로 다채로운 과일을 올려낸 수박 화채 스무디','ICE','drinks/수박 화채 스무디.jpg','수박 화채 스무디',4500),(26,'2024-10-31 16:56:52.033356','2024-10-31 16:56:52.033356','스무디&프라페',2,'바삭하고 고소한 코코넛 칩을 올리고 쌉싸름한 커피와 달콤한 코코넛이 조화로운 스무디','ICE','drinks/코코넛 커피 스무디.jpg','코코넛 커피 스무디',4800),(27,'2024-10-31 16:56:52.035631','2024-10-31 16:56:52.035631','스무디&프라페',3,'우유에 죠리퐁 씨리얼이 믹싱 된 얼음을 갈아 만든 시원한 프라페음료','ICE','drinks/ICE_플레인퐁크러쉬.jpg','플레인퐁크러쉬',3900),(28,'2024-10-31 16:56:52.038239','2024-10-31 16:56:52.038239','스무디&프라페',4,'리얼 벌꿀이 들어가 더 달콤한 퍼프허니 시리얼과 부드럽게 달달한 초코가 함께 만드는 즐거운 맛의 프라페.','ICE','drinks/ICE_초코허니퐁크러쉬.jpg','초코허니퐁크러쉬',3900),(29,'2024-10-31 16:56:52.047847','2024-10-31 16:56:52.047847','스무디&프라페',5,'바닐라빈 향을 머금은 부드러운 슈크림과 리얼 벌꿀이 들어간 퍼프허니 시리얼을 시원하게 즐기는 프라페.','ICE','drinks/ICE_슈크림허니퐁크러쉬.jpg','슈크림허니퐁크러쉬',3900),(30,'2024-10-31 16:56:52.054048','2024-10-31 16:56:52.054048','스무디&프라페',6,'바삭하고 달달한 퐁에 상큼한 딸기와 부드러운 우유, 얼음을 함께 블렌딩해 시원하게 즐기는 프라페.','ICE','drinks/ICE_딸기퐁크러쉬.jpg','딸기퐁크러쉬',3900),(31,'2024-10-31 16:56:52.056568','2024-10-31 16:56:52.056568','스무디&프라페',7,'바삭하고 달달한 퐁에 부드러운 바나나와 우유, 얼음을 함께 블렌딩해 부드럽고 시원하게 즐기는 프라페.','ICE','drinks/ICE_바나나퐁크러쉬.jpg','바나나퐁크러쉬',3900),(32,'2024-10-31 16:56:52.058892','2024-10-31 16:56:52.058892','스무디&프라페',8,'진한 초코스무디에 바삭한 쿠키를 넣어 퐁신퐁신한 마시멜로우 잼과 함께 달콤하게 즐기는 스무디','ICE','drinks/ICE_스모어 블랙쿠키 프라페.jpg','스모어블랙쿠키프라페',4400),(33,'2024-10-31 16:56:52.061330','2024-10-31 16:56:52.061330','스무디&프라페',9,'바삭하고 달콤한 오레오와 고소한 우유, 부드러운 바닐라향의 조화를 느낄 수 있는 프라페.','ICE','drinks/ICE_쿠키프라페.jpg','쿠키프라페',3900),(34,'2024-10-31 16:56:52.063537','2024-10-31 16:56:52.063537','스무디&프라페',10,'부드러운 바닐라와 달달한 딸기, 바삭한 오레오 쿠키가 달콤한 하모니를 선물하는 프라페.','ICE','drinks/ICE_딸기쿠키프라페.jpg','딸기쿠키프라페',3900),(35,'2024-10-31 16:56:52.065545','2024-10-31 16:56:52.065545','스무디&프라페',11,'상쾌한 민트에 달콤하게 씹는 재미를 더한 초콜릿칩의 즐거운 하모니가 매력적인 프라페.','ICE','drinks/ICE_민트프라페.jpg','민트프라페',3900),(36,'2024-10-31 16:56:52.068015','2024-10-31 16:56:52.068015','스무디&프라페',12,'바삭한 쿠키와 부드러운 바닐라에 향긋한 에스프레소를 섞어 만든 힐링 프라페.','ICE','drinks/ICE_커피프라페.jpg','커피프라페',3900),(37,'2024-10-31 16:56:52.069979','2024-10-31 16:56:52.069979','스무디&프라페',13,'진한 초코소스와 부드러운 바닐라향의 만남으로 질리지 않는 달콤함을 완성한 프라페.','ICE','drinks/ICE_리얼초코프라페.jpg','리얼초코프라페',3900),(38,'2024-10-31 16:56:52.072219','2024-10-31 16:56:52.072219','스무디&프라페',14,'향긋한 녹차 위에 우유와 휘핑크림을 더해 더 부드럽게 즐길 수 있는 프라페.','ICE','drinks/ICE_녹차프라페.jpg','녹차프라페',3900),(39,'2024-10-31 16:56:52.074192','2024-10-31 16:56:52.074192','스무디&프라페',15,'다채로운 비주얼로 보는 즐거움을 채우고, 달콤함과 상큼함 색깔마다 달라지는 유쾌한 맛까지 잡은 이색프라페.','ICE','drinks/ICE_유니콘프라페.jpg','유니콘프라페',4800),(40,'2024-10-31 16:56:52.076229','2024-10-31 16:56:52.076229','스무디&프라페',16,'상큼한 딸기 요거트 스무디 위에 고급스런 맛의 치즈케이크가 듬뿍 올라가 먹는 재미를 배가한 스무디.','ICE','drinks/ICE_스트로베리치즈홀릭.jpg','스트로베리치즈홀릭',4500),(41,'2024-10-31 16:56:52.078253','2024-10-31 16:56:52.078253','스무디&프라페',17,'더 시원하게 요거트의 새콤달콤한 맛을 오롯이 만끽할 수 있는 스무디.','ICE','drinks/ICE_플레인요거트스무디.jpg','플레인요거트스무디',3900),(42,'2024-10-31 16:56:52.080275','2024-10-31 16:56:52.080275','스무디&프라페',18,'요거트의 상큼함과 딸기의 상큼함을 상냥하게 어우른 상큼 스무디.','ICE','drinks/ICE_딸기요거트스무디.jpg','딸기요거트스무디',3900),(43,'2024-10-31 16:56:52.082222','2024-10-31 16:56:52.082222','스무디&프라페',19,'열대과일 망고의 진한 단 맛과 산뜻한 요거트의 하모니가 인상적인 스무디.','ICE','drinks/ICE_망고요거트스무디.jpg','망고요거트스무디',3900),(44,'2024-10-31 16:56:52.084134','2024-10-31 16:56:52.084134','신상품',1,'돌아온 여름시즌 베스트셀러! 달달하고 시원한 메가MGC커피 만의 수박주스','ICE','drinks/수박 주스.jpg','수박 주스',4000),(45,'2024-10-31 16:56:52.086212','2024-10-31 16:56:52.086212','신상품',2,'엄선된 시칠리아 레드오렌지와 자몽이 만난 상큼한 주스에 프로바이오틱스를 더해 건강한 블렌딩 주스','ICE','drinks/레드오렌지자몽주스.jpg','레드오렌지자몽주스',4000),(46,'2024-10-31 16:56:52.088091','2024-10-31 16:56:52.088091','신상품',3,'달콤한 샤인머스캣과 케일이 만난 싱그러운 주스에 칼슘을 더해 건강한 블렌딩 주스','ICE','drinks/샤인머스캣그린주스.jpg','샤인머스켓그린주스',4000),(47,'2024-10-31 16:56:52.090005','2024-10-31 16:56:52.090005','신상품',4,'새콤달콤한 딸기주스에 피쉬 콜라겐을 더해 건강한 블렌딩 주스','ICE','drinks/딸기주스.jpg','딸기주스',4000),(48,'2024-10-31 16:56:52.091914','2024-10-31 16:56:52.091914','신상품',5,'상큼한 딸기와 부드러운 바나나가 만나, 새콤달콤한 매력이 살아 있는 과일 음료.','ICE','drinks/딸기바나나주스.jpg','딸기바나나주스',4000),(49,'2024-10-31 16:56:52.093889','2024-10-31 16:56:52.093889','에이드',6,'상큼한 레몬, 상쾌한 라임, 달콤쌉싸름한 자몽의 3색 맛을 한데 어우른 메가MGC커피 시그니처 에이드.','ICE','drinks/ICE_메가에이드.jpg','메가에이드',3900),(50,'2024-10-31 16:56:52.095718','2024-10-31 16:56:52.095718','에이드',7,'시트러스향 가득한 레몬의 상큼함과 톡쏘는 탄산의 상쾌함이 만난 청량 에이드.','ICE','drinks/ICE_레몬에이드.jpg','레몬에이드',3500),(51,'2024-10-31 16:56:52.097791','2024-10-31 16:56:52.097791','에이드',8,'레몬에이드의 상큼한 청량감에 블루큐라소의 진한 향미를 더한 에이드.','ICE','drinks/ICE_블루레몬에이드.jpg','블루레몬에이드',3500),(52,'2024-10-31 16:56:52.099743','2024-10-31 16:56:52.099743','에이드',9,'자몽의 달콤쌉싸름한 맛과 탄산의 톡쏘는 목넘김이 어우러진 트로피컬 에이드.','ICE','drinks/ICE_자몽에이드.jpg','자몽에이드',3500),(53,'2024-10-31 16:56:52.101577','2024-10-31 16:56:52.101577','에이드',10,'산뜻한 청포도와 상쾌한 탄산의 달달한 조화가 인상적인 에이드.','ICE','drinks/ICE_청포도에이드.jpg','청포도에이드',3500),(54,'2024-10-31 16:56:52.103588','2024-10-31 16:56:52.103588','에이드',11,'섞으면 마법처럼 색이 변하는 재미에 라임의 청량함으로 입까지 즐거운 이색 에이드.','ICE','drinks/ICE_유니콘매직에이드(블루).jpg','유니콘매직에이드(블루)',3800),(55,'2024-10-31 16:56:52.105616','2024-10-31 16:56:52.105616','에이드',12,'섞으면 마법처럼 색이 변하는 재미에 레몬의 상큼함으로 입까지 즐거운 이색 에이드.','ICE','drinks/ICE_유니콘매직에이드(핑크).jpg','유니콘매직에이드(핑크)',3800),(56,'2024-10-31 16:56:52.107594','2024-10-31 16:56:52.107594','에이드',13,'체리의 새콤함과 청량감을 동시에 즐길 수 있는 환상적인 에이드.','ICE','drinks/ICE_체리콕.jpg','체리콕',3300),(57,'2024-10-31 16:56:52.109428','2024-10-31 16:56:52.109428','에이드',14,'상큼한 라임과 달콤한 향기의 애플민트가 어우러져 상쾌함을 한잔에 가득 채운 모히또 음료.','ICE','drinks/ICE_라임모히또.jpg','라임모히또',3800),(58,'2024-10-31 16:56:52.111368','2024-10-31 16:56:52.111368','음료',1,'산뜻하고 달콤한 딸기가 부드러운 우유와 어우러져 더욱 기분 좋게 즐기는 아이스 라떼.','ICE','drinks/ICE_딸기라떼.jpg','딸기라떼',3700),(59,'2024-10-31 16:56:52.113897','2024-10-31 16:56:52.113897','음료',2,'부드러운 우유에 진한 초코소스가 어우러져 달콤하게 입맛을 깨우는 초콜릿 음료.','ICE','drinks/ICE_아이스초코.jpg','아이스초코',3500),(60,'2024-10-31 16:56:52.115996','2024-10-31 16:56:52.115996','음료',3,'진한 초코와 리얼 오레오를 블렌딩해 씹는 맛을 더한 달콤한 아이스 라떼.','ICE','drinks/ICE_오레오초코라떼.jpg','오레오초코라떼',3900),(61,'2024-10-31 16:56:52.117992','2024-10-31 16:56:52.117992','음료',4,'부드러운 우유에 진한 초코소스, 달콤한 휘핑크림의 삼박자 조화로 완성한 달콤 초코 음료.','ICE','drinks/ICE_메가초코.jpg','메가초코',3800),(62,'2024-10-31 16:56:52.123404','2024-10-31 16:56:52.123404','음료',5,'향긋한 녹차에 우유를 더해 입 안에 부드러운 푸릇함을 선물하는 라떼.','ICE','drinks/ICE_녹차라떼.jpg','녹차라떼',3500),(63,'2024-10-31 16:56:52.125712','2024-10-31 16:56:52.125712','음료',6,'우유에 곡물을 더해 고소하고 든든하게 즐기는 라떼.','ICE','drinks/ICE_곡물라떼.jpg','곡물라떼',3000),(64,'2024-10-31 16:56:52.128001','2024-10-31 16:56:52.128001','음료',7,'달콤하고 고소한 고구마와 부드러운 우유가 만나 누구나 즐기기 좋은 든든한 라떼.','ICE','drinks/ICE_고구마라떼.jpg','고구마라떼',3500),(65,'2024-10-31 16:56:52.130350','2024-10-31 16:56:52.130350','음료',8,'은은하게 퍼지는 카라멜의 달달한 향기와 견과의 고소함을 한입에 즐길 수 있는 라떼.','ICE','drinks/ICE_토피넛라떼.jpg','토피넛라떼',3700),(66,'2024-10-31 16:56:52.132187','2024-10-31 16:56:52.132187','음료',9,'우유와 은은한 홍차가 어우러져 부드럽고 향긋한 한 모금을 완성한 라떼.','ICE','drinks/ICE_로얄밀크티라떼.jpg','로얄밀크티라떼',3700),(67,'2024-10-31 16:56:52.134001','2024-10-31 16:56:52.134001','음료',10,'모리셔스의 진한 흑당과 부드러운 우유가 달콤하게 조화를 이루는 라떼.','ICE','drinks/ICE_흑당라떼.jpg','흑당라떼',3300),(68,'2024-10-31 16:56:52.135797','2024-10-31 16:56:52.135797','음료',11,'타바론 얼그레이 홍차의 깊은 맛을 살린 일크티 라떼에 진한 흑당의 달콤함을 채운 음료.','ICE','drinks/ICE_흑당밀크티라떼.jpg','흑당밀크티라떼',3500),(69,'2024-10-31 16:56:52.137584','2024-10-31 16:56:52.137584','음료',12,'모리셔스의 진한 흑당과 부드러운 우유가 달콤한 조화에 흑당 버블을 함께 즐기는 라떼.','ICE','drinks/ICE_흑당버블라떼.jpg','흑당버블라떼',3700),(70,'2024-10-31 16:56:52.139423','2024-10-31 16:56:52.139423','음료',13,'타바론 얼그레이 홍차의 깊은 맛을 살린 일크티 라떼에 진한 흑당과 흑당 버블의 달콤함을 채운 음료.','ICE','drinks/ICE_흑당버블밀크티라떼.jpg','흑당버블밀크티라떼',3800),(71,'2024-10-31 16:56:52.141277','2024-10-31 16:56:52.141277','음료',14,'부드러운 우유에 진한 초코소스가 어우러져 달콤하게 입맛을 깨우는 초콜릿 음료.','HOT','drinks/HOT_핫초코.jpg','핫초코',3500),(72,'2024-10-31 16:56:52.143157','2024-10-31 16:56:52.143157','음료',15,'부드러운 우유에 진한 초코소스, 달콤한 휘핑크림의 삼박자 조화로 완성한 달콤 초코 음료.','HOT','drinks/HOT_메가초코.jpg','메가초코',3800),(73,'2024-10-31 16:56:52.145074','2024-10-31 16:56:52.145074','음료',16,'향긋한 녹차에 우유를 더해 입 안에 부드러운 푸릇함을 선물하는 라떼.','HOT','drinks/HOT_로얄밀크티라떼.jpg','로얄밀크티라떼',3700),(74,'2024-10-31 16:56:52.146831','2024-10-31 16:56:52.146831','음료',17,'향긋한 녹차에 우유를 더해 입 안에 부드러운 푸릇함을 선물하는 라떼.','HOT','drinks/HOT_녹차라떼.jpg','녹차라떼',3500),(75,'2024-10-31 16:56:52.148751','2024-10-31 16:56:52.148751','음료',18,'달콤하고 고소한 고구마와 부드러운 우유가 만나 누구나 즐기기 좋은 든든한 라떼.','HOT','drinks/HOT_고구마라떼.jpg','고구마라떼',3500),(76,'2024-10-31 16:56:52.150693','2024-10-31 16:56:52.150693','음료',19,'우유에 곡물을 더해 고소하고 든든하게 즐기는 라떼.','HOT','drinks/HOT_곡물라떼.jpg','곡물라떼',3000),(77,'2024-10-31 16:56:52.152510','2024-10-31 16:56:52.152510','음료',20,'은은하게 퍼지는 카라멜의 달달한 향기와 견과의 고소함을 한입에 즐길 수 있는 라떼.','HOT','drinks/HOT_토피넛라떼.jpg','토피넛라떼',3800),(78,'2024-10-31 16:56:52.154387','2024-10-31 16:56:52.154387','커피(콜드브루)',1,'차가운 물에 장시간 우려내 깔끔한 목넘김을 느낄 수 있는 콜드브루.','HOT','drinks/HOT_콜드브루오리지널.jpg','콜드브루오리지널',3500),(79,'2024-10-31 16:56:52.156185','2024-10-31 16:56:52.156185','커피(콜드브루)',2,'콜드브루에 고소한 우유를 섞어, 깔끔함과 부드러움을 잡은 라떼.','HOT','drinks/HOT_콜드브루라떼.jpg','콜드브루라떼',4000),(80,'2024-10-31 16:56:52.158179','2024-10-31 16:56:52.158179','커피(콜드브루)',3,'차가운 물에 장시간 우려내 깔끔한 목넘김을 느낄 수 있는 콜드브루.','ICE','drinks/ICE_콜드브루오리지널.jpg','콜드브루오리지널',3500),(81,'2024-10-31 16:56:52.160128','2024-10-31 16:56:52.160128','커피(콜드브루)',4,'콜드브루에 고소한 우유를 섞어, 깔끔함과 부드러움을 잡은 라떼.','ICE','drinks/ICE_콜드브루라떼.jpg','콜드브루라떼',4000),(82,'2024-10-31 16:56:52.161876','2024-10-31 16:56:52.161876','커피(콜드브루)',5,'카페인을 줄였지만, 원두 본연의 향미를 풍부하게 살려 맛을 잡은 콜드브루 디카페인','ICE','drinks/ICE_콜드브루디카페인.jpg','콜드브루 디카페인',3500),(83,'2024-10-31 16:56:52.164034','2024-10-31 16:56:52.164034','커피(콜드브루)',6,'카페인을 줄였지만, 원두 본연의 향미를 풍부하게 살려 맛을 잡은 콜드브루 디카페인','HOT','drinks/HOT_콜드브루디카페인.jpg','콜드브루 디카페인',4000),(84,'2024-10-31 16:56:52.166013','2024-10-31 16:56:52.166013','커피(콜드브루)',7,'고소한 우유를 섞어, 커피향에 부드러운 풍미를 가미한 콜드브루 디카페인 라떼','ICE','drinks/ICE_콜드브루디카페인라떼.jpg','콜드브루 디카페인라떼',3500),(85,'2024-10-31 16:56:52.167842','2024-10-31 16:56:52.167842','커피(콜드브루)',8,'고소한 우유를 섞어, 커피향에 부드러운 풍미를 가미한 콜드브루 디카페인 라떼','ICE','drinks/ICE_콜드브루디카페인라떼.jpg','콜드브루 디카페인라떼',4000),(86,'2024-10-31 16:56:52.169695','2024-10-31 16:56:52.169695','커피(HOT)',1,'메가MGC커피 블렌드 원두로 추출한 에스프레소에 물을 더해, 풍부한 바디감을 느낄 수 있는 스탠다드 커피.','HOT','drinks/HOT_아메리카노.jpg','아메리카노',1500),(87,'2024-10-31 16:56:52.171654','2024-10-31 16:56:52.171654','커피(HOT)',2,'아메리카노의 묵직한 바디감에 달콤한 사양벌꿀이 소프트하게 어우러진 커피.','HOT','drinks/HOT_꿀아메리카노.jpg','꿀아메리카노',2500),(88,'2024-10-31 16:56:52.173421','2024-10-31 16:56:52.173421','커피(HOT)',3,'아메리카노에 헤이즐넛의 풍성한 향과 달콤함을 담아 향긋하고 부드럽게','HOT','drinks/HOT_헤이즐넛아메리카노.jpg','헤이즐넛아메리카노',2500),(89,'2024-10-31 16:56:52.175395','2024-10-31 16:56:52.175395','커피(HOT)',4,'아메리카노에 바닐라의 부드러운 향과 달콤함을 조화롭게 담아낸 커피.','HOT','drinks/HOT_바닐라아메리카노.jpg','바닐라아메리카노',2700),(90,'2024-10-31 16:56:52.177264','2024-10-31 16:56:52.177264','커피(HOT)',5,'진한 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼.','HOT','drinks/HOT_카페라떼.jpg','카페라떼',2900),(91,'2024-10-31 16:56:52.179041','2024-10-31 16:56:52.179041','커피(HOT)',6,'바닐라의 짙은 향과 풍부한 폼 밀크의 조화가 인상적인 달콤한 라떼.','HOT','drinks/HOT_바닐라라떼.jpg','바닐라라떼',3400),(92,'2024-10-31 16:56:52.180902','2024-10-31 16:56:52.180902','커피(HOT)',7,'향기로운 에스프레소 샷, 부드러운 우유 그리고 달콤한 연유가 조화롭게 어우러진 라떼.','HOT','drinks/HOT_연유라떼.jpg','연유라떼',3900),(93,'2024-10-31 16:56:52.182782','2024-10-31 16:56:52.182782','커피(HOT)',8,'폼 밀크 속에 진한 에스프레소와 달콤한 카라멜을 가미해 부드럽게 즐기는 커피.','HOT','drinks/HOT_카라멜마끼아또.jpg','카라멜마끼야또',3700),(94,'2024-10-31 16:56:52.184609','2024-10-31 16:56:52.184609','커피(HOT)',9,'초코를 만나 풍부해진 에스프레소와 고소한 우유, 부드러운 휘핑크림까지 더해 달콤하게 즐기는 커피.','HOT','drinks/HOT_카페모카.jpg','카페모카',3900),(95,'2024-10-31 16:56:52.186521','2024-10-31 16:56:52.186521','커피(HOT)',10,'에스프레소 위에 올려진 우유 거품, 그리고 시나몬 파우더로 완성한 조화로운 맛의 커피.','HOT','drinks/HOT_카푸치노.jpg','카푸치노',2900),(96,'2024-10-31 16:56:52.188352','2024-10-31 16:56:52.188352','커피(HOT)',11,'부드러운 카페라떼에 헤이즐넛의 풍부한 향과 달콤함을 담아 향긋하게 즐길 수 있는 라떼.','HOT','drinks/HOT_헤이즐넛라떼.jpg','헤이즐넛라떼',3400),(97,'2024-10-31 16:56:52.190317','2024-10-31 16:56:52.190317','커피(HOT)',12,'에스프레소와 티라미수 소스 & 우유 그리고 풍미를 더해주는 달달한 크림까지 곁들여 완성한 티라미수 라떼.','HOT','drinks/HOT_티라미수라떼.jpg','티라미수라떼',3900),(98,'2024-10-31 16:56:52.196075','2024-10-31 16:56:52.196075','커피(ICE)',1,'깊고 진한 메가MGC커피 아메리카노를 \'960ml\' 더 큼직하게 즐길 수 있는 대용량 커피.','ICE','drinks/ICE_메가리카노.jpg','메가리카노',3000),(99,'2024-10-31 16:56:52.197990','2024-10-31 16:56:52.197990','커피(ICE)',2,'메가MGC커피 블렌드 원두로 추출한 에스프레소에 물을 더해, 풍부한 바디감을 느낄 수 있는 스탠다드 커피.','ICE','drinks/ICE_아메리카노.jpg','아메리카노',2000),(100,'2024-10-31 16:56:52.199927','2024-10-31 16:56:52.199927','커피(ICE)',3,'아메리카노의 묵직한 바디감에 달콤한 사양벌꿀이 소프트하게 어우러진 커피.','ICE','drinks/ICE_꿀아메리카노.jpg','꿀아메리카노',2500),(101,'2024-10-31 16:56:52.201669','2024-10-31 16:56:52.201669','커피(ICE)',4,'아메리카노에 헤이즐넛의 풍성한 향과 달콤함을 담아 향긋하고 부드럽게 즐기는 커피.','ICE','drinks/ICE_헤이즐넛아메리카노.jpg','헤이즐넛아메리카노',2500),(102,'2024-10-31 16:56:52.203431','2024-10-31 16:56:52.203431','커피(ICE)',5,'아메리카노에 바닐라의 부드러운 향과 달콤함을 조화롭게 담아낸 커피.','ICE','drinks/ICE_바닐라아메리카노.jpg','바닐라아메리카노',2500),(103,'2024-10-31 16:56:52.205348','2024-10-31 16:56:52.205348','커피(ICE)',6,'연유를 섞은 라떼에 에스프레소를 얼린 커피큐브를 올려, 녹을수록 더 진한 커피가 느껴지는 라떼.','ICE','drinks/ICE_큐브라떼.jpg','큐브라떼',4200),(104,'2024-10-31 16:56:52.207258','2024-10-31 16:56:52.207258','커피(ICE)',7,'진한 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼.','ICE','drinks/ICE_카페라떼.jpg','카페라떼',2900),(105,'2024-10-31 16:56:52.209127','2024-10-31 16:56:52.209127','커피(ICE)',8,'바닐라의 짙은 향과 풍부한 폼 밀크의 조화가 인상적인 달콤한 라떼.','ICE','drinks/ICE_바닐라라떼.jpg','바닐라라떼',3400),(106,'2024-10-31 16:56:52.210967','2024-10-31 16:56:52.210967','커피(ICE)',9,'폼 밀크 속에 진한 에스프레소와 달콤한 카라멜을 가미해 부드럽게 즐기는 커피.','ICE','drinks/ICE_카라멜마끼아또.jpg','카라멜마끼아또',3500),(107,'2024-10-31 16:56:52.212791','2024-10-31 16:56:52.212791','커피(ICE)',10,'초코를 만나 풍부해진 에스프레소와 고소한 우유, 부드러운 휘핑크림까지 더해 달콤하게 즐기는 커피.','ICE','drinks/ICE_카페모카.jpg','카페모카',3700),(108,'2024-10-31 16:56:52.214605','2024-10-31 16:56:52.214605','커피(ICE)',11,'에스프레소 위에 올려진 우유 거품, 그리고 시나몬 파우더로 완성한 조화로운 맛의 커피.','ICE','drinks/ICE_카푸치노.jpg','카푸치노',2700),(109,'2024-10-31 16:56:52.217130','2024-10-31 16:56:52.217130','커피(ICE)',12,'부드러운 카페라떼에 헤이즐넛의 풍부한 향과 달콤함을 담아 향긋하게 즐길 수 있는 라떼.','ICE','drinks/ICE_헤이즐넛라떼.jpg','헤이즐넛라떼',3400),(110,'2024-10-31 16:56:52.219145','2024-10-31 16:56:52.219145','커피(ICE)',13,'에스프레소와 티라미수 소스 & 우유 그리고 풍미를 더해주는 달달한 크림까지 곁들여 완성한 티라미수 라떼.','ICE','drinks/ICE_티라미수라떼.jpg','티라미수라떼',3900),(111,'2024-10-31 16:56:52.220935','2024-10-31 16:56:52.220935','티',1,'상큼 달콤한 트로피컬 과일, 강렬한 레드 용과 에이드에 아식한 알로에 펄이 들어간 그린티 베이스의 티플레저','ICE','drinks/트로피컬 용과 티플레저.jpg','트로피컬 용과 티플레져',3800),(112,'2024-10-31 16:56:52.222833','2024-10-31 16:56:52.222833','티',2,'홍차의 깊은 맛과 풍부한 복숭아 향이 어우러진 달콤한 여름철 인기 음료','ICE','drinks/ICE_복숭아아이스티.jpg','복숭아아이스티',3000),(113,'2024-10-31 16:56:52.224715','2024-10-31 16:56:52.224715','티',3,'달콤한 꿀청에 재운 자몽에 홍차의 부드러움을 어우른 상큼한 과일티.','ICE','drinks/ICE_허니자몽블랙티.jpg','허니자몽블랙티',3700),(114,'2024-10-31 16:56:52.226557','2024-10-31 16:56:52.226557','티',4,'애플티의 향긋함과 유자청의 상큼달콤함을 한컵에 담아낸 과일티.','ICE','drinks/ICE_사과유자차.jpg','사과유자차',3500),(115,'2024-10-31 16:56:52.228602','2024-10-31 16:56:52.228602','티',5,'비타민이 가득 든 상큼달콤한 유자를 듬뿍 넣어 향긋한 즐거움을 전하는 과일티.','ICE','drinks/ICE_유자차.jpg','유자차',3300),(116,'2024-10-31 16:56:52.230657','2024-10-31 16:56:52.230657','티',6,'상큼한 레몬의 맛과 향을 오롯이 살린 비타민C 가득한 과일티.','ICE','drinks/ICE_레몬차.jpg','레몬차',3300),(117,'2024-10-31 16:56:52.232552','2024-10-31 16:56:52.232552','티',7,'달콤쌉싸름한 자몽의 조화로운 맛을 한 잔 가득 느낄 수 있는 과일티.','ICE','drinks/ICE_자몽차.jpg','자몽차',3300),(118,'2024-10-31 16:56:52.234760','2024-10-31 16:56:52.234760','티',8,'고소한 감칠맛과 부드러운 목넘김으로 산뜻하게 마음을 위로하는 국내산 녹차.','ICE','drinks/ICE_녹차.jpg','녹차',2500),(119,'2024-10-31 16:56:52.236525','2024-10-31 16:56:52.236525','티',9,'멘톨향의 묵직한 청량감, 상쾌한 맛과 향이 인상적인 허브티.','ICE','drinks/ICE_페퍼민트.jpg','페퍼민트',2500),(120,'2024-10-31 16:56:52.238524','2024-10-31 16:56:52.238524','티',10,'마음을 진정 시켜주는 산뜻한 풀내음을 느낄 수 있는 허브티.','ICE','drinks/ICE_캐모마일.jpg','캐모마일',2500),(121,'2024-10-31 16:56:52.241091','2024-10-31 16:56:52.241091','티',11,'홍차 특유의 풍부한 플레이버를 만끽할 수 있는 허브티.','ICE','drinks/ICE_얼그레이.jpg','얼그레이',2500),(122,'2024-10-31 16:56:52.243244','2024-10-31 16:56:52.243244','티',12,'달콤한 꿀청에 재운 자몽에 홍차의 부드러움을 어우른 상큼한 과일티.','HOT','drinks/HOT_허니자몽블랙티.jpg','허니자몽블랙티',3700),(123,'2024-10-31 16:56:52.245006','2024-10-31 16:56:52.245006','티',13,'애플티의 향긋함과 유자청의 상큼달콤함을 한컵에 담아낸 과일티.','HOT','drinks/HOT_사과유자차.jpg','사과유자차',3500),(124,'2024-10-31 16:56:52.246837','2024-10-31 16:56:52.246837','티',14,'비타민이 가득 든 상큼달콤한 유자를 듬뿍 넣어 향긋한 즐거움을 전하는 과일티.','HOT','drinks/HOT_유자차.jpg','유자차',3300),(125,'2024-10-31 16:56:52.248907','2024-10-31 16:56:52.248907','티',15,'상큼한 레몬의 맛과 향을 오롯이 살린 비타민C 가득한 과일티.','HOT','drinks/HOT_레몬차.jpg','레몬차',3300),(126,'2024-10-31 16:56:52.254069','2024-10-31 16:56:52.254069','티',16,'달콤쌉싸름한 자몽의 조화로운 맛을 한 잔 가득 느낄 수 있는 과일티.','HOT','drinks/HOT_자몽차.jpg','자몽차',3300),(127,'2024-10-31 16:56:52.256309','2024-10-31 16:56:52.256309','티',17,'고소한 감칠맛과 부드러운 목넘김으로 산뜻하게 마음을 위로하는 국내산 녹차.','HOT','drinks/HOT_녹차.jpg','녹차',2500),(128,'2024-10-31 16:56:52.258622','2024-10-31 16:56:52.258622','티',18,'멘톨향의 묵직한 청량감, 상쾌한 맛과 향이 인상적인 허브티.','HOT','drinks/HOT_페퍼민트.jpg','페퍼민트',2500),(129,'2024-10-31 16:56:52.261781','2024-10-31 16:56:52.261781','티',19,'마음을 진정 시켜주는 산뜻한 풀내음을 느낄 수 있는 허브티.','HOT','drinks/HOT_캐모마일.jpg','캐모마일',2500),(130,'2024-10-31 16:56:52.263378','2024-10-31 16:56:52.263378','티',20,'홍차 특유의 풍부한 플레이버를 만끽할 수 있는 허브티.','HOT','drinks/HOT_얼그레이.jpg','얼그레이',2500),(131,'2024-10-31 16:56:52.265037','2024-10-31 16:56:52.265037','디저트',1,'핫도그 보물섬을 찾아서 생크림 오믈렛 파도를 헤엄치는 커스타드맛 상어로 구성 된 여름 간식 꾸러미','','food/첨벙첨벙 간식꾸러미.jpg','첨벙첨벙간식꾸러미',3900),(132,'2024-10-31 16:56:52.267457','2024-10-31 16:56:52.267457','디저트',2,'진한 초코 스무디에 바삭바삭한 허니 퐁시리얼, 리얼 오레오와 아이스크림을 올려 낸 파르페','','food/오레오초코 퐁파르페.jpg','오레오초코퐁파르페',4400),(133,'2024-10-31 16:56:52.270331','2024-10-31 16:56:52.270331','디저트',3,'달콤한 딸기 요거트 스무디에 바삭바삭한 허니 퐁시리얼, 이탈리아 과자 보노미 글라사떼와 아이스크림을 올려 낸 파르페','','food/딸기크림 퐁파르페.jpg','딸기크림퐁파르페',4400),(134,'2024-10-31 16:56:52.272280','2024-10-31 16:56:52.272280','디저트',4,'그래놀라가 콕콕 박힌 통곡물 쿠키에 달콤하게 구운 마시멜로우가 만나 더욱 건강한 쿠키','','food/그래놀라 스모어 쿠키.jpg','그레놀라스모어쿠키',2900),(135,'2024-10-31 16:56:52.274036','2024-10-31 16:56:52.274036','디저트',5,'고소한 식빵 사이에 햄과 치즈를 샌드하고, 빵 윗면에 멜팅치즈를 토핑해 든든한 한끼를 선물하는 샌드위치.','','food/크로크무슈.jpg','크로크무슈',3800),(136,'2024-10-31 16:56:52.276061','2024-10-31 16:56:52.276061','디저트',6,'풍미 가득한 치즈와 바비큐 소스로 맛을 낸 소고기를 듬뿍 담은 미트파이','','food/미트파이.jpg','미트파이',4400),(137,'2024-10-31 16:56:52.278712','2024-10-31 16:56:52.278712','디저트',7,'고소한 프랑스산 프레지덩 버터를 듬뿍넣어 더 부드럽고 짭쪼롬하게 즐길 수 있는 베이커리 메뉴','','food/버터버터소금빵.jpg','버터버터소금빵',3200),(138,'2024-10-31 16:56:52.280535','2024-10-31 16:56:52.280535','디저트',8,'제주 당근과 에그마요를 품은 간편하고 든든한 한 손 샌드위치.','','food/제주당근에그마요.jpg','제주당근에그마요샌드위치',3400),(139,'2024-10-31 16:56:52.282380','2024-10-31 16:56:52.282380','디저트',9,'이탈리아산 듀럼밀을 사용한 쫄깃한 도우에 버터, 모짜렐라 치즈, 꿀을 더해 든든하게 만든 단짠단짠 허니버터 피자','','food/허니버터 피자.jpg','허니버터피자',5100),(140,'2024-10-31 16:56:52.284110','2024-10-31 16:56:52.284110','디저트',10,'이탈리아산 듀럼밀을 사용한 쫄깃한 도우에 토마토소스, 모짜렐라 치즈 바질을 더해 든든하게 만든 풍미가득 마르게리타 피자','','food/마르게리타 피자.jpg','마르게리타피자',5400),(141,'2024-10-31 16:56:52.286134','2024-10-31 16:56:52.286134','디저트',11,'경산 대추와 우리 밀을 넣은 달콤 쫀득한 피에 국내산 조청과 쌀 튀밥을 입혀 바삭하게 구운 한과','','food/경산 대추 과즐.jpg','경산대추과즐',1900),(142,'2024-10-31 16:56:52.287649','2024-10-31 16:56:52.287649','디저트',12,'전라북도 장수 고원에서 자란 사과를 품은 말랑 촉촉한 동물 모양 비건 젤리','','food/장수 사과 비건 젤리.jpg','장수사과비건젤리',1900),(143,'2024-10-31 16:56:52.289138','2024-10-31 16:56:52.289138','디저트',13,'햄과 치즈의 조화로운 한끼를 만끽할 수 있는 메가커피 샌드위치.','','food/햄앤치즈샌드.jpg','햄앤치즈샌드',2000),(144,'2024-10-31 16:56:52.290694','2024-10-31 16:56:52.290694','디저트',14,'꿀을 섞은 크림을 바삭한 쿠키슈 안에 넣어, 건강하고 맛있게 완성한 디저트.','','food/아이스허니와앙슈.jpg','아이스허니와앙슈',2400),(145,'2024-10-31 16:56:52.292309','2024-10-31 16:56:52.292309','디저트',15,'진하고 폭신한 초콜릿 스펀지 사이에 부드러운 생크림이 듬뿍 들어간 몽쉘 케이크.','','food/몽쉘케이크.jpg','몽쉘케이크',5300),(146,'2024-10-31 16:56:52.294003','2024-10-31 16:56:52.294003','디저트',16,'화이트 초코칩이 가득 박힌 말차 쿠키에 달콤하게 구운 마시멜로우를 얹어 달콤쌉싸름한 매력을 간직한 쿠키.','','food/말차스모어쿠키.jpg','말차스모어쿠키',2900),(147,'2024-10-31 16:56:52.295660','2024-10-31 16:56:52.295660','디저트',17,'초코칩이 콕콕 박힌 촉촉한 초코 쿠키에 달콤하게 구운 마시멜로우가 만나 더 진한 초코 맛 쿠키.','','food/초코스모어쿠키.jpg','초코스모어쿠키',2900),(148,'2024-10-31 16:56:52.297299','2024-10-31 16:56:52.297299','디저트',18,'쫄깃한 빵 속 포슬포슬한 감자를 가득 담아낸 베이커리 메뉴.','','food/감자빵.jpg','감자빵',3500),(149,'2024-10-31 16:56:52.298879','2024-10-31 16:56:52.298879','디저트',19,'메가커피 인기 음료인 유니콘 프라페를 모티브로 만든 매지컬 비주얼 마카롱.','','food/유니콘프라페마카롱.jpg','유니콘프라페마카롱',2100),(150,'2024-10-31 16:56:52.300425','2024-10-31 16:56:52.300425','디저트',20,'메가커피 인기메뉴인 쿠키프라페를 모티브로 만든 달콤한 마카롱.','','food/쿠키프라페마카롱.jpg','쿠키프라페마카롱',2100),(151,'2024-10-31 16:56:52.302074','2024-10-31 16:56:52.302074','디저트',21,'메가커피 시그니처 음료 메가초코를 모티브로 만든 초코맛 마카롱.','','food/메가초코 마카롱.jpg','메가초코마카롱',2100),(152,'2024-10-31 16:56:52.303617','2024-10-31 16:56:52.303617','디저트',22,'딸기요거트 스무디를 연상하게 하는 상큼한 마카롱.','','food/딸기요거트 마카롱.jpg','딸기요거트마카롱',2100),(153,'2024-10-31 16:56:52.305232','2024-10-31 16:56:52.305232','디저트',23,'진한 치즈의 맛을 달콤하고 부드럽게 느낄 수 있는 케이크.','','food/치즈케익.jpg','치즈케익',3500),(154,'2024-10-31 16:56:52.306752','2024-10-31 16:56:52.306752','디저트',24,'달콤한 초콜릿 본연의 맛을 더 진하게 느낄 수 있는 케이크.','','food/초코무스케익.jpg','초코케익',3500),(155,'2024-10-31 16:56:52.308251','2024-10-31 16:56:52.308251','디저트',25,'부드러운 치즈 맛과 코코아의 조화로운 향으로 호불호 없이 즐기는 케이크.','','food/티라미수케익.jpg','티라미수케익',3500),(156,'2024-10-31 16:56:52.309904','2024-10-31 16:56:52.309904','디저트',26,'바삭하고 쫄깃하게 구워낸 빵에 달콤한 크림을 올려 즐기는 조화로운 베이커리 메뉴.','','food/허니브레드.jpg','허니브레드',4500),(157,'2024-10-31 16:56:52.311604','2024-10-31 16:56:52.311604','디저트',27,'부드러운 빵 속에 쫄깃한 소시지를 넣어 알찬 한입을 완성한 베이커리 메뉴.','','food/핫도그.jpg','핫도그',2700),(158,'2024-10-31 16:56:52.313132','2024-10-31 16:56:52.313132','디저트',28,'진한 초콜릿칩을 넣어 만든 메가MGC커피 시그니처 쿠키.','','food/초콜릿칩 쿠키.jpg','메가쿠기(초코칩)',2000),(159,'2024-10-31 16:56:52.314694','2024-10-31 16:56:52.314694','디저트',29,'고소한 마카다미아를 넣어 만든 메가MGC커피 시그니처 쿠키.','','food/마카다미아 쿠키.jpg','메가쿠키(마카다미아)',2000),(160,'2024-10-31 16:56:52.316253','2024-10-31 16:56:52.316253','디저트',30,'버터풍미가 가득한 크루와상의 바삭함과 와플의 부드러움을 합친 겉바속촉 베이커리 메뉴.','','food/플레인크로플.jpg','플레인크로플',2500),(161,'2024-10-31 16:56:52.317812','2024-10-31 16:56:52.317812','디저트',31,'따뜻하고 바삭한 크로플 위에 차갑고 달콤한 바닐라 아이스크림을 올려 만든 매력적인 베이커리 메뉴.','','food/아이스크림크로플.jpg','아이스크림크로플',3500);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_menu`
--

DROP TABLE IF EXISTS `order_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `menu_id` int DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4oumkm265ypdtcndhueq1cv26` (`menu_id`),
  KEY `FKmw4iqpidcbvklykhbhxewx124` (`order_id`),
  CONSTRAINT `FK4oumkm265ypdtcndhueq1cv26` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`),
  CONSTRAINT `FKmw4iqpidcbvklykhbhxewx124` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_menu`
--

LOCK TABLES `order_menu` WRITE;
/*!40000 ALTER TABLE `order_menu` DISABLE KEYS */;
INSERT INTO `order_menu` VALUES (1,2,1,1),(2,3,3,2),(3,5,4,3),(5,1,99,5),(12,1,47,11),(13,1,99,11),(14,1,1,12),(15,1,3,12),(16,1,44,13),(17,1,48,13),(18,1,3,14),(19,1,47,15),(20,1,48,15),(21,1,44,15),(22,1,47,16),(23,1,48,17),(24,1,44,18),(25,1,100,19),(26,1,47,20),(27,1,45,21),(28,1,45,22),(29,1,46,23),(30,1,46,24),(31,1,46,25),(32,1,46,26),(33,1,44,26),(34,1,47,27),(35,1,47,28),(36,1,45,28),(37,1,44,29),(38,1,47,30),(39,1,44,31),(40,1,45,32);
/*!40000 ALTER TABLE `order_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2024-11-12 10:10:38.078396','PENDING',10,1),(2,'2024-11-12 10:10:44.497075','PENDING',20,2),(3,'2024-11-12 10:10:49.064594','COMPLETED',30,1),(5,'2024-11-12 16:48:50.507688','COMPLETED',20,1),(11,'2024-11-13 13:43:28.365592','COMPLETED',10,1),(12,'2024-11-13 13:57:10.188573','COMPLETED',30,1),(13,'2024-11-13 13:57:44.973486','COMPLETED',20,1),(14,'2024-11-13 13:58:07.724862','COMPLETED',20,2),(15,'2024-11-13 13:59:00.761191','COMPLETED',30,2),(16,'2024-11-13 13:59:26.783140','COMPLETED',30,2),(17,'2024-11-13 13:59:52.556990','COMPLETED',20,2),(18,'2024-11-13 14:55:35.753126','COMPLETED',30,2),(19,'2024-11-13 14:59:15.581709','COMPLETED',40,1),(20,'2024-11-13 15:01:23.114649','COMPLETED',40,2),(21,'2024-11-13 15:41:07.916740','PENDING',40,1),(22,'2024-11-13 15:42:09.431645','PENDING',40,2),(23,'2024-11-13 15:43:29.877947','COMPLETED',50,1),(24,'2024-11-13 15:44:08.225074','COMPLETED',50,2),(25,'2024-11-13 15:52:41.845807','PENDING',50,1),(26,'2024-11-13 15:53:06.164354','COMPLETED',50,2),(27,'2024-11-13 15:55:50.235992','PENDING',20,1),(28,'2024-11-13 15:59:33.007187','COMPLETED',10,1),(29,'2024-11-13 16:03:13.949394','COMPLETED',10,2),(30,'2024-11-13 16:03:36.346629','COMPLETED',10,1),(31,'2024-11-13 16:03:54.156209','COMPLETED',20,2),(32,'2024-11-13 16:13:07.023718','COMPLETED',30,1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `view_count`
--

DROP TABLE IF EXISTS `view_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `view_count` (
  `id` int NOT NULL AUTO_INCREMENT,
  `requesturi` varchar(255) DEFAULT NULL,
  `view_count` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `view_count`
--

LOCK TABLES `view_count` WRITE;
/*!40000 ALTER TABLE `view_count` DISABLE KEYS */;
/*!40000 ALTER TABLE `view_count` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-14  5:00:01
