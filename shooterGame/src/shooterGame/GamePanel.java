package shooterGame;

import bonus.BonusObject;
import entities.*;
import shooting.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {
    private Shooter shooter;
    private ShootingLevel1 shootingLevel1;
    private ShootingLevel2 shootingLevel2;
    private shooting.ShootingLevel3 shootingLevel3;
    private ShootingLevel4 shootingLevel4;
    private boolean isDoubleMode = false;
    private long doubleModeStartTime = 0;
    private final int DOUBLE_MODE_DURATION = 120000;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private Timer inputTimer;
    private List<Enemy1> enemy1List = new ArrayList<>();
    private List<Enemy2> enemy2List = new ArrayList<>();
    private List<Enemy3> enemy3List = new ArrayList<>();
    private List<Enemy4> enemy4List = new ArrayList<>();
    private List<Enemy5> enemy5List = new ArrayList<>();
    private Timer enemySpawnTimer;
    private int pointsCount = 0;
    private List<BonusObject> bonuses = new ArrayList<>();
    private List<Rocket> rockets = new ArrayList<>();
    private long lastRocketLaunchTime = 0;
    private static final int ROCKET_INTERVAL = 3000;//////////////////////////////////////////
    private long lastBulletTime = 0;
    private static final int BULLET_INTERVAL = 40; // милисекунди между изстрели
    private boolean isTripleMode = false;
    private long tripleModeStartTime = 0;
    private final int TRIPLE_MODE_DURATION = 120000;
    private boolean isQuadMode = false;
    private long quadModeStartTime = 0;
    private final int QUAD_MODE_DURATION = 120000;
    private ShootingLevel5 shootingLevel5;
    private boolean isPentaMode = false;
    private long pentaModeStartTime = 0;
    private final int PENTA_MODE_DURATION = 120000;
    private Drone leftDrone;
    private Drone rightDrone;
    private long lastDroneShootTime = 0;
    private final int DRONE_BULLET_INTERVAL = 300; // милисекунди








    public GamePanel() {
        setBackground(Color.BLACK);
        shooter = new Shooter(700, 800);
        shootingLevel1 = new ShootingLevel1(this);
        shootingLevel2 = new ShootingLevel2(this, shooter);
        shootingLevel3 = new ShootingLevel3(this, shooter);
        shootingLevel4 = new ShootingLevel4(this, shooter);
        shootingLevel5 = new ShootingLevel5(this, shooter);
        leftDrone = new Drone(shooter.getX() - 30, shooter.getY());
        rightDrone = new Drone(shooter.getX() + shooter.getWidth() + 20, shooter.getY());




        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        enemySpawnTimer = new Timer(2500, e -> {
            int enemyDiameter = 10;
            int minX = 450;
            int maxX = getWidth() - 450 - enemyDiameter;

            double chance = Math.random(); // между 0 и 1
            int x = (int)(Math.random() * (maxX - minX + 1)) + minX;

            if (chance < 0.01) { // 1%
                enemy5List.add(new Enemy5(x, 0));
                return;
            } else if (chance < 0.05) {
                enemy1List.add(new GoldenGift(x, 0, this));
                return;
            } else if (chance < 0.20) {
                enemy2List.add(new Enemy2(x, 0));
                return;
            } else if (chance < 0.25) {
                enemy3List.add(new Enemy3(x, 0));
                return;
            } else if (chance < 0.35) {
                enemy4List.add(new Enemy4(x, 0));
                return;
            }


            // 🧩 Обикновени Enemy (останалите 64%)
            int groupSize;
            double roll = Math.random();
            if (roll < 0.5) groupSize = 2;
            else if (roll < 0.8) groupSize = 3;
            else if (roll < 0.95) groupSize = 4;
            else groupSize = 5;

            int groupX = (int)(Math.random() * (maxX - minX + 1)) + minX;
            for (int i = 0; i < groupSize; i++) {
                enemy1List.add(new Enemy1(groupX, -i * (enemyDiameter + 10)));
            }
        });





        enemySpawnTimer.start();

        inputTimer = new Timer(16, e -> {
            int moveSpeed = 8;
            int panelWidth = getWidth();
            int newX = shooter.getX();

            if (pressedKeys.contains(KeyEvent.VK_A)) {
                newX -= moveSpeed;
                if (newX >= 0) shooter.setPosition(newX, shooter.getY());
            }

            if (pressedKeys.contains(KeyEvent.VK_D)) {
                newX += moveSpeed;
                if (newX + shooter.getWidth() <= panelWidth) shooter.setPosition(newX, shooter.getY());
            }

            // Стрелба
            long currentTime = System.currentTimeMillis();


            int centerX = shooter.getX() + shooter.getWidth() / 2;
            int droneOffsetX = 60;  // разстояние по хоризонтала
            int droneOffsetY = 50;  // малко по-надолу

            leftDrone.updatePosition(centerX - droneOffsetX, shooter.getY() + droneOffsetY);
            rightDrone.updatePosition(centerX + droneOffsetX, shooter.getY() + droneOffsetY);





            if ((pressedKeys.contains(KeyEvent.VK_PLUS) || pressedKeys.contains(KeyEvent.VK_ADD))
                    && currentTime - lastBulletTime >= BULLET_INTERVAL) {

                int shooterCenterX = shooter.getX() + shooter.getWidth() / 2;
                int shooterBarrelY = shooter.getY() - 20;

                if (isPentaMode) {
                    shootingLevel5.shoot(shooterCenterX, shooterBarrelY);
                }
                if (isQuadMode) {
                    shootingLevel4.shoot(shooterCenterX, shooterBarrelY);
                } else if (isTripleMode) {
                    shootingLevel3.shoot(shooterCenterX, shooterBarrelY);
                } else if (isDoubleMode) {
                    shootingLevel2.shoot(shooterCenterX, shooterBarrelY);
                } else {
                    shootingLevel1.shoot(shooterCenterX, shooterBarrelY);
                }




                lastBulletTime = currentTime; // обнови времето на последния изстрел
            }

            if (currentTime - lastDroneShootTime >= DRONE_BULLET_INTERVAL) {
                leftDrone.shoot();
                rightDrone.shoot();
                lastDroneShootTime = currentTime;
            }




            // Обновяване на врагове (Enemy)
            for (int i = 0; i < enemy1List.size(); i++) {
                Enemy1 enemy = enemy1List.get(i);
                enemy.update();

                List<Bullet> bullets = isPentaMode
                        ? shootingLevel5.getBullets()
                        : isQuadMode
                        ? shootingLevel4.getBullets()
                        : isTripleMode
                        ? shootingLevel3.getBullets()
                        : isDoubleMode
                        ? shootingLevel2.getBullets()
                        : shootingLevel1.getBullets();




                for (Bullet bullet : bullets) {
                    if (enemy.getBounds().intersects(bullet.getBounds()) && !enemy.isExploding()) {
                        enemy.explode();
                        bullet.setInvisible();
                    }
                }

                if (enemy.isFaded()) {
                    enemy1List.remove(i);
                    pointsCount++;
                    i--;
                }
            }

            // Обновяване на врагове от ниво 2 (Enemy2)
            for (int i = 0; i < enemy2List.size(); i++) {
                Enemy2 enemy2 = enemy2List.get(i);
                enemy2.update();

                List<Bullet> bullets = isPentaMode
                        ? shootingLevel5.getBullets()
                        : isQuadMode
                        ? shootingLevel4.getBullets()
                        : isTripleMode
                        ? shootingLevel3.getBullets()
                        : isDoubleMode
                        ? shootingLevel2.getBullets()
                        : shootingLevel1.getBullets();


                for (Bullet bullet : bullets) {
                    if (enemy2.getBounds().intersects(bullet.getBounds()) && !enemy2.isExploding()) {
                        enemy2.takeHit(5); // намалява живот
                        bullet.setInvisible();
                    }
                }

                if (enemy2.isFaded()) {
                    enemy2List.remove(i);
                    pointsCount += 2; // награда за труден враг
                    i--;
                }
            }

            for (int i = 0; i < enemy3List.size(); i++) {
                Enemy3 enemy3 = enemy3List.get(i);
                enemy3.update();

                List<Bullet> bullets = isPentaMode
                        ? shootingLevel5.getBullets()
                        : isQuadMode
                        ? shootingLevel4.getBullets()
                        : isTripleMode
                        ? shootingLevel3.getBullets()
                        : isDoubleMode
                        ? shootingLevel2.getBullets()
                        : shootingLevel1.getBullets();

                for (Bullet bullet : bullets) {
                    if (enemy3.getBounds().intersects(bullet.getBounds()) && !enemy3.isExploding()) {
                        enemy3.takeHit(5);
                        bullet.setInvisible();
                    }
                }

                if (enemy3.isFaded()) {
                    enemy3List.remove(i);
                    pointsCount += 15; // повече точки за Enemy3
                    i--;
                }
            }

            // Обновяване на врагове от ниво 4 (Enemy4)
            for (int i = 0; i < enemy4List.size(); i++) {
                Enemy4 enemy4 = enemy4List.get(i);
                enemy4.update();

                List<Bullet> bullets = isPentaMode
                        ? shootingLevel5.getBullets()
                        : isQuadMode
                        ? shootingLevel4.getBullets()
                        : isTripleMode
                        ? shootingLevel3.getBullets()
                        : isDoubleMode
                        ? shootingLevel2.getBullets()
                        : shootingLevel1.getBullets();

                for (Bullet bullet : bullets) {
                    if (enemy4.getBounds().intersects(bullet.getBounds()) && !enemy4.isExploding()) {
                        enemy4.takeHit(5);
                        bullet.setInvisible();
                    }
                }

                if (enemy4.isFaded()) {
                    enemy4List.remove(i);
                    pointsCount += 20; // по-силен враг -> повече точки
                    i--;
                }
            }

            // Обновяване на босовете
            for (int i = 0; i < enemy5List.size(); i++) {
                Enemy5 boss = enemy5List.get(i);
                boss.update();

                List<Bullet> bullets = isPentaMode
                        ? shootingLevel5.getBullets()
                        : isQuadMode
                        ? shootingLevel4.getBullets()
                        : isTripleMode
                        ? shootingLevel3.getBullets()
                        : isDoubleMode
                        ? shootingLevel2.getBullets()
                        : shootingLevel1.getBullets();

                for (Bullet bullet : bullets) {
                    if (boss.getBounds().intersects(bullet.getBounds()) && !boss.isExploding()) {
                        boss.takeHit(5);
                        bullet.setInvisible();
                    }
                }

                if (boss.isFaded()) {
                    enemy5List.remove(i);
                    pointsCount += 30; // награда за бос
                    i--;
                }
            }

            // Обновяване на бонуси
            for (int i = 0; i < bonuses.size(); i++) {
                BonusObject bonus = bonuses.get(i);
                bonus.update();

                if (bonus.getBounds().intersects(shooter.getBounds())) {
                    if (isQuadMode && !isPentaMode) {
                        isPentaMode = true;
                        pentaModeStartTime = System.currentTimeMillis();
                    } else if (isTripleMode && !isQuadMode) {
                        isQuadMode = true;
                        quadModeStartTime = System.currentTimeMillis();
                    } else if (isDoubleMode && !isTripleMode) {
                        isTripleMode = true;
                        tripleModeStartTime = System.currentTimeMillis();
                    } else if (!isDoubleMode) {
                        isDoubleMode = true;
                        doubleModeStartTime = System.currentTimeMillis();
                    }

                    shooter.setGlowing(true);
                    bonuses.remove(i);
                    i--;
                    continue;
                }

                if (!bonus.isVisible()) {
                    bonuses.remove(i);
                    i--;
                }
            }

            // 📦 Събиране на всички патрони от двата дрона
            List<Bullet> droneBullets = new ArrayList<>();
            droneBullets.addAll(leftDrone.getBullets());
            droneBullets.addAll(rightDrone.getBullets());

            // 🎯 Удря врагове тип Enemy
            for (int i = 0; i < enemy1List.size(); i++) {
                Enemy1 enemy = enemy1List.get(i);
                for (Bullet bullet : droneBullets) {
                    if (enemy.getBounds().intersects(bullet.getBounds()) && !enemy.isExploding()) {
                        bullet.setInvisible();
                        enemy.explode(); // няма HP
                    }
                }
            }

            // 🎯 Удря Enemy2
            for (Enemy2 e2 : enemy2List) {
                for (Bullet bullet : droneBullets) {
                    if (e2.getBounds().intersects(bullet.getBounds()) && !e2.isExploding()) {
                        bullet.setInvisible();
                        e2.takeHit(1);
                    }
                }
            }

            // 🎯 Удря Enemy3
            for (Enemy3 e3 : enemy3List) {
                for (Bullet bullet : droneBullets) {
                    if (e3.getBounds().intersects(bullet.getBounds()) && !e3.isExploding()) {
                        bullet.setInvisible();
                        e3.takeHit(1);
                    }
                }
            }

            // 🎯 Удря Enemy4
            for (Enemy4 e4 : enemy4List) {
                for (Bullet bullet : droneBullets) {
                    if (e4.getBounds().intersects(bullet.getBounds()) && !e4.isExploding()) {
                        bullet.setInvisible();
                        e4.takeHit(1);
                    }
                }
            }

            // 🎯 Удря босове
            for (Enemy5 boss : enemy5List) {
                for (Bullet bullet : droneBullets) {
                    if (boss.getBounds().intersects(bullet.getBounds()) && !boss.isExploding()) {
                        bullet.setInvisible();
                        boss.takeHit(1);
                    }
                }
            }



            // Изключване на временните режими след изтичане на времето
            if (isPentaMode && System.currentTimeMillis() - pentaModeStartTime > PENTA_MODE_DURATION) {
                isPentaMode = false;
            }
            if (isQuadMode && System.currentTimeMillis() - quadModeStartTime > QUAD_MODE_DURATION) {
                isQuadMode = false;
            }
            if (isTripleMode && System.currentTimeMillis() - tripleModeStartTime > TRIPLE_MODE_DURATION) {
                isTripleMode = false;
            }
            if (isDoubleMode && System.currentTimeMillis() - doubleModeStartTime > DOUBLE_MODE_DURATION) {
                isDoubleMode = false;
            }

            // Обновяване на патрони
            if (isPentaMode) {
                shootingLevel5.update();
            } else if (isQuadMode) {
                shootingLevel4.update();
            } else if (isTripleMode) {
                shootingLevel3.update();
            } else if (isDoubleMode) {
                shootingLevel2.update();
            } else {
                shootingLevel1.updateBullets();
            }




            // 🚀 Автоматично изстрелване на ракети
            if (currentTime - lastRocketLaunchTime >= ROCKET_INTERVAL) {
                List<Enemy1> allTargets = new ArrayList<>();
                allTargets.addAll(enemy5List);
                allTargets.addAll(enemy4List);
                allTargets.addAll(enemy3List);
                allTargets.addAll(enemy2List);
                allTargets.addAll(enemy1List);

                // филтрирай само живите цели
                allTargets.removeIf(enemy -> enemy.isExploding());


                for (int i = 0; i < 8 && !allTargets.isEmpty(); i++) {/////////////////////////////
                    Enemy1 closest = null;
                    double minDist = Double.MAX_VALUE;

                    for (Enemy1 enemy : allTargets) {
                        double dx = shooter.getX() - enemy.getX();
                        double dy = shooter.getY() - enemy.getY();
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        if (dist < minDist) {
                            minDist = dist;
                            closest = enemy;
                        }
                    }


                    if (closest != null) {
                        int offsetX = switch (i) {
                            case 0 -> shooter.getLeftWingX();
                            case 1 -> shooter.getRightWingX();
                            case 2 -> shooter.getX(); // център
                            case 3 -> shooter.getX() + shooter.getWidth(); // дясна рамка
                            default -> shooter.getX();
                        };

                        rockets.add(new Rocket(offsetX, shooter.getY(), closest));
                        allTargets.remove(closest); // всяка ракета има различна цел
                    }
                }

                lastRocketLaunchTime = currentTime;
            }


            // Обновяване на ракети
            for (int i = 0; i < rockets.size(); i++) {
                Rocket r = rockets.get(i);
                r.update();

                if (!r.isVisible()) {
                    rockets.remove(i);
                    i--;
                    continue;
                }

                if (!r.getTarget().isExploding() && r.getBounds().intersects(r.getTarget().getBounds())) {
                    if (!r.hasHit()) {
                        r.setHasHit(true);
                        r.explode();

                        if (r.getTarget() instanceof Enemy5 boss) {
                            boss.takeHit(5);
                        } else if (r.getTarget() instanceof Enemy4 e4) {
                            e4.takeHit(5);
                        } else if (r.getTarget() instanceof Enemy3 e3) {
                            e3.takeHit(5);
                        } else if (r.getTarget() instanceof Enemy2 e2) {
                            e2.takeHit(5);
                        } else if (r.getTarget() instanceof Enemy1 e1) {
                            e1.explode();
                        }
                    }
                }
            }

            leftDrone.updateBullets();
            rightDrone.updateBullets();



            repaint();
        });


        inputTimer.start();

    }



    public void spawnBonus(BonusObject b) {
        bonuses.add(b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        shooter.draw(g2d);

        leftDrone.draw(g2d);
        rightDrone.draw(g2d);


        if (isPentaMode) {
            shootingLevel5.draw(g2d);
        } else if (isQuadMode) {
            shootingLevel4.draw(g2d);
        } else if (isTripleMode) {
            shootingLevel3.draw(g2d);
        } else if (isDoubleMode) {
            shootingLevel2.draw(g2d);
        } else {
            shootingLevel1.draw(g2d);
        }




        for (Enemy1 enemy : enemy1List) enemy.draw(g2d);
        for (Enemy2 e2 : enemy2List) e2.draw(g2d);
        for (Enemy3 e3 : enemy3List) e3.draw(g2d);
        for (Enemy4 e4 : enemy4List) e4.draw(g2d);
        for (Enemy5 boss : enemy5List) boss.draw(g2d);



        for (BonusObject bonus : bonuses) bonus.draw(g2d);

        for (Rocket rocket : rockets) {
            rocket.draw(g2d);
        }

        // Брояч за убийства
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreText = "Points: " + pointsCount;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(scoreText);
        g2d.drawString(scoreText, getWidth() - textWidth - 20, 30);

        // Индикатор за стрелба
        if (isPentaMode) {
            g2d.setColor(Color.PINK); // или друг отличителен цвят
            g2d.drawString("PENTA SHOOT: ON", 20, 30);
        } else if (isQuadMode) {
            g2d.setColor(Color.MAGENTA);
            g2d.drawString("QUAD SHOOT: ON", 20, 30);
        } else if (isTripleMode) {
            g2d.setColor(Color.CYAN);
            g2d.drawString("TRIPLE SHOOT: ON", 20, 30);
        } else if (isDoubleMode) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("DOUBLE SHOOT: ON", 20, 30);
        }



    }
}
