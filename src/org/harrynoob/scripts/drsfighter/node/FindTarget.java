package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class FindTarget extends Node {

	@Override
	public boolean activate() {
		return  !Players.getLocal().isMoving()
				&& (Players.getLocal().getInteracting() == null
				&& (DRSFighter.instance.getCurrentTarget() == null 
				|| !DRSFighter.instance.getCurrentTarget().validate()));
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		DRSFighter.instance.status = "Finding new target";
		WidgetChild actionBarWidget = Widgets.get(640, 6);
		NPC target = NPCs.getNearest(new Filter<NPC>() {
			public boolean accept(NPC npc)
			{
				return npc.getInteracting() == null
						&& npc.getId() == Variables.SPIDER_ID
						&& npc.isIdle()
						&& npc.getLocation().distance(Variables.VARROCK_CENTRAL_TILE) < 7;
			}
		});
		if(target != null && target.validate())
		{
			DRSFighter.instance.status = "Attacking new target";
			if((actionBarWidget != null && actionBarWidget.contains(target.getCentralPoint()) || !Calculations.isOnScreen(target.getCentralPoint())))
			{
				Camera.turnTo(target);
				if((actionBarWidget != null && actionBarWidget.contains(target.getCentralPoint()) || !Calculations.isOnScreen(target.getCentralPoint())))
				{
					Walking.walk(Variables.VARROCK_CENTRAL_TILE);
				}
			}
			
			if(target.interact("Attack", target.getName()))
			{
				DRSFighter.instance.setCurrentTarget(target.validate() ? target : null);
				Task.sleep(200);
			}
			else
			{
				Walking.walk(Variables.VARROCK_CENTRAL_TILE);
			}
			boolean b = Settings.get(463) == 0 && Widgets.get(750, 2) != null ? Widgets.get(750, 2).click(true) : false;
			b = !b;
		}
	}

}
