package ui;
import core.Block;
import core.Board;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

  private static final int BLOCK_SIZE = 30; 

  private Board board;

  public GamePanel(Board board) {
    this.board = board;
    
    int panelWidth = board.col * BLOCK_SIZE;
    int panelHeight = board.row * BLOCK_SIZE;
    setPreferredSize(new Dimension(panelWidth, panelHeight));
    
    setBackground(Color.BLACK);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    drawLandedBlocks(g);
    
    if (board.isGameOver) {
      g.setColor(new Color(255, 255, 255, 150)); 
      g.fillRect(0, 0, getWidth(), getHeight());

      g.setColor(Color.RED);
      String msg = "GAME OVER";

      int stringWidth = g.getFontMetrics().stringWidth(msg);
      g.drawString(msg, (getWidth() - stringWidth) / 2, getHeight() / 2);
    }

    drawActiveShape(g);
  }

  private void drawLandedBlocks(Graphics g) {
    for (int r = 0; r < board.row; r++) {
      for (int c = 0; c < board.col; c++) {
        int colorCode = board.board[r * board.col + c];
        
        if (colorCode != 0) {
          g.setColor(getBlockColor(colorCode));
          g.fillRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

          g.setColor(Color.DARK_GRAY);
          g.drawRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
      }
    }
  }

  private void drawActiveShape(Graphics g) {
    if (board.activeShape != null) {
      
      
      for (Block block : board.activeShape.getBlocks()) {
        int colorCode = block.color; 
        g.setColor(getBlockColor(colorCode));

        g.fillRect(block.col * BLOCK_SIZE, block.row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE); //
        
        g.setColor(Color.WHITE);
        g.drawRect(block.col * BLOCK_SIZE, block.row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
      }
    }
  }


  private Color getBlockColor(int colorCode) {
    return switch (colorCode) {
      case 1 -> Color.RED;
      case 2 -> Color.GREEN;
      case 3 -> Color.BLUE;
      case 4 -> Color.YELLOW;

      default -> Color.BLACK; 
    };
  }

}