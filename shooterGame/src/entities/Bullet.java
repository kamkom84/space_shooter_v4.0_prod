package entities;

import java.awt.*;

public class Bullet {
    private int x, y;
    private final int width = 3;
    private final int height = 5;
    private final int speed = 20;
    private boolean visible = true;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;
        if (y + height < 0) {
            visible = false;
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setInvisible() {
        this.visible = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
