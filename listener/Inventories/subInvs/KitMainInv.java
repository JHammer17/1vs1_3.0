package de.onevsone.listener.Inventories.subInvs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.onevsone.Main;
import de.onevsone.listener.Inventories.MainInv;
import de.onevsone.objects.OneVsOnePlayer;

public class KitMainInv implements Listener {

	public static void open(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*6, "Einstellungen§b");
		
		ItemStack changeNameSlot1 = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Slot 1: §7" + Main.ins.database.getKitName(p.getUniqueId(), 1), null);
		ItemStack changeNameSlot2 = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Slot 2: §7" + Main.ins.database.getKitName(p.getUniqueId(), 2), null);
		ItemStack changeNameSlot3 = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Slot 3: §7" + Main.ins.database.getKitName(p.getUniqueId(), 3), null);
		ItemStack changeNameSlot4 = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Slot 4: §7" + Main.ins.database.getKitName(p.getUniqueId(), 4), null);
		ItemStack changeNameSlot5 = Main.ins.utils.createItem(Material.SIGN, 0, 1, "§6Slot 5: §7" + Main.ins.database.getKitName(p.getUniqueId(), 5), null);
		
		ItemStack settings = Main.ins.utils.createItem(Material.IRON_PLATE, 0, 1, "§6§lEinstellungen", null);
		
		ItemStack enabled = Main.ins.utils.createItem(Material.INK_SACK, 10, 1, "§a§lAktiv", null);
		ItemStack disabled = Main.ins.utils.createItem(Material.INK_SACK, 8, 1, "§c§lDeaktiviert", null);
		ItemStack out = Main.ins.utils.createItem(Material.BARRIER, 0, 1, "§c§lDeaktiviert", null);
		
		inv.setItem(11, changeNameSlot1);
		inv.setItem(12, changeNameSlot2);
		inv.setItem(13, changeNameSlot3);
		inv.setItem(14, changeNameSlot4);
		inv.setItem(15, changeNameSlot5);
		
		inv.setItem(20, settings);
		inv.setItem(21, settings);
		inv.setItem(22, settings);
		inv.setItem(23, settings);
		inv.setItem(24, settings);
		
		int selectedSlot = Main.ins.database.getSelectedKit(p.getUniqueId());
		
		if(selectedSlot <= 1) inv.setItem(29, enabled);
		else inv.setItem(29, disabled);
		
		if(p.hasPermission("1vs1.Kits.hasKit2")) {
			if(selectedSlot == 2) inv.setItem(30, enabled);
			else inv.setItem(30, disabled);
		} else {
			inv.setItem(30, out);
		}
		
		if(p.hasPermission("1vs1.Kits.hasKit3")) {
			if(selectedSlot == 3) inv.setItem(31, enabled);
			else inv.setItem(31, disabled);
		} else {
			inv.setItem(31, out);
		}
		
		if(p.hasPermission("1vs1.Kits.hasKit4")) {
			if(selectedSlot == 4) inv.setItem(32, enabled);
			else inv.setItem(32, disabled);
		} else {
			inv.setItem(32, out);
		}
		
