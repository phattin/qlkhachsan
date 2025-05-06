package DTO;

public class ThongKeDTO {
    private int thangOrNam;
    private double tongDoanhThu;

    public ThongKeDTO(int thangOrNam, double tongDoanhThu) {
        this.thangOrNam = thangOrNam;
        this.tongDoanhThu = tongDoanhThu;
    }

    public int getThangOrNam() {
        return thangOrNam;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }
}