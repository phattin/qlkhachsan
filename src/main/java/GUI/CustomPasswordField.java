package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomPasswordField extends JPasswordField {
    private int cornerRadius = 15;
    private Color borderColor = new Color(180, 180, 180);
    private Color backgroundColor = Color.WHITE;
    private Color placeholderColor = new Color(255, 218, 31, 200); // Màu vàng cho placeholder và icon
    private String placeholder = "";
    private Icon icon = null;

    public CustomPasswordField(int columns) {
        super(columns);
        setOpaque(false); // Tắt nền mặc định
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Sự kiện focus đổi màu viền
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                borderColor = new Color(0, 150, 255);
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                borderColor = new Color(180, 180, 180);
                repaint();
            }
        });
    }

    // Tùy chỉnh độ bo góc
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    // Cài đặt placeholder
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    // Cài đặt icon
    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    // Cài đặt màu placeholder
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }

    // Vẽ ô nhập bo góc
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền và viền bo góc
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Vẽ icon (nếu có)
        int iconSize = 20;
        int iconPadding = 10;
        if (icon != null) {
            g2.setColor(placeholderColor);
            icon.paintIcon(this, g2, iconPadding, (getHeight() - iconSize) / 2);
        }

        // Vẽ placeholder (nếu có và ô trống)
        if (placeholder != null && getText().isEmpty() && !isFocusOwner()) {
            g2.setColor(placeholderColor);
            int textX = (icon != null) ? iconSize + iconPadding * 2 : 10;
            g2.drawString(placeholder, textX, getHeight() / 2 + getFont().getSize() / 2 - 2);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    // Điều chỉnh khoảng cách text khi có icon
    @Override
    public Insets getInsets() {
        if (icon != null) {
            return new Insets(5, 45, 5, 5);
        }
        return super.getInsets();
    }
}
