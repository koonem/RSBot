package org.powerbot.harrynoob.scripts.drsfighter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;
import org.powerbot.harrynoob.scripts.drsfighter.gui.MainPanel;
import org.powerbot.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.harrynoob.scripts.drsfighter.node.*;

@Manifest(name = "DRSFighter", version = 0.1, authors = "harrynoob")
public class DRSFighter extends ActiveScript {
	
	private Node[] NODE_LIST = {new FindTarget(), new FoodEater(), new EquipWeapon(), new RejuvenateSwitcher(), new EquipShield(), new RejuvenateUser(), new UltimateUser(), new AbilityUser()};
	public static DRSFighter instance;
	public MainPanel main;
	public boolean activated;
	private NPC currentTarget;
	private Client client = Context.client();
	
	public void onStart()
	{
		instance = this;
		try {
			SwingUtilities.invokeLater(new Runnable(){				
				public void run()
				{
					try {
						main = new MainPanel();
						System.out.println("Main created");
						main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						main.setSize(600, 600);
						main.setLocationRelativeTo(null);
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
		if(Players.getLocal() == null) return 0;
	    if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
	    	return 1000;       
	    }

	    if (client != Context.client()) {
	        WidgetCache.purge();
	        Context.get().getEventManager().addListener(this);
	        client = Context.client();
	    }
	    else if(activated)
		{
			for(Node n : NODE_LIST)
			{
				if(n.activate())
				{
					n.execute();
				}
			}
			
		}
		return 0;
	}
	
	public void onStop()
	{
		if(main != null) main.setVisible(false);
	}
	
	public void activate()
	{
		activated = true;
		Variables.initOptions(main);
	}

	public NPC getCurrentTarget() {
		return currentTarget;
	}

	public void setCurrentTarget(NPC currentTarget) {
		this.currentTarget = currentTarget;
	}
}
