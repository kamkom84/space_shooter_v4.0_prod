package entities;

import java.awt.*;

public class Particle {
    private double x, y;
    private int size;
    private Color color;
    private double speedX, speedY;
    private float alpha = 1.0f;

    public Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.size = (int) (Math.random() * 6 + 2); // 2 до 7 px
        this.color = color;

        // Случайна скорост по X и Y (между -3 и +3)
        this.speedX = Math.random() * 6 - 3;
        this.speedY = Math.random() * 6 - 3;
    }

    public void update() {
        x += speedX;
        y += speedY;
        alpha -= 0.03f;
        if (alpha < 0f) alpha = 0f;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(
                color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)
        ));
        g2d.fillOval((int) x, (int) y, size, size);
    }

    public boolean isFaded() {
        return alpha <= 0;
    }

    public boolean isVisible() {
        return alpha > 0f;
    }
}
