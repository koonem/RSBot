package org.powerbot.harrynoob.scripts.drsfighter.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.harrynoob.api.Actionbar;
import org.powerbot.harrynoob.api.Percentages;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;


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
				Actionbar.getSlotWithAbility(a).activate(true);
				break;
			}
		}
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
