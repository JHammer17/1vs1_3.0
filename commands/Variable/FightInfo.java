/**
 * 
 */
package de.onevsone.commands.Variable;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.BestOfsPrefs;
import de.onevsone.enums.PlayerState;
import de.onevsone.objects.OneVsOneArena;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 25.03.2018 15:42:44					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class FightInfo implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
				if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INARENA ||
				   Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.SPECTATOR) {
					
					String arena = "";
					if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INARENA) {
						arena = Main.ins.getOneVsOnePlayer(p).getArena();
					} else {
						arena = Main.ins.getOneVsOnePlayer(p).getSpecator();
					}
					
					if(arena == null || arena.equalsIgnoreCase("")) return true; 
					
					
					String pos1Name = "";
					String pos2Name = "";
					
					String map = "-";
					String kit = "-";
					String fightLength = "-";
					
					String type = "First wins";
					String ranked = "§cNein";
					
					OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
					
					for(UUID pos1 : arenaObj.getPos1()) {
						if(Bukkit.getPlayer(pos1) != null) {
							pos1Name = Bukkit.getPlayer(pos1).getDisplayName();
						}
					}
					
					for(UUID pos2 : arenaObj.getPos2()) {
						if(Bukkit.getPlayer(pos2) != null) {
							pos2Name = Bukkit.getPlayer(pos2).getDisplayName();
						}
					}
					
					
					map = arenaObj.getLayout();
					kit = arenaObj.getKitName();
					fightLength = arenaObj.getFormatedFightTime();
					
					
					if(arenaObj.getBestOf() == BestOfsPrefs.BESTOF1) {
						type = "First wins";
					} else if(arenaObj.getBestOf() == BestOfsPrefs.BESTOF3) {
						type = "Best of 3";
					} else {
						type = "Best of 5";
					}
					
					
					if(arenaObj.isRanked()) {
						ranked = "§aJa";
					}
					
					p.sendMessage("§7----------[§6§lKampfinfos§r§7]----------");
					p.sendMessage(arenaObj.getColorPos1().colorKey() + "§l" + pos1Name + "§r §7(§f" + arenaObj.getColorPos1().getName() + "§7)");
					p.sendMessage(" §7vs");
					p.sendMessage(arenaObj.getColorPos2().colorKey() + "§l" + pos2Name+ "§r §7(§f" + arenaObj.getColorPos2().getName() + "§7)");
					p.sendMessage("§a");
					p.sendMessage("§7Kit: §6" + kit);
					p.sendMessage("§7Typ: §6" + type);
					p.sendMessage("§7Map: §6" + map);
					p.sendMessage("§7Ranked: §6" + ranked);
					p.sendMessage("§7Länge: §6" + fightLength);
					p.sendMessage("§7----------[§6§lKampfinfos§r§7]----------");
					
				} else {
					p.sendMessage(Main.ins.prefixRed + "§cDiesen Befehl darfst du hier nicht benutzen!");
				}
				
			}
			
			
		}
		return true;
	}
	
}
