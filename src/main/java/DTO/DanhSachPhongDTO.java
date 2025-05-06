package DTO;

public class DanhSachPhongDTO {
    private String maDSP;
    private String maDatPhong;
    private String maPhong;
    
    // Constructor không tham số
    public DanhSachPhongDTO() {
    }
    
    // Constructor đầy đủ tham số
    public DanhSachPhongDTO(String maDSP, String maDatPhong, String maPhong) {
        this.maDSP = maDSP;
        this.maDatPhong = maDatPhong;
        this.maPhong = maPhong;
    }
    
    // Getter và Setter
    public String getMaDSP() {
        return maDSP;
    }
    
    public void setMaDSP(String maDSP) {
        this.maDSP = maDSP;
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
    
    @Override
    public String toString() {
        return "DanhSachPhongDTO{" +
                "maDSP='" + maDSP + '\'' +
                ", maDatPhong='" + maDatPhong + '\'' +
                ", maPhong='" + maPhong + '\'' +
                '}';
    }
}
