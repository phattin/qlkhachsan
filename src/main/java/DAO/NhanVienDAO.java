package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.NhanVienDTO;

public class NhanVienDAO {
    public static ArrayList<NhanVienDTO> getAllNhanVien() {
        ArrayList<NhanVienDTO> nvList = new ArrayList<>();
        String query = "SELECT * FROM nhanvien WHERE TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nvList.add(new NhanVienDTO(
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getString("Email"),
                        rs.getString("DiaChi"),
                        rs.getLong("Luong"),
                        rs.getString("NgayNhanViec"),
                        rs.getString("TrangThai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nvList;
    }

    public static NhanVienDTO getNhanVienByMa(String maNhanVien) {
        String query = "SELECT * FROM nhanvien WHERE TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new NhanVienDTO(
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDIenThoai"),
                        rs.getString("Email"),
                        rs.getString("DiaChi"),
                        rs.getLong("Luong"),
                        rs.getString("NgayNhanViec"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }

    public boolean add(NhanVienDTO nvDTO) {
        int result = 0;
        String sql = "INSERT INTO nhanvien (MaNV, HoTen, GioiTinh, SoDienThoai, Email, DiaChi, Luong, NgayNhanViec TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nvDTO.getMaNhanVien());
            pstmt.setString(2, nvDTO.getHoTen());
            pstmt.setString(3, nvDTO.getGioiTinh());
            pstmt.setString(4, nvDTO.getSDT());
            pstmt.setString(5, nvDTO.getEmail());
            pstmt.setString(6, nvDTO.getDiaChi());
            pstmt.setLong(7, nvDTO.getLuong());
            pstmt.setString(8, nvDTO.getNgayNhanViec());
            pstmt.setString(9, nvDTO.getTrangThai());
            
            result = pstmt.executeUpdate();
            if(result > 0) 
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean update(NhanVienDTO nvDTO) {
        int result = 0;
        String sql = "UPDATE nhanvien SET HoTen=?, GioiTinh=?, SoDienThoai=?, Email=?, DiaChi=?, Luong=? WHERE MaNV=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nvDTO.getHoTen());
            pstmt.setString(2, nvDTO.getGioiTinh());
            pstmt.setString(3, nvDTO.getSDT());
            pstmt.setString(4, nvDTO.getEmail());
            pstmt.setString(5, nvDTO.getDiaChi());
            pstmt.setLong(6, nvDTO.getLuong());
            pstmt.setString(7, nvDTO.getMaNhanVien());
            
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean delete(String maNhanVien) {
        int result = 0;
        String sql = "UPDATE nhanvien SET TrangThai = ? WHERE MaNV=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Ẩn"); 
            pstmt.setString(2, maNhanVien);
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public String increaseMaNV() {
        String maNV = null;
        String query = "SELECT MAX(MaNV) AS MaxMaNV FROM nhanvien";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maNV = rs.getString("MaxMaNV");
                if (maNV != null) {
                    int newMaNV = Integer.parseInt(maNV.substring(2)) + 1;
                    maNV = "NV" + String.format("%03d", newMaNV);
                } else {
                    maNV = "NV001"; // Nếu không có nhân viên nào, bắt đầu từ NV001
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maNV;
    }

    public boolean phoneNumberExists(String phoneNumber) {
        String query = "SELECT * FROM nhanvien WHERE SoDienThoai = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về, tức là số điện thoại đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Không tìm thấy số điện thoại
    }
    public boolean emailExists(String email) {
        String query = "SELECT * FROM nhanvien WHERE Email = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về, tức là email đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Không tìm thấy email
    }
}
