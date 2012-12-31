package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Actionbar;
import org.harrynoob.api.Percentages;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;


public class ThresholdUser extends Node {

	@Override
	public boolean activate() {
		return Percentages.getHealthPercent(Players.getLocal().get()) > 70
				&& DRSFighter.instance.getCurrentTarget() != null
				&& DRSFighter.instance.getCurrentTarget().validate()
				&& Players.getLocal().getInteracting() != null
				&& Players.getLocal().getInteracting().equals(DRSFighter.instance.getCurrentTarget())
				&& Actionbar.getAdrenalinPercent() >= 50
				&& Actionbar.getAdrenalinPercent() < 100
				&& !Players.getLocal().isMoving();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Actionbar.Ability[] thresholds = getUltimateCount() > 0 ? new Actionbar.Ability[getUltimateCount()] : null;
		if(thresholds == null) return;
		byte j = 0x0;
		for(int i = 0; i < 12; i++)
		{
			if(Actionbar.getAbilityAt(i) != null && Actionbar.getAbilityAt(i).getAbilityType() == Actionbar.AbilityType.THRESHOLD)
			{
				thresholds[j] = Actionbar.getAbilityAt(i);
				j++;
			}
		}
		for(Actionbar.Ability a : thresholds)
		{
			if(Actionbar.getSlotWithAbility(a).isAvailable())
			{
				DRSFighter.instance.status = "Using threshold";
				Actionbar.getSlotWithAbility(a).activate(true);
				break;
			}
		}
		Task.sleep(300);
	}
	
	private int getUltimateCount()
	{
		int j = 0;
		for(int i = 0; i < 12; i++)
		{
			if(Actionbar.getSlotStateAt(i).equals(Actionbar.SlotState.ABILITY) 
					&& Actionbar.getAbilityAt(i) != null
					&& Actionbar.getAbilityAt(i).getAbilityType().equals(Actionbar.AbilityType.THRESHOLD))
					j++;
		}
		return j;
	}

}
