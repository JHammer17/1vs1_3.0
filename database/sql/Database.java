package de.onevsone.database.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.onevsone.Main;
import de.onevsone.enums.OvOColor;
import de.onevsone.objects.FightInfo;
import de.onevsone.objects.PlayerStatsData;

public class Database {

	public static SQLite sql = null;
	public static Main plugin;
	public static Connection connection;
	public static String table = "KitDatabase";

	public static String playerTable = "PlayerData";
	public static String kitsTable = "KitData";
	public static String statsTable = "StatData";
	public static String fightsTable = "FightsData";
	
	
	public int tokens = 0;

	public String getDBName() {
		return table;
	}

	
	
	
	
	
	public static ResultSet executeCommand(String cmd, boolean result) {
		
		
//		if(Main.ins.utils.isMainThread() && result) Bukkit.broadcastMessage("§b" + cmd);
		try {
			
			PreparedStatement ps = connection.prepareStatement(cmd);
			
			if (result) {
				
				
				if(!ps.executeQuery().next()) {
//					Bukkit.broadcastMessage("§c§lERROR");
					return null;
				} else {
//					Bukkit.broadcastMessage("§a§lOk!");
				}
				
				ResultSet rs = ps.executeQuery();
				
				return rs;
			} else {
				
				
				ps.execute();
				ps.close();
				
				
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return null;
	}

	public static Connection getCon() {
		return connection;
	}

	public static void initialize() {
		connection = Main.ins.database.sql.getSQLConnection();
		try {
			connection = Main.ins.database.sql.getSQLConnection();
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + playerTable + " LIMIT 1");
			ResultSet rs = ps.executeQuery();
			close(ps, rs);
		} catch (SQLException ex) {
			Main.ins.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
		}
	}

	public static boolean isConnected() {
		if (!Main.ins.checkDatabaseConnection)
			return true;
		if (Main.ins.database.sql.getSQLConnection() == null)
			return false;
		try {
			Connection conn = Main.ins.database.sql.getSQLConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table);
			ps.executeQuery();
			closeStatments(ps, conn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void close(PreparedStatement ps, ResultSet rs) {
		try {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		} catch (SQLException ex) {
			Error.close(plugin, ex);
		}
	}

	public static void closeStatments(PreparedStatement ps, Connection con) {
		try {
			if (ps != null)
				ps.close();

		} catch (SQLException ex) {
			Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
		}
	}

	public static boolean isUserExists(UUID uuid, String table) {

		if (uuid == null)
			return false;

		try {

			String tableName = playerTable;
			if (table.equalsIgnoreCase("Kit"))
				tableName = kitsTable;
			else if (table.equalsIgnoreCase("Stats"))
				tableName = statsTable;

			ResultSet rs = executeCommand("SELECT UUID FROM " + tableName + " WHERE UUID = '" + uuid.toString() + "' LIMIT 1", true);
			if(rs == null) return false;
			
			boolean data = rs.next();
			rs.close();
			return data;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

	public static boolean isUserExists(UUID uuid) {

		if (uuid == null)
			return false;

		try {
			ResultSet rs = executeCommand(
					"SELECT PlayerName FROM " + playerTable + " WHERE UUID = '" + uuid.toString() + "'", true);
			if(rs == null) return false;
			
			boolean data = rs.next();
			rs.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void addUser(UUID uuid, String name, String table) {
		StringBuilder defaultKitPrefsBuilder = new StringBuilder();
		for (int i = 0; i < Main.ins.defaultKitPrefs; i++) 
			defaultKitPrefsBuilder.append("0");
		

		Connection conn = connection;
		PreparedStatement ps = null;
		try {

			String tableName = playerTable;
			if (table.equalsIgnoreCase("Kit"))
				tableName = kitsTable;
			if (table.equalsIgnoreCase("Stats"))
				tableName = statsTable;

			StringBuilder cmd = new StringBuilder();

			if (tableName.equalsIgnoreCase(playerTable)) {

				cmd.append("INSERT INTO ").append(playerTable)
						.append("(UUID,PlayerName,Skin,DisabledMaps,QueuePrefs,ColorPos1,ColorPos2,ColorPos1Alt,ColorPos2Alt) VALUES('").append(uuid.toString())
						.append("','").append(name).append("','','','1110','1','4','4','1')");
			} else if (tableName.equalsIgnoreCase(kitsTable)) {
				cmd.append("INSERT INTO ").append(kitsTable).append("(UUID,KitName,Selected,")
						.append("KitName1,KitInv1,KitArmor1,KitSettings1,Kit1Plays,Kit1Plays30,Kit1Plays24,")
						.append("KitName2,KitInv2,KitArmor2,KitSettings2,Kit2Plays,Kit2Plays30,Kit2Plays24,")
						.append("KitName3,KitInv3,KitArmor3,KitSettings3,Kit3Plays,Kit3Plays30,Kit3Plays24,")
						.append("KitName4,KitInv4,KitArmor4,KitSettings4,Kit4Plays,Kit4Plays30,Kit4Plays24,")
						.append("KitName5,KitInv5,KitArmor5,KitSettings5,Kit5Plays,Kit5Plays30,Kit5Plays24) ").append("VALUES('")
						.append(uuid).append("','").append(name).append("',").append("'1',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','')");

			} else if (tableName.equalsIgnoreCase(statsTable)) {
				cmd.append("INSERT INTO ").append(statsTable)
						.append("(UUID, RankPoints, Fights, FightsWon, Fights30, FightsWon30) ")
						.append("VALUES('" + uuid + "','0','0','0','0','0')");
			}

			if (!cmd.toString().equalsIgnoreCase("")) {
				executeCommand(cmd.toString(), false);
			}

			return;
		} catch (Exception ex) {
			Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return;
	}

	public static void addCustomUser(String uuid, String name, String table) {
		StringBuilder defaultKitPrefsBuilder = new StringBuilder();
		for (int i = 0; i < Main.ins.defaultKitPrefs; i++) 
			defaultKitPrefsBuilder.append("0");
		

		Connection conn = connection;
		PreparedStatement ps = null;
		try {

			String tableName = playerTable;
			if (table.equalsIgnoreCase("Kit"))
				tableName = kitsTable;
			if (table.equalsIgnoreCase("Stats"))
				tableName = statsTable;

			StringBuilder cmd = new StringBuilder();

			if (tableName.equalsIgnoreCase(playerTable)) {

				cmd.append("INSERT INTO ").append(playerTable)
						.append("(UUID,PlayerName,Skin,DisabledMaps,QueuePrefs,ColorPos1,ColorPos2,ColorPos1Alt,ColorPos2Alt) VALUES('").append(uuid.toString())
						.append("','").append(name).append("','','','1110','1','4','4','1')");
			} else if (tableName.equalsIgnoreCase(kitsTable)) {
				cmd.append("INSERT INTO ").append(kitsTable).append("(UUID,KitName,Selected,")
						.append("KitName1,KitInv1,KitArmor1,KitSettings1,Kit1Plays,Kit1Plays30,Kit1Plays24,")
						.append("KitName2,KitInv2,KitArmor2,KitSettings2,Kit2Plays,Kit2Plays30,Kit2Plays24,")
						.append("KitName3,KitInv3,KitArmor3,KitSettings3,Kit3Plays,Kit3Plays30,Kit3Plays24,")
						.append("KitName4,KitInv4,KitArmor4,KitSettings4,Kit4Plays,Kit4Plays30,Kit4Plays24,")
						.append("KitName5,KitInv5,KitArmor5,KitSettings5,Kit5Plays,Kit5Plays30,Kit5Plays24) ").append("VALUES('")
						.append(uuid).append("','").append(name).append("',").append("'1',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','',")
						.append("'-','','','" + defaultKitPrefsBuilder.toString() + "','','','')");

			} else if (tableName.equalsIgnoreCase(statsTable)) {
				cmd.append("INSERT INTO ").append(statsTable)
						.append("(UUID, RankPoints, Fights, FightsWon, Fights30, FightsWon30) ")
						.append("VALUES('" + uuid + "','0','0','0','0','0')");
			}

			if (!cmd.toString().equalsIgnoreCase("")) {
				executeCommand(cmd.toString(), false);
			}

			return;
		} catch (Exception ex) {
			Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return;
	}
	
	
	public static void updateUserName(UUID uuid, String name) {
		executeCommand("UPDATE " + playerTable + " SET PlayerName = '" + name + "' WHERE UUID = '" + uuid + "'", false);
	}

	public static void setKit(UUID uuid, String kit, boolean armor, String kitID) {

		if (!armor) {
			Database.executeCommand(
					"UPDATE " + kitsTable + " SET KitInv" + kitID + " = '" + kit + "' WHERE UUID = '" + uuid + "'",
					false);
		} else {
			Database.executeCommand(
					"UPDATE " + kitsTable + " SET KitArmor" + kitID + " = '" + kit + "' WHERE UUID = '" + uuid + "'",
					false);
		}
	}

	public static void setKit(String kitname, String kit, boolean armor, String kitID) {

		if (!armor) {
			Database.executeCommand("UPDATE " + kitsTable + " SET KitInv" + kitID + " = '" + kit + "' WHERE KitName = '"
					+ kitname + "'", false);
		} else {
			Database.executeCommand("UPDATE " + kitsTable + " SET KitArmor" + kitID + " = '" + kit
					+ "' WHERE KitName = '" + kitname + "'", false);
		}
	}

	public static String getKit(UUID uuid, boolean armor, String kitID) {

		if (!armor) {
			try {
				
				ResultSet rs = executeCommand("SELECT KitInv" + kitID + " FROM " + kitsTable + " WHERE UUID = '" + uuid + "'",true);
				if(rs == null) return "";
				String data = rs.getString("KitInv" + kitID);
				rs.close();
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				
				ResultSet rs = executeCommand("SELECT KitArmor" + kitID + " FROM " + kitsTable + " WHERE UUID = '" + uuid + "'",true);
				if(rs == null) return "";
				String data = rs.getString("KitArmor" + kitID);
				rs.close();
						
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public static String getCustomKit(String name, boolean armor) {

		if (!armor) {
			try {
				
				ResultSet rs = executeCommand("SELECT KitInv1 FROM " + kitsTable + " WHERE KitName = '" + name + "'",
						true);
				if(rs == null) return "";
				
					
					String data = rs.getString("KitInv1");
					rs.close();
					return data;
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ResultSet rs = executeCommand("SELECT KitArmor1 FROM " + kitsTable + " WHERE KitName = '" + name + "'",true);
				
				if(rs == null) return "";
				
				
					String data = rs.getString("KitArmor1");
					rs.close();
					return data;
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public static String resolveCustomKit(String name) {

		try {
			
			ResultSet rs = executeCommand("SELECT UUID FROM " + kitsTable + " WHERE KitName = '" + name + "'",
					true);
			if(rs == null) return "";
			
				
				String data = rs.getString("UUID");
				rs.close();
				return data;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getRawKitSettings(UUID uuid, int kitID) {
		if (kitID > 6 || kitID <= 0) {
			return null;
		}
		try {
			ResultSet rs = executeCommand(
					"SELECT KitSettings" + kitID + " FROM " + kitsTable + " WHERE UUID = '" + uuid + "'", true);

			if(rs == null) return "";
			
			
			String data = rs.getString("KitSettings" + kitID);
			rs.close();

			return data;
		} catch (SQLException e) {
		}

		return null;
	}
	
	public static String getRawKitSettings(String name) {
		try {
			ResultSet rs = executeCommand(
					"SELECT KitSettings1 FROM " + kitsTable + " WHERE KitName = '" + name + "'", true);
			if(rs == null) return "";
			
			String data = rs.getString("KitSettings1");
			rs.close();

			return data;
		} catch (SQLException e) {
		}

		return null;
	}
	
	public static void setRawKitSettings(String name, String settings) {
		if(Main.ins.database.isCustomKitExists(name)) {
			executeCommand("UPDATE " + kitsTable + " SET KitSettings1='" + settings + "' WHERE KitName = '" + name + "'", false);
		}
	}

	public static boolean isKitSettingEnabled(UUID uuid, int pref, int kitID) {

		if (pref > Main.ins.defaultKitPrefs || kitID > 6 || kitID <= 0) {
			return false;
		}

		try {
			ResultSet rs = executeCommand(
					"SELECT KitSettings" + kitID + " FROM " + kitsTable + " WHERE UUID = '" + uuid + "'", true);

			if(rs == null) return false;
			
			
			
			String[] data = rs.getString("KitSettings" + kitID).split("");
			rs.close();

			int[] intData = new int[Main.ins.defaultKitPrefs];

			for (int i = 0; i < data.length; i++) {
				if (i >= Main.ins.defaultKitPrefs) {
					break;
				}
				try {

					intData[i] = Integer.parseInt(data[i]);
				} catch (Exception e) {

					intData[i] = 0;
				}
			}

			if (intData[pref] >= 1)
				return true;
			else
				return false;

		} catch (SQLException e) {
		}
		return false;
	}

	public static void setKitPref(UUID uuid, int pref, boolean state, int kitID) {
		String[] raw = getRawKitSettings(uuid, kitID).split("");

		if (state)
			raw[pref] = "1";
		else
			raw[pref] = "0";

		StringBuilder builder = new StringBuilder();
		for (String str : raw)
			builder.append(str);

		executeCommand("UPDATE " + kitsTable + " SET KitSettings" + kitID + " = '" + builder.toString() + "' WHERE UUID='" + uuid + "'", false);

	}

	public static String getUserName(UUID uuid) {

		try {
			ResultSet rs = executeCommand("SELECT PlayerName FROM " + playerTable + " WHERE UUID = '" + uuid + "'",
					true);

			if(rs == null) return "";
			
			
			String data = rs.getString("PlayerName");

			rs.close();
			return data;

		} catch (Exception e) {
		}

		return "";
	}

	public static UUID getUUID(String name) {
		
		try {

			ResultSet rs = executeCommand("SELECT UUID FROM " + playerTable + " WHERE PlayerName='" + name + "' ",
					true);

			if(rs == null) return null;
			
			
			
			UUID uuid = UUID.fromString(rs.getString("UUID"));
			
			
			
			return uuid;

		} catch (Exception e) {
		}

		return null;
	}

	public static int getSelectedKit(UUID uuid) {

		try {

			ResultSet rs = executeCommand("SELECT Selected FROM " + kitsTable + " WHERE UUID='" + uuid.toString() + "'",
					true);

			if(rs == null) return 1;
			
			
			int data = rs.getInt("Selected");

			rs.close();

			if (data <= 0 || data > 5)
				return 1;
			return data;

		} catch (Exception e) {
		}

		return 1;
	}

	public static void setSelectedKit(UUID uuid, int kit) {
		if (kit <= 0 || kit > 5)
			return;
		executeCommand("UPDATE " + kitsTable + " SET Selected='" + kit + "'" + " WHERE UUID='" + uuid.toString() + "'",
				false);
	}

	public static String getKitName(UUID uuid, int kit) {
		try {

			if (kit <= 0 || kit > 5)
				return "-";

			ResultSet rs = executeCommand(
					"SELECT KitName" + kit + " FROM " + kitsTable + " WHERE UUID='" + uuid.toString() + "'", true);

			if(rs == null) return "";
			
			
			String data = rs.getString("KitName" + kit);
			rs.close();
			return data;

		} catch (Exception e) {
		}
		return "-";
	}

	public static void setKitName(UUID uuid, int kit, String name) {
		executeCommand(
				"UPDATE " + kitsTable + " SET KitName" + kit + "='" + name + "' WHERE UUID='" + uuid.toString() + "'",
				false);
	}

	public static String getRawQueueSettings(UUID uuid) {
		try {
			ResultSet rs = executeCommand("SELECT QueuePrefs FROM " + playerTable + " WHERE UUID = '" + uuid + "'",
					true);
			
			if(rs == null) return "";
			
			String data = rs.getString("QueuePrefs");
			rs.close();

			return data;
		} catch (SQLException e) {
		}

		return null;
	}

	
	
	/**
	 * 
	 * @param uuid
	 * @param pref
	 * @return
	 * 
	 * 0: Autoqueue
	 * 1: Kit
	 * 2: Bestof
	 * 3: RankedType
	 */
	public static int isQueuePrefEnabled(UUID uuid, int pref) {

		if (pref > 4) {
			return -1;
		}

		try {
			ResultSet rs = executeCommand("SELECT QueuePrefs FROM " + playerTable + " WHERE UUID = '" + uuid + "'",
					true);

			if(rs == null) return 1;
			
			
			String[] data = rs.getString("QueuePrefs").split("");
			rs.close();

			int[] intData = new int[4];

			for (int i = 0; i < data.length; i++) {
				if (i >= 4) {
					break;
				}
				try {

					intData[i] = Integer.parseInt(data[i]);
				} catch (Exception e) {

					intData[i] = 0;
				}
			}

			return intData[pref];
		} catch (SQLException e) {
		}
		return -1;
	}

	public static void setQueuePref(UUID uuid, int pref, int state) {
		if (pref > 3)
			return;

		String[] raw = getRawQueueSettings(uuid).split("");

		raw[pref] = "" + state;

		StringBuilder builder = new StringBuilder();
		for (String str : raw)
			builder.append(str);

		executeCommand("UPDATE " + playerTable + " SET QueuePrefs='" + builder.toString() + "' WHERE UUID='"
				+ uuid.toString() + "'", false);

	}

	public static boolean isMapDisabled(UUID uuid, String mapName) {
		try {

			ResultSet rs = executeCommand(
					"SELECT DisabledMaps FROM " + playerTable + " WHERE UUID = '" + uuid.toString() + "'", true);

			if(rs == null) return false;
			
			
			String data = rs.getString("DisabledMaps");

			rs.close();

			if (data.contains(mapName))
				return true;
			else
				return false;

		} catch (Exception e) {
		}
		return false;
	}

	public static String getDisabledMaps(UUID uuid) {
		try {
			ResultSet rs = executeCommand(
					"SELECT DisabledMaps FROM " + playerTable + " WHERE UUID = '" + uuid.toString() + "'", true);

			if(rs == null) return "";
			
			
			String data = rs.getString("DisabledMaps");
			rs.close();
			return data;
		} catch (SQLException e) {}

		return "";
	}

	public static void setMapDisabled(UUID uuid, String MapName, boolean disabled) {
		
		try {

			String[] disabledList = getDisabledMaps(uuid).split(" ");
			for (int i = 0; i < disabledList.length; i++) {
				if (disabledList[i].equalsIgnoreCase(MapName)) {
					disabledList[i] = "";
				}
			}

			String disabledMaps = "";
			for (int i = 0; i < disabledList.length; i++) {
				disabledMaps = disabledMaps + disabledList[i] + " ";
			}
			if (disabled) {
				disabledMaps = disabledMaps + MapName;
			}

			executeCommand("UPDATE " + playerTable + " SET DisabledMaps = '" + disabledMaps + "' WHERE UUID = '"
					+ uuid.toString() + "'", false);

		} catch (Exception localSQLException) {}

	}

	
	/**
	 * 
	 * @param uuid
	 * @param amount
	 * @param timed 1 = All-Time 2 = 30-Days 3 = 24h
	 * @param type 1 = Fights 2 = FightsWon 3 = FightsLost 4 = FightsEz 5 = FightsTight 6 = FightsKills 7 = FightsDeath 8 = FightWL 9 = FightsKD 
	 */
	public static void setStats(UUID uuid, float amount, int timed, int type) {
		
		
		StringBuilder cmd = new StringBuilder();
		
		cmd.append("Fights");
		
		if(type == 2) cmd.append("Won"); 
		if(type == 3) cmd.append("Lost"); 
		if(type == 4) cmd.append("Ez"); 
		if(type == 5) cmd.append("Tight"); 
		if(type == 6) cmd.append("Kills"); 
		if(type == 7) cmd.append("Deaths"); 
		if(type == 8) cmd.append("WL"); 
		if(type == 9) cmd.append("KD"); 
		
		if(timed == 2) cmd.append("30");
		if(timed == 3) cmd.append("24");
		
		executeCommand("UPDATE " + statsTable + " SET " + cmd.toString() +" = '" + amount + "' WHERE UUID = '" + uuid.toString() + "'", false);
		
		

		
	}
	
	
	
	
	/**
	 * 
	 * @param uuid
	 * @param amount
	 * @param timed 1 = All-Time 2 = 30-Days 3 = 24h
	 * @param subid 1-5
	 */
	public static void setKitStats(String uuid, int amount, int timed, int subID) {
		
		StringBuilder cmd = new StringBuilder();
		 
		cmd.append("Kit").append(subID).append("Plays");
		
		
		
		if(timed == 2) cmd.append("30");
		if(timed == 3) cmd.append("24");
		
		executeCommand("UPDATE " + kitsTable + " SET " + cmd.toString() +" = '" + amount + "' WHERE UUID = '" + uuid.toString() + "'", false);
	}
	
	
	//TODO STATSTEIL
	public static float getStats(UUID uuid, int timed, int type) {
		try {
			
			
			String data;
			
			StringBuilder cmd = new StringBuilder();
			
			cmd.append("Fights");
			
			if(type == 2) cmd.append("Won"); 
			if(type == 3) cmd.append("Lost"); 
			if(type == 4) cmd.append("Ez"); 
			if(type == 5) cmd.append("Tight"); 
			if(type == 6) cmd.append("Kills"); 
			if(type == 7) cmd.append("Deaths"); 
			if(type == 8) cmd.append("WL"); 
			if(type == 9) cmd.append("KD"); 
			
			if(timed == 2) cmd.append("30");
			if(timed == 3) cmd.append("24");
			
			
			
			
			ResultSet rs = executeCommand("SELECT " + cmd.toString() + " FROM " + statsTable + " WHERE UUID = '" + uuid.toString() + "'", true);
			
			if(rs == null) return 0;
			
			
			try {
				
				data = rs.getString(cmd.toString());
				
				float value = Float.parseFloat(data);
				
				rs.close();
				
				return value;
			} catch (NumberFormatException | NullPointerException e) {
				return 0;
			} 
		} catch (SQLException e) {}
		
		
		return 0;
	}
	
	public static int getKitStats(String uuid, int timed, int subID) {
		try {
			
			
			String data;
			
			StringBuilder cmd = new StringBuilder();
			
			cmd.append("Kit").append(subID).append("Plays");
			
			
			
			if(timed == 2) cmd.append("30");
			if(timed == 3) cmd.append("24");
			
			
			
			
			ResultSet rs = executeCommand("SELECT " + cmd.toString() + " FROM " + kitsTable + " WHERE UUID = '" + uuid.toString() + "'", true);
			
			if(rs == null) return 0;
			
			
			try {
				data = rs.getString(cmd.toString());
				
				int value = Integer.parseInt(data);
				
				rs.close();
				return value;
			} catch (NumberFormatException | NullPointerException e) {
				return 0;
			} 
		} catch (SQLException e) {}
		
		
		return 0;
	}
	
	public static HashMap<Integer, PlayerStatsData> getTop(int timed) {
		HashMap<Integer, PlayerStatsData> datas = new HashMap<>();
		try {
			ResultSet rs = null;
			if(timed == 1) {
				rs = executeCommand("SELECT * from " + statsTable + " order by CAST(FightsWon AS UNSIGNED) desc LIMIT " + Main.ins.topPlaces , true);
			} else if(timed == 2) {
				rs = executeCommand("SELECT * from " + statsTable + " order by CAST(FightsWon30 AS UNSIGNED) desc LIMIT " + Main.ins.topPlaces , true);
			} else if(timed == 3) {
				rs = executeCommand("SELECT * from " + statsTable + " order by CAST(FightsWon24 AS UNSIGNED) desc LIMIT " + Main.ins.topPlaces , true);
			}
			
			
		  	int place = 1;
		  	
		  	if(rs == null) return datas;
		  	
			while(rs.next()) {
				
				try {
					
					String sUUID = rs.getString("UUID");
					
					if(sUUID.equalsIgnoreCase("Timestamps")) {
						continue;
					}
					
					UUID uuid = UUID.fromString(sUUID);
					
					
					int fights = (int) getStats(uuid, 1, 1);
					int fights30 = (int) getStats(uuid, 2, 1);
					int fights24 = (int) getStats(uuid, 3, 1);
					
					int fightsWon = (int) getStats(uuid, 1, 2);
					int fightsWon30 = (int) getStats(uuid, 2, 2);
					int fightsWon24 = (int) getStats(uuid, 3, 2);
					
					
					PlayerStatsData data = new PlayerStatsData(place, 
							uuid, 
							getUserName(uuid), 
							fightsWon, 
							fights, 
							fightsWon30, 
							fights30,
							fightsWon24,
							fights24);
					
					
					datas.put(place, data);
					
					
					place++;
					
					
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				
			}
			
			rs.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return datas;
	}
	
	
	public static void updateSkin(UUID uuid, String texture) {
		executeCommand("UPDATE " + playerTable + " SET Skin='" + texture + "' WHERE UUID='" + uuid.toString() + "'", false);
	}
	
	public static String getSkin(UUID uuid) {
		try {
			
			
			ResultSet rs = executeCommand("SELECT Skin FROM " + playerTable + " WHERE UUID='" + uuid.toString() + "'", true);
			
			if(rs == null) return "";
			
			String data = rs.getString("Skin");
			
			rs.close();
			
			
			
			return data;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static void updateUserData(final Player p) {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(!p.isOnline()) return;
				
				updateUserName(p.getUniqueId(), p.getName());

				try {
					Object playerHandle = p.getClass().getMethod("getHandle").invoke(p);
					Method getProfile = playerHandle.getClass().getMethod("getProfile");
					
					
					GameProfile profile = (GameProfile) getProfile.invoke(playerHandle);
					
					for(Property pp : profile.getProperties().values()) {
						if(pp.getName().equalsIgnoreCase("textures")) {
							updateSkin(p.getUniqueId(), pp.getValue());//TODO
						}
					}
					
				} catch (IllegalAccessException |
						IllegalArgumentException | 
						InvocationTargetException | 
						NoSuchMethodException | 
						SecurityException e) {
					System.err.print("A Error accourd while updating Skin Value...");
					e.printStackTrace();
					
				} 

				
			}
		}.runTaskAsynchronously(Main.ins);
	}
	
	/**
	 * 
	 * @param uuid
	 * @param timed 0 = Alltime, 1 = 30d, 2 = 24h
	 * @return
	 */
	public static int getPosition(UUID uuid, int timed) {
		 try {
		  
		   ResultSet rs = null;
		   
		   if(timed == 1) {
			   rs = executeCommand("SELECT * FROM " + statsTable + " ORDER BY FightsWon desc", true);
		   } else if(timed == 2) {
			   rs = executeCommand("SELECT * FROM " + statsTable + " ORDER BY FightsWon30 desc", true);
		   } else if(timed == 3) {
			   rs = executeCommand("SELECT * FROM " + statsTable + " ORDER BY FightsWon24 desc", true);
		   }
		   
		   
		   
		   if(rs == null) return -1;
		   
		   int place = 1;
		   
		   while (rs.next()) {
		   
			   String id = rs.getString("UUID");
			   
			   if (id.equalsIgnoreCase(uuid.toString())) {
				   rs.close();
				   return place;
			   }
			   place++;
		   }
		   return place;
		 
	
	
		 } catch (Exception e) {
			
		 }
		 return -1;
	}
	
	public static void updateRankPoints(UUID uuid, int amount) {
		int points = getRankPoints(uuid)+amount;
		if(points < 0) return;
		executeCommand("UPDATE " + statsTable + " SET RankPoints=  " + points + " WHERE UUID = '" + uuid.toString() + "'", false);
	}
	
	public static int getRankPoints(UUID uuid) {
		try {
			ResultSet rs = executeCommand("SELECT RankPoints FROM " + statsTable + " WHERE UUID='" + uuid.toString() + "'", true);
			if(rs == null) return 0;
			
			int data = rs.getInt("RankPoints");
			rs.close();
			return data;
		} catch (SQLException e) {}
		
		return -1;
	}
	
	public static int getColor(UUID uuid, boolean pos1, boolean alternate) {
		try {
			
			StringBuilder searched = new StringBuilder().append("ColorPos");
			if(pos1) searched = searched.append("1");
			else searched = searched.append("2");
			if(alternate) searched = searched.append("Alt");
			
			
			ResultSet rs = executeCommand("SELECT " + searched.toString() + " FROM " + playerTable + " WHERE UUID='" + uuid.toString() + "'", true);
			if(rs == null) return 0;
			int data = rs.getInt(searched.toString());
			rs.close();
			return data;
		} catch (SQLException e) {}
		
		return -1;
	}
	
	public static void setColor(UUID uuid, boolean pos1, boolean alternate, OvOColor color) {
		StringBuilder searched = new StringBuilder().append("ColorPos");
		if(pos1) searched = searched.append("1");
		else searched = searched.append("2");
		if(alternate) searched = searched.append("Alt");
		executeCommand("UPDATE " + playerTable + " SET " + searched.toString() + "=  " + color.getSubID() + " WHERE UUID = '" + uuid.toString() + "'", false);
	}
	
	/**
	 * 1: Player Kit
	 * 2: Custom Kit
	 * 3: Kit not found
	 */
	public static int getKitType(String id) {
		try {
			if(Main.ins.database.isUserExists(UUID.fromString(id), kitsTable)) 
				return 1;
			
		} catch (Exception e) {
			if(Main.ins.database.isUserExists(Main.ins.database.getUUID(id), kitsTable)) 
				return 1;
			if(Main.ins.database.isCustomKitExists(id)) 
				return 2;
		}
		
		return 3;
	}
	
	public static boolean isCustomKitExists(String name) {
		ResultSet rs = 
				executeCommand("SELECT UUID FROM " + 
				kitsTable + " WHERE KitName = '" + 
				name + "'", 
				true);
		
		if(rs == null) return false;
		try {
			String uuid = rs.getString("UUID");
			if(uuid.contains("CUSTOM")) return true;
		} catch (SQLException e) {}
		
		
		return false;
	}
	
	public static void setCustomKitData(String name, String kit, String armor, String prefs) {
		if(Main.ins.database.getKitType(name) == 3) {
			addCustomUser("CUSTOM" + UUID.randomUUID(), name, "Kit");
		}
		setKit(name, kit, false, "1");
		setKit(name, armor, true, "1");
		setRawKitSettings(name, prefs);
	}
	
	public static void setLastReset(int timed, boolean override) {
			
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ResultSet rs = executeCommand("SELECT * FROM " + statsTable + " WHERE UUID = 'Timestamps'", true);
				try {
					if(rs != null && rs.getString("UUID") != null) {
						
						long lastReset30d = rs.getLong("FightsEz30");
						long lastReset24h = rs.getLong("FightsEz24");
						
						rs.close();
						
						if(((System.currentTimeMillis()/1000)-(lastReset24h/1000)) >= ((long)86400) || override) {
							Bukkit.broadcastMessage("§aStats letzte 24h werden resetet!");
							
							
							executeCommand("UPDATE " + statsTable + " SET Fights24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsWon24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsLost24 = 0", false);
							
							executeCommand("UPDATE " + statsTable + " SET FightsEz24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsTight24 = 0", false);
							
							executeCommand("UPDATE " + statsTable + " SET FightsKills24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsDeaths24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsKD24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsWL24 = 0", false);
							
							executeCommand("UPDATE " + statsTable + " SET Fights24 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsEz24 = '" + System.currentTimeMillis() + "' WHERE UUID = 'Timestamps'", false);
							
						}
						
						if(((System.currentTimeMillis()/1000)-(lastReset30d/1000) >= ((long)2592000)) || override) {
							Bukkit.broadcastMessage("§aStats letzte 30d werden resetet!");
							executeCommand("UPDATE " + statsTable + " SET Fights30 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsWon30 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsLost30 = 0", false);
							
							executeCommand("UPDATE " + statsTable + " SET FightsEz30 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsTight30 = 0", false);
							
							executeCommand("UPDATE " + statsTable + " SET FightsKills30 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsDeaths30 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsKD30 = 0", false);
							executeCommand("UPDATE " + statsTable + " SET FightsWL30 = 0", false);
							
							executeCommand("UPDATE " + statsTable + " SET FightsEz30 = '" + System.currentTimeMillis() + "' WHERE UUID = 'Timestamps'", false);
							
						}
						
						
						
					} else {
						executeCommand("INSERT INTO " + statsTable + " (UUID) VALUES ('Timestamps')", false);
						setLastReset(timed, false);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.ins);
		
	
	}
	
	
	public static void addFight(long time, UUID id, 
			ArrayList<UUID> pos1List, ArrayList<UUID> pos2List,
			ArrayList<String> pos1Names, ArrayList<String> pos2Names,
			boolean pos1Wins, String colorPos1, String colorPos2,
			ArrayList<Float> pos1Health, ArrayList<Float> pos2Health, 
			String arena, String map, boolean ranked, UUID tournament,
			String tName,  String itemName, int subID, String kit,
			int duration, 
			ArrayList<Integer> kills1, ArrayList<Integer> kills2) {
			
			StringBuilder cmd = new StringBuilder();
			
			cmd.append("INSERT INTO ").append(fightsTable)
			.append("(Time, ID,Pos1List,Pos2List,Pos1ListNames,Pos2ListNames,")
			.append("pos1Win,Color1,Color2,Win1Health,Win2Health,")
			.append("Arena,Map,Ranked,Tournament,Tname,")
			.append("Item,SubID,Kit,Duration,Kills1,Kills2) ")
			.append("VALUES(")
			.append("'").append(time).append("',")
			.append("'").append(id).append("',")//ID
			.append("'").append(pos1List.toString()).append("',")
			.append("'").append(pos2List.toString()).append("',")
			.append("'").append(pos1Names.toString()).append("',")
			.append("'").append(pos2Names.toString()).append("',")
			.append("'").append(pos1Wins).append("',")
			.append("'").append(colorPos1).append("',")
			.append("'").append(colorPos2).append("',")
			.append("'").append(pos1Health).append("',")
			.append("'").append(pos2Health).append("',")
			.append("'").append(arena).append("',")
			.append("'").append(map).append("',")
			.append("'").append(ranked).append("',")
			.append("'").append(tournament).append("',")
			.append("'").append(tName).append("',")
			.append("'").append(itemName).append("',")
			.append("'").append(subID).append("',")
			.append("'").append(kit).append("',")
			.append("'").append(duration).append("',")
			.append("'").append(kills1.toString()).append("',")
			.append("'").append(kills2.toString()).append("')");
			
			executeCommand(cmd.toString(), false);
			
			
	}
	
	public static FightInfo loadFightInfo(UUID uuid) {
		if(uuid == null) return null;
		
		ResultSet rs = executeCommand("Select * FROM " + fightsTable + " WHERE ID='" + uuid.toString() + "'", true);
		
		try {
			long timeStamp = rs.getLong("Time");
			UUID resultID = UUID.fromString(rs.getString("ID"));
			
			List<String> lID1 = 
					Arrays.asList(rs.getString("Pos1List")
							.replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));		
			
			ArrayList<UUID> uuidPos1 = new ArrayList<>();
			
			for(String str :  lID1) 
				uuidPos1.add(UUID.fromString(str));
			
			
			List<String> lID2 = Arrays.asList(rs.getString("Pos2List").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));
			ArrayList<UUID> uuidPos2 = new ArrayList<>();
			for(String str :  lID2) uuidPos2.add(UUID.fromString(str.replaceAll(" ", "")));
			
			ArrayList<String> namesPos1 = new ArrayList<>();
			ArrayList<String> namesPos2 = new ArrayList<>();
			
			List<String> name1 = Arrays.asList(rs.getString("Pos1ListNames").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));
			List<String> name2 = Arrays.asList(rs.getString("Pos2ListNames").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));
			namesPos1.addAll(name1);
			namesPos2.addAll(name2);
			
			
			
			boolean pos1Win = rs.getString("pos1Win").equalsIgnoreCase("true");
			String colorPos1 = rs.getString("Color1");
			String colorPos2 = rs.getString("Color2");
			
			
			List<String> winHealth1 = Arrays.asList(rs.getString("Win1Health").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));			
			ArrayList<Float> health1 = new ArrayList<>();
			for(String str :  winHealth1) {
				try {
					
					health1.add(Float.parseFloat(str));
				} catch (NumberFormatException e) {
					health1.add(0.0F);
					
				}
			}
			List<String> winHealth2 = Arrays.asList(rs.getString("Win2Health").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));			
			ArrayList<Float> health2 = new ArrayList<>();
			for(String str :  winHealth2) {
				try {
					health2.add(Float.parseFloat(str));
				} catch (NumberFormatException e) {
					health2.add(0.0F);
				}
			}
			
			
			String arena = rs.getString("Arena");
			String map =  rs.getString("Map");
			boolean ranked = rs.getString("Ranked").equalsIgnoreCase("true");
			
			String tournamentUUID = rs.getString("Tournament");
			UUID tournament = null;
			if(tournamentUUID != null && !tournamentUUID.equalsIgnoreCase("null") && !tournamentUUID.equalsIgnoreCase("")) {
				tournament = UUID.fromString(tournamentUUID);
			}
			
			String tName = rs.getString("Tname");
			
			String item = rs.getString("Item");
			int subID = rs.getInt("SubID");
			String kit = rs.getString("Kit");
			int duration = rs.getInt("Duration");
			
			List<String> killL1 = Arrays.asList(rs.getString("Kills1").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));			
			ArrayList<Integer> kills1 = new ArrayList<>();
			for(String str :  killL1) {
				try {
					kills1.add(Integer.parseInt(str));
				} catch (NumberFormatException e) {
					kills1.add(0);
				}
			}
			List<String> killL2 = Arrays.asList(rs.getString("Kills2").replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "").split(","));
			ArrayList<Integer> kills2 = new ArrayList<>();
			for(String str :  killL2) {
				try {
					kills2.add(Integer.parseInt(str));
				} catch (NumberFormatException e) {
					kills2.add(0);
				}
			}
			
			
			return new FightInfo(timeStamp, resultID, uuidPos1, uuidPos2, 
					namesPos1, namesPos2, pos1Win, colorPos1, colorPos2, 
					health1, health2, arena, map, ranked, tournament, 
					tName, item, subID, kit, duration, kills1, kills2);
			
			
		} catch (Exception e) {
			Bukkit.broadcastMessage("§aFehler");
			
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static ArrayList<FightInfo> getAllFightsForUUID(UUID player, int offset) {
		ArrayList<FightInfo> fightinfos = new ArrayList<>();
		
		ResultSet rs = executeCommand("SELECT ID FROM FightsData WHERE Pos1List LIKE \"%" + player.toString() + "%\" OR Pos2List LIKE \"%" + player.toString() + "%\" ORDER BY Time DESC LIMIT " + ((8*6)+1) + " OFFSET " + offset*8, true);
		
		if(rs != null) {
			try {
				while(rs.next()) {
					fightinfos.add(loadFightInfo(UUID.fromString(rs.getString("ID"))));
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		 
		return fightinfos;
	}
	
	public static ArrayList<FightInfo> getAllFights(int offset) {
		ArrayList<FightInfo> fightinfos = new ArrayList<>();
		
		ResultSet rs = executeCommand("SELECT ID FROM FightsData ORDER BY Time DESC LIMIT " + ((8*6)+1) + " OFFSET " + offset*8, true);
		
		if(rs != null) {
			try {
				while(rs.next()) {
					fightinfos.add(loadFightInfo(UUID.fromString(rs.getString("ID"))));
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		 
		return fightinfos;
	}
	
	
	//SELECT * FROM FightsData WHERE Pos2ListNames LIKE "%Test12%" OR Pos1ListNames LIKE "%Test12%";
	
	
	
	
}
//
//
// public static boolean isDefaultExists() {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, "default");
// ResultSet rs = ps.executeQuery();
// boolean data = rs.next();
// closeStatments(ps, conn);
// return data;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return false;
// }
//
// public static boolean isNameRegistered(String Name) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT UUID FROM " + table + " WHERE PlayerName =
// ?");
// ps.setString(1, Name);
//
//
//
// ResultSet rs = ps.executeQuery();
//
//
// if(rs.next()) {
// String str = rs.getString("UUID");
// //closeStatments(ps, conn);
// if(str.startsWith("-") || str.startsWith("CUSTOM")) return false;
// } else {
// return false;
// }
//
// return true;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return false;
// }
//
//
//
// public static void addDefault() {
// StringBuilder defaultKitPrefsBuilder = new StringBuilder();
// for (int i = 0; i < Main.ins.defaultKitPrefs; i++) {
// if (i == 0) {
// defaultKitPrefsBuilder.append("f");
// } else {
// defaultKitPrefsBuilder.append(" f");
// }
// }
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
//
// ps = conn.prepareStatement("INSERT INTO " + table + " (" + "PlayerName"
// + ",UUID"
// + ",KitInv"
// + ",KitArmor"
// + ",Settings"
// + ",QuequePrefs"
// + ",KitInv2"
// + ",KitArmor2"
// + ",KitSettings2"
// + ",KitInv3"
// + ",KitArmor3"
// + ",KitSettings3"
// + ",KitInv4"
// + ",KitArmor4"
// + ",KitSettings4"
// + ",KitInv5"
// + ",KitArmor5"
// + ",KitSettings5"
// + ",Fights"
// + ",FightsWon"
// + ",DefaultKit"
// + ",DisabledMaps"//22
// + ",Fights30"
// + ",FightsWon30"
// + ",Kit1Plays"
// + ",Kit1Plays30"
// + ",Kit2Plays"
// + ",Kit2Plays30"
// + ",Kit3Plays"
// + ",Kit3Plays30"
// + ",Kit4Plays"
// + ",Kit4Plays30"
// + ",Kit5Plays"
// + ",Kit5Plays30"
// + ",Kit1Plays24h"
// + ",Kit2Plays24h"
// + ",Kit3Plays24h"
// + ",Kit4Plays24h"
// + ",Kit5Plays24h"//38
// + ")"
// + "
// VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
// ps.setString(1, "-");
// ps.setString(2, "default");
// ps.setString(3, "");
// ps.setString(4, "");
// ps.setString(5, "");
// ps.setString(6, "2 1");
// ps.setString(7, "");
// ps.setString(8, "");
// ps.setString(9, "" + defaultKitPrefsBuilder.toString());
// ps.setString(10, "");
// ps.setString(11, "");
// ps.setString(12, "" + defaultKitPrefsBuilder.toString());
// ps.setString(13, "");
// ps.setString(14, "");
// ps.setString(15, "" + defaultKitPrefsBuilder.toString());
// ps.setString(16, "");
// ps.setString(17, "");
// ps.setString(18, "" + defaultKitPrefsBuilder.toString());
// ps.setString(19, "0");
// ps.setString(20, "0");
// ps.setString(21, "1");
// ps.setString(22, "1");
//
// ps.setString(23, "0");
// ps.setString(24, "0");
// ps.setString(25, "0");
// ps.setString(26, "0");
// ps.setString(27, "0");
// ps.setString(28, "0");
// ps.setString(29, "0");
// ps.setString(30, "0");
// ps.setString(31, "0");
// ps.setString(32, "0");
// ps.setString(33, "0");
// ps.setString(34, "0");
// ps.setString(35, "0");
// ps.setString(36, "0");
// ps.setString(37, "0");
// ps.setString(38, "0");
// ps.setString(39, "0");
// ps.executeUpdate();
// closeStatments(ps, conn);
// return;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// }
//
// public static void updateUserName(UUID uuid, String Name) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET PlayerName = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, Name);
// ps.executeUpdate();
// closeStatments(ps, conn);
// return;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// }
//
// public static String getUserName(UUID uuid) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, uuid.toString());
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("PlayerName");
// closeStatments(ps, conn);
// return data;
// }
// return null;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return null;
// }
//
// public static UUID getUserID(String Name) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT UUID FROM " + table + " WHERE PlayerName =
// ?");
// ps.setString(1, Name);
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// try {
// UUID uuid = UUID.fromString(rs.getString("UUID"));
// closeStatments(ps, conn);
// return uuid;
// } catch (Exception e) {
//
// return null;
// }
//
// }
// return null;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return null;
// }
//
// public static boolean getPrefDefault(int pref) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID =
// ?");
//
// ps.setString(1, "default");
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String[] prefs = rs.getString("Settings").split(" ");
// if ((pref >= prefs.length) || (pref < 0)) {
// closeStatments(ps, conn);
// return false;
// }
// if (prefs[pref].equalsIgnoreCase("t")) {
// closeStatments(ps, conn);
// return true;
// }
// return false;
// }
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
//
// return false;
// }
//
// public static boolean getPref(UUID uuid, int pref, String KitID) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID =
// ?");
// if ((!KitID.equalsIgnoreCase("")) && (!KitID.equalsIgnoreCase("1"))) {
// ps = conn.prepareStatement("SELECT KitSettings" + KitID + " FROM " + table +
// " WHERE UUID = ?");
// }
// ps.setString(1, uuid.toString());
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// if ((!KitID.equalsIgnoreCase("")) && (!KitID.equalsIgnoreCase("1"))) {
// String[] prefs = rs.getString("KitSettings" + KitID).split(" ");
// if ((pref >= prefs.length) || (pref < 0)) {
// closeStatments(ps, conn);
// return false;
// }
// if (prefs[pref].equalsIgnoreCase("t")) {
// closeStatments(ps, conn);
// return true;
// }
// return false;
// }
// String[] prefs = rs.getString("Settings").split(" ");
// if ((pref >= prefs.length) || (pref < 0)) {
// closeStatments(ps, conn);
// return false;
// }
// if (prefs[pref].equalsIgnoreCase("t")) {
// closeStatments(ps, conn);
// return true;
// }
// return false;
// }
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return false;
//
// }
//
// public static String[] getRawPrefDefault() {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID =
// ?");
//
// ps.setString(1, "default");
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String[] prefs = rs.getString("Settings").split(" ");
// closeStatments(ps, conn);
// return prefs;
// }
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
//
// return null;
// }
//
// public static String[] getRawPref(UUID uuid, String KitID) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID =
// ?");
// if (!KitID.equalsIgnoreCase("")) {
// ps = conn.prepareStatement("SELECT KitSettings" + KitID + " FROM " + table +
// " WHERE UUID = ?");
// }
// ps.setString(1, uuid.toString());
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
//
// if (KitID.equalsIgnoreCase("")) {
// String[] prefs = rs.getString("Settings").split(" ");
// return prefs;
// }
// String[] prefs = rs.getString("KitSettings" + KitID).split(" ");
// closeStatments(ps, conn);
// return prefs;
// }
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
//
// return null;
// }
//
// public static boolean setStats(UUID uuid, int Higher, String Stat, boolean
// timed) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if(!timed) {
// if (Stat.equalsIgnoreCase("FightsWon")) {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET FightsWon = ? WHERE UUID
// = ?");
// ps.setString(2, uuid.toString());
// int newStat = Integer.parseInt(getStats(uuid, Stat, timed));
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
// } else if (Stat.equalsIgnoreCase("Fights")) {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Fights = ? WHERE UUID =
// ?");
// ps.setString(2, uuid.toString());
// int newStat = Integer.parseInt(getStats(uuid, Stat, timed));
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
// }
// } else {
// if (Stat.equalsIgnoreCase("FightsWon")) {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET FightsWon30 = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
// int newStat = 0;
// if(getStats(uuid, Stat, true) != null)
// newStat = Integer.parseInt(getStats(uuid, Stat, true));
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
// } else if (Stat.equalsIgnoreCase("Fights")) {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Fights30 = ? WHERE UUID
// = ?");
// ps.setString(2, uuid.toString());
// int newStat = 0;
// if(getStats(uuid, Stat, true) != null)
// newStat = Integer.parseInt(getStats(uuid, Stat, true));
//
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
// }
// }
//
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
//
// public static boolean setPrefDefault(int Pref, boolean state) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (isDefaultExists()) {
// try {
// if (Pref >= getRawPrefDefault().length) updatePrefDefault();
//
// String[] setThis = getRawPrefDefault();
// if (state) {
// setThis[Pref] = "t";
// } else {
// setThis[Pref] = "f";
// }
// boolean first = true;
// String build = "";
// for (int i = 0; i < setThis.length; i++) {
// if (first) {
// build = setThis[i];
// first = false;
// } else {
// build = build + " " + setThis[i];
// }
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID
// = ?");
//
// ps.setString(2, "default");
// ps.setString(1, build);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// addDefault();
// try {
// if (Pref > getRawPrefDefault().length) {
// return false;
// }
// String[] setThis = getRawPrefDefault();
// if (state) {
// setThis[Pref] = "t";
// } else {
// setThis[Pref] = "f";
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID
// = ?");
// ps.setString(2, "default");
// ps.setString(1, setThis.toString());
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return false;
// }
//
// public static boolean setPref(UUID uuid, int Pref, boolean state, String
// KitID) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (isUserExists(uuid)) {
// try {
// if (Pref >= getRawPref(uuid, KitID).length) {
// return false;
// }
//
// String[] setThis = getRawPref(uuid, KitID);
// if (state) {
// setThis[Pref] = "t";
// } else {
// setThis[Pref] = "f";
// }
// boolean first = true;
// String build = "";
// for (int i = 0; i < setThis.length; i++) {
// if (first) {
// build = setThis[i];
// first = false;
// } else {
// build = build + " " + setThis[i];
// }
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID
// = ?");
// if (!KitID.equalsIgnoreCase("")) {
// ps = conn.prepareStatement("UPDATE " + table + " SET KitSettings" + KitID + "
// = ? WHERE UUID = ?");
// }
// ps.setString(2, uuid.toString());
// ps.setString(1, build);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// if (Bukkit.getPlayer(uuid) != null) {
// addUser(uuid, Bukkit.getPlayer(uuid).getName());
// try {
// if (Pref >= getRawPref(uuid, KitID).length) {
// return false;
// }
//
// String[] setThis = getRawPref(uuid, KitID);
// if (state) {
// setThis[Pref] = "t";
// } else {
// setThis[Pref] = "f";
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID
// = ?");
// if (!KitID.equalsIgnoreCase("")) {
// ps = conn.prepareStatement("UPDATE " + table + " SET KitSettings" + KitID + "
// = ? WHERE UUID = ?");
// }
// ps.setString(2, uuid.toString());
// ps.setString(1, setThis.toString());
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// }
// return false;
// }
//
// public static PlayerQuequePrefs getQuequePrefState(UUID uuid) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID
// = ?");
//
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String State = rs.getString("QuequePrefs").split(" ")[0];
// if (State == null) {
// return null;
// }
// if (State.equalsIgnoreCase("1")) {
// closeStatments(ps, conn);
// return PlayerQuequePrefs.ownKit;
// }
// if (State.equalsIgnoreCase("2")) {
// closeStatments(ps, conn);
// return PlayerQuequePrefs.EnemieKit;
// }
// if (State.equalsIgnoreCase("3")) {
// closeStatments(ps, conn);
// return PlayerQuequePrefs.RandomKit;
// }
// return null;
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return null;
// }
//
// @SuppressWarnings("resource")
// public static boolean setQuequePref(UUID uuid, PlayerQuequePrefs state) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (isUserExists(uuid)) {
// try {
// if (state == null) {
// return false;
// }
// int id = 2;
// if (state == PlayerQuequePrefs.ownKit) {
// id = 1;
// } else if (state == PlayerQuequePrefs.EnemieKit) {
// id = 2;
// } else if (state == PlayerQuequePrefs.RandomKit) {
// id = 3;
// } else {
// return false;
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
//
// ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID
// = ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
//
// String save = rs.getString("QuequePrefs");
//
// String[] saveData = save.split(" ");
// saveData[0] = "" + id;
// save = "";
// for (int i = 0; saveData.length > i; i++) {
// save = save + saveData[i] + " ";
// }
// ps = conn.prepareStatement("UPDATE " + table + " SET QuequePrefs = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, save);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// if (Bukkit.getPlayer(uuid) != null) {
// addUser(uuid, Bukkit.getPlayer(uuid).getName());
// try {
// if (state == null) {
// return false;
// }
// int id = 2;
// if (state == PlayerQuequePrefs.ownKit) {
// id = 1;
// } else if (state == PlayerQuequePrefs.EnemieKit) {
// id = 2;
// } else if (state == PlayerQuequePrefs.RandomKit) {
// id = 3;
// } else {
// return false;
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET QuequePrefs = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, "" + id);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// }
// return false;
// }
//
// public static boolean setKit(UUID uuid, String Kit, boolean Armor, String
// KitID) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (!Armor) {
// if (isUserExists(uuid)) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitInv" + KitID + " = ?
// WHERE UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, Kit);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// if (Bukkit.getPlayer(uuid) != null) {
// addUser(uuid, Bukkit.getPlayer(uuid).getName());
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitInv" + KitID + " = ?
// WHERE UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, Kit);
// ps.executeUpdate();
// closeStatments(ps, conn);
// return true;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
// return false;
// }
// if (isUserExists(uuid)) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor" + KitID + " =
// ? WHERE UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, Kit);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// if (Bukkit.getPlayer(uuid) != null) {
// addUser(uuid, Bukkit.getPlayer(uuid).getName());
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor" + KitID + " =
// ? WHERE UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, Kit);
// ps.executeUpdate();
// closeStatments(ps, conn);
// return true;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
// return false;
// }
//
// public static boolean setKitDefault(String Kit, boolean Armor) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (!Armor) {
// if (isDefaultExists()) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitInv = ? WHERE UUID =
// ?");
// ps.setString(2, "default");
// ps.setString(1, Kit);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// addDefault();
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitInv = ? WHERE UUID =
// ?");
// ps.setString(2, "default");
// ps.setString(1, Kit);
// ps.executeUpdate();
// closeStatments(ps, conn);
// return true;
// } catch (SQLException e) {
// e.printStackTrace();
//
// return false;
// }
// }
// if (isDefaultExists()) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor = ? WHERE UUID
// = ?");
// ps.setString(2, "default");
// ps.setString(1, Kit);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// addDefault();
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor = ? WHERE UUID
// = ?");
// ps.setString(2, "default");
// ps.setString(1, Kit);
// ps.executeUpdate();
// closeStatments(ps, conn);
// return true;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return false;
// }
//
// public static String getKitDefault(boolean Armor) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (!Armor) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT KitInv FROM " + table + " WHERE UUID =
// ?");
//
// ps.setString(1, "default");
// ResultSet rs = ps.executeQuery();
// if (!rs.next()) {
// return null;
// }
// String data = rs.getString("KitInv");
// closeStatments(ps, conn);
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// } else {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT KitArmor FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, "default");
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("KitArmor");
// closeStatments(ps, conn);
// return data;
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
// return "";
// }
//
// public static String getKit(UUID uuid, boolean Armor, String KitID) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (!Armor) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
//
// if(uuid == null) return "";
// conn = Main.ins.sql.getSQLConnection();
//
// ps = conn.prepareStatement("SELECT KitInv" + KitID + " FROM " + table + "
// WHERE UUID = ?");
//
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (!rs.next()) {
// return null;
// }
// String data = rs.getString("KitInv" + KitID);
// closeStatments(ps, conn);
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// } else {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// if(uuid == null) return "";
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT KitArmor" + KitID + " FROM " + table + "
// WHERE UUID = ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("KitArmor" + KitID);
// closeStatments(ps, conn);
// return data;
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
// return "";
// }
//
// public static String getStats(UUID uuid, String Typ, boolean timed) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if(!timed) {
// if (Typ.equalsIgnoreCase("Fights")) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Fights FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (!rs.next()) return "0";
// String data = rs.getString("Fights");
// closeStatments(ps, conn);
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// } else if (Typ.equalsIgnoreCase("FightsWon")) {
// try {
// if (sql == null) sql = new SQLite();
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT FightsWon FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("FightsWon");
// closeStatments(ps, conn);
// return data;
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
// } else {
// if (Typ.equalsIgnoreCase("Fights")) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Fights30 FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("Fights30");;
// closeStatments(ps, conn);
// return data;
// }
// String data = rs.getString("Fights30");
// closeStatments(ps, conn);
// return data;
// } catch (SQLException e) {
// //e.printStackTrace();
// }
// } else if (Typ.equalsIgnoreCase("FightsWon")) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT FightsWon30 FROM " + table + " WHERE UUID
// = ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("FightsWon30");
// closeStatments(ps, conn);
// return data;
// }
// } catch (SQLException e) {
// //e.printStackTrace();
// }
// }
// }
//
// return "0";
// }
//
// public static void updatePref(UUID uuid, String KitID) {
// Connection conn = null;
// PreparedStatement ps = null;
// if (isUserExists(uuid)) {
// try {
// if (getRawPref(uuid, KitID).length != Main.ins.defaultKitPrefs) {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
//
// String[] data = new String[Main.ins.defaultKitPrefs];
// String[] saveData = getRawPref(uuid, KitID);
//
// int filled = 0;
//
// for (String str : saveData) {
// data[filled] = str;
// filled++;
// }
//
// while (filled < Main.ins.defaultKitPrefs) {
// data[filled] = "f";
// filled++;
// }
//
// StringBuilder builder = new StringBuilder();
// boolean first = true;
// for (String str : data) {
// if (first) {
// builder.append(str);
// first = false;
// } else {
// builder.append(" ");
// builder.append(str);
// }
//
// }
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID
// = ?");
// if (!KitID.equalsIgnoreCase("")) {
// ps = conn.prepareStatement(
// "UPDATE " + table + " SET KitSettings" + KitID + " = ? WHERE UUID = ?");
// }
// ps.setString(2, uuid.toString());
//
// ps.setString(1, builder.toString());
// ps.executeUpdate();
// }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// }
// }
//
// public static void updatePrefDefault() {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (isDefaultExists()) {
// try {
// if (getRawPrefDefault().length != Main.ins.defaultKitPrefs) {
// if (sql == null) {
// sql = new SQLite();
// }
//
// String[] data = new String[Main.ins.defaultKitPrefs];
// String[] saveData = getRawPrefDefault();
//
// int filled = 0;
//
// for (String str : saveData) {
// data[filled] = str;
// filled++;
// }
//
// while (filled < Main.ins.defaultKitPrefs) {
// data[filled] = "f";
// filled++;
// }
//
// StringBuilder builder = new StringBuilder();
// boolean first = true;
// for (String str : data) {
// if (first) {
// builder.append(str);
// first = false;
// } else {
// builder.append(" ");
// builder.append(str);
// }
//
// }
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID
// = ?");
//
// ps.setString(2, "default");
// ps.setString(1, "" + builder.toString());
// ps.executeUpdate();
// }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// }
// }
//
// public static String getDefaultKit(UUID uuid) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT DefaultKit FROM " + table + " WHERE UUID =
// ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String data = rs.getString("DefaultKit");
// closeStatments(ps, conn);
// return data;
// }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return "1";
// }
//
// public static void setDefaultKit(UUID uuid, String ID) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (isUserExists(uuid)) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET DefaultKit = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, ID);
// ps.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// }
// }
//
// public static PlayerBestOfsPrefs getQuequePrefState2(UUID uuid) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID
// = ?");
//
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// while (rs.next()) {
// if (rs.getString("QuequePrefs").split(" ").length >= 2) {
// String State = rs.getString("QuequePrefs").split(" ")[1];
// if (State == null) {
// return null;
// }
// if (State.equalsIgnoreCase("1")) {
// return PlayerBestOfsPrefs.BestOf1;
// }
// if (State.equalsIgnoreCase("2")) {
// return PlayerBestOfsPrefs.BestOf3;
// }
// if (State.equalsIgnoreCase("3")) {
// return PlayerBestOfsPrefs.BestOf5;
// }
// return null;
// }
// }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return null;
// }
//
// @SuppressWarnings("resource")
// public static boolean setQuequePref2(UUID uuid, PlayerBestOfsPrefs state) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (isUserExists(uuid)) {
// try {
// if (state == null) {
// return false;
// }
// int id = 2;
// if (state == PlayerBestOfsPrefs.BestOf1) {
// id = 1;
// } else if (state == PlayerBestOfsPrefs.BestOf3) {
// id = 2;
// } else if (state == PlayerBestOfsPrefs.BestOf5) {
// id = 3;
// } else {
// return false;
// }
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID
// = ?");
//
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
//
// String used = rs.getString("QuequePrefs");
// if (used.split(" ").length >= 2) {
// sql = new SQLite();
// conn = Main.ins.sql.getSQLConnection();
//
// ps = conn.prepareStatement("UPDATE " + table + " SET QuequePrefs = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
//
// String save = used;
//
// String[] saveData = save.split(" ");
// saveData[1] = "" + id;
// save = "";
// for (int i = 0; saveData.length > i; i++) {
// save = save + saveData[i] + " ";
// }
// ps.setString(1, save);
// ps.executeUpdate();
// }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return true;
// }
// if (Bukkit.getPlayer(uuid) != null) {
// addUser(uuid, Bukkit.getPlayer(uuid).getName());
// setQuequePref2(uuid, state);
// return true;
// }
// return false;
// }
//
// public static HashMap<Integer, UUID> Top5Players(boolean timed) {
// HashMap<Integer, UUID> Top5 = new HashMap<>();
// if(!timed) {
// try {
// if (!isConnected())
// return null;
//
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(FightsWon AS
// UNSIGNED) desc LIMIT " + Main.ins.topPlaces);
//
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String uuid = rs.getString("UUID");
// if (!uuid.equalsIgnoreCase("default") &&
// !uuid.toLowerCase().contains("custom")) {
// try {
// Top5.put(Integer.valueOf(Platz), UUID.fromString(uuid));
// Platz++;
// } catch (Exception localException) {}
// }
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// } else {
// try {
// if (!isConnected())
// return null;
//
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(FightsWon30 AS
// UNSIGNED) desc LIMIT " + Main.ins.topPlaces);
//
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String uuid = rs.getString("UUID");
// if (!uuid.equalsIgnoreCase("default") &&
// !uuid.toLowerCase().contains("custom")) {
// try {
// Top5.put(Integer.valueOf(Platz), UUID.fromString(uuid));
// Platz++;
// } catch (Exception localException) {}
// }
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
// return Top5;
// }
//
// public static Integer getPositionKit(String name, int type) {
//
// try {
//
// if(type == 0) {
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String id = rs.getString("PlayerName");
// if (id.equalsIgnoreCase(name)) {
// closeStatments(ps, con);
// return Platz;
// }
//
// if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom"))
// {
// Platz++;
// }
// }
// return Platz;
// } else if(type == 1) {
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays30 AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String id = rs.getString("PlayerName");
// if (id.equalsIgnoreCase(name)) {
// closeStatments(ps, con);
// return Platz;
// }
//
// if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom"))
// {
// Platz++;
// }
// }
// return Platz;
// } else if(type == 2) {
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays24h AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String id = rs.getString("PlayerName");
// if (id.equalsIgnoreCase(name)) {
// closeStatments(ps, con);
// return Platz;
// }
//
// if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom"))
// {
// Platz++;
// }
// }
// return Platz;
// }
//
//
// } catch (SQLException e) {
// e.printStackTrace();
// }
//
// return Integer.valueOf(-1);
// }
//
// public static HashMap<Integer, String> Top5Kits(int type) {
// HashMap<Integer, String> Top5 = new HashMap<>();
// if(type == 0) {
// try {
// if (!isConnected()) return Top5;
//
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays AS
// UNSIGNED) desc LIMIT " + Main.ins.topPlaces);
//
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String uuid = rs.getString("PlayerName");
// if (!uuid.equalsIgnoreCase("default")) {
// try {
// Top5.put(Integer.valueOf(Platz), uuid);
// Platz++;
// } catch (Exception localException) {}
// }
// }
// } catch (SQLException e) {
// return Top5;
// }
// } else if(type == 1) {
// try {
// if (!isConnected())
// return Top5;
//
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays30 AS
// UNSIGNED) desc LIMIT " + Main.ins.topPlaces);
//
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
//
// String uuid = rs.getString("PlayerName");
// if (!uuid.equalsIgnoreCase("default") ) {
// try {
// Top5.put(Integer.valueOf(Platz), uuid);
// Platz++;
// } catch (Exception localException) {}
// }
// }
// } catch (SQLException e) {
// return Top5;
// }
// } else if(type == 2) {
// try {
// if (!isConnected())
// return Top5;
//
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays24h AS
// UNSIGNED) desc LIMIT " + Main.ins.topPlaces);
//
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String uuid = rs.getString("PlayerName");
// if (!uuid.equalsIgnoreCase("default") ) {
// try {
// Top5.put(Integer.valueOf(Platz), uuid);
// Platz++;
// } catch (Exception localException) {}
// }
// }
// } catch (SQLException e) {
// return Top5;
// }
// }
//
// return Top5;
// }
//
//
//
//
// public static Integer getPosition(UUID uuid, boolean timed) {
// try {
// if(!timed) {
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(FightsWon AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String id = rs.getString("UUID");
// if (id.equalsIgnoreCase(uuid.toString())) {
// closeStatments(ps, con);
// return Platz;
// }
//
// if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom"))
// {
// Platz++;
// }
// }
// return Platz;
// } else {
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(FightsWon30 AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String id = rs.getString("UUID");
// if (id.equalsIgnoreCase(uuid.toString())) {
// closeStatments(ps, con);
// return Platz;
// }
//
// if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom"))
// {
// Platz++;
// }
// }
// return Platz;
// }
//
//
// } catch (SQLException e) {
// e.printStackTrace();
// }
//
// return Integer.valueOf(-1);
// }
//
//
//
// public static boolean isMapDisabled(UUID uuid, String MapName) {
// PreparedStatement ps = null;
// Connection con = Main.ins.sql.getSQLConnection();
// try {
//
// ps = con.prepareStatement("SELECT DisabledMaps FROM " + table + " WHERE UUID
// = ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String Inv = rs.getString("DisabledMaps");
// String[] InvS = Inv.split(" ");
// for (int i = 0; i < InvS.length; i++) {
// if (InvS[i].equalsIgnoreCase(MapName)) {
// return true;
// }
// }
// return false;
// }
// } catch (Exception localException) {
// } finally {
// closeStatments(ps, con);
// }
// return false;
// }
//
// public static String getDisabledMaps(UUID uuid) {
// PreparedStatement ps = null;
// Connection con = Main.ins.sql.getSQLConnection();
// try {
// ps = con.prepareStatement("SELECT DisabledMaps FROM " + table + " WHERE UUID
// = ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
// String Inv = rs.getString("DisabledMaps");
// if (Inv.equalsIgnoreCase("null")) {
// createDisabled(uuid);
// return "";
// }
// return Inv;
// }
// } catch (Exception localException) {
// } finally {
// closeStatments(ps, con);
// }
// return "";
// }
//
// public static void setMapDisabled(UUID uuid, String MapName, boolean
// disabled) {
// PreparedStatement ps = null;
// Connection con = Main.ins.sql.getSQLConnection();
// try {
//
// ps = con.prepareStatement("UPDATE " + table + " SET DisabledMaps = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
//
// String[] disabledList = getDisabledMaps(uuid).split(" ");
// for (int i = 0; i < disabledList.length; i++) {
// if (disabledList[i].equalsIgnoreCase(MapName)) {
// disabledList[i] = "";
// }
// }
// String disabledMaps = "";
// for (int i = 0; i < disabledList.length; i++) {
// disabledMaps = disabledMaps + disabledList[i] + " ";
// }
// if (disabled) {
// disabledMaps = disabledMaps + MapName;
// }
// ps.setString(1, disabledMaps);
// ps.executeUpdate();
// } catch (SQLException localSQLException) {
// } finally {
// closeStatments(ps, con);
// }
// }
//
// public static void createDisabled(UUID uuid) {
// PreparedStatement ps = null;
// Connection con = null;
// try {
// con = Main.ins.sql.getSQLConnection();
// ps = con.prepareStatement("UPDATE " + table + " SET DisabledMaps = ? WHERE
// UUID = ?");
// ps.setString(2, uuid.toString());
// ps.setString(1, "");
// ps.executeUpdate();
// } catch (SQLException localSQLException) {
// } finally {
// closeStatments(ps, con);
// }
// }
//
//
// public static int isCustomKitExits(String Name) {
// if(isNameRegistered(Name)) {
// return 2;
// }
//
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE
// PlayerName = ?");
// ps.setString(1, Name);
// ResultSet rs = ps.executeQuery();
// boolean data = rs.next();
// closeStatments(ps, conn);
// if(data) return 1;
// return 0;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return 0;
// }
//
// @SuppressWarnings("resource")
// public static void addCustomKit(String Name, String Inv, String Armor,
// String[] Prefs) {
//
// Connection conn = connection;
// PreparedStatement ps = null;
//
// if(isCustomKitExits(Name) == 1) {
//
// try {
// if (sql == null) {
// sql = new SQLite();
// }
//
// String[] prefs = Prefs;
//
// boolean first = true;
// StringBuilder Data = new StringBuilder();
// for (int i = 0; i < prefs.length; i++) {
// if (first) {
// first = false;
// Data = Data.append(prefs[i]);
// } else {
// Data.append(" ");
// Data.append(prefs[i]);
// }
// }
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitInv = ? WHERE
// PlayerName = ?");
// ps.setString(2, Name);
// ps.setString(1, Inv);
// ps.executeUpdate();
//
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor = ? WHERE
// PlayerName = ?");
// ps.setString(2, Name);
// ps.setString(1, Armor);
// ps.executeUpdate();
// closeStatments(ps, conn);
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE
// PlayerName = ?");
// ps.setString(2, Name);
// ps.setString(1, Data.toString());
// ps.executeUpdate();
// closeStatments(ps, conn);
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
//
// return;
// }
//
//
// try {
// String[] prefs = Prefs;
//
// boolean first = true;
// StringBuilder Data = new StringBuilder();
// for (int i = 0; i < prefs.length; i++) {
// if (first) {
// first = false;
// Data = Data.append(prefs[i]);
// } else {
// Data.append(" ");
// Data.append(prefs[i]);
// }
// }
//
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("INSERT INTO " + table + " (" + "PlayerName" +
// ",UUID" + ",KitInv"
// + ",KitArmor" + ",Settings" + ",QuequePrefs" + ",KitInv2" + ",KitArmor2" +
// ",KitSettings2"
// + ",KitInv3" + ",KitArmor3" + ",KitSettings3" + ",KitInv4" + ",KitArmor4" +
// ",KitSettings4"
// + ",KitInv5" + ",KitArmor5" + ",KitSettings5" + ",Fights" + ",FightsWon" +
// ",DefaultKit"
// + ",DisabledMaps)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
// ps.setString(1, Name);
// ps.setString(2, "CUSTOM " + UUID.randomUUID() + UUID.randomUUID() +
// UUID.randomUUID());
// ps.setString(3, Inv);
// ps.setString(4, Armor);
// ps.setString(5, Data.toString());
// ps.setString(6, "2 1");
// ps.setString(7, "");
// ps.setString(8, "");
// ps.setString(9, "");
// ps.setString(10, "");
// ps.setString(11, "");
// ps.setString(12, "");
// ps.setString(13, "");
// ps.setString(14, "");
// ps.setString(15, "");
// ps.setString(16, "");
// ps.setString(17, "");
// ps.setString(18, "");
// ps.setString(19, "0");
// ps.setString(20, "0");
// ps.setString(21, "1");
// ps.setString(22, "1");
// ps.executeUpdate();
// closeStatments(ps, conn);
// return;
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return;
//
//
//
// }
//
// public static String loadCustomKit(String Name, boolean Armor) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if (!Armor) {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
//
// ps = conn.prepareStatement("SELECT KitInv FROM " + table + " WHERE PlayerName
// = ?");
//
// ps.setString(1, Name);
// ResultSet rs = ps.executeQuery();
// if (!rs.next()) {
// return null;
// }
// String data = rs.getString("KitInv");
// closeStatments(ps, conn);
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// } else {
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT KitArmor FROM " + table + " WHERE
// PlayerName = ?");
// ps.setString(1, Name);
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
//
// String data = rs.getString("KitArmor");
//
// closeStatments(ps, conn);
// return data;
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
// return "";
// }
//
// public static boolean getCustomKitPref(String Name, int id) {
//
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE
// PlayerName = ?");
//
// ps.setString(1, Name);
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
//
//
// String[] prefs = rs.getString("Settings").split(" ");
// if ((id >= prefs.length) || (id < 0)) {
// closeStatments(ps, conn);
// return false;
// }
// if (prefs[id].equalsIgnoreCase("t")) {
// closeStatments(ps, conn);
// return true;
// }
// return false;
// }
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
// return false;
//
//
// }
//
// public static String[] getCustomKitRawPref(String Name) {
// Connection conn = connection;
// PreparedStatement ps = null;
// try {
// if (sql == null) {
// sql = new SQLite();
// }
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE
// PlayerName = ?");
//
// ps.setString(1, Name);
//
// ResultSet rs = ps.executeQuery();
// if (rs.next()) {
//
//
// String[] prefs = rs.getString("Settings").split(" ");
// closeStatments(ps, conn);
// return prefs;
// }
// } catch (SQLException ex) {
// Main.ins.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
// } finally {
// closeStatments(ps, conn);
// }
//
// return null;
// }
//
// public static void deleteCustomKit(String Name) {
//
// Connection conn = connection;
// PreparedStatement ps = null;
//
// if(isCustomKitExits(Name) == 1) {
//
// try {
// if (sql == null) sql = new SQLite();
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("DELETE FROM " + table + " WHERE PlayerName = ?");
//
// ps.setString(1, Name);
// ps.executeUpdate();
//
//
//
// closeStatments(ps, conn);
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
//
// return;
// }
// return;
//
// }
//
// public static Integer getAllUserEntrys() {
//
//
// try {//SELECT * from KitDatabase order by CAST(FightsWon AS UNSIGNED) desc
// limit 5 1vs1Kits
// Connection conn = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = conn.prepareStatement("SELECT * from " + table +"
// order by CAST(FightsWon AS UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 0;
// while(rs.next()) {
// String place = rs.getString("UUID");
// if(!place.toLowerCase().contains("default") &&
// !place.toLowerCase().contains("custom")) {
// Platz++;
// }
// }
// closeStatments(ps, conn);
// return Platz;
// } catch (SQLException e) {
// return -1;
// }
//
// }
//
// public static void updateRankPoints(final UUID uuid, final int amount) {
// Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
//
// @Override
// public void run() {
// PreparedStatement ps = null;
// try {
//
//
// ps = Main.ins.sql.getSQLConnection().prepareStatement("UPDATE " + table + "
// SET RankPoints = ? WHERE UUID = ?");
//
// ps.setString(2, uuid.toString());
// int points = getRankPoints(uuid)+amount;
// if(points <= -1) points = 0;
// ps.setString(1, "" + points);
// ps.executeUpdate();
//
//
// ps.close();
// } catch (SQLException e) {
// e.printStackTrace();
// }
//
// }
// });
//
//
// return;
// }
//
// public static int getRankPoints(UUID uuid) {
// try {
// PreparedStatement ps = null;
// ps = Main.ins.sql.getSQLConnection().prepareStatement("SELECT RankPoints FROM
// " + table + " WHERE UUID = ?");
// ps.setString(1, uuid.toString());
// ResultSet rs = ps.executeQuery();
// while(rs.next()) {
// return Integer.parseInt(rs.getString("RankPoints"));
// }
// } catch (NumberFormatException e) {
// return 0;
// } catch (SQLException e) {
// return 0;
// }
// return 0;
// }
//
// public static HashMap<Integer, UUID> Top5PlayersRankPoints() {
// HashMap<Integer, UUID> Top5 = new HashMap<>();
// try {
// if (!isConnected())
// return null;
//
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(RankPoints AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next() && Platz <= 5) {
//
// String uuid = rs.getString("UUID");
//
// if (!uuid.equalsIgnoreCase("default") &&
// !uuid.toLowerCase().contains("custom")) {
// try {
// Top5.put(Integer.valueOf(Platz), UUID.fromString(uuid));
// Platz++;
// } catch (Exception localException) {}
// }
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return Top5;
// }
//
// public static Integer getPositionRankPoints(UUID uuid) {
//
// try {
// Connection con = Main.ins.sql.getSQLConnection();
// PreparedStatement ps = con
// .prepareStatement("SELECT * from " + table + " order by CAST(RankPoints AS
// UNSIGNED) desc");
//
// ResultSet rs = ps.executeQuery();
// int Platz = 1;
// while (rs.next()) {
// String id = rs.getString("UUID");
// if (id.equalsIgnoreCase(uuid.toString())) {
// closeStatments(ps, con);
//
// return Platz;
// }
//
// if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom"))
// {
// Platz++;
// }
// }
// return Platz;
// } catch (SQLException e) {
// e.printStackTrace();
// }
//
// return Integer.valueOf(-1);
// }
//
// public static boolean exists(String row) {
// try {
// if (!isConnected()) return false;
//
//
// PreparedStatement ps =
// Main.ins.sql.getSQLConnection().prepareStatement("SELECT "+ row + " FROM " +
// table);
//
// boolean result = ps.executeQuery().next();
//
// ps.close();
//
// return result;
// } catch (SQLException e) {
// return false;
// }
// }
//
// public static void reset30DayStats() {
// //
//
// try {
// if (!isConnected()) return;
//
//
// PreparedStatement ps =
// Main.ins.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET
// Fights30 = 0");
//
// ps.executeUpdate();
// ps = Main.ins.sql.getSQLConnection().prepareStatement("UPDATE " + table + "
// SET FightsWon30 = 0");
//
// ps.executeUpdate();
//
// for(int i = 1; i < 5; i++) {
// ps = Main.ins.sql.getSQLConnection().prepareStatement("UPDATE " + table + "
// SET Kit" + i + "Plays30 = 0");
//
// ps.executeUpdate();
// }
//
// return;
// } catch (SQLException e) {
// return;
// }
// }
//
// public static void reset24hStats() {
// //
//
// try {
// if (!isConnected()) return;
//
// for(int i = 1; i < 5; i++) {
// PreparedStatement ps =
// Main.ins.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET
// Kit" + i + "Plays24h = 0");
//
// ps.executeUpdate();
// }
//
//
//
// return;
// } catch (SQLException e) {
// return;
// }
// }
//
// public static void setStatsKit(String Name, int Higher, int kit, int type) {
// Connection conn = connection;
// PreparedStatement ps = null;
//
// try {
// if(type == 0) {
// if (kit > 5 || kit < 0) return;
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays = ?
// WHERE PlayerName = ?");
// //ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30
// = ? WHERE PlayerName = ?");
// ps.setString(2, Name);//TODO
// int newStat = 0;
// if(getStatsKit(Name, kit, type) != null)
// newStat = Integer.parseInt(getStatsKit(Name, kit, type));
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
//
// } else if(type == 1){
// if (kit > 5 || kit < 0) return;
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30 =
// ? WHERE PlayerName = ?");
// //ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30
// = ? WHERE PlayerName = ?");
// ps.setString(2, Name);//TODO
// int newStat = 0;
// if(getStatsKit(Name, kit, type) != null)
// newStat = Integer.parseInt(getStatsKit(Name, kit, type));
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
//
//
// } else if(type == 2){
// if (kit > 5 || kit < 0) return;
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays24h =
// ? WHERE PlayerName = ?");
// //ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30
// = ? WHERE PlayerName = ?");
// ps.setString(2, Name);//TODO
// int newStat = 0;
// if(getStatsKit(Name, kit, type) != null)
// newStat = Integer.parseInt(getStatsKit(Name, kit, type));
// newStat += Higher;
// ps.setString(1, "" + newStat);
// ps.executeUpdate();
//
//
// }
//
//
// } catch (Exception e) {
// e.printStackTrace();
// } finally {
// closeStatments(ps, conn);
// }
// return;
// }
//
// public static String getStatsKit(String Name, int kit, int type) {
// Connection conn = connection;
// PreparedStatement ps = null;
// if(type == 0) {
// if (kit > 5 || kit < 0) return "0";
// try {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Kit" + kit + "Plays FROM " + table + "
// WHERE PlayerName = ?");
// ps.setString(1, Name);
// ResultSet rs = ps.executeQuery();
// if (!rs.next()) return "0";
// String data = rs.getString("Kit" + kit + "Plays");
// closeStatments(ps, conn);
// if(data == null || data.equalsIgnoreCase("null")) return "0";
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
//
// } else if(type == 1) {
// if (kit > 5 || kit < 0) return "0";
// try {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Kit" + kit + "Plays30 FROM " + table + "
// WHERE PlayerName = ?");
// ps.setString(1, Name);
// ResultSet rs = ps.executeQuery();
// if(!rs.next()) return "0";
// String data = rs.getString("Kit" + kit + "Plays30");
// closeStatments(ps, conn);
// if(data == null || data.equalsIgnoreCase("null")) return "0";
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// } else if(type == 2) {
// if (kit > 5 || kit < 0) return "0";
// try {
//
// conn = Main.ins.sql.getSQLConnection();
// ps = conn.prepareStatement("SELECT Kit" + kit + "Plays24h FROM " + table + "
// WHERE PlayerName = ?");
// ps.setString(1, Name);
// ResultSet rs = ps.executeQuery();
// if(!rs.next()) return "0";
// String data = rs.getString("Kit" + kit + "Plays24h");
// closeStatments(ps, conn);
// if(data == null || data.equalsIgnoreCase("null")) return "0";
// return data;
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
//
// return "0";
// }
//
//
// }
