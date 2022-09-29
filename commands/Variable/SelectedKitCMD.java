/**
 * 
 */
package de.onevsone.commands.Variable;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 28.03.2018 11:48:45					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class SelectedKitCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("1vs1.Command.selectedKit")) {
				if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
					if(Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.UNKNOWN) {
						
						
						String kit = "";
						
						try {
							kit = Main.ins.getOneVsOnePlayer(p).getKitLoaded();
							
							if(kit.contains(":")) {
								String[] sKit = kit.split(":");
								kit = Main.ins.database.getUserName(UUID.fromString(sKit[0])) + ":" + sKit[1];
							}
							
						} catch (Exception e) {}
						
						p.sendMessage(Main.ins.prefixGreen + "Ausgewähltes Kit: §6" + kit);
						
						
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
