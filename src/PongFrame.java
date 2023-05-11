import javax.swing.*;

public class PongFrame extends JFrame {

    PongFrame(){
        this.add(new PongPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}