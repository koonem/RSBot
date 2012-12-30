package org.powerbot.harrynoob.scripts.drsfighter.branch;

import org.powerbot.core.script.job.state.Branch;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.harrynoob.api.Percentages;
import org.powerbot.harrynoob.scripts.drsfighter.misc.Variables;

public class SwitchingRejuvenate extends Branch {

	public SwitchingRejuvenate(Node[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean branch() {
		return Variables.switchWeapons
				&& Percentages.getHealthPercent(Players.getLocal().get()) < 70;
	}

}
