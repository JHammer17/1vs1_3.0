package de.onevsone.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.Inventories.MainInv;

public class LobbySettingsItemMgr implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).isIn1vs1()) {
				if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INLOBBY) {
					if(e.getItem() != null && 
							e.getItem().getType() == Material.REDSTONE_COMPARATOR && 
							e.getItem().hasItemMeta() && 
							e.getItem().getItemMeta().hasDisplayName() && 
							e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Einstellungen §7(/settings)")) {
						e.setCancelled(true);
						
						MainInv.openInv(e.getPlayer());
					}
				}
			}
		}
	}
	
	
	
	
	
	
}
