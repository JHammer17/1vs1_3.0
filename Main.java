package de.onevsone;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.onarandombox.MultiverseCore.MultiverseCore;

import de.onevsone.arenas.builder.worldReset.WorldResetMgr;
import de.onevsone.commands.MainCMD;
import de.onevsone.commands.Variable.ChallangeCMD;
import de.onevsone.commands.Variable.ColorCMD;
import de.onevsone.commands.Variable.CreateCMD;
import de.onevsone.commands.Variable.FightInfo;
import de.onevsone.commands.Variable.HeadCMD;
import de.onevsone.commands.Variable.JoinCMD;
import de.onevsone.commands.Variable.KitCMD;
import de.onevsone.commands.Variable.KitSettingsCMD;
import de.onevsone.commands.Variable.KitStatsCMD;
import de.onevsone.commands.Variable.QueueCMD;
import de.onevsone.commands.Variable.SelectedKitCMD;
import de.onevsone.commands.Variable.SettingsCMD;
import de.onevsone.commands.Variable.SpectateCMD;
import de.onevsone.commands.Variable.StatsCMD;
import de.onevsone.commands.Variable.StatsMenuCMD;
import de.onevsone.commands.Variable.TeamCMD;
import de.onevsone.commands.Variable.TournamentCMD;
import de.onevsone.database.DBMgr;
import de.onevsone.database.sql.Database;
import de.onevsone.listener.KitEditRegion;
import de.onevsone.listener.statsmgr.SignMgr;
import de.onevsone.listener.statsmgr.SkullMgr;
import de.onevsone.methods.ConfigMgr;
import de.onevsone.methods.LayoutMgr;
import de.onevsone.methods.QueueManager;
import de.onevsone.methods.StatHolograms;
import de.onevsone.methods.Utils;
import de.onevsone.methods.entities.BlackDealer;
import de.onevsone.methods.entities.QueueZombie;
import de.onevsone.methods.scoreboards.ScoreBoardMgr;
import de.onevsone.methods.scoreboards.ScoreboardAPI;
import de.onevsone.objects.KitStand;
import de.onevsone.objects.OneVsOneArena;
import de.onevsone.objects.OneVsOneKitStandMgr;
import de.onevsone.objects.OneVsOnePlayer;
import de.onevsone.objects.OneVsOneTournament;

public class Main extends JavaPlugin implements Listener{

    public static Main ins;
    
    public boolean mysqlDB = false;

    public String prefixGreen = "§2│ §a1vs1§2»§7 ";
    public String prefixYellow = "§6│ §e1vs1§6»§7 ";
    public String prefixRed = "§4│ §c1vs1§4»§7 ";
    public String prefixBlue = "§1│ §91vs1§1»§7 ";
    public String debugPrefix = "§7[§9Debug§7] §b";
    
    public String prefixTournament = "§3│ §bTurnier§3»§7 ";

    public boolean saveInvs = true;
    public boolean saveOldScoreboard = true;
    
//    public boolean spawnFirework = true;

    public boolean msgMeWhenIStupid = true;

    public boolean silentQueue = true;
    public boolean silentKitSettings = true;
    public boolean silentBlackDealer = true;

    
    public boolean useSkinDataFromDB = true;

    public int wandID = 286;
    public int wandSubID = 0;
    
    public int maxFightTime = 300;

    public int minPlayersTournament = 2;
    
    public HashMap<String, String> globalSavedData = new HashMap<>();
    
    HashMap<UUID, OneVsOnePlayer> oneVSOnePlayers = new HashMap<UUID, OneVsOnePlayer>();
    HashMap<String, OneVsOneArena> oneVSOneArenas = new HashMap<String, OneVsOneArena>();

    public Utils utils = new Utils();

    public ArrayList<EnchantingInventory> inventories = new ArrayList<EnchantingInventory>();

    
    public int defaultKitPrefs = 16;// 16
    public boolean checkDatabaseConnection = true;
    public int topPlaces = 10;

    public int minArenaBuildDistanceWalls = 2;
    public int minArenaBuildDistanceTop = 2;
    public int minArenaBuildDistanceBottom = 1;

    public int timeInArenaLoser = 10;
    public int timeInArenaWinner = 20*7;
    
    public String version = getServerVersion();
    
    
    public ScoreboardAPI scoreAPI = new ScoreboardAPI();
    
    public HashMap<UUID, KitStand> kitStands = new HashMap<>();
    public ArrayList<UUID> kitStandUUIDs = new ArrayList<>();
    
    public HashMap<UUID, OneVsOneTournament> tournaments = new HashMap<>();
    
    public DBMgr database;
    
    public MultiverseCore mv;
    
    public OneVsOneKitStandMgr kitStandMgr;
	
