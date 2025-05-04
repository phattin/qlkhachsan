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


    
    
}
