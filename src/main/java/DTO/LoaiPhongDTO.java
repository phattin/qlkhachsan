
package DTO;


public class LoaiPhongDTO {
    private int MaLoaiPhong;
    private String TenLoaiPhong;
    private int SoGiuong;
    private int GiaPhong;
    
    public LoaiPhongDTO(){        
    }
    
    public LoaiPhongDTO(int MaLoaiPhong, String TenLoaiPhong, int SoGiuong, int GiaPhong){
        this.MaLoaiPhong = MaLoaiPhong;
        this.TenLoaiPhong = TenLoaiPhong;
        this.SoGiuong = SoGiuong;
        this.GiaPhong = GiaPhong;
    }

    public int getMaLoaiPhong() {
        return MaLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return TenLoaiPhong;
    }

    public int getSoGiuong() {
        return SoGiuong;
    }

    public int getGiaPhong() {
        return GiaPhong;
    }

    public void setMaLoaiPhong(int MaLoaiPhong) {
        this.MaLoaiPhong = MaLoaiPhong;
    }

    public void setTenLoaiPhong(String TenLoaiPhong) {
        this.TenLoaiPhong = TenLoaiPhong;
    }

    public void setSoGiuong(int SoGiuong) {
        this.SoGiuong = SoGiuong;
    }

    public void setGiaPhong(int GiaPhong) {
        this.GiaPhong = GiaPhong;
    }
}
