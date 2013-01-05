package org.harrynoob.scripts.drsfighter.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NewGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewGUI window = new NewGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 230);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Weapon", null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Use Rejuvenate");
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.gridwidth = 4;
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox.gridx = 1;
		gbc_chckbxNewCheckBox.gridy = 1;
		panel.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
		
		JCheckBox chckbxUseWeaponSwitching = new JCheckBox("Use weapon switching");
		GridBagConstraints gbc_chckbxUseWeaponSwitching = new GridBagConstraints();
		gbc_chckbxUseWeaponSwitching.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxUseWeaponSwitching.gridx = 6;
		gbc_chckbxUseWeaponSwitching.gridy = 1;
		panel.add(chckbxUseWeaponSwitching, gbc_chckbxUseWeaponSwitching);
		
		JLabel lblhOffhand = new JLabel("2h / offhand");
		GridBagConstraints gbc_lblhOffhand = new GridBagConstraints();
		gbc_lblhOffhand.insets = new Insets(0, 0, 5, 5);
		gbc_lblhOffhand.gridx = 6;
		gbc_lblhOffhand.gridy = 2;
		panel.add(lblhOffhand, gbc_lblhOffhand);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 7;
		gbc_comboBox.gridy = 2;
		panel.add(comboBox, gbc_comboBox);
		
		JLabel lblMakeSureTo = new JLabel("<html>Make sure to have 2h/offhand and shield in inventory on startup!</html>");
		lblMakeSureTo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblMakeSureTo = new GridBagConstraints();
		gbc_lblMakeSureTo.anchor = GridBagConstraints.WEST;
		gbc_lblMakeSureTo.gridheight = 4;
		gbc_lblMakeSureTo.gridwidth = 5;
		gbc_lblMakeSureTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblMakeSureTo.gridx = 1;
		gbc_lblMakeSureTo.gridy = 2;
		panel.add(lblMakeSureTo, gbc_lblMakeSureTo);
		
		JLabel lblShield = new JLabel("Shield");
		GridBagConstraints gbc_lblShield = new GridBagConstraints();
		gbc_lblShield.insets = new Insets(0, 0, 5, 5);
		gbc_lblShield.gridx = 6;
		gbc_lblShield.gridy = 4;
		panel.add(lblShield, gbc_lblShield);
		
		JComboBox comboBox_1 = new JComboBox();
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 7;
		gbc_comboBox_1.gridy = 4;
		panel.add(comboBox_1, gbc_comboBox_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Banking", null, panel_1, null);
		tabbedPane.setEnabledAt(1, false);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Other", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Donate", null, panel_3, null);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Finish", null, panel_4, null);
	}

}
