package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean add(TaiKhoanDTO tkDTO) {
        int result = 0;
        String sql = "INSERT INTO taikhoan (TenDangNhap, MatKhau, MaNhanVien, MaChucVu, TrangThai) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tkDTO.getTenDangNhap());
            pstmt.setString(2, tkDTO.getMatKhau());   
            pstmt.setString(3, tkDTO.getMaNV());   
            pstmt.setString(4, tkDTO.getMaChucVu());   
            pstmt.setString(5, tkDTO.getTrangThai());
            result = pstmt.executeUpdate();
            if(result > 0) 
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean update(TaiKhoanDTO tkDTO) {
        int result = 0;
        String sql = "UPDATE phong SET MatKhau=?, MaNV=?, MaCV=? WHERE TenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(4, tkDTO.getTenDangNhap());
            pstmt.setString(1, tkDTO.getMatKhau());   
            pstmt.setString(2, tkDTO.getMaNV());   
            pstmt.setString(3, tkDTO.getMaChucVu());   
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean delete(String username) {
        int result = 0;
        String sql = "UPDATE phong SET TrangThai = ? WHERE TenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(4, username);
            pstmt.setString(1, "Ẩn"); 
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
