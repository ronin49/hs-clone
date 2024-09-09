import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class MainField extends JComponent implements KeyListener, ActionListener {

    private Image field = new ImageIcon("hs-clone/field.png").getImage();

    public void paint(Graphics g){
        Graphics2D gr = (Graphics2D)g;
        gr.drawImage(field, 0,0,1550,800,null);
    }
    public void drawField(){
        MainField mainField = new MainField();
        JFrame f = new JFrame("Hearthstone");
        f.setSize(550,550);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addKeyListener(mainField);
        f.add(new MainField());
        f.add(mainField);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
