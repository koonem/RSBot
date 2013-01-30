package org.harrynoob.scripts.drsfighter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.harrynoob.api.Debugger;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.gui.MainPanel;
import org.harrynoob.scripts.drsfighter.gui.Painter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.harrynoob.scripts.drsfighter.node.*;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

@Manifest(name = "DRSFighter", topic = 899074, version = 1.18, authors = "harrynoob", description = "Kills deadly red spiders. Supports weapon switching & charm looting & effigies & spintickets!", website = "http://www.powerbot.org/community/topic/882944-eoc-drsfighter-kills-deadly-red-spiders-great-xp/")
public class DRSFighter extends ActiveScript implements PaintListener,
		MouseListener, MessageListener {

	private Node[] NODE_LIST = { new IceFailsafe(), new RockFailsafe(),
			new ZamorakFailsafe(), new ZamorakFailsafe2(),
			new ZamorakFailsafe3(), new FiregiantFailsafe2(),
			new FiregiantFailsafe(), new MossFailsafe2(), new MossFailsafe(),
			new BugsFailsafe(), new StrDrinker(), new AttDrinker(),
			new FailsafeTimer(), new TeleportFailsafe(),
			new RejuvenateSwitcher(), new EquipWeapon(), new SpinEffigy(),
			new CharmLooter(), new TargetSwitcher(), new FindTarget(),
			new FoodEater(), new EquipShield(), new RejuvenateUser(),
			new UltimateUser(), new ThresholdUser(), new AbilityUser() };
	public static DRSFighter instance;
	public Debugger d;
	public MainPanel main;
	public boolean activated;
	private NPC currentTarget;
	private Client client = Context.client();
	private Painter paint;

	private int startxp;
	public Timer timer;
	private boolean paintShown;
	public String status;

	public void onStart() {
		Variables.StrLvlAtStart = Skills.getRealLevel(Skills.STRENGTH);
		Variables.AttLvlAtStart = Skills.getRealLevel(Skills.ATTACK);
		instance = this;
		paintShown = true;
		status = "GUI";
		startxp = -1;
		paint = new Painter();
		d = new Debugger();
		d.logMessage("Started DRSFighter");
		d.logMessage("This can be closed & opened again through the paint");
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						main = new MainPanel();
						System.out.println("Main created");
						main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						main.setLocationRelativeTo(null);
						main.setTitle("DRSFighter v1.18");
						main.pack();
						main.setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int loop() {
		try {
			if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
				return 1000;
			}

			if (client != Context.client()) {
				WidgetCache.purge();
				Context.get().getEventManager().addListener(this);
				client = Context.client();
			} else if (Players.getLocal() == null)
				return 0;
			else if (activated) {
				Utilities.ensureActionBar(true);
				for (Node n : NODE_LIST) {
					if (n.activate()) {
						System.out.println("Activating node: " + n.toString());
						n.execute();
						Task.sleep(100);
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 50;
	}

	public void onStop() {
		if (main != null)
			main.setVisible(false);
		if(d != null)
			d.setVisible(false);
		JOptionPane
				.showMessageDialog(
						null,
						"<html>Thanks for using the script! Please consider donating! (link can be found in forum thread)</html>",
						"Thank you", JOptionPane.PLAIN_MESSAGE);
	}

	public void activate() {
		activated = true;
		Variables.initOptions(main);
		timer = new Timer(0);
		startxp = getCombatXp();
		d.logMessage("Activated DRSFighter. Switching: "+Variables.switchWeapons+" Looting: "+Variables.lootCharms);
	}

	public NPC getCurrentTarget() {
		return currentTarget;
	}

	public void setCurrentTarget(NPC currentTarget) {
		this.currentTarget = currentTarget;
	}

	public int getCombatXp() {
		return Skills.getExperience(Skills.ATTACK)
				+ Skills.getExperience(Skills.DEFENSE)
				+ Skills.getExperience(Skills.STRENGTH)
				+ Skills.getExperience(Skills.RANGE)
				+ Skills.getExperience(Skills.MAGIC)
				+ Skills.getExperience(Skills.CONSTITUTION);
	}

	public int getExpGain() {
		if (startxp == 0) {
			getCombatXp();
		}
		int curr = getCombatXp();
		return (curr - startxp);
	}

	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	/*	if (paintShown) {
			g.setColor(new Color(150, 211 - 80, 128 - 80));
			g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5,
					Mouse.getY() + 5);
			g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5,
					Mouse.getY() - 5);
			g.fillRect(6, 344 + y, 506, 129);
			g.setColor(color1);
			g.setStroke(stroke1);
			g.drawRect(6, 344 + y, 506, 129);
			g.setFont(font1);
			g.drawString("DRSFighter by harrynoob", 180 - 25, 372 + y);
			g.setColor(color2);
			g.drawString("DRSFighter by harrynoob", 179 - 25, 371 + y);
			g.setFont(font2);
			g.setColor(color1);
			g.drawString(
					"Time running: "
							+ (timer != null ? timer.toElapsedString()
									: "00:00:00"), 54, 398 + y);
			g.setColor(color2);
			g.drawString(
					"Time running: "
							+ (timer != null ? timer.toElapsedString()
									: "00:00:00"), 53, 397 + y);
			g.setColor(color1);
			g.drawString("XP: " + getExpGain(), 279, 446 + y);
			g.setColor(color2);
			g.drawString("XP: " + getExpGain(), 278, 445 + y);
			g.setColor(color1);
			g.drawString("Status: " + status, 279, 396 + y);
			g.setColor(color2);
			g.drawString("Status: " + status, 278, 395 + y);
			g.setColor(color1);
			g.drawString("Average rejuv time: " + getAverageRejuvTime(),
					279 - 110, 396 + y + 25);
			g.setColor(color2);
			g.drawString("Average rejuv time: " + getAverageRejuvTime(),
					278 - 110, 395 + y + 25);
			g.setColor(color1);
			g.drawString("XP P/H: " + xpHour, 54, 446 + y);
			g.setColor(color2);
			g.drawString("XP P/H: " + xpHour, 53, 445 + y);
			g.setColor(color3);
			g.fillRect(10, 348 + y, 499, 123);
		}*/

		if(paintShown) paint.onRepaint(g1);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Rectangle r = new Rectangle(454, 410, 506, 396);
		r = new Rectangle(454, 396, 506 - 454, 410 - 396);
		/*if (r.contains(arg0.getPoint()))
			paintShown = !paintShown; */
		//paintShown = paintShown ? !r.contains(arg0.getPoint()) : true ;
		if(paintShown && r.contains(arg0.getPoint())) paintShown = false;
		else if(!paintShown) paintShown = true;
		getDebugger().logMessage(String.format("Mouse event: showPaint: %b, closePaint: %b", paintShown, r.contains(arg0.getPoint())));
		//Als paint zichtbaar dan moet r.contains, anders sws enablen?
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	public void findNewTarget() {
		new FindTarget().execute();
	}

	@Override
	public void messageReceived(org.powerbot.core.event.events.MessageEvent m) {
		
		if (m.getId() == 2 && m.getMessage().equalsIgnoreCase("test-z")) {
			Keyboard.sendKey('\n');
			Keyboard.sendText("He is the master", true);
		}
	}
	
	public static Debugger getDebugger() {
		return instance.d;
	}

}
