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
import javax.swing.BoxLayout;
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
import BUS.TaiKhoanBUS;
import DTO.ChucVuDTO;
import DTO.TaiKhoanDTO;
import DTO.TaiKhoanDTO;
import fillter.Button;
import fillter.Colors;

public class TaiKhoanGUI extends JPanel {
    private Button EditBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch;
    private DefaultTableModel tableModel;
    private TaiKhoanBUS tkBUS;

    public TaiKhoanGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        tkBUS = new TaiKhoanBUS();
        tkBUS = new TaiKhoanBUS();

        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120)); // cao hơn để chứa 2 hàng

        // Hàng trên: nút
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelButtons.setBackground(Colors.MAIN_BACKGROUND);

        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        EditBtn.addActionListener(e -> suaTaiKhoan());

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

        cbSearch = new JComboBox<>(new String[]{"Tên đăng nhập", "Họ tên"});
        cbSearch.setPreferredSize(new Dimension(80, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        // Thêm vào panel con
        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Thêm panel con vào pSearch
        pSearch.add(panelSearchFields);

        // Thêm cả 2 hàng vào PanelHeader
        PanelHeader.add(panelButtons, BorderLayout.NORTH);
        PanelHeader.add(pSearch, BorderLayout.SOUTH);
        
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

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<TaiKhoanDTO> danhSachTaiKhoan = TaiKhoanBUS.getAllTaiKhoan();
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
        String[] dsTenChucVu = dsChucVu.stream().map(ChucVuDTO::getTenChucVu).toArray(String[]::new);
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
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại! Kiểm tra dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        dialog.setVisible(true);
    }

    
}

