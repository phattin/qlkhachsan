package GUI;

import fillter.Button;
import fillter.Colors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import BUS.HoaDonBUS;
import DTO.HoaDonDTO;

public class HoaDonGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JComboBox<String> CBFilter;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;

    private DefaultTableModel tableModel;
    private HoaDonBUS hoaDonBUS;

    public HoaDonGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        hoaDonBUS = new HoaDonBUS(); // Không cần Connection

        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        AddBtn.addActionListener(e -> addNewHoaDon());
        EditBtn.addActionListener(e -> editHoaDon());
        DeleteBtn.addActionListener(e -> deleteHoaDon());

        CBFilter = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});
        CBFilter.setPreferredSize(new Dimension(120, 35));

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(CBFilter);
        PanelHeader.add(txtSearch);

        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã Hóa Đơn", "Mã NV", "Ngày Tạo", "Tổng Tiền", "Tiền Trả", "Tiền Thừa", "Trạng Thái"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<HoaDonDTO> danhSachHoaDon = hoaDonBUS.layDanhSachHoaDon();

        for (HoaDonDTO hoaDon : danhSachHoaDon) {
            tableModel.addRow(new Object[]{
                hoaDon.getMaHoaDon(),
                hoaDon.getMaNhanVien(),
                hoaDon.getNgayTao(),
                hoaDon.getTongTien(),
                hoaDon.getTienTra(),
                hoaDon.getTienThua(),
                hoaDon.getTrangThai()
            });
        }
    }

    private void addNewHoaDon() {
        try {

            String maHD = JOptionPane.showInputDialog(this, "Nhập Mã Hóa Đơn:");
            String maNV = JOptionPane.showInputDialog(this, "Nhập Mã Nhân Viên:");
            String ngayTaoStr = JOptionPane.showInputDialog(this, "Nhập Ngày Tạo (yyyy-mm-dd):");
            String tongTienStr = JOptionPane.showInputDialog(this, "Nhập Tổng Tiền:");
            String tienTraStr = JOptionPane.showInputDialog(this, "Nhập Tiền Trả:");
            String tienThuaStr = JOptionPane.showInputDialog(this, "Nhập Tiền Thừa:");
            String trangThai = JOptionPane.showInputDialog(this, "Nhập Trạng Thái:");

            HoaDonDTO hoaDon = new HoaDonDTO(
                maHD,
                maNV,
                java.sql.Date.valueOf(ngayTaoStr),
                Double.parseDouble(tongTienStr),
                Double.parseDouble(tienTraStr),
                Double.parseDouble(tienThuaStr),
                trangThai
            );

            hoaDonBUS.themHoaDon(hoaDon);
            JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editHoaDon() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String maHD = tableModel.getValueAt(selectedRow, 0).toString();
            String maNV = tableModel.getValueAt(selectedRow, 1).toString();
            java.util.Date utilNgayTao = (java.util.Date) tableModel.getValueAt(selectedRow, 2);
            java.sql.Date ngayTao = new java.sql.Date(utilNgayTao.getTime());
            double tongTien = Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString());
            double tienTra = Double.parseDouble(tableModel.getValueAt(selectedRow, 4).toString());
            double tienThua = Double.parseDouble(tableModel.getValueAt(selectedRow, 5).toString());
            String trangThai = tableModel.getValueAt(selectedRow, 6).toString();

            HoaDonDTO hoaDon = new HoaDonDTO(maHD, maNV, ngayTao, tongTien, tienTra, tienThua, trangThai);

            // Sửa xong thì show input dialog
            String newTrangThai = JOptionPane.showInputDialog(this, "Nhập Trạng Thái mới:", trangThai);
            if (newTrangThai != null && !newTrangThai.isEmpty()) {
                hoaDon.setTrangThai(newTrangThai);
            }

            hoaDonBUS.capnhatHoaDon(hoaDon);
            JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thành công!");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteHoaDon() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hóa đơn [" + maHD + "] không?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (hoaDonBUS.xoaHoaDon(maHD)) {
                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
