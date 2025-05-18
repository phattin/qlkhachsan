package DTO;

public class ChucNangDTO {
    private String maChucNang;
    private  String tenChucNang;

    public ChucNangDTO() {
    }

    public ChucNangDTO(String maChucNang, String tenChucNang) {
        this.maChucNang = maChucNang;
        this.tenChucNang = tenChucNang;
    }

    public String getMaChucNang() {
        return maChucNang;
    }

    public void setMaChucNang(String maChucNang) {
        this.maChucNang = maChucNang;
    }

    public String getTenChucNang() {
        return tenChucNang;
    }

    public void setTenChucNang(String tenChucNang) {
        this.tenChucNang = tenChucNang;
    }
}
