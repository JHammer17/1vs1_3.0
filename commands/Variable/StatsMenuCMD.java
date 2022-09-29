package de.onevsone.commands.Variable;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.methods.StatsMenu;

public class StatsMenuCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
				if(args.length == 0) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							StatsMenu.openStatsMenu(p, p.getUniqueId(), null, false);
						}
					}.runTaskAsynchronously(Main.ins);
				} else 
				
				if(args.length == 1) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							UUID uuid = Main.ins.database.getUUID(args[0]);
							if(uuid != null) {
								StatsMenu.openStatsMenu(p, uuid, null, false);
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cUser nicht gefunden!");
							}
						}
					}.runTaskAsynchronously(Main.ins);
				}
			
			}
			
			
			
			
		}
		
		
		return true;
	}

}
