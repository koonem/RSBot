package org.harrynoob.scripts.lesserdemons.node;

import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class RuneHelmLooter extends Node 
{

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return getRuneHelms() != null
				&& !Players.getLocal().isMoving()
				&& Players.getLocal().getInteracting() == null
				&& (DRSFighter.instance.getCurrentTarget() == null
				|| (DRSFighter.instance.getCurrentTarget() != null && !DRSFighter.instance.getCurrentTarget().validate()));

	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GroundItem g = GroundItems.getNearest(new Filter<GroundItem>()
				{
			public boolean accept(GroundItem g)
			{
				return g.getGroundItem() != null
						&& g.getGroundItem().getName().equals("Rune helm");
			}
		});
		if(g != null)
		{
			if(!Utilities.isOnScreen(g)) Camera.turnTo(g);
			g.interact("Take", g.getGroundItem().getName());
		}
	}
	
	private GroundItem[] getRuneHelms()
	{
		GroundItem[] g = GroundItems.getLoaded(new Filter<GroundItem>()
				{
					public boolean accept(GroundItem g)
					{
						return g.getGroundItem() != null
								&& g.getGroundItem().getName().equals("Rune helm");
					}
				});
		return g.length > 0 ? g : null;
	}

}
