// To do:
// Make the survival chances parameters
public class Tower extends Sector {
  public Tower(int ally, int enemy) {
    super(ally, enemy, 0.6, 0.4);
  }

  public boolean enemyCanPass() {
    return false;
  }

  public boolean allyCanPass() {
    return false;
  }
}
