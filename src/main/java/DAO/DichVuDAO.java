package DAO;

import DTO.DichVuDTO;
import java.sql.*;
import java.util.ArrayList;

public class DichVuDAO {
    private Connection conn;

    public DichVuDAO() {
        try {
            // Giả sử bạn đã có class DBConnect, bạn có thể thay thế theo cách bạn connect
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlkhachsan", "root", "");
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    
}
