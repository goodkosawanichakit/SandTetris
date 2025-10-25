package core.shape;

import core.Block;

public class I extends Shape {
  public I(int startRow, int startCol) {
    super(startRow, startCol);
  }

  @Override
  protected void initializeBlocks() {
    blocks.add(new Block(centerRow, centerCol - 1, 1));
    blocks.add(new Block(centerRow, centerCol, 1));
    blocks.add(new Block(centerRow, centerCol + 1, 1));
    blocks.add(new Block(centerRow, centerCol + 2, 1));
  }
}
