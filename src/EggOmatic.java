import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Rectangle;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.bot.Context;

@Manifest(authors = "Snipa", name = "EggOmatic", version = 1.0)
public class EggOmatic extends ActiveScript implements PaintListener,
		MouseListener, MessageListener {
	// Area's
	public final Area BankArea = new Area(new Tile[] { new Tile(3092, 3500, 0),
			new Tile(3092, 3496, 0), new Tile(3098, 3496, 0),
			new Tile(3098, 3500, 0) });
	public final Area SpawnArea = new Area(new Tile[] {
			new Tile(3099, 3505, 0), new Tile(3113, 3505, 0),
			new Tile(3113, 3488, 0), new Tile(3099, 3488, 0) });
	public final Area ObeliskArea = new Area(new Tile[] {
			new Tile(3115, 3518, 0), new Tile(3115, 3506, 0),
			new Tile(3139, 3506, 0), new Tile(3139, 3518, 0) });
	// Paths
	public final Tile[] RechargePath = new Tile[] { new Tile(3102, 3502, 0),
			new Tile(3109, 3505, 0), new Tile(3115, 3508, 0),
			new Tile(3120, 3511, 0), new Tile(3123, 3513, 0) };
	public final Tile[] BankPath = new Tile[] { new Tile(3095, 3497, 0),
			new Tile(3100, 3497, 0) };
	public final Tile[] LootPath = new Tile[] { new Tile(3100, 3497, 0) };

	// Variables
	public int GoldEarned = 0;
	public int EggsLooted = 0;
	public int casttimes = 3;
	public int priceEgg = 0;
	public int MaxGroundEggs = 4;
	public int MinSummoningPoints = 2;
	public double gpHr = 0.0;
	public String status = "";
	public boolean isLooting = false;
	public boolean hidePaint = false;
	public boolean firstTime = false;
	public boolean startedUp = false;
	public  final Image background = getImage("http://i.imgur.com/LXxet.png");
	public Image button = getImage("http://i.imgur.com/a8pxI.png");
	public static final Rectangle open = new Rectangle(486, 309, 50, 50);
	public Point p;
	long startTime = System.currentTimeMillis();
	
	//Fonts
	private static final  Font font1 = new Font("Algerian", 0, 16);
	private static final  Font font2 = new Font("Algerian", 0, 16);

	private Tree jobs = null;

	public void onStart() {
		status = ("Preparing");
		Mouse.setSpeed(Mouse.Speed.FAST);
		priceEgg = Integer.parseInt(getPrice(223));
		enableRun();
		SceneObject Booth = SceneEntities.getNearest(42377);
		Camera.turnTo(Booth);
		Booth.interact("Bank");
		while (!Bank.isOpen()) {
			Task.sleep(50);
		}
		if (Inventory.getCount() != 0) {
			Bank.depositInventory();
		}
		Task.sleep(1000);
		Bank.withdraw(12428, Bank.Amount.ALL);
		Bank.close();
		startedUp = true;
	}
	
	public void onStop() {
		Game.logout(true);
	}

	public int loop() {
		gpHr = (GoldEarned * 3600000D)
				/ (int) (System.currentTimeMillis() - startTime);

		if (jobs == null) {
			jobs = new Tree(new Node[] { new Failsafe(), new Walk(),
					new Banking(), new Loot(), new Recharge(), new Renew(),
					new Spawn() });
		}
		final Node job = jobs.state();
		if (job != null && startedUp) {
			jobs.set(job);
			getContainer().submit(job);
			job.join();
			return 100;
		}
		return Random.nextInt(200, 300);
	}

	public class Walk extends Node {

		public boolean activate() {
			// Conditie
			return ((Summoning.getPoints() > MinSummoningPoints && ObeliskArea.contains(Players
					.getLocal().getLocation()))
					|| (Summoning.getPoints() <= MinSummoningPoints && !ObeliskArea
							.contains(Players.getLocal().getLocation()))
					|| (Inventory.getCount() == 1 && BankArea.contains(Players
							.getLocal().getLocation())) || (isSummoned()
					&& BankArea.contains(Players.getLocal().getLocation()) || isLooting));

		}

		public void execute() {

			if (Summoning.getPoints() > MinSummoningPoints
					&& ObeliskArea.contains(Players.getLocal().getLocation())) {
				status = ("Walking back");
				Walking.newTilePath(RechargePath).reverse().traverse();
				Task.sleep(500, 700);
			}
			if (Summoning.getPoints() <= MinSummoningPoints
					&& !ObeliskArea.contains(Players.getLocal().getLocation())) {
				status = ("Recharging");
				Camera.setAngle(290);
				//traverse(RechargePath, false);
				Walking.newTilePath(RechargePath).traverse();
				System.out.println(Summoning.getPoints());
				Task.sleep(500, 700);
			}
			if (Inventory.getCount() == 1
					&& BankArea.contains(Players.getLocal().getLocation())) {
				status = ("Walking");
				Walking.newTilePath(BankPath).traverse();
				Task.sleep(500, 700);
				firstTime = true;
			}
			if (isSummoned()
					&& BankArea.contains(Players.getLocal().getLocation())) {
				Walking.newTilePath(BankPath).traverse();
				Task.sleep(500, 700);
				firstTime = true;
			}
			if (isLooting) {
				Walking.newTilePath(LootPath).traverse();
				Task.sleep(500, 700);
				isLooting = false;
			}

		}
	}

	public class Recharge extends Node {

		public boolean activate() {
			// Conditie
			return (ObeliskArea.contains(Players.getLocal().getLocation()) && Summoning.getPoints() <= MinSummoningPoints);

		}

		public void execute() {
			SceneObject Stone = SceneEntities.getNearest(29954);
			Camera.turnTo(Stone);
			Camera.setPitch(Random.nextInt(3, 5));
			Stone.interact("Renew-points");
		}

	}

	public class Banking extends Node {

		public boolean activate() {
			// Conditie
			return (Inventory.isFull()
					&& ((SpawnArea.contains(Players.getLocal().getLocation())) || BankArea
						.contains(Players.getLocal())) && !Widgets.get(205).validate());

		}

		public void execute() {
			status = ("Banking");
			SceneObject Booth = SceneEntities.getNearest(42377);
			Camera.setPitch(Random.nextInt(85,87));
			Camera.turnTo(Booth);
			Booth.interact("Bank");
			while (!Bank.isOpen()) {
				Task.sleep(50);
			}
			Bank.deposit(223, Bank.Amount.ALL);
			Task.sleep(500);
			Bank.close();
		}
	}

	public class Renew extends Node {

		public boolean activate() {
			// Conditie
			return (!isSummoned() && (SpawnArea.contains(Players.getLocal()
					.getLocation()) || BankArea.contains(Players.getLocal())) && !Widgets.get(205).validate());

		}

		public void execute() {
			status = ("Renewing familliar");
			if (!(Inventory.getCount(12059) > 0)) {
				System.out.println("opening bank");
				Task.sleep(300);
				Bank.open();
				while (!Bank.isOpen()) {
					Task.sleep(50);
				}
				Bank.withdraw(12059, 1);
				Task.sleep(300);
				Bank.close();
			}
			Task.sleep(50);
			Inventory.getItem(12059).getWidgetChild().interact("Summon");
		}
	}

	public class Loot extends Node {

		public boolean activate() {
			// Conditie
			return (GroundItems.getNearest(223) != null
					&& SpawnArea.contains(Players.getLocal())
					&& !Inventory.isFull() && (GroundItems.getLoaded(10, 223).length > MaxGroundEggs || (GroundItems
					.getLoaded(10, 223).length + Inventory.getCount() > 27)) && !Widgets.get(205).validate());
		}

		public void execute() {
		status = ("Start looting ");
			isLooting = true;
			for (GroundItem gItem : GroundItems.getLoaded()) {
				if (gItem.getId() == 223 && !Inventory.isFull()) {
					int initialCount = Inventory.getCount(true, gItem.getId());
					turnCamera();
					// Camera.turnTo(gItem, 5);
					Camera.setPitch(Random.nextInt(45, 60));
					Timer timer = new Timer(3000);
					while (timer.isRunning()
							&& initialCount == Inventory.getCount(true,
									gItem.getId())) {
						gItem.interact("Take", gItem.getGroundItem().getName());
						Task.sleep(300, 500);
					}
					EggsLooted++;
					GoldEarned += (priceEgg);
				}
			}

			status = ("Done looting ");
		}
	}

	public class Spawn extends Node {

		public boolean activate() {
			// Conditie
			return ((isSummoned() && SpawnArea.contains(Players.getLocal()) && !Inventory
					.isFull()) && Players.getLocal().getAnimation() == -1 && !Widgets.get(205).validate());
		}

		public void execute() {
			status = ("Spawning");
			if (firstTime) {
				Summoning.select("Call Follower");
				firstTime = false;
			}
			for (int i = 0; i < 3; i++) {
				Widgets.get(747, 5).click(true);
				Task.sleep(300);
			}

		}

	}

	public class Failsafe extends Node {

		public boolean activate() {
			// Conditie
			return (Widgets.get(205).validate());
		}

		public void execute() {
			System.out.println("Closing board");
			Widgets.get(205, 62).click(true);
		}

	}

	public static int getTimeLeft() {
		return Math.round((Settings.get(1786) / (float) 2.13333333333));
	}

	public static boolean enableRun() {
		if (!(Settings.get(463) == 1)) {
			Widgets.get(750, 2).click(true);
		}
		return true;
	}

	public static boolean isSummoned() {
		return (getTimeLeft() > 0);
	}

	public static void turnCamera() {
		int yaw = Camera.getYaw();
		if (yaw < 90) {
			Camera.setAngle(Random.nextInt(0, 100));
		} else {
			Camera.setAngle(Random.nextInt(80, 180));
		}
	}

	public void onRepaint(Graphics g) {
		if (Game.getClientState() == 11) {
			g.drawImage(button, 486, 330, null);
			g.drawString("Toggle paint", 440, 315);
			g.drawString(status, 435, 370);
			if (!hidePaint) {
				g.drawImage(background, 0, 385, null);

				g.setColor(Color.red);
				g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5,
						Mouse.getX() + 5, Mouse.getY() + 5);
				g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5,
						Mouse.getX() + 5, Mouse.getY() - 5);

				g.setColor(Color.WHITE);
				g.setFont(font1);
				g.drawString(String.valueOf(GoldEarned), 250, 460);
				g.drawString(String.valueOf(priceEgg), 410, 460);
				g.drawString(String.valueOf(EggsLooted), 435, 421);

				g.drawString(
						Time.format((System.currentTimeMillis() - startTime)),
						250, 421);
				g.setFont(font2);
				g.drawString(String.valueOf((int) gpHr), 325, 500);
			} else {
				g.drawImage(button, 486, 330, null);
			}
		}

	}

	public String getPrice(int itemID) {
		try {
			String[] info = { "0", "0" };
			final URL url = new URL(
					"http://www.tip.it/runescape/index.php?gec&itemid="
							+ itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String input;
			while ((input = br.readLine()) != null) {
				if (input.startsWith("<h2>")) {
					info[0] = input.substring(4, input.length() - 5);
				}
				if (input
						.startsWith("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
					info[1] = input.substring(49, input.lastIndexOf("gp"))
							.replaceAll(",", "");
					return info[1];
				}
			}
		} catch (final Exception ignored) {
		}
		return null;
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	public void mouseClicked(MouseEvent e) {
		p = e.getPoint();
		if (open.contains(p) && !hidePaint) {
			hidePaint = true;
			button = getImage("http://i.imgur.com/a8pxI.png");
		} else if (open.contains(p) && hidePaint) {
			hidePaint = false;
			button = getImage("http://i.imgur.com/fW50S.png");
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(MessageEvent e) {

		if (e.getMessage().contentEquals(
				"You do not have enough scrolls to do that.")) {
			Context.get().getScriptHandler().stop();
		}
	}

}