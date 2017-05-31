import java.util.Scanner;

public class App implements Runnable{
	volatile String command = "";
	
	public void run() {
		BattleField b = new BattleField();
		long reinforcementTimer = System.currentTimeMillis();
		BattleGui gui = new BattleGui();
		while(b.getTroopNumbers() > 0) {
			if(command != "") {
				processInput(b, command);
				command = "";
			}
			b.update();
			if(System.currentTimeMillis() - reinforcementTimer > 180000) {
				int enemy = 1000 + (int)(Math.random() * ((10000 - 3000) + 1));
				b.enemyReinforce(enemy);
				reinforcementTimer = System.currentTimeMillis();
			}
			updateGui(gui, b);
			try {
				//Thread.sleep(10);
				Thread.sleep(1000);
			} catch(InterruptedException e) {}
		}
	}
	
	
	public static void main(String[] args) {
		App app = new App();
		Thread t = new Thread(app);
		t.start();
		Scanner s = new Scanner(System.in);
		while(true) {
			if(s.hasNext()) {
				String str = s.nextLine();
				if(str == "stop") {
					break;
				}
				System.out.println(str);
				app.command = str;
			}
		}
	}
	
	private String processInput(BattleField b, String command) {
		command = command.toLowerCase();
		System.out.println(command);
		String[] parts = command.split(" ");
		Sector destination;
		Sector source;
		int number;
		if(parts[0].compareTo("move") == 0) {
			source = interpretSector(b, parts[1]);
			destination = interpretSector(b, parts[2]);
			number = Integer.parseInt(parts[3]);
			if(source != b.getFront()) {
				b.move(source, destination, number);
				
			}
		} else if(parts[0].compareTo("add") == 0) {
			destination = interpretSector(b, parts[1]);
			number = Integer.parseInt(parts[2]);
			if(destination != b.getFront()) {
				b.add(destination, number);
				System.out.println(destination.getAllyNumbers());
			}
		} else if(parts[0].compareTo("abandon") == 0) {
			destination = interpretSector(b, parts[1]);
			if(destination != b.getFront()) {
				b.abandon(destination);
			}
		} else if(parts[0].compareTo("advantage") == 0) {
			destination = interpretSector(b, parts[1]);
			if(destination != b.getFront()) {
				b.giveAdvantage(destination);
			}
		}
		return parts[0];
	}
	
	private Sector interpretSector(BattleField b, String sec) {
		Sector target = b.getFront();
		switch(sec) {
			case "outer1":
				target = b.getOuters()[0];
				break;
			case "outer2":
				target = b.getOuters()[1];
				break;
			case "outer3":
				target = b.getOuters()[2];
				break;
			case "outer4":
				target = b.getOuters()[3];
				break;
			case "inner1":
				target = b.getInners()[0];
				break;
			case "inner2":
				target = b.getInners()[1];
				break;
			case "inner3":
				target = b.getInners()[2];
				break;
			case "inner4":
				target = b.getInners()[3];
				break;
			case "inner5":
				target = b.getInners()[4];
				break;
			case "inner6":
				target = b.getInners()[5];
				break;
			case "inner7":
				target = b.getInners()[6];
				break;
			case "inner8":
				target = b.getInners()[7];
				break;
			case "tower":
				target = b.getTower();
				break;
		}
		return target;
	}
	
	private void updateGui(BattleGui gui, BattleField b) {
		gui.frontTroops(b.getFrontNumbers());
		gui.commandTroops(b.getTroopNumbers());
		gui.setUnassignedTroops(b.getFreeNumbers());
		Sector[] temp = b.getOuters();
		for(int i = 0; i < temp.length; i++) {
			gui.setOuterSector(i, temp[i].getAllyNumbers(), temp[i].getEnemyNumbers());
		}
		temp = b.getInners();
		for(int i = 0; i < temp.length; i++) {
			gui.setInnerSector(i, temp[i].getAllyNumbers(), temp[i].getEnemyNumbers());
		}
		gui.setTower(b.getTower().getAllyNumbers(), b.getTower().getEnemyNumbers());
		gui.setFront(b.getFront().getAllyNumbers(), b.getFront().getEnemyNumbers());
		gui.setTowerFallen(b.towerHasFallen());
	}
}
