package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Percentages;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;

public class EquipWeapon extends Node {

	@Override
	public boolean activate() {
		return Variables.switchWeapons
				&& Inventory.getItem(Variables.weaponID) != null
				/*&& Equipment.appearanceContainsOneOf(Variables.shieldID)*/
				&& Percentages.getHealthPercent(Players.getLocal().get()) > 90;
	}

	@Override
	public void execute() {
		DRSFighter.instance.status = "Switching to weapon";
		if(Equipment.equip(Variables.weaponID))
		{	
			Task.sleep(1000);
			if(Inventory.getCount(Variables.shieldID) == 0) execute();
			return;
		}


	}

}
