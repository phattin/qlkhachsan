package DAO;

import DTO.HoaDonDTo;
import java.sql.*;
import java.util.ArrayList;

public class HoaDonDAO {
    private Connection conn;

    public HoaDonDAO(Connection conn) {
        this.conn = conn;
    }
    public ArrayList<HoaDonDTO> getAllHoaDon() {
        ArrayList<HoaDonDTO> dshoadon = new ArrayList<>();
        String query = "SELECT * FROM hoadon";

        try {
            Statement stmt = conn,createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                dshoadon.add(new HoaDonDTO(
                    rs.getString("MaHoaDon"),
                    rs.getString("MsNhanVien"),
                    rs.getDate("NgayTao"),
                    rs.getDouble("TongTien"),
                    rs.getDouble("TienTra"),
                    rs.getDouble("TienThua"),
                    rs.getString("TrangThai")
                ));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách hóa đơn:" + e.getMessage())
        }

        return dshoadon;
    }

}