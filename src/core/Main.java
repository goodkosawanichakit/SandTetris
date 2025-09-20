import java.util.Scanner;

class Block {
  public int row;
  public int col;
  public int color;

  public Block(int r, int c, int color) {
    this.row = r; this.col = c;
    this.color = color;
  }

  int getR() {return this.row;}
  int getC() {return this.col;}
}

class Board {
  public int row = 20;
  public int col = 10;
  public int board[] = new int[row * col];
  
  private char colorChar(int val) {
    switch (val) {
      case 1: return 'R'; 
      case 2: return 'G';
      case 3: return 'B'; 
      case 4: return 'Y'; 
      default: return '.';
    }
  }

  synchronized void printBoard() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < board.length; i++) {
      sb.append(colorChar(board[i]));
      if (i % col == col - 1) sb.append('\n');
    }
    System.out.print("\033[H\033[2J");
    System.out.flush();
    System.out.print(sb.toString());
  }

  synchronized void updatePhysics() {
    for (int i = ((row - 1) * col) - 1; i >= 0; i--) {
      if (board[i] != 0 && board[i + col] == 0) {
        board[i + col] = board[i];
        board[i] = 0;
      }
    }
  }
}

public class Main {
  public static void main(String args[]) throws InterruptedException {
    Board board = new Board();
    Block red = new Block(0, 6, 1);
    board.board[red.getR() * board.col + red.getC() - 1] = red.color;
    Thread physicsThread = new Thread(() -> {
      try {
        while (true) {
          board.updatePhysics();
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {}
    });

    Thread renderThread = new Thread(() -> {
      try {
        while (true) {
          board.printBoard();
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {}
    });

    Thread inputThread = new Thread(() -> {
      try {
        java.util.Scanner sc = new java.util.Scanner(System.in);
          while (true) {
            String line = sc.nextLine();
            if (line.equals("q")) {
              System.out.println("Quitting...");
              System.exit(0);
          }
        }
      } catch (Exception e) {}
    });

    physicsThread.start();
    renderThread.start();
    inputThread.start();
  }
}

