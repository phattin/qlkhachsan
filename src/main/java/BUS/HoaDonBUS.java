package BUS;

import java.util.ArrayList;

import DAO.HoaDonDAO;
import DTO.HoaDonDTO;

public class HoaDonBUS {
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    public static ArrayList<HoaDonDTO> layDanhSachHoaDon() {
        return DAO.HoaDonDAO.getAllHoaDon();
    }

    public static HoaDonDTO getHoaDonByMa(String maHD) {
        return DAO.HoaDonDAO.getHoaDonByMa(maHD);
    }

    public boolean themHoaDon(HoaDonDTO hoaDon) {
        return hoaDonDAO.themHoaDon(hoaDon);
    }
        
    public boolean capnhatHoaDon(HoaDonDTO hoaDon) {
        return hoaDonDAO.capnhatHoaDon(hoaDon);
    }
        
    public boolean xoaHoaDon(String maHoaDon) {
        return hoaDonDAO.xoaHoaDon(maHoaDon);
    }

    public String tangMaHoaDon() {
        return hoaDonDAO.tangMaHoaDon();
    }

    public ArrayList<HoaDonDTO> search(String cbxSearch, String txtSearch, String TrangThai, String dateFrom, String dateTo) {
      
        return hoaDonDAO.search(cbxSearch, txtSearch, TrangThai, dateFrom, dateTo);
    }
}
