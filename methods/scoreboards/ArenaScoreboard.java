/**
 * 
 */
package de.onevsone.methods.scoreboards;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.BestOfsPrefs;
import de.onevsone.enums.OvOColor;
import de.onevsone.objects.OneVsOneArena;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 29.03.2018 10:12:55					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class ArenaScoreboard {

	/*
	 * Design:
	 * 
	 *  Titel:  §7Kit: %KitName% 
	 *      0:  %EMPTY%
	 *      1:  %color%%Pos1_1%
	 *      2:  %color%%Pos1_2%
	 *      3:  %color%%Pos1_3%
	 *      4:   §8vs
	 * 	    5:  %color%%Pos2_1%
	 * 		6:  %color%%Pos2_2%
	 * 		7:  %color%%Pos2_3%
	 */
	
	@SuppressWarnings("unchecked")
	public void updateArenaBoard(Player p, String arena) {
		
		if(Main.ins.scoreAPI.getSB(p).getObjective("1vs1-Lobby") != null) {
			Main.ins.scoreAPI.remove(p);
			updateArenaBoard(p, arena);
			return;
		}
		
		OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
		
		OvOColor pos1Color = Main.ins.getOneVsOneArena(arena).getColorPos1();
		OvOColor pos2Color = Main.ins.getOneVsOneArena(arena).getColorPos2();
		
		String pos1_1 = "";
		String pos1_2 = "";
		String pos1_3 = "";
		
		String pos2_1 = "";
		String pos2_2 = "";
		String pos2_3 = "";
		
		
		
		
		String colorKeyPos1_1 = "";
		String colorKeyPos1_2 = "";
		String colorKeyPos1_3 = "";
		
		if(pos1Color != null && pos1Color.colorKey() != null) {
			colorKeyPos1_1 = arenaObj.getColorPos1().colorKey();
			colorKeyPos1_2 = arenaObj.getColorPos1().colorKey();
			colorKeyPos1_3 = arenaObj.getColorPos1().colorKey();
		}
		
		

		String colorKeyPos2_1 = "";
		String colorKeyPos2_2 = "";
		String colorKeyPos2_3 = "";
		
		if(pos2Color != null &&  pos2Color.colorKey() != null) {
			colorKeyPos2_1 = arenaObj.getColorPos2().colorKey();
			colorKeyPos2_2 = arenaObj.getColorPos2().colorKey();
			colorKeyPos2_3 = arenaObj.getColorPos2().colorKey();
		}
		
		
		ArrayList<UUID> pos1C = (ArrayList<UUID>) arenaObj.getPos1().clone();
		ArrayList<UUID> pos2C = (ArrayList<UUID>) arenaObj.getPos2().clone();
		
		for(UUID uuid : pos1C) {
			if(Bukkit.getPlayer(uuid) != null) {
				if(pos1_1 .equalsIgnoreCase("")) {
					pos1_1 = Bukkit.getPlayer(uuid).getDisplayName();
					if(arenaObj.isDeath(uuid)) {
						
						colorKeyPos1_1 = "§8§m"; 
					}
					
				} else if(pos1_2 .equalsIgnoreCase("")) {
					pos1_2 = Bukkit.getPlayer(uuid).getDisplayName();
					if(arenaObj.isDeath(uuid)) colorKeyPos1_2 = "§8§m"; 
				} else if(pos1_3 .equalsIgnoreCase("")) {
					pos1_3 = Bukkit.getPlayer(uuid).getDisplayName();
					if(arenaObj.isDeath(uuid)) colorKeyPos1_3 = "§8§m"; 
				} else break;
			} else {
				if(pos1_1 .equalsIgnoreCase("")) {
					pos1_1 = Bukkit.getOfflinePlayer(uuid).getName();
					if(arenaObj.isDeath(uuid)) {
						
						colorKeyPos1_1 = "§8§m"; 
					}
				} else if(pos1_2 .equalsIgnoreCase("")) {
					pos1_2 = Bukkit.getOfflinePlayer(uuid).getName();
					if(arenaObj.isDeath(uuid)) {
						
						colorKeyPos1_2 = "§8§m"; 
					}
				} else if(pos1_3 .equalsIgnoreCase("")) {
					pos1_3 = Bukkit.getOfflinePlayer(uuid).getName();
					if(arenaObj.isDeath(uuid)) colorKeyPos1_3 = "§8§m"; 
				} else break;
			}
		}
		
		
		for(UUID uuid : pos2C) {
			if(Bukkit.getPlayer(uuid) != null) {
				if(pos2_1.equalsIgnoreCase("")) {
					pos2_1 = Bukkit.getPlayer(uuid).getDisplayName();
					if(arenaObj.isDeath(uuid)) colorKeyPos2_1 = "§8§m"; 
				} else if(pos2_2 .equalsIgnoreCase("")) {
					pos2_2 = Bukkit.getPlayer(uuid).getDisplayName();
					if(arenaObj.isDeath(uuid)) {
						colorKeyPos2_2 = "§8§m"; 
						
					}
				} else if(pos2_3 .equalsIgnoreCase("")) {
					pos2_3 = Bukkit.getPlayer(uuid).getDisplayName();
					if(arenaObj.isDeath(uuid)) colorKeyPos2_3 = "§8§m"; 
				} else break;
			} else {
				if(pos2_1.equalsIgnoreCase("")) {
					pos2_1 = Bukkit.getOfflinePlayer(uuid).getName();
					if(arenaObj.isDeath(uuid)) colorKeyPos2_1 = "§8§m"; 
				} else if(pos2_2 .equalsIgnoreCase("")) {
					pos2_2 = Bukkit.getOfflinePlayer(uuid).getName();
					if(arenaObj.isDeath(uuid)) colorKeyPos2_2 = "§8§m"; 
				} else if(pos2_3 .equalsIgnoreCase("")) {
					pos2_3 = Bukkit.getOfflinePlayer(uuid).getName();
					if(arenaObj.isDeath(uuid)) colorKeyPos2_3 = "§8§m"; 
				} else break;
			}
		}
		
		Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L1", "", "§0§r", "", 16);
		
			
		if(!pos1_1.equalsIgnoreCase("")) 
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L2", "", "§1" + colorKeyPos1_1 + "§l", pos1_1, 15);
		else Main.ins.scoreAPI.removeLine(p, "1vs1-Arena", "L2");
		if(!pos1_2.equalsIgnoreCase(""))
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L3", "", "§2" + colorKeyPos1_2 + "§l", pos1_2, 14);
		else Main.ins.scoreAPI.removeLine(p, "1vs1-Arena", "L3");
		if(!pos1_3.equalsIgnoreCase(""))
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L4", "", "§3" + colorKeyPos1_3 + "§l", pos1_3, 13);
		else Main.ins.scoreAPI.removeLine(p, "1vs1-Arena", "L4");
		
		Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L5", " §f§lvs", "§4§r", "", 12);
		
		if(!pos2_1.equalsIgnoreCase("")) 
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L6", "", "§4" + colorKeyPos2_1 + "§l", pos2_1, 11);
		else Main.ins.scoreAPI.removeLine(p, "1vs1-Arena", "L6");
		if(!pos2_2.equalsIgnoreCase("")) 
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L7", "", "§5" + colorKeyPos2_2 + "§l", pos2_2, 10);
		else Main.ins.scoreAPI.removeLine(p, "1vs1-Arena", "L7");
		if(!pos2_3.equalsIgnoreCase("")) 
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L8", "", "§6" + colorKeyPos2_3 + "§l", pos2_3, 9);
		else Main.ins.scoreAPI.removeLine(p, "1vs1-Arena", "L8");
		
		Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L9", "", "§7", "", 8);
		
		Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L10", "§6Kampfzeit:", " §8§f", arenaObj.getFormatedFightTime(), 7);
		Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L11", "§6Map:", " §a§8§f", arenaObj.getLayout(), 6);
		
		
		if(arenaObj.isRanked()) {
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L12", "", "§9", "", 5);
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L13", "", "§0", "§6§lRANKED", 4);
		}
		if(arenaObj.getBestOf() != BestOfsPrefs.BESTOF1) {
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L12", "", "§9", "", 5);
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L13", "", "§0§c§l", arenaObj.getBestOf().getName() + ":", 4);
			Main.ins.scoreAPI.setLine(p, "1vs1-Arena", "L14", arenaObj.getColorPos1().colorKey() + arenaObj.getBestOfWinsP1(), " §0§1§7- §r", arenaObj.getColorPos2().colorKey() + arenaObj.getBestOfWinsP2(), 3);
		}
		
		
		Main.ins.scoreAPI.setTitle(p, "§7Kit: " + (arenaObj.getKitName()+":"+arenaObj.getSubID()).replaceAll(":0", "").replaceAll(":1", "").replaceAll(":d", ""), "1vs1-Arena");
	}
	
}
