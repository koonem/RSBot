package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class FindTarget extends Node {

	@Override
	public boolean activate() {
		return  (!Players.getLocal().isMoving()
				&& (Players.getLocal().getInteracting() == null
				&& (DRSFighter.instance.getCurrentTarget() == null 
				|| !DRSFighter.instance.getCurrentTarget().validate())))
				|| (Variables.failsafeTimer != null
				&& Variables.failsafeTimer.getRemaining() == 0);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		DRSFighter.instance.status = "Finding new target";
		WidgetChild actionBarWidget = Widgets.get(640, 6);
		NPC target = NPCs.getNearest(new Filter<NPC>() {
			public boolean accept(NPC npc)
			{
				return npc.getInteracting() == null
						&& npc.getId() == Variables.SPIDER_ID
						&& npc.isIdle()
						&& npc.getLocation().distanceTo() < 10
						&& !targetHasOtherEnemies();
			}
			
			private boolean targetHasOtherEnemies()
			{
				Player[] p = Players.getLoaded(new Filter<Player>()
						{
							public boolean accept(Player p)
							{
								return !p.equals(Players.getLocal())
										&& p.getInteracting() != null
										&& p.getInteracting().equals(DRSFighter.instance.getCurrentTarget());
										
							}

						});
				return p != null && p.length > 0;
			}

		});
		if(spidersAttackUs())
		{
			target = getNewTarget();
		}
		if(target != null && target.validate())
		{
			Variables.failsafeTimer = null;
			DRSFighter.instance.status = "Attacking new target";
			if((actionBarWidget != null && actionBarWidget.contains(target.getCentralPoint()) || !Calculations.isOnScreen(target.getCentralPoint())))
			{
				Camera.turnTo(target);
				if((actionBarWidget != null && actionBarWidget.contains(target.getCentralPoint()) || !Calculations.isOnScreen(target.getCentralPoint())))
				{
					Walking.walk(Variables.VARROCK_CENTRAL_TILE);
				}
			}
			if(!target.equals(NPCs.getNearest(new Filter<NPC>() {
				public boolean accept(NPC npc)
				{
					return npc.getInteracting() == null
							&& npc.getId() == Variables.SPIDER_ID
							&& npc.isIdle()
							&& npc.getLocation().distance(Variables.VARROCK_CENTRAL_TILE) < 7
							&& !targetHasOtherEnemies();
				}

				private boolean targetHasOtherEnemies()
				{
					Player[] p = Players.getLoaded(new Filter<Player>()
							{
						public boolean accept(Player p)
						{
							return !p.equals(Players.getLocal())
									&& p.getInteracting() != null
									&& p.getInteracting().equals(DRSFighter.instance.getCurrentTarget());

						}

							});
					return p != null && p.length > 0;
				}

			}))) return;
			if(target.interact("Attack", target.getName()))
			{
				DRSFighter.instance.setCurrentTarget(target.validate() ? target : null);
				Task.sleep(1000);
			}
			else
			{
				Walking.walk(Variables.VARROCK_CENTRAL_TILE);
			}
			boolean b = Settings.get(463) == 0 && Widgets.get(750, 2) != null ? Widgets.get(750, 2).click(true) : false;
			b = !b;
		}
	}
	
	private boolean spidersAttackUs()
	{
		NPC[] npc = NPCs.getLoaded(new Filter<NPC>()
				{
					public boolean accept(NPC n)
					{
						return n.getInteracting() != null
								&& n.getInteracting().equals(Players.getLocal());
					}
				});
		return npc != null && npc.length > 0;
	}
	
	private NPC getNewTarget()
	{
		return NPCs.getNearest(new Filter<NPC>(){
			public boolean accept(NPC n)
			{
				return n.getInteracting() != null
						&& n.getInteracting().equals(Players.getLocal())
						&& !targetHasOtherEnemies(n);
			}
		});
	}
	
	private boolean targetHasOtherEnemies(final NPC n)
	{
		Player[] p = Players.getLoaded(new Filter<Player>()
				{
					public boolean accept(Player p)
					{
						return !p.equals(Players.getLocal())
								&& p.getInteracting() != null
								&& p.getInteracting().equals(n);
								
					}

				});
		return p != null && p.length > 0;
	}


	
	
}
