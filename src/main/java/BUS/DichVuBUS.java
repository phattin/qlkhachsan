package BUS;

import DAO.DichVuDAO;
import DTO.DichVuDTO;
import java.util.ArrayList;

public class DichVuBUS {
    private DichVuDAO dichVuDAO;

    public DichVuBUS() {
        dichVuDAO = new DichVuDAO();
    }

    // Lấy danh sách tất cả dịch vụ
    public ArrayList<DichVuDTO> getAllDichVu() {
        return dichVuDAO.getAllDichVu();
    }

    // Thêm dịch vụ mới
    public boolean addDichVu(DichVuDTO dichVu) {
        if (dichVu == null || dichVu.getMaDichVu().isEmpty() || dichVu.getTenDichVu().isEmpty() || dichVu.getGiaDichVu() < 0) {
            System.err.println("Dữ liệu dịch vụ không hợp lệ!");
            return false;
        }
        return dichVuDAO.insertDichVu(dichVu) > 0;
    }

    // Cập nhật thông tin dịch vụ
    public boolean updateDichVu(DichVuDTO dichVu) {
        if (dichVu == null || dichVu.getMaDichVu().isEmpty() || dichVu.getTenDichVu().isEmpty() || dichVu.getGiaDichVu() < 0) {
            System.err.println("Dữ liệu dịch vụ không hợp lệ!");
            return false;
        }
        return dichVuDAO.updateDichVu(dichVu) > 0;
    }

    // Xóa dịch vụ
    public boolean deleteDichVu(String maDV) {
        if (maDV == null || maDV.isEmpty()) {
            System.err.println("Mã dịch vụ không hợp lệ!");
            return false;
        }
        return dichVuDAO.deleteDichVu(maDV) > 0;
    }

    // Kiểm tra mã dịch vụ đã tồn tại chưa
    public boolean isMaDVExists(String maDV) {
        return dichVuDAO.getDichVuByMa(maDV) != null;
    }

    // Lấy dịch vụ theo mã
    public DichVuDTO getById(String maDV) {
        return dichVuDAO.getDichVuByMa(maDV);
    }

    // Tạo mã dịch vụ mới tự động
    public String generateNewMaDV() {
        return dichVuDAO.increaseMaDV();
    }
}