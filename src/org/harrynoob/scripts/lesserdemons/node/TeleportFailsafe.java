package org.harrynoob.scripts.lesserdemons.node;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class TeleportFailsafe extends Node {

	private int LODESTONE_WIDGET_ID = 1092;
	private int VARROCK_LODESTONE_WIDGET_ID = 51;
	private int ABILITY_BOOK_WIDGET_ID = 679;
	private int ABILITY_BOOK_CHILD_WIDGET_ID = 0;
	private int ABILITY_BOOK_MAGIC_TAB_WIDGET_ID = 2;
	private int ABILITY_BOOK_MAGIC_TELEPORT_WIDGET_ID = 6;
	private int ABILITY_BOOK_MAGIC_TAB_HOME_TELEPORT_WIDGET_ID = 36;
	
	@Override
	public boolean activate() {
		return Players.getLocal().getHealthPercent() < 25
				&& !hasInteracting()
				&& Players.getLocal().getAnimation() != 16385;
	}

	@Override
	public void execute() {
		Tabs.ABILITY_BOOK.open();
		WidgetChild w1;
		if((w1 = Widgets.get(ABILITY_BOOK_WIDGET_ID, ABILITY_BOOK_CHILD_WIDGET_ID)) != null && w1.validate())
		{
			WidgetChild w2 = w1.getChild(ABILITY_BOOK_MAGIC_TAB_WIDGET_ID);
			if(w2 != null && w2.validate())
			{
				w2.interact("Magic");
			}
			WidgetChild w3 = w1.getChild(ABILITY_BOOK_MAGIC_TELEPORT_WIDGET_ID);
			if(w3 != null && w3.validate())
			{
				w3.interact("Teleport-spells");
			}
			WidgetChild w4 = w1.getChild(ABILITY_BOOK_MAGIC_TAB_HOME_TELEPORT_WIDGET_ID);
			if(w4 != null && w4.validate())
			{
				w4.interact("Cast");
			}
		}
		WidgetChild lodestones = Widgets.get(LODESTONE_WIDGET_ID, VARROCK_LODESTONE_WIDGET_ID);
		if(lodestones != null && lodestones.isOnScreen())
		{
			lodestones.interact("Teleport");
		}
	}
	
	private boolean hasInteracting()
	{
		NPC[] n = NPCs.getLoaded(new Filter<NPC>()
				{
					public boolean accept(NPC n)
					{
						return n.getInteracting() != null
								&& n.getInteracting().equals(Players.getLocal());
					}
				});
		return n != null && n.length > 0; 
	}
	
}
