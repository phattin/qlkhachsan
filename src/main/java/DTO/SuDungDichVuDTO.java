
package DTO;

public class SuDungDichVuDTO {
    private int maSDDV;
    private int maDP;
    private int maDv;
    private int soLuong;
    
    public SuDungDichVuDTO(){
        
    }

    public SuDungDichVuDTO(int maSDDV, int maDP, int maDv, int soLuong) {
        this.maSDDV = maSDDV;
        this.maDP = maDP;
        this.maDv = maDv;
        this.soLuong = soLuong;
    }

    public int getMaSDDV() {
        return maSDDV;
    }

    public int getMaDP() {
        return maDP;
    }

    public int getMaDv() {
        return maDv;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setMaSDDV(int maSDDV) {
        this.maSDDV = maSDDV;
    }

    public void setMaDP(int maDP) {
        this.maDP = maDP;
    }

    public void setMaDv(int maDv) {
        this.maDv = maDv;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    
}
