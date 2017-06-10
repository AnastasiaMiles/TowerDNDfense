package miles.anastasia.towerdndfense.players;

import java.util.Random;
import java.util.Set;

import miles.anastasia.towerdndfense.Player;
import miles.anastasia.towerdndfense.Sector;

public class AttackerDarkspawn extends Player {
  private final Random random;
  private int minToMigrate = 1000;
  private int minPerSector = 300;
  
  public AttackerDarkspawn() {
    this.random = new Random();
  }
  
  @Override
  public void update(long interval) {
    Set<Sector> entrances = getBattle().getEntranceSectors(this);
    while (getBattle().getTroops(this) > 0) {
      for (Sector s : entrances) {
        getBattle().addTroopsTo(s, random.nextInt(getBattle().getTroops(this) + 1), this);
        if (getBattle().getTroops(this) == 0) { break; }
      }
    }
    
    for (Sector s : getBattle().getAllSectors()) {
      if (s.canAttackersPass() && s.getAttackers() > minToMigrate) {
        for (Sector next : s.migratesTo()) {
          if (s.getAttackers() <= minPerSector) {
            break;
          }
          if (!random.nextBoolean()) {
            int num = random.nextInt(minToMigrate - minPerSector);
            if (s.getAttackers() - num >= s.getDefenders()) {
              s.migrateTo(this, next, num);
            }
          }
        }
      }
    }
  }
}
