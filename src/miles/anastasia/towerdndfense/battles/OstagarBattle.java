package miles.anastasia.towerdndfense.battles;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import miles.anastasia.towerdndfense.Battle;
import miles.anastasia.towerdndfense.Gui;
import miles.anastasia.towerdndfense.Player;
import miles.anastasia.towerdndfense.Sector;

public class OstagarBattle extends Battle {
  private float towerHealth = 100f;
  private final Random random;
  private Sector tower;
  private Sector front;
  
  private long reinforcementTime = 60; // 60 intervals
  private long reinforceTimer;
  private int reinforceMax = 10000;
  private int reinforceMin = 1000;
  
  
  public OstagarBattle(Player attacker, Player defender, int defenderTroops) {
    super(attacker, defender);
    this.random = new Random();
    this.tower = new InnerSector(this, "tower");
    this.addSectors(tower);
    
    Sector inner1 = new InnerSector(this, "inner1");
    Sector inner2 = new InnerSector(this, "inner2");
    Sector inner3 = new InnerSector(this, "inner3");
    Sector inner4 = new InnerSector(this, "inner4");
    Sector inner5 = new InnerSector(this, "inner5");
    Sector inner6 = new InnerSector(this, "inner6");
    Sector inner7 = new InnerSector(this, "inner7");
    Sector inner8 = new InnerSector(this, "inner8");
    inner1.migratesTo(tower);
    inner2.migratesTo(tower);
    inner3.migratesTo(tower);
    inner4.migratesTo(tower);
    inner5.migratesTo(tower);
    inner6.migratesTo(tower);
    inner7.migratesTo(tower);
    inner8.migratesTo(tower);
    this.addSectors(inner1, inner2, inner3, inner4, inner5, inner6, inner7, inner8);
    
    Sector outer1 = new OuterSector(this, "outer1");
    Sector outer2 = new OuterSector(this, "outer2");
    Sector outer3 = new OuterSector(this, "outer3");
    Sector outer4 = new OuterSector(this, "outer4");
    outer1.migratesTo(inner1, inner2);
    outer2.migratesTo(inner3, inner4);
    outer3.migratesTo(inner5, inner6);
    outer4.migratesTo(inner7, inner8);
    
    this.front = new FrontSector(this, "front", defender);
    this.front.migratesTo(outer1, outer2, outer3, outer4);
    this.addSectors(outer1, outer2, outer3, outer4, front);
    
    this.defenderTroops = defenderTroops;
    this.attackerTroops = 25000;
  }

  @Override
  public Gui createGui() {
    return new Gui(this);
  }

  @Override
  public boolean isOver() {
    return false;
  }
  
  @Override
  public Set<Sector> getEntranceSectors(Player player) {
    if (!isAttacker(player) && !isDefender(player)) {
      throw new RuntimeException("Player must be part of the battle!");
    }
    if (isAttacker(player)) {
      Set<Sector> out = new HashSet<>();
      out.add(front);
      return out;
    }
    Set<Sector> out = new HashSet<>(getAllSectors());
    out.remove(front);
    return out;
  }
  
  @Override
  public void setup() {
    super.setup();
    reinforceTimer = System.currentTimeMillis();
  }
  
  @Override
  public void update(long interval) {
    if (tower.attackerPortion() >= 0.5 && towerHealth > 0) {
      towerHealth -= 0.25f;
    }
    
    if (System.currentTimeMillis() - reinforceTimer > reinforcementTime * interval) {
      int num = random.nextInt(reinforceMax - reinforceMin + 1) + reinforceMin;
      this.attackerTroops += num;
      reinforceTimer = System.currentTimeMillis();
    }
    
    for (Sector s : getAllSectors()) {
      s.fight();
    }
    attacker().update(interval);
    defender().update(interval);
  }
  
  /*
   * Private Sector classes for OstagarBattle
   */
  private class InnerSector extends Sector {
    public InnerSector(Battle battle, String name) {
      super(battle, name);
      this.attackerSurvival = 0.4f;
      this.defenderSurvival = 0.6f;
    }
    
    @Override
    public boolean canDefendersPass() {
      return defenderPortion() >= 0.4;
    }
    
    @Override
    public boolean canAttackersPass() {
      return attackerPortion() >= 0.8;
    }
    
    @Override
    public boolean canMigrateTo(Player player, Sector sector) {
      if (isDefender(player)) {
        return true;
      }
      return super.canMigrateTo(player, sector);
    }
  }
  private class OuterSector extends Sector {
    public OuterSector(Battle battle, String name) {
      super(battle, name);
      this.attackerSurvival = 0.4f;
      this.defenderSurvival = 0.6f;
    }
    
    @Override
    public boolean canMigrateTo(Player player, Sector sector) {
      if (isDefender(player)) {
        return true;
      }
      return super.canMigrateTo(player, sector);
    }
    
    @Override
    public boolean canDefendersPass() {
      return defenderPortion() >= 0.5;
    }
    
    @Override
    public boolean canAttackersPass() {
      return attackerPortion() >= 0.7;
    }
  }
  private class FrontSector extends Sector {
    private double initialDefending;
    public FrontSector(Battle battle, String name, Player defender) {
      super(battle, name);
      this.addUnits(defender, 20000);
      initialDefending = this.getDefenders();
    }
    
    @Override
    public boolean canDefendersPass() {
      return false;
    }
    
    @Override
    public boolean canAttackersPass() {
      return (this.getDefenders() / initialDefending) < 0.5;
    }
  }
}
