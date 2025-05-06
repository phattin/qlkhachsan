package GUI.Dialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.sql.Date;

import BUS.DatPhongBUS;
import BUS.LoaiPhongBUS;
import BUS.PhongBUS;
import BUS.KhachHangBUS;
import DTO.DatPhongDTO;
import DTO.KhachHangDTO;
import DTO.LoaiPhongDTO;
import fillter.Button;
import fillter.Colors;

public class AddBookingGUI extends JDialog {
    private JTextField txtMaDP, txtMaPhong, txtTenLP, txtSoGiuong, txtGiaTien, txtMaKH, txtTenKH, txtTongTienPhong, txtTongTienDV;
    private JSpinner spnCheckIn, spnCheckOut;
    private Button btnAdd, btnSave, btnCancel;
    private JCheckBox chkThueXe, chkBreakfast, chkSpa, chkGiatUi, chkHoBoi;
    private DatPhongBUS datPhongBus;
    private DatPhongDTO datPhong;
    private LoaiPhongBUS loaiPhongBus;
    private PhongBUS phongBus;
    private KhachHangBUS khachHangBus;

    public AddBookingGUI(JFrame parent, DatPhongBUS datPhongBus, String title, String type, DatPhongDTO datPhong) {
        super(parent, title, true);
        this.datPhongBus = datPhongBus;
        this.datPhong = datPhong;
        this.loaiPhongBus = new LoaiPhongBUS();
        this.phongBus = new PhongBUS();
        this.khachHangBus = new KhachHangBUS();
        initComponent(type);

        if (datPhong != null) {
            txtMaDP.setText(datPhong.getMaDatPhong());
            txtMaPhong.setText(datPhong.getMaPhong());

            LoaiPhongDTO loaiPhong = phongBus.getLoaiPhongByMaPhong(datPhong.getMaPhong());
            if (loaiPhong != null) {
                txtTenLP.setText(loaiPhong.getTenLoaiPhong());
                txtSoGiuong.setText(String.valueOf(loaiPhong.getSoGiuong()));
                txtGiaTien.setText(String.valueOf(loaiPhong.getGiaPhong()));
            }

            txtMaKH.setText(datPhong.getMaKH());
            KhachHangDTO khachHang = khachHangBus.getById(datPhong.getMaKH());
            if (khachHang != null) {
                txtTenKH.setText(khachHang.getHoTen());
            }

            spnCheckIn.setValue(datPhong.getNgayNhanPhong());
            spnCheckOut.setValue(datPhong.getNgayTraPhong());

            txtMaDP.setEnabled(false);
        }

        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public AddBookingGUI(JFrame parent, DatPhongBUS datPhongBus, String title, String type) {
        super(parent, title, true);
        this.datPhongBus = datPhongBus;
        this.loaiPhongBus = new LoaiPhongBUS();
        this.phongBus = new PhongBUS();
        this.khachHangBus = new KhachHangBUS();
        initComponent(type);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void initComponent(String type) {
        this.setSize(600, 500);
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Mã Đặt Phòng: "));
        inputPanel.add(txtMaDP = new JTextField());
        inputPanel.add(new JLabel("Mã Phòng: "));
        inputPanel.add(txtMaPhong = new JTextField());
        inputPanel.add(new JLabel("Tên Loại Phòng: "));
        inputPanel.add(txtTenLP = new JTextField());
        inputPanel.add(new JLabel("Số Giường: "));
        inputPanel.add(txtSoGiuong = new JTextField());
        inputPanel.add(new JLabel("Giá Tiền/giờ: "));
        inputPanel.add(txtGiaTien = new JTextField());
        inputPanel.add(new JLabel("Mã Khách Hàng: "));
        inputPanel.add(txtMaKH = new JTextField());
        inputPanel.add(new JLabel("Tên Khách Hàng: "));
        inputPanel.add(txtTenKH = new JTextField());

        inputPanel.add(new JLabel("Ngày Nhận Phòng: "));
        spnCheckIn = new JSpinner(new SpinnerDateModel());
        spnCheckIn.setEditor(new JSpinner.DateEditor(spnCheckIn, "yyyy-MM-dd HH:mm"));
        inputPanel.add(spnCheckIn);

        inputPanel.add(new JLabel("Ngày Trả Phòng: "));
        spnCheckOut = new JSpinner(new SpinnerDateModel());
        spnCheckOut.setEditor(new JSpinner.DateEditor(spnCheckOut, "yyyy-MM-dd HH:mm"));
        inputPanel.add(spnCheckOut);

        // Buttons
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
        btnAdd.addActionListener(e -> addBooking());
        btnSave.addActionListener(e -> saveBooking());

        // Khi người dùng nhập mã phòng
        txtMaPhong.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateRoomInfo();
            }

            public void removeUpdate(DocumentEvent e) {
                updateRoomInfo();
            }

            public void changedUpdate(DocumentEvent e) {
                updateRoomInfo();
            }
        });

