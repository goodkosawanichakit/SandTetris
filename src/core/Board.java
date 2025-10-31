package core;

import core.shape.*;
import java.util.Arrays;
import java.util.Random;

public class Board {
  public int row = 20;
  public int col = 10;
  public int board[] = new int[row * col];
  public boolean isGameOver = false;
  public Shape activeShape = null;
  private final Random random = new Random();

  // Terminal Version
  // private char colorChar(int val) {
  //     return switch (val) {
  //         case 1 -> 'R';
  //         case 2 -> 'G';
  //         case 3 -> 'B';
  //         case 4 -> 'Y';
  //         default -> '.';
  //     };
  // }

  // public synchronized void printBoard() {
  //   // Create a temporary board for rendering
  //   int[] displayBoard = board.clone();
    
  //   // Add active shapes to display
  //   if (activeShape != null && activeShape.isActive()) {
  //     for (Block block : activeShape.getBlocks()) {
  //       int index = block.row * col + block.col;
  //       if (index >= 0 && index < displayBoard.length) {
  //         displayBoard[index] = block.color;
  //       }
  //     }
  //   }
    
  //   StringBuilder sb = new StringBuilder();
  //   for (int i = 0; i < displayBoard.length; i++) {
  //     sb.append(colorChar(displayBoard[i]));
  //     if (i % col == col - 1) sb.append('\n');
  //   }
  //   System.out.print("\033[H\033[2J");
  //   System.out.flush();
  //   System.out.print(sb.toString());
  // }

  private Shape createRandomShape(int row, int col) {
    int shapeType = random.nextInt(7);
    int color = random.nextInt(4) + 1;
      return switch (shapeType) {
        case 0 -> new I(row, col, color);
        case 1 -> new O(row, col, color);
        case 2 -> new T(row, col, color);
        case 3 -> new L(row, col, color);
        case 4 -> new J(row, col, color);
        case 5 -> new S(row, col, color);
        case 6 -> new Z(row, col, color);
        default -> new T(row, col, color);
      };
  }

 public synchronized void controlMove(int direction) {

    if (activeShape == null) return;

    boolean canMoveHorizontal = true;

    for (Block block : activeShape.getBlocks()) {
      int nextCol = block.col + direction;
      int currentRow = block.row;

      if (nextCol < 0) {
        canMoveHorizontal = false;
        break; 
      }

      if (nextCol >= col) { 
        canMoveHorizontal = false;
        break; 
      }

      if (currentRow >= 0) { 
        int nextIndex = currentRow * col + nextCol;
        
        if (nextIndex >= 0 && nextIndex < board.length && board[nextIndex] != 0) {
          canMoveHorizontal = false;
          break; 
        }
      }
    } 

    if (canMoveHorizontal) {
      activeShape.moveHorizontal(direction);
    }
  }

  public synchronized void controlRotate() {
    if (activeShape == null) return;
    
    activeShape.rotate(); 

    if (isPositionValid(activeShape)) {
      return;
    }

    activeShape.rotate(); 
    activeShape.rotate();
    activeShape.rotate(); 
  }

  public synchronized void updatePhysics() {

    if (activeShape != null) {
      boolean canMoveDown = true;
      for (Block block : activeShape.getBlocks()) {
        int nextRow = block.row + 1;
        int currentCol = block.col;
        
        if (nextRow >= row) {
          canMoveDown = false;
          break;
        }
        
        int nextIndex = nextRow * col + currentCol;
        if (board[nextIndex] != 0) {
          canMoveDown = false;
          break;
        }
      }

      if (canMoveDown) {
        activeShape.moveDown();
      } else {
        for (Block block : activeShape.getBlocks()) {
          int index = block.row * col + block.col;
          if (index >= 0 && index < board.length) {
            board[index] = block.color;
          }
        }
        activeShape = null; 
        checkAndClearLines(); 
      }
    }

    if (activeShape == null) spawnRandomShape();
  }
  
  private boolean isPositionValid(Shape shape) {
    for (Block block : shape.getBlocks()) {
      int r = block.row;
      int c = block.col;

     
      if (c < 0 || c >= col) {
        return false;
      }
    
      if (r >= row) {
        return false;
      }
     
      if (r < 0) {
        continue; 
      }
      
 
      if (board[r * col + c] != 0) {
        return false;
      }
    }
    return true;
  }

  public void spawnRandomShape() {

    int startCol = col / 2;
    Shape newShape = createRandomShape(0, startCol); 
   
    if (isPositionValid(newShape)) {
      activeShape = newShape; 
    } else {
      isGameOver = true;
      activeShape = null; 
    }
  }

  private void checkAndClearLines() {

    for (int r = row - 1; r >= 0; r--) {
      boolean isFull = true;
      
      for (int c = 0; c < col; c++) {
        if (board[r * col + c] == 0) { 
          isFull = false;
          break; 
        }
      }
      
      if (isFull) {
        clearRowAndShiftDown(r);
        
        r++;
      }
    }
  }
  
  private void clearRowAndShiftDown(int rowToClear) {
    for (int r = rowToClear; r > 0; r--) {
      for (int c = 0; c < col; c++) {
        board[r * col + c] = board[(r - 1) * col + c];
      }
    }
    
    for (int c = 0; c < col; c++) {
      board[0 * col + c] = 0;
    }
  }

  public synchronized void resetGame() {
   
    Arrays.fill(board, 0);
   
    activeShape = null; 
    
    isGameOver = false;
    
  }
  
  public boolean isGameRunning() {
    return !isGameOver;
  }
  
}
