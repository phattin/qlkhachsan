package BUS;

import DAO.DanhSachPhongDAO;
import DTO.DanhSachPhongDTO;
import DTO.PhongDTO;
import java.util.ArrayList;

public class DanhSachPhongBUS {
    private DanhSachPhongDAO danhSachPhongDAO = new DanhSachPhongDAO();
    private PhongBUS phongBUS = new PhongBUS();

    public ArrayList<DanhSachPhongDTO> layDanhSachPhong() {
        return danhSachPhongDAO.getAllDanhSachPhong();
    }

    public DanhSachPhongDTO layDanhSachPhongTheoMa(String maDSP) {
        return danhSachPhongDAO.getDanhSachPhongByMa(maDSP);
    }

    public ArrayList<DanhSachPhongDTO> getDanhSachPhongTheoMaHoaDon(String maHD) {
        return danhSachPhongDAO.getDanhSachPhongTheoMaHoaDon(maHD);
    }

    public boolean themDanhSachPhong(DanhSachPhongDTO dsp) {
        // Kiểm tra dữ liệu đầu vào
        if (dsp.getMaDatPhong() == null || dsp.getMaDatPhong().trim().isEmpty() ||
            dsp.getMaPhong() == null || dsp.getMaPhong().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra xem phòng đã có trong bất kỳ danh sách nào chưa
        if (danhSachPhongDAO.checkPhongExist(dsp.getMaPhong())) {
            System.out.println("Phòng đã được đặt trong danh sách khác!");
            return false;
        }
        
        // Cập nhật trạng thái phòng thành "Đã đặt"
        PhongDTO phong = phongBUS.getById(dsp.getMaPhong());
        if (phong != null) {
            phong.setTrangThai("Đã đặt");
            phongBUS.updatePhong(phong);
        }
        
        return danhSachPhongDAO.addDanhSachPhong(dsp);
    }

    public boolean capNhatDanhSachPhong(DanhSachPhongDTO dsp) {
        // Kiểm tra dữ liệu đầu vào
        if (dsp.getMaDSP() == null || dsp.getMaDSP().trim().isEmpty() ||
            dsp.getMaDatPhong() == null || dsp.getMaDatPhong().trim().isEmpty() ||
            dsp.getMaPhong() == null || dsp.getMaPhong().trim().isEmpty()) {
            return false;
        }
        
        // Lấy thông tin cũ để biết phòng cũ
        DanhSachPhongDTO dspCu = danhSachPhongDAO.getDanhSachPhongByMa(dsp.getMaDSP());
        if (dspCu == null) {
            return false;
        }
        
        // Kiểm tra nếu đổi phòng, thì phòng mới không được đặt ở nơi khác
        if (!dsp.getMaPhong().equals(dspCu.getMaPhong()) && danhSachPhongDAO.checkPhongExist(dsp.getMaPhong())) {
            System.out.println("Phòng đã được đặt trong danh sách khác!");
            return false;
        }
        
        // Cập nhật trạng thái
        if (!dsp.getMaPhong().equals(dspCu.getMaPhong())) {
            // Đặt trạng thái phòng cũ thành "Trống"
            PhongDTO phongCu = phongBUS.getById(dspCu.getMaPhong());
            if (phongCu != null) {
                phongCu.setTrangThai("Trống");
                phongBUS.updatePhong(phongCu);
            }
            
            // Đặt trạng thái phòng mới thành "Đã đặt"
            PhongDTO phongMoi = phongBUS.getById(dsp.getMaPhong());
            if (phongMoi != null) {
                phongMoi.setTrangThai("Đã đặt");
                phongBUS.updatePhong(phongMoi);
            }
        }
        
        return danhSachPhongDAO.updateDanhSachPhong(dsp);
    }

    public boolean xoaDanhSachPhong(String maDSP) {
        // Lấy thông tin phòng trước khi xóa
        DanhSachPhongDTO dsp = danhSachPhongDAO.getDanhSachPhongByMa(maDSP);
        if (dsp == null) {
            return false;
        }
        
        // Đặt trạng thái phòng về "Trống"
        PhongDTO phong = phongBUS.getById(dsp.getMaPhong());
        if (phong != null) {
            phong.setTrangThai("Trống");
            phongBUS.updatePhong(phong);
        }
        
        return danhSachPhongDAO.deleteDanhSachPhong(maDSP);
    }

    public boolean xoaDanhSachPhongTheoMaDatPhong(String maDatPhong) {
        // Lấy danh sách phòng trước khi xóa
        ArrayList<DanhSachPhongDTO> dsList = danhSachPhongDAO.getDanhSachPhongByMaDatPhong(maDatPhong);
        
        // Đặt trạng thái các phòng về "Trống"
        for (DanhSachPhongDTO dsp : dsList) {
            PhongDTO phong = phongBUS.getById(dsp.getMaPhong());
            if (phong != null) {
                phong.setTrangThai("Trống");
                phongBUS.updatePhong(phong);
            }
        }
        
        return danhSachPhongDAO.deleteDanhSachPhongByMaDatPhong(maDatPhong);
    }
    
    public ArrayList<DanhSachPhongDTO> layDanhSachPhongTheoMaDatPhong(String maDatPhong) {
        return danhSachPhongDAO.getDanhSachPhongByMaDatPhong(maDatPhong);
    }
    
    public ArrayList<String> layDanhSachMaPhongTheoMaDatPhong(String maDatPhong) {
        ArrayList<String> danhSachMaPhong = new ArrayList<>();
        ArrayList<DanhSachPhongDTO> danhSachPhong = danhSachPhongDAO.getDanhSachPhongByMaDatPhong(maDatPhong);
        
        for (DanhSachPhongDTO dsp : danhSachPhong) {
            danhSachMaPhong.add(dsp.getMaPhong());
        }
        
        return danhSachMaPhong;
    }
    
    public boolean kiemTraPhongDaDat(String maPhong) {
        return danhSachPhongDAO.checkPhongExist(maPhong);
    }
    
    public int demSoPhongTheoMaDatPhong(String maDatPhong) {
        return danhSachPhongDAO.countPhongByMaDatPhong(maDatPhong);
    }
}
