package org.harrynoob.scripts.drsfighter.node;

import org.g;
import org.harrynoob.api.Condition;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class SpinEffigy extends Node {

	@Override
	public boolean activate() {
		return getSpinEffigies() != null && getSpinEffigies().length > 0
				&& !Players.getLocal().isMoving()
				&& Players.getLocal().getInteracting() == null
				&& (DRSFighter.instance.getCurrentTarget() == null
				|| (DRSFighter.instance.getCurrentTarget() != null && !DRSFighter.instance.getCurrentTarget().validate()));
	}

	@Override
	public void execute() {
		for(final GroundItem d : getSpinEffigies())
		{
			final GroundItem g = GroundItems.getNearest(new int[] {Variables.EFFIGY_ID, Variables.SPIN_TICKET_ID});
			if(!Utilities.isOnScreen(g))
				Camera.turnTo(g);
			g.interact("Take");
			if(Utilities.waitFor(new Condition(){

				@Override
				public boolean validate() {
					return !g.validate();
				}
				
			}, 5000))
			{
				Environment.enableRandom(, false)
				
			}
		}
	}
	
	private GroundItem[] getSpinEffigies()
	{
		GroundItem[] g = GroundItems.getLoaded(
				new Filter<GroundItem>()
				{
					public boolean accept(GroundItem g)
					{
						return (g.getId() == Variables.EFFIGY_ID | g.getId() == Variables.SPIN_TICKET_ID)
								&& g.getLocation().canReach();
					}
				});
		return g;
	}

}
