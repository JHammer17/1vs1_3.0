package de.onevsone.methods;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.onevsone.Main;
import de.onevsone.enums.KitPrefs;

public class KitMgr {

	/*
	 * Player Kits!
	 * 
	 */
	public static void saveKit(UUID uuid, ItemStack[] content, boolean armor, int subID) {
		
		if(uuid == null || content == null) return;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!armor) {
					Inventory inv = Bukkit.createInventory(null, 9*5);
					
					inv.setContents(content.clone());
					Main.ins.database.setKit(uuid, toBase64(inv), armor, ""+subID);
				} else {
					Main.ins.database.setKit(uuid, toBase64Armor(content), armor, ""+subID);
				}
				
			}
		}.runTaskAsynchronously(Main.ins);
		
	}
	
	/*
	 * Custom Kits!
	 * 
	 */
	public static void saveKit(String kitName, Inventory inv, boolean armor, int subID, ItemStack[] armorContens) {
		
		if(kitName == null || inv == null) return;
		
		
	}
	
	/*
	 * Player Kits
	 * 
	 */
	public static void loadKit(Player p, UUID kitUUID, boolean armor, int subID) {
		if(!p.isOnline() || kitUUID == null || subID <= 0 || subID >= 6) return;
		
		if(!armor) {
			try {
				String content = Main.ins.database.getKit(kitUUID, armor, ""+subID);
				if(content != null && !content.equalsIgnoreCase("")) {
					
					Inventory inv = fromBase64(content);
					
					if(inv != null) {
						p.getInventory().setContents(inv.getContents());
					} else {
						
						p.getInventory().clear();
					}
					p.updateInventory();
				} else {
					p.getInventory().clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				String content = Main.ins.database.getKit(kitUUID, armor, ""+subID);
				if(content != null && !content.equalsIgnoreCase("")) {
					ItemStack[] inv = fromBase64Armor(content);
					if(inv != null) {
						p.getInventory().setArmorContents(inv);
					} else {
						p.getInventory().setArmorContents(null);
					}
					p.updateInventory();
				} else {
					p.getInventory().setArmorContents(null);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/*
	 * Custom Kits
	 * 
	 */
	public static void loadKit(Player p, String kitName, boolean armor, int subID) {
		if(!p.isOnline() || kitName == null || subID <= 0 || subID >= 6) return;
		
		try {
			UUID uuid = UUID.fromString(kitName);
			loadKit(p, uuid, armor, subID);
			p.updateInventory();
			return;
		} catch (Exception e) {
			if(!p.isOnline() || kitName == null || subID <= 0 || subID >= 6) return;
			
			if(!armor) {
				try {
					String content = Main.ins.database.getCustomKit(kitName, armor);
					if(content != null && !content.equalsIgnoreCase("")) {
						
						Inventory inv = fromBase64(content);
						
						if(inv != null) {
							p.getInventory().setContents(inv.getContents());
						} else {
							
							p.getInventory().clear();
						}
						p.updateInventory();
					} else {
						p.getInventory().clear();
					}
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			} else {
				try {
					String content = Main.ins.database.getCustomKit(kitName, armor);
					if(content != null && !content.equalsIgnoreCase("")) {
						ItemStack[] inv = fromBase64Armor(content);
						if(inv != null) {
							p.getInventory().setArmorContents(inv);
						} else {
							p.getInventory().setArmorContents(null);
						}
						p.updateInventory();
					} else {
						p.getInventory().setArmorContents(null);
					}
					
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}
		
		
	}
	
	public static void sendKitInfos(UUID kitUUID, int kitID, Player p) {
    	String[] datas = Main.ins.database.getRawKitSettings(kitUUID, kitID).split("");
    	
    	StringBuilder builder = new StringBuilder();
    	
    	for(int i = 0; i < datas.length; i++) {
    		if(datas[i].equalsIgnoreCase("1")) {
    			if(!builder.toString().equalsIgnoreCase("")) {
    				builder.append("§8, §6").append(KitPrefs.getName(KitPrefs.getPrefByID(i)));
    			} else {
    				builder.append(KitPrefs.getName(KitPrefs.getPrefByID(i)));
    			}
    		}
    	}
    	
    	if(builder.toString().equalsIgnoreCase("")) {
    		builder.append("-");
    	}
    	
    	String customName = Main.ins.database.getKitName(kitUUID, kitID);
    	
    	String subID = "";
    	if(kitID > 1) {
    		subID = ":" + kitID;
    	}
    	
    	if(customName.equalsIgnoreCase("-")) {
    		p.sendMessage("§7Kit: §6" + Main.ins.database.getUserName(kitUUID) + subID + " §7Einstellungen: §6" + builder.toString());
        	
    	} else {
    		p.sendMessage("§7Kit: §6" + Main.ins.database.getUserName(kitUUID) + subID + " §8(" + customName + ") §7Einstellungen: §6" + builder.toString());
        	
    	}
    }
	
	
	public static String getKitName(UUID uuid) {		
		return Main.ins.database.getUserName(uuid);
	}
	
	public static void sendKitInfos(String kitName, Player p) {
		String[] datas = Main.ins.database.getRawKitSettings(kitName).split("");
    	
    	StringBuilder builder = new StringBuilder();
    	
    	for(int i = 0; i < datas.length; i++) {
    		if(datas[i].equalsIgnoreCase("1")) {
    			if(!builder.toString().equalsIgnoreCase("")) {
    				builder.append("§8, §6").append(KitPrefs.getName(KitPrefs.getPrefByID(i)));
    			} else {
    				builder.append(KitPrefs.getName(KitPrefs.getPrefByID(i)));
    			}
    		}
    	}
    	
    	if(builder.toString().equalsIgnoreCase("")) {
    		builder.append("-");
    	}
    	
    	
    	
    	
    	
    		p.sendMessage("§7Kit: §6" + kitName + " §7Einstellungen: §6" + builder.toString());
    	
	}
	
	
	public static boolean kitExists(UUID uuid) {
		return Main.ins.database.isUserExists(uuid, Main.ins.database.kitsTable);
	}
	
	public static boolean kitExists(String name) {
		return Main.ins.database.isCustomKitExists(name);
	}
	
	private static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeInt(inventory.getSize());
      
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Error.", e);
        }    
    }
	
	private static String toBase64Armor(ItemStack[] Armor) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            for (int i = 0; i < Armor.length; i++) {
            	
                dataOutput.writeObject(Armor[i]);
            }
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Error.", e);
        }    
    }

	public static Inventory fromBase64(String data) throws IOException {
    	
    	if(data == null) return null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            dataInput.readInt();
            Inventory inventory = Bukkit.getServer().createInventory(null, 36);
            
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (Exception e) {
        	return null;
        }
    }
    
    public static ItemStack[] fromBase64Armor(String data) throws IOException {
    	if(data == null) return null;
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            
            ItemStack[] Armor = {new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR)};
            
            for (int i = 0; i < 4; i++) {
              Armor[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return Armor;
        } catch (Exception e) {
        	return null;
        }
    }
    
    
    public static String getRawKitData(String kitName, boolean armor, int subID) {
		if(kitName == null || subID <= 0 || subID >= 6) return null;
		
		try {
			UUID uuid = UUID.fromString(kitName);
			
			String content = Main.ins.database.getKit(uuid, armor, ""+subID);
			
			return content;
		} catch (Exception e) {
			
			if(Main.ins.database.getKitType(kitName) == 1) 
				return getRawKitData(Main.ins.database.getUUID(kitName).toString(), armor, subID);
			
			
			if(kitName == null || subID <= 0 || subID >= 6) return null;
			
			if(!armor) {
				try {
					String content = Main.ins.database.getCustomKit(kitName, armor);
					if(content != null && !content.equalsIgnoreCase("")) {
						return content;
					} 
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			} else {
				try {
					String content = Main.ins.database.getCustomKit(kitName, armor);
					if(content != null && !content.equalsIgnoreCase("")) {
						return content;
					} 
					
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}
		
		return null;
		
	}
}
