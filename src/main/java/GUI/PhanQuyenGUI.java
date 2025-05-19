package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
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
import javax.swing.table.DefaultTableModel;

import BUS.ChucNangBUS;
import BUS.ChucVuBUS;
import BUS.PhanQuyenBUS;
import DTO.ChucNangDTO;
import DTO.ChucVuDTO;
import DTO.PhanQuyenDTO;
import fillter.Button;
import fillter.Colors;

public class PhanQuyenGUI extends JPanel {
    private Button AddBtn, DeleteBtn, EditBtn;
    private JTextField txtSearch;
    private JTable ContentTable;
    private JPanel PanelHeader, PanelContent, pSearch;
    private JComboBox cbSearch;
    private DefaultTableModel tableModel;
    private PhanQuyenBUS pqBUS;
    private ChucVuBUS cvBUS;
    private ChucNangBUS cnBUS;

    public PhanQuyenGUI() {
        this.setBackground(Colors.MAIN_BACKGROUND);
        this.setPreferredSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.setLayout(new BorderLayout(5, 5));

        pqBUS = new PhanQuyenBUS();
        cvBUS = new ChucVuBUS();
        cnBUS = new ChucNangBUS();

        PanelHeader = new JPanel();
        PanelHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // căn trái, khoảng cách giữa các phần tử
        PanelHeader.setBackground(Colors.MAIN_BACKGROUND);
        PanelHeader.setPreferredSize(new Dimension(this.getWidth(), 75));

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelButtons.setBackground(Colors.MAIN_BACKGROUND);

        AddBtn = new Button("menuButton", "Thêm", 120, 30, "/Icon/them_icon.png");
        AddBtn.addActionListener(e -> themPhanQuyen());

        DeleteBtn = new Button("menuButton", "Xóa", 120, 30, "/Icon/xoa_icon.png");
        DeleteBtn.addActionListener(e -> xoaPhanQuyen());

        EditBtn = new Button("menuButton", "Sửa", 120, 30, "/Icon/sua_icon.png");
        EditBtn.addActionListener(e -> suaPhanQuyen());

        panelButtons.add(AddBtn);
        panelButtons.add(DeleteBtn);
        panelButtons.add(EditBtn);

        // Tạo panel con để bao phủ ô tìm kiếm và comboBox
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

        cbSearch = new JComboBox<>(new String[]{"Mã chức vụ", "Tên chức vụ"});
        cbSearch.setPreferredSize(new Dimension(120, 30));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));

        // Thêm vào panel con
        panelSearchFields.add(cbSearch);
        panelSearchFields.add(txtSearch);

        // Thêm cả 2 hàng vào PanelHeader
        PanelHeader.add(panelButtons, BorderLayout.NORTH);
        PanelHeader.add(panelSearchFields, BorderLayout.SOUTH);
        
        PanelContent = new JPanel(new BorderLayout());
        PanelContent.setBackground(Colors.MAIN_BACKGROUND);

        String[] columnNames = {"Mã chức vụ", "Tên chức vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ContentTable = new JTable(tableModel);
        ContentTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ContentTable);
        scrollPane.getViewport().setBackground(Colors.MAIN_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.MAIN_BUTTON, 2));

        PanelContent.add(scrollPane, BorderLayout.CENTER);

        this.add(PanelHeader, BorderLayout.NORTH);
        this.add(PanelContent, BorderLayout.CENTER);

        loadTableData(ChucVuBUS.getAllChucVu());

        // Khi thay đổi cbSearch
        cbSearch.addActionListener(e -> timKiemPhanQuyen());
        // Khi người dùng nhập liệu vào txtSearch
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiemPhanQuyen(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiemPhanQuyen(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiemPhanQuyen(); }
        });
    }

    private void loadTableData(ArrayList<ChucVuDTO> danhSachChucVu) {
        tableModel.setRowCount(0);
    
        for (ChucVuDTO pq : danhSachChucVu) {
            tableModel.addRow(new Object[]{
                pq.getMaChucVu(),
                pq.getTenChucVu()
            });
        }
    }
    
    private void themPhanQuyen() {
        // Field cơ bản
        JTextField txtMaCV = new JTextField(cvBUS.increaseMaCV());
        txtMaCV.setEditable(false);
        JTextField txtTenCV = new JTextField();

        // Lấy danh sách chức năng
        ArrayList<ChucNangDTO> dsChucNang = ChucNangBUS.getAllChucNang();

        // Tạo checkbox tương ứng với chức năng
        JPanel chucNangPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 checkbox mỗi hàng
        ArrayList<JCheckBox> listCheckBox = new ArrayList<>();

        for (ChucNangDTO cn : dsChucNang) {
            JCheckBox checkBox = new JCheckBox(cn.getTenChucNang());
            listCheckBox.add(checkBox);
            chucNangPanel.add(checkBox);
        }

        // Tạo panel chứa input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Tiện để thêm label và field
        BiConsumer<String, JComponent> addRow = (labelText, inputField) -> {
            inputPanel.add(new JLabel(labelText));
            inputField.setPreferredSize(new Dimension(250, 25));
            inputPanel.add(inputField);
        };

        // Thêm vào panel
        addRow.accept("Mã chức vụ:", txtMaCV);
        addRow.accept("Tên chức vụ:", txtTenCV);

        // Tạo scroll cho checkbox (tránh quá dài)
        JScrollPane scrollPane = new JScrollPane(chucNangPanel);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chức năng"));

        // Nút xác nhận - hủy
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        // Dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm chức vụ");
        dialog.setModal(true);
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        dialog.add(inputPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện
        btnCancel.addActionListener(e -> dialog.dispose());

        btnOk.addActionListener(e -> {
            String maCV = txtMaCV.getText().trim();
            String tenCV = txtTenCV.getText().trim();

            // Lấy các checkbox đã chọn
            ArrayList<String> dsChucNangDuocChon = new ArrayList<>();
            for (JCheckBox cb : listCheckBox) {
                if (cb.isSelected()) {
                    dsChucNangDuocChon.add(cb.getText());
                }
            }

            if (tenCV.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dsChucNangDuocChon.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một chức năng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!cvBUS.checkTextAdd(tenCV))
                return;
            // Thêm chức vụ và phân quyền
            ChucVuDTO cvDTO = new ChucVuDTO(maCV, tenCV, "Hiện");
            if(!cvBUS.add(cvDTO)){
                JOptionPane.showMessageDialog(dialog, "Thêm chức vụ thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (String tenCNDuocChon: dsChucNangDuocChon){
                PhanQuyenDTO pqDTO = new PhanQuyenDTO(maCV, cnBUS.getMaByTenChucNang(tenCNDuocChon));
                if(!pqBUS.add(pqDTO)){
                    JOptionPane.showMessageDialog(dialog, "Thêm phân quyền thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
                }
            }

            JOptionPane.showMessageDialog(dialog, "Thêm chức vụ thành công!");
            dialog.dispose();
            loadTableData(ChucVuBUS.getAllChucVu());
        });

        dialog.setVisible(true);
    }

    private void suaPhanQuyen() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Lấy dữ liệu đang chọn
        String maCV = ContentTable.getValueAt(selectedRow, 0).toString();
        String tenCVHienTai = ContentTable.getValueAt(selectedRow, 1).toString();
    
        // Field cơ bản
        JTextField txtMaCV = new JTextField(maCV);
        txtMaCV.setEditable(false);
        JTextField txtTenCV = new JTextField(tenCVHienTai);
    
        // Lấy danh sách chức năng
        ArrayList<ChucNangDTO> dsChucNang = ChucNangBUS.getAllChucNang();
        ArrayList<ChucNangDTO> dsChucNangDaCo = cnBUS.getChucNangByMaChucVu(maCV);
        ArrayList<String> dsTenChucNangDaCo = new ArrayList<>();
        for (ChucNangDTO cn : dsChucNangDaCo) {
            dsTenChucNangDaCo.add(cn.getTenChucNang());
        }
    
        // Tạo checkbox chức năng
        JPanel chucNangPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        ArrayList<JCheckBox> listCheckBox = new ArrayList<>();
    
        for (ChucNangDTO cn : dsChucNang) {
            JCheckBox checkBox = new JCheckBox(cn.getTenChucNang());
            if (dsTenChucNangDaCo.contains(cn.getTenChucNang()))
                checkBox.setSelected(true); // nếu đã có thì setSelected
            listCheckBox.add(checkBox);
            chucNangPanel.add(checkBox);
        }
    
        // Tạo panel chứa input
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    
        BiConsumer<String, JComponent> addRow = (labelText, inputField) -> {
            inputPanel.add(new JLabel(labelText));
            inputField.setPreferredSize(new Dimension(250, 25));
            inputPanel.add(inputField);
        };
    
        addRow.accept("Mã chức vụ:", txtMaCV);
        addRow.accept("Tên chức vụ:", txtTenCV);
    
        JScrollPane scrollPane = new JScrollPane(chucNangPanel);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chức năng"));
    
        Button btnOk = new Button("confirm", "Xác nhận", 120, 35, null);
        Button btnCancel = new Button("cancel", "Hủy", 120, 35, null);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
    
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa chức vụ");
        dialog.setModal(true);
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
    
        dialog.add(inputPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        btnCancel.addActionListener(e -> dialog.dispose());
    
        btnOk.addActionListener(e -> {
            String tenMoi = txtTenCV.getText().trim();
    
            ArrayList<String> dsChucNangDuocChon = new ArrayList<>();
            for (JCheckBox cb : listCheckBox) {
                if (cb.isSelected()) {
                    dsChucNangDuocChon.add(cb.getText());
                }
            }
    
            if (tenMoi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (dsChucNangDuocChon.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một chức năng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!cvBUS.checkTextUpdate(maCV, tenMoi))
                return;
    
            // Update chức vụ
            ChucVuDTO cvDTO = new ChucVuDTO(maCV, tenMoi, "Hiện");
            if (!cvBUS.update(cvDTO)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật chức vụ thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Xóa hết quyền cũ và thêm quyền mới
            if (!pqBUS.deleteByMaChucVu(maCV)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật phân quyền thất bại (lỗi xóa cũ)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (String tenChucNangMoi : dsChucNangDuocChon) {
                PhanQuyenDTO pqDTO = new PhanQuyenDTO(maCV, cnBUS.getMaByTenChucNang(tenChucNangMoi));
                if (!pqBUS.add(pqDTO)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật phân quyền thất bại (lỗi thêm mới)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
    
            JOptionPane.showMessageDialog(dialog, "Cập nhật chức vụ thành công!");
            dialog.dispose();
            loadTableData(ChucVuBUS.getAllChucVu());
        });
    
        dialog.setVisible(true);
    }
    

    private void xoaPhanQuyen() {
        int selectedRow = ContentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String maCV = ContentTable.getValueAt(selectedRow, 0).toString();
        int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa chức vụ này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION && !cvBUS.hasNhanVienOfChucVu(maCV)) {
            if (cvBUS.delete(maCV)) {
                JOptionPane.showMessageDialog(null, "Xóa chức vụ thành công!");
                loadTableData(ChucVuBUS.getAllChucVu());
            } else {
                JOptionPane.showMessageDialog(null, "Xóa chức vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void timKiemPhanQuyen() {
        String kieuTim = cbSearch.getSelectedItem().toString();
        String tuKhoa = txtSearch.getText().trim();
    
        // Gọi DAO
        ArrayList<ChucVuDTO> dsKetQua = cvBUS.search(
            kieuTim, tuKhoa
        );
    
        // Load kết quả vào bảng
        tableModel.setRowCount(0);
        loadTableData(dsKetQua);
}

}

