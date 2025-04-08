package DAO;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.KhachHangDTO;
import Connection.DatabaseConnection;

public class KhachHangDAO {
    
    public ArrayList<KhachHangDTO> getAll(){
        ArrayList<KhachHangDTO> dskhachhang = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dskhachhang.add(new KhachHangDTO(
                    rs.getString("MaKH"),
                    rs.getString("HoTen"),
                    rs.getString("CCCD"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("DiaChi")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return dskhachhang;
    }

    public int add(KhachHangDTO khachHang) {
    int result = 0;
    String insertSQL = "INSERT INTO khachhang (MaKH, HoTen, CCCD, SoDienThoai, Email, DiaChi) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {

        // Chèn dữ liệu vào bảng khách hàng
        pstmtInsert.setString(1, khachHang.getMaKhachHang()); // MaKH
        pstmtInsert.setString(2, khachHang.getHoTen()); // HoTen
        pstmtInsert.setString(3, khachHang.getCCCD()); // CCCD
        pstmtInsert.setString(4, khachHang.getSDT()); // SoDienThoai
        pstmtInsert.setString(5, khachHang.getEmail()); // Email
        pstmtInsert.setString(6, khachHang.getDiaChi()); // DiaChi

        // Thực thi câu lệnh SQL và lấy số dòng bị ảnh hưởng
        result = pstmtInsert.executeUpdate();

        // Nếu có ít nhất một dòng bị ảnh hưởng, có thể xác nhận thêm (trả về 1 hoặc số dòng bị ảnh hưởng)
        if (result > 0) {
            System.out.println("Thêm khách hàng thành công!");
        } else {
            System.err.println("Không có dòng nào bị ảnh hưởng!");
        }

    } catch (SQLException e) {
        System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
    }

    return result;  // Trả về số dòng bị ảnh hưởng, nếu 0 có thể hiểu là không có thay đổi
}

    
    
    





}
