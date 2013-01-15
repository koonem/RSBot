package org.harrynoob.scripts.cosmiccrafter.node;

import org.harrynoob.api.Condition;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.cosmiccrafter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class CraftRunes extends Node {

	@Override
	public boolean activate() {
		return isInAltar();
	}

	@Override
	public void execute() {
		Variables.CURRENT_PATH = null;
		if (Inventory.getCount(Variables.PURE_ESSENCE_ID) > 0) {
			SceneObject altar = SceneEntities
					.getNearest(Variables.COSMIC_ALTAR_CRAFTING_ID);
			if (altar != null && altar.validate()) {
				if(!altar.getLocation().canReach())	Walking.newTilePath(getMidTile(Players.getLocal().getLocation(), altar.getLocation())).traverse();
				if(!altar.isOnScreen())
				{
					Camera.turnTo(altar);
					Camera.setPitch(false);
				}
				altar.interact("Craft-rune");
				Utilities.waitFor(new Condition()
				{
					public boolean validate()
					{
						return Inventory.getCount(Variables.PURE_ESSENCE_ID) == 0;
					}
				}, 4000);
			}
		}
		if(Inventory.getCount(Variables.PURE_ESSENCE_ID) == 0)
		{
			Tile t = Variables.PORTAL_TILES[Random.nextInt(0, 3)];
			Walking.newTilePath(getMidTile(t, Players.getLocal().getLocation())).traverse();
			final SceneObject portal = SceneEntities.getNearest(Variables.PORTAL_ID);
			if(portal != null && portal.validate())
			{
				Camera.setPitch(true);
				if(!portal.isOnScreen()) Walking.walk(t);
				Utilities.waitFor(new Condition(){
					public boolean validate()
					{
						return portal.isOnScreen();
					}
				}, 5000);
				portal.interact("Enter");
			}
		}
	}
	
	private boolean isInAltar()
	{
		for(Tile t : Variables.MAP_BASES)
		{
			if(compareTile(Game.getMapBase(), t))
				return true;
		}
		return false;
	}
	
	private boolean compareTile(Tile t1, Tile t2)
	{
		System.out.println(String.format("X1: %d X2: %d Y1: %d Y2: %d", t1.getX(), t2.getX(), t1.getY(), t2.getY()));

		return t1.getX() == t2.getX() && t1.getY() == t2.getY();
	}
	
	private Tile[] getMidTile(Tile t1, Tile t2)
	{
		return new Tile[] { new Tile((t1.getX() + t2.getX()) / 2, (t1.getY() + t2.getY()) / 2, 0)};
	}
}
