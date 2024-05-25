-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 25, 2024 at 07:05 AM
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
(12, 'Vitor Roque', 'striker', 'Brazil', 19, 'right', 25000.00, 30000.00),
(13, 'Raphinha', 'striker', 'Brazil', 28, 'left', 30000.00, 0.00),
(14, 'Joao Felix', 'striker', 'Portugal', 24, 'right', 60000.00, 120000.00),
(16, 'Mikayil Faye', 'defender', 'Guinea', 19, 'left', 3000.00, 2500.00),
(17, 'De jong', 'midfielder', 'Holland', 27, 'right', 60000.00, 85000.00),
(18, 'Inaki Pena', 'goalkeeper', 'Spain', 25, 'right', 5000.00, 0.00);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`id_player`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `id_player` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
