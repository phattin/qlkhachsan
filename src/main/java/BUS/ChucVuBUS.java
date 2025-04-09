package BUS;

import java.util.ArrayList;

import DAO.ChucVuDAO;
import DTO.ChucVuDTO;

public class ChucVuBUS {
    private ChucVuDAO cvDAO = new ChucVuDAO();
    public static ArrayList<ChucVuDTO> getAllChucVu() {
        return DAO.ChucVuDAO.getAllChucVu();
    }

    public static ChucVuDTO getChucVuByMa(String maChucVu) {
        return DAO.ChucVuDAO.getChucVuByMa(maChucVu);
    }

    public static ChucVuDTO getChucVuByMaNhanVien(String maNhanVien) {
        return DAO.ChucVuDAO.getChucVuByMaNhanVien(maNhanVien);
    }

    public boolean add(ChucVuDTO cvDTO) {
        return cvDAO.add(cvDTO);
    }

    public boolean update(ChucVuDTO cvDTO) {
        return cvDAO.update(cvDTO);
    }

    public boolean delete(String maChucVu) {
        return cvDAO.delete(maChucVu);
    }

}
