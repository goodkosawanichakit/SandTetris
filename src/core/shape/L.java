package core.shape;

import core.Block;

public class L extends Shape {
  public L(int startRow, int startCol) {
    super(startRow, startCol);
  }
  
  @Override
  protected void initializeBlocks() {
    blocks.add(new Block(centerRow, centerCol + 1, 2));
    blocks.add(new Block(centerRow + 1, centerCol - 1, 2));
    blocks.add(new Block(centerRow + 1, centerCol, 2));
    blocks.add(new Block(centerRow + 1, centerCol + 1, 2));
  }
}
