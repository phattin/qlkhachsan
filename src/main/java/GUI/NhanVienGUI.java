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

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
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
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch;
    private DefaultTableModel tableModel;
    private NhanVienBUS nvBUS;
    private TaiKhoanBUS tkBUS;

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

        // Thêm panel con vào pSearch
        pSearch.add(panelSearchFields);

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

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<NhanVienDTO> danhSachNhanVien = NhanVienBUS.getAllNhanVien();
    
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
        JTextField txtMaNV = new JTextField();
        txtMaNV.setText(nvBUS.increaseMaNV()); // Tạo mã NV tự động
        txtMaNV.setEditable(false); // Mã NV không thể chỉnh sửa
        JTextField txtHoTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();
        JTextField txtLuong = new JTextField();
        //Chọn ngày
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(new java.util.Date()); // Gán ngày mặc định (ngày hiện tại)
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Định dạng hiển thị ngày
        // Vô hiệu hóa chỉnh sửa trong text field bên trong JDateChooser
        JTextField dateEditor = ((JTextField) dateChooser.getDateEditor().getUiComponent());
        dateEditor.setEditable(false);  // Không cho người dùng chỉnh sửa bằng tay

        JTextField txtUsername = new JTextField();
        JTextField txtPassword = new JTextField();
        //Tạo các dữ liệu cho chức vụ
        ArrayList<ChucVuDTO> dsChucVu = ChucVuBUS.getAllChucVu();
        String[] dsTenChucVu = new String[dsChucVu.size()];
        for (int i = 0; i < dsChucVu.size(); i++) {
            dsTenChucVu[i] = dsChucVu.get(i).getTenChucVu();
        }
        JComboBox<String> cbChucVu = new JComboBox<>(dsTenChucVu);
    
        JPanel panel = new JPanel(new GridLayout(11, 2));
        panel.add(new JLabel("Mã NV:")); panel.add(txtMaNV);
        panel.add(new JLabel("Họ Tên:")); panel.add(txtHoTen);
        panel.add(new JLabel("GioiTinh:")); panel.add(cbGioiTinh);
        panel.add(new JLabel("Số Điện Thoại:")); panel.add(txtSDT);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Địa Chỉ:")); panel.add(txtDiaChi);
        panel.add(new JLabel("Lương:")); panel.add(txtLuong);
        panel.add(new JLabel("Ngày Nhận Việc:")); panel.add(dateChooser);
        panel.add(new JLabel("Username:")); panel.add(txtUsername);
        panel.add(new JLabel("Password:")); panel.add(txtPassword);
        panel.add(new JLabel("Chức Vụ:")); panel.add(cbChucVu);
        while (true){
            try{
                int result = JOptionPane.showConfirmDialog(null, panel, "Thêm Khách Hàng", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String maKH = txtMaNV.getText();
                    String hoTen = txtHoTen.getText();
                    String gioiTinh = cbGioiTinh.getSelectedItem().toString();
                    String sdt = txtSDT.getText();
                    String email = txtEmail.getText();
                    String diaChi = txtDiaChi.getText();
                    String luong = txtLuong.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String ngayNhanViec = sdf.format(dateChooser.getDate());
                    String username = txtUsername.getText();
                    String password = txtPassword.getText();
                    String maChucVu = dsChucVu.get(cbChucVu.getSelectedIndex()).getMaChucVu();
            
                    if (maKH.isEmpty() || hoTen.isEmpty() || gioiTinh.isEmpty() || sdt.isEmpty() || email.isEmpty() || diaChi.isEmpty() || luong.isEmpty() || ngayNhanViec.isEmpty() || username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    NhanVienDTO nv = new NhanVienDTO(maKH, hoTen, gioiTinh, sdt, email, diaChi, Long.parseLong(luong), ngayNhanViec, "Hiện");
                    TaiKhoanDTO tk = new TaiKhoanDTO(username, password, maKH, maChucVu, "Hiện");
                    if (!nvBUS.checkTextAdd(hoTen, sdt, email, diaChi, Long.parseLong(luong)) || !tkBUS.checkTextAdd(username, password))
                        continue;
                    if (nvBUS.add(nv) && tkBUS.add(tk)) {
                        JOptionPane.showMessageDialog(null, "Thêm nhân viên và tài khoản thành công!");
                        loadTableData();
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm nhân viên và tài khoản thất bại! Kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                    break;
                }catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
        }
    }

    private void suaNhanVien() {
        JTextField txtMaNV = new JTextField();
        txtMaNV.setEditable(false); // Mã NV không thể chỉnh sửa
        JTextField txtHoTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();
        JTextField txtLuong = new JTextField();
        //Chọn ngày
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(new java.util.Date()); // Gán ngày mặc định (ngày hiện tại)
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Định dạng hiển thị ngày
        // Vô hiệu hóa chỉnh sửa trong text field bên trong JDateChooser
        JTextField dateEditor = ((JTextField) dateChooser.getDateEditor().getUiComponent());
        dateEditor.setEditable(false);  // Không cho người dùng chỉnh sửa bằng tay

        //Tạo các dữ liệu cho chức vụ
        ArrayList<ChucVuDTO> dsChucVu = ChucVuBUS.getAllChucVu();
        String[] dsTenChucVu = new String[dsChucVu.size()];
        for (int i = 0; i < dsChucVu.size(); i++) {
            dsTenChucVu[i] = dsChucVu.get(i).getTenChucVu();
        }
        JComboBox<String> cbChucVu = new JComboBox<>(dsTenChucVu);
    
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Lấy và gán thông tin nhân viên từ bảng
        txtMaNV.setText(ContentTable.getValueAt(selectedRow, 0).toString());
        txtHoTen.setText(ContentTable.getValueAt(selectedRow, 1).toString());
        cbGioiTinh.setSelectedItem((ContentTable.getValueAt(selectedRow, 2).toString()));
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

        //Tạo panel để hiển thị thông tin nhân viên
        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Mã NV:")); panel.add(txtMaNV);
        panel.add(new JLabel("Họ Tên:")); panel.add(txtHoTen);
        panel.add(new JLabel("GioiTinh:")); panel.add(cbGioiTinh);
        panel.add(new JLabel("Số Điện Thoại:")); panel.add(txtSDT);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Địa Chỉ:")); panel.add(txtDiaChi);
        panel.add(new JLabel("Lương:")); panel.add(txtLuong);
        panel.add(new JLabel("Ngày Nhận Việc:")); panel.add(dateChooser);
        while (true){
            try{
                int result = JOptionPane.showConfirmDialog(null, panel, "Sửa Khách Hàng", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String maKH = txtMaNV.getText();
                    String hoTen = txtHoTen.getText();
                    String gioiTinh = (String) cbGioiTinh.getSelectedItem();
                    String sdt = txtSDT.getText();
                    String email = txtEmail.getText();
                    String diaChi = txtDiaChi.getText();
                    String luong = txtLuong.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String ngayNhanViec = sdf.format(dateChooser.getDate());
            
                    if (maKH.isEmpty() || hoTen.isEmpty() || gioiTinh.isEmpty() || sdt.isEmpty() || email.isEmpty() || diaChi.isEmpty() || luong.isEmpty() || ngayNhanViec.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
            
                    NhanVienDTO nv = new NhanVienDTO(maKH, hoTen, gioiTinh, sdt, email, diaChi, Long.parseLong(luong), ngayNhanViec, "Hiện");
                    if (nvBUS.update(nv)) {
                        JOptionPane.showMessageDialog(null, "Sửa nhân viên thành công!");
                        loadTableData();
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa nhân viên thất bại! Kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                    break;
                }catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Lương phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
        }
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
            if (nvBUS.delete(maNV)) {
                JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
}

