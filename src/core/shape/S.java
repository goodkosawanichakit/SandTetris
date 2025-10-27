package core.shape;

import core.Block;

public class S extends Shape {
  public S(int startRow, int startCol, int color) {
    super(startRow, startCol, color);
  }
  
  @Override
  protected void initializeBlocks(int color) {
    blocks.add(new Block(centerRow, centerCol, color)); 
    blocks.add(new Block(centerRow, centerCol + 1, color));
    blocks.add(new Block(centerRow + 1, centerCol - 1, color));
    blocks.add(new Block(centerRow + 1, centerCol, color));
  }
}
