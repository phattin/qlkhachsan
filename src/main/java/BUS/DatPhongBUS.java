package BUS;

import DAO.DatPhongDAO;
import DTO.DatPhongDTO;
<<<<<<< HEAD
import java.util.List;

public class DatPhongBUS {
=======

import java.util.ArrayList;
import java.util.List;

public class DatPhongBUS {

>>>>>>> origin/Nhat2
    private DatPhongDAO datPhongDAO;

    public DatPhongBUS() {
        datPhongDAO = new DatPhongDAO();
    }

<<<<<<< HEAD
    public boolean themDatPhong(DatPhongDTO datPhong) {
        if (datPhong == null || datPhong.getMaDatPhong().isEmpty() || 
            datPhong.getMaPhong().isEmpty() || datPhong.getMaKH().isEmpty() ||
            datPhong.getNgayNhanPhong() == null || datPhong.getNgayTraPhong() == null) {
            System.err.println("Dữ liệu đặt phòng không hợp lệ!");
            return false;
        }
        return datPhongDAO.themDatPhong(datPhong);
    }
    

    public List<DatPhongDTO> layDanhSachDatPhong(String maPhong) {
        return datPhongDAO.layDanhSachDatPhong(maPhong);
    }
    
    public DatPhongDTO layDatPhongTheoMaDatPhong(String maDatPhong) {
        List<DatPhongDTO> danhSachDatPhong = layDanhSachDatPhong(maDatPhong);
        return danhSachDatPhong.stream()
                .filter(dp -> dp.getMaDatPhong().equals(maDatPhong))
                .findFirst()
                .orElse(null);
    }

    public boolean xoaDatPhong(String maDatPhong) {
        if (maDatPhong == null || maDatPhong.isEmpty()) {
            System.err.println("Mã đặt phòng không hợp lệ!");
            return false;
        }
        return datPhongDAO.xoaDatPhong(maDatPhong);
    }
}
=======
    // Lấy tất cả danh sách đặt phòng
    public ArrayList<DatPhongDTO> getAllDatPhong() {
        return datPhongDAO.getALL();
    }

    // Thêm đặt phòng mới
    public boolean addDatPhong(DatPhongDTO datPhong) {
        return datPhongDAO.add(datPhong);
    }

    // Cập nhật thông tin đặt phòng
    public boolean updateDatPhong(DatPhongDTO datPhong) {
        return datPhongDAO.update(datPhong);
    }

    // Xoá đặt phòng theo mã
    public boolean deleteDatPhong(String maDatPhong) {
        return datPhongDAO.delete(maDatPhong);
    }

    // Lấy đặt phòng theo mã
    public DatPhongDTO getById(String maDatPhong) {
        return datPhongDAO.getById(maDatPhong);
    }

    // Lấy danh sách đặt phòng theo mã phòng
    public List<DatPhongDTO> layDanhSachDatPhong(String maPhong) {
        return datPhongDAO.getByMaPhong(maPhong);
    }
}

>>>>>>> origin/Nhat2
