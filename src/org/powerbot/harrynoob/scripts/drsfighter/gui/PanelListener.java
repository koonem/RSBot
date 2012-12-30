package org.powerbot.harrynoob.scripts.drsfighter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;

public class PanelListener implements ActionListener {

	public MainPanel panel;
	
	public PanelListener(MainPanel head)
	{
		panel = head;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JComponent a = e.getSource() instanceof JButton ? (JButton)e.getSource(): (JCheckBox)e.getSource();
		if(a.getName().equals("weaponSwitchButton"))
		{
			panel.changeWeaponSwitch();
		}
		else if(a.getName().equals("rejuvenateBox"))
		{
			panel.changeRejuvenate();
		}
		else if(a.getName().equals("bankCheckBox"))
		{
			panel.changeBank();
		}
		else if(a.getName().equals("foodCheckBox"))
		{
			panel.changeFood();
		}
		else if(a.getName().equals("start"))
		{
			panel.setVisible(false);
			DRSFighter.instance.activate();
		}
	}
	
}
