package org.harrynoob.scripts.drsfighter.branch;


import org.harrynoob.api.Percentages;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Branch;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;

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
