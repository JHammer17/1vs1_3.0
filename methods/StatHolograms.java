/**
 * 
 */
package de.onevsone.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import de.onevsone.Main;
import de.onevsone.objects.OneVsOnePlayer;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 15.08.2019 17:05:09					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class StatHolograms {

	static BukkitRunnable rb;
	
	public static void spawnHologram(OneVsOnePlayer p) {
		
		if(p == null) return;
		if(Bukkit.getPlayer(p.getUUID()) == null) return;
		
		
		Player player = Bukkit.getPlayer(p.getUUID());
		
		if(!p.isIn1vs1()) return;
		
		Location loc = Main.ins.utils.getHoloSpawn();
		
		String title = "§cERROR";
		
		
		if(p.getHoloStatsType() == 1) {
			title = "§a§lAlltime Statistiken:";
		} else if(p.getHoloStatsType() == 2) {
			title = "§a§l30 Tage Statistiken:";
		} else if(p.getHoloStatsType() == 3) {
			title = "§a§l24h Statistiken:";
		}
		
		createArmorStand(player, loc.clone().add(0,3.9,0), title);
		
//		createArmorStand(player, loc.clone().add(0,3.3,0), "§7");
		
		
		
		createArmorStand(player, loc.clone().add(0,3.3,0), "§fSpiele: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 1));
		createArmorStand(player, loc.clone().add(0,3.0,0), "§fdavon gewonnen: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 2));
		createArmorStand(player, loc.clone().add(0,2.7,0), "§fEZ gewonnen: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 4));
		createArmorStand(player, loc.clone().add(0,2.4,0), "§fKnapp gewonnen: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 5));
		createArmorStand(player, loc.clone().add(0,2.1,0), "§fVerloren: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 3));
		createArmorStand(player, loc.clone().add(0,1.8,0), "§fW/L: §6" + Main.ins.database.getStats(player.getUniqueId(), p.getHoloStatsType(), 8));

		createArmorStand(player, loc.clone().add(0,1.2,0), "§fKills: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 6));
		createArmorStand(player, loc.clone().add(0,0.9,0), "§fTode: §6" + Main.ins.database.getStatsAsInt(player.getUniqueId(), p.getHoloStatsType(), 7));
		createArmorStand(player, loc.clone().add(0,0.6,0), "§fK/D: §6" + Main.ins.database.getStats(player.getUniqueId(), p.getHoloStatsType(), 9));
		
			
		createArmorStand(player, loc.clone(), "§fPosition im Ranking: §6" + Main.ins.database.getPosition(player.getUniqueId(), p.getHoloStatsType()));
		
		
		
		//Statistiken von %Spieler%
		
		//Won
		//Spiele
		//Platz
		//KD
		
		//Won 30 Tage
		//Spiele 30 Tage
		//Platz 30 Tage
		//KD 30 Tage
		
		//Rangpunkte
		
	}
	
	public static void spawnHologramForAllPlayers() {
		for(OneVsOnePlayer players : Main.ins.getOneVsOnePlayersCopy().values()) {
			spawnHologram(players);
		}
	}
	
	public static void despawnHologram(OneVsOnePlayer p) {
		
		
		if(Bukkit.getPlayer(p.getUUID()) == null) {
			return;
		}
		
		Player player = Bukkit.getPlayer(p.getUUID());
		
		for(int id : p.getHologramIds()) {
			
			try {
				
				int[] idlist = {id};
				
				sendPacket(player, getNMSClass("PacketPlayOutEntityDestroy").
						getConstructor(int[].class).
						newInstance(idlist));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}
	
	public static void despawnHologramForAllPlayers() {
		for(OneVsOnePlayer players : Main.ins.getOneVsOnePlayersCopy().values()) {
			despawnHologram(players);
		}
	}
	
	public static void respawnHologram(OneVsOnePlayer p) {
		despawnHologram(p);
		spawnHologram(p);
	}
	
	public static void respawnHologramAllPlayers() {
		despawnHologramForAllPlayers();
		spawnHologramForAllPlayers();
	}
	
	
	private static void createArmorStand(Player p, Location loc, String display) {
		
		if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
			
			
			try {
				
				

				Object worldServer = loc.getWorld().getClass().getMethod("getHandle").invoke(loc.getWorld());
				
				
				Class<?> c = worldServer.getClass().getClass().cast(getNMSClass("World"));
				
				
				Constructor<?> enArmorStand = getNMSClass("EntityArmorStand").
						getConstructor(c);
				
				
				
				
				Object aS = enArmorStand.newInstance(worldServer);
				
				
				Method setLoc = aS.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
				Method setCustomName = aS.getClass().getMethod("setCustomName", String.class);
				Method setCustomNameVisible = aS.getClass().getMethod("setCustomNameVisible", boolean.class);
				Method setGravity = aS.getClass().getMethod("setGravity", boolean.class);
				Method setSmall = aS.getClass().getMethod("setSmall", boolean.class);
				Method setInvisible = aS.getClass().getMethod("setInvisible", boolean.class);
				
				setLoc.invoke(aS, loc.getX(), loc.getY(), loc.getZ(), 0f, 0f);
				setCustomName.invoke(aS, display);
				setCustomNameVisible.invoke(aS, true);
				setGravity.invoke(aS, true);
				setSmall.invoke(aS, true);
				setInvisible.invoke(aS, true);
				
				
				Constructor<?> finalPacketConstructor = getNMSClass("PacketPlayOutSpawnEntityLiving").
						getConstructor(getNMSClass("EntityLiving"));
				
				
				
				Method getID = aS.getClass().getMethod("getId");
				
				int id = (int) getID.invoke(aS);
				
	        
		        sendPacket(p, finalPacketConstructor.newInstance(aS));
		        
				Main.ins.getOneVsOnePlayer(p).addHologramId(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
	public static void startAutoRespawner() {
		rb = new BukkitRunnable() {
			
			@Override
			public void run() {
				respawnHologramAllPlayers();
				
			}
		};
		rb.runTaskTimerAsynchronously(Main.ins, 0, 20*30);
		
	}
	
	public static void stopAutoRespawner() {
		if(rb != null) rb.cancel();
	}
	
	private static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
}
