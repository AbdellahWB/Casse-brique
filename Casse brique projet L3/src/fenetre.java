import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class fenetre extends JPanel {

    private Timer timer;
    private String message = "Game Over";
    private Balle balle;
    private raquette raquette;
    private brique[] briques;
    private boolean inGame = true;

    public fenetre() {
        setBackground(Color.darkGray);
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(resolution.WIDTH, resolution.HEIGHT));

        gameInit();
    }

    private void gameInit() {

        briques = new brique[resolution.N_OF_BRICKS];

        balle = new Balle();
        raquette = new raquette();

        int k = 0;

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 6; j++) {

                briques[k] = new brique(j * 40 + 30, i * 10 + 50);
                k++;
            }
        }

        timer = new Timer(resolution.PERIOD, new GameCycle());
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (inGame) {

            drawObjects(g2d);
        } else {

            gameFinished(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics2D g2d) {

        g2d.drawImage(balle.getImage(), balle.getX(), balle.getY(),
                balle.getImageWidth(), balle.getImageHeight(), this);
        g2d.drawImage(raquette.getImage(), raquette.getX(), raquette.getY(),
                raquette.getImageWidth(), raquette.getImageHeight(), this);

        for (int i = 0; i < resolution.N_OF_BRICKS; i++) {

            if (!briques[i].isDestroyed()) {

                g2d.drawImage(briques[i].getImage(), briques[i].getX(),
                        briques[i].getY(), briques[i].getImageWidth(),
                        briques[i].getImageHeight(), this);
            }
        }
    }

    private void gameFinished(Graphics2D g2d) {

        var font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(message,
                (resolution.WIDTH - fontMetrics.stringWidth(message)) / 2,
                resolution.WIDTH / 2);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            raquette.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            raquette.keyPressed(e);
        }
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private void doGameCycle() {

        balle.move();
        raquette.move();
        checkCollision();
        repaint();
    }

    private void stopGame() {

        inGame = false;
        timer.stop();
    }

    private void checkCollision() {

        if (balle.getRect().getMaxY() > resolution.BOTTOM_EDGE) {

            stopGame();
        }

        for (int i = 0, j = 0; i < resolution.N_OF_BRICKS; i++) {

            if (briques[i].isDestroyed()) {

                j++;
            }

            if (j == resolution.N_OF_BRICKS) {

                message = "Victory";
                stopGame();
            }
        }

        if ((balle.getRect()).intersects(raquette.getRect())) {

            int paddleLPos = (int) raquette.getRect().getMinX();
            int ballLPos = (int) balle.getRect().getMinX();

            int first = paddleLPos + 8;
            int second = paddleLPos + 16;
            int third = paddleLPos + 24;
            int fourth = paddleLPos + 32;

            if (ballLPos < first) {

                balle.setXDir(-1);
                balle.setYDir(-1);
            }

            if (ballLPos >= first && ballLPos < second) {

                balle.setXDir(-1);
                balle.setYDir(-1 * balle.getYDir());
            }

            if (ballLPos >= second && ballLPos < third) {

                balle.setXDir(0);
                balle.setYDir(-1);
            }

            if (ballLPos >= third && ballLPos < fourth) {

                balle.setXDir(1);
                balle.setYDir(-1 * balle.getYDir());
            }

            if (ballLPos > fourth) {

                balle.setXDir(1);
                balle.setYDir(-1);
            }
        }

        for (int i = 0; i < resolution.N_OF_BRICKS; i++) {

            if ((balle.getRect()).intersects(briques[i].getRect())) {

                int ballLeft = (int) balle.getRect().getMinX();
                int ballHeight = (int) balle.getRect().getHeight();
                int ballWidth = (int) balle.getRect().getWidth();
                int ballTop = (int) balle.getRect().getMinY();

                var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                var pointLeft = new Point(ballLeft - 1, ballTop);
                var pointTop = new Point(ballLeft, ballTop - 1);
                var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                if (!briques[i].isDestroyed()) {

                    if (briques[i].getRect().contains(pointRight)) {

                        balle.setXDir(-1);
                    } else if (briques[i].getRect().contains(pointLeft)) {

                        balle.setXDir(1);
                    }

                    if (briques[i].getRect().contains(pointTop)) {

                        balle.setYDir(1);
                    } else if (briques[i].getRect().contains(pointBottom)) {

                        balle.setYDir(-1);
                    }

                    briques[i].setDestroyed(true);
                }
            }
        }
    }
}
