CREATE TABLE IF NOT EXISTS `toll_event` (
  `toll_event_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_registration` varchar(50) NOT NULL,
  `image_id` bigint NOT NULL,
  `timestamp` timestamp(3) NOT NULL,
  PRIMARY KEY (`toll_event_id`),
  CONSTRAINT UC_toll_event UNIQUE (`vehicle_registration`, `image_id`, `timestamp`)
);

CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `customer_address` varchar(150) NOT NULL,
  PRIMARY KEY (`customer_id`)
);

CREATE TABLE IF NOT EXISTS `vehicle` (
  `vehicle_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_registration` varchar(20) NOT NULL,
  `vehicle_type` varchar(15) NOT NULL,
  PRIMARY KEY (`vehicle_id`)
);

CREATE TABLE IF NOT EXISTS `customer_vehicle` (
  `customer_id` int NOT NULL,
  `vehicle_id` int NOT NULL,
  CONSTRAINT PK_customer_vehicle PRIMARY KEY (`customer_id`, `vehicle_id`),
  FOREIGN KEY (`customer_id`) REFERENCES customer(`customer_id`),
  FOREIGN KEY (`vehicle_id`) REFERENCES vehicle(`vehicle_id`)
);

CREATE TABLE IF NOT EXISTS `vehicle_type_cost` (
  `vehicle_type` varchar(15) NOT NULL,
  `cost` double UNSIGNED NOT NULL,
  PRIMARY KEY (`vehicle_type`)
);