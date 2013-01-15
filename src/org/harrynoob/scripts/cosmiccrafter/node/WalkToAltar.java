package org.harrynoob.scripts.cosmiccrafter.node;

import org.harrynoob.scripts.cosmiccrafter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.tab.Inventory;

public class WalkToAltar extends Node {

	@Override
	public boolean activate() {
		return Inventory.getCount(Variables.PURE_ESSENCE_ID) > 0;
	}

	@Override
	public void execute() {
		/*if(Utilities.waitFor(new Condition()
		{
			public boolean validate()
			{
				if(Variables.CURRENT_PATH == null) Variables.CURRENT_PATH = Walking.newTilePath(Variables.BANK_ALTAR);
				return Variables.CURRENT_PATH != null ? !Variables.CURRENT_PATH.traverse(EnumSet.of(Path.TraversalOption.SPACE_ACTIONS)) : false ;
			}
		}, 6000)){
			
		}*/
		if(Variables.CURRENT_PATH == null) Variables.CURRENT_PATH = Walking.newTilePath(Variables.BANK_ALTAR);
		Variables.CURRENT_PATH.traverse();

	}

	
	
}
