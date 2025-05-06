package DTO;

public class PhongDTO {
    private String maPhong;
    private String maLoaiPhong;
    private String trangThai;

    public PhongDTO() {}

    public PhongDTO(String maPhong, String maLoaiPhong, String trangThai) {
        this.maPhong = maPhong;
        this.maLoaiPhong = maLoaiPhong;
        this.trangThai = trangThai;
    }

    public String getMaPhong() { return maPhong; }
    public String getMaLoaiPhong() { return maLoaiPhong; }
    public String getTrangThai() { return trangThai; }

    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}