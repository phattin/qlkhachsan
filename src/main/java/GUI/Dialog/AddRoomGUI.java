package GUI.Dialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;

import BUS.LoaiPhongBUS;
import BUS.PhongBUS;
import DTO.LoaiPhongDTO;
import DTO.PhongDTO;
import fillter.Button;
import fillter.Colors;

public class AddRoomGUI extends JDialog {
    private JTextField txtMaPhong, txtMaLP, txtTenLP, txtSoGiuong, txtGiaTien, txtTrangThai;
    private Button  btnAdd, btnSave, btnCancel;
    private PhongBUS phongBus;
    private LoaiPhongBUS loaiPhongBus;
    private PhongDTO phong;

    public AddRoomGUI(JFrame parent, PhongBUS phongBus, String title, String type, PhongDTO phong){
        super(parent, title, true);
        this.phongBus = phongBus;
        this.phong = phong;
        this.loaiPhongBus = new LoaiPhongBUS();
        initComponent(type);
        
        if(phong != null){
            txtMaPhong.setText(phong.getMaPhong());
            txtMaLP.setText(phong.getMaLoaiPhong());
    
            LoaiPhongDTO loaiPhong = loaiPhongBus.getById(phong.getMaLoaiPhong());
            if (loaiPhong != null) {
                txtTenLP.setText(loaiPhong.getTenLoaiPhong());
                txtSoGiuong.setText(String.valueOf(loaiPhong.getSoGiuong()));
                txtGiaTien.setText(String.valueOf(loaiPhong.getGiaPhong()));
            }
    
            txtTrangThai.setText(phong.getTrangThai());
            txtMaPhong.setEnabled(false);
        }
    
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    

    public AddRoomGUI(JFrame parent, PhongBUS phongBus, String title, String type){
        super(parent, title, true);
        this.phongBus = phongBus;
        initComponent(type);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void initComponent(String type) {
        this.loaiPhongBus = new LoaiPhongBUS();
        this.setSize(550, 350);
        this.setLayout(new BorderLayout());

        //panel input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground((Colors.MAIN_BACKGROUND));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Mã Phòng: "));
        inputPanel.add(txtMaPhong = new JTextField());
        inputPanel.add(new JLabel("Mã Loại Phòng: "));
        inputPanel.add(txtMaLP = new JTextField());
        inputPanel.add(new JLabel("Tên Loại Phòng: "));
        inputPanel.add(txtTenLP = new JTextField());
        inputPanel.add(new JLabel("Số Giường: "));
        inputPanel.add(txtSoGiuong = new JTextField());
        inputPanel.add(new JLabel("Giá Tiền/giờ: "));
        inputPanel.add(txtGiaTien = new JTextField());
        inputPanel.add(new JLabel("Trạng Thái: "));
        inputPanel.add(txtTrangThai = new JTextField());

        

        // Panel Button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(Colors.MAIN_BACKGROUND);

        btnAdd = new Button("add", "THÊM", 90, 35);
        btnSave = new Button("confirm", "LƯU", 90, 35);
        btnCancel = new Button("cancel", "HỦY", 90, 35);

        switch (type){
            case "add" -> btnPanel.add(btnAdd);
            case "save" -> btnPanel.add(btnSave);
        }
        btnPanel.add(btnCancel);

        this.add(inputPanel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);

        //Action Listeners
        btnCancel.addActionListener(e -> dispose());
        btnAdd.addActionListener(e -> addRoom());
        btnSave.addActionListener(e -> saveRoom());

        if (phong != null) { 
            txtTenLP.setEditable(false);
            txtSoGiuong.setEditable(false);
            txtGiaTien.setEditable(false);
        } 
    
        txtMaLP.getDocument().addDocumentListener(new DocumentListener() {
            @Override
        public void insertUpdate(DocumentEvent e) {
            updateLoaiPhongInfo();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateLoaiPhongInfo();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateLoaiPhongInfo();
        }
        });
    }

    private void updateLoaiPhongInfo() {
        String maLP = txtMaLP.getText().trim();
    
        // Nếu mã loại phòng trống, không làm gì cả
        if (maLP.isEmpty()) {
            txtTenLP.setText("");
            txtSoGiuong.setText("");
            txtGiaTien.setText("");
            return;
        }
    
        // Lấy thông tin loại phòng từ cơ sở dữ liệu
        LoaiPhongDTO loaiPhong = loaiPhongBus.getById(maLP);
        if (loaiPhong != null) {
            // Nếu tìm thấy loại phòng, cập nhật thông tin
            txtTenLP.setText(loaiPhong.getTenLoaiPhong());
            txtSoGiuong.setText(String.valueOf(loaiPhong.getSoGiuong()));
            txtGiaTien.setText(String.valueOf(loaiPhong.getGiaPhong()));
        } 
    }
    
    

    private void addRoom(){
        if(!checkFormInput()) return;
        String maPhong = txtMaPhong.getText().trim();
        String maLP = txtMaLP.getText().trim();
        String trangThai = txtTrangThai.getText().trim();

        PhongDTO phong = new PhongDTO(maPhong, maLP, trangThai);
        if(phongBus.addPhong(phong)){
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công! ");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Mã phòng đã tồn tại hoặc dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveRoom() {
        if (!checkFormInput()) return;
    
        String maPhong = txtMaPhong.getText().trim();  
        String maLP = txtMaLP.getText().trim();
        
        // Chỉ lấy thông tin mới của loại phòng khi mã loại phòng thay đổi
        LoaiPhongDTO loaiPhong = loaiPhongBus.getById(maLP);
        if (loaiPhong != null) {        
            // Cập nhật mã phòng, mã loại phòng và trạng thái
            String trangThai = txtTrangThai.getText().trim();
    
            PhongDTO phong = new PhongDTO(maPhong, maLP, trangThai);
            if (phongBus.updatePhong(phong)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mã loại phòng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    private boolean checkFormInput() {
        String maPhong = txtMaPhong.getText().trim();
        if (maPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (phong == null && phongBus.isMaPhongExists(maPhong)) {
            JOptionPane.showMessageDialog(this, "Mã phòng đã tồn tại! Vui lòng nhập mã phòng khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        String maLP = txtMaLP.getText().trim();
        if (maLP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã loại phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        String tenLP = txtTenLP.getText().trim();
        if (tenLP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên loại phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        String soGiuongStr = txtSoGiuong.getText().trim();
        if (!soGiuongStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Số giường phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        String giaTienStr = txtGiaTien.getText().trim();
        if (!giaTienStr.matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        String trangThai = txtTrangThai.getText().trim();
        if (trangThai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trạng thái không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        return true;
    }    
    
}