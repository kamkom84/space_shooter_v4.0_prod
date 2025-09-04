package entities;
import java.awt.*;

public class Shooter {
    private int x, y;
    private final int width = 50;
    private final int height = 40;
    private boolean doubleShoot = false;
    private final Color color = Color.BLUE;
    private boolean glowing = false;
    private long glowStartTime = 0;
    private static final int GLOW_DURATION = 300; // милисекунди



    public Shooter(int x, int y) {
        this.x = x;
        this.y = y;
    }




    public void draw1(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (glowing) {
            long elapsed = System.currentTimeMillis() - glowStartTime;
            if (elapsed > GLOW_DURATION) {
                glowing = false;
            }
        }

        Color bodyColor = glowing ? Color.YELLOW : color;
        g2d.setColor(bodyColor);

        // Централен триъгълник (основно тяло)
        int[] xBody = { x, x + width / 2, x + width };
        int[] yBody = { y + height, y, y + height };
        g2d.fillPolygon(xBody, yBody, 3);

        // Ляво крило
        int[] xLeftWing = { x, x - 10, x + width / 4 };
        int[] yLeftWing = { y + height, y + height + 10, y + height };
        g2d.fillPolygon(xLeftWing, yLeftWing, 3);

        // Дясно крило
        int[] xRightWing = { x + width, x + width + 10, x + width - width / 4 };
        int[] yRightWing = { y + height, y + height + 10, y + height };
        g2d.fillPolygon(xRightWing, yRightWing, 3);


    }

    public void draw2(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (glowing) {
            long elapsed = System.currentTimeMillis() - glowStartTime;
            if (elapsed > GLOW_DURATION) glowing = false;
        }

        Color bodyColor = glowing ? Color.YELLOW : color;
        g2d.setColor(bodyColor);

        // Намален мащаб с 25%
        int scale = 3; // колкото по-голям, толкова по-малък става самолетът

        // Централно тяло – дълго и тънко
        int[] xBody = {
                x + width / 2 - 5 / scale, x + width / 2 + 5 / scale,
                x + width / 2 + 10 / scale, x + width / 2 + 10 / scale,
                x + width / 2 + 6 / scale, x + width / 2 + 6 / scale,
                x + width / 2 - 6 / scale, x + width / 2 - 6 / scale,
                x + width / 2 - 10 / scale, x + width / 2 - 10 / scale,
                x + width / 2 - 5 / scale
        };

        int[] yBody = {
                y,
                y,
                y + 20 / scale,
                y + 60 / scale,
                y + 90 / scale,
                y + height / scale,
                y + height / scale,
                y + 90 / scale,
                y + 60 / scale,
                y + 20 / scale,
                y
        };

        g2d.fillPolygon(xBody, yBody, xBody.length);

        // Крила – разперени назад
        int[] xLeftWing = {
                x + width / 2 - 10 / scale,
                x - 20 / scale,
                x + width / 2 - 10 / scale
        };
        int[] yLeftWing = {
                y + 40 / scale,
                y + 70 / scale,
                y + 100 / scale
        };

        int[] xRightWing = {
                x + width / 2 + 10 / scale,
                x + width + 20 / scale,
                x + width / 2 + 10 / scale
        };
        int[] yRightWing = {
                y + 40 / scale,
                y + 70 / scale,
                y + 100 / scale
        };

        g2d.fillPolygon(xLeftWing, yLeftWing, 3);
        g2d.fillPolygon(xRightWing, yRightWing, 3);

        // Опашка – двойна стабилизаторна
        g2d.fillRect(x + width / 2 - 8 / scale, y + height / scale, 3, 10 / scale);
        g2d.fillRect(x + width / 2 + 5 / scale, y + height / scale, 3, 10 / scale);
    }

    public void draw3(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (glowing) {
            long elapsed = System.currentTimeMillis() - glowStartTime;
            if (elapsed > GLOW_DURATION) glowing = false;
        }

        Color bodyColor = glowing ? Color.YELLOW : color;
        g2d.setColor(bodyColor);

        // Централно тяло – дълго и тясно
        int[] xBody = {
                x + width / 2 - 4, x + width / 2 + 4,
                x + width / 2 + 8, x + width / 2 + 8,
                x + width / 2 + 5, x + width / 2 + 5,
                x + width / 2 - 5, x + width / 2 - 5,
                x + width / 2 - 8, x + width / 2 - 8,
                x + width / 2 - 4
        };
        int[] yBody = {
                y, y,
                y + 15, y + 40,
                y + 60, y + height,
                y + height, y + 60,
                y + 40, y + 15,
                y
        };
        g2d.fillPolygon(xBody, yBody, xBody.length);

        // Крила – с лек наклон назад
        int[] xLeftWing = {
                x + width / 2 - 8,
                x - 10,
                x + width / 2 - 8
        };
        int[] yLeftWing = {
                y + 30,
                y + 55,
                y + 75
        };

        int[] xRightWing = {
                x + width / 2 + 8,
                x + width + 10,
                x + width / 2 + 8
        };
        int[] yRightWing = {
                y + 30,
                y + 55,
                y + 75
        };

        g2d.fillPolygon(xLeftWing, yLeftWing, 3);
        g2d.fillPolygon(xRightWing, yRightWing, 3);

        // Двойна вертикална опашка
        g2d.fillRect(x + width / 2 - 6, y + height, 2, 8);
        g2d.fillRect(x + width / 2 + 4, y + height, 2, 8);
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (glowing) {
            long elapsed = System.currentTimeMillis() - glowStartTime;
            if (elapsed > GLOW_DURATION) glowing = false;
        }

        Color bodyColor = glowing ? Color.YELLOW : color;
        g2d.setColor(bodyColor);

        // Пропорционален мащаб: 80%
        double scale = 0.8;

        int cx = x + width / 2;
        int cy = y;

        // Централно тяло
        int[] xBody = {
                cx - 3, cx + 3,
                cx + 6, cx + 6,
                cx + 4, cx + 4,
                cx - 4, cx - 4,
                cx - 6, cx - 6,
                cx - 3
        };
        int[] yBody = {
                cy, cy,
                cy + 12, cy + 32,
                cy + 48, cy + (int)(height * scale),
                cy + (int)(height * scale), cy + 48,
                cy + 32, cy + 12,
                cy
        };
        g2d.fillPolygon(xBody, yBody, xBody.length);

        // Крила
        int[] xLeftWing = {
                cx - 6,
                (int)(x - 8 * scale),
                cx - 6
        };
        int[] yLeftWing = {
                cy + 24,
                cy + 44,
                cy + 60
        };

        int[] xRightWing = {
                cx + 6,
                (int)(x + width + 8 * scale),
                cx + 6
        };
        int[] yRightWing = {
                cy + 24,
                cy + 44,
                cy + 60
        };

        g2d.fillPolygon(xLeftWing, yLeftWing, 3);
        g2d.fillPolygon(xRightWing, yRightWing, 3);

        // Двойна вертикална опашка
        g2d.fillRect(cx - 5, cy + (int)(height * scale), 2, 6);
        g2d.fillRect(cx + 3, cy + (int)(height * scale), 2, 6);
    }
















    public void enableDoubleShoot() {
        doubleShoot = true;

        // Започни премигване
        glowing = true;
        glowStartTime = System.currentTimeMillis();
    }




    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getLeftWingX() {
        return x + 5;
    }

    public int getRightWingX() {
        return x + getWidth() - 5;
    }

    public void setGlowing(boolean value) {
        this.glowing = value;
        if (value) {
            glowStartTime = System.currentTimeMillis();
        }
    }

}
