package org.harrynoob.scripts.drsfighter.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.harrynoob.scripts.drsfighter.DRSFighter;

public class PanelListener implements ActionListener {

	private String paypal = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&encrypted=-----BEGIN%20PKCS7-----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-----END%20PKCS7-----";
	public MainPanel panel;

	public PanelListener(MainPanel head) {
		panel = head;
	}

	public void actionPerformed(ActionEvent e) {
		JComponent a = e.getSource() instanceof JButton ? (JButton) e
				.getSource() : (JCheckBox) e.getSource();
		if (a.getName().equals("weaponSwitchButton")) {
			panel.changeWeaponSwitch();
		} else if (a.getName().equals("rejuvenateBox")) {
			panel.changeRejuvenate();
		} else if (a.getName().equals("bankCheckBox")) {
			panel.changeBank();
		} else if (a.getName().equals("foodCheckBox")) {
			panel.changeFood();
		} else if (a.getName().equals("start")) {
			panel.setVisible(false);
			DRSFighter.instance.activate();
		} else if (a.getName().equals("donate")) {
			openWebpage(paypal);
		}
	}

	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(String url) {
		try {
			openWebpage(new URL(url).toURI());
		} catch (URISyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
