/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Thang Nguyen
 */
public class TaiKhoanDTO {

    private String tenDangNhap;
    private String matkhau;
    private String maNhanVien;
    private String maChucVu;
    private String trangthai;

    public TaiKhoanDTO() {
        tenDangNhap = "";
        matkhau = "";
        maNhanVien = "";
        maChucVu = "";
        trangthai = "Hiện";
    }

    public TaiKhoanDTO(String tenDangNhap, String matkhau, String maNhanVien, String maChucVu, String trangthai) {
        this.tenDangNhap = tenDangNhap;
        this.matkhau = matkhau;
        this.maNhanVien = maNhanVien;
        this.maChucVu = maChucVu;
        this.trangthai = trangthai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matkhau;
    }

    public void setMatKhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTrangThai() {
        return trangthai;
    }

    public void setTrangThai(String trangthai) {
        this.trangthai = trangthai;
    }
}
