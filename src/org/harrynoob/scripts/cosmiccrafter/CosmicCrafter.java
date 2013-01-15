package org.harrynoob.scripts.cosmiccrafter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.harrynoob.scripts.cosmiccrafter.node.*;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

@Manifest(name="CosmicCrafter", authors = "harrynoob", description = "Crafts cosmic runes", version = 0.1)
public class CosmicCrafter extends ActiveScript implements PaintListener {
	
	private final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private Node[] NODE_LIST = {new CraftRunes(), new EnterAltar(), new Banker(), new WalkToAltar(), new WalkToBank()};
	private Timer timer;
	private long startTime = 0;
	private static int essenceDone = 0;
	private static int essenceHour = 0;
	
	@Override
	public void onStart()
	{
		timer = new Timer(0);
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public int loop() {
		for(Node n : NODE_LIST)
		{
			System.out.println("Tried: "+n.toString());
			if(n.activate())
			{
				n.execute();
				break;
			}
		}
		return Random.nextInt(10, 20);
	}

	@Override
	public void onRepaint(Graphics arg0) {
		Graphics2D g = (Graphics2D)arg0;
		essenceHour = (int) ((essenceDone * 3600000D) / (System.currentTimeMillis() - startTime));
		g.setRenderingHints(antialiasing);
		g.setFont(new Font("Park Avenue BT", Font.ITALIC, 16));
		g.setColor(Color.white);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5, Mouse.getY() + 5);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5, Mouse.getY() - 5);
		g.setColor(new Color(40, 40, 40, 180));
		g.fillRect(0, 300, 200, 77);
		g.setColor(Color.white);
		g.drawString("Essence done: "+essenceDone, 5, 322);
		g.drawString("Essence/h: "+essenceHour, 5, 342);
		g.drawString("Time running: "+timer.toElapsedString(), 5, 362);
	}

	public static void updateValues(int i)
	{
		essenceDone += i;
	}
	
	//You feel a powerful force take hold of you.
	//You step through the portal.
	//
}
