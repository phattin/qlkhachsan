package GUI;

import BUS.DichVuBUS;
import DTO.DichVuDTO;
import GUI.Dialog.AddServiceGUI; // Import dialog mới
import fillter.Button;
import fillter.Colors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DichVuGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JComboBox<String> cbSearchOption; // Thêm ComboBox cho tùy chọn tìm kiếm
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent;

    private DefaultTableModel tableModel;
    private DichVuBUS dichVuBUS;

    public DichVuGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        dichVuBUS = new DichVuBUS();

        PanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 60));

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");

        AddBtn.addActionListener(e -> addNewService());
        EditBtn.addActionListener(e -> editService());
        DeleteBtn.addActionListener(e -> deleteService());

        String[] searchOptions = {"Mã DV", "Tên Dịch Vụ", "Giá Dịch Vụ"};
        cbSearchOption = new JComboBox<>(searchOptions);
        cbSearchOption.setPreferredSize(new Dimension(120, 35));

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        txtSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                filterTableData(txtSearch.getText(), (String) cbSearchOption.getSelectedItem());
            }
        });

        PanelHeader.add(AddBtn);
        PanelHeader.add(DeleteBtn);
        PanelHeader.add(EditBtn);
        PanelHeader.add(cbSearchOption); // Thêm ComboBox vào header
        PanelHeader.add(txtSearch);

        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã DV", "Tên Dịch Vụ", "Giá Dịch Vụ"};
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
        ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.getAllDichVu();

        for (DichVuDTO dichVu : danhSachDichVu) {
            tableModel.addRow(new Object[]{
                    dichVu.getMaDV(),
                    dichVu.getTenDV(),
                    dichVu.getGiaDV()
            });
        }
    }

    private void filterTableData(String searchText, String searchOption) {
        tableModel.setRowCount(0);
        ArrayList<DichVuDTO> danhSachDichVu = dichVuBUS.timKiemDichVu(searchText, searchOption);

        for (DichVuDTO dichVu : danhSachDichVu) {
            tableModel.addRow(new Object[]{
                    dichVu.getMaDV(),
                    dichVu.getTenDV(),
                    dichVu.getGiaDV()
            });
        }
    }

    private void addNewService() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddServiceGUI addServiceDialog = new AddServiceGUI(parentFrame, dichVuBUS, "Thêm dịch vụ", "add");
        // Sau khi dialog đóng, load lại dữ liệu
        addServiceDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loadTableData();
            }
        });
    }

    private void editService() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDV = tableModel.getValueAt(selectedRow, 0).toString();
        DichVuDTO dichVu = dichVuBUS.getDichVuByMa(maDV);

        if (dichVu != null) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            AddServiceGUI editServiceDialog = new AddServiceGUI(parentFrame, dichVuBUS, "Chỉnh sửa dịch vụ", "save", dichVu);
            // Sau khi dialog đóng, load lại dữ liệu
            editServiceDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    loadTableData();
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ để chỉnh sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteService() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDV = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa dịch vụ [" + maDV + "] không?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dichVuBUS.xoaDichVu(maDV)) {
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
