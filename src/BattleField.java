import java.lang.Math;

//To do:
	//Improve migration algorithm (move random numbers, fix timing)
	//Improve reinforcement timing (make more random), numbers, and movement
	//Fix a number of functions that use exactly 8 inner and 4 outer
		//sectors so that this can be more flexible
	//Incorporate a function to make backup troops accessible
	//Incorporate a function to make front accessible
	//Make the survival chance for each sector flexible / input based
	//Improve advantage system to be more diverse than just increasing
		//survival chance by .1
	//Incorporate option to make the battle last approx for an input
		//amount of time: ie have tower fall ~45 min, have battle end ~1hr
		//while maintaining seemingly random encounters/fights/deaths
	//Improve algorithms to determine whether enemy or friendly
		//Controls a sector / can bleed through it
	//Incorporate consequences of controlling / losing sectors
	//Incorporate the option to not make the battle one-sided
		//ie don't make Darkspawn have an infinite number
		//Make this an actual realistic battle simulator where
		//the outcome depends on strategy and skill

public class BattleField {
	private InnerSector[] innerSectors;
	private OuterSector[] outerSectors;
	private Tower tower;
	private Front front;
	private double towerHealth = 100;
	private int frontTroops;
	private int freeTroops;
	private int controlledTroops;
	private int backupTroops;
	
	public BattleField(int frontNum, int controlledNum, int backupNum, int outerNum) {
		frontTroops = frontNum;
		freeTroops = controlledTroops = controlledNum;
		backupTroops = backupNum;
		innerSectors = new InnerSector[outerNum*2];
		initializeInners();
		outerSectors = new OuterSector[outerNum];
		initializeOuters();
		front = new Front(frontTroops, 25000);
		tower = new Tower(0, 0);
	}
	
	private void initializeInners() {
		for(int i = 0; i < innerSectors.length; i++) {
			innerSectors[i] = new InnerSector(0, 0);
		}
	}
	
	private void initializeOuters() {
		for(int i = 0; i < outerSectors.length; i++) {
			outerSectors[i] = new OuterSector(0, 0, innerSectors[2*i], innerSectors[2*i + 1]);
		}
	}
	
	public boolean towerHasFallen() {
		return towerHealth <= 0;
	}
	
	public int getFrontNumbers() {
		return frontTroops;
	}
	
	public int getTroopNumbers() {
		return controlledTroops;
	}
	
	public int getFreeNumbers() {
		return freeTroops;
	}
	
	public int getBackupNumbers() {
		return backupTroops;
	}
	
	public double towerHealth() {
		return towerHealth;
	}
	
	public OuterSector[] getOuters() {
		return outerSectors;
	}
	
	public InnerSector[] getInners() {
		return innerSectors;
	}
	
	public Tower getTower() {
		return tower;
	}
	
	public Front getFront() {
		return front;
	}
	
	public void add(Sector s, int number) {
		if(number <= freeTroops) {
			freeTroops -= number;
			s.addAlly(number);
		}
	}
	
	public void move(Sector source, Sector destination, int number) {
		if(source.getAllyNumbers() >= number) {
			source.removeAlly(number);
			destination.addAlly(number);
		}
	}
	
	public void abandon(Sector s) {
		int number = s.getAllyNumbers();
		s.removeAlly(number);
		freeTroops += number;
	}
	
	public void giveAdvantage(Sector s) {
		s.addAllyAdvantage(.1);
	}
	
	public void removeAdvantage(Sector s) {
		s.removeAllyAdvantage(.1);
	}
	
	public void enemyReinforce(int enemy) {
		front.addEnemy(enemy);
	}
	
	public void update() {
		if(tower.enemyProportion() >= 0.5) {
			towerHealth -= 0.25;
		}
		int tempTotalTroops = 0;
		front.fight();
		frontTroops = front.getAllyNumbers();
		for(int i = 0; i < outerSectors.length; i++) {
			outerSectors[i].fight();
			tempTotalTroops += outerSectors[i].getAllyNumbers();
			if(outerSectors[i].allyHasAdvantage) {
				if(System.currentTimeMillis() - outerSectors[i].timeAdvantageStarted > 2000) {
					removeAdvantage(outerSectors[i]);
				}
			}
		}
		for(int i = 0; i < innerSectors.length; i++) {
			innerSectors[i].fight();
			tempTotalTroops += innerSectors[i].getAllyNumbers();
			if(innerSectors[i].allyHasAdvantage) {
				if(System.currentTimeMillis() - innerSectors[i].timeAdvantageStarted > 2000) {
					removeAdvantage(innerSectors[i]);
				}
			}
		}
		tower.fight();
		tempTotalTroops += tower.getAllyNumbers();
		if(tower.allyHasAdvantage) {
			if(System.currentTimeMillis() - tower.timeAdvantageStarted > 12000) {
				removeAdvantage(tower);
			}
		}
		migrate();
		controlledTroops = tempTotalTroops + freeTroops;
	}
	
