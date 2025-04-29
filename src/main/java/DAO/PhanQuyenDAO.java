package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.PhanQuyenDTO;

public class PhanQuyenDAO {
    public static ArrayList<PhanQuyenDTO> getAllPhanQuyen() {
        ArrayList<PhanQuyenDTO> pqList = new ArrayList<>();
        String query = "SELECT * FROM phanquyen";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pqList.add(new PhanQuyenDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("MaChucNang")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pqList;
    }

    public static ArrayList<PhanQuyenDTO> getPhanQuyenByMaChucVu(String maChucVu) {
        ArrayList<PhanQuyenDTO> pqList = new ArrayList<>();
        String query = "SELECT * FROM phanquyen WHERE MaChucVu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maChucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pqList.add(new PhanQuyenDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("MaChucNang")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pqList;
    }

    public boolean add(PhanQuyenDTO pqDTO) {
        int result = 0;
        String sql = "INSERT INTO phanquyen (MaChucVu, MaChucNang) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, pqDTO.getMaChucVu());   
            pstmt.setString(2, pqDTO.getMaChucNang());
            result = pstmt.executeUpdate();
            if(result > 0) 
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean deleteByMaChucVu(String maChucVu) {
        String sql = "DELETE FROM phanquyen WHERE MaChucVu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maChucVu);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }
}
