package org.harrynoob.scripts.drsfighter.node;

import static org.harrynoob.api.Actionbar.getSlotWithAbility;
import static org.harrynoob.api.Actionbar.isAbilityAvailable;

import org.harrynoob.api.Actionbar;
import org.harrynoob.api.Condition;
import org.harrynoob.api.Percentages;
import org.harrynoob.api.Utilities;
import org.harrynoob.api.Actionbar.Defence_Abilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.util.Timer;

public class RejuvenateSwitcher extends Node {

	@Override
	public boolean activate() {
		return Variables.rejuvenate
				&& getSlotWithAbility(Defence_Abilities.REJUVENATE) != null
				&& Actionbar.getAdrenalinPercent() == 100
				&& Variables.switchWeapons
				&& Percentages.getHealthPercent(Players.getLocal().get()) < 70
				&& !Players.getLocal().isMoving()
				&& Equipment.containsOneOf(Variables.shieldID);
	}

	@Override
	public void execute() {
		if (isAbilityAvailable(getSlotWithAbility(Defence_Abilities.REJUVENATE)
				.getIndex())) {
			DRSFighter.instance.status = "Using Rejuvenate";
			if (Utilities.waitFor(new Condition() {
				public boolean validate() {
					return getSlotWithAbility(Defence_Abilities.REJUVENATE)
							.activate(false);
				}
			}, 6000)) {
				Variables.rejuvTimer = new Timer(10500);
				Task.sleep(400);
				Variables.firstRejuvMillis = Variables.rejuvUsed == 0 ? System
						.currentTimeMillis() : Variables.firstRejuvMillis;
				Variables.rejuvUsed++;
			}
		}
	}

}
