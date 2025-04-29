package BUS;

import java.util.ArrayList;

import DAO.PhanQuyenDAO;
import DTO.PhanQuyenDTO;

public class PhanQuyenBUS {
    private PhanQuyenDAO pqDAO = new PhanQuyenDAO();
    public static ArrayList<PhanQuyenDTO> getAllPhanQuyen() {
        return DAO.PhanQuyenDAO.getAllPhanQuyen();
    }

    public static ArrayList<PhanQuyenDTO> getPhanQuyenByMaChucVu(String maChucVu) {
        return DAO.PhanQuyenDAO.getPhanQuyenByMaChucVu(maChucVu);
    }

    public boolean add(PhanQuyenDTO pqDTO) {
        return pqDAO.add(pqDTO);
    }

    public boolean deleteByMaChucVu(String maChucVu) {
        return pqDAO.deleteByMaChucVu(maChucVu);
    }

}
