/**
 * 
 */
package de.onevsone.commands.Variable;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.Inventories.TournamentInv;
import de.onevsone.objects.OneVsOneTournament;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 11:37:20					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class CreateCMD implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
				if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INLOBBY) {
					if(!p.hasPermission("1vs1.Command.createTournament")) {
						
						return true;
					}
					
					UUID tournamentID = null;
					
					if(Main.ins.getOneVsOnePlayer(p).getPlayertournament() != null) {
						tournamentID = Main.ins.getOneVsOnePlayer(p).getPlayertournament();
					} else {
						tournamentID = UUID.randomUUID();
						
						Main.ins.getOneVsOnePlayer(p).setPlayertournament(tournamentID);
						
						OneVsOneTournament tournament = new OneVsOneTournament(p.getUniqueId(), 
								tournamentID, "", 
								Main.ins.getOneVsOnePlayer(p).getKitLoaded());
						
						Main.ins.tournaments.put(tournamentID, tournament);
						
						if(Main.ins.getOneVsOnePlayer(p).getTeamObj() != null) {
							tournament.addParticipant(p, Main.ins.getOneVsOnePlayer(p).getTeamObj().getMemberList());
						} else {
							tournament.addParticipant(p, new ArrayList<>());
							
						}
						
						
					}
					
					
					
					
					TournamentInv.openTournamentMainInv(p, tournamentID);
					
				} else {
					p.sendMessage(Main.ins.prefixRed + "§cDiesen Befehl darfst du hier nicht benutzen!");
				}
				
			}
			
			
		}
		return true;
	}

}
