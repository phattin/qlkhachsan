package BUS;

import DAO.ThongKeDAO;
import DTO.ThongKeDTO;
import java.util.ArrayList;

public class ThongKeBUS {
    private ThongKeDAO dao = new ThongKeDAO();

    public ArrayList<ThongKeDTO> layThongKeTheoThang(int nam) {
        return dao.layThongKeTheoThang(nam);
    }

    public ArrayList<ThongKeDTO> layThongKeTheoNam() {
        return dao.layThongKeTheoNam();
    }

    public double tinhTongDoanhThu(ArrayList<ThongKeDTO> ds) {
        return ds.stream().mapToDouble(ThongKeDTO::getTongDoanhThu).sum();
    }
}