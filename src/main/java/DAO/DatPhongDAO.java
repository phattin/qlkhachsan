package DAO;

import DTO.DatPhongDTO;
import Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class DatPhongDAO {

    // Lấy tất cả đặt phòng
    public ArrayList<DatPhongDTO> getALL() {
        ArrayList<DatPhongDTO> dsdatphong = new ArrayList<>();
        String sql = "SELECT * FROM datphong ";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsdatphong.add(new DatPhongDTO(
                        rs.getString("MaDatPhong"),
                        rs.getString("MaPhong"),
                        rs.getString("MaKhachHang"),
                        rs.getDate("NgayNhanPhong"),
                        rs.getDate("NgayTraPhong")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đặt phòng: " + e.getMessage());
        }
        return dsdatphong;
    }

    // Thêm đặt phòng mới
    public boolean add(DatPhongDTO datPhong) {
        String sql = "INSERT INTO datphong (MaDatPhong, MaPhong, MaKhachHang, NgayNhanPhong, NgayTraPhong) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, datPhong.getMaDatPhong());
            pstmt.setString(2, datPhong.getMaPhong());
            pstmt.setString(3, datPhong.getMaKH());
            pstmt.setDate(4, new java.sql.Date(datPhong.getNgayNhanPhong().getTime()));
            pstmt.setDate(5, new java.sql.Date(datPhong.getNgayTraPhong().getTime()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm đặt phòng: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật thông tin đặt phòng
    public boolean update(DatPhongDTO datPhong) {
        String sql = "UPDATE datphong SET MaPhong = ?, MaKhachhang = ?, NgayNhanPhong = ?, NgayTraPhong = ? WHERE MaDatPhong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, datPhong.getMaPhong());
            pstmt.setString(2, datPhong.getMaKH());
            pstmt.setDate(3, new java.sql.Date(datPhong.getNgayNhanPhong().getTime()));
            pstmt.setDate(4, new java.sql.Date(datPhong.getNgayTraPhong().getTime()));
            pstmt.setString(5, datPhong.getMaDatPhong());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật đặt phòng: " + e.getMessage());
        }
        return false;
    }

    // Xoá đặt phòng theo mã
    public boolean delete(String maDatPhong) {
        String sql = "DELETE FROM datphong WHERE MaDatPhong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maDatPhong);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xoá đặt phòng: " + e.getMessage());
        }
        return false;
    }

    // Tìm đặt phòng theo mã
    public DatPhongDTO getById(String maDatPhong) {
        DatPhongDTO datPhong = null;
        String sql = "SELECT * FROM datphong WHERE MaDatPhong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maDatPhong);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                datPhong = new DatPhongDTO(
                        rs.getString("MaDatPhong"),
                        rs.getString("MaPhong"),
                        rs.getString("MaKhachHang"),
                        rs.getDate("NgayNhanPhong"),
                        rs.getDate("NgayTraPhong")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm đặt phòng: " + e.getMessage());
        }
        return datPhong;
    }

    public ArrayList<DatPhongDTO> getByMaPhong(String maPhong) {
        ArrayList<DatPhongDTO> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM datphong WHERE MaPhong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maPhong);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                danhSach.add(new DatPhongDTO(
                        rs.getString("MaDatPhong"),
                        rs.getString("MaPhong"),
                        rs.getString("MaKhachHang"),
                        rs.getDate("NgayNhanPhong"),
                        rs.getDate("NgayTraPhong")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đặt phòng theo mã phòng: " + e.getMessage());
        }
        return danhSach;
    }
}