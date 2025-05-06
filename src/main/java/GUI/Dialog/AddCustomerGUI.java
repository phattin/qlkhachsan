package GUI.Dialog;

import javax.swing.*;
import java.awt.*;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
import fillter.Colors;
import fillter.Button;


public class AddCustomerGUI extends JDialog {
    private JTextField txtMaKH, txtHoTen, txtCCCD, txtSoDienThoai, txtEmail, txtDiaChi;
    private Button btnAdd, btnSave, btnCancel;
    private KhachHangBUS khachHangBus;
    private KhachHangDTO khachHang;

    public AddCustomerGUI(JFrame parent, KhachHangBUS khachHangBus, String title, String type, KhachHangDTO khachHang) {
        super(parent, title, true);
        this.khachHangBus = khachHangBus;
        this.khachHang = khachHang;
        initComponent(type);
        if (khachHang != null) {
            txtMaKH.setText(khachHang.getMaKhachHang());
            txtHoTen.setText(khachHang.getHoTen());
            txtCCCD.setText(khachHang.getCCCD());
            txtSoDienThoai.setText(khachHang.getSDT());
            txtEmail.setText(khachHang.getEmail());
            txtDiaChi.setText(khachHang.getDiaChi());
            txtMaKH.setEnabled(false); // Không cho sửa mã khách hàng
        }
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public AddCustomerGUI(JFrame parent, KhachHangBUS khachHangBus, String title, String type) {
        super(parent, title, true);
        this.khachHangBus = khachHangBus;
        initComponent(type);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void initComponent(String type) {
        this.setSize(550, 350);
        this.setLayout(new BorderLayout());

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Mã Khách Hàng:"));
        inputPanel.add(txtMaKH = new JTextField());
        inputPanel.add(new JLabel("Họ Tên:"));
        inputPanel.add(txtHoTen = new JTextField());
        inputPanel.add(new JLabel("CCCD:"));
        inputPanel.add(txtCCCD = new JTextField());
        inputPanel.add(new JLabel("Số Điện Thoại:"));
        inputPanel.add(txtSoDienThoai = new JTextField());
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail = new JTextField());
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiaChi = new JTextField());

        // Panel button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(Colors.MAIN_BACKGROUND);

        btnAdd = new Button("add", "THÊM", 90, 35);
        btnSave = new Button("confirm", "LƯU", 90, 35);
        btnCancel = new Button("cancel", "HỦY", 90, 35);

        switch (type) {
            case "add" -> btnPanel.add(btnAdd);
            case "save" -> btnPanel.add(btnSave);
        }
        btnPanel.add(btnCancel);

        this.add(inputPanel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);

        // Action listeners
        btnCancel.addActionListener(e -> dispose());
        btnAdd.addActionListener(e -> addCustomer());
        btnSave.addActionListener(e -> saveCustomer());
    }

    private void addCustomer() {
        if (!checkFormInput()) return;
        String maKH = txtMaKH.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        KhachHangDTO khachHang = new KhachHangDTO(maKH, hoTen, cccd, sdt, email, diaChi);
        if (khachHangBus.addKhachHang(khachHang)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Mã khách hàng đã tồn tại hoặc dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCustomer() {
        if (!checkFormInput()) return;
        String maKH = txtMaKH.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        KhachHangDTO khachHang = new KhachHangDTO(maKH, hoTen, cccd, sdt, email, diaChi);
        if (khachHangBus.updateKhachHang(khachHang)) {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkFormInput() {
        String maKH = txtMaKH.getText().trim();
        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (khachHang == null && khachHangBus.isMaKHExists(maKH)) {
            JOptionPane.showMessageDialog(this, "Mã phòng đã tồn tại! Vui lòng nhập mã phòng khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String cccd = txtCCCD.getText().trim();
        if (!cccd.matches("\\d{9,12}")) {
            JOptionPane.showMessageDialog(this, "CCCD phải có từ 9 đến 12 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sdt = txtSoDienThoai.getText().trim();
        if (!sdt.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có từ 10 đến 11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String diaChi = txtDiaChi.getText().trim();
        if (diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
