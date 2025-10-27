// คุณอาจจะต้อง import core.Board; และ core.Block; ถ้าไฟล์นี้อยู่นอก package
package ui;
import core.Board;
import core.Block;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class GamePanel extends JPanel {

  // ขนาดของบล็อก (พิกเซล) - คุณปรับแก้ได้ตามใจชอบ
  private static final int BLOCK_SIZE = 30; 

  private Board board;

  public GamePanel(Board board) {
    this.board = board;
    
    // คำนวณขนาดของ Panel ให้พอดีกับกระดาน
    int panelWidth = board.col * BLOCK_SIZE;
    int panelHeight = board.row * BLOCK_SIZE;
    setPreferredSize(new Dimension(panelWidth, panelHeight));
    
    setBackground(Color.BLACK); // สีพื้นหลัง
  }

  /**
   * นี่คือหัวใจของการวาดภาพ (แทนที่ printBoard() เดิม)
   * Swing จะเรียกเมธอดนี้อัตโนมัติเมื่อเราสั่ง .repaint()
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); // ห้ามลบ! (สำหรับ Swing)

    // วาดบล็อกที่ "ตกถึงพื้นแล้ว" (จาก board.board[])
    drawLandedBlocks(g);
    
    // วาดบล็อกที่ "กำลังตก" (จาก board.activeShape)
    drawActiveShape(g);
  }

  /**
   * วนลูปกระดานหลัก (board.board) เพื่อวาดบล็อกที่ล็อคแล้ว
   */
  private void drawLandedBlocks(Graphics g) {
    for (int r = 0; r < board.row; r++) {
      for (int c = 0; c < board.col; c++) {
        int colorCode = board.board[r * board.col + c]; //
        
        if (colorCode != 0) {
          g.setColor(getBlockColor(colorCode));
          g.fillRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
          
          // (เสริม) วาดขอบให้สวยขึ้น
          g.setColor(Color.DARK_GRAY);
          g.drawRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
      }
    }
  }

  /**
   * วาด activeShape (ถ้ามี) ทับลงไป
   */
  private void drawActiveShape(Graphics g) {
    if (board.activeShape != null) {
      // ใช้สีจากตัว Shape เอง
      int colorCode = board.activeShape.getBlocks().get(0).color; //
      g.setColor(getBlockColor(colorCode));
      
      for (Block block : board.activeShape.getBlocks()) { //
        g.fillRect(block.col * BLOCK_SIZE, block.row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE); //
        
        // (เสริม) วาดขอบ
        g.setColor(Color.WHITE);
        g.drawRect(block.col * BLOCK_SIZE, block.row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE); //
      }
    }
  }

  /**
   * เมธอดแปลงรหัสสี (int) ไปเป็นสีของ Swing (Color)
   * (แทนที่ colorChar เดิม)
   */
  private Color getBlockColor(int colorCode) {
    return switch (colorCode) {
      case 1 -> Color.RED;
      case 2 -> Color.GREEN;
      case 3 -> Color.BLUE;
      case 4 -> Color.YELLOW;
      // เพิ่มสีอื่นๆ ได้ตามใจชอบ
      default -> Color.BLACK; // สี Default (สำหรับ 0)
    };
  }
}