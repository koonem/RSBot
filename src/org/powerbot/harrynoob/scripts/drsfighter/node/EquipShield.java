package org.powerbot.harrynoob.scripts.drsfighter.node;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.harrynoob.api.Actionbar;
import org.powerbot.harrynoob.api.Percentages;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;
import org.powerbot.harrynoob.scripts.drsfighter.misc.Variables;

public class EquipShield extends Node {

	@Override
	public boolean activate() {
		return Variables.switchWeapons
				&& Inventory.getItem(Variables.shieldID) != null
				/*&& Equipment.appearanceContainsOneOf(Variables.weaponID)*/
				&& Percentages.getHealthPercent(Players.getLocal().get()) < 70
				&& Actionbar.getAdrenalinPercent() == 100;
	}

	int tries;
	@Override
	public void execute() {
		DRSFighter.instance.status = "Switching to shield";
		if(Equipment.equip(Variables.shieldID) || tries >= 5)
		{
			Task.sleep(500);
			return;
		}
		else
		{
			Task.sleep(500);
			tries++;
			execute();
		}
	}

}
