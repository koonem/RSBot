package org.harrynoob.scripts.drsfighter.misc;


import org.harrynoob.scripts.drsfighter.gui.MainPanel;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Equipment.Slot;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;


public class Variables {
	public static Area BugFailed = new Area(new Tile[] {
			new Tile(3266, 5558, 0), new Tile(3274, 5558, 0),
			new Tile(3274, 5544, 0), new Tile(3266, 5544, 0) });
	
	public static String[] bankLocations = {"Varrock East"/*, "Draynor Village", "Edgeville"*/};
	public static String[] food = {"Trout", "Salmon", "Tuna", "Lobster", "Swordfish", "Monkfish", "Shark", "Manta ray", "Rocktail"}; 
	public static final int[] FOOD_IDS = {333, 351, 329, 361, 379, 365, 373, 7946, 385, 697, 391, 15266, 15272};
	public static final int[] STR_POTION_IDS = {161, 159, 157, 2440};
	public static final int[] ATT_POTION_IDS = {149, 147, 145, 2436 };
	public static final int SPIDER_ID = 63;
	public static final int REJUVENATE_ANIMATION_ID = 18082;
	public static final int[] CHARM_IDS = {12158, 12159, 12160, 12163};
	public static final int SPIN_TICKET_ID = 24154;
	public static final int EFFIGY_ID = 18778;
	public static final Tile VARROCK_CENTRAL_TILE = new Tile(3179, 9885, 0).randomize(1, 1);
	
	public static int PORTAL_ID[] = {77746, 77745};
	public static int StrLvlAtStart;
	public static int AttLvlAtStart;
	public static boolean rejuvenate;
	public static boolean switchWeapons;
	public static boolean banking;
	public static boolean withdrawFood;
	public static boolean lootCharms;
//	public static boolean requiresSwitch;
	public static int weaponID;
	public static int shieldID;
	public static String bankLocation;
	public static String foodWithdrawal;
	public static Timer rejuvTimer;
	public static Timer failsafeTimer;
	public static Timer lastRejuvTimer;
	public static long firstRejuvMillis;
	public static int rejuvUsed = 0;
		
	public static boolean mouseHop = false;
	
	public enum FightLocation
	{
		VARROCK(new Tile(3144, 9840, -1), "Varrock Sewers", "Varrock East", new Tile(3179, 9886, 0)),
		CHAOS_TUNNELS(new Tile(0,0,0), "Chaos Tunnels", "Edgeville", new Tile(3259, 5556, 0))
		/*KARAMJA(new Tile(0,0,0), "Karamja Volcano", "Draynor Village")*/;
		
		public Tile areaTile;
		public String locationName;
		public String bankName;
		public Tile centralTile;
		
		FightLocation(Tile tile, String name, String preferredBank, Tile centralTile)
		{
			
			areaTile = tile; 
			locationName = name;
			bankName = preferredBank;
			this.centralTile = centralTile;
		}
		
	}
	
	public static void initOptions(MainPanel mainPanel)
	{
		boolean[] b = mainPanel.booleanOptions();
		rejuvenate = b[0];
		switchWeapons = b[1];
		banking = b[2];
		withdrawFood = b[3];
		lootCharms = b[4];
		String[] s = mainPanel.stringOptions();
		weaponID = getItemByName(s[0]) != null ? getItemByName(s[0]).getId() : 0;
		shieldID = getItemByName(s[1]) != null ? getItemByName(s[1]).getId() : 0;
		bankLocation = s[2];
		foodWithdrawal = s[3];
	}
	
	private static Item getItemByName(final String n)
	{
		if(n == null) return null;
		Item i = Inventory.getItem(new Filter<Item>(){

			@Override
			public boolean accept(Item arg0) {
				return arg0.getName().equals(n);
			}
		});
		return i != null ? i : (Equipment.getItem(Slot.WEAPON) != null
				? Equipment.getItem(Slot.WEAPON) : Equipment.getItem(Slot.SHIELD) );
	}	
}
