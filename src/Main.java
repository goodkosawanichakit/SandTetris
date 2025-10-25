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

    Thread generateShape = new Thread(() -> {
      try {
        while (true) {
          board.spawnRandomShape();
          Thread.sleep(2000);
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
    generateShape.start();
  }
}