		if(p.hasPermission("1vs1.Kits.hasKit5")) {
			if(selectedSlot == 5) inv.setItem(33, enabled);
			else inv.setItem(33, disabled);
		} else {
			inv.setItem(33, out);
		}
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(e.getInventory().getName().equalsIgnoreCase("Einstellungen§b")) {
					e.setCancelled(true);
					if(e.getClickedInventory() != null && e.getClickedInventory().getName().equalsIgnoreCase("Einstellungen§b")) {
						
						e.setCancelled(true);
						
						if(e.isRightClick()) {
							MainInv.openInv(p);
							return;
						}
						
						if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
							return;
						}
						Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
						
						
						/*Kit Rename*/
						
						if(e.getSlot() == 11) {
							OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
							
							if(player.isIn1vs1()) {
								p.closeInventory();
								p.sendMessage(Main.ins.prefixYellow + "Schreibe nun den Namen für dein 1. Kit in den Chat!");
								player.setInput(true);
								player.setInputReason("KitRename1");;
							}
							
						} else if(e.getSlot() == 12) {
							if(p.hasPermission("1vs1.Kits.hasKit2")) {
								OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
								
								if(player.isIn1vs1()) {
									p.closeInventory();
									p.sendMessage(Main.ins.prefixYellow + "Schreibe nun den Namen für dein 2. Kit in den Chat!");
									player.setInput(true);
									player.setInputReason("KitRename2");;
								}
							}
						} else if(e.getSlot() == 13) {
							if(p.hasPermission("1vs1.Kits.hasKit3")) {
								OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
								
								if(player.isIn1vs1()) {
									p.closeInventory();
									p.sendMessage(Main.ins.prefixYellow + "Schreibe nun den Namen für dein 3. Kit in den Chat!");
									player.setInput(true);
									player.setInputReason("KitRename3");;
								}
							}
						} else if(e.getSlot() == 14) {
							if(p.hasPermission("1vs1.Kits.hasKit4")) {
								OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
								
								if(player.isIn1vs1()) {
									p.closeInventory();
									p.sendMessage(Main.ins.prefixYellow + "Schreibe nun den Namen für dein 4. Kit in den Chat!");
									player.setInput(true);
									player.setInputReason("KitRename4");;
								}
							}
						} else if(e.getSlot() == 15) {
							if(p.hasPermission("1vs1.Kits.hasKit5")) {
								OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
								
								if(player.isIn1vs1()) {
									p.closeInventory();
									p.sendMessage(Main.ins.prefixYellow + "Schreibe nun den Namen für dein 5. Kit in den Chat!");
									player.setInput(true);
									player.setInputReason("KitRename5");;
								}
							}
						} 
						/*!Kit Rename*/
						
						/*Kitsettingsauswahl*/
						if(e.getSlot() == 20) {
							KitSubInv.openSettingsInv(p, 1);
						} else if(e.getSlot() == 21) {
							if(p.hasPermission("1vs1.Kits.hasKit2")) KitSubInv.openSettingsInv(p, 2);
						} else if(e.getSlot() == 22) {
							if(p.hasPermission("1vs1.Kits.hasKit3")) KitSubInv.openSettingsInv(p, 3);
						} else if(e.getSlot() == 23) {
							if(p.hasPermission("1vs1.Kits.hasKit4")) KitSubInv.openSettingsInv(p, 4);
						} else if(e.getSlot() == 24) {
							if(p.hasPermission("1vs1.Kits.hasKit5")) KitSubInv.openSettingsInv(p, 5);
						}
						/*!Kitsettingsauswahl*/
						
						/*KitSelect*/
						if(e.getSlot() == 29) {
							Main.ins.database.setSelectedKit(p.getUniqueId(), 1);
							KitMainInv.open(p);
						} else if(e.getSlot() == 30) {
							if(p.hasPermission("1vs1.Kits.hasKit2")) {
								Main.ins.database.setSelectedKit(p.getUniqueId(), 2);
							 KitMainInv.open(p);
							}
						} else if(e.getSlot() == 31) {
							if(p.hasPermission("1vs1.Kits.hasKit3")) {
								Main.ins.database.setSelectedKit(p.getUniqueId(), 3);
							 KitMainInv.open(p);
							}
						} else if(e.getSlot() == 32) {
							if(p.hasPermission("1vs1.Kits.hasKit4")) {
								Main.ins.database.setSelectedKit(p.getUniqueId(), 4);
							 KitMainInv.open(p);
							}
						} else if(e.getSlot() == 33) {
							if(p.hasPermission("1vs1.Kits.hasKit5")) {
							 Main.ins.database.setSelectedKit(p.getUniqueId(), 5);
							 KitMainInv.open(p);
							}
						}
						/*!KitSelect*/
						
						
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onAsyncChat(AsyncPlayerChatEvent e) {
		
		if(Main.ins.getOneVsOnePlayer(e.getPlayer()).isIn1vs1()) {
			Player p = e.getPlayer();
			OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
			
			if(player.isInput()) {
				if(player.getInputReason().contains("KitRename")) {
					
					if(e.getMessage().length() >= 30) {
						e.setCancelled(true);
						p.sendMessage(Main.ins.prefixRed + "§cDieser Name ist zu Lang! (Name < 30 Zeichen)");
						player.setInput(false);
						player.setInputReason(null);
						return;
					}
					
					
					
					if(player.getInputReason().equalsIgnoreCase("KitRename1")) {
						Main.ins.database.setKitName(p.getUniqueId(), 1, e.getMessage());
					} else if(player.getInputReason().equalsIgnoreCase("KitRename2")) {
						Main.ins.database.setKitName(p.getUniqueId(), 2, e.getMessage());
					} else if(player.getInputReason().equalsIgnoreCase("KitRename3")) {
						Main.ins.database.setKitName(p.getUniqueId(), 3, e.getMessage());
					} else if(player.getInputReason().equalsIgnoreCase("KitRename4")) {
						Main.ins.database.setKitName(p.getUniqueId(), 4, e.getMessage());
					} else if(player.getInputReason().equalsIgnoreCase("KitRename5")) {
						Main.ins.database.setKitName(p.getUniqueId(), 5, e.getMessage());
					} 
					e.setCancelled(true);
					player.setInput(false);
					player.setInputReason(null);
					p.sendMessage(Main.ins.prefixGreen + "§aKit Name erfolgreich geändert!");
					
					open(p);
					
				}
			}
			
		}
	}
	
	
}
