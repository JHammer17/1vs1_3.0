package de.onevsone.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;


import de.onevsone.Main;
import de.onevsone.api.PlayerScrollEvent;
import de.onevsone.enums.PlayerState;
import de.onevsone.methods.StatHolograms;
import de.onevsone.methods.SoundMgr.JSound;

public class LobbyEvents implements Listener {
	
	
	/*
	 * Cancel all Inv Click Events in the Lobby
	 */
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {

		if (Main.ins.isInOneVsOnePlayers(e.getWhoClicked().getUniqueId())
				&& Main.ins.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() == PlayerState.INLOBBY) {
			if (e.getCurrentItem() != null)
				e.setCancelled(true);
		}

	}
	
	/*
	 * Removes from Autoqueue
	 */
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())
				&& (Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY || 
				Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).isWasInQueue()) {
				Main.ins.getOneVsOnePlayer(e.getPlayer()).setWasInQueue(false);
				e.getPlayer().sendMessage(Main.ins.prefixBlue + "Du wurdest §cnicht §7in die §6Warteschlange §7hinzugefügt!");
				Main.ins.utils.getSoundMgr().playSound(e.getPlayer(), JSound.NOTE_BASS, 1f, 1f);
			}
		}
	}
	

	/*
	 * Cancel all Item Drops in the Lobby
	 */
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && 
			(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY ||
			 Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			if (e.getItemDrop() != null) {
				e.setCancelled(true);
			}	
		}

	}
	
	/*
	 * Cancel Hunger Event
	 */
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getEntity().getUniqueId()) && 
			(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INLOBBY ||
			 Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			e.setCancelled(true);
			e.setFoodLevel(25);
		}

	}

	/*
	 * Cancel all Damage Events caused by the Player
	 */
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		
		if(Main.ins.isInOneVsOnePlayers(e.getDamager().getUniqueId()) && 
				(Main.ins.getOneVsOnePlayer(e.getDamager().getUniqueId()).getpState() == PlayerState.INLOBBY ||
				 Main.ins.getOneVsOnePlayer(e.getDamager().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			e.setCancelled(true);
			e.setDamage(0);
		}
		
	}

	/*
	 * Cancel all Damage Events caused against the Player
	 */
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getEntity().getUniqueId()) && 
			(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INLOBBY ||
			 Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			e.setCancelled(true);
			e.setDamage(0);
		}

	}

	/*
	 * Cancel all Crafting Events of the Player
	 */
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getWhoClicked().getUniqueId()) && 
			Main.ins.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() == PlayerState.INLOBBY) {
			e.setCancelled(true);
		}
	}

	/*
	 * Cancel Block Place Event
	 */
	@EventHandler
	public void onBuild(BlockPlaceEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && 
		    (Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY ||
		    Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			e.setCancelled(true);
		}
	}
	
	/*
	 * Cancel Block Place Event
	 */
	@EventHandler
	public void onBuild(BlockBreakEvent e) {
		if (Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && 
		    (Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY ||
		    Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInterAct(PlayerInteractEvent e) {
	 Player p = e.getPlayer();
	 /*Sachen, wenn man nicht im Kit-Editor bereich ist*/
	 if(Main.ins.isInOneVsOnePlayers(p.getUniqueId()) && Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INLOBBY) {
		 if(e.getAction() == Action.RIGHT_CLICK_BLOCK && 
			Main.ins.utils.checkRegion(e.getClickedBlock().getLocation(), Main.ins.utils.getMaxKitEdit(), Main.ins.utils.getMinKitEdit())) {
				  
					
					
				   if(p.getGameMode() != GameMode.CREATIVE) {
				   if(e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.WOOD_BUTTON) {} else {
					   e.setCancelled(true);
				   }
				   
				   
				   return; 
				  }
				 }
	 
	 
	 /*----------------------------*/
		
	 
     /*Verzauberungstisch im Kit-Editor*/
	 if(Main.ins.isInOneVsOnePlayers(p.getUniqueId()) && 
		Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.INARENA) {
	  
	  if(e.getAction() == Action.RIGHT_CLICK_BLOCK && Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INKITEDIT) {
	   
	   /*Virtueller Amboss*/
	   if(e.getClickedBlock().getType() == Material.ANVIL) {
		p.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));   
		return;
	   }
	   /*------------------*/
	   /*Werkbank*/
	   if(e.getClickedBlock().getType() == Material.WORKBENCH) return;
	   /*------------------*/
	   /*Verzauberungstisch*/
	   if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) return;
	   /*------------------*/
	  }
	  
	 
	 
	  if(e.getAction() == Action.RIGHT_CLICK_BLOCK 
		  && e.getItem() != null && e.getItem().getType() == Material.WRITTEN_BOOK 
		  && e.getClickedBlock().getType() != Material.TRAP_DOOR
		  && e.getClickedBlock().getType() != Material.STONE_BUTTON
		  && e.getClickedBlock().getType() != Material.WOOD_BUTTON
		  && e.getClickedBlock().getType() != Material.DISPENSER
		  && e.getClickedBlock().getType() != Material.DROPPER
		  && e.getClickedBlock().getType() != Material.FENCE_GATE
		  && e.getClickedBlock().getType() != Material.ACACIA_FENCE
		  && e.getClickedBlock().getType() != Material.BIRCH_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.DARK_OAK_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.JUNGLE_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.SPRUCE_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.TRAP_DOOR
		  && e.getClickedBlock().getType() != Material.ACACIA_DOOR
		  && e.getClickedBlock().getType() != Material.BIRCH_DOOR
		  && e.getClickedBlock().getType() != Material.DARK_OAK_DOOR
		  && e.getClickedBlock().getType() != Material.JUNGLE_DOOR
		  && e.getClickedBlock().getType() != Material.WOOD_DOOR
		  && e.getClickedBlock().getType() != Material.DAYLIGHT_DETECTOR
		  && e.getClickedBlock().getType() != Material.DAYLIGHT_DETECTOR_INVERTED
		  && e.getClickedBlock().getType() != Material.LEVER
		  && e.getClickedBlock().getType() != Material.HOPPER
		  && e.getClickedBlock().getType() != Material.FURNACE
		  && e.getClickedBlock().getType() != Material.WORKBENCH
		  && e.getClickedBlock().getType() != Material.ANVIL
		  && e.getClickedBlock().getType() != Material.ENDER_CHEST
		  && e.getClickedBlock().getType() != Material.TRAPPED_CHEST
		  && e.getClickedBlock().getType() != Material.CHEST
		  && e.getClickedBlock().getType() != Material.JUKEBOX
		  && e.getClickedBlock().getType() != Material.ENCHANTMENT_TABLE
		  && e.getClickedBlock().getType() != Material.ENDER_PORTAL_FRAME
		  && e.getClickedBlock().getType() != Material.BED
		  && e.getClickedBlock().getType() != Material.COMMAND) return;
	  else if(e.getAction() != Action.RIGHT_CLICK_AIR)e.setCancelled(true);
	 
	  
	  
	  
	  if(e.getItem() != null) {
		  if(e.getItem().getType() == Material.POTION) e.setCancelled(true);
		  if(e.getItem().getType() != Material.DIAMOND_SWORD &&
			 e.getItem().getType() != Material.IRON_SWORD &&
			 e.getItem().getType() != Material.STONE_SWORD &&
			 e.getItem().getType() != Material.WOOD_SWORD &&
			 e.getItem().getType() != Material.GOLD_SWORD) p.updateInventory();
			  
	  }
	 
	  
	  
	  
		 
	  
	  }
	 }
	 /*---------------------------------*/
	}
	
	@EventHandler
	 public void onSplash(PotionSplashEvent e) {
		 
		 for(Entity en : e.getAffectedEntities()) {
			if(en instanceof Player) {
			 Player p = (Player) en;
			 if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
			  ItemStack stack = e.getPotion().getItem();
			  PotionMeta meta = (PotionMeta) stack.getItemMeta();
			  
			  for(int i = 0; i < meta.getCustomEffects().size(); i++) {	
			   if(meta.getCustomEffects().get(i).getAmplifier() >= 252 || meta.getCustomEffects().get(i).getAmplifier() <= 0) {
				   e.setCancelled(true);
			   }			
			  }
			 }
			}
		 }
	 }
	 
	 @EventHandler
	 public void onShoot(EntityShootBowEvent e) {
		 if(e.getEntity() instanceof Player) {
			 Player p = (Player) e.getEntity();
//			 if(Kit.hasKit.contains(p)) e.setCancelled(true);//TODO Cancel, wenn Kit hat mit /kit
			 if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INKITEDIT) e.setCancelled(true);
		 }
		 
		 
	 }
	
	 
	 @EventHandler
	 public void onPotionThrow(ProjectileLaunchEvent e) {
		 
		 	if(e.getEntity().getShooter() instanceof Player) {
		 		Player p = (Player) e.getEntity().getShooter();
		 		 if(Main.ins.getOneVsOnePlayer(p.getUniqueId()).getpState() == PlayerState.INKITEDIT ||
		 			Main.ins.getOneVsOnePlayer(p.getUniqueId()).getpState() == PlayerState.INLOBBY) {
					e.setCancelled(true); 
				 }
			}
 	}
	 
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent e) {
	 		 if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT ||
	 			Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY) {
				e.setCancelled(true); 
			 }
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e) {
	 		 if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT ||
	 			Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY) {
				e.setCancelled(true); 
			 }
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INKITEDIT ||
	 			Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY) {
				e.setCancelled(true); 
			 }
	}
	
	@EventHandler
	public void toggleChallanger(PlayerInteractEvent e) {
		if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INLOBBY) {
		 if(e.getPlayer().isSneaking() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			 
			 if(e.getItem() != null && (Main.ins.utils.compareItems(Main.ins.utils.getChallanger(), e.getItem()) || Main.ins.utils.compareItems(Main.ins.utils.getChallangerDisabled(), e.getItem()))) {
			  if(System.currentTimeMillis()-Main.ins.getOneVsOnePlayer(e.getPlayer()).getAcceptChallangesTimer() > 1000) {
				  Main.ins.getOneVsOnePlayer(e.getPlayer()).setAcceptChallangesTimer(System.currentTimeMillis());
				  if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getAcceptChallanges()) {
						 Main.ins.getOneVsOnePlayer(e.getPlayer()).setAcceptChallanges(false);
						 Main.ins.utils.updateChallanger(e.getPlayer());
					 } else {
						 Main.ins.getOneVsOnePlayer(e.getPlayer()).setAcceptChallanges(true);
						 Main.ins.utils.updateChallanger(e.getPlayer());
					 }
				
			  } else if(System.currentTimeMillis()-Main.ins.getOneVsOnePlayer(e.getPlayer()).getAcceptChallangesTimer() > 200) {
				  e.getPlayer().sendMessage(Main.ins.prefixRed + "Bitte warte einen Moment");
			  }
			 }
			 
			
		 }
	    }
	}
	
	@EventHandler
	public void onScroll(PlayerScrollEvent e) {
		
		
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() != PlayerState.INLOBBY &&
			   Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() != PlayerState.INKITEDIT) {
				return;
			}
			
			if(Main.ins.utils.getHoloSpawn().getWorld() != null) {
				if(e.getPlayer().getWorld().getName().equalsIgnoreCase(Main.ins.utils.getHoloSpawn().getWorld().getName())) {
					
					if(e.getPlayer().getLocation().distance(Main.ins.utils.getHoloSpawn()) <= 12) {//TODO Distanz in config!!!
						if(e.nextSelected()) {
							Main.ins.getOneVsOnePlayer(e.getPlayer()).setHoloStatsType(Main.ins.getOneVsOnePlayer(e.getPlayer()).getHoloStatsType()+1);
						} else {
							Main.ins.getOneVsOnePlayer(e.getPlayer()).setHoloStatsType(Main.ins.getOneVsOnePlayer(e.getPlayer()).getHoloStatsType()-1);
						}
						StatHolograms.respawnHologram(Main.ins.getOneVsOnePlayer(e.getPlayer()));
					}
				}
			}
			
			
			
		}
		
	}
	
	@EventHandler
	public void onWorldInit(WorldInitEvent e) {
		
		if(e.getWorld().getName().contains("1vs1Worlds/Arenas")) {
			e.getWorld().setKeepSpawnInMemory(false);
//			WorldLoadEvent.getHandlerList().unregister(Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
		}
	}
	
//	@EventHandler(priority=EventPriority.LOWEST)
//	public void onLoad(WorldLoadEvent e) {
//		
//		if(e.getWorld().getName().contains("1vs1Worlds/Arenas")) {
//			new BukkitRunnable() {
//				
//				@Override
//				public void run() {
////					WorldResetMgr.alreadyLoading = false;
//					Bukkit.broadcastMessage("§aFinshed " + e.getWorld().getName());
//				}
//			}.runTaskLaterAsynchronously(Main.ins, 40);
//		}
//		
////		
//	}
	
	
}
