package de.onevsone.methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import de.onevsone.Main;

public class BlackDealerInvCreator {
	
	public static void createInv(Player p) {
		
		
		if(p.getItemInHand() == null) return;
		
		ItemStack item = p.getItemInHand();
		
		Inventory inv = Bukkit.createInventory(null, 9*3,"Schwarzhändler");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		ItemStack amount = Main.ins.utils.createItem(Material.MELON_SEEDS, 0, item.getAmount(), "§6Anzahl: " + item.getAmount(), null);
		//ItemStack rename = Main.ins.utils.createItem(Material.SIGN, 0,1, "§cNamen ändern", null);
		ItemStack enchants = Main.ins.utils.createItem(Material.ENCHANTMENT_TABLE, 0, 1, "§6Verzaubern", null);
		ItemStack unbreakable = Main.ins.utils.createItem(Material.DIAMOND, 0, 1, "§6(Un)Zerstörbar machen", "§7Das Item kann nicht mehr zerstört werden!");
		
		ItemStack appleMod = Main.ins.utils.createItem(Material.GOLDEN_APPLE, 0, 1, "§6UHC Apfel", null);
		ItemStack durability = Main.ins.utils.createItem(Material.ANVIL, 0, 1, "§6Haltbarkeit ändern", null);

		if(item != null && item.getItemMeta() != null &&
				item.getItemMeta().spigot().isUnbreakable()) {
			unbreakable = Main.ins.utils.applyEnchant(unbreakable, Enchantment.ARROW_DAMAGE, 1);
			ItemMeta meta = unbreakable.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			unbreakable.setItemMeta(meta);
		}
		
		if(item != null && item.getType() == Material.POTION) {
			ItemStack potion = Main.ins.utils.createItem(Material.POTION, 0, 1, "§6Trankeffekt bearbeiten", "\n§7Diesen Trank bearbeiten");
			inv.setItem(15, potion);
		}
		
		
		ItemStack add = Main.ins.utils.createItem(Material.WOOD_BUTTON, 0, 1, "§6+1",  "\n§8Shift Klick: +5");
		ItemStack remove = Main.ins.utils.createItem(Material.WOOD_BUTTON, 0, 1, "§6-1",  "\n§8Shift Klick: -5");
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(13, amount);
		
		inv.setItem(14, enchants);
		
		inv.setItem(4, add);
		inv.setItem(4+9+9, remove);
		
		if(isApple(item)) {
		 inv.setItem(15, appleMod);
		}
		
		if(isDChangeable(item)) {
		 inv.setItem(15, durability);
		 inv.setItem(16, unbreakable);
		}
		
		p.openInventory(inv);
		
	}
	
	public static void createInvDur(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "Haltbarkeit verändern");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		
		ItemStack item = p.getItemInHand();
		int dur = item.getType().getMaxDurability()-item.getDurability();
		ItemStack display = Main.ins.utils.createItem(Material.ANVIL, 0, 1, "§7" + dur + "/" + item.getType().getMaxDurability(), null);
		
		
		ItemStack add = Main.ins.utils.createItem(Material.WOOD_BUTTON, 0, 1, "§6+1",  "\n§8Shift Klick: +10%");
		ItemStack remove = Main.ins.utils.createItem(Material.WOOD_BUTTON, 0, 1, "§6-1", "\n§8Shift Klick: -10%");
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(6+9-1, display);
		inv.setItem(6-1, add);
		inv.setItem(6+9*2-1, remove);
		
