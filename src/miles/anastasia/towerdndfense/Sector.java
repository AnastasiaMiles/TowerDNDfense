package miles.anastasia.towerdndfense;


import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Sector {
  private final Battle battle;
  private final String name;
  private final Random rand;
  protected Set<Sector> migratesTo;
  
  private int defenders;
  private int attackers;
  protected float attackerSurvival = 0.5f;
  protected float defenderSurvival = 0.5f;
  
  public Sector(Battle battle, String name) {
    this.battle = battle;
    this.name = name;
    this.migratesTo = new HashSet<>();
    this.rand = new Random();
  }
  
  public final int getAttackers() {
    return this.attackers;
  }
  
  public final int getDefenders() {
    return this.defenders;
  }
  
  public void migratesTo(Sector...sectors) {
    if (battle.running) {
      return;
    }
    
    for (Sector s : sectors) {
      migratesTo.add(s);
    }
  }
  
  public Set<Sector> migratesTo() {
    return Collections.unmodifiableSet(migratesTo);
  }
  
  protected void addUnits(Player player, int units) {
    if (battle.isAttacker(player) && battle.isDefender(player)) {
      throw new RuntimeException("Player must be part of the battle!");
    }
    if (battle.isAttacker(player)) {
      attackers += units;
    } else {
      defenders += units;
    }
  }
  
  protected void removeUnits(Player player, int units) {
    if (battle.isAttacker(player) && battle.isDefender(player)) {
      throw new RuntimeException("Specified player is not attacker or defender!");
    }
    if (battle.isAttacker(player)) {
      attackers -= units;
      if (attackers < 0) { attackers = 0; }
    } else {
      defenders -= units;
      if (defenders < 0) { defenders = 0; }
    }
  }
  
  public final Battle getBattle() {
    return this.battle;
  }
  
  public final String getName() {
    return this.name;
  }
  
  public double defenderPortion() {
    if (attackers + defenders == 0) {
      return 1;
    }
    return (defenders / (double) (attackers + defenders));
  }
  
  public double attackerPortion() {
    if (attackers + defenders == 0) {
      return 0;
    }
    return (attackers / (double) (attackers + defenders));
  }
  
  public boolean canDefendersPass() {
    return defenderPortion() > 0.5;
  }
  
  public boolean canAttackersPass() {
    return attackerPortion() > 0.5;
  }
  
  public boolean canMigrateTo(Player player, Sector sector) {
    if (battle.isAttacker(player) && battle.isDefender(player)) {
      throw new RuntimeException("Specified player is not attacker or defender!");
    }
    if (!migratesTo.contains(sector)) {
      return false;
    }
    if ((battle.isAttacker(player) && canAttackersPass()) || (battle.isDefender(player) && canDefendersPass())) {
      return true;
    }
    return false;
  }
  
  public boolean migrateTo(Player player, Sector sector, int count) {
    if (canMigrateTo(player, sector)) {
      this.removeUnits(player, count);
      sector.addUnits(player, count);
      return true;
    }
    return false;
  }
  
  public void fight() {
    if (defenders <= 0 || attackers <= 0) {
      return;
    }
    
    int defendFighters = (int) Math.ceil((attackers - (attackers) % 100) / 100);
    int attackFighters = (int) Math.ceil((defenders - (defenders) % 100) / 100);
    for (int i = 0; i < defendFighters; i++) {
      if (rand.nextDouble() >= defenderSurvival) {
        defenders--;
      }
    }
    if (defenders < 0) {
      defenders = 0;
    }
    for (int i = 0; i < attackFighters; i++) {
      if (rand.nextDouble() >= attackerSurvival) {
        attackers--;
      }
    }
    if (attackers < 0) {
      attackers = 0;
    } 
  }
  
  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
