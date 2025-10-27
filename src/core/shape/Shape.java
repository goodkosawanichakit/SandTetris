package core.shape;

import core.Block;
import java.util.ArrayList;
import java.util.List;


public abstract class Shape {
  protected List<Block> blocks;
  protected int centerRow;
  protected int centerCol;
  protected boolean isActive; // true = falling, false = landed
  
  public Shape(int startRow, int startCol, int color) {
    this.centerRow = startRow;
    this.centerCol = startCol;
    this.blocks = new ArrayList<>();
    this.isActive = true;
    initializeBlocks(color);
  }

  protected abstract void initializeBlocks(int color);

  public List<Block> getBlocks() {
    return blocks;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setInactive() {
    this.isActive = false;
  }

  public synchronized void moveDown() {
    for (Block block: blocks) {
      block.row++;
    }
    centerRow++;
  }

  public void moveHorizontal(int direction) {
    for (Block block : blocks) {
      block.col += direction;
    }
    centerCol += direction;
  }

  public void rotate() {
    for (Block block: blocks) {
      int relativeRow = block.row - centerRow;
      int relativeCol = block.col - centerCol;

      // Rotation matrix: (x, y) -> (-y, x)
      int newRelativeRow = -relativeCol;
      int newRelativeCol = relativeRow;

      block.row = centerRow + newRelativeRow;
      block.col = centerCol + newRelativeCol;
    }
  }
}
