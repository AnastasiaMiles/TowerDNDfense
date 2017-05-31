import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BattleGui {	
	private final JFrame frame;
	
	private final JLabel frontTroops;
	private final JLabel commandTroops;
	private final JLabel unassignedTroops;
	
	private final JLabel[] front;
	private final JLabel[] outerSectors;
	private final JLabel[] innerSectors;
	private final JLabel[] tower;
	private final List<JLabel> generalLabels;
	private final JLabel hasFallen;
	
	private static int outerSectorCount = 4;
	private static int innerSectorCount = 8;
	
	public BattleGui() {
		this.frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Battle GUI");
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLayout(null);
		
		this.generalLabels = new ArrayList<>();
		this.outerSectors = new JLabel[outerSectorCount * 2];
		this.innerSectors = new JLabel[innerSectorCount * 2];
		this.tower = new JLabel[2];
		this.front = new JLabel[2];
		
		JLabel dsText = new JLabel("Darkspawn Troops:  ??");
		dsText.setBounds(10, 20, 155, 20);
		frame.add(dsText);
		generalLabels.add(dsText);
		
		JLabel frontTroopsText = new JLabel("Front Troops: ");
		frontTroopsText.setBounds(10, 40, 80, 20);
		frame.add(frontTroopsText);
		generalLabels.add(frontTroopsText);
		
		frontTroops = new JLabel("0");
		frontTroops.setBounds(90, 40, 110, 20);
		frame.add(frontTroops);
		
		JLabel commandTroopsText = new JLabel("Commanded Troops: ");
		commandTroopsText.setBounds(10, 60, 180, 20);
		frame.add(commandTroopsText);
		generalLabels.add(commandTroopsText);
		
		commandTroops = new JLabel("0");
		commandTroops.setBounds(135, 60, 100, 20);
		frame.add(commandTroops);
		
		JLabel unassignedTroopsText = new JLabel("Unassigned Troops: ");
		unassignedTroopsText.setBounds(10, 80, 180, 20);
		frame.add(unassignedTroopsText);
		generalLabels.add(unassignedTroopsText);
		
		unassignedTroops = new JLabel("0");
		unassignedTroops.setBounds(135, 80, 100, 20);
		frame.add(unassignedTroops);
		
		JLabel loghainTroops = new JLabel("Loghain Troops:  50,000");
		loghainTroops.setBounds(10, 100, 180, 20);
		frame.add(loghainTroops);
		generalLabels.add(loghainTroops);
		
		hasFallen = new JLabel("Tower Has Fallen");
		hasFallen.setFont(new Font("Courier", Font.BOLD,20));
		hasFallen.setBounds(500, 20, 300, 60);
		hasFallen.setVisible(false);
		hasFallen.setForeground(Color.RED);
		frame.add(hasFallen);
		
		JPanel sectorPanel = new JPanel(new GridLayout(0, 3));
		JLabel temp;
		sectorPanel.add(temp = new JLabel("SECTOR", SwingConstants.CENTER));
		generalLabels.add(temp);
		sectorPanel.add(temp = new JLabel("ALLIES", SwingConstants.CENTER));
		generalLabels.add(temp);
		sectorPanel.add(temp = new JLabel("DARKSPAWN", SwingConstants.CENTER));
		generalLabels.add(temp);
		sectorPanel.setPreferredSize(new Dimension(600, 400));
		sectorPanel.setBounds(75, 140, 600, 300);
		
		
		sectorPanel.add(temp = new JLabel("FRONT", SwingConstants.LEFT));
		generalLabels.add(temp);
		sectorPanel.add((front[0] = new JLabel("0", SwingConstants.CENTER)));
		sectorPanel.add((front[1] = new JLabel("0", SwingConstants.CENTER)));
		for (int i = 0; i < outerSectorCount; i++) {
			sectorPanel.add(temp = new JLabel("OUTER SECTOR  " + (i+1), SwingConstants.LEFT));
			generalLabels.add(temp);
			sectorPanel.add((outerSectors[i*2] = new JLabel("0", SwingConstants.CENTER)));
			sectorPanel.add((outerSectors[i*2 + 1] = new JLabel("0", SwingConstants.CENTER)));
		}
		for (int i = 0; i < innerSectorCount; i++) {
			sectorPanel.add(temp = new JLabel("INNER SECTOR    " + (i+1), SwingConstants.LEFT));
			generalLabels.add(temp);
			sectorPanel.add((innerSectors[i*2] = new JLabel("0", SwingConstants.CENTER)));
			sectorPanel.add((innerSectors[i*2 + 1] = new JLabel("0", SwingConstants.CENTER)));
		}
		sectorPanel.add(temp = new JLabel("TOWER", SwingConstants.LEFT));
		generalLabels.add(temp);
		sectorPanel.add((tower[0] = new JLabel("0", SwingConstants.CENTER)));
		sectorPanel.add((tower[1] = new JLabel("0", SwingConstants.CENTER)));
		
		frame.add(sectorPanel, BorderLayout.CENTER);	
		frame.repaint();
	}
	
	public void frontTroops(int count) {
		this.frontTroops.setText(NumberFormat.getNumberInstance(Locale.US).format(count));
	}
	
	public void commandTroops(int count) {
		this.commandTroops.setText(NumberFormat.getNumberInstance(Locale.US).format(count));
	}
	
	public void setUnassignedTroops(int count) {
		this.unassignedTroops.setText(NumberFormat.getNumberInstance(Locale.US).format(count));
	}
	
	public void setOuterSector(int sectorNum, int allyNum, int enemyNum) {
		this.outerSectors[sectorNum*2].setText(NumberFormat.getNumberInstance(Locale.US).format(allyNum));
		this.outerSectors[(sectorNum*2) + 1].setText(NumberFormat.getNumberInstance(Locale.US).format(enemyNum));
	}
	
	public void setInnerSector(int sectorNum, int allyNum, int enemyNum) {
		this.innerSectors[sectorNum*2].setText(NumberFormat.getNumberInstance(Locale.US).format(allyNum));
		this.innerSectors[(sectorNum*2) + 1].setText(NumberFormat.getNumberInstance(Locale.US).format(enemyNum));
	}
	
	public void setTower(int allyNum, int enemyNum) {
		this.tower[0].setText(NumberFormat.getNumberInstance(Locale.US).format(allyNum));
		this.tower[1].setText(NumberFormat.getNumberInstance(Locale.US).format(enemyNum));
	}
	
	public void setFront(int allyNum, int enemyNum) {
		this.front[0].setText(NumberFormat.getNumberInstance(Locale.US).format(allyNum));
		this.front[1].setText(NumberFormat.getNumberInstance(Locale.US).format(enemyNum));
	}
	
	public void setTowerFallen(boolean value) {
		Color color = value ? Color.RED : Color.BLACK;
		for (JLabel label : generalLabels) {
			label.setForeground(color);
		}
		for (int i = 0; i < outerSectorCount*2; i++) {
			outerSectors[i].setForeground(color);
		}
		for (int i = 0; i < innerSectorCount*2; i++) {
			innerSectors[i].setForeground(color);
		}
		frontTroops.setForeground(color);
		commandTroops.setForeground(color);
		unassignedTroops.setForeground(color);
		tower[0].setForeground(color);
		tower[1].setForeground(color);
		front[0].setForeground(color);
		front[1].setForeground(color);
		hasFallen.setVisible(value);
	}
}