package GUI;

import fillter.Button;
import fillter.Colors;
<<<<<<< HEAD
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
=======

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
import GUI.Dialog.AddCustomerGUI;

import java.awt.*;
>>>>>>> origin/Nhat2
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

<<<<<<< HEAD
=======
        // Header
>>>>>>> origin/Nhat2
        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
<<<<<<< HEAD
        AddBtn.addActionListener(e -> addNewCustomer());
        
=======
>>>>>>> origin/Nhat2
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        PanelHeader.add(AddBtn);
<<<<<<< HEAD
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(txtSearch);
        
=======
        PanelHeader.add(EditBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(txtSearch);

        // Content
>>>>>>> origin/Nhat2
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

<<<<<<< HEAD
        loadTableData();
=======
        // Load table
        loadTableData();

        // Button events
        AddBtn.addActionListener(e -> addNewCustomer());
        EditBtn.addActionListener(e -> editCustomer());
        DeleteBtn.addActionListener(e -> deleteCustomer());
>>>>>>> origin/Nhat2
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<KhachHangDTO> danhSachKhachHang = khachHangBUS.getAllKhachHang();
<<<<<<< HEAD
    
=======

>>>>>>> origin/Nhat2
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
<<<<<<< HEAD
    
    private void addNewCustomer() {
        JTextField txtMaKH = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtCCCD = new JTextField();
        JTextField txtSoDienThoai = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();
    
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã KH:")); panel.add(txtMaKH);
        panel.add(new JLabel("Họ Tên:")); panel.add(txtHoTen);
        panel.add(new JLabel("CCCD:")); panel.add(txtCCCD);
        panel.add(new JLabel("Số Điện Thoại:")); panel.add(txtSoDienThoai);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Địa Chỉ:")); panel.add(txtDiaChi);
    
        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm Khách Hàng", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String maKH = txtMaKH.getText();
            String hoTen = txtHoTen.getText();
            String cccd = txtCCCD.getText();
            String soDienThoai = txtSoDienThoai.getText();
            String email = txtEmail.getText();
            String diaChi = txtDiaChi.getText();
    
            if (maKH.isEmpty() || hoTen.isEmpty() || cccd.isEmpty() || soDienThoai.isEmpty() || email.isEmpty() || diaChi.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            KhachHangDTO khachHang = new KhachHangDTO(maKH, hoTen, cccd, soDienThoai, email, diaChi);
            if (khachHangBUS.addKhachHang(khachHang)) {
                JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm khách hàng thất bại! Kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
=======

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
>>>>>>> origin/Nhat2
            }
        }
    }
}
