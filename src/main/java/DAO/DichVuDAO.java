package DAO;

import DTO.DichVuDTO;
import java.sql.*;
import java.util.ArrayList;

public class DichVuDAO {
    private Connection conn;

    public DichVuDAO() {
        try {
     
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlkhachsan", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DichVuDTO> getAllDichVu() {
        ArrayList<DichVuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM dichvu";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DichVuDTO(
                        rs.getString("MaDV"),
                        rs.getString("TenDV"),
                        rs.getInt("GiaDV")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public DichVuDTO getDichVuByMa(String maDV) {
        String sql = "SELECT * FROM dichvu WHERE MaDV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DichVuDTO(
                        rs.getString("MaDV"),
                        rs.getString("TenDV"),
                        rs.getInt("GiaDV")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addDichVu(DichVuDTO dichVu) {
        String sql = "INSERT INTO dichvu (MaDV, TenDV, GiaDV) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dichVu.getMaDV());
            ps.setString(2, dichVu.getTenDV());
            ps.setInt(3, dichVu.getGiaDV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDichVu(DichVuDTO dichVu) {
        String sql = "UPDATE dichvu SET TenDV = ?, GiaDV = ? WHERE MaDV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dichVu.getTenDV());
            ps.setInt(2, dichVu.getGiaDV());
            ps.setString(3, dichVu.getMaDV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDichVu(String maDV) {
        String sql = "DELETE FROM dichvu WHERE MaDV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
