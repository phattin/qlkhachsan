package GUI;

import fillter.Button;
import fillter.Colors;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
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

        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        AddBtn.addActionListener(e -> addNewCustomer());
        
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(txtSearch);
        
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

        loadTableData();
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
            }
        }
    }
}
