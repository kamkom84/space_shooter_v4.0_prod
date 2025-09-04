package entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Enemy5 extends Enemy1 {
    private int x, y;
    private final int radius = 12;
    private final int speed = 1;
    private int health = 150;

    private boolean exploding = false;
    private float alpha = 1.0f;
    private final List<Particle> particles = new ArrayList<>();

    private final Color[] bossColors = {
            Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED, new Color(128, 0, 128) // лилав
    };
    private int currentColorIndex = 0;
    private long lastColorChangeTime = System.currentTimeMillis();
    private final long COLOR_CHANGE_INTERVAL = 200;
    private boolean blinking = false;
    private long blinkStartTime;
    private final long BLINK_DURATION = 100; // време на премигване


    public Enemy5(int x, int y) {
        super(x, y);
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (!exploding) {
            y += speed;

            long now = System.currentTimeMillis();
            if (now - lastColorChangeTime >= COLOR_CHANGE_INTERVAL) {
                currentColorIndex = (currentColorIndex + 1) % bossColors.length;
                lastColorChangeTime = now;
            }

        } else {
            alpha -= 0.01f;
            if (alpha < 0f) alpha = 0f;

            for (int i = 0; i < particles.size(); i++) {
                Particle p = particles.get(i);
                p.update();
                if (!p.isVisible()) {
                    particles.remove(i);
                    i--;
                }
            }
        }
    }





    public void draw(Graphics2D g2d) {
        if (!exploding) {
            if (blinking) {
                long elapsed = System.currentTimeMillis() - blinkStartTime;
                if (elapsed < BLINK_DURATION) {
                    if ((System.currentTimeMillis() / 100) % 2 == 0) return;
                } else {
                    blinking = false;
                }
            }

            // 🟡 Тяло на боса
            g2d.setColor(bossColors[currentColorIndex]);
            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            // 👁 Очите (винаги бели)
            g2d.setColor(Color.RED);
            g2d.fillOval(x - 15, y - 10, 10, 10);
            g2d.fillOval(x + 5, y - 10, 10, 10);

            // 🪶 Крила тип Enemy2/3/4
            g2d.setColor(Color.CYAN); // можеш да смениш на друг цвят
            g2d.setStroke(new BasicStroke(2));

            for (int i = 0; i < 3; i++) {
                int offset = 4 + i * 4;

                // Ляво крило ↖
                g2d.drawLine(x - radius, y, x - radius - 10, y - offset);

                // Дясно крило ↗
                g2d.drawLine(x + radius, y, x + radius + 10, y - offset);
            }
        }

        // 💥 Частици при експлозия
        for (Particle p : particles) {
            p.draw(g2d);
        }

        // 🔴 Експлозия (фейд)
        if (exploding && alpha > 0f) {
            g2d.setColor(new Color(1f, 0f, 0f, alpha));
            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }











    public void takeHit(int damage) {
        health -= damage;
        if (health <= 0) {
            explode();
        } else {
            blinking = true;
            blinkStartTime = System.currentTimeMillis();
        }
    }


    public void explode() {
        if (!exploding) {
            exploding = true;
            for (int i = 0; i < 60; i++) {
                particles.add(new Particle(x, y, Color.WHITE));
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean isExploding() {
        return exploding;
    }

    public boolean isFaded() {
        return alpha <= 0f;
    }

    public boolean isVisible() {
        return alpha > 0f;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
