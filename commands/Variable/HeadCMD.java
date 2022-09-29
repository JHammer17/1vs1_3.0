/**
 * 
 */
package de.onevsone.commands.Variable;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 14.08.2019 17:08:59					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class HeadCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INKITEDIT) {
					if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
						
						if(p.hasPermission("1vs1.Command.Head") || p.hasPermission("1vs1.Premium") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
							p.getInventory().setHelmet(p.getItemInHand());
							p.sendMessage(Main.ins.prefixGreen + "Du hast nun einen neuen Kopf!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "Für diesen Befehl hast du nicht die benötigten Berechtigungen!");
						}
						
						
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "Du musst ein Item in der Hand halten!");
					}
 				} else {
 					p.sendMessage(Main.ins.prefixRed + "Diesen Befehl kannst du hier nicht ausführen");
 				}
			} else {
				p.sendMessage(Main.ins.prefixRed + "Diesen Befehl kannst du nur in 1vs1 ausführen.");
			}
			
		} else {
			sender.sendMessage(Main.ins.prefixRed + "Du bist kein Spieler.");
		}
		
		
		return true;
	}

}
