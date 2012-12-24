import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;
import org.powerbot.game.client.CombatStatusData;
import org.powerbot.game.client.CombatStatus;
import org.powerbot.game.client.LinkedListNode;
import org.powerbot.game.client.LinkedList;
import org.powerbot.game.client.RSCharacter;
import org.powerbot.game.api.Manifest;

//http://pastie.org/private/jsdkwmxcvds1kpxtvxra (v0.2)
//http://pastie.org/private/6fki2nil6qjh6hblp53iaw (v0.3)

@Manifest(authors={"harrynoob"}, description="Kills deadly red spiders efficiently", name="DRSFighter", version=0.4, topic=882944)
public class DeadlyRedSpider extends ActiveScript implements PaintListener, MouseListener{

	private static Filter<NPC> SPIDER_FILTER = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			return npc.getId() == 63 && !npc.isInCombat() && npc.getAnimation() == -1 && npc.getLocation().distanceTo() < 14.2D;
		}
	};
	
	private Node[] NODE_LIST = {new TargetFinder(this), new AbilityUser(this)};
	private Node currentNode;
	private NPC target;
	private Timer timer;
	private int startxp;
	private long startTime;
	private int xpHour;
	private boolean paintShown = true;;
	private boolean lowHp;
	private Client client = Context.client();
	
	public String status;
	
	
	@Override
	public void onStart()
	{
		status = "Initializing..."; 
		timer = new Timer(0);
		startTime = System.currentTimeMillis();
		Mouse.setSpeed(Mouse.Speed.FAST);
		Camera.setPitch(true);
	}
	
	@Override
	public int loop() {
		if(lowHp) return 10000;
	    if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
	    	status = "Failsafe time!";
	    	return 1000;       
	    }

	    if (client != Context.client()) {
	        WidgetCache.purge();
	        Context.get().getEventManager().addListener(this);
	        client = Context.client();
	        status = "Failsafe time!";
	    }
	    if(Players.getLocal() != null && getHealthPercent(Players.getLocal().get()) < 25)
	    {
	    	status = "Low HP! Logging out...";
	    	lowHp = true;
	    	Game.logout(true);
	    }
		if(currentNode == null)
		{
			for(Node n : NODE_LIST)
			{
				if(n.activate())
				{
					n.execute();
					break;
				}
			}
		}
		if(Settings.get(463) == 0)
		{
			WidgetChild wg = Widgets.get(750, 2);
			if(wg != null) wg.click(true);
		}
		
		return Random.nextInt(100, 200);
	}
	
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(0, 0, 0);
    private final Color color2 = new Color(250, 250, 250);
    private final Color color3 = new Color(40, 40, 40, 0);

    private final BasicStroke stroke1 = new BasicStroke(5);

    private final Font font1 = new Font("Felix Titling", 0, 19);
    private final Font font2 = new Font("Felix Titling", 0, 14);
    private final int y = 50;
    private final Image img1 = getImage("http://rs2-officialremake.webs.com/img/kbase/guides/area/varrock_sewers/deadly_red_spider2.gif");

    public int getExpGain() {
        if(startxp == 0) {
            startxp = Settings.get(91);
        }
        int curr = Settings.get(91);
        return (curr - startxp) / 10;
    }
    
    public void onRepaint(Graphics g1) {
		xpHour = (int) ((getExpGain() * 3600000D) / (System.currentTimeMillis() - startTime));
        Graphics2D g = (Graphics2D)g1;
        if(paintShown)
        {
	        g.setColor(new Color(150,211-80,128-80));
	        g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5, Mouse.getY() + 5);
	        g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5, Mouse.getY() - 5);
	        g.fillRect(6, 344+y, 506, 129);
	        g.setColor(color1);
	        g.setStroke(stroke1);
	        g.drawRect(6, 344+y, 506, 129);
	        g.setFont(font1);
	        g.drawString("DRSFighter by harrynoob", 180-25, 372+y);
	        g.setColor(color2);
	        g.drawString("DRSFighter by harrynoob", 179-25, 371+y);
	        g.setFont(font2);
	        g.setColor(color1);
	        g.drawString("Time running: "+timer.toElapsedString(), 54, 398+y);
	        g.setColor(color2);
	        g.drawString("Time running: "+timer.toElapsedString(), 53, 397+y);
	        g.setColor(color1);
	        g.drawString("XP: "+getExpGain(), 279, 446+y);
	        g.setColor(color2);
	        g.drawString("XP: "+getExpGain(), 278, 445+y);
	        g.setColor(color1);
	        g.drawString("Status: "+status, 279, 396+y);
	        g.setColor(color2);
	        g.drawString("Status: "+status, 278, 395+y);
	        g.setColor(color1);
	        g.drawString("XP P/H: "+xpHour, 54, 446+y);
	        g.setColor(color2);
	        g.drawString("XP P/H: "+xpHour, 53, 445+y);
	        g.drawImage(img1, 598, 345, null);
	        g.setColor(color3);
	        g.fillRect(10, 348+y, 499, 123);
    	}
    }  
	
	public Filter<NPC> getFilter()
	{
		return SPIDER_FILTER;
	}
	
	public void setNode(Node n)
	{
		this.currentNode = n;
	}
	
    public static CombatStatusData getHealthBar(final RSCharacter accessor) {
       LinkedListNode sentinel = (LinkedListNode) ((LinkedList) accessor.getCombatStatusList()).getTail();
       LinkedListNode current = (LinkedListNode) sentinel.getNext();
       if (!sentinel.equals(current)) {
           if (!sentinel.equals(current.getNext())) {
               current = (LinkedListNode) current.getNext();
           }
           sentinel = ((LinkedListNode) ((LinkedList) ((CombatStatus) current).getData()).getTail());
           if (!sentinel.equals(sentinel.getNext())) {
               final CombatStatusData healthBar = (CombatStatusData) sentinel.getNext();
               if (healthBar != null) {
                   return healthBar;
               }
           }
       }
       return null;
   }
    public static int getHealthPercent(final RSCharacter accessor) {
        final CombatStatusData healthBar = getHealthBar(accessor);
        return healthBar != null ? toPercent(healthBar.getHPRatio() * Context.multipliers().CHARACTER_HPRATIO) : 100;
    }

    public static int toPercent(final int ratio) {
        return (int) Math.ceil((ratio * 100) / 0xFF);
    }

	public NPC getTarget() {
		return target;
	}

	public void setTarget(NPC target) {
		this.target = target;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getX() < 510 && arg0.getX() > 10 && arg0.getY() > 398 && arg0.getY() < 398+123)
			paintShown = !paintShown;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {		
	}


}

