package miles.anastasia.towerdndfense;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import miles.anastasia.towerdndfense.players.ConsolePlayer;

public abstract class Battle {
  private Player attacker;
  private Player defender;
  private Set<Sector> sectors;
  protected int attackerTroops;
  protected int defenderTroops;
  boolean running;
  
  public Battle(Player attacker, Player defender) {
    if (attacker == defender) {
      throw new IllegalArgumentException("Attacker and Defender cannot be the same!");
    }
    if (attacker instanceof ConsolePlayer && defender instanceof ConsolePlayer) {
      throw new IllegalArgumentException("Attacker and Defender cannot both be ConsolePlayers!");
    }
    this.attacker = attacker;
    this.defender = defender;
    this.sectors = new HashSet<>();
  }
  
  protected Player attacker() {
    return this.attacker;
  }
  
  protected Player defender() {
    return this.defender;
  }
  
  protected void addSectors(Sector...sectors) {
    for (Sector s : sectors) {
      this.sectors.add(s);
    }
  }
  
  public boolean addTroopsTo(Sector sector, int numTroops, Player player) {
    if (isAttacker(player) && isDefender(player)) {
      throw new RuntimeException("Player must be part of the battle!");
    }
    
    Set<Sector> entrance = getEntranceSectors(player);
    if (!entrance.contains(sector) || isAttacker(player) && attackerTroops - numTroops < 0 ||
        isDefender(player) && defenderTroops - numTroops < 0) {
      return false;
    }
    
    sector.addUnits(player, numTroops);
    if (isAttacker(player)) {
      attackerTroops -= numTroops;
    } else {
      defenderTroops -= numTroops;
    }
    return true;
  }
  
  public int getTroops(Player player) {
    if (isAttacker(player)) {
      return attackerTroops;
    }
    if (isDefender(player)) {
      return defenderTroops;
    }
    throw new RuntimeException("Player must be part of the battle!");
  }
  
  public Set<Sector> getAllSectors() {
    return Collections.unmodifiableSet(sectors);
  }
  
  public boolean isAttacker(Player player) {
    return player == attacker;
  }
  
  public boolean isDefender(Player player) {
    return player == defender;
  }
  
  public void stop() {
    attacker.running = false;
    defender.running = false;
    this.running = false;
  }
  
  public void setup() {
    attacker.setBattle(this);
    attacker.running = true;
    defender.setBattle(this);
    defender.running = true;
    this.running = true;
  }
  
  public abstract Gui createGui();
  public abstract boolean isOver();  
  public abstract void update(long interval);
  public abstract Set<Sector> getEntranceSectors(Player player);
}
