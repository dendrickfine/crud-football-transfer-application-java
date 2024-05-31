-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 31, 2024 at 07:59 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `istore`
--

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `id_player` int(11) NOT NULL,
  `name_player` varchar(30) NOT NULL,
  `position` enum('goalkeeper','defender','midfielder','striker','coach') NOT NULL,
  `citizenship` varchar(30) NOT NULL,
  `age` int(11) NOT NULL,
  `foot` enum('right','left','both') NOT NULL,
  `market_value` decimal(10,2) NOT NULL,
  `sell_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`id_player`, `name_player`, `position`, `citizenship`, `age`, `foot`, `market_value`, `sell_price`) VALUES
(13, 'Raphinha', 'striker', 'Brazil', 28, 'left', 30000.00, 0.00),
(14, 'Joao Felix', 'striker', 'Portugal', 24, 'right', 60000.00, 0.00),
(16, 'Mikayil Faye', 'defender', 'Guinea', 19, 'left', 3000.00, 2500.00),
(17, 'De jong', 'midfielder', 'Holland', 27, 'right', 60000.00, 85000.00),
(18, 'Inaki Pena', 'goalkeeper', 'Spain', 25, 'right', 5000.00, 0.00),
(19, 'Lamine Yamal', 'striker', 'Spain', 17, 'left', 130000.00, 150000.00),
(20, 'Ter Stegen', 'goalkeeper', 'Germany', 31, 'right', 50000.00, 30000.00);

-- --------------------------------------------------------

--
-- Table structure for table `selling`
--

CREATE TABLE `selling` (
  `id_selling` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `id_player` int(11) NOT NULL,
  `selling_date` date NOT NULL,
  `total_payment` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `selling`
--

INSERT INTO `selling` (`id_selling`, `id`, `id_player`, `selling_date`, `total_payment`) VALUES
(23, 1, 13, '2024-05-08', 100000.00),
(24, 11, 18, '2024-05-25', 10000.00),
(25, 11, 14, '2024-05-29', 130000.00);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(25) NOT NULL,
  `email` varchar(25) NOT NULL,
  `phone` varchar(25) NOT NULL,
  `address` varchar(40) NOT NULL,
  `password` varchar(25) NOT NULL,
  `role` enum('admin','customer') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `phone`, `address`, `password`, `role`) VALUES
(1, 'Tommy Doyle', 'tommy@wolves.uk', '081903567808', 'Molineaux', '123456', 'customer'),
(7, 'Joan Laporta', 'laporta@barca.es', '081903567882', 'Spotify Camp Nou', '123456', 'admin'),
(11, 'Michael Edwards', 'me@lfc.uk', '6281903567882', 'Anfield', '123456', 'customer'),
(13, 'Todd Boehly', 'todd@chelsea.uk', '0819035677', 'Stanford Bridge', '123456', 'customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`id_player`);

--
-- Indexes for table `selling`
--
ALTER TABLE `selling`
  ADD PRIMARY KEY (`id_selling`),
  ADD KEY `id` (`id`) USING BTREE,
  ADD KEY `id_player` (`id_player`) USING BTREE;

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `id_player` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `selling`
--
ALTER TABLE `selling`
  MODIFY `id_selling` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `selling`
--
ALTER TABLE `selling`
  ADD CONSTRAINT `selling_ibfk_1` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `selling_ibfk_2` FOREIGN KEY (`id_player`) REFERENCES `players` (`id_player`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
