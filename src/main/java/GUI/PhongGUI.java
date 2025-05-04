package GUI;

import fillter.Button;
import fillter.Colors;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import BUS.PhongBUS;
import DTO.PhongDTO;
import GUI.Dialog.AddCustomerGUI;
import GUI.Dialog.AddRoomGUI;
import BUS.DatPhongBUS;
import BUS.KhachHangBUS;
import BUS.LoaiPhongBUS;
import DTO.DatPhongDTO;
import DTO.KhachHangDTO;
import DTO.LoaiPhongDTO;

import java.util.ArrayList;
import java.util.List;


public class PhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JComboBox<String> CBFilter;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    
    private DefaultTableModel tableModel;
    private PhongBUS phongBUS;
    private DatPhongBUS datPhongBUS;
    private LoaiPhongBUS loaiPhongBus;

    public PhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        phongBUS = new PhongBUS();
        datPhongBUS = new DatPhongBUS();

        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        AddBtn.addActionListener(e -> addNewRoom());
        EditBtn.addActionListener(e -> editRoom());
        DeleteBtn.addActionListener(e -> deleteRoom());

        CBFilter = new JComboBox<>(new String[]{"Tất cả", "Phòng trống", "Phòng đã đặt"});
        CBFilter.setPreferredSize(new Dimension(120, 35));

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        // Thêm chức năng tìm kiếm
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }
        });

        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(CBFilter);
        PanelHeader.add(txtSearch);
        
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã Phòng", "Mã Loại Phòng", "Loại Phòng", "Số Giường", "Giá Tiền", "Trạng Thái", "Xem Thêm"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        ContentTable.getColumn("Xem Thêm").setCellRenderer(new ButtonRenderer());
        ContentTable.getColumn("Xem Thêm").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData();

        CBFilter.addActionListener(e -> locDuLieu());
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();
    
        for (PhongDTO phong : danhSachPhong) {
            LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
            tableModel.addRow(new Object[]{
                phong.getMaPhong(),
                phong.getMaLoaiPhong(),
                loaiPhong.getTenLoaiPhong(),
                loaiPhong.getSoGiuong(),
                loaiPhong.getGiaPhong(),
                phong.getTrangThai(),
                "Xem Thêm"
            });
        }
    }
    

    
    private void addNewRoom() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddRoomGUI(parentFrame, phongBUS, "Thêm phòng", "add");
        loadTableData();
    }

    private void editRoom() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String maPhong = tableModel.getValueAt(selectedRow, 0).toString();
        String maLP = tableModel.getValueAt(selectedRow, 1).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 5).toString();
    
        PhongDTO phong = new PhongDTO(maPhong, maLP, trangThai);
    
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        AddRoomGUI addRoomGUI = new AddRoomGUI(parentFrame, phongBUS, "Chỉnh sửa phòng", "save", phong);
    
        JComboBox<String> cbMaLP = addRoomGUI.getLoaiPhongComboBox();
        cbMaLP.setSelectedItem(maLP);
    
        loadTableData();
    }
    

    private void deleteRoom() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String maPhong = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phòng [" + maPhong + "] không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            if (phongBUS.deletePhong(maPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa phòng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void timKiem() {
        String tuKhoa = txtSearch.getText().toLowerCase().trim();
        locVaTimKiem(tuKhoa, CBFilter.getSelectedItem().toString());
    }

    private void locDuLieu() {
        String tuKhoa = txtSearch.getText().toLowerCase().trim();
        locVaTimKiem(tuKhoa, CBFilter.getSelectedItem().toString());
    }

    private void locVaTimKiem(String tuKhoa, String tuyChinh) {
        tableModel.setRowCount(0);
        ArrayList<PhongDTO> danhSachPhong = phongBUS.getAllPhong();
        
        for (PhongDTO phong : danhSachPhong) {
            LoaiPhongDTO loaiPhong = phongBUS.getLoaiPhongByMaPhong(phong.getMaPhong());
            
            // Áp dụng bộ lọc
            if (!tuyChinh.equals("Tất cả")) {
                if (tuyChinh.equals("Phòng trống") && !phong.getTrangThai().equals("Trống")) {
                    continue;
                } else if (tuyChinh.equals("Phòng đã đặt") && !phong.getTrangThai().equals("Đã đặt")) {
                    continue;
                }
            }
            
            // Bỏ qua nếu không khớp với từ khóa tìm kiếm (tìm kiếm trên tất cả các cột)
            if (!tuKhoa.isEmpty()) {
                boolean timThay = false;
                
                // Kiểm tra tất cả các cột
                if (phong.getMaPhong().toLowerCase().contains(tuKhoa) ||
                    phong.getMaLoaiPhong().toLowerCase().contains(tuKhoa) ||
                    loaiPhong.getTenLoaiPhong().toLowerCase().contains(tuKhoa) ||
                    String.valueOf(loaiPhong.getSoGiuong()).toLowerCase().contains(tuKhoa) ||
                    String.valueOf(loaiPhong.getGiaPhong()).toLowerCase().contains(tuKhoa) ||
                    phong.getTrangThai().toLowerCase().contains(tuKhoa)) {
                    timThay = true;
                }
                
                if (!timThay) {
                    continue;
                }
            }
            
            // Thêm dòng nếu đạt cả hai điều kiện lọc và tìm kiếm
            tableModel.addRow(new Object[]{
                phong.getMaPhong(),
                phong.getMaLoaiPhong(),
                loaiPhong.getTenLoaiPhong(),
                loaiPhong.getSoGiuong(),
                loaiPhong.getGiaPhong(),
                phong.getTrangThai(),
                "Xem Thêm"
            });
        }
    }
    
    
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setText("Xem Thêm");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String maPhong;
    private DatPhongBUS datPhongBUS;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Xem Thêm");
        datPhongBUS = new DatPhongBUS();

        button.addActionListener(e -> {
            if (maPhong != null) {
                showBookingDetails(maPhong);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        maPhong = table.getValueAt(row, 0).toString(); // Lấy mã phòng từ dòng hiện tại
        return button;
    }

    private void showBookingDetails(String maPhong) {
        List<DatPhongDTO> danhSach = datPhongBUS.layDanhSachDatPhong(maPhong);
        if (danhSach.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Phòng này chưa có thông tin đặt phòng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        JDialog detailsDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(button), "Chi tiết đặt phòng", true);
        detailsDialog.setSize(500, 300);
        detailsDialog.setLocationRelativeTo(null);
    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Colors.WHITE_FONT);
    
        KhachHangBUS khachHangBUS = new KhachHangBUS();
    
        for (DatPhongDTO dp : danhSach) {
            KhachHangDTO khachHang = khachHangBUS.getById(dp.getMaKH());
            String tenKhachHang = (khachHang != null) ? khachHang.getHoTen() : "Không tìm thấy";
    
            JPanel bookingPanel = new JPanel();
            bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
            bookingPanel.setBackground(Colors.WHITE_FONT);
            bookingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    
            bookingPanel.add(createInfoRow("Mã Phòng:", dp.getMaPhong()));
            bookingPanel.add(createInfoRow("Khách Hàng:", tenKhachHang));
            bookingPanel.add(createInfoRow("Ngày Nhận:", dp.getNgayNhanPhong().toString()));
            bookingPanel.add(createInfoRow("Ngày Trả:", dp.getNgayTraPhong().toString()));
    
            mainPanel.add(bookingPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
        detailsDialog.add(scrollPane, BorderLayout.CENTER);
        detailsDialog.setVisible(true);
    }
    
    // Tạo dòng thông tin có nhãn và giá trị thẳng hàng
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Colors.WHITE_FONT);
    
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(100, 20)); // căn lề nhãn đều nhau
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
    
        JLabel val = new JLabel(value);
    
        row.add(lbl);
        row.add(val);
        return row;
    }
    
    
}