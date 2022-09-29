/**
 * 
 */
package de.onevsone.listener.Inventories;

import java.util.UUID;

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

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 17:30:18					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class TournamentInfoInv implements Listener {

	public static void open(Player p, UUID tournamentID) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "Turnier§a");
		
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Hier könnte Ihre Werbung stehen!");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§a", null);
		ItemStack infos = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Informationen", builder.toString());
		
		
		inv.setItem(0, infos);
		
		inv.setItem(1, empty);
		inv.setItem(9, empty);
		inv.setItem(10, empty);
		inv.setItem(18, empty);
		inv.setItem(19, empty);
		
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if (Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if (e.getInventory().getName().equalsIgnoreCase("Turnier§a")) {
					e.setCancelled(true);
					
					
				}
			}
		}
	}
	
}
