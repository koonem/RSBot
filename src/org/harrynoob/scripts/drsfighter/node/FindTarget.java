package org.harrynoob.scripts.drsfighter.node;

import org.harrynoob.api.Condition;
import org.harrynoob.api.Utilities;
import org.harrynoob.scripts.drsfighter.DRSFighter;
import org.harrynoob.scripts.drsfighter.misc.Variables;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;

public class FindTarget extends Node {
	
	private final Filter<NPC> POSSIBLE_FILTER = new Filter<NPC>() {
		public boolean accept(NPC n) {
			return !(Summoning.isFamiliarSummoned() && Summoning.getFamiliar().getId() == n.getId())
					//Check if not summ familiar
					&& (n.getInteracting() == null
					|| (n.getInteracting() != null && n.getInteracting().equals(Players.getLocal().get())))
					//Check if it's either attacking nothing or us
					&& n.getAnimation() != 5329 
					//Check if it isn't dying (5329) 
					&& (n.getId() == Variables.SPIDER_ID 
					|| (n.getId() != Variables.SPIDER_ID && n.getInteracting() != null
					&& n.getInteracting().equals(Players.getLocal())));
					//Check if it's actually a spider or it's attacking us due to AoE attacks
		}
	};
	
	private final Filter<NPC> PRIORITY_FILTER = new Filter<NPC>() {
		public boolean accept(NPC n) {
			return !(Summoning.isFamiliarSummoned() && Summoning.getFamiliar().getId() == n.getId())
					&& (n.getInteracting() != null
					&& n.getInteracting().getName().equalsIgnoreCase(Players.getLocal().getName()))
					&& n.getAnimation() != 5329;
		}
	};
	
	private final Filter<Player> PLAYER_SAME_TARGET_FILTER = new Filter<Player>() {
		public boolean accept(Player p) {
			return !p.equals(Players.getLocal())
					//Check if it isn't us
					&& getCurrentInteracting() != null
					//Check if we are even attacking anything at all
					&& p.getInteracting() != null
					&& p.getInteracting().equals(getCurrentInteracting());
					//Check if it isn't attacking what we are
		}
	};
	
	@Override
	public boolean activate() {
		return (getCurrentInteracting() == null 
				|| !getCurrentInteracting().validate())
				|| Players.getLoaded(PLAYER_SAME_TARGET_FILTER).length > 0
				//Check if not attacking
				&& getPossibleTargets() != null && getPossibleTargets().length > 0
				&& Players.getLocal().getHealthPercent() > 33;
				//Check if any possible targets available
	}
	
	@Override
	public void execute() {
		final NPC newTarget;
		enableRun();
		DRSFighter.getDebugger().logMessage("Finding new target");
		newTarget = NPCs.getNearest(PRIORITY_FILTER) != null ? NPCs.getNearest(PRIORITY_FILTER) : NPCs.getNearest(POSSIBLE_FILTER);
		if(newTarget != null && newTarget.validate()) {
			DRSFighter.getDebugger().logMessage("Camera angle to new target: "+Camera.getMobileAngle(newTarget));
			if(!Utilities.isOnScreen(newTarget)) {
				Utilities.cameraTurnTo(newTarget);
				DRSFighter.getDebugger().logMessage("Rotating camera");
			}
			if(Utilities.waitFor(new Condition() {
				public boolean validate() {
					return Utilities.isOnScreen(newTarget);
				}
			}, 2000)) {
				if(!Variables.mouseHop) {
					Mouse.move(newTarget.getCentralPoint());
				}
				Mouse.hop((int)newTarget.getCentralPoint().getX(), (int) newTarget.getCentralPoint().getY());
				if(newTarget.interact("Attack", newTarget.getName()) && Utilities.waitFor(new Condition() {
							public boolean validate() {
								return (Players.getLocal().isMoving()
										|| Players.getLocal().getInteracting() != null)
										&& POSSIBLE_FILTER.accept(newTarget);
							}
						}, 1000)) {
					DRSFighter.getDebugger().logMessage("Attacking new target");
					DRSFighter.instance.setCurrentTarget(newTarget);
				}
			}
		} else {
			DRSFighter.getDebugger().logMessage("Could not find any suitable targets.");
		}
	}
		
	private org.powerbot.game.api.wrappers.interactive.Character getCurrentInteracting() {
		return Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().validate() ?
				Players.getLocal().getInteracting() : null;
	}
	
	private NPC[] getPossibleTargets() {
		return NPCs.getLoaded(POSSIBLE_FILTER);
	}
	
	private void enableRun() {
		if (Settings.get(Settings.SETTING_RUN_ENABLED) != 1) {
			if (Widgets.get(750, 2) != null && Widgets.get(750, 2).validate())
				Widgets.get(750, 2).click(true);
		}
	}
}