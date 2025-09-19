import java.util.Scanner;

class Board {
  private int row = 20;
  private int col = 10;
  public int board[] = new int[row * col];

  synchronized void printBoard() {
    System.out.print("\033[H\033[2J"); 
    System.out.flush();
    for (int i = 0; i < board.length; i++) {
      if (board[i] == 0)
        System.out.print('.');
      else if (board[i] == 1)
        System.out.print('O');
      if (i % col == col - 1)
        System.out.println(); 
    }  
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
    board.board[6] = 1;

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

