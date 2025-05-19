package BUS;

import DTO.KhachHangDTO;
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
        return khachHangDAO.add(khachHang) > 0; 
    }

    
    public boolean updateKhachHang(KhachHangDTO khachHang) {
        return khachHangDAO.update(khachHang) > 0; 
    }

    public boolean deleteKhachHang(String maKH) {
        return khachHangDAO.delete(maKH) > 0;  
    }

    public boolean isMaKHExists(String maKH) {
        return khachHangDAO.getById(maKH) != null;
    }

    public KhachHangDTO getById(String maKH) {
        return khachHangDAO.getById(maKH);
    }

    public KhachHangDTO getBySDT(String sdt) {
        if (sdt == null || sdt.isEmpty()) {
            System.err.println("Số điện thoại không hợp lệ!");
            return null;
        }
        return khachHangDAO.getBySDT(sdt);
    }

    public String increaseMaKH() {
        return khachHangDAO.increaseMaKH();
    }
}