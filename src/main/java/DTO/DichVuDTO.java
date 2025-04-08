package DTO;

public class DichVuDTO {
    private String maDichVu;
    private String tenDichVu;
    private int giaDichVu;
    
    public DichVuDTO() {}

    public DichVuDTO(String maDichVu, String tenDichVu, int giaDichVu) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.giaDichVu = giaDichVu;
    }

    public String getMaDichVu() {
        return maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public int getGiaDichVu() {
        return giaDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        this.maDichVu = maDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public void setGiaDichVu(int giaDichVu) {
        this.giaDichVu = giaDichVu;
    }
}
