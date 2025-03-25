package fillter;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public final class Button extends JButton implements MouseListener {
    public Color defaultColor; 
    public String buttonType;
    public static Button selectedButton = null;


    // Constructor chính
    public Button(String TypeB, String text) {
        this(TypeB, text, 100, 40);
    }

    public Button(String TypeB, String text, int width, int height) {
        initComponent(TypeB, text, width, height);
    }

    public Button(String typeBtn, String text, int width, int height, String urlIcon) {
        initComponent(typeBtn, text, width, height);
        setButtonIcon(urlIcon);
    }

    // Phương thức khởi tạo nút
    public void initComponent(String TypeB, String text, int width, int height) {
        this.buttonType = TypeB;
        this.defaultColor = Colors.MAIN_BUTTON; // Màu mặc định cho nút

        Color ColorB = Colors.BUTTON_DEFAULT; // Màu mặc định

        if (TypeB != null) {
            switch (TypeB) {
                case "add" -> ColorB = Colors.BUTTON_GREEN;
                case "delete" -> ColorB = Colors.BUTTON_RED;
                case "edit" -> ColorB = Colors.BUTTON_BLUE;
                case "confirm" -> ColorB = Colors.BUTTON_GREEN;
                case "login" -> ColorB = Colors.BUTTON_BLUE;
                case "menuButton" -> ColorB = Colors.WHITE_FONT;
            }
        }

        this.setText(text);
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(ColorB);
        this.defaultColor = ColorB;
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.addMouseListener(this);
        this.setHorizontalAlignment(SwingConstants.LEFT);  
        this.setHorizontalTextPosition(SwingConstants.RIGHT); // Chữ bên phải icon
        this.setIconTextGap(20);
    }

    // Phương thức đặt icon cho nút
    public void setButtonIcon(String urlImage) {
        if (urlImage != null && !urlImage.isEmpty()) {
            ImageIcon icon = new ImageIcon(getClass().getResource(urlImage));           
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            this.setIcon(new ImageIcon(scaledImage));
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            this.setIconTextGap(5); // Khoảng cách giữa icon và text
            
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if ("menuButton".equals(buttonType)) 
            this.setBackground(Colors.HOVER_BUTTON); // Màu hover khác cho menuButton
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if ("menuButton".equals(buttonType)) {
            // Chỉ đổi màu về mặc định nếu đây không phải là nút đang được chọn
            if (this != Button.selectedButton) {
            this.setBackground(defaultColor);
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
