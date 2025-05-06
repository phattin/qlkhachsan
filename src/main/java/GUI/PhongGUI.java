package GUI;

import fillter.Button;
import fillter.Colors;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import BUS.PhongBUS;
import DTO.PhongDTO;
import BUS.DatPhongBUS;
import BUS.KhachHangBUS;
import BUS.LoaiPhongBUS;
import DTO.DatPhongDTO;
import DTO.KhachHangDTO;
import DTO.LoaiPhongDTO;

import java.util.ArrayList;
import java.util.List;


public class PhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch, txtGiaMin, txtGiaMax;
    private JComboBox<String> CBFilter;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    private JComboBox<String> cbSearch; 
    private static PhongGUI instance;

    
    private DefaultTableModel tableModel;
    private PhongBUS phongBUS;
    private DatPhongBUS datPhongBUS;
    private LoaiPhongBUS loaiPhongBus;

    public PhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        phongBUS = new PhongBUS();
        datPhongBUS = new DatPhongBUS();

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

        // Panel tìm kiếm các trường
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

        cbSearch = new JComboBox<>(new String[]{"Mã Phòng", "Mã Loại Phòng", "Loại Phòng", "Số Giường"});
        cbSearch.setPreferredSize(new Dimension(100, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Panel lọc trạng thái phòng
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterPanel.setBackground(Colors.MAIN_BACKGROUND);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Trạng Thái",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        CBFilter = new JComboBox<>(new String[]{"Tất cả", "Phòng trống", "Phòng đã đặt"});
        CBFilter.setPreferredSize(new Dimension(120, 30));
        filterPanel.add(CBFilter);

        // Thêm các panel con vào panel tìm kiếm
        searchPanel.add(filterPanel);
        searchPanel.add(panelSearchFields);

        // Panel lọc giá phòng (thêm mới)
        JPanel priceFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        priceFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        priceFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Lọc theo giá",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        txtGiaMin = new JTextField();
        txtGiaMax = new JTextField();
        txtGiaMin.setPreferredSize(new Dimension(100, 30));
        txtGiaMax.setPreferredSize(new Dimension(100, 30));
        
        priceFilterPanel.add(new JLabel("Từ:"));
        priceFilterPanel.add(txtGiaMin);
        priceFilterPanel.add(new JLabel("Đến:"));
        priceFilterPanel.add(txtGiaMax);

        Button btnClearFilter = new Button("edit", "Hủy lọc", 80, 30, null);
        btnClearFilter.setBackground(new Color(220, 220, 220));
        btnClearFilter.setForeground(Color.BLACK);
        btnClearFilter.addActionListener(e -> {
            txtGiaMin.setText("");
            txtGiaMax.setText("");
            txtSearch.setText(""); // Xóa cả nội dung tìm kiếm
            CBFilter.setSelectedIndex(0); // Reset bộ lọc trạng thái
            loadTableData();
        });
        
        priceFilterPanel.add(btnClearFilter);
        
        // Thêm panel lọc giá vào panel tìm kiếm
        searchPanel.add(priceFilterPanel);

        // Thêm cả hai panel vào PanelHeader
        PanelHeader.add(buttonPanel, BorderLayout.NORTH);
        PanelHeader.add(searchPanel, BorderLayout.SOUTH);

        // Content
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã Phòng", "Mã Loại Phòng", "Loại Phòng", "Số Giường", "Giá Tiền", "Trạng Thái", "Xem Thêm"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        ContentTable.getColumn("Xem Thêm").setCellRenderer(new ButtonRenderer());
        ContentTable.getColumn("Xem Thêm").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData();

        // Button events
        AddBtn.addActionListener(e -> addNewRoom());
        EditBtn.addActionListener(e -> editRoom());
        DeleteBtn.addActionListener(e -> deleteRoom());
        
        // Khi thay đổi cbSearch hoặc CBFilter
        cbSearch.addActionListener(e -> timKiem());
        CBFilter.addActionListener(e -> timKiem());

        // Khi người dùng nhập liệu vào txtSearch
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
        });
        
        // Thêm document listeners cho txtGiaMin và txtGiaMax
        txtGiaMin.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
        });
        
        txtGiaMax.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
        });
        
        // Lưu instance hiện tại
        instance = this;
    }

    // Sửa phương thức tìm kiếm
    private void timKiem() {
        String kieuTim = cbSearch.getSelectedItem().toString();
        String tuKhoa = txtSearch.getText().trim().toLowerCase();
        String trangThai = CBFilter.getSelectedItem().toString();
        
        // Lấy giá trị giá min và giá max
        double giaMin = 0;
        double giaMax = Integer.MAX_VALUE;
        
        try {
            if (!txtGiaMin.getText().trim().isEmpty()) {
                giaMin = Double.parseDouble(txtGiaMin.getText().trim());
            }
            
            if (!txtGiaMax.getText().trim().isEmpty()) {
                giaMax = Double.parseDouble(txtGiaMax.getText().trim());
            }
        } catch (NumberFormatException ex) {
            // Xử lý lỗi khi người dùng nhập giá không hợp lệ
            // Giữ nguyên giá trị mặc định và không hiển thị thông báo lỗi
        }
        
        tableModel.setRowCount(0);
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();
        
        for (PhongDTO phong : danhSachPhong) {
            LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
            
            // Áp dụng bộ lọc trạng thái
            if (!trangThai.equals("Tất cả")) {
                if (trangThai.equals("Phòng trống") && !phong.getTrangThai().equals("Trống")) {
                    continue;
                } else if (trangThai.equals("Phòng đã đặt") && !phong.getTrangThai().equals("Đã đặt")) {
                    continue;
                }
            }
            
            // Lọc theo khoảng giá
            double giaPhong = loaiPhong.getGiaPhong();
            if (giaPhong < giaMin || giaPhong > giaMax) {
                continue;
            }
            
            // Áp dụng tìm kiếm nếu có từ khóa
            if (!tuKhoa.isEmpty()) {
                boolean timThay = false;
                
                switch (kieuTim) {
                    case "Mã Phòng":
                        timThay = phong.getMaPhong().toLowerCase().contains(tuKhoa);
                        break;
                    case "Mã Loại Phòng":
                        timThay = loaiPhong.getMaLoaiPhong().toLowerCase().contains(tuKhoa);
                        break;
                    case "Loại Phòng":
                        timThay = loaiPhong.getTenLoaiPhong().toLowerCase().contains(tuKhoa);
                        break;
                    case "Số Giường":
                        timThay = String.valueOf(loaiPhong.getSoGiuong()).contains(tuKhoa);
                        break;
                }
                
                if (!timThay) {
                    continue;
                }
            }
            
            // Thêm dòng nếu đạt cả hai điều kiện lọc và tìm kiếm
            tableModel.addRow(new Object[]{
                phong.getMaPhong(),
                phong.getMaLoaiPhong(),
                loaiPhong.getTenLoaiPhong(),
                loaiPhong.getSoGiuong(),
                loaiPhong.getGiaPhong(),
                phong.getTrangThai(),
                "Xem Thêm"
            });
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();
    
        for (PhongDTO phong : danhSachPhong) {
            LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
            tableModel.addRow(new Object[]{
                phong.getMaPhong(),
                phong.getMaLoaiPhong(),
                loaiPhong.getTenLoaiPhong(),
                loaiPhong.getSoGiuong(),
                loaiPhong.getGiaPhong(),
                phong.getTrangThai(),
                "Xem Thêm"
            });
        }
    }

    public static void refreshTable() {
        if (instance != null) {
            instance.loadTableData();
        }
    }
    

    
    // Thay thế phương thức addNewRoom() hiện tại
    private void addNewRoom() {
        // Tạo dialog thêm phòng mới
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm phòng mới", true);
        dialog.setSize(550, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        
        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground((Colors.MAIN_BACKGROUND));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Mã phòng tự động tạo
        JLabel lblMaPhong = new JLabel("Mã Phòng: ");
        JTextField txtMaPhong = new JTextField(phongBUS.increaseMaPhong());
        txtMaPhong.setEditable(false);
        
        // Loại phòng combobox
        JLabel lblMaLP = new JLabel("Mã Loại Phòng: ");
        JComboBox<String> cbMaLP = new JComboBox<>();
        
        // Các field thông tin loại phòng
        JLabel lblTenLP = new JLabel("Tên Loại Phòng: ");
        JTextField txtTenLP = new JTextField();
        txtTenLP.setEditable(false);
        
        JLabel lblSoGiuong = new JLabel("Số Giường: ");
        JTextField txtSoGiuong = new JTextField();
        txtSoGiuong.setEditable(false);
        
        JLabel lblGiaTien = new JLabel("Giá Tiền/giờ: ");
        JTextField txtGiaTien = new JTextField();
        txtGiaTien.setEditable(false);
        
        // Trạng thái input
        JLabel lblTrangThai = new JLabel("Trạng Thái: ");
        JTextField txtTrangThai = new JTextField("Trống"); // Mặc định là trống
        txtTrangThai.setEditable(false);
        // Thêm các thành phần vào panel
        inputPanel.add(lblMaPhong);
        inputPanel.add(txtMaPhong);
        inputPanel.add(lblMaLP);
        inputPanel.add(cbMaLP);
        inputPanel.add(lblTenLP);
        inputPanel.add(txtTenLP);
        inputPanel.add(lblSoGiuong);
        inputPanel.add(txtSoGiuong);
        inputPanel.add(lblGiaTien);
        inputPanel.add(txtGiaTien);
        inputPanel.add(lblTrangThai);
        inputPanel.add(txtTrangThai);
        
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(Colors.MAIN_BACKGROUND);
        
        Button btnAdd = new Button("add", "THÊM", 90, 35);
        Button btnCancel = new Button("cancel", "HỦY", 90, 35);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        
        // Thêm các panel vào dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Lấy danh sách loại phòng
        LoaiPhongBUS loaiPhongBUS = new LoaiPhongBUS();
        ArrayList<LoaiPhongDTO> dsLoaiPhong = loaiPhongBUS.getAllLoaiPhong();
        for (LoaiPhongDTO lp : dsLoaiPhong) {
            cbMaLP.addItem(lp.getMaLoaiPhong());
        }
        
        // Listener khi thay đổi loại phòng
        cbMaLP.addActionListener(e -> {
            String maLP = (String) cbMaLP.getSelectedItem();
            if (maLP == null || maLP.trim().isEmpty()) {
                txtTenLP.setText("");
                txtSoGiuong.setText("");
                txtGiaTien.setText("");
                return;
            }
            
            LoaiPhongDTO loaiPhong = loaiPhongBUS.getById(maLP);
            if (loaiPhong != null) {
                txtTenLP.setText(loaiPhong.getTenLoaiPhong());
                txtSoGiuong.setText(String.valueOf(loaiPhong.getSoGiuong()));
                txtGiaTien.setText(String.valueOf(loaiPhong.getGiaPhong()));
            }
        });
        
        // Trigger để hiển thị thông tin loại phòng ban đầu
        if (cbMaLP.getItemCount() > 0) {
            cbMaLP.setSelectedIndex(0);
        }
        
        // Xử lý sự kiện nút Hủy
        btnCancel.addActionListener(e -> dialog.dispose());
        
        // Xử lý sự kiện nút Thêm
        btnAdd.addActionListener(e -> {
            // Lấy dữ liệu từ form
            String maPhong = txtMaPhong.getText().trim();
            String maLoaiPhong = (String) cbMaLP.getSelectedItem();
            String trangThai = txtTrangThai.getText().trim();
            
            // Kiểm tra dữ liệu
            if (maPhong.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (phongBUS.isMaPhongExists(maPhong)) {
                JOptionPane.showMessageDialog(dialog, "Mã phòng đã tồn tại! Vui lòng nhập mã phòng khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (maLoaiPhong == null || maLoaiPhong.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã loại phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (trangThai.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Trạng thái không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tạo đối tượng mới
            PhongDTO phong = new PhongDTO(maPhong, maLoaiPhong, trangThai);
            
            // Thêm vào CSDL
            if (phongBUS.addPhong(phong)) {
                JOptionPane.showMessageDialog(dialog, "Thêm phòng thành công!");
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }
    
    // Thay thế phương thức editRoom() hiện tại
    private void editRoom() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy thông tin phòng từ bảng
        String maPhong = tableModel.getValueAt(selectedRow, 0).toString();
        String maLoaiPhongCurrent = tableModel.getValueAt(selectedRow, 1).toString();
        String trangThaiCurrent = tableModel.getValueAt(selectedRow, 5).toString();
        
        // Tạo dialog sửa phòng
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chỉnh sửa phòng", true);
        dialog.setSize(550, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        
        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground((Colors.MAIN_BACKGROUND));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Mã phòng không được sửa
        JLabel lblMaPhong = new JLabel("Mã Phòng: ");
        JTextField txtMaPhong = new JTextField(maPhong);
        txtMaPhong.setEditable(false);
        
        // Loại phòng combobox
        JLabel lblMaLP = new JLabel("Mã Loại Phòng: ");
        JComboBox<String> cbMaLP = new JComboBox<>();
        
        // Các field thông tin loại phòng
        JLabel lblTenLP = new JLabel("Tên Loại Phòng: ");
        JTextField txtTenLP = new JTextField();
        txtTenLP.setEditable(false);
        
        JLabel lblSoGiuong = new JLabel("Số Giường: ");
        JTextField txtSoGiuong = new JTextField();
        txtSoGiuong.setEditable(false);
        
        JLabel lblGiaTien = new JLabel("Giá Tiền/giờ: ");
        JTextField txtGiaTien = new JTextField();
        txtGiaTien.setEditable(false);
        
        // Trạng thái input
        JLabel lblTrangThai = new JLabel("Trạng Thái: ");
        JTextField txtTrangThai = new JTextField(trangThaiCurrent);
        txtTrangThai.setEditable(false);
        
        // Thêm các thành phần vào panel
        inputPanel.add(lblMaPhong);
        inputPanel.add(txtMaPhong);
        inputPanel.add(lblMaLP);
        inputPanel.add(cbMaLP);
        inputPanel.add(lblTenLP);
        inputPanel.add(txtTenLP);
        inputPanel.add(lblSoGiuong);
        inputPanel.add(txtSoGiuong);
        inputPanel.add(lblGiaTien);
        inputPanel.add(txtGiaTien);
        inputPanel.add(lblTrangThai);
        inputPanel.add(txtTrangThai);
        
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(Colors.MAIN_BACKGROUND);
        
        Button btnSave = new Button("confirm", "LƯU", 90, 35);
        Button btnCancel = new Button("cancel", "HỦY", 90, 35);
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        // Thêm các panel vào dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Lấy danh sách loại phòng
        LoaiPhongBUS loaiPhongBUS = new LoaiPhongBUS();
        ArrayList<LoaiPhongDTO> dsLoaiPhong = loaiPhongBUS.getAllLoaiPhong();
        int selectedIndex = 0;
        int index = 0;
        
        for (LoaiPhongDTO lp : dsLoaiPhong) {
            cbMaLP.addItem(lp.getMaLoaiPhong());
            if (lp.getMaLoaiPhong().equals(maLoaiPhongCurrent)) {
                selectedIndex = index;
            }
            index++;
        }
        cbMaLP.setSelectedIndex(selectedIndex);
        
        // Listener khi thay đổi loại phòng
        cbMaLP.addActionListener(e -> {
            String maLP = (String) cbMaLP.getSelectedItem();
            if (maLP == null || maLP.trim().isEmpty()) {
                txtTenLP.setText("");
                txtSoGiuong.setText("");
                txtGiaTien.setText("");
                return;
            }
            
            LoaiPhongDTO loaiPhong = loaiPhongBUS.getById(maLP);
            if (loaiPhong != null) {
                txtTenLP.setText(loaiPhong.getTenLoaiPhong());
                txtSoGiuong.setText(String.valueOf(loaiPhong.getSoGiuong()));
                txtGiaTien.setText(String.valueOf(loaiPhong.getGiaPhong()));
            }
        });
        
        // Trigger chọn loại phòng ban đầu
        if (cbMaLP.getItemCount() > 0) {
            cbMaLP.setSelectedIndex(selectedIndex);
        }
        
        // Xử lý sự kiện nút Hủy
        btnCancel.addActionListener(e -> dialog.dispose());
        
        // Xử lý sự kiện nút Lưu
        btnSave.addActionListener(e -> {
            // Lấy dữ liệu từ form
            String maLoaiPhong = (String) cbMaLP.getSelectedItem();
            String trangThai = txtTrangThai.getText().trim();
            
            // Kiểm tra dữ liệu
            if (maLoaiPhong == null || maLoaiPhong.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã loại phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (trangThai.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Trạng thái không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tạo đối tượng cập nhật
            PhongDTO phong = new PhongDTO(maPhong, maLoaiPhong, trangThai);
            
            // Cập nhật vào CSDL
            if (phongBUS.updatePhong(phong)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật phòng thành công!");
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }

    // Phương thức deleteRoom() không cần thay đổi nhiều vì nó đã trực tiếp sử dụng PhongBUS
    private void deleteRoom() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maPhong = tableModel.getValueAt(selectedRow, 0).toString();
        String tenLoaiPhong = tableModel.getValueAt(selectedRow, 2).toString(); // Lấy tên loại phòng để hiển thị
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa phòng [" + maPhong + " - " + tenLoaiPhong + "] không?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (phongBUS.deletePhong(maPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa phòng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Xóa phòng thất bại! Phòng có thể đang được sử dụng hoặc đã bị xóa trước đó.", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setText("Xem Thêm");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String maPhong;
    private DatPhongBUS datPhongBUS;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Xem Thêm");
        datPhongBUS = new DatPhongBUS();

        button.addActionListener(e -> {
            if (maPhong != null) {
                showBookingDetails(maPhong);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        maPhong = table.getValueAt(row, 0).toString(); // Lấy mã phòng từ dòng hiện tại
        return button;
    }

    private void showBookingDetails(String maPhong) {
        // Kiểm tra xem phòng có trong bảng danhsachphong không
        List<DatPhongDTO> danhSach = datPhongBUS.layDanhSachDatPhongTuDanhSachPhong(maPhong);
        
        if (danhSach.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Phòng này chưa có thông tin đặt phòng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JDialog detailsDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(button), "Chi tiết đặt phòng", true);
        detailsDialog.setSize(500, 300);
        detailsDialog.setLocationRelativeTo(null);
    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Colors.WHITE_FONT);
    
        KhachHangBUS khachHangBUS = new KhachHangBUS();
    
        for (DatPhongDTO dp : danhSach) {
            KhachHangDTO khachHang = khachHangBUS.getById(dp.getMaKH());
            String tenKhachHang = (khachHang != null) ? khachHang.getHoTen() : "Không tìm thấy";
    
            JPanel bookingPanel = new JPanel();
            bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
            bookingPanel.setBackground(Colors.WHITE_FONT);
            bookingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    
            bookingPanel.add(createInfoRow("Mã Đặt Phòng:", dp.getMaDatPhong()));
            bookingPanel.add(createInfoRow("Mã Phòng:", dp.getMaPhong()));
            bookingPanel.add(createInfoRow("Khách Hàng:", tenKhachHang));
            bookingPanel.add(createInfoRow("Ngày Nhận:", dp.getNgayNhanPhong().toString()));
            bookingPanel.add(createInfoRow("Ngày Trả:", dp.getNgayTraPhong().toString()));
    
            mainPanel.add(bookingPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
        detailsDialog.add(scrollPane, BorderLayout.CENTER);
        detailsDialog.setVisible(true);
    }
    
    
    // Tạo dòng thông tin có nhãn và giá trị thẳng hàng
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Colors.WHITE_FONT);
    
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(100, 20)); // căn lề nhãn đều nhau
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
    
        JLabel val = new JLabel(value);
    
        row.add(lbl);
        row.add(val);
        return row;
    }
    
    
}