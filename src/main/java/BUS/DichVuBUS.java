package BUS;

import DAO.DichVuDAO;
import DTO.DichVuDTO;
import java.util.ArrayList;

public class DichVuBUS {
    private DichVuDAO dichVuDAO = new DichVuDAO();

    // Lấy danh sách dịch vụ
    public ArrayList<DichVuDTO> layDanhSachDichVu() {
        return dichVuDAO.getAllDichVu();
    }

    // Tính tổng tiền dịch vụ dựa vào danh sách các mã dịch vụ được chọn (từ checkbox)
    public int tinhTongTienDichVu(ArrayList<String> dsMaDV) {
        int tongTien = 0;
        for (String maDV : dsMaDV) {
            DichVuDTO dv = dichVuDAO.getDichVuByMa(maDV);
            if (dv != null) {
                tongTien += dv.getGiaDichVu(); // Cộng tiền của mỗi dịch vụ vào tổng
            }
        }
        return tongTien;
    }
    
    
}
