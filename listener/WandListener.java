package de.onevsone.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import de.onevsone.Main;

public class WandListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getItem() != null) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK || 
			   e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(Main.ins.getOneVsOnePlayer(e.getPlayer()).isEditMode()) {
					if(e.getItem().getTypeId() == Main.ins.wandID && 
					   e.getItem().getDurability() == Main.ins.wandSubID) {
						
						e.setCancelled(true);
						
						if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							Main.ins.getOneVsOnePlayer(e.getPlayer()).setPos2(e.getClickedBlock().getLocation());
							e.getPlayer().sendMessage(Main.ins.prefixBlue + "Position 2 gesetzt!");
						} else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
							Main.ins.getOneVsOnePlayer(e.getPlayer()).setPos1(e.getClickedBlock().getLocation());
							e.getPlayer().sendMessage(Main.ins.prefixBlue + "Position 1 gesetzt!");
						}
						
					}
					
				}
				
			}	
		}
		
	}
	
}
