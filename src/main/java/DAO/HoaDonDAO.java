package DAO;

import DTO.HoaDonDTO;
import Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonDAO {

    public ArrayList<HoaDonDTO> getAllHoaDon() {
        ArrayList<HoaDonDTO> dshoadon = new ArrayList<>();
        String query = "SELECT * FROM hoadon";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                dshoadon.add(new HoaDonDTO(
                    rs.getString("MaHoaDon"),
                    rs.getString("MaNhanVien"),
                    rs.getDate("NgayTao"),
                    rs.getDouble("TongTien"),
                    rs.getDouble("TienTra"),
                    rs.getDouble("TienThua"),
                    rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách hóa đơn:" + e.getMessage());
        }

        return dshoadon;
    }

    public boolean themHoaDon(HoaDonDTO hoaDon) {
        String query = "INSERT INTO hoadon (MaHoaDon, MaNhanVien, NgayTao, TongTien, TienTra, TienThua, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, hoaDon.getMaHoaDon());
            pstmt.setString(2, hoaDon.getMaNhanVien());
            pstmt.setDate(3, new java.sql.Date(hoaDon.getNgayTao().getTime()));
            pstmt.setDouble(4, hoaDon.getTongTien());
            pstmt.setDouble(5, hoaDon.getTienTra());
            pstmt.setDouble(6, hoaDon.getTienThua());
            pstmt.setString(7, hoaDon.getTrangThai());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm hóa đơn:" + e.getMessage());
            return false;
        }
    } 

    public boolean capnhatHoaDon(HoaDonDTO hoaDon) {
        String query = "UPDATE hoadon SET MaNhanVien = ?, NgayTao = ?, TongTien = ?, TienTra = ?, TienThua = ?, TrangThai = ? WHERE MaHoaDon = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, hoaDon.getMaNhanVien());
            pstmt.setDate(2, new java.sql.Date(hoaDon.getNgayTao().getTime()));
            pstmt.setDouble(3, hoaDon.getTongTien());
            pstmt.setDouble(4, hoaDon.getTienTra());
            pstmt.setDouble(5, hoaDon.getTienThua());
            pstmt.setString(6, hoaDon.getTrangThai());
            pstmt.setString(7, hoaDon.getMaHoaDon());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật hóa đơn:" + e.getMessage());
            return false;
        }
    }

    public boolean xoaHoaDon(String maHoaDon) {
        String query = "DELETE FROM hoadon WHERE MaHoaDon = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maHoaDon);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa hóa đơn:" + e.getMessage());
            return false;
        }
    }
}