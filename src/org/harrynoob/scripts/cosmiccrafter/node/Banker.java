package org.harrynoob.scripts.cosmiccrafter.node;

import org.harrynoob.scripts.cosmiccrafter.CosmicCrafter;
import org.harrynoob.scripts.cosmiccrafter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

public class Banker extends Node {

	@Override
	public boolean activate() {
		System.out.println(Inventory.getCount(Variables.COSMIC_RUNE_ID));
		return (Inventory.getCount(Variables.COSMIC_RUNE_ID) > 0
				|| Inventory.getCount(Variables.PURE_ESSENCE_ID) == 0)
				&& Bank.getNearest() != null;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Variables.CURRENT_PATH = null;
		if(Bank.isOpen() || Bank.open())
			if(Bank.depositInventory())
				if(Bank.withdraw(Variables.PURE_ESSENCE_ID, Bank.Amount.ALL))
				{
					Bank.close();
					CosmicCrafter.updateValues(Inventory.getCount(Variables.PURE_ESSENCE_ID));
				}
	}	
}
