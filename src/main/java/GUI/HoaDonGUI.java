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
import javax.swing.JButton;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import BUS.DanhSachPhongBUS;
import BUS.DatPhongBUS;
import BUS.DichVuBUS;
import BUS.HoaDonBUS;
import BUS.KhachHangBUS;
import BUS.LoaiPhongBUS;
import BUS.NhanVienBUS;
import BUS.PhongBUS;
import BUS.SuDungDichVuBUS;
import DTO.DanhSachPhongDTO;
import DTO.DatPhongDTO;
import DTO.DichVuDTO;
import DTO.HoaDonDTO;
import DTO.LoaiPhongDTO;
import DTO.PhongDTO;
import DTO.SuDungDichVuDTO;
import fillter.Button;
import fillter.Colors;

public class HoaDonGUI extends JPanel {
    private Button DetailBtn, PaymentBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch;
    private DefaultTableModel tableModel;
    private HoaDonBUS hoaDonBUS;
    private JComboBox<String> cbxTrangThaiFilter;
    private JDateChooser dateFrom, dateTo;

    public HoaDonGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        hoaDonBUS = new HoaDonBUS();

        PanelHeader = new JPanel();
        PanelHeader.setLayout(new BorderLayout());
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 120));

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelButtons.setBackground(Colors.MAIN_BACKGROUND);

        DetailBtn = new Button("menuButton", "Xem chi tiết", 240, 30, "/Icon/detail-icon.png");
        DetailBtn.addActionListener(e -> xemChiTiet());

        PaymentBtn = new Button("menuButton", "Thanh toán", 240, 30, "/Icon/payment-icon.png");
        PaymentBtn.addActionListener(e -> thanhToan());

        panelButtons.add(DetailBtn);
        panelButtons.add(PaymentBtn);

        pSearch = new JPanel();
        pSearch.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pSearch.setBackground(Colors.MAIN_BACKGROUND);

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

        cbSearch = new JComboBox<>(new String[]{"Mã Hóa Đơn", "Mã Nhân Viên"});
        cbSearch.setPreferredSize(new Dimension(150, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        JPanel statusFillterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        statusFillterPanel.setBackground(Colors.MAIN_BACKGROUND);
        statusFillterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Trạng Thái",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        cbxTrangThaiFilter = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});
        statusFillterPanel.add(cbxTrangThaiFilter);

        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        dateFilterPanel.setBackground(Colors.MAIN_BACKGROUND);
        dateFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Ngày Tạo",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(110, 30));
        dateTo.setPreferredSize(new Dimension(110, 30));
        dateFrom.setDateFormatString("yyyy-MM-dd");
        dateTo.setDateFormatString("yyyy-MM-dd");
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setEditable(false);

        dateFilterPanel.add(new JLabel("Từ:"));
        dateFilterPanel.add(dateFrom);
        dateFilterPanel.add(new JLabel("Đến:"));
        dateFilterPanel.add(dateTo);

        pSearch.add(panelSearchFields);
        pSearch.add(statusFillterPanel);
        pSearch.add(dateFilterPanel);

        PanelHeader.add(panelButtons, BorderLayout.NORTH);
        PanelHeader.add(pSearch, BorderLayout.SOUTH);

        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columNames = {"Mã Hóa Đơn","Tên Nhân Viên","Ngày Tạo", "Tổng Tiền", "Tiền Trả", "Tiền Thừa", "Trạng Thái"};
        tableModel = new DefaultTableModel(columNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData();

        cbSearch.addActionListener(e -> timKiemHoaDon());

        cbxTrangThaiFilter.addActionListener(e -> timKiemHoaDon());

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiemHoaDon();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiemHoaDon();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiemHoaDon();
            }
        });

        dateFrom.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiemHoaDon();
            }
        });

        dateTo.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                timKiemHoaDon();
            }
        });
    }

    public void loadTableData() {
        tableModel.setRowCount(0);

        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#,###");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");

        ArrayList<HoaDonDTO> danhSachHoaDon = hoaDonBUS.layDanhSachHoaDon();
        KhachHangBUS khachHangBUS = new KhachHangBUS();
        NhanVienBUS nhanVienBUS = new NhanVienBUS();

        for (HoaDonDTO hoaDon : danhSachHoaDon) {
            String tongTien = decimalFormat.format(hoaDon.getTongTien()) + " VNĐ";
            String tienTra = decimalFormat.format(hoaDon.getTienTra()) + " VNĐ";
            String tienThua = decimalFormat.format(hoaDon.getTienThua()) + " VNĐ";
            String ngayTao = hoaDon.getNgayTao() != null ? dateFormat.format(hoaDon.getNgayTao()) : "";


            // Lấy tên nhân viên
            String tenNhanVien = hoaDon.getMaNhanVien();
            if (hoaDon.getMaNhanVien() != null) {
                var nv = nhanVienBUS.getNhanVienByMa(hoaDon.getMaNhanVien());
                if (nv != null) tenNhanVien = nv.getHoTen();
            }

            tableModel.addRow(new Object[]{
                hoaDon.getMaHoaDon(),
                tenNhanVien,
                ngayTao,
                tongTien,
                tienTra,
                tienThua,
                hoaDon.getTrangThai()
            });
        }
    }

    private void capNhatTienThua(JTextField txtTongTien, JTextField txtTienTra, JTextField txtTienThua) {
        try {
            int tongTien = Integer.parseInt(txtTongTien.getText().trim());
            int tienTra = Integer.parseInt(txtTienTra.getText().trim());
            int tienThua = tienTra - tongTien;
            txtTienThua.setText(String.valueOf(tienThua));
        } catch (NumberFormatException e) {
            txtTienThua.setText("");
        }
    }
    

    private void xemChiTiet() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để xem chi tiết.");
            return;
        }

        String maHD = tableModel.getValueAt(selectedRow, 0).toString();

        // Lấy dữ liệu
        DanhSachPhongBUS dspBUS = new DanhSachPhongBUS();
        SuDungDichVuBUS sddvBUS = new SuDungDichVuBUS();
        ArrayList<DanhSachPhongDTO> danhSachPhong = dspBUS.getDanhSachPhongTheoMaHoaDon(maHD);
        ArrayList<SuDungDichVuDTO> danhSachDichVu = sddvBUS.getDanhSachDichVuTheoMaHoaDon(maHD);

        // Tạo dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Chi tiết hóa đơn: " + maHD);
        dialog.setModal(true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(2, 1, 10, 10));

        // Bảng phòng
        String[] colPhong = {"Mã Phòng", "Loại Phòng", "Số giường", "Giá"};
        DefaultTableModel phongModel = new DefaultTableModel(colPhong, 0);
        for (DanhSachPhongDTO p : danhSachPhong) {
            PhongBUS phongBUS = new PhongBUS();
            LoaiPhongBUS loaiPhongBUS = new LoaiPhongBUS();
            PhongDTO phongDTO = phongBUS.getById(p.getMaPhong());
            LoaiPhongDTO loaiPhongDTO = loaiPhongBUS.getById(phongDTO.getMaLoaiPhong());
            phongModel.addRow(new Object[]{p.getMaPhong(), loaiPhongDTO.getTenLoaiPhong(), loaiPhongDTO.getSoGiuong(), loaiPhongDTO.getGiaPhong()});
        }
        JTable tblPhong = new JTable(phongModel);
        JScrollPane scrollPhong = new JScrollPane(tblPhong);
        scrollPhong.setBorder(BorderFactory.createTitledBorder("Danh sách phòng"));

        // Bảng dịch vụ
        String[] colDV = {"Mã DV", "Tên DV", "Đơn giá", "Số lượng", "Thành tiền"};
        DefaultTableModel dvModel = new DefaultTableModel(colDV, 0);
        for (SuDungDichVuDTO dv : danhSachDichVu) {
            DichVuBUS dvBUS = new DichVuBUS();
            DichVuDTO dvDTO = dvBUS.getById(dv.getMaDv());
            double thanhTien = dv.getSoLuong() * dvDTO.getGiaDichVu();
            dvModel.addRow(new Object[]{dv.getMaDv(), dvDTO.getTenDichVu(), dvDTO.getGiaDichVu(), dv.getSoLuong(), thanhTien});
        }
        JTable tblDV = new JTable(dvModel);
        JScrollPane scrollDV = new JScrollPane(tblDV);
        scrollDV.setBorder(BorderFactory.createTitledBorder("Danh sách dịch vụ"));

        dialog.add(scrollPhong);
        dialog.add(scrollDV);

        dialog.setVisible(true);
    }  
    

    // ...existing code...
    private void thanhToan() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để thanh toán.");
            return;
        }

        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        HoaDonDTO hoaDon = HoaDonBUS.getHoaDonByMa(maHD);
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin hóa đơn.");
            return;
        }

        JTextField txtMaHD = new JTextField(hoaDon.getMaHoaDon());
        txtMaHD.setEditable(false);

        JTextField txtTongTien = new JTextField(String.valueOf((int) hoaDon.getTongTien()));
        txtTongTien.setEditable(false);

        JTextField txtTienTra = new JTextField();
        JTextField txtTienThua = new JTextField();
        txtTienThua.setEditable(false);

        DocumentListener tinhTienThuaListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capNhatTienThua(txtTongTien, txtTienTra, txtTienThua); }
            public void removeUpdate(DocumentEvent e) { capNhatTienThua(txtTongTien, txtTienTra, txtTienThua); }
            public void changedUpdate(DocumentEvent e) { capNhatTienThua(txtTongTien, txtTienTra, txtTienThua); }
        };

        txtTienTra.getDocument().addDocumentListener(tinhTienThuaListener);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        BiConsumer<String, JComponent> addRow = (label, field) -> {
            inputPanel.add(new JLabel(label));
            inputPanel.add(field);
        };

        addRow.accept("Mã HĐ:", txtMaHD);
        addRow.accept("Tổng tiền phải trả:", txtTongTien);
        addRow.accept("Tiền khách trả:", txtTienTra);
        addRow.accept("Tiền thối:", txtTienThua);

        Button btnOk = new Button("confirm", "Xác nhận thanh toán", 160, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        JDialog dialog = new JDialog();
        dialog.setTitle("Thanh Toán Hóa Đơn");
        dialog.setModal(true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());

        btnOk.addActionListener(e -> {
            try {
                double tongTien = Double.parseDouble(txtTongTien.getText());
                double tienTra = Double.parseDouble(txtTienTra.getText());

                if (tienTra < tongTien) {
                    JOptionPane.showMessageDialog(null, "Tiền trả không được nhỏ hơn tổng tiền!");
                    return;
                }

                String trangThaiCu = hoaDon.getTrangThai(); // Lưu trạng thái cũ
                hoaDon.setTienTra(tienTra);
                hoaDon.setTienThua(tienTra - tongTien);
                hoaDon.setTrangThai("Đã thanh toán");
                String trangThaiMoi = hoaDon.getTrangThai(); // Trạng thái mới

                if (hoaDonBUS.capnhatHoaDon(hoaDon)) {
                    // Nếu trạng thái đổi từ "Chưa thanh toán" sang "Đã thanh toán"
                    if (!"Đã thanh toán".equals(trangThaiCu) && "Đã thanh toán".equals(trangThaiMoi)) {
                        String maDatPhong = hoaDon.getMaDatPhong();
                        if (maDatPhong != null && !maDatPhong.isEmpty()) {
                            // Lấy thông tin đặt phòng
                            DatPhongBUS datPhongBUS = new DatPhongBUS();
                            DatPhongDTO datPhong = datPhongBUS.getById(maDatPhong);

                            if (datPhong != null) {
                                // Lấy danh sách phòng đã đặt
                                DanhSachPhongBUS danhSachPhongBUS = new DanhSachPhongBUS();
                                ArrayList<String> danhSachMaPhong = danhSachPhongBUS.layDanhSachMaPhongTheoMaDatPhong(maDatPhong);

                                // Cập nhật trạng thái của phòng về "Trống"
                                PhongBUS phongBUS = new PhongBUS();
                                for (String maPhong : danhSachMaPhong) {
                                    PhongDTO phong = phongBUS.getById(maPhong);
                                    if (phong != null) {
                                        phong.setTrangThai("Trống");
                                        phongBUS.updatePhong(phong);
                                    }
                                }

                                // Xóa các bản ghi trong bảng sử dụng dịch vụ
                                SuDungDichVuBUS suDungDichVuBUS = new SuDungDichVuBUS();
                                suDungDichVuBUS.xoaSuDungDichVuTheoMaDP(maDatPhong);

                                // Xóa các bản ghi trong bảng danh sách phòng
                                danhSachPhongBUS.xoaDanhSachPhongTheoMaDatPhong(maDatPhong);

                                // Xóa bản ghi trong bảng đặt phòng
                                datPhongBUS.deleteDatPhong(maDatPhong);

                                JOptionPane.showMessageDialog(null, "Cập nhật thành công! Đã xóa đặt phòng và đặt trạng thái phòng về 'Trống'.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Cập nhật thành công! Không tìm thấy thông tin đặt phòng để xóa.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Cập nhật thành công! Không có mã đặt phòng để xóa.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                    }

                    dialog.dispose();
                    loadTableData();
                    PhongGUI.refreshTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng số!");
            }
        });
        dialog.setVisible(true);
    }
    // ...existing code...
    

    private void timKiemHoaDon() {
        try {
            String kieuTim = cbSearch.getSelectedItem().toString();
            String tuKhoa = txtSearch.getText().trim();
            String trangThai = cbxTrangThaiFilter.getSelectedItem().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayTu = "", ngayDen = "";
    
            if (dateFrom.getDate() != null) {
                ngayTu = sdf.format(dateFrom.getDate());
            }
            if (dateTo.getDate() != null) {
                ngayDen = sdf.format(dateTo.getDate());
            }
    
            ArrayList<HoaDonDTO> dsKetQua = hoaDonBUS.search(
                kieuTim,
                tuKhoa,
                trangThai.equals("Tất cả") ? "" : trangThai,
                ngayTu,
                ngayDen
            );
    
            tableModel.setRowCount(0);
            loadTableData();
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
}