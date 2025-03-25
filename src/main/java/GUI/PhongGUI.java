package GUI;

import fillter.Button;
import fillter.Colors;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PhongGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JComboBox<String> CBFilter;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;
    
    private DefaultTableModel tableModel;

    public PhongGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        // ======= Header Panel ======= //
        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Sử dụng FlowLayout để dễ căn chỉnh
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        CBFilter = new JComboBox<>(new String[]{"Tất cả", "Phòng trống", "Phòng đã đặt"});
        CBFilter.setPreferredSize(new Dimension(120, 35));

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        // Thêm các thành phần vào PanelHeader
        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(CBFilter);
        PanelHeader.add(txtSearch);
        
        // ======= Content Panel ======= //
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        // Tạo bảng dữ liệu
        String[] columnNames = {"Mã Phòng", "Mã Loại Phòng", "Trạng Thái"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setDefaultEditor(Object.class, null);
        ContentTable.setRowHeight(30);

        // Tùy chỉnh tiêu đề bảng
        ContentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        ContentTable.getTableHeader().setBackground(Colors.MAIN_BUTTON);
        ContentTable.getTableHeader().setForeground(Color.WHITE);

        // Đặt bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        // Thêm vào giao diện chính
        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);
    }
}
