package GUI;

import fillter.Button;
import fillter.Colors;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;

import java.awt.*;
import java.util.ArrayList;

public class KhachHangGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    private DefaultTableModel tableModel;
    private KhachHangBUS khachHangBUS;
    private JComboBox<String> cbSearch; // Thêm combobox để chọn tiêu chí tìm kiếm

    public KhachHangGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        khachHangBUS = new KhachHangBUS();

        // Header
        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120)); // Tăng chiều cao để chứa 2 hàng

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Colors.MAIN_BACKGROUND);

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        buttonPanel.add(AddBtn);
        buttonPanel.add(DeleteBtn);
        buttonPanel.add(EditBtn);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Colors.MAIN_BACKGROUND);

        // Tạo panel con để bao phủ ô tìm kiếm và comboBox
        JPanel panelSearchFields = new JPanel();
        panelSearchFields.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelSearchFields.setBackground(Colors.MAIN_BACKGROUND);
        panelSearchFields.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Tìm Kiếm",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        cbSearch = new JComboBox<>(new String[]{"Mã KH", "Họ tên", "CCCD", "SĐT", "Email", "Địa chỉ"});
        cbSearch.setPreferredSize(new Dimension(100, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        searchPanel.add(panelSearchFields);

        // Thêm cả hai panel vào PanelHeader
        PanelHeader.add(buttonPanel, BorderLayout.NORTH);
        PanelHeader.add(searchPanel, BorderLayout.SOUTH);

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
        AddBtn.addActionListener(e -> themKhachHang());
        EditBtn.addActionListener(e -> suaKhachHang());
        DeleteBtn.addActionListener(e -> xoaKhachHang());
        
        // Khi thay đổi cbSearch
        cbSearch.addActionListener(e -> timKiem());

        // Khi người dùng nhập liệu vào txtSearch
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
        });
    }

    // Phương thức tìm kiếm
    private void timKiem() {
        String kieuTim = cbSearch.getSelectedItem().toString();
        String tuKhoa = txtSearch.getText().trim().toLowerCase();
        
        if (tuKhoa.isEmpty()) {
            loadTableData();  // Nếu ô tìm kiếm trống, hiển thị tất cả dữ liệu
            return;
        }

        tableModel.setRowCount(0);
        ArrayList<KhachHangDTO> danhSachKhachHang = khachHangBUS.getAllKhachHang();

        for (KhachHangDTO kh : danhSachKhachHang) {
            boolean timThay = false;
            
            switch (kieuTim) {
                case "Mã KH":
                    timThay = kh.getMaKhachHang().toLowerCase().contains(tuKhoa);
                    break;
                case "Họ tên":
                    timThay = kh.getHoTen().toLowerCase().contains(tuKhoa);
                    break;
                case "CCCD":
                    timThay = kh.getCCCD().toLowerCase().contains(tuKhoa);
                    break;
                case "SĐT":
                    timThay = kh.getSDT().toLowerCase().contains(tuKhoa);
                    break;
                case "Email":
                    timThay = (kh.getEmail() != null && kh.getEmail().toLowerCase().contains(tuKhoa));
                    break;
                case "Địa chỉ":
                    timThay = (kh.getDiaChi() != null && kh.getDiaChi().toLowerCase().contains(tuKhoa));
                    break;
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

    // Phương thức thêm khách hàng (tích hợp từ AddCustomerGUI)
    private void themKhachHang() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm khách hàng", true);
        dialog.setSize(550, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Các trường nhập liệu
        JTextField txtMaKH = new JTextField(khachHangBUS.increaseMaKH());
        txtMaKH.setEditable(false);
        JTextField txtHoTen = new JTextField();
        JTextField txtCCCD = new JTextField();
        JTextField txtSoDienThoai = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();
        
        // Thêm các trường vào panel
        inputPanel.add(new JLabel("Mã Khách Hàng:"));
        inputPanel.add(txtMaKH);
        inputPanel.add(new JLabel("Họ Tên:"));
        inputPanel.add(txtHoTen);
        inputPanel.add(new JLabel("CCCD:"));
        inputPanel.add(txtCCCD);
        inputPanel.add(new JLabel("Số Điện Thoại:"));
        inputPanel.add(txtSoDienThoai);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiaChi);

        // Panel nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button btnAdd = new Button("add", "THÊM", 90, 35);
        Button btnCancel = new Button("cancel", "HỦY", 90, 35);

        btnPanel.add(btnAdd);
        btnPanel.add(btnCancel);

        // Thêm các panel vào dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // Action listeners
        btnCancel.addActionListener(e -> dialog.dispose());
        btnAdd.addActionListener(e -> {
            // Kiểm tra form
            String maKH = txtMaKH.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();
            String diaChi = txtDiaChi.getText().trim();

            ArrayList<KhachHangDTO> danhSachKhachHang = khachHangBUS.getAllKhachHang();
            for (KhachHangDTO kh : danhSachKhachHang) {
                if (kh.getCCCD().equals(cccd)) {
                    JOptionPane.showMessageDialog(dialog, "CCCD đã tồn tại cho khách hàng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (kh.getSDT().equals(sdt)) {
                    JOptionPane.showMessageDialog(dialog, "SDT đã tồn tại cho khách hàng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Kiểm tra các trường
            if (hoTen.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || email.isEmpty() || diaChi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (hoTen.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(dialog, "Họ tên không được chứa số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!cccd.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "CCCD phải có đúng 10 số và bắt đầu bằng số 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!sdt.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "Số điện thoại phải có đúng 10 số và bắt đầu bằng số 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w.-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(dialog, "Email phải có đuôi @gmail.com!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }         
            // Tạo DTO và thêm vào CSDL
            KhachHangDTO khachHang = new KhachHangDTO(maKH, hoTen, cccd, sdt, email, diaChi);
            if (khachHangBUS.addKhachHang(khachHang)) {
                JOptionPane.showMessageDialog(dialog, "Thêm khách hàng thành công!");
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Mã khách hàng đã tồn tại hoặc dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // Phương thức sửa khách hàng (tích hợp từ AddCustomerGUI)
    private void suaKhachHang() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin khách hàng từ bảng
        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        String tenKH = tableModel.getValueAt(selectedRow, 1).toString();
        String cccd = tableModel.getValueAt(selectedRow, 2).toString();
        String sdt = tableModel.getValueAt(selectedRow, 3).toString();
        String email = tableModel.getValueAt(selectedRow, 4).toString();
        String diaChi = tableModel.getValueAt(selectedRow, 5).toString();

        // Tạo dialog sửa khách hàng
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chỉnh sửa khách hàng", true);
        dialog.setSize(550, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Các trường nhập liệu
        JTextField txtMaKH = new JTextField(maKH);
        txtMaKH.setEditable(false); // Không cho sửa mã khách hàng
        JTextField txtHoTen = new JTextField(tenKH);
        JTextField txtCCCD = new JTextField(cccd);
        JTextField txtSoDienThoai = new JTextField(sdt);
        JTextField txtEmail = new JTextField(email);
        JTextField txtDiaChi = new JTextField(diaChi);
        
        // Thêm các trường vào panel
        inputPanel.add(new JLabel("Mã Khách Hàng:"));
        inputPanel.add(txtMaKH);
        inputPanel.add(new JLabel("Họ Tên:"));
        inputPanel.add(txtHoTen);
        inputPanel.add(new JLabel("CCCD:"));
        inputPanel.add(txtCCCD);
        inputPanel.add(new JLabel("Số Điện Thoại:"));
        inputPanel.add(txtSoDienThoai);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiaChi);

        // Panel nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button btnSave = new Button("confirm", "LƯU", 90, 35);
        Button btnCancel = new Button("cancel", "HỦY", 90, 35);

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        // Thêm các panel vào dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // Action listeners
        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            // Kiểm tra form
            String hoTenMoi = txtHoTen.getText().trim();
            String cccdMoi = txtCCCD.getText().trim();
            String sdtMoi = txtSoDienThoai.getText().trim();
            String emailMoi = txtEmail.getText().trim();
            String diaChiMoi = txtDiaChi.getText().trim();

            ArrayList<KhachHangDTO> danhSachKhachHang = khachHangBUS.getAllKhachHang();
            for (KhachHangDTO kh : danhSachKhachHang) {
                if (kh.getCCCD().equals(cccdMoi) && !kh.getMaKhachHang().equals(maKH)) {
                    JOptionPane.showMessageDialog(dialog, "CCCD đã tồn tại cho khách hàng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (kh.getSDT().equals(sdtMoi) && !kh.getMaKhachHang().equals(maKH)) {
                    JOptionPane.showMessageDialog(dialog, "SDT đã tồn tại cho khách hàng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Kiểm tra các trường
            if (hoTenMoi.isEmpty() || cccdMoi.isEmpty() || sdtMoi.isEmpty() || emailMoi.isEmpty() || diaChiMoi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (hoTenMoi.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(dialog, "Họ tên không được chứa số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!cccdMoi.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "CCCD phải có đúng 10 số và bắt đầu bằng số 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!sdtMoi.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "Số điện thoại phải có đúng 10 số và bắt đầu bằng số 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!emailMoi.matches("^[\\w.-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(dialog, "Email phải có đuôi @gmail.com!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tạo DTO và cập nhật vào CSDL
            KhachHangDTO khachHang = new KhachHangDTO(maKH, hoTenMoi, cccdMoi, sdtMoi, emailMoi, diaChiMoi);
            if (khachHangBUS.updateKhachHang(khachHang)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật khách hàng thành công!");
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // Phương thức xóa khách hàng
    private void xoaKhachHang() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        String tenKH = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa khách hàng [" + maKH + " - " + tenKH + "] không?", 
                "Xác nhận", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangBUS.deleteKhachHang(maKH)) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Xóa khách hàng thất bại! Khách hàng có thể đang có liên kết đến bảng khác.", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}