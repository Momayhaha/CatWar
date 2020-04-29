import javax.swing.*;
import java.awt.*;

public class CatWar extends JFrame{

    public CatWar(){

        initCatWar();
    }
    private void initCatWar(){
        add(new Games());
        setTitle("Cat War!!");
        setSize(1024,760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.BLACK);
    }

    public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable(){

        @Override
        public void run(){
           CatWar ex = new CatWar();
           ex.setVisible(true);
        }
      });
    }
}
