/**
 * 
 */
package de.onevsone.commands.Variable;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.objects.OneVsOnePlayer;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 18:05:00					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class JoinCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("1vs1.Command.joinT")) {
				if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
					
					if(args.length == 1) {

						String target = args[0];
							
						if(Bukkit.getPlayer(target) != null) {
							if(Main.ins.getOneVsOnePlayer(p).getPlayertournament() != null) {
								p.sendMessage(Main.ins.prefixRed + "§cDu bist bereits in einem Turnier!");
								return true;
							}
							if(Main.ins.isInOneVsOnePlayers(Bukkit.getPlayer(target).getUniqueId())) {
								
								
								
								OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(target).getUniqueId());
								
								if(player.getPlayertournament() != null) {
									
									if(Main.ins.tournaments.containsKey(player.getPlayertournament())) {
										
										if(Main.ins.tournaments.get(player.getPlayertournament()).isOpen()) {
											
											
											if(Main.ins.getOneVsOnePlayer(p).getTeamObj() != null) {
												Main.ins.tournaments.get(player.getPlayertournament()).addParticipant(p, Main.ins.getOneVsOnePlayer(p).getTeamObj().getMemberList());
											} else {
												Main.ins.tournaments.get(player.getPlayertournament()).addParticipant(p, new ArrayList<>());
												
											}
										}
										
										
									}
									
								}
								
							}
						}
						
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
