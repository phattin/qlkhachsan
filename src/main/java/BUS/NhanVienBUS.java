package BUS;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;
import TOOL.Validation;

public class NhanVienBUS {
    private NhanVienDAO nvDAO = new NhanVienDAO();
    public static ArrayList<NhanVienDTO> getAllNhanVien() {
        return DAO.NhanVienDAO.getAllNhanVien();
    }

    public static NhanVienDTO getNhanVienByMa(String maNV) {
        return DAO.NhanVienDAO.getNhanVienByMa(maNV);
    }

    public boolean add(NhanVienDTO nvDTO) {
        return nvDAO.add(nvDTO);
    }

    public boolean update(NhanVienDTO nvDTO) {
        return nvDAO.update(nvDTO);
    }

    public boolean delete(String maNV) {
        //Xóa nhân viên và tài khoản
        TaiKhoanBUS tkBUS = new TaiKhoanBUS();
        return nvDAO.delete(maNV) && tkBUS.delete(maNV);
    }

    public String increaseMaNV() {
        return nvDAO.increaseMaNV();
    }

    public boolean checkTextAdd(String hoTen, String sdt, String email, String diaChi, long luong) {
        if (!Validation.isName(hoTen)) {
            JOptionPane.showMessageDialog(null, "Họ tên không chứa số và kí tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (hoTen.length() > 100) {
            JOptionPane.showMessageDialog(null, "Họ tên không được quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (!Validation.isPhoneNumber(sdt)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (nvDAO.phoneNumberExists(sdt)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (!Validation.isEmail(email)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (email.length() > 100) {
            JOptionPane.showMessageDialog(null, "Email không được quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (nvDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(null, "Email đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (diaChi.length() > 255) {
            JOptionPane.showMessageDialog(null, "Địa chỉ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (luong <= 0) {
            JOptionPane.showMessageDialog(null, "Lương phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean checkTextUpdate(String maNV, String hoTen, String sdt, String email, String diaChi, long luong) {
        if (!Validation.isName(hoTen)) {
            JOptionPane.showMessageDialog(null, "Họ tên không chứa số và kí tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (hoTen.length() > 100) {
            JOptionPane.showMessageDialog(null, "Họ tên không được quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (!Validation.isPhoneNumber(sdt)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (nvDAO.phoneNumberExists(maNV, sdt)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (!Validation.isEmail(email)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (email.length() > 100) {
            JOptionPane.showMessageDialog(null, "Email không được quá 100 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (nvDAO.emailExists(maNV, email)) {
            JOptionPane.showMessageDialog(null, "Email đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (diaChi.length() > 255) {
            JOptionPane.showMessageDialog(null, "Địa chỉ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (luong <= 0) {
            JOptionPane.showMessageDialog(null, "Lương phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


    public ArrayList<NhanVienDTO> search(String cbTimKiem, String txTimKiem, String gioiTinh,
                                              Double luongMin, Double luongMax,
                                              String ngayBatDau, String ngayKetThuc) {
        return nvDAO.search(cbTimKiem , txTimKiem, gioiTinh, luongMin, luongMax, ngayBatDau, ngayKetThuc);
    }
}
