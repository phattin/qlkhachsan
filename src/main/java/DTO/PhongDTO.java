
package DTO;


public class PhongDTO {
    private int MaPhong;
    private int MaLoaiPhong;
    private String TrangThai;
    
    public PhongDTO(){        
    }
    
    public PhongDTO(int MaPhong, int MaLoaiPhong, String TrangThai){
        this.MaPhong = MaPhong;
        this.MaLoaiPhong = MaLoaiPhong;
        this.TrangThai = TrangThai;
    }
    
    public int getMaPhong(){
        return MaPhong;
    }
    
    public int getMaLoaiPhong(){
        return MaLoaiPhong;
    }
    
    public String getTrangThai(){
        return TrangThai;
    }
    
    public void setMaPhong(int MaPhong){
        this.MaPhong = MaPhong;
    }
    
    public void setMaLoaiPhong(int MaLoaiPhong){
        this.MaLoaiPhong = MaLoaiPhong;
    }
    
    public void setTrangThai(String TrangThai){
        this.TrangThai = TrangThai;
    }
    
    @Override
    public String toString(){
        return "PhongDTO{" + "MaPhong=" + MaPhong +", MaLoaiPhong=" + MaLoaiPhong + ", TrangThai=" + TrangThai +"}";
    }
}
