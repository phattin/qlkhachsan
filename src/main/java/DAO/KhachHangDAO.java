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

<<<<<<< HEAD
    public int add(KhachHangDTO khachHang) {
=======
    public int add(KhachHangDTO obj) {
>>>>>>> origin/Nhat2
    int result = 0;
    String insertSQL = "INSERT INTO khachhang (MaKH, HoTen, CCCD, SoDienThoai, Email, DiaChi) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {

<<<<<<< HEAD
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
=======
        pstmtInsert.setString(1, obj.getMaKhachHang()); 
        pstmtInsert.setString(2, obj.getHoTen()); 
        pstmtInsert.setString(3, obj.getCCCD());
        pstmtInsert.setString(4, obj.getSDT()); 
        pstmtInsert.setString(5, obj.getEmail()); 
        pstmtInsert.setString(6, obj.getDiaChi()); 

        result = pstmtInsert.executeUpdate();

>>>>>>> origin/Nhat2
        if (result > 0) {
            System.out.println("Thêm khách hàng thành công!");
        } else {
            System.err.println("Không có dòng nào bị ảnh hưởng!");
        }

    } catch (SQLException e) {
        System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
    }
<<<<<<< HEAD

    return result;  // Trả về số dòng bị ảnh hưởng, nếu 0 có thể hiểu là không có thay đổi
}

    
    
    





=======
        return result; 
    }

    public int update(KhachHangDTO obj) {
        int result = 0;
        String sql = "UPDATE khachhang SET HoTen = ?, CCCD = ?, SoDienThoai = ?, Email = ?, DiaChi = ? WHERE MaKH = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, obj.getHoTen());
            pstmt.setString(2, obj.getCCCD());
            pstmt.setString(3, obj.getSDT());
            pstmt.setString(4, obj.getEmail());
            pstmt.setString(5, obj.getDiaChi());
            pstmt.setString(6, obj.getMaKhachHang());
    
            result = pstmt.executeUpdate();
    
            if (result > 0) {
                System.out.println("Cập nhật khách hàng thành công!");
            } else {
                System.err.println("Không có dòng nào được cập nhật!");
            }
    
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
    
        return result;
    }
    
    public int delete(String maKH) {
        int result = 0;
        String sql = "DELETE FROM khachhang WHERE MaKH = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, maKH);
            result = pstmt.executeUpdate();
    
            if (result > 0) {
                System.out.println("Xoá khách hàng thành công!");
            } else {
                System.err.println("Không tìm thấy khách hàng để xoá!");
            }
    
        } catch (SQLException e) {
            System.err.println("Lỗi khi xoá khách hàng: " + e.getMessage());
        }
    
        return result;
    }
    
    public KhachHangDTO getById(String maKH) {
        String sql = "SELECT * FROM khachhang WHERE MaKH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                return new KhachHangDTO(
                    rs.getString("MaKH"),
                    rs.getString("HoTen"),
                    rs.getString("CCCD"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("DiaChi")
                );
            }
    
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
        }
        return null; // Trả về null nếu không tìm thấy khách hàng
    }
    
    
>>>>>>> origin/Nhat2
}
