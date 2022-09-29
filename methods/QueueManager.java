package de.onevsone.methods;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.enums.BestOfsPrefs;
import de.onevsone.enums.PlayerState;
import de.onevsone.objects.OneVsOneArena;
import de.onevsone.objects.OneVsOnePlayer;

public class QueueManager {

	public QueueManager() {
		startChecker();
	}
	
	public void startChecker() {
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArrayList<OneVsOnePlayer> found = new ArrayList<>();
				
				for(OneVsOnePlayer p1 : getQueueList()) {
					for(OneVsOnePlayer p2 : getQueueList()) {
						if(!p1.equals(p2) && !found.contains(p1) && !found.contains(p2)) {
							
							if(p1.getPlayer().isOnline() && p2.getPlayer().isOnline()) {
								if(Main.ins.isInOneVsOnePlayers(p1.getPlayer().getUniqueId()) && Main.ins.isInOneVsOnePlayers(p2.getPlayer().getUniqueId())) {
									if(p1.getpState() == PlayerState.INLOBBY && p2.getpState() == PlayerState.INLOBBY) {
										if(compare(p1.getPlayer().getUniqueId(), p2.getPlayer().getUniqueId())) {
											if(tryJoin(p1.getPlayer().getUniqueId(), p2.getPlayer().getUniqueId(), true)) {
												found.add(p1);
												found.add(p2);
											}
										} 
									}
								}
									
							}
							
							
							
						
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 1);
	}
	
	public static boolean tryJoin(UUID p1, UUID p2, boolean byQueue) {
		
		if(byQueue && !compare(p1, p2)) return false;
			
		
		
		String arena = getRndmArena(getRndmMap(p1, p2, byQueue));
		
		if(arena == null) {
			if(Bukkit.getPlayer(p1) != null) {
				Bukkit.getPlayer(p1).sendMessage(Main.ins.prefixRed + "§cEs konnte keine freie Arena gefunden werden!");
			}
			if(Bukkit.getPlayer(p2) != null) {
				Bukkit.getPlayer(p2).sendMessage(Main.ins.prefixRed + "§cEs konnte keine freie Arena gefunden werden!");
			}
			return false;
		}
		
		int rankedType1 = Main.ins.database.isQueuePrefEnabled(p1, 3);
		int rankedType2 = Main.ins.database.isQueuePrefEnabled(p2, 3);
		
		int bestOfType = Main.ins.database.isQueuePrefEnabled(p1, 2);
		
		new BukkitRunnable() {
				
				@Override
				public void run() {
					
					
					
					if(Main.ins.isInOneVsOneArenas(arena) && !Main.ins.getOneVsOneArena(arena).isUsed() && !Main.ins.getOneVsOneArena(arena).isLocked()) {
						
					 boolean playRanked = false;
					 if(rankedType1 == 0 && rankedType2 == 0) playRanked = new Random().nextInt(2) == 0;
						
					 if(rankedType1 == 1) playRanked = true;
					 if(rankedType1 == 2) playRanked = false;
					 
					 if(rankedType2 == 1) playRanked = true;
					 if(rankedType2 == 2) playRanked = false;
					 
					if(byQueue) {
						 Main.ins.getOneVsOneArena(arena).setRanked(playRanked);
					} else {
						Main.ins.getOneVsOneArena(arena).setRanked(false);
					}
					 
					 
					
					 if(bestOfType == 1) Main.ins.getOneVsOneArena(arena).setBestOf(BestOfsPrefs.BESTOF1);
					 
					 if(bestOfType == 2) Main.ins.getOneVsOneArena(arena).setBestOf(BestOfsPrefs.BESTOF3);
					 if(bestOfType == 3) Main.ins.getOneVsOneArena(arena).setBestOf(BestOfsPrefs.BESTOF5);
					 
					 Main.ins.getOneVsOneArena(arena).setBestOfWinsP1(0);
					 Main.ins.getOneVsOneArena(arena).setBestOfWinsP2(0);
					 
					 
					 
					 if(byQueue) {
						 
						 ArrayList<Player> pos1Players = new ArrayList<>();
						 ArrayList<Player> pos2Players = new ArrayList<>();
						 
						 if(Main.ins.getOneVsOnePlayer(p1).getTeamObj() != null) {
							 for(UUID uuid : Main.ins.getOneVsOnePlayer(p1).getTeamObj().getAll()) {
								 if(Bukkit.getPlayer(uuid) != null) 
									 pos1Players.add(Bukkit.getPlayer(uuid));
							 }
						 } else {
							 pos1Players.add(Bukkit.getPlayer(p1));
						 }
 						 
						 if(Main.ins.getOneVsOnePlayer(p2).getTeamObj() != null) {
							 for(UUID uuid : Main.ins.getOneVsOnePlayer(p2).getTeamObj().getAll()) {
								 if(Bukkit.getPlayer(uuid) != null) 
									 pos2Players.add(Bukkit.getPlayer(uuid));
							 }
						 } else {
							 pos2Players.add(Bukkit.getPlayer(p2));
						 }
						 
						 if(Main.ins.isInOneVsOneArenas(arena)) {
							 if(!Main.ins.getOneVsOneArena(arena).hasErrors()) {
								 new BukkitRunnable() {
									
									@Override
									public void run() {
										Main.ins.getOneVsOneArena(arena).join(pos1Players, pos2Players, getKit(p1, p2), Main.ins.database.getSelectedKit(p1));
									}
								}.runTask(Main.ins);
							 } else {
								 for(Player player : pos1Players) {
									 if(player != null && player.isOnline()) 
										 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (2)");
								 }
								 for(Player player : pos2Players) {
									 if(player != null && player.isOnline()) 
										 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (2)");
								 }
							 }
							 
							 
						 } else {
							 for(Player player : pos1Players) {
								 if(player != null && player.isOnline()) 
									 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (1)");
							 }
							 for(Player player : pos2Players) {
								 if(player != null && player.isOnline()) 
									 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (1)");
							 }
						 }
						 
						 
						
					 } else {
						 ArrayList<Player> pos1Players = new ArrayList<>();
						 ArrayList<Player> pos2Players = new ArrayList<>();
						 
						 if(Main.ins.getOneVsOnePlayer(p1).getTeamObj() != null) {
							 for(UUID uuid : Main.ins.getOneVsOnePlayer(p1).getTeamObj().getAll()) {
								 if(Bukkit.getPlayer(uuid) != null) 
									 pos1Players.add(Bukkit.getPlayer(uuid));
							 }
						 } else {
							 pos1Players.add(Bukkit.getPlayer(p1));
						 }
 						 
						 if(Main.ins.getOneVsOnePlayer(p2).getTeamObj() != null) {
							 for(UUID uuid : Main.ins.getOneVsOnePlayer(p2).getTeamObj().getAll()) {
								 if(Bukkit.getPlayer(uuid) != null) 
									 pos2Players.add(Bukkit.getPlayer(uuid));
							 }
						 } else {
							 pos2Players.add(Bukkit.getPlayer(p2));
						 }
						 
						 
						 if(Main.ins.isInOneVsOneArenas(arena)) {
							 if(!Main.ins.getOneVsOneArena(arena).hasErrors()) {
								 new BukkitRunnable() {
									
									@Override
									public void run() {
										Main.ins.getOneVsOneArena(arena).join(pos1Players, pos2Players, Main.ins.getOneVsOnePlayer(p1).getKitLoaded(), Main.ins.database.getSelectedKit(p1));
									}
								}.runTask(Main.ins);
							 } else {
								 for(Player player : pos1Players) {
									 if(player != null && player.isOnline()) 
										 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (2)");
								 }
								 for(Player player : pos2Players) {
									 if(player != null && player.isOnline()) 
										 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (2)");
								 }
							 }
							 
							 
						 } else {
							 for(Player player : pos1Players) {
								 if(player != null && player.isOnline()) 
									 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (1)");
							 }
							 for(Player player : pos2Players) {
								 if(player != null && player.isOnline()) 
									 player.sendMessage(Main.ins.prefixRed + "Die Arena " + arena + " hat einen Fehler! (1)");
							 }
						 }
						 
						
					 }
					
					 Main.ins.getOneVsOnePlayer(p1).setWasInQueue(byQueue);
					 Main.ins.getOneVsOnePlayer(p2).setWasInQueue(byQueue);
					 
					
					
					}
				  
				}
			}.runTaskAsynchronously(Main.ins);
		
		
		return true;
	}
	
	
	
	public static String getKit(UUID p1, UUID p2) {
		
		int kitType1 = Main.ins.database.isQueuePrefEnabled(p1, 1);
		int kitType2 = Main.ins.database.isQueuePrefEnabled(p2, 1);
		
		if(kitType1 == 0) {
			return Main.ins.getOneVsOnePlayer(p1).getKitLoaded();
		}
		
		if(kitType1 == 1) {
			return Main.ins.getOneVsOnePlayer(p2).getKitLoaded();
		}
		
		if(kitType1 == 2 && kitType2 == 0) {
			return Main.ins.getOneVsOnePlayer(p2).getKitLoaded();
		}
		else  {
			return Main.ins.getOneVsOnePlayer(p1).getKitLoaded();
		}
	}
	
	
	public static String getRndmArena(String map) {
		if(map == null) return null;
		ArrayList<String> arenaList = new ArrayList<>();
		for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
			if(!arenas.isUsed() && !arenas.isLocked() && arenas.getLayout().equalsIgnoreCase(map)) {
				arenaList.add(arenas.getName());
			}
		}
		
		
		if(arenaList.isEmpty()) return null;
		
		Random r = new Random();
		
		
		
		
		return arenaList.get(r.nextInt(arenaList.size()));
	}
	
	@SuppressWarnings("unchecked")
	public static String getRndmMap(UUID p1, UUID p2, boolean byQueue) {
		String disabledMaps1 = Main.ins.database.getDisabledMaps(p1);
		String disabledMaps2 = Main.ins.database.getDisabledMaps(p2);
		
		ArrayList<String> layouts = new ArrayList<>();
		ArrayList<String> allLayouts = new ArrayList<>();;
		
			for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
				if(!layouts.contains(arenas.getLayout()) && !arenas.isUsed() && !arenas.isLocked()) {	
				 layouts.add(arenas.getLayout());
				 allLayouts.add(arenas.getLayout());
				} 
			}
			
		for(String map : disabledMaps1.split(" ")) 
			layouts.remove(map);
		
		
		for(String map : disabledMaps2.split(" ")) 
			layouts.remove(map);
		
		
		if(layouts.isEmpty() && byQueue) {
			return null;
		} else if(layouts.isEmpty() && !byQueue) {
			layouts = (ArrayList<String>) allLayouts.clone();
			for(String map : disabledMaps1.split(" ")) 
				layouts.remove(map);
			
			if(layouts.isEmpty()) {
				layouts = (ArrayList<String>) allLayouts.clone();
				for(String map : disabledMaps2.split(" ")) 
					layouts.remove(map);
				
				if(layouts.isEmpty()) {
					layouts = (ArrayList<String>) allLayouts.clone();
				}
			}
			
		}
		
		if(layouts.isEmpty()) return null;
		
		Random r = new Random();
		
		return layouts.get(r.nextInt(layouts.size()));
		
	}
	
	
	public static boolean compare(UUID p1, UUID p2) {
		
		String disabledMaps1 = Main.ins.database.getDisabledMaps(p1);
		String disabledMaps2 = Main.ins.database.getDisabledMaps(p2);
		
		ArrayList<String> layouts = getAllLayouts();
		
		for(String map : disabledMaps1.split(" ")) 
			layouts.remove(map);
		
		
		for(String map : disabledMaps2.split(" ")) 
			layouts.remove(map);
		
		if(layouts.isEmpty()) return false;
		
		
		int kitType1 = Main.ins.database.isQueuePrefEnabled(p1, 1);
		int kitType2 = Main.ins.database.isQueuePrefEnabled(p2, 1);
		
		if((kitType1 == 0 && (kitType2 == 0)) || 
			kitType1 == 1 && (kitType2 == 1)) return false;
		
		
		int bestOfType1 = Main.ins.database.isQueuePrefEnabled(p1, 2);
		int bestOfType2 = Main.ins.database.isQueuePrefEnabled(p2, 2);
		
		
		if(bestOfType1 != bestOfType2) return false;
		
		int rankedType1 = Main.ins.database.isQueuePrefEnabled(p1, 3);
		int rankedType2 = Main.ins.database.isQueuePrefEnabled(p2, 3);
		
		if((rankedType1 == 1 && rankedType2 == 2) || 
		   (rankedType1 == 2 && rankedType2 == 1)) return false;
		
		return true;
	}
	
	
	public ArrayList<OneVsOnePlayer> getQueueList() {
		ArrayList<OneVsOnePlayer> queuedPlayers = new ArrayList<>();
		for(OneVsOnePlayer player : Main.ins.getOneVsOnePlayersCopy().values()) 
			if(player.isInQueue()) 
				queuedPlayers.add(player);
		
		return queuedPlayers;
	}
	
	public ArrayList<UUID> getQueueListUUID() {
		ArrayList<UUID> queuedPlayers = new ArrayList<>();
		for(OneVsOnePlayer player : Main.ins.getOneVsOnePlayersCopy().values()) 
			if(player.isInQueue()) 
				queuedPlayers.add(player.getPlayer().getUniqueId());
		
		return queuedPlayers;
	}
	
	public static ArrayList<String> getAllLayouts() {
		ArrayList<String> allLayouts = new ArrayList<>();
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
		
		if(cfg.getConfigurationSection("Layout") != null) {
			for(String layout : cfg.getConfigurationSection("Layout").getKeys(false)) {
				allLayouts.add(layout);
			}
		}
		
		
		return allLayouts;
	}
	
	
}
