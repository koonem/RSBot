import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.*;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;

@Manifest( authors = {"harrynoob"}, name = "BonePicker", version = 0.2)
public class BonePicker extends ActiveScript implements PaintListener, MessageListener{

	public static final int BONES_ID = 526;
	
	int bonesBuried;
	int bonesHour;
	int xpHour;
	int xp;
	final int xNorm = 565;
	final int yNorm = 390;
	long startTime;
	boolean burying;
	Timer timer = new Timer(0);
	
	public void onStart()
	{
		bonesBuried = 0;
		startTime = System.currentTimeMillis();
		timer.reset();
		Mouse.setSpeed(Mouse.Speed.FAST);
	}
	
	@Override
	public int loop() {
		if(Players.getLocal().isMoving()){ return Random.nextInt(150, 500);}
		burying = (Inventory.getCount(BONES_ID) > 0 && Inventory.getCount() == 28) || (burying && Inventory.getCount(BONES_ID) > 0);
		if(burying) {
			dropBone();
			return Random.nextInt(250, 500);
		}
		else
		{
			pickBone();
		}
		return Random.nextInt(500, 1000);
	}
	
	
	public void pickBone()
	{
		GroundItem GROUND_BONES = GroundItems.getNearest(BONES_ID);
		if(GROUND_BONES != null)
		{
			if(!GROUND_BONES.isOnScreen()) Camera.turnTo(GROUND_BONES);
			GROUND_BONES.interact("Take", "Bones");
		}
	}
	
	public void dropBone()
	{
		Item INVENTORY_BONES = Inventory.getItem(526);
		if(INVENTORY_BONES != null)
		{
			INVENTORY_BONES.getWidgetChild().interact("Bury");
		}
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		if(arg0.getId() == 109)
		{
			if(arg0.getMessage().equals("You bury the bones."))
			{
				bonesBuried++;
				xp = (int)(bonesBuried * 4.5);
			}
		}
	}	

	@Override
	public void onRepaint(Graphics g) {
		// TODO Auto-generated method stub
		bonesHour = (int) ((bonesBuried * 3600000D) / (System.currentTimeMillis() - startTime));
		xpHour = (int)(bonesHour * 4.5);
		g.setFont(new Font("Arial", 1, 12));
		g.setColor(Color.WHITE);
		g.fillRect(xNorm, yNorm, 160, 125);
		g.setColor(Color.BLACK);
		g.drawString("Bones buried: "+bonesBuried, xNorm + 10, yNorm + 12);
		g.drawString("Bones P/H: "+bonesHour, xNorm + 10, yNorm + 32);
		g.drawString("Time running: "+timer.toElapsedString(), xNorm+10, yNorm + 52);
		g.drawString("XP gained: "+xp, xNorm + 10, yNorm + 72);
		g.drawString("XP P/H: "+xpHour, xNorm + 10, yNorm + 92);
		g.drawString("Made by harrynoob", xNorm + 10, yNorm + 112);
	}

	
}
