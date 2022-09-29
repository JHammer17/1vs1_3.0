package de.onevsone.listener.statsmgr;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.objects.PlayerStatsData;

public class SkullMgr implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {	
				Player p = e.getPlayer();
				
				
				if(Main.ins.getOneVsOnePlayer(p).getSkullModePlace() > 0) {
					
					int place = Main.ins.getOneVsOnePlayer(p).getSkullModePlace();
					
					if(e.getClickedBlock().getType() == Material.SKULL) {
						e.setCancelled(true);
						
						YamlConfiguration cfg = Main.ins.utils.getYaml("Signs");
						
						Location clicked = e.getClickedBlock().getLocation();
						
						String suffix = "";
						if(Main.ins.getOneVsOnePlayer(p).getSkullModeTimed() == 1) {
							suffix = "30d";
						} else if(Main.ins.getOneVsOnePlayer(p).getSkullModeTimed() == 2) {
							suffix = "24h";
						} 
						
						
						cfg.set("Top.Skull.Top" + suffix +"." + place + ".X" , clicked.getBlockX());
						cfg.set("Top.Skull.Top" + suffix + "." + place + ".Y" , clicked.getBlockY());
						cfg.set("Top.Skull.Top" + suffix + "." + place + ".Z" , clicked.getBlockZ());
						cfg.set("Top.Skull.Top" + suffix + "." + place + ".World" , clicked.getWorld().getName());
						
						try {
							cfg.save(Main.ins.utils.getPluginFile("Signs"));
						} catch (IOException e1) {
							p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
							return;
						}

						Main.ins.getOneVsOnePlayer(p).setSkullModeTimed(-1);
						Main.ins.getOneVsOnePlayer(p).setSkullModePlace(-1);
						p.sendMessage(Main.ins.prefixGreen + "Kopf gesetzt!");
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cDu musst einen Rechtsklick auf einen Kopf machen, um ihn zu setzen!");
					}
					
					
					
			} 
		}
	}
	
	
	static HashMap<Integer, TopSkull> skulls = new HashMap<>();
	
	public static void reloadSkulls() {
		skulls.clear();
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Signs");
		
		if(cfg.getConfigurationSection("Top.Skull.Top") != null ) {
			for(String str : cfg.getConfigurationSection("Top.Skull.Top").getKeys(false)) {
				try {
				 	
					int place = Integer.parseInt(str);
					
					int x = cfg.getInt("Top.Skull.Top." + str + ".X");
					int y = cfg.getInt("Top.Skull.Top." + str + ".Y");
					int z = cfg.getInt("Top.Skull.Top." + str + ".Z");
					
					String world = cfg.getString("Top.Skull.Top." + str + ".World");
					
					if(world != null) {	
					 Location loc = new Location(Bukkit.getWorld(world), x, y, z);
					 skulls.put(skulls.size()+1, new TopSkull(loc, place, 0));
					}
				} catch (NumberFormatException e) {}
 			}
		}
		
		if(cfg.getConfigurationSection("Top.Skull.Top30d") != null ) {
			for(String str : cfg.getConfigurationSection("Top.Skull.Top30d").getKeys(false)) {
				try {
				 	
					int place = Integer.parseInt(str);
					
					int x = cfg.getInt("Top.Skull.Top30d." + str + ".X");
					int y = cfg.getInt("Top.Skull.Top30d." + str + ".Y");
					int z = cfg.getInt("Top.Skull.Top30d." + str + ".Z");
					
					String world = cfg.getString("Top.Skull.Top30d." + str + ".World");
					
					if(world != null) {	
					 Location loc = new Location(Bukkit.getWorld(world), x, y, z);
					 skulls.put(skulls.size()+1, new TopSkull(loc, place, 1));
					}
				} catch (NumberFormatException e) {}
 			}
		}
		
		if(cfg.getConfigurationSection("Top.Skull.Top24h") != null ) {
			for(String str : cfg.getConfigurationSection("Top.Skull.Top").getKeys(false)) {
				try {
				 	
					int place = Integer.parseInt(str);
					
					int x = cfg.getInt("Top.Skull.Top24h." + str + ".X");
					int y = cfg.getInt("Top.Skull.Top24h." + str + ".Y");
					int z = cfg.getInt("Top.Skull.Top24h." + str + ".Z");
					
					String world = cfg.getString("Top.Skull.Top24h." + str + ".World");
					
					if(world != null) {	
					 Location loc = new Location(Bukkit.getWorld(world), x, y, z);
					 skulls.put(skulls.size()+1, new TopSkull(loc, place, 2));
					}
				} catch (NumberFormatException e) {}
 			}
		}
		
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				refreshSkulls();
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	public static void refreshSkulls() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				HashMap<Integer, PlayerStatsData> stats = Main.ins.database.getTop(1);
				HashMap<Integer, PlayerStatsData> stats30 = Main.ins.database.getTop(2);
				HashMap<Integer, PlayerStatsData> stats24 = Main.ins.database.getTop(3);
				
				for(TopSkull tS : skulls.values()) {
					
					if(tS.getLocation() != null && tS.getLocation().getWorld() != null) {
						String data = "";
						
						
						
						if(tS.getTimed() == 0) {
							if(stats.get(tS.getPlace()) != null && 
							   stats.get(tS.getPlace()).getUuid() != null) 
								data = Main.ins.database.getSkin(stats.get(tS.getPlace()).getUuid());
						} else if(tS.getTimed() == 1) {
							if(stats30.get(tS.getPlace()) != null && 
							   stats30.get(tS.getPlace()).getUuid() != null) 
								data = Main.ins.database.getSkin(stats30.get(tS.getPlace()).getUuid());
						} else if(tS.getTimed() == 2) {
							if(stats24.get(tS.getPlace()) != null &&
							   stats24.get(tS.getPlace()).getUuid() != null) 
								data = Main.ins.database.getSkin(stats24.get(tS.getPlace()).getUuid());
						} 
						
						
						
						
						final String dataF = data;
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								
								if(tS.getLocation().getBlock().getType() != Material.SKULL)return;
								
								Skull skull = (Skull) tS.getLocation().getBlock().getState();
								skull.setSkullType(SkullType.PLAYER);;
								
								
								if(tS.getTimed() == 0) {
									if(stats.containsKey(tS.getPlace()) && 
											   stats.get(tS.getPlace()).getName() != null) {
												if(Main.ins.useSkinDataFromDB) {

													 Main.ins.utils.getSkullPlacer().setSkullTexture(dataF,  skull.getBlock());
												} else {
													skull.setOwner(stats.get(tS.getPlace()).getName());
													skull.update();
												}
												
											} else {
												if(Main.ins.useSkinDataFromDB) {
													 Main.ins.utils.getSkullPlacer().setSkullTexture(Main.ins.utils.getUnknownSkinTexture(),  skull.getBlock());
												} else {
													skull.setOwner("MHF_QUESTION");
													skull.update();
												}
												
											}
								} else if(tS.getTimed() == 1) {
									if(stats30.containsKey(tS.getPlace()) && 
									   stats30.get(tS.getPlace()).getName() != null) {
												if(Main.ins.useSkinDataFromDB) {

													 Main.ins.utils.getSkullPlacer().setSkullTexture(dataF,  skull.getBlock());
												} else {
													skull.setOwner(stats30.get(tS.getPlace()).getName());
													skull.update();
												}
												
											} else {
												if(Main.ins.useSkinDataFromDB) {
													 Main.ins.utils.getSkullPlacer().setSkullTexture(Main.ins.utils.getUnknownSkinTexture(),  skull.getBlock());
												} else {
													skull.setOwner("MHF_QUESTION");
													skull.update();
												}
												
											}
								} else if(tS.getTimed() == 2) {
									if(stats24.containsKey(tS.getPlace()) && 
											stats24.get(tS.getPlace()).getName() != null) {
												if(Main.ins.useSkinDataFromDB) {

													 Main.ins.utils.getSkullPlacer().setSkullTexture(dataF,  skull.getBlock());
												} else {
													skull.setOwner(stats24.get(tS.getPlace()).getName());
													skull.update();
												}
												
											} else {
												if(Main.ins.useSkinDataFromDB) {
													 Main.ins.utils.getSkullPlacer().setSkullTexture(Main.ins.utils.getUnknownSkinTexture(),  skull.getBlock());
												} else {
													skull.setOwner("MHF_QUESTION");
													skull.update();
												}
												
											}
								} 
								
								
								
								
								
								
							}
						}.runTask(Main.ins);
					}
					
				}				
		  }
		}.runTaskAsynchronously(Main.ins);
	}
		
	public static class TopSkull {
		
		private Location loc;
		private int place;
		private int timed;

		public TopSkull(Location loc, int place, int timed) {
			this.loc = loc;
			this.place = place;
			this.timed = timed;
		}
		
		public Location getLocation() {
			return loc;
		}
		
		public int getPlace() {
			return place;
		}
		
		public int getTimed() {
			return timed;
		}
		
	}
		
	
	
	
	 
	
	
}
