package org.powerbot.harrynoob.scripts.drsfighter.node;

import static org.powerbot.harrynoob.api.Actionbar.getSlotWithAbility;
import static org.powerbot.harrynoob.api.Actionbar.isAbilityAvailable;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.harrynoob.api.Actionbar;
import org.powerbot.harrynoob.api.Actionbar.Defence_Abilities;
import org.powerbot.harrynoob.api.Percentages;
import org.powerbot.harrynoob.scripts.drsfighter.misc.Variables;

public class RejuvenateUser extends Node {

	@Override
	public boolean activate() {
		return Variables.rejuvenate 
				&& getSlotWithAbility(Defence_Abilities.REJUVENATE) != null 
				&& Actionbar.getAdrenalinPercent() == 100 
				&& !Variables.switchWeapons
				&& Percentages.getHealthPercent(Players.getLocal().get()) < 70
				&& !Players.getLocal().isMoving();
	}

	@Override
	public void execute() {
		if(isAbilityAvailable(getSlotWithAbility(Defence_Abilities.REJUVENATE).getIndex()))
			getSlotWithAbility(Defence_Abilities.REJUVENATE).activate(true);
	}

}
