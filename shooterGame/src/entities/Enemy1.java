package entities;

import java.awt.*;
import java.util.List;

import java.util.ArrayList;

public class Enemy1 {
    protected int x, y;
    protected final int diameter = 12;
    protected final int speed = 1;
    protected List<Particle> particles = new ArrayList<>();

    protected boolean exploding = false;
    protected long explosionStartTime = 0;
    protected float alpha = 1.0f;

    public Enemy1(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (exploding) {
            long elapsed = System.currentTimeMillis() - explosionStartTime;

            // Изчаква преди започване на избледняването
            if (elapsed >= 1000) {
                alpha -= 0.02f;
                if (alpha < 0f) alpha = 0f;
            }

            // Обнови частиците
            for (int i = 0; i < particles.size(); i++) {
                Particle p = particles.get(i);
                p.update();
                if (!p.isVisible()) {
                    particles.remove(i);
                    i--;
                }
            }

        } else {
            y += speed; // Нормално движение на врага
        }
    }



    public void draw(Graphics2D g2d) {
        if (!exploding) {
            // Основното тяло
            g2d.setColor(Color.BLUE);
            g2d.fillOval(x, y, diameter, diameter);

            // Сини крила – по три линии от всяка страна
            g2d.setColor(Color.CYAN); // или Color.BLUE за по-тъмен син

            for (int i = 0; i < 3; i++) {
                int offset = 4 + i * 4;

                // Ляво крило ↖
                g2d.drawLine(x, y + diameter / 2, x - 10, y + diameter / 2 - offset);

                // Дясно крило ↗
                g2d.drawLine(x + diameter, y + diameter / 2, x + diameter + 10, y + diameter / 2 - offset);
            }
        }

        // Частици при експлозия
        for (Particle p : particles) {
            p.draw(g2d);
        }

        if (exploding && alpha > 0f) {
            g2d.setColor(new Color(1f, 0f, 0f, alpha));
            g2d.fillOval(x, y, diameter, diameter);
        }
    }



    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public boolean isVisible() {
        return alpha > 0;
    }

    public boolean isExploding() {
        return exploding;
    }

    public void explode() {
        if (!exploding) {
            exploding = true;
            explosionStartTime = System.currentTimeMillis();
            for (int i = 0; i < 20; i++) {
                particles.add(new Particle(x + diameter / 2, y + diameter / 2, Color.RED));
            }
        }
    }

    public boolean isFaded() {
        return !isVisible();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void takeHit(int amount) {
        explode();
    }

}
