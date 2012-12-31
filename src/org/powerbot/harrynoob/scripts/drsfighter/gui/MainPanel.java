package org.powerbot.harrynoob.scripts.drsfighter.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;

import static org.powerbot.harrynoob.scripts.drsfighter.misc.Variables.bankLocations;
import static org.powerbot.harrynoob.scripts.drsfighter.misc.Variables.food;

public class MainPanel extends JFrame implements WindowListener {

	//Nodig:
	//Checkbuttons: - Rejuvenate
	// 				- Weapon Switching
	// 				- Debugging
	// 				- Food banken?
	// 				- Charms?
	//				Later:
	//					- Worldhoppen?
	//Uitklappen: 	- Locatie
	//				- 2h wapen
	//				- Shield
	//				- Bank?
	// 				Later:
	//					- Offhand support
	//Save button
	// 
	
	private JTabbedPane tabbedPane;

	//weaponSetupPane:
	//		- Weapon Switching
	// 		- 2h wapen
	//   	- Shield
	//		- Rejuv?
	//		Later:
	//			- Offhand
	
	private JComponent weaponSetupPane;
	private JCheckBox rejuvenateBox;
	private JCheckBox weaponSwitchButton;
	private JComboBox<String> weaponBox;
	private JComboBox<String> shieldBox;
	private JLabel weaponLabel;
	private JLabel shieldLabel;
	
	
	//bankPane:
	// 		- Banking checkbutton
	// 		- Banking locatie
	// 		- Food uitklappen
	//		- Locatie
	private JComponent bankPane;
	private JCheckBox bankCheckBox;
	private JCheckBox foodCheckBox;
	private JComboBox foodComboBox;
	private JComboBox locationBox;
	
	//finishPane:
	//		- Start button
	// 		- Save button
	private JComponent finishPane;
	private JButton startButton;
	private JButton saveButton;
	
	private PanelListener panelListener;
	private static JPanel mainPanel;
	public MainPanel()
	{
		mainPanel = new JPanel(new GridLayout(1,1));
		panelListener = new PanelListener(this);
		tabbedPane = new JTabbedPane();
		weaponSetupPane = weaponSetupPane();
		bankPane = bankPane();
		bankPane.setEnabled(false);
		finishPane = finishTab();
		tabbedPane.add("Basic", weaponSetupPane);
		//tabbedPane.add("Banking", bankPane);
		tabbedPane.add("Finish", finishPane);
		mainPanel.add(tabbedPane);
		add(mainPanel);
		//tabbedPane.add(finishPane);
	}
	    
    private JComponent weaponSetupPane()
    {
    	System.out.println("Started setting up weapon..");
    	JPanel panel = new JPanel(false);
    	rejuvenateBox = new JCheckBox("Use Rejuv", true);
    	rejuvenateBox.addActionListener(panelListener);
    	rejuvenateBox.setName("rejuvenateBox");
    	weaponSwitchButton = new JCheckBox("Switch weapons", false);
    	weaponSwitchButton.setName("weaponSwitchButton");
    	weaponSwitchButton.addActionListener(panelListener);
    	weaponLabel = new JLabel("2h/offhand: ");
    	//Temp "", otherwise none
    	weaponBox = getInventoryNames() != null ? new JComboBox<String>(getInventoryNames()) : new JComboBox<String>(new String[] { "" });
    	weaponBox.setEnabled(false);
    	shieldLabel = new JLabel("Shield: ");
    	shieldBox = getInventoryNames() != null ? new JComboBox<String>(getInventoryNames()) : new JComboBox<String>(new String[] { "" });
    	shieldBox.setEnabled(false);
    	panel.setLayout(new GridLayout(3, 2));
        panel.add(rejuvenateBox);
        panel.add(weaponSwitchButton);
        panel.add(weaponLabel);
        panel.add(weaponBox);
        panel.add(shieldLabel);
        panel.add(shieldBox);
    	System.out.println("Set up weapon!");
        return panel;
    }
    
    private JComponent bankPane()
    {
    	System.out.println("Started with bankpane");
    	JPanel panel = new JPanel(new GridLayout(2,2));
    	bankCheckBox = new JCheckBox("Enabled banking", false);
    	bankCheckBox.setName("bankCheckBox");
    	bankCheckBox.addActionListener(panelListener);
    	foodCheckBox = new JCheckBox("Withdraw food", false);
    	foodCheckBox.setEnabled(false);
    	foodCheckBox.setName("foodCheckBox");
    	foodCheckBox.addActionListener(panelListener);
    	foodComboBox = new JComboBox<String>(food);
    	foodComboBox.setEnabled(false);
    	locationBox = new JComboBox<String>(bankLocations);
    	locationBox.setEnabled(false);
    	System.out.println("Done with bankpane");
    	panel.add(bankCheckBox);
    	panel.add(foodCheckBox);
    	panel.add(locationBox);
    	panel.add(foodComboBox);
    	return panel;
    }
    
    private JComponent finishTab()
    {
    	JPanel panel = new JPanel(new BorderLayout());
    	startButton = new JButton("Start!");
    	startButton.addActionListener(panelListener);
    	startButton.setName("start");
    	panel.add(startButton, BorderLayout.CENTER);
    	return panel;
    }
    
    private String[] getInventoryNames()
    {
    	HashSet<String> s = new HashSet<String>();
    	for(Item i : Inventory.getAllItems(false))
    	{
    		if(i != null && i.getWidgetChild().validate() && !s.contains(i.getName())) s.add(i.getName());
    	}
    	return s.size() > 0 ? (String[])s.toArray(new String[]{"nanana"}) : null;
    }
    
    public void changeRejuvenate()
    {
    	weaponSwitchButton.setEnabled(rejuvenateBox.isSelected());
    	weaponBox.setEnabled(rejuvenateBox.isSelected());
    	shieldBox.setEnabled(rejuvenateBox.isSelected());
    }
    
    public void changeWeaponSwitch()
    {
    	weaponBox.setEnabled(weaponSwitchButton.isSelected());
    	shieldBox.setEnabled(weaponSwitchButton.isSelected());
    }
    
    public void changeFood()
    {
    	foodComboBox.setEnabled(foodCheckBox.isSelected());
    }
    
    public void changeBank()
    {
    	foodCheckBox.setEnabled(bankCheckBox.isSelected());
    	foodComboBox.setEnabled(bankCheckBox.isSelected());
    	locationBox.setEnabled(bankCheckBox.isSelected());
    }

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public String[] stringOptions()
	{
		String[] s = new String[4];
		s[0] = (String)weaponBox.getSelectedItem();
		s[1] = (String)shieldBox.getSelectedItem();
		s[2] = (String)foodComboBox.getSelectedItem();
		s[3] = (String)locationBox.getSelectedItem();
		return s;
	}
	
	public boolean[] booleanOptions()
	{
		boolean[] b = new boolean[4];
		b[0] = rejuvenateBox.isSelected();
		b[1] = weaponSwitchButton.isSelected() && b[0];
		b[2] = bankCheckBox.isSelected();
		b[3] = foodCheckBox.isSelected() && b[2];
		return b;
	}
    
}
