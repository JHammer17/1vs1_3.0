package de.onevsone.listener.Inventories.subInvs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.onevsone.Main;
import de.onevsone.listener.Inventories.MainInv;
import de.onevsone.methods.LayoutMgr;
import de.onevsone.methods.SoundMgr.JSound;

public class MapsInv implements Listener {

	
	private static HashMap<UUID, HashMap<Integer, String>> slots = new HashMap<>();
	private static HashMap<UUID, Integer> offset = new HashMap<>();
	
	public static void open(Player p, Inventory inv) {
		if(inv == null || inv.getSize() < 9*6 || !inv.getName().equalsIgnoreCase("Maps§a")) {
			inv = Bukkit.createInventory(null, 9*6, "Maps§a");
			p.openInventory(inv);
		} else {
			inv.clear();
		}
		
		ItemStack disabled = Main.ins.utils.createItem(Material.INK_SACK, 8, 1, "§c§lDeaktiviert", null);
		ItemStack enabled = Main.ins.utils.createItem(Material.INK_SACK, 10, 1, "§a§lAktiv", null);
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§a", null);
		ItemStack goUp = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 0, 1, "§6^ Hoch ^", null);
		ItemStack goDown = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 0, 1, "§6v Runter v", null);
		
		inv.setItem(4, empty);
		inv.setItem(13, empty);
		inv.setItem(22, empty);
		inv.setItem(31, empty);
		inv.setItem(40, empty);
		inv.setItem(49, empty);
		
		int slot = 0;
		int number = 0;
		boolean rtl = true;
		
		if(slots.containsKey(p.getUniqueId())) slots.get(p.getUniqueId()).clear();
		
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
		HashMap<Integer, String> slotType = new HashMap<>();
		
		String disabledList = Main.ins.database.getDisabledMaps(p.getUniqueId());
		
		
		if(offset.containsKey(p.getUniqueId()) && offset.get(p.getUniqueId()) > 0) {
			inv.setItem(4, goUp);
		}
		
		int pOffset = 0;
		if(offset.containsKey(p.getUniqueId())) {
			pOffset = offset.get(p.getUniqueId());
		}
		
		if(cfg.getConfigurationSection("Layout") != null) {
			for(String layout : cfg.getConfigurationSection("Layout").getKeys(false)) {
				number++;
				if(number <= pOffset*4) {
					continue;
				}
				
				if(slot >= 54) {
					inv.setItem(49, goDown);
					break;
				}
				
				slotType.put(slot, layout);
				slotType.put(slot+1, layout);
				
				
				
				int id = 1;
				int subID = 0;
				String author = "-";
				
				if(cfg.getString("Layout." + layout + ".Author") != null) 
					author = cfg.getString("Layout." + layout + ".Author");
				
				if(cfg.getString("Layout." + layout + ".ItemID") != null && 
				   cfg.getString("Layout." + layout + ".ItemID").contains(":")) {
					try {
						String[] idS = cfg.getString("Layout." + layout + ".ItemID").split(":");
						if(idS.length == 2) {
							id = Integer.parseInt(idS[0]);
							subID = Integer.parseInt(idS[1]);
						}  
						
					} catch (NumberFormatException e) {}
				} else {
					try {
						id = Integer.parseInt(cfg.getString("Layout." + layout + ".ItemID"));
					} catch (NumberFormatException e) {}
				}
				
				ItemStack mapItem = Main.ins.utils.createItem(id, subID, 1, "§6" + layout, "\n§7von: " + author);
				
				if(rtl) {
					inv.setItem(slot, mapItem);
					
					if(isLayoutDisabled(disabledList, layout)) {
						inv.setItem(slot+1, disabled);
					} else {
						inv.setItem(slot+1, enabled);
					}
				} else {

					if(isLayoutDisabled(disabledList, layout)) {
						inv.setItem(slot, disabled);
					} else {
						inv.setItem(slot, enabled);
					}
					inv.setItem(slot+1, mapItem);
				}
				
				rtl = !rtl;
				
				slot+=2;
				
				if(slot == 4 || slot == 13 || slot == 22 || slot == 31 || slot == 40 || slot == 49) {
					slot++;
				}
			}
			
			while(slots.containsKey(p.getUniqueId())) slots.remove(p.getUniqueId());
			
			slots.put(p.getUniqueId(), slotType);
		}
		
		
		
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(e.getInventory().getName().equalsIgnoreCase("Maps§a")) {
					e.setCancelled(true);
					if(e.getClickedInventory() != null && e.getClickedInventory().getName().equalsIgnoreCase("Maps§a")) {
						
						if(e.isRightClick()) {
							MainInv.openInv(p);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							return;
						}
						
						if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
							return;
						}
						
						Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
						
						if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return; 
						
						
						int slot = e.getSlot();
						
						if(slot == 4 || slot == 13 || slot == 22 || slot == 31 || slot == 40 || slot == 49) {
							if(slot == 4) {
								if(offset.containsKey(p.getUniqueId()) && offset.get(p.getUniqueId()) > 0) {
									int newOffset = offset.get(p.getUniqueId());
									
									newOffset--;
									offset.remove(p.getUniqueId());
									
									offset.put(p.getUniqueId(), newOffset);
									open(p, e.getInventory());
								}
							}
							if(slot == 49) {
								if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().getDurability() == 0) {
									int newOffset = 0;
									if(offset.containsKey(p.getUniqueId())) {
										newOffset = offset.get(p.getUniqueId());
									}
									newOffset++;
									offset.remove(p.getUniqueId());
									
									offset.put(p.getUniqueId(), newOffset);
									open(p, e.getInventory());
								}
							}
							return;
						}
						
						if(slots.containsKey(p.getUniqueId())) {
							HashMap<Integer, String> datas = slots.get(p.getUniqueId());
							
							String disabledList = Main.ins.database.getDisabledMaps(p.getUniqueId());
							
							ArrayList dList = LayoutMgr.getAllLayouts();
							
									
									
							dList.removeAll(Arrays.asList(disabledList.split(" ")));
							
							dList.remove(datas.get(e.getSlot()));
							
							if(dList.isEmpty()  && !isLayoutDisabled(disabledList, datas.get(e.getSlot()))) {
								p.sendMessage(Main.ins.prefixRed + "§cDu musst mindestens eine Map aktiviert haben!");
								e.setCancelled(true);
								return;
							}
						
//							for(String layout : Main.ins.)
							
							
							Main.ins.database.setMapDisabled(p.getUniqueId(), datas.get(e.getSlot()), !isLayoutDisabled(disabledList, datas.get(e.getSlot())));
							
							open(p, e.getInventory());
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cEin Fehler ist aufgetreten!");
							p.closeInventory();
						}
						
						
					}
				}
			}
		}
	}
	
	public static boolean isLayoutDisabled(String disabledList, String layout) {
		String[] datas = disabledList.split(" ");
		
		for(int i = 0; i < datas.length; i++) {
			if(datas[i].equals(layout)) return true;
		}
		
		return false;
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase("Maps§a")) {
			slots.remove(e.getPlayer().getUniqueId());
			offset.remove(e.getPlayer().getUniqueId());
		}
	}
	
	
}
