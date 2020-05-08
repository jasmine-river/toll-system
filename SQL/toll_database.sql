-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 08, 2020 at 09:26 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toll_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(50) NOT NULL,
  `customer_address` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `customer_name`, `customer_address`) VALUES
(1, 'Tom Ford', '123 Dublin Road'),
(2, 'Clara Oswald', '123 Dublin Road'),
(3, 'John Smith', '123 Dublin Road'),
(4, 'Amy Pond', '123 Dublin Road'),
(5, 'Rory Williams', '123 Dublin Road'),
(6, 'Rose Tyler', '123 Dublin Road'),
(7, 'Martha Jones', '123 Dublin Road'),
(8, 'Mickey Smith', '123 Dublin Road'),
(9, 'Donna Noble', '123 Dublin Road'),
(10, 'Jack Hartness', '123 Dublin Road');

-- --------------------------------------------------------

--
-- Table structure for table `customer_vehicle`
--

CREATE TABLE `customer_vehicle` (
  `customer_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customer_vehicle`
--

INSERT INTO `customer_vehicle` (`customer_id`, `vehicle_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 4),
(2, 5),
(3, 6),
(3, 7),
(3, 8),
(4, 9),
(4, 10),
(5, 11),
(5, 12),
(5, 13),
(6, 14),
(6, 15),
(7, 16),
(7, 17),
(7, 18),
(8, 19),
(8, 20),
(9, 21),
(9, 22),
(10, 23),
(10, 24),
(10, 25),
(10, 26);

-- --------------------------------------------------------

--
-- Table structure for table `toll_event`
--

CREATE TABLE `toll_event` (
  `toll_event_id` int(11) NOT NULL,
  `vehicle_registration` varchar(20) NOT NULL,
  `image_id` bigint(20) NOT NULL,
  `timestamp` timestamp(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3),
  `billing_status` enum('Incomplete','Complete') NOT NULL DEFAULT 'Incomplete'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `toll_event`
--

INSERT INTO `toll_event` (`toll_event_id`, `vehicle_registration`, `image_id`, `timestamp`, `billing_status`) VALUES
(1, '191LH1111', 30402, '2020-02-14 10:15:30.123', 'Incomplete'),
(2, '201LH304', 30405, '2020-02-14 13:15:33.123', 'Incomplete'),
(3, '201LH3025', 30408, '2020-02-14 15:15:36.123', 'Incomplete'),
(4, '192D33457', 30409, '2020-02-14 16:15:37.123', 'Incomplete'),
(5, '161C3457', 30410, '2020-02-14 22:15:38.123', 'Incomplete'),
(6, '191LH1111', 30411, '2020-02-14 23:15:39.123', 'Incomplete'),
(7, '191LH1112', 30412, '2020-02-15 12:15:40.123', 'Incomplete'),
(8, '191LH1113', 30413, '2020-02-15 12:15:41.123', 'Incomplete'),
(9, '191LH1114', 30414, '2020-02-15 12:15:42.123', 'Incomplete'),
(10, '201LH304', 30415, '2020-02-15 12:15:43.123', 'Incomplete'),
(11, '201LH305', 30416, '2020-02-15 12:15:44.123', 'Incomplete'),
(12, '201LH306', 30417, '2020-02-15 12:15:45.123', 'Incomplete'),
(13, '201LH307', 30418, '2020-02-15 12:15:46.123', 'Incomplete'),
(14, '201LH308', 30419, '2020-02-15 21:15:47.123', 'Incomplete'),
(15, '191LH1111', 30421, '2020-02-16 11:16:49.666', 'Incomplete'),
(16, '152DL345', 30422, '2020-02-16 11:16:50.123', 'Incomplete'),
(17, '201LH304', 30423, '2020-02-16 11:16:51.123', 'Incomplete'),
(18, '201LH305', 30424, '2020-02-16 11:16:52.123', 'Incomplete'),
(19, '201LH3064', 30425, '2020-02-16 11:16:53.123', 'Incomplete'),
(20, '201LH3076', 30426, '2020-02-16 11:16:54.123', 'Incomplete'),
(21, '201LH3083', 30427, '2020-02-16 11:16:55.123', 'Incomplete'),
(22, '201LH309', 30428, '2020-02-16 11:16:56.123', 'Incomplete'),
(23, '201LH310', 30429, '2020-02-16 11:16:57.123', 'Incomplete'),
(24, '201LH311', 30430, '2020-02-16 11:16:58.123', 'Incomplete'),
(25, '201LH312', 30431, '2020-02-16 11:16:59.123', 'Incomplete'),
(26, '191LH1111', 30432, '2020-02-17 13:20:01.123', 'Incomplete'),
(27, '201CN3456', 30433, '2020-02-17 14:25:02.123', 'Incomplete'),
(28, '201CN3457', 30434, '2020-02-17 16:20:03.123', 'Incomplete'),
(29, '201LH304', 30435, '2020-02-17 16:20:04.123', 'Incomplete'),
(30, '181MH3456', 30436, '2020-02-17 17:33:05.123', 'Incomplete'),
(31, '181MH3456', 30437, '2020-02-17 18:20:06.123', 'Incomplete'),
(32, '181MH3458', 30438, '2020-02-17 18:20:07.123', 'Incomplete'),
(33, '181MH3459', 30439, '2020-02-17 18:58:08.123', 'Incomplete'),
(34, '181MH3461', 30441, '2020-02-17 23:25:10.654', 'Incomplete');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `vehicle_id` int(11) NOT NULL,
  `vehicle_registration` varchar(20) NOT NULL,
  `vehicle_type` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`vehicle_id`, `vehicle_registration`, `vehicle_type`) VALUES
(1, '152DL345', 'Car'),
(2, '161C3457', 'Van'),
(3, '181MH3456', 'Truck'),
(4, '181MH3458', 'Van'),
(5, '181MH3459', 'Car'),
(6, '181MH3461', 'Truck'),
(7, '191LH1111', 'Car'),
(8, '191LH1112', 'Car'),
(9, '191LH1113', 'Van'),
(10, '191LH1114', 'Truck'),
(11, '192D33457', 'Van'),
(12, '201CN3456', 'Car'),
(13, '201CN3457', 'Car'),
(14, '201LH3025', 'Car'),
(15, '201LH304', 'Truck'),
(16, '201LH305', 'Truck'),
(17, '201LH306', 'Van'),
(18, '201LH3064', 'Truck'),
(19, '201LH307', 'Car'),
(20, '201LH3076', 'Car'),
(21, '201LH308', 'Van'),
(22, '201LH3083', 'Truck'),
(23, '201LH309', 'Car'),
(24, '201LH310', 'Truck'),
(25, '201LH311', 'Van'),
(26, '201LH312', 'Car');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle_type_cost`
--

CREATE TABLE `vehicle_type_cost` (
  `vehicle_type` varchar(15) NOT NULL,
  `cost` double UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `vehicle_type_cost`
--

INSERT INTO `vehicle_type_cost` (`vehicle_type`, `cost`) VALUES
('Car', 3),
('Truck', 9),
('Van', 6);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `customer_vehicle`
--
ALTER TABLE `customer_vehicle`
  ADD PRIMARY KEY (`customer_id`,`vehicle_id`),
  ADD KEY `vehicle_id` (`vehicle_id`);

--
-- Indexes for table `toll_event`
--
ALTER TABLE `toll_event`
  ADD PRIMARY KEY (`toll_event_id`),
  ADD UNIQUE KEY `UC_toll_event` (`vehicle_registration`,`image_id`,`timestamp`);

--
-- Indexes for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`vehicle_id`);

--
-- Indexes for table `vehicle_type_cost`
--
ALTER TABLE `vehicle_type_cost`
  ADD PRIMARY KEY (`vehicle_type`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `toll_event`
--
ALTER TABLE `toll_event`
  MODIFY `toll_event_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `vehicle`
--
ALTER TABLE `vehicle`
  MODIFY `vehicle_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer_vehicle`
--
ALTER TABLE `customer_vehicle`
  ADD CONSTRAINT `customer_vehicle_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `customer_vehicle_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
