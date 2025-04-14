package BUS;

import DTO.KhachHangDTO;
<<<<<<< HEAD
=======
import DTO.LoaiPhongDTO;
>>>>>>> origin/Nhat2
import DAO.KhachHangDAO;
import java.util.ArrayList;

public class KhachHangBUS {
    private KhachHangDAO khachHangDAO;

    public KhachHangBUS() {
        khachHangDAO = new KhachHangDAO();
    }

    public ArrayList<KhachHangDTO> getAllKhachHang() {
        return khachHangDAO.getAll();
    }

    public boolean addKhachHang(KhachHangDTO khachHang) {
        if (khachHang == null || khachHang.getMaKhachHang().isEmpty() || khachHang.getHoTen().isEmpty() || khachHang.getCCCD().isEmpty() || khachHang.getSDT().isEmpty() || khachHang.getEmail().isEmpty() || khachHang.getDiaChi().isEmpty()) {
            System.err.println("Dữ liệu khách hàng không hợp lệ!");
            return false;
        }
        return khachHangDAO.add(khachHang) > 0; 
    }

<<<<<<< HEAD
    /* 
=======
    
>>>>>>> origin/Nhat2
    public boolean updateKhachHang(KhachHangDTO khachHang) {
        if (khachHang == null || khachHang.getMaKhachHang().isEmpty() || khachHang.getHoTen().isEmpty() || khachHang.getCCCD().isEmpty()) {
            System.err.println("Dữ liệu khách hàng không hợp lệ!");
            return false;
        }
        return khachHangDAO.update(khachHang) > 0; 
    }

    public boolean deleteKhachHang(String maKH) {
        if (maKH == null || maKH.isEmpty()) {
            System.err.println("Mã khách hàng không hợp lệ!");
            return false;
        }
        return khachHangDAO.delete(maKH) > 0;  
    }
<<<<<<< HEAD
    */
=======

    public boolean isMaKHExists(String maKH) {
        return khachHangDAO.getById(maKH) != null;
    }

    public KhachHangDTO getById(String maKH) {
        return khachHangDAO.getById(maKH);
    }
>>>>>>> origin/Nhat2
}
