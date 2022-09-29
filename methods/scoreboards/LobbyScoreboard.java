/**
 * 
 */
package de.onevsone.methods.scoreboards;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.objects.OneVsOnePlayer;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 29.03.2018 10:12:19					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class LobbyScoreboard {

	public void updateLobbyBoard(Player p) {
		if(p == null) return;
		if(Main.ins.scoreAPI.getSB(p).getObjective("1vs1-Arena") != null) {
			Main.ins.scoreAPI.remove(p);
			updateLobbyBoard(p);
			return;
		}
		
		OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
		
		if(player == null) return;
		
		String challanged = "-";
		String challangedBy = "-";
		
		String challanged2 = "";
		String challangedBy2 = "";
		
	
		if(player.getChallanged().size() >= 1) {
			if(Bukkit.getPlayer(player.getChallanged().get(0)) != null) {
				challanged = Bukkit.getPlayer(player.getChallanged().get(0)).getDisplayName();
			}
			if(player.getChallanged().size() > 1) challanged2 = "...";
		}
		
		if(player.getChallangedBy().size() >= 1) {
			if(Bukkit.getPlayer(player.getChallangedBy().get(0)) != null) {
				challangedBy = Bukkit.getPlayer(player.getChallangedBy().get(0)).getDisplayName();
			}
			if(player.getChallangedBy().size() > 1) challangedBy2 = "...";
		}
		
		
		if(player.isInQueue()) {
			if(!challanged.equalsIgnoreCase("-")) challanged2 = "...";
			challanged = "Warteschlange";
		}
		
		
		
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L1", "", "§0§r", "", 16);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L2", "§6» » »", "§1§r", "", 15);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L3", challanged, "§2§r", challanged2, 14);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L4", "", "§3§r", "", 13);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L5", "§6« « «", "§4§r", "", 12);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L6", challangedBy, "§5§r", challangedBy2, 11);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L7", "", "§6§r", "", 10);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L8", "§6Startende ", "§7§r", "§6Turniere:", 9);
		Main.ins.scoreAPI.setLine(p, "1vs1-Lobby", "L9", "-", "§8§r", "", 8);
		
		
		
		String kit = "";
		
		try {
			kit = Main.ins.getOneVsOnePlayer(p).getKitLoaded();
			
			if(kit.contains(":")) {
				String[] sKit = kit.split(":");
				kit = Main.ins.database.getUserName(UUID.fromString(sKit[0])) + ":" + sKit[1];
			}
			
		} catch (Exception e) {}		
		
		
		if(p == null || kit == null) return;
		Main.ins.scoreAPI.setTitle(p, "§6» §fKit: §6" + kit.replaceAll(":1", "").replaceAll(":d", ""), "1vs1-Lobby");
		
	}
	
	
	
}
