package GUI;

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
    

}