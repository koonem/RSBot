import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;


@Manifest(authors="harrynoob", name="TinCopperPower", description="Slow but consequent XP", version=0.1)
public class TinCopperPower extends ActiveScript implements PaintListener, MessageListener {
	
	public int tinCount;
	public int copperCount;
	public boolean firstDrop = true;
	
	public static final int[] ROCK_IDS = {3027, 3038, 3229, 3245};
//	public static final int[] COPPER_IDS = {3027, 3229};
//	public static final int[] TIN_IDS = {3038, 3245};
	public static final int[] ORE_IDS = {436, 438};
	public static final int[] GEM_IDS = {1623, 1621, 1619, 1617};
	
	//Paint
	long startTime;
	int tinHour;
	int copperHour;
	final int xNorm = 0;
	final int yNorm = 395;
	int xpGained;
	int xpHour;
	Timer timer = new Timer(0);
	public byte currentState; 
	
	@Override
	public void onStart()
	{
		startTime = System.currentTimeMillis();
		timer.reset();
	}
	
	@Override
	public int loop() {
		//States: 1 = mining-sequence, 2 = drop-sequence, 3 = unknown
		if(Inventory.getCount() == 28)
		{
			currentState = 0x2;
			drop();
			return Random.nextInt(100, 200);
		} else
		if(currentState == 0x2 && (Inventory.getCount(ORE_IDS) > 0 || Inventory.getCount(GEM_IDS) > 0))
		{
			drop();
			return Random.nextInt(100, 200);
		} else
		if(currentState == 0x1 && !Players.getLocal().isMoving() && Players.getLocal().getAnimation() == -1)
		{
			mine();
		}
		else
		{
			currentState = 0x1;
		}
		
	//	updateValues();
		System.out.println("Tin: "+tinCount+"\tCopper: "+copperCount);
		return Random.nextInt(500, 1250);
	}
	
	public void mine()
	{
		SceneObject NEAREST_ROCK = SceneEntities.getNearest(ROCK_IDS);
		if(NEAREST_ROCK != null)
		{
			if(NEAREST_ROCK.isOnScreen())
			{
				NEAREST_ROCK.interact("Mine");
			}
		}
	}
	
	public void drop()
	{
		//WidgetParent ID = 640 for ActionBar
		//WidgetChild ID(1) = 64
		//WidgetChild ID(2) = 66
		//System.out.println("Dropseq activate");
		WidgetChild w1 = Widgets.get(640).getChild(64);
		WidgetChild w2 = Widgets.get(640).getChild(66);
		if(Inventory.getCount(436) != 0)
		{
			w1.click(true);
			return;
		}
		if(Inventory.getCount(438) != 0)
		{
			w2.click(true);
			return;
		} else
		System.out.println("No Tin/Copper found! Going to Gems...");
		dropGems();
	}
	
	public void dropGems()
	{
		Item[] gem_array = getGemArray();
		//System.out.println("Got the array!");
		for(int i = 0; i < (gem_array.length); i++)
		{
			gem_array[i].getWidgetChild().interact("Drop");
			Task.sleep(500);
		}
	}
	
	private Item[] getGemArray()
	{
		return Inventory.getItems(new Filter<Item>()
				{
					public boolean accept(Item i)
					{
						/*for(int j = 0; j < TinCopperPower.GEM_IDS.length - 1; j++)
						{
							if(TinCopperPower.GEM_IDS[j] == i.getId()) return true;
						}*/
						if(i.getName().toLowerCase().contains("uncut")) {/*System.out.println("Filter loves "+i.getName()+"!")*/;return true;}
						//System.out.println("Filtering delivered false yo, item was: "+i.getName());
						return false;
					}
				}
		);
	}

	@Override
	public void onRepaint(Graphics g) {
		// TODO Auto-generated method stub
		tinHour = (int) ((tinCount * 3600000D) / (System.currentTimeMillis() - startTime));
		copperHour = (int) ((copperCount * 3600000D) / (System.currentTimeMillis() - startTime));
		xpGained = (int)((tinCount+copperCount)*17.5D);
		xpHour = (int) ((xpGained * 3600000D) / (System.currentTimeMillis() - startTime));
		g.setColor(Color.YELLOW);
		g.fillRect(xNorm, yNorm, 518, 520);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", 1, 14));
		g.drawString("Tin per hour: "+tinHour, xNorm+25, yNorm+20);
		g.drawString("Copper per hour: "+copperHour, xNorm+25, yNorm+60);
		g.drawString("Tin mined: "+tinCount, xNorm+25, yNorm+100);
		g.drawString("Copper mined: "+copperCount, xNorm+25, yNorm+140);
		
		g.drawString("Time running: "+timer.toElapsedString(), xNorm+250, yNorm+20);
		g.drawString("XP Gained: "+xpGained, xNorm+250, yNorm+60);
		g.drawString("XP/hour: "+xpHour, xNorm+250, yNorm+100);
		g.drawString("Made by harrynoob", xNorm+250, yNorm+140);
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId() == 109)
		{
			if(arg0.getMessage().equals("You manage to mine some tin."))
			{
				tinCount++;
			}
			else if(arg0.getMessage().equals("You manage to mine some copper."))
			{
				copperCount++;
			}
		}
		
	}
	
}
