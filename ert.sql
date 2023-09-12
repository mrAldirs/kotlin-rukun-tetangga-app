-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2023 at 05:31 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ert`
--

-- --------------------------------------------------------

--
-- Table structure for table `anggota_keluarga`
--

CREATE TABLE `anggota_keluarga` (
  `kd_anggota` varchar(15) NOT NULL,
  `kd_user` varchar(15) DEFAULT NULL,
  `nama_anggota` varchar(50) DEFAULT NULL,
  `status_anggota` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `anggota_keluarga`
--

INSERT INTO `anggota_keluarga` (`kd_anggota`, `kd_user`, `nama_anggota`, `status_anggota`) VALUES
('ANG-64a9e1b278c', 'USR000002', 'Painah', 'Istri'),
('ANG-64a9e1b2799', 'USR000002', 'Sumirah', 'Anak 1'),
('ANG-64a9e1b279e', 'USR000002', 'Paijah', 'Anak 2'),
('ANG-64a9e1b27a2', 'USR000002', 'Joni', 'Anak 3'),
('ANG-64a9e1b27a6', 'USR000002', 'Sintol', 'Anak 4');

-- --------------------------------------------------------

--
-- Table structure for table `dokumentasi`
--

CREATE TABLE `dokumentasi` (
  `kd_dokumentasi` varchar(15) NOT NULL,
  `kd_kegiatan` varchar(15) DEFAULT NULL,
  `foto_dokumentasi` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `dokumentasi`
--

INSERT INTO `dokumentasi` (`kd_dokumentasi`, `kd_kegiatan`, `foto_dokumentasi`) VALUES
('ALB000002', 'KL000004', 'IMG20230709035043.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `kegiatan`
--

CREATE TABLE `kegiatan` (
  `kd_kegiatan` varchar(15) NOT NULL,
  `kd_user` varchar(15) DEFAULT NULL,
  `nama_kegiatan` varchar(35) DEFAULT NULL,
  `tempat_kegiatan` varchar(50) DEFAULT NULL,
  `tgl_kegiatan` date DEFAULT NULL,
  `jam_kegiatan` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kegiatan`
--

INSERT INTO `kegiatan` (`kd_kegiatan`, `kd_user`, `nama_kegiatan`, `tempat_kegiatan`, `tgl_kegiatan`, `jam_kegiatan`) VALUES
('KL000001', 'USR000003', 'Hadroh dan Tasyakuran\n', 'Mushola', '2023-05-24', '19:00:00'),
('KL000002', 'USR000002', 'Kembang-kembangan Desa', 'Bu Nida', '2023-05-31', '18:00:00'),
('KL000003', 'USR000002', 'Bersih Desa', 'Depan Jalan', '2023-06-10', '07:30:00'),
('KL000004', 'USR000003', 'Bersih lingkungan', 'Depan Jalan', '2023-06-15', '22:30:00'),
('KL000005', 'USR000002', 'ybhbh', NULL, '2023-07-13', '17:05:00');

-- --------------------------------------------------------

--
-- Table structure for table `keluhan`
--

