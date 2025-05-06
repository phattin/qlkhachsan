package GUI.Dialog;

import BUS.DichVuBUS;
import DTO.DichVuDTO;
import fillter.Button;
import fillter.Colors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddServiceGUI extends JDialog {

    private JTextField txtMaDV;
    private JTextField txtTenDV;
    private JTextField txtGiaDV;
    private JLabel lblMaDV;
    private JLabel lblTenDV;
    private JLabel lblGiaDV;
    private Button btnAdd;
    private Button btnSave;
    private Button btnCancel;
    private DichVuBUS dichVuBUS;
    private String type; // "add" hoặc "save"
    private DichVuDTO dichVu;

    public AddServiceGUI(JFrame parent, DichVuBUS dichVuBUS, String title, String type, DichVuDTO dichVu) {
        super(parent, title, true);
        this.dichVuBUS = dichVuBUS;
        this.dichVu = dichVu;
        this.type = type;
        initComponents();

        if (dichVu != null) {
            txtMaDV.setText(dichVu.getMaDV());
            txtTenDV.setText(dichVu.getTenDV());
            txtGiaDV.setText(String.valueOf(dichVu.getGiaDV()));
            txtMaDV.setEditable(false); // Không cho phép sửa mã dịch vụ
        }

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public AddServiceGUI(JFrame parent, DichVuBUS dichVuBUS, String title, String type) {
        super(parent, title, true);
        this.dichVuBUS = dichVuBUS;
        this.type = type;
        initComponents();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        this.setSize(400, 250);
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Mã Dịch Vụ: "));
        inputPanel.add(txtMaDV = new JTextField(dichVuBUS.getAllDichVu().isEmpty() ? "DV001" : generateNextMaDV()));
        txtMaDV.setEditable(false);

        inputPanel.add(new JLabel("Tên Dịch Vụ: "));
        inputPanel.add(txtTenDV = new JTextField());

        inputPanel.add(new JLabel("Giá Dịch Vụ: "));
        inputPanel.add(txtGiaDV = new JTextField());

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

        // Action Listeners
        btnCancel.addActionListener(e -> dispose());
        if (btnAdd != null) {
            btnAdd.addActionListener(e -> addService());
        }
        if (btnSave != null) {
            btnSave.addActionListener(e -> saveService());
        }
    }

    private String generateNextMaDV() {
        java.util.List<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu();
        if (danhSachDichVu.isEmpty()) {
            return "DV001";
        }
        String lastMaDV = danhSachDichVu.get(danhSachDichVu.size() - 1).getMaDV();
        int nextNumber = Integer.parseInt(lastMaDV.substring(2)) + 1;
        return String.format("DV%03d", nextNumber);
    }

    private void addService() {
        if (!checkFormInput()) return;
        String maDV = txtMaDV.getText().trim();
        String tenDV = txtTenDV.getText().trim();
        String giaStr = txtGiaDV.getText().trim();

        try {
            int giaDV = Integer.parseInt(giaStr);
            DichVuDTO dichVu = new DichVuDTO(maDV, tenDV, giaDV);
            if (dichVuBUS.themDichVu(dichVu)) {
                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveService() {
        if (!checkFormInput()) return;
        if (dichVu == null) return; // Should not happen if called from edit

        String maDV = dichVu.getMaDV(); // Keep the original MaDV
        String tenDV = txtTenDV.getText().trim();
        String giaStr = txtGiaDV.getText().trim();

        try {
            int giaDV = Integer.parseInt(giaStr);
            DichVuDTO updatedDichVu = new DichVuDTO(maDV, tenDV, giaDV);
            if (dichVuBUS.capNhatDichVu(updatedDichVu)) {
                JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkFormInput() {
        String tenDV = txtTenDV.getText().trim();
        if (tenDV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String giaStr = txtGiaDV.getText().trim();
        if (giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!giaStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Bạn có thể thêm các getter cho các component nếu cần thiết
    public JTextField getTxtMaDV() {
        return txtMaDV;
    }

    public JTextField getTxtTenDV() {
        return txtTenDV;
    }

    public JTextField getTxtGiaDV() {
        return txtGiaDV;
    }
}