package de.onevsone.methods;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.AsyncCatcher;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.utils.WorldManager;

import de.onevsone.Main;
import de.onevsone.enums.OvOColor;
import net.md_5.bungee.api.ChatColor;

public class Utils {

	private ItemStack challanger;
	private ItemStack challangerDisabled;
	private ItemStack spectate;
	private ItemStack book;
	private ItemStack tournamentItem;
	private ItemStack rankItem;
	private ItemStack settingsItem;
	private ItemStack leaveItem;
	
	private Location mainSpawn = null;
	private Location exitSpawn = null;
	
	private Location maxKitEditLoc = null;
	private Location minKitEditLoc = null;
	
	private Location queueSpawn = null;
	private Location blackDealerSpawn = null;
	
	private SoundMgr sound = new SoundMgr();
	private SkullPlacer placer = new SkullPlacer();
	private Location holoSpawn;
	
	public void reloadBasics() {
		challanger = createItem(Material.DIAMOND_SWORD, 0, 1, "§6Herausfordern &7(/c [Name])", "\n§b§lLinksklick §r§7auf einen Spieler:\n"
																							 + "§7 um diesen Herauszufordern, \n§7 oder um eine bestehende Herausforderung anzunehmen\n\n"
																							 + "§a§lRechtsklick §r§7auf einen Spieler:"
																							 + "\n§7 um diesen in dein Team einzuladen,\n§7 oder eine bestehende Teamanfrage anzunehmen \n\n"
																							 + "§d§lShift-Rechtsklick:§r§7\n§7"
																							 + " Deaktivieren von Herausforderungen/Teamanfragen\n§7 von anderen Spielern", ItemFlag.HIDE_UNBREAKABLE);
		challangerDisabled = createItem(Material.WOOD_SWORD, 0, 1, "§8Herausforderungen deaktiviert", "\n§b§lLinksklick §r§7auf einen Spieler:\n"
				 																					+ "§7 um diesen Herauszufordern, \n§7 oder um eine bestehende Herausforderung anzunehmen\n\n"
				 																					+ "§a§lRechtsklick §r§7auf einen Spieler:"
				 																					+ "\n§7 um diesen in dein Team einzuladen,\n§7 oder eine bestehende Teamanfrage anzunehmen \n\n"
				 																					+ "§d§lShift-Rechtsklick:§r§7\n§7"
				 																					+ " Reaktivieren von Herausforderungen/Teamanfragen\n§7 von anderen Spielern", ItemFlag.HIDE_UNBREAKABLE);
		spectate = createItem(Material.GHAST_TEAR, 0, 1, "§6Beobachten &7(/spec)", "\n§a§lRechtsklick: "
																				 + "\n§r§7 um ein laufendes Spiel zu beobachten");
		settingsItem = createItem(Material.REDSTONE_COMPARATOR, 0, 1, "§6Einstellungen §7(/settings)", "\n§a§lRechtsklick: "
																									 + "\n§r§7 um Einstellungen vorzunehmen");
		leaveItem = createItem(Material.WATCH, 0, 1, "§c➼ §r§71vs1 verlassen", "\n§a§lRechtsklick: "
																			 + "\n§r§7 um den Spielmodus zu verlassen.");
		
		ItemMeta challangerMeta = challanger.getItemMeta();
		challangerMeta.spigot().setUnbreakable(true);
		challanger.setItemMeta(challangerMeta);
		
		ItemMeta challangerDisableMeta = challangerDisabled.getItemMeta();
		challangerDisableMeta.spigot().setUnbreakable(true);
		challangerDisabled.setItemMeta(challangerDisableMeta);
		
		
		YamlConfiguration cfg = getYaml("Spawns");
		
		int x = cfg.getInt("Spawns.Lobby.X");
		int y = cfg.getInt("Spawns.Lobby.Y");
		int z = cfg.getInt("Spawns.Lobby.Z");
		
		float yaw = (float) cfg.getDouble("Spawns.Lobby.Yaw");
		float pitch = (float) cfg.getDouble("Spawns.Lobby.Pitch");
		
		
		String world = cfg.getString("Spawns.Lobby.World");
		
		if(world != null && Bukkit.getWorld(world) != null) 
			mainSpawn = new Location(Bukkit.getWorld(world), (double)x+0.5,y+2,(double)z+0.5,yaw,pitch);
		
		x = cfg.getInt("Spawns.Exit.X");
		y = cfg.getInt("Spawns.Exit.Y");
		z = cfg.getInt("Spawns.Exit.Z");
		
		yaw = (float) cfg.getDouble("Spawns.Exit.Yaw");
		pitch = (float) cfg.getDouble("Spawns.Exit.Pitch");
		
		
		world = cfg.getString("Spawns.Exit.World");
		
		if(world != null && Bukkit.getWorld(world) != null) 
			exitSpawn = new Location(Bukkit.getWorld(world), (double)x-0.5,y+2,(double)z-0.5,yaw,pitch);
		
		double x2 = cfg.getDouble("Spawns.Queue.X");
		double y2 = cfg.getDouble("Spawns.Queue.Y");
		double z2 = cfg.getDouble("Spawns.Queue.Z");
		world = cfg.getString("Spawns.Queue.World");

		if (world != null && Bukkit.getWorld(world) != null) 
			queueSpawn = new Location(Bukkit.getWorld(world), x2, y2, z2); 
		
		double x3 = cfg.getDouble("Spawns.BlackDealer.X");
		double y3 = cfg.getDouble("Spawns.BlackDealer.Y");
		double z3 = cfg.getDouble("Spawns.BlackDealer.Z");
		world = cfg.getString("Spawns.BlackDealer.World");

		if (world != null && Bukkit.getWorld(world) != null) 
			blackDealerSpawn = new Location(Bukkit.getWorld(world), x3, y3, z3); 
		
		
		int kiteditX1 = cfg.getInt("Spawns.KitEdit.X1");
		int kiteditY1 = cfg.getInt("Spawns.KitEdit.Y1");
		int kiteditZ1 = cfg.getInt("Spawns.KitEdit.Z1");
		
		int kiteditX2 = cfg.getInt("Spawns.KitEdit.X2");
		int kiteditY2 = cfg.getInt("Spawns.KitEdit.Y2");
		int kiteditZ2 = cfg.getInt("Spawns.KitEdit.Z2");
		
		world = cfg.getString("Spawns.KitEdit.World");

		int maxX = Math.max(kiteditX1, kiteditX2);
		int maxY = Math.max(kiteditY1, kiteditY2);
		int maxZ = Math.max(kiteditZ1, kiteditZ2);
		
		int minX = Math.min(kiteditX1, kiteditX2);
		int minY = Math.min(kiteditY1, kiteditY2);
		int minZ = Math.min(kiteditZ1, kiteditZ2);
		
		if(world != null && Bukkit.getWorld(world) != null) {
			maxKitEditLoc = new Location(Bukkit.getWorld(world), maxX, maxY, maxZ);
			minKitEditLoc = new Location(Bukkit.getWorld(world), minX, minY, minZ);
		}
		
		cfg = Main.ins.utils.getYaml("Spawns.yml");
		
		double xD = cfg.getDouble("Hologram.X");
		double yD = cfg.getDouble("Hologram.Y");
		double zD = cfg.getDouble("Hologram.Z");
		world = cfg.getString("Hologram.World");
		
		if(world != null && !world.equalsIgnoreCase("") && Bukkit.getWorld(world) != null) {
			holoSpawn = new Location(Bukkit.getWorld(world), xD, yD, zD);
		}
		
		
		
		
	}
	
