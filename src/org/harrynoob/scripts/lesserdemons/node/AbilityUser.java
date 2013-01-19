package org.harrynoob.scripts.lesserdemons.node;


import org.harrynoob.api.Actionbar;
import org.harrynoob.scripts.lesserdemons.LDFighter;
import org.harrynoob.scripts.lesserdemons.misc.Variables;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;

public class AbilityUser extends Node {

	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() != null
				&& LDFighter.instance.getCurrentTarget() != null
				&& LDFighter.instance.getCurrentTarget().validate()
				&& !Players.getLocal().isMoving();
	}

	@Override
	public void execute() {
		Variables.failsafeTimer = null;
        prepareAbility();
		for(int i = 0; i < 12; i++)
		{
			if(Actionbar.getSlot(i).isAvailable() 
					&& Actionbar.getAbilityAt(i) != null 
					&& Actionbar.getAbilityAt(i).getAbilityType() == Actionbar.AbilityType.BASIC
					&& Actionbar.getSlotStateAt(i).equals(Actionbar.SlotState.ABILITY)
					&& !Actionbar.getSlot(i).getCooldownWidget().isOnScreen())
			{
				LDFighter.instance.status = "Using basic abilities";
				Actionbar.getSlot(i).activate(true);
				Task.sleep(500);
				break;
			}
		}
	}

    private void prepareAbility()
    {
        if(Widgets.get(137, 56).isOnScreen() && !Widgets.get(137, 56).getText().equalsIgnoreCase("[Press Enter to Chat]"))
        {
            Keyboard.sendKey('\u001B');
            Task.sleep(400);
            Keyboard.sendKey('\u001B');
        }
    }


}
