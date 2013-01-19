package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Timer;

public class FailsafeTimer extends Node {

	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() == null
				&& Variables.failsafeTimer == null
				&& !Players.getLocal().isMoving();
	}

	@Override
	public void execute() {
		Variables.failsafeTimer = new Timer(4000);
	}

}
