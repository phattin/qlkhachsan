package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.ChucVuDTO;

public class ChucVuDAO {
    public static ArrayList<ChucVuDTO> getAllChucVu() {
        ArrayList<ChucVuDTO> cvList = new ArrayList<>();
        String query = "SELECT * FROM chucvu WHERE TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cvList.add(new ChucVuDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("TrangThai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cvList;
    }

    public static ChucVuDTO getChucVuByMa(String maChucVu) {
        String query = "SELECT * FROM chucvu WHERE MaChucVu = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maChucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ChucVuDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }

    public static ChucVuDTO getChucVuByMaNhanVien(String maNhanVien) {
        String query = "SELECT * FROM chucvu"
        + " JOIN taikhoan ON chucvu.MaChucVu = taikhoan.MaChucVu"
        + " JOIN nhanvien ON taikhoan.MaNV = nhanvien.MaNV"
        + " WHERE taikhoan.MaNV = ? AND taikhoan.TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ChucVuDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }


    public boolean add(ChucVuDTO cvDTO) {
        int result = 0;
        String sql = "INSERT INTO chucvu (MaChucVu, TenChucVu, TrangThai) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, cvDTO.getMaChucVu());   
            pstmt.setString(2, cvDTO.getTenChucVu());   
            pstmt.setString(3, cvDTO.getTrangThai());
            result = pstmt.executeUpdate();
            if(result > 0) 
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean update(ChucVuDTO cvDTO) {
        int result = 0;
        String sql = "UPDATE phong SET MatKhau=?, MaNV=?, MaChucVu=? WHERE TenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, cvDTO.getMaChucVu());   
            pstmt.setString(2, cvDTO.getTenChucVu());
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
            pstmt.setString(1, "Ẩn"); 
            pstmt.setString(2, username);
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
