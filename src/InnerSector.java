

//To do:
	//Make the survival chances parameters
	//Improve algorithm for determining if enemies vs allies
		//can bleed through
		//make proportions parameters

public class InnerSector extends Sector{
	public InnerSector(int ally, int enemy) {
		super(ally, enemy, 0.6, 0.4);
	}
	
	public boolean enemyCanPass() {
		return(enemyProportion() >= 0.8);
	}
	
	public boolean allyCanPass() {
		return(allyProportion() >= 0.4);
	}
	
}
