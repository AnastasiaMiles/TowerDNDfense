
// To do:
// Make the survival chances parameters
// Improve algorithm for determining if enemies vs allies
// can bleed through
// make proportions parameters

public class OuterSector extends Sector {
  private InnerSector innerOne;
  private InnerSector innerTwo;

  public OuterSector(int ally, int enemy, InnerSector innerOne, InnerSector innerTwo) {
    super(ally, enemy, 0.6, 0.4);
    this.innerOne = innerOne;
    this.innerTwo = innerTwo;
  }

  public boolean enemyCanPass() {
    return (enemyProportion() >= 0.7);
  }

  public boolean allyCanPass() {
    return (allyProportion() >= 0.5);
  }

  public InnerSector one() {
    return innerOne;
  }

  public InnerSector two() {
    return innerTwo;
  }
}
