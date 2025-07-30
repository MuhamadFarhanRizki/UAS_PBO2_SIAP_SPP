
-- Tabel spp (id disamakan dengan tahun)
CREATE TABLE `spp` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `tahun` VARCHAR(10) DEFAULT NULL,
  `nominal` INT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data spp
INSERT INTO `spp` (`id`, `tahun`, `nominal`) VALUES
(2020, '2020', 50000),
(2022, '2022', 60000),
(2021, '2021', 50000); -- id disesuaikan dengan "tahun" agar dropdown tampil tahun

-- Tabel siswa
CREATE TABLE `siswa` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nis` VARCHAR(20) NOT NULL UNIQUE,
  `nama` VARCHAR(100) DEFAULT NULL,
  `tanggal_lahir` VARCHAR(255) DEFAULT NULL,
  `tahun_masuk` BIGINT DEFAULT NULL,
  `jenis_kelamin` VARCHAR(10) DEFAULT NULL,
  `nominal` INT DEFAULT NULL,
  FOREIGN KEY (`tahun_masuk`) REFERENCES `spp` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data siswa
INSERT INTO `siswa` (`nis`, `nama`, `tanggal_lahir`, `tahun_masuk`, `jenis_kelamin`, `nominal`) VALUES
('001', 'Raffi', '2025-07-24', 2021, 'Laki-Laki', 50000);

-- Tabel pembayaran
CREATE TABLE `pembayaran` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nis` VARCHAR(20) DEFAULT NULL,
  `nama_siswa` VARCHAR(100) DEFAULT NULL,
  `tahun_masuk` VARCHAR(10) DEFAULT NULL,
  `nominal` INT DEFAULT NULL,
  `metode_bayar` VARCHAR(50) DEFAULT NULL,
  `status_bayar` VARCHAR(20) DEFAULT NULL,
  `tanggal_bayar` DATE DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabel users
CREATE TABLE `users` (
  `username` VARCHAR(50) NOT NULL PRIMARY KEY,
  `password` VARCHAR(100) DEFAULT NULL,
  `role` VARCHAR(20) DEFAULT NULL,
  `nama` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data users
INSERT INTO `users` (`username`, `password`, `role`, `nama`) VALUES
('admin123', '123', 'admin', NULL),
('siswa001', '123', 'siswa', NULL);
