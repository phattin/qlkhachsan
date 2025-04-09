package BUS;

import DAO.HoaDonDAO;
import DTO.HoaDonDTO;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Connection;

public class HoaDonBUS {
    private HoaDonDAO hoaDonDAO;

    public HoaDonBUS(Connection conn) {
        this.hoaDonDAO = new HoaDonDAO(conn);
    }

    public ArrayList<HoaDonDTO> layDanhSachHoaDon() {
        return hoaDonDAO.getAll();
    }

    public void themHoaDon(HoaDonDTO hoaDon) {
        try {
            hoaDonDAO.themHoaDon(hoaDon);
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm hóa đơn: " + e.getMessage());
        }
    }

    public void capnhatHoaDon(HoaDonDTO hoaDon) {
        try {
            hoaDonDAO.capnhatHoaDon(hoaDon);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    public void xoaHoaDon(String maHoaDon) {
        try {
            hoaDonDAO.xoaHoaDon(maHoaDon);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }

    public HoaDonDTO layHoaDonTheoMa(String maHoaDon) {
        return hoaDonDAO.layHoaDonTheoMa(maHoaDon);
    }
    
}