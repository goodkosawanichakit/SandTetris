import core.Board;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import ui.GamePanel;

public class Main {
  public static void main(String args[]) {
   
    Board board = new Board();

    GamePanel panel = new GamePanel(board);

    JFrame frame = new JFrame("Tetris");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel); 
    frame.setResizable(false); 
    frame.pack();
    frame.setLocationRelativeTo(null);

    frame.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (board.isGameOver) {
          if (key == KeyEvent.VK_ENTER) {
            board.resetGame();
          }
        } else {
          if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            board.controlMove(-1); 
          }
          if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            board.controlMove(1); 
          }
          if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            board.controlRotate(); 
          }
        }
      }
    });

    frame.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (board.isGameOver && e.getButton() == MouseEvent.BUTTON1) {
          board.resetGame();
        }
      }
    });

    frame.setVisible(true);

    Thread gameLoopThread = new Thread(() -> {
      final int TARGET_FPS = 60;
      final long FRAME_TIME = 1000 / TARGET_FPS;
      final int PHYSICS_SPEED = 200;
      long lastPhysicsUpdate = System.currentTimeMillis();
      try {
        while (true) {
          long startTime = System.currentTimeMillis();

          if (board.isGameRunning() && (System.currentTimeMillis() - lastPhysicsUpdate > PHYSICS_SPEED)) {
            board.updatePhysics(); //
            lastPhysicsUpdate = System.currentTimeMillis();
          }

          panel.repaint();

          long elapsedTime = System.currentTimeMillis() - startTime;
          long sleepTime = FRAME_TIME - elapsedTime;

          if (sleepTime > 0) {
            Thread.sleep(sleepTime);
          }
        }
      } catch (InterruptedException e) {}
    });

    gameLoopThread.start();
  }
}
