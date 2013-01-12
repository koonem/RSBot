package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Utilities;
import org.harrynoob.api.Condition;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class FindTarget2 extends Node {

	private Filter<NPC> otherEnemyFilter = new Filter<NPC>()
			{
				public boolean accept(NPC n)
					{
						return n.getId() > 0
								&& n.getInteracting() != null
								&& n.getInteracting().equals(Players.getLocal());
					}
			};
			
	private Filter<NPC> normalFilter = new Filter<NPC>()
			{
				public boolean accept(NPC n)
				{
					return n.getId() == Variables.SPIDER_ID
							&& (n.getInteracting() == null
							|| (n.getInteracting() != null
							&& !n.getInteracting().validate()));
				}
			};
			
	@Override
	public boolean activate() {
		return !hasTarget() || (!attacksCurrentTarget() && hasPossibleTargets())
				|| (Variables.failsafeTimer != null
				&& Variables.failsafeTimer.getRemaining() == 0);
	}

	@Override
	public void execute() {
		final NPC newTarget = getNewTarget();
		if(newTarget != null && newTarget.validate() && newTarget.getLocation().canReach())
		{
			Variables.failsafeTimer = null;
			DRSFighter.instance.status = "Attacking new target";
			if(!Utilities.isOnScreen(newTarget))
			{
				Camera.turnTo(newTarget);
				if(!Utilities.isOnScreen(newTarget))
				{
					return;
				}
			}
			if(Utilities.waitFor(new Condition()
			{
				public boolean validate()
				{
					return newTarget.interact("Attack", newTarget.getName())
							|| Players.getLocal().isMoving();
				}
			}, 3000))
			{
				DRSFighter.instance.setCurrentTarget(newTarget);
			}
			else
			{
				Camera.turnTo(newTarget);
			}
		}
		else
		{
			Walking.walk(Variables.VARROCK_CENTRAL_TILE);
		}
	}
	
	private boolean hasTarget()
	{
		return !(getTarget() == null
				|| (getTarget() != null
				&& !getTarget().validate()));
	}
	
	private boolean attacksCurrentTarget()
	{
		return Players.getLocal().getInteracting() != null
				&& Players.getLocal().getInteracting().validate()
				&& Players.getLocal().getInteracting().getInteracting() != null
				&& Players.getLocal().getInteracting().getInteracting().equals(Players.getLocal());
	}
	
	private boolean otherEnemies()
	{
		NPC[] npcs = NPCs.getLoaded(otherEnemyFilter);
		return npcs != null && npcs.length > 0 && Players.getLocal().getInteracting() == null;
	}
	
	private NPC getNewTarget()
	{
		if(otherEnemies())
		{
			return NPCs.getNearest(otherEnemyFilter);
		}
		else if(attacksOtherThanTarget())
		{
			return (NPC)Players.getLocal().getInteracting();
		}
		return NPCs.getNearest(normalFilter);
	}
	
	private boolean attacksOtherThanTarget()
	{
		return Players.getLocal().getInteracting() != null
				&& getTarget() != null ? !Players.getLocal().getInteracting().equals(getTarget()) : false;
	}
	
	private NPC getTarget()
	{
		return DRSFighter.instance.getCurrentTarget();
	}

	private boolean hasPossibleTargets()
	{
		return attacksOtherThanTarget() || otherEnemies();
	}
}
