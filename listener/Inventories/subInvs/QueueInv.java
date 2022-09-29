package de.onevsone.listener.Inventories.subInvs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.Inventories.MainInv;
import de.onevsone.methods.SoundMgr.JSound;
import de.onevsone.methods.Utils;

public class QueueInv implements Listener {
	
	public static void open(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9*4, "Warteschlangen Einstellungen§a");
		
		Utils utils = Main.ins.utils;
		
		ItemStack joinQueue = utils.createItem(Material.STAINED_GLASS_PANE, 5, 1, "§a§lWarteschlange betreten", null);
		ItemStack leaveQueue = utils.createItem(Material.STAINED_GLASS_PANE, 14, 1, "§c§lWarteschlange verlassen", null);
		
		ItemStack autoQueue = utils.createItem(Material.QUARTZ, 0, 1, "§6Automatische Warteschlange", "\n§a§lWenn aktiv:\n§7Nach einen Kampf, der durch \n§7die Warteschlange zustande kam, \n§7wirst du automatisch wieder zur \n§7Warteschlange hinzugefügt.");
		
		ItemStack ownKit = utils.createItem(Material.MINECART, 0, 1, "§6Eigenes Kit", "\n§7Es wird nur mit deinem Kit gespielt.");
		ItemStack enemyKit = utils.createItem(Material.POWERED_MINECART, 0, 1, "§6Kit des Gegners", "\n§7Es wird nur mit dem Kit des Gegners gespielt.");
		ItemStack randomKit = utils.createItem(Material.EXPLOSIVE_MINECART, 0, 1, "§6Zufälliges Kit", "\n§7Es wird mit einem zufälligen Kit gespielt.");
		
		ItemStack bestOf1 = utils.createItem(Material.GOLD_CHESTPLATE, 0, 1, "§6Best of 1", null);
		ItemStack bestOf3 = utils.createItem(Material.IRON_CHESTPLATE, 0, 3, "§6Best of 3", null);
		ItemStack bestOf5 = utils.createItem(Material.DIAMOND_CHESTPLATE, 0, 5, "§6Best of 5", null);
		
		ItemStack rankedAndUnranked = utils.createItem(Material.DIAMOND_HELMET, 0, 1, "§6Ranked & Unranked Kämpfe", null);
		ItemStack ranked = utils.createItem(Material.GOLD_HELMET, 0, 1, "§6Ranked Kämpfe", null);
		ItemStack unranked = utils.createItem(Material.IRON_HELMET, 0, 1, "§6Unranked Kämpfe", null);
		
		ItemStack enabled = utils.createItem(Material.INK_SACK, 10, 1, "§a§lAktiv", null);
		ItemStack disabled = utils.createItem(Material.INK_SACK, 8, 1, "§c§lDeaktiviert", null);
		

		int autoQueueEnabled = Main.ins.database.isQueuePrefEnabled(p.getUniqueId(), 0);
		int kitType = Main.ins.database.isQueuePrefEnabled(p.getUniqueId(), 1);
		int bestOfType = Main.ins.database.isQueuePrefEnabled(p.getUniqueId(), 2);
		int rankedType = Main.ins.database.isQueuePrefEnabled(p.getUniqueId(), 3);
		
		
		if(!Main.ins.getOneVsOnePlayer(p).isInQueue()) {
			inv.setItem(0, joinQueue);
			inv.setItem(1, joinQueue);
			inv.setItem(9, joinQueue);
			inv.setItem(10, joinQueue);
		} else {
			inv.setItem(0, leaveQueue);
			inv.setItem(1, leaveQueue);
			inv.setItem(9, leaveQueue);
			inv.setItem(10, leaveQueue);
		}
		
		//AutoQueue = 0; Kit = 1; BOf = 2; Ranked = 3; 
		
		inv.setItem(3, ownKit);
		inv.setItem(4, enemyKit);
		inv.setItem(5, randomKit);
		
		
		
		inv.setItem(7, disabled);
		if(bestOfType == 1) inv.setItem(7, enabled);
		inv.setItem(8, bestOf1);
		
		inv.setItem(12, disabled);
		inv.setItem(13, disabled);
		inv.setItem(14, disabled);
		
