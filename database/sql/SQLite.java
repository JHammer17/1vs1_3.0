package de.onevsone.database.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import de.onevsone.Main;

public class SQLite {
	
	    String dbname = "KitDatabase";
		 
	    public String sqlCreatePlayerDatasTableQuery = 
	    		"CREATE TABLE IF NOT EXISTS PlayerData (" +
	    		"`UUID` varchar(100) NOT NULL," +
	    		"`PlayerName` varchar(100) NOT NULL," +
	    		"`Skin` longtext NOT NULL," +
	    		"`DisabledMaps` longtext NOT NULL," +
	    		"`QueuePrefs` varchar(150) NOT NULL," +
	    		"`ColorPos1` Int NOT NULL," + 
	    		"`ColorPos2` Int NOT NULL," + 
	    		"`ColorPos1Alt` Int NOT NULL," + 
	    		"`ColorPos2Alt` Int NOT NULL" + 
	    		");";
	    
	    public String sqlCreateKitDatasTableQuery = 
	    		"CREATE TABLE IF NOT EXISTS KitData (" +
	    	    "`UUID` varchar(100) NOT NULL," +
	    	    "`KitName` varchar(100) NOT NULL," +
				"`Selected` Int NOT NULL," +
	    	    
				"`KitName1` varchar(100) NOT NULL," +
	    	    "`KitInv1` longtext NOT NULL," +
	    	    "`KitArmor1` longtext NOT NULL," +
	    	    "`KitSettings1` longtext NOT NULL," +
	    	    "`Kit1Plays` longtext NOT NULL," +
	            "`Kit1Plays30` longtext NOT NULL," +
	            "`Kit1Plays24` longtext NOT NULL," +
	            
				"`KitName2` varchar(100) NOT NULL," +
	    	    "`KitInv2` longtext NOT NULL," +
	    	    "`KitArmor2` longtext NOT NULL," +
	    	    "`KitSettings2` longtext NOT NULL," +
	    	    "`Kit2Plays` longtext NOT NULL," +
	            "`Kit2Plays30` longtext NOT NULL," +
	            "`Kit2Plays24` longtext NOT NULL," +
	            
				"`KitName3` varchar(100) NOT NULL," +
				"`KitInv3` longtext NOT NULL," +
	    	    "`KitArmor3` longtext NOT NULL," +
	    	    "`KitSettings3` longtext NOT NULL," +
	    	    "`Kit3Plays` longtext NOT NULL," +
	            "`Kit3Plays30` longtext NOT NULL," +
	            "`Kit3Plays24` longtext NOT NULL," +
	            
				"`KitName4` varchar(100) NOT NULL," +
				"`KitInv4` longtext NOT NULL," +
	    	    "`KitArmor4` longtext NOT NULL," +
	    	    "`KitSettings4` longtext NOT NULL," +
	    	    "`Kit4Plays` longtext NOT NULL," +
	            "`Kit4Plays30` longtext NOT NULL," +
	            "`Kit4Plays24` longtext NOT NULL," +
	            
				"`KitName5` varchar(100) NOT NULL," +
				"`KitInv5` longtext NOT NULL," +
	    	    "`KitArmor5` longtext NOT NULL," +
	    	    "`KitSettings5` longtext NOT NULL," +
	    	    "`Kit5Plays` longtext NOT NULL," +
	            "`Kit5Plays30` longtext NOT NULL," +
	            "`Kit5Plays24` longtext NOT NULL" +
	            ");";
	    
	    public String sqlCreateStatDatasTableQuery = 
	    		"CREATE TABLE IF NOT EXISTS StatData (" +
	    	    "`UUID` varchar(100) NOT NULL," +
	    				
	    	    "`RankPoints` Int," +
	    	    
	    	    "`Fights` Int DEFAULT 0," +
	            "`FightsWon` Int DEFAULT 0," +
	            "`FightsLost` Int DEFAULT 0," +
	            "`FightsEz` Int DEFAULT 0," +
	            "`FightsTight` Int DEFAULT 0," +
	            "`FightsKills` Int DEFAULT 0," +
	            "`FightsDeaths` Int DEFAULT 0," +
	            "`FightsKD` FLOAT(150) DEFAULT 0," +
	            "`FightsWL` FLOAT(150) DEFAULT 0," +
	            
