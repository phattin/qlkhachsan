package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.TaiKhoanDTO;

public class TaiKhoanDAO {
    public static ArrayList<TaiKhoanDTO> getAllTaiKhoan() {
        ArrayList<TaiKhoanDTO> tkList = new ArrayList<>();
        String query = "SELECT * FROM taikhoan WHERE TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tkList.add(new TaiKhoanDTO(
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("MaNhanVien"),
                        rs.getString("MaChucVu"),
                        rs.getString("TrangThai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tkList;
    }

    public static TaiKhoanDTO getTaiKhoan(String tenDangNhap, String matkhau) {
        String query = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matkhau);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("MaNhanVien"),
                        rs.getString("MaChucVu"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }

    public boolean add(TaiKhoanDTO tkDTO) {
        int result = 0;
        String sql = "INSERT INTO taikhoan (TenDangNhap, MatKhau, MaNhanVien, MaChucVu, TrangThai) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tkDTO.getTenDangNhap());
            pstmt.setString(2, tkDTO.getMatKhau());   
            pstmt.setString(3, tkDTO.getMaNhanVien());   
            pstmt.setString(4, tkDTO.getMaChucVu());   
            pstmt.setString(5, tkDTO.getTrangThai());
            result = pstmt.executeUpdate();
            if(result > 0) 
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean update(TaiKhoanDTO tkDTO) {
        int result = 0;
        String sql = "UPDATE taikhoan SET MatKhau=?, MaNhanVien=?, MaChucVu=? WHERE TenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, tkDTO.getMatKhau());   
                 pstmt.setString(2, tkDTO.getMaNhanVien());   
                 pstmt.setString(3, tkDTO.getMaChucVu());   
            pstmt.setString(4, tkDTO.getTenDangNhap());
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
        String sql = "UPDATE taikhoan SET TrangThai = ? WHERE MaNhanVien=?";

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

    public boolean isUsernameExists(String username) {
        String sql = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Nếu có kết quả trả về thì username đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameUpdateExists(String username, String maNhanVien) {
        String sql = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND MaNhanVien != ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, maNhanVien);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Nếu có kết quả trả về thì username đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<TaiKhoanDTO> search(String cbTimKiem, String txTimKiem, String chucVu) {

        ArrayList<TaiKhoanDTO> result = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM taikhoan JOIN nhanvien ON taikhoan.MaNhanVien = nhanvien.MaNhanVien WHERE taikhoan.TrangThai = 'Hiện'");
        ArrayList<Object> parameters = new ArrayList<>();

        // Tìm kiếm theo combobox
        if (cbTimKiem != null && !txTimKiem.isEmpty()) {
            txTimKiem = "%" + txTimKiem + "%";
            switch (cbTimKiem) {
                case "Tên đăng nhập":
                    query.append(" AND TenDangNhap LIKE ?");
                    parameters.add(txTimKiem);
                    break;
                case "Họ tên":
                    //Kiểm tra cứu theo tên nhân viên từ bảng nhân viên
                    query.append(" AND HoTen LIKE ?");
                    parameters.add(txTimKiem);
                    break;
            }
        }

        // Giới tính
        if (!chucVu.equals("Tất cả")) {
            query.append(" AND MaChucVu = ?");
            String maChucVu = ChucVuDAO.getMaChucVuByTen(chucVu);
            parameters.add(maChucVu);
        }

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            // Gán tham số vào dấu ?
            for (int i = 0; i < parameters.size(); i++) {
                Object param = parameters.get(i);
                if (param instanceof String)
                    stmt.setString(i + 1, (String) param);
                else if (param instanceof Double)
                    stmt.setDouble(i + 1, (Double) param);
            }

            // Thực thi và đọc kết quả
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new TaiKhoanDTO(
                            rs.getString("TenDangNhap"),
                            rs.getString("MatKhau"),
                            rs.getString("MaNhanVien"),
                            rs.getString("MaChucVu"),
                            rs.getString("TrangThai")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Query: " + query.toString());
        System.out.println("Parameters: " + parameters.toString());
        return result;
    }

    private static String maNVDangNhap = null;

    public static void setMaNVDangDN(String maNV) {
        maNVDangNhap = maNV;
    }

    public static String getMaNVDangDN() {
        return maNVDangNhap;
    }
}
