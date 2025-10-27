package core;

import core.shape.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
  public int row = 20;
  public int col = 10;
  public int board[] = new int[row * col];
  public List<Shape> activeShapes = new ArrayList<>();
  private Random random = new Random();
  
  private char colorChar(int val) {
    switch (val) {
      case 1: return 'R'; 
      case 2: return 'G';
      case 3: return 'B'; 
      case 4: return 'Y'; 
      default: return '.';
    }
  }

  public synchronized void printBoard() {
    // Create a temporary board for rendering
    int[] displayBoard = board.clone();
    
    // Add active shapes to display
    for (Shape shape : activeShapes) {
      if (shape.isActive()) {
        for (Block block : shape.getBlocks()) {
          int index = block.row * col + block.col;
          if (index >= 0 && index < displayBoard.length) {
            displayBoard[index] = block.color;
          }
        }
      }
    }
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < displayBoard.length; i++) {
      sb.append(colorChar(displayBoard[i]));
      if (i % col == col - 1) sb.append('\n');
    }
    System.out.print("\033[H\033[2J");
    System.out.flush();
    System.out.print(sb.toString());
  }

  public synchronized void updatePhysics() {
    List<Shape> shapesToRemove = new ArrayList<>();
    
    for (Shape shape : activeShapes) {
      if (!shape.isActive()) continue;
      
      // Check if shape can move down
      boolean canMoveDown = true;
      for (Block block : shape.getBlocks()) {
        int nextRow = block.row + 1;
        int currentCol = block.col;
        
        // Check bottom boundary
        if (nextRow >= row) {
          canMoveDown = false;
          break;
        }
        
        // Check collision with locked blocks
        int nextIndex = nextRow * col + currentCol;
        if (board[nextIndex] != 0) {
          canMoveDown = false;
          break;
        }
      }
      
      if (canMoveDown) {
        shape.moveDown();
      } else {
        // Shape has landed - lock it to the board
        shape.setInactive();
        for (Block block : shape.getBlocks()) {
          int index = block.row * col + block.col;
          if (index >= 0 && index < board.length) {
            board[index] = block.color;
          }
        }
        shapesToRemove.add(shape);
      }
    }
    
    // Remove landed shapes from active list
    activeShapes.removeAll(shapesToRemove);

    if (activeShapes.isEmpty()) {
      spawnRandomShape();
    }
  }
  
  // Spawn a random shape at the top
  public void spawnRandomShape() {
    int startCol = col / 2;
    Shape newShape = createRandomShape(0, startCol);
    activeShapes.add(newShape);
  }
  
  private Shape createRandomShape(int row, int col) {
    int shapeType = random.nextInt(7);
    int color = random.nextInt(4) + 1;
    switch (shapeType) {
      case 0: return new I(row, col, color);
      case 1: return new O(row, col, color);
      case 2: return new T(row, col, color);
      case 3: return new L(row, col, color);
      case 4: return new J(row, col, color);
      case 5: return new S(row, col, color);
      case 6: return new Z(row, col, color);
      default: return new T(row, col, color);
    }
  }

  private Shape getActiveShape() {
    for (Shape s : activeShapes) {
      if (s.isActive()) return s;
    }
    return null;
  }

 public synchronized void controlMove(int direction) {
    Shape active = getActiveShape();
    if (active == null) return; // ไม่มีตัวให้ขยับ

    // 1. สร้าง Flag เช็กว่าขยับได้ไหม
    boolean canMoveHorizontal = true;

    // 2. Loop เช็กทุกก้อน Block ใน Shape นั้น
    for (Block block : active.getBlocks()) {
      int nextCol = block.col + direction; // ตำแหน่งคอลัมน์ใหม่ที่จะไป
      int currentRow = block.row;

      // --- ทำการเช็ก 3 อย่าง ---

      // CHECK 1: ชนขอบซ้ายหรือไม่?
      if (nextCol < 0) {
        canMoveHorizontal = false;
        break; // หยุดเช็ก (เจอตัวชนแล้ว)
      }

      // CHECK 2: ชนขอบขวาหรือไม่?
      if (nextCol >= col) { // 'col' คือความกว้าง (10), index ที่ valid คือ 0-9
        canMoveHorizontal = false;
        break; // หยุดเช็ก
      }

      // CHECK 3: ชนบล็อกอื่นที่ค้างอยู่หรือไม่?
      // (เช็กเฉพาะบล็อกที่อยู่บนกระดานแล้วเท่านั้น)
      if (currentRow >= 0) { 
        int nextIndex = currentRow * col + nextCol;
        // เช็กว่า index ไม่ล้น และ ช่องนั้นมีบล็อกอยู่ ( != 0 )
        if (nextIndex >= 0 && nextIndex < board.length && board[nextIndex] != 0) {
          canMoveHorizontal = false;
          break; // หยุดเช็ก
        }
      }
    } // สิ้นสุด for loop

    // 3. ถ้าเช็กทุกก้อนแล้ว "canMoveHorizontal" ยังเป็น true = ขยับได้
    if (canMoveHorizontal) {
      active.moveHorizontal(direction);
    }
  }

  public synchronized void controlRotate() {
    Shape active = getActiveShape();
    if (active != null) {
      active.rotate();
    }
  }
}
