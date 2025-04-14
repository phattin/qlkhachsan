package BUS;

import DAO.DatPhongDAO;
import DTO.DatPhongDTO;

import java.util.ArrayList;
import java.util.List;

public class DatPhongBUS {

    private DatPhongDAO datPhongDAO;

    public DatPhongBUS() {
        datPhongDAO = new DatPhongDAO();
    }

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

