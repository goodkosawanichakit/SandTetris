import java.util.Scanner;

enum Color {NONE, RED, GREEN, BLUE, YELLOW};

class Position {
  public int row;
  public int col;
  Position(int r, int c) {this.row = r; this.col = c;}
}

class Block {
  public Position pos;
  public Color color;
  Block(int r, int c, Color color) {
    pos = new Position(r, c);
    this.color = color;
  }
  int getR() {return pos.row;}
  int getC() {return pos.col;}
}

class Board {
  private int row = 20;
  private int col = 10;
  public Block board[] = new Block[row * col];

  synchronized void printBoard() {
    System.out.print("\033[H\033[2J"); 
    System.out.flush();
    for (int i = 0; i < board.length; i++) {
      if (board[i].color == Color.NONE)
        System.out.print('.');
      else if (board[i].color == Color.RED)
        System.out.print('R');
      else if (board[i].color == Color.GREEN)
        System.out.println('G');
      else if (board[i].color == Color.BLUE)
        System.out.println('B');
      else if (board[i].color == Color.YELLOW)
        System.out.println('Y');
      if (i % col == col - 1)
        System.out.println(); 
    }  
  }

  synchronized void updatePhysics() {
    for (int i = ((row - 1) * col) - 1; i >= 0; i--) {
      if (board[i] != null && board[i + col] == null) {
        board[i + col] = board[i];
        board[i] = null;
      }
    }
  }
}

public class Main {
  public static void main(String args[]) throws InterruptedException {
    Board board = new Board();
    Block red = new Block(6, 1, Color.RED);
    board.board[red.getC() * red.getR()] = red;

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

