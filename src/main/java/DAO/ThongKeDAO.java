package DAO;

import DTO.ThongKeDTO;
import java.sql.*;
import java.util.ArrayList;

import Connection.DatabaseConnection;

public class ThongKeDAO {
    private Connection conn;

    public ThongKeDAO() {
        this.conn = DatabaseConnection.getConnection(); // Đảm bảo Database.java có getConnection()
    }

    public ArrayList<ThongKeDTO> layThongKeTheoThang(int nam) {
        ArrayList<ThongKeDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT MONTH(NgayTao) AS Thang, SUM(TongTien) AS DoanhThu FROM hoadon WHERE YEAR(NgayTao) = ? AND TrangThai = 'Đã Thanh Toán' GROUP BY Thang";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                dsThongKe.add(new ThongKeDTO(rs.getInt("Thang"), rs.getDouble("DoanhThu")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsThongKe;
    }

    public ArrayList<ThongKeDTO> layThongKeTheoNam() {
        ArrayList<ThongKeDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT YEAR(NgayTao) AS Nam, SUM(TongTien) AS DoanhThu FROM hoadon WHERE TrangThai = 'Đã Thanh Toán' GROUP BY Nam";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dsThongKe.add(new ThongKeDTO(rs.getInt("Nam"), rs.getDouble("DoanhThu")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsThongKe;
    }
}