package GUI;

import fillter.Button;
import fillter.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
import GUI.Dialog.AddCustomerGUI;

import java.awt.*;
import java.util.ArrayList;

public class KhachHangGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    private DefaultTableModel tableModel;
    private KhachHangBUS khachHangBUS;

    public KhachHangGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        khachHangBUS = new KhachHangBUS();

        // Header
        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        // Thêm chức năng tìm kiếm
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }
        });

        PanelHeader.add(AddBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(txtSearch);

        // Content
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã KH", "Họ Tên", "CCCD", "SĐT", "Email", "Địa Chỉ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        // Load table
        loadTableData();

        // Button events
        AddBtn.addActionListener(e -> addNewCustomer());
        EditBtn.addActionListener(e -> editCustomer());
        DeleteBtn.addActionListener(e -> deleteCustomer());
    }

    // Thêm phương thức tìm kiếm
    private void timKiem() {
        String tuKhoa = txtSearch.getText().toLowerCase().trim();
        if (tuKhoa.isEmpty()) {
            loadTableData();  // Nếu ô tìm kiếm trống, hiển thị tất cả dữ liệu
            return;
        }

        tableModel.setRowCount(0);
        ArrayList<KhachHangDTO> danhSachKhachHang = khachHangBUS.getAllKhachHang();

        for (KhachHangDTO kh : danhSachKhachHang) {
            boolean timThay = false;
            
            // Tìm kiếm trên tất cả các cột
            if (kh.getMaKhachHang().toLowerCase().contains(tuKhoa) ||
                kh.getHoTen().toLowerCase().contains(tuKhoa) ||
                kh.getCCCD().toLowerCase().contains(tuKhoa) ||
                kh.getSDT().toLowerCase().contains(tuKhoa) ||
                (kh.getEmail() != null && kh.getEmail().toLowerCase().contains(tuKhoa)) ||
                (kh.getDiaChi() != null && kh.getDiaChi().toLowerCase().contains(tuKhoa))) {
                timThay = true;
            }
            
            if (timThay) {
                tableModel.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getHoTen(),
                    kh.getCCCD(),
                    kh.getSDT(),
                    kh.getEmail(),
                    kh.getDiaChi()
                });
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<KhachHangDTO> danhSachKhachHang = khachHangBUS.getAllKhachHang();

        for (KhachHangDTO kh : danhSachKhachHang) {
            tableModel.addRow(new Object[]{
                kh.getMaKhachHang(),
                kh.getHoTen(),
                kh.getCCCD(),
                kh.getSDT(),
                kh.getEmail(),
                kh.getDiaChi()
            });
        }
    }

    private void addNewCustomer() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddCustomerGUI(parentFrame, khachHangBUS, "Thêm khách hàng", "add");
        loadTableData();
    }

    private void editCustomer() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        String tenKH = tableModel.getValueAt(selectedRow, 1).toString();
        String cccd = tableModel.getValueAt(selectedRow, 2).toString();
        String sdt = tableModel.getValueAt(selectedRow, 3).toString();
        String email = tableModel.getValueAt(selectedRow, 4).toString();
        String diaChi = tableModel.getValueAt(selectedRow, 5).toString();

        KhachHangDTO kh = new KhachHangDTO(maKH, tenKH, cccd, sdt, email, diaChi);

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddCustomerGUI(parentFrame, khachHangBUS, "Chỉnh sửa khách hàng", "save", kh);
        loadTableData(); // Reload sau chỉnh sửa
    }

    private void deleteCustomer() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng [" + maKH + "] không?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangBUS.deleteKhachHang(maKH)) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}