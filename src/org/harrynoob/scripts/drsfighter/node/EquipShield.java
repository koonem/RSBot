package org.harrynoob.scripts.drsfighter.node;


import org.harrynoob.api.Actionbar;
import org.harrynoob.api.Condition;
import org.harrynoob.api.Percentages;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;

public class EquipShield extends Node {

	@Override
	public boolean activate() {
		return Variables.switchWeapons
				&& Inventory.getItem(Variables.shieldID) != null
				/*&& Equipment.appearanceContainsOneOf(Variables.weaponID)*/
				&& Percentages.getHealthPercent(Players.getLocal().get()) < 70
				&& Actionbar.getAdrenalinPercent() == 100;
	}

	@Override
	public void execute() {
		DRSFighter.instance.status = "Switching to shield";
		Utilities.ensureInventoryTab();
		Equipment.equip(Variables.shieldID);
		if(Utilities.waitFor(new Condition()
		{
			public boolean validate()
			{
				return Equipment.containsOneOf(Variables.shieldID);
			}
		}, 4000))
		{
			System.out.println("Succesfully equipped shield!");
			return;
		}
	}
}
