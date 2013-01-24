package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Condition;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class CharmLooter extends Node {

	@Override
	public boolean activate() {
		return Variables.lootCharms
				&& GroundItems.getNearest(Variables.CHARM_IDS) != null
				&& !Players.getLocal().isMoving()
				&& Players.getLocal().getInteracting() == null;
	}

	@Override
	public void execute() {
		final GroundItem charm = GroundItems.getNearest(Variables.CHARM_IDS);
		if (charm != null) {
			DRSFighter.instance.status = "Looting charms";
			if (!Utilities.isOnScreen(charm)) {
				Utilities.cameraTurnTo(charm);
				if(!Utilities.isOnScreen(charm)) {
					return;
				}
			}
			if(Variables.mouseHop) Mouse.hop((int)charm.getCentralPoint().getX(), (int)charm.getCentralPoint().getY());
			charm.interact("Take", charm.getGroundItem().getName());
			Utilities.waitFor(new Condition(){
				public boolean validate(){
					return !charm.validate();
				}
			}, 3000);
		}
	}
}
