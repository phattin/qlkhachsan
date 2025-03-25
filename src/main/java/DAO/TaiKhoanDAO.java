package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.TaiKhoanDTO;

public class TaiKhoanDAO {
    public static ArrayList<TaiKhoanDTO> getAllTaiKhoan() {
        ArrayList<TaiKhoanDTO> tkList = new ArrayList<>();
        String query = "SELECT * FROM taikhoan WHERE TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tkList.add(new TaiKhoanDTO(
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("MaNV"),
                        rs.getString("MaChucVu"),
                        rs.getString("TrangThai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tkList;
    }

    public static TaiKhoanDTO getTaiKhoan(String tenDangNhap, String matkhau) {
        String query = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matkhau);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("MaNV"),
                        rs.getString("MaChucVu"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }
}
