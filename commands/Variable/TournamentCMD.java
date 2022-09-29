/**
 * 
 */
package de.onevsone.commands.Variable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.listener.Inventories.TournamentInfoInv;
import de.onevsone.objects.OneVsOneTournament;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 17:24:05					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class TournamentCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("1vs1.Command.Tournament")) {
				if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
					
					if(Main.ins.getOneVsOnePlayer(p).getPlayertournament() != null) {
						
						OneVsOneTournament tournament = Main.ins.tournaments.get(Main.ins.getOneVsOnePlayer(p).getPlayertournament());
						
						if(tournament != null) {
							
							TournamentInfoInv.open(p, tournament.getTournamentID());
							
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
