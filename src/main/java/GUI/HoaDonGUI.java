package GUI;

import BUS.HoaDonBUS;
import DTO.HoaDonDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

public class HoaDonFrame extends JInternalFrame {
    private HoaDonBUS hoaDonBUS;
    private JTable table;
    private DefaultTableModel model;

    public HoaDonFrame(Connection conn) {
        setTitle("Quản lý hóa đơn");
        setSize(800, 400);
        setClosable(true);
        setResizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        hoaDonBUS = new HoaDonBUS(conn);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "Mã HĐ", "Mã NV", "Ngày Tạo", "Tổng Tiền", "Tiền Trả", "Tiền Thừa", "Trạng Thái"
        });
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);
        add(btnPanel, BorderLayout.SOUTH);

        // Load data on init
        loadTableData();

        //Làm mới
        btnLamMoi.addActionListener(e -> loadTableData());

        //Thêm
        btnThem.addActionListener(e -> {
            try {
                String maHD = JOptionPane.showInputDialog(this, "Mã hóa đơn:");
                String maNV = JOptionPane.showInputDialog(this, "Mã nhân viên:");
                String ngayTaoStr = JOptionPane.showInputDialog(this, "Ngày tạo (yyyy-mm-dd):");
                String tongTienStr = JOptionPane.showInputDialog(this, "Tổng tiền:");
                String tienTraStr = JOptionPane.showInputDialog(this, "Tiền trả:");
                String tienThuaStr = JOptionPane.showInputDialog(this, "Tiền thừa:");
                String trangThai = JOptionPane.showInputDialog(this, "Trạng thái:");

                HoaDonDTO hd = new HoaDonDTO(
                    maHD,
                    maNV,
                    Date.valueOf(ngayTaoStr),
                    Double.parseDouble(tongTienStr),
                    Double.parseDouble(tienTraStr),
                    Double.parseDouble(tienThuaStr),
                    trangThai
                );

                hoaDonBUS.themHoaDon(hd);
                JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
            }
        });

        //Sửa
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa.");
                return;
            }

            try {
                String maHD = model.getValueAt(row, 0).toString();
                String maNV = JOptionPane.showInputDialog(this, "Mã nhân viên:", model.getValueAt(row, 1));
                String ngayTaoStr = JOptionPane.showInputDialog(this, "Ngày tạo (yyyy-mm-dd):", model.getValueAt(row, 2));
                String tongTienStr = JOptionPane.showInputDialog(this, "Tổng tiền:", model.getValueAt(row, 3));
                String tienTraStr = JOptionPane.showInputDialog(this, "Tiền trả:", model.getValueAt(row, 4));
                String tienThuaStr = JOptionPane.showInputDialog(this, "Tiền thừa:", model.getValueAt(row, 5));
                String trangThai = JOptionPane.showInputDialog(this, "Trạng thái:", model.getValueAt(row, 6));

                HoaDonDTO hd = new HoaDonDTO(
                    maHD,
                    maNV,
                    Date.valueOf(ngayTaoStr),
                    Double.parseDouble(tongTienStr),
                    Double.parseDouble(tienTraStr),
                    Double.parseDouble(tienThuaStr),
                    trangThai
                );

                hoaDonBUS.capNhatHoaDon(hd);
                JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thành công!");
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage());
            }
        });

        // Xóa
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa.");
                return;
            }

            String maHD = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hóa đơn " + maHD + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    hoaDonBUS.xoaHoaDon(maHD);
                    JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                    loadTableData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
                }
            }
        });
    }

    private void loadTableData() {
        model.setRowCount(0);
        ArrayList<HoaDonDTO> ds = hoaDonBUS.layDanhSachHoaDon();
        for (HoaDonDTO hd : ds) {
            model.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getMaNhanVien(),
                hd.getNgayTao(),
                hd.getTongTien(),
                hd.getTienTra(),
                hd.getTienThua(),
                hd.getTrangThai()
            });
        }
    }
}
