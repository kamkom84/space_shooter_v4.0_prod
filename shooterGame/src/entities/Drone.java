package entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Drone {
    private int x, y;
    private final int width = 10;
    private final List<Bullet> bullets = new ArrayList<>();

    public Drone(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void shoot() {
        bullets.add(new Bullet(x + width / 2, y));
    }

    public void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (!bullet.isVisible()) {
                iterator.remove();
            }
        }
    }




    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(2));

        int cx = x;
        int cy = y;

        int size = 10;

        int[] xPoints = {
                cx,              // връх
                cx + size / 2,
                cx,
                cx - size / 2
        };

        int[] yPoints = {
                cy - size,       // връх нагоре
                cy,
                cy + size,
                cy
        };

        Polygon star = new Polygon();

        // Добавяме ръчно 8 точки за звездата (4 в, 4 извити)
        star.addPoint(cx, cy - size);            // Горе
        star.addPoint(cx + size / 3, cy - size / 3);
        star.addPoint(cx + size, cy);            // Дясно
        star.addPoint(cx + size / 3, cy + size / 3);
        star.addPoint(cx, cy + size);            // Долу
        star.addPoint(cx - size / 3, cy + size / 3);
        star.addPoint(cx - size, cy);            // Ляво
        star.addPoint(cx - size / 3, cy - size / 3);

        g2d.drawPolygon(star);

        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
    }






    public List<Bullet> getBullets() {
        return bullets;
    }
}
