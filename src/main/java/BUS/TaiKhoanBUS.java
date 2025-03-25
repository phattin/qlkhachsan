package BUS;

import java.util.ArrayList;

import DTO.TaiKhoanDTO;

public class TaiKhoanBUS {
    public static ArrayList<TaiKhoanDTO> getAllTaiKhoan() {
        return DAO.TaiKhoanDAO.getAllTaiKhoan();
    }

    public static TaiKhoanDTO getTaiKhoan(String tenDangNhap, String matkhau) {
        return DAO.TaiKhoanDAO.getTaiKhoan(tenDangNhap, matkhau);
    }
}
