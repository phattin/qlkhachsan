package DTO;

public class PhongDTO {
    private String maPhong;
    private String maLoaiPhong;
<<<<<<< HEAD
    private String tenLoaiPhong;
    private int soGiuong;
    private int giaTien;
=======
>>>>>>> origin/Nhat2
    private String trangThai;

    public PhongDTO() {}

<<<<<<< HEAD
    public PhongDTO(String maPhong, String maLoaiPhong, String tenLoaiPhong, int soGiuong, int giaTien, String trangThai) {
        this.maPhong = maPhong;
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.soGiuong = soGiuong;
        this.giaTien = giaTien;
=======
    public PhongDTO(String maPhong, String maLoaiPhong, String trangThai) {
        this.maPhong = maPhong;
        this.maLoaiPhong = maLoaiPhong;
>>>>>>> origin/Nhat2
        this.trangThai = trangThai;
    }

    public String getMaPhong() { return maPhong; }
    public String getMaLoaiPhong() { return maLoaiPhong; }
<<<<<<< HEAD
    public String getTenLoaiPhong() { return tenLoaiPhong; }
    public int getSoGiuong() { return soGiuong; }
    public int getGiaTien() { return giaTien; }
=======
>>>>>>> origin/Nhat2
    public String getTrangThai() { return trangThai; }

    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
<<<<<<< HEAD
    public void setTenLoaiPhong(String tenLoaiPhong) { this.tenLoaiPhong = tenLoaiPhong; }
    public void setSoGiuong(int soGiuong) { this.soGiuong = soGiuong; }
    public void setGiaTien(int giaTien) { this.giaTien = giaTien; }
=======
>>>>>>> origin/Nhat2
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
