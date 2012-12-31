package org.powerbot.harrynoob.scripts.drsfighter.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.util.Filter;
import org.powerbot.harrynoob.scripts.drsfighter.DRSFighter;

public class TargetSwitcher extends Node {

	@Override
	public boolean activate() {
		return targetHasOtherEnemies()
				&& DRSFighter.instance.getCurrentTarget() != null
				&& DRSFighter.instance.getCurrentTarget().validate();
	}

	@Override
	public void execute() {
		DRSFighter.instance.setCurrentTarget(null);
	}
	
	private boolean targetHasOtherEnemies()
	{
		Player[] p = Players.getLoaded(new Filter<Player>()
				{
					public boolean accept(Player p)
					{
						return p.getInteracting() != null
								&& p.getInteracting().equals(DRSFighter.instance.getCurrentTarget())
								&& !p.equals(Players.getLocal());
					}

				});
		return p != null && p.length > 0;
	}

}
