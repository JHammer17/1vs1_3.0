package de.onevsone.listener.statsmgr;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.objects.PlayerStatsData;

public class SignMgr implements Listener {
	
	
	
	
	@EventHandler
	public void onSign(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[1vs1Top]")) {
			try {
				int place = Integer.parseInt(e.getLine(1));
				Player p = e.getPlayer();
				
				YamlConfiguration cfg = Main.ins.utils.getYaml("Signs");
				
				Location loc = e.getBlock().getLocation();
				
				cfg.set("Top.Signs.Top." + place + ".X", loc.getBlockX());
				cfg.set("Top.Signs.Top." + place + ".Y", loc.getBlockY());
				cfg.set("Top.Signs.Top." + place + ".Z", loc.getBlockZ());
				cfg.set("Top.Signs.Top." + place + ".World", loc.getWorld().getName());
				
				
				cfg.save(Main.ins.utils.getPluginFile("Signs"));
				
				e.setLine(0, "-----");
				e.setLine(1, "Loading...");
				e.setLine(2, "");
				e.setLine(3, "-----");
				
				e.getBlock().getState().update();
				
				p.sendMessage(Main.ins.prefixGreen + "Schild registriert!");
			} catch (NumberFormatException e1) {
				e.getPlayer().sendMessage(Main.ins.prefixRed + "§cDu musst eine Zahl als Platz angeben! (Maximal " + Main.ins.topPlaces + ")");
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				e.getPlayer().sendMessage(Main.ins.prefixRed + "§cFehler beim Speichern der Datei!");
			}
			
		} else if(e.getLine(0).equalsIgnoreCase("[1vs1Top30D]")) {
			try {
				int place = Integer.parseInt(e.getLine(1));
				Player p = e.getPlayer();
				
				YamlConfiguration cfg = Main.ins.utils.getYaml("Signs");
				
				Location loc = e.getBlock().getLocation();
				
				cfg.set("Top.Signs.Top30d." + place + ".X", loc.getBlockX());
				cfg.set("Top.Signs.Top30d." + place + ".Y", loc.getBlockY());
				cfg.set("Top.Signs.Top30d." + place + ".Z", loc.getBlockZ());
				cfg.set("Top.Signs.Top30d." + place + ".World", loc.getWorld().getName());
				
				
				cfg.save(Main.ins.utils.getPluginFile("Signs"));
				
				e.setLine(0, "-----");
				e.setLine(1, "Loading...");
				e.setLine(2, "");
				e.setLine(3, "-----");
				
				e.getBlock().getState().update();
				
				p.sendMessage(Main.ins.prefixGreen + "Schild registriert!");
			} catch (NumberFormatException e1) {
				e.getPlayer().sendMessage(Main.ins.prefixRed + "§cDu musst eine Zahl als Platz angeben! (Maximal " + Main.ins.topPlaces + ")");
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				e.getPlayer().sendMessage(Main.ins.prefixRed + "§cFehler beim Speichern der Datei!");
			}
			
		} else if(e.getLine(0).equalsIgnoreCase("[1vs1Top24h]")) {
			try {
				int place = Integer.parseInt(e.getLine(1));
				Player p = e.getPlayer();
				
				YamlConfiguration cfg = Main.ins.utils.getYaml("Signs");
				
				Location loc = e.getBlock().getLocation();
				
				cfg.set("Top.Signs.Top24h." + place + ".X", loc.getBlockX());
				cfg.set("Top.Signs.Top24h." + place + ".Y", loc.getBlockY());
				cfg.set("Top.Signs.Top24h." + place + ".Z", loc.getBlockZ());
				cfg.set("Top.Signs.Top24h." + place + ".World", loc.getWorld().getName());
				
				
				cfg.save(Main.ins.utils.getPluginFile("Signs"));
				
				e.setLine(0, "-----");
				e.setLine(1, "Loading...");
				e.setLine(2, "");
				e.setLine(3, "-----");
				
				e.getBlock().getState().update();
				
				p.sendMessage(Main.ins.prefixGreen + "Schild registriert!");
			} catch (NumberFormatException e1) {
				e.getPlayer().sendMessage(Main.ins.prefixRed + "§cDu musst eine Zahl als Platz angeben! (Maximal " + Main.ins.topPlaces + ")");
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				e.getPlayer().sendMessage(Main.ins.prefixRed + "§cFehler beim Speichern der Datei!");
			}
			
		}
	}
	
	private static HashMap<Integer, SignData> signDatas = new HashMap<>();
	
	
	
	public static void reloadSigns() {
		
		signDatas.clear();
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Signs");
		
			
		if(cfg.getConfigurationSection("Top.Signs.Top") != null ) {
			
			for(String str : cfg.getConfigurationSection("Top.Signs.Top").getKeys(false)) {
				
				try {
				 	
					int place = Integer.parseInt(str);
					
					int x = cfg.getInt("Top.Signs.Top." + str + ".X");
					int y = cfg.getInt("Top.Signs.Top." + str + ".Y");
					int z = cfg.getInt("Top.Signs.Top." + str + ".Z");
					
					String world = cfg.getString("Top.Signs.Top." + str + ".World");
					
					if(world == null) continue;
					
					SignData data = new SignData(x, y, z, world, place, 0);
					
					signDatas.put(signDatas.size()+1, data);
					
				} catch (NumberFormatException e) {}
 			}
		}
		
		if(cfg.getConfigurationSection("Top.Signs.Top30d") != null ) {
			for(String str : cfg.getConfigurationSection("Top.Signs.Top30d").getKeys(false)) {
				try {
				 	
					int place = Integer.parseInt(str);
					
					int x = cfg.getInt("Top.Signs.Top30d." + str + ".X");
					int y = cfg.getInt("Top.Signs.Top30d." + str + ".Y");
					int z = cfg.getInt("Top.Signs.Top30d." + str + ".Z");
					
					String world = cfg.getString("Top.Signs.Top30d." + str + ".World");
					
					if(world == null) continue;
					
					SignData data = new SignData(x, y, z, world, place, 1);
					
					signDatas.put(signDatas.size()+1, data);
					
					
				} catch (NumberFormatException e) {}
 			}
		}
		
		if(cfg.getConfigurationSection("Top.Signs.Top24h") != null ) {
			for(String str : cfg.getConfigurationSection("Top.Signs.Top24h").getKeys(false)) {
				try {
				 	
					int place = Integer.parseInt(str);
					
					int x = cfg.getInt("Top.Signs.Top24h." + str + ".X");
					int y = cfg.getInt("Top.Signs.Top24h." + str + ".Y");
					int z = cfg.getInt("Top.Signs.Top24h." + str + ".Z");
					
					String world = cfg.getString("Top.Signs.Top24h." + str + ".World");
					
					if(world == null) continue;
					
					SignData data = new SignData(x, y, z, world, place, 2);
					
					signDatas.put(signDatas.size()+1, data);
					
					
				} catch (NumberFormatException e) {}
 			}
		}
		
		refreshSigns();
		
	}
	
	
	
	
	public static void refreshSigns() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				HashMap<Integer, PlayerStatsData> stats = Main.ins.database.getTop(1);
				HashMap<Integer, PlayerStatsData> stats30 = Main.ins.database.getTop(2);
				HashMap<Integer, PlayerStatsData> stats24 = Main.ins.database.getTop(3);
				
				
				
				
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						for(int signnumber : signDatas.keySet()) {
							
								
								
								try {
									int place = signDatas.get(signnumber).getPlace();
									if(signDatas.get(signnumber).getLocation().getChunk().isLoaded()) {
										
										
										Sign s = (Sign) signDatas.get(signnumber).getLocation().getBlock().getState();
										
										s.setLine(0, "#" + place);
										
										
										String name = "-";
										String wins = "";
										String fights = "";
										
										
										
										if(signDatas.get(signnumber).isAllTime()) {
											if(stats.containsKey(place)) {
												name = stats.get(place).getName();
												wins = "Wins: "+stats.get(place).getWins();
												fights = "Fights: "+stats.get(place).getFights();
											}
										} else if(signDatas.get(signnumber).isTop30d()) {
											if(stats30.containsKey(place)) {
												name = stats30.get(place).getName();
												wins = "Wins: "+stats30.get(place).getWins30();
												fights = "Fights: "+stats30.get(place).getFights30();
											}
										} else if(signDatas.get(signnumber).isTop24h()) {
											if(stats24.containsKey(place)) {
												name = stats24.get(place).getName();
												wins = "Wins: "+stats24.get(place).getWins24();
												fights = "Fights: "+stats24.get(place).getFights24();
											}
										}
										
										s.setLine(1, name);
										s.setLine(2, wins);
										s.setLine(3, fights);
										
											
										s.update();
									}
								} catch (NullPointerException | ClassCastException e) {}
							
							
							
						}
						
						
					}
				}.runTask(Main.ins);
			}
		}.runTaskAsynchronously(Main.ins);		
	}
	
	
	public static class SignData {
		
		private int x;
		private int y;
		private int z;
		private String world;
		private int place;
		private int timed;

		public SignData(int x, int y, int z, String world, int place, int timed) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.world = world;
			this.place = place;
			this.timed = timed;
		}

		/**
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * @return the y
		 */
		public int getY() {
			return y;
		}

		/**
		 * @return the z
		 */
		public int getZ() {
			return z;
		}

		/**
		 * @return the world
		 */
		public String getWorld() {
			return world;
		}

		/**
		 * @return the place
		 */
		public int getPlace() {
			return place;
		}
		
		public Location getLocation() {
			
			if(world == null || Bukkit.getWorld(world) == null) return null; 
			
			return new Location(Bukkit.getWorld(world), x, y, z);
		}
		
		public boolean isAllTime() {
			return this.timed == 0;
		}
		
		public boolean isTop30d() {
			return this.timed == 1;
		}
		
		public boolean isTop24h() {
			return this.timed == 2;
		}
		
	}
	
	
}
