package DAO;

import DTO.HoaDonDTO;
import Connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HoaDonDAO {

    public static ArrayList<HoaDonDTO> getAllHoaDon() {
        ArrayList<HoaDonDTO> dshoadon = new ArrayList<>();
        String query = "SELECT * FROM hoadon";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    dshoadon.add(new HoaDonDTO(
                        rs.getString("MaHoaDon"),
                        rs.getString("MaDatPhong"),
                        rs.getString("MaNhanVien"),
                        rs.getDate("NgayTao"),
                        rs.getDouble("TongTien"),
                        rs.getDouble("TienTra"),
                        rs.getDouble("TienThua"),
                        rs.getString("TrangThai")

                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dshoadon;
    }

    public static HoaDonDTO getHoaDonByMa(String maHD) {
        String query = "SELECT * FROM hoadon WHERE MaHoaDon = ? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maHD);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new HoaDonDTO(
                        rs.getString("MaHoaDon"),
                        rs.getString("MaDatPhong"),
                        rs.getString("MaNhanVien"),
                        rs.getDate("NgayTao"),
                        rs.getDouble("TongTien"),
                        rs.getDouble("TienTra"),
                        rs.getDouble("TienThua"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    

    public boolean themHoaDon(HoaDonDTO hoaDon) {
        String query = "INSERT INTO hoadon (MaHoaDon, MaDatPhong, MaNhanVien, NgayTao, TongTien, TienTra, TienThua, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, hoaDon.getMaHoaDon());
            pstmt.setString(2, hoaDon.getMaDatPhong());
            pstmt.setString(3, hoaDon.getMaNhanVien());
            pstmt.setDate(4, new java.sql.Date(hoaDon.getNgayTao().getTime()));
            pstmt.setDouble(5, hoaDon.getTongTien());
            pstmt.setDouble(6, hoaDon.getTienTra());
            pstmt.setDouble(7, hoaDon.getTienThua());
            pstmt.setString(8, hoaDon.getTrangThai());


            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm hóa đơn: " + e.getMessage());
            return false;
        }
    } 

    public boolean capnhatHoaDon(HoaDonDTO hoaDon) {
        String query = "UPDATE hoadon SET MaDatPhong = ?, MaNhanVien = ?, NgayTao = ?, TongTien = ?, TienTra = ?, TienThua = ?, TrangThai = ? WHERE MaHoaDon = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

                 pstmt.setString(1, hoaDon.getMaDatPhong());
                 pstmt.setString(2, hoaDon.getMaNhanVien());
                 pstmt.setDate(3, new java.sql.Date(hoaDon.getNgayTao().getTime()));
                 pstmt.setDouble(4, hoaDon.getTongTien());
                 pstmt.setDouble(5, hoaDon.getTienTra());
                 pstmt.setDouble(6, hoaDon.getTienThua());
                 pstmt.setString(7, hoaDon.getTrangThai());
                 pstmt.setString(8, hoaDon.getMaHoaDon());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaHoaDon(String maHoaDon) {
        String query = "UPDATE hoadon SET trangThaiXoa = 1 WHERE MaHoaDon = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maHoaDon);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa hóa đơn: " + e.getMessage());
            return false;
        }
    }

    public static HoaDonDTO getHoaDonByMaDatPhong(String maDatPhong) {
        String query = "SELECT * FROM hoadon WHERE MaDatPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maDatPhong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new HoaDonDTO(
                        rs.getString("MaHoaDon"),
                        rs.getString("MaDatPhong"),
                        rs.getString("MaNhanVien"),
                        rs.getDate("NgayTao"),
                        rs.getInt("TongTien"),
                        rs.getInt("TienTra"),
                        rs.getInt("TienThua"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String tangMaHoaDon() {
        String maHD = null;
        String query = "SELECT MAX(MaHoaDon) AS MaxMaHD FROM hoadon";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                maHD = rs.getString("MaxMaHD");
                if (maHD != null) {
                    int newMaHD = Integer.parseInt(maHD.substring(2)) + 1;
                    maHD = "HD" + String.format("%03d", newMaHD);
                } else {
                    maHD = "HD001";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHD;
    }

    public ArrayList<HoaDonDTO> search(String cbxSearch, String txSearch, String TrangThai, String dateFrom, String dateTo) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM hoadon WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();
    
        if (cbxSearch != null && !txSearch.isEmpty()) {
            txSearch = "%" + txSearch + "%";
            switch (cbxSearch) {
                case "Mã Hóa Đơn":
                    query.append(" AND MaHoaDon LIKE ?");
                    params.add(txSearch);
                    break;
                case "Mã Nhân Viên":
                    query.append(" AND MaNhanVien LIKE ?");
                    params.add(txSearch);
                    break;
                case "Ngày Tạo":
                    query.append(" AND NgayTao = ?");
                    params.add(java.sql.Date.valueOf(txSearch));
                    break;
            }
        }
    
        if (TrangThai != null && !TrangThai.isEmpty()) {
            query.append(" AND TrangThai = ?");
            params.add(TrangThai);
        }
    
        if (dateFrom != null && !dateFrom.isEmpty()) {
            query.append(" AND NgayTao >= ?");
            params.add(java.sql.Date.valueOf(dateFrom));
        }
    
        if (dateTo != null && !dateTo.isEmpty()) {
            query.append(" AND NgayTao <= ?");
            params.add(java.sql.Date.valueOf(dateTo));
        }
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
    
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String)
                    stmt.setString(i + 1, (String) param);
                else if (param instanceof Double)
                    stmt.setDouble(i + 1, (Double) param);
                else if (param instanceof java.sql.Date)
                    stmt.setDate(i + 1, (java.sql.Date) param);
            }
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new HoaDonDTO(
                        rs.getString("MaHoaDon"),
                        rs.getString("MaDatPhong"),
                        rs.getString("MaNhanVien"),
                        rs.getDate("NgayTao"),
                        rs.getDouble("TongTien"),
                        rs.getDouble("TienTra"),
                        rs.getDouble("TienThua"),
                        rs.getString("TrangThai")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        System.out.println("Query: " + query.toString());
        System.out.println("Params: " + params.toString());
    
        return result;
    }
}