package de.onevsone.methods.entities;


import de.onevsone.Main;
import de.onevsone.listener.Inventories.subInvs.QueueInv;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class QueueZombie implements Listener {

	
	public static Location queueLoc;
	static UUID queueUUID;

	static Zombie zombie;
	
	static String oldNameQueque;
	static String nameQueue = "§c§lWarteschlange";
	
	
	
	
	public static void spawnQuequeZombie() {
		
		if(Main.ins.utils.getQueueSpawn() == null) return;
		
		if(Main.ins.utils.getQueueSpawn().getWorld().getDifficulty() == Difficulty.PEACEFUL) {
			Bukkit.getConsoleSender().sendMessage("§cAchtung! Die Welt " + Main.ins.utils.getQueueSpawn().getWorld().getName() + " ist im Peaceful Modus! Der Blackdealer und der Warteschlangen Zombie des 1vs1-Main.inss kann nicht Spawnwen!");
			if(Main.ins.msgMeWhenIStupid) {
				for(Player players : Bukkit.getOnlinePlayers()) {
					players.sendMessage(Main.ins.prefixRed + "§cAchtung! Die Welt " + Main.ins.utils.getQueueSpawn().getWorld().getName() + " ist im Peaceful Modus! Der Blackdealer und der Warteschlangen Zombie des 1vs1-Plugins kann nicht Spawnwen!");
				}
			}
			
			return;
		}
		
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(!Main.ins.utils.getQueueSpawn().getChunk().isLoaded()) {
					Bukkit.broadcastMessage("Chunk nicht geladen!");
					return;
				}
				
				despawnQuequeZombie();
				
				zombie = (Zombie) Main.ins.utils.getQueueSpawn().getWorld().spawnEntity(Main.ins.utils.getQueueSpawn(), EntityType.ZOMBIE);
				zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE,100, false, false));
				
				
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							if(Main.ins.silentQueue) {
								if(Main.ins.getServerVersion().toLowerCase().contains("1.8") || Main.ins.getServerVersion().toLowerCase().contains("1_8")) {
									Object handle = zombie.getClass().getMethod("getHandle").invoke(zombie);
									handle.getClass().getMethod("b", boolean.class).invoke(handle, true);
								} else if(Main.ins.getServerVersion().toLowerCase().contains("1.9") || Main.ins.getServerVersion().toLowerCase().contains("1_9")) {
									Object handle = zombie.getClass().getMethod("getHandle").invoke(zombie);
									handle.getClass().getMethod("c", boolean.class).invoke(handle, true);
								} else {
									zombie.getClass().getMethod("setSilent", boolean.class).invoke(zombie, true);
								}
							}
							
							
						} catch (Exception e1) {
							e1.printStackTrace();
						} 
						
						if(zombie.isInsideVehicle()) {
							zombie.getVehicle().remove();
							zombie.remove();
							despawnQuequeZombie();
							spawnQuequeZombie();
							return;
						}
						
						if (zombie.isBaby()) {
							for(Entity en : zombie.getNearbyEntities(1.5, 1.5, 1.5)) {
								if(en.getType() == EntityType.CHICKEN) {
									en.remove();
								}
							}
							zombie.remove();
							
							despawnQuequeZombie();
							spawnQuequeZombie();
							return;
						}

						if(Main.ins.getServerVersion().equalsIgnoreCase("1.8")) zombie.setVillager(false);
						
						zombie.setRemoveWhenFarAway(false);
						zombie.setMaxHealth(1);
						zombie.setHealth(zombie.getMaxHealth());
						
						zombie.setCanPickupItems(false);
						zombie.setCustomName(nameQueue);
						zombie.setCustomNameVisible(true);

						ItemStack Helm = new ItemStack(Material.SKULL_ITEM);
						Helm.setDurability((short) 2);
						ItemStack Brust = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
						ItemStack Hose = new ItemStack(Material.CHAINMAIL_LEGGINGS);
						ItemStack Schuhe = new ItemStack(Material.CHAINMAIL_BOOTS);

						ItemStack Schwert = new ItemStack(Material.STONE_SWORD);

						zombie.getEquipment().setHelmet(Helm);
						zombie.getEquipment().setChestplate(Brust);
						zombie.getEquipment().setLeggings(Hose);
						zombie.getEquipment().setBoots(Schuhe);

						zombie.getEquipment().setItemInHand(Schwert);

								
						oldNameQueque = zombie.getCustomName();

						queueUUID = zombie.getUniqueId();
						queueLoc = Main.ins.utils.getQueueSpawn();
						
					}
				}.runTaskAsynchronously(Main.ins);
				
			}
		}.runTask(Main.ins);

		
		
		
		
	}
	
	

	public static Class<?> getNMSClass(String name) {
	
	 String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	 try {
      return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
	 } catch (Exception e) {}
	  return null;
	}
	
	public static void respawnZombie() {
		new BukkitRunnable() {
			

			public void run() {
				despawnQuequeZombie();
				spawnQuequeZombie();
				
			}
		}.runTask(Main.ins);
		
		

	}

	public static void despawnQuequeZombie() {
		
		if(Main.ins.utils.getQueueSpawn() != null)
		for(Entity en : Main.ins.utils.getQueueSpawn().getWorld().getEntities()) {
			if(en != null && 
					en.getType() == EntityType.ZOMBIE && 
					en.getCustomName( )!= null && 
					en.getCustomName().equalsIgnoreCase(nameQueue)) {
				
				en.remove();
			}
			
		}
		if(zombie != null) zombie.remove();
		
		
		
//		if(true) return;
//		
//		if (WarteLoc != null && WarteUUID != null) {
//			//WarteLoc.getChunk().load();
//
//			for (int i = 0; i < WarteLoc.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//
//			}
//
//			Location newChunk1 = WarteLoc;
//
//			newChunk1 = newChunk1.add(16, 0, 0);
//			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//
//			newChunk1 = newChunk1.add(-32, 0, 0);
//			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//
//			newChunk1 = newChunk1.add(16, 0, 16);
//			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
//			
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//
//			newChunk1 = newChunk1.add(0, 0, -32);
//			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
//			
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(0, 0, 16);
//
//			newChunk1 = newChunk1.add(16, 0, 16);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(-16, 0, -16);
//
//			newChunk1 = newChunk1.add(-16, 0, 16);
//
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(16, 0, -16);
//
//			newChunk1 = newChunk1.add(16, 0, -16);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(-16, 0, 16);
//
//			newChunk1 = newChunk1.add(-16, 0, -16);
//
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(16, 0, 16);
//
//		} else if (WarteUUID == null && WarteLoc != null) {
//			WarteLoc.getChunk().load();
//
//			for (int i = 0; i < WarteLoc.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
//						.toString().equalsIgnoreCase(WarteUUID.toString())) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//
//			}
//
//			Location newChunk1 = WarteLoc;
//
//			newChunk1 = newChunk1.add(16, 0, 0);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//
//			newChunk1 = newChunk1.add(-32, 0, 0);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//
//			newChunk1 = newChunk1.add(16, 0, 16);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//
//			newChunk1 = newChunk1.add(0, 0, -32);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(0, 0, 16);
//
//			newChunk1 = newChunk1.add(16, 0, 16);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(-16, 0, -16);
//			newChunk1 = newChunk1.add(-16, 0, 16);
//
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(16, 0, -16);
//
//			newChunk1 = newChunk1.add(16, 0, -16);
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(-16, 0, 16);
//
//			newChunk1 = newChunk1.add(-16, 0, -16);
//
//			if (!newChunk1.getChunk().isLoaded()) {
//				//newChunk1.getChunk().load();
//			}
//			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
//
//				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
//						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
//								.equalsIgnoreCase(
//										nameQueue)) {
//					WarteLoc.getChunk().getEntities()[i].remove();
//				}
//			}
//			newChunk1 = newChunk1.add(16, 0, 16);
//		}
	}

	private static void teleportBack() {
		new BukkitRunnable() {
			

			public void run() {
				
				if(zombie != null && zombie.isDead()) {
					respawnZombie();
					return;
				}
				
				try {
					for (World worlds : Bukkit.getWorlds()) {
						for (Entity en : worlds.getEntities()) {
							if (en instanceof Zombie) {
								if (en.getCustomName() != null
										&& en.getCustomName().equalsIgnoreCase(
												nameQueue)) {
									YamlConfiguration cfg = Main.ins.utils.getYaml("spawns");

									Location loc = new Location(Bukkit.getWorld("world"),
											0, 0, 0);
									double x = cfg.getDouble("Spawns.Queue.X");
									double y = cfg.getDouble("Spawns.Queue.Y");
									double z = cfg.getDouble("Spawns.Queue.Z");
									String worldname = cfg.getString("Spawns.Queue.World");

									if (worldname == null) {
										return;
									}
									World welt = Bukkit.getWorld(worldname);

									loc.setX(x);
									loc.setY(y);
									loc.setZ(z);
									loc.setWorld(welt);
									
									
									
									if(en.getLocation().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && en.getLocation().distance(loc) > 1.5) {
										en.teleport(loc);
									}
								}
							}
						}
					}
				} catch (Exception e) {e.printStackTrace();}
				
				
				
				
			}
		}.runTaskAsynchronously(Main.ins);
		
	}

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if (e.getEntity() instanceof Zombie) {
			if (e.getEntity().getCustomName() != null
					&& e.getEntity()
							.getCustomName()
							.equalsIgnoreCase(
									nameQueue)) {
				if(e.getEntity() instanceof Player) return;
				e.setCancelled(true);
			}
		}

	}

	static int respawntime = 0;

	public static void respawner() {

		
				if (respawntime <= 0) {
					
					
					new BukkitRunnable() {
						

						public void run() {
							despawnQuequeZombie();
							spawnQuequeZombie();
							
						}
					}.runTask(Main.ins);
					
					respawntime = 120;
				} else {
					respawntime--;
				}
				teleportBack();

			
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Zombie) {
			if (e.getEntity().getCustomName() != null
					&& e.getEntity()
							.getCustomName()
							.equalsIgnoreCase(
									nameQueue)) {
				e.setCancelled(true);
				e.setDamage(0);
				
				
				
			}

		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Zombie) {
			if (e.getEntity().getCustomName() != null
					&& e.getEntity()
							.getCustomName()
							.equalsIgnoreCase(
									nameQueue)) {
				e.setCancelled(true);
				e.setDamage(0);
				
				if(e.getDamager().getType().equals(EntityType.PLAYER)) {
					Player p = (Player)e.getDamager();
					
					
					if(Main.ins.utils.getChallanger().equals(p.getItemInHand())) {
						if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
							if(Main.ins.getOneVsOnePlayer(p).isInQueue()) {
								Main.ins.getOneVsOnePlayer(p).setInQueue(false);
								p.sendMessage(Main.ins.prefixBlue + "Du bist nun §cnicht §7mehr in der §6Warteschlange!");
							} else {
								Main.ins.getOneVsOnePlayer(p).setInQueue(true);
								p.sendMessage(Main.ins.prefixBlue + "Du bist nun in der §6Warteschlange!");
							}
						}
					}
					
					
				}
				
			}

		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof Zombie) {
			if (e.getRightClicked().getCustomName() != null
					&& e.getRightClicked()
							.getCustomName()
							.equalsIgnoreCase(
									nameQueue)) {
				e.setCancelled(true);
				
				
				
					Player p = (Player)e.getPlayer();
					
					
					if(Main.ins.utils.getChallangerDisabled().equals(p.getItemInHand()) || Main.ins.utils.getChallanger().equals(p.getItemInHand())) {
						if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
							QueueInv.open(p);
						}
					}
					
					
				
				
			}
		}
		
	}
	
	public static void startChecker() {
		
		 final HashMap<UUID, Location> playerLocs = new HashMap<UUID, Location>();
		    new BukkitRunnable() {
				

				public void run() {
					
					for(final Player p : Bukkit.getOnlinePlayers())  {
						if(p == null || playerLocs == null) continue;
						
							if(playerLocs.containsKey(p.getUniqueId()) && 
							   playerLocs.get(p.getUniqueId()).getWorld().getName().equals(p.getWorld().getName()))
								
								if(playerLocs.get(p.getUniqueId()).distance(p.getLocation()) > 0) {
									onMove(p);
								}
					}
						
								
					
					playerLocs.clear();
					
					for(final Player p : Bukkit.getOnlinePlayers())  {
						playerLocs.put(p.getUniqueId(), p.getLocation());
					}
					
					
				}
			}.runTaskTimerAsynchronously(Main.ins, 0,1);
	}
	
	
	public static void onMove(final Player p) {
				if(queueLoc != null && p.getWorld().getName().equalsIgnoreCase(queueLoc.getWorld().getName())) {
					if(p.getLocation().distance(queueLoc) <= 3 ) {
						p.spigot().setCollidesWithEntities(false);
						return;
					} else {
						p.spigot().setCollidesWithEntities(true);
					}
				} else {
					p.spigot().setCollidesWithEntities(true);
				}
	}

}
