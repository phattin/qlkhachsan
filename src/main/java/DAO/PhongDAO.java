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
        String sql = "SELECT p.MaPhong, p.MaLoaiPhong, lp.TenLoaiPhong, lp.SoGiuong, lp.GiaPhong, p.TrangThai " +
                     "FROM phong p " +
                     "JOIN loaiphong lp ON p.MaLoaiPhong = lp.MaLoaiPhong";
    
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                dsphong.add(new PhongDTO(
                    rs.getString("MaPhong"),
                    rs.getString("MaLoaiPhong"),
                    rs.getString("TenLoaiPhong"),
                    rs.getInt("SoGiuong"),  // Lấy tên loại phòng tự động
                    rs.getInt("GiaPhong"),         // Lấy giá tiền tự động
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
        String getLoaiPhongSQL = "SELECT TenLoaiPhong, SoGiuong, GiaPhong FROM loaiphong WHERE MaLoaiPhong = ?";
        String insertSQL = "INSERT INTO phong (MaPhong, MaLoaiPhong, TrangThai) VALUES (?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtGet = conn.prepareStatement(getLoaiPhongSQL);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {
    
            // Lấy TenLoaiPhong và GiaTien từ MaLoaiPhong
            pstmtGet.setString(1, obj.getMaLoaiPhong());
            ResultSet rs = pstmtGet.executeQuery();
            if (!rs.next()) {
                System.err.println("Lỗi: Không tìm thấy loại phòng!");
                return result;
            }
            String tenLoaiPhong = rs.getString("TenLoaiPhong");
            int soGiuong = rs.getInt("SoGiuong");
            int giaTien = rs.getInt("GiaPhong");
    
            // Chèn dữ liệu vào bảng phòng
            pstmtInsert.setString(1, obj.getMaPhong());
            pstmtInsert.setString(2, obj.getMaLoaiPhong());
            pstmtInsert.setString(3, obj.getTrangThai());
    
            result = pstmtInsert.executeUpdate();
    
            // Lưu TenLoaiPhong và GiaTien vào obj để hiển thị trên bảng
            obj.setTenLoaiPhong(tenLoaiPhong);
            obj.setSoGiuong(soGiuong);
            obj.setGiaTien(giaTien);
    
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
}
