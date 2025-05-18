package DTO;

import java.sql.Date;

public class HoaDonDTO {
    private String MaHoaDon;
    private String MaDatPhong;
    private String MaNhanVien;
    private Date NgayTao;
    private double TongTien;
    private double TienTra;
    private double TienThua;
    private String TrangThai;

    public HoaDonDTO() {}

    public HoaDonDTO(String MaHoaDon, String MaDatPhong, String MaNhanVien, 
                    Date NgayTao, double TongTien, double TienTra, double TienThua, String TrangThai) {
        this.MaHoaDon = MaHoaDon;
        this.MaDatPhong = MaDatPhong;
        this.MaNhanVien = MaNhanVien;
        this.NgayTao = NgayTao;
        this.TongTien = TongTien;
        this.TienTra = TienTra;
        this.TienThua = TienThua;
        this.TrangThai = TrangThai;
    }

    public String getMaHoaDon() { return MaHoaDon; }
    public void setMaHoaDon(String MaHoaDon) { this.MaHoaDon = MaHoaDon; }

    public String getMaDatPhong() { return MaDatPhong; }
    public void setMaDatPhong(String MaDatPhong) { this.MaDatPhong = MaDatPhong; }

    public String getMaNhanVien() { return MaNhanVien; }
    public void setMaNhanVien(String MaNhanVien) { this.MaNhanVien = MaNhanVien; }

    public Date getNgayTao() { return NgayTao; }
    public void setNgayTao(Date NgayTao) { this.NgayTao = NgayTao; }

    public double getTongTien() { return TongTien; }
    public void setTongTien(double TongTien) { this.TongTien = TongTien; }

    public double getTienTra() { return TienTra; }
    public void setTienTra(double TienTra) { this.TienTra = TienTra; }

    public double getTienThua() { return TienThua; }
    public void setTienThua(double TienThua) { this.TienThua = TienThua; }

    public String getTrangThai() { return TrangThai; }
    public void setTrangThai(String TrangThai) { this.TrangThai = TrangThai; }

}