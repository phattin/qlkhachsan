package BUS;

import DAO.DatPhongDAO;
import DTO.DatPhongDTO;
import java.util.List;

public class DatPhongBUS {
    private DatPhongDAO datPhongDAO;

    public DatPhongBUS() {
        datPhongDAO = new DatPhongDAO();
    }

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