class TargetFinder extends Node
{
	private DeadlyRedSpider instance;
	
	public TargetFinder(DeadlyRedSpider as)
	{
		this.instance = as;
	}
	
	@Override
	public boolean activate() {
		return Players.getLocal() != null && Players.getLocal().getAnimation() == -1
				&& !Players.getLocal().isMoving() && 
				(instance.getTarget() == null || (instance.getTarget() != null && !instance.getTarget().validate())
				|| Players.getLocal().isIdle());
	}

	@Override
	public void execute() {
		instance.setNode(this);
		instance.status = "Finding new target";
		NPC SPIDER_TARGET = NPCs.getNearest(instance.getFilter());
		if(SPIDER_TARGET == null) 
		{
			Walking.walk(new Tile(3176, 9883, 0).randomize(1, 1));
			instance.setNode(null);
			return;	
		}
		Camera.turnTo(SPIDER_TARGET);
		if(!SPIDER_TARGET.isInCombat() && !SPIDER_TARGET.getName().startsWith("*"))
		{
			if(!SPIDER_TARGET.isOnScreen()) 
			{
				Walking.walk(SPIDER_TARGET);
				instance.setNode(null);
				return;
			}
			if(!SPIDER_TARGET.interact("Attack", SPIDER_TARGET.getName()))
				{
				Camera.turnTo(SPIDER_TARGET);
				Task.sleep(Random.nextInt(0, 25));
				SPIDER_TARGET.interact("Attack", SPIDER_TARGET.getName());
				}
			instance.setTarget(SPIDER_TARGET);
		}
		instance.setNode(null);
	}
	
}

