package org.harrynoob.scripts.drsfighter.node;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.misc.Variables;

public class BugsFailsafe extends Node {

	private NPC n;
	
	@Override
	public boolean activate() {
		n = NPCs.getNearest(Variables.SPIDER_ID);
		return Variables.BugFailed.contains(Players.getLocal().getLocation())
				|| (n != null && !n.getLocation().canReach());
	}

	@Override
	public void execute() {
		SceneObject portal = SceneEntities.getNearest(Variables.PORTAL_ID);

		if (portal != null && portal.validate()) {
			if (Utilities.isOnScreen(portal)) {
				portal.hover();
				portal.interact("Enter", portal.getDefinition().getName());
				Task.sleep(1000, 2000);
				while (Players.getLocal().isMoving())
					Task.sleep(100, 200);
			} else
				Utilities.cameraTurnTo(portal);
		}
	}
}
