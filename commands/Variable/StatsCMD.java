package de.onevsone.commands.Variable;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;

public class StatsCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if((sender instanceof Player) && args.length == 0) {
			showStats(((Player)sender), "", false, 1, ((Player)sender).getUniqueId());
			return true;
		}
		
		if(args.length == 0) {
			sender.sendMessage("§cDu hast keine Stats!");
			return true;
		}
		
		if((sender instanceof Player) && args.length == 1) {
			Player p = (Player)sender;
			
			if(args[0].equalsIgnoreCase("alltime") || args[0].equalsIgnoreCase("all")) {
				showStats(sender, "", false, 1, p.getUniqueId());
				return true;	
			} else if(args[0].equalsIgnoreCase("30D") || args[0].equalsIgnoreCase("30Days") || args[0].equalsIgnoreCase("30")) {
				showStats(sender, "", false, 2, p.getUniqueId());
				return true;	
			} else if(args[0].equalsIgnoreCase("24h") || args[0].equalsIgnoreCase("30Hours") || args[0].equalsIgnoreCase("24")) {
				showStats(sender, "", false, 3, p.getUniqueId());
				return true;	
			} 
			
		}
		if(args.length == 1) {
			if(Main.ins.database.getUUID(args[0]) != null) {
				UUID uuid = Main.ins.database.getUUID(args[0]);
				String name = Main.ins.database.getUserName(uuid);
				showStats(sender, name, true, 1, uuid);
				return true;
			} else {
				sender.sendMessage(Main.ins.prefixRed + "§cUser nicht gefunden!");
			}
		} else if(args.length == 2) {
			if(Main.ins.database.getUUID(args[0]) != null) {
				UUID uuid = Main.ins.database.getUUID(args[0]);
				String name = Main.ins.database.getUserName(uuid);
				
				if(args[1].equalsIgnoreCase("alltime") || args[1].equalsIgnoreCase("all")) {
					showStats(sender, name, true, 1, uuid);
					return true;	
				} else if(args[1].equalsIgnoreCase("30D") || args[1].equalsIgnoreCase("30Days") || args[1].equalsIgnoreCase("30")) {
					showStats(sender, name, true, 2, uuid);
					return true;	
				} else if(args[1].equalsIgnoreCase("24h") || args[1].equalsIgnoreCase("30Hours") || args[1].equalsIgnoreCase("24")) {
					showStats(sender, name, true, 3, uuid);
					return true;	
				} 
			} else {
				sender.sendMessage(Main.ins.prefixRed + "§cUser nicht gefunden!");
			}
		}
		
		sender.sendMessage(Main.ins.prefixRed + "§cNutze: /stats {Spieler|all|30d|24h}");
		
		return true;
	}
	
	private void showStats(CommandSender p, String shownName, boolean showName, int timed, UUID target) {
		if(!showName) {
			p.sendMessage("§8╔ §a§lDeine Statistiken");
			p.sendMessage("§8║");
		} else {
			p.sendMessage("§8╔ §a§lStatistiken von " + shownName);
			p.sendMessage("§8║");
		}
		
		if(timed == 1) {
			p.sendMessage("§8╠ §6§lAlltime:");
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Kämpfe: §6" + Main.ins.database.getStatsAsInt(target, 1, 1));
			p.sendMessage("§8╠ §7davon gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 1, 2));
			p.sendMessage("§8╠ §7EZ gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 1, 4));
			p.sendMessage("§8╠ §7Knapp Gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 1, 5));
			p.sendMessage("§8╠ §7Verloren: §6" + Main.ins.database.getStatsAsInt(target, 1, 3));
			p.sendMessage("§8╠ §7W/L: §6" + Main.ins.database.getStats(target, 1, 8));
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Kills: §6" + Main.ins.database.getStatsAsInt(target, 1, 6));
			p.sendMessage("§8╠ §7Tode: §6" + Main.ins.database.getStatsAsInt(target, 1, 7));
			p.sendMessage("§8╠ §7K/D: §6" + Main.ins.database.getStats(target, 1, 9));
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Position im Ranking: §6" + Main.ins.database.getPosition(target, 1));
		} else if(timed == 2) {
			p.sendMessage("§8╠ §6§lLetzte 30 Tage:");
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Kämpfe: §6" + Main.ins.database.getStatsAsInt(target, 2, 1));
			p.sendMessage("§8╠ §7davon gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 2, 2));
			p.sendMessage("§8╠ §7EZ gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 2, 4));
			p.sendMessage("§8╠ §7Knapp Gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 2, 5));
			p.sendMessage("§8╠ §7Verloren: §6" + Main.ins.database.getStatsAsInt(target, 2, 3));
			p.sendMessage("§8╠ §7W/L: §6" + Main.ins.database.getStats(target, 2, 8));
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Kills: §6" + Main.ins.database.getStatsAsInt(target, 2, 6));
			p.sendMessage("§8╠ §7Tode: §6" + Main.ins.database.getStatsAsInt(target, 2, 7));
			p.sendMessage("§8╠ §7K/D: §6" + Main.ins.database.getStats(target, 2, 9));
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Position im Ranking: §6" + Main.ins.database.getPosition(target, 2));
		} else if(timed == 3) {
			p.sendMessage("§8╠ §6§lLetzte 24 Stunden:");
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Kämpfe: §6" + Main.ins.database.getStatsAsInt(target, 3, 1));
			p.sendMessage("§8╠ §7davon gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 3, 2));
			p.sendMessage("§8╠ §7EZ gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 3, 4));
			p.sendMessage("§8╠ §7Knapp Gewonnen: §6" + Main.ins.database.getStatsAsInt(target, 3, 5));
			p.sendMessage("§8╠ §7Verloren: §6" + Main.ins.database.getStatsAsInt(target, 3, 3));
			p.sendMessage("§8╠ §7W/L: §6" + Main.ins.database.getStats(target, 3, 8));
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Kills: §6" + Main.ins.database.getStatsAsInt(target, 3, 6));
			p.sendMessage("§8╠ §7Tode: §6" + Main.ins.database.getStatsAsInt(target, 3, 7));
			p.sendMessage("§8╠ §7K/D: §6" + Main.ins.database.getStats(target, 3, 9));
			p.sendMessage("§8║");
			p.sendMessage("§8╠ §7Position im Ranking: §6" + Main.ins.database.getPosition(target, 3));
		} 
		
		p.sendMessage("§8╚");
		
	}

}
