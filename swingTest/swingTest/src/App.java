import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        JButton button = new JButton("TestButton");
        button.setBounds(200,200,100,100);

        frame.add(button);
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
