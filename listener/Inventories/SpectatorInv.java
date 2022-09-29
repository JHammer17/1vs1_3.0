/**
 * 
 */
package de.onevsone.listener.Inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.arenas.SpectateMgr;
import de.onevsone.enums.BestOfsPrefs;
import de.onevsone.enums.PlayerState;
import de.onevsone.objects.OneVsOneArena;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 24.03.2018 18:44:31					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class SpectatorInv implements Listener {


	public SpectatorInv() {
		updater();
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).isIn1vs1()) {
				if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INLOBBY ||
				   Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.SPECTATOR) {
					if(e.getItem() != null && 
							e.getItem().getType() == Material.GHAST_TEAR && 
							e.getItem().hasItemMeta() && 
							e.getItem().getItemMeta().hasDisplayName() && 
							e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Beobachten §7(/spec)")) {
						e.setCancelled(true);
						open(e.getPlayer(), null);
					}
				}
			}
		}
	}

	
	
	private static HashMap<UUID, HashMap<Integer, String>> slots = new HashMap<>();
	private static ArrayList<UUID> opened = new ArrayList<>();
	
	
	public static void open(Player p, Inventory inv) {
		boolean created = false;
		
		if(inv == null || (!inv.getTitle().equalsIgnoreCase("Kampf beobachten§a") || inv.getSize() != 54)) {
			inv = Bukkit.createInventory(null, 9*6, "Kampf beobachten§a");
			created = true;
		}
		
		if(!opened.contains(p.getUniqueId())) opened.add(p.getUniqueId());
		
		
		int s = 0;
		HashMap<Integer, String> slot;
		
		if(slots.containsKey(p.getUniqueId())) {
			slot = slots.get(p.getUniqueId());
		} else {
			slot = new HashMap<>();
		}
		slot.clear();
		
		for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
			
			if(!arenas.isUsed() || arenas.isEnded() || arenas.isLocked()) continue;
			
			StringBuilder lore = new StringBuilder();
			
			lore.append("§7--------------------\n");
			for(UUID pos1 : arenas.getPos1()) {
				if(Bukkit.getPlayer(pos1) != null) {
					lore.append("§b§l" + Bukkit.getPlayer(pos1).getDisplayName() + "\n");
				}
			}
			lore.append("§7 vs\n");
			for(UUID pos2 : arenas.getPos2()) {
				if(Bukkit.getPlayer(pos2) != null) {
					lore.append("§b§l" + Bukkit.getPlayer(pos2).getDisplayName() + "\n");
				}
			}
			lore.append("§7--------------------\n");
			lore.append("§7 Map: §6" + arenas.getLayout() + "\n");
			lore.append("§7 Kit: §6" + arenas.getKitName() + "\n");
			lore.append("§7 Kampfdauer: §6" + arenas.getFormatedFightTime() + "\n");
			
			lore.append("§7--------------------");
			if(arenas.getBestOf() != BestOfsPrefs.BESTOF1) {
				if(arenas.getBestOf() == BestOfsPrefs.BESTOF3) {
					lore.append("\n§7Typ: §6Best of 3");
					lore.append("\n§7--------------------");
				} else {
					lore.append("\n§7Typ: §6Best of 5");
					lore.append("\n§7--------------------");
				}
			}
			
			if(arenas.isRanked()) {
				lore.append("\n§6§lRanked");
				lore.append("\n§7--------------------");
			}
			
			
			
			int id = 41;
			int subID = 0;
			
			YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
			
			
			
				
				
				if(cfg.getString("Layout." + arenas.getLayout() + ".ItemID") != null && 
				   cfg.getString("Layout." + arenas.getLayout() + ".ItemID").contains(":")) {
					try {
						String[] idS = cfg.getString("Layout." + arenas.getLayout() + ".ItemID").split(":");
						if(idS.length == 2) {
							id = Integer.parseInt(idS[0]);
							subID = Integer.parseInt(idS[1]);
						}  
						
					} catch (NumberFormatException e) {}
				} else {
					try {
						id = Integer.parseInt(cfg.getString("Layout." + arenas.getLayout() + ".ItemID"));
					} catch (NumberFormatException e) {}
				}
				
			ItemStack arena = Main.ins.utils.createItem(id, subID, 1, "§6" + arenas.getName(), lore.toString());
			
			slot.put(s, arenas.getName());
			
			inv.setItem(s, arena);
			
			s++;
				
		}
		
		for(int i = s;i < 54; i++) {
			ItemStack barrier = Main.ins.utils.createItem(Material.BARRIER, 0, 1, "§6Arena " + (i+1), "\n§8- §cnicht besetzt §8-");
					
			inv.setItem(i, barrier);
		}
		
		
		slots.remove(p.getUniqueId());
		slots.put(p.getUniqueId(), slot);
		
		
		if(created) p.openInventory(inv);
	}
	
	
	private void updater() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(UUID uuid : opened) {
					if(Bukkit.getPlayer(uuid) == null) {
						opened.remove(uuid);
						continue;
					}
					if(Bukkit.getPlayer(uuid).getOpenInventory() != null) {
						open(Bukkit.getPlayer(uuid), Bukkit.getPlayer(uuid).getOpenInventory().getTopInventory());
						
					}
					
					
					
				}
				
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 20);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		opened.remove(e.getPlayer().getUniqueId());
		slots.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
			if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
				Player p = (Player) e.getWhoClicked();
				if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
					if(e.getInventory().getName().equalsIgnoreCase("Kampf beobachten§a")) {
						e.setCancelled(true);
						if(e.getClickedInventory().getName().equalsIgnoreCase("Kampf beobachten§a")) {
							
							e.setCancelled(true);
							
							if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
								return;
							}
							Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
							
							
							if(slots.get(p.getUniqueId()).get(e.getSlot()) != null) {
								SpectateMgr.spectate(p, slots.get(p.getUniqueId()).get(e.getSlot()), false, false);
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cIn dieser Arena findet gerade kein Kampf statt!");
							}
						}
					}
				}
			}
		}
	}
	
}
