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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import BUS.HoaDonBUS;
// import BUS.NhanVienBUS;
// import BUS.TaiKhoanBUS;
import DTO.HoaDonDTO;
// import DTO.NhanVienDTO;
// import DTO.TaiKhoanDTO;
import fillter.Button;
import fillter.Colors;

public class HoaDonGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch;
    private DefaultTableModel tableModel;
    private HoaDonBUS hoaDonBUS;
    private JComboBox<String> cbxTrangThaiFilter;
    private JDateChooser dateFrom, dateTo;

    public HoaDonGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        hoaDonBUS = new HoaDonBUS();

        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120));

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelButtons.setBackground(Colors.MAIN_BACKGROUND);

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        AddBtn.addActionListener(e -> themHoaDon());

        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        DeleteBtn.addActionListener(e -> xoaHoaDon());

        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        EditBtn.addActionListener(e -> suaHoaDon());

        panelButtons.add(AddBtn);
        panelButtons.add(DeleteBtn);
        panelButtons.add(EditBtn);

        pSearch = new JPanel();
        pSearch.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pSearch.setBackground(Colors.MAIN_BACKGROUND);

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

        cbSearch = new JComboBox<>(new String[]{"Mã Hóa Đơn", "Mã Nhân Viên"});
        cbSearch.setPreferredSize(new Dimension(150, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        JPanel statusFillterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        statusFillterPanel.setBackground(Colors.MAIN_BACKGROUND);
        statusFillterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Trạng Thái",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        cbxTrangThaiFilter = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});
        statusFillterPanel.add(cbxTrangThaiFilter);

        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        dateFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        dateFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Ngày Tạo",
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


        
        JButton btnClearDate = new JButton("🔄️");
        btnClearDate.setPreferredSize(new Dimension(50, 30));
    
        btnClearDate.addActionListener(e -> {
            dateFrom.setDate(null);
            dateTo.setDate(null);
            txtSearch.setText("");
            cbSearch.setSelectedIndex(0);
            cbxTrangThaiFilter.setSelectedIndex(0);
        });
        


        pSearch.add(panelSearchFields);
        pSearch.add(statusFillterPanel);
        pSearch.add(dateFilterPanel);
        pSearch.add(btnClearDate);

        PanelHeader.add(panelButtons, BorderLayout.NORTH);
        PanelHeader.add(pSearch, BorderLayout.SOUTH);

        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columNames = {"Mã Hóa Đơn","Mã Nhân Viên","Ngày Tạo", "Tổng Tiền", "Tiền Trả", "Tiền Thừa", "Trạng Thái"};
        tableModel = new DefaultTableModel(columNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData(hoaDonBUS.layDanhSachHoaDon());

        cbSearch.addActionListener(e -> timKiemHoaDon());

        cbxTrangThaiFilter.addActionListener(e -> timKiemHoaDon());

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiemHoaDon();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiemHoaDon();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiemHoaDon();
            }
        });

        dateFrom.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiemHoaDon();
            }
        });

        dateTo.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiemHoaDon();
            }
        });
    }

    private void loadTableData(ArrayList<HoaDonDTO> danhSachHoaDon) {

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

    private void capNhatTienThua(JTextField txtTongTien, JTextField txtTienTra, JTextField txtTienThua) {
        try {
            double tongTien = Double.parseDouble(txtTongTien.getText().trim());
            double tienTra = Double.parseDouble(txtTienTra.getText().trim());
            double tienThua = tienTra - tongTien;
            txtTienThua.setText(String.valueOf(tienThua));
        } catch (NumberFormatException e) {
            txtTienThua.setText("");
        }
    }
    

    private void themHoaDon() {
        JTextField txtMaHoaDon = new JTextField(hoaDonBUS.tangMaHoaDon());
        txtMaHoaDon.setEditable(false);
    
        JTextField txtMaNhanVien = new JTextField();
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        ((JTextField) dateChooser.getDateEditor().getUiComponent()).setEditable(false);
    
        JTextField txtTongTien = new JTextField();
        JTextField txtTienTra = new JTextField();
        JTextField txtTienThua = new JTextField();
        txtTienThua.setEditable(false);

        DocumentListener tinhTienThuaListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            capNhatTienThua(txtTongTien, txtTienTra, txtTienThua);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            capNhatTienThua(txtTongTien, txtTienTra, txtTienThua);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            capNhatTienThua(txtTongTien, txtTienTra, txtTienThua);
        }
};  

        txtTongTien.getDocument().addDocumentListener(tinhTienThuaListener);
        txtTienTra.getDocument().addDocumentListener(tinhTienThuaListener);

        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Đã thanh toán", "Chưa thanh toán"});
    
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    
        BiConsumer<String, JComponent> addRow = (labelText, inputField) -> {
            inputPanel.add(new JLabel(labelText));
            inputField.setPreferredSize(new Dimension(250, 25));
            inputPanel.add(inputField);
        };
    
        addRow.accept("Mã Hóa Đơn:", txtMaHoaDon);
        addRow.accept("Mã Nhân Viên:", txtMaNhanVien);
        addRow.accept("Ngày Tạo:", dateChooser);
        addRow.accept("Tổng Tiền:", txtTongTien);
        addRow.accept("Tiền Trả:", txtTienTra);
        addRow.accept("Tiền Thừa:", txtTienThua);
        addRow.accept("Trạng Thái:", cbTrangThai);
    
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
    
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm Hóa Đơn");
        dialog.setModal(true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
    
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        btnCancel.addActionListener(e -> dialog.dispose());
    
        btnOk.addActionListener(e -> {
            try {
                String maHD = txtMaHoaDon.getText().trim();
                String maNV = txtMaNhanVien.getText().trim();
                Date ngayTao = dateChooser.getDate();
                double tongTien = Double.parseDouble(txtTongTien.getText().trim());
                double tienTra = Double.parseDouble(txtTienTra.getText().trim());
                double tienThua = Double.parseDouble(txtTienThua.getText().trim());
                String trangThai = (String) cbTrangThai.getSelectedItem();
    
                if (maNV.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                java.util.Date utilDate = dateChooser.getDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                HoaDonDTO hoaDon = new HoaDonDTO(maHD, maNV, sqlDate, tongTien, tienTra, tienThua, trangThai);
    
                if (hoaDonBUS.themHoaDon(hoaDon)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm hóa đơn thành công!");
                    dialog.dispose();
                    tableModel.setRowCount(0);
                    loadTableData(hoaDonBUS.layDanhSachHoaDon());
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Tổng tiền, tiền trả và tiền thừa phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        dialog.setVisible(true);
    }

    private void suaHoaDon() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để sửa.");
            return;
        }
    
        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        HoaDonDTO hoaDon = hoaDonBUS.getHoaDonByMa(maHD);
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin hóa đơn.");
            return;
        }
    
        JTextField txtMaHD = new JTextField(hoaDon.getMaHoaDon());
        txtMaHD.setEditable(false);
        JTextField txtMaNV = new JTextField(hoaDon.getMaNhanVien());
        JDateChooser dateChooser = new JDateChooser(hoaDon.getNgayTao());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        ((JTextField) dateChooser.getDateEditor().getUiComponent()).setEditable(false);
    
        JTextField txtTongTien = new JTextField(String.valueOf(hoaDon.getTongTien()));
        JTextField txtTienTra = new JTextField(String.valueOf(hoaDon.getTienTra()));
        JTextField txtTienThua = new JTextField(String.valueOf(hoaDon.getTienThua()));
        txtTienThua.setEditable(false);
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Đã thanh toán", "Chưa thanh toán"});
        cbTrangThai.setSelectedItem(hoaDon.getTrangThai());

        DocumentListener tinhTienThuaListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capNhatTienThua(txtTongTien, txtTienTra, txtTienThua); }
            public void removeUpdate(DocumentEvent e) { capNhatTienThua(txtTongTien, txtTienTra, txtTienThua); }
            public void changedUpdate(DocumentEvent e) { capNhatTienThua(txtTongTien, txtTienTra, txtTienThua); }
        };
        txtTongTien.getDocument().addDocumentListener(tinhTienThuaListener);
        txtTienTra.getDocument().addDocumentListener(tinhTienThuaListener);
    
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        BiConsumer<String, JComponent> addRow = (label, field) -> {
            inputPanel.add(new JLabel(label));
            inputPanel.add(field);
        };
    
        addRow.accept("Mã HĐ:", txtMaHD);
        addRow.accept("Mã NV:", txtMaNV);
        addRow.accept("Ngày tạo:", dateChooser);
        addRow.accept("Tổng tiền:", txtTongTien);
        addRow.accept("Tiền trả:", txtTienTra);
        addRow.accept("Tiền thừa:", txtTienThua);
        addRow.accept("Trạng thái:", cbTrangThai);
    
        Button btnOk = new Button("confirm", "Lưu", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
    
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa Hóa Đơn");
        dialog.setModal(true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
    
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        btnCancel.addActionListener(e -> dialog.dispose());
    
        btnOk.addActionListener(e -> {
            try {
                hoaDon.setMaNhanVien(txtMaNV.getText());
                hoaDon.setNgayTao(new java.sql.Date(dateChooser.getDate().getTime()));
                hoaDon.setTongTien(Double.parseDouble(txtTongTien.getText()));
                hoaDon.setTienTra(Double.parseDouble(txtTienTra.getText()));
                hoaDon.setTienThua(Double.parseDouble(txtTienThua.getText()));
                hoaDon.setTrangThai(cbTrangThai.getSelectedItem().toString());
    
                if (hoaDonBUS.capnhatHoaDon(hoaDon)) {
                    JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                    dialog.dispose();
                    tableModel.setRowCount(0);
                    loadTableData(hoaDonBUS.layDanhSachHoaDon());
                } else {
                    JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng số!");
            }
        });
    
        dialog.setVisible(true);
    }
    
    

    private void xoaHoaDon() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để xóa.");
            return;
        }
    
        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (hoaDonBUS.xoaHoaDon(maHD)) {
                JOptionPane.showMessageDialog(null, "Xóa thành công!");
                tableModel.setRowCount(0);
                loadTableData(HoaDonBUS.layDanhSachHoaDon());
            } else {
                JOptionPane.showMessageDialog(null, "Xóa thất bại!");
            }
        }
    }
    

    private void timKiemHoaDon() {
        try {
            String kieuTim = cbSearch.getSelectedItem().toString();
            String tuKhoa = txtSearch.getText().trim();
            String trangThai = cbxTrangThaiFilter.getSelectedItem().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayTu = "", ngayDen = "";
    
            if (dateFrom.getDate() != null) {
                ngayTu = sdf.format(dateFrom.getDate());
            }
            if (dateTo.getDate() != null) {
                ngayDen = sdf.format(dateTo.getDate());
            }
    
            ArrayList<HoaDonDTO> dsKetQua = hoaDonBUS.search(
                kieuTim,
                tuKhoa,
                trangThai.equals("Tất cả") ? "" : trangThai,
                ngayTu,
                ngayDen
            );
    
            tableModel.setRowCount(0);
            loadTableData(dsKetQua);
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
     
    
    
}