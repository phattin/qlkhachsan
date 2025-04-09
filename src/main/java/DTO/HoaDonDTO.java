package DTO

import java.util.Date;

public class HoaDonDTO {
    private String MaHoaDon;
    private String MsNhanVien;
    private Date NgayTao;
    private double TongTien;
    private double TienTra;
    private double TienThua;
    private String TrangThai;

    public HoaDonDTO() {}

    public HoaDonDTO(String MaHoaDon, String MaNhanVien, Date NgayTAo, double TongTien, double TienTra, double TienThua, String TrangThai) {
        this.MaHoaDon = MaHoaDon;
        this.MsNhanVien = MaNhanVien;
        this.NgayTao = NgayTAo;
        this.TongTien = TongTien;
        this.TienTra = TienTra;
        this.TienThua = TienThua;
        this.TrangThai = TrangThai;
    }

    public String getMaHoaDon() { return MaHoaDon;}
    public void setMaHoaDon(String MaHoaDon) { this.MaHoaDon = MaHoaDon;}

    public String getMaNhanVien() { return MsNhanVien;}
    public void setMaNhanVien(String MaNhanVien) { this.MaNhanVien = MaNhanVien;}

    public Date getNgayTao() { return NgayTao;}
    public void setNgayTao(Date NgayTao) { this.NgayTao = NgayTao;}

    public double getTongTien() { return TongTien;}
    public void setTongTien(double TongTien) { this.TongTien = TongTien;}

    public double getTienTra() { return TienTra;}
    public void setTienTra(double TienTra) { this.TienTra = TienTra;}

    public double getTienThua() { return TienThua;}
    public void setTienThua(double TienThua) { this.TienThua = TienThua;}

    public String getTrangThai() { return TrangThai;}
    public void setTrangThai(String TrangThai) { this.TrangThai = TrangThai;}
    

}