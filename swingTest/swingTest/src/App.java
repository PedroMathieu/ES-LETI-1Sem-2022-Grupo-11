import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.Timer;
//import java.util.TimerTask;

public class App {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Test Frame");

        
        ImageIcon icon = new ImageIcon("src/kappa.png");
        JLabel testLabel = new JLabel("", icon, JLabel.CENTER);
        frame.add(testLabel);

        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
