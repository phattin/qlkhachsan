package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Connection.DatabaseConnection;
import DTO.LoaiPhongDTO;
import DTO.PhongDTO;

public class PhongDAO {

    public ArrayList<PhongDTO> getALL() {
        ArrayList<PhongDTO> dsphong = new ArrayList<>();
        String sql = "SELECT * FROM phong ";
    
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                dsphong.add(new PhongDTO(
                    rs.getString("MaPhong"),
                    rs.getString("MaLoaiPhong"),        
                    rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách phòng: " + e.getMessage());
        }
        return dsphong;
    }
    

    
    public int add(PhongDTO obj) {
        int result = 0;
        String sql = "INSERT INTO phong (MaPhong, MaLoaiPhong, TrangThai) VALUES (?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, obj.getMaPhong());
            pstmt.setString(2, obj.getMaLoaiPhong());
            pstmt.setString(3, obj.getTrangThai());
    
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm phòng: " + e.getMessage());
        }
        return result;
    }    
    
    public int update(PhongDTO obj) {
        int result = 0;
        String sql = "UPDATE phong SET MaLoaiPhong=?, TrangThai=? WHERE MaPhong=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, obj.getMaLoaiPhong());  
            pstmt.setString(2, obj.getTrangThai());
            pstmt.setString(3, obj.getMaPhong());  

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật phòng: " + e.getMessage());
        }
        return result; 
    }

    public int delete(String maPhong) {  
        int result = 0;
        String sql = "DELETE FROM phong WHERE MaPhong=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maPhong); 

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa phòng: " + e.getMessage());
        }
        return result;
    }

    public PhongDTO getById(String maPhong) {
        PhongDTO phong = null;
        String sql = "SELECT * FROM phong WHERE MaPhong = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, maPhong);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                phong = new PhongDTO(
                    rs.getString("MaPhong"),
                    rs.getString("MaLoaiPhong"),
                    rs.getString("TrangThai")
                );
            }
    
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm phòng theo mã: " + e.getMessage());
        }
    
        return phong;
    }

    public LoaiPhongDTO getLoaiPhongByMaPhong(String maPhong) {
        String sql = "SELECT lp.MaLoaiPhong, lp.TenLoaiPhong, lp.SoGiuong, lp.GiaPhong " +
                     "FROM phong p JOIN loaiphong lp ON p.MaLoaiPhong = lp.MaLoaiPhong " +
                     "WHERE p.MaPhong = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, maPhong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new LoaiPhongDTO(
                            rs.getString("MaLoaiPhong"),
                            rs.getString("TenLoaiPhong"),
                            rs.getInt("SoGiuong"),
                            rs.getDouble("GiaPhong")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm loại phòng theo mã phòng: " + e.getMessage());
        }
    
        return null;
    }

}
