package BUS;

import DAO.HoaDonDAO;
import DTO.HoaDonDTO;
import java.util.ArrayList;

public class HoaDonBUS {
    private HoaDonDAO hoaDonDAO;

    public HoaDonBUS() {
        hoaDonDAO = new HoaDonDAO();
    }

    // Lấy danh sách hóa đơn
    public ArrayList<HoaDonDTO> layDanhSachHoaDon() {
        return hoaDonDAO.getAllHoaDon();
    }

    // Thêm hóa đơn mới
    public boolean themHoaDon(HoaDonDTO hoaDon) {
        return hoaDonDAO.themHoaDon(hoaDon);
    }

    // Cập nhật hóa đơn
    public boolean capnhatHoaDon(HoaDonDTO hoaDon) {
        return hoaDonDAO.capnhatHoaDon(hoaDon);
    }

    // Xóa hóa đơn
    public boolean xoaHoaDon(String maHoaDon) {
        return hoaDonDAO.xoaHoaDon(maHoaDon);
    }
}
