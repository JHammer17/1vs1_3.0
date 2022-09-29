package de.onevsone.listener;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;

public class KitEditorEvents implements Listener {

	
	private ItemStack lapis;


	public KitEditorEvents() {
		Dye d = new Dye();
	    d.setColor(DyeColor.BLUE);
	    this.lapis = d.toItemStack();
	    this.lapis.setAmount(64);
	}
	
	
	/*
	 * Opens the Trash bin
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INKITEDIT) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getType() == Material.CAULDRON) {
					e.setCancelled(true);
					e.getPlayer().openInventory(Bukkit.createInventory(null, 9*6,"Mülleimer§a"));
				}
			}
		}
		
		
	}
	
	@EventHandler
	public void openInventoryEvent(InventoryOpenEvent e) {
	 /*Verzauberungstisch mit Lapis*/
     if(e.getInventory() instanceof EnchantingInventory){
      if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
    	  e.getInventory().setItem(1, this.lapis);
    	  Main.ins.inventories.add((EnchantingInventory)e.getInventory());
      }
	 }
	}
	  
	@EventHandler
	public void closeInventoryEvent(InventoryCloseEvent e) {
	 /*Lapis aus Verzauberungstisch clearen*/
	 if((e.getInventory() instanceof EnchantingInventory)) {
	  if(Main.ins.inventories.contains((EnchantingInventory)e.getInventory())) {
	   e.getInventory().setItem(1, null);
	   Main.ins.inventories.remove((EnchantingInventory)e.getInventory());
      }
	 }
	}
	
	@EventHandler
	public void onPotionDrink(PlayerItemConsumeEvent e) {
		if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INKITEDIT) {
			if(e.getItem().getType() == Material.POTION || e.getItem().getType() == Material.MILK_BUCKET)
			e.setCancelled(true);
			
		}
	}
	
	@EventHandler
	public void onPotionDrink(PlayerInteractEvent e) {
		if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INKITEDIT) {
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getItem() != null)  {
					if(e.getItem().getType() == Material.MONSTER_EGG || 
					   e.getItem().getType() == Material.MONSTER_EGGS) {
							e.setCancelled(true);
				 }
				}
			}
			
			
			
		}
	}
	
}
