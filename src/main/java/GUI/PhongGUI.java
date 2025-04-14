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
import BUS.LoaiPhongBUS;
import DTO.DatPhongDTO;
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
        txtSearch.setPreferredSize(new Dimension(150, 35));

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
        new AddRoomGUI(parentFrame, phongBUS, "Chỉnh sửa phòng", "save", phong);
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

        StringBuilder info = new StringBuilder();
        for (DatPhongDTO dp : danhSach) {
            info.append("Mã Đặt Phòng: ").append(dp.getMaDatPhong()).append("\n");
            info.append("Mã Phòng: ").append(dp.getMaPhong()).append("\n");
            info.append("Mã Khách Hàng: ").append(dp.getMaKH()).append("\n");
            info.append("Ngày Nhận: ").append(dp.getNgayNhanPhong()).append("\n");
            info.append("Ngày Trả: ").append(dp.getNgayTraPhong()).append("\n");
            info.append("------------------------------------\n");
        }

        JOptionPane.showMessageDialog(null, info.toString(), "Thông Tin Đặt Phòng", JOptionPane.INFORMATION_MESSAGE);
    }
}