package BUS;

import java.util.ArrayList;

import DAO.ChucNangDAO;
import DTO.ChucNangDTO;

public class ChucNangBUS {
    private ChucNangDAO cnDAO = new ChucNangDAO();
    public static ArrayList<ChucNangDTO> getAllChucNang() {
        return DAO.ChucNangDAO.getAllChucNang();
    }

    public static ChucNangDTO getChucNangByMa(String maChucNang) {
        return DAO.ChucNangDAO.getChucNangByMa(maChucNang);
    }

    public String getMaByTenChucNang(String tenCN){
        return cnDAO.getMaByTenChucNang(tenCN);
    }

    public ArrayList<ChucNangDTO> getChucNangByMaChucVu(String maChucVu) {
        return cnDAO.getChucNangByMaChucVu(maChucVu);
    }

}
