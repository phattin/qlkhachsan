package BUS;

import DTO.LoaiPhongDTO;
import DTO.PhongDTO;
import DAO.PhongDAO;
import DAO.LoaiPhongDAO;
import java.util.ArrayList;

public class PhongBUS {
    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;

    public PhongBUS() {
        phongDAO = new PhongDAO();
        loaiPhongDAO = new LoaiPhongDAO(); // Sửa lỗi thiếu DAO
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

    public LoaiPhongDTO getLoaiPhongByMaPhong(String maPhong) {
        return phongDAO.getLoaiPhongByMaPhong(maPhong);
    }
    

    public LoaiPhongDTO getById(String maLoaiPhong) {
        return loaiPhongDAO.getById(maLoaiPhong);
    }

    public boolean isMaPhongExists(String maPhong) {
        PhongDTO phong = phongDAO.getById(maPhong);
        return phong != null;  
    }

    public String increaseMaPhong() {
        return phongDAO.increaseMaPhong();
    }
}
        