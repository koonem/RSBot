package org.powerbot.harrynoob.scripts.drsfighter.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;
import org.powerbot.harrynoob.scripts.drsfighter.misc.Variables;

public class FindTarget extends Node {

	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() == null
				&& !Players.getLocal().isMoving()
				&& (DRSFighter.instance.getCurrentTarget() == null 
				|| !DRSFighter.instance.getCurrentTarget().validate());
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		NPC target = NPCs.getNearest(new Filter<NPC>() {
			public boolean accept(NPC npc)
			{
				return npc.getInteracting() == null
						&& npc.getId() == Variables.SPIDER_ID
						&& npc.isIdle();
			}
		});
		if(target != null && target.validate())
		{
			if(!Calculations.isOnScreen(target.getCentralPoint()))
			{
				Camera.turnTo(target);
			}
			if(target.interact("Attack", target.getName()))
			{
				DRSFighter.instance.setCurrentTarget(target.validate() ? target : null);
			}
		}
	}

}
