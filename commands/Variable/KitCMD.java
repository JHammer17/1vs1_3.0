package de.onevsone.commands.Variable;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.methods.KitMgr;
import de.onevsone.objects.OneVsOnePlayer;

public class KitCMD implements CommandExecutor {

	public KitCMD() {
		startChecker();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
			if(player.isIn1vs1()) {
				if(player.getpState() == PlayerState.INLOBBY || 
				   player.getpState() == PlayerState.INKITEDIT) {
					
					
					
					if(args.length == 0) {
						int subID = Main.ins.database.getSelectedKit(p.getUniqueId());
						KitMgr.loadKit(p, p.getUniqueId(), false, subID);
						KitMgr.loadKit(p, p.getUniqueId(), true, subID);
						
						KitMgr.sendKitInfos(p.getUniqueId(), subID, p);
						if(player.getpState() == PlayerState.INLOBBY) player.setHasKit(true);
						player.setKitLoaded(p.getUniqueId() + ":" + subID);
					} else {
						try {
							int subID = Integer.parseInt(args[0]);
							
							if(subID > 0 && subID < 6) {
								KitMgr.loadKit(p, p.getUniqueId(), false, subID);
								KitMgr.loadKit(p, p.getUniqueId(), true, subID);
								
								KitMgr.sendKitInfos(p.getUniqueId(), subID, p);
								if(player.getpState() == PlayerState.INLOBBY) player.setHasKit(true);
								player.setKitLoaded(p.getUniqueId() + ":" + subID);
								return true;
							}
							
							
							
						} catch (NumberFormatException e) {}
						
						int subID = 1;
						String kit = args[0];
						
						if(args[0].contains(":")) {
							
							String[] a = args[0].split(":");
							
							try {
								if(a.length != 2) {
									p.sendMessage(Main.ins.prefixRed + "Nutze: /kit [Name]:{SubID}");
									return true;
								}
								
								subID = Integer.parseInt(a[1]);
								
								if(subID > 5 || subID < 1) {
									p.sendMessage(Main.ins.prefixRed + "Nutze: /kit [Name]:{SubID}");
									return true;
								}
								
								kit = a[0];
								
							} catch (NumberFormatException e) {
								p.sendMessage(Main.ins.prefixRed + "Nutze: /kit [Name]:{SubID}");
								return true;
							}
						}
							
							if(KitMgr.kitExists(Main.ins.database.getUUID(kit))) {
								UUID uuid = Main.ins.database.getUUID(kit);
								KitMgr.loadKit(p, uuid, false, subID);
								KitMgr.loadKit(p, uuid, true, subID);
								
								KitMgr.sendKitInfos(uuid, subID, p);
								
								if(player.getpState() == PlayerState.INLOBBY) player.setHasKit(true);
								
								player.setKitLoaded(uuid + ":" + subID);
								
								return true;
								
							} else if(Main.ins.database.getKitType(args[0]) == 2) {
								
								KitMgr.loadKit(p, args[0], false, subID);
								KitMgr.loadKit(p, args[0], true, subID);
								
								KitMgr.sendKitInfos(args[0], p);
								
								if(player.getpState() == PlayerState.INLOBBY) player.setHasKit(true);
								player.setKitLoaded(args[0] + ":1");
								
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

	public static void startChecker() {

		final HashMap<UUID, Location> playerLocs = new HashMap<>();
		new BukkitRunnable() {

			@Override
			public void run() {

				for (final OneVsOnePlayer p : Main.ins.getOneVsOnePlayersCopy().values()) {
					if (p == null || playerLocs == null)
						continue;

					if (playerLocs.containsKey(p.getPlayer().getUniqueId())
							&& playerLocs.get(p.getPlayer().getUniqueId()).getWorld().getName()
									.equals(p.getPlayer().getWorld().getName())) {
						if (playerLocs.get(p.getPlayer().getUniqueId()).distance(p.getPlayer().getLocation()) > 0.2) {
							onMove(p.getPlayer());
						}
					} else {
						onMove(p.getPlayer());
					}

						
				}

				playerLocs.clear();

				for (final OneVsOnePlayer p : Main.ins.getOneVsOnePlayersCopy().values()) {
					playerLocs.put(p.getPlayer().getUniqueId(), p.getPlayer().getLocation());
				}

			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 1);
	}

	public static void onMove(final Player p) {
		OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
		if(player.isIn1vs1()) {

				if(player.isHasKit()) {
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					player.setHasKit(false);
					
					if(player.getpState() == PlayerState.INLOBBY) {
						Main.ins.utils.giveLobbyItems(p);
					}
				}
				
			
		}
	}
	
	
	
}
