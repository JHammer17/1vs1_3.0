/**
 * 
 */
package de.onevsone.arenas;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 24.03.2018 20:23:23					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class SpectateMgr implements Listener {

	public static boolean spectate(final Player p, String arena, boolean tournament, boolean death) {
	
		
		if(!Main.ins.utils.isMainThread()) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					spectate(p, arena, tournament, death);
					
				}
			}.runTask(Main.ins);
			return true;
		}
		
		
		if(p != null && arena != null) {
			if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
				
				
				 if(Main.ins.getOneVsOnePlayer(p).getpState() == 
					 PlayerState.SPECTATOR || 
					 Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INLOBBY || 
					 tournament || death) {
					 
					 for(Player online : Bukkit.getOnlinePlayers()) {
						 if(Main.ins.isInOneVsOnePlayers(online.getUniqueId())) {
							 if(Main.ins.getOneVsOnePlayer(online).getpState() != PlayerState.SPECTATOR) {
								 online.showPlayer(p);
							 }
						 }
					 }
					 //TODO WIEDER EINBAUEN!
					 
					 
//					 if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.SPECTATOR) {
//						 String oldArena = Main.ins.getOneVsOnePlayer(p).getSpecator();
//						 for(UUID pos1 : Main.ins.getOneVsOneArena(oldArena).getPos1()) {
//							 if(Bukkit.getPlayer(pos1) != null) {
//								 Bukkit.getPlayer(pos1).showPlayer(p);
//							 }
//						 }
//						 
//						 for(UUID pos2 : Main.ins.getOneVsOneArena(oldArena).getPos2()) {
//							 if(Bukkit.getPlayer(pos2) != null) {
//								 Bukkit.getPlayer(pos2).showPlayer(p);
//							 }
//						 }
//					 }
					 
					 
					 if(!Main.ins.getOneVsOneArena(arena).isUsed() || (Main.ins.getOneVsOneArena(arena).isEnded() && !tournament)) {
						 p.sendMessage(Main.ins.prefixRed + "§cIn dieser Arena findet gerade kein Kampf statt!");
						 return false;
					 }
					 
					 if(Main.ins.getOneVsOneArena(arena).isLocked()) {
						 p.sendMessage(Main.ins.prefixRed + "§cIn dieser Arena findet gerade kein Kampf statt!");
						 return false;
					 }
					 
					 p.teleport(Main.ins.getOneVsOneArena(arena).getMiddleLoc());
					 
					 Main.ins.getOneVsOnePlayer(p).setSpecator(arena);
					 Main.ins.getOneVsOnePlayer(p).setInQueue(false);
					 
					 
					 Main.ins.getOneVsOnePlayer(p).setpState(PlayerState.SPECTATOR);
					 
					
					 
					 
					 new BukkitRunnable() {
						
						@Override
						public void run() {
							
							 for(Player online : Bukkit.getOnlinePlayers()) 
								 online.hidePlayer(p);
//								 
							 
							 
							p.spigot().setCollidesWithEntities(false);
							p.setAllowFlight(true);
							
						}
					}.runTaskLater(Main.ins, 12);
					 
					
					 Main.ins.utils.giveSpectatorItem(p, arena, tournament, death);
					 p.getInventory().setHelmet(Main.ins.utils.createItem(Material.IRON_HELMET, 00, 1, "Lel", null));
					 
					 Main.ins.getOneVsOneArena(arena).addSpectator(p.getUniqueId());
					 
					 
					 return true;
				 }
			}
		}
		
		return false;
	}
	
	public static void stopSpectate(Player p, boolean tournament, boolean isDeathPlayer) {
		if(p != null) {
			if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
				 if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.SPECTATOR || 
					tournament) {
					 
					 String arena = Main.ins.getOneVsOnePlayer(p).getSpecator();
					 
					 
					 
					 if(!isDeathPlayer && !tournament) {
						 Main.ins.utils.tpToLobby(p);
						 Main.ins.getOneVsOnePlayer(p).setpState(PlayerState.INLOBBY);
						 
					 }
					 
					 Main.ins.getOneVsOnePlayer(p).setSpecator(null);
					 Main.ins.getOneVsOnePlayer(p).setInQueue(false);
					 
					 
					 
					 for(Player online : Bukkit.getOnlinePlayers()) {
						 online.showPlayer(p);
					 }
					 //TODO EINBAUEN
					 
				
					 
					 
					 
					 
					 p.setAllowFlight(false);
					 p.setFlying(false);
					 p.spigot().setCollidesWithEntities(true);
					 if(!isDeathPlayer) 
					 Main.ins.utils.giveLobbyItems(p);
					 
					 Main.ins.getOneVsOneArena(arena).removeSpectator(p.getUniqueId());
					 
				 }
			}
		}
		
		return;
	}
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
				ItemStack leave = Main.ins.utils.createItem(Material.INK_SACK, 8, 1, "§cSpectator Modus verlassen", null);
				
				if(e.getItem() != null) {
				 if(e.getItem().equals(leave)) {
					 e.setCancelled(true);
					 stopSpectate(e.getPlayer(), false, false);
				 }
				 
				 UUID clicked = getUUIDBySlot(
						 e.getPlayer().getInventory().getHeldItemSlot(), 
						 Main.ins.getOneVsOneArena(
								 Main.ins.getOneVsOnePlayer(
								 e.getPlayer().getUniqueId()).
								 getSpecator()).getName());
				 
				 if(clicked != null && Bukkit.getPlayer(clicked) != null) {
					 
					 e.getPlayer().openInventory(Bukkit.getPlayer(clicked).getInventory());
				 }
				 
				}
				
			}
		}
	}
	
	@EventHandler
	public void onInteract(BlockBreakEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(BlockPlaceEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(EntityDamageByEntityEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getDamager().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getDamager().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(EntityDamageEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getEntity().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getWhoClicked().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(EntityTargetEvent e) {
		if(e != null && e.getTarget() != null &&
				e.getTarget().getType() == EntityType.PLAYER && 
				Main.ins.isInOneVsOnePlayers(e.getTarget().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getTarget().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(FoodLevelChangeEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getEntity().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				e.setCancelled(true);
			}
		}
	}
	
	private UUID getUUIDBySlot(int slot, String arena) {
		int s = 0;
		for(UUID pos1 : Main.ins.getOneVsOneArena(arena).getPos1()) {
			if(Bukkit.getPlayer(pos1) == null) continue;
			if(s > 2) break;
			
			if(s == slot) return pos1;
			
			s++;
		}
		
		for(UUID pos2 : Main.ins.getOneVsOneArena(arena).getPos2()) {
			if(Bukkit.getPlayer(pos2) == null) continue;
			
			if(s > 5) break;
			
			if(s == slot) return pos2;
			
			s++;
		}
		return null;
	}
	
	
	
	
}
