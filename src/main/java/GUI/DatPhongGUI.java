package GUI;

import fillter.Button;
import fillter.Colors;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.table.DefaultTableModel;

import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;

import java.util.ArrayList;

import BUS.DanhSachPhongBUS;
import BUS.DatPhongBUS;
import BUS.DichVuBUS;
import BUS.KhachHangBUS;
import BUS.PhongBUS;
import BUS.SuDungDichVuBUS;
import DTO.DanhSachPhongDTO;
import DTO.DatPhongDTO;
import DTO.DichVuDTO;
import DTO.KhachHangDTO;
import DTO.LoaiPhongDTO;
import DTO.PhongDTO;
import DTO.SuDungDichVuDTO;
import com.toedter.calendar.JDateChooser;

public class DatPhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JPanel PanelHeader, PanelContent;
    private JTable ContentTable;
    private DefaultTableModel tableModel;
    private DatPhongBUS datPhongBUS;
    private JTextField txtSearch; // Thêm trường tìm kiếm



    // Sửa constructor DatPhongGUI()
    public DatPhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        //PANEL HEADER
        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120)); // Tăng chiều cao để chứa 2 hàng
        
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Colors.MAIN_BACKGROUND);
        
        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        
        buttonPanel.add(AddBtn);
        buttonPanel.add(DeleteBtn);
        buttonPanel.add(EditBtn);
        
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Colors.MAIN_BACKGROUND);
        
        // Panel tìm kiếm theo mã, SĐT, tên khách hàng
        JPanel searchFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchFieldsPanel.setBackground(Colors.MAIN_BACKGROUND);
        searchFieldsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Tìm Kiếm",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        JComboBox<String> cbSearchType = new JComboBox<>(new String[]{"Mã đặt phòng", "Số điện thoại", "Tên khách hàng"});
        cbSearchType.setPreferredSize(new Dimension(120, 30));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(150, 30));
        
        searchFieldsPanel.add(cbSearchType);
        searchFieldsPanel.add(txtSearch);
        
        // Panel lọc theo ngày đặt phòng
        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        dateFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        dateFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Ngày Đặt Phòng",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        JDateChooser dateFrom = new JDateChooser();
        JDateChooser dateTo = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(110, 30));
        dateTo.setPreferredSize(new Dimension(110, 30));
        dateFrom.setDateFormatString("yyyy-MM-dd");
        dateTo.setDateFormatString("yyyy-MM-dd");
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setEditable(false);

        Button btnClearDateFilter = new Button("edit", "Hủy lọc", 80, 30, null);
        btnClearDateFilter.setBackground(new Color(220, 220, 220));
        btnClearDateFilter.setForeground(Color.BLACK);
        btnClearDateFilter.addActionListener(e -> {
            dateFrom.setDate(null);
            dateTo.setDate(null);
            loadTableData(); // Tải lại tất cả dữ liệu
        });

        
        dateFilterPanel.add(new JLabel("Từ:"));
        dateFilterPanel.add(dateFrom);
        dateFilterPanel.add(new JLabel("Đến:"));
        dateFilterPanel.add(dateTo);
        dateFilterPanel.add(btnClearDateFilter);
        
        // Thêm các panel tìm kiếm vào searchPanel
        searchPanel.add(searchFieldsPanel);
        searchPanel.add(dateFilterPanel);
        
        // Thêm cả hai panel vào PanelHeader
        PanelHeader.add(buttonPanel, BorderLayout.NORTH);
        PanelHeader.add(searchPanel, BorderLayout.SOUTH);

        // PANEL CONTENT
        datPhongBUS = new DatPhongBUS();
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã Đặt Phòng", "Số Điện Thoại", "Tên Khách Hàng", "Ngày Nhận Phòng", "Ngày Trả Phòng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        // Add panels to main layout
        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        // Load table data
        loadTableData();

        // Add action listener to buttons
        AddBtn.addActionListener(e -> themDatPhong());
        DeleteBtn.addActionListener(e -> xoaDatPhong());
        EditBtn.addActionListener(e -> suaDatPhong());
        
        // Xử lý sự kiện tìm kiếm
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(cbSearchType, dateFrom, dateTo); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(cbSearchType, dateFrom, dateTo); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(cbSearchType, dateFrom, dateTo); }
        });
        
        cbSearchType.addActionListener(e -> timKiem(cbSearchType, dateFrom, dateTo));
        
        // Xử lý sự kiện thay đổi ngày
        dateFrom.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiem(cbSearchType, dateFrom, dateTo);
            }
        });
        
        dateTo.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiem(cbSearchType, dateFrom, dateTo);
            }
        });
    }

    // Thêm phương thức tìm kiếm 
    private void timKiem(JComboBox<String> cbSearchType, JDateChooser dateFrom, JDateChooser dateTo) {
        String searchType = cbSearchType.getSelectedItem().toString();
        String keyword = txtSearch.getText().trim().toLowerCase();
        
        // Lấy ngày từ JDateChooser
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = "";
        String toDate = "";
        
        if (dateFrom.getDate() != null) {
            fromDate = sdf.format(dateFrom.getDate());
        }
        
        if (dateTo.getDate() != null) {
            toDate = sdf.format(dateTo.getDate());
        }
        
        // Nếu không có tiêu chí tìm kiếm nào, hiển thị tất cả
        if (keyword.isEmpty() && fromDate.isEmpty() && toDate.isEmpty()) {
            loadTableData();
            return;
        }
        
        // Lấy danh sách tất cả đặt phòng
        ArrayList<DatPhongDTO> danhSachDatPhong = datPhongBUS.getAllDatPhong();
        KhachHangBUS khachHangBUS = new KhachHangBUS();
        
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);
        
        for (DatPhongDTO dp : danhSachDatPhong) {
            KhachHangDTO khachHang = khachHangBUS.getById(dp.getMaKH());
            String tenKhachHang = (khachHang != null) ? khachHang.getHoTen().toLowerCase() : "";
            String soDienThoai = (khachHang != null) ? khachHang.getSDT() : "";
            
            boolean matchesKeyword = false;
            boolean matchesDate = true;
            
            // Kiểm tra theo từ khóa
            if (!keyword.isEmpty()) {
                switch (searchType) {
                    case "Mã đặt phòng":
                        matchesKeyword = dp.getMaDatPhong().toLowerCase().contains(keyword);
                        break;
                    case "Số điện thoại":
                        matchesKeyword = soDienThoai.toLowerCase().contains(keyword);
                        break;
                    case "Tên khách hàng":
                        matchesKeyword = tenKhachHang.contains(keyword);
                        break;
                }
            } else {
                matchesKeyword = true; // Nếu không có từ khóa, coi như tất cả đều trùng khớp
            }
            
            // Kiểm tra theo ngày
            if (!fromDate.isEmpty()) {
                if (dp.getNgayNhanPhong().toString().compareTo(fromDate) < 0) {
                    matchesDate = false;
                }
            }
            
            if (!toDate.isEmpty()) {
                if (dp.getNgayNhanPhong().toString().compareTo(toDate) > 0) {
                    matchesDate = false;
                }
            }
            
            // Nếu thỏa mãn cả điều kiện từ khóa và ngày, thêm vào kết quả
            if (matchesKeyword && matchesDate) {
                tableModel.addRow(new Object[] {
                    dp.getMaDatPhong(),
                    soDienThoai,
                    khachHang != null ? khachHang.getHoTen() : "Không tìm thấy",
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
            String soDienThoai = (khachHang != null) ? khachHang.getSDT() : "Không tìm thấy";

            tableModel.addRow(new Object[]{
                dp.getMaDatPhong(),
                soDienThoai,
                tenKhachHang,
                dp.getNgayNhanPhong(),
                dp.getNgayTraPhong()
            });
        }
    }

    private void themDatPhong() {
        JDialog dialog = new JDialog((Frame) null, "Thêm Đặt Phòng", true);
        dialog.setSize(1100, 670); // Điều chỉnh kích thước phù hợp
        dialog.setBackground(Colors.SUB_BACKGROUND);
        dialog.setLocationRelativeTo(null);

        // Panel Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 0, 10));
        headerPanel.setBackground(Colors.SUB_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Panel bên trái chứa thông tin đặt phòng và khách hàng
        JPanel leftHeaderPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        leftHeaderPanel.setBackground(Colors.MAIN_BACKGROUND);
        leftHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        leftHeaderPanel.add(new JLabel("Mã đặt phòng :"));
        JTextField txtMaDatPhong = new JTextField();
        leftHeaderPanel.add(txtMaDatPhong);

        leftHeaderPanel.add(new JLabel("SĐT khách hàng :"));
        JTextField txtSDT = new JTextField();
        leftHeaderPanel.add(txtSDT);

        leftHeaderPanel.add(new JLabel("Tên khách hàng :"));
        JTextField txtTenKH = new JTextField();
        txtTenKH.setEditable(false);
        leftHeaderPanel.add(txtTenKH);

        // Thêm DocumentListener cho trường SĐT khách hàng
        txtSDT.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateKhachHangInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateKhachHangInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateKhachHangInfo();
            }

            private void updateKhachHangInfo() {
                String sdt = txtSDT.getText().trim();
                if (!sdt.isEmpty()) {
                    // Tìm khách hàng theo SĐT
                    KhachHangBUS khachHangBUS = new KhachHangBUS();
                    KhachHangDTO khachHang = khachHangBUS.getBySDT(sdt);
                    
                    if (khachHang != null) {
                        // Hiển thị tên khách hàng vào trường txtTenKH
                        txtTenKH.setText(khachHang.getHoTen());
                        // Lưu mã KH để sử dụng khi lưu đặt phòng
                        txtSDT.putClientProperty("MaKH", khachHang.getMaKhachHang());
                    } else {
                        // Nếu không tìm thấy khách hàng
                        txtTenKH.setText("Không tìm thấy khách hàng");
                        txtSDT.putClientProperty("MaKH", null);
                    }
                } else {
                    // Nếu trường SĐT trống
                    txtTenKH.setText("");
                    txtSDT.putClientProperty("MaKH", null);
                }
            }
        });

        // Panel bên phải chứa thông tin ngày nhận và ngày trả
        JPanel rightHeaderPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        rightHeaderPanel.setBackground(Colors.MAIN_BACKGROUND);
        rightHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        rightHeaderPanel.add(new JLabel("Ngày Nhận : "));
        JSpinner spinnerNgayNhan = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayNhan = new JSpinner.DateEditor(spinnerNgayNhan, "dd/MM/yyyy HH:mm");
        spinnerNgayNhan.setEditor(editorNgayNhan);
        rightHeaderPanel.add(spinnerNgayNhan);

        rightHeaderPanel.add(new JLabel("Ngày Trả : "));
        JSpinner spinnerNgayTra = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayTra = new JSpinner.DateEditor(spinnerNgayTra, "dd/MM/yyyy HH:mm");
        spinnerNgayTra.setEditor(editorNgayTra);
        rightHeaderPanel.add(spinnerNgayTra);

        // Thêm các panel vào headerPanel
        headerPanel.add(leftHeaderPanel);
        headerPanel.add(rightHeaderPanel);

        // Đặt font cho tất cả các thành phần trong headerPanel
        Font headerFont = new Font("Arial", Font.PLAIN, 14);
        for (Component component : leftHeaderPanel.getComponents()) {
            if (component instanceof JLabel || component instanceof JTextField) {
                component.setFont(headerFont);
            }
        }
        for (Component component : rightHeaderPanel.getComponents()) {
            if (component instanceof JLabel || component instanceof JTextField) {
                component.setFont(headerFont);
            }
        }

        // Tạo PhongPanel bên trái
        JPanel phongWrapper = new JPanel(new BorderLayout());
        phongWrapper.setBackground(Colors.MAIN_BACKGROUND);

        // Panel hiển thị danh sách phòng trống
        JPanel danhSachPhongTrongPanel = new JPanel(new BorderLayout());
        danhSachPhongTrongPanel.setBackground(Colors.MAIN_BACKGROUND);
        danhSachPhongTrongPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel lblDanhSachPhongTrong = new JLabel("Danh Sách Phòng Trống");
        lblDanhSachPhongTrong.setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachPhongTrong.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSachPhongTrong.setForeground(Color.BLACK);

        // Tạo bảng hiển thị danh sách phòng trống
        String[] phongColumnNames = {"Mã Phòng", "Loại Phòng", "Giá Phòng/Ngày", "Số Giường", "Trạng Thái"};
        DefaultTableModel phongTableModel = new DefaultTableModel(phongColumnNames, 0);
        JTable phongTable = new JTable(phongTableModel);
        phongTable.setRowHeight(30);

        // Lấy danh sách phòng từ PhongBUS
        PhongBUS phongBUS = new PhongBUS();
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();

        // Lọc các phòng có trạng thái "Trống"
        for (PhongDTO phong : danhSachPhong) {
            if ("Trống".equals(phong.getTrangThai())) {
                LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
                if (loaiPhong != null) {
                    phongTableModel.addRow(new Object[]{
                        phong.getMaPhong(),
                        loaiPhong.getTenLoaiPhong(),
                        loaiPhong.getGiaPhong(),
                        loaiPhong.getSoGiuong(),
                        phong.getTrangThai()
                    });
                }
            }
        }

        JScrollPane phongScrollPane = new JScrollPane(phongTable);
        phongScrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        phongScrollPane.setPreferredSize(new Dimension(phongScrollPane.getPreferredSize().width, 150));

        // Panel để thêm phòng vào đặt phòng
        JPanel themPhongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        themPhongPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button btnThemPhong = new Button("add", "THÊM PHÒNG", 120, 30, null);
        btnThemPhong.setBackground(Color.GREEN);
        btnThemPhong.setForeground(Color.BLACK);

        themPhongPanel.add(btnThemPhong);

        // Thêm các thành phần vào panel danh sách phòng trống
        danhSachPhongTrongPanel.add(lblDanhSachPhongTrong, BorderLayout.NORTH);
        danhSachPhongTrongPanel.add(phongScrollPane, BorderLayout.CENTER);
        danhSachPhongTrongPanel.add(themPhongPanel, BorderLayout.SOUTH);

        // Panel hiển thị danh sách phòng đã đặt
        JPanel danhSachPhongDatPanel = new JPanel(new BorderLayout());
        danhSachPhongDatPanel.setBackground(Colors.MAIN_BACKGROUND);
        danhSachPhongDatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDanhSachPhongDat = new JLabel("Danh Sách Phòng Đặt");
        lblDanhSachPhongDat.setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachPhongDat.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSachPhongDat.setForeground(Color.BLACK);

        String[] phongDatColumnNames = {"Mã Phòng", "Loại Phòng", "Giá/Ngày", "Ngày Nhận", "Ngày Trả", "Tổng Tiền"};
        DefaultTableModel phongDatTableModel = new DefaultTableModel(phongDatColumnNames, 0);
        JTable phongDatTable = new JTable(phongDatTableModel);
        phongDatTable.setRowHeight(30);

        JScrollPane phongDatScrollPane = new JScrollPane(phongDatTable);
        phongDatScrollPane.getViewport().setBackground(Colors.WHITE_FONT);
        phongDatScrollPane.setPreferredSize(new Dimension(phongDatScrollPane.getPreferredSize().width, 150));

        // Panel nút thao tác với phòng đã đặt
        JPanel phongDatActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        phongDatActionPanel.setBackground(Colors.MAIN_BACKGROUND);

        Button xoaPhongDat = new Button("delete", "XÓA", 60, 30, null);

        phongDatActionPanel.add(xoaPhongDat);
        phongDatActionPanel.add(new JLabel("Tổng Tiền Phòng: "));
        JTextField txtTongTienPhong = new JTextField();
        phongDatActionPanel.add(txtTongTienPhong);
        txtTongTienPhong.setPreferredSize(new Dimension(160, 30));
        txtTongTienPhong.setEditable(false);

        // Thêm các thành phần vào panel danh sách phòng đã đặt
        danhSachPhongDatPanel.add(lblDanhSachPhongDat, BorderLayout.NORTH);
        danhSachPhongDatPanel.add(phongDatScrollPane, BorderLayout.CENTER);
        danhSachPhongDatPanel.add(phongDatActionPanel, BorderLayout.SOUTH);

        // Thêm các panel vào phongWrapper
        phongWrapper.add(danhSachPhongTrongPanel, BorderLayout.NORTH);
        phongWrapper.add(danhSachPhongDatPanel, BorderLayout.SOUTH);        
        
        // Panel hiện danh sách các dịch vụ
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
        ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu();
        for (DichVuDTO dv : danhSachDichVu) {
            dichVuTableModel.addRow(new Object[]{
                dv.getMaDichVu(), 
                dv.getTenDichVu(), 
                dv.getGiaDichVu()
            });
        }

        JScrollPane scrollPane = new JScrollPane(dichVuTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 150)); 

            // Panel Số Lượng
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
            //End Panel Số Lượng

            //Thêm các panel vào Panel Dịch Vụ
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
            //Panel Tổng Tiền Dịch Vụ
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
        txtTongTienDichVu.setEditable(false);
            //End Panel Tổng Tiền Dịch Vụ

        suDungDichVuPanel.add(lblDanhSachSuDungDichVu, BorderLayout.NORTH);
        suDungDichVuPanel.add(scrollPanel, BorderLayout.CENTER);
        suDungDichVuPanel.add(tongTienDichVuPanel, BorderLayout.SOUTH);

            //Panel Bọc 2 Panel Dịch Vụ
        JPanel dichvuWrapper = new JPanel(new BorderLayout());
        dichvuWrapper.setBackground(Colors.MAIN_BACKGROUND);

        dichvuWrapper.add(dichvuPanel, BorderLayout.NORTH);
        dichvuWrapper.add(suDungDichVuPanel, BorderLayout.SOUTH);
            //End Panel Bọc 2 Panel Dịch Vụ

            // Tạo panel chính để chứa inputPanel và dichvuPanel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 5, 10));
        mainPanel.setBackground(Colors.SUB_BACKGROUND);
        mainPanel.add(phongWrapper);
        mainPanel.add(dichvuWrapper);

            // Panel Footer
        // Tạo footerPanel để chứa tổng tiền và nút xác nhận
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
            String SDT = txtSDT.getText();
            
            // Kiểm tra các trường bắt buộc
            if (maDatPhong.isEmpty() || SDT.isEmpty() || phongDatTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin đặt phòng và chọn ít nhất một phòng!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy mã khách hàng từ SDT
            KhachHangBUS khachHangBUS = new KhachHangBUS();
            KhachHangDTO khachHang = khachHangBUS.getBySDT(SDT);
            
            if (khachHang == null) {
                JOptionPane.showMessageDialog(dialog, "Không tìm thấy khách hàng với số điện thoại này!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String maKhachHang = khachHang.getMaKhachHang();


            // Thêm đối tượng DatPhong vào CSDL (một bản ghi chính)
            Date ngayNhanChung = new Date(((java.util.Date) spinnerNgayNhan.getValue()).getTime());
            Date ngayTraChung = new Date(((java.util.Date) spinnerNgayTra.getValue()).getTime());
            
            // Chọn phòng đầu tiên làm phòng chính cho bản ghi DatPhong
            String maPhongChinh = (String) phongDatTableModel.getValueAt(0, 0);
            DatPhongDTO datPhong = new DatPhongDTO(maDatPhong, maPhongChinh, maKhachHang, ngayNhanChung, ngayTraChung);
            
            if (datPhongBUS.addDatPhong(datPhong)) {
                // Nếu thêm đặt phòng thành công, thêm tất cả các phòng vào DanhSachPhong
                DanhSachPhongBUS danhSachPhongBUS = new DanhSachPhongBUS();
                boolean allRoomsSaved = true;
                
                // Duyệt qua từng phòng trong bảng và lưu vào CSDL
                for (int i = 0; i < phongDatTableModel.getRowCount(); i++) {
                    String maPhong = (String) phongDatTableModel.getValueAt(i, 0);
                    
                    // Tạo đối tượng DanhSachPhongDTO và lưu vào CSDL
                    DanhSachPhongDTO dSPhong = new DanhSachPhongDTO();
                    dSPhong.setMaDatPhong(maDatPhong);
                    dSPhong.setMaPhong(maPhong);
                    
                    // Thêm vào cơ sở dữ liệu thông qua BUS
                    if (!danhSachPhongBUS.themDanhSachPhong(dSPhong)) {
                        allRoomsSaved = false;
                    }
                    
                    // Cập nhật trạng thái phòng thành "Đã đặt"
                    PhongDTO phong = phongBUS.getById(maPhong);
                    if (phong != null) {
                        phong.setTrangThai("Đã đặt");
                        phongBUS.updatePhong(phong);
                    }
                }
                
                // Thêm dịch vụ vào cơ sở dữ liệu
                boolean allServicesSaved = true;
                SuDungDichVuBUS suDungDichVuBUS = new SuDungDichVuBUS();
                
                for (int i = 0; i < suDungDichVuTableModel.getRowCount(); i++) {
                    String tenDV = (String) suDungDichVuTableModel.getValueAt(i, 1);
                    int soLuong = (int) suDungDichVuTableModel.getValueAt(i, 2);
                    
                    // Lấy mã dịch vụ từ tên dịch vụ
                    String maDV = "";
                    for (DichVuDTO dv : danhSachDichVu) {
                        if (dv.getTenDichVu().equals(tenDV)) {
                            maDV = dv.getMaDichVu();
                            break;
                        }
                    }
                    
                    // Tạo đối tượng SuDungDichVuDTO và lưu vào CSDL
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
                
                // Hiển thị thông báo
                if (allRoomsSaved && allServicesSaved) {
                    JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng, danh sách phòng và dịch vụ thành công!");
                } else if (!allRoomsSaved) {
                    JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng thành công nhưng một số phòng không được lưu!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng và danh sách phòng thành công nhưng một số dịch vụ không được lưu!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
                
                dialog.dispose();
                loadTableData(); // Cập nhật lại bảng

                // Refresh bảng phòng
                PhongGUI.refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm đặt phòng thất bại!",
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
        footerPanel.add(btnXacNhan);
            //End Panel Footer

        // Tạo panel bọc footerPanel để tạo margin trên
        JPanel footerWrapper = new JPanel(new BorderLayout());
        footerWrapper.setBackground(Colors.SUB_BACKGROUND);
        footerWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // Margin trên 10px

        // Thêm footerPanel vào footerWrapper
        footerWrapper.add(footerPanel, BorderLayout.CENTER);

        // Tạo wrapperPanel để chứa toàn bộ nội dung của dialog
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Colors.SUB_BACKGROUND);
        
        // Thêm mainPanel và footerWrapper vào wrapperPanel
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);
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
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
        
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
            capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
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
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
                    } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!", 
                       "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnThemPhong.addActionListener(e -> {
            int selectedRow = phongTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một phòng từ danh sách phòng trống!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy thông tin phòng từ hàng được chọn
            String maPhong = (String) phongTableModel.getValueAt(selectedRow, 0);
            String loaiPhong = (String) phongTableModel.getValueAt(selectedRow, 1);
            double giaPhong = (double) phongTableModel.getValueAt(selectedRow, 2);

            // Lấy ngày nhận và ngày trả từ header
            java.util.Date ngayNhan = (java.util.Date) spinnerNgayNhan.getValue();
            java.util.Date ngayTra = (java.util.Date) spinnerNgayTra.getValue();

            // Kiểm tra ngày nhận và ngày trả
            if (ngayTra.before(ngayNhan) || ngayTra.equals(ngayNhan)) {
                JOptionPane.showMessageDialog(dialog, "Ngày trả phải sau ngày nhận!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tính số ngày
            long diffInMillies = ngayTra.getTime() - ngayNhan.getTime();
            long soNgay = diffInMillies / (1000 * 60 * 60 * 24);
            
            // Tính tổng tiền phòng
            double tongTien = giaPhong * soNgay;

            // Lấy mã đặt phòng từ header
            String maDatPhong = txtMaDatPhong.getText();
            if (maDatPhong.isEmpty()) {
                maDatPhong = "(Chưa có)";
            }

            // Định dạng ngày để hiển thị
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String ngayNhanStr = sdf.format(ngayNhan);
            String ngayTraStr = sdf.format(ngayTra);

            // Thêm vào bảng phòng đã đặt
            phongDatTableModel.addRow(new Object[]{
                maPhong,
                loaiPhong,
                giaPhong,
                ngayNhanStr,
                ngayTraStr,
                tongTien
            });
            
            // Cập nhật tổng tiền phòng
            capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
            
            // Cập nhật tổng tiền đặt phòng
            capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
            
            // Xóa phòng đã đặt khỏi bảng phòng trống (để tránh đặt trùng)
            phongTableModel.removeRow(selectedRow);
            
            // Thêm thông tin vào DanhSachPhong trong database khi xác nhận đặt phòng
            // (Sẽ thực hiện ở nút xác nhận)
        });

        // Xử lý sự kiện khi nhấn nút xóa phòng đã đặt
        xoaPhongDat.addActionListener(e -> {
            int selectedRow = phongDatTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một phòng từ danh sách phòng đã đặt để xóa!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy thông tin phòng để thêm lại vào phòng trống
            String maPhong = (String) phongDatTableModel.getValueAt(selectedRow, 0);
            String loaiPhong = (String) phongDatTableModel.getValueAt(selectedRow, 1);
            double giaPhong = (double) phongDatTableModel.getValueAt(selectedRow, 2);
            
            // Tìm thông tin phòng
            PhongBUS phongBus = new PhongBUS();
            PhongDTO phongInfo = phongBus.getById(maPhong);
            
            if (phongInfo != null) {
                // Tìm thông tin loại phòng
                LoaiPhongDTO loaiPhongInfo = phongBus.getLoaiPhongByMaPhong(maPhong);
                int soGiuong = 0;
                
                // Nếu tìm được loại phòng, lấy số giường, nếu không tìm được thì để mặc định là 1
                if (loaiPhongInfo != null) {
                    soGiuong = loaiPhongInfo.getSoGiuong();
                } else {
                    soGiuong = 1; // giá trị mặc định
                }
                
                // Thêm lại vào bảng phòng trống
                phongTableModel.addRow(new Object[]{
                    maPhong,
                    loaiPhong,
                    giaPhong,
                    soGiuong,
                    "Trống"
                });
                
                // Xóa khỏi bảng phòng đã đặt
                phongDatTableModel.removeRow(selectedRow);
                
                // Cập nhật tổng tiền phòng
                capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
                
                // Cập nhật tổng tiền đặt phòng
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
            } else {
                // Nếu không tìm thấy thông tin phòng, chỉ xóa khỏi bảng phòng đã đặt
                JOptionPane.showMessageDialog(dialog, 
                    "Không tìm thấy thông tin phòng trong cơ sở dữ liệu, phòng sẽ bị xóa khỏi danh sách đặt.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    
                // Xóa khỏi bảng phòng đã đặt
                phongDatTableModel.removeRow(selectedRow);
                
                // Cập nhật tổng tiền phòng
                capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
                
                // Cập nhật tổng tiền đặt phòng
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
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
            "Lưu ý: Tất cả dịch vụ đã sử dụng liên quan cũng sẽ bị xóa và các phòng sẽ được chuyển về trạng thái Trống.", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // 1. Lấy danh sách các mã phòng liên quan đến đặt phòng cần xóa
                DanhSachPhongBUS danhSachPhongBUS = new DanhSachPhongBUS();
                ArrayList<String> danhSachMaPhong = danhSachPhongBUS.layDanhSachMaPhongTheoMaDatPhong(maDatPhong);
                
                // 2. Xóa các dịch vụ sử dụng liên quan trước
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
                
                // 3. Xóa các bản ghi trong danhsachphong
                boolean xoaDanhSachPhongThanhCong = danhSachPhongBUS.xoaDanhSachPhongTheoMaDatPhong(maDatPhong);
                
                if (!xoaDanhSachPhongThanhCong && !danhSachMaPhong.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa dữ liệu từ danh sách phòng!\n" +
                        "Vui lòng kiểm tra lại dữ liệu hoặc thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 4. Cập nhật trạng thái các phòng về "Trống"
                PhongBUS phongBUS = new PhongBUS();
                boolean tatCaPhongDaCapNhat = true;
                
                for (String maPhong : danhSachMaPhong) {
                    PhongDTO phong = phongBUS.getById(maPhong);
                    if (phong != null) {
                        phong.setTrangThai("Trống");
                        if (!phongBUS.updatePhong(phong)) {
                            tatCaPhongDaCapNhat = false;
                            System.err.println("Không thể cập nhật trạng thái phòng: " + maPhong);
                        }
                    }
                }
                
                // 5. Xóa đặt phòng
                if (datPhongBUS.deleteDatPhong(maDatPhong)) {
                    if (tatCaPhongDaCapNhat) {
                        JOptionPane.showMessageDialog(this, 
                            "Xóa đặt phòng thành công và các phòng đã được chuyển về trạng thái Trống!", 
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Xóa đặt phòng thành công nhưng một số phòng không thể cập nhật trạng thái!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    loadTableData(); // Cập nhật lại bảng

                    // Refresh bảng phòng
                    PhongGUI.refreshTable();

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
    private void capNhatTongTienPhong(DefaultTableModel phongDatTableModel, JTextField txtTongTienPhong) {
        double tongTien = 0;
        for (int i = 0; i < phongDatTableModel.getRowCount(); i++) {
            tongTien += (double) phongDatTableModel.getValueAt(i, 5); // Lấy giá trị từ cột Tổng tiền
        }
        txtTongTienPhong.setText(String.valueOf(tongTien));
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
    // Phương thức tính tổng tiền dịch vụ
    private void capNhatTongTienDichVu(DefaultTableModel suDungDichVuTableModel, JTextField txtTongTienDichVu) {
        int tongTien = 0;
        for (int i = 0; i < suDungDichVuTableModel.getRowCount(); i++) {
            tongTien += (int) suDungDichVuTableModel.getValueAt(i, 3); // Cột thành tiền
        }
        txtTongTienDichVu.setText(String.valueOf(tongTien));
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
        DatPhongDTO datPhongHienTai = datPhongBUS.getById(maDatPhong);
        
        if (datPhongHienTai == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin đặt phòng!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Lấy thông tin khách hàng
        KhachHangBUS khachHangBUS = new KhachHangBUS();
        KhachHangDTO khachHang = khachHangBUS.getById(datPhongHienTai.getMaKH());
        
        JDialog dialog = new JDialog((Frame) null, "Sửa Đặt Phòng", true);
        dialog.setSize(1100, 670);
        dialog.setBackground(Colors.SUB_BACKGROUND);
        dialog.setLocationRelativeTo(null);
    
        // Panel Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 0, 10));
        headerPanel.setBackground(Colors.SUB_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    
        // Panel bên trái chứa thông tin đặt phòng và khách hàng
        JPanel leftHeaderPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        leftHeaderPanel.setBackground(Colors.MAIN_BACKGROUND);
        leftHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    
        leftHeaderPanel.add(new JLabel("Mã đặt phòng :"));
        JTextField txtMaDatPhong = new JTextField(maDatPhong);
        txtMaDatPhong.setEditable(false); // Không cho phép sửa mã đặt phòng
        leftHeaderPanel.add(txtMaDatPhong);
    
        leftHeaderPanel.add(new JLabel("SĐT khách hàng :"));
        JTextField txtSDT = new JTextField();
        if (khachHang != null) {
            txtSDT.setText(khachHang.getSDT());
            // Thêm dòng này để lưu maKH vào ClientProperty
            txtSDT.putClientProperty("MaKH", khachHang.getMaKhachHang());
        }
        leftHeaderPanel.add(txtSDT);
    
        leftHeaderPanel.add(new JLabel("Tên khách hàng :"));
        JTextField txtTenKH = new JTextField();
        txtTenKH.setEditable(false);
        if (khachHang != null) {
            txtTenKH.setText(khachHang.getHoTen());
        }
        leftHeaderPanel.add(txtTenKH);
    
        // Thêm DocumentListener cho trường SĐT khách hàng
        txtSDT.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateKhachHangInfo();
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateKhachHangInfo();
            }
    
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateKhachHangInfo();
            }
    
            private void updateKhachHangInfo() {
                String sdt = txtSDT.getText().trim();
                if (!sdt.isEmpty()) {
                    // Tìm khách hàng theo SĐT
                    KhachHangBUS khachHangBUS = new KhachHangBUS();
                    KhachHangDTO khachHang = khachHangBUS.getBySDT(sdt);
                    
                    if (khachHang != null) {
                        // Hiển thị tên khách hàng vào trường txtTenKH
                        txtTenKH.setText(khachHang.getHoTen());
                        // Lưu mã KH để sử dụng khi lưu đặt phòng
                        txtSDT.putClientProperty("MaKH", khachHang.getMaKhachHang());
                    } else {
                        // Nếu không tìm thấy khách hàng
                        txtTenKH.setText("Không tìm thấy khách hàng");
                        txtSDT.putClientProperty("MaKH", null);
                    }
                } else {
                    // Nếu trường SĐT trống
                    txtTenKH.setText("");
                    txtSDT.putClientProperty("MaKH", null);
                }
            }
        });
    
        // Panel bên phải chứa thông tin ngày nhận và ngày trả
        JPanel rightHeaderPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        rightHeaderPanel.setBackground(Colors.MAIN_BACKGROUND);
        rightHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    
        rightHeaderPanel.add(new JLabel("Ngày Nhận : "));
        JSpinner spinnerNgayNhan = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayNhan = new JSpinner.DateEditor(spinnerNgayNhan, "dd/MM/yyyy HH:mm");
        spinnerNgayNhan.setEditor(editorNgayNhan);
        spinnerNgayNhan.setValue(new java.util.Date(datPhongHienTai.getNgayNhanPhong().getTime()));
        rightHeaderPanel.add(spinnerNgayNhan);
    
        rightHeaderPanel.add(new JLabel("Ngày Trả : "));
        JSpinner spinnerNgayTra = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorNgayTra = new JSpinner.DateEditor(spinnerNgayTra, "dd/MM/yyyy HH:mm");
        spinnerNgayTra.setEditor(editorNgayTra);
        spinnerNgayTra.setValue(new java.util.Date(datPhongHienTai.getNgayTraPhong().getTime()));
        rightHeaderPanel.add(spinnerNgayTra);
    
        // Thêm các panel vào headerPanel
        headerPanel.add(leftHeaderPanel);
        headerPanel.add(rightHeaderPanel);
    
        // Đặt font cho tất cả các thành phần trong headerPanel
        Font headerFont = new Font("Arial", Font.PLAIN, 14);
        for (Component component : leftHeaderPanel.getComponents()) {
            if (component instanceof JLabel || component instanceof JTextField) {
                component.setFont(headerFont);
            }
        }
        for (Component component : rightHeaderPanel.getComponents()) {
            if (component instanceof JLabel || component instanceof JTextField) {
                component.setFont(headerFont);
            }
        }
    
        // Tạo PhongPanel bên trái
        JPanel phongWrapper = new JPanel(new BorderLayout());
        phongWrapper.setBackground(Colors.MAIN_BACKGROUND);
    
        // Panel hiển thị danh sách phòng trống
        JPanel danhSachPhongTrongPanel = new JPanel(new BorderLayout());
        danhSachPhongTrongPanel.setBackground(Colors.MAIN_BACKGROUND);
        danhSachPhongTrongPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
    
        JLabel lblDanhSachPhongTrong = new JLabel("Danh Sách Phòng Trống");
        lblDanhSachPhongTrong.setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachPhongTrong.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSachPhongTrong.setForeground(Color.BLACK);
    
        // Tạo bảng hiển thị danh sách phòng trống
        String[] phongColumnNames = {"Mã Phòng", "Loại Phòng", "Giá Phòng/Ngày", "Số Giường", "Trạng Thái"};
        DefaultTableModel phongTableModel = new DefaultTableModel(phongColumnNames, 0);
        JTable phongTable = new JTable(phongTableModel);
        phongTable.setRowHeight(30);
    
        // Lấy danh sách phòng từ PhongBUS
        PhongBUS phongBUS = new PhongBUS();
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();
    
        // Lọc các phòng có trạng thái "Trống"
        for (PhongDTO phong : danhSachPhong) {
            if ("Trống".equals(phong.getTrangThai())) {
                LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
                if (loaiPhong != null) {
                    phongTableModel.addRow(new Object[]{
                        phong.getMaPhong(),
                        loaiPhong.getTenLoaiPhong(),
                        loaiPhong.getGiaPhong(),
                        loaiPhong.getSoGiuong(),
                        phong.getTrangThai()
                    });
                }
            }
        }
    
        JScrollPane phongScrollPane = new JScrollPane(phongTable);
        phongScrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        phongScrollPane.setPreferredSize(new Dimension(phongScrollPane.getPreferredSize().width, 150));
    
        // Panel để thêm phòng vào đặt phòng
        JPanel themPhongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        themPhongPanel.setBackground(Colors.MAIN_BACKGROUND);
    
        Button btnThemPhong = new Button("add", "THÊM PHÒNG", 120, 30, null);
        btnThemPhong.setBackground(Color.GREEN);
        btnThemPhong.setForeground(Color.BLACK);
    
        themPhongPanel.add(btnThemPhong);
    
        // Thêm các thành phần vào panel danh sách phòng trống
        danhSachPhongTrongPanel.add(lblDanhSachPhongTrong, BorderLayout.NORTH);
        danhSachPhongTrongPanel.add(phongScrollPane, BorderLayout.CENTER);
        danhSachPhongTrongPanel.add(themPhongPanel, BorderLayout.SOUTH);
    
        // Panel hiển thị danh sách phòng đã đặt
        JPanel danhSachPhongDatPanel = new JPanel(new BorderLayout());
        danhSachPhongDatPanel.setBackground(Colors.MAIN_BACKGROUND);
        danhSachPhongDatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        JLabel lblDanhSachPhongDat = new JLabel("Danh Sách Phòng Đặt");
        lblDanhSachPhongDat.setHorizontalAlignment(SwingConstants.CENTER);
        lblDanhSachPhongDat.setFont(new Font("Arial", Font.BOLD, 16));
        lblDanhSachPhongDat.setForeground(Color.BLACK);
    
        String[] phongDatColumnNames = {"Mã Phòng", "Loại Phòng", "Giá/Ngày", "Ngày Nhận", "Ngày Trả", "Tổng Tiền"};
        DefaultTableModel phongDatTableModel = new DefaultTableModel(phongDatColumnNames, 0);
        JTable phongDatTable = new JTable(phongDatTableModel);
        phongDatTable.setRowHeight(30);
    
        JScrollPane phongDatScrollPane = new JScrollPane(phongDatTable);
        phongDatScrollPane.getViewport().setBackground(Colors.WHITE_FONT);
        phongDatScrollPane.setPreferredSize(new Dimension(phongDatScrollPane.getPreferredSize().width, 150));
    
        // Panel nút thao tác với phòng đã đặt
        JPanel phongDatActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        phongDatActionPanel.setBackground(Colors.MAIN_BACKGROUND);
    
        Button xoaPhongDat = new Button("delete", "XÓA", 60, 30, null);
    
        phongDatActionPanel.add(xoaPhongDat);
        phongDatActionPanel.add(new JLabel("Tổng Tiền Phòng: "));
        JTextField txtTongTienPhong = new JTextField();
        phongDatActionPanel.add(txtTongTienPhong);
        txtTongTienPhong.setPreferredSize(new Dimension(160, 30));
        txtTongTienPhong.setEditable(false);
    
        // Thêm các thành phần vào panel danh sách phòng đã đặt
        danhSachPhongDatPanel.add(lblDanhSachPhongDat, BorderLayout.NORTH);
        danhSachPhongDatPanel.add(phongDatScrollPane, BorderLayout.CENTER);
        danhSachPhongDatPanel.add(phongDatActionPanel, BorderLayout.SOUTH);
    
        // Thêm các panel vào phongWrapper
        phongWrapper.add(danhSachPhongTrongPanel, BorderLayout.NORTH);
        phongWrapper.add(danhSachPhongDatPanel, BorderLayout.SOUTH);        
        
        // Panel hiện danh sách các dịch vụ
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
                dv.getMaDichVu(), 
                dv.getTenDichVu(), 
                dv.getGiaDichVu()
            });
        }
    
        JScrollPane scrollPane = new JScrollPane(dichVuTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 150));
    
        // Panel Số Lượng
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
    
        // Thêm các panel vào Panel Dịch Vụ
        dichvuPanel.add(lblDanhSachDichVu, BorderLayout.NORTH);
        dichvuPanel.add(scrollPane, BorderLayout.CENTER);
        dichvuPanel.add(soLuongPanel, BorderLayout.SOUTH);
        
        // SOUTH
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
    
        JScrollPane scrollPanel = new JScrollPane(suDungDichVuTable);
        scrollPanel.getViewport().setBackground(Colors.WHITE_FONT);
        scrollPanel.setPreferredSize(new Dimension(scrollPanel.getPreferredSize().width, 150));
        
        // Panel Tổng Tiền Dịch Vụ
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
        txtTongTienDichVu.setEditable(false);
    
        suDungDichVuPanel.add(lblDanhSachSuDungDichVu, BorderLayout.NORTH);
        suDungDichVuPanel.add(scrollPanel, BorderLayout.CENTER);
        suDungDichVuPanel.add(tongTienDichVuPanel, BorderLayout.SOUTH);
    
        // Panel Bọc 2 Panel Dịch Vụ
        JPanel dichvuWrapper = new JPanel(new BorderLayout());
        dichvuWrapper.setBackground(Colors.MAIN_BACKGROUND);
    
        dichvuWrapper.add(dichvuPanel, BorderLayout.NORTH);
        dichvuWrapper.add(suDungDichVuPanel, BorderLayout.SOUTH);
    
        // Tạo panel chính để chứa inputPanel và dichvuPanel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 5, 10));
        mainPanel.setBackground(Colors.SUB_BACKGROUND);
        mainPanel.add(phongWrapper);
        mainPanel.add(dichvuWrapper);
    
        // Panel Footer
        // Tạo footerPanel để chứa tổng tiền và nút xác nhận
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(Colors.MAIN_BACKGROUND);
    
        // Label và TextField hiển thị tổng tiền đặt phòng
        JLabel lblTongTienDatPhong = new JLabel("Tổng Tiền Đặt Phòng: ");
        JTextField txtTongTienDatPhong = new JTextField();
        txtTongTienDatPhong.setPreferredSize(new Dimension(180, 30));
        txtTongTienDatPhong.setEditable(false);
    
        // Nút xác nhận đặt phòng
        Button btnCapNhat = new Button("confirmButton", "CẬP NHẬT", 150, 30, null);
        btnCapNhat.setBackground(Color.GREEN);
    
        // Thêm các thành phần vào footerPanel
        footerPanel.add(lblTongTienDatPhong);
        footerPanel.add(txtTongTienDatPhong);
        footerPanel.add(btnCapNhat);
    
        // Panel chính
        JPanel footerWrapper = new JPanel(new BorderLayout());
        footerWrapper.setBackground(Colors.SUB_BACKGROUND);
        footerWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        footerWrapper.add(footerPanel, BorderLayout.CENTER);
    
        // Tạo wrapperPanel để chứa toàn bộ nội dung của dialog
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Colors.SUB_BACKGROUND);
        
        // Thêm mainPanel và footerWrapper vào wrapperPanel
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        wrapperPanel.add(footerWrapper, BorderLayout.SOUTH);
    
        // Thêm wrapperPanel vào dialog
        dialog.add(wrapperPanel, BorderLayout.CENTER);
    
        // Load danh sách phòng đã đặt và dịch vụ đã sử dụng
        DanhSachPhongBUS danhSachPhongBUS = new DanhSachPhongBUS();
        ArrayList<String> danhSachMaPhong = danhSachPhongBUS.layDanhSachMaPhongTheoMaDatPhong(maDatPhong);
        
        // Load thông tin phòng đã đặt vào bảng
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String ngayNhanStr = sdf.format(datPhongHienTai.getNgayNhanPhong());
        String ngayTraStr = sdf.format(datPhongHienTai.getNgayTraPhong());
        
        // Tính số ngày
        long diffInMillies = datPhongHienTai.getNgayTraPhong().getTime() - datPhongHienTai.getNgayNhanPhong().getTime();
        long soNgay = diffInMillies / (1000 * 60 * 60 * 24);
        
        // Thêm phòng chính vào bảng phòng đã đặt
        String maPhongChinh = datPhongHienTai.getMaPhong();
        PhongDTO phongChinh = phongBUS.getById(maPhongChinh);
        if (phongChinh != null) {
            LoaiPhongDTO loaiPhongChinh = phongBUS.getLoaiPhongByMaPhong(maPhongChinh);
            if (loaiPhongChinh != null) {
                double giaPhong = loaiPhongChinh.getGiaPhong();
                double tongTien = giaPhong * soNgay;
                
                phongDatTableModel.addRow(new Object[]{
                    maPhongChinh,
                    loaiPhongChinh.getTenLoaiPhong(),
                    giaPhong,
                    ngayNhanStr,
                    ngayTraStr,
                    tongTien
                });
            }
        }
        
        // Thêm các phòng khác từ danhsachphong
        for (String maPhong : danhSachMaPhong) {
            if (!maPhong.equals(maPhongChinh)) { // Không thêm lại phòng chính
                PhongDTO phong = phongBUS.getById(maPhong);
                if (phong != null) {
                    LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(maPhong);
                    if (loaiPhong != null) {
                        double giaPhong = loaiPhong.getGiaPhong();
                        double tongTien = giaPhong * soNgay;
                        
                        phongDatTableModel.addRow(new Object[]{
                            maPhong,
                            loaiPhong.getTenLoaiPhong(),
                            giaPhong,
                            ngayNhanStr,
                            ngayTraStr,
                            tongTien
                        });
                    }
                }
            }
        }
        
        // Load dịch vụ đã sử dụng
        SuDungDichVuBUS suDungDichVuBUS = new SuDungDichVuBUS();
        ArrayList<SuDungDichVuDTO> danhSachSuDungDichVu = suDungDichVuBUS.layDanhSachSuDungDichVuTheoDP(maDatPhong);
        
        for (SuDungDichVuDTO sddv : danhSachSuDungDichVu) {
            String tenDV = "";
            int giaDV = 0;
            
            // Tìm tên và giá dịch vụ từ mã dịch vụ
            for (DichVuDTO dv : danhSachDichVu) {
                if (dv.getMaDichVu().equals(sddv.getMaDv())) {
                    tenDV = dv.getTenDichVu();
                    giaDV = dv.getGiaDichVu();
                    break;
                }
            }
            
            int thanhTien = sddv.getSoLuong() * giaDV;
            
            suDungDichVuTableModel.addRow(new Object[]{
                maDatPhong,
                tenDV,
                sddv.getSoLuong(),
                thanhTien
            });
        }
        
        // Cập nhật tổng tiền phòng và dịch vụ
        capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
        capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
        capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
    
        // Xử lý sự kiện thêm phòng
        btnThemPhong.addActionListener(e -> {
            int selectedRowPhong = phongTable.getSelectedRow();
            if (selectedRowPhong == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một phòng từ danh sách phòng trống!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            // Lấy thông tin phòng từ hàng được chọn
            String maPhong = (String) phongTableModel.getValueAt(selectedRowPhong, 0);
            String loaiPhong = (String) phongTableModel.getValueAt(selectedRowPhong, 1);
            double giaPhong = (double) phongTableModel.getValueAt(selectedRowPhong, 2);
    
            // Lấy ngày nhận và ngày trả từ header
            java.util.Date ngayNhan = (java.util.Date) spinnerNgayNhan.getValue();
            java.util.Date ngayTra = (java.util.Date) spinnerNgayTra.getValue();
    
            // Kiểm tra ngày nhận và ngày trả
            if (ngayTra.before(ngayNhan) || ngayTra.equals(ngayNhan)) {
                JOptionPane.showMessageDialog(dialog, "Ngày trả phải sau ngày nhận!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Tính số ngày
            long diffInMillies1 = ngayTra.getTime() - ngayNhan.getTime();
            long soNgay1 = diffInMillies1 / (1000 * 60 * 60 * 24);
            
            // Tính tổng tiền phòng
            double tongTien = giaPhong * soNgay1;
    
            // Định dạng ngày để hiển thị
            java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String ngayNhanStr1 = sdf.format(ngayNhan);
            String ngayTraStr1 = sdf.format(ngayTra);
    
            // Thêm vào bảng phòng đã đặt
            phongDatTableModel.addRow(new Object[]{
                maPhong,
                loaiPhong,
                giaPhong,
                ngayNhanStr,
                ngayTraStr,
                tongTien
            });
            
            // Cập nhật tổng tiền phòng
            capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
            
            // Cập nhật tổng tiền đặt phòng
            capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
            
            // Xóa phòng đã đặt khỏi bảng phòng trống (để tránh đặt trùng)
            phongTableModel.removeRow(selectedRowPhong);
        });
    
        // Xử lý sự kiện xóa phòng đã đặt
        xoaPhongDat.addActionListener(e -> {
            int selectedRowPhongDat = phongDatTable.getSelectedRow();
            if (selectedRowPhongDat == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một phòng từ danh sách phòng đã đặt để xóa!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Kiểm tra nếu là phòng chính thì không cho xóa
            String maPhongSelected = (String) phongDatTableModel.getValueAt(selectedRowPhongDat, 0);
            if (maPhongSelected.equals(datPhongHienTai.getMaPhong())) {
                JOptionPane.showMessageDialog(dialog, "Không thể xóa phòng chính khỏi đặt phòng!\n" +
                    "Đây là phòng chính của đặt phòng này.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy thông tin phòng để thêm lại vào phòng trống
            String maPhong = (String) phongDatTableModel.getValueAt(selectedRowPhongDat, 0);
            String loaiPhong = (String) phongDatTableModel.getValueAt(selectedRowPhongDat, 1);
            double giaPhong = (double) phongDatTableModel.getValueAt(selectedRowPhongDat, 2);
            
            // Tìm thông tin phòng
            PhongBUS phongBus = new PhongBUS();
            PhongDTO phongInfo = phongBus.getById(maPhong);
            
            if (phongInfo != null) {
                // Tìm thông tin loại phòng
                LoaiPhongDTO loaiPhongInfo = phongBus.getLoaiPhongByMaPhong(maPhong);
                int soGiuong = 0;
                
                // Nếu tìm được loại phòng, lấy số giường, nếu không tìm được thì để mặc định là 1
                if (loaiPhongInfo != null) {
                    soGiuong = loaiPhongInfo.getSoGiuong();
                } else {
                    soGiuong = 1; // giá trị mặc định
                }
                
                // Thêm lại vào bảng phòng trống
                phongTableModel.addRow(new Object[]{
                    maPhong,
                    loaiPhong,
                    giaPhong,
                    soGiuong,
                    "Trống"
                });
                
                // Xóa khỏi bảng phòng đã đặt
                phongDatTableModel.removeRow(selectedRowPhongDat);
                
                // Cập nhật tổng tiền phòng
                capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
                
                // Cập nhật tổng tiền đặt phòng
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
            } else {
                // Nếu không tìm thấy thông tin phòng, chỉ xóa khỏi bảng phòng đã đặt
                JOptionPane.showMessageDialog(dialog, 
                    "Không tìm thấy thông tin phòng trong cơ sở dữ liệu, phòng sẽ bị xóa khỏi danh sách đặt.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    
                // Xóa khỏi bảng phòng đã đặt
                phongDatTableModel.removeRow(selectedRowPhongDat);
                
                // Cập nhật tổng tiền phòng
                capNhatTongTienPhong(phongDatTableModel, txtTongTienPhong);
                
                // Cập nhật tổng tiền đặt phòng
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
            }
        });
    
        // Xử lý sự kiện thêm dịch vụ
        btnThemVaoPhieu.addActionListener(e -> {
            int selectedRowDichVu = dichVuTable.getSelectedRow();
            if (selectedRowDichVu == -1) {
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
                String maDV = (String) dichVuTableModel.getValueAt(selectedRowDichVu, 0);
                String tenDV = (String) dichVuTableModel.getValueAt(selectedRowDichVu, 1);
                int giaDV = (int) dichVuTableModel.getValueAt(selectedRowDichVu, 2);
        
                // Tính thành tiền
                int thanhTien = giaDV * soLuong;
        
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
                capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
        
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        // Cập nhật xử lý cho nút xóa dịch vụ
        xoaDV.addActionListener(e -> {
            int selectedRowSuDungDV = suDungDichVuTable.getSelectedRow();
            if (selectedRowSuDungDV == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách để xóa!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Xóa dòng được chọn
            suDungDichVuTableModel.removeRow(selectedRowSuDungDV);
            
            // Cập nhật lại tổng tiền dịch vụ
            capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
            
            // Cập nhật tổng tiền đặt phòng
            capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
        });
    
        // Thêm xử lý cho nút sửa số lượng
        suaDV.addActionListener(e -> {
            int selectedRowSuaDV = suDungDichVuTable.getSelectedRow();
            if (selectedRowSuaDV == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một dịch vụ từ danh sách để sửa số lượng!", 
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy tên dịch vụ để tìm giá
            String tenDV = (String) suDungDichVuTableModel.getValueAt(selectedRowSuaDV, 1);
            
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
                    suDungDichVuTableModel.getValueAt(selectedRowSuaDV, 2));
            
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
                    suDungDichVuTableModel.setValueAt(newSoLuong, selectedRowSuaDV, 2);
                    suDungDichVuTableModel.setValueAt(thanhTienMoi, selectedRowSuaDV, 3);
                    
                    // Cập nhật lại tổng tiền dịch vụ
                    capNhatTongTienDichVu(suDungDichVuTableModel, txtTongTienDichVu);
                    
                    // Cập nhật tổng tiền đặt phòng
                    capNhatTongTienDatPhong(txtTongTienPhong, txtTongTienDichVu, txtTongTienDatPhong);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        // Xử lý nút cập nhật
        btnCapNhat.addActionListener(e -> {
            try {
                // Lấy dữ liệu từ form
                String maKH = (String) txtSDT.getClientProperty("MaKH");
                if (maKH == null || maKH.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn khách hàng hợp lệ!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (phongDatTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một phòng!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Date ngayNhanChung = new Date(((java.util.Date) spinnerNgayNhan.getValue()).getTime());
                Date ngayTraChung = new Date(((java.util.Date) spinnerNgayTra.getValue()).getTime());
                
                // Chọn phòng đầu tiên làm phòng chính cho bản ghi DatPhong
                String maPhongChinh1 = (String) phongDatTableModel.getValueAt(0, 0);
                
                // Tạo đối tượng DatPhongDTO mới
                DatPhongDTO datPhongMoi = new DatPhongDTO(maDatPhong, maPhongChinh1, maKH, ngayNhanChung, ngayTraChung);
                
                // Thực hiện cập nhật
                if (datPhongBUS.updateDatPhong(datPhongMoi)) {
                    // Nếu cập nhật đặt phòng thành công
                    
                    // 1. Trước tiên, xóa tất cả các phòng cũ từ danh sách phòng
                    boolean allRoomsDeleted = danhSachPhongBUS.xoaDanhSachPhongTheoMaDatPhong(maDatPhong);
                    if (!allRoomsDeleted) {
                        JOptionPane.showMessageDialog(dialog, 
                                "Không thể xóa hết các phòng hiện có. Quá trình cập nhật có thể không đầy đủ.",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    // 2. Sau đó, thêm tất cả các phòng từ bảng vào danh sách phòng
                    boolean allRoomsAdded = true;
                    for (int i = 0; i < phongDatTableModel.getRowCount(); i++) {
                        String maPhong = (String) phongDatTableModel.getValueAt(i, 0);
                        
                        DanhSachPhongDTO dSPhong = new DanhSachPhongDTO();
                        dSPhong.setMaDatPhong(maDatPhong);
                        dSPhong.setMaPhong(maPhong);
                        
                        if (!danhSachPhongBUS.themDanhSachPhong(dSPhong)) {
                            allRoomsAdded = false;
                        }
                        
                        // Cập nhật trạng thái phòng thành "Đã đặt"
                        PhongDTO phong = phongBUS.getById(maPhong);
                        if (phong != null) {
                            phong.setTrangThai("Đã đặt");
                            phongBUS.updatePhong(phong);
                        }
                    }
                    
                    // 3. Cập nhật dịch vụ: xóa tất cả dịch vụ cũ và thêm dịch vụ mới
                    boolean allServicesDeleted = suDungDichVuBUS.xoaSuDungDichVuTheoMaDP(maDatPhong);
                    if (!allServicesDeleted) {
                        JOptionPane.showMessageDialog(dialog, 
                                "Không thể xóa hết các dịch vụ hiện có. Quá trình cập nhật có thể không đầy đủ.",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    boolean allServicesAdded = true;
                    for (int i = 0; i < suDungDichVuTableModel.getRowCount(); i++) {
                        String tenDV = (String) suDungDichVuTableModel.getValueAt(i, 1);
                        int soLuong = (int) suDungDichVuTableModel.getValueAt(i, 2);
                        
                        // Tìm mã dịch vụ từ tên
                        String maDV = "";
                        for (DichVuDTO dv : danhSachDichVu) {
                            if (dv.getTenDichVu().equals(tenDV)) {
                                maDV = dv.getMaDichVu();
                                break;
                            }
                        }
                        
                        if (!maDV.isEmpty()) {
                            SuDungDichVuDTO sdDV = new SuDungDichVuDTO();
                            sdDV.setMaDP(maDatPhong);
                            sdDV.setMaDv(maDV);
                            sdDV.setSoLuong(soLuong);
                            
                            if (!suDungDichVuBUS.themSuDungDichVu(sdDV)) {
                                allServicesAdded = false;
                            }
                        }
                    }
                    
                    // Hiển thị thông báo kết quả
                    if (allRoomsAdded && allServicesAdded) {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật đặt phòng thành công!");
                    } else if (!allRoomsAdded) {
                        JOptionPane.showMessageDialog(dialog, 
                                "Cập nhật đặt phòng không hoàn chỉnh: Một số phòng không được thêm vào!",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                                "Cập nhật đặt phòng không hoàn chỉnh: Một số dịch vụ không được thêm vào!",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    dialog.dispose();
                    loadTableData(); // Cập nhật lại bảng đặt phòng
                    PhongGUI.refreshTable(); // Cập nhật lại bảng phòng
                    
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật đặt phòng thất bại!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật đặt phòng: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    
        // Hiển thị dialog
        dialog.setVisible(true);
    }
}