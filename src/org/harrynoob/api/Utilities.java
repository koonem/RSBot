package org.harrynoob.api;

import org.powerbot.core.script.job.Task;
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
		return e.isOnScreen() && (actionbar == null || !(actionbar != null && actionbar.getBoundingRectangle().contains(e.getCentralPoint())));
	}
	
}
