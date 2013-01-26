package org.harrynoob.scripts.drsfighter.node;

import java.util.HashMap;

import org.harrynoob.api.Condition;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.Tile;

public class CharmLooter extends Node {

	private HashMap<Tile, Integer> h = new HashMap<Tile, Integer>();
	private Tile t;
	
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
		if (charm != null && allowTile((t = charm.getLocation()))) {
			DRSFighter.instance.status = "Looting charms";
			if (!Utilities.isOnScreen(charm)) {
				Utilities.cameraTurnTo(charm);
				if(!Utilities.isOnScreen(charm)) {
					return;
				}
			}
			if(Variables.mouseHop) Mouse.hop((int)charm.getCentralPoint().getX(), (int)charm.getCentralPoint().getY());
			charm.interact("Take", charm.getGroundItem().getName());
			if(!Utilities.waitFor(new Condition(){
				public boolean validate(){
					return !charm.validate();
				}
			}, 3000)) {
				h.put(t, h.containsKey(t) ? new Integer(h.get(t).intValue() + 1) : new Integer(1));
			}
		}
	}
	
	private boolean allowTile(Tile b) {
		return h.containsKey(b) ? h.get(b).intValue() < 5 : true;
	}
	
}
