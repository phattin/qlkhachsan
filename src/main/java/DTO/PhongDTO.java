package DTO;

public class PhongDTO {
    private String maPhong;
    private String maLoaiPhong;
    private String trangThai;
    private int trangThaiXoa; 

    public PhongDTO() {}

    public PhongDTO(String maPhong, String maLoaiPhong, String trangThai, int trangThaiXoa) {
        this.maPhong = maPhong;
        this.maLoaiPhong = maLoaiPhong;
        this.trangThai = trangThai;
        this.trangThaiXoa = trangThaiXoa;
    }

    public String getMaPhong() { return maPhong; }
    public String getMaLoaiPhong() { return maLoaiPhong; }
    public String getTrangThai() { return trangThai; }
    public int getTrangThaiXoa() { return trangThaiXoa; }

    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public void setTrangThaiXoa(int trangThaiXoa) { this.trangThaiXoa = trangThaiXoa; }
}