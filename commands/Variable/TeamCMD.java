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
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.TeamMgr;
import de.onevsone.objects.OneVsOnePlayer;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 28.03.2018 15:05:17					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class TeamCMD implements CommandExecutor {
	
	/*
	 *	Arguments: 
	 *   - info/list
	 *   - kick [Player]
	 *   - leave
	 *   
	 */
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("1vs1.Command.Color")) {
				if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
					if(Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.UNKNOWN &&
					   Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.INARENA &&
					   Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.SPECTATOR) {
						
						if(args.length != 0) {
							
							OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
							
							if(player.getTeamObj() != null) {
								
							
							    if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("list")) {
								
							
									 p.sendMessage(Main.ins.prefixBlue + "§7Team von §6" + Bukkit.getPlayer(player.getTeamObj().getLeader()).getDisplayName());
									 
									
									for(UUID uuid : player.getTeamObj().getMemberList()) {
										 if(Bukkit.getPlayer(uuid) != null) {
											 p.sendMessage("§1│ §a● §7" + Bukkit.getPlayer(uuid).getDisplayName());
										 }
									 }
								} else if(args[0].equalsIgnoreCase("kick")) {
									
									if(args.length == 2) {
										if(Bukkit.getPlayer(args[1]) != null) {
											if(player.getTeamObj().getMemberList().contains(Bukkit.getPlayer(args[1]).getUniqueId())) {
												
												TeamMgr.removeTeam(Bukkit.getPlayer(args[1]), true);
												p.sendMessage(Main.ins.prefixGreen + "§6" + args[1] + " §7wurde aus dem Team geworfen!");
												
											} else {
												p.sendMessage(Main.ins.prefixRed + "§cDieser Spieler ist nich in deinen Team!");
											}
										} else {
											p.sendMessage(Main.ins.prefixRed + "§cDieser Spieler ist nicht Online!");
										}
									} else {
										p.sendMessage(Main.ins.prefixRed + "§7Nutze: /team kick [Spieler]");
									}
									
								} else if(args[0].equalsIgnoreCase("leave")) {
									
									if(args.length == 1) {
										
											if(player.getTeamObj() != null) {
												
												if(player.getTeamObj().getLeader().equals(p.getUniqueId())) {
													TeamMgr.destroyTeam(p, true);
												} else {
													TeamMgr.removeTeam(p, true);	
												}
												
												
											} else {
												p.sendMessage(Main.ins.prefixRed + "§cDu hast kein Team!");
											}
										
									} else {
										p.sendMessage(Main.ins.prefixRed + "§7Nutze: /team leave");
									}
									
								}
								
								
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cDu bist in keinen Team");
							}
						} else {
							p.sendMessage(Main.ins.prefixRed + "§7Nutze: /team [info/list|kick|leave] {Player}");
						}					
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cDiesen Befehl kannst du hier nicht ausführen.");
					}
				} else {
					p.sendMessage(Main.ins.prefixRed + "§cDieser Befehl ist nur in 1vs1 verfügbar.");
				}
			} else {
				p.sendMessage(Main.ins.prefixRed + "§cDu hast nicht die benötigten Rechte, um diesen Befehl nutzen zu können!");
			}
			
			
		} else {
			sender.sendMessage(Main.ins.prefixRed + "§cDu bist kein Spieler!");
		}
		return true;
	}

}
