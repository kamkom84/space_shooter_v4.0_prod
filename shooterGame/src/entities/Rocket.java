package entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rocket {
    private double x, y;
    private double vx, vy;
    private final double speed = 6.0;
    private boolean visible = true;
    private Enemy1 target;
    private boolean exploding = false;
    private float alpha = 1f;
    private long explosionStartTime;
    private final List<Particle> particles = new ArrayList<>();
    private double angle;
    private boolean hasHit = false;







    public Rocket(int x, int y, Enemy1 target) {
        this.x = x;
        this.y = y;
        this.target = target;

        // Първоначална посока
        if (target != null) {
            updateDirection();
        }
    }

    public void update() {
        if (exploding) {
            long elapsed = System.currentTimeMillis() - explosionStartTime;
            if (elapsed >= 1000) alpha -= 0.02f;
            if (alpha < 0f) alpha = 0f;

            for (int i = 0; i < particles.size(); i++) {
                Particle p = particles.get(i);
                p.update();
                if (!p.isVisible()) {
                    particles.remove(i);
                    i--;
                }
            }
            return;
        }

        if (target != null && !target.isExploding()) {
            updateDirection(); // обновяване на посоката към целта
            x += vx;
            y += vy;

            if (target.getBounds().contains(x, y)) {
                target.takeHit(5);/////////////////////////////////////
                explode();
            }
        } else {
            visible = false;
        }
    }

    private void updateDirection() {
        if (target == null) return;

        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist != 0) {
            vx = dx / dist * speed;
            vy = dy / dist * speed;
            angle = Math.atan2(vy, vx);
        }
    }


    public void draw(Graphics2D g2d) {
        if (!visible) return;

        Graphics2D g = (Graphics2D) g2d.create();
        g.translate(x, y);
        g.rotate(angle); // посоката сочи към целта

        double scale = 0.5; // С коефициент за мащабиране

        // тяло
        g.setColor(Color.GRAY);
        g.fillRect((int)(-6 * scale), (int)(-2 * scale), (int)(12 * scale), (int)(4 * scale));

        // връх
        g.setColor(Color.RED);
        g.fillPolygon(
                new int[]{
                        (int)(6 * scale), (int)(12 * scale), (int)(6 * scale)
                },
                new int[]{
                        (int)(-3 * scale), (int)(0), (int)(3 * scale)
                },
                3
        );

        // крила
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int)(-8 * scale), (int)(-4 * scale), (int)(2 * scale), (int)(5 * scale));
        g.fillRect((int)(-8 * scale), (int)(-1 * scale), (int)(2 * scale), (int)(5 * scale));

        // огън
        g.setColor(Color.ORANGE);
        g.fillOval((int)(-10 * scale), (int)(-3 * scale), (int)(4 * scale), (int)(6 * scale));

        g.dispose();
    }




    public boolean isVisible() {
        return visible || (exploding && alpha > 0);
    }

    public void explode() {
        exploding = true;
        visible = false;
        explosionStartTime = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            particles.add(new Particle((int) x, (int) y, Color.RED));
        }
    }

    public Enemy1 getTarget() {
        return target;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x - 4, (int)y - 4, 8, 8);
    }

    public boolean hasHit() {
        return hasHit;
    }

    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }

}
