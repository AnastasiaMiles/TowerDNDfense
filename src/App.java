import java.util.Scanner;

// To do:
// Improve timing proportions
// Make reinforcement timer more random / better suited
// to the battle

public class App implements Runnable {
  volatile String command = "";
  private int sleepTime = 100;
  private int reinforcementTime = 1800;
  private int reinforcementMin = 1000;
  private int reinforcementMax = 10000;

  public void run() {
    BattleField b = new BattleField(20000, 20000, 50000, 4);
    long reinforcementTimer = System.currentTimeMillis();
    BattleGui gui = new BattleGui();
    while (b.getTroopNumbers() > 0) {
      if (command != "") {
        processInput(b, command);
        command = "";
      }
      b.update();
      if (System.currentTimeMillis() - reinforcementTimer > reinforcementTime) {
        int enemy = reinforcementMin + (int) (Math.random() *
            ((reinforcementMax - reinforcementMin) + 1));
        b.enemyReinforce(enemy);
        reinforcementTimer = System.currentTimeMillis();
      }
      updateGui(gui, b);
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
      }
    }
  }


  public static void main(String[] args) {
    App app = new App();
    Thread t = new Thread(app);
    t.start();
    Scanner s = new Scanner(System.in);
    while (true) {
      if (s.hasNext()) {
        app.command = s.nextLine();
      }
    }
  }

  private String processInput(BattleField b, String command) {
    command = command.toLowerCase();
    String[] parts = command.split(" ");
    Sector destinationSector;
    Sector sourceSector;
    int numberToMove;
    if (parts[0].compareTo("move") == 0) {
      sourceSector = interpretSector(b, parts[1]);
      destinationSector = interpretSector(b, parts[2]);
      numberToMove = Integer.parseInt(parts[3]);
      if (sourceSector != b.getFront()) {
        b.move(sourceSector, destinationSector, numberToMove);

      }
    } else if (parts[0].compareTo("add") == 0) {
      destinationSector = interpretSector(b, parts[1]);
      numberToMove = Integer.parseInt(parts[2]);
      if (destinationSector != b.getFront()) {
        b.add(destinationSector, numberToMove);
        System.out.println(destinationSector.getAllyNumbers());
      }
    } else if (parts[0].compareTo("abandon") == 0) {
      destinationSector = interpretSector(b, parts[1]);
      if (destinationSector != b.getFront()) {
        b.abandon(destinationSector);
      }
    } else if (parts[0].compareTo("advantage") == 0) {
      destinationSector = interpretSector(b, parts[1]);
      if (destinationSector != b.getFront()) {
        b.giveAdvantage(destinationSector);
      }
    }
    return parts[0];
  }

  private Sector interpretSector(BattleField b, String sec) {
    Sector target = b.getFront();
    switch (sec) {
      case "outer1":
        target = b.getOuters()[0];
        break;
      case "outer2":
        target = b.getOuters()[1];
        break;
      case "outer3":
        target = b.getOuters()[2];
        break;
      case "outer4":
        target = b.getOuters()[3];
        break;
      case "inner1":
        target = b.getInners()[0];
        break;
      case "inner2":
        target = b.getInners()[1];
        break;
      case "inner3":
        target = b.getInners()[2];
        break;
      case "inner4":
        target = b.getInners()[3];
        break;
      case "inner5":
        target = b.getInners()[4];
        break;
      case "inner6":
        target = b.getInners()[5];
        break;
      case "inner7":
        target = b.getInners()[6];
        break;
      case "inner8":
        target = b.getInners()[7];
        break;
      case "tower":
        target = b.getTower();
        break;
    }
    return target;
  }

  private void updateGui(BattleGui gui, BattleField b) {
    gui.frontTroops(b.getFrontNumbers());
    gui.commandTroops(b.getTroopNumbers());
    gui.setUnassignedTroops(b.getFreeNumbers());
    gui.setTower(b.getTower().getAllyNumbers(), b.getTower().getEnemyNumbers());
    gui.setFront(b.getFront().getAllyNumbers(), b.getFront().getEnemyNumbers());
    gui.setTowerFallen(b.towerHasFallen());

    Sector[] temp = b.getOuters();
    for (int i = 0; i < temp.length; i++) {
      gui.setOuterSector(i, temp[i].getAllyNumbers(), temp[i].getEnemyNumbers());
    }
    temp = b.getInners();
    for (int i = 0; i < temp.length; i++) {
      gui.setInnerSector(i, temp[i].getAllyNumbers(), temp[i].getEnemyNumbers());
    }
  }
}
