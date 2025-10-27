package core.shape;

import core.Block;

public class O extends Shape {
  public O(int startRow, int startCol, int color) {
    super(startRow, startCol, color);
  }
  
  @Override
  protected void initializeBlocks(int color) {
    blocks.add(new Block(centerRow, centerCol, color)); 
    blocks.add(new Block(centerRow, centerCol + 1, color));
    blocks.add(new Block(centerRow + 1, centerCol, color));
    blocks.add(new Block(centerRow + 1, centerCol + 1, color));
  }
  
  @Override
  public void rotate() {
    // O-piece doesn't rotate - override to do nothing
  }
}
