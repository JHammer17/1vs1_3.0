/**
 * 
 */
package de.onevsone.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.database.sql.Database;
import de.onevsone.database.sql.SQLite;
import de.onevsone.enums.OvOColor;
import de.onevsone.objects.PlayerStatsData;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 16.08.2019 20:05:40					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class DBMgr {

	
	public String playerTable = "PlayerData";
	public String kitsTable = "KitData";
	public String statsTable = "StatData";
	
	/*Init Methods of Databases*/
	public SQLite sql = null;
	
	public DBMgr() {
		startSQLLite();
	}
	
	private void startSQLLite() {
        sql = new SQLite();
    }
	
	public void load() {
		sql.load();
	}
	
	public void stopSQL() {
		 if (sql != null && sql.getSQLConnection() != null) {
	            try {
	                sql.getSQLConnection().close();
	                sql = null;
	            } catch (SQLException e) {
	            }
	        }
	}
	
	/* Data Prefetcher */
	
	public void prefetchAllData(UUID uuid) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(int i = 1; i < 5; i++) {
					getKit(uuid, false, "" + i);
					getKit(uuid, true, "" + i);
					getRawKitSettings(uuid, i);
					for(int j = 0; j < 16; j++) {
						isKitSettingEnabled(uuid, j, i);
					}
					getKitName(uuid, i);
				}
				getSelectedKit(uuid);
				getRawQueueSettings(uuid);
				
				for(int i = 0; i < 4; i++) 
					isQueuePrefEnabled(uuid, i);
				
				getDisabledMaps(uuid);
				
				
				
				for(int i = 1; i < 3; i++) {
					for(int j = 1; j < 9; j++) {
						getStats(uuid, i, j);
					}
				}
				
				getSkin(uuid);
				getRankPoints(uuid);
				getColor(uuid, true, true);
				getColor(uuid, true, false);
				getColor(uuid, false, true);
				getColor(uuid, false, false);
				
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	
	
	
	
	/*Database Methods*/
	public ResultSet executeCommand(String cmd, boolean result) {
		if(Main.ins.mysqlDB) {
			return null;//TODO MYSQL Implementation
		} else {
			return Database.executeCommand(cmd, result);
		}
	}
	
	public boolean isConnected() {
		if(Main.ins.mysqlDB) {
			return false;//TODO MYSQL Implementation
		} else {
			return Database.isConnected();
		}
	}
	
	public boolean isUserExists(UUID uuid, String table) {
		if(Main.ins.mysqlDB) {
			return false;//TODO MYSQL Implementation
		} else {
			return Database.isUserExists(uuid, table);
		}
	}
	
	public boolean isUserExists(UUID uuid) {
		if(Main.ins.mysqlDB) {
			return false;//TODO MYSQL Implementation
		} else {
			return Database.isUserExists(uuid);
		}
	}
	
	public void addUser(UUID uuid, String name, String table) {
		if(Main.ins.mysqlDB) {
			return;//TODO MYSQL Implementation
		} else {
			Database.addUser(uuid, name, table);
		}
	}
	
	public void addCustomUser(UUID uuid, String name, String table) {
		if(Main.ins.mysqlDB) {
			return;//TODO MYSQL Implementation
		} else {
			Database.addCustomUser(uuid.toString(), name, table);
		}
	}
	
	public void updateUserName(UUID uuid, String name) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.updateUserName(uuid, name);
				}
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	public void setKit(UUID uuid, String kit, boolean armor, String kitID) {
		
		if(armor) Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("kitArmor" + kitID, kit);
		else Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("kitInv" + kitID, kit);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setKit(uuid, kit, armor, kitID);
				}
				
			}
		}.runTaskAsynchronously(Main.ins);
		
	}
	
	public void setKit(String kitname, String kit, boolean armor, String kitID) {
		if(Main.ins.mysqlDB) {
			return;//TODO MYSQL Implementation
		} else {
			Database.setKit(kitname, kit, armor, kitID);
		}
	}
	
	public String getKit(UUID uuid, boolean armor, String kitID) {
		
		
		
		if(armor) {
			if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("kitArmor" + kitID) != null) 
				return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("kitArmor" + kitID);
		} else {
			if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("kitInv" + kitID) != null) 
				return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("kitInv" + kitID);
		}
		
		String data = Database.getKit(uuid, armor, kitID);
		
		if(armor) Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("kitArmor" + kitID, data);
		else Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("kitInv" + kitID, data);
		
		
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public String getCustomKit(String name, boolean armor) {
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return Database.getCustomKit(name, armor);
		}
	}
	
	public String resolveCustomKit(String name) {
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return Database.resolveCustomKit(name);
		}
	}
	
	public String getRawKitSettings(UUID uuid, int kitID) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("RawKit" + kitID) != null) {
			return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("RawKit" + kitID);
		}
		
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return Database.getRawKitSettings(uuid, kitID);
		}
	}
	
	public String getRawKitSettings(String name) {
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return Database.getRawKitSettings(name);
		}
	}
	
	public void setRawKitSettings(String name, String settings) {
		
		if(Main.ins.mysqlDB) {
			return;//TODO MYSQL Implementation
		} else {
			Database.setRawKitSettings(name, settings);;
		}
	}

	public boolean isKitSettingEnabled(UUID uuid, int pref, int kitID) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("Pref_" + kitID + "_" + pref) != null) {
			return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("Pref_" + kitID + "_" + pref).equalsIgnoreCase("true");
		}
		
		boolean data = Database.isKitSettingEnabled(uuid, pref, kitID);
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("Pref_" + kitID + "_" + pref, "" + data);
		
				
		if(Main.ins.mysqlDB) {
			return false;//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public void setKitPref(UUID uuid, int pref, boolean state, int kitID) {
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("Pref_" + kitID + "_" + pref, "" + state);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setKitPref(uuid, pref, state, kitID);;
				}
				
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	public String getUserName(UUID uuid) {//TODO Globales Daten speichern!
		
		if(Main.ins.globalSavedData.containsKey("UserName_" + uuid)) {
			return Main.ins.getGlobalData("UserName_" + uuid);
		}
		
		String data = Database.getUserName(uuid);
		
		Main.ins.addGlobalData("UserName_" + uuid, data);
		
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public UUID getUUID(String name) { //TODO Globales Daten Speichern!
		if(Main.ins.globalSavedData.containsKey("UUID_" + name)) {
			try {
				return UUID.fromString(Main.ins.getGlobalData("UUID_" + name));
			} catch (IllegalArgumentException e) {
			}
		}
		
		UUID data = Database.getUUID(name);
		
		if(data != null) Main.ins.addGlobalData("UUID_" + name, data.toString());
		
		
		if(Main.ins.mysqlDB) {
			return UUID.randomUUID();//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public int getSelectedKit(UUID uuid) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("SelectedKit") != null) {
			try {
				return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("SelectedKit"));
			} catch (Exception e) {}
		}
		
		int data = Database.getSelectedKit(uuid);
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("SelectedKit", "" + data);
		
		
		
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public void setSelectedKit(UUID uuid, int kit) {
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("SelectedKit", "" + kit);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setSelectedKit(uuid, kit);;
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
	}
	
	public String getKitName(UUID uuid, int kit) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("KitName" + kit) != null) {
			return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("KitName" + kit);
		}
		
		String data = Database.getKitName(uuid, kit);
				
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("KitName" + kit, data);
		
		
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public void setKitName(UUID uuid, int kit, String name) {
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("KitName" + kit, name);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setKitName(uuid, kit, name);;
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
		
	}
	
	public String getRawQueueSettings(UUID uuid) {
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return Database.getRawQueueSettings(uuid);
		}
	}
	
	public int isQueuePrefEnabled(UUID uuid, int pref) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("QPref" + pref) != null) {
			
			try {
				return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("QPref" + pref));
			} catch (NumberFormatException e) {}
			
		}
		
		
		int data = Database.isQueuePrefEnabled(uuid, pref);
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("QPref" + pref, "" + data);
		
		
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public void setQueuePref(UUID uuid, int pref, int state) {
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("QPref" + pref, "" + state);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setQueuePref(uuid, pref, state);
				}
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	public boolean isMapDisabled(UUID uuid, String mapName) {
		return getDisabledMaps(uuid).contains(mapName);
	}
	
	public String getDisabledMaps(UUID uuid) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("DisabledMaps") != null) {
			return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("DisabledMaps");
		}
		
		String data = Database.getDisabledMaps(uuid);
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("DisabledMaps", data);
		
		
		
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public void setMapDisabled(UUID uuid, String MapName, boolean disabled) {
		
		if(!disabled && getDisabledMaps(uuid).contains(MapName)) {
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("DisabledMaps", getDisabledMaps(uuid).replaceAll(MapName + " ", ""));
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("DisabledMaps", getDisabledMaps(uuid).replaceAll(" " + MapName, ""));

			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("DisabledMaps", getDisabledMaps(uuid).replaceAll(MapName, ""));

		} else if(disabled && !getDisabledMaps(uuid).contains(MapName)) {
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("DisabledMaps", getDisabledMaps(uuid) + MapName + " ");
		}
		
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setMapDisabled(uuid, MapName, disabled);;
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
		
	}
	
	/**
	 * 
	 * @param uuid
	 * @param amount
	 * @param timed 1 = All-Time 2 = 30-Days 3 = 24h
	 * @param type 1 = Fights 2 = FightsWon 3 = FightsLost 4 = FightsEz 5 = FightsTight 6 = FightsKills 7 = FightsDeath 8 = FightWL 9 = FightsKD 
	 */
	public void setStats(UUID uuid, float amount, int timed, int type) {
		
		
		StringBuilder cmd = new StringBuilder();
		
		cmd.append("Stats");
		
		if(type == 2) cmd.append("Won"); 
		if(type == 3) cmd.append("Lost"); 
		if(type == 4) cmd.append("Ez"); 
		if(type == 5) cmd.append("Tight"); 
		if(type == 6) cmd.append("Kills"); 
		if(type == 7) cmd.append("Death"); 
		if(type == 8) cmd.append("WL"); 
		if(type == 9) cmd.append("KD"); 
		
		
		if(timed == 2) cmd.append("30");
		if(timed == 3) cmd.append("24");
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData(cmd.toString(), "" + amount);
		
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setStats(uuid, amount, timed, type);
				}
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	/**
	 * 
	 * @param uuid
	 * @param timed 1 = All-Time 2 = 30-Days 3 = 24h
	 * @param type 1 = Fights 2 = FightsWon 3 = FightsLost 4 = FightsEz 5 = FightsTight 6 = FightsKills 7 = FightsDeath 8 = FightWL 9 = FightsKD 
	 */
	public float getStats(UUID uuid, int timed, int type) {
		StringBuilder cmd = new StringBuilder();
		
		cmd.append("Stats");
		
		if(type == 2) cmd.append("Won"); 
		if(type == 3) cmd.append("Lost"); 
		if(type == 4) cmd.append("Ez"); 
		if(type == 5) cmd.append("Tight"); 
		if(type == 6) cmd.append("Kills"); 
		if(type == 7) cmd.append("Death"); 
		if(type == 8) cmd.append("WL"); 
		if(type == 9) cmd.append("KD"); 
		
		
		if(timed == 2) cmd.append("30");
		if(timed == 3) cmd.append("24");
		
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData(cmd.toString()) != null) {	
			try {
				return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData(cmd.toString()));
			} catch (NumberFormatException e) {}
		}
		
		
		float data =  Database.getStats(uuid, timed, type);
		
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData(cmd.toString(), "" + data);
		
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public int getStatsAsInt(UUID uuid, int timed, int type) {
		return (int) getStats(uuid, timed, type);
	}
	
	
	/**
	 * 
	 * @param timed 1 = AllTime 2 = 30d 3 = 24h
	 * @return
	 */
	public HashMap<Integer, PlayerStatsData> getTop(int timed) {
		if(Main.ins.mysqlDB) {
			return new HashMap<>();//TODO MYSQL Implementation
		} else {
			return Database.getTop(timed);
		}
	}
	
	public void updateSkin(UUID uuid, String texture) {
		
		if(texture == null) texture = "";
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("SkinData", texture);
		
		final String fTexture = texture;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.updateSkin(uuid, fTexture);
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
		
	}
	
	public String getSkin(UUID uuid) {
		
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("SkinData") != null) {
			return Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("SkinData");
		}
		
		String data = Database.getSkin(uuid);
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("SkinData", data);
		
		
		if(Main.ins.mysqlDB) {
			return "";//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public void updateUserData(final Player p) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.updateUserData(p);
				}
			}
		};
	}
	
	/**
	 * 
	 * @param uuid
	 * @param timed 1 = Alltime, 2 = 30d, 3 = 24h
	 * @return
	 */
	public int getPosition(UUID uuid, int timed) {
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return Database.getPosition(uuid, timed);
		}
	}
	
	public void updateRankPoints(UUID uuid, int amount) {
		
		int rankPoints = getRankPoints(uuid)+amount;
		if(rankPoints < 0) rankPoints = 0;
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("RankPoints", "" + amount);
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.updateRankPoints(uuid, amount);
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
		
	}
	
	public int getRankPoints(UUID uuid) {
		
		if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("RankPoints") != null) {
			try {
				return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("RankPoints"));
			} catch (NumberFormatException e) {}
		}
		
		
		int data = Database.getRankPoints(uuid);
		
		Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("RankPoints", "" + data);
		
		
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return data;
		}
	}
	
	public int getColor(UUID uuid, boolean pos1, boolean alternate) {
		
		
		if(pos1 && alternate) {
			if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos1Alt") != null) {
				try {
					return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos1Alt"));
				} catch (NumberFormatException e) {}
			}
		} else if(!pos1 && alternate) {
			if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos2Alt") != null) {
				try {
					return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos2Alt"));
				} catch (NumberFormatException e) {}
			}	
		} else if(pos1 && !alternate) {
			if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos1") != null) {
				try {
					return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos1"));
				} catch (NumberFormatException e) {}
			}
		} else if(!pos1 && !alternate) {
			if(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos2") != null) {
				try {
					return Integer.parseInt(Main.ins.getOneVsOnePlayer(uuid).getPrefetchData("ColorPos2"));
				} catch (NumberFormatException e) {}
			}
		}
		
		int data = Database.getColor(uuid, pos1, alternate);
				
				if(pos1 && alternate) {
					Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos1Alt", "" + data);
				} else if(!pos1 && alternate) {
					Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos2Alt", "" + data);
				} else if(pos1 && !alternate) {
					Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos1", "" + data);
				} else if(!pos1 && !alternate) {
					Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos2", "" + data);
				} 
		
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return Database.getColor(uuid, pos1, alternate);
		}
	}
	
	public void setColor(UUID uuid, boolean pos1, boolean alternate, OvOColor color) {
		
		if(pos1 && alternate) {
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos1Alt", "" + color.getSubID());
		} else if(!pos1 && alternate) {
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos2Alt", "" + color.getSubID());
		} else if(pos1 && !alternate) {
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos1", "" + color.getSubID());
		} else if(!pos1 && !alternate) {
			Main.ins.getOneVsOnePlayer(uuid).addPrefetchData("ColorPos2", "" + color.getSubID());
		} 
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.ins.mysqlDB) {
					return;//TODO MYSQL Implementation
				} else {
					Database.setColor(uuid, pos1, alternate, color);;
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
	}
	
	public int getKitType(String id) {
		if(Main.ins.mysqlDB) {
			return 0;//TODO MYSQL Implementation
		} else {
			return Database.getKitType(id);
		}
	}
	
	public boolean isCustomKitExists(String name) {
		if(Main.ins.mysqlDB) {
			return false;//TODO MYSQL Implementation
		} else {
			return Database.isCustomKitExists(name);
		}
	}
	
	public void setCustomKitData(String name, String kit, String armor, String prefs) {
		if(Main.ins.mysqlDB) {
			return;//TODO MYSQL Implementation
		} else {
			Database.setCustomKitData(name, kit, armor, prefs);;
		}
	}
	
	public int getKitStats(String uuid, int timed, int subID) {
		if(Main.ins.mysqlDB) {
			return -1;//TODO MYSQL Implementation
		} else {
			return Database.getKitStats(uuid, timed, subID);
		}
	}
	
	public void setKitStats(String uuid, int timed, int subID, int amount) {
		if(Main.ins.mysqlDB) {
			return;//TODO MYSQL Implementation
		} else {
			Database.setKitStats(uuid, amount, timed, subID);;
		}
	}
	
}
