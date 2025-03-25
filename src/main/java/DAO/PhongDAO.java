package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.PhongDTO;

public class PhongDAO {

    public ArrayList<PhongDTO> getALL() {
        ArrayList<PhongDTO> dsphong = new ArrayList<>();
        String sql = "SELECT * FROM phong";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsphong.add(new PhongDTO(
                    rs.getInt("MaPhong"),
                    rs.getInt("MaLoaiPhong"),
                    rs.getString("TrangThai")                   
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsphong;
    }

    
    public int add(PhongDTO obj) {
        int result = 0;
        String sql = "INSERT INTO phong (MaPhong, MaLoaiPhong, TrangThai) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, obj.getMaPhong());
            pstmt.setInt(2, obj.getMaLoaiPhong());           
            pstmt.setString(3, obj.getTrangThai());
            
            result = pstmt.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result; 
    }

    public int update(PhongDTO obj) {
        int result = 0;
        String sql = "UPDATE phong SET MaLoaiPhong=?, TrangThai=? WHERE MaPhong=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, obj.getMaLoaiPhong());           
            pstmt.setString(2, obj.getTrangThai());
            pstmt.setInt(3, obj.getMaPhong());

            result = pstmt.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result; 
    }

    public int delete(int MaPhong) {  
        int result = 0;
        String sql = "DELETE FROM phong WHERE MaPhong=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, MaPhong); 

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