		p.openInventory(inv);
		
	}
	
	public static void createEnchantInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*5, "Item Verzaubern");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		
		ItemStack item = p.getItemInHand();
		
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(0+9*2, empty);
		inv.setItem(0+9*3, empty);
		inv.setItem(0+9*4, empty);
		inv.setItem(1+9*2, empty);
		inv.setItem(1+9*3, empty);
		inv.setItem(1+9*4, empty);
		inv.setItem(2+9*2, empty);
		inv.setItem(2+9*3, empty);
		inv.setItem(2+9*4, empty);
		
		inv.setItem(3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Explosionsschutz", null));
		inv.setItem(4, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Schusssicher", null));
		inv.setItem(5, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Atmung", null));
		inv.setItem(6, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Wasseraffinität", null));
		inv.setItem(7, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Dornen", null));
		inv.setItem(8, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Wasserläufer", null));
		
		inv.setItem(3+9, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Rückstoß", null));
		inv.setItem(4+9, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Verbrennung", null));
		inv.setItem(5+9, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Plünderung", null));
		inv.setItem(6+9, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Stärke", null));
		inv.setItem(7+9, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Flamme", null));
		inv.setItem(8+9, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Schlag", null));
		
		inv.setItem(3+9*2, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Haltbarkeit", null));
		inv.setItem(4+9*2, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Glück", null));
		inv.setItem(5+9*2, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Köder", null));
		inv.setItem(6+9*2, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Glück des Meeres", null));
		inv.setItem(7+9*2, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Unendlich", null));
		inv.setItem(8+9*2, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Schärfe", null));
		
		inv.setItem(3+9*3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Nemesis der Gliederfüßer", null));
		inv.setItem(4+9*3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Feuerschutz", null));
		inv.setItem(5+9*3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Bann", null));
		inv.setItem(6+9*3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Behutsamkeit", null));
		inv.setItem(7+9*3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Schutz", null));
		inv.setItem(8+9*3, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Effizienz", null));
		
		inv.setItem(3+9*4, Main.ins.utils.createItem(Material.ENCHANTED_BOOK, 0, 1, "§6Federfall", null));

		
		p.openInventory(inv);
	}
	
	public static void addEnchantInv(Player p,Enchantment ench) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "Verzaubern");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		
		ItemStack item = p.getItemInHand();
		
		
		
		ItemStack add = Main.ins.utils.createItem(Material.WOOD_BUTTON, 0, 1, "§6+1",  "\n§8Shift Klick: +5");
		ItemStack remove = Main.ins.utils.createItem(Material.WOOD_BUTTON, 0, 1, "§6-1", "\n§8Shift Klick: -5");
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		
		inv.setItem(5, add);
		inv.setItem(5+9*2, remove);
		
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		meta.addStoredEnchant(ench, ench.getStartLevel(), true);
		book.setItemMeta(meta);
		
		inv.setItem(14, book);
		
		p.openInventory(inv);
	}
	
	public static void openAppleInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "GOLDEN-HEAD");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		ItemStack display = Main.ins.utils.createItem(Material.GOLDEN_APPLE, 0, 1, "§6Golden Head", "\n§a§lWenn aktiv:\n§7Golden Heads stammen\n§7aus dem orginalen \n§7UHC-Bukkit-Plugin und\n§7 und regenerieren \n§7vier Herzen.\n§7Normale goldene Äpfel \n§7regenerieren hingegen nur zwei.");
		
		ItemStack item = p.getItemInHand();
		
		
		
		ItemStack enabled = Main.ins.utils.createItem(Material.INK_SACK, 10, 1, "&a&lAktiv", null);
		ItemStack disabled = Main.ins.utils.createItem(Material.INK_SACK, 8, 1, "&c&lDeaktiviert", null);
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(14, display);
		inv.setItem(15, disabled);
		if(p.getItemInHand().getType() == Material.GOLDEN_APPLE && p.getItemInHand().hasItemMeta() &&
		   p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
			inv.setItem(15, enabled);
		}
		p.openInventory(inv);
		
	}
	
	public static void openPotionInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*4, "Trank Modifizieren§a");
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		ItemStack item = p.getItemInHand();
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(9, empty);
		inv.setItem(11, empty);
		
		inv.setItem(18, empty);
		inv.setItem(19, empty);
		inv.setItem(20, empty);
		
		inv.setItem(27, empty);
		inv.setItem(28, empty);
		inv.setItem(29, empty);
		
		inv.setItem(3, Main.ins.utils.createPotion(Material.POTION, 5, 1, null, "§7Direktheilung", true, null));
		inv.setItem(4, Main.ins.utils.createPotion(Material.POTION, 1, 1, null, "§7Regeneration", true, null));
		inv.setItem(5, Main.ins.utils.createPotion(Material.POTION, 9, 1, null, "§7Stärke", true, null));
		inv.setItem(6, Main.ins.utils.createPotion(Material.POTION, 3, 1, null, "§7Feuerschutz", true, null));
		inv.setItem(7, Main.ins.utils.createPotion(Material.POTION, 2, 1, null, "§7Schnelligkeit", true, null));
		inv.setItem(8, Main.ins.utils.createPotion(Material.POTION, 11, 1, null, "§7Sprungkraft", true, null));
		
		inv.setItem(12, Main.ins.utils.createPotion(Material.POTION, 14, 1, null, "§7Unsichtbarkeit", true, null));
		inv.setItem(13, Main.ins.utils.createPotion(Material.POTION, 6, 1, null, "§7Nachtsicht", true, null));
		inv.setItem(14, Main.ins.utils.createPotion(Material.POTION, 13, 1, null, "§7Unterwasseratem", true, null));
		inv.setItem(15, Main.ins.utils.createPotion(Material.POTION, 5, 1, "§fTrank der Extraenergie", "§7Extraenergie", true, null));
		inv.setItem(16, Main.ins.utils.createPotion(Material.POTION, 1, 1, "§fTrank der Absorption", "§7Absorption", true, null));
		inv.setItem(17, Main.ins.utils.createPotion(Material.POTION, 1, 1, "§fTrank des Widerstandes", "§7Resistenz", true, null));
		
		inv.setItem(21, Main.ins.utils.createPotion(Material.POTION, 3, 1, "§fTrank der Sättigung", "§7Sättigung", true, null));
		inv.setItem(22, Main.ins.utils.createPotion(Material.POTION, 2, 1, "§fTrank der Eile", "§7Eile", true, null));
		inv.setItem(23, Main.ins.utils.createPotion(Material.POTION, 12, 1, null, "§cDirektschaden", true, null));
		inv.setItem(24, Main.ins.utils.createPotion(Material.POTION, 4, 1, null, "§cVergiftung", true, null));
		inv.setItem(25, Main.ins.utils.createPotion(Material.POTION, 10, 1, null, "§cLangsamkeit", true, null));
		inv.setItem(26, Main.ins.utils.createPotion(Material.POTION, 8, 1, null, "§cSchwäche", true, null));
		
		inv.setItem(30, Main.ins.utils.createPotion(Material.POTION, 12, 1, "§fTrank des Zerfalls", "§cWither", true, null));
		inv.setItem(31, Main.ins.utils.createPotion(Material.POTION, 8, 1, "§fTrank der Blindheit", "§cBlindheit", true, null));
		inv.setItem(32, Main.ins.utils.createPotion(Material.POTION, 13, 1, "§fTrank der Übelkeit", "§cÜbelkeit", true, null));
		inv.setItem(33, Main.ins.utils.createPotion(Material.POTION, 4, 1, "§fTrank des Hungers", "§cHunger", true, null));
		inv.setItem(34, Main.ins.utils.createPotion(Material.POTION, 10, 1, "§fTrank der Trägheit", "§cAbbaulähmung", true, null));
		
		
		p.openInventory(inv);
	}
	
	public static boolean isApple(ItemStack stack) {
		return stack.getType() == Material.GOLDEN_APPLE ? true : false;
	}
	
	public static boolean isDChangeable(ItemStack stack) {
		
		if(stack.getType() == Material.WOOD_AXE ||
		   stack.getType() == Material.WOOD_HOE ||
		   stack.getType() == Material.WOOD_PICKAXE ||
		   stack.getType() == Material.WOOD_SPADE ||
		   stack.getType() == Material.WOOD_SWORD ||
		   stack.getType() == Material.LEATHER_BOOTS ||
		   stack.getType() == Material.LEATHER_CHESTPLATE ||
		   stack.getType() == Material.LEATHER_HELMET ||
		   stack.getType() == Material.LEATHER_LEGGINGS) {
			return true;
		}
		
		if(stack.getType() == Material.GOLD_AXE ||
				   stack.getType() == Material.GOLD_HOE ||
				   stack.getType() == Material.GOLD_PICKAXE ||
				   stack.getType() == Material.GOLD_SPADE ||
				   stack.getType() == Material.GOLD_SWORD ||
				   stack.getType() == Material.GOLD_HELMET ||
				   stack.getType() == Material.GOLD_CHESTPLATE ||
				   stack.getType() == Material.GOLD_BOOTS ||
				   stack.getType() == Material.GOLD_LEGGINGS) {
					return true;
				}
		
		if(stack.getType() == Material.STONE_AXE ||
				   stack.getType() == Material.STONE_HOE ||
				   stack.getType() == Material.STONE_PICKAXE ||
				   stack.getType() == Material.STONE_SPADE ||
				   stack.getType() == Material.STONE_SWORD||
				   stack.getType() == Material.CHAINMAIL_HELMET ||
				   stack.getType() == Material.CHAINMAIL_CHESTPLATE ||
				   stack.getType() == Material.CHAINMAIL_BOOTS ||
				   stack.getType() == Material.CHAINMAIL_LEGGINGS) {
					return true;
				}
		
		if(stack.getType() == Material.IRON_AXE ||
				   stack.getType() == Material.IRON_HOE ||
				   stack.getType() == Material.IRON_PICKAXE ||
				   stack.getType() == Material.IRON_SPADE ||
				   stack.getType() == Material.IRON_SWORD||
				   stack.getType() == Material.IRON_HELMET ||
				   stack.getType() == Material.IRON_CHESTPLATE ||
				   stack.getType() == Material.IRON_BOOTS ||
				   stack.getType() == Material.IRON_LEGGINGS) {
					return true;
				}
		if(stack.getType() == Material.DIAMOND_AXE ||
				   stack.getType() == Material.DIAMOND_HOE ||
				   stack.getType() == Material.DIAMOND_PICKAXE ||
				   stack.getType() == Material.DIAMOND_SPADE ||
				   stack.getType() == Material.DIAMOND_SWORD||
				   stack.getType() == Material.DIAMOND_HELMET ||
				   stack.getType() == Material.DIAMOND_CHESTPLATE ||
				   stack.getType() == Material.DIAMOND_BOOTS ||
				   stack.getType() == Material.DIAMOND_LEGGINGS ||
				   stack.getType() == Material.BOW ||
				   stack.getType() == Material.FISHING_ROD ||
				   stack.getType() == Material.SHEARS||
				   stack.getType() == Material.FLINT_AND_STEEL) {
					return true;
				}
		
		
		return false;
	}
	
	public static void createPotionEditorInv(Player p, PotionEffectType effect, ItemStack clicked) {
		
		Inventory inv = null;
		
		if(p.getOpenInventory() != null && 
				p.getOpenInventory().getTitle().equalsIgnoreCase("Trank Modifizieren§b")) {
			inv = p.getOpenInventory().getTopInventory();
		} else {
			inv = Bukkit.createInventory(null, 9*3, "Trank Modifizieren§b");
			p.openInventory(inv);
		}
			
		Main.ins.getOneVsOnePlayer(p).setBlackDealerEffect(effect);
		
		ItemStack empty = Main.ins.utils.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		ItemStack item = p.getItemInHand();
		
		int potionLevel = 0;
		int potionTimer = 0;
		
		if(item.getType() == Material.POTION) {
			PotionMeta meta = (PotionMeta) item.getItemMeta();
			
			
//			Collection<PotionEffect> fx = Potion.fromItemStack(item).getEffects();
//			
//			fx.addAll(meta.getCustomEffects());
			
			
			boolean hasAsBaseEffect = 
			Potion.fromItemStack(p.getItemInHand()).getType().getEffectType().equals(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect());
			
			
			int baseLevel = 0;
			
			if(hasAsBaseEffect) {
				baseLevel = Potion.fromItemStack(p.getItemInHand()).getEffects().iterator().next().getAmplifier()+1;
			}
			
			
			for(PotionEffect potionEffect : meta.getCustomEffects()) {
				if(potionEffect.getType().getName().equalsIgnoreCase(effect.getName())) {
					potionLevel = potionEffect.getAmplifier()+1;
					potionTimer = potionEffect.getDuration()/20;
				}
			}
			
			if(hasAsBaseEffect && potionLevel == 0) {
				potionLevel = baseLevel;
			}
			
			
			
			
		} else {
			p.closeInventory();
			p.sendMessage(Main.ins.prefixRed + "§cFehler beim Laden der Trankdaten! Bitte öffne das Inventar erneut!");
			return;
		}
		
		
		ItemStack level = Main.ins.utils.createItem(Material.EMERALD, 0, potionLevel, "§7Level: §6" + potionLevel, null);
		ItemStack time = Main.ins.utils.createItem(Material.WATCH, 0, 1, "§7Dauer: §6" + Main.ins.utils.getFormattedTime(potionTimer), null);
		
		ItemStack addLevel = Main.ins.utils.createItem(Material.STONE_BUTTON, 0, 1, "§6+1 Level", "\n§8Shift-Klick: +5 Level");
		ItemStack remLevel = Main.ins.utils.createItem(Material.STONE_BUTTON, 0, 1, "§6-1 Level", "\n§8Shift-Klick: -5 Level");
		
		ItemStack addTime = Main.ins.utils.createItem(Material.STONE_BUTTON, 0, 1, "§6+10 Sekunden", "\n§8Shift-Klick: +1 Sekunde");
		ItemStack remTime = Main.ins.utils.createItem(Material.STONE_BUTTON, 0, 1, "§6-10 Sekunden", "\n§8Shift-Klick: -1 Sekunde");
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(9, empty);
		inv.setItem(11, empty);
		
		if(clicked != null) inv.setItem(13, clicked);
		
		
		inv.setItem(5, addLevel);
		inv.setItem(14, level);
		inv.setItem(23, remLevel);
		
		inv.setItem(6, addTime);
		inv.setItem(15, time);
		inv.setItem(24, remTime);
		
		
		inv.setItem(18, empty);
		inv.setItem(19, empty);
		inv.setItem(20, empty);
		
		
		
		
		
	}
	
}
