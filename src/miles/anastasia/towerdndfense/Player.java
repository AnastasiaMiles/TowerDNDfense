package miles.anastasia.towerdndfense;

public abstract class Player {
  private Battle battle;
  boolean running;
  
  void setBattle(Battle battle) {
    if (inBattle()) {
      throw new RuntimeException("Cannot change battle when Player is currently active in one!");
    }
    this.battle = battle;
  }
  
  public final Battle getBattle() {
    return this.battle;
  }
  
  public boolean inBattle() {
    return running;
  }
  
  public abstract void update(long interval);
}
