package BUS;

import java.util.ArrayList;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;

public class TaiKhoanBUS {
    private TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    public static ArrayList<TaiKhoanDTO> getAllTaiKhoan() {
        return DAO.TaiKhoanDAO.getAllTaiKhoan();
    }

    public static TaiKhoanDTO getTaiKhoan(String tenDangNhap, String matkhau) {
        return DAO.TaiKhoanDAO.getTaiKhoan(tenDangNhap, matkhau);
    }

    public boolean add(TaiKhoanDTO tkDTO) {
        return tkDAO.add(tkDTO);
    }

    public boolean update(TaiKhoanDTO tkDTO) {
        return tkDAO.update(tkDTO);
    }

    public boolean delete(String tenDangNhap) {
        return tkDAO.delete(tenDangNhap);
    }
}
