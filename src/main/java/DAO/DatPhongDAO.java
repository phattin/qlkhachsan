package DAO;

import DTO.DatPhongDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatPhongDAO {
    private Connection conn;

    public DatPhongDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlkhachsan", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean themDatPhong(DatPhongDTO datPhong) {
        String query = "INSERT INTO datphong (MaDatPhong, MaPhong, MaKH, NgayNhanPhong, NgayTraPhong) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, datPhong.getMaDatPhong());
            ps.setString(2, datPhong.getMaPhong());
            ps.setString(3, datPhong.getMaKH());
            ps.setDate(4, new java.sql.Date(datPhong.getNgayNhanPhong().getTime()));
            ps.setDate(5, new java.sql.Date(datPhong.getNgayTraPhong().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DatPhongDTO> layDanhSachDatPhong(String maPhong) {
        List<DatPhongDTO> danhSach = new ArrayList<>();
        String query = "SELECT * FROM datphong WHERE maPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DatPhongDTO dp = new DatPhongDTO(
                    rs.getString("maDatPhong"),
                    rs.getString("maPhong"),
                    rs.getString("maKH"),
                    rs.getDate("ngayNhanPhong"),
                    rs.getDate("ngayTraPhong")
                );
                danhSach.add(dp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    public boolean xoaDatPhong(String maDatPhong) {
        String query = "DELETE FROM datphong WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maDatPhong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
