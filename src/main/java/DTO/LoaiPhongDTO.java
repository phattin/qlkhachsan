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
    
    
}
