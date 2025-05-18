package DTO;

public class SuDungDichVuDTO {
    private String maSDDV; // Chuyển từ int sang String
    private String maDP;   // Chuyển từ int sang String
    private String maDv;   // Chuyển từ int sang String
    private int soLuong;
    
    public SuDungDichVuDTO(){
        
    }

    public SuDungDichVuDTO(String maSDDV, String maDP, String maDv, int soLuong) {
        this.maSDDV = maSDDV;
        this.maDP = maDP;
        this.maDv = maDv;
        this.soLuong = soLuong;
    }

    public String getMaSDDV() {
        return maSDDV;
    }

    public String getMaDP() {
        return maDP;
    }

    public String getMaDv() {
        return maDv;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setMaSDDV(String maSDDV) {
        this.maSDDV = maSDDV;
    }

    public void setMaDP(String maDP) {
        this.maDP = maDP;
    }

    public void setMaDv(String maDv) {
        this.maDv = maDv;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
