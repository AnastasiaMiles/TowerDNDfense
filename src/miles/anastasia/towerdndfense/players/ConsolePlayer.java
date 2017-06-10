package miles.anastasia.towerdndfense.players;

import miles.anastasia.towerdndfense.Player;
import miles.anastasia.towerdndfense.Sector;

public class ConsolePlayer extends Player {
  volatile String command = "";
  
  public void processCommand(String command) {
    this.command = command;
  }

  @Override
  public void update(long interval) {
    if (command == "") { return; }
    String c = command.toLowerCase();
    String[] parts = c.split(" ");
    if (parts[0].equals("move")) {
      Sector sourceSector = getSector(parts[1]);
      Sector destinationSector = getSector(parts[2]);
      int numToMove = Integer.parseInt(parts[3]);
      sourceSector.migrateTo(this, destinationSector, numToMove);
    } else if (parts[0].equals("add")) {
      Sector sourceSector = getSector(parts[1]);
      int numToAdd = Integer.parseInt(parts[2]);
      getBattle().addTroopsTo(sourceSector, numToAdd, this);
    }
      
    command = "";
  }
  
  private Sector getSector(String name) {
    for (Sector s : getBattle().getAllSectors()) {
      if (s.getName().equalsIgnoreCase(name)) {
        return s;
      }
    }
    return null;
  }
}
