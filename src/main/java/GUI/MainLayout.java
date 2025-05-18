package GUI; 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import BUS.ChucNangBUS;
import BUS.ChucVuBUS;
import BUS.PhanQuyenBUS;
import DTO.PhanQuyenDTO;
import fillter.Button;
import fillter.Colors;

public final class MainLayout extends JFrame {
    private JPanel Title, Menu, Content; 
    private Button[] buttons;
    private static String maNhanVien;
    
    public MainLayout(String maNV) {
        this.maNhanVien = maNV;
        initComponent();
    }

    public void initComponent() {
        this.setSize(new Dimension(Colors.WIDTH, Colors.HEIGHT));
        this.getContentPane().setBackground(Colors.SUB_BACKGROUND);
        this.getContentPane().setLayout(new BorderLayout(5, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);

        // ======== Panel Title ========
        Title = new JPanel(null);
        Title.setBackground(Colors.MAIN_BUTTON);
        Title.setPreferredSize(new Dimension(0, 50));

        JLabel LBTitle = new JLabel("Quản Lý Khách Sạn");
        LBTitle.setFont(Colors.TITLE_FONT);
        LBTitle.setForeground(Colors.BLACK_FONT);
        LBTitle.setBounds(10, 5, 300, 40);

        ImageIcon minimizeIcon = new ImageIcon(getClass().getResource("/Icon/minimize_icon.png"));
        ImageIcon closeIcon = new ImageIcon(getClass().getResource("/Icon/close_icon.png"));
        Image imgMinimize = minimizeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image imgClose = closeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        JButton ButtonMinimize = new JButton(new ImageIcon(imgMinimize));
        ButtonMinimize.setBackground(Colors.MAIN_BUTTON);
        ButtonMinimize.setBorder(null);
        ButtonMinimize.setBounds(Colors.WIDTH - 80, 10, 30, 30);
        ButtonMinimize.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton ButtonClose = new JButton(new ImageIcon(imgClose));
        ButtonClose.setBackground(Colors.MAIN_BUTTON);
        ButtonClose.setBorder(null);
        ButtonClose.setBounds(Colors.WIDTH - 40, 10, 30, 30);
        ButtonClose.addActionListener(e -> System.exit(0));

        // ======== Panel Menu ========
        Menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        Menu.setBackground(Colors.WHITE_FONT);
        Menu.setPreferredSize(new Dimension(200, 0));

        ArrayList<String> LBButtons = new ArrayList<>();
        
        // ======== Panel Content ========
        Content = new JPanel(new BorderLayout()); 
        Content.setBackground(Colors.WHITE_FONT);
        
        // Tạo các đường dẫn chứa icon
        String urlPhong = "/Icon/PHONG.png";
        String urlDatPhong = "/Icon/PHONG.png";
        String urlKhachHang = "/Icon/KhachHang_icon.png";
        String urlNhanVien = "/Icon/Nhanvien_icon.png";
        String urlTaiKhoan = "/Icon/TaiKhoan_icon.png";
        String urlPhanQuyen = "/Icon/PhanQuyen_icon.png";
        String urlHoaDon = "/Icon/XuatHang_icon.png";
        String urlDichVu = "/Icon/ThongTinSach_icon.png";
        String urlThongKe = "/Icon/ThongKe_icon.png";
        // Danh sách chứa các đường dẫn
        ArrayList<String> urlList = new ArrayList<>();
        
        //Kiểm tra quyền của nhân viên
        System.out.println("Mã nhân viên: " + maNhanVien);
        String maChucVu = ChucVuBUS.getChucVuByMaNhanVien(maNhanVien).getMaChucVu();
        System.out.println("Mã chức vụ: " + maChucVu);
        ArrayList<PhanQuyenDTO> pqList = PhanQuyenBUS.getPhanQuyenByMaChucVu(maChucVu);
        for (PhanQuyenDTO pq : pqList) {
            //Tên chức năng
            String tenChucNang = ChucNangBUS.getChucNangByMa(pq.getMaChucNang()).getTenChucNang();
            switch (tenChucNang) {
                case "Quản lý phòng":
                LBButtons.add("Phòng");
                urlList.add(urlPhong);
                break;
                case "Đặt phòng":
                LBButtons.add("Đặt phòng");
                urlList.add(urlDatPhong);
                break;
                case "Quản lý khách hàng":
                LBButtons.add("Khách hàng");
                urlList.add(urlKhachHang);
                break;
                case "Quản lý nhân viên":
                LBButtons.add("Nhân viên");
                urlList.add(urlNhanVien);
                    break;
                    case "Quản lý tài khoản":
                    LBButtons.add("Tài khoản");
                    urlList.add(urlTaiKhoan);
                    break;
                    case "Quản lý phân quyền":
                    LBButtons.add("Phân quyền");
                    urlList.add(urlPhanQuyen);
                    break;
                    case "Quản lý hóa đơn":
                    LBButtons.add("Hóa đơn");
                    urlList.add(urlHoaDon);
                    break;
                    case "Quản lý dịch vụ":
                    LBButtons.add("Dịch vụ");
                    urlList.add(urlDichVu);
                    break;
                    case "Thống kê":
                    LBButtons.add("Thống kê");
                    urlList.add(urlThongKe);
                    break;
                }
            }
            
            //Chọn các chức năng
            buttons = new Button[LBButtons.size()];
            for (int i = 0; i < LBButtons.size(); i++) {
                String tenChucNang = LBButtons.get(i);
                String iconPath = urlList.get(i);
                buttons[i] = new Button("menuButton", tenChucNang, 180, 40);
                buttons[i].setButtonIcon(iconPath);
            
                Menu.add(buttons[i]);
            
                // Tạo panel tương ứng (tuỳ vào tên chức năng)
                JPanel targetPanel = switch (tenChucNang) {
                    case "Phòng" -> new PhongGUI();
                    case "Đặt phòng" -> new DatPhongGUI();
                    case "Khách hàng" -> new KhachHangGUI();
                    case "Nhân viên" -> new NhanVienGUI();
                    case "Tài khoản" -> new TaiKhoanGUI();
                    case "Phân quyền" -> new PhanQuyenGUI();
                    case "Hóa đơn" -> new HoaDonGUI();
                     case "Dịch vụ" -> new DichVuGUI();
                    case "Thống kê" -> new ThongKeGUI();
                    default -> new JPanel(); // mặc định nếu không khớp
                };
            
                final int index = i;
                final JPanel selectedPanel = targetPanel;
                buttons[i].addActionListener(e -> switchPanel(selectedPanel, buttons[index]));
            }
        // =============== Thêm nút Đăng Xuất cuối cùng Menu ===============
        JPanel panelDangXuat = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelDangXuat.setBackground(Colors.WHITE_FONT);
        panelDangXuat.setPreferredSize(new Dimension(200, 80)); // đủ chỗ cho nút

        Button btnDangXuat = new Button("menuButton", "Đăng xuất", 180, 40, "/Icon/logout_icon.png");
        btnDangXuat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.maNhanVien = null; // Đặt lại mã nhân viên
                this.dispose(); // Đóng cửa sổ hiện tại
                Login App = new Login();
                App.setVisible(true);
            }
        });

        panelDangXuat.add(btnDangXuat);
        Menu.add(panelDangXuat);  // thêm vào cuối Menu

        Title.add(LBTitle);
        Title.add(ButtonMinimize);
        Title.add(ButtonClose);

        this.getContentPane().add(Menu, BorderLayout.WEST);
        this.getContentPane().add(Content, BorderLayout.CENTER);
        this.getContentPane().add(Title, BorderLayout.NORTH);

        //  Thiết lập trang chủ hiển thị đầu tiên
        String firstChucNang = LBButtons.get(0);
        JPanel firstPanel = switch (firstChucNang) {
            case "Phòng" -> new PhongGUI();
            case "Đặt phòng" -> new DatPhongGUI();
            case "Khách hàng" -> new KhachHangGUI();
             case "Nhân viên" -> new NhanVienGUI();
             case "Tài khoản" -> new TaiKhoanGUI();
             case "Phân quyền" -> new PhanQuyenGUI();
            case "Hóa đơn" -> new HoaDonGUI();
             case "Dịch vụ" -> new DichVuGUI();
            case "Thống kê" -> new ThongKeGUI();
            default -> new JPanel();
        };
        switchPanel(firstPanel, buttons[0]);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void switchPanel(JPanel newPanel, Button selectedBtn) {
        Content.removeAll();
        Content.add(newPanel, BorderLayout.CENTER);
        Content.revalidate();
        Content.repaint();

        // Gọi loadTableData nếu panel có hàm này
        try {
            java.lang.reflect.Method method = newPanel.getClass().getMethod("loadTableData");
            method.invoke(newPanel);
        } catch (Exception ex) {
            // Không có hàm loadTableData thì bỏ qua
        }

        if (Button.selectedButton != null) {
            Button.selectedButton.setBackground(Button.selectedButton.defaultColor);
        }
        selectedBtn.setBackground(Colors.HOVER_BUTTON);
        Button.selectedButton = selectedBtn;
    }

    public static String getMaNVDangDN() {
        return maNhanVien;
    }
}
