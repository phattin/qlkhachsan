package BUS;

import DAO.SuDungDichVuDAO;
import DTO.SuDungDichVuDTO;
import java.util.ArrayList;

public class SuDungDichVuBUS {
    private SuDungDichVuDAO suDungDichVuDAO = new SuDungDichVuDAO();

    public ArrayList<SuDungDichVuDTO> layDanhSachSuDungDichVu() {
        return suDungDichVuDAO.getAllSuDungDichVu();
    }

    public SuDungDichVuDTO laySuDungDichVuTheoMa(String maSDDV) {
        return suDungDichVuDAO.getSuDungDichVuByMa(maSDDV);
    }

    public ArrayList<SuDungDichVuDTO> getDanhSachDichVuTheoMaHoaDon(String maHD) {
        return suDungDichVuDAO.getDanhSachDichVuTheoMaHoaDon(maHD);
    }

    public boolean themSuDungDichVu(SuDungDichVuDTO sddv) {
        return suDungDichVuDAO.addSuDungDichVu(sddv);
    }

    public boolean capNhatSuDungDichVu(SuDungDichVuDTO sddv) {
        return suDungDichVuDAO.updateSuDungDichVu(sddv);
    }

    public boolean xoaSuDungDichVu(String maSDDV) {
        return suDungDichVuDAO.deleteSuDungDichVu(maSDDV);
    }

    public boolean xoaSuDungDichVuTheoMaDP(String maDatPhong) {
        return suDungDichVuDAO.deleteSuDungDichVuByMaDP(maDatPhong);
    }
    
    public ArrayList<SuDungDichVuDTO> layDanhSachSuDungDichVuTheoDP(String maDatPhong) {
        return suDungDichVuDAO.getSuDungDichVuByMaDP(maDatPhong);
    }
}