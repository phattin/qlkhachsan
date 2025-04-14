package BUS;

<<<<<<< HEAD
public class LoaiPhongBUS {
=======
import DAO.LoaiPhongDAO;
import DTO.LoaiPhongDTO;
import java.util.ArrayList;

public class LoaiPhongBUS {

    private LoaiPhongDAO loaiPhongDAO;

    public LoaiPhongBUS() {
        loaiPhongDAO = new LoaiPhongDAO();
    }

    // Lấy danh sách tất cả loại phòng
    public ArrayList<LoaiPhongDTO> getAllLoaiPhong() {
        return loaiPhongDAO.getALL();
    }

    // Thêm loại phòng mới
    public boolean addLoaiPhong(LoaiPhongDTO lp) {
        return loaiPhongDAO.add(lp);
    }

    // Cập nhật thông tin loại phòng
    public boolean updateLoaiPhong(LoaiPhongDTO lp) {
        return loaiPhongDAO.update(lp);
    }

    // Xoá loại phòng theo mã
    public boolean deleteLoaiPhong(String maLoaiPhong) {
        return loaiPhongDAO.delete(maLoaiPhong);
    }

    // Tìm loại phòng theo mã
    public LoaiPhongDTO getById(String maLoaiPhong) {
        return loaiPhongDAO.getById(maLoaiPhong);
    }

>>>>>>> origin/Nhat2
    
}
