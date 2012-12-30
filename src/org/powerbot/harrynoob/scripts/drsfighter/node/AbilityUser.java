package org.powerbot.harrynoob.scripts.drsfighter.node;

import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.harrynoob.api.Actionbar.*;
import org.powerbot.harrynoob.api.Actionbar;
import org.powerbot.harrynoob.api.Percentages;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;
import org.powerbot.harrynoob.scripts.drsfighter.misc.*;
import org.powerbot.core.script.job.state.Node;

public class AbilityUser extends Node {

	@Override
	public boolean activate() {
		return Percentages.getHealthPercent(Players.getLocal().get()) > 70
				&& Actionbar.getAdrenalinPercent() != 100
				&& Players.getLocal().getInteracting() != null
				&& DRSFighter.instance.getCurrentTarget() != null
				&& DRSFighter.instance.getCurrentTarget().validate();
	}

	@Override
	public void execute() {
		for(int i = 0; i < 12; i++)
		{
			if(Actionbar.getSlot(i).isAvailable() && Actionbar.getAbilityAt(i) != null & Actionbar.getAbilityAt(i).getAbilityType() != Actionbar.AbiltiyType.ULTIMATE)
			{
				Actionbar.getSlot(i).activate(true);
				break;
			}
		}
	}

}
