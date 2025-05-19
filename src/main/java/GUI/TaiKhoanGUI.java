package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
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

import BUS.ChucVuBUS;
import BUS.NhanVienBUS;
import BUS.TaiKhoanBUS;
import DTO.ChucVuDTO;
import DTO.TaiKhoanDTO;
import fillter.Button;
import fillter.Colors;

public class TaiKhoanGUI extends JPanel {
    private Button EditBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch, cbChucVuFilter;
    private static DefaultTableModel tableModel;
    private TaiKhoanBUS tkBUS;

    public TaiKhoanGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        tkBUS = new TaiKhoanBUS();
        tkBUS = new TaiKhoanBUS();

        PanelHeader = new JPanel();
        PanelHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // căn trái, khoảng cách giữa các phần tử
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 75));

        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        EditBtn.addActionListener(e -> suaTaiKhoan());

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

        cbSearch = new JComboBox<>(new String[]{"Tên đăng nhập", "Họ tên"});
        cbSearch.setPreferredSize(new Dimension(120, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        // Thêm vào panel con
        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Tạo panel con để bao phủ  comboBox chức vụ
        JPanel pChucVuFilter = new JPanel();
        pChucVuFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pChucVuFilter.setBackground(Colors.MAIN_BACKGROUND);
        pChucVuFilter.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Chức vụ",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        ArrayList<ChucVuDTO> dsChucVu = ChucVuBUS.getAllChucVu();
        String[] dsTenChucVu = new String[dsChucVu.size() + 1];
        dsTenChucVu[0] = "Tất cả";
        for (int i = 1; i < dsChucVu.size() + 1; i++)
            dsTenChucVu[i] = dsChucVu.get(i-1).getTenChucVu();
        cbChucVuFilter = new JComboBox<>(dsTenChucVu);
        cbChucVuFilter.setPreferredSize(new Dimension(120, 30));

        // Thêm vào panel con
        pChucVuFilter.add(cbChucVuFilter);

        // Thêm cả 2 hàng vào PanelHeader
        PanelHeader.add(EditBtn, BorderLayout.NORTH);
        PanelHeader.add(panelSearchFields, BorderLayout.SOUTH);
        PanelHeader.add(pChucVuFilter, BorderLayout.SOUTH);
        
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã nhân viên", "Họ tên nhân viên", "Tên đăng nhập", "Mật khẩu", "Mã chức vụ" , "Chức vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        // Ẩn cột "Mã nhân viên" và "Mã chức vụ"
        ContentTable.getColumnModel().getColumn(0).setMinWidth(0);
        ContentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        ContentTable.getColumnModel().getColumn(0).setWidth(0);

        ContentTable.getColumnModel().getColumn(4).setMinWidth(0);
        ContentTable.getColumnModel().getColumn(4).setMaxWidth(0);
        ContentTable.getColumnModel().getColumn(4).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData(TaiKhoanBUS.getAllTaiKhoan());
        // Khi thay đổi cbSearch
        cbSearch.addActionListener(e -> timKiemTaiKhoan());

        // Khi thay đổi cbChucVuFilter
        cbChucVuFilter.addActionListener(e -> timKiemTaiKhoan());

        // Khi người dùng nhập liệu vào txtSearch
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiemTaiKhoan(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiemTaiKhoan(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiemTaiKhoan(); }
        });
    }

    private void loadTableData(ArrayList<TaiKhoanDTO> danhSachTaiKhoan) {
        tableModel.setRowCount(0);
        for (TaiKhoanDTO tk : danhSachTaiKhoan) {
            tableModel.addRow(new Object[]{
                tk.getMaNhanVien(),
                NhanVienBUS.getNhanVienByMa(tk.getMaNhanVien()).getHoTen(),
                tk.getTenDangNhap(),
                tk.getMatKhau(),
                tk.getMaChucVu(),
                ChucVuBUS.getChucVuByMa(tk.getMaChucVu()).getTenChucVu()
            });
        }
    }

    public static DefaultTableModel getTableTaiKhoan(){
        return tableModel;
    }
    

    private void suaTaiKhoan() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tài khoản để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String maNV = ContentTable.getValueAt(selectedRow, 0).toString();
        String username = ContentTable.getValueAt(selectedRow, 2).toString();
        String password = ContentTable.getValueAt(selectedRow, 3).toString();
        String maChucVu = ContentTable.getValueAt(selectedRow, 4).toString();


        JTextField txtMaNhanVien = new JTextField(maNV);
        txtMaNhanVien.setEnabled(false);
        JTextField txtUsername = new JTextField(username);
        JTextField txtPassword = new JTextField(password);
    
        ArrayList<ChucVuDTO> dsChucVu = ChucVuBUS.getAllChucVu();
        String[] dsTenChucVu = new String[dsChucVu.size()];
        for (int i = 0; i < dsChucVu.size(); i++)
            dsTenChucVu[i] = dsChucVu.get(i).getTenChucVu();
        JComboBox<String> cbChucVu = new JComboBox<>(dsTenChucVu);
    
        // Tìm vị trí của mã chức vụ hiện tại để set selected
        for (int i = 0; i < dsChucVu.size(); i++) {
            if (dsChucVu.get(i).getMaChucVu().equals(maChucVu)) {
                cbChucVu.setSelectedIndex(i);
                break;
            }
        }
    
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    
        BiConsumer<String, JComponent> addRow = (labelText, inputField) -> {
            inputPanel.add(new JLabel(labelText));
            inputField.setPreferredSize(new Dimension(250, 25));
            inputPanel.add(inputField);
        };
    
        addRow.accept("Nhân viên:", txtMaNhanVien);
        addRow.accept("Username:", txtUsername);
        addRow.accept("Password:", txtPassword);
        addRow.accept("Chức vụ:", cbChucVu);
    
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
    
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa Tài Khoản");
        dialog.setModal(true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        if(MainLayout.getMaNVDangDN().equals(maNV))
            cbChucVu.setEnabled(false);
    
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        btnCancel.addActionListener(e -> dialog.dispose());
    
        btnOk.addActionListener(e -> {
            String newUsername = txtUsername.getText();
            String newPassword = txtPassword.getText();
            String newMaChucVu = dsChucVu.get(cbChucVu.getSelectedIndex()).getMaChucVu();
            String newMaNhanVien = txtMaNhanVien.getText();
    
            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tkBUS.checkTextUpdate(newUsername, newPassword, newMaChucVu, newMaNhanVien))
                return;
    
            TaiKhoanDTO tk = new TaiKhoanDTO(newUsername, newPassword, newMaNhanVien, newMaChucVu, "Hiện");
    
            if (tkBUS.update(tk)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật tài khoản thành công!");
                dialog.dispose();
                loadTableData(TaiKhoanBUS.getAllTaiKhoan());
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại! Kiểm tra dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        dialog.setVisible(true);
    }

    private void timKiemTaiKhoan() {
        try{
            String kieuTim = cbSearch.getSelectedItem().toString();
            String tuKhoa = txtSearch.getText().trim();
            String chucVu = cbChucVuFilter.getSelectedItem().toString();

            // Gọi DAO
            ArrayList<TaiKhoanDTO> dsKetQua = tkBUS.search(
                kieuTim, tuKhoa, chucVu
            );
        
            // Load kết quả vào bảng
            tableModel.setRowCount(0);
            loadTableData(dsKetQua);
        } catch(NumberFormatException ne){
            JOptionPane.showMessageDialog(null, "Lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}

