import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        Icon icon = new ImageIcon("kappa.png");
        JButton button = new JButton(icon);
        button.setBounds(200, 200, 100, 100);

        frame.add(button);
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setVisible(true);

        /*
         * button.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * int rand1 = (int)(250*Math.random());
         * int rand2 = (int)(250*Math.random());
         * button.setBounds(rand1, rand2, 100, 100);
         * String label = button.getText();
         * button.setText(label + "A");
         * label = button.getText();
         * System.out.println("Current button label is: " + label);
         * }
         * });
         */

        /*
         * Timer timer = new Timer();
         * int begin = 1000;
         * int timeInt = 10 * 100;
         * timer.scheduleAtFixedRate(new TimerTask(){
         * public void run(){
         * button.setBounds((int)(250*Math.random()), (int)(250*Math.random()), 100,
         * 100);
         * }
         * }, begin, timeInt);
         */

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.setBounds((int) (250 * Math.random()), (int) (250 * Math.random()), 100, 100);
            }
        });
    }

    public static void createFrame() {
    }
}