        // Khi người dùng nhập mã khách hàng
        txtMaKH.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
            updateCustomerInfo();
            }

            public void removeUpdate(DocumentEvent e) {
                updateCustomerInfo();
            }

            public void changedUpdate(DocumentEvent e) {
                updateCustomerInfo();
            }
        });

    }

    private void addBooking(){
        if (!checkFormInput()) return;
    
        // Lấy dữ liệu từ các trường nhập
        String maDP = txtMaDP.getText().trim();
        String maPhong = txtMaPhong.getText().trim();
        String maKH = txtMaKH.getText().trim();
        Date ngayNhanPhong = (Date) spnCheckIn.getValue(); // Lấy giá trị từ JSpinner check-in
        Date ngayTraPhong = (Date) spnCheckOut.getValue(); // Lấy giá trị từ JSpinner check-out

        // Tạo đối tượng DatPhongDTO với các tham số
        DatPhongDTO datPhong = new DatPhongDTO(maDP, maPhong, maKH, ngayNhanPhong, ngayTraPhong);
    
        // Thêm booking vào hệ thống
        if (datPhongBus.addDatPhong(datPhong)) {
            JOptionPane.showMessageDialog(this, "Thêm đặt phòng thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Có lỗi khi thêm đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBooking() {
        // Kiểm tra thông tin nhập vào
        if (!checkFormInput()) return;
    
        // Lấy thông tin từ các trường nhập liệu
        String maDatPhong = txtMaDP.getText().trim();
        String maPhong = txtMaPhong.getText().trim();
        String maKH = txtMaKH.getText().trim();
        Date ngayNhanPhong = (Date) spnCheckIn.getValue();
        Date ngayTraPhong = (Date) spnCheckOut.getValue(); 

        if (ngayNhanPhong == null || ngayTraPhong == null) {
            JOptionPane.showMessageDialog(this, "Ngày nhận phòng và ngày trả phòng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DatPhongDTO datPhong = new DatPhongDTO(maDatPhong, maPhong, maKH, ngayNhanPhong, ngayTraPhong);

        if (datPhongBus.updateDatPhong(datPhong)) {
            JOptionPane.showMessageDialog(this, "Cập nhật đặt phòng thành công!");
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật đặt phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private boolean checkFormInput() {
        String maDatPhong = txtMaDP.getText().trim();
        if (maDatPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã đặt phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String maPhong = txtMaPhong.getText().trim();
        if (maPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String maKH = txtMaKH.getText().trim();
        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date checkInDate = (Date) spnCheckIn.getValue();
        Date checkOutDate = (Date) spnCheckOut.getValue();

        if (checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận phòng và ngày trả phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkInDate.after(checkOutDate)) {
            JOptionPane.showMessageDialog(this, "Ngày nhận phòng phải trước ngày trả phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void updateRoomInfo() {
        String maPhong = txtMaPhong.getText().trim();
        if (!maPhong.isEmpty()) {
            LoaiPhongDTO loaiPhong = phongBus.getLoaiPhongByMaPhong(maPhong);
            if (loaiPhong != null) {
                txtTenLP.setText(loaiPhong.getTenLoaiPhong());
                txtSoGiuong.setText(String.valueOf(loaiPhong.getSoGiuong()));
                txtGiaTien.setText(String.valueOf(loaiPhong.getGiaPhong()));
            } else {
                txtTenLP.setText("");
                txtSoGiuong.setText("");
                txtGiaTien.setText("");
            }
        }
    }
    
    private void updateCustomerInfo() {
        String maKH = txtMaKH.getText().trim();
        if (!maKH.isEmpty()) {
            KhachHangDTO khachHang = khachHangBus.getById(maKH);
            if (khachHang != null) {
                txtTenKH.setText(khachHang.getHoTen());
            } else {
                txtTenKH.setText("");
            }
        }
    }
    

}

