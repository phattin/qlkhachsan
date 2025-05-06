package DAO;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.DichVuDTO;
import Connection.DatabaseConnection;

public class DichVuDAO {
    
    public ArrayList<DichVuDTO> getAllDichVu() {
        ArrayList<DichVuDTO> dsDichVu = new ArrayList<>();
        String sql = "SELECT * FROM dichvu";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dsDichVu.add(new DichVuDTO(
                    rs.getString("MaDV"),
                    rs.getString("TenDV"),
                    rs.getInt("GiaDV")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
        }
        return dsDichVu;
    }

    public DichVuDTO getDichVuByMa(String maDV) {
        String sql = "SELECT * FROM dichvu WHERE MaDV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maDV);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new DichVuDTO(
                    rs.getString("MaDV"),
                    rs.getString("TenDV"),
                    rs.getInt("GiaDV")
                );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin dịch vụ: " + e.getMessage());
        }
        return null;
    }

    public int insertDichVu(DichVuDTO obj) {
        int result = 0;
        String insertSQL = "INSERT INTO dichvu (MaDV, TenDV, GiaDV) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {

            pstmtInsert.setString(1, obj.getMaDichVu());
            pstmtInsert.setString(2, obj.getTenDichVu());
            pstmtInsert.setInt(3, obj.getGiaDichVu());

            result = pstmtInsert.executeUpdate();

            if (result > 0) {
                System.out.println("Thêm dịch vụ thành công!");
            } else {
                System.err.println("Không có dòng nào bị ảnh hưởng!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm dịch vụ: " + e.getMessage());
        }
        return result;
    }

    public int updateDichVu(DichVuDTO obj) {
        int result = 0;
        String sql = "UPDATE dichvu SET TenDV = ?, GiaDV = ? WHERE MaDV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, obj.getTenDichVu());
            pstmt.setInt(2, obj.getGiaDichVu());
            pstmt.setString(3, obj.getMaDichVu());

            result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Cập nhật dịch vụ thành công!");
            } else {
                System.err.println("Không có dòng nào được cập nhật!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật dịch vụ: " + e.getMessage());
        }

        return result;
    }

    public int deleteDichVu(String maDV) {
        int result = 0;
        String sql = "DELETE FROM dichvu WHERE MaDV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maDV);
            result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Xoá dịch vụ thành công!");
            } else {
                System.err.println("Không tìm thấy dịch vụ để xoá!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xoá dịch vụ: " + e.getMessage());
        }

        return result;
    }

    public String increaseMaDV() {
        String maDV = null;
        String query = "SELECT MAX(MaDV) AS MaxMaDV FROM dichvu";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maDV = rs.getString("MaxMaDV");
                if (maDV != null) {
                    int newMaDV = Integer.parseInt(maDV.substring(2)) + 1;
                    maDV = "DV" + String.format("%03d", newMaDV);
                } else {
                    maDV = "DV001"; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maDV;
    }
}