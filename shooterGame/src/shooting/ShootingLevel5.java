package shooting;

import entities.Bullet;
import entities.Shooter;
import shooterGame.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShootingLevel5 {
    private final List<Bullet> bullets = new ArrayList<>();
    private final GamePanel panel;
    private final Shooter shooter;

    public ShootingLevel5(GamePanel panel, Shooter shooter) {
        this.panel = panel;
        this.shooter = shooter;
    }

    public void shoot(int x, int y) {
        // Стреля 5 патрона: най-ляв, ляв, център, десен, най-десен
        bullets.add(new Bullet(x - 16, y));  // най-ляв
        bullets.add(new Bullet(x - 8, y));   // ляв
        bullets.add(new Bullet(x, y));       // център
        bullets.add(new Bullet(x + 8, y));   // десен
        bullets.add(new Bullet(x + 16, y));  // най-десен
    }

    public void draw(Graphics2D g2d) {
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
    }

    public void update() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();

            if (!bullet.isVisible()) {
                iterator.remove();
            }
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}

