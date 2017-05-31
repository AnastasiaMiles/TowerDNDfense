
public class Front extends Sector{
	private int initialForces;
	public Front(int ally, int enemy) {
		super(ally, enemy, 0.6, 0.4);
		initialForces = ally;
	}
	
	public boolean enemyCanPass() {
		if(this.getAllyNumbers() / initialForces < 0.5) {
			return true;
		}
		return false;
	}
	
	public boolean allyCanPass() {
		return false;
	}
}