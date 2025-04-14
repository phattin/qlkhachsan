package DTO;

import java.sql.Date;

public class DatPhongDTO {
    private String maDatPhong;
    private String maPhong;
    private String maKH;
    private Date ngayNhanPhong;
    private Date ngayTraPhong;


    public DatPhongDTO() {}

    public DatPhongDTO(String maDatPhong, String maPhong, String maKH, Date ngayNhanPhong, Date ngayTraPhong) {
        this.maDatPhong = maDatPhong;
        this.maPhong = maPhong;
        this.maKH = maKH;
        this.ngayNhanPhong = ngayNhanPhong;
        this.ngayTraPhong = ngayTraPhong;

    }

    public String getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(String maDatPhong) {
        this.maDatPhong = maDatPhong;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public Date getNgayNhanPhong() {
        return ngayNhanPhong;
    }

    public void setNgayNhanPhong(Date ngayNhanPhong) {
        this.ngayNhanPhong = ngayNhanPhong;
    }

    public Date getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void setNgayTraPhong(Date ngayTraPhong) {
        this.ngayTraPhong = ngayTraPhong;
    }

<<<<<<< HEAD
=======

>>>>>>> origin/Nhat2
}