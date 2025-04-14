package BUS;

import java.util.ArrayList;

import javax.swing.JOptionPane;

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

    public boolean delete(String maNhanVien) {
        return tkDAO.delete(maNhanVien);
    }

    public boolean checkTextAdd(String tenDangNhap, String matKhau) {
        if (tenDangNhap.length() > 50) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập không được quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (tkDAO.isUsernameExists(tenDangNhap)) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải từ 6 ký tự trở lên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (matKhau.length() < 6 || matKhau.length() > 50) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải từ 6 đến 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean checkTextUpdate(String tenDangNhap, String matKhau, String maChucVu, String maNhanVien) {
        if (tenDangNhap.length() > 50) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập không được quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (tkDAO.isUsernameUpdateExists(tenDangNhap, maNhanVien)) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải từ 6 ký tự trở lên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (matKhau.length() < 6 || matKhau.length() > 50) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải từ 6 đến 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    
}
