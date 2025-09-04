package shooting;

import entities.Bullet;
import shooterGame.GamePanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShootingLevel1 {
    private final List<Bullet> bullets = new ArrayList<>();
    private final GamePanel panel;

    public ShootingLevel1(GamePanel panel) {
        this.panel = panel;
    }

    public void shoot(int x, int y) {
        bullets.add(new Bullet(x, y));
    }

    public void draw(java.awt.Graphics2D g2d) {
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();

            if (!bullet.isVisible()) {
                iterator.remove();  // маха патрона, ако излезе от екрана или е невидим
            }
        }
    }




}
