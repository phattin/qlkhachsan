package DTO;

public class PhongDTO {
    private String maPhong;
    private String maLoaiPhong;
    private String tenLoaiPhong;
    private int soGiuong;
    private int giaTien;
    private String trangThai;

    public PhongDTO() {}

    public PhongDTO(String maPhong, String maLoaiPhong, String tenLoaiPhong, int soGiuong, int giaTien, String trangThai) {
        this.maPhong = maPhong;
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.soGiuong = soGiuong;
        this.giaTien = giaTien;
        this.trangThai = trangThai;
    }

    public String getMaPhong() { return maPhong; }
    public String getMaLoaiPhong() { return maLoaiPhong; }
    public String getTenLoaiPhong() { return tenLoaiPhong; }
    public int getSoGiuong() { return soGiuong; }
    public int getGiaTien() { return giaTien; }
    public String getTrangThai() { return trangThai; }

    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public void setTenLoaiPhong(String tenLoaiPhong) { this.tenLoaiPhong = tenLoaiPhong; }
    public void setSoGiuong(int soGiuong) { this.soGiuong = soGiuong; }
    public void setGiaTien(int giaTien) { this.giaTien = giaTien; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
