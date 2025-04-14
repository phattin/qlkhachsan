package GUI;

<<<<<<< HEAD
import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;

import BUS.DatPhongBUS;
import BUS.KhachHangBUS;
import DTO.DatPhongDTO;
import DTO.KhachHangDTO;
import BUS.PhongBUS;
import DTO.PhongDTO;

public class DatPhongGUI extends JPanel {
    private JTextField txtMaKH, txtName, txtPhone, txtEmail, txtCCCD, txtAddress, txtMaDatPhong, txtMaPhong, txtLoaiPhong, txtGiaTien;
    private JSpinner spnCheckIn, spnCheckOut;
    private JCheckBox chkWifi, chkBreakfast, chkSpa;
    private JButton btnBook;
    private KhachHangBUS khachHangBUS;
    private PhongBUS phongBUS;

    public DatPhongGUI() {
        setLayout(new BorderLayout(10, 10));

        khachHangBUS = new KhachHangBUS();
        phongBUS = new PhongBUS();

        // PHẦN 1: Thông tin khách hàng
        JPanel panelKhachHang = new JPanel(new GridLayout(6, 2, 5, 5));
        panelKhachHang.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        panelKhachHang.setPreferredSize(new Dimension(600, 200));
        panelKhachHang.add(new JLabel("Mã khách hàng:"));
        txtMaKH = new JTextField();
        txtMaKH.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadCustomerInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                loadCustomerInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                loadCustomerInfo();
            }
        });
        panelKhachHang.add(txtMaKH);

        panelKhachHang.add(new JLabel("Tên khách hàng:"));
        txtName = new JTextField();
        panelKhachHang.add(txtName);

        panelKhachHang.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        panelKhachHang.add(txtPhone);

        panelKhachHang.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelKhachHang.add(txtEmail);

        panelKhachHang.add(new JLabel("CCCD:"));
        txtCCCD = new JTextField();
        panelKhachHang.add(txtCCCD);

        panelKhachHang.add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        panelKhachHang.add(txtAddress);

        // PHẦN 2: Thông tin đặt phòng 
        JPanel panelDatPhong = new JPanel(new GridLayout(6, 2, 5, 5));
        panelDatPhong.setBorder(BorderFactory.createTitledBorder("Thông tin đặt phòng"));
        panelDatPhong.setPreferredSize(new Dimension(600, 250)); 
        panelDatPhong.add(new JLabel("Mã Đặt phòng:"));
        txtMaDatPhong = new JTextField();
        panelDatPhong.add(txtMaDatPhong);

        panelDatPhong.add(new JLabel("Mã phòng:"));
        txtMaPhong = new JTextField();
        txtMaPhong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadRoomInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                loadRoomInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                loadRoomInfo();
            }
        });
        panelDatPhong.add(txtMaPhong);

        panelDatPhong.add(new JLabel("Loại phòng:"));
        txtLoaiPhong = new JTextField();
        panelDatPhong.add(txtLoaiPhong);

        panelDatPhong.add(new JLabel("Giá Tiền/ngày:"));
        txtGiaTien = new JTextField();
        panelDatPhong.add(txtGiaTien);

        panelDatPhong.add(new JLabel("Ngày nhận phòng:"));
        spnCheckIn = new JSpinner(new SpinnerDateModel());
        panelDatPhong.add(spnCheckIn);

        panelDatPhong.add(new JLabel("Ngày trả phòng:"));
        spnCheckOut = new JSpinner(new SpinnerDateModel());
        panelDatPhong.add(spnCheckOut);

        // Nút đặt phòng
        btnBook = new JButton("Đặt phòng");
        JPanel panelButton = new JPanel();
        panelButton.add(btnBook);
        btnBook.addActionListener(e -> handleBooking());

        // GỘP CÁC PANEL LẠI
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.add(panelKhachHang);
        mainPanel.add(panelDatPhong);

        add(mainPanel, BorderLayout.NORTH);
        add(panelButton, BorderLayout.SOUTH);
    }

    // Phương thức để lấy thông tin khách hàng khi nhập mã
    private void loadCustomerInfo() {
        String maKH = txtMaKH.getText().trim();
        if (maKH.isEmpty()) {
            clearCustomerInfo();
            return;
        }

        // Tìm thông tin khách hàng từ BUS
        for (KhachHangDTO customer : khachHangBUS.getAllKhachHang()) {
            if (customer.getMaKhachHang().equals(maKH)) {
                txtName.setText(customer.getHoTen());
                txtPhone.setText(customer.getSDT());
                txtEmail.setText(customer.getEmail());
                txtCCCD.setText(customer.getCCCD());
                txtAddress.setText(customer.getDiaChi());
                return;
            }
        }

        clearCustomerInfo();
    }


    private void clearCustomerInfo() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
        txtAddress.setText("");
    }

    // Phương thức để lấy thông tin phòng khi nhập mã phòng
    private void loadRoomInfo() {
        String maPhong = txtMaPhong.getText().trim();
        if (maPhong.isEmpty()) {
            clearRoomInfo();
            return;
        }

        // Tìm thông tin phòng từ BUS
        for (PhongDTO room : phongBUS.getAllPhong()) {
            if (room.getMaPhong().equals(maPhong)) {
                txtLoaiPhong.setText(room.getTenLoaiPhong());
                txtGiaTien.setText(String.valueOf(room.getGiaTien()));
                return;
            }
        }

        clearRoomInfo();
    }

    private void clearRoomInfo() {
        txtLoaiPhong.setText("");
        txtGiaTien.setText("");
    }

    private void handleBooking() {
        String maDatPhong = txtMaDatPhong.getText();
        String maPhong = txtMaPhong.getText();
        String maKH = txtMaKH.getText();

        java.util.Date ngayNhan = (java.util.Date) spnCheckIn.getValue();
        java.util.Date ngayTra = (java.util.Date) spnCheckOut.getValue();

        // Kiểm tra đầu vào
        if (maDatPhong.isEmpty() || maPhong.isEmpty() || maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra ngày
        if (ngayNhan.after(ngayTra)) {
            JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày nhận!", "Lỗi ngày", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng DTO và thêm vào database
        DatPhongDTO datPhong = new DatPhongDTO();
        datPhong.setMaDatPhong(maDatPhong);
        datPhong.setMaPhong(maPhong);
        datPhong.setMaKH(maKH);
        datPhong.setNgayNhanPhong(new java.sql.Date(ngayNhan.getTime()));
        datPhong.setNgayTraPhong(new java.sql.Date(ngayTra.getTime()));


        DatPhongBUS datPhongBUS = new DatPhongBUS();
        if (datPhongBUS.themDatPhong(datPhong)) {
            JOptionPane.showMessageDialog(this, "Đặt phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearBookingForm();
        } else {
            JOptionPane.showMessageDialog(this, "Đặt phòng thất bại! Vui lòng kiểm tra lại dữ liệu.", "Thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearBookingForm() {
        txtMaDatPhong.setText("");
        txtMaPhong.setText("");
        txtLoaiPhong.setText("");
        txtGiaTien.setText("");
        spnCheckIn.setValue(new java.util.Date());
        spnCheckOut.setValue(new java.util.Date());
    }
=======
import fillter.Button;
import fillter.Colors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import BUS.DatPhongBUS;
import BUS.PhongBUS;
import BUS.KhachHangBUS;
import DTO.DatPhongDTO;
import DTO.PhongDTO;
import GUI.Dialog.AddBookingGUI;
import GUI.Dialog.AddRoomGUI;
import DTO.KhachHangDTO;
import DTO.LoaiPhongDTO;

public class DatPhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JComboBox<String> CBFilter;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;

    private DefaultTableModel tableModel;
    private DatPhongBUS datPhongBUS;
    private PhongBUS phongBUS;
    private KhachHangBUS khachHangBUS;

    public DatPhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        datPhongBUS = new DatPhongBUS();
        phongBUS = new PhongBUS();
        khachHangBUS = new KhachHangBUS();

        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        AddBtn.addActionListener(e -> addNewBooking());
        EditBtn.addActionListener(e -> editBooking());
        DeleteBtn.addActionListener(e -> deleteBooking());

        CBFilter = new JComboBox<>(new String[]{"Tất cả", "Đặt phòng", "Phòng đã nhận"});
        CBFilter.setPreferredSize(new Dimension(120, 35));

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(CBFilter);
        PanelHeader.add(txtSearch);

        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã Đặt Phòng", "Mã Phòng", "Loại Phòng", "Giá Phòng", "Mã Khách Hàng", "Tên KH", "Ngày Nhận Phòng", "Ngày Trả Phòng"};
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
        ArrayList<DatPhongDTO> danhSachDatPhong = datPhongBUS.getAllDatPhong();

        for (DatPhongDTO datPhong : danhSachDatPhong) {
            LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(datPhong.getMaPhong());
            KhachHangDTO khachHang = khachHangBUS.getById(datPhong.getMaKH());
            tableModel.addRow(new Object[]{
                datPhong.getMaDatPhong(),
                datPhong.getMaPhong(),
                loaiPhong.getTenLoaiPhong(),
                loaiPhong.getGiaPhong(),
                datPhong.getMaKH(),
                khachHang.getHoTen(),
                datPhong.getNgayNhanPhong(),
                datPhong.getNgayTraPhong()
            });
        }
    }

    private void addNewBooking() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddBookingGUI(parentFrame, datPhongBUS, "Thêm đặt phòng", "add");
        loadTableData();
    }

    private void editBooking() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng đặt phòng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String maDatPhong = tableModel.getValueAt(selectedRow, 0).toString();
        String maPhong = tableModel.getValueAt(selectedRow, 1).toString();
        String maKH = tableModel.getValueAt(selectedRow, 4).toString(); 
        java.util.Date utilCheckIn = (java.util.Date) tableModel.getValueAt(selectedRow, 6);
        java.util.Date utilCheckOut = (java.util.Date) tableModel.getValueAt(selectedRow, 7);
    
        java.sql.Date ngayNhanPhong = new java.sql.Date(utilCheckIn.getTime());
        java.sql.Date ngayTraPhong = new java.sql.Date(utilCheckOut.getTime());
    
        DatPhongDTO datPhong = new DatPhongDTO(maDatPhong, maPhong, maKH, ngayNhanPhong, ngayTraPhong);
    
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddBookingGUI(parentFrame, datPhongBUS, "Chỉnh sửa đặt phòng", "save", datPhong);
    
        loadTableData();
    }
    
    private void deleteBooking() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đặt phòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String maDatPhong = tableModel.getValueAt(selectedRow, 0).toString(); // Cột mã đặt phòng
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa đặt phòng [" + maDatPhong + "] không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            if (datPhongBUS.deleteDatPhong(maDatPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa đặt phòng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa đặt phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

>>>>>>> origin/Nhat2
}