CREATE TABLE `keluhan` (
  `kd_keluhan` varchar(15) NOT NULL,
  `kd_user` varchar(15) DEFAULT NULL,
  `tgl_keluhan` date DEFAULT NULL,
  `teks_keluhan` text DEFAULT NULL,
  `img_keluhan` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `keluhan`
--

INSERT INTO `keluhan` (`kd_keluhan`, `kd_user`, `tgl_keluhan`, `teks_keluhan`, `img_keluhan`) VALUES
('KL000001', 'USR000002', '2023-05-21', 'p woi', 'IMG20230521195620.jpg'),
('KL000002', 'USR000002', '2023-05-21', 'p woi aku nduwe apik', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `laporan`
--

CREATE TABLE `laporan` (
  `kd_laporan` varchar(15) NOT NULL,
  `kd_kegiatan` varchar(15) DEFAULT NULL,
  `tgl_laporan` date DEFAULT NULL,
  `uang_masuk` varchar(25) DEFAULT NULL,
  `uang_keluar` varchar(25) DEFAULT NULL,
  `total` varchar(35) DEFAULT NULL,
  `keterangan` text DEFAULT NULL,
  `bukti_bayar` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `laporan`
--

INSERT INTO `laporan` (`kd_laporan`, `kd_kegiatan`, `tgl_laporan`, `uang_masuk`, `uang_keluar`, `total`, `keterangan`, `bukti_bayar`) VALUES
('LP000001', 'KL000002', '2023-07-09', '50000', '45000', '500', 'masih sisa', 'IMG20230709025644.jpg'),
('LP000002', 'KL000003', '2023-07-09', '2500', '2500', '500', 'uang pas', 'IMG20230709044808.jpg'),
('LP000003', 'KL000001', '2023-07-09', '60000', '50000', '10500', 'masih sisa bossku', 'IMG20230709051330.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `profil`
--

CREATE TABLE `profil` (
  `nama` varchar(35) NOT NULL,
  `nik` varchar(20) DEFAULT NULL,
  `jenis_kelamin` varchar(15) DEFAULT NULL,
  `usia` int(11) DEFAULT NULL,
  `pendidikan` varchar(35) DEFAULT NULL,
  `pekerjaan` varchar(35) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `no_hp` varchar(15) DEFAULT NULL,
  `foto_profil` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `profil`
--

INSERT INTO `profil` (`nama`, `nik`, `jenis_kelamin`, `usia`, `pendidikan`, `pekerjaan`, `email`, `no_hp`, `foto_profil`) VALUES
('Admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin.webp'),
('Mahendra Putra', '123456789', 'Laki-laki', 41, 'SMA', 'Development Microsoft', 'mahendraputra@gmail.com', '08123456798012', 'IMG20230522144508.jpg'),
('Yossa Mahendra Putra', '5124', 'Laki-laki', 25, 'D3 Manajemen Informatika', 'Data Analyst', 'yoss@gmail.com', '0812345678', 'haya.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `kd_user` varchar(15) NOT NULL,
  `nama` varchar(35) DEFAULT NULL,
  `username` varchar(35) DEFAULT NULL,
  `password` varchar(35) DEFAULT NULL,
  `level` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`kd_user`, `nama`, `username`, `password`, `level`) VALUES
('USR000001', 'Admin', 'admin', 'admin', 'Admin'),
('USR000002', 'Yossa Mahendra Putra', 'yossa', 'yossa', 'User'),
('USR000003', 'Mahendra Putra', 'mahendra', 'mahendra', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anggota_keluarga`
--
ALTER TABLE `anggota_keluarga`
  ADD PRIMARY KEY (`kd_anggota`),
  ADD KEY `kd_user` (`kd_user`);

--
-- Indexes for table `dokumentasi`
--
ALTER TABLE `dokumentasi`
  ADD PRIMARY KEY (`kd_dokumentasi`),
  ADD KEY `kd_kegiatan` (`kd_kegiatan`);

--
-- Indexes for table `kegiatan`
--
ALTER TABLE `kegiatan`
  ADD PRIMARY KEY (`kd_kegiatan`),
  ADD KEY `fk_relationship_4` (`kd_user`);

--
-- Indexes for table `keluhan`
--
ALTER TABLE `keluhan`
  ADD PRIMARY KEY (`kd_keluhan`),
  ADD KEY `fk_relationship_1` (`kd_user`);

--
-- Indexes for table `laporan`
--
ALTER TABLE `laporan`
  ADD PRIMARY KEY (`kd_laporan`),
  ADD KEY `kd_kegiatan` (`kd_kegiatan`);

--
-- Indexes for table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`nama`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`kd_user`),
  ADD KEY `fk_relationship_5` (`nama`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `anggota_keluarga`
--
ALTER TABLE `anggota_keluarga`
  ADD CONSTRAINT `anggota_keluarga_ibfk_1` FOREIGN KEY (`kd_user`) REFERENCES `user` (`kd_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `dokumentasi`
--
ALTER TABLE `dokumentasi`
  ADD CONSTRAINT `dokumentasi_ibfk_1` FOREIGN KEY (`kd_kegiatan`) REFERENCES `kegiatan` (`kd_kegiatan`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `kegiatan`
--
ALTER TABLE `kegiatan`
  ADD CONSTRAINT `fk_relationship_4` FOREIGN KEY (`kd_user`) REFERENCES `user` (`kd_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `keluhan`
--
ALTER TABLE `keluhan`
  ADD CONSTRAINT `fk_relationship_1` FOREIGN KEY (`kd_user`) REFERENCES `user` (`kd_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `laporan`
--
ALTER TABLE `laporan`
  ADD CONSTRAINT `laporan_ibfk_1` FOREIGN KEY (`kd_kegiatan`) REFERENCES `kegiatan` (`kd_kegiatan`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_relationship_5` FOREIGN KEY (`nama`) REFERENCES `profil` (`nama`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
