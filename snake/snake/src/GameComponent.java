import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GameComponent extends JButton {
    public static final int COMPONENTDIMENSION = 50;
    private int posx;
    private int posy;

    public GameComponent(){
        super("");
    }

    public int getPosX(){
        return posx;
    }

    public int getPosY(){
        return posy;
    }

    public void setPosX(int x){
        posx = x;
    }

    public void setPosY(int y){
        posy = y;
    }
}