class AbilityUser extends Node
{
	private DeadlyRedSpider instance;
	boolean REJUV_ASAP = false;
	
	public AbilityUser(DeadlyRedSpider as)
	{
		this.instance = as;
	}

	@Override
	public boolean activate() {
		return Players.getLocal() != null && Players.getLocal().isInCombat() && instance.getTarget() != null && Players.getLocal().getInteracting() != null;
	}

	@Override
	public void execute() {
		instance.setNode(this);
		if(instance.getTarget() == null || (instance.getTarget() != null && !instance.getTarget().validate()) || (instance.getTarget() != null && instance.getTarget().getLocation().distanceTo() > 2.0D))
		{
			instance.setNode(null);
			return;
		}
		instance.status = "Using abilities";
		for(Player p : Players.getLoaded())
		{
			if(p.getInteracting() != null && p.getInteracting().equals(instance.getTarget()) && !p.equals(Players.getLocal())) 
			{
				instance.setTarget(null);
				instance.setNode(null);
				return;
			}
		}
		ActionBar.Slot REJUV_SLOT = ActionBar.getSlotWithAbility(ActionBar.Defence_Abilities.REJUVENATE);
		if(REJUV_SLOT == null)
		{ 
			instance.setNode(null);
			return;
		}
		if(DeadlyRedSpider.getHealthPercent(Players.getLocal().get()) <= 65)
		{
			REJUV_ASAP = true;
		}
		if(REJUV_ASAP && REJUV_SLOT.isAvailable())
		{
			REJUV_SLOT.activate(true);
			Task.sleep(Random.nextInt(0, 25));
		}
		if(ActionBar.getAdrenalinPercent() == 100)
		{
			for(int i = 0; i < 12; i++)
			{
				ActionBar.Ability a = ActionBar.getAbilityAt(i);
				if(a == null || Players.getLocal().getInteracting() == null) continue;
				if(a.getAbilityType() == ActionBar.AbiltiyType.ULTIMATE && ActionBar.getSlot(i).isAvailable() && !REJUV_ASAP && Players.getLocal().getInteracting().getHpPercent() > 25)
				{
					ActionBar.getSlotWithAbility(a).activate(true);
					Task.sleep(Random.nextInt(0, 25));
					break;
				}
			}
		}
		for(int i = 0; i < 12; i++){
			if(ActionBar.getAbilityAt(i) == null) continue;
			if(ActionBar.getSlot(i).isAvailable() && !ActionBar.getSlot(i).getCooldownWidget().isOnScreen() && ActionBar.getAbilityAt(i).getAbilityType() != ActionBar.AbiltiyType.ULTIMATE)
			{
				ActionBar.getSlot(i).activate(true);
				break;
			}
		}
		REJUV_ASAP = false;
		instance.setNode(null);
	}
}

