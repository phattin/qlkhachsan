package DTO;

public class PhanQuyenDTO {
    private String maChucVu;
    private String maChucNang;

    public PhanQuyenDTO() {
    }

    public PhanQuyenDTO(String maChucVu, String maChucNang) {
        this.maChucNang = maChucNang;
        this.maChucVu = maChucVu;
    }

    public String getMaChucNang() {
        return maChucNang;
    }

    public void setMaChucNang(String maChucNang) {
        this.maChucNang = maChucNang;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

}
