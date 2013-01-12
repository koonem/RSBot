package org.harrynoob.api;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Utilities {

	public static boolean waitFor(final Condition c, final long timeout) {
			final Timer t = new Timer(timeout);
			while (t.isRunning() && !c.validate()) {
					Task.sleep(20);
			}
		return c.validate();
	}

	public static boolean isOnScreen(Entity e)
	{
		WidgetChild actionbar = Widgets.get(640, 6);
		if(actionbar != null && !actionbar.visible())
		{
			WidgetChild abToggle = Widgets.get(640, 28);
			if(abToggle != null)
			{
				abToggle.interact("Expand");
			}
		}
		return e.isOnScreen() && (actionbar == null || !(actionbar != null && actionbar.getBoundingRectangle().contains(e.getCentralPoint())));
	}
	
	public static void ensureInventoryTab()
	{
		if(!Tabs.INVENTORY.isOpen()) Tabs.INVENTORY.open();
	}
	
}
