package org.harrynoob.scripts.cosmiccrafter.node;

import org.harrynoob.scripts.cosmiccrafter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class EnterAltar extends Node {

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return Inventory.getCount(Variables.COSMIC_RUNE_ID) == 0
				&& !Players.getLocal().isMoving()
				&& SceneEntities.getNearest(Variables.COSMIC_ALTAR_ENTRANCE_ID) != null
				&& SceneEntities.getNearest(Variables.COSMIC_ALTAR_ENTRANCE_ID).validate();
	}

	@Override
	public void execute() {
		SceneObject entrance = SceneEntities.getNearest(Variables.COSMIC_ALTAR_ENTRANCE_ID);
		if(entrance != null && entrance.validate())
		{
			if(!entrance.isOnScreen())
				Walking.walk(new Tile(entrance.getLocation().getX(), entrance.getLocation().getY(), entrance.getLocation().getPlane()));
			entrance.interact("Enter");
		}
	}

}
