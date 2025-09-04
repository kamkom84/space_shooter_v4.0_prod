package entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Enemy4 extends Enemy1 {
    private int x, y;
    private final int size = 20;
    private final int speed = 1;
    private int health = 100; // По-издръжлив враг

    private boolean exploding = false;
    private long explosionStartTime = 0;
    private float alpha = 1.0f;
    private final List<Particle> particles = new ArrayList<>();
    private boolean blinking = false;
    private long blinkStartTime;
    private final long BLINK_DURATION = 200;

    public Enemy4(int x, int y) {
        super(x, y);
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (exploding) {
            long elapsed = System.currentTimeMillis() - explosionStartTime;
            if (elapsed >= 1000) {
                alpha -= 0.02f;
                if (alpha < 0f) alpha = 0f;
            }

            for (int i = 0; i < particles.size(); i++) {
                Particle p = particles.get(i);
                p.update();
                if (!p.isVisible()) {
                    particles.remove(i);
                    i--;
                }
            }
        } else {
            y += speed;

            if (blinking && System.currentTimeMillis() - blinkStartTime > BLINK_DURATION) {
                blinking = false;
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

            // Лилаво тяло
            g2d.setColor(new Color(128, 0, 128)); // Purple
            int[] xPoints = { x, x + size / 2, x - size / 2 };
            int[] yPoints = { y + size, y, y };
            g2d.fillPolygon(xPoints, yPoints, 3);

            // Червени крила
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.RED);

            for (int i = 0; i < 3; i++) {
                int offset = 4 + i * 4;
                g2d.drawLine(x - size / 2, y, x - size / 2 - 10, y - offset);
                g2d.drawLine(x + size / 2, y, x + size / 2 + 10, y - offset);
            }
        }

        // Експлозия
        for (Particle p : particles) {
            p.draw(g2d);
        }

        if (exploding && alpha > 0f) {
            g2d.setColor(new Color(1f, 0f, 0f, alpha));
            g2d.fillOval(x - size / 2, y, size, size);
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
            explosionStartTime = System.currentTimeMillis();
            for (int i = 0; i < 30; i++) {
                particles.add(new Particle(x, y + size / 2, Color.RED));
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - size / 2, y, size, size);
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
