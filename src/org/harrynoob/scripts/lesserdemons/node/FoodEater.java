package org.harrynoob.scripts.lesserdemons.node;


import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.lesserdemons.LDFighter;
import org.harrynoob.scripts.lesserdemons.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

public class FoodEater extends Node {

	@Override
	public boolean activate() {
		return Players.getLocal().getHealthPercent() < 50
				&& Inventory.getCount(Variables.FOOD_IDS) > 0;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		LDFighter.instance.status = "Eating food";
		Utilities.ensureInventoryTab();
		if(Inventory.getItem(Variables.FOOD_IDS).getWidgetChild() != null && Inventory.getItem(Variables.FOOD_IDS).getWidgetChild().validate())
		{
			Inventory.getItem(Variables.FOOD_IDS).getWidgetChild().interact("Eat");
		}
	}

}
