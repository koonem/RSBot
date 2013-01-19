package org.harrynoob.scripts.lesserdemons.node;


import org.harrynoob.scripts.lesserdemons.LDFighter;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.util.Filter;

public class TargetSwitcher extends Node {

	@Override
	public boolean activate() {
		return LDFighter.instance.getCurrentTarget() != null
				&& LDFighter.instance.getCurrentTarget().validate()
				&& targetHasOtherEnemies();
	}

	@Override
	public void execute() {
			LDFighter.instance.setCurrentTarget(null);
			Task.sleep(500);
			LDFighter.instance.findNewTarget();
	}
	
	private boolean targetHasOtherEnemies()
	{
		Player[] p = Players.getLoaded(new Filter<Player>()
				{
					public boolean accept(Player p)
					{
						return !p.get().equals(Players.getLocal().get())
								&& p.getInteracting() != null
								&& p.getInteracting().equals(LDFighter.instance.getCurrentTarget());
					}

				});
		return p != null && p.length > 0;
	}
}
