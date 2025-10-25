package core.shape;

import core.Block;

public class Z extends Shape {
  public Z(int startRow, int startCol) {
    super(startRow, startCol);
  }
  
  @Override
  protected void initializeBlocks() {
    blocks.add(new Block(centerRow, centerCol - 1, 1)); 
    blocks.add(new Block(centerRow, centerCol, 1));
    blocks.add(new Block(centerRow + 1, centerCol, 1));
    blocks.add(new Block(centerRow + 1, centerCol + 1, 1));
  }
}
