package DTO;

public class LoaiPhongDTO {
    private String maLoaiPhong;
    private String tenLoaiPhong;
    private int soGiuong;
    private double giaPhong;
    
    public LoaiPhongDTO(){
        
    }
    
    public LoaiPhongDTO(String maLoaiPhong, String tenLoaiPhong, int soGiuong, double giaPhong){
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.soGiuong = soGiuong;
        this.giaPhong = giaPhong;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public int getSoGiuong() {
        return soGiuong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    public void setSoGiuong(int soGiuong) {
        this.soGiuong = soGiuong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }
    
    
}