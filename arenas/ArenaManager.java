package de.onevsone.arenas;

import de.onevsone.Main;
import de.onevsone.objects.OneVsOneArena;

public class ArenaManager {

	public static void resetAllArenas() {
		for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
			arenas.resetArena();
		}
	}
	
	public static void reloadAllArenaDatas() {
		for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
			arenas.reloadData();
		}
	}
	
	
	
}
