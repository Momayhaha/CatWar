import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.*;
import java.net.URL;
import java.io.*;

class Games extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private final int DELAY = 33;
    private Clip clip;
    private Clip intro;
    ImageGames game;
    int move = 0;       //move of cat
    int gstart = 0;     //game start
    int life = 10;
    int score = 0;
    int crash = 0;      //variable mouse affected crash
    int choicem = 0;    // choice mouse
    int livem = 0;      //if the mouse is active
    int mouseX = 0;     // coordinate x mouse
    int mouseY = 0;     // coordinate y mouse
    int timeintro=0;
    int mxcatch = 0;      //coordinated x mouse catch
    int mycatch = 0;      //coordinated y mouse catch


    public Games(){
      initGames();
    }
    private void initGames(){

      addKeyListener(this);         // for keyboard events
      setFocusable(true);           // recognizes obtaining focus from keyboard
      setBackground(Color.BLACK);
      game = new ImageGames();
      timer = new Timer(DELAY, this);
      timer.start();

    }

    // JPanel method
    public void paintComponent(Graphics g){     //This method, which in turn calls drawing
      super.paintComponent(g);                  // will be called continuously by actionPerformed

      draw(g);
      Messages(g);
      if (life<1) gameOver(g);
      Toolkit.getDefaultToolkit().sync();

    }

    // Call to graphic methods:
    private void draw (Graphics g){
      Graphics2D g2 = (Graphics2D) g;
      if(gstart == 1){
          g2.drawImage(game.getBG(), 5, 5, this);     //positioned the background
          g2.drawImage(game.getCAT(), game.getX(), game.getY(),this);   //image cat
          if (crash == 1) g2.drawImage(game.getBLOOD(),mxcatch,mycatch,this);  //image blood
          if (livem == 0) cremouse();
          if (choicem==0 && livem > 0 && livem < 11) g2.drawImage(game.getMOUSE(),mouseX,mouseY,this);
          if (choicem==1 && livem > 0 && livem < 11) g2.drawImage(game.getMOUSE(),mouseX,mouseY,this);
          if (choicem==2 && livem > 0 && livem < 11) g2.drawImage(game.getMOUSE(),mouseX,mouseY,this);    //image mouse
          if (choicem==3 && livem > 0 && livem < 11) g2.drawImage(game.getMOUSE(),mouseX,mouseY,this);
          if (choicem==4 && livem > 0 && livem < 11) g2.drawImage(game.getMOUSE(),mouseX,mouseY,this);

          g2.drawImage(game.getFOOD(), 440, 300,this);    //image food
      }
      else if (gstart == 0){
        g2.drawImage(game.getLOGO(),290,250,this);    //image logo
      }

    }

    public void Messages(Graphics g){
      Graphics2D g2 = (Graphics2D) g;
      Font font = new Font("Serif", Font.BOLD, 20);
      g2.setFont(font);
      g2.setColor(Color.GREEN);
      if (gstart == 1){
          g2.drawString("Score : ",100,130);
          String Score2 = Integer.toString(score);
          g2.drawString(Score2, 175, 130);
          g2.drawString("Cat: Don't let mouses eat food!", 100, 100);
          g2.drawString("Life!", 100, 600);
          int heart = life;
          while(heart > 0){
            g2.drawString("X", (150+(heart*15)), 600);
            heart -= 1;
          }
      }
    }
    public void gameOver(Graphics g){
      Graphics2D g2 = (Graphics2D) g;
      Font font = new Font("Helvetica", Font.BOLD, 36);
      g2.setColor(Color.CYAN);
      g2.setFont(font);
      g2.drawString("G A M E  O V E R", 375, 460);
      g2.drawString("SCORE :", 400, 520);
      String endScore = Integer.toString(score);
      g2.drawString(endScore, 570, 520);
    }
    // Call to game management methods:
    @Override
    public void actionPerformed(ActionEvent e){
      if (life > 0){
        if(gstart == 0 && timeintro < 1){
         timeintro += 1;
         playIntro();
       }
        if(gstart == 1){
          moveCat();    //cat movement routine
          moveMouse();  //routine for mouse movement
          crashFood();  // check mouse and food
          crashCat();   // check mouse and cat
        }
      }
        repaint();
    }

    // Receiving keys
    public void keyPressed(KeyEvent ke){
      int key = ke.getKeyCode();
      if (key == KeyEvent.VK_LEFT){
          move = 1;
      }
      if (key == KeyEvent.VK_RIGHT){
          move = 2;
      }
      if (key == KeyEvent.VK_UP){
          move = 3;
      }
      if (key == KeyEvent.VK_DOWN){
          move = 4;
      }
      if (key == KeyEvent.VK_SPACE){
         gstart = 1;
         if (intro.isRunning()) intro.stop();
      }
    }
    public void keyReleased(KeyEvent ke){

    }
    public void keyTyped(KeyEvent ke){
/*      if (gstart == 0) {
        gstart = 1;
        if (intro.isRunning()) intro.stop();
      }*/
    }
    public void moveCat(){
      if (move==1) game.moveLeft();
      else if (move==2)game.moveRight();
      else if (move==3) game.moveUp();
      else if (move==4)game.moveDown();
    }
    //create mouse
    Random rand;
    public void cremouse(){
      rand = new Random();
      choicem = rand.nextInt(5);
      choicem += 1;
      //Lateral positioning of the mouse
      if (choicem == 1 || choicem == 3) mouseX = rand.nextInt(1000);
      if (choicem == 1) mouseY = 1;

      if (choicem == 2) mouseX = 1000;
      if (choicem == 2 || choicem == 4) mouseY = rand.nextInt(700);

      if (choicem == 3) mouseY = 700;
      if (choicem == 4) mouseX = 1;

      livem = 1;
    }

    public void moveMouse(){
      if (livem > 0 && mouseX < 480) mouseX+=9;
      if (livem > 0 && mouseX > 500) mouseX-=9;
      if (livem > 0 && mouseY < 340) mouseY+=9;
      if (livem > 0 && mouseY > 340) mouseY-=9;
      livem += 1;
      if (livem == 11) livem = 1; crash = 0;

    }
    // A method that creates a rectangle around the sprite.
    public Rectangle getBoundmouse(){
      game.getImagesizemouse();
      return new Rectangle(mouseX, mouseY, game.whidtmouse, game.heightmouse);
    }

    // check mouse intersection with food
    public void crashFood(){
      Rectangle r1 = getBoundmouse();
      Rectangle r2 = game.getBoundfood();
      if (livem == 1){
        if(r1.intersects(r2)) livem = 0;
        if(r1.intersects(r2)) life -= 1;
      }
    }
    // check mouse intersection with cat
    public void crashCat(){
      Rectangle r1 = getBoundmouse();
      Rectangle r2 = game.getBoundcat();
      if (crash == 0 && r1.intersects(r2) && livem >1) crash = 1; mxcatch= mouseX; mycatch = mouseY;
      if (r1.intersects(r2)) livem = 0;
      if (r1.intersects(r2)) score += 10;
      if (r1.intersects(r2)) playSound();
    }

    public void playSound() {
  		try {

//  			File file = new File ("Sound/bite.wav");
        URL url = this.getClass().getClassLoader().getResource("res/Sound/bite.wav");
  			AudioInputStream in = AudioSystem.getAudioInputStream(url);
  			clip = AudioSystem.getClip();
  			clip.open(in);
  			if (clip.isRunning())
  				clip.stop();   // Stop the player if it is still running
  			clip.setFramePosition(0); // rewind to the beginning
  			clip.start();
  			}catch (IOException e) {
  			e.printStackTrace();
  			}catch (LineUnavailableException e) {
  			e.printStackTrace();
  			}catch (UnsupportedAudioFileException e) {
  			e.printStackTrace();
  		}
  	}
    public void playIntro() {
      try{

//        File file = new File ("Sound/intro.wav");
        URL url = this.getClass().getClassLoader().getResource("res/Sound/intro.wav");
        AudioInputStream in = AudioSystem.getAudioInputStream(url);
        intro = AudioSystem.getClip();
        intro.open(in);
        intro.start();
        }catch (IOException e) {
        e.printStackTrace();
        }catch (LineUnavailableException e) {
        e.printStackTrace();
        }catch (UnsupportedAudioFileException e) {
        e.printStackTrace();
      }
    }


