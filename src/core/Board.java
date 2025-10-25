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

  synchronized public void printBoard() {
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

  synchronized public void updatePhysics() {
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
  }

  // Spawn a random shape at the top
  synchronized public void spawnRandomShape() {
    int startCol = col / 2;
    Shape newShape = createRandomShape(0, startCol);
    activeShapes.add(newShape);
  }
  
  private Shape createRandomShape(int row, int col) {
    int shapeType = random.nextInt(7);
    switch (shapeType) {
      case 0: return new I(row, col);
      case 1: return new O(row, col);
      case 2: return new T(row, col);
      case 3: return new L(row, col);
      case 4: return new J(row, col);
      case 5: return new S(row, col);
      case 6: return new Z(row, col);
      default: return new T(row, col);
    }
  }
}