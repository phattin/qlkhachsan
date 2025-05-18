package DTO;

public class ChucVuDTO {
    private String maChucVu;
    private  String tenChucVu;
    private String trangThai;

    public ChucVuDTO() {
    }

    public ChucVuDTO(String maChucVu, String tenChucVu, String trangThai) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
        this.trangThai = trangThai;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
