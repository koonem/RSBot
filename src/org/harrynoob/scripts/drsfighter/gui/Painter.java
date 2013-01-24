package org.harrynoob.scripts.drsfighter.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;

public class Painter {

	private final int[] startXp;
	private SkillData sd;
	private Timer timer;

	public Painter() {
		timer = new Timer(0);
		sd = new SkillData(timer);
		// startXp = Arrays.copyOfRange(Skills.getExperiences(), 0, 6);
		int[] s = { Skills.getExperience(0), Skills.getExperience(1),
				Skills.getExperience(2), Skills.getExperience(3),
				Skills.getExperience(4), Skills.getExperience(5),
				Skills.getExperience(6) };
		sd.hashCode();
		startXp = s;

	}

	public void onRepaint(Graphics arg0) {
		onRepaint((Graphics2D) arg0);
	}

	// START: Code generated using Enfilade's Easel
	private final Color color1 = new Color(255, 255, 255);
	private final Color color2 = new Color(153, 0, 0);

	private BufferedImage i;
	private final BasicStroke stroke1 = new BasicStroke(1);

	private final Font font1 = new Font("Arial", Font.BOLD, 13);

	public void onRepaint(Graphics2D g) {
		if(i == null) i = getImage();
		g.setColor(color1);
		// g.fillRect(7, 413, 506, 19);
		// g.setColor(color2);
		g.setStroke(stroke1);
		g.setFont(font1);
		g.drawImage(i, 6, 394, null);
		g.drawString(timer.toElapsedString(), 308, 452);
		g.drawString(String.format("%d", getTotalXpDifference()), 298, 468);
		g.drawString(String.format("%d", (int) ((getTotalXpDifference() * 3600000D) / timer.getElapsed())), 272, 485);
		g.drawString(getAverageRejuvTime(), 251, 502);
		// g.drawRect(8, 395, 502, 17);
		// g.fillRect(689, 287, 0, 0);
		g.setColor(color2);
		g.drawRect(689, 287, 0, 0);
		//drawLevelGains(g);
	}

	private int getTotalXpDifference() {
		int diff = 0;
		for(int x = 0; x < getXpDifferences().length; x++) {
			diff += getXpDifferences()[x];
		}
		return diff;
	}
	
	/*private void drawLevelGains(Graphics2D g) {
		int a = 0;
		int[] x = getXpDifferences();
		for (int i = 0; i < 6; i++) {
			if (x[i] != 0) {
				g.setColor(color1);
				g.fillRect(0, a, 502, 19);
				g.setColor(color5);
				g.drawString(
						getNameByIndex(i)
								+ " level: "
								+ Skills.getRealLevel(i)
								+ " Experience gained: "
								+ x[i]
								+ " ("
								+ sd.experience(SkillData.Rate.HOUR, i)
								+ ") "
								+ "- Time to level: "
								+ longToTime(sd.timeToLevel(
										SkillData.Rate.HOUR, i)), 10, a + 14);
				a += 19;
			}
		}
	}*/

	private int[] getXpDifferences() {
		int[] x = new int[7];
		for (int i = 0; i < 6; i++) {
			x[i] = Skills.getExperience(i) - startXp[i];
		}
		return x;
	}

	/*private String longToTime(long timeMillis) {
		long time = timeMillis / 1000;
		String seconds = Integer.toString((int) (time % 60));
		String minutes = Integer.toString((int) ((time % 3600) / 60));
		String hours = Integer.toString((int) (time / 3600));
		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}
		return hours + ":" + minutes + ":" + seconds;

	}

	private String getNameByIndex(int i) {
		String s = null;
		switch (i) {
		case 0:
			s = "Attack";
			break;
		case 1:
			s = "Defence";
			break;
		case 2:
			s = "Strength";
			break;
		case 3:
			s = "Constitution";
			break;
		case 4:
			s = "Range";
			break;
		case 5:
			s = "Prayer";
			break;
		case 6:
			s = "Magic";
			break;
		default:
			s = "None";
			break;
		}
		return s;
	}*/
	// END: Code generated using Enfilade's Easel
	
	private BufferedImage getImage() {
		try  {
			BufferedImage i = ImageIO.read(new URL("http://i.imgur.com/fzk88IG.png"));
			DRSFighter.getDebugger().logMessage("Succesfully loaded paint image.");
			return i;
		} catch(IOException iex) {
			DRSFighter.getDebugger().logMessage(iex.getMessage());
			return null;
		}
	}
	
	private String getAverageRejuvTime() {
		return Time
				.format(Variables.firstRejuvMillis != 0
						|| Variables.rejuvUsed != 0 ? (System
						.currentTimeMillis() - Variables.firstRejuvMillis)
						/ Variables.rejuvUsed : 0);
	}

}
