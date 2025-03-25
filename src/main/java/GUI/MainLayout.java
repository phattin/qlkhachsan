package GUI; 

import fillter.Button;
import fillter.Colors;
import java.awt.*;
import javax.swing.*;

public final class MainLayout extends JFrame {
    private JPanel Title, Menu, Content; 
    private Button[] buttons;
    private String maNV;
    
    public MainLayout(String maNV) {
        initComponent();
        this.maNV = maNV;
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

        String[] LBButtons = {
            "TRANG CHỦ", "PHÒNG", "KHÁCH HÀNG", "NHÂN VIÊN",
            "HÓA ĐƠN", "DỊCH VỤ", "BÁO CÁO", "THỐNG KÊ"
        };
        buttons = new Button[LBButtons.length];

        // ======== Panel Content ========
        Content = new JPanel(new BorderLayout()); 
        Content.setBackground(Colors.WHITE_FONT);

        // Tạo các màn hình
        JPanel emptyPanel = new JPanel();
        PhongGUI Phong = new PhongGUI();
//        Khachhang Khachhang = new Khachhang();
//        Nhanvien Nhanvien = new Nhanvien();
//        Hoadon Hoadon = new Hoadon();
//        Dichvu Dichvu = new Dichvu();
//        Baocao Baocao = new Baocao();
//        Thongke Thongke = new Thongke();

        for (int i = 0; i < LBButtons.length; i++) {
            final int index = i;
            buttons[i] = new Button("menuButton", LBButtons[i], 180, 40);
            Menu.add(buttons[i]);

            JPanel targetPanel = new JPanel();
            switch (index) {
                case 0: 
                    buttons[i].setButtonIcon("/Icon/TrangChu_icon.png");  
                    targetPanel = emptyPanel;
                    break;
                case 1:
                    buttons[i].setButtonIcon("/Icon/PHONG.png");
                    targetPanel = Phong;
                    break;
                case 2:
                    buttons[i].setButtonIcon("/Icon/KhachHang_icon.png");
//                    targetPanel = Khachhang;
                    break;
                case 3:
                    buttons[i].setButtonIcon("/Icon/Nhanvien_icon.png");
//                    targetPanel = Nhanvien;
                    break;
                case 4:
                    buttons[i].setButtonIcon("/Icon/XuatHang_icon.png");
//                    targetPanel = Hoadon;
                    break;
                case 5:
                    buttons[i].setButtonIcon("/Icon/ThongTinSach_icon.png");
//                    targetPanel = Dichvu;
                    break;
                case 6:
                    buttons[i].setButtonIcon("/Icon/ThongKe_icon.png");
//                    targetPanel = Baocao;
                    break;
                case 7:
                    buttons[i].setButtonIcon("/Icon/ThongKe_icon.png");
//                    targetPanel = Thongke;
                    break;
                default:
                    targetPanel = emptyPanel;
            }
            final JPanel selectedPanel = targetPanel;
            buttons[index].addActionListener(e -> switchPanel(selectedPanel, buttons[index]));
        }

        Title.add(LBTitle);
        Title.add(ButtonMinimize);
        Title.add(ButtonClose);

        this.getContentPane().add(Menu, BorderLayout.WEST);
        this.getContentPane().add(Content, BorderLayout.CENTER);
        this.getContentPane().add(Title, BorderLayout.NORTH);

        //  Thiết lập trang chủ hiển thị đầu tiên
        switchPanel(emptyPanel, buttons[0]); 

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void switchPanel(JPanel newPanel, Button selectedBtn) {
        Content.removeAll();
        Content.add(newPanel, BorderLayout.CENTER);
        Content.revalidate();
        Content.repaint();

        if (Button.selectedButton != null) {
            Button.selectedButton.setBackground(Button.selectedButton.defaultColor); // Trả về màu cũ
        }

        // Đổi màu cho nút đang chọn
        selectedBtn.setBackground(Colors.HOVER_BUTTON);
        Button.selectedButton = selectedBtn;
    }

    public static void main(String args[]) {
    }
}
