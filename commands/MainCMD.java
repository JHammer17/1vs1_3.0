package de.onevsone.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import de.onevsone.Main;
import de.onevsone.arenas.builder.worldReset.WorldResetMgr;
import de.onevsone.arenas.builder.worldReset.WorldResetMgr.DelayedReset;
import de.onevsone.database.sql.Database;
import de.onevsone.enums.KitPrefs;
import de.onevsone.enums.OvOColor;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.ChallangeMgr;
import de.onevsone.methods.LayoutMgr;
import de.onevsone.methods.StatHolograms;
import de.onevsone.methods.StatsMenu;
import de.onevsone.methods.entities.BlackDealer;
import de.onevsone.methods.entities.QueueZombie;
import de.onevsone.objects.FightInfo;
import de.onevsone.objects.KitStand;
import de.onevsone.objects.OneVsOneArena;
import de.onevsone.objects.OneVsOneKitStandMgr;
import de.onevsone.objects.OneVsOnePlayer;

public class MainCMD implements CommandExecutor {

	@SuppressWarnings("static-access")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("addFightTestEntry"))  {
				
				ArrayList<UUID> uuids1 = new ArrayList<>();
				uuids1.add(UUID.randomUUID());
				ArrayList<UUID> uuids2 = new ArrayList<>();
				uuids2.add(UUID.randomUUID());
				ArrayList<String> names1 = new ArrayList<>();
				names1.add("Test1");
				ArrayList<String> names2 = new ArrayList<>();
				names2.add("Test2");
				
				ArrayList<Integer> kills1 = new ArrayList<>();
				kills1.add(1);
				ArrayList<Integer> kills2 = new ArrayList<>();
				kills2.add(2);
				
				ArrayList<Float> health1 = new ArrayList<>();
				health1.add(1f);
				ArrayList<Float> health2 = new ArrayList<>();
				health2.add(1f);
				
				
				Database.addFight(System.currentTimeMillis()/1000,
						UUID.randomUUID(), uuids1, uuids2, 
						names1, names2, true, 
						OvOColor.RED.toString(), OvOColor.BLUE.toString(), 
						health1, health2, "Test", "TestMap", false, null, "", 
	 				  "1", 0, "TEST", 300, kills1, kills2);
				
				sender.sendMessage(Main.ins.prefixGreen + "Geschrieben WOW!");
			}
			
			if(args[0].equalsIgnoreCase("readOut"))  {
				sender.sendMessage("§acommand");
				if(args.length == 2) {
					
					FightInfo info = Database.loadFightInfo(UUID.fromString(args[1]));
					
					sender.sendMessage("Timestamp: " + info.getTime());
					sender.sendMessage("ID: " + info.getId());
					sender.sendMessage("Pos1List: " + info.getPos1List());
					sender.sendMessage("Pos2List: " + info.getPos2List());
					sender.sendMessage("Pos1Names: " + info.getPos1Names());
					sender.sendMessage("Pos2Names: " + info.getPos2Names());
					sender.sendMessage("Pos1Win: " + info.isPos1Wins());
					sender.sendMessage("Color1: " + info.getColorPos1());
					sender.sendMessage("Color2: " + info.getColorPos2());
					sender.sendMessage("Win1Health: " + info.getPos1Health());
					sender.sendMessage("Win2Health: " + info.getPos2Health());
					sender.sendMessage("Arena: " + info.getArena());
					sender.sendMessage("Map: " + info.getMap());
					sender.sendMessage("Ranked: " + info.isRanked());
					sender.sendMessage("Tournament: " + info.getTournament());
					sender.sendMessage("TName: " + info.gettName());
					sender.sendMessage("Item: " + info.getItemName());
					sender.sendMessage("SubId: " + info.getSubID());
					sender.sendMessage("Kit: " + info.getKit());
					sender.sendMessage("Dauer: " + info.getDuration());
					sender.sendMessage("Kills1: " + info.getKills1());
					sender.sendMessage("Kills2: " + info.getKills2());
					
					
				}
				
				
			}
			
			
			if(args[0].equalsIgnoreCase("a")) {
				
				int x = 30;
				int delay = 0;
				while(x > 0) {
					
					new BukkitRunnable() {
					
						@Override
						public void run() {
						
							int count = 0;
							for(OneVsOneArena arena : Main.ins.getOneVsOneArenasCopy().values()) {
								
								if(!arena.isUsed() && !arena.isLocked() && 
									arena.isReady() && !arena.isEnded() && !arena.isStarted()) {
									arena.resetArena();
									count++;
								}
							}
							
							sender.sendMessage(Main.ins.prefixYellow + "§6" + count + " Arenen §7werden zurückgesetzt!");
							
							
						
						}
					}.runTaskLater(Main.ins, delay*20);
					x--;
					delay+=5;
				}
			}
			
			if(args[0].equalsIgnoreCase("b")) {
				
				int x = 5000;
				int delay = 0;
				while(x > 0) {
					
					new BukkitRunnable() {
					
						@Override
						public void run() {
						
							int count = 0;
							for(OneVsOneArena arena : Main.ins.getOneVsOneArenasCopy().values()) {
								
								if(!arena.isUsed() && !arena.isLocked() && 
									arena.isReady() && !arena.isEnded() && !arena.isStarted()) {
									arena.resetArena();
									count++;
								}
							}
							
							sender.sendMessage(Main.ins.prefixYellow + "§6" + count + " Arenen §7werden zurückgesetzt!");
							
							
						
						}
					}.runTaskLater(Main.ins, delay*20);
					x--;
					delay+=5;
				}
			}
		}
		
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(args.length > 0) {
				
				if(args[0].equalsIgnoreCase("info")) {
					p.sendMessage("" + Main.ins.getOneVsOnePlayer(p).getSpecator());
					
					p.sendMessage("" + Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(p).getSpecator()).getSpectators().contains(p.getUniqueId()));
					p.sendMessage("" + Main.ins.getOneVsOnePlayer(p).getArena());
				}
				
				if(args[0].equalsIgnoreCase("test")) {
					//TODO DELETE THIS
					
					if(args[0].equalsIgnoreCase("TEST")) {
						Database.setLastReset(0, true);
						return true;
					}
					
					Bukkit.broadcastMessage("" + p.getHealth());
					
					if(args.length == 4) {
						
						if(args[2].equalsIgnoreCase("1")) {
							if(args[3].equalsIgnoreCase("1")) {
								Main.ins.getOneVsOneArena(args[1]).updateKD(p.getUniqueId(), true);
							} else {
								Main.ins.getOneVsOneArena(args[1]).updateKD(p.getUniqueId(), false);
							}
							
						} else {
							if(args[3].equalsIgnoreCase("1")) {
								Main.ins.getOneVsOneArena(args[1]).updateStats(p.getUniqueId(), true, false, false);
							} else {
								Main.ins.getOneVsOneArena(args[1]).updateStats(p.getUniqueId(), false, false, false);
							}
						}
					}
					
					
					p.sendMessage("§aUpdate!");
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("fightmenu")) {
					StatsMenu.openStatsMenu(p, null, null, true);
				}
				
				if(args[0].equalsIgnoreCase("arenaInfo")) {
					if(args.length == 2){
						if(Main.ins.isInOneVsOneArenas(args[1])) {
							//╔╚╠● 
							OneVsOneArena arena = Main.ins.getOneVsOneArena(args[1]);
							p.sendMessage("§7-----§8[§6" + arena.getName() +"§8]§7-----");
							p.sendMessage("§8● §7Ingame: §6" + Main.ins.utils.formatBoolean(arena.isUsed()));
							if(arena.isUsed()) {
								p.sendMessage("§8╔ §7Kit: §6" + arena.getKitName());
								p.sendMessage("§8╠ §7Zeit: §6" + arena.getFormatedFightTime());
								p.sendMessage("§8╠ §7Best of: §6" + arena.getBestOf().getName());
								p.sendMessage("§8╠ §7Ranked: §6" + Main.ins.utils.formatBoolean(arena.isRanked()));
								p.sendMessage("§8╠ §7Turnierkampf: §6" + Main.ins.utils.formatBoolean(arena.isTournament()));
								p.sendMessage("§8╠ ");
								for(UUID pos1 : arena.getPos1()) {
									if(Bukkit.getPlayer(pos1) != null) {
										p.sendMessage("§8╠ " + arena.getColorPos1().colorKey() + Bukkit.getPlayer(pos1).getDisplayName());
									}
								}
								p.sendMessage("§8╠ §7 vs");
								for(UUID pos2 : arena.getPos2()) {
									if(Bukkit.getPlayer(pos2) != null) {
										p.sendMessage("§8╠ " + arena.getColorPos2().colorKey() + Bukkit.getPlayer(pos2).getDisplayName());
									}
								}
								
								p.sendMessage("§8╚");
							
							}
							p.sendMessage("§8● §7Bereit: §6" + Main.ins.utils.formatBoolean(arena.isReady()));
							
							p.sendMessage("§7-----§8[§6" + arena.getName() +"§8]§7-----");
							
							p.sendMessage(Main.ins.prefixBlue + "§7Für erweiterte Details nutze: §a/1vs1 arenainfo " + args[1] + " -d");
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cArena '" + args[1] +"' nicht gefunden!");
						}
					} else if(args.length == 3) {
						if(Main.ins.isInOneVsOneArenas(args[1])) {
							//╔╚╠●
							OneVsOneArena arena = Main.ins.getOneVsOneArena(args[1]);
							p.sendMessage("§7-----§8[§6" + arena.getName() +"§8]§7-----");
							p.sendMessage("§8● §7Ingame: §6" + Main.ins.utils.formatBoolean(arena.isUsed()));
							if(arena.isUsed()) {
								p.sendMessage("§8╔ §7Kit: §6" + arena.getKitName());
								p.sendMessage("§8╠ §7Zeit: §6" + arena.getFormatedFightTime());
								p.sendMessage("§8╠ §7Best of: §6" + arena.getBestOf().getName());
								p.sendMessage("§8╠ §7Ranked: §6" + Main.ins.utils.formatBoolean(arena.isRanked()));
								p.sendMessage("§8╠ §7Turnierkampf: §6" + Main.ins.utils.formatBoolean(arena.isTournament()));
								p.sendMessage("§8╠ §7WeltArena: " + Main.ins.utils.formatBoolean(arena.isWorldArena()));
								p.sendMessage("§8╠ §7Spectator: §6" + arena.getSpectators().size());
								String spectatorList = "";
								
								for(UUID players : arena.getSpectators()) {
									if(Bukkit.getPlayer(players) != null) {
										if(spectatorList.equalsIgnoreCase("")) {
											spectatorList = "§6" + Bukkit.getPlayer(players).getName();
										} else {
											spectatorList = "§7, §6" + spectatorList + Bukkit.getPlayer(players).getName();
										}
									}
								}
								
								p.sendMessage("§8╠ " + spectatorList);
								p.sendMessage("§8╠ §7Gestartet: " + Main.ins.utils.formatBoolean(arena.isStarted()));
								p.sendMessage("§8╠ §7Beendet: " + Main.ins.utils.formatBoolean(arena.isEnded()));
								p.sendMessage("§8╠ §7Einstellungen: §6" + arena.getSettings().size());
								
								String prefList = "";
								
								for(KitPrefs pref : arena.getSettings()) {
									
									if(prefList.equalsIgnoreCase("")) {
										prefList = "§6" + pref.getName();
									} else {
										prefList = prefList + "§7, §6" + pref.getName();
									}
									
								}
								if(prefList.equalsIgnoreCase("")) prefList = "-";
								p.sendMessage("§8╠ §6" + prefList);
								
								p.sendMessage("§8╠ §7WeltArena: " + Main.ins.utils.formatBoolean(arena.isWorldArena()));
								
								
								p.sendMessage("§8╠");
								for(UUID pos1 : arena.getPos1()) {
									if(Bukkit.getPlayer(pos1) != null) {
										p.sendMessage("§8╠ §l" + arena.getColorPos1().colorKey() + Bukkit.getPlayer(pos1).getDisplayName());
									}
								}
								p.sendMessage("§8╠ §7   vs");
								for(UUID pos2 : arena.getPos2()) {
									if(Bukkit.getPlayer(pos2) != null) {
										p.sendMessage("§8╠ §l" + arena.getColorPos2().colorKey() + Bukkit.getPlayer(pos2).getDisplayName());
									}
								}
								
								p.sendMessage("§8╚");
							
							}
							p.sendMessage("§8● §7Bereit: §6" + Main.ins.utils.formatBoolean(arena.isReady()));
							
							p.sendMessage("§7-----§8[§6" + arena.getName() +"§8]§7-----");
							
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cArena '" + args[1] +"' nicht gefunden!");
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 arenaInfo [Arena] {-d}");
					}
					return true;
				}
				
				if(args[0].equalsIgnoreCase("resetInfo")) {
					
					p.sendMessage(Main.ins.prefixBlue + "§7Am zurücksetzen: " + Main.ins.utils.formatBoolean(WorldResetMgr.isLoading()));
					p.sendMessage(Main.ins.prefixBlue + "§7Wartend: §6" + WorldResetMgr.getWaiting());
					
					StringBuilder builder = new StringBuilder() ;
					
					for(DelayedReset dR : WorldResetMgr.getResetList()) {
						if(builder.toString().equalsIgnoreCase("")) builder.append(dR.getArena().getName());
						else builder.append(", ").append(dR.getArena().getName());
					}
					
					p.sendMessage(Main.ins.prefixBlue + "§7-> §c" + builder.toString());
					
					
				}
				
				
				if(args[0].equalsIgnoreCase("resetAllArenas")) {
					int count = 0;
					for(OneVsOneArena arena : Main.ins.getOneVsOneArenasCopy().values()) {
						
						if(!arena.isUsed() && !arena.isLocked() && 
							arena.isReady() && !arena.isEnded() && !arena.isStarted()) {
							arena.resetArena();
							count++;
						}
					}
					
					p.sendMessage(Main.ins.prefixYellow + "§6" + count + " Arenen §7werden zurückgesetzt!");
					
					return true;
				} 
				
				
				if(args[0].equalsIgnoreCase("listarenas"))  {
					if(args.length >= 1) {
						StringBuilder builder = new StringBuilder();
						p.sendMessage(Main.ins.prefixBlue + "§6Arenen: §7[§a" + Main.ins.getOneVsOneArenasCopy().size() +"§7]");
						
						for(OneVsOneArena arena : Main.ins.getOneVsOneArenasCopy().values()) {
							if(!builder.toString().equalsIgnoreCase("")) builder.append("§7, ");
							
							if(!arena.isOk()) builder.append("§c");
							else if(arena.hasErrors() && !(arena.isLocked() || !arena.isReady())) builder.append("§4");
							else if(arena.isUsed()) builder.append("§2");
							else if(arena.isReady()) builder.append("§a");
							else if(arena.isLocked() || !arena.isReady()) builder.append("§e");
							
							
							
							builder.append(arena.getName());
						}
						
						p.sendMessage(builder.toString());
						
						p.sendMessage("§a");
						
						if(args.length == 1 || (args.length == 2 && !args[1].equalsIgnoreCase("-l"))) {
							p.sendMessage(Main.ins.prefixBlue + "§7Nutze: §a/1vs1 listArenas -l §7um eine Legende zu bekommen");
						}
						
						if(args.length == 2 && args[1].equalsIgnoreCase("-l")) {
							p.sendMessage(Main.ins.prefixBlue + "Legende:");
							p.sendMessage("§cArena nicht ok!");
							p.sendMessage("§2Spiel in der Arena");
							p.sendMessage("§aBereit");
							p.sendMessage("§eAm zurücksetzen");
							p.sendMessage("§4Fehler in der Arena");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 listArenas {-l}");
					}
				}
				
				if(args[0].equalsIgnoreCase("listKitStands"))  {
					
						StringBuilder builder = new StringBuilder();
						p.sendMessage(Main.ins.prefixBlue + "§6KitStands: §7[§a" + Main.ins.kitStands.size() +"§7]");
						
						for(UUID standID : Main.ins.kitStandUUIDs) {
							if(standID != null) {
								if(Main.ins.kitStands.get(standID) != null) {
									KitStand stand = Main.ins.kitStands.get(standID);
									
									if(builder.toString().equalsIgnoreCase("")) {
										builder.append(stand.getName());
									} else builder.append(", ").append(stand.getName());
								}
							}
							
							
						}
						
						p.sendMessage("§a" + builder.toString());
						
						
						
						
					
				}
				
				if(args[0].equalsIgnoreCase("reloadKitStands"))  {
					//TODO Perms
					if(!Main.ins.kitStands.isEmpty()) {
						
						Main.ins.kitStandMgr.spawnKitStands();
						p.sendMessage(Main.ins.prefixGreen + "Alle Kitstand neu geladen!");
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cEs sind keine Kitstands gesetzt!");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("setKitStandType"))  {
					if(args.length == 3) {
					 
						
						YamlConfiguration cfg = Main.ins.utils.getYaml("Kitstands");
						
						
						if(cfg.getConfigurationSection("Kitstand." + args[1]) != null) {
							if(args[2].equalsIgnoreCase("alltime")) {
								cfg.set("Kitstand." + args[1] + ".Timed", "alltime");
								try {
									cfg.save(Main.ins.utils.getPluginFile("Kitstands"));
								} catch (IOException e) {
									e.printStackTrace();
									p.sendMessage("§cFehler beim Speichern der Datei!");
									return true;
								}
								Main.ins.kitStandMgr.spawnKitStands();
								p.sendMessage(Main.ins.prefixGreen + "§aKitstand geupdated!");
								
							} else if(args[2].equalsIgnoreCase("30d")) {
								cfg.set("Kitstand." + args[1] + ".Timed", "30d");
								try {
									cfg.save(Main.ins.utils.getPluginFile("Kitstands"));
								} catch (IOException e) {
									e.printStackTrace();
									p.sendMessage("§cFehler beim Speichern der Datei!");
									return true;
								}
								Main.ins.kitStandMgr.spawnKitStands();
								p.sendMessage(Main.ins.prefixGreen + "§aKitstand geupdated!");
								
							} else if(args[2].equalsIgnoreCase("24h")) {
								cfg.set("Kitstand." + args[1] + ".Timed", "24h");
								try {
									cfg.save(Main.ins.utils.getPluginFile("Kitstands"));
								} catch (IOException e) {
									e.printStackTrace();
									p.sendMessage("§cFehler beim Speichern der Datei!");
									return true;
								}
								Main.ins.kitStandMgr.spawnKitStands();
								p.sendMessage(Main.ins.prefixGreen + "§aKitstand geupdated!");
								
								
							} else {
								p.sendMessage(Main.ins.prefixRed + "Nutze: /1vs1 setKitStandType [Name] [alltime|30d|24h]");
							}
								
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cKitstand nicht gefunden! Nutze 1vs1 listKitStands um dir alle Kitstands anzeigen zu lassen!");
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "Nutze: /1vs1 setKitStandType [Name] [alltime|30d|24h]");
					}
					
					
				}
				
				
				/*
				 * joinarena => Let two Players join a Arena
				 */
				if(args[0].equalsIgnoreCase("joinarena")) {
					if(args.length == 4) {
						
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						
						if(cfg.getConfigurationSection("Arena." + args[3]) != null) {
							if(Main.ins.getOneVsOneArena(args[3]).isOk()) {
								Main.ins.getOneVsOneArena(args[3]).join((ArrayList<Player>) Arrays.asList(Bukkit.getPlayer(args[1])), (ArrayList<Player>) Arrays.asList(Bukkit.getPlayer(args[2])), p.getUniqueId().toString(), Main.ins.database.getSelectedKit(p.getUniqueId()));
								p.sendMessage("§aOk!");
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
							}
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 joinarena [Spieler1] [Spieler2] [Arena]");
					}
				}
				
				if(args[0].equalsIgnoreCase("check")) {
					if(args.length == 2) {
						if(Main.ins.isInOneVsOneArenas(args[1])) {
							
							p.sendMessage("§aReady: §f" + Main.ins.getOneVsOneArena(args[1]).isReady() + " §aUsed: §f" + Main.ins.getOneVsOneArena(args[1]).isUsed() + " §cLocked: §f" + Main.ins.getOneVsOneArena(args[1]).isLocked());
							
						} else {
							p.sendMessage("§cArena nicht gefunden!");
						}
					} else {
						p.sendMessage("§cFalsche Verwendung: /1vs1 check [Arena]");
					}
				}
				
				/*
				 * setSkull Command => Sets the Skull for Top
				 */
				if(args[0].equalsIgnoreCase("setSkull")) {
					if(!Main.ins.getOneVsOnePlayer(p).isEditMode()) {
						p.sendMessage(Main.ins.prefixRed + "§cDu musst im Editmodus sein, um diesen Befehl nutzen zu können!");
						return true;
					}
					
					if(args.length == 3) {
						try {
							int place = Integer.parseInt(args[2]);
							
							Main.ins.getOneVsOnePlayer(p).setSkullModePlace(-1);
							Main.ins.getOneVsOnePlayer(p).setSkullModeTimed(-1);
							
							
							if(place < 0) {
								p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setSkull [alltime|30d|24h] [Platz]");
								return true;
							}
							
							if(place == 0) {
								Main.ins.getOneVsOnePlayer(p).setSkullModePlace(-1);
								Main.ins.getOneVsOnePlayer(p).setSkullModeTimed(-1);
								p.sendMessage(Main.ins.prefixBlue + "Du hast den Skullplace Mode verlassen!");
								return true;
							}
							
							if(args[1].equalsIgnoreCase("alltime") || args[1].equalsIgnoreCase("all")) {
								Main.ins.getOneVsOnePlayer(p).setSkullModePlace(place);
								Main.ins.getOneVsOnePlayer(p).setSkullModeTimed(0);
							} else if(args[1].equalsIgnoreCase("30d") || args[1].equalsIgnoreCase("30")) {
								Main.ins.getOneVsOnePlayer(p).setSkullModePlace(place);
								Main.ins.getOneVsOnePlayer(p).setSkullModeTimed(1);
							} else if(args[1].equalsIgnoreCase("24h") || args[1].equalsIgnoreCase("24")) {
								Main.ins.getOneVsOnePlayer(p).setSkullModePlace(place);
								Main.ins.getOneVsOnePlayer(p).setSkullModeTimed(2);
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setSkull [alltime|30d|24h] [Platz]");
								return true;
							}
							
							
							p.sendMessage(Main.ins.prefixYellow + "§7Mache nun einen Rechtsklick, auf einen Kopf, um einen Top Kopf zu erstellen!");
							return true;
						} catch (NumberFormatException e) {
							p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setSkull [alltime|30d|24h] [Platz]");
							return true;
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "§c/1vs1 setSkull [alltime|30d|24h] [Platz]");
					}
				}
				
				
				
				
				
				
				/*
				 * setLayoutItem Command => Sets the Layout item
				 */
				if(args[0].equalsIgnoreCase("setLayoutItem")) {
					if(args.length == 3) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
						
						
						if(cfg.getConfigurationSection("Layout." + args[1]) != null) {
							
							cfg.set("Layout." + args[1] + ".ItemID", "" + args[2]);
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Layouts"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage(Main.ins.prefixRed + "§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
								
								
							p.sendMessage(Main.ins.prefixGreen + "Layout Item erfolgreich geändert.");
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDieses Layout exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setLayoutItem [Layoutname] [ID]:{SubID}");
					}
					return true;
				}
				
				/*
				 * setLayoutItem Command => Sets the Layout item
				 */
				if(args[0].equalsIgnoreCase("setLayoutAuthor")) {
					if(args.length >= 3) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
						
						
						if(cfg.getConfigurationSection("Layout." + args[1]) != null) {
							
							StringBuilder author = new StringBuilder();
							
							for(int i = 2; i < args.length; i++) 
								author.append(" ").append(args[i]);
							
							
							cfg.set("Layout." + args[1] + ".Author", author.toString().replaceFirst(" ", ""));
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Layouts"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage(Main.ins.prefixRed + "§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
								
								
							p.sendMessage(Main.ins.prefixGreen + "Layout Autor erfolgreich geändert.");
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDieses Layout exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setLayoutItem [Author]");
					}
					return true;
				}
				
				
				/*
				 * Reset Command => Resets a Arena
				 */
				if(args[0].equalsIgnoreCase("resetArena")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						if(cfg.getConfigurationSection("Arena." + args[1]) != null) {
							if(Main.ins.getOneVsOneArena(args[1]).isOk()) {
								if(Main.ins.getOneVsOneArena(args[1]).isReady()) {
									Main.ins.getOneVsOneArena(args[1]).resetArena();
									p.sendMessage(Main.ins.prefixGreen + "Diese Arena wird nun zurückgesetzt!");
								} else {
									p.sendMessage(Main.ins.prefixRed + "§cDiese Arena setzt sich bereits zurück!");
								}
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cDiese Arena hat einen Fehler!");
							}
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 resetArena [Arena]");
					}
				}
				
				/*
				 * Create Command => Creates an Arena 
				 */
				if(args[0].equalsIgnoreCase("create")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						
						if(cfg.getConfigurationSection("Arena." + args[1]) == null) {
							cfg.set("Arena." + args[1] + ".Enabled", true);
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "Arena '" + args[1] + "' erfolgreich erstellt!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert bereits!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 create [Arenaname]");
					}
					return true;
				}
				
				/*
				 * resetList Command => Shows a reset List 
				 */
				if(args[0].equalsIgnoreCase("resetList")) {
					if(args.length == 1) {
						p.sendMessage("§7Folgene Arenen werden zurückgesetzt:");
						for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
							if(!arenas.isReady()) {
								p.sendMessage("§8- §6" + arenas.getName() + " §7(§b" + arenas.getReset().getPercent() +"§7)");
							}
							
							
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 resetList");
					}
					return true;
				}
				
				/*
				 * save Layout Command => Changes a Layout
				 */
				if(args[0].equalsIgnoreCase("saveLayout") || args[0].equalsIgnoreCase("createLayout")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
						
						if(Main.ins.getOneVsOnePlayer(p).getPos1() == null || Main.ins.getOneVsOnePlayer(p).getPos2() == null) {
							p.sendMessage(Main.ins.prefixRed + "§cDu hast nicht beide Positionen gesetzt!");
							return true;
						} 
						
						Location maxLoc = Main.ins.utils.getMaxLoc(Main.ins.getOneVsOnePlayer(p).getPos1(), Main.ins.getOneVsOnePlayer(p).getPos2());
						Location minLoc = Main.ins.utils.getMinLoc(Main.ins.getOneVsOnePlayer(p).getPos1(), Main.ins.getOneVsOnePlayer(p).getPos2());
						
						if(maxLoc == null || minLoc == null) {
							p.sendMessage(Main.ins.prefixRed + "§cBeide Positionen müssen auf der selen Welt sein!");
							return true;
						}
						
						cfg.set("Layout." + args[1] + ".maxX", maxLoc.getBlockX());
						cfg.set("Layout." + args[1] + ".maxY", maxLoc.getBlockY());
						cfg.set("Layout." + args[1] + ".maxZ", maxLoc.getBlockZ());
						
						cfg.set("Layout." + args[1] + ".minX", minLoc.getBlockX());
						cfg.set("Layout." + args[1] + ".minY", minLoc.getBlockY());
						cfg.set("Layout." + args[1] + ".minZ", minLoc.getBlockZ());
						
						cfg.set("Layout." + args[1] + ".world", maxLoc.getWorld().getName());
						
						try {
							cfg.save(Main.ins.utils.getPluginFile("Layouts"));
						} catch (IOException e) {
							e.printStackTrace();
							p.sendMessage(Main.ins.prefixRed + "§cEin Fehler beim Speichern der Datei ist aufgetreten!");
							return true;
						}
							
							
						p.sendMessage(Main.ins.prefixGreen + "Layout '" + args[1] + "' erfolgreich gespeichert!");
						
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 saveLayout [Layoutname]");
					}
					return true;
				}
				
				/*
				 * setPos1 Command => Sets the first Position for the Arena
				 */
				if(args[0].equalsIgnoreCase("setPos1")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						
						if(cfg.getConfigurationSection("Arena." + args[1]) != null) {
							
							cfg.set("Arena." + args[1] + ".Pos1X", p.getLocation().getBlockX());
							cfg.set("Arena." + args[1] + ".Pos1Y", p.getLocation().getBlockY());
							cfg.set("Arena." + args[1] + ".Pos1Z", p.getLocation().getBlockZ());
							cfg.set("Arena." + args[1] + ".Pos1Yaw", p.getLocation().getYaw());
							cfg.set("Arena." + args[1] + ".Pos1Pitch", p.getLocation().getPitch());
							
							
							
							cfg.set("Arena." + args[1] + ".Pos1World", p.getLocation().getWorld().getName());
							
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "Position 1 gesetzt!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setPos1 [Arenaname]");
					}
					return true;
				}
				
				/*
				 * setPos2 Command => Sets the second Position for the Arena
				 */
				if(args[0].equalsIgnoreCase("setPos2")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						
						if(cfg.getConfigurationSection("Arena." + args[1]) != null) {
							
							cfg.set("Arena." + args[1] + ".Pos2X", p.getLocation().getBlockX());
							cfg.set("Arena." + args[1] + ".Pos2Y", p.getLocation().getBlockY());
							cfg.set("Arena." + args[1] + ".Pos2Z", p.getLocation().getBlockZ());
							cfg.set("Arena." + args[1] + ".Pos2Yaw", p.getLocation().getYaw());
							cfg.set("Arena." + args[1] + ".Pos2Pitch", p.getLocation().getPitch());
							
							
							cfg.set("Arena." + args[1] + ".Pos2World", p.getLocation().getWorld().getName());
							
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "Position 2 gesetzt!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setPos2 [Arenaname]");
					}
					return true;
				}
				
				/*
				 * setMiddle Command => Sets the middle Position for the Arena
				 */
				if(args[0].equalsIgnoreCase("setMiddle")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						
						if(cfg.getConfigurationSection("Arena." + args[1]) != null) {
							
							cfg.set("Arena." + args[1] + ".MiddleX", p.getLocation().getBlockX());
							cfg.set("Arena." + args[1] + ".MiddleY", p.getLocation().getBlockY());
							cfg.set("Arena." + args[1] + ".MiddleZ", p.getLocation().getBlockZ());
							
							cfg.set("Arena." + args[1] + ".MiddleWorld", p.getLocation().getWorld().getName());
							
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "Mitte gesetzt!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setMiddle [Arenaname]");
					}
					return true;
				}
				
				/*
				 * setReset Command => Sets the Reset Position for the Arena
				 */
				if(args[0].equalsIgnoreCase("setReset")) {
					if(args.length == 2) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						
						if(cfg.getConfigurationSection("Arena." + args[1]) != null) {
							
							cfg.set("Arena." + args[1] + ".ResetX", p.getLocation().getBlockX());
							cfg.set("Arena." + args[1] + ".ResetY", p.getLocation().getBlockY());
							cfg.set("Arena." + args[1] + ".ResetZ", p.getLocation().getBlockZ());
							
							cfg.set("Arena." + args[1] + ".ResetWorld", p.getLocation().getWorld().getName());
							
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "Reset Position gesetzt!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setReset [Arenaname]");
					}
					return true;
				}
				
				/*
				 * setLayout Command => Sets the Layout for the Arena
				 */
				if(args[0].equalsIgnoreCase("setLayout")) {
					if(args.length == 3) {
						YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
						YamlConfiguration cfg2 = Main.ins.utils.getYaml("Layouts");
						
						if(cfg.getConfigurationSection("Arena." + args[1]) != null) {
						 if(cfg2.getConfigurationSection("Layout." + args[2]) != null) {
							 
						 
							cfg.set("Arena." + args[1] + ".Layout", args[2]);
							
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "Layout gesetzt!");
						 } else {
							 p.sendMessage(Main.ins.prefixRed + "§cDieses Layout exestiert nicht!");
						 }
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDiese Arena exestiert nicht!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setReset [Arenaname] [Layout]");
					}
					return true;
				}
				
				/*
				 * Wand Command => Gives Wand (Default Golden Axe)
				 */
				if(args[0].equalsIgnoreCase("wand")) {
					p.getInventory().addItem(Main.ins.utils.createItem(Main.ins.wandID, Main.ins.wandSubID, 1, null, null));
					return true;
				}
				
				/*
				 * Edit Command => Toggles Edit CMD
				 */
				if(args[0].equalsIgnoreCase("edit")) {
					
					if(Main.ins.getOneVsOnePlayer(p).isEditMode()) {
						
						Main.ins.getOneVsOnePlayer(p).setEditMode(false);
						p.sendMessage(Main.ins.prefixGreen + "§cDu bist nun nicht mehr im Edit-Modus!");
					} else {
						if(!Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
							Main.ins.addPlayer(p.getUniqueId());
							Main.ins.getOneVsOnePlayer(p).setIn1vs1(false);
							Main.ins.getOneVsOnePlayer(p).setpState(PlayerState.UNKNOWN);
						}
						
						Main.ins.getOneVsOnePlayer(p).setEditMode(true);
						p.sendMessage(Main.ins.prefixGreen + "§aDu bist nun im Edit-Modus!");
					}
					
					return true;
				}
				
				/*
				 * setKitEdit => SetKitEdit CMD
				 */
				if(args[0].equalsIgnoreCase("setKitEdit")) {
					
					OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
					
					if(player.getPos1() != null && player.getPos2() != null) {
						
						if(player.getPos1().getWorld().getName().equals(player.getPos2().getWorld().getName())) {
							YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
							
							
							cfg.set("Spawns.KitEdit.X1", player.getPos1().getBlockX());
							cfg.set("Spawns.KitEdit.Y1", player.getPos1().getBlockY());
							cfg.set("Spawns.KitEdit.Z1", player.getPos1().getBlockZ());
							
							cfg.set("Spawns.KitEdit.X2", player.getPos2().getBlockX());
							cfg.set("Spawns.KitEdit.Y2", player.getPos2().getBlockY());
							cfg.set("Spawns.KitEdit.Z2", player.getPos2().getBlockZ());
							
							cfg.set("Spawns.KitEdit.World", player.getPos1().getWorld().getName());
							
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Spawns"));
							} catch (IOException e) {
								p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
								e.printStackTrace();
								return true;
							}
							
							Main.ins.utils.reloadBasics();
							
							p.sendMessage(Main.ins.prefixGreen + "Der Kiteditbereich wurde erfolgreich gesetzt!");
							
							
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cBeide Positionen sind nicht auf der selben Welt!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cDu hast nicht beide Positionen gesetzt!");
					}
					
					return true;
				}
				
				
				/*
				 * Set Lobby Command => Sets the Lobby
				 */
				if(args[0].equalsIgnoreCase("setLobby")) {
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
					
					int x = p.getLocation().getBlockX();
					int y = p.getLocation().getBlockY();
					int z = p.getLocation().getBlockZ();
					
					String world = p.getLocation().getWorld().getName();
					
					cfg.set("Spawns.Lobby.X", x);
					cfg.set("Spawns.Lobby.Y", y);
					cfg.set("Spawns.Lobby.Z", z);
					
					cfg.set("Spawns.Lobby.Yaw", p.getLocation().getYaw());
					cfg.set("Spawns.Lobby.Pitch", p.getLocation().getPitch());
					
					cfg.set("Spawns.Lobby.World", world);
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns"));
					} catch (IOException e) {
						p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
						e.printStackTrace();
						return true;
					}
					
					Main.ins.utils.reloadBasics();
					
					p.sendMessage(Main.ins.prefixGreen + "Der Spawn wurde erfolgreich gesetzt!");
					
					
					return true;
				}
				
				/*
				 * Set Exit Command => Sets the Exit
				 */
				if(args[0].equalsIgnoreCase("setExit")) {
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
					
					int x = p.getLocation().getBlockX();
					int y = p.getLocation().getBlockY();
					int z = p.getLocation().getBlockZ();
					
					String world = p.getLocation().getWorld().getName();
					
					cfg.set("Spawns.Exit.X", x);
					cfg.set("Spawns.Exit.Y", y);
					cfg.set("Spawns.Exit.Z", z);
					
					cfg.set("Spawns.Exit.Yaw", p.getLocation().getYaw());
					cfg.set("Spawns.Exit.Pitch", p.getLocation().getPitch());
					
					cfg.set("Spawns.Exit.World", world);
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns"));
					} catch (IOException e) {
						p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
						e.printStackTrace();
						return true;
					}
					
					Main.ins.utils.reloadBasics();
					
					p.sendMessage(Main.ins.prefixGreen + "Der Ausgang wurde erfolgreich gesetzt!");
					return true;
				}
				
				/*
				 * setQueue Command => Sets the Queue
				 */
				if(args[0].equalsIgnoreCase("setQueue")) {
					
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
					
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					
					String world = p.getLocation().getWorld().getName();
					
					cfg.set("Spawns.Queue.X", x);
					cfg.set("Spawns.Queue.Y", y);
					cfg.set("Spawns.Queue.Z", z);
					
					
					cfg.set("Spawns.Queue.World", world);
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns"));
					} catch (IOException e) {
						p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
						e.printStackTrace();
						return true;
					}
					
					Main.ins.utils.reloadBasics();
					QueueZombie.respawnZombie();
					
					
					p.sendMessage(Main.ins.prefixGreen + "Die Warteschlange wurde erfolgreich gesetzt!");
					return true;
				}
				
				/*
				 * setBlackDealer Command => Sets the Blackdealer
				 */
				if(args[0].equalsIgnoreCase("setBlackDealer")) {
					
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
					
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					
					String world = p.getLocation().getWorld().getName();
					
					cfg.set("Spawns.BlackDealer.X", x);
					cfg.set("Spawns.BlackDealer.Y", y);
					cfg.set("Spawns.BlackDealer.Z", z);
					
					
					cfg.set("Spawns.BlackDealer.World", world);
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns"));
					} catch (IOException e) {
						p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
						e.printStackTrace();
						return true;
					}
					
					Main.ins.utils.reloadBasics();
					BlackDealer.respawnBlackDealer();
					
					
					p.sendMessage(Main.ins.prefixGreen + "Der Blackdealer wurde erfolgreich gesetzt!");
					return true;
				}
				
				/*
				 * setSettingsVillager Command => Sets the Setting Villager
				 */
				if(args[0].equalsIgnoreCase("setKitSettings")) {
					
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
					
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					
					String world = p.getLocation().getWorld().getName();
					
					cfg.set("Spawns.KitSettings.X", x);
					cfg.set("Spawns.KitSettings.Y", y);
					cfg.set("Spawns.KitSettings.Z", z);
					
					cfg.set("Spawns.KitSettings.Yaw", p.getLocation().getYaw());
					cfg.set("Spawns.KitSettings.Pitch", p.getLocation().getPitch());
					
					cfg.set("Spawns.KitSettings.World", world);
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns"));
					} catch (IOException e) {
						p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
						e.printStackTrace();
						return true;
					}
					
					Main.ins.utils.reloadBasics();
					
					p.sendMessage(Main.ins.prefixGreen + "Der Kiteinstellungsvillager wurde erfolgreich gesetzt!");
					return true;
				}
				
				/*
				 * setBlackDealer Command => Sets the Blackdealer
				 */
				if(args[0].equalsIgnoreCase("setBlackDealer")) {
					
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns");
					
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					
					String world = p.getLocation().getWorld().getName();
					
					cfg.set("Spawns.BlackDealer.X", x);
					cfg.set("Spawns.BlackDealer.Y", y);
					cfg.set("Spawns.BlackDealer.Z", z);
					
					cfg.set("Spawns.BlackDealer.Yaw", p.getLocation().getYaw());
					cfg.set("Spawns.BlackDealer.Pitch", p.getLocation().getPitch());
					
					cfg.set("Spawns.BlackDealer.World", world);
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns"));
					} catch (IOException e) {
						p.sendMessage(Main.ins.prefixRed + "§cBeim Speichern der Datei ist ein Fehler aufgetreten!");
						e.printStackTrace();
						return true;
					}
					
					Main.ins.utils.reloadBasics();
					
					p.sendMessage(Main.ins.prefixGreen + "Der Schwarzhändler wurde erfolgreich gesetzt!");
					return true;
				}
				
				
				/*
				 * toggle Command => Joins/Leaves the 1vs1-Mode
				 */
				if(args[0].equalsIgnoreCase("toggle")) {
					
					if(Main.ins.isInOneVsOnePlayers(p.getUniqueId()) && Main.ins.getOneVsOnePlayer(p).getpState() != PlayerState.UNKNOWN) {
						toggle1vs1(p, false, false);
					} else {
						toggle1vs1(p, true, false);
					}
					
					return true;
				}
				
				/*
				 * setCustomKit/setServerKit => Sets a Serverkit
				 */
				if(args[0].equalsIgnoreCase("setCustomKit") || args[0].equalsIgnoreCase("setServerKit")) {
					// /1vs1 setCustomKit [Vorlage]:{SubID} [CustomKitName] 
					if(args.length == 3) {
						if(!Main.ins.database.isUserExists(Main.ins.database.getUUID(args[2]), Main.ins.database.kitsTable)) {
							
							long before = System.currentTimeMillis();
							
							
							Main.ins.database.setCustomKitData(args[2], Main.ins.database.getKit(Main.ins.database.getUUID(args[1]), false, "1"), Main.ins.database.getKit(Main.ins.database.getUUID(args[1]), true, "1"), Main.ins.database.getRawKitSettings(Main.ins.database.getUUID(args[1]), 1));
						
							p.sendMessage("§aFertig nach " + ((System.currentTimeMillis()-before))  + " Millisekunden");
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDieses Kit gehört bereits einen User!");
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setCustomKit [Vorlage]:{SubID} [CustomKitName]");
					}
					
				}
				
				/*
				 * setKitstand => Sets kitstand
				 */
				if(args[0].equalsIgnoreCase("setKitStand")) {
					// /1vs1 setKitstand [Name] [Kit]:{SubID}
					if(args.length == 3) {
						String kit = "";
						int subID = 1;
						if(args[2].contains(":")) {
							
							try {
								
								String[] sKit = args[2].split(":");
								
								kit = sKit[0];
								if(sKit.length >= 2) 
									subID = Integer.parseInt(sKit[1]);
								
							} catch (NumberFormatException e) {
								p.sendMessage(Main.ins.prefixRed + "§cDu musst eine Nummer als SubID angeben!");
								return true;
							}
							
						} else kit = args[2];
						
						YamlConfiguration cfg = Main.ins.utils.getYaml("Kitstands");
						
						cfg.set("Kitstand." + args[1] + ".X", (((double)p.getLocation().getBlockX())+0.5));
						cfg.set("Kitstand." + args[1] + ".Y", (((double)p.getLocation().getBlockY())+0.5));
						cfg.set("Kitstand." + args[1] + ".Z", (((double)p.getLocation().getBlockZ())+0.5));
						
						cfg.set("Kitstand." + args[1] + ".Yaw", p.getLocation().getYaw());
						cfg.set("Kitstand." + args[1] + ".Pitch", p.getLocation().getPitch());
						
						
						
						cfg.set("Kitstand." + args[1] + ".World", p.getLocation().getWorld().getName());
						
						cfg.set("Kitstand." + args[1] + ".Kit", kit);
						cfg.set("Kitstand." + args[1] + ".SubID", subID);
						
						try {
							cfg.save(Main.ins.utils.getPluginFile("Kitstands"));
						} catch (IOException e) {
							e.printStackTrace();
							p.sendMessage(Main.ins.prefixRed + "§cEin Fehler beim Speichern der Datei ist aufgetreten!");
							return true;
						}
						
						
						new OneVsOneKitStandMgr().spawnKitStands();
						
						p.sendMessage(Main.ins.prefixGreen + "§aKitstand gesetzt!");
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 setKitstand [Name] [Kit]:{SubID}");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("removeLayout")) {
					if(args.length == 2) {
						
						if(LayoutMgr.layoutExists(args[1])) {
						 for(OneVsOneArena arena : Main.ins.getOneVsOneArenasCopy().values()) {
							 if(arena.getLayout().equalsIgnoreCase(args[1])) {
								 p.sendMessage(Main.ins.prefixRed + "§cEs gibt noch Arenen, die das Layout '" + args[1] + "' nutzen");
								 return true;
							 }
						 }
							LayoutMgr.removeLayout(args[1]);
							p.sendMessage(Main.ins.prefixGreen + "§7Layout §6'" + args[1] + "' §7entfernt!");
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cLayout '" + args[1] + "' wurde nicht gefunden!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 removeLayout [Name]");
					}
					return true;
				}
				
				if(args[0].equalsIgnoreCase("removeArena")) {
					if(args.length == 2) {
						
						if(Main.ins.isInOneVsOneArenas(args[1]) && Main.ins.getOneVsOneArena(args[1]) != null) {
						 if(!Main.ins.getOneVsOneArena(args[1]).isUsed()) {
							 
							WorldResetMgr.unloadWorldArena(args[1]);
							 
							 
							Main.ins.removeArena(args[1]);
							YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
							
							cfg.set("Arena." + args[1], null);
							
							try {
								cfg.save(Main.ins.utils.getPluginFile("Arenas"));
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cEin Fehler beim Speichern der Datei ist aufgetreten!");
								return true;
							}
							
							
							
							p.sendMessage(Main.ins.prefixGreen + "§7Arena §6'" + args[1] + "' §7entfernt!");
							
						 } else {
							 p.sendMessage(Main.ins.prefixRed + "§cDiese Arena wird noch verwendet!");
						 }
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cLayout '" + args[1] + "' wurde nicht gefunden!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§cNutze: /1vs1 removeArena [Name]");
					}
					return true;
				}
				
				
				if(args[0].equalsIgnoreCase("createWLayout")) {
					if(args.length == 2) {
						
						OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
						if(player.getPos1() != null && player.getPos2() != null) {
							if(player.getPos1().getWorld().getName().equalsIgnoreCase(player.getPos2().getWorld().getName())) {
								
								if(!LayoutMgr.layoutExists(args[1])) {
									
									if(LayoutMgr.addLayout(args[1], true, player.getPos1(), player.getPos2(), p)) {
										p.sendMessage(Main.ins.prefixYellow + "§eWeltLayout wird erstellt! Dieser Prozess kann eine kurze Zeit in anspruch nehmen...");
										p.sendMessage(Main.ins.prefixYellow + "§7Du wirst benachrichtigt, wenn das Layout bereit ist!");
									} else {
										p.sendMessage(Main.ins.prefixRed + "§cEin Fehler ist aufgetreten...");
									}
									
									
									
								} else {
									p.sendMessage(Main.ins.prefixRed + "§cDieses Layout exestiert bereits!");
								}
								
								
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cBeide Positionen müssen auf der gleichen Welt liegen!");
							}
						} else {
							p.sendMessage(Main.ins.prefixRed + "§cDu musst beide Positionen gesetzt haben! (Goldene Axt)");
						}
						
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§c/1vs1 createWLayout [LayoutName]");
					}
				}
				
				if(args[0].equalsIgnoreCase("setWPos1")) {
					if(args.length == 2) {
						if(LayoutMgr.isWorldLayout(args[1])) {
							
							Location playerLocation = p.getLocation();
							Location layoutLocation = LayoutMgr.getMinPos(args[1]);
							
							int disX = playerLocation.getBlockX()-layoutLocation.getBlockX();
							int disY = playerLocation.getBlockY()-layoutLocation.getBlockY();
							int disZ = playerLocation.getBlockZ()-layoutLocation.getBlockZ();
							
							float yaw = playerLocation.getYaw();
							float pitch = playerLocation.getPitch();
							
							Location arenaLocation = new Location(Bukkit.getWorld("1vs1Worlds/Presets/" + args[1]), 0.5+(double)disX, 0+(double)disY+4, 0.5+(double)disZ);
							
							File f = new File("1vs1Worlds/Presets/" + args[1] + "/data.yml");
							YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
							
							cfg.set("Data.Pos1.X", arenaLocation.getX());
							cfg.set("Data.Pos1.Y", arenaLocation.getY());
							cfg.set("Data.Pos1.Z", arenaLocation.getZ());
							cfg.set("Data.Pos1.Yaw", yaw);
							cfg.set("Data.Pos1.Pitch", pitch);
							
							try {
								cfg.save(f);
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cERROR");
								return true;
							}
							
							p.sendMessage("§aPosition 1 gesetzt!");
							
						} else {
							p.sendMessage("§cDieses Layout ist kein WeltArenaLayout...");
						}
					} else {
						p.sendMessage("§c/1vs1 setWPos1 [WeltLayoutName]");
					}
				}
				
				if(args[0].equalsIgnoreCase("setWPos2")) {
					if(args.length == 2) {
						if(LayoutMgr.isWorldLayout(args[1])) {
							
							Location playerLocation = p.getLocation();
							Location layoutLocation = LayoutMgr.getMinPos(args[1]);
							
							int disX = playerLocation.getBlockX()-layoutLocation.getBlockX();
							int disY = playerLocation.getBlockY()-layoutLocation.getBlockY();
							int disZ = playerLocation.getBlockZ()-layoutLocation.getBlockZ();
							
							float yaw = playerLocation.getYaw();
							float pitch = playerLocation.getPitch();
							
							Location arenaLocation = new Location(Bukkit.getWorld("1vs1Worlds/Presets/" + args[1]), 0.5+(double)disX, 0+(double)disY+4, 0.5+(double)disZ);
							
							File f = new File("1vs1Worlds/Presets/" + args[1] + "/data.yml");
							YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
							
							cfg.set("Data.Pos2.X", arenaLocation.getX());
							cfg.set("Data.Pos2.Y", arenaLocation.getY());
							cfg.set("Data.Pos2.Z", arenaLocation.getZ());
							cfg.set("Data.Pos2.Yaw", yaw);
							cfg.set("Data.Pos2.Pitch", pitch);
							
							try {
								cfg.save(f);
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cERROR");
								return true;
							}
							
							p.sendMessage("§aPosition 2 gesetzt!");
							
						} else {
							p.sendMessage("§cDieses Layout ist kein WeltArenaLayout...");
						}
					} else {
						p.sendMessage("§c/1vs1 setWPos2 [WeltLayoutName]");
					}
				}
				
				if(args[0].equalsIgnoreCase("setWMiddle")) {
					if(args.length == 2) {
						if(LayoutMgr.isWorldLayout(args[1])) {
							
							Location playerLocation = p.getLocation();
							Location layoutLocation = LayoutMgr.getMinPos(args[1]);
							
							int disX = playerLocation.getBlockX()-layoutLocation.getBlockX();
							int disY = playerLocation.getBlockY()-layoutLocation.getBlockY();
							int disZ = playerLocation.getBlockZ()-layoutLocation.getBlockZ();
							
							float yaw = playerLocation.getYaw();
							float pitch = playerLocation.getPitch();
							
							Location arenaLocation = new Location(Bukkit.getWorld("1vs1Worlds/Presets/" + args[1]), 0.5+(double)disX, 0+(double)disY+4, 0.5+(double)disZ);
							
							File f = new File("1vs1Worlds/Presets/" + args[1] + "/data.yml");
							YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
							
							cfg.set("Data.Middle.X", arenaLocation.getX());
							cfg.set("Data.Middle.Y", arenaLocation.getY());
							cfg.set("Data.Middle.Z", arenaLocation.getZ());
							cfg.set("Data.Middle.Yaw", yaw);
							cfg.set("Data.Middle.Pitch", pitch);
							
							try {
								cfg.save(f);
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage("§cERROR");
								return true;
							}
							
							p.sendMessage("§aMitte gesetzt!");
							
						} else {
							p.sendMessage("§cDieses Layout ist kein WeltArenaLayout...");
						}
					} else {
						p.sendMessage("§c/1vs1 setWMiddle [WeltLayoutName]");
					}
				}
				
				if(args[0].equalsIgnoreCase("createWArena")) {
					if(args.length == 3) {
						//				1				2
						//createWArena [WeltArenaName] [WeltLayoutName]
						if(LayoutMgr.layoutExists(args[2])) {
							if(LayoutMgr.isWorldLayout(args[2])) {
								
								int worldLayoutReady = LayoutMgr.isWorldLayoutReady(args[2]);
								/*
								 * 1 = Layout not found 
								 * 2 = not World Layout 
								 * 3 = World not ready 
								 * 4 = Pos1 not set 
								 * 5 = Pos2 not set 
								 * 6 = Middle not set
								 */
								if(worldLayoutReady == 1) {
									p.sendMessage(Main.ins.prefixRed + "§cDas Layout konnte nicht gefunden werden!");
									return true;
								} else if(worldLayoutReady == 2) {
									p.sendMessage(Main.ins.prefixRed + "§cDieses Layout ist kein Weltlayout");
									return true;
								} else if(worldLayoutReady == 3) {
									p.sendMessage(Main.ins.prefixRed + "§cDieses Weltlayout ist noch nicht bereit!");
									return true;
								} else if(worldLayoutReady == 4) {
									p.sendMessage(Main.ins.prefixRed + "§cFür dieses Weltlayout ist die 1. Position nicht gesetzt!");
									return true;
								} else if(worldLayoutReady == 5) {
									p.sendMessage(Main.ins.prefixRed + "§cFür dieses Weltlayout ist die 2. Position nicht gesetzt!");
									return true;
								} else if(worldLayoutReady == 6) {
									p.sendMessage(Main.ins.prefixRed + "§cFür dieses Weltlayout ist die Mitte nicht gesetzt!");
									return true;
								}
								
								
								YamlConfiguration arenaYml = Main.ins.utils.getYaml("Arenas");
								
								if(arenaYml.getConfigurationSection("Arena." + args[1]) == null) {
									
									arenaYml.set("Arena." + args[1] + ".WorldArena", true);
									arenaYml.set("Arena." + args[1] + ".Layout", args[2]);
									arenaYml.set("Arena." + args[1] + ".Enabled", true);
									
									
									
									try {
										arenaYml.save(Main.ins.utils.getPluginFile("Arenas"));
									} catch (IOException e) {
										e.printStackTrace();
									}
									Main.ins.addArena(args[1]);
									Main.ins.getOneVsOneArena(args[1]).resetArena();
									p.sendMessage(Main.ins.prefixGreen + "§aArena erstellt");
								} else {
									p.sendMessage("§cDiese Arena exestiert bereits!");
								}
								
								
							} else {
								p.sendMessage("§cDas ausgwählte Layout ist kein Worldlayout");
							}
						} else {
							p.sendMessage("§cDieses Layout exestiert nicht!");
						}
					} else {
						p.sendMessage("§c/1vs1 createWArena [WeltArenaName] [WeltLayoutName]");
					}
				}
				
				
				if(args[0].equalsIgnoreCase("createMassWArena")) {
					if(args.length == 4) {
						//				1				2				3
						//createWArena [WeltArenaName] [WeltLayoutName]	[Anzahl]
						if(LayoutMgr.layoutExists(args[2])) {
							if(LayoutMgr.isWorldLayout(args[2])) {
								
								int amount = 0;
								
								try {
									amount = Integer.parseInt(args[3]);
								} catch (NumberFormatException e) {
									p.sendMessage(Main.ins.prefixRed + "§c");
									return true;
								}
								
								if(amount < 1) {
									p.sendMessage(Main.ins.prefixRed + "§cBitte gebe eine positive Zahl ein!");
									return true;
								}
								
								for(int i = 0; i < amount; i++) {
									YamlConfiguration arenaYml = Main.ins.utils.getYaml("Arenas");
									
									if(arenaYml.getConfigurationSection("Arena." + args[1]) == null) {
										
										arenaYml.set("Arena." + args[1]+"-"+i + ".WorldArena", true);
										arenaYml.set("Arena." + args[1]+"-"+i + ".Layout", args[2]);
										arenaYml.set("Arena." + args[1]+"-"+i + ".Enabled", true);
										
										
										
										try {
											arenaYml.save(Main.ins.utils.getPluginFile("Arenas"));
										} catch (IOException e) {
											e.printStackTrace();
										}
										Main.ins.addArena(args[1]+"-"+i);
										Main.ins.getOneVsOneArena(args[1]+"-"+i).resetArena();
										p.sendMessage(Main.ins.prefixGreen + "§aArena erstellt " + args[1]+"-"+i);
									} else {
										p.sendMessage("§cDiese Arena exestiert bereits!" + args[1]+"-"+i);
									}
								}
								
								
								
								
							} else {
								p.sendMessage("§cDas ausgwählte Layout ist kein Worldlayout");
							}
						} else {
							p.sendMessage("§cDieses Layout exestiert nicht!");
						}
					} else {
						p.sendMessage("§c/1vs1 createMassWArena [WeltArenaName] [WeltLayoutName] [Anzahl]");
					}
				}
			
				if(args[0].equalsIgnoreCase("reloadSigns") || args[0].equalsIgnoreCase("refreshSigns")) {
					//TODO Permission;
					
					Main.ins.reloadSignsAndSkulls();
					Main.ins.refreshSignsAndSkulls();
					
					p.sendMessage(Main.ins.prefixGreen + "Alle Schilder und Köpfe wurden neu geladen!");
				}
				
				if(args[0].equalsIgnoreCase("setHologram") || args[0].equalsIgnoreCase("setHolo") ||
				   args[0].equalsIgnoreCase("setStatHologram") || args[0].equalsIgnoreCase("setStatHolo")) {
					//TODO Permission;
					YamlConfiguration cfg = Main.ins.utils.getYaml("Spawns.yml");
					
					cfg.set("Hologram.X", p.getLocation().getX());
					cfg.set("Hologram.Y", p.getLocation().getY());
					cfg.set("Hologram.Z", p.getLocation().getZ());
					cfg.set("Hologram.World", p.getLocation().getWorld().getName());
					
					
					try {
						cfg.save(Main.ins.utils.getPluginFile("Spawns.yml"));
					} catch (IOException e) {
						e.printStackTrace();
						return true;
					}
					
					StatHolograms.respawnHologramAllPlayers();
					
					p.sendMessage(Main.ins.prefixGreen + "HologramSpawn gesetzt!");
					
					
				}
				
				if(args[0].equalsIgnoreCase("reloadHolograms")) {
					//TODO Permission;
					StatHolograms.respawnHologramAllPlayers();
					
					p.sendMessage(Main.ins.prefixGreen + "Alle Stat Hologramme neu geladen!");
					
					
				}
				
				if(args[0].equalsIgnoreCase("tpWorld")) {
					if(args.length == 2) {
						
						Bukkit.broadcastMessage("" + Bukkit.getWorld("1vs1Worlds/Arenas/" + args[1]).getName());
						
						for(World worlds : Bukkit.getWorlds()) {
							Bukkit.broadcastMessage(worlds.getName());
						}
						
						p.teleport(new Location(Bukkit.getWorld("1vs1Worlds/Arenas/" + args[1]), 0, 100, 0));
					}
					
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("worldinfo")) {
					p.sendMessage("" + p.getLocation().getWorld());
					
					
					return true;
				}
					
				
				if(args[0].equalsIgnoreCase("teleportToArena") || 
						args[0].equalsIgnoreCase("teleportArena") || 
						args[0].equalsIgnoreCase("tpArena") || 
						args[0].equalsIgnoreCase("tpToArena")) {
					//TODO PERMISSION
					if(args.length == 2) {
						if(Main.ins.isInOneVsOneArenas(args[1])) {
							OneVsOneArena arena = Main.ins.getOneVsOneArena(args[1]);
							
							
							if(arena.getMiddleLoc() != null && arena.getMiddleLoc().getWorld() != null) {
								p.teleport(arena.getMiddleLoc());
								
							} else if(arena.getLocPos1() != null && arena.getLocPos1().getWorld() != null) {
								p.teleport(arena.getLocPos1());
								
							} else if(arena.getLocPos2() != null && arena.getLocPos2().getWorld() != null) {
								p.teleport(arena.getLocPos2());
								
							} else if(arena.getResetLoc() != null && arena.getResetLoc().getWorld() != null) {
								p.teleport(arena.getResetLoc());
								
							} else if(arena.getBuildCorner1() != null && arena.getBuildCorner1().getWorld() != null) {
								p.teleport(arena.getBuildCorner1());
								
							} else if(arena.getBuildCorner2() != null && arena.getBuildCorner2().getWorld() != null) {
								p.teleport(arena.getBuildCorner2());
								
							} else {
								p.sendMessage(Main.ins.prefixRed + "Es wurde keine Position gefunden, für diese Arena!");
								return true;
							}
							
							p.sendMessage(Main.ins.prefixGreen + "§7Du wurdest in die Arena §6" + args[1] + " §7teleportiert.");
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "Arena §6" + args[1] + " §7nicht gefunden!");
						}
					} else {
						p.sendMessage(Main.ins.prefixRed + "Nutze: /1vs1 tpArena [Arena]");
					}
					
					
				}
				
				if(args[0].equalsIgnoreCase("teleportTo") || 
						args[0].equalsIgnoreCase("tpTo") || 
						args[0].equalsIgnoreCase("teleport") || 
						args[0].equalsIgnoreCase("tp")) {
					if(args.length == 2) {
						
						if(args[1].equalsIgnoreCase("MainSpawn")) {
							if(Main.ins.utils.getMainSpawn() != null && Main.ins.utils.getMainSpawn().getWorld() != null) {
								Main.ins.utils.tpToLobby(p);
							} else {
								p.sendMessage(Main.ins.prefixRed + "Mainspawn wurde noch nicht gesetzt!");
							}
						} else if(args[1].equalsIgnoreCase("ExitSpawn")) {
							if(Main.ins.utils.getExitSpawn() != null && Main.ins.utils.getExitSpawn().getWorld() != null) {
								p.teleport(Main.ins.utils.getExitSpawn());
							} else {
								p.sendMessage(Main.ins.prefixRed + "ExitSpawn wurde noch nicht gesetzt!");
							}
						} else if(args[1].equalsIgnoreCase("Hologram")) {
							if(Main.ins.utils.getHoloSpawn() != null && Main.ins.utils.getHoloSpawn().getWorld() != null) {
								p.teleport(Main.ins.utils.getHoloSpawn());
							} else {
								p.sendMessage(Main.ins.prefixRed + "Hologramspawn wurde noch nicht gesetzt!");
							}
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "Nutze /1vs1 tp [MainSpawn|ExitSpawn|Hologram]");
					}
				}
				
				
			} else {
				//TODO HILFE
			}
					
			
			
		} else {
			sender.sendMessage(Main.ins.prefixRed + "§cDu musst ein Spieler sein, um den 1vs1 Befehl nutzen zu koennen!");
		}
		
		return true;
	}
	
	public static void toggle1vs1(Player p, boolean in1vs1, boolean shutdown) {
		if(!in1vs1) {
			
			StatHolograms.despawnHologram(Main.ins.getOneVsOnePlayer(p));
    		
			
			
//			TODO Turniermgr TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
//			if(!shutdown) if(tMgr != null) tMgr.delete();
			
			
			ChallangeMgr.removeComplete(p);
	   		
			
			//ScoreBoardManager.updateBoard(p);
			
			
			p.setHealth(20);
			p.setFoodLevel(20);
			
			
			
			p.setAllowFlight(false);
			p.setFlying(false);
    		p.getInventory().setArmorContents(null);
    		p.getInventory().clear();
    		
    		
    		
    		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
    		p.setScoreboard(board);
    		//TODO SBoard Mgr ScoreBoardManager.removeBoard(p);
    		//TODO Kit CMD remove Kit.hasKit.remove(p);
    		
    		Main.ins.scoreAPI.remove(p);
    		Main.ins.scoreAPI.removeTablist(p, "1vs1-Arena", p.getName());
    		Main.ins.scoreAPI.removeTablist(p, "1vs1-Lobby", p.getName());
    		
    		for(Player players : Bukkit.getOnlinePlayers()) {
    			players.showPlayer(p);
    			p.showPlayer(players);
    		}
    		
    		Location exit = Main.ins.utils.getExitSpawn();
			
    		
    		if(exit != null) {
				p.teleport(exit);
			} else {
				p.teleport(p.getLocation().getWorld().getSpawnLocation());
				p.sendMessage("§cDer Ausgang wurde nicht gesetzt! Du wurdest zum Weltspawn teleportiert!");
			}
    		
    		
    		if(Main.ins.saveOldScoreboard) 
				if(Main.ins.getOneVsOnePlayer(p).getOldBoard() != null) p.setScoreboard(Main.ins.getOneVsOnePlayer(p).getOldBoard());
			
    		
    		if(Main.ins.saveInvs) {
    			if(Main.ins.getOneVsOnePlayer(p).getPlayerInv() != null) {
    				p.getInventory().setContents(Main.ins.getOneVsOnePlayer(p).getPlayerInv());
        			p.updateInventory();
        		}
        		if(Main.ins.getOneVsOnePlayer(p).getPlayerArmor() != null) 
        			p.getInventory().setArmorContents(Main.ins.getOneVsOnePlayer(p).getPlayerArmor());
        		
        		
        		p.setExp(Main.ins.getOneVsOnePlayer(p).getPlayerXP());
        		p.setLevel(Main.ins.getOneVsOnePlayer(p).getPlayerLvl());
        		
        		
        		
        		p.updateInventory();
    		}
    		

    		p.setGameMode(GameMode.SURVIVAL);
//    		TODO SB entfernen2 plugin.scoreAPI.removeBoard(p);
    		for(PotionEffect effect : p.getActivePotionEffects())  p.removePotionEffect(effect.getType());

    		Main.ins.removePlayer(p.getUniqueId());
    		
		} else {
			
			Location lobby = Main.ins.utils.getMainSpawn();
			
			if(lobby == null) {
				p.sendMessage(Main.ins.prefixRed + "§cDie Lobby wurde nicht gesetzt!");
				return;
			}
			
			Main.ins.addPlayer(p.getUniqueId());
			
			OneVsOnePlayer player = Main.ins.getOneVsOnePlayer(p);
			
			if(Main.ins.saveOldScoreboard) 
    		 if(p.getScoreboard() != null) Main.ins.getOneVsOnePlayer(p).setOldBoard(p.getScoreboard());;
			
			
			if(Main.ins.saveInvs) {
    			player.setPlayerInv(p.getInventory().getContents());
    			player.setPlayerArmor(p.getInventory().getArmorContents());
    			player.setPlayerXP(p.getExp());
    			player.setPlayerLvl(p.getLevel());
    		}
			
			
			Main.ins.utils.tpToLobby(p);
    		
    		
    		p.getInventory().setArmorContents(null);
    		p.setAllowFlight(false);
			p.setFlying(false);
    		
    		p.setHealth(20);
    		p.setMaxHealth(20);
    		p.setFoodLevel(20);
    		p.setExp(0);
    		p.setLevel(0);
    		p.setNoDamageTicks(0);
    		
    		Main.ins.utils.giveLobbyItems(p);
    		p.updateInventory();
    		for(PotionEffect effect : p.getActivePotionEffects())  p.removePotionEffect(effect.getType());

    		
    		//TODO (My)SQL plugin.getDBMgr().loadUserData(p);
    	
    		
    		p.setGameMode(GameMode.ADVENTURE);
    		
    		StatHolograms.spawnHologram(Main.ins.getOneVsOnePlayer(p));
    		
		}
	}
	
	@SuppressWarnings("unused")
	private void sendHelp(Player p, int Page) {
		
		int maxPage = 10;
		
		
		p.sendMessage("§f-§6-§f-§6-§f-§6-§f-§6-§f-§6-§f[§9§l1vs1§r§f]§6-§f-§6-§f-§6-§f-§6-§f-§6-§f-");
		p.sendMessage("§c");
		
		if(Page == 1) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bArenen:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 resetAllArenas");
			p.sendMessage(" §7?? §f/1vs1 create [Arena]");
			p.sendMessage(" §7?? §6/1vs1 setting [Arena] Spawn1");
			p.sendMessage(" §7?? §f/1vs1 setting [Arena] Spawn2");
			p.sendMessage(" §7?? §6/1vs1 setting [Arena] setMiddle");
			
		}
		
		if(Page == 2) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bArenen:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §f/1vs1 setting [Arena] setMiddle");
			p.sendMessage(" §7?? §6/1vs1 setting [Arena] setReset");
			p.sendMessage(" §7?? §f/1vs1 setting [Arena] Layout [Layout]");
			p.sendMessage(" §7?? §6/1vs1 addACSArena [Arena] [ACS-Name]");
			p.sendMessage(" §7?? §f/1vs1 cloneArena [Arena] [NewArena]");
			
		}
		
		if(Page == 3) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bArenen:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 toggleArena [Arena]");
			p.sendMessage(" §7?? §f/1vs1 delete [Arena]");
			p.sendMessage(" §7?? §6/1vs1 tpArena [Arena]");
			p.sendMessage(" §7?? §f/1vs1 reset [Arena]");
		}
		
		if(Page == 4) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bConfigs:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 reloadSigns");
			p.sendMessage(" §7?? §f/1vs1 reloadConfig");
			p.sendMessage(" §7?? §6/1vs1 reloadMessages");
		}
		
		if(Page == 5) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bEinrichtung:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §f/1vs1 setLobby");
			p.sendMessage(" §7?? §6/1vs1 setExit");
			p.sendMessage(" §7?? §f/1vs1 guide");
			p.sendMessage(" §7?? §6/1vs1 setKitEdit");
			p.sendMessage(" §7?? §f/1vs1 setQueue");
		}
		
		if(Page == 6) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bConfigs:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 setBlackDealer");
			p.sendMessage(" §7?? §f/1vs1 setSkull");
			p.sendMessage(" §7?? §6/1vs1 setSkull30");
			p.sendMessage(" §7?? §f/1vs1 setSettingsVillager");
		}
		
		if(Page == 7) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bKits & Kitstands:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 deleteLayout [Layout]");
			p.sendMessage(" §7?? §f/1vs1 saveArenaLayout [Name]");
			p.sendMessage(" §7?? §6/1vs1 setting LayoutItemID [Layout] [ID]:{SubID}");
			p.sendMessage(" §7?? §f/1vs1 setting LayoutAuthor [Layout] [Autor]");
		}
		
		if(Page == 8) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bSpieler:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 toggle");
			p.sendMessage(" §7?? §f/1vs1 edit");
			p.sendMessage(" §7?? §6/1vs1 setting LayoutItemID [Layout] [ID]:{SubID}");
			p.sendMessage(" §7?? §f/1vs1 setRankPoints [Name] [Punkte]");
		}
		
		if(Page == 9) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bSonstiges:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §6/1vs1 version");
			p.sendMessage(" §7?? §f/1vs1 in1vs1");
			p.sendMessage(" §7?? §6/1vs1 queue");
			p.sendMessage(" §7?? §f/1vs1 resetList");
			p.sendMessage(" §7?? §6/1vs1 check");
		}
		
		if(Page == 10) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bSonstiges:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7?? §f/1vs1 resetStats [Name]");
			p.sendMessage(" §7?? §6/1vs1 listArenas");
			p.sendMessage(" §7?? §f/1vs1 help {1-X}");
		}
		
		p.sendMessage("§c");
		p.sendMessage("§f-§6-§f-§6-§f-§6-§f-§6-§f-§6-§f[§9§l1vs1§r§f]§6-§f-§6-§f-§6-§f-§6-§f-§6-§f-");
	}
	
		
	
	
	
}