    @Override
    public void onEnable() {
        ins = this;
        
        mv = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        new WorldResetMgr();
        
        
        new BukkitRunnable() {


            public void run() {	
                new ConfigMgr().reloadConfig();
         		
                utils.reloadBasics();
                registerCommands();
                new Register();
                new KitEditRegion();
                new QueueManager();
                new ScoreBoardMgr();
               
                database = new DBMgr();
                database.load();
                
                Database.setLastReset(0, false);
                
                reloadQueue();
                
                kitStandMgr = new OneVsOneKitStandMgr();
                
                for(Player players : Bukkit.getOnlinePlayers()) {
                    if(!database.isUserExists(players.getUniqueId())) {
                    	database.addUser(players.getUniqueId(), players.getName(), "Player");
                    } else {
                    	database.updateUserData(players);
                    }
                    if(!database.isUserExists(players.getUniqueId(), "Kit")) {
                    	database.addUser(players.getUniqueId(), players.getName(), "Kit");
                    }
                    if(!database.isUserExists(players.getUniqueId(), "Stats")) {
                    	database.addUser(players.getUniqueId(), players.getName(), "Stats");
                    }
                    
                    
                    
                }
            

				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						
							kitStandMgr.spawnKitStands();
							kitStandMgr.autoRefill();
							kitStandMgr.autoRespawn(); //TODO ÜBERARBEITEN
						
						for(String layout : LayoutMgr.getAllLayouts()) {
							if(LayoutMgr.isWorldLayoutReady(layout) == 3) {
								
								Location pos1 = LayoutMgr.getMinPos(layout);
								Location pos2 = LayoutMgr.getMaxPos(layout);
								
								LayoutMgr.removeLayout(layout);
								LayoutMgr.addLayout(layout, true, pos1, pos2, null);
								
								for(Player players : Bukkit.getOnlinePlayers()) {
									if(players.hasPermission("1vs1.layoutinfo")) {
										players.sendMessage(Main.ins.prefixYellow + "§eLayout " + layout + " wird erzeugt!");
										players.sendMessage(Main.ins.prefixYellow + "§7Du wirst benachrichtigt, wenn das Layout fertig erzeugt wird.");
									}
								}
							}
						}
					}
				}.runTask(Main.ins);
				
				
                if(utils.getYaml("Arenas").getConfigurationSection("Arena") != null) {
                    for(String arena : utils.getYaml("Arenas").getConfigurationSection("Arena").getKeys(false)) {
                        addArena(arena);
                        getOneVsOneArena(arena).resetArena();
                    }
                }

                
                new BukkitRunnable() {
					
					@Override
					public void run() {
						for(Player players : Bukkit.getOnlinePlayers()) {
		                	MainCMD.toggle1vs1(players, true, false);
		                }
					}
				}.runTask(Main.ins);
                

                reloadSignsAndSkulls(); //TODO ÜBERARBEITEN wenn chunk nicht geladen => ignorieren
                autoRefreshSigns(); //TODO ÜBERARBEITEN
                StatHolograms.startAutoRespawner();
                
            }
        }.runTaskAsynchronously(Main.ins);
        
        
        getServer().getPluginManager().registerEvents(this, Main.ins);
    }

    @Override
    public void onDisable() {
        for(OneVsOnePlayer players : getOneVsOnePlayersCopy().values()) {
            if(players.isIn1vs1()) MainCMD.toggle1vs1(players.getPlayer(), false, true);
        }
        QueueZombie.despawnQuequeZombie();
        BlackDealer.despawnBlackDealer();

        for (EnchantingInventory ei : this.inventories) ei.setItem(1, null);
        this.inventories = null;


        database.stopSQL();
        
        StatHolograms.stopAutoRespawner();
        StatHolograms.despawnHologramForAllPlayers();
        
        new OneVsOneKitStandMgr().removeAllKitStands();
        Bukkit.getServer().getScheduler().cancelAllTasks();
    }

    


    public void registerCommands() {
        getCommand("1vs1").setExecutor(new MainCMD());
        getCommand("kit").setExecutor(new KitCMD());
        getCommand("stats").setExecutor(new StatsCMD());
        getCommand("settings").setExecutor(new SettingsCMD());
        getCommand("challange").setExecutor(new ChallangeCMD());
        getCommand("spectate").setExecutor(new SpectateCMD());
        getCommand("fightinfo").setExecutor(new FightInfo());
        
        getCommand("q").setExecutor(new QueueCMD());
        getCommand("color").setExecutor(new ColorCMD());
        getCommand("kitsettings").setExecutor(new KitSettingsCMD());
        getCommand("selectedKit").setExecutor(new SelectedKitCMD());
        getCommand("team").setExecutor(new TeamCMD());
        getCommand("create").setExecutor(new CreateCMD());
        getCommand("tournament").setExecutor(new TournamentCMD());
        getCommand("join").setExecutor(new JoinCMD());
        
        getCommand("head").setExecutor(new HeadCMD());
        getCommand("kitStats").setExecutor(new KitStatsCMD());
        getCommand("statsmenu").setExecutor(new StatsMenuCMD());
        
    }

    public String getServerVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return version;
    }

    private void reloadQueue() {
        QueueZombie.startChecker();
        BlackDealer.startChecker();
        new BukkitRunnable() {


            public void run() {
                QueueZombie.respawner();
                BlackDealer.respawner();
            }
        }.runTaskTimerAsynchronously(this, 0, 20);
    }

    //OneVsOnePlayer Mgr Part

    public OneVsOnePlayer getOneVsOnePlayer(Player p) {
        if(p == null) return new OneVsOnePlayer(p);
        if(oneVSOnePlayers.containsKey(p.getUniqueId())) {
            return oneVSOnePlayers.get(p.getUniqueId());
        }

        return new OneVsOnePlayer(p);
    }

    public OneVsOnePlayer getOneVsOnePlayer(UUID uuid) {
        if(Bukkit.getPlayer(uuid) == null) return new OneVsOnePlayer(Bukkit.getPlayer(uuid));
        if(oneVSOnePlayers.containsKey(Bukkit.getPlayer(uuid).getUniqueId()))
            return oneVSOnePlayers.get(Bukkit.getPlayer(uuid).getUniqueId());
        return new OneVsOnePlayer(Bukkit.getPlayer(uuid));
    }

    public int getOneVsOnePlayerSize() {
        return oneVSOnePlayers.size();
    }

    public void removePlayer(UUID uuid) {
        while(oneVSOnePlayers.containsKey(uuid)) oneVSOnePlayers.remove(uuid);
    }

    public boolean isInOneVsOnePlayers(UUID uuid) {
        return (oneVSOnePlayers.containsKey(uuid) && oneVSOnePlayers.get(uuid).isIn1vs1());
    }

    public void addPlayer(UUID uuid) {

        if(Bukkit.getPlayer(uuid) != null) {
            OneVsOnePlayer player = new OneVsOnePlayer(Bukkit.getPlayer(uuid));
            player.init();
            oneVSOnePlayers.put(uuid, player);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<UUID, OneVsOnePlayer> getOneVsOnePlayersCopy() {
        return (HashMap<UUID, OneVsOnePlayer>) oneVSOnePlayers.clone();
    }

    //>OneVsOnePlayer Mgr Part

    //OneVsOneArena Mgr Part
    public OneVsOneArena getOneVsOneArena(String name) {
        if(name == null) return new OneVsOneArena(name);
        if(oneVSOneArenas.containsKey(name)) {
            return oneVSOneArenas.get(name);
        }
        
        return new OneVsOneArena(name);
    }


    public int getOneVsOneArenaSize() {
        return oneVSOneArenas.size();
    }

    public void removeArena(String name) {
        oneVSOneArenas.remove(name);
    }

    public boolean isInOneVsOneArenas(String name) {
        return oneVSOneArenas.containsKey(name);
    }

    public void addArena(String name) {

        if(name != null) {
            OneVsOneArena arena = new OneVsOneArena(name);
            arena.init();
            oneVSOneArenas.put(name, arena);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, OneVsOneArena> getOneVsOneArenasCopy() {
        return (HashMap<String, OneVsOneArena>) oneVSOneArenas.clone();
    }
    
    
   
    
    public static void changeName(String name, Player player) {
        try {
          Method getHandle = player.getClass().getMethod("getHandle",(Class<?>[]) null);
          try {
            Class.forName("com.mojang.authlib.GameProfile");
          } catch (ClassNotFoundException e) {
            return;
          }
            Object profile = getHandle.invoke(player).getClass()
           	 .getMethod("getProfile")
                .invoke(getHandle.invoke(player));
            Field ff = profile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(profile, name);
          for (Player players : Bukkit.getOnlinePlayers()) {
            players.hidePlayer(player);
            players.showPlayer(player);
          }
        } catch (NoSuchMethodException | SecurityException
            | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | NoSuchFieldException e) {
          e.printStackTrace();
        }
      }
    
    public void refreshSignsAndSkulls() {
    	new BukkitRunnable() {
			
			@Override
			public void run() {
				SignMgr.refreshSigns();
		    	SkullMgr.refreshSkulls();
				
			}
		}.runTask(Main.ins);
    	
    }
    
    public void reloadSignsAndSkulls() {
    	new BukkitRunnable() {
			
			@Override
			public void run() {
				SignMgr.reloadSigns();
				SkullMgr.reloadSkulls();
				
			}
		}.runTask(Main.ins);
    }
    
    public void autoRefreshSigns() {
    	new BukkitRunnable() {
			
			@Override
			public void run() {
				reloadSignsAndSkulls();
			}
		}.runTaskTimer(this, 0, 20*10);
    }
   
    public void addGlobalData(String key, String value) {
    	if(this.globalSavedData.containsKey(key)) this.globalSavedData.remove(key);
    	this.globalSavedData.put(key, value);
    }
    
    public String getGlobalData(String key) {
    	if(this.globalSavedData.containsKey(key)) return this.globalSavedData.get(key);
    	return null;
    }
    
   
    	
    
    
    
}