//----------------------------------------------------------------

class ImageGames{
// Image variables.
  Image bg;
  Image cat;
  Image mouse;
  Image blood;
  Image food;
  Image logo;
  Image mouse2;

// cat coordinate variables.
  int x = 480;
  int y = 530;

// variables of the squares of the images
  int widtfood;
  int heightfood;
  int whidtcat;
  int heightcat;
  int whidtmouse;
  int heightmouse;

  ImageGames(){

    initImageGames();

  }

  public void initImageGames(){
    //upload pictures for the game.
      ImageIcon BG = new ImageIcon("res/Images/BG.png");
      ImageIcon CAT = new ImageIcon("res/Images/CAT.png");
      ImageIcon MOUSE = new ImageIcon("res/Images/MOUSE.png");
      ImageIcon BLOOD = new ImageIcon("res/Images/BLOOD.png");
      ImageIcon FOOD = new ImageIcon("res/Images/FOOD.png");
      ImageIcon LOGO = new ImageIcon("res/Images/LOGO.png");

    bg = BG.getImage();
    cat = CAT.getImage();
    mouse = MOUSE.getImage();
    blood = BLOOD.getImage();
    food = FOOD.getImage();
    logo = LOGO.getImage();

  }

  //METHODS THAT RETURN THE IMAGES:
  public Image getBG(){
    return bg;
  }
  public Image getCAT(){
    return cat;
  }
  public Image getMOUSE(){
    return mouse;
  }
  public Image getBLOOD(){
    return blood;
  }
  public Image getFOOD(){
    return food;
  }
  public  Image getLOGO(){
    return logo;
  }

// METHODS THAT RETURN COORDINATE;
// coordinate methods cat:
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
  //CAT MOVEMENT METHODS
  public void moveUp(){
    if(y>70)
      y -= 10;
  }
  public void moveDown(){
    if(y<580)
      y += 10;
  }
  public void moveLeft(){
    if(x>70)
      x -= 10;
  }
  public void moveRight(){
    if(x<880)
      x += 10;
  }

  //square extraction from cat
    public void getImagesizecat(){
      whidtcat = cat.getWidth(null);
      heightcat = cat.getHeight(null);
    }

  // A method that creates a rectangle around the sprite.
    public Rectangle getBoundcat(){
      getImagesizecat();
      return new Rectangle(x, y, whidtcat, heightcat);
    }

// square mouse extraction
    public void getImagesizemouse(){
      whidtmouse = mouse.getWidth(null);
      heightmouse = mouse.getHeight(null);
    }

// square extraction from food
    public void getImagesizefood(){
      widtfood = food.getWidth(null);
      heightfood = food.getHeight(null);
    }

// A method that creates a rectangle around the sprite.
    public Rectangle getBoundfood(){
      getImagesizefood();
      return new Rectangle(480, 330, widtfood, heightfood);
    }
  }
}
