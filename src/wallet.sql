-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 16, 2019 at 05:49 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wallet`
--

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `amount` float NOT NULL,
  `title` text NOT NULL,
  `type` varchar(11) NOT NULL,
  `date` date DEFAULT NULL,
  `category` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `user_id`, `amount`, `title`, `type`, `date`, `category`) VALUES
(32, 1, 1000, 'Wypłata', 'INCOMING', '2018-12-15', 'IN'),
(34, 1, 150, 'Mleko', 'OUTCOMING', '2019-01-08', 'FOOD'),
(35, 1, 120, 'Traktor', 'INCOMING', '2019-01-08', 'IN'),
(36, 1, 234, 'Coś tam', 'OUTCOMING', '2019-01-16', 'ELECTRONIC'),
(37, 4, 123, 'aaa', 'INCOMING', '2019-01-16', 'IN');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL DEFAULT '',
  `expired_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `surname`, `email`, `password`, `token`, `expired_date`) VALUES
(1, 'Krzysztof', 'Piaskowy', 'krzychu2956@gmail.com', 'mleko123', 'KVdxr0oNHht6gD0ErEWRHFKiuuI573pQndryN1jHq4we4ALFDUGRII3sJI74pf3kCdH0oR3I256NPvoWtmOu2HRRYpEtTGcpqAaR', '2019-01-16 17:27:51'),
(2, 'Marian', 'Mleczko', 'mleko@ok.pl', 'mleko123', 'jzGHVwaaSTVk5Ns3CU5v3FwDURl6XFlu2OdKU0BGVHVywTpsThMCQIUF36qYVpr08NAKmUkJnzi6Dno2nxHHa9vB2f9mANxITIXk', '2018-12-23 01:36:08'),
(3, 'Adam', 'Mickiewicz', 'adas@mickiewicz.pl', 'mleko123', 'uutClDS1dVjQ0fOilW0qKE7KsaXT3hFO8llHbhQglmkhohMeGUPvaZgmxyHLyFnrNRu7ryz8FOxKoi8DM6N2oUqnQnFLLxOhiL8U', '2018-12-30 18:11:18'),
(4, 'Adam', 'Mickiewicz', 'adam.mickiewicz@poeta.pl', 'adam123', '8Au3Af7xBsvGj9GlwFaTXuTp55kIwJA6KEY6hpoOingdTNfpgUFyWlsCHR76sNEluZGY1uyr3IsHFGsCDeWDQi2kQ0QBaiLBgMyT', '2019-01-16 16:22:55');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
