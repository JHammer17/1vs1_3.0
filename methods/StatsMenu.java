package de.onevsone.methods;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.database.sql.Database;
import de.onevsone.enums.OvOColor;
import de.onevsone.objects.FightInfo;
import de.onevsone.objects.OneVsOnePlayer;

public class StatsMenu implements Listener {
	
	
	
	
	
	public static void openStatsMenu(Player p, UUID owner, Inventory inv, boolean fightmenu) {
		if(p == null || (owner == null && !fightmenu)) return;
		
		boolean invGiven = true;
		
		if(inv == null) {
			inv = Bukkit.createInventory(null, 9*6, "Statsmenü§a");
			invGiven = false;
		}
		
		inv.clear();
		
		ItemStack sideBar = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§a", null);
		
		ItemStack up = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 0, 1, "§6▲ Hoch ▲", null);
		
		boolean sMShowMaps = Main.ins.getOneVsOnePlayer(p).isStatsMenuShowMaps();
		
		
		ItemStack showMaps = Main.ins.utils.createItem(Material.INK_SACK, (sMShowMaps ? 10 : 8), 1, "§6Zeige Maps", null);
		ItemStack showWin = Main.ins.utils.createItem(Material.INK_SACK, (sMShowMaps ? 8 : 10), 1, "§6Zeige Gewonnen", null);
		
		
		inv.setItem(8, sideBar);
		inv.setItem(17, sideBar);
		if(!fightmenu) inv.setItem(26, showMaps);
		if(!fightmenu) inv.setItem(35, showWin);
		inv.setItem(44, sideBar);
		inv.setItem(53, sideBar);
		
		if(fightmenu) inv.setItem(26, sideBar);
		if(fightmenu) inv.setItem(35, sideBar);
		
		
		if(Main.ins.getOneVsOnePlayer(p).getStatsMenuOffset() > 0) {
			inv.setItem(8, up);
		}
		
