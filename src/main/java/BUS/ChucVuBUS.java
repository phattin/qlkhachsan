package BUS;

import java.util.ArrayList;

import javax.swing.JOptionPane;

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

    public String increaseMaCV() {
        return cvDAO.increaseMaCV();
    }

    public boolean checkTextAdd(String tenCV){
        if(tenCV.length() > 50){
            JOptionPane.showMessageDialog(null, "Tên chức vụ không được quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if(cvDAO.TenChucVuExists(tenCV)){
            JOptionPane.showMessageDialog(null, "Tên chức vụ đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean checkTextUpdate(String maCV, String tenCV){
        if(tenCV.length() > 50){
            JOptionPane.showMessageDialog(null, "Tên chức vụ không được quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if(cvDAO.TenChucVuUpdateExists(maCV, tenCV)){
            JOptionPane.showMessageDialog(null, "Tên chức vụ đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean hasNhanVienOfChucVu(String maCV){
        if(cvDAO.hasNhanVienOfChucVu(maCV)){
            JOptionPane.showMessageDialog(null, "Đang có nhân viên thuộc chức vụ này. Không thể xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    public ArrayList<ChucVuDTO> search(String cbTimKiem, String txTimKiem){
        return cvDAO.search(cbTimKiem, txTimKiem);
    }
}
