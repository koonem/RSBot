package org.powerbot.harrynoob.scripts.drsfighter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;

public class PanelListener implements ActionListener {

	public MainPanel panel;
	
	public PanelListener(MainPanel head)
	{
		panel = head;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JCheckBox a = (JCheckBox)e.getSource();
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
