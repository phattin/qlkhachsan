-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 14, 2025 lúc 09:44 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlkhachsan`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chucnang`
--

CREATE TABLE `chucnang` (
  `MaChucNang` varchar(10) NOT NULL,
  `TenChucNang` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chucnang`
--

INSERT INTO `chucnang` (`MaChucNang`, `TenChucNang`) VALUES
('CN001', 'Trang chủ'),
('CN002', 'Quản lý phòng'),
('CN003', 'Đặt phòng'),
('CN004', 'Quản lý khách hàng'),
('CN005', 'Quản lý nhân viên'),
('CN006', 'Quản lý tài khoản'),
('CN007', 'Quản lý phân quyền'),
('CN008', 'Quản lý hóa đơn'),
('CN009', 'Quản lý dịch vụ'),
('CN010', 'Thống kê');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chucvu`
--

CREATE TABLE `chucvu` (
  `MaChucVu` varchar(10) NOT NULL,
  `TenChucVu` varchar(50) NOT NULL,
  `TrangThai` enum('Ẩn','Hiện') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chucvu`
--

INSERT INTO `chucvu` (`MaChucVu`, `TenChucVu`, `TrangThai`) VALUES
('CV001', 'Quản lý', 'Hiện'),
('CV002', 'Nhân viên', 'Hiện');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhsachphong`
--

CREATE TABLE `danhsachphong` (
  `MaDSP` varchar(10) NOT NULL,
  `MaDatPhong` varchar(10) DEFAULT NULL,
  `MaPhong` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `datphong`
--

CREATE TABLE `datphong` (
  `MaDatPhong` varchar(10) NOT NULL,
  `MaPhong` varchar(10) DEFAULT NULL,
  `MaKhachHang` varchar(10) DEFAULT NULL,
  `NgayNhanPhong` date NOT NULL,
  `NgayTraPhong` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `datphong`
--

INSERT INTO `datphong` (`MaDatPhong`, `MaPhong`, `MaKhachHang`, `NgayNhanPhong`, `NgayTraPhong`) VALUES
('DP001', 'P001', 'KH001', '2025-03-25', '2025-03-27');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `dichvu`
--

CREATE TABLE `dichvu` (
  `MaDV` varchar(10) NOT NULL,
  `TenDV` varchar(100) NOT NULL,
  `GiaDV` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `dichvu`
--

INSERT INTO `dichvu` (`MaDV`, `TenDV`, `GiaDV`) VALUES
('DV001', 'Thuê xe', 20000),
('DV002', 'Ăn sáng', 20000),
('DV003', 'Giặt ủi', 20000),
('DV004', 'Hồ bơi', 20000),
('DV005', 'Spa & thư giãn', 20000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `MaHoaDon` varchar(10) NOT NULL,
  `MaDatPhong` varchar(10) DEFAULT NULL,
  `MaKhachHang` varchar(10) DEFAULT NULL,
  `MaNhanVien` varchar(10) NOT NULL,
  `NgayTao` date NOT NULL,
  `TongTien` int(10) NOT NULL,
  `TienTra` int(10) NOT NULL,
  `TienThua` int(10) NOT NULL,
  `TrangThai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`MaHoaDon`, `MaDatPhong`, `MaKhachHang`, `MaNhanVien`, `NgayTao`, `TongTien`, `TienTra`, `TienThua`, `TrangThai`) VALUES
('HD001', 'DP001', 'KH001', 'NV001', '2025-03-25', 600000, 600000, 0, 'Đã thanh toán');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKhachHang` varchar(10) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `CCCD` varchar(20) NOT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`MaKhachHang`, `HoTen`, `CCCD`, `SoDienThoai`, `Email`, `DiaChi`) VALUES
('KH001', 'Le Van C', '123456789012', '0912345678', 'lvc@example.com', 'Hà Nội'),
('KH002', 'Pham Thi D', '098765432109', '0908765432', 'ptd@example.com', 'TP. HCM');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaiphong`
--

CREATE TABLE `loaiphong` (
  `MaLoaiPhong` varchar(10) NOT NULL,
  `TenLoaiPhong` varchar(50) NOT NULL,
  `SoGiuong` int(3) NOT NULL,
  `GiaPhong` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaiphong`
--

INSERT INTO `loaiphong` (`MaLoaiPhong`, `TenLoaiPhong`, `SoGiuong`, `GiaPhong`) VALUES
('LP001', 'Phòng đơn', 1, 300000),
('LP002', 'Phòng đôi', 2, 500000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `MaNhanVien` varchar(10) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `GioiTinh` enum('Nam','Nữ') DEFAULT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `DiaChi` varchar(255) NOT NULL,
  `Luong` int(10) NOT NULL,
  `NgayNhanViec` date NOT NULL,
  `TrangThai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`MaNhanVien`, `HoTen`, `GioiTinh`, `SoDienThoai`, `Email`, `DiaChi`, `Luong`, `NgayNhanViec`, `TrangThai`) VALUES
('NV001', 'Nguyen Van A', 'Nam', '0123456789', 'nva@example.com', 'Quận 5', 10000000, '2023-01-15', 'Hiện'),
('NV002', 'Tran Thi B', 'Nữ', '0987654321', 'ttb@example.com', 'Quận 8', 8000000, '2023-05-20', 'Hiện');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phanquyen`
--

CREATE TABLE `phanquyen` (
  `MaChucVu` varchar(10) NOT NULL,
  `MaChucNang` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phanquyen`
--

INSERT INTO `phanquyen` (`MaChucVu`, `MaChucNang`) VALUES
('CV001', 'CN001'),
('CV001', 'CN002'),
('CV001', 'CN003'),
('CV001', 'CN004'),
('CV001', 'CN005'),
('CV001', 'CN006'),
('CV001', 'CN007'),
('CV001', 'CN008'),
('CV001', 'CN009'),
('CV001', 'CN010'),
('CV002', 'CN003'),
('CV002', 'CN008');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phong`
--

CREATE TABLE `phong` (
  `MaPhong` varchar(10) NOT NULL,
  `MaLoaiPhong` varchar(10) DEFAULT NULL,
  `TrangThai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phong`
--

INSERT INTO `phong` (`MaPhong`, `MaLoaiPhong`, `TrangThai`) VALUES
('P001', 'LP001', 'Trống'),
('P002', 'LP002', 'Đã đặt');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sudungdichvu`
--

CREATE TABLE `sudungdichvu` (
  `MaSuDungDV` varchar(10) NOT NULL,
  `MaDatPhong` varchar(10) DEFAULT NULL,
  `MaDV` varchar(10) DEFAULT NULL,
  `SoLuong` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sudungdichvu`
--

INSERT INTO `sudungdichvu` (`MaSuDungDV`, `MaDatPhong`, `MaDV`, `SoLuong`) VALUES
('SD001', 'DP001', 'DV001', 2),
('SD002', 'DP001', 'DV002', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `MaTK` varchar(10) NOT NULL,
  `MaNhanVien` varchar(10) DEFAULT NULL,
  `MaChucVu` varchar(10) DEFAULT NULL,
  `TenDangNhap` varchar(50) NOT NULL,
  `MatKhau` varchar(255) NOT NULL,
  `TrangThai` enum('Ẩn','Hiện') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`MaTK`, `MaNhanVien`, `MaChucVu`, `TenDangNhap`, `MatKhau`, `TrangThai`) VALUES
('TK001', 'NV001', 'CV001', 'admin', '123456', 'Hiện'),
('TK002', 'NV002', 'CV002', 'user01', 'password', 'Hiện');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chucnang`
--
ALTER TABLE `chucnang`
  ADD PRIMARY KEY (`MaChucNang`);

--
-- Chỉ mục cho bảng `chucvu`
--
ALTER TABLE `chucvu`
  ADD PRIMARY KEY (`MaChucVu`);

--
-- Chỉ mục cho bảng `danhsachphong`
--
ALTER TABLE `danhsachphong`
  ADD PRIMARY KEY (`MaDSP`),
  ADD KEY `MaDatPhong` (`MaDatPhong`),
  ADD KEY `MaPhong` (`MaPhong`);

--
-- Chỉ mục cho bảng `datphong`
--
ALTER TABLE `datphong`
  ADD PRIMARY KEY (`MaDatPhong`),
  ADD KEY `MaPhong` (`MaPhong`),
  ADD KEY `MaKH` (`MaKhachHang`);

--
-- Chỉ mục cho bảng `dichvu`
--
ALTER TABLE `dichvu`
  ADD PRIMARY KEY (`MaDV`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MaHoaDon`),
  ADD KEY `MaDatPhong` (`MaDatPhong`),
  ADD KEY `MaKH` (`MaKhachHang`),
  ADD KEY `hoadon_ibfk_3` (`MaNhanVien`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKhachHang`),
  ADD UNIQUE KEY `CCCD` (`CCCD`);

--
-- Chỉ mục cho bảng `loaiphong`
--
ALTER TABLE `loaiphong`
  ADD PRIMARY KEY (`MaLoaiPhong`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`MaNhanVien`);

--
-- Chỉ mục cho bảng `phanquyen`
--
ALTER TABLE `phanquyen`
  ADD KEY `fk_chucvu_phanquyen` (`MaChucVu`),
  ADD KEY `fk_chucnang` (`MaChucNang`);

--
-- Chỉ mục cho bảng `phong`
--
ALTER TABLE `phong`
  ADD PRIMARY KEY (`MaPhong`),
  ADD KEY `MaLoaiPhong` (`MaLoaiPhong`);

--
-- Chỉ mục cho bảng `sudungdichvu`
--
ALTER TABLE `sudungdichvu`
  ADD PRIMARY KEY (`MaSuDungDV`),
  ADD KEY `MaDatPhong` (`MaDatPhong`),
  ADD KEY `MaDV` (`MaDV`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`MaTK`),
  ADD UNIQUE KEY `TenDangNhap` (`TenDangNhap`),
  ADD KEY `MaNV` (`MaNhanVien`),
  ADD KEY `MaChucVu` (`MaChucVu`);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `danhsachphong`
--
ALTER TABLE `danhsachphong`
  ADD CONSTRAINT `danhsachphong_ibfk_1` FOREIGN KEY (`MaDatPhong`) REFERENCES `datphong` (`MaDatPhong`),
  ADD CONSTRAINT `danhsachphong_ibfk_2` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`);

--
-- Các ràng buộc cho bảng `datphong`
--
ALTER TABLE `datphong`
  ADD CONSTRAINT `datphong_ibfk_1` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`),
  ADD CONSTRAINT `datphong_ibfk_2` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`);

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`MaDatPhong`) REFERENCES `datphong` (`MaDatPhong`),
  ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`),
  ADD CONSTRAINT `hoadon_ibfk_3` FOREIGN KEY (`MaNhanVien`) REFERENCES `nhanvien` (`MaNhanVien`);

--
-- Các ràng buộc cho bảng `phanquyen`
--
ALTER TABLE `phanquyen`
  ADD CONSTRAINT `fk_chucnang_phanquyen` FOREIGN KEY (`MaChucNang`) REFERENCES `chucnang` (`MaChucNang`),
  ADD CONSTRAINT `fk_chucvu_phanquyen` FOREIGN KEY (`MaChucVu`) REFERENCES `chucvu` (`MaChucVu`);

--
-- Các ràng buộc cho bảng `phong`
--
ALTER TABLE `phong`
  ADD CONSTRAINT `phong_ibfk_1` FOREIGN KEY (`MaLoaiPhong`) REFERENCES `loaiphong` (`MaLoaiPhong`);

--
-- Các ràng buộc cho bảng `sudungdichvu`
--
ALTER TABLE `sudungdichvu`
  ADD CONSTRAINT `sudungdichvu_ibfk_1` FOREIGN KEY (`MaDatPhong`) REFERENCES `datphong` (`MaDatPhong`),
  ADD CONSTRAINT `sudungdichvu_ibfk_2` FOREIGN KEY (`MaDV`) REFERENCES `dichvu` (`MaDV`);

--
-- Các ràng buộc cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD CONSTRAINT `taikhoan_ibfk_1` FOREIGN KEY (`MaNhanVien`) REFERENCES `nhanvien` (`MaNhanVien`),
  ADD CONSTRAINT `taikhoan_ibfk_2` FOREIGN KEY (`MaChucVu`) REFERENCES `chucvu` (`MaChucVu`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
