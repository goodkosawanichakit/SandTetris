package core.shape;

import core.Block;

public class O extends Shape {
  public O(int startRow, int startCol) {
    super(startRow, startCol);
  }
  
  @Override
  protected void initializeBlocks() {
    blocks.add(new Block(centerRow, centerCol, 4)); 
    blocks.add(new Block(centerRow, centerCol + 1, 4));
    blocks.add(new Block(centerRow + 1, centerCol, 4));
    blocks.add(new Block(centerRow + 1, centerCol + 1, 4));
  }
  
  @Override
  public void rotate() {
    // O-piece doesn't rotate - override to do nothing
  }
}
