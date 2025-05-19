package GUI;

import fillter.Button;
import fillter.Colors;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import BUS.LoaiPhongBUS;
import DTO.LoaiPhongDTO;

import java.awt.*;
import java.util.ArrayList;

public class LoaiPhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    private DefaultTableModel tableModel;
    private LoaiPhongBUS loaiPhongBUS;
    private JComboBox<String> cbSearch;
    private JTextField txtSearch, txtGiaMin, txtGiaMax;
    private Button btnClearFilter;

    public LoaiPhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        loaiPhongBUS = new LoaiPhongBUS();

        // Header
        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120));

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

        cbSearch = new JComboBox<>(new String[]{"Tất cả", "Mã loại phòng", "Tên loại phòng", "Số giường"});
        cbSearch.setPreferredSize(new Dimension(130, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Panel lọc giá phòng
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

        // Nút hủy lọc
        btnClearFilter = new Button("edit", "Hủy lọc", 80, 30, null);
        btnClearFilter.setBackground(new Color(220, 220, 220));
        btnClearFilter.setForeground(Color.BLACK);
        btnClearFilter.addActionListener(e -> {
            txtGiaMin.setText("");
            txtGiaMax.setText("");
            txtSearch.setText("");
            cbSearch.setSelectedIndex(0);
            loadTableData();
        });
        priceFilterPanel.add(btnClearFilter);

        // Thêm các panel vào searchPanel
        searchPanel.add(panelSearchFields);
        searchPanel.add(priceFilterPanel);

        // Thêm searchPanel vào PanelHeader
        PanelHeader.add(buttonPanel, BorderLayout.NORTH);
        PanelHeader.add(searchPanel, BorderLayout.SOUTH);

        // Content
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã loại phòng", "Tên loại phòng", "Số giường", "Giá phòng"};
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
        AddBtn.addActionListener(e -> themLoaiPhong());
        EditBtn.addActionListener(e -> suaLoaiPhong());
        DeleteBtn.addActionListener(e -> xoaLoaiPhong());

        // Khi thay đổi cbSearch
        cbSearch.addActionListener(e -> timKiem());

        // Khi người dùng nhập liệu vào txtSearch
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
        });
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
        cbSearch.addActionListener(e -> timKiem());
    }

    private void timKiem() {
        String kieuTim = cbSearch.getSelectedItem().toString();
        String tuKhoa = txtSearch.getText().trim().toLowerCase();
        String giaMinStr = txtGiaMin.getText().trim();
        String giaMaxStr = txtGiaMax.getText().trim();

        double giaMin = 0;
        double giaMax = Double.MAX_VALUE;
        try {
            if (!giaMinStr.isEmpty()) giaMin = Double.parseDouble(giaMinStr);
            if (!giaMaxStr.isEmpty()) giaMax = Double.parseDouble(giaMaxStr);
        } catch (NumberFormatException e) {
            // Nếu nhập sai định dạng số, giữ giá trị mặc định
        }

        tableModel.setRowCount(0);
        ArrayList<LoaiPhongDTO> ds = loaiPhongBUS.getAllLoaiPhong();

        for (LoaiPhongDTO lp : ds) {
            // Lọc theo khoảng giá
            double gia = lp.getGiaPhong();
            if (gia < giaMin || gia > giaMax) continue;

            // Áp dụng tìm kiếm nếu có từ khóa
            boolean timThay = tuKhoa.isEmpty();
            if (!tuKhoa.isEmpty()) {
                switch (kieuTim) {
                    case "Tất cả":
                        timThay =
                            lp.getMaLoaiPhong().toLowerCase().contains(tuKhoa) ||
                            lp.getTenLoaiPhong().toLowerCase().contains(tuKhoa) ||
                            String.valueOf(lp.getSoGiuong()).contains(tuKhoa);
                        break;
                    case "Mã loại phòng":
                        timThay = lp.getMaLoaiPhong().toLowerCase().contains(tuKhoa);
                        break;
                    case "Tên loại phòng":
                        timThay = lp.getTenLoaiPhong().toLowerCase().contains(tuKhoa);
                        break;
                    case "Số giường":
                        timThay = String.valueOf(lp.getSoGiuong()).contains(tuKhoa);
                        break;
                }
            }

            if (timThay) {
                tableModel.addRow(new Object[]{
                    lp.getMaLoaiPhong(),
                    lp.getTenLoaiPhong(),
                    lp.getSoGiuong(),
                    lp.getGiaPhong()
                });
            }
        }
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<LoaiPhongDTO> ds = loaiPhongBUS.getAllLoaiPhong();
        for (LoaiPhongDTO lp : ds) {
            tableModel.addRow(new Object[]{
                lp.getMaLoaiPhong(),
                lp.getTenLoaiPhong(),
                lp.getSoGiuong(),
                lp.getGiaPhong()
            });
        }
    }

    private void themLoaiPhong() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm loại phòng", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMa = new JTextField(loaiPhongBUS.increaseMaLP());
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField();
        JTextField txtSoGiuong = new JTextField();
        JTextField txtGia = new JTextField();

        inputPanel.add(new JLabel("Mã loại phòng:"));
        inputPanel.add(txtMa);
        inputPanel.add(new JLabel("Tên loại phòng:"));
        inputPanel.add(txtTen);
        inputPanel.add(new JLabel("Số giường:"));
        inputPanel.add(txtSoGiuong);
        inputPanel.add(new JLabel("Giá phòng:"));
        inputPanel.add(txtGia);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button btnAdd = new Button("add", "THÊM", 90, 35);
        Button btnCancel = new Button("cancel", "HỦY", 90, 35);

        btnPanel.add(btnAdd);
        btnPanel.add(btnCancel);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnAdd.addActionListener(e -> {
            String ma = txtMa.getText().trim();
            String ten = txtTen.getText().trim();
            String soGiuongStr = txtSoGiuong.getText().trim();
            String giaStr = txtGia.getText().trim();

            if (ma.isEmpty() || ten.isEmpty() || soGiuongStr.isEmpty() || giaStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int soGiuong;
            double gia;
            try {
                soGiuong = Integer.parseInt(soGiuongStr);
                gia = Double.parseDouble(giaStr);
                if (soGiuong <= 0 || gia < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số giường phải là số nguyên dương, giá phòng phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LoaiPhongDTO lp = new LoaiPhongDTO(ma, ten, soGiuong, gia);
            if (loaiPhongBUS.addLoaiPhong(lp)) {
                JOptionPane.showMessageDialog(dialog, "Thêm loại phòng thành công!");
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm loại phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void suaLoaiPhong() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại phòng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ma = tableModel.getValueAt(selectedRow, 0).toString();
        String ten = tableModel.getValueAt(selectedRow, 1).toString();
        String soGiuong = tableModel.getValueAt(selectedRow, 2).toString();
        String gia = tableModel.getValueAt(selectedRow, 3).toString();

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chỉnh sửa loại phòng", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMa = new JTextField(ma);
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField(ten);
        JTextField txtSoGiuong = new JTextField(soGiuong);
        JTextField txtGia = new JTextField(gia);

        inputPanel.add(new JLabel("Mã loại phòng:"));
        inputPanel.add(txtMa);
        inputPanel.add(new JLabel("Tên loại phòng:"));
        inputPanel.add(txtTen);
        inputPanel.add(new JLabel("Số giường:"));
        inputPanel.add(txtSoGiuong);
        inputPanel.add(new JLabel("Giá phòng:"));
        inputPanel.add(txtGia);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button btnSave = new Button("confirm", "LƯU", 90, 35);
        Button btnCancel = new Button("cancel", "HỦY", 90, 35);

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            String tenMoi = txtTen.getText().trim();
            String soGiuongStr = txtSoGiuong.getText().trim();
            String giaStr = txtGia.getText().trim();

            if (tenMoi.isEmpty() || soGiuongStr.isEmpty() || giaStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int soGiuongMoi;
            double giaMoi;
            try {
                soGiuongMoi = Integer.parseInt(soGiuongStr);
                giaMoi = Double.parseDouble(giaStr);
                if (soGiuongMoi <= 0 || giaMoi < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số giường phải là số nguyên dương, giá phòng phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LoaiPhongDTO lp = new LoaiPhongDTO(ma, tenMoi, soGiuongMoi, giaMoi);
            if (loaiPhongBUS.updateLoaiPhong(lp)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật loại phòng thành công!");
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật loại phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void xoaLoaiPhong() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại phòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ma = tableModel.getValueAt(selectedRow, 0).toString();
        String ten = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa loại phòng [" + ma + " - " + ten + "] không?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (loaiPhongBUS.deleteLoaiPhong(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa loại phòng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa loại phòng thất bại! Có thể loại phòng đang được sử dụng.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}