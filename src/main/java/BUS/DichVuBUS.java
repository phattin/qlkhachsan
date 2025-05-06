package BUS;

import DAO.DichVuDAO;
import DTO.DichVuDTO;
import java.util.ArrayList;

public class DichVuBUS {
    private DichVuDAO dichVuDAO = new DichVuDAO();

    // Lấy danh sách tất cả dịch vụ
    public ArrayList<DichVuDTO> getAllDichVu() {
        return dichVuDAO.getAllDichVu();
    }

    // Lấy thông tin dịch vụ theo mã dịch vụ
    public DichVuDTO getDichVuByMa(String maDV) {
        return dichVuDAO.getDichVuByMa(maDV);
    }

    // Thêm một dịch vụ mới
    public boolean themDichVu(DichVuDTO dichVu) {
        if (dichVu == null || dichVu.getMaDV().trim().isEmpty() || dichVu.getTenDV().trim().isEmpty() || dichVu.getGiaDV() <= 0) {
            return false; // Dữ liệu không hợp lệ
        }
        return dichVuDAO.addDichVu(dichVu);
    }

    // Cập nhật thông tin một dịch vụ
    public boolean capNhatDichVu(DichVuDTO dichVu) {
        if (dichVu == null || dichVu.getMaDV().trim().isEmpty() || dichVu.getTenDV().trim().isEmpty() || dichVu.getGiaDV() <= 0) {
            return false; // Dữ liệu không hợp lệ
        }
        return dichVuDAO.updateDichVu(dichVu);
    }

    // Xóa một dịch vụ theo mã dịch vụ
    public boolean xoaDichVu(String maDV) {
        return dichVuDAO.deleteDichVu(maDV);
    }

    // Tính tổng tiền dịch vụ dựa vào danh sách các mã dịch vụ được chọn
    public int tinhTongTienDichVu(ArrayList<String> dsMaDV) {
        int tongTien = 0;
        for (String maDV : dsMaDV) {
            DichVuDTO dv = dichVuDAO.getDichVuByMa(maDV);
            if (dv != null) {
                tongTien += dv.getGiaDV();
            }
        }
        return tongTien;
    }

    // Tìm kiếm dịch vụ đa năng theo tùy chọn
    public ArrayList<DichVuDTO> timKiemDichVu(String searchText, String searchOption) {
        ArrayList<DichVuDTO> ketQuaTimKiem = new ArrayList<>();
        ArrayList<DichVuDTO> tatCaDichVu = getAllDichVu();
        String searchTextLower = searchText.trim().toLowerCase();

        for (DichVuDTO dichVu : tatCaDichVu) {
            switch (searchOption) {
                case "Mã DV":
                    if (dichVu.getMaDV().toLowerCase().contains(searchTextLower)) {
                        ketQuaTimKiem.add(dichVu);
                    }
                    break;
                case "Tên Dịch Vụ":
                    if (dichVu.getTenDV().toLowerCase().contains(searchTextLower)) {
                        ketQuaTimKiem.add(dichVu);
                    }
                    break;
                case "Giá Dịch Vụ":
                    try {
                        int giaTimKiem = Integer.parseInt(searchText);
                        if (dichVu.getGiaDV() == giaTimKiem) { // Tìm kiếm chính xác theo giá
                            ketQuaTimKiem.add(dichVu);
                        }
                    } catch (NumberFormatException e) {
                        // Nếu searchText không phải là số, bỏ qua tìm kiếm theo giá
                    }
                    break;
            }
        }
        return ketQuaTimKiem;
    }
}
