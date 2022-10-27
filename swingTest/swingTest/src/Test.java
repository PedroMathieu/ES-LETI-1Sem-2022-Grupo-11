import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.Timer;
//import java.util.TimerTask;

public class Test {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("My Application");
        JButton button = new JButton("Click me!");
        JButton button2 = new JButton("Button 2!");
        JTextField textField = new JTextField();

        button.setBounds(290, 350, 100, 100);
        button2.setBounds(290, 450, 100, 100);
        textField.setBounds(0, 200, 700, 30);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().equals("")) {
                    textField.setText("I've been updated!");
                } else if (textField.getText().equals((String) ("I've been updated!"))) {
                    textField.setText("I've been updated... Again...");
                } else {
                    String again = "And again...";
                    textField.setText(textField.getText() + again);
                }
            }
        });

        frame.add(button);
        frame.add(textField);

        frame.setSize(700, 700);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
