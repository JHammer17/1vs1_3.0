/**
 * 
 */
package de.onevsone.commands.Variable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.Inventories.subInvs.ColorSettingsInv;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 25.03.2018 22:03:24					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class ColorCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("1vs1.Command.Color")) {
				if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
					if(Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.UNKNOWN) {
						ColorSettingsInv.open(p);
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
