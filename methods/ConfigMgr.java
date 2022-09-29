/**
 * 
 */
package de.onevsone.methods;

import java.io.File;
import java.util.Arrays;

import org.bukkit.configuration.file.YamlConfiguration;

import de.onevsone.CommentYAML;
import de.onevsone.Main;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 24.03.2018 15:00:12					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class ConfigMgr {

	
	CommentYAML configFile = new CommentYAML();
	
	@SuppressWarnings("static-access")
	public void reloadConfig() {
		 try {
             
			 YamlConfiguration cfg = YamlConfiguration.loadConfiguration(Main.ins.utils.getPluginFile("Config"));
	         
             
             File file = Main.ins.utils.getPluginFile("Config");
             configFile.loadFile(file);
             
             
             
             configFile.setHeader(file, "##########################\n"
             						 + "   1vs1 - Like Timolia   #\n"
             						 + "       by JHammer17      #\n"
             						 + "##########################"
             						 );
             
             
             configFile.setComment("Config.saveStats", "Gibt an, ob die Statistiken über Kämpfe und Kits gespeichert werden sollen (Default: true)");
             configFile.setComment("Config.arenaRegionLeaveDamage", "Schaden, den ein Spieler pro Sekunde erhält, wenn er die Arena verlässt (Default: 5)");
             configFile.setComment("Config.minArenaBuildDistanceWalls", "Besagt, ab welcher Distanz gebaut werden kann von den Wänden aus (Default: 2)");
             configFile.setComment("Config.minArenaBuildDistanceTop", "Besagt, ab welcher Distanz gebaut werden kann von der Decke aus (Default: 2)");
             configFile.setComment("Config.minArenaBuildDistanceBottom", "Besagt, ab welcher Distanz gebaut werden kann vom Boden aus (Default: 1)");
             configFile.setComment("Config.arenaCheckTimer", "Alle Arenen werden alle paar Sekunden geprüft, auf mögliche Fehler,\nwie das sie als Besetzt markiert ist, obwohl sie frei ist (Default: 10)");
             configFile.setComment("Config.maxArenaEntitys", "Maximale Anzahl an Entitys die in einen Kampf genutzt werden können (Default: 16)");
             configFile.setComment("Config.soupHeal", "Heilung, die eine Suppe macht (Default: 3.5)");
             configFile.setComment("Config.startTime", "Die Zeit bevor der Kampf in der Arena startet (Default: 3)");
             configFile.setComment("Config.MySQL.AllowAPIReadPassword", "Soll die API das Passwort auslesen können? (Default: false)");
             configFile.setComment("Config.MySQL", "MySQL Zeug");
             configFile.setComment("Config.BungeeMode.LobbyServer", "Server der im Bungeecord als Lobbyserver festgelegt wurde. (Default: Lobby)");
             configFile.setComment("Config.BungeeMode", "Spieler werden beim betreten des Servers automatisch in den 1vs1-Moduse gesetzt\n"
					  + "und beim Verlassen auf den unten beschrieben Server gesendet. (Default: false)");
             configFile.setComment("Config.showArenaNamesSpectatorGUI", "Gibt an, ob die Namen einer Arena in der Spectator GUI angezeigt werden sollen,\noder ob die Arenen durchnummeriert werden sollen (Default: true)");
             configFile.setComment("Config.toggleCoolDown", "Cooldown, für Einstellungen o.ä. die eine MySQL Verbindung brauchen in Millisekunden (Default: 0)");
             configFile.setComment("Config.saveInvs", "Besagt, ob das Inventar von Spielern beim betreten des 1vs1-Modus gespeichert werden sollen (Default: true)");
             configFile.setComment("Config.saveOldScoreboard", "Besagt, ob das Scoreboard von Spielern beim betreten des 1vs1-Modus gespeicher werden soll (Default: true)");
             configFile.setComment("Config.updateNotification", "Besagt, ob Updatenachrichten gezeigt werden sollen, beim Reload/Restart (Default: true)");
             configFile.setComment("Config.updateNotificationJoin", "Besagt, ob Updatenachrichten gezeit werden sollen, beim Joinen eines Admins (Default: true)");
             configFile.setComment("Config.autoEndmatch", "Zeit bis der Kampf automatisch beendet werden soll, in Sekunden (-1 für aus) (Default: 300)");
             configFile.setComment("Config.scoreBoardName", "Name, der für das Scoreboardobjective verwendet werden soll (Default: 1vs1)");
             configFile.setComment("Config.overrideJoinMsg", "Gibt an, ob die Join- und Leavenachricht überschrieben werden soll (Default: false)");
             configFile.setComment("Config.useScoreboard", "Gibt an, ob das Scoreboard benutzt werden soll in der Lobby und Ingame (Default: true)");
             configFile.setComment("Config.maxBlocksPerTick", "Maximale Menge an Blöcken, die beim Reset einer Arena pro Tick gesetzt werden können.\nWenn der Mapreset die zu langsam ist, setzte diese Zahl hoch, wenn es lagt setze sie runter! (Default: 5000)");
             configFile.setComment("Config.defaultMaxTeamSize", "Maximale Menge an Teammitgliedern für User ohne andere Regelungen an (Default: 3)");
             configFile.setComment("Config.useEconomySystem", "Gibt an, ob (wenn installiert mit Vault) ein Spieler Geld für gewonnen Kämpfe bekommen soll (Default: true)");
             configFile.setComment("Config.maxTNTArenaGame", "Gibt die maximale Menge TNT pro Spiel an (Default: 32)");
             configFile.setComment("Config.silentQueue", "Gibt an, ob der Warteschlangenzombie still sein soll (Default: true)");
             configFile.setComment("Config.silentBlackDealer", "Gibt an, ob der Schwarzhändler still sein soll (Default: true)");
             configFile.setComment("Config.msgMeWhenImStupid", "Gibt an, ob Fehler bei der Einrichtung automatisch gemeldet werden sollen (Default: true)");
             configFile.setComment("Config.ACS.ACSEnabled", "Gibt an, ob das ACS aktiviert sein soll (Default: true)");
             configFile.setComment("Config.ACS.MinArenas", "Die minimale Menge an Arenen, die exestieren müssen (Default: 1)");
             configFile.setComment("Config.ACS.MaxArenas", "Die maximale Menge an Arenen, die exestieren dürfen (Default: 30)");
             configFile.setComment("Config.ACS.World", "Die Welt, auf der die Arenen gespawnt werden sollen (Default: 1vs1-ACS)");
             configFile.setComment("Config.ACS.disX", "Die Distanz zwischen verschiedenen Arenatypen, die nebeneinander sind (Default: 0)");
             configFile.setComment("Config.ACS.disZ", "Die Distanz zwischen gleichen Arenatypen, die hintereinander sind (Default: 0)");
             configFile.setComment("Config.topPlaces", "Die Menge an Plätzen die in einer Datenbankabfrage angefragt werden sollen");
             configFile.setComment("Config.Items.ChallangerID", "Die ID des Challangers (Default: 276)");
             configFile.setComment("Config.Items.SpectatorID", "Die ID des Spectatoritems (Default: 370)");
             configFile.setComment("Config.Items.SettingsID", "Die ID des Settingsitems (Default: 404)");
             configFile.setComment("Config.Items.LeaveID", "Die ID des Leaveitems (Default: 347)");
             configFile.setComment("Config.Items.ChallangerSlot", "Der Slot des Challangers (Default: 1)");
             configFile.setComment("Config.Item.SpectatorItemSlot", "Der Slot des Spectatoritems (Default: 2)");
             configFile.setComment("Config.Items.BookItemSLot", "Der Slot des Buches (Default: 3)");
             configFile.setComment("Config.Items.RankItemSlot", "Der Slot des Rankitems (Default: 7)");
             configFile.setComment("Config.Items.SettingsItemSlot", "Der Slot des Settingsitems (Default: 8)");
             configFile.setComment("Config.Items.LeaveItemSlot", "Der Slot des leaveitems (Default: 9)");
             configFile.setComment("Config.blockedCommands", "Blockierte Befehle in 1vs1");
             configFile.setComment("Config.Ranking.RankPointsWins", "Die Menge an Rangpunkten, die ein Spieler beim Gewinn erhält (Default: 1)");
             configFile.setComment("Config.Ranking.RankPointsLose", "Die Menge an Rangpunkten, die ein Spieler beim Verlust erhält (Default: -1)");
             configFile.setComment("Config.apiEnabled", "Gibt an, ob die API aktiv ist (Default: true)");
             configFile.setComment("Config.fireWorkEnabled", "Gibt an, ob nach jedem Kampf ein Feuerwerk gespawnt werden soll (Default: true)");
             configFile.setComment("Config.useSkinsfromDB", "Gibt an, ob die Skins von der Datenbank für die Top Wand benutzt werden sollen (Default: true)");
            
             
             if(cfg.get("Config.BungeeMode.Enabled") == null) 
            	 configFile.set(file, "Config.BungeeMode.BungeeModeEnabled", false);
             if(cfg.get("Config.BungeeMode.LobbyServer") == null) 
                 configFile.set(file, "Config.BungeeMode.LobbyServer", "Lobby");
             if(cfg.get("Config.MySQL.MySQLEnabled") == null) 
                 configFile.set(file, "Config.MySQL.MySQLEnabled", false);
             if(cfg.get("Config.MySQL.Domain") == null) 
            	 configFile.set(file, "Config.MySQL.Domain", "localhost");
             if(cfg.get("Config.MySQL.Port") == null) 
            	 configFile.set(file, "Config.MySQL.Port", 3306);
             if(cfg.get("Config.MySQL.Database") == null) 
            	 configFile.set(file, "Config.MySQL.Database", "Database");
             if(cfg.get("Config.MySQL.Username") == null) 
            	 configFile.set(file, "Config.MySQL.Username", "Username");	 
             if(cfg.get("Config.MySQL.Password") == null) 
            	 configFile.set(file, "Config.MySQL.Password", "SuperSecretPassword");
             if(cfg.get("Config.MySQL.AllowAPIReadPassword") == null) 
            	 configFile.set(file, "Config.MySQL.AllowAPIReadPassword", false);
             if(cfg.get("Config.startTime") == null) 
            	 configFile.set(file, "Config.startTime", 3);
             if(cfg.get("Config.soupHeal") == null) 
            	 configFile.set(file, "Config.soupHeal", 3.5);
             if(cfg.get("Config.maxArenaEntitys") == null) 
            	 configFile.set(file, "Config.maxArenaEntitys", 16);
             if(cfg.get("Config.arenaCheckTimer") == null) 
            	 configFile.set(file, "Config.arenaCheckTimer", 10);
             if(cfg.get("Config.minArenaBuildDistanceBottom") == null) 
            	 configFile.set(file, "Config.minArenaBuildDistanceBottom", 1);
             if(cfg.get("Config.minArenaBuildDistanceTop") == null) 
            	 configFile.set(file, "Config.minArenaBuildDistanceTop", 2);
             if(cfg.get("Config.minArenaBuildDistanceWalls") == null) 
            	 configFile.set(file, "Config.minArenaBuildDistanceWalls", 2);
             if(cfg.get("Config.saveStats") == null) 
            	 configFile.set(file, "Config.saveStats", true);
             if(cfg.get("Config.arenaRegionLeaveDamage") == null) 
            	 configFile.set(file, "Config.arenaRegionLeaveDamage", 5);
             if(cfg.get("Config.showArenaNamesSpectatorGUI") == null) 
            	 configFile.set(file, "Config.showArenaNamesSpectatorGUI", true);
             
             
             if(cfg.get("Config.toggleCoolDown") == null) 
            	 configFile.set(file, "Config.toggleCoolDown", 0);
             if(cfg.get("Config.saveInvs") == null) 
            	 configFile.set(file, "Config.saveInvs", true);
             if(cfg.get("Config.saveOldScoreboard") == null) 
            	 configFile.set(file, "Config.saveOldScoreboard", true);
             if(cfg.get("Config.updateNotification") == null) 
            	 configFile.set(file, "Config.updateNotification", true);
             if(cfg.get("Config.updateNotificationJoin") == null) 
            	 configFile.set(file, "Config.updateNotificationJoin", true);
             if(cfg.get("Config.autoEndmatch") == null) 
            	 configFile.set(file, "Config.autoEndmatch", 300);
             if(cfg.get("Config.scoreBoardName") == null) 
            	 configFile.set(file, "Config.scoreBoardName", "1vs1");
             if(cfg.get("Config.overrideJoinMsg") == null) 
            	 configFile.set(file, "Config.overrideJoinMsg", false);
             if(cfg.get("Config.useScoreboard") == null) 
            	 configFile.set(file, "Config.useScoreboard", true);
             if(cfg.get("Config.maxBlocksPerTick") == null) 
            	 configFile.set(file, "Config.maxBlocksPerTick", 5000);
             if(cfg.get("Config.defaultMaxTeamSize") == null) 
            	 configFile.set(file, "Config.defaultMaxTeamSize", 3);
             if(cfg.get("Config.useEconomySystem") == null) 
            	 configFile.set(file, "Config.useEconomySystem", true);
             if(cfg.get("Config.maxTNTArenaGame") == null) 
            	 configFile.set(file, "Config.maxTNTArenaGame", 32);
             if(cfg.get("Config.silentQueue") == null) 
            	 configFile.set(file, "Config.silentQueue", true);
             if(cfg.get("Config.silentBlackDealer") == null) 
            	 configFile.set(file, "Config.silentBlackDealer", true);
             if(cfg.get("Config.msgMeWhenImStupid") == null) 
            	 configFile.set(file, "Config.msgMeWhenImStupid", true);
             if(cfg.get("Config.ACS.ACSEnabled") == null) 
            	 configFile.set(file, "Config.ACS.ACSEnabled", true);
             if(cfg.get("Config.ACS.MinArenas") == null) 
            	 configFile.set(file, "Config.ACS.MinArenas", 1);
             if(cfg.get("Config.ACS.MaxArenas") == null) 
            	 configFile.set(file, "Config.ACS.MaxArenas", 30);
             if(cfg.get("Config.ACS.World") == null) 
            	 configFile.set(file, "Config.ACS.World", "1vs1-ACS");
             if(cfg.get("Config.ACS.MinArenas") == null) 
            	 configFile.set(file, "Config.ACS.disX", 0);
             if(cfg.get("Config.ACS.MaxArenas") == null) 
            	 configFile.set(file, "Config.ACS.disZ", 0);
             if(cfg.get("Config.topPlaces") == null) 
            	 configFile.set(file, "Config.topPlaces", 15);
             if(cfg.get("Config.Items.ChallangerID") == null) 
            	 configFile.set(file, "Config.Items.ChallangerID", 276);
             if(cfg.get("Config.Items.SpecatorID") == null) 
            	 configFile.set(file, "Config.Items.SpecatorID", 370);
             if(cfg.get("Config.Items.SettingsID") == null) 
            	 configFile.set(file, "Config.Items.SettingsID", 404);
             if(cfg.get("Config.Items.LeaveID") == null) 
            	 configFile.set(file, "Config.Items.LeaveID", 347);
             if(cfg.get("Config.Items.ChallangerSlot") == null) 
            	 configFile.set(file, "Config.Items.ChallangerSlot", 1);
             if(cfg.get("Config.Items.SpecatorItemSlot") == null) 
            	 configFile.set(file, "Config.Items.SpecatorItemSlot", 2);
             if(cfg.get("Config.Items.LeaveID") == null) 
            	 configFile.set(file, "Config.Items.BookItemSlot", 3);
             if(cfg.get("Config.Items.ChallangerSlot") == null) 
            	 configFile.set(file, "Config.Items.RankItemSlot", 7);
             if(cfg.get("Config.Items.SpecatorItemSlot") == null) 
            	 configFile.set(file, "Config.Items.SettingsItemSlot", 8);
             if(cfg.get("Config.Items.SpecatorItemSlot") == null) 
            	 configFile.set(file, "Config.Items.LeaveItemSlot", 9);
             
            
             if(cfg.get("Config.blockedCommands") == null) 
            	 configFile.set(file, "Config.blockedCommands", Arrays.asList("Spawn", "warp", "back", "setHome", "Home", "setspawn", "setwarp", "plotme", "p", "tpa", "tpaccept", "tpahere", "tpahereaccept"));
             if(cfg.get("Config.Ranking.RankPointsWins") == null) 
            	 configFile.set(file, "Config.Ranking.RankPointsWins", 1);
             if(cfg.get("Config.Ranking.RankPointsLose") == null) 
            	 configFile.set(file, "Config.Ranking.RankPointsLose", -1);
             if(cfg.get("Config.apiEnabled") == null) 
            	 configFile.set(file, "Config.apiEnabled", true);
             if(cfg.get("Config.fireWorkEnabled") == null) 
            	 configFile.set(file, "Config.fireWorkEnabled", true);
             if(cfg.get("Config.useSkinsfromDB") == null) 
            	 configFile.set(file, "Config.useSkinsfromDB", true);
             
             
             
             configFile.save(Main.ins.utils.getPluginFile("Config"));
             
             
		  } catch(Exception e) {
		 }          
		 
	}
	
	
	
	
}
