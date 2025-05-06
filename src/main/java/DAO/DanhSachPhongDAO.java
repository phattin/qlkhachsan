package DAO;

import DTO.DanhSachPhongDTO;
import java.sql.*;
import java.util.ArrayList;

public class DanhSachPhongDAO {
    private Connection conn;

    public DanhSachPhongDAO() {
        try {
            // Kết nối cơ sở dữ liệu
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlkhachsan", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách tất cả các phòng đã đặt
    public ArrayList<DanhSachPhongDTO> getAllDanhSachPhong() {
        ArrayList<DanhSachPhongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM danhsachphong";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DanhSachPhongDTO(
                    rs.getString("MaDSP"),
                    rs.getString("MaDatPhong"),
                    rs.getString("MaPhong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thông tin danh sách phòng theo mã DSP
    public DanhSachPhongDTO getDanhSachPhongByMa(String maDSP) {
        String sql = "SELECT * FROM danhsachphong WHERE MaDSP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DanhSachPhongDTO(
                    rs.getString("MaDSP"),
                    rs.getString("MaDatPhong"),
                    rs.getString("MaPhong")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm phòng vào danh sách phòng đã đặt
    public boolean addDanhSachPhong(DanhSachPhongDTO dsp) {
        // Tạo mã tự động
        String maDSP = taoMaDSPMoi();
        dsp.setMaDSP(maDSP);
        
        String sql = "INSERT INTO danhsachphong (MaDSP, MaDatPhong, MaPhong) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dsp.getMaDSP());
            ps.setString(2, dsp.getMaDatPhong());
            ps.setString(3, dsp.getMaPhong());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi thêm phòng vào danh sách: " + e.getMessage());
            return false;
        }
    }

    // Tạo mã DSP mới
    private String taoMaDSPMoi() {
        // Truy vấn trực tiếp để lấy mã lớn nhất từ cơ sở dữ liệu
        String sql = "SELECT MAX(CAST(SUBSTRING(MaDSP, 4) AS UNSIGNED)) as maxId FROM danhsachphong WHERE MaDSP LIKE 'DSP%'";
        
        int maxID = 0;
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString("maxId") != null) {
                maxID = rs.getInt("maxId");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lấy mã DSP lớn nhất: " + e.getMessage());
            
            // Backup plan - dùng phương pháp cũ nếu truy vấn SQL thất bại
            ArrayList<DanhSachPhongDTO> danhSachPhong = getAllDanhSachPhong();
            
            // Tìm số lớn nhất hiện có
            for (DanhSachPhongDTO dsp : danhSachPhong) {
                String maDSP = dsp.getMaDSP();
                if (maDSP != null && maDSP.startsWith("DSP")) {
                    try {
                        int id = Integer.parseInt(maDSP.substring(3));
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
        
        // Định dạng mã với số 0 đứng trước nếu cần (ví dụ: DSP001, DSP002...)
        return String.format("DSP%03d", maxID);
    }

    // Cập nhật danh sách phòng
    public boolean updateDanhSachPhong(DanhSachPhongDTO dsp) {
        String sql = "UPDATE danhsachphong SET MaDatPhong = ?, MaPhong = ? WHERE MaDSP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dsp.getMaDatPhong());
            ps.setString(2, dsp.getMaPhong());
            ps.setString(3, dsp.getMaDSP());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa phòng khỏi danh sách
    public boolean deleteDanhSachPhong(String maDSP) {
        String sql = "DELETE FROM danhsachphong WHERE MaDSP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDSP);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tất cả phòng theo mã đặt phòng
    public boolean deleteDanhSachPhongByMaDatPhong(String maDatPhong) {
        String sql = "DELETE FROM danhsachphong WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi xóa phòng theo mã đặt phòng: " + e.getMessage());
            return false;
        }
    }

    // Lấy danh sách phòng theo mã đặt phòng
    public ArrayList<DanhSachPhongDTO> getDanhSachPhongByMaDatPhong(String maDatPhong) {
        ArrayList<DanhSachPhongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM danhsachphong WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DanhSachPhongDTO(
                    rs.getString("MaDSP"),
                    rs.getString("MaDatPhong"),
                    rs.getString("MaPhong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra phòng đã tồn tại trong danh sách nào chưa
    public boolean checkPhongExist(String maPhong) {
        String sql = "SELECT * FROM danhsachphong WHERE MaPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu phòng đã tồn tại trong danh sách
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Đếm số lượng phòng theo mã đặt phòng
    public int countPhongByMaDatPhong(String maDatPhong) {
        String sql = "SELECT COUNT(*) as total FROM danhsachphong WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
