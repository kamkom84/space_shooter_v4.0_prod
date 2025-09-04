package bonus;

import java.awt.*;

public class BonusObject {
    private int x, y;
    private final int size = 20;
    private final int speed = 1;
    private boolean visible = true;

    private float alpha = 1.0f;
    private boolean fadingOut = true;
    private int blinkCounter = 0;

    public BonusObject(int x, int y) {
        this.x = x;
        this.y = y;
    }




    public void update() {
        y += speed;

        // Променя alpha за блинк
        blinkCounter++;
        if (blinkCounter % 5 == 0) { // на всеки 5 update цикъла
            if (fadingOut) {
                alpha -= 0.1f;
                if (alpha <= 0.3f) {
                    alpha = 0.3f;
                    fadingOut = false;
                }
            } else {
                alpha += 0.1f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    fadingOut = true;
                }
            }
        }

        // Извън екрана
        if (y > 1080) {
            visible = false;
        }
    }





    public void draw(Graphics g) {
        if (!visible) return;

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Централно жълто кръгче
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x - size / 2, y - size / 2, size, size);

        // Текст "+1" вътре
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "+1";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2d.drawString(text, x - textWidth / 2, y + textHeight / 2 - 2);

        // Крила – по 3 пера от двете страни, с наклон
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.YELLOW);

        for (int i = 0; i < 3; i++) {
            int offset = 4 + i * 4;

            // Ляво крило (линии под наклон ↖)
            g2d.drawLine(x - size / 2, y, x - size / 2 - 10, y - offset);
            // Дясно крило (линии под наклон ↗)
            g2d.drawLine(x + size / 2, y, x + size / 2 + 10, y - offset);
        }

        g2d.setComposite(originalComposite);
    }



    public boolean isVisible() {
        return visible;
    }




    public Rectangle getBounds() {
        return new Rectangle(x - size / 2, y - size / 2, size, size);
    }





}
