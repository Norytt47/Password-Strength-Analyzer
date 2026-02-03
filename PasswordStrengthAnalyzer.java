import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class PasswordStrengthAnalyzer extends JFrame {

    private JPasswordField passwordField;
    private JLabel strengthLabel;
    private JProgressBar strengthBar;
    private JTextArea tipsArea;
    private Point mouseClickPoint;

    public PasswordStrengthAnalyzer() {

        setTitle("Password Strength Analyzer");
        setSize(620, 380);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Draggable window
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen() - mouseClickPoint.x;
                int y = e.getYOnScreen() - mouseClickPoint.y;
                setLocation(x, y);
            }
        });

        Font appFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Sidebar
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(20, 20, 20));
        sidebar.setPreferredSize(new Dimension(140, 0));

        JLabel title = new JLabel("<html><center>Password<br>Analyzer</center></html>", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton closeBtn = new JButton("X");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(180, 50, 50));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));

        sidebar.add(title, BorderLayout.CENTER);
        sidebar.add(closeBtn, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.WEST);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 14, 14));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel enterLabel = new JLabel("Enter Password:");
        enterLabel.setForeground(Color.WHITE);
        enterLabel.setFont(appFont);

        // Password panel (field + show/hide)
        JPanel passwordPanel = new JPanel(new BorderLayout(8, 0));
        passwordPanel.setBackground(new Color(30, 30, 30));

        passwordField = new JPasswordField();
        passwordField.setFont(appFont);
        passwordField.setBackground(new Color(45, 45, 45));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        passwordField.setEchoChar('•');

        JButton toggleBtn = new JButton("Show");
        toggleBtn.setFont(appFont);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBackground(new Color(60, 60, 60));
        toggleBtn.setForeground(Color.WHITE);

        toggleBtn.addActionListener(e -> {
            if (passwordField.getEchoChar() == '\u0000') {
                passwordField.setEchoChar('•');
                toggleBtn.setText("Show");
            } else {
                passwordField.setEchoChar('\u0000');
                toggleBtn.setText("Hide");
            }
        });

        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(toggleBtn, BorderLayout.EAST);

        strengthLabel = new JLabel("Strength: ");
        strengthLabel.setForeground(Color.WHITE);
        strengthLabel.setFont(appFont);

        strengthBar = new JProgressBar(0, 5);
        strengthBar.setBackground(new Color(60, 60, 60));
        strengthBar.setBorderPainted(false);

        tipsArea = new JTextArea();
        tipsArea.setEditable(false);
        tipsArea.setLineWrap(true);
        tipsArea.setWrapStyleWord(true);
        tipsArea.setFont(appFont);
        tipsArea.setBackground(new Color(45, 45, 45));
        tipsArea.setForeground(Color.WHITE);
        tipsArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                analyzePassword(new String(passwordField.getPassword()));
            }
        });

        mainPanel.add(enterLabel);
        mainPanel.add(passwordPanel);
        mainPanel.add(strengthLabel);
        mainPanel.add(strengthBar);
        mainPanel.add(tipsArea);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void analyzePassword(String password) {
        int score = 0;
        StringBuilder tips = new StringBuilder();

        if (password.length() >= 8)
            score++;
        else
            tips.append("• Use at least 8 characters\n");

        if (Pattern.compile("[A-Z]").matcher(password).find())
            score++;
        else
            tips.append("• Add uppercase letters\n");

        if (Pattern.compile("[a-z]").matcher(password).find())
            score++;
        else
            tips.append("• Add lowercase letters\n");

        if (Pattern.compile("[0-9]").matcher(password).find())
            score++;
        else
            tips.append("• Add numbers\n");

        if (Pattern.compile("[^a-zA-Z0-9]").matcher(password).find())
            score++;
        else
            tips.append("• Add special characters (!@#$)\n");

        strengthBar.setValue(score);

        if (score <= 2) {
            strengthLabel.setText("Strength: Weak");
            strengthBar.setForeground(Color.RED);
        } else if (score <= 4) {
            strengthLabel.setText("Strength: Moderate");
            strengthBar.setForeground(Color.ORANGE);
        } else {
            strengthLabel.setText("Strength: Strong");
            strengthBar.setForeground(Color.GREEN);
            tips.append("✔ Strong password!");
        }

        tipsArea.setText(tips.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PasswordStrengthAnalyzer().setVisible(true);
        });
    }
}
