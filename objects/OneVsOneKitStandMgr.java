/**
 * 
 */
package de.onevsone.objects;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.methods.KitMgr;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 30.03.2018 17:25:29					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class OneVsOneKitStandMgr {
	
	public void spawnKitStands() {
		removeAllKitStands();
		Main.ins.kitStands.clear();
		Main.ins.kitStandUUIDs.clear();
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Kitstands");
		
		if(cfg.getConfigurationSection("Kitstand") != null) {
			for(String name : cfg.getConfigurationSection("Kitstand").getKeys(false)) {
				spawnStand(name);
			}
		}
		fillAllKitStands();
		
	}
	
	public void spawnStand(String name) {
		YamlConfiguration cfg = Main.ins.utils.getYaml("Kitstands");
		
		double x = cfg.getDouble("Kitstand." + name + ".X");
		int y = cfg.getInt("Kitstand." + name + ".Y");
		double z = cfg.getDouble("Kitstand." + name + ".Z");
		
		float yaw = (float) cfg.getDouble("Kitstand." + name + ".Yaw");
		float pitch = (float) cfg.getDouble("Kitstand." + name + ".Pitch");
		
		String world = cfg.getString("Kitstand." + name + ".World");
		
		String kit = cfg.getString("Kitstand." + name + ".Kit");
		int subID = cfg.getInt("Kitstand." + name + ".SubID");
		
		String timedString = cfg.getString("Kitstand." + name + ".Timed");
		
		if(timedString == null) timedString = "";
		
		int timed = 1;
		
		
		if(timedString.equalsIgnoreCase("30d")) timed = 2;
		else if(timedString.equalsIgnoreCase("24h")) timed = 3;
		
		if(Bukkit.getWorld(world) != null) {
			Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			
			
			final ArmorStand stand = Bukkit.getWorld(world).spawn(loc, ArmorStand.class);
			for(Entity en : stand.getNearbyEntities(0.2, 0.35, 0.2)) 
				if(en.getType().equals(EntityType.ARMOR_STAND) && 
				   !stand.getUniqueId().equals(en.getUniqueId())) 
					
					en.remove();
				
			
			
			final  ArmorStand infoStand =  Bukkit.getWorld(world).spawn(loc.clone().add(0,0.27,0), ArmorStand.class);
			
			infoStand.setVisible(false);
			
			infoStand.setBasePlate(false);
			infoStand.setGravity(false);
			
			final int timedF = timed;
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					int value = 0;
					
					
					
					if(KitMgr.kitExists(Main.ins.database.getUUID(kit))) {
						UUID uuid = Main.ins.database.getUUID(kit);
						value = Main.ins.database.getKitStats(uuid.toString(), timedF, subID);
						
					} else if(Main.ins.database.getKitType(kit) == 2) {
						value = Main.ins.database.getKitStats(Main.ins.database.resolveCustomKit(kit), timedF, 1);						
					}
					final int show = value;
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							if(timedF == 2) {
								infoStand.setCustomName(show + "x letzte 30 Tage");
							} else if(timedF == 3) {
								infoStand.setCustomName(show + "x letzte 24 Stunden");
							} else {
								infoStand.setCustomName(show + "x Alltime");
							}
							
							infoStand.setCustomNameVisible(true);
							
						}
					}.runTask(Main.ins);
				}
			}.runTaskAsynchronously(Main.ins);
			
			
			stand.setArms(true);
			stand.setCustomName("/kit §l" + kit + ":" + subID);
			stand.setCustomNameVisible(true);
			
			KitStand standObj = new KitStand(stand, kit, subID, loc, name, timed);
			
			Main.ins.kitStands.put(stand.getUniqueId(), standObj);
			Main.ins.kitStandUUIDs.add(stand.getUniqueId());
		}
	}
	
	public void fillAllKitStands() {
		for(KitStand stand : Main.ins.kitStands.values()) 
			fillKitStand(stand.getStand().getUniqueId());
		
	}
	
	public void fillKitStand(UUID uuid) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				KitStand ks = Main.ins.kitStands.get(uuid);
				
				if(ks == null || ks.getStand() == null) return;
				
				String inv = KitMgr.getRawKitData(ks.getKit(), false, ks.getSubID());
				String armor = KitMgr.getRawKitData(ks.getKit(), true, ks.getSubID());
				
				try {
					
					if(inv == null || inv.equalsIgnoreCase("")) {
//						ks.getStand().remove();
						return; 
					}
					
					ItemStack contentsInv = KitMgr.fromBase64(inv).getItem(0);
					ItemStack[] contentsArmor = KitMgr.fromBase64Armor(armor);
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							ks.getStand().setItemInHand(contentsInv);
							ks.getStand().setHelmet(contentsArmor[0]);
							ks.getStand().setChestplate(contentsArmor[1]);
							ks.getStand().setLeggings(contentsArmor[2]);
							ks.getStand().setBoots(contentsArmor[3]);
						}
					}.runTask(Main.ins);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}	
				
			}
		}.runTaskAsynchronously(Main.ins);
		
		
	}
	
	public void removeAllKitStands() {
		for(KitStand ks : Main.ins.kitStands.values()) 
			ks.getStand().remove();
		Main.ins.kitStands.clear();
		Main.ins.kitStandUUIDs.clear();
	}
	
	public void autoRespawn() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				removeAllKitStands();
				spawnKitStands();
			}
		}.runTaskTimer(Main.ins, 20*60, 20*60);
	}
	
	public void autoRefill() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				fillAllKitStands();
			}
		}.runTaskTimer(Main.ins, 20*10, 20*10);
	}
	
	
	
	
}
