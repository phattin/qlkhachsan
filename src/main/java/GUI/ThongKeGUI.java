package GUI;

import BUS.ThongKeBUS;
import DTO.ThongKeDTO;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ThongKeGUI extends JPanel {
    private JComboBox<String> cbThongKeTheo, cbNam;
    private JButton btnThongKe;
    private JPanel chartPanel;
    private JLabel lblThongTinTong;
    private ThongKeBUS bus = new ThongKeBUS();

    public ThongKeGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        initUI();
    }

    private void initUI() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controlPanel.setBackground(new Color(240, 248, 255));

        cbThongKeTheo = new JComboBox<>(new String[]{"Thống kê theo tháng", "Thống kê theo năm"});

        cbNam = new JComboBox<>();
        for (int year = 2020; year <= 2025; year++) cbNam.addItem(String.valueOf(year));
        cbNam.setSelectedItem(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setBackground(new Color(70, 130, 180));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);

        btnThongKe.addActionListener(e -> thongKe());
        cbThongKeTheo.addActionListener(e -> {
            String loai = (String) cbThongKeTheo.getSelectedItem();
            
            cbNam.setEnabled(loai.equals("Thống kê theo tháng"));
        });

        controlPanel.add(new JLabel("Chọn loại thống kê:"));
        controlPanel.add(cbThongKeTheo);
        controlPanel.add(new JLabel("Chọn năm:"));
        controlPanel.add(cbNam);
        controlPanel.add(btnThongKe);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);

        lblThongTinTong = new JLabel("Tổng doanh thu: ", JLabel.CENTER);
        lblThongTinTong.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        lblThongTinTong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblThongTinTong.setForeground(new Color(60, 60, 60));

        add(controlPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(lblThongTinTong, BorderLayout.SOUTH);
    }

    private void thongKe() {
        String loai = (String) cbThongKeTheo.getSelectedItem();
        int nam = Integer.parseInt((String) cbNam.getSelectedItem());

        ArrayList<ThongKeDTO> ds;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String title = "";

        if (loai.equals("Thống kê theo tháng")) {
            ds = bus.layThongKeTheoThang(nam);
            for (ThongKeDTO tk : ds) {
                dataset.addValue(tk.getTongDoanhThu(), "Doanh thu", "Tháng " + tk.getThangOrNam());
            }
            title = "Doanh thu theo tháng năm " + nam;
        } else {
            ds = bus.layThongKeTheoNam();
            for (ThongKeDTO tk : ds) {
                dataset.addValue(tk.getTongDoanhThu(), "Doanh thu", "Năm " + tk.getThangOrNam());
            }
            title = "Doanh thu các năm";
        }
        // Tạo biểu đồ
        JFreeChart chart = ChartFactory.createBarChart(
            title,
            loai.equals("Thống kê theo tháng") ? "Tháng" : "Năm",
            "Doanh thu (VND)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        // Tuỳ chỉnh biểu đồ
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 20));
        chart.getTitle().setPaint(new Color(33, 37, 41));

        var plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(200, 200, 200));

        // Trục X
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 13));
        plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
        plot.getDomainAxis().setCategoryMargin(0.2);
        
        // Trục Y
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 13));
        plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));

        // Màu cột và độ rộng
        var renderer = new org.jfree.chart.renderer.category.BarRenderer();
        renderer.setSeriesPaint(0, new Color(100, 149, 237));
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.1); 
        renderer.setItemMargin(0.05);

        // Bo góc cho cột
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        plot.setRenderer(renderer);

        // Hiển thị
        chartPanel.removeAll();
        chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();

        // Tổng doanh thu + số lượng mục
        double tong = bus.tinhTongDoanhThu(ds);
        String chiTiet = (loai.equals("Thống kê theo tháng"))
                ? "năm " + nam + " (" + ds.size() + " tháng)"
                : "(" + ds.size() + " năm)";
        lblThongTinTong.setText("Tổng doanh thu " + chiTiet + ": " + String.format("%,.0f VND", tong));
        lblThongTinTong.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    }
}
