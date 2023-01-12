import javax.swing.*;
import java.awt.event.KeyEvent;

public class raquette extends image {

    private int dx;

    public raquette() {
        
        initPaddle();        
    }
    
    private void initPaddle() {

        loadImage();
        getImageDimensions();

        resetState();
    }
    
    private void loadImage() {
        
        var ii = new ImageIcon("Casse brique projet L3/src/ressources/paddle.png");
        image = ii.getImage();        
    }    

    void move() {

        x += dx;

        if (x <= 0) {

            x = 0;
        }

        if (x >= resolution.WIDTH - imageWidth) {

            x = resolution.WIDTH - imageWidth;
        }
    }

    void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -3;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 3;
        }
    }

    void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }

    private void resetState() {

        x = resolution.INIT_PADDLE_X;
        y = resolution.INIT_PADDLE_Y;
    }
}
