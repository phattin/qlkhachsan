package BUS;

import DTO.PhongDTO;
import DAO.PhongDAO;
import java.util.ArrayList;

public class PhongBUS {
    private PhongDAO phongDAO;

    public PhongBUS() {
        phongDAO = new PhongDAO();
    }

    public ArrayList<PhongDTO> getAllPhong() {
        return phongDAO.getALL();
    }

    public boolean addPhong(PhongDTO phong) {
        if (phong == null || phong.getMaPhong().isEmpty() || phong.getMaLoaiPhong().isEmpty() || phong.getTrangThai().isEmpty()) {
            System.err.println("Dữ liệu phòng không hợp lệ!");
            return false;
        }
        return phongDAO.add(phong) > 0; 
    }

    public boolean updatePhong(PhongDTO phong) {
        if (phong == null || phong.getMaPhong().isEmpty() || phong.getMaLoaiPhong().isEmpty() || phong.getTrangThai().isEmpty()) {
            System.err.println("Dữ liệu phòng không hợp lệ!");
            return false;
        }
        return phongDAO.update(phong) > 0; 
    }

    public boolean deletePhong(String maPhong) {
        if (maPhong == null || maPhong.isEmpty()) {
            System.err.println("Mã phòng không hợp lệ!");
            return false;
        }
        return phongDAO.delete(maPhong) > 0;  
    }
}
        