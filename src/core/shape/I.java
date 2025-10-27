package core.shape;

import core.Block;

public class I extends Shape {
  public I(int startRow, int startCol, int color) {
    super(startRow, startCol, color);
  }

  @Override
  protected void initializeBlocks(int color) {
    blocks.add(new Block(centerRow, centerCol - 1, color));
    blocks.add(new Block(centerRow, centerCol, color));
    blocks.add(new Block(centerRow, centerCol + 1, color));
    blocks.add(new Block(centerRow, centerCol + 2, color));
  }
}
