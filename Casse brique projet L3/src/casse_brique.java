import javax.swing.*;
import java.awt.*;

public class casse_brique extends JFrame {

    public casse_brique() {
        
        initUI();
    }
    
    private void initUI() {

        add(new fenetre());
        setTitle("casse_brique");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {

            var game = new casse_brique();
            game.setVisible(true);
        });
    }
}
