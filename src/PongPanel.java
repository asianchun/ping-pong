import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PongPanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int DELAY = 35;
    private static final int BOARD_LENGTH = 4;
    private int playerOneScore = 0;
    private int ballSpeed;
    private int playerTwoScore = 0;
    private int playerOnePos;
    private int playerTwoPos;
    private int ballX;
    private int ballY;
    private boolean playerOneTurn = true;
    private boolean gameOver = false;
    private boolean pause = true;
    private String direction = "UL";
    private Timer timer;

    PongPanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawGame(g);
    }

    public void drawGame(Graphics g) {
/*        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
            g.drawLine(0,i * UNIT_SIZE,SCREEN_WIDTH,i * UNIT_SIZE);
        }
        for (int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; i++){
            g.drawLine(i * UNIT_SIZE,0,i * UNIT_SIZE,SCREEN_HEIGHT);
        }*/

        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 40));
        g.drawString(String.valueOf(playerOneScore), 400,50);
        g.drawString(String.valueOf(playerTwoScore), 580,50);
        g.drawLine(SCREEN_WIDTH/2,0,SCREEN_WIDTH/2,SCREEN_HEIGHT);

        if (gameOver){
            if (playerOneScore == 5){
                g.setColor(Color.red);
                g.drawString("Player 1 wins!!", 100, SCREEN_HEIGHT/2);
            }
            else {
                g.setColor(Color.blue);
                g.drawString("Player 2 wins!!", 580, SCREEN_HEIGHT/2);
            }
        }
        else {
            g.fillOval(ballX,ballY, UNIT_SIZE, UNIT_SIZE);
        }

        g.setColor(Color.red);
        g.fillRect(0,playerOnePos,UNIT_SIZE, BOARD_LENGTH * UNIT_SIZE);

        g.setColor(Color.blue);
        g.fillRect(980,playerTwoPos,UNIT_SIZE, BOARD_LENGTH * UNIT_SIZE);
    }

    public void startGame() {
        createBallAndBoard();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void checkPoint(){
        if (ballX < 0){
            updateScore(2);
        }
        else if (ballX > (SCREEN_WIDTH - UNIT_SIZE)){
            updateScore(1);
        }
        else if (ballX <= UNIT_SIZE){
            for (int i = playerOnePos - UNIT_SIZE; i <= (playerOnePos + BOARD_LENGTH * UNIT_SIZE); i++){
                if (ballY == i){
                    if (direction.equals("UL")){
                        direction = "UR";
                    }
                    else if (direction.equals("DL")){
                        direction = "DR";
                    }

                    ballSpeed = ballSpeed + 2;
                }
            }
        }
        else if (ballX >= (SCREEN_WIDTH - UNIT_SIZE * 2)){
            for (int i = playerTwoPos - UNIT_SIZE; i <= (playerTwoPos + BOARD_LENGTH * UNIT_SIZE); i++){
                if (ballY == i){
                    if (direction.equals("UR")){
                        direction = "UL";
                    }
                    else if (direction.equals("DR")){
                        direction = "DL";
                    }

                    ballSpeed = ballSpeed + 2;
                }
            }
        }
    }

    public void checkBorders(){
        if ((ballY < 0) && (direction.equals("UR"))){
            direction = "DR";
        }
        else if ((ballY < 0) && (direction.equals("UL"))){
            direction = "DL";
        }
        else if ((ballY > (SCREEN_HEIGHT - UNIT_SIZE)) && (direction.equals("DR"))){
            direction = "UR";
        }
        else if ((ballY > (SCREEN_HEIGHT - UNIT_SIZE)) && (direction.equals("DL"))){
            direction = "UL";
        }
    }

    public void moveBall(){
        switch (direction){
            case "UR":
                ballY = ballY - ballSpeed;
                ballX = ballX + ballSpeed;
                break;
            case "UL":
                ballY = ballY - ballSpeed;
                ballX = ballX - ballSpeed;
                break;
            case "DR":
                ballY = ballY + ballSpeed;
                ballX = ballX + ballSpeed;
                break;
            case "DL":
                ballY = ballY + ballSpeed;
                ballX = ballX - ballSpeed;
                break;
        }
    }

    public void updateScore(int player) {
        pause = true;

        if (player == 1) {
            playerOneScore++;
            playerOneTurn = false;
            direction = "UR";
        }
        else {
            playerTwoScore++;
            playerOneTurn = true;
            direction = "UL";
        }

        if (playerOneScore == 5 || playerTwoScore == 5){
            gameOver = true;
            timer.setRepeats(false);
        }
        else {
            createBallAndBoard();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void createBallAndBoard() {
        if (playerOneTurn){
            ballX = 40;
        }
        else {
            ballX = 940;
        }

        ballY = 280;
        playerOnePos = 260;
        playerTwoPos = 260;
        ballSpeed = 5;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pause){
            moveBall();
            checkPoint();
            checkBorders();
        }
        repaint();
    }

    private class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            pause = false;

            switch (e.getKeyCode()){
                case KeyEvent.VK_W:
                    if (playerOnePos != 0){
                        playerOnePos = playerOnePos - 20;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (playerOnePos < 520){
                        playerOnePos = playerOnePos + 20;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (playerTwoPos != 0){
                        playerTwoPos = playerTwoPos - 20;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (playerTwoPos < 520){
                        playerTwoPos = playerTwoPos + 20;
                    }
                    break;
            }
        }
    }
}