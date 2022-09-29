/**
 * 
 */
package de.onevsone.methods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.onevsone.arenas.builder.worldReset.WorldResetMgr;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 17.04.2019 20:11:23					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class LayoutMgr {

	@SuppressWarnings("static-access")
	public static ArrayList<String> getAllLayouts() {
		ArrayList<String> layouts = new ArrayList<>();
		
		File f = new File("plugins/1vs1/Layouts.yml");
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
		
		for(String str : cfg.getConfigurationSection("Layout").getKeys(false)) {
			layouts.add(str);
		}
		
		
		return layouts;
	}
	
	@SuppressWarnings("static-access")
	public static boolean layoutExists(String name) {
		
		File f = new File("plugins/1vs1/Layouts.yml");
		if(!f.exists()) return false;
		
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
		
		return cfg.getConfigurationSection("Layout." + name) != null;
		
	}
	
	@SuppressWarnings("static-access")
	public static boolean isWorldLayout(String name) {
		
		if(!layoutExists(name)) return false;
		File f = new File("plugins/1vs1/Layouts.yml");
		
		if(!f.exists()) return false;
		
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
		
		return cfg.getBoolean("Layout." + name + ".worldLayout");
	}
	
	@SuppressWarnings("static-access")
	public static boolean removeLayout(String name) {
		if(!layoutExists(name)) return false;
		
		File f = new File("plugins/1vs1/Layouts.yml");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.err.println("§cDie Datei \"plugins/1vs1/Layouts.yml\" konnte nicht erstellt werden!");
				e.printStackTrace();
				return false;
			}
		}
		
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
		
		cfg.set("Layout." + name, null);
		
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	@SuppressWarnings("static-access")
	public static boolean addLayout(String name, boolean worldLayout, Location pos1, Location pos2, Player creator) {
		
		if(layoutExists(name)) return false;
		
		File f = new File("plugins/1vs1/Layouts.yml");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.err.println("§cDie Datei \"plugins/1vs1/Layouts.yml\" konnte nicht erstellt werden!");
				e.printStackTrace();
				return false;
			}
		}
		
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
		
		int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
		int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
		int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
		
		int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
		int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
		int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		
		String world = pos1.getWorld().getName();
		
		cfg.set("Layout." + name + ".maxX", maxX);
		cfg.set("Layout." + name + ".maxY", maxY);
		cfg.set("Layout." + name + ".maxZ", maxZ);
		
		cfg.set("Layout." + name + ".minX", minX);
		cfg.set("Layout." + name + ".minY", minY);
		cfg.set("Layout." + name + ".minZ", minZ);
		
		cfg.set("Layout." + name + ".world", world);
		
		cfg.set("Layout." + name + ".worldLayout", worldLayout);
		
		Location min = new Location(Bukkit.getWorld(world), minX, minY, minZ);
		Location max = new Location(Bukkit.getWorld(world), maxX, maxY, maxZ);
		
		
		if(worldLayout) {
			WorldResetMgr.createNewWorldLayout(name, min, max, creator);
		}
		
		
		try {
			cfg.save(f);
		} catch (IOException e) {
			System.err.println("§cDie Datei \"plugins/1vs1/Layouts.yml\" konnte nicht gespeichert werden!");
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	@SuppressWarnings("static-access")
	public static Location getMinPos(String name) {
		if(!layoutExists(name)) return null;
		
		
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(new File("plugins/1vs1/Layouts.yml"));
		
		int minX = cfg.getInt("Layout." + name + ".minX");
		int minY = cfg.getInt("Layout." + name + ".minY");
		int minZ = cfg.getInt("Layout." + name + ".minZ");
		
		String world = cfg.getString("Layout." + name + ".world");
		
		if(world == null || Bukkit.getWorld(world) == null) return null;
		
		return new Location(Bukkit.getWorld(world), minX, minY, minZ);
	}
	
	@SuppressWarnings("static-access")
	public static Location getMaxPos(String name) {
		if(!layoutExists(name)) return null;
		
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(new File("plugins/1vs1/Layouts.yml"));
		
		int maxX = cfg.getInt("Layout." + name + ".maxX");
		int maxY = cfg.getInt("Layout." + name + ".maxY");
		int maxZ = cfg.getInt("Layout." + name + ".maxZ");
		
		String world = cfg.getString("Layout." + name + ".world");
		
		if(world == null || Bukkit.getWorld(world) == null) return null;
		
		return new Location(Bukkit.getWorld(world), maxX, maxY, maxZ);
	}
	
	
	/**
	 * 
	 * @param name Name of Worldlayout
	 * @return 0 = Ok! 1 = Layout not found 2 = not World Layout 3 = World not ready 4 = Pos1 not set 5 = Pos2 not set 6 = Middle not set
	 */
	public static int isWorldLayoutReady(String name) {
		if(layoutExists(name)) {
			if(isWorldLayout(name)) {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File("1vs1Worlds/Presets/" + name + "/data.yml"));
				
				if(cfg.getBoolean("Data.finishedBuilding")) {
					if(cfg.getConfigurationSection("Data.Pos1") != null) {
						if(cfg.getConfigurationSection("Data.Pos2") != null) {
							if(cfg.getConfigurationSection("Data.Middle") != null) {
								return 0;
							} else {
								return 6;
							}
						} else {
							return 5;
						}
					} else {
						return 4;
					}
				} else {
					return 3;
				}
			} else {
				return 2;
			}
		} else {
			return 1;
		}
	}
	
}
