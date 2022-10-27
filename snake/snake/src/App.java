import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class App {
    public static void main(String[] args) throws Exception {
        createFrame();
    }

    public static void createFrame(){
        JFrame frame = new JFrame("Snake");
        JPanel panel = new JPanel();
        
        frame.setSize(GameComponent.COMPONENTDIMENSION*10+10, GameComponent.COMPONENTDIMENSION*12+10);
        frame.setVisible(true);
    }
}
