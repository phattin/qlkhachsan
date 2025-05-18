package GUI;

import fillter.Button;
import fillter.Colors;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import BUS.DichVuBUS;
import DTO.DichVuDTO;

import java.awt.*;
import java.util.ArrayList;

public class DichVuGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch, txtGiaMin, txtGiaMax; 
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    private DefaultTableModel tableModel;
    private DichVuBUS dichVuBUS;
    private JComboBox<String> cbSearch;

    public DichVuGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        dichVuBUS = new DichVuBUS();

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

        cbSearch = new JComboBox<>(new String[]{"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ"});
        cbSearch.setPreferredSize(new Dimension(100, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Panel lọc theo giá dịch vụ
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
            loadTableData();
        });
        
        priceFilterPanel.add(btnClearFilter);

        searchPanel.add(panelSearchFields);
        searchPanel.add(priceFilterPanel);

        // Thêm cả hai panel vào PanelHeader
        PanelHeader.add(buttonPanel, BorderLayout.NORTH);
        PanelHeader.add(searchPanel, BorderLayout.SOUTH);

        // Content
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã Dịch Vụ", "Tên Dịch Vụ", "Giá Dịch Vụ"};
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
        AddBtn.addActionListener(e -> themDichVu());
        EditBtn.addActionListener(e -> suaDichVu());
        DeleteBtn.addActionListener(e -> xoaDichVu());
        
        // Khi thay đổi cbSearch
        cbSearch.addActionListener(e -> timKiem());

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
    }

    private void timKiem() {
        String searchType = cbSearch.getSelectedItem().toString();
        String keyword = txtSearch.getText().trim().toLowerCase();
        
        // Lấy giá trị giá min và giá max
        int giaMin = 0;
        int giaMax = Integer.MAX_VALUE;
        
        try {
            if (!txtGiaMin.getText().trim().isEmpty()) {
                giaMin = Integer.parseInt(txtGiaMin.getText().trim());
            }
            
            if (!txtGiaMax.getText().trim().isEmpty()) {
                giaMax = Integer.parseInt(txtGiaMax.getText().trim());
            }
        } catch (NumberFormatException ex) {
            // Xử lý lỗi khi người dùng nhập giá không hợp lệ - không hiển thị thông báo lỗi
            // Vẫn giữ các giá trị mặc định
        }
        
        // Nếu không có tiêu chí tìm kiếm nào, hiển thị tất cả dữ liệu
        if (keyword.isEmpty() && txtGiaMin.getText().trim().isEmpty() && txtGiaMax.getText().trim().isEmpty()) {
            loadTableData();
            return;
        }
        
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);
        ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu();
        
        // Lọc dữ liệu theo cả từ khóa và khoảng giá
        for (DichVuDTO dv : danhSachDichVu) {
            boolean matchesKeyword = false;
            int gia = dv.getGiaDichVu();
            
            // Kiểm tra nếu giá nằm trong khoảng lọc
            boolean giaHopLe = gia >= giaMin && gia <= giaMax;
            
            // Nếu không có từ khóa tìm kiếm, chỉ lọc theo giá
            if (keyword.isEmpty()) {
                matchesKeyword = true; // Coi như tất cả đều khớp với từ khóa trống
            } else {
                // Kiểm tra theo loại tìm kiếm
                switch (searchType) {
                    case "Mã dịch vụ":
                        matchesKeyword = dv.getMaDichVu().toLowerCase().contains(keyword);
                        break;
                    case "Tên dịch vụ":
                        matchesKeyword = dv.getTenDichVu().toLowerCase().contains(keyword);
                        break;
                    case "Giá dịch vụ":
                        matchesKeyword = String.valueOf(dv.getGiaDichVu()).contains(keyword);
                        break;
                }
            }
            
            // Chỉ thêm vào kết quả nếu thỏa mãn cả hai điều kiện
            if (matchesKeyword && giaHopLe) {
                tableModel.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    dv.getGiaDichVu()
                });
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu();

        for (DichVuDTO dv : danhSachDichVu) {
            tableModel.addRow(new Object[]{
                dv.getMaDichVu(),
                dv.getTenDichVu(),
                dv.getGiaDichVu()
            });
        }
    }

    private void themDichVu() {
        // Tạo các field
        JTextField txtMaDV = new JTextField();
        String generatedId = dichVuBUS.generateNewMaDV();
        if (generatedId != null) {
            txtMaDV.setText(generatedId);
            txtMaDV.setEditable(false);
        }
        
        JTextField txtTenDV = new JTextField();
        JTextField txtGiaDV = new JTextField();
        
        // Tạo panel chứa các dòng input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 0 dòng, 2 cột, khoảng cách 10px
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Thêm các trường vào panel
        inputPanel.add(new JLabel("Mã Dịch Vụ:"));
        txtMaDV.setPreferredSize(new Dimension(250, 25));
        inputPanel.add(txtMaDV);
        
        inputPanel.add(new JLabel("Tên Dịch Vụ:"));
        txtTenDV.setPreferredSize(new Dimension(250, 25));
        inputPanel.add(txtTenDV);
        
        inputPanel.add(new JLabel("Giá Dịch Vụ:"));
        txtGiaDV.setPreferredSize(new Dimension(250, 25));
        inputPanel.add(txtGiaDV);
        
        // Nút
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        
        // Tạo dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm Dịch Vụ");
        dialog.setModal(true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Sự kiện
        btnCancel.addActionListener(e -> dialog.dispose());
        
        btnOk.addActionListener(e -> {
            try {
                String maDV = txtMaDV.getText().trim();
                String tenDV = txtTenDV.getText().trim();
                int giaDV = Integer.parseInt(txtGiaDV.getText().trim());

                if (maDV.isEmpty() || tenDV.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (dichVuBUS.isMaDVExists(maDV)) {
                    JOptionPane.showMessageDialog(dialog, "Mã dịch vụ đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tạo đối tượng mới
                DichVuDTO dichVuMoi = new DichVuDTO(maDV, tenDV, giaDV);
                
                // Thêm vào cơ sở dữ liệu
                boolean result = dichVuBUS.addDichVu(dichVuMoi);
                
                if (result) {
                    // Tải lại dữ liệu bảng
                    loadTableData();
                    
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá dịch vụ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }

    private void suaDichVu() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String maDV = tableModel.getValueAt(selectedRow, 0).toString();
        
        // Lấy thông tin dịch vụ hiện tại từ CSDL
        DichVuDTO currentDichVu = dichVuBUS.getById(maDV);
        if (currentDichVu == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin dịch vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo các field
        JTextField txtMaDV = new JTextField(currentDichVu.getMaDichVu());
        txtMaDV.setEditable(false);  // Không cho phép sửa mã
        
        JTextField txtTenDV = new JTextField(currentDichVu.getTenDichVu());
        JTextField txtGiaDV = new JTextField(String.valueOf(currentDichVu.getGiaDichVu()));
        
        // Tạo panel chứa các dòng input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 0 dòng, 2 cột, khoảng cách 10px
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Thêm các trường vào panel
        inputPanel.add(new JLabel("Mã Dịch Vụ:"));
        txtMaDV.setPreferredSize(new Dimension(250, 25));
        inputPanel.add(txtMaDV);
        
        inputPanel.add(new JLabel("Tên Dịch Vụ:"));
        txtTenDV.setPreferredSize(new Dimension(250, 25));
        inputPanel.add(txtTenDV);
        
        inputPanel.add(new JLabel("Giá Dịch Vụ:"));
        txtGiaDV.setPreferredSize(new Dimension(250, 25));
        inputPanel.add(txtGiaDV);
        
        // Nút
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        
        // Tạo dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa Dịch Vụ");
        dialog.setModal(true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Sự kiện
        btnCancel.addActionListener(e -> dialog.dispose());
        
        btnOk.addActionListener(e -> {
            try {
                String tenDVMoi = txtTenDV.getText().trim();
                int giaDVMoi = Integer.parseInt(txtGiaDV.getText().trim());

                if (tenDVMoi.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tên dịch vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tạo đối tượng mới với thông tin cập nhật
                DichVuDTO dichVuCapNhat = new DichVuDTO(maDV, tenDVMoi, giaDVMoi);
                
                // Cập nhật trong cơ sở dữ liệu
                boolean result = dichVuBUS.updateDichVu(dichVuCapNhat);
                
                if (result) {
                    // Tải lại dữ liệu bảng
                    loadTableData();
                    
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá dịch vụ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }

    private void xoaDichVu() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String maDV = tableModel.getValueAt(selectedRow, 0).toString();
        String tenDV = tableModel.getValueAt(selectedRow, 1).toString();

        int result = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa dịch vụ: " + tenDV + "?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Xóa trong cơ sở dữ liệu
            boolean deleteResult = dichVuBUS.deleteDichVu(maDV);
            
            if (deleteResult) {
                // Tải lại dữ liệu bảng
                loadTableData();
                
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa dịch vụ. Dịch vụ có thể đang được sử dụng hoặc đã bị xóa trước đó.",
                    "Lỗi xóa dịch vụ", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}