package DAO;

import java.sql.*;
import java.util.ArrayList;
import Connection.DatabaseConnection;
import DTO.LoaiPhongDTO;

public class LoaiPhongDAO {

    // Lấy tất cả loại phòng
    public ArrayList<LoaiPhongDTO> getALL() {
        ArrayList<LoaiPhongDTO> dsloaiphong = new ArrayList<>();
        String sql = "SELECT * FROM phong";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsloaiphong.add(new LoaiPhongDTO(
                        rs.getString("MaLoaiPhong"),
                        rs.getString("TenLoaiPhong"),
                        rs.getInt("SoGiuong"),
                        rs.getDouble("GiaPhong")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách loại phòng: " + e.getMessage());
        }

        return dsloaiphong;
    }

    // Thêm loại phòng mới
    public boolean add(LoaiPhongDTO lp) {
        String sql = "INSERT INTO phong (MaLoaiPhong, TenLoaiPhong, SoGiuong, GiaPhong) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, lp.getMaLoaiPhong());
            pstmt.setString(2, lp.getTenLoaiPhong());
            pstmt.setInt(3, lp.getSoGiuong());
            pstmt.setDouble(4, lp.getGiaPhong());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm loại phòng: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật loại phòng
    public boolean update(LoaiPhongDTO lp) {
        String sql = "UPDATE phong SET TenLoaiPhong = ?, SoGiuong = ?, GiaPhong = ? WHERE MaLoaiPhong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, lp.getTenLoaiPhong());
            pstmt.setInt(2, lp.getSoGiuong());
            pstmt.setDouble(3, lp.getGiaPhong());
            pstmt.setString(4, lp.getMaLoaiPhong());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật loại phòng: " + e.getMessage());
            return false;
        }
    }

    // Xoá loại phòng
    public boolean delete(String maLoaiPhong) {
        String sql = "DELETE FROM phong WHERE MaLoaiPhong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maLoaiPhong);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xoá loại phòng: " + e.getMessage());
            return false;
        }
    }

    // Tìm loại phòng theo mã
    public LoaiPhongDTO getById(String maLoaiPhong) {
        String sql = "SELECT * FROM LoaiPhong WHERE MaLoaiPhong = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, maLoaiPhong);
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
            System.err.println("Lỗi khi tìm loại phòng theo mã: " + e.getMessage());
        }
        return null;
    }
    
}
