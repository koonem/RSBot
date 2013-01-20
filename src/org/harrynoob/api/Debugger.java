package org.harrynoob.api;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Debugger extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea debugField;
	private JScrollPane jps;
	
	public Debugger() {
		super("Debug console");
		debugField = new JTextArea();
		debugField.setEditable(false);
		DefaultCaret c = ((DefaultCaret)debugField.getCaret());
		c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jps = new JScrollPane(debugField);
		add(jps);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)d.getHeight(), (int)d.getWidth());
		setLocation(0, 0);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public void logMessage(String s){
		debugField.append(s + "\n");
		if(this.getHeight() < Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2)
			this.pack();
	}	
}