 				"`Fights30` Int DEFAULT 0," +
 				"`FightsWon30` Int DEFAULT 0," +
 				"`FightsLost30` Int DEFAULT 0," +
 				"`FightsEz30` Int DEFAULT 0," +
 				"`FightsTight30` Int DEFAULT 0," +
 				"`FightsKills30` Int DEFAULT 0," +
 				"`FightsDeaths30` Int DEFAULT 0," +
 				"`FightsKD30` FLOAT(150) DEFAULT 0," +
 				"`FightsWL30` FLOAT(150) DEFAULT 0," +
				
				"`Fights24` Int DEFAULT 0," +
				"`FightsWon24` Int DEFAULT 0," +
				"`FightsLost24` Int DEFAULT 0," +
				"`FightsEz24` Int DEFAULT 0," +
				"`FightsTight24` Int DEFAULT 0," +
				"`FightsKills24` Int DEFAULT 0," +
				"`FightsDeaths24` Int DEFAULT 0," +
				"`FightsKD24` FLOAT(150) DEFAULT 0," +
				"`FightsWL24` FLOAT(150) DEFAULT 0" +
				");";
	    
	    public String sqlCreateFightDB = 
	    		"CREATE TABLE IF NOT EXISTS FightsData (" +
	    		"`Time` bigint NOT NULL," +
	    	    "`ID` varchar(100) NOT NULL," +
	    				
	    	    "`Pos1List` longtext," +
				"`Pos2List` longtext," +
				"`Pos1ListNames` longtext," +
				"`Pos2ListNames` longtext," +
				
				"`pos1Win` boolean DEFAULT true," +

				"`Color1` varchar(100)," +
	    	    "`Color2` varchar(100)," +
	    	    "`Win1Health` float," +
	    	    "`Win2Health` float," +
	    	    
	    	    "`Arena` varchar(100)," +
	    	    "`Map` varchar(100)," +
	    	    
	    	    "`Ranked` boolean DEFAULT false," +
	    	    
	            "`Tournament` longtext," +
	            "`Tname` varchar(100)," +
	            
				"`Item` varchar(100)," +
	    	    "`SubID` Int," +
	    	    "`Kit` varchar(100)," +
	    	    "`Duration` Int," +
	    	    "`Kills1` longtext," +
	            "`Kills2` longtext);";	 
	    
	    public Connection getSQLConnection() {
	        File dataFolder = new File("plugins/1vs1/" + dbname + ".db");
	        if (!dataFolder.exists()){
	            try {
	                dataFolder.createNewFile();
	            } catch (IOException e) {
	                Main.ins.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
	            }
	        }
	        try {
	            if(Database.connection!=null&&!Database.connection.isClosed()){
	                return Database.connection;
	            }
	            Class.forName("org.sqlite.JDBC");
	            Database.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
	            Statement st = Database.connection.createStatement();        
	            String sql="PRAGMA synchronous=OFF";
	            st.execute(sql);//TODO In config deaktivierbar machen! Soll DB "schneller" machen tuts auch
	            return Database.connection;
	        } catch (SQLException ex) {
	            Main.ins.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
	        } catch (ClassNotFoundException ex) {
	        	Main.ins.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
	        }
	        return null;
	    }

	    public void load() {
	    	new Database();
	    	Database.connection = getSQLConnection();
	        try {
	            Statement s = Database.connection.createStatement();
	            
	            s.executeUpdate(sqlCreatePlayerDatasTableQuery);
	            s.executeUpdate(sqlCreateKitDatasTableQuery);
	            s.executeUpdate(sqlCreateStatDatasTableQuery);
	            s.executeUpdate(sqlCreateFightDB);
	            
	            s.close();
	        } catch (SQLException e) {
	           e.printStackTrace();
	        }
	        Database.initialize();
	    }
}
