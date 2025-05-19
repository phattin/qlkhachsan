package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.ChucVuDTO;

public class ChucVuDAO {
    public static ArrayList<ChucVuDTO> getAllChucVu() {
        ArrayList<ChucVuDTO> cvList = new ArrayList<>();
        String query = "SELECT * FROM chucvu WHERE TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cvList.add(new ChucVuDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("TrangThai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cvList;
    }

    public static ChucVuDTO getChucVuByMa(String maChucVu) {
        String query = "SELECT * FROM chucvu WHERE MaChucVu = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maChucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ChucVuDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }

    public static String getMaChucVuByTen(String tenChucVu) {
        String query = "SELECT * FROM chucvu WHERE TenChucVu = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenChucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("MaChucVu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }

    public static ChucVuDTO getChucVuByMaNhanVien(String maNhanVien) {
        String query = "SELECT * FROM chucvu"
        + " JOIN taikhoan ON chucvu.MaChucVu = taikhoan.MaChucVu"
        + " JOIN nhanvien ON taikhoan.MaNhanVien = nhanvien.MaNhanVien"
        + " WHERE taikhoan.MaNhanVien = ? AND taikhoan.TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ChucVuDTO(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }


    public boolean add(ChucVuDTO cvDTO) {
        int result = 0;
        String sql = "INSERT INTO chucvu (MaChucVu, TenChucVu, TrangThai) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, cvDTO.getMaChucVu());   
            pstmt.setString(2, cvDTO.getTenChucVu());   
            pstmt.setString(3, cvDTO.getTrangThai());
            result = pstmt.executeUpdate();
            if(result > 0) 
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean update(ChucVuDTO cvDTO) {
        int result = 0;
        String sql = "UPDATE chucvu SET TenChucVu=? WHERE MaChucVu=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, cvDTO.getTenChucVu());
                 pstmt.setString(2, cvDTO.getMaChucVu());   
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean delete(String maCV) {
        int result = 0;
        String sql = "UPDATE chucvu SET TrangThai = ? WHERE MaChucVu = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Ẩn"); 
            pstmt.setString(2, maCV);
            result = pstmt.executeUpdate(); 
            if(result > 0) 
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public String increaseMaCV() {
        String maCV = null;
        String query = "SELECT MAX(MaChucVu) AS MaxMaCV FROM chucvu";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maCV = rs.getString("MaxMaCV");
                if (maCV != null) {
                    int newMaCV = Integer.parseInt(maCV.substring(2)) + 1;
                    maCV = "CV" + String.format("%03d", newMaCV);
                } else {
                    maCV = "CV001"; // Nếu không có chức vụ nào, bắt đầu từ CV001
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maCV;
    }

    public boolean TenChucVuExists(String tenCV) {
        String query = "SELECT * FROM chucvu WHERE TenChucVu = ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenCV);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về, tức là tên chức vụ đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Không tìm thấy tên chức vụ
    }

    public boolean TenChucVuUpdateExists(String maCV, String tenCV) {
        String query = "SELECT * FROM chucvu WHERE TenChucVu = ? AND MaChucVu != ? AND TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenCV);
            stmt.setString(2, maCV);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về, tức là tên chức vụ đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Không tìm thấy tên chức vụ
    }

    public boolean hasNhanVienOfChucVu(String maCV){
        String query = "SELECT nhanvien.MaNhanVien FROM nhanvien "
        + "JOIN taikhoan ON taikhoan.MaNhanVien = nhanvien.MaNhanVien "
        + "JOIN chucvu ON taikhoan.MaChucVu = chucvu.MaChucVu "
        + "WHERE chucvu.MaChucVu = ? AND nhanvien.TrangThai = 'Hiện' AND chucvu.TrangThai = 'Hiện'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maCV);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về, tức là tên chức vụ đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Không tìm thấy tên chức vụ
    }
    public ArrayList<ChucVuDTO> search(String cbTimKiem, String txTimKiem) {

        ArrayList<ChucVuDTO> result = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM chucvu WHERE TrangThai = 'Hiện'");
        ArrayList<Object> parameters = new ArrayList<>();

        // Tìm kiếm theo combobox
        if (cbTimKiem != null && !txTimKiem.isEmpty()) {
            txTimKiem = "%" + txTimKiem + "%";
            switch (cbTimKiem) {
                case "Mã chức vụ":
                    query.append(" AND MaChucVu LIKE ?");
                    parameters.add(txTimKiem);
                    break;
                case "Tên chức vụ":
                    query.append(" AND TenChucVu LIKE ?");
                    parameters.add(txTimKiem);
                    break;
            }
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
                    result.add(new ChucVuDTO(
                            rs.getString("MaChucVu"),
                            rs.getString("TenChucVu"),
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
}
