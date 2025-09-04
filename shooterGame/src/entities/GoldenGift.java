package entities;

import bonus.BonusObject;
import shooterGame.GamePanel;

import java.awt.*;

public class GoldenGift extends Enemy1 {
    private boolean spawnedBonus = false;
    private GamePanel panel;

    public GoldenGift(int x, int y, GamePanel panel) {
        super(x, y);
        this.panel = panel;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // 💡 Светещ ореол
        int glowSize = diameter + 20; // колко да е по-голям от жълтия кръг
        int glowX = x - (glowSize - diameter) / 2;
        int glowY = y - (glowSize - diameter) / 2;

        // Копие за по-фина работа с прозрачност
        Graphics2D g2 = (Graphics2D) g2d.create();

        // Мека прозрачност – сияние
        g2.setColor(new Color(1f, 1f, 1f, 0.15f));
        g2.setComposite(AlphaComposite.SrcOver);
        g2.fillOval(glowX, glowY, glowSize, glowSize);

        g2.dispose(); // Връщаме ресурса

        // 🟡 Жълтата сърцевина
        if (exploding) {
            g2d.setColor(new Color(1f, 1f, 0f, alpha));
        } else {
            g2d.setColor(Color.YELLOW);
        }
        g2d.fillOval(x, y, diameter, diameter);
    }



    @Override
    public void update() {
        super.update();

        if (exploding && !spawnedBonus && (System.currentTimeMillis() - explosionStartTime > 300)) {
            spawnedBonus = true;
            int bonusX = x + diameter / 2;
            int bonusY = y + diameter;
            panel.spawnBonus(new BonusObject(bonusX, bonusY));
        }
    }
}
