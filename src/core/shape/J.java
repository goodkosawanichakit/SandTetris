package core.shape;

import core.Block;

public class J extends Shape {
  public J(int startRow, int startCol) {
    super(startRow, startCol);
  }
  
  @Override
  protected void initializeBlocks() {
    blocks.add(new Block(centerRow, centerCol - 1, 3)); 
    blocks.add(new Block(centerRow + 1, centerCol - 1, 3));
    blocks.add(new Block(centerRow + 1, centerCol, 3));
    blocks.add(new Block(centerRow + 1, centerCol + 1, 3));
  }
}
