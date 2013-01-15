package org.harrynoob.scripts.cosmiccrafter.misc;

import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

public class Variables {
	
	public static final int COSMIC_RUNE_ID = 564;
	public static final int COSMIC_ALTAR_ENTRANCE_ID = 66456; //or 2458
	public static final int COSMIC_ALTAR_CRAFTING_ID = 2484;
	public static final int PORTAL_ID = 2471; //or 7389
	public static final int PURE_ESSENCE_ID = 7936;
	
	public static final Tile[] BANK_ALTAR = 
	{
		 new Tile(2381, 4458, 0), new Tile(2395, 4451, 0),
		 new Tile(2406, 4446, 0), new Tile(2415, 4437, 0),
		 new Tile(2419, 4426, 0), new Tile(2419, 4417, 0),
		 new Tile(2413, 4409, 0), new Tile(2401, 4408, 0),
		 new Tile(2388, 4408, 0), new Tile(2377, 4411, 0),
		 new Tile(2379, 4402, 0), new Tile(2386, 4397, 0),
		 new Tile(2398, 4394, 0), new Tile(2405, 4389, 0),
		 new Tile(2407, 4379, 0)
	};
	public static final Tile[] BANK_ALTAR_OLD = {
		new Tile(2381, 4458, 0), new Tile(2396, 4451, 0),
		new Tile(2411, 4444, 0), new Tile(2419, 4429, 0),
		new Tile(2414, 4408, 0), new Tile(2400, 4408, 0),
		new Tile(2384, 4410, 0), new Tile(2386, 4397, 0), 
		new Tile(2406, 4406, 0),
		new Tile(2406, 4392, 0), 
		new Tile(2408, 4379, 0)
	};
	public static TilePath CURRENT_PATH;
	
	public static final Tile[] MAP_BASES = {
		new Tile(2112, 4784, -1)/*EAST*/, new Tile(2072, 4784, -1)/*WEST*/,
		new Tile(2088, 4760, -1)/*SOUTH*/, new Tile(2088, 4800, -1)/*NORTH*/
	};
	
	public static final Tile[] PORTAL_TILES = 
	{
		new Tile(2162, 4833, 0), //EAST
		new Tile(2142, 4853, 0), //NORTH
		new Tile(2122, 4833, 0), //WEST
		new Tile(2142, 4813, 0)
	};
	
}
