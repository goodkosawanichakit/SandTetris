import core.Board;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
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

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
          board.controlMove(-1); //
        }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
          board.controlMove(1); //
        }
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
          board.controlRotate(); //
        }
        
        // (เสริม) กด S หรือ ลูกศรล่าง เพื่อ "ทิ้งตัว" (Hard Drop)
        // ถ้าจะทำ ต้องไปเพิ่มเมธอด hardDrop() ใน Board.java
        // if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
        //   board.hardDrop(); 
        // }
      }
    });

    // 5. แสดงหน้าต่าง
    frame.setVisible(true);

    // 6. (แทนที่ physicsThread/renderThread) สตาร์ท Game Loop
    Thread gameLoopThread = new Thread(() -> {
      try {
        while (true) {
          board.updatePhysics(); // 1. อัปเดต Logic
          panel.repaint();     // 2. สั่งวาดจอใหม่ (เรียก paintComponent)
          
          Thread.sleep(200); //
        }
      } catch (InterruptedException e) {}
    });

    gameLoopThread.start();
  }
}