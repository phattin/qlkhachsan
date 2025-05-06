package GUI;

import fillter.Button;
import fillter.Colors;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.sql.Date;

import javax.swing.table.DefaultTableModel;

import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;

import java.util.ArrayList;
import BUS.DatPhongBUS;
import BUS.DichVuBUS;
import BUS.KhachHangBUS;
import BUS.PhongBUS;
import BUS.SuDungDichVuBUS;
import DTO.DatPhongDTO;
import DTO.DichVuDTO;
import DTO.KhachHangDTO;
import DTO.LoaiPhongDTO;
import DTO.PhongDTO;
import DTO.SuDungDichVuDTO;
import com.toedter.calendar.JDateChooser;

public class DatPhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn, ViewEmptyRoomBtn;
    private JPanel PanelHeader, PanelContent;
    private JTable ContentTable;
    private DefaultTableModel tableModel;
    private DatPhongBUS datPhongBUS;
    private JTextField txtSearch; // Thêm trường tìm kiếm



    public DatPhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));
    
        //PANEL HEADER
        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));
    
        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        ViewEmptyRoomBtn = new Button("menuButton", "Danh Sách Phòng Trống", 210, 30, "/Icon/PHONG.png");
        
        // Thêm ô tìm kiếm
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        // Thêm chức năng tìm kiếm
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiem();
            }
        });

        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(txtSearch);
        PanelHeader.add(ViewEmptyRoomBtn);

    
        // PANEL CONTENT
        datPhongBUS = new DatPhongBUS();
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);
    
        String[] columnNames = {"Mã Đặt Phòng", "Mã Phòng", "Tên Khách Hàng", "Ngày Nhận Phòng", "Ngày Trả Phòng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);
    
        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));
    
        PanelContent.add(scrollPane, BorderLayout.CENTER);
        
        // Panel chính chỉ chứa bảng
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Colors.MAIN_BACKGROUND);
        mainPanel.add(PanelContent, BorderLayout.CENTER);
    
        // Add panels to main layout
        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
    
        // Load table data
        loadTableData();
    
        // Add action listener to buttons
        AddBtn.addActionListener(e -> themDatPhong());
        DeleteBtn.addActionListener(e -> xoaDatPhong());
        EditBtn.addActionListener(e -> suaDatPhong());
        ViewEmptyRoomBtn.addActionListener(e -> xemPhongTrong());
    }

    // Thêm phương thức tìm kiếm
    private void timKiem() {
        String tuKhoa = txtSearch.getText().toLowerCase().trim();
        if (tuKhoa.isEmpty()) {
            loadTableData();  // Nếu ô tìm kiếm trống, hiển thị tất cả dữ liệu
            return;
        }

        tableModel.setRowCount(0);
        ArrayList<DatPhongDTO> danhSachDatPhong = datPhongBUS.getAllDatPhong();
        KhachHangBUS khachHangBUS = new KhachHangBUS();

        for (DatPhongDTO dp : danhSachDatPhong) {
            KhachHangDTO khachHang = khachHangBUS.getById(dp.getMaKH());
            String tenKhachHang = (khachHang != null) ? khachHang.getHoTen() : "Không tìm thấy";
            
            boolean timThay = false;
            
            // Tìm kiếm trên tất cả các cột
            if (dp.getMaDatPhong().toLowerCase().contains(tuKhoa) ||
                dp.getMaPhong().toLowerCase().contains(tuKhoa) ||
                dp.getMaKH().toLowerCase().contains(tuKhoa) ||
                tenKhachHang.toLowerCase().contains(tuKhoa) ||
                (dp.getNgayNhanPhong() != null && dp.getNgayNhanPhong().toString().toLowerCase().contains(tuKhoa)) ||
                (dp.getNgayTraPhong() != null && dp.getNgayTraPhong().toString().toLowerCase().contains(tuKhoa))) {
                timThay = true;
            }
            
            if (timThay) {
                tableModel.addRow(new Object[]{
                    dp.getMaDatPhong(),
                    dp.getMaPhong(),
                    tenKhachHang,
                    dp.getNgayNhanPhong(),
                    dp.getNgayTraPhong()
                });
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<DatPhongDTO> danhSachDatPhong = datPhongBUS.getAllDatPhong();
        KhachHangBUS khachHangBUS = new KhachHangBUS();

        for (DatPhongDTO dp : danhSachDatPhong) {
            KhachHangDTO khachHang = khachHangBUS.getById(dp.getMaKH());
            String tenKhachHang = (khachHang != null) ? khachHang.getHoTen() : "Không tìm thấy";
            tableModel.addRow(new Object[]{
                dp.getMaDatPhong(),
                dp.getMaPhong(),
                tenKhachHang,
                dp.getNgayNhanPhong(),
                dp.getNgayTraPhong()
            });
        }
    }

    private void themDatPhong() {
        JDialog dialog = new JDialog((Frame) null, "Thêm Đặt Phòng", true);
        dialog.setSize(1100, 570); // Điều chỉnh kích thước phù hợp
        dialog.setBackground(Colors.SUB_BACKGROUND);
        dialog.setLocationRelativeTo(null);

        // Tạo inputPanel bên trái
        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(27, 25, 25, 25));

        inputPanel.add(new JLabel("Mã Đặt Phòng: "));
        JTextField txtMaDatPhong = new JTextField();
        inputPanel.add(txtMaDatPhong);

        inputPanel.add(new JLabel("Mã Khách Hàng: "));
        JTextField txtMaKH = new JTextField();
        inputPanel.add(txtMaKH);

        inputPanel.add(new JLabel("Họ Tên: "));
        JTextField txtHoTen = new JTextField();
        txtHoTen.setEditable(false); // Không cho phép chỉnh sửa
        inputPanel.add(txtHoTen);

        inputPanel.add(new JLabel("Mã Phòng: "));
        JTextField txtMaPhong = new JTextField();
        inputPanel.add(txtMaPhong);

        inputPanel.add(new JLabel("Giá Phòng/Ngày: "));
        JTextField txtGiaPhong = new JTextField();
        txtGiaPhong.setEditable(false); // Không cho phép chỉnh sửa
        inputPanel.add(txtGiaPhong);

        txtMaKH.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateHoTen();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateHoTen();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateHoTen();
            }

            private void updateHoTen() {
                String maKH = txtMaKH.getText();
                if (!maKH.isEmpty()) {
                    KhachHangBUS khachHangBUS = new KhachHangBUS();
                    KhachHangDTO khachHang = khachHangBUS.getById(maKH);
                    if (khachHang != null) {
                        txtHoTen.setText(khachHang.getHoTen());
                    } else {
                        txtHoTen.setText(""); // Xóa nếu không tìm thấy
                    }
                } else {
                    txtHoTen.setText(""); // Xóa nếu trường mã khách hàng trống
                }
            }
        });

        // Lắng nghe sự kiện thay đổi nội dung của txtMaPhong
        txtMaPhong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateGiaPhong();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateGiaPhong();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateGiaPhong();
            }

            private void updateGiaPhong() {
                String maPhong = txtMaPhong.getText();
                if (!maPhong.isEmpty()) {
                    PhongBUS phongBUS = new PhongBUS();
                    LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(maPhong);
                    if (loaiPhong != null) {
                        txtGiaPhong.setText(String.valueOf(loaiPhong.getGiaPhong()));
                    } else {
                        txtGiaPhong.setText(""); // Xóa nếu không tìm thấy
                    }
                } else {
                    txtGiaPhong.setText(""); // Xóa nếu trường mã phòng trống
                }
            }
        });
        inputPanel.add(new JLabel("Ngày Nhận: "));
        JSpinner spinnerNgayNhan = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayNhan = new JSpinner.DateEditor(spinnerNgayNhan, "dd/MM/yyyy HH:mm");
        spinnerNgayNhan.setEditor(editorNgayNhan);
        inputPanel.add(spinnerNgayNhan);

        inputPanel.add(new JLabel("Ngày Trả: "));
        JSpinner spinnerNgayTra = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayTra = new JSpinner.DateEditor(spinnerNgayTra, "dd/MM/yyyy HH:mm");
        spinnerNgayTra.setEditor(editorNgayTra);
        inputPanel.add(spinnerNgayTra);

        inputPanel.add(new JLabel("Tổng Tiền Phòng: "));
        JTextField txtTongTien = new JTextField();
        txtTongTien.setEditable(false); // Không cho phép chỉnh sửa vì tổng tiền sẽ được tính tự động
        inputPanel.add(txtTongTien);

        // Lắng nghe sự kiện thay đổi ngày nhận hoặc ngày trả để tính tổng tiền
        ChangeListener dateChangeListener = e -> calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
        spinnerNgayNhan.addChangeListener(dateChangeListener);
        spinnerNgayTra.addChangeListener(dateChangeListener);

        // Lắng nghe sự kiện thay đổi giá phòng để tính tổng tiền
        txtGiaPhong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
            }
        });

        // Đặt font cho tất cả các thành phần trong inputPanel
        Font inputFont = new Font("Arial", Font.PLAIN, 14);
        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel || component instanceof JTextField || component instanceof JSpinner) {
                component.setFont(inputFont);
            }
        }

        // Tạo dichvuPanel bên phải
        JPanel dichvuPanel = new JPanel(new BorderLayout());
        dichvuPanel.setBackground(Colors.MAIN_BACKGROUND);
        dichvuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // Padding bên trong
        
        JLabel lblDanhSachDichVu = new JLabel("Danh Sách Các Dịch Vụ");
        lblDanhSachDichVu.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa
        lblDanhSachDichVu.setFont(new Font("Arial", Font.BOLD, 16)); // Đặt font chữ
        lblDanhSachDichVu.setForeground(Color.BLACK); // Đặt màu chữ
        
        // Tạo bảng hiển thị danh sách dịch vụ
        String[] dichvuNames = {"Mã Dịch Vụ", "Tên Dịch Vụ", "Giá Dịch Vụ"};
        DefaultTableModel dichVuTableModel = new DefaultTableModel(dichvuNames, 0);
        JTable dichVuTable = new JTable(dichVuTableModel);
        dichVuTable.setRowHeight(30);

       // Lấy danh sách dịch vụ từ DichVuBUS
    DichVuBUS dichVuBUS = new DichVuBUS();
    ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu(); // Đúng tên phương thức BUS
    for (DichVuDTO dv : danhSachDichVu) {
        dichVuTableModel.addRow(new Object[]{
            dv.getMaDV(), 
            dv.getTenDV(), 
            dv.getGiaDV() 
        });
    }

        JScrollPane scrollPane = new JScrollPane(dichVuTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 150)); 

        // Thêm ô nhập số lượng và nút "THÊM VÀO PHIẾU" vào dichvuPanel
        JPanel soLuongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        soLuongPanel.setBackground(Colors.MAIN_BACKGROUND);

        soLuongPanel.add(new JLabel("Số lượng:"));
        JTextField txtSoLuong = new JTextField();
        txtSoLuong.setPreferredSize(new Dimension(30, 30));

        Button btnThemVaoPhieu = new Button("add", "THÊM", 70, 30, null);
        btnThemVaoPhieu.setBackground(Color.GREEN);
        btnThemVaoPhieu.setForeground(Color.BLACK);

        // Thêm các thành phần vào soLuongPanel
        soLuongPanel.add(txtSoLuong);
        soLuongPanel.add(btnThemVaoPhieu);

        // Thêm soLuongPanel vào vị trí BorderLayout.SOUTH của dichvuPanel
        dichvuPanel.add(lblDanhSachDichVu, BorderLayout.NORTH);
        dichvuPanel.add(scrollPane, BorderLayout.CENTER);
        dichvuPanel.add(soLuongPanel, BorderLayout.SOUTH);

        //SOUTH
        JPanel suDungDichVuPanel = new JPanel(new BorderLayout());
        suDungDichVuPanel.setBackground(Colors.MAIN_BACKGROUND);
        suDungDichVuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDanhSachSuDungDichVu = new JLabel("Danh Sách Sử Dụng Các Dịch Vụ");
        lblDanhSachSuDungDichVu.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa
        lblDanhSachSuDungDichVu.setFont(new Font("Arial", Font.BOLD, 16)); // Đặt font chữ
        lblDanhSachSuDungDichVu.setForeground(Color.BLACK); // Đặt màu chữ

        String[] columnNames  = { "Mã Đặt Phòng", "Tên Dịch Vụ", "Số Lượng", "Thành Tiền"};
        DefaultTableModel suDungDichVuTableModel = new DefaultTableModel(columnNames, 0);
        JTable suDungDichVuTable = new JTable(suDungDichVuTableModel);
        suDungDichVuTable.setRowHeight(30);

        JScrollPane scrollPanel = new JScrollPane(suDungDichVuTable);
        scrollPanel.getViewport().setBackground(Colors.WHITE_FONT);
        scrollPanel.setPreferredSize(new Dimension(scrollPanel.getPreferredSize().width, 150)); 

        JPanel tongTienDichVuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tongTienDichVuPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button xoaDV = new Button("delete", "XÓA", 60, 30, null);
        Button suaDV = new Button("edit", "SỬA SỐ LƯỢNG", 127, 30, null);
        
        tongTienDichVuPanel.add(xoaDV);
        tongTienDichVuPanel.add(suaDV);
        tongTienDichVuPanel.add(new JLabel("Tổng Tiền Dịch Vụ : "));
        JTextField txtTongTienDichVu = new JTextField();
        tongTienDichVuPanel.add(txtTongTienDichVu);
        txtTongTienDichVu.setPreferredSize(new Dimension(160, 30));
        

        suDungDichVuPanel.add(lblDanhSachSuDungDichVu, BorderLayout.NORTH);
        suDungDichVuPanel.add(scrollPanel, BorderLayout.CENTER);
        suDungDichVuPanel.add(tongTienDichVuPanel, BorderLayout.SOUTH);

        JPanel dichvuWrapper = new JPanel(new BorderLayout());
        dichvuWrapper.setBackground(Colors.MAIN_BACKGROUND);

        dichvuWrapper.add(dichvuPanel, BorderLayout.NORTH);
        dichvuWrapper.add(suDungDichVuPanel, BorderLayout.SOUTH);

        // Tạo panel chính để chứa inputPanel và dichvuPanel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBackground(Colors.SUB_BACKGROUND);
        mainPanel.add(inputPanel);
        mainPanel.add(dichvuWrapper);

        // Tạo panel chứa tổng tiền đặt phòng và nút xác nhận
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(Colors.MAIN_BACKGROUND);

        // Label và TextField hiển thị tổng tiền đặt phòng
        JLabel lblTongTienDatPhong = new JLabel("Tổng Tiền Đặt Phòng: ");
        JTextField txtTongTienDatPhong = new JTextField();
        txtTongTienDatPhong.setPreferredSize(new Dimension(180, 30));
        txtTongTienDatPhong.setEditable(false); // Không cho phép chỉnh sửa

        // Nút xác nhận đặt phòng
        Button btnXacNhan = new Button("confirmButton", "XÁC NHẬN", 150, 30, null);
        btnXacNhan.setBackground(Color.GREEN);

        btnXacNhan.addActionListener(e -> {
            try {
                // Lấy dữ liệu từ các trường nhập liệu
                String maDatPhong = txtMaDatPhong.getText();
                String maKH = txtMaKH.getText();
                String maPhong = txtMaPhong.getText();
                Date ngayNhan = new Date(((java.util.Date) spinnerNgayNhan.getValue()).getTime());
                Date ngayTra = new Date(((java.util.Date) spinnerNgayTra.getValue()).getTime());
        
                // Kiểm tra các trường bắt buộc
                if (maDatPhong.isEmpty() || maKH.isEmpty() || maPhong.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin đặt phòng!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Tạo đối tượng DatPhongDTO
                DatPhongDTO datPhong = new DatPhongDTO(maDatPhong, maPhong, maKH, ngayNhan, ngayTra);
        
                // Gọi phương thức thêm từ DatPhongBUS
                if (datPhongBUS.addDatPhong(datPhong)) {
                    // Nếu đặt phòng thành công, lưu dịch vụ vào CSDL
                    boolean allServicesSaved = true;
                    SuDungDichVuBUS suDungDichVuBUS = new SuDungDichVuBUS();
                    
                    // Duyệt qua từng dịch vụ trong bảng và lưu vào CSDL
                    for (int i = 0; i < suDungDichVuTableModel.getRowCount(); i++) {
                        String tenDV = (String) suDungDichVuTableModel.getValueAt(i, 1);
                        int soLuong = (int) suDungDichVuTableModel.getValueAt(i, 2);
                        
                    // Lấy mã dịch vụ từ tên dịch vụ
                      String maDV = "";
                     for (DichVuDTO dv : danhSachDichVu) {
                     if (dv.getTenDV().equals(tenDV)) { // Sử dụng getTenDV()
                      maDV = dv.getMaDV();           // Sử dụng getMaDV()
                        break;
                               }
                            }
                        
                        // Tạo đối tượng SuDungDichVuDTO và lưu vào CSDL
                        if (!maDV.isEmpty()) {
                            SuDungDichVuDTO suDungDichVu = new SuDungDichVuDTO();
                            // Không cần thiết lập maSDDV vì nó sẽ được sinh tự động
                            suDungDichVu.setMaDP(maDatPhong); // Không cần chuyển đổi, giữ nguyên String
                            suDungDichVu.setMaDv(maDV);       // Không cần chuyển đổi, giữ nguyên String
                            suDungDichVu.setSoLuong(soLuong);
                            
                            if (!suDungDichVuBUS.themSuDungDichVu(suDungDichVu)) {
                                allServicesSaved = false;
                            }
                        }
                    }
                    
                    // Hiển thị thông báo
                    if (allServicesSaved) {
                        JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng và dịch vụ thành công!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng thành công nhưng một số dịch vụ không được lưu!", 
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    dialog.dispose();
                    loadTableData(); // Cập nhật lại bảng
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Thêm các thành phần vào footerPanel
        footerPanel.add(lblTongTienDatPhong);
        footerPanel.add(txtTongTienDatPhong);
        footerPanel.add(btnXacNhan);

        // Tạo panel bọc footerPanel để tạo margin trên
        JPanel footerWrapper = new JPanel(new BorderLayout());
        footerWrapper.setBackground(Colors.SUB_BACKGROUND);
        footerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Margin trên 10px

        // Thêm footerPanel vào footerWrapper
        footerWrapper.add(footerPanel, BorderLayout.CENTER);

        // Tạo wrapperPanel để chứa toàn bộ nội dung của dialog
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Colors.SUB_BACKGROUND);
        
        // Thêm mainPanel và footerWrapper vào wrapperPanel
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        wrapperPanel.add(footerWrapper, BorderLayout.SOUTH);

        // Thêm wrapperPanel vào dialog
        dialog.add(wrapperPanel, BorderLayout.CENTER);


        btnThemVaoPhieu.addActionListener(e -> {
            int selectedRow = dichVuTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // Lấy số lượng từ txtSoLuong
            String soLuongText = txtSoLuong.getText().trim();
            if (soLuongText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số lượng!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            try {
                int soLuong = Integer.parseInt(soLuongText);
                if (soLuong <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
        
                // Lấy thông tin dịch vụ từ hàng đã chọn
                String maDV = (String) dichVuTableModel.getValueAt(selectedRow, 0);
                String tenDV = (String) dichVuTableModel.getValueAt(selectedRow, 1);
                int giaDV = (int) dichVuTableModel.getValueAt(selectedRow, 2);
        
                // Tính thành tiền
                int thanhTien = giaDV * soLuong;
        
                // Lấy mã đặt phòng từ txtMaDatPhong
                String maDatPhong = txtMaDatPhong.getText();
                if (maDatPhong.isEmpty()) {
                    maDatPhong = "(Chưa có)"; // Nếu chưa nhập mã đặt phòng
                }
                
        
                // Thêm vào bảng sử dụng dịch vụ
                suDungDichVuTableModel.addRow(new Object[]{
                    maDatPhong,
                    tenDV,
                    soLuong,
                    thanhTien
                });
        
                // Reset số lượng
                txtSoLuong.setText("");
                // Tính tổng tiền dịch vụ
                capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
                
                // Cập nhật tổng tiền đặt phòng
                capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
        
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            loadTableData();
        });

        // Cập nhật xử lý cho nút xóa dịch vụ
        xoaDV.addActionListener(e -> {
            int selectedRow = suDungDichVuTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách để xóa!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Xóa dòng được chọn
            suDungDichVuTableModel.removeRow(selectedRow);
            
            // Cập nhật lại tổng tiền dịch vụ
            capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
            
            // Cập nhật tổng tiền đặt phòng
            capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
        });

        // Thêm xử lý cho nút sửa số lượng
        suaDV.addActionListener(e -> {
            int selectedRow = suDungDichVuTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách để sửa số lượng!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy tên dịch vụ để tìm giá
            String tenDV = (String) suDungDichVuTableModel.getValueAt(selectedRow, 1);
            
            // Tìm giá dịch vụ từ bảng dịch vụ
            int giaDV = 0;
            for (int i = 0; i < dichVuTableModel.getRowCount(); i++) {
                if (dichVuTableModel.getValueAt(i, 1).equals(tenDV)) {
                    giaDV = (int) dichVuTableModel.getValueAt(i, 2);
                    break;
                }
            }
            
            // Hỏi số lượng mới
            String soLuongMoi = JOptionPane.showInputDialog(dialog, "Nhập số lượng mới:", 
                    suDungDichVuTableModel.getValueAt(selectedRow, 2));
            
            if (soLuongMoi != null && !soLuongMoi.isEmpty()) {
                try {
                    int newSoLuong = Integer.parseInt(soLuongMoi);
                    if (newSoLuong <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!", 
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // Tính lại thành tiền
                    int thanhTienMoi = giaDV * newSoLuong;
                    
                    // Cập nhật lại bảng
                    suDungDichVuTableModel.setValueAt(newSoLuong, selectedRow, 2);
                    suDungDichVuTableModel.setValueAt(thanhTienMoi, selectedRow, 3);
                    
                    // Cập nhật lại tổng tiền dịch vụ
                    capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
                    
                    // Cập nhật tổng tiền đặt phòng
                    capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Hiển thị dialog
        dialog.setVisible(true);

    }

    private void xoaDatPhong() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đặt phòng để xóa!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String maDatPhong = (String) tableModel.getValueAt(selectedRow, 0); // Lấy mã đặt phòng từ cột đầu tiên
        
        // Xác nhận lại việc xóa
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa đặt phòng " + maDatPhong + "?\n" +
            "Lưu ý: Tất cả dịch vụ đã sử dụng liên quan cũng sẽ bị xóa.", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Xóa các dịch vụ sử dụng liên quan trước
                SuDungDichVuBUS suDungDichVuBUS = new SuDungDichVuBUS();
                boolean xoaDichVuThanhCong = suDungDichVuBUS.xoaSuDungDichVuTheoMaDP(maDatPhong);
                
                if (!xoaDichVuThanhCong) {
                    int confirmDel = JOptionPane.showConfirmDialog(this, 
                        "Không thể xóa hết dịch vụ đã sử dụng. Bạn vẫn muốn tiếp tục xóa đặt phòng?", 
                        "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (confirmDel != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                
                // Xóa đặt phòng
                if (datPhongBUS.deleteDatPhong(maDatPhong)) {
                    JOptionPane.showMessageDialog(this, "Xóa đặt phòng thành công!", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData(); // Cập nhật lại bảng
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa đặt phòng thất bại!\n" +
                        "Có thể đặt phòng đã được thanh toán hoặc có ràng buộc khác.", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa đặt phòng: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Phương thức tính tổng tiền phòng
    private void calculateTotal(JTextField txtGiaPhong, JSpinner spinnerNgayNhan, JSpinner spinnerNgayTra, JTextField txtTongTien) {
        try {
            // Lấy giá phòng
            String giaPhongText = txtGiaPhong.getText();
            if (giaPhongText.isEmpty()) {
                txtTongTien.setText("");
                return;
            }
            double giaPhong = Double.parseDouble(giaPhongText);

            // Lấy ngày nhận và ngày trả
            java.util.Date ngayNhan = (java.util.Date) spinnerNgayNhan.getValue();
            java.util.Date ngayTra = (java.util.Date) spinnerNgayTra.getValue();

            // Tính số ngày
            long diffInMillies = ngayTra.getTime() - ngayNhan.getTime();
            long soNgay = diffInMillies / (1000 * 60 * 60 * 24); // Chuyển đổi từ milliseconds sang ngày

            if (soNgay <= 0) {
                txtTongTien.setText("0"); // Nếu ngày trả <= ngày nhận, tổng tiền là 0
                return;
            }

            // Tính tổng tiền
            double tongTien = giaPhong * soNgay;
            txtTongTien.setText(String.valueOf(tongTien));
        } catch (Exception e) {
            txtTongTien.setText(""); // Xóa nếu có lỗi
        }
    }

    // Phương thức tính tổng tiền dịch vụ
    private void capNhatTongTienDichVu(DefaultTableModel suDungDichVuTableModel, JTextField txtTongTienDichVu) {
        int tongTienDV = 0;
        for (int i = 0; i < suDungDichVuTableModel.getRowCount(); i++) {
            tongTienDV += (int) suDungDichVuTableModel.getValueAt(i,3);
        }
        txtTongTienDichVu.setText(String.valueOf(tongTienDV));
    }

    // Phương thức tính tổng tiền đặt phòng
    private void capNhatTongTienDatPhong(JTextField txtTongTien, JTextField txtTongTienDichVu, JTextField txtTongTienDatPhong) {
        try {
            double tongTienPhong = 0;
            if (!txtTongTien.getText().isEmpty()) {
                tongTienPhong = Double.parseDouble(txtTongTien.getText());
            }
            
            double tongTienDV = 0;
            if (!txtTongTienDichVu.getText().isEmpty()) {
                tongTienDV = Double.parseDouble(txtTongTienDichVu.getText());
            }
            
            double tongTienDatPhong = tongTienPhong + tongTienDV;
            txtTongTienDatPhong.setText(String.valueOf(tongTienDatPhong));
        } catch (NumberFormatException ex) {
            txtTongTienDatPhong.setText("0");
        }
    }

    private void suaDatPhong() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đặt phòng để sửa!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy thông tin đặt phòng được chọn
        String maDatPhong = (String) tableModel.getValueAt(selectedRow, 0);
        String maPhong = (String) tableModel.getValueAt(selectedRow, 1);
        String maKH = (String) tableModel.getValueAt(selectedRow, 2);
        Date ngayNhanPhong = (Date) tableModel.getValueAt(selectedRow, 3);
        Date ngayTraPhong = (Date) tableModel.getValueAt(selectedRow, 4);
        
        // Tạo dialog tương tự như dialog thêm đặt phòng
        JDialog dialog = new JDialog((Frame) null, "Sửa Đặt Phòng", true);
        dialog.setSize(1100, 570);
        dialog.setBackground(Colors.SUB_BACKGROUND);
        dialog.setLocationRelativeTo(null);
    
        // Tạo inputPanel bên trái
        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        inputPanel.setBackground(Colors.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(27, 25, 25, 25));
    
        inputPanel.add(new JLabel("Mã Đặt Phòng: "));
        JTextField txtMaDatPhong = new JTextField(maDatPhong);
        txtMaDatPhong.setEditable(false); // Không cho phép sửa mã đặt phòng
        inputPanel.add(txtMaDatPhong);
    
        inputPanel.add(new JLabel("Mã Khách Hàng: "));
        JTextField txtMaKH = new JTextField(maKH);
        inputPanel.add(txtMaKH);
    
        inputPanel.add(new JLabel("Họ Tên: "));
        JTextField txtHoTen = new JTextField();
        txtHoTen.setEditable(false);
        inputPanel.add(txtHoTen);
    
        inputPanel.add(new JLabel("Mã Phòng: "));
        JTextField txtMaPhong = new JTextField(maPhong);
        inputPanel.add(txtMaPhong);
    
        inputPanel.add(new JLabel("Giá Phòng/Ngày: "));
        JTextField txtGiaPhong = new JTextField();
        txtGiaPhong.setEditable(false);
        inputPanel.add(txtGiaPhong);
    
        // Cập nhật họ tên khách hàng
        KhachHangBUS khachHangBUS = new KhachHangBUS();
        KhachHangDTO khachHang = khachHangBUS.getById(maKH);
        if (khachHang != null) {
            txtHoTen.setText(khachHang.getHoTen());
        }
    
        // Cập nhật giá phòng
        PhongBUS phongBUS = new PhongBUS();
        LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(maPhong);
        if (loaiPhong != null) {
            txtGiaPhong.setText(String.valueOf(loaiPhong.getGiaPhong()));
        }
    
        // Thêm các listener tương tự như trong phương thức themDatPhong
        txtMaKH.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateHoTen();
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateHoTen();
            }
    
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateHoTen();
            }
    
            private void updateHoTen() {
                String maKH = txtMaKH.getText();
                if (!maKH.isEmpty()) {
                    KhachHangBUS khachHangBUS = new KhachHangBUS();
                    KhachHangDTO khachHang = khachHangBUS.getById(maKH);
                    if (khachHang != null) {
                        txtHoTen.setText(khachHang.getHoTen());
                    } else {
                        txtHoTen.setText("");
                    }
                } else {
                    txtHoTen.setText("");
                }
            }
        });
    
        txtMaPhong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateGiaPhong();
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateGiaPhong();
            }
    
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateGiaPhong();
            }
    
            private void updateGiaPhong() {
                String maPhong = txtMaPhong.getText();
                if (!maPhong.isEmpty()) {
                    PhongBUS phongBUS = new PhongBUS();
                    LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(maPhong);
                    if (loaiPhong != null) {
                        txtGiaPhong.setText(String.valueOf(loaiPhong.getGiaPhong()));
                    } else {
                        txtGiaPhong.setText("");
                    }
                } else {
                    txtGiaPhong.setText("");
                }
            }
        });
    
        // Thêm các trường ngày nhận và ngày trả
        inputPanel.add(new JLabel("Ngày Nhận: "));
        JSpinner spinnerNgayNhan = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayNhan = new JSpinner.DateEditor(spinnerNgayNhan, "dd/MM/yyyy HH:mm");
        spinnerNgayNhan.setEditor(editorNgayNhan);
        spinnerNgayNhan.setValue(new java.util.Date(ngayNhanPhong.getTime()));
        inputPanel.add(spinnerNgayNhan);
    
        inputPanel.add(new JLabel("Ngày Trả: "));
        JSpinner spinnerNgayTra = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayTra = new JSpinner.DateEditor(spinnerNgayTra, "dd/MM/yyyy HH:mm");
        spinnerNgayTra.setEditor(editorNgayTra);
        spinnerNgayTra.setValue(new java.util.Date(ngayTraPhong.getTime()));
        inputPanel.add(spinnerNgayTra);
    
        inputPanel.add(new JLabel("Tổng Tiền Phòng: "));
        JTextField txtTongTien = new JTextField();
        txtTongTien.setEditable(false);
        inputPanel.add(txtTongTien);
    
        // Lắng nghe sự kiện thay đổi ngày nhận hoặc ngày trả để tính tổng tiền
        ChangeListener dateChangeListener = e -> calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
        spinnerNgayNhan.addChangeListener(dateChangeListener);
        spinnerNgayTra.addChangeListener(dateChangeListener);
    
        txtGiaPhong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
            }
    
            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
            }
        });
    
        // Tính tổng tiền ban đầu
        calculateTotal(txtGiaPhong, spinnerNgayNhan, spinnerNgayTra, txtTongTien);
    
        // Đặt font cho tất cả các thành phần trong inputPanel
        Font inputFont = new Font("Arial", Font.PLAIN, 14);
        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel || component instanceof JTextField || component instanceof JSpinner) {
                component.setFont(inputFont);
            }
        }
    
        // Panel dịch vụ và panel sử dụng dịch vụ (tương tự như trong themDatPhong)
        // (Phần này giống với phần trong themDatPhong, có thể tách thành phương thức riêng để tái sử dụng)
        
        // Tạo dichvuPanel bên phải, hiển thị dịch vụ đã sử dụng
        JPanel dichvuPanel = new JPanel(new BorderLayout());
        dichvuPanel.setBackground(Colors.MAIN_BACKGROUND);
        dichvuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel lblDanhSachDichVu = new JLabel("Danh Sách Các Dịch Vụ");
        lblDanhSachDichVu.setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachDichVu.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSachDichVu.setForeground(Color.BLACK);
        
        // Tạo bảng hiển thị danh sách dịch vụ
        String[] dichvuNames = {"Mã Dịch Vụ", "Tên Dịch Vụ", "Giá Dịch Vụ"};
        DefaultTableModel dichVuTableModel = new DefaultTableModel(dichvuNames, 0);
        JTable dichVuTable = new JTable(dichVuTableModel);
        dichVuTable.setRowHeight(30);
    
        // Lấy danh sách dịch vụ từ DichVuBUS
        DichVuBUS dichVuBUS = new DichVuBUS();
        ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu();
        for (DichVuDTO dv : danhSachDichVu) {
            dichVuTableModel.addRow(new Object[]{
                dv.getMaDV(), 
                dv.getTenDV(), 
                dv.getGiaDV()
            });
        }
    
        JScrollPane scrollPane = new JScrollPane(dichVuTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 150));
    
        // Thêm ô nhập số lượng và nút "THÊM VÀO PHIẾU" vào dichvuPanel
        JPanel soLuongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        soLuongPanel.setBackground(Colors.MAIN_BACKGROUND);
    
        soLuongPanel.add(new JLabel("Số lượng:"));
        JTextField txtSoLuong = new JTextField();
        txtSoLuong.setPreferredSize(new Dimension(30, 30));
    
        Button btnThemVaoPhieu = new Button("add", "THÊM", 70, 30, null);
        btnThemVaoPhieu.setBackground(Color.GREEN);
        btnThemVaoPhieu.setForeground(Color.BLACK);
    
        soLuongPanel.add(txtSoLuong);
        soLuongPanel.add(btnThemVaoPhieu);
    
        dichvuPanel.add(lblDanhSachDichVu, BorderLayout.NORTH);
        dichvuPanel.add(scrollPane, BorderLayout.CENTER);
        dichvuPanel.add(soLuongPanel, BorderLayout.SOUTH);
    
        // Panel hiển thị các dịch vụ đã sử dụng
        JPanel suDungDichVuPanel = new JPanel(new BorderLayout());
        suDungDichVuPanel.setBackground(Colors.MAIN_BACKGROUND);
        suDungDichVuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        JLabel lblDanhSachSuDungDichVu = new JLabel("Danh Sách Sử Dụng Các Dịch Vụ");
        lblDanhSachSuDungDichVu.setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachSuDungDichVu.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSachSuDungDichVu.setForeground(Color.BLACK);
    
        String[] columnNames = {"Mã Đặt Phòng", "Tên Dịch Vụ", "Số Lượng", "Thành Tiền"};
        DefaultTableModel suDungDichVuTableModel = new DefaultTableModel(columnNames, 0);
        JTable suDungDichVuTable = new JTable(suDungDichVuTableModel);
        suDungDichVuTable.setRowHeight(30);
    
        // Lấy danh sách dịch vụ đã sử dụng của đặt phòng này
        SuDungDichVuBUS suDungDichVuBUS = new SuDungDichVuBUS();
        ArrayList<SuDungDichVuDTO> danhSachSuDungDichVu = suDungDichVuBUS.layDanhSachSuDungDichVuTheoDP(maDatPhong);
        for (SuDungDichVuDTO sddv : danhSachSuDungDichVu) {
            String tenDV = "";
            int giaDV = 0;
            
            // Tìm tên và giá dịch vụ từ mã dịch vụ
            for (DichVuDTO dv : danhSachDichVu) {
                if (dv.getMaDV().equals(sddv.getMaDv())) {
                    tenDV = dv.getTenDV();
                    giaDV = dv.getGiaDV();
                    break;
                }
            }
            
            int thanhTien = sddv.getSoLuong() * giaDV;
            
            suDungDichVuTableModel.addRow(new Object[]{
                sddv.getMaDP(),
                tenDV,
                sddv.getSoLuong(),
                thanhTien
            });
        }
    
        JScrollPane scrollPanel = new JScrollPane(suDungDichVuTable);
        scrollPanel.getViewport().setBackground(Colors.WHITE_FONT);
        scrollPanel.setPreferredSize(new Dimension(scrollPanel.getPreferredSize().width, 150));
    
        JPanel tongTienDichVuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tongTienDichVuPanel.setBackground(Colors.MAIN_BACKGROUND);
    
        Button xoaDV = new Button("delete", "XÓA", 60, 30, null);
        Button suaDV = new Button("edit", "SỬA SỐ LƯỢNG", 127, 30, null);
        
        tongTienDichVuPanel.add(xoaDV);
        tongTienDichVuPanel.add(suaDV);
        tongTienDichVuPanel.add(new JLabel("Tổng Tiền Dịch Vụ : "));
        JTextField txtTongTienDichVu = new JTextField();
        tongTienDichVuPanel.add(txtTongTienDichVu);
        txtTongTienDichVu.setPreferredSize(new Dimension(160, 30));
        
        suDungDichVuPanel.add(lblDanhSachSuDungDichVu, BorderLayout.NORTH);
        suDungDichVuPanel.add(scrollPanel, BorderLayout.CENTER);
        suDungDichVuPanel.add(tongTienDichVuPanel, BorderLayout.SOUTH);
    
        // Cập nhật tổng tiền dịch vụ ban đầu
        capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
    
        JPanel dichvuWrapper = new JPanel(new BorderLayout());
        dichvuWrapper.setBackground(Colors.MAIN_BACKGROUND);
    
        dichvuWrapper.add(dichvuPanel, BorderLayout.NORTH);
        dichvuWrapper.add(suDungDichVuPanel, BorderLayout.SOUTH);
    
        // Panel chính
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBackground(Colors.SUB_BACKGROUND);
        mainPanel.add(inputPanel);
        mainPanel.add(dichvuWrapper);
    
        // Panel footer với nút cập nhật
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(Colors.MAIN_BACKGROUND);
    
        JLabel lblTongTienDatPhong = new JLabel("Tổng Tiền Đặt Phòng: ");
        JTextField txtTongTienDatPhong = new JTextField();
        txtTongTienDatPhong.setPreferredSize(new Dimension(180, 30));
        txtTongTienDatPhong.setEditable(false);
    
        Button btnCapNhat = new Button("confirmButton", "CẬP NHẬT", 150, 30, null);
        btnCapNhat.setBackground(Color.GREEN);
    
        // Cập nhật tổng tiền đặt phòng ban đầu
        capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
    
        // Xử lý sự kiện cho nút cập nhật
        btnCapNhat.addActionListener(e -> {
            try {
                // Lấy dữ liệu mới
                String newMaPhong = txtMaPhong.getText();
                String newMaKH = txtMaKH.getText();
                Date newNgayNhan = new Date(((java.util.Date) spinnerNgayNhan.getValue()).getTime());
                Date newNgayTra = new Date(((java.util.Date) spinnerNgayTra.getValue()).getTime());

                // Kiểm tra dữ liệu
                if (newMaKH.isEmpty() || newMaPhong.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin đặt phòng!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tạo đối tượng DatPhongDTO với dữ liệu mới
                DatPhongDTO updatedDatPhong = new DatPhongDTO(maDatPhong, newMaPhong, newMaKH, newNgayNhan, newNgayTra);

                // Thực hiện cập nhật
                if (datPhongBUS.updateDatPhong(updatedDatPhong)) {
                    // Cập nhật thành công, xử lý các dịch vụ
                    boolean allServicesSaved = true;
                    
                    // Xóa các dịch vụ cũ và thêm lại các dịch vụ mới
                    if (!suDungDichVuBUS.xoaSuDungDichVuTheoMaDP(maDatPhong)) {
                        allServicesSaved = false;
                    }
                    
                    // Thêm lại các dịch vụ từ bảng
                    for (int i = 0; i < suDungDichVuTableModel.getRowCount(); i++) {
                        String tenDV = (String) suDungDichVuTableModel.getValueAt(i, 1);
                        int soLuong = (int) suDungDichVuTableModel.getValueAt(i, 2);
                        
                        // Lấy mã dịch vụ từ tên dịch vụ
                        String maDV = "";
                        for (DichVuDTO dv : danhSachDichVu) {
                            if (dv.getTenDV().equals(tenDV)) {
                                maDV = dv.getMaDV();
                                break;
                            }
                        }
                        
                        if (!maDV.isEmpty()) {
                            SuDungDichVuDTO suDungDichVu = new SuDungDichVuDTO();
                            suDungDichVu.setMaDP(maDatPhong);
                            suDungDichVu.setMaDv(maDV);
                            suDungDichVu.setSoLuong(soLuong);
                            
                            if (!suDungDichVuBUS.themSuDungDichVu(suDungDichVu)) {
                                allServicesSaved = false;
                            }
                        }
                    }
                    
                    // Hiển thị thông báo kết quả
                    if (allServicesSaved) {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật đặt phòng và dịch vụ thành công!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật đặt phòng thành công nhưng một số dịch vụ không được lưu!",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    dialog.dispose();
                    loadTableData(); // Cập nhật lại bảng chính
                    
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật đặt phòng thất bại!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    
        // Thêm các thành phần vào footerPanel
        footerPanel.add(lblTongTienDatPhong);
        footerPanel.add(txtTongTienDatPhong);
        footerPanel.add(btnCapNhat);
    
        // Panel wrapper cho footer
        JPanel footerWrapper = new JPanel(new BorderLayout());
        footerWrapper.setBackground(Colors.SUB_BACKGROUND);
        footerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        footerWrapper.add(footerPanel, BorderLayout.CENTER);
    
        // Panel wrapper chính
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Colors.SUB_BACKGROUND);
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        wrapperPanel.add(footerWrapper, BorderLayout.SOUTH);
    
        // Thêm wrapper panel vào dialog
        dialog.add(wrapperPanel, BorderLayout.CENTER);
    
        // Xử lý action listener cho các nút trong dialog tương tự như trong themDatPhong
        btnThemVaoPhieu.addActionListener(e -> {
            // Code xử lý thêm dịch vụ vào phiếu
            int rowDichVu = dichVuTable.getSelectedRow();
            if (rowDichVu == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            String soLuongText = txtSoLuong.getText().trim();
            if (soLuongText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số lượng!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            try {
                int soLuong = Integer.parseInt(soLuongText);
                if (soLuong <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
    
                String maDV = (String) dichVuTableModel.getValueAt(rowDichVu, 0);
                String tenDV = (String) dichVuTableModel.getValueAt(rowDichVu, 1);
                int giaDV = (int) dichVuTableModel.getValueAt(rowDichVu, 2);
    
                int thanhTien = giaDV * soLuong;
    
                suDungDichVuTableModel.addRow(new Object[]{
                    maDatPhong,
                    tenDV,
                    soLuong,
                    thanhTien
                });
    
                txtSoLuong.setText("");
                capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
                capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        xoaDV.addActionListener(e -> {
            int rowToDelete = suDungDichVuTable.getSelectedRow();
            if (rowToDelete == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách để xóa!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            suDungDichVuTableModel.removeRow(rowToDelete);
            capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
            capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
        });
    
        suaDV.addActionListener(e -> {
            int rowToUpdate = suDungDichVuTable.getSelectedRow();
            if (rowToUpdate == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách để sửa số lượng!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String tenDV = (String) suDungDichVuTableModel.getValueAt(rowToUpdate, 1);
            int giaDV = 0;
            for (int i = 0; i < dichVuTableModel.getRowCount(); i++) {
                if (dichVuTableModel.getValueAt(i, 1).equals(tenDV)) {
                    giaDV = (int) dichVuTableModel.getValueAt(i, 2);
                    break;
                }
            }
            
            String soLuongMoi = JOptionPane.showInputDialog(dialog, "Nhập số lượng mới:",
                    suDungDichVuTableModel.getValueAt(rowToUpdate, 2));
            
            if (soLuongMoi != null && !soLuongMoi.isEmpty()) {
                try {
                    int newSoLuong = Integer.parseInt(soLuongMoi);
                    if (newSoLuong <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int thanhTienMoi = giaDV * newSoLuong;
                    
                    suDungDichVuTableModel.setValueAt(newSoLuong, rowToUpdate, 2);
                    suDungDichVuTableModel.setValueAt(thanhTienMoi, rowToUpdate, 3);
                    
                    capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
                    capNhatTongTienDatPhong(txtTongTien, txtTongTienDichVu, txtTongTienDatPhong);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        // Hiển thị dialog
        dialog.setVisible(true);
    }
    /**
     * Hiển thị danh sách phòng trống trong một dialog
     */
    private void xemPhongTrong() {
        // Tạo dialog hiển thị danh sách phòng trống
        JDialog dialog = new JDialog((Frame) null, "Danh Sách Phòng Trống", true);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        // Lấy danh sách phòng từ PhongBUS
        PhongBUS phongBUS = new PhongBUS();
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();
        
        // Lọc các phòng có trạng thái "Trống"
        ArrayList<PhongDTO> danhSachPhongTrong = new ArrayList<>();
        for (PhongDTO phong : danhSachPhong) {
            if ("Trống".equals(phong.getTrangThai())) {
                danhSachPhongTrong.add(phong);
            }
        }

        // Tạo model cho bảng
        String[] columnNames = {"Mã Phòng", "Loại Phòng", "Giá Phòng/Ngày", "Số Giường", "Trạng Thái"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa dữ liệu trong bảng
            }
        };

        // Thêm dữ liệu vào model
        for (PhongDTO phong : danhSachPhongTrong) {
            LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
            if (loaiPhong != null) {
                tableModel.addRow(new Object[]{
                    phong.getMaPhong(),
                    loaiPhong.getTenLoaiPhong(),
                    loaiPhong.getGiaPhong(),
                    loaiPhong.getSoGiuong(),
                    phong.getTrangThai()
                });
            }
        }

        // Tạo bảng
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        // Tạo JScrollPane để có thể cuộn bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);

        dialog.add(scrollPane, BorderLayout.CENTER);


        // Hiển thị dialog
        dialog.setVisible(true);
    }
}
