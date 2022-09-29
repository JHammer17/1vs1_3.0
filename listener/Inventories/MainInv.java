package de.onevsone.listener.Inventories;

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
import de.onevsone.listener.Inventories.subInvs.ColorSettingsInv;
import de.onevsone.listener.Inventories.subInvs.KitMainInv;
import de.onevsone.listener.Inventories.subInvs.MapsInv;
import de.onevsone.listener.Inventories.subInvs.QueueInv;

public class MainInv implements Listener {

	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "Einstellungen§a");
		
		ItemStack kitSettings = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Kits", null);
		ItemStack queueSettings = Main.ins.utils.createItem(Material.DIAMOND_SWORD, 0, 1, "§6Warteschlange", null);
		ItemStack disabledMapsSettings = Main.ins.utils.createItem(Material.PAPER, 0, 1, "§6Maps", null);
		ItemStack otherSettings = Main.ins.utils.createItem(Material.FIREWORK_CHARGE, 0, 1, "§6Farbauswahl", null);
		
		inv.setItem(10, kitSettings);
		inv.setItem(12, queueSettings);
		inv.setItem(14, disabledMapsSettings);
		inv.setItem(16, otherSettings);
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(e.getInventory().getName().equalsIgnoreCase("Einstellungen§a")) {
					e.setCancelled(true);
					if(e.getClickedInventory() != null && e.getClickedInventory().getName().equalsIgnoreCase("Einstellungen§a")) {
						
						e.setCancelled(true);
						
						if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
							return;
						}
						Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
						
						
						if(e.getSlot() == 10) {
							KitMainInv.open(p);
						} else if(e.getSlot() == 12) {
							QueueInv.open(p);
						} else if(e.getSlot() == 14) {
							MapsInv.open(p, null);
						} else if(e.getSlot() == 16) {
							ColorSettingsInv.open(p);
						}
						
						
					}
				}
			}
		}
	}
	
}
