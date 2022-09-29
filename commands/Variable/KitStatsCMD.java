package de.onevsone.commands.Variable;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.methods.KitMgr;
import de.onevsone.objects.OneVsOnePlayer;

public class KitStatsCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
			if(player.isIn1vs1()) {
				if(player.getpState() == PlayerState.INLOBBY || 
				   player.getpState() == PlayerState.INKITEDIT) {
					
					
					
					
					if(args.length == 1) {
						
						
						int subID = 1;
						String kit = args[0];
						
						if(args[0].contains(":")) {
							
							String[] a = args[0].split(":");
							
							try {
								if(a.length != 2) {
									p.sendMessage(Main.ins.prefixRed + "Nutze: /kitstats [Name]:{SubID}");
									return true;
								}
								
								subID = Integer.parseInt(a[1]);
								
								if(subID > 5 || subID < 1) {
									p.sendMessage(Main.ins.prefixRed + "Nutze: /kitstats [Name]:{SubID}");
									return true;
								}
								
								kit = a[0];
								
							} catch (NumberFormatException e) {
								p.sendMessage(Main.ins.prefixRed + "Nutze: /kitstats [Name]:{SubID}");
								return true;
							}
						}
							
							if(KitMgr.kitExists(Main.ins.database.getUUID(kit))) {
								UUID uuid = Main.ins.database.getUUID(kit);
								
								int alltime = Main.ins.database.getKitStats(uuid.toString(), 1, subID);
								int timed30d = Main.ins.database.getKitStats(uuid.toString(), 2, subID);
								int timed24h = Main.ins.database.getKitStats(uuid.toString(), 3, subID);
								
								p.sendMessage(Main.ins.prefixBlue + "§7Statistiken für Kit: §6" + args[0]);
								p.sendMessage("§7Alltime: §6" + alltime);
								p.sendMessage("§7Letzte 30 Tage: §6" + timed30d);
								p.sendMessage("§7Letzte 24 Stunden: §6" + timed24h);
								
//								
								return true;
								
							} else if(Main.ins.database.getKitType(args[0]) == 2) {
								
								
								int alltime = Main.ins.database.getKitStats(Main.ins.database.resolveCustomKit(args[0]), 1, 1);
								int timed30d = Main.ins.database.getKitStats(Main.ins.database.resolveCustomKit(args[0]), 2, 1);
								int timed24h = Main.ins.database.getKitStats(Main.ins.database.resolveCustomKit(args[0]), 3, 1);
								
								p.sendMessage(Main.ins.prefixBlue + "§7Statistiken für Kit: §6" + args[0]);
								p.sendMessage("§7Alltime: §6" + alltime);
								p.sendMessage("§7Letzte 30 Tage: §6" + timed30d);
								p.sendMessage("§7Letzte 24 Stunden: §6" + timed24h);
								
								
//								
								return true;
								
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cDieses Kit exestiert nicht!");
							}
							
						
						
						
					}
					
				} else {
					p.sendMessage(Main.ins.prefixRed + "§cDiesen Befehl kannst du hier nicht ausführen.");
				}
			} else {
				p.sendMessage(Main.ins.prefixRed + "§cDu bist nicht in 1vs1.");
			}
		} 
		return true;
	}
	
}
