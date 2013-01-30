package org.harrynoob.scripts.drsfighter.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Equipment.Slot;
import org.powerbot.game.api.wrappers.node.Item;

import static org.harrynoob.scripts.drsfighter.misc.Variables.bankLocations;
import static org.harrynoob.scripts.drsfighter.misc.Variables.food;

public class MainPanel extends JFrame implements WindowListener {

	// Nodig:
	// Checkbuttons: - Rejuvenate
	// - Weapon Switching
	// - Debugging
	// - Food banken?
	// - Charms?
	// Later:
	// - Worldhoppen?
	// Uitklappen: - Locatie
	// - 2h wapen
	// - Shield
	// - Bank?
	// Later:
	// - Offhand support
	// Save button
	//
	static final long serialVersionUID = 0;
	private JTabbedPane tabbedPane;

	// weaponSetupPane:
	// - Weapon Switching
	// - 2h wapen
	// - Shield
	// - Rejuv?
	// Later:
	// - Offhand

	private JComponent weaponSetupPane;
	private JCheckBox rejuvenateBox;
	private JCheckBox weaponSwitchButton;
	private JComboBox<String> weaponBox;
	private JComboBox<String> shieldBox;
	private JLabel weaponLabel;
	private JLabel shieldLabel;

	// bankPane:
	// - Banking checkbutton
	// - Banking locatie
	// - Food uitklappen
	// - Locatie
	private JComponent bankPane;
	private JCheckBox bankCheckBox;
	private JCheckBox foodCheckBox;
	private JComboBox<String> foodComboBox;
	private JComboBox<String> locationBox;

	// finishPane:
	// - Start button
	// - Save button
	private JComponent finishPane;
	private JButton startButton;
	private JCheckBox looting;
	private JCheckBox devMode;
	// private JButton saveButton;

	private JComponent donatePane;
	// private Image donateImage;
	private JLabel imageLabel;
	private JLabel donateLabel;

	private PanelListener panelListener;
	private static JPanel mainPanel;

	public MainPanel() {
		mainPanel = new JPanel(new GridLayout(1, 1));
		panelListener = new PanelListener(this);
		tabbedPane = new JTabbedPane();
		weaponSetupPane = weaponSetupPane();
		bankPane = bankPane();
		bankPane.setEnabled(false);
		finishPane = finishTab();
		donatePane = donateTab();
		tabbedPane.add("Basic", weaponSetupPane);
		// tabbedPane.add("Banking", bankPane);
		tabbedPane.add("Finish", finishPane);
		tabbedPane.add("Donate", donatePane);
		mainPanel.add(tabbedPane);
		add(mainPanel);
		// tabbedPane.add(finishPane);
	}

	private JComponent weaponSetupPane() {
		System.out.println("Started setting up weapon..");
		JPanel panel = new JPanel(false);
		rejuvenateBox = new JCheckBox("Use Rejuv", true);
		rejuvenateBox.addActionListener(panelListener);
		rejuvenateBox.setName("rejuvenateBox");
		weaponSwitchButton = new JCheckBox("Switch weapons", false);
		weaponSwitchButton.setName("weaponSwitchButton");
		weaponSwitchButton.addActionListener(panelListener);
		weaponLabel = new JLabel("2h/offhand: ");
		// Temp "", otherwise none
		weaponBox = getInventoryNames() != null ? new JComboBox<String>(
				getInventoryNames()) : new JComboBox<String>(
				new String[] { "" });
		weaponBox.setEnabled(false);
		shieldLabel = new JLabel("Shield: ");
		shieldBox = getInventoryNames() != null ? new JComboBox<String>(
				getInventoryNames()) : new JComboBox<String>(
				new String[] { "" });
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

	private JComponent bankPane() {
		System.out.println("Started with bankpane");
		JPanel panel = new JPanel(new GridLayout(2, 2));
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

	private JComponent finishTab() {
		JPanel panel = new JPanel(new BorderLayout());
		startButton = new JButton("Start!");
		startButton.addActionListener(panelListener);
		startButton.setName("start");
		looting = new JCheckBox("Loot charms");
		looting.setSelected(true);
		panel.add(startButton, BorderLayout.CENTER);
		panel.add(looting, BorderLayout.SOUTH);
		return panel;
	}

	private JComponent donateTab() {
		JPanel panel = new JPanel(new BorderLayout());
		imageLabel = new JLabel("Link is in forum thread", JLabel.CENTER);
		// imageLabel.addActionListener(panelListener);
		donateLabel = new JLabel("Donate €1!", JLabel.CENTER);
		devMode = new JCheckBox("Devmode (danger)");
		devMode.setSelected(false);
		panel.add(imageLabel, BorderLayout.CENTER);
		panel.add(donateLabel, BorderLayout.NORTH);
		panel.add(devMode, BorderLayout.SOUTH);
		return panel;
	}

	private String[] getInventoryNames() {
		HashSet<String> s = new HashSet<String>();
		for (Item i : Inventory.getAllItems(false)) {
			if (i != null && i.getWidgetChild().validate()
					&& !s.contains(i.getName()))
				s.add(i.getName());
		}
		if(Equipment.getItem(Slot.WEAPON) != null) {
			s.add(Equipment.getCachedItem(Slot.WEAPON).getName());
		}
		if(Equipment.getItem(Slot.SHIELD) != null) {
			s.add(Equipment.getCachedItem(Slot.SHIELD).getName());
		}
		return s.size() > 0 ? (String[]) s.toArray(new String[s.size()])
				: null;
	}

	public void changeRejuvenate() {
		weaponSwitchButton.setEnabled(rejuvenateBox.isSelected());
		weaponBox.setEnabled(rejuvenateBox.isSelected());
		shieldBox.setEnabled(rejuvenateBox.isSelected());
	}

	public void changeWeaponSwitch() {
		weaponBox.setEnabled(weaponSwitchButton.isSelected());
		shieldBox.setEnabled(weaponSwitchButton.isSelected());
	}

	public void changeFood() {
		foodComboBox.setEnabled(foodCheckBox.isSelected());
	}

	public void changeBank() {
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

	/*
	 * private Image getImage(String url) { try { URL u = new
	 * URL("https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif"); Image
	 * a = ImageIO.read(u); return a; } catch(Exception e) {
	 * e.printStackTrace(); return null; } }
	 */

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

	public String[] stringOptions() {
		String[] s = new String[4];
		s[0] = (String) weaponBox.getSelectedItem();
		s[1] = (String) shieldBox.getSelectedItem();
		s[2] = (String) foodComboBox.getSelectedItem();
		s[3] = (String) locationBox.getSelectedItem();
		return s;
	}

	public boolean[] booleanOptions() {
		boolean[] b = new boolean[5];
		b[0] = rejuvenateBox.isSelected();
		b[1] = weaponSwitchButton.isSelected() && b[0];
		b[2] = bankCheckBox.isSelected();
		b[3] = foodCheckBox.isSelected() && b[2];
		b[4] = looting.isSelected();
		Variables.mouseHop = devMode.isSelected();
		return b;
	}

}
