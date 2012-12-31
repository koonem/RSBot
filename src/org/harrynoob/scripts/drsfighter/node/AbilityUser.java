package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Actionbar;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;

public class AbilityUser extends Node {

	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() != null
				&& DRSFighter.instance.getCurrentTarget() != null
				&& DRSFighter.instance.getCurrentTarget().validate()
				&& !Players.getLocal().isMoving();
	}

	@Override
	public void execute() {
		Variables.failsafeTimer = null;
		for(int i = 0; i < 12; i++)
		{
			if(Actionbar.getSlot(i).isAvailable() 
					&& Actionbar.getAbilityAt(i) != null 
					&& Actionbar.getAbilityAt(i).getAbilityType() == Actionbar.AbilityType.BASIC
					&& Actionbar.getSlotStateAt(i).equals(Actionbar.SlotState.ABILITY)
					&& !Actionbar.getSlot(i).getCooldownWidget().isOnScreen())
			{
				DRSFighter.instance.status = "Using basic abilities";
				Actionbar.getSlot(i).activate(true);
				Task.sleep(500);
				break;
			}
		}
	}

}
