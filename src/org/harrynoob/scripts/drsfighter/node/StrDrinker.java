package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;

public class StrDrinker extends Node {

  @Override
	public boolean activate() {
		return Skills.getLevel(Skills.STRENGTH) - Variables.StrLvlAtStart < 5
				&& Inventory.getCount(Variables.STR_POTION_IDS) > 0;
	}

	@Override
	public void execute() {
		Utilities.ensureInventoryTab();
		if (Inventory.getItem(Variables.STR_POTION_IDS).getWidgetChild() != null
				&& Inventory.getItem(Variables.STR_POTION_IDS).getWidgetChild()
						.validate()) {
			Inventory.getItem(Variables.STR_POTION_IDS).getWidgetChild()
					.interact("Drink");
			Task.sleep(800, 900);
		}
	}

}
