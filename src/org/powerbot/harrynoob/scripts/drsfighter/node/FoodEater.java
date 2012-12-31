package org.powerbot.harrynoob.scripts.drsfighter.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.harrynoob.api.Percentages;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;
import org.powerbot.harrynoob.scripts.drsfighter.misc.Variables;

public class FoodEater extends Node {

	@Override
	public boolean activate() {
		return Percentages.getHealthPercent(Players.getLocal().get()) < 50
				&& Inventory.getCount(Variables.FOOD_IDS) > 0;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		DRSFighter.instance.status = "Eating food";
		if(Inventory.getItem(Variables.FOOD_IDS).getWidgetChild() != null && Inventory.getItem(Variables.FOOD_IDS).getWidgetChild().validate())
		{
			Inventory.getItem(Variables.FOOD_IDS).getWidgetChild().interact("Eat");
		}
	}

}
