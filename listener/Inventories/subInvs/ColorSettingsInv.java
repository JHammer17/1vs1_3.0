/**
 * 
 */
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
import de.onevsone.enums.OvOColor;
import de.onevsone.listener.Inventories.MainInv;
import de.onevsone.methods.SoundMgr.JSound;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 25.03.2018 19:57:14					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class ColorSettingsInv implements Listener {

	public static void open(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "Farbauswahl§a");
		
		ItemStack next = Main.ins.utils.createItem(Material.STONE_BUTTON, 0, 1, "§6Nächste Farbe", null);
		ItemStack last = Main.ins.utils.createItem(Material.STONE_BUTTON, 0, 1, "§6Vorherige Farbe", null);
		
		
		
		int colorPos1 = Main.ins.database.getColor(p.getUniqueId(), true, false);
		int colorPos2 = Main.ins.database.getColor(p.getUniqueId(), false, false);
		int colorPos1Alt = Main.ins.database.getColor(p.getUniqueId(), true, true);
		int colorPos2Alt = Main.ins.database.getColor(p.getUniqueId(), false, true);
		
		
		
		
		ItemStack colorPos1Item = Main.ins.utils.createItem(Material.INK_SACK, OvOColor.resolveBySubID(colorPos1).getSubID(), 1, "§7Farbe Position 1: " + OvOColor.resolveBySubID(colorPos1).colorKey() + OvOColor.resolveBySubID(colorPos1).getName(), null);
		ItemStack colorPos2Item = Main.ins.utils.createItem(Material.INK_SACK, OvOColor.resolveBySubID(colorPos2).getSubID(), 1, "§7Farbe Position 2: " + OvOColor.resolveBySubID(colorPos2).colorKey() + OvOColor.resolveBySubID(colorPos2).getName(), null);
		ItemStack colorPos1AltItem = Main.ins.utils.createItem(Material.INK_SACK, OvOColor.resolveBySubID(colorPos1Alt).getSubID(), 1, "§7Alternative Farbe Position 1: " +OvOColor.resolveBySubID(colorPos1Alt).colorKey() + OvOColor.resolveBySubID(colorPos1Alt).getName(), null);
		ItemStack colorPos2AltItem = Main.ins.utils.createItem(Material.INK_SACK, OvOColor.resolveBySubID(colorPos2Alt).getSubID(), 1, "§7Alternative Farbe Position 2: " + OvOColor.resolveBySubID(colorPos2Alt).colorKey() + OvOColor.resolveBySubID(colorPos2Alt).getName(), null);
		
		if(colorPos1 >= 16 || colorPos1 < 0) colorPos1Item = Main.ins.utils.createItem(Material.NETHER_STAR, 0, 1, "§6" + OvOColor.resolveBySubID(colorPos1).getName(), null);
		if(colorPos2 >= 16 || colorPos2 < 0) colorPos2Item = Main.ins.utils.createItem(Material.NETHER_STAR, 0, 1, "§6" + OvOColor.resolveBySubID(colorPos2).getName(), null);
		if(colorPos1Alt >= 16 || colorPos1Alt < 0) colorPos1AltItem = Main.ins.utils.createItem(Material.NETHER_STAR, 0, 1, "§6" + OvOColor.resolveBySubID(colorPos1Alt).getName(), null);
		if(colorPos2Alt >= 16 || colorPos2Alt < 0) colorPos2AltItem = Main.ins.utils.createItem(Material.NETHER_STAR, 0, 1, "§6" + OvOColor.resolveBySubID(colorPos2Alt).getName(), null);
		
		
		inv.setItem(2, next);
		inv.setItem(3, next);
		
		
		
		inv.setItem(20, last);
		inv.setItem(21, last);
		
		
		
		
		if(colorPos1 >= 16) {
			colorPos1AltItem = Main.ins.utils.createItem(Material.BARRIER, 0, 1, "§cDeaktiviert", null);
		} else {
			inv.setItem(5, next);
			inv.setItem(23, last);
		}
		if(colorPos2 >= 16) {
			colorPos2AltItem = Main.ins.utils.createItem(Material.BARRIER, 0, 1, "§cDeaktiviert", null);
		} else {
			inv.setItem(6, next);
			inv.setItem(24, last);
		}
		
		
		inv.setItem(11, colorPos1Item);
		inv.setItem(12, colorPos2Item);
		
		inv.setItem(14, colorPos1AltItem);
		inv.setItem(15, colorPos2AltItem);
		
		
		
		p.openInventory(inv);
	 
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(e.getClickedInventory() != null && e.getInventory().getName().equalsIgnoreCase("Farbauswahl§a")) {
					e.setCancelled(true);
					if(e.getClickedInventory().getName().equalsIgnoreCase("Farbauswahl§a")) {
						
						if(e.isRightClick()) {
							MainInv.openInv(p);
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 1.0f, 1.0f);
							return;
						}
						
						if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
							return;
						}
						
						Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
						
						if(e.getCurrentItem() == null) return;
						
						int slot = e.getSlot();
						
						
						/*Normal Position 1*/
						if(slot == 2) {
							int color = Main.ins.database.getColor(p.getUniqueId(), true, false);
							color++;
							if(color > 16 || color < 0) color = 0;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), true, true);
							if(color == altColor) color++;
							if(color > 16 || color < 0) color = 0;
							
							
							
							Main.ins.database.setColor(p.getUniqueId(), true, false, OvOColor.resolveBySubID(color));
							open(p);
						} else if(slot == 20) {
							int color = Main.ins.database.getColor(p.getUniqueId(), true, false);
							color--;
							if(color > 16 || color < 0) color = 16;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), true, true);
							if(color == altColor) color--;
							if(color > 16 || color < 0) color = 16;							
							
							
							
							Main.ins.database.setColor(p.getUniqueId(), true, false, OvOColor.resolveBySubID(color));
							open(p);
						}
						
						/*Normal Position 2*/
						if(slot == 3) {
							int color = Main.ins.database.getColor(p.getUniqueId(), false, false);
							color++;
							if(color > 16 || color < 0) color = 0;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), false, true);
							if(color == altColor) color++;
							if(color > 16 || color < 0) color = 0;	
							
							Main.ins.database.setColor(p.getUniqueId(), false, false, OvOColor.resolveBySubID(color));
							open(p);
						} else if(slot == 21) {
							int color = Main.ins.database.getColor(p.getUniqueId(), false, false);
							color--;
							if(color > 16 || color < 0) color = 16;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), false, true);
							if(color == altColor) color--;
							if(color > 16 || color < 0) color = 16;
							
							Main.ins.database.setColor(p.getUniqueId(), false, false, OvOColor.resolveBySubID(color));
							open(p);
						}
						
						/*Alternativ Position 1*/
						if(slot == 5) {
							int color = Main.ins.database.getColor(p.getUniqueId(), true, true);
							color++;
							if(color > 16 || color < 0) color = 0;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), true, false);
							if(color == altColor) color++;
							if(color > 16 || color < 0) color = 0;
							
							
							Main.ins.database.setColor(p.getUniqueId(), true, true, OvOColor.resolveBySubID(color));
							open(p);
						} else if(slot == 23) {
							int color = Main.ins.database.getColor(p.getUniqueId(), true, true);
							color--;
							if(color > 16 || color < 0) color = 16;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), true, false);
							if(color == altColor) color--;
							if(color > 16 || color < 0) color = 16;
							
							Main.ins.database.setColor(p.getUniqueId(), true, true, OvOColor.resolveBySubID(color));
							open(p);
						}
						
						/*Alternativ Position 2*/
						if(slot == 6) {
							int color = Main.ins.database.getColor(p.getUniqueId(), false, true);
							color++;
							if(color > 16 || color < 0) color = 0;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), false, false);
							if(color == altColor) color++;
							if(color > 16 || color < 0) color = 0;
							
							
							Main.ins.database.setColor(p.getUniqueId(), false, true, OvOColor.resolveBySubID(color));
							open(p);
						} else if(slot == 24) {
							int color = Main.ins.database.getColor(p.getUniqueId(), false, true);
							color--;
							if(color > 16 || color < 0) color = 16;
							int altColor = Main.ins.database.getColor(p.getUniqueId(), false, false);
							if(color == altColor) color--;
							if(color > 16 || color < 0) color = 16;
							
							Main.ins.database.setColor(p.getUniqueId(), false, true, OvOColor.resolveBySubID(color));
							open(p);
						}
						
						
					}
				}
			}
		}
	}
	
}
