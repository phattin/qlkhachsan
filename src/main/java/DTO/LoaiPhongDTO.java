<<<<<<< HEAD

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
=======
package DTO;

public class LoaiPhongDTO {
    private int maLoaiPhong;
    private String tenLoaiPhong;
    private int soGiuong;
    private int giaPhong;
    
    public LoaiPhongDTO(){
        
    }
    
    public LoaiPhongDTO(int maLoaiPhong, String tenLoaiPhong, int soGiuong, int giaPhong){
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.soGiuong = soGiuong;
        this.giaPhong = giaPhong;
    }

    public int getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public int getSoGiuong() {
        return soGiuong;
    }

    public int getGiaPhong() {
        return giaPhong;
    }

    public void setMaLoaiPhong(int maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    public void setSoGiuong(int soGiuong) {
        this.soGiuong = soGiuong;
    }

    public void setGiaPhong(int giaPhong) {
        this.giaPhong = giaPhong;
    }
    
    
>>>>>>> Nhat1
}