	public Location getMaxKitEdit() {
		return maxKitEditLoc;
	}
	
	public Location getMinKitEdit() {
		return minKitEditLoc;
	}
	
	public Location getQueueSpawn() {
		return queueSpawn;
	}
	
	public Location getBlackDealerSpawn() {
		return blackDealerSpawn;
	}

	public Location getMainSpawn() {
		return mainSpawn.clone();
	}
	
	public void tpToLobby(Player p) {
		if(p != null) {
			
			if(getMainSpawn() != null && getMainSpawn().getWorld() != null) p.teleport(getMainSpawn()); 
			
			StatHolograms.spawnHologram(Main.ins.getOneVsOnePlayer(p));
		}
	}
	
	
	public Location getExitSpawn() {
		return exitSpawn.clone();
	}
	
	public void giveLobbyItems(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		
		updateChallanger(p);
		
		
		p.getInventory().setItem(1, spectate);
		p.getInventory().setItem(7, settingsItem);
		p.getInventory().setItem(8, leaveItem);
	}
	
	public void updateChallanger(Player p) {
		p.getInventory().setItem(0, createItem(Material.AIR, 0, 1, "", ""));
		p.updateInventory();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.getOneVsOnePlayer(p).getAcceptChallanges()) {
					p.getInventory().setItem(0, challanger);
				} else {
					p.getInventory().setItem(0, challangerDisabled);
				}
				
			}
		}.runTaskLater(Main.ins, 1);
		
		p.updateInventory();
	}
	
	public void giveSpectatorItem(Player p, String arena, boolean tournament, boolean death) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				int slot = 0;
				
				for(UUID pos1 : Main.ins.getOneVsOneArena(arena).getPos1()) {
					if(Bukkit.getPlayer(pos1) == null) continue;
					ItemStack player = new ItemStack(Material.LEATHER_HELMET);
					LeatherArmorMeta meta = (LeatherArmorMeta) player.getItemMeta();
					
					OvOColor c = Main.ins.getOneVsOneArena(arena).getColorPos1();
					meta.setColor(c.getColor());
					meta.setDisplayName(c.colorKey() + Bukkit.getPlayer(pos1).getDisplayName());
					player.setItemMeta(meta);
					
					if(slot > 2) break;
					
					p.getInventory().setItem(slot, player);
					slot++;
				}
				
				for(UUID pos2 : Main.ins.getOneVsOneArena(arena).getPos2()) {
					if(Bukkit.getPlayer(pos2) == null) continue;
					ItemStack player = new ItemStack(Material.LEATHER_HELMET);
					LeatherArmorMeta meta = (LeatherArmorMeta) player.getItemMeta();
					OvOColor c = Main.ins.getOneVsOneArena(arena).getColorPos2();
					meta.setColor(c.getColor());
					meta.setDisplayName(c.colorKey() + Bukkit.getPlayer(pos2).getDisplayName());
					player.setItemMeta(meta);
					
					if(slot > 5) break;
					p.getInventory().setItem(slot, player);
					slot++;
				}
				
			}
		}.runTaskLater(Main.ins, 3);
		
		
		if(!tournament && !death) {
			ItemStack leave = Main.ins.utils.createItem(Material.INK_SACK, 8, 1, "§cSpectator Modus verlassen", null);
			p.getInventory().setItem(8, leave);
			p.getInventory().setItem(7, spectate);
		}
		
		
		
		
	}
	
	public void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {

		new BukkitRunnable() {

			@Override
			public void run() {
				try {

					Object resetTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("RESET")
							.get(null);
					Object resetChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
							.getMethod("a", String.class).invoke(null, "");

					Constructor<?> resetConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
							getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
							getNMSClass("IChatBaseComponent"));
					Object rPacket = resetConstructor.newInstance(resetTitle, resetChat);
					sendPacket(player, rPacket);

					Object timesTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES")
							.get(null);
					Object timesChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
							.getMethod("a", String.class).invoke(null, "");

					Constructor<?> timesConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
							getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
							getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
					Object timesPacket = timesConstructor.newInstance(timesTitle, timesChat, fadeIn, stay, fadeOut);
					sendPacket(player, timesPacket);

					Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE")
							.get(null);
					Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
							.invoke(null, "{\"text\":\"" + title + "\"}");

					Object SubenumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE")
							.get(null);
					Object Subchat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
							.getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");

					Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
							getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
							getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
					Object packet = titleConstructor.newInstance(enumTitle, chat, fadeIn, stay, fadeOut);

					Constructor<?> SubtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
							getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
							getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
					Object Subpacket = SubtitleConstructor.newInstance(SubenumTitle, Subchat, fadeIn, stay, fadeOut);

					sendPacket(player, packet);
					sendPacket(player, Subpacket);
				}

				catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		}.runTaskAsynchronously(Main.ins);

	}

	public void sendActionBar(Player p, String msg) {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (getNMSVersion().startsWith("v1_12_")) {
					sendActionBar1_12(p, msg);
					return;
				}
				try {

					Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
							.invoke(null, "{\"text\":\"" + msg + "\"}");
					Constructor<?> titleConstructor = getNMSClass("PacketPlayOutChat")
							.getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
					Object packet = titleConstructor.newInstance(chat, (byte) 2);
					sendPacket(p, packet);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		}.runTaskAsynchronously(Main.ins);

	}

	public ItemStack createItem(Material mat, int SubID, int Amount, String Display, String Lore) {
		return createItem(mat, SubID, Amount, Display, Lore, null);

	}
	
	public ItemStack createItem(int id, int SubID, int Amount, String Display, String Lore) {
		return createItem(id, SubID, Amount, Display, Lore, null);

	}
	
	public ItemStack createItem(Material mat, int SubID, int Amount, String Display, String Lore, ItemFlag... flags) {
		ItemStack itemstack = new ItemStack(mat);
		if(mat == null || mat == Material.AIR) return itemstack;
		ItemMeta itemMeta = itemstack.getItemMeta();
		ArrayList<String> LoreList = new ArrayList<String>();
		if (Display != null) {
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Display));
		}

		if (Lore != null) {
			String[] Lines = ChatColor.translateAlternateColorCodes('&', Lore).split("\n");
			for (int i = 0; i < Lines.length; i++) {
				LoreList.add(Lines[i]);
			}
		}
		
		
		

		itemMeta.setLore(LoreList);
		
		if(flags != null)for(ItemFlag flag : flags) itemMeta.addItemFlags(flag);
		
		itemstack.setItemMeta(itemMeta);
		itemstack.setAmount(Amount);
		itemstack.setDurability((short) SubID);
		
		return itemstack;

	}

	@SuppressWarnings("deprecation")
	public ItemStack createItem(int id, int SubID, int Amount, String Display, String Lore, ItemFlag... flags) {
		ItemStack itemstack = new ItemStack(id);
		ItemMeta itemMeta = itemstack.getItemMeta();
		ArrayList<String> LoreList = new ArrayList<String>();
		if (Display != null) {
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Display));
		}

		if (Lore != null) {
			String[] Lines = ChatColor.translateAlternateColorCodes('&', Lore).split("\n");
			for (int i = 0; i < Lines.length; i++) {
				LoreList.add(Lines[i]);
			}
		}

		
		
		itemMeta.setLore(LoreList);
		
		if(flags != null)for(ItemFlag flag : flags) itemMeta.addItemFlags(flag);
		
		itemstack.setItemMeta(itemMeta);
		itemstack.setAmount(Amount);
		itemstack.setDurability((short) SubID);
		return itemstack;

	}

	public ItemStack applyEnchant(ItemStack stack, Enchantment ench, int level) {
		if (level <= 0) {
			stack.removeEnchantment(ench);
			return stack;
		}
		stack.addUnsafeEnchantment(ench, level);

		return stack;
	}

	public ItemStack applyEnchant(ItemStack stack, Enchantment ench, int level, int maxlvl) {
		if (level <= 0) {
			stack.removeEnchantment(ench);
			return stack;
		}
		if (level > maxlvl) {
			level = maxlvl;
		}
		stack.addUnsafeEnchantment(ench, level);

		return stack;
	}

	public int getEnchLevel(ItemStack stack, Enchantment ench) {
		if (stack.hasItemMeta()) {
			if (stack.getItemMeta().hasEnchants()) {
				if (stack.getItemMeta().hasEnchant(ench)) {
					return stack.getEnchantmentLevel(ench);
				}
			}
		}
		return 0;
	}

	private void sendActionBar1_12(Player player, String message) {

		try {
			Class<?> craftPlayerClass = Class
					.forName("org.bukkit.craftbukkit." + getNMSVersion() + ".entity.CraftPlayer");
			Object craftPlayer = craftPlayerClass.cast(player);

			Class<?> c4 = Class.forName("net.minecraft.server." + getNMSVersion() + ".PacketPlayOutChat");
			Class<?> c5 = Class.forName("net.minecraft.server." + getNMSVersion() + ".Packet");
			Class<?> c2 = Class.forName("net.minecraft.server." + getNMSVersion() + ".ChatComponentText");
			Class<?> c3 = Class.forName("net.minecraft.server." + getNMSVersion() + ".IChatBaseComponent");
			Class<?> chatMessageTypeClass = Class
					.forName("net.minecraft.server." + getNMSVersion() + ".ChatMessageType");
			Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
			Object chatMessageType = null;
			for (Object obj : chatMessageTypes)
				if (obj.toString().equals("GAME_INFO"))
					chatMessageType = obj;

			Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
			Object ppoc = c4.getConstructor(new Class[] { c3, chatMessageTypeClass })
					.newInstance(new Object[] { o, chatMessageType });
			Method m1 = craftPlayerClass.getDeclaredMethod("getHandle", new Class[0]);
			Object h = m1.invoke(craftPlayer, new Object[0]);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
			m5.invoke(pc, new Object[] { ppoc });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getNMSVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	public YamlConfiguration defaultYML() {
		if (defaultFile() != null) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(defaultFile());
			return cfg;
		} else
			return null;
	}

	public File defaultFile() {
		
		File file = new File("plugins/1vs1/config.yml");
		File dir = new File("plugins/1vs1");
		
		if (!file.exists()) {
			
			try {
				dir.mkdirs();
				file.createNewFile();
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

				file = new File("plugins/1vs1/config.yml");
				cfg = YamlConfiguration.loadConfiguration(file);

				generateDefaultFileConfig(false);

				cfg.save(file);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		return file;
	}

	public void generateDefaultFileConfig(boolean reset) {
		
		if (!reset) {
			File file = new File("plugins/1vs1/config.yml");
			if (!file.exists())
				Main.ins.saveResource("config.yml", reset);

		} else
			Main.ins.saveResource("config.yml", reset);
	}
	
	public File getPluginFile(String Pfad) {
		File file = new File("plugins/1vs1/" + Pfad + ".yml");
		if (!file.exists() || file == null) {
			try {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}

	public boolean existFile(String Pfad) {
		File file = new File("plugins/1vs1/" + Pfad + ".yml");
		try {
			if (!file.exists() || file == null)
				return false;
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public YamlConfiguration getYaml(String Pfad) {
		if (getPluginFile(Pfad) != null) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getPluginFile(Pfad));
			return cfg;
		}
		return null;
	}
	
	
	/**
	 * @return the challanger (active) item
	 */
	public ItemStack getChallanger() {
		return challanger;
	}
	
	/**
	 * @return the challanger (disabled)
	 */
	public ItemStack getChallangerDisabled() {
		return challangerDisabled;
	}



	/**
	 * @return the spectate
	 */
	public ItemStack getSpectate() {
		return spectate;
	}



	/**
	 * @return the book
	 */
	public ItemStack getBook() {
		return book;
	}



	/**
	 * @return the tournamentItem
	 */
	public ItemStack getTournamentItem() {
		return tournamentItem;
	}



	/**
	 * @return the rankItem
	 */
	public ItemStack getRankItem() {
		return rankItem;
	}



	/**
	 * @return the settingsItem
	 */
	public ItemStack getSettingsItem() {
		return settingsItem;
	}



	/**
	 * @return the leaveItem
	 */
	public ItemStack getLeaveItem() {
		return leaveItem;
	}
	
	
	public boolean checkRegion(Location checkLoc, Location loc1, Location loc2) {
		
		Location min = getMinLoc(loc1, loc2);
		Location max = getMaxLoc(loc1, loc2);
		
	    if(min == null || max == null) return false;
	
	    
	    if(min.getWorld() == null || max.getWorld() == null) return false;
	    
	    
        if(min.getBlockY() > checkLoc.getBlockY() ||
           max.getBlockY() < checkLoc.getBlockY()) {
        	return false;
        }
        if (!checkLoc.getWorld().getName().equalsIgnoreCase(min.getWorld().getName())) {
            return false;
        }
        int hx = checkLoc.getBlockX();
        int hz = checkLoc.getBlockZ();
        if (hx < min.getBlockX()) return false;
        if (hx > max.getBlockX()) return false;
        if (hz < min.getBlockZ()) return false;
        if (hz > max.getBlockZ()) return false;
        return true;
    
	}
	
	public Location getMinLoc(Location loc1, Location loc2) {
		
		if(loc1 == null || 
		   loc2 == null || 
		   loc1.getWorld() == null || 
		   loc2.getWorld() == null) return null;
		
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		
		if(!loc1.getWorld().getName().equals(loc2.getWorld().getName())) return null;
		
		return new Location(loc1.getWorld(), minX, minY, minZ);
	}
	
	public Location getMaxLoc(Location loc1, Location loc2) {
		
		if(loc1 == null || 
		   loc2 == null|| 
		   loc1.getWorld() == null || 
		   loc2.getWorld() == null) return null;
		
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		
		if(!loc1.getWorld().getName().equals(loc2.getWorld().getName())) return null;
		
		return new Location(loc1.getWorld(), maxX, maxY, maxZ);
	}
	
	public Location getPos1Layout(String layout) {
		YamlConfiguration cfg = getYaml("Layouts");
		if(cfg.getConfigurationSection("Layout." + layout) != null) {
			int x = cfg.getInt("Layout." + layout + ".maxX");
			int y = cfg.getInt("Layout." + layout + ".maxY");
			int z = cfg.getInt("Layout." + layout + ".maxZ");
			
			String world = cfg.getString("Layout." + layout + ".world");
			
			if(world == null || Bukkit.getWorld(world) == null) return null;
			
			return new Location(Bukkit.getWorld(world), x, y, z);
		}
		return null;
	}
	
	public Location getPos2Layout(String layout) {
		YamlConfiguration cfg = getYaml("Layouts");
		if(cfg.getConfigurationSection("Layout." + layout) != null) {
			int x = cfg.getInt("Layout." + layout + ".minX");
			int y = cfg.getInt("Layout." + layout + ".minY");
			int z = cfg.getInt("Layout." + layout + ".minZ");
			
			String world = cfg.getString("Layout." + layout + ".world");
			
			if(world == null || Bukkit.getWorld(world) == null) return null;
			
			return new Location(Bukkit.getWorld(world), x, y, z);
		}
		return null;
	}
	
	public Location getResetPosArena(String arena) {
		YamlConfiguration cfg = getYaml("Arenas");
		if(cfg.getConfigurationSection("Arena." + arena) != null) {
			int x = cfg.getInt("Arena." + arena + ".ResetX");
			int y = cfg.getInt("Arena." + arena + ".ResetY");
			int z = cfg.getInt("Arena." + arena + ".ResetZ");
			
			String world = cfg.getString("Arena." + arena + ".ResetWorld");
			
			if(world == null || Bukkit.getWorld(world) == null) return null;
			
			return new Location(Bukkit.getWorld(world), x, y, z);
		}
		return null;
	}
	
	public SoundMgr getSoundMgr() {
		return sound;
	}
	
	public SkullPlacer getSkullPlacer() {
		return this.placer;
	}
	
	public String getUnknownSkinTexture() {
		return "eyJ0aW1lc3RhbXAiOjE1MjA5Njg1NjM2ODcsInByb2ZpbGVJZCI6IjYwNmUyZmYwZWQ3NzQ4NDI5ZDZjZTFkMzMyMWM3ODM4IiwicHJvZmlsZU5hbWUiOiJNSEZfUXVlc3Rpb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUxNjNkYWZhYzFkOTFhOGM5MWRiNTc2Y2FhYzc4NDMzNjc5MWE2ZTE4ZDhmN2Y2Mjc3OGZjNDdiZjE0NmI2In19fQ====";
	}
	
	public boolean isMainThread() {
		try {
			AsyncCatcher.catchOp("Test");
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	
	public boolean isMultiverseInstalled() {
		return Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core") != null;
	}
	
	public MultiverseCore getMultiverseMgr() {
		if(!isMultiverseInstalled()) return null;
		 return (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
	}
	
	public WorldManager getMvMgr() {
		if(getMultiverseMgr() == null) return null;
		return (WorldManager) getMultiverseMgr().getMVWorldManager();
	}
	
	public boolean removeWorld(String name) {
		if(name == null || getMvMgr() == null) return false;
		
		try {
			return getMvMgr().removeWorldFromConfig(name);
		} catch (Exception e) {
			//CATCH MULTIVERSE API ERROR
		}
		return false;
	}
	
	public boolean addWorld(String name) {
		if(name == null || getMvMgr() == null) return false;
		try {
			 return getMvMgr().addWorld(name, Environment.NORMAL, "1234", WorldType.FLAT, false, "normal");
		} catch (NullPointerException e) {
			//CATCH MULTIVERSE API ERROR
		}
		return false;
	}
	
	public String formatBoolean(boolean b) {
		return b == true ? "§aJa" : "§cNein";
	}
	
	public String formatBoolen(boolean b, String colorCodeYes, String colorCodeNo) {
		return b == true ? colorCodeYes + "Ja" : colorCodeNo + "Nein";
	}
	
	public String formatBoolen(boolean b, String colorCodeYes, String colorCodeNo, String bTrue, String bFalse) {
		return b == true ? colorCodeYes + bTrue : colorCodeNo + bFalse;
	}
	
	@SuppressWarnings("null")
	public ItemStack createPotion(Material material, int subId, int amount, String displayName, String lore, boolean hideEffects, PotionEffect... effects) {
		
		ItemStack potion = createItem(material, subId, amount, displayName, lore);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		
		if(effects == null) {
			meta.clearCustomEffects();
		} else {
			for(PotionEffect effect : effects) 
				meta.addCustomEffect(effect, true);
			
		}
		
		if(hideEffects) meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		potion.setItemMeta(meta);
		
		return potion;
	}
	
	public String getFormattedTime(int secs) {
		int mins = 0;
		
		while(secs >= 60) {
			secs -= 60;
			mins++;
		}
		
		if(secs < 10) return mins + ":0" + secs; 
		 else return mins + ":" + secs;
	}
	
	public boolean compareItems(ItemStack item1, ItemStack item2) {
		return compareItem(item1, item2, false);
	}
	
	public boolean compareItem(ItemStack item1, ItemStack item2, boolean compareAmount) {
		if(item1 == null || item2 == null) return false;
		if(item1.getType() != item2.getType()) return false;
		if(item1.hasItemMeta() != item2.hasItemMeta()) return false;
		if(compareAmount && (item1.getAmount() != item2.getAmount())) return false;
		if(item1.getDurability() != item2.getDurability()) return false;
		if(!item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) return false;
		if(item1.getItemMeta().hasEnchants() != item2.getItemMeta().hasEnchants()) return false;
		if(!item1.getItemMeta().getLore().containsAll(item2.getItemMeta().getLore())) return false;
		return true;
	}
	
	public Location getHoloSpawn() {
		return this.holoSpawn;
	}

	
	
}
