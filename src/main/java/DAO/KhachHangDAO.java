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
                    rs.getString("MaKhachHang"),
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

    public int add(KhachHangDTO obj) {
    int result = 0;
    String insertSQL = "INSERT INTO khachhang (MaKhachHang, HoTen, CCCD, SoDienThoai, Email, DiaChi) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {

        pstmtInsert.setString(1, obj.getMaKhachHang()); 
        pstmtInsert.setString(2, obj.getHoTen()); 
        pstmtInsert.setString(3, obj.getCCCD());
        pstmtInsert.setString(4, obj.getSDT()); 
        pstmtInsert.setString(5, obj.getEmail()); 
        pstmtInsert.setString(6, obj.getDiaChi()); 

        result = pstmtInsert.executeUpdate();

        if (result > 0) {
            System.out.println("Thêm khách hàng thành công!");
        } else {
            System.err.println("Không có dòng nào bị ảnh hưởng!");
        }

    } catch (SQLException e) {
        System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
    }
        return result; 
    }

    public int update(KhachHangDTO obj) {
        int result = 0;
        String sql = "UPDATE khachhang SET HoTen = ?, CCCD = ?, SoDienThoai = ?, Email = ?, DiaChi = ? WHERE MaKhachHang = ?";
    
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
        String sql = "DELETE FROM khachhang WHERE MaKhachHang = ?";
    
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
        String sql = "SELECT * FROM khachhang WHERE MaKhachHang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                return new KhachHangDTO(
                    rs.getString("MaKhachHang"),
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
        return null; 
    }
    
    public String increaseMaKH() {
        String maKH = null;
        String query = "SELECT MAX(MaKhachHang) AS MaxMaKhachHang FROM khachhang";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maKH = rs.getString("MaxMaKhachHang");
                if (maKH != null) {
                    int newMaKH = Integer.parseInt(maKH.substring(2)) + 1;
                    maKH = "KH" + String.format("%03d", newMaKH);
                } else {
                    maKH = "KH001"; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maKH;
    }
    // Thêm phương thức getBySDT để lấy khách hàng theo số điện thoại
    public KhachHangDTO getBySDT(String sdt) {
        if (sdt == null || sdt.isEmpty()) {
            return null;
        }
        
        KhachHangDTO khachHang = null;
        String sql = "SELECT * FROM khachhang WHERE SoDienThoai = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sdt);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                khachHang = new KhachHangDTO();
                khachHang.setMaKhachHang(rs.getString("MaKhachHang"));
                khachHang.setHoTen(rs.getString("HoTen"));
                khachHang.setCCCD(rs.getString("CCCD"));
                khachHang.setSDT(rs.getString("SoDienThoai"));
                khachHang.setEmail(rs.getString("Email"));
                khachHang.setDiaChi(rs.getString("DiaChi"));
            }
            
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return khachHang;
    }

}