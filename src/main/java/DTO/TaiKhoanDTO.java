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
    private String maNV;
    private String maChucVu;
    private String trangthai;

    public TaiKhoanDTO() {
        tenDangNhap = "";
        matkhau = "";
        maNV = "";
        maChucVu = "";
        trangthai = "Hiện";
    }

    public TaiKhoanDTO(String tenDangNhap, String matkhau, String maNV, String maChucVu, String trangthai) {
        this.tenDangNhap = tenDangNhap;
        this.matkhau = matkhau;
        this.maNV = maNV;
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

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}
