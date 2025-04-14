package DTO;

public class LoaiPhongDTO {
<<<<<<< HEAD
    private int maLoaiPhong;
    private String tenLoaiPhong;
    private int soGiuong;
    private int giaPhong;
=======
    private String maLoaiPhong;
    private String tenLoaiPhong;
    private int soGiuong;
    private double giaPhong;
>>>>>>> origin/Nhat2
    
    public LoaiPhongDTO(){
        
    }
    
<<<<<<< HEAD
    public LoaiPhongDTO(int maLoaiPhong, String tenLoaiPhong, int soGiuong, int giaPhong){
=======
    public LoaiPhongDTO(String maLoaiPhong, String tenLoaiPhong, int soGiuong, double giaPhong){
>>>>>>> origin/Nhat2
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.soGiuong = soGiuong;
        this.giaPhong = giaPhong;
    }

<<<<<<< HEAD
    public int getMaLoaiPhong() {
=======
    public String getMaLoaiPhong() {
>>>>>>> origin/Nhat2
        return maLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public int getSoGiuong() {
        return soGiuong;
    }

<<<<<<< HEAD
    public int getGiaPhong() {
        return giaPhong;
    }

    public void setMaLoaiPhong(int maLoaiPhong) {
=======
    public double getGiaPhong() {
        return giaPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
>>>>>>> origin/Nhat2
        this.maLoaiPhong = maLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    public void setSoGiuong(int soGiuong) {
        this.soGiuong = soGiuong;
    }

<<<<<<< HEAD
    public void setGiaPhong(int giaPhong) {
        this.giaPhong = giaPhong;
    }
    
=======
    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }
    
    
>>>>>>> origin/Nhat2
}