/**
 * Created with IntelliJ IDEA.
 * User: Sharon
 * Date: 11/24/12
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
 
class ActionBar
{
    private static final int ID_SETTINGS_ITEM_BASE = 811;
    private static final int ID_SETTINGS_ABILITY_BASE = 727;
    private static final int ID_SETTINGS_ADRENALIN = 679;
 
    private static final int ID_WIDGET_ACTION_BAR = 640;
 
    public static SlotState getSlotStateAt(final int index)
    {
        final int item = Settings.get(ID_SETTINGS_ITEM_BASE + index);
        if(item > -1)
            return SlotState.ITEM;
        return Settings.get(ID_SETTINGS_ABILITY_BASE + index) > -1 ? SlotState.ABILITY : SlotState.EMPTY;
    }
 
    public static int getItemIdAt(final int index)
    {
        return Settings.get(ID_SETTINGS_ITEM_BASE + index);
    }
 
    public static Ability getAbilityAt(final int index)
    {
        final int id = Settings.get(ID_SETTINGS_ABILITY_BASE + index);
        if(id == -1)
            return null;
 
        for(Ability ability : Attack_Abilities.values())
            if(ability.getId() == id)
                return ability;
 
        for(Ability ability : Strength_Abilities.values())
            if(ability.getId() == id)
                return ability;
 
        for(Ability ability : Ranged_Abilities.values())
            if(ability.getId() == id)
                return ability;
 
        for(Ability ability : Magic_Abilities.values())
            if(ability.getId() == id)
                return ability;
 
        for(Ability ability : Defence_Abilities.values())
            if(ability.getId() == id)
                return ability;
 
        for(Ability ability : Constitution_Abilities.values())
            if(ability.getId() == id)
                return ability;
 
        return null;
    }
 
    public static Slot getSlotWithId(final int id)
    {
        for(int i = 0; i < 12; i++)
            if(Settings.get(ID_SETTINGS_ITEM_BASE + i) == id)
                return Slot.values()[i];
        return null;
    }
 
    public static Slot getSlotWithAbility(final Ability ability)
    {
        for(int i = 0; i < 12; i++)
            if(Settings.get(ID_SETTINGS_ABILITY_BASE + i) == ability.getId())
                return Slot.values()[i];
        return null;
    }
 
    public static boolean isAbilityAvailable(final int index)
    {
        if(!getSlotStateAt(index).equals(SlotState.ABILITY))
            return false;
 
        final Slot slot = Slot.values()[index];
 
        if(slot != null)
        {
            final WidgetChild available = slot.getAvailableWidget();
            final WidgetChild cooldown = slot.getCooldownWidget();
 
            if(available != null && available.validate() && cooldown != null && cooldown.validate())
            {
                return cooldown.getTextureId() == 14521 && available.getTextColor() == 16777215;
            }
        }
 
        return false;
    }
 
    public static Slot getSlot(final int index)
    {
        return Slot.values()[index];
    }
 
    public static int getAdrenalinPercent()
    {
        return Settings.get(ID_SETTINGS_ADRENALIN) / 10;
    }
 
    public enum Slot
    {
        ONE(0, 32, 36, 70),
        TWO(1, 72, 73, 75),
        THREE(2, 76, 77, 79),
        FOUR(3, 80, 81, 83),
        FIVE(4, 84, 85, 87),
        SIX(5, 88, 89, 91),
        SEVEN(6, 92, 93, 95),
        EIGHT(7, 96, 97, 99),
        NINE(8, 100, 101, 103),
        TEN(9, 104, 105, 107),
        ELEVEN(10, 108, 109, 111),
        TWELVE(11, 112, 113, 115);
 
        private int index;
        private int widgetChildAvailable;
        private int widgetChildCoolDown;
        private int widgetChildText;
 
        Slot(final int index, final int widgetChildAvailable, final int widgetChildCoolDown, final int widgetChildText)
        {
            this.index = index;
            this.widgetChildAvailable = widgetChildAvailable;
            this.widgetChildCoolDown = widgetChildCoolDown;
            this.widgetChildText = widgetChildText;
        }
 
        public int getIndex()
        {
            return this.index;
        }
 
        public int getWidgetChildAvailable()
        {
            return this.widgetChildAvailable;
        }
 
        public int getWidgetChildCoolDown()
        {
            return this.widgetChildCoolDown;
        }
 
        public int getWidgetChildText()
        {
            return this.widgetChildText;
        }
 
        public WidgetChild getAvailableWidget()
        {
            return Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildAvailable);
        }
 
        public WidgetChild getCooldownWidget()
        {
            return Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildCoolDown);
        }
 
        public boolean isAvailable()
        {
            final WidgetChild available = getAvailableWidget();
 
            return available != null && available.validate() && available.getTextColor() == 16777215;
        }
 
        public SlotState getSlotState()
        {
            final int item = Settings.get(ID_SETTINGS_ITEM_BASE + index);
            if(item > -1)
                return SlotState.ITEM;
            return Settings.get(ID_SETTINGS_ABILITY_BASE + index) > -1 ? SlotState.ABILITY : SlotState.EMPTY;
        }
 
        public boolean activate(boolean sendKey)
        {
            final WidgetChild widgetChild = Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildAvailable);
 
            if(!widgetChild.validate())
                return false;
 
            if(sendKey)
            {
                Keyboard.sendKey(Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildText).getText().charAt(0));
                return true;
            }
            else
            {
                return widgetChild.click(true);
            }
        }
    }
 
    public enum SlotState
    {
        EMPTY,
        ABILITY,
        ITEM
    }
 
    public enum AbiltiyType
    {
        BASIC,
        THRESHOLD,
        ULTIMATE
    }
 
    public interface Ability
    {
        public int getId();
 
        public String getName();
 
        public int getCoolDown();
 
        public AbiltiyType getAbilityType();
    }
 
    public enum Attack_Abilities implements Ability
    {
        SLICE(17, "Slice", 5, AbiltiyType.BASIC),
        SLAUGHTER(113, "Slaughter", 30, AbiltiyType.THRESHOLD),
        OVERPOWER(161, "Overpower", 30, AbiltiyType.ULTIMATE),
        HAVOC(65, "Havoc", 10, AbiltiyType.BASIC),
        BACKHAND(97, "BAckhand", 15, AbiltiyType.BASIC),
        SMASH(81, "Smash", 10, AbiltiyType.BASIC),
        BARGE(33, "Barge", 20, AbiltiyType.BASIC),
        FLURRY(129, "Flurry", 20, AbiltiyType.THRESHOLD),
        SEVER(49, "Sever", 30, AbiltiyType.BASIC),
        HURRICANE(145, "Hurricane", 20, AbiltiyType.THRESHOLD),
        MASSACRE(177, "Massacre", 60, AbiltiyType.ULTIMATE),
        METEOR_STRIKE(193, "Meteor Strike", 60, AbiltiyType.ULTIMATE);
 
        private int id;
        private String name;
        private int coolDown;
        private AbiltiyType abiltiyType;
 
        Attack_Abilities(final int id, final String name, final int coolDown, final AbiltiyType abiltiyType)
        {
            this.id = id;
            this.name = name;
            this.coolDown = coolDown;
            this.abiltiyType = abiltiyType;
        }
 
        @Override
        public int getId()
        {
            return this.id;
        }
 
        @Override
        public String getName()
        {
            return this.name;
        }
 
        @Override
        public int getCoolDown()
        {
            return this.coolDown;
        }
 
        @Override
        public AbiltiyType getAbilityType()
        {
            return this.abiltiyType;
        }
    }
 
    public enum Strength_Abilities implements Ability
    {
        KICK(34, "Kick", 15, AbiltiyType.BASIC),
        PUNISH(50, "Punish", 5, AbiltiyType.BASIC),
        DISMEMBER(18, "Dismember", 30, AbiltiyType.BASIC),
        FURY(66, "Fury", 20, AbiltiyType.BASIC),
        DESTROY(146, "Destroy", 20, AbiltiyType.THRESHOLD),
        QUAKE(130, "Quake", 20, AbiltiyType.THRESHOLD),
        BERSERK(162, "Berserk", 60, AbiltiyType.ULTIMATE),
        CLEAVE(98, "Cleave", 10, AbiltiyType.BASIC),
        ASSAULT(114, "Assault", 30, AbiltiyType.THRESHOLD),
        DECIMATE(82, "Decimate", 10, AbiltiyType.BASIC),
        PULVERISE(194, "Pulverise", 60, AbiltiyType.ULTIMATE),
        FRENZY(178, "Frenzy", 60, AbiltiyType.ULTIMATE);
 
        private int id;
        private String name;
        private int coolDown;
        private AbiltiyType abiltiyType;
 
        Strength_Abilities(final int id, final String name, final int coolDown, final AbiltiyType abiltiyType)
        {
            this.id = id;
            this.name = name;
            this.coolDown = coolDown;
            this.abiltiyType = abiltiyType;
        }
 
        @Override
        public int getId()
        {
            return this.id;
        }
 
        @Override
        public String getName()
        {
            return this.name;
        }
 
        @Override
        public int getCoolDown()
        {
            return this.coolDown;
        }
 
        @Override
        public AbiltiyType getAbilityType()
        {
            return this.abiltiyType;
        }
    }
 
    public enum Ranged_Abilities implements Ability
    {
        PIERCING_SHOT(21, "Piercing Shot", 5, AbiltiyType.BASIC),
        SNAP_SHOT(117, "Snap Shot", 20, AbiltiyType.THRESHOLD),
        DEADSHOT(197, "Deadshot", 30, AbiltiyType.ULTIMATE),
        SNIPE(89, "Snipe", 10, AbiltiyType.BASIC),
        BINDING_SHOT(37, "Binding Shot", 15, AbiltiyType.BASIC),
        FRAGMENTATION_SHOT(85, "Fragmentation Shot", 30, AbiltiyType.BASIC),
        ESCAPE(53, "Escape", 20, AbiltiyType.BASIC),
        RAPID_FIRE(133, "Rapid Fire", 20, AbiltiyType.THRESHOLD),
        RICOCHET(101, "Ricochet", 10, AbiltiyType.BASIC),
        BOMBARDMENT(149, "Bombardment", 30, AbiltiyType.THRESHOLD),
        INCENDIARY_SHOT(165, "Incendiary Shot", 60, AbiltiyType.ULTIMATE),
        UNLOAD(181, "Unload", 60, AbiltiyType.ULTIMATE);
 
        private int id;
        private String name;
        private int coolDown;
        private AbiltiyType abiltiyType;
 
        Ranged_Abilities(final int id, final String name, final int coolDown, final AbiltiyType abiltiyType)
        {
            this.id = id;
            this.name = name;
            this.coolDown = coolDown;
            this.abiltiyType = abiltiyType;
        }
 
        @Override
        public int getId()
        {
            return this.id;
        }
 
        @Override
        public String getName()
        {
            return this.name;
        }
 
        @Override
        public int getCoolDown()
        {
            return this.coolDown;
        }
 
        @Override
        public AbiltiyType getAbilityType()
        {
            return this.abiltiyType;
        }
    }
 
    public enum Magic_Abilities implements Ability
    {
        WRACK(22, "Wrack", 20, AbiltiyType.BASIC),
        ASPHYXIATE(118, "Asphyxiate", 5, AbiltiyType.THRESHOLD),
        OMNIPOWER(198, "Omnipower", 30, AbiltiyType.ULTIMATE),
        DRAGON_BREATH(102, "Dragon Breat", 10, AbiltiyType.BASIC),
        IMPACT(54, "Impact", 15, AbiltiyType.BASIC),
        COMBUST(86, "Combust", 10, AbiltiyType.BASIC),
        SURGE(38, "Surge", 20, AbiltiyType.BASIC),
        DETONATE(134, "Detonate", 30, AbiltiyType.THRESHOLD),
        CHAIN(70, "Chain", 10, AbiltiyType.BASIC),
        WILD_MAGIC(150, "Wild Magic", 20, AbiltiyType.THRESHOLD),
        METAMORPHOSIS(166, "Metamorphosis", 60, AbiltiyType.ULTIMATE),
        TSUNAMI(182, "Tsunami", 60, AbiltiyType.ULTIMATE);
 
        private int id;
        private String name;
        private int coolDown;
        private AbiltiyType abiltiyType;
 
        Magic_Abilities(final int id, final String name, final int coolDown, final AbiltiyType abiltiyType)
        {
            this.id = id;
            this.name = name;
            this.coolDown = coolDown;
            this.abiltiyType = abiltiyType;
        }
 
        @Override
        public int getId()
        {
            return this.id;
        }
 
        @Override
        public String getName()
        {
            return this.name;
        }
 
        @Override
        public int getCoolDown()
        {
            return this.coolDown;
        }
 
        @Override
        public AbiltiyType getAbilityType()
        {
            return this.abiltiyType;
        }
    }
 
    public enum Defence_Abilities implements Ability
    {
        ANTICIPATION(19, "Anticipation", 25, AbiltiyType.BASIC),
        BASH(99, "Bash", 15, AbiltiyType.BASIC),
        REVENGE(147, "Revenge", 20, AbiltiyType.THRESHOLD),
        PROVOKE(51, "Provoke", 10, AbiltiyType.BASIC),
        IMMORTALITY(195, "Immortality", 120, AbiltiyType.ULTIMATE),
        FREEDOM(35, "Freedom", 30, AbiltiyType.BASIC),
        REFLECT(115, "Reflect", 15, AbiltiyType.THRESHOLD),
        RESONANCE(67, "Resonance", 30, AbiltiyType.BASIC),
        REJUVENATE(179, "Rejuvenate", 60, AbiltiyType.ULTIMATE),
        DEBILITATE(131, "Debilitate", 30, AbiltiyType.THRESHOLD),
        PREPARATION(83, "Preparation", 5, AbiltiyType.BASIC),
        BARRICADE(163, "Barricade", 60, AbiltiyType.ULTIMATE);
 
        private int id;
        private String name;
        private int coolDown;
        private AbiltiyType abiltiyType;
 
        Defence_Abilities(final int id, final String name, final int coolDown, final AbiltiyType abiltiyType)
        {
            this.id = id;
            this.name = name;
            this.coolDown = coolDown;
            this.abiltiyType = abiltiyType;
        }
 
        @Override
        public int getId()
        {
            return this.id;
        }
 
        @Override
        public String getName()
        {
            return this.name;
        }
 
        @Override
        public int getCoolDown()
        {
            return this.coolDown;
        }
 
        @Override
        public AbiltiyType getAbilityType()
        {
            return this.abiltiyType;
        }
    }
 
    public enum Constitution_Abilities implements Ability
    {
        REGENERATE(20, "Regenerate", 0, AbiltiyType.BASIC),
        MOMENTUM(116, "Momentum", 0, AbiltiyType.ULTIMATE),
        INCITE(36, "Incite", 0, AbiltiyType.BASIC),
        SINGLE_WAY_WILDERNESS(132, "Single-way Wilderness", 10, AbiltiyType.BASIC);
 
        private int id;
        private String name;
        private int coolDown;
        private AbiltiyType abiltiyType;
 
        Constitution_Abilities(final int id, final String name, final int coolDown, final AbiltiyType abiltiyType)
        {
            this.id = id;
            this.name = name;
            this.coolDown = coolDown;
            this.abiltiyType = abiltiyType;
        }
 
        @Override
        public int getId()
        {
            return this.id;
        }
 
        @Override
        public String getName()
        {
            return this.name;
        }
 
        @Override
        public int getCoolDown()
        {
            return this.coolDown;
        }
 
        @Override
        public AbiltiyType getAbilityType()
        {
            return this.abiltiyType;
        }
    }
}
