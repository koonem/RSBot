import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;



@Manifest(authors="BossLizard", name="TinPower", version=0.1)

public class TinPower extends ActiveScript{

	public int currentState = 0; //0 = Startup/Idle, 1 = Mining, 2 = Dropping
	Player CURR_PLAYER = null;
	static final int[] TIN_ROCK_ID = {3245,3038};
	static final int[] ACTIONBAR_WIDGET_DROP = {640,66};
	static final int[] GEM_ID = {1621, 1623, 1619, 1617};
	public static int startTin;
	public static int tinMinedBeforeDrop;
	public static int currentTinInInventory;
	public static int tinPerHour;
	WidgetChild DROP_WIDGET;
	public void onStart()
	{
		startTin = Inventory.getCount(438);
		System.out.println("Tin on start: "+startTin);
		tinMinedBeforeDrop = 0;
		DROP_WIDGET = new Widget(ACTIONBAR_WIDGET_DROP[0]).getChild(ACTIONBAR_WIDGET_DROP[1]);
	}
	
	@Override
	public int loop() {
		CURR_PLAYER = Players.getLocal();
		if(Inventory.getCount() == 28 || (currentState == 2 && Inventory.getItem(438) != null))
		{
			//System.out.println("Current state: 2");
			currentState = 2;
			drop();
			if(Inventory.getCount(GEM_ID) > 0)
			{
				dropGems();
			}
			return Random.nextInt(50, 150);
		}
		else if(Inventory.getCount() < 28 && currentState != 2 && CURR_PLAYER.getAnimation() == -1 && !CURR_PLAYER.isMoving())
		{
			//System.out.println("Current state: 1");
			
			currentState = 1;
			mine();
			return Random.nextInt(500, 1500);
		}
		else
		{
			currentTinInInventory = Inventory.getCount(438);
			currentState = 0;
			//System.out.println("Current state: 0");
		}
		System.out.println("Tin mined since inventory: "+ (currentTinInInventory - startTin));
		System.out.println("Tin mined before drop: "+tinMinedBeforeDrop);
		//System.out.println("");
		
		return Random.nextInt(500, 1500);
	}
	
	public void mine()
	{
		SceneObject TIN_ROCK = SceneEntities.getNearest(TIN_ROCK_ID);
		if(TIN_ROCK != null && TIN_ROCK.isOnScreen())
		{
			TIN_ROCK.interact("Mine");
		}
	}
	
	public void drop()
	{
		if(DROP_WIDGET == null) stop();
		DROP_WIDGET.interact("Drop");
	}
	
	public void dropGems()
	{
		Item[] GEM_LIST = getGemArray();
		
		for(int i = 0; i < (GEM_LIST.length - 1); i++)
		{
			GEM_LIST[i].getWidgetChild().interact("Drop");
			Task.sleep(Random.nextInt(500,100));
		}
	
	}
	
	private Item[] getGemArray()
	{
		return Inventory.getItems(new Filter<Item>() {
			final int[] GEM_ID = {1621, 1623, 1619, 1617};
			@Override
			public boolean accept(Item arg0) {
				for(int i : GEM_ID)
				{
					if(arg0.getId() == i)
					{
						return true;
					}
				}
				return false;
			}
		});
	}
}

class Painter implements PaintListener
{
	Font font1 = new Font("Verdana", 0, 20); //20 being the font size
	String version = "0.1";
	String tin = "0";
	String tinPerHour = "0";
	Timer runTime = new Timer(0);
	@Override
	public void onRepaint(Graphics g) {
		tin = TinPower.tinMinedBeforeDrop + TinPower.currentTinInInventory + "";
		g.setColor(Color.YELLOW);
		g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5, Mouse.getY() + 5);
		g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5, Mouse.getY() - 5);
		
		g.setColor(Color.WHITE);
		g.setFont(font1);
		g.drawString(runTime.toElapsedString(), 247, 371); //Draws elapsed time in hours : minutes : seconds
		g.drawString(tin, 312, 400);
		g.drawString(tinPerHour, 246, 429);
		g.drawString(version, 235, 465);

	}
}