		final Inventory fInv = inv;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				ItemStack down = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 0, 1, "§6▼ Runter ▼", null);
				
				
				OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
				
				
				
				
				
				int slot = 0;
				ArrayList<FightInfo> infos = new ArrayList<>();
				
				if(!fightmenu) {
					infos.addAll(Database.getAllFightsForUUID(owner, player.getStatsMenuOffset()));
				} else {
					infos.addAll(Database.getAllFights(player.getStatsMenuOffset()));
				}

				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
				
				for(FightInfo info : infos) {
					
					Date time = new Date(info.getTime());
					
					StringBuilder loreBuilder = new StringBuilder();
					
					loreBuilder.append("\n");
					
					
					OvOColor colorP1 = OvOColor.RED;
					try {
						colorP1 = OvOColor.valueOf(info.getColorPos1());
					} catch (IllegalArgumentException e) {}
					OvOColor colorP2 = OvOColor.BLUE;
					try {
						colorP2 = OvOColor.valueOf(info.getColorPos2());
					} catch (IllegalArgumentException e) {}
					

					int hId = 0;
					ArrayList<Float> health1 = info.getPos1Health();
					ArrayList<Float> health2 = info.getPos2Health();
					
					for(String name : info.getPos1Names()) {
						
						float health = 0.0f;
						
						if(health1.size() >= hId+1) {
							health = health1.get(hId);
						}
						
						if(info.isPos1Wins()) {
							loreBuilder.append(colorP1.colorKey() + "§n" + name + "§r §7(§c" + health + "❤§7)\n");
						} else {
							loreBuilder.append(colorP1.colorKey() + name + "§r §7(§c" + health + "❤§7)\n");
						}
						hId++;
					}
					hId = 0;
					loreBuilder.append(" §f§lvs\n");
					
					for(String name : info.getPos2Names()) {
						
						float health = 0.0f;
						
						if(health2.size() >= hId+1) {
							health = health2.get(hId);
						}
						
						if(!info.isPos1Wins()) {
							loreBuilder.append(colorP2.colorKey() + "§n" + name + "§r §7(§c" + health + "❤§7)\n");
						} else {
							loreBuilder.append(colorP2.colorKey() + name + "§r §7(§c" + health + "❤§7)\n");
						}
						hId++;
					}
					
					loreBuilder.append("\n\n");
					if(fightmenu) loreBuilder.append("§7Arena: §6" + info.getArena() + "\n");
					loreBuilder.append("§7Map: §6" + info.getMap() + "\n\n");
					loreBuilder.append("§7Dauer: §6" + Main.ins.utils.getFormattedTime(info.getDuration()) + "\n");
					loreBuilder.append("§7Kit: §6" + info.getKit());
					if(info.isRanked()) loreBuilder.append("\n§6-*-*-*- §6§lRANKED -*-*-*- ");
					
					int id = 1;
					int subId = info.getSubID();
					
					
					try {
						id = Integer.parseInt(info.getItemName());
					} catch (NumberFormatException e) {subId = 0;}
					
					if(!player.isStatsMenuShowMaps()) {
						subId = 0;
						if(info.isPos1Wins() && info.getPos1List().contains(owner)) id = 133;
						else if(!info.isPos1Wins() && info.getPos2List().contains(owner)) id = 133;
						else id = 152;
					}
					
					
					
					
					ItemStack display = Main.ins.utils.createItem(id, (player.isStatsMenuShowMaps() ? subId : 0), 1, "§6" + format.format(time) + " Uhr", loreBuilder.toString(), ItemFlag.HIDE_ENCHANTS);
					
					if(info.isRanked()) Main.ins.utils.applyEnchant(display, Enchantment.ARROW_DAMAGE, 1);
					
					if(fInv != null && slot < 53)fInv.setItem(slot, display);
					
					
					slot++;
					if(slot == 8 || 
					   slot == 17 || 
					   slot == 26 || 
					   slot == 35 || 
					   slot == 44) slot++;
					if(slot > 53) {
						fInv.setItem(53, down);
						break;
					}
				}
				
				
				
			}
		}.runTaskAsynchronously(Main.ins);
		
		if(!fightmenu) Main.ins.getOneVsOnePlayer(p).setStatsMenuOwner(owner);
		
		Main.ins.getOneVsOnePlayer(p).setFightsMenu(fightmenu);
		
		if(!invGiven) p.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		
		if(e.getInventory() != null && e.getClickedInventory() != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("Statsmenü§a")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("Statsmenü§a")) {
					if(e.getWhoClicked() instanceof Player) {
						Player p = (Player)e.getWhoClicked();
						
						if(e.getSlot() == 8) {
							if(e.getCurrentItem() != null && e.getCurrentItem().getType() ==  Material.STAINED_GLASS_PANE && e.getCurrentItem().getDurability() == 0) {
								Main.ins.getOneVsOnePlayer(p).setStatsMenuOffset(Main.ins.getOneVsOnePlayer(p).getStatsMenuOffset()-1);
								openStatsMenu(p, Main.ins.getOneVsOnePlayer(p).getStatsMenuOwner(), e.getClickedInventory(), Main.ins.getOneVsOnePlayer(p).isFightMenu());
								
							}
						} else if(e.getSlot() == 53) {
							if(e.getCurrentItem() != null && e.getCurrentItem().getType() ==  Material.STAINED_GLASS_PANE && e.getCurrentItem().getDurability() == 0) {
								Main.ins.getOneVsOnePlayer(p).setStatsMenuOffset(Main.ins.getOneVsOnePlayer(p).getStatsMenuOffset()+1);
								openStatsMenu(p, Main.ins.getOneVsOnePlayer(p).getStatsMenuOwner(), e.getClickedInventory(), Main.ins.getOneVsOnePlayer(p).isFightMenu());	
							}
						} else
						
						if(!Main.ins.getOneVsOnePlayer(p).isFightMenu()) {
							if(e.getSlot() == 26) {
								if(e.getCurrentItem() != null && e.getCurrentItem().getType() ==  Material.INK_SACK) {
									Main.ins.getOneVsOnePlayer(p).setStatsMenuShowMaps(true);
									openStatsMenu(p, Main.ins.getOneVsOnePlayer(p).getStatsMenuOwner(), e.getClickedInventory(), false);
									
								}
							} else if(e.getSlot() == 35) {
								if(e.getCurrentItem() != null && e.getCurrentItem().getType() ==  Material.INK_SACK) {
									Main.ins.getOneVsOnePlayer(p).setStatsMenuShowMaps(false);
									openStatsMenu(p, Main.ins.getOneVsOnePlayer(p).getStatsMenuOwner(), e.getClickedInventory(), false);	
								}
							}
						}
							
						
						
						
					}
				}
				
			}
		}
	}
	
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getTitle().equalsIgnoreCase("Statsmenü§a")) {
			Main.ins.getOneVsOnePlayer((Player) e.getPlayer()).resetStatMenuSettings();
		}
	}
	
}