	public void migrate() {
		migrateFront();
		for(int i = 0; i < outerSectors.length; i++) {
			migrateFurther(outerSectors[i]);
		}
	}
	
	private void migrateFront() {
		if(front.enemyProportion() >= 0.6) {
			int frontNum = front.getEnemyNumbers();
			int numPerSection = frontNum / 500;
			for(int i = 0; i < 10; i++) {
				double move = Math.random();
				if(move >= 0.6) {
					front.removeEnemy(numPerSection);
				}
				else {
					return;
				}
				if(move >= 0.6 && move < 0.7) {
					outerSectors[0].addEnemy(numPerSection);
				}
				else if(move >= 0.7 && move < 0.8) {
					outerSectors[1].addEnemy(numPerSection);
				}
				else if(move >= 0.8 && move < 0.9) {
					outerSectors[2].addEnemy(numPerSection);
				}
				else if(move >= 0.9 && move < 1) {
					outerSectors[3].addEnemy(numPerSection);
				}
			}
		}
	}
	
	private void migrateFurther(OuterSector outer) {
		int numPerSection;
		if(outer.enemyProportion() >= 0.7) {
			int outerNum = outer.getEnemyNumbers();
			numPerSection = outerNum / 250;
			for(int i = 0; i < 20; i++) {
				double move = Math.random();
				if(move >= 0.7) {
					outer.removeEnemy(numPerSection);
					if(move >= 0.7 && move < 0.85) {
						outer.one().addEnemy(numPerSection);
					} else {
						outer.two().addEnemy(numPerSection);
					}
				}
			}
		}
		
		if(outer.one().enemyProportion() >= 0.8 && outer.two().enemyProportion() >= 0.8) {
			int innerOneNum = outer.one().getEnemyNumbers(); 
			numPerSection = innerOneNum / 100;
			for(int i = 0; i < 20; i++) {
				double move = Math.random();
				if(move >= 0.8) {
					outer.one().removeEnemy(numPerSection);
					tower.addEnemy(numPerSection);
				}
			}
			int innerTwoNum = outer.two().getEnemyNumbers(); 
			numPerSection = innerTwoNum / 100;
			for(int i = 0; i < 20; i++) {
				double move = Math.random();
				if(move >= 0.8) {
					outer.two().removeEnemy(numPerSection);
					tower.addEnemy(numPerSection);
				}
			}
		}
	}
	
	
	/* The following are temporarily disabled functions for
	 * migrating income enemy reinforcements.
	 
	private void propagate(int remainingEnemies) {
		int temp;
		if(remainingEnemies > 0) {
			for(int i = 0; i < outerSectors.length; i++) {
				temp = reinforceOuter(remainingEnemies/4, outerSectors[i]);
				if(temp > 0) {
					reinforceInner((int)temp/2, outerSectors[i].one());
					reinforceInner((int)temp/2, outerSectors[i].two());
				}
			}
		}
	}
	
	private int reinforceFront(int enemy) {
		if(!front.enemyCanPass()) {
			front.addEnemy(enemy);
			return 0;
		}
		if(front.allyProportion() == 0) {
			return enemy;
		}
		int remainder = 0;
		for(int i = 0; i < 5; i++) {
			double move = Math.random();
			if(move >= 0.5) {
				remainder += (int) (enemy / 5);
			} else {
				front.addEnemy((int)(enemy/5));
			}
		}
		return remainder;
	}
	
	private int reinforceOuter(int enemy, OuterSector outer) {
		if(!outer.enemyCanPass()) {
			outer.addEnemy(enemy);
			return 0;
		}
		if(outer.allyProportion() == 0) {
			return enemy;
		}
		int remainder = 0;
		for(int i = 0; i < 3; i++) {
			double move = Math.random();
			if(move >= 0.5) {
				remainder += (int) (enemy / 3);
			} else {
				outer.addEnemy((int)(enemy/3));
			}
		}
		return remainder;
	}
	
	private void reinforceInner(int enemy, InnerSector inner) {
		if(!inner.enemyCanPass()) {
			inner.addEnemy(enemy);
			return;
		}
		if(inner.allyProportion() == 0) {
			tower.addEnemy(enemy);
		}
		for(int i = 0; i < 2; i++) {
			double move = Math.random();
			if(move >= 0.5) {
				tower.addEnemy((int) (enemy / 2));
			} else {
				inner.addEnemy((int)(enemy/2));
			}
		}
	}
	*/
}