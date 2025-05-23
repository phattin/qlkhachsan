package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import BUS.ChucVuBUS;
import BUS.NhanVienBUS;
import BUS.TaiKhoanBUS;
import DTO.ChucVuDTO;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import fillter.Button;
import fillter.Colors;

public class NhanVienGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch, txtLuongMin, txtLuongMax;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch;
    private DefaultTableModel tableModel;
    private NhanVienBUS nvBUS;
    private TaiKhoanBUS tkBUS;
    private JComboBox<String> cbGioiTinhFilter;
    private JDateChooser dateFrom, dateTo;

    public NhanVienGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        nvBUS = new NhanVienBUS();
        tkBUS = new TaiKhoanBUS();

        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120)); // cao hơn để chứa 2 hàng

        // Hàng trên: nút
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelButtons.setBackground(Colors.MAIN_BACKGROUND);

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        AddBtn.addActionListener(e -> themNhanVien());

        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        DeleteBtn.addActionListener(e -> xoaNhanVien());

        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        EditBtn.addActionListener(e -> suaNhanVien());

        panelButtons.add(AddBtn);
        panelButtons.add(DeleteBtn);
        panelButtons.add(EditBtn);

        // Hàng dưới: ô tìm kiếm
        pSearch = new JPanel();
        pSearch.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pSearch.setBackground(Colors.MAIN_BACKGROUND);

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

        cbSearch = new JComboBox<>(new String[]{"Mã NV", "Họ tên", "SĐT", "Email", "Địa chỉ"});
        cbSearch.setPreferredSize(new Dimension(80, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        // Thêm vào panel con
        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Panel lọc giới tính
        JPanel genderFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        genderFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        genderFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Giới tính",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        cbGioiTinhFilter = new JComboBox<>(new String[]{"Tất cả", "Nam", "Nữ"});
        cbGioiTinhFilter.setPreferredSize(new Dimension(80, 30));
        genderFilterPanel.add(cbGioiTinhFilter);

        // Panel lọc lương
        JPanel salaryFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        salaryFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        salaryFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Lương",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        txtLuongMin = new JTextField();
        txtLuongMax = new JTextField();
        txtLuongMin.setPreferredSize(new Dimension(100, 30));
        txtLuongMax.setPreferredSize(new Dimension(100, 30));
        salaryFilterPanel.add(new JLabel("Từ:"));
        salaryFilterPanel.add(txtLuongMin);
        salaryFilterPanel.add(new JLabel("Đến:"));
        salaryFilterPanel.add(txtLuongMax);

        // Panel lọc ngày nhận việc
        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        dateFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        dateFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Ngày Nhận Việc",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(110, 30));
        dateTo.setPreferredSize(new Dimension(110, 30));
        dateFrom.setDateFormatString("yyyy-MM-dd");
        dateTo.setDateFormatString("yyyy-MM-dd");
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setEditable(false);

        dateFilterPanel.add(new JLabel("Từ:"));
        dateFilterPanel.add(dateFrom);
        dateFilterPanel.add(new JLabel("Đến:"));
        dateFilterPanel.add(dateTo);

        // Thêm panel con vào pSearch
        pSearch.add(panelSearchFields);
        pSearch.add(genderFilterPanel);
        pSearch.add(salaryFilterPanel);
        pSearch.add(dateFilterPanel);

        // Thêm cả 2 hàng vào PanelHeader
        PanelHeader.add(panelButtons, BorderLayout.NORTH);
        PanelHeader.add(pSearch, BorderLayout.SOUTH);
        
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã NV", "Họ Tên", "Giới Tính", "SĐT", "Email", "Địa Chỉ", "Lương", "Ngày Nhận Việc"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData(NhanVienBUS.getAllNhanVien());

        // Khi thay đổi cbSearch
        cbSearch.addActionListener(e -> timKiemNhanVien());

        // Khi thay đổi cbGioiTinhFilter
        cbGioiTinhFilter.addActionListener(e -> timKiemNhanVien());

        // Khi người dùng nhập liệu vào txtSearch
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
        });

        // Khi nhập liệu vào txtLuongMin
        txtLuongMin.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
        });

        // Khi nhập liệu vào txtLuongMax
        txtLuongMax.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiemNhanVien(); }
        });

        // Khi chọn ngày trong dateFrom
        dateFrom.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiemNhanVien();
            }
        });

        // Khi chọn ngày trong dateTo
        dateTo.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiemNhanVien();
            }
        });
    }

    private void loadTableData(ArrayList<NhanVienDTO> danhSachNhanVien) {
    
        for (NhanVienDTO nv : danhSachNhanVien) {
            tableModel.addRow(new Object[]{
                nv.getMaNhanVien(),
                nv.getHoTen(),
                nv.getGioiTinh(),
                nv.getSDT(),
                nv.getEmail(),
                nv.getDiaChi(),
                nv.getLuong(),
                nv.getNgayNhanViec()
            });
        }
    }
    
    private void themNhanVien() {
        // Tạo các field
        JTextField txtMaNV = new JTextField(nvBUS.increaseMaNV()); 
        txtMaNV.setEditable(false);
        JTextField txtHoTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();
        JTextField txtLuong = new JTextField();
        
        // Chọn ngày
        JDateChooser dateChooser = new JDateChooser(); 
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");  // Định dạng ngày là yyyy-MM-dd
        ((JTextField) dateChooser.getDateEditor().getUiComponent()).setEditable(false);  // Vô hiệu hóa chỉnh sửa thủ công
    
        JTextField txtUsername = new JTextField();
        JTextField txtPassword = new JTextField();
    
        // Tạo danh sách chức vụ
        ArrayList<ChucVuDTO> dsChucVu = ChucVuBUS.getAllChucVu();
        String[] dsTenChucVu = new String[dsChucVu.size()];
        for (int i = 0; i < dsChucVu.size(); i++)
            dsTenChucVu[i] = dsChucVu.get(i).getTenChucVu();
        JComboBox<String> cbChucVu = new JComboBox<>(dsTenChucVu);
    
        // Tạo panel chứa các dòng input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 0 dòng, 2 cột, khoảng cách giữa các ô
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    
        // Hàm tiện để tạo dòng có label và field
        BiConsumer<String, JComponent> addRow = (labelText, inputField) -> {
            inputPanel.add(new JLabel(labelText));
            inputField.setPreferredSize(new Dimension(250, 25));
            inputPanel.add(inputField);
        };
    
        // Thêm các trường vào panel
        addRow.accept("Mã NV:", txtMaNV);
        addRow.accept("Họ Tên:", txtHoTen);
        addRow.accept("Giới Tính:", cbGioiTinh);
        addRow.accept("SĐT:", txtSDT);
        addRow.accept("Email:", txtEmail);
        addRow.accept("Địa Chỉ:", txtDiaChi);
        addRow.accept("Lương:", txtLuong);
        addRow.accept("Ngày Nhận Việc:", dateChooser);
        addRow.accept("Username:", txtUsername);
        addRow.accept("Password:", txtPassword);
        addRow.accept("Chức Vụ:", cbChucVu);
    
        // Nút
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
    
        // Tạo dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm Nhân Viên");
        dialog.setModal(true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
    
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        // Sự kiện
        btnCancel.addActionListener(e -> dialog.dispose());
    
        btnOk.addActionListener(e -> {
            try {
                String maKH = txtMaNV.getText();
                String hoTen = txtHoTen.getText();
                String gioiTinh = cbGioiTinh.getSelectedItem().toString();
                String sdt = txtSDT.getText();
                String email = txtEmail.getText();
                String diaChi = txtDiaChi.getText();
                String luong = txtLuong.getText();
                String ngayNhanViec = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                String maChucVu = dsChucVu.get(cbChucVu.getSelectedIndex()).getMaChucVu();
    
                if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || diaChi.isEmpty() || luong.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                NhanVienDTO nv = new NhanVienDTO(maKH, hoTen, gioiTinh, sdt, email, diaChi, Long.parseLong(luong), ngayNhanViec, "Hiện");
                TaiKhoanDTO tk = new TaiKhoanDTO(username, password, maKH, maChucVu, "Hiện");
    
                if (!nvBUS.checkTextAdd(hoTen, sdt, email, diaChi, Long.parseLong(luong)) ||
                    !tkBUS.checkTextAdd(username, password)) {
                    return;
                }
    
                if (nvBUS.add(nv) && tkBUS.add(tk)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên và tài khoản thành công!");
                    dialog.dispose();
                    loadTableData(NhanVienBUS.getAllNhanVien());
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại! Kiểm tra dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        dialog.setVisible(true);
    }

    private void suaNhanVien() {
        // Tạo các field
        JTextField txtMaNV = new JTextField(); 
        txtMaNV.setEditable(false); // Mã NV không thể chỉnh sửa
        JTextField txtHoTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();
        JTextField txtLuong = new JTextField();
        
        // Chọn ngày
        JDateChooser dateChooser = new JDateChooser(); 
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");  // Định dạng ngày là yyyy-MM-dd
        ((JTextField) dateChooser.getDateEditor().getUiComponent()).setEditable(false);  // Vô hiệu hóa chỉnh sửa thủ công
    
        // Tạo danh sách chức vụ
        ArrayList<ChucVuDTO> dsChucVu = ChucVuBUS.getAllChucVu();
        String[] dsTenChucVu = dsChucVu.stream().map(ChucVuDTO::getTenChucVu).toArray(String[]::new);
        JComboBox<String> cbChucVu = new JComboBox<>(dsTenChucVu);
    
        // Lấy dữ liệu từ bảng để chỉnh sửa
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Gán dữ liệu cho các trường
        txtMaNV.setText(ContentTable.getValueAt(selectedRow, 0).toString());
        txtHoTen.setText(ContentTable.getValueAt(selectedRow, 1).toString());
        cbGioiTinh.setSelectedItem(ContentTable.getValueAt(selectedRow, 2).toString());
        txtSDT.setText(ContentTable.getValueAt(selectedRow, 3).toString());
        txtEmail.setText(ContentTable.getValueAt(selectedRow, 4).toString());
        txtDiaChi.setText(ContentTable.getValueAt(selectedRow, 5).toString());
        txtLuong.setText(ContentTable.getValueAt(selectedRow, 6).toString());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date ngay = sdf.parse(ContentTable.getValueAt(selectedRow, 7).toString());
            dateChooser.setDate(ngay);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    
        // Tạo panel chứa các dòng input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 0 dòng, 2 cột, khoảng cách giữa các ô
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    
        // Hàm tiện để tạo dòng có label và field
        BiConsumer<String, JComponent> addRow = (labelText, inputField) -> {
            inputPanel.add(new JLabel(labelText));
            inputField.setPreferredSize(new Dimension(250, 20)); 
            inputPanel.add(inputField);
        };
    
        // Thêm các trường vào panel
        addRow.accept("Mã NV:", txtMaNV);
        addRow.accept("Họ Tên:", txtHoTen);
        addRow.accept("Giới Tính:", cbGioiTinh);
        addRow.accept("SĐT:", txtSDT);
        addRow.accept("Email:", txtEmail);
        addRow.accept("Địa Chỉ:", txtDiaChi);
        addRow.accept("Lương:", txtLuong);
        addRow.accept("Ngày Nhận Việc:", dateChooser);
        addRow.accept("Chức Vụ:", cbChucVu);
    
        // Nút
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
    
        // Tạo dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa Nhân Viên");
        dialog.setModal(true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
    
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        // Sự kiện
        btnCancel.addActionListener(e -> dialog.dispose());
    
        btnOk.addActionListener(e -> {
            try {
                String maNV = txtMaNV.getText();
                String hoTen = txtHoTen.getText();
                String gioiTinh = cbGioiTinh.getSelectedItem().toString();
                String sdt = txtSDT.getText();
                String email = txtEmail.getText();
                String diaChi = txtDiaChi.getText();
                String luong = txtLuong.getText();
                String ngayNhanViec = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
                String maChucVu = dsChucVu.get(cbChucVu.getSelectedIndex()).getMaChucVu();
    
                if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || diaChi.isEmpty() || luong.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                NhanVienDTO nv = new NhanVienDTO(maNV, hoTen, gioiTinh, sdt, email, diaChi, Long.parseLong(luong), ngayNhanViec, "Hiện");
                if (nvBUS.update(nv)) {
                    JOptionPane.showMessageDialog(dialog, "Sửa nhân viên thành công!");
                    dialog.dispose();
                    loadTableData(NhanVienBUS.getAllNhanVien());
                } else {
                    JOptionPane.showMessageDialog(dialog, "Sửa thất bại! Kiểm tra dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        dialog.setVisible(true);
    }

    private void xoaNhanVien() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String maNV = ContentTable.getValueAt(selectedRow, 0).toString();
        int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa nhân viên này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (MainLayout.getMaNVDangDN().equals(maNV)) {
                JOptionPane.showMessageDialog(null, "Không thể xóa chính mình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
             // Kiểm tra xem nhân viên có tài khoản không
            if (nvBUS.delete(maNV)) {
                JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
                loadTableData(NhanVienBUS.getAllNhanVien());
            } else {
                JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void timKiemNhanVien() {
        try{
            String kieuTim = cbSearch.getSelectedItem().toString();
            String tuKhoa = txtSearch.getText().trim();
            String gioiTinh = cbGioiTinhFilter.getSelectedItem().toString();

            String luongMinText = txtLuongMin.getText().trim();
            String luongMaxText = txtLuongMax.getText().trim();
            //Kiểm tra nếu không rỗng thì chuyển kiểu
            Double luongMin = luongMinText.isEmpty() ? null : Double.parseDouble(luongMinText);
            Double luongMax = luongMaxText.isEmpty() ? null : Double.parseDouble(luongMaxText);

            // Lấy ngày dưới dạng String
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayTu = "";
            String ngayDen = "";
        
            if (dateFrom.getDate() != null) {
                ngayTu = sdf.format(dateFrom.getDate());
            }
            if (dateTo.getDate() != null) {
                ngayDen = sdf.format(dateTo.getDate());
            }
        
            // Gọi DAO
            ArrayList<NhanVienDTO> dsKetQua = nvBUS.search(
                kieuTim, tuKhoa, gioiTinh, luongMin, luongMax, ngayTu, ngayDen
            );
        
            // Load kết quả vào bảng
            tableModel.setRowCount(0);
            loadTableData(dsKetQua);
        } catch(NumberFormatException ne){
            JOptionPane.showMessageDialog(null, "Lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    
}

