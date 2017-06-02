import java.lang.Math;

// To do:
// Improve fighting algorithm
// more realistic or random number of deaths
// that responds to differences in numbers
// and survival chance
// One big issue is that if one side is vastly outnumbered
// the opponent will very very rarely see a death
// ...It becomes unbalanced fast


public abstract class Sector {
  private int enemy;
  private int ally;
  private double allySurvivalChance;
  private double enemySurvivalChance;
  public boolean allyHasAdvantage;
  public long timeAdvantageStarted;

  public Sector(int ally, int enemy, double allySurvivalChance, double enemySurvivalChance) {
    this.ally = ally;
    this.enemy = enemy;
    this.allySurvivalChance = allySurvivalChance;
    this.enemySurvivalChance = enemySurvivalChance;
    this.allyHasAdvantage = false;
    this.timeAdvantageStarted = 0;
  }

  public final void addEnemy(int reinforcements) {
    enemy += reinforcements;
  }

  public final void addAlly(int reinforcements) {
    ally += reinforcements;
  }

  public final void removeEnemy(int number) {
    if (enemy - number >= 0) {
      enemy -= number;
    }
  }

  public final void removeAlly(int number) {
    if (ally - number >= 0) {
      ally -= number;
    }
  }

  public final int getEnemyNumbers() {
    return enemy;
  }

  public final int getAllyNumbers() {
    return ally;
  }

  public abstract boolean enemyCanPass();

  public abstract boolean allyCanPass();

  public double enemyProportion() {
    if ((enemy + ally) == 0) {
      return 0;
    }
    return (enemy / (double) (enemy + ally));
  }

  public double allyProportion() {
    if ((enemy + ally) == 0) {
      return 0;
    }
    return (ally / (double) (enemy + ally));
  }

  public final void addAllyAdvantage(double advantage) {
    allySurvivalChance += advantage;
    enemySurvivalChance -= advantage;
    allyHasAdvantage = true;
    timeAdvantageStarted = System.currentTimeMillis();
  }

  public final void removeAllyAdvantage(double advantage) {
    enemySurvivalChance += advantage;
    allySurvivalChance -= advantage;
    allyHasAdvantage = false;
    timeAdvantageStarted = 0;
  }


  public void fight() {
    if (ally == 0) {
      return;
    }
    if (enemy == 0) {
      return;
    }
    int allyFighters = (int) Math.ceil((enemy - (enemy) % 100) / 100);
    int enemyFighters = (int) Math.ceil((ally - (ally) % 100) / 100);
    int allyDeaths = 0;
    int enemyDeaths = 0;
    for (int i = 0; i < allyFighters; i++) {
      double roll = Math.random();
      if (roll >= allySurvivalChance) {
        allyDeaths++;
      }
    }
    for (int i = 0; i < enemyFighters; i++) {
      double roll = Math.random();
      if (roll >= enemySurvivalChance) {
        enemyDeaths++;
      }
    }
    ally -= allyDeaths;
    if (ally < 0) {
      ally = 0;
    }
    enemy -= enemyDeaths;
    if (enemy < 0) {
      enemy = 0;
    }
  }
}
