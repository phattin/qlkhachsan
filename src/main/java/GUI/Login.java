package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import BUS.TaiKhoanBUS;
import DTO.TaiKhoanDTO;

public class Login extends JFrame {

    private TittleBar tittleBar;

    public Login() {
        // Cấu hình cửa sổ
        setTitle("Đăng nhập");
        setSize(734, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        // ===== THÊM THANH TIÊU ĐỀ =====
        tittleBar = new TittleBar(this);
        add(tittleBar, BorderLayout.NORTH);

        // Panel chính chứa 2 phần
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        getContentPane().add(mainPanel);

        // ==== PHẦN BÊN TRÁI (Hình ảnh + Tiêu đề) ==== 
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE); // Nền trắng
        leftPanel.setLayout(new BorderLayout());
        

        ImageIcon originalIcon = new ImageIcon("src/main/resources/images/iconKhachSan.png");
        Image originalImage = originalIcon.getImage();
        int newWidth = 200;
        int newHeight = 200;
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        // Icon 
        ImageIcon icon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(icon);

        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // ==== PHẦN BÊN PHẢI (Form đăng nhập) ====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.white); // Nền trắng
        rightPanel.setLayout(null);

        JLabel loginTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Roboto", Font.BOLD, 27));
        loginTitle.setBounds(50, 100, 200, 30);
        rightPanel.add(loginTitle);
        
        CustomTextField userField = new CustomTextField(20);
        userField.setBounds(30, 170, 250, 40);
        userField.setPlaceholder("Nhập tên đăng nhập");
        ImageIcon userIcon = new ImageIcon("src/main/resources/images/userIcon.png");
        Image imgUser = userIcon.getImage();
        Image resizedImgUser = imgUser.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        userIcon = new ImageIcon(resizedImgUser);
        userField.setIcon(userIcon);
        userField.setText("admin");
    
        rightPanel.add(userField);

        CustomPasswordField passField = new CustomPasswordField(20);
        passField.setBounds(30, 230, 250, 40);
        passField.setPlaceholder("Nhập mật khẩu");
        ImageIcon passwordIcon = new ImageIcon("src/main/resources/images/keyIcon.png");
        Image imgPass = passwordIcon.getImage();
        Image resizedImgPass = imgPass.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        passwordIcon = new ImageIcon(resizedImgPass);
        passField.setIcon(passwordIcon);
        passField.setText("123456");
        rightPanel.add(passField);

        //Khi ấn enter trong user hay pass
        userField.addActionListener((ActionEvent e) -> {
            passField.requestFocusInWindow();
        });
        passField.addActionListener((ActionEvent e) -> {
            checkLogin(userField, passField);
        });

        CustomButton loginButton = new CustomButton("Đăng nhập");
        loginButton.setBounds(30, 300, 250, 40);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(255, 218, 31));
        loginButton.setFocusPainted(false);

        loginButton.addActionListener((ActionEvent e) -> {
            checkLogin(userField, passField); // Gọi hàm check khi bấm nút
        });

        rightPanel.add(loginButton);
        // Thêm vào mainPanel   
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

    }

    private void checkLogin(JTextField userField, JTextField passField) {
        String username = userField.getText();
        String password = passField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            TaiKhoanDTO tkDTO = TaiKhoanBUS.getTaiKhoan(username, password);
            if (tkDTO != null) {
                TaiKhoanBUS.setMaNVDangDN(tkDTO.getMaNhanVien());
                //Chay vao frame Main_Layout
                JOptionPane.showMessageDialog(null, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
                MainLayout mainLayout = new MainLayout(tkDTO.getMaNhanVien());
                mainLayout.setVisible(true);
             }
            else{
                JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);                
                
            }}

    }

    public static void main(String[] args) {
        Login a = new Login();
        a.setVisible(true);
    }
}