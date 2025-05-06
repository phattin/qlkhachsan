package DTO;

public class NhanVienDTO {
    private String maNhanVien;
    private String hoTen;
    private String gioiTinh;
    private String sdt;
    private String email;
    private String diaChi;
    private long luong;
    private String ngayNhanViec;
    private String trangThai;

    public NhanVienDTO() {
    }

    public NhanVienDTO(String maNhanVien, String hoTen, String gioiTinh, String sdt, String email, String diaChi, long luong, String ngayNhanViec, String trangThai) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.luong = luong;
        this.ngayNhanViec = ngayNhanViec;
        this.trangThai = trangThai;
    }
    
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSDT() {
        return sdt;
    }

    public void setSDT(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public long getLuong() {
        return luong;
    }

    public void setLuong(long luong) {
        this.luong = luong;
    }

    public String getNgayNhanViec() {
        return ngayNhanViec;
    }

    public void setNgayNhanViec(String ngayNhanViec) {
        this.ngayNhanViec = ngayNhanViec;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
