/**
 * 
 */
package de.onevsone.arenas.builder.worldReset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.Files;

import de.onevsone.Main;
import de.onevsone.arenas.builder.BlockMapReset;
import de.onevsone.objects.OneVsOneArena;

/**
 * ###################################################### # @author JHammer17 #
 * # Erstellt am 20.04.2019 17:44:29 # # # # Alle Ihhalte dieser Klasse dürfen #
 * # frei verwendet oder verbreitet werden. # # Es wird keine Zustimmung von
 * JHammer17 benötigt. # # #
 * ######################################################
 */
public class WorldResetMgr {

	public WorldResetMgr() {
			startAutoQueue();
	}
	
	public static class DelayedReset {
		
		private String worldName;
		private String presetLocation;
		private OneVsOneArena arena;

		public DelayedReset(String worldName, String presetLocation,  OneVsOneArena arena) {
			this.worldName = worldName;
			this.presetLocation = presetLocation;
			this.arena = arena;
		}

		public String getWorldname() {
			return worldName;
		}

		public String getPresetLocation() {
			return presetLocation;
		}

		public OneVsOneArena getArena() {
			return arena;
		}
		
		
		
	}
	
	public void startAutoQueue() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!currentLoading && !waiting.isEmpty()) {
					DelayedReset reset =  waiting.get(0);
					
					if(reset.getArena() != null && reset.getArena().isOk()) {
						try {
							startReset(reset.getWorldname(), reset.getPresetLocation(), reset.getArena());
							currentLoading = true;
							waiting.remove(reset);
//							Bukkit.broadcastMessage("§aZurücksetzen: " + reset.getArena().getName() + " §eWartend: " + waiting.size());
							
						} catch (Exception e) {
							currentLoading = false;
//							Bukkit.broadcastMessage("§cFehler beim zurücksetzen");
						}
						
						return;
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 20);
		
	}
	
	
	
	private static ArrayList<DelayedReset> waiting = new ArrayList<>();
	private static boolean currentLoading = false;
	
	
	public static boolean isLoading() {
		return currentLoading;
	}
	
	public static int getWaiting() {
		return waiting.size();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<DelayedReset> getResetList() {
		return (ArrayList<DelayedReset>) waiting.clone();
	}
 	
	public static void startReset(World w, String presetLocation, OneVsOneArena arena) {
		startReset(w.getName(), presetLocation, arena);
	}

	
	
	
	public static void unloadWorldArena(String worldName) {
		if(worldName == null) return;
		if(Main.ins.isInOneVsOneArenas(worldName) && Main.ins.getOneVsOneArena(worldName) != null) {
			if(Main.ins.getOneVsOneArena(worldName).isWorldArena()) {
				if (Bukkit.getWorld(worldName) != null && 
						Bukkit.getWorld(worldName).getPlayers().size() > 0) {
			
					for (Player players : Bukkit.getWorld(worldName).getPlayers()) {
						if (Main.ins.isInOneVsOnePlayers(players.getUniqueId()))
							Main.ins.utils.tpToLobby(players);
						else
							players.teleport(Main.ins.utils.getExitSpawn());

					}
				}
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						Bukkit.unloadWorld("1vs1Worlds/Arenas/" + worldName, true);
						
						deleteAllFiles("1vs1Worlds/Arenas/" + worldName);
						
					}
				}.runTask(Main.ins);
				
				
			}
		}
	}
	
	
	
	public static void startReset(String worldName, String presetLocation, OneVsOneArena arena) {
		
		if(currentLoading) {
			waiting.add(new DelayedReset(worldName, presetLocation, arena));
			return;
		}
		currentLoading = true;
		
		
		
		if (worldName == null || 
			worldName.equalsIgnoreCase("null") || 
			presetLocation == null || 
			arena == null) {
			currentLoading = false;
			return;
		}

		new BukkitRunnable() {

			@Override
			public void run() {
		
				if (Bukkit.getWorld(worldName) != null && 
						Bukkit.getWorld(worldName).getPlayers().size() > 0) {
			
					for (Player players : Bukkit.getWorld(worldName).getPlayers()) {
						if (Main.ins.isInOneVsOnePlayers(players.getUniqueId()))
							Main.ins.utils.tpToLobby(players);
						else
							players.teleport(Main.ins.utils.getExitSpawn());

					}
				}

				reset(worldName, presetLocation, arena, 1);
			}
		}.runTask(Main.ins);
	}
	
	
	private static void reset(String worldName, String presetLocation, OneVsOneArena arena, int chance) {
		
		
		
		
		
		Bukkit.unloadWorld(worldName, false);

		new BukkitRunnable() {

			@Override
			public void run() {
				
				
				deleteAllFiles(worldName);
				copyWorldFolder(new File(presetLocation), new File(worldName));
				
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						
						World w = AsyncWorldLoader.createAsyncWorld(new WorldCreator(worldName));
						
						
						if(Main.ins.mv != null && Main.ins.mv.getMVWorldManager().isMVWorld(worldName)) {
							Main.ins.mv.getMVWorldManager().getMVWorld(worldName).setAutoLoad(false);
							Main.ins.mv.getMVWorldManager().getMVWorld(worldName).setKeepSpawnInMemory(false);
							Main.ins.mv.getMVWorldManager().getMVWorld(worldName).setHidden(true);
						}
						
						new BukkitRunnable() {

							@Override
							public void run() {
								if(w == null || !w.getName().equalsIgnoreCase(worldName)) {
									int chanceC = chance;
									chanceC += 1;
									
									if(chanceC >= 5) {
										Bukkit.getConsoleSender().sendMessage("§cWorld " + worldName + " couldn't be reseted!  Arena broken!");
										currentLoading = false;
										return;
									}
									
									reset(worldName, presetLocation, arena, chanceC+=1);
									
									return;
								}
								
								
								if(!Main.ins.mv.getMVWorldManager().isMVWorld(worldName)) {
									Main.ins.mv.getMVWorldManager().addWorld(worldName, Environment.NORMAL, worldName, WorldType.NORMAL, false, null);
								}
								
								if(Main.ins.mv != null && Main.ins.mv.getMVWorldManager().isMVWorld(worldName)) {
									Main.ins.mv.getMVWorldManager().getMVWorld(worldName).setAutoLoad(false);
									Main.ins.mv.getMVWorldManager().getMVWorld(worldName).setKeepSpawnInMemory(false);
									Main.ins.mv.getMVWorldManager().getMVWorld(worldName).setHidden(true);
								}
								
								
								
								w.setGameRuleValue("doDaylightCycle", "false");
								w.setGameRuleValue("doMobSpawning", "false");
								w.setGameRuleValue("doWeatherCycle", "false");
								w.setGameRuleValue("spectatorsGenerateChunks", "false");
								w.setTime(6000);

								for (Entity e : w.getEntities()) 
									if (!(e instanceof Player)) e.remove();

								
								
								arena.finishWorldReset();
								
								currentLoading = false;
								
							}
						}.runTask(Main.ins);
					}
				}.runTaskLaterAsynchronously(Main.ins, 5);

			}
		}.runTaskAsynchronously(Main.ins);
	}
	

	public static void createNewWorldLayout(String name, Location min, Location max, Player creator) {
		if (Bukkit.getWorld("1vs1Worlds/Presets/" + name) != null) {
			for (Player players : Bukkit.getWorld("1vs1Worlds/Presets/" + name).getPlayers()) {
				if (Main.ins.isInOneVsOnePlayers(players.getUniqueId()))
					Main.ins.utils.tpToLobby(players);
				else
					players.teleport(Main.ins.utils.getExitSpawn());

			}
			Bukkit.unloadWorld("1vs1Worlds/Presets/" + name, false);
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				deleteAllFiles("1vs1Worlds/Presets/" + name);

				World w = AsyncWorldLoader
						.createAsyncWorld(new WorldCreator("1vs1Worlds/Presets/" + name).type(WorldType.FLAT));

				new BukkitRunnable() {

					@SuppressWarnings("static-access")
					@Override
					public void run() {
						File f = new File("1vs1Worlds/Presets/" + name + "/data.yml");
						if (!f.exists()) {
							try {
								f.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);

						cfg.set("Data.LayoutName", name);

						try {
							cfg.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}

						w.setGameRuleValue("doDaylightCycle", "false");
						w.setGameRuleValue("doMobSpawning", "false");
						w.setGameRuleValue("doWeatherCycle", "false");
						w.setGameRuleValue("spectatorsGenerateChunks", "false");
						w.setTime(6000);

						for (Entity e : w.getEntities())
							if (!(e instanceof Player))
								e.remove();

						new BlockMapReset(min, max, new Location(w, 0, 4, 0), "1vs1Worlds/Presets/" + name, 1000, true, creator)
								.copy();

						

					}
				}.runTask(Main.ins);

			}
		}.runTask(Main.ins);
	}

	public static void finishLayoutCreation(String name, Player creator) {
		 for(Player players : Bukkit.getWorld(name).getPlayers()) {
			 if(Main.ins.isInOneVsOnePlayers(players.getUniqueId()))
				 Main.ins.utils.tpToLobby(players);
			 else
				 players.teleport(Main.ins.utils.getExitSpawn());
		 }
		 
		 Bukkit.unloadWorld(name, true);
		 
		 
		 if(creator != null && Bukkit.getPlayer(creator.getUniqueId()) != null) {
			 creator.sendMessage(Main.ins.prefixGreen + "§7Das Layout §6" + name.replaceFirst("1vs1Worlds/Presets/", "") + " §7ist nun §abereit!");
		 } else {
			 for(Player players : Bukkit.getOnlinePlayers()) {
					if(players.hasPermission("1vs1.layoutinfo")) {
						players.sendMessage(Main.ins.prefixGreen + "§7Das Layout §6" + name.replaceFirst("1vs1Worlds/Presets/", "") + " §7ist nun §abereit!");
						 
					}
				}
		 }
		 
	}

	private static void deleteAllFiles(String path) {
		if (!new File(path).exists() && !new File(path).isDirectory())
			return;

		for (File file : new File(path).listFiles()) {
			if (file.isDirectory())
				deleteAllFiles(file.getPath());
			file.delete();
		}

		new File(path).delete();
	}

	private static void copyWorldFolder(File from, File to) {
		try {
			ArrayList<String> ignore = new ArrayList<String>();
			ignore.add("session.dat");
			ignore.add("uid.dat");
			ignore.add("session.lock");
			ignore.add("data.yml");
			if (!ignore.contains(from.getName())) {
				if (from.isDirectory()) {
					if (!to.exists()) {
						to.mkdirs();
					}
					String[] files = from.list();
					for (String file : files) {
						File srcFile = new File(from, file);
						File destFile = new File(to, file);
						copyWorldFolder(srcFile, destFile);
					}
				} else {
					Files.copy(from, to);
				}
			}
		} catch (FileNotFoundException e) {
			Bukkit.broadcastMessage("§4File Fehler! " + from + " " + to);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
