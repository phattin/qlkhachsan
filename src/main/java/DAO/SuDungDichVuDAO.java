package DAO;

import DTO.SuDungDichVuDTO;
import java.sql.*;
import java.util.ArrayList;

public class SuDungDichVuDAO {
    private Connection conn;

    public SuDungDichVuDAO() {
        try {
            // Kết nối cơ sở dữ liệu
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlkhachsan", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách tất cả các dịch vụ đã sử dụng
    public ArrayList<SuDungDichVuDTO> getAllSuDungDichVu() {
        ArrayList<SuDungDichVuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sudungdichvu";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new SuDungDichVuDTO(
                    String.valueOf(rs.getInt("maSDDV")), // Chuyển thành String
                    rs.getString("maDP"),
                    rs.getString("maDv"),
                    rs.getInt("soLuong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thông tin dịch vụ đã sử dụng theo mã
    public SuDungDichVuDTO getSuDungDichVuByMa(String maSDDV) {
        String sql = "SELECT * FROM sudungdichvu WHERE maSDDV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSDDV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SuDungDichVuDTO(
                    String.valueOf(rs.getInt("maSDDV")), // Chuyển thành String
                    rs.getString("maDP"),
                    rs.getString("maDv"),
                    rs.getInt("soLuong")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Sửa phương thức thêm dịch vụ đã sử dụng
    public boolean addSuDungDichVu(SuDungDichVuDTO sddv) {
        // Tạo mã tự động
        String maSDDV = taoMaSuDungDichVuMoi();
        sddv.setMaSDDV(maSDDV);
        
        String sql = "INSERT INTO sudungdichvu (MaSuDungDV, MaDatPhong, MaDV, SoLuong) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sddv.getMaSDDV());
            ps.setString(2, sddv.getMaDP());
            ps.setString(3, sddv.getMaDv());
            ps.setInt(4, sddv.getSoLuong());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi thêm dịch vụ: " + e.getMessage());
            return false;
        }
    }

    // Thêm phương thức tạo mã SDDV mới
    private String taoMaSuDungDichVuMoi() {
        // Truy vấn trực tiếp để lấy mã lớn nhất từ cơ sở dữ liệu
        String sql = "SELECT MAX(CAST(SUBSTRING(MaSuDungDV, 3) AS UNSIGNED)) as maxId FROM sudungdichvu WHERE MaSuDungDV LIKE 'SD%'";
        
        int maxID = 0;
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString("maxId") != null) {
                maxID = rs.getInt("maxId");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lấy mã SDDV lớn nhất: " + e.getMessage());
            
            // Backup plan - dùng phương pháp cũ nếu truy vấn SQL thất bại
            ArrayList<SuDungDichVuDTO> danhSachSuDungDV = getAllSuDungDichVu();
            
            // Tìm số lớn nhất hiện có
            for (SuDungDichVuDTO sddv : danhSachSuDungDV) {
                String maSDDV = sddv.getMaSDDV();
                if (maSDDV != null && maSDDV.startsWith("SD")) {
                    try {
                        int id = Integer.parseInt(maSDDV.substring(2));
                        if (id > maxID) {
                            maxID = id;
                        }
                    } catch (NumberFormatException ex) {
                        // Bỏ qua các mã không đúng định dạng
                    }
                }
            }
        }
        
        // Tăng số lớn nhất lên 1
        maxID++;
        
        // Định dạng mã với số 0 đứng trước nếu cần (ví dụ: SD001, SD002...)
        return String.format("SD%03d", maxID);
    }

    // Cập nhật dịch vụ đã sử dụng
    public boolean updateSuDungDichVu(SuDungDichVuDTO sddv) {
        String sql = "UPDATE sudungdichvu SET maDP = ?, maDv = ?, soLuong = ? WHERE maSDDV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sddv.getMaDP());
            ps.setString(2, sddv.getMaDv());
            ps.setInt(3, sddv.getSoLuong());
            ps.setString(4, sddv.getMaSDDV());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa dịch vụ đã sử dụng
    public boolean deleteSuDungDichVu(String maSDDV) {
        String sql = "DELETE FROM sudungdichvu WHERE maSDDV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSDDV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tất cả các dịch vụ đã sử dụng theo mã đặt phòng
    public boolean deleteSuDungDichVuByMaDP(String maDatPhong) {
        String sql = "DELETE FROM sudungdichvu WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi xóa dịch vụ theo mã đặt phòng: " + e.getMessage());
            return false;
        }
    }

    // Thêm phương thức để lấy dịch vụ đã sử dụng theo mã đặt phòng
    public ArrayList<SuDungDichVuDTO> getSuDungDichVuByMaDP(String maDatPhong) {
        ArrayList<SuDungDichVuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sudungdichvu WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SuDungDichVuDTO(
                    rs.getString("MaSuDungDV"),
                    rs.getString("MaDatPhong"),
                    rs.getString("MaDV"),
                    rs.getInt("SoLuong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}