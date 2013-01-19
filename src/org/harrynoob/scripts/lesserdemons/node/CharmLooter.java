package org.harrynoob.scripts.lesserdemons.node;


import org.harrynoob.scripts.lesserdemons.LDFighter;
import org.harrynoob.scripts.lesserdemons.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class CharmLooter extends Node {

	@Override
	public boolean activate() {
		return Variables.lootCharms
				&& GroundItems.getNearest(Variables.CHARM_IDS) != null
				&& !Players.getLocal().isMoving()
				&& Players.getLocal().getInteracting() == null
				&& (LDFighter.instance.getCurrentTarget() == null
				|| (LDFighter.instance.getCurrentTarget() != null && !LDFighter.instance.getCurrentTarget().validate()));
				
	}

	@Override
	public void execute() {
		GroundItem charm = GroundItems.getNearest(Variables.CHARM_IDS);
		WidgetChild actionBarWidget = Widgets.get(640, 6);
		if(charm != null)
		{
			LDFighter.instance.status = "Looting charms";
			if((actionBarWidget != null && actionBarWidget.contains(charm.getCentralPoint()) || !Calculations.isOnScreen(charm.getCentralPoint())))
			{
				Camera.turnTo(charm);
				if((actionBarWidget != null && actionBarWidget.contains(charm.getCentralPoint()) || !Calculations.isOnScreen(charm.getCentralPoint())))
				{
					Walking.walk(Variables.VARROCK_CENTRAL_TILE);
				}
			}

			charm.interact("Take", charm.getGroundItem().getName());
			Task.sleep(1500);
		}
	}
}
