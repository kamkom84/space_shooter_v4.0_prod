package shooterGame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("space_shooter_v.4.0_prod");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            GamePanel gamePanel = new GamePanel();
            frame.setContentPane(gamePanel);
            frame.setVisible(true);

            gamePanel.requestFocusInWindow();
        });
    }





















}
