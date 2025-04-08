package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import BUS.DatPhongBUS;
import BUS.PhongBUS;
import DTO.DatPhongDTO;
import DTO.PhongDTO;
import fillter.Button;
import fillter.Colors;


public class PhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JComboBox<String> CBFilter;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    
    private DefaultTableModel tableModel;
    private PhongBUS phongBUS;
    private DatPhongBUS datPhongBUS;

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
        AddBtn.addActionListener(e -> addNewRoom());
        
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        CBFilter = new JComboBox<>(new String[]{"Tất cả", "Phòng trống", "Phòng đã đặt", "Phòng VIP", "Phòng Thường"});
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
            tableModel.addRow(new Object[]{
                phong.getMaPhong(),
                phong.getMaLoaiPhong(),
                phong.getTenLoaiPhong(),
                phong.getSoGiuong(),
                phong.getGiaTien(),
                phong.getTrangThai(),
                "Xem Thêm"
            });
        }
    }
    

    
    private void addNewRoom() {
        JTextField txtMaPhong = new JTextField();
        JTextField txtMaLoaiPhong = new JTextField();
        JTextField txtTrangThai = new JTextField();
    
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Mã Phòng:"));
        panel.add(txtMaPhong);
        panel.add(new JLabel("Mã Loại Phòng:"));
        panel.add(txtMaLoaiPhong);
        panel.add(new JLabel("Trạng Thái:"));
        panel.add(txtTrangThai);
    
        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm Phòng", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String maPhong = txtMaPhong.getText();
            String maLoaiPhong = txtMaLoaiPhong.getText();
            String trangThai = txtTrangThai.getText();
    
            if (maPhong.isEmpty() || maLoaiPhong.isEmpty() || trangThai.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
   
            PhongDTO phong = new PhongDTO(maPhong, maLoaiPhong, "", 0, 0, trangThai);
            if (phongBUS.addPhong(phong)) {
                JOptionPane.showMessageDialog(null, "Thêm phòng thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm phòng thất bại! Kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
