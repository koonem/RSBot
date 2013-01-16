import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Job;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.input.Mouse.Speed;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

@Manifest(authors = { "Geashaw" }, name = "SwamptarsPro", description = "Collects swamp tar in the lumbridge swamp", version = 0.1)
public class swamptarspro extends ActiveScript implements PaintListener {

	private final ProgressTimer RUN_TIME = new ProgressTimer();
	private final ProgressTimer timer = new ProgressTimer();
	
	private long lastRunTime;
	private int lastCollected;
	private int lastGpmade;
	public int collected, startcount, tarcount, tarsperhour, tarprice, gpmade, profit;
	private NumberFormat k = new DecimalFormat("###,###,###");	
	
	public final static int tar = 1939;
	public final static int mushedtar = 10145;
	Area SWAMP_AREA = new Area(new Tile(3159, 3160, 0), new Tile(3200, 3200, 0));

	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;

	public synchronized final void provide(final Node... jobs) {
		for (final Node job : jobs) {
			if (!jobsCollection.contains(job)) {
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}

	public synchronized final void revoke(final Node... jobs) {
		for (final Node job : jobs) {
			if (jobsCollection.contains(job)) {
				jobsCollection.remove(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}

	public final void submit(final Job... jobs) {
		for (final Job job : jobs) {
			getContainer().submit(job);
		}
	}
	
	private Client client = Context.client();
	
	private void updateDatabase(long time, int tarsCollected, int gpmade) {
		try {
			URL url;
			url = new URL("http://forum.web-succes.nl/scripts/collector/updater.php?username="+Context.get().getDisplayName()+"&collectingruntime="+time+"&tarscollected="+tarsCollected+"&tarprofit="+gpmade+"&status="+1);
			URLConnection con = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int loop() {		
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			return 1000;
		}
		if (client != Context.client()) {
			WidgetCache.purge();
			Context.get().getEventManager().addListener(this);
			client = Context.client();
		}
		
		if (timer.getElapsed() >= 60000) {
			long time = RUN_TIME.getElapsed() - lastRunTime;
			int amount = collected - lastCollected;
			int profit = gpmade - lastGpmade;
			updateDatabase(time, amount, profit);
			lastRunTime = RUN_TIME.getElapsed();
			lastCollected = collected;
			lastGpmade = gpmade;
			timer.reset();
		}
		
		if (jobContainer != null) {
			final Node job = jobContainer.state();
			if (job != null) {
				jobContainer.set(job);
				getContainer().submit(job);
				job.join();
			}
		}
		return Random.nextInt(10, 25);
	}

	@Override
	public void onStart() {
		Mouse.setSpeed(Speed.VERY_FAST);
		provide(new loot());
		startcount = Inventory.getCount(true, tar);
		tarprice = getPrice(tar);
	}
	
	public void onStop(){
		long time = RUN_TIME.getElapsed() - lastRunTime;
		int amount = collected - lastCollected;
		updateDatabase(time, amount, gpmade);
	}

	private int getPrice(final int id) {
        try {
            final URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
            final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("mark_price")) {
                    return Integer.parseInt(line.substring(line.indexOf("mark_price\":\"") + 13, line.indexOf("\"daily_gp") - 2).replaceAll(",", "").trim());
                }
            }
            br.close();
        } catch (final Throwable t) {
        }
        return -1;
    }
	
	private int getPerHour(final int value){
		return (int) (value * 3600000D / (RUN_TIME.getElapsed()));
	}

	public class loot extends Node {
		@Override
		public boolean activate() {
			return SWAMP_AREA.contains(Players.getLocal());
		}

		@Override
		public void execute() {
			if (!Walking.isRunEnabled() && Walking.getEnergy() > 20) {
				Walking.setRun(true);
				Task.sleep(300);
			}
			final GroundItem Item = GroundItems.getNearest(tar);
			if (Item != null) {
				if (Item.isOnScreen()) {
					Item.interact("Take", Item.getGroundItem().getName());
				} else {
					Walking.walk(Item);

				}
			}
			Task.sleep(50);
		}
	}	
	
	public class ProgressTimer {
	       
        private long start;
       
        public ProgressTimer() {
                this.start = System.currentTimeMillis();
        }

        public void reset() {
                this.start = System.currentTimeMillis();
        }
       
        public long getElapsed() {
                return (System.currentTimeMillis() - start);
        }
        
        public String toElapsedString() {
        	return Time.format(getElapsed());
        }
 
	}
	
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		
		collected = (Inventory.getCount(true, tar) - startcount);
		gpmade = collected * tarprice;
		
		int x = Mouse.getX();
		int y = Mouse.getY();

		g.drawLine(x, y - 10, x, y + 10);
		g.drawLine(x - 10, y, x + 10, y);

		g.setFont(new Font("Century Gothic", 0, 12));
		g.setColor(Color.WHITE);
		g.drawString("Runtime: " + RUN_TIME.toElapsedString(), 560, 470);
        g.drawString("Collected: " + k.format(collected) + " (" + k.format(getPerHour(collected)) + ")", 560, 485);
        g.drawString("Profit: " + k.format(gpmade) + " (" + k.format(getPerHour(gpmade)) + ")", 560,500); 
	}
}