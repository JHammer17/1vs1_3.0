package de.onevsone.listener.Inventories.subInvs;

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

public class KitSubInv implements Listener {

	public static void openSettingsInv(Player p, int kitId) {
		Inventory inv = Bukkit.createInventory(null, 9*4, "Einstellungen§c");
		
		Main.ins.getOneVsOnePlayer(p).setPreferencesInv(kitId);
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§a", null);
		
		inv.setItem(4, empty);
		inv.setItem(13, empty);
		inv.setItem(22, empty);
		inv.setItem(31, empty);
		
		ItemStack disabled = Main.ins.utils.createItem(Material.INK_SACK, 8, 1, "§c§lDeaktiviert", null);
		ItemStack enabled = Main.ins.utils.createItem(Material.INK_SACK, 10, 1, "§a§lAktiv", null);
		
		
		ItemStack build = Main.ins.utils.createItem(Material.IRON_PICKAXE, 0, 1, "§6Nicht bauen", "\n§a§lWenn aktiv:\n§r§7Man kann mit deinen Kit in\n§7der Arena nicht bauen.");
		ItemStack starve = Main.ins.utils.createItem(Material.COOKED_BEEF, 0, 1, "§6Hunger", "\n§a§lWenn aktiv:\n§r§7Du verlierst ganz normal Hunger.");
		ItemStack noExplosionDmg = Main.ins.utils.createItem(Material.PRISMARINE_SHARD, 0, 1, "§6Kein Explosions-Block-Schaden", "\n§a§lWenn aktiv:\n§r§7Explosionen (Creeper, TnT, etc.)\n§7machen keinen Schaden mehr an der Umwelt.");
		ItemStack instantTnt = Main.ins.utils.createItem(Material.TNT, 0, 1, "§6Instant TNT", "\n§a§lWenn aktiv:\n§r§7Platziertes TNT wird sofort gezündet.");
		ItemStack noCrafting = Main.ins.utils.createItem(Material.WORKBENCH, 0, 1, "§6Kein Craften", "\n§a§lWenn aktiv:\n§r§7Du kannst nichts mehr craften.");
		ItemStack soupHeal = Main.ins.utils.createItem(Material.MUSHROOM_SOUP, 0, 1, "§6Suppenheilung", "\n§a§lWenn aktiv:\n§r§7Mit einen Rechtsklick mit\n§7einer Pilzsuppe erhältst du 3.5 Herzen.");
		ItemStack noFriendlyFire = Main.ins.utils.createItem(Material.BLAZE_POWDER, 0, 1, "§6Kein Friendly Fire", "\n§a§lWenn aktiv:\n§r§7Bei einem Teamkampf\n§7kannst du deinen Teamkollegen\n§7Schaden zufügen.");
		ItemStack noItemDrops = Main.ins.utils.createItem(Material.BARRIER, 0, 1, "§6Kein Item Drops", "\n§a§lWenn aktiv:\n§r§7Beim Tod wird das\n§7Inventar nciht gedroppt\n§7(Nur bei Teamkämpfen\n§7interessant).");
		ItemStack waterDmg = Main.ins.utils.createItem(Material.WATER_BUCKET, 0, 1, "§6Schaden im Wasser", "\n§a§lWenn aktiv:\n§r§7Du bekommst im Wasser\n§7jede Sekunde 1.5 Herzen Schaden.");
		ItemStack noNaturalReg = Main.ins.utils.createItem(Material.POTION, 8193, 1, "§6Keine natürliche Regeneration", "\n§a§lWenn aktiv:\n§r§7Du regenerierst nicht mehr natürlich.\n§7Tränke und Goldäpfel lassen dich\n§7dennoch regenerieren.");
		ItemStack soupNoob = Main.ins.utils.createItem(Material.BOWL, 0, 1, "§6Soup-Noob", "\n§a§lWenn aktiv:\n§r§7Du kannst keine Schwerter\n§7oder Pilzsuppen mehr droppen.");
		ItemStack noFallDmg = Main.ins.utils.createItem(Material.IRON_BOOTS, 0, 1, "§6Kein Fallschaden", "\n§a§lWenn aktiv:\n§r§7Du erhältst keinen Fallschaden.");
		ItemStack noArrowPickup = Main.ins.utils.createItem(Material.ARROW, 0, 1, "§6Keine Pfeile aufheben", "\n§a§lWenn aktiv:\n§r§7Pfeile, die auf den Boden\n§7aufkommen verschwinden sofort.");
		ItemStack noHitDelay = Main.ins.utils.createItem(Material.SUGAR, 0, 1, "§6Kein Hit-Delay", "\n§a§lWenn aktiv:\n§r§7Normalerweise kann ein Spieler nur zweimal\n§7pro Sekunde Schaden bekommen.\n§7Diese Mechanik wird hiermit aufgehoben.");
		ItemStack doubleJump = Main.ins.utils.createItem(Material.FIREWORK, 0, 1, "§6Doppelsprung", "\n§a§lWenn aktiv:\n§r§7Wenn du zweimal hintereinander Springst,\n§7springst du doppelt so hoch.");
		
		inv.setItem(0, build);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 0, kitId)) inv.setItem(1, enabled);
		else inv.setItem(1, disabled);
		
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 1, kitId)) inv.setItem(2, enabled);
		else inv.setItem(2, disabled);
		inv.setItem(3, starve);
		
		inv.setItem(5, noFallDmg);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 2, kitId)) inv.setItem(6, enabled);
		else inv.setItem(6, disabled);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 3, kitId)) inv.setItem(7, enabled);
		else inv.setItem(7, disabled);
		inv.setItem(8, noArrowPickup);
		
		inv.setItem(9, instantTnt);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 4, kitId)) inv.setItem(10, enabled);
		else inv.setItem(10, disabled);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 5, kitId)) inv.setItem(11, enabled);
		else inv.setItem(11, disabled);
		inv.setItem(12, noExplosionDmg);
		
		inv.setItem(14, soupNoob);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 6, kitId)) inv.setItem(15, enabled);
		else inv.setItem(15, disabled);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 7, kitId)) inv.setItem(16, enabled);
		else inv.setItem(16, disabled);
		inv.setItem(17, noHitDelay);
		
		inv.setItem(18, noCrafting);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 8, kitId)) inv.setItem(19, enabled);
		else inv.setItem(19, disabled);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 9, kitId)) inv.setItem(20, enabled);
		else inv.setItem(20, disabled);
		inv.setItem(21, soupHeal);
		
		inv.setItem(23, noNaturalReg);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 10, kitId)) inv.setItem(24, enabled);
		else inv.setItem(24, disabled);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 11, kitId)) inv.setItem(25, enabled);
		else inv.setItem(25, disabled);
		inv.setItem(26, doubleJump);
		
		
		inv.setItem(27, noItemDrops);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 12, kitId)) inv.setItem(28, enabled);
		else inv.setItem(28, disabled);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 13, kitId)) inv.setItem(29, enabled);
		else inv.setItem(29, disabled);
		inv.setItem(30, noFriendlyFire);
		
		inv.setItem(32, waterDmg);
		if(Main.ins.database.isKitSettingEnabled(p.getUniqueId(), 14, kitId)) inv.setItem(33, enabled);
		else inv.setItem(33, disabled);
	
		
		
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if(Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if(e.getInventory().getName().equalsIgnoreCase("Einstellungen§c")) {
					e.setCancelled(true);
					if(e.getClickedInventory() != null && e.getClickedInventory().getName().equalsIgnoreCase("Einstellungen§c")) {
						
						if(e.isRightClick()) {
							KitMainInv.open(p);
							return;
						}
						
						if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
							return;
						}
						Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
						
						
						int prefInv = Main.ins.getOneVsOnePlayer(p).getPreferencesInv();
						
						if(e.getSlot() == 0 || e.getSlot() == 1) 
							togglePref(p.getUniqueId(), 0, prefInv);
						
						
						if(e.getSlot() == 2 || e.getSlot() == 3) 
							togglePref(p.getUniqueId(), 1, prefInv);
						
						
						if(e.getSlot() == 5 || e.getSlot() == 6) 
							togglePref(p.getUniqueId(), 2, prefInv);
						
					
						if(e.getSlot() == 7 || e.getSlot() == 8) 
							togglePref(p.getUniqueId(), 3, prefInv);
						
						//-----
						
						if(e.getSlot() == 9 || e.getSlot() == 10) 
							togglePref(p.getUniqueId(), 4, prefInv);
						
						if(e.getSlot() == 11 || e.getSlot() == 12) 
							togglePref(p.getUniqueId(), 5, prefInv);
						
						if(e.getSlot() == 14 || e.getSlot() == 15) 
							togglePref(p.getUniqueId(), 6, prefInv);
						
						if(e.getSlot() == 16 || e.getSlot() == 17) 
							togglePref(p.getUniqueId(), 7, prefInv);
						
						//-----
						
						if(e.getSlot() == 18 || e.getSlot() == 19) 
							togglePref(p.getUniqueId(), 8, prefInv);
						
						if(e.getSlot() == 20 || e.getSlot() == 21) 
							togglePref(p.getUniqueId(), 9, prefInv);
						
						if(e.getSlot() == 23 || e.getSlot() == 24) 
							togglePref(p.getUniqueId(), 10, prefInv);
						
						if(e.getSlot() == 25 || e.getSlot() == 26) 
							togglePref(p.getUniqueId(), 11, prefInv);
						
						//-----
						
						if(e.getSlot() == 27 || e.getSlot() == 28) 
							togglePref(p.getUniqueId(), 12, prefInv);
						
						if(e.getSlot() == 29 || e.getSlot() == 30) 
							togglePref(p.getUniqueId(), 13, prefInv);
						
						if(e.getSlot() == 32 || e.getSlot() == 33) 
							togglePref(p.getUniqueId(), 14, prefInv);
						
						openSettingsInv(p, prefInv);
					}
				}
			}
		}
	}
	
	public static void togglePref(UUID uuid, int pref, int kitID) {
		if(Main.ins.database.isKitSettingEnabled(uuid, pref, kitID)) 
			Main.ins.database.setKitPref(uuid, pref, false, kitID);
		else Main.ins.database.setKitPref(uuid, pref, true, kitID);
		
	}
	
}
