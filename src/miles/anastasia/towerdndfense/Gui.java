package miles.anastasia.towerdndfense;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Gui {
  private final Battle battle;
  private final JFrame frame;
  
  private JLabel attackerTroops;
  private JLabel defenderTroops;
  private Map<String, JLabel[]> sectors;
  
  public Gui(Battle battle) {
    this.battle = battle;
    this.frame = new JFrame();
    this.sectors = new HashMap<>();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Battle GUI");
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setLayout(null);
    
    JLabel attackerTroopsText = new JLabel("Attacker Troops: "); 
    attackerTroopsText.setBounds(10, 40, 80, 20);
    frame.add(attackerTroopsText);
    
    attackerTroops = new JLabel("0");
    attackerTroops.setBounds(90, 40, 110, 20);
    frame.add(attackerTroops);
    
    JLabel defenderTroopsText = new JLabel("Defender Troops: ");
    defenderTroopsText.setBounds(10, 60, 180, 20);
    frame.add(defenderTroopsText);
    
    defenderTroops = new JLabel("0");
    defenderTroops.setBounds(135, 60, 100, 20);
    frame.add(defenderTroops);
    
    JPanel sectorPanel = new JPanel(new GridLayout(0, 3));
    sectorPanel.add(new JLabel("SECTOR", SwingConstants.CENTER));
    sectorPanel.add(new JLabel("DEFENDING", SwingConstants.CENTER));
    sectorPanel.add(new JLabel("ATTACKING", SwingConstants.CENTER));
    sectorPanel.setPreferredSize(new Dimension(600, 400));
    sectorPanel.setBounds(75, 140, 600, 300);
    
    
    
    for (Sector s : battle.getAllSectors()) {
      sectorPanel.add(new JLabel(s.getName(), SwingConstants.CENTER));
     sectors.put(s.getName(), new JLabel[] { new JLabel("0", SwingConstants.CENTER),
         new JLabel("0", SwingConstants.CENTER) });
      sectorPanel.add(sectors.get(s.getName())[0]);
      sectorPanel.add(sectors.get(s.getName())[1]);
    }
    
    frame.add(sectorPanel, BorderLayout.CENTER);
    frame.repaint();
  }
  
  public void update() {
    attackerTroops.setText(NumberFormat.getNumberInstance(Locale.US).format(battle.attackerTroops));
    defenderTroops.setText(NumberFormat.getNumberInstance(Locale.US).format(battle.defenderTroops));
    for (Sector s : battle.getAllSectors()) {
      JLabel[] data = sectors.get(s.getName());
      data[0].setText(NumberFormat.getNumberInstance(Locale.US).format(s.getDefenders()));
      data[1].setText(NumberFormat.getNumberInstance(Locale.US).format(s.getAttackers()));
    }
  }
}
