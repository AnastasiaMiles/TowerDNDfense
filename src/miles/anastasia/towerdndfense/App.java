package miles.anastasia.towerdndfense;

import miles.anastasia.towerdndfense.battles.OstagarBattle;
import miles.anastasia.towerdndfense.players.AttackerDarkspawn;
import miles.anastasia.towerdndfense.players.ConsolePlayer;

public class App {

  public static void main(String[] args) {
    new BattleRunner(new OstagarBattle(new AttackerDarkspawn(), new ConsolePlayer(), 20000), 1000).run();
  }

}
