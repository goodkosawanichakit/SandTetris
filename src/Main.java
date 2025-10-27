import core.Board;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame; // <--- Import เพิ่ม
import ui.GamePanel;

public class Main {
  public static void main(String args[]) {
    // 1. สร้าง "เอนจิ้น" ของเกม (Board)
    Board board = new Board();

    // 2. สร้าง "จอเกม" (GamePanel)
    GamePanel panel = new GamePanel(board);

    // 3. สร้าง "หน้าต่าง" (JFrame)
    JFrame frame = new JFrame("Tetris");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel); // เอาจอเกมใส่ในหน้าต่าง
    frame.setResizable(false); // ไม่ให้ย่อขยายหน้าต่าง
    frame.pack(); // คำนวณขนาดหน้าต่างให้พอดีกับ Panel
    frame.setLocationRelativeTo(null); // ให้หน้าต่างอยู่กลางจอ

    // 4. (แทนที่ inputThread) เพิ่ม KeyListener (ตัวดักคีย์บอร์ด)
    frame.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (board.isGameOver) {
          // ถ้าแพ้อยู่: กด ENTER เพื่อเริ่มใหม่
          if (key == KeyEvent.VK_ENTER) {
            board.resetGame();
          }
        } else {
          // ถ้ายังไม่แพ้: ก็ควบคุมตามปกติ
          if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            board.controlMove(-1); //
          }
          if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            board.controlMove(1); //
          }
          if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            board.controlRotate(); //
          }
        }
        
        // (เสริม) กด S หรือ ลูกศรล่าง เพื่อ "ทิ้งตัว" (Hard Drop)
        // ถ้าจะทำ ต้องไปเพิ่มเมธอด hardDrop() ใน Board.java
        // if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
        //   board.hardDrop(); 
        // }
      }
    });

    frame.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        // ถ้าแพ้อยู่ และ คลิกซ้าย (BUTTON1)
        if (board.isGameOver && e.getButton() == MouseEvent.BUTTON1) {
          board.resetGame();
        }
      }
    });

    // 5. แสดงหน้าต่าง
    frame.setVisible(true);

    // 6. (แทนที่ physicsThread/renderThread) สตาร์ท Game Loop
    Thread gameLoopThread = new Thread(() -> {
      final int TARGET_FPS = 60;
      final long FRAME_TIME = 1000 / TARGET_FPS;
      final int PHYSICS_SPEED = 200;
      long lastPhysicsUpdate = System.currentTimeMillis();
      try {
        // Loop นี้จะ "ไม่ตาย" แม้จะ Game Over
        while (true) {
          long startTime = System.currentTimeMillis();

          // 2. อัปเดต Physics (เฉพาะเมื่อถึงเวลา และยังไม่แพ้)
          if (board.isGameRunning() && (System.currentTimeMillis() - lastPhysicsUpdate > PHYSICS_SPEED)) {
            board.updatePhysics(); //
            lastPhysicsUpdate = System.currentTimeMillis();
          }

          // 3. วาดภาพ (ทำทุกเฟรม)
          panel.repaint();

          // 4. คำนวณเวลา Sleep เพื่อให้ได้ 60 FPS
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