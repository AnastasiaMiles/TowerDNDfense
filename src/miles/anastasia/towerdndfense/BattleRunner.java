package miles.anastasia.towerdndfense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import miles.anastasia.towerdndfense.players.ConsolePlayer;

public class BattleRunner implements Runnable {
  private volatile Battle battle;
  private volatile long interval;
  
  public BattleRunner(Battle battle, long updateInterval) {
    this.battle = battle;
    this.interval = updateInterval;
  }

  @Override
  public void run() {
    Thread bThread = battleThread();
    bThread.start();

    ConsolePlayer cPlayer = null;
    if (battle.attacker() instanceof ConsolePlayer || battle.defender() instanceof ConsolePlayer) {
      cPlayer = (ConsolePlayer)(battle.attacker() instanceof ConsolePlayer ? battle.attacker() : battle.defender());
    }
    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line;
    try {
      while (!(line = br.readLine()).trim().equals("")) {
        if (line.equalsIgnoreCase("stop")) {
          break;
        } else if (line.startsWith("sleep")) {
          String[] parts = line.split(" ");
          if (parts.length == 2) {
            interval = Integer.valueOf(parts[1]);
          }
        } else if (cPlayer != null) {
          cPlayer.processCommand(line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      battle.stop();
      try {
        br.close();
      } catch (IOException e) { }
    }
  }
  
  private Thread battleThread() {
    return new Thread(new Runnable() {
      @Override
      public void run() {
        battle.setup();
        Gui gui = battle.createGui();
        
        long next = System.currentTimeMillis();
        while (!battle.isOver() && battle.running) {
          if (System.currentTimeMillis() > next) {
            battle.update(interval);
            if (gui != null) {
              gui.update();
            }
            next = System.currentTimeMillis() + interval;
          }
          
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            battle.stop();
          }
        }
      }
    });
  }
}