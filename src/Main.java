import core.Board;

public class Main {
  public static void main(String args[]) throws InterruptedException {
    Board board = new Board();
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
          if (line.equals("a")) { // ขยับซ้าย
            board.controlMove(-1); 
          }
          if (line.equals("d")) { // ขยับขวา
            board.controlMove(1);
          }
          if (line.equals("w")) { // หมุน
            board.controlRotate();
          }
        }
      } catch (Exception e) {}
    });

    physicsThread.start();
    renderThread.start();
    inputThread.start();
  }
}