		if(kitType == 0) {
			inv.setItem(12, enabled);
		} else if(kitType == 1) {
			inv.setItem(13, enabled);
		} else if(kitType == 2) {
			inv.setItem(14, enabled);
		}
		
		
		inv.setItem(16, disabled);
		if(bestOfType == 2) inv.setItem(16, enabled);
		inv.setItem(17, bestOf3);
		
		inv.setItem(18, autoQueue);
		inv.setItem(19, disabled);
		if(autoQueueEnabled == 1) {
			inv.setItem(19, enabled);
		} 
		
		
		inv.setItem(21, disabled);
		inv.setItem(22, disabled);
		inv.setItem(23, disabled);
		
		if(rankedType == 0) {
			inv.setItem(21, enabled);
		} else if(rankedType == 1) {
			inv.setItem(22, enabled);
		} else if(rankedType == 2) {
			inv.setItem(23, enabled);
		}
		
		
		inv.setItem(25, disabled);
		if(bestOfType == 3) inv.setItem(25, enabled);
		inv.setItem(26, bestOf5);
		
		inv.setItem(30, rankedAndUnranked);
		inv.setItem(31, ranked);
		inv.setItem(32, unranked);
		
		p.openInventory(inv);
		
		
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(e.getClickedInventory() != null && e.getInventory().getName().equalsIgnoreCase("Warteschlangen Einstellungen§a")) {
					e.setCancelled(true);
					if(e.getClickedInventory() != null &&
					   e.getClickedInventory().getName().equalsIgnoreCase("Warteschlangen Einstellungen§a")) {
						
						if(e.isRightClick()) {
							MainInv.openInv(p);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							return;
						}
						
						
						if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
							return;
						}
						Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
						
						
						int s = e.getSlot();
						
						
						/*Queue Join/Leave*/
						if(s == 0 || s == 1 || s == 9 || s == 10) {
							if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INARENA ||
							   Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.SPECTATOR) {
								p.sendMessage(Main.ins.prefixRed + "§cDu musst in der Lobby sein, um der Warteschlange beitreten zu können!");
								return;
							}
							
							if(!Main.ins.getOneVsOnePlayer(p).isInQueue()) {
								Main.ins.getOneVsOnePlayer(p).setInQueue(true);
								p.sendMessage(Main.ins.prefixBlue + "Du bist nun in der §6Warteschlange.");
								Main.ins.utils.getSoundMgr().playSound(p, JSound.ORB_PICKUP, 1.0f, 1.0f);
							} else {
								Main.ins.getOneVsOnePlayer(p).setInQueue(false);
								p.sendMessage(Main.ins.prefixBlue + "Du bist §cnicht §7mehr in der §6Warteschlange.");
								Main.ins.utils.getSoundMgr().playSound(p, JSound.NOTE_BASS, 1.0f, 1.0f);
							}
							open(p);
							return;
						}
						/*!Queue Join/Leave*/
						
						/*Auto Queue Toggle*/
						if(s == 18 || s == 19) {
							if(Main.ins.database.isQueuePrefEnabled(p.getUniqueId(), 0) == 0) {
								Main.ins.database.setQueuePref(p.getUniqueId(), 0, 1);
							} else {
								Main.ins.database.setQueuePref(p.getUniqueId(), 0, 0);
							}
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						/*!Auto Queue Toggle*/
						
						/*Kit Toggles*/
						if(s == 3 || s == 12) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 1, 0);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						if(s == 4 || s == 13) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 1, 1);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						if(s == 5 || s == 14) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 1, 2);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						/*!Kit Toggles*/
						
						
						/*Best of Toggles*/
						if(s == 7 || s == 8) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 2, 1);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						if(s == 16 || s == 17) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 2, 2);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						if(s == 25 || s == 26) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 2, 3);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						/*!Best of Toggles*/
						
						/*Ranked Toggles*/
						
						if(s == 21 || s == 30) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 3, 0);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						if(s == 22 || s == 31) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 3, 1);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						if(s == 23 || s == 32) {
							Main.ins.database.setQueuePref(p.getUniqueId(), 3, 2);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							open(p);
							return;
						}
						
						/*!Ranked Toggles*/
						
					}
				}
			}
		}
	}
}
