import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = (int)Math.pow(SIZE/DOT_SIZE, 2);

    private Image dot;
    private Image apple;

    private int appleX;
    private int appleY;
    //Координаты змейки и ее размер
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int snakeSize;

    private Timer timer;
    private int record;
    //Направления движения змейки
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = true;

    private boolean inGame = true;

    public GameField(){
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    private void initGame(){
        snakeSize = 3;
        record = 0;
        for(int i = 0; i < snakeSize; i++){
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    private void createApple(){
        appleX = new Random().nextInt(19)*DOT_SIZE;
        appleY = new Random().nextInt(19)*DOT_SIZE;
    }

    private void loadImages(){
        ImageIcon ii_apple = new ImageIcon("apple.png");
        ImageIcon ii_dot = new ImageIcon("dot.png");
        apple = ii_apple.getImage();
        dot = ii_dot.getImage();

    }

    private void move(){
        for(int i = snakeSize; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
    }

    private void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            record++;
            snakeSize++;
            createApple();
        }
    }

    private void checkCollisions(){
        for(int i = snakeSize-1; i >= 0; i--){
            if(i > 4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if(x[0] > SIZE){
            inGame = false;
        }
        if(x[0] < 0){
            inGame = false;
        }
        if(y[0] > SIZE){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
    }

    public void restart(){
        inGame = true;

        right = true;
        left = false;
        up = false;
        down = false;

        snakeSize = 3;
        record = 0;
        for(int i = 0; i < snakeSize; i++){
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple, appleX, appleY, this);
            for(int i = 0; i < snakeSize; i++){
                g.drawImage(dot, x[i], y[i], this);
            }
        }else{
            String endGame = "Record: " + record;
            g.setColor(Color.white);
            g.drawString(endGame, 100, SIZE/2 - 50);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_ENTER){
                restart();
            }

            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && !down){
                up = true;
                left = false;
                right = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                down = true;
                left = false;
                right = false;
            }
        }
    }
}
