package de.onevsone.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import de.onevsone.methods.KitMgr;
import de.onevsone.methods.QueueManager;
import de.onevsone.methods.SoundMgr.JSound;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.onevsone.Main;
import de.onevsone.arenas.SpectateMgr;
import de.onevsone.arenas.builder.BlockMapReset;
import de.onevsone.arenas.builder.worldReset.WorldResetMgr;
import de.onevsone.database.sql.Database;
import de.onevsone.enums.KitPrefs;
import de.onevsone.enums.OvOColor;
import de.onevsone.enums.BestOfsPrefs;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.ChallangeMgr;

public class OneVsOneArena {

	private boolean ok = false;
	private boolean ready = false;
	private String name;
	private boolean locked = false;

	private ArrayList<UUID> pos1 = new ArrayList<UUID>();
	private ArrayList<UUID> pos2 = new ArrayList<UUID>();

	private ArrayList<UUID> death = new ArrayList<UUID>();
	private ArrayList<UUID> spectators = new ArrayList<UUID>();

	private BestOfsPrefs bestOf = BestOfsPrefs.BESTOF1;
	private int bestOfWinsP1 = 0;
	private int bestOfWinsP2 = 0;
	private int timer = 0;

	private int maxFightTime = 0;

	private String kitName = "-";

	private OvOColor pos1Color;
	private OvOColor pos2Color;

	Location locPos1;
	Location locPos2;

	Location corner1;
	Location corner2;

	Location buildCorner1;
	Location buildCorner2;

	int startCountdown = 3;
	int endMatchTimer = 300;

	boolean started = false;
	boolean ended = false;
	boolean used = false;

	boolean ranked = false;

	boolean tournament = false;
	UUID tournamentID;

	ArrayList<KitPrefs> settings = new ArrayList<KitPrefs>();

	String layout = "";

	Location middleLoc;
	Location resetLoc;

	BlockMapReset reset;

	String kit = "";
	int subID = 0;

	BukkitRunnable mainTask;
	BukkitRunnable builderTask;
	private int groupID1;
	private int groupID2;

	private boolean worldArena = false;
	private String arenaWorldName = "";
	
	public OneVsOneArena(String name) {
		this.name = name;
	}

	public void setColorPos1(OvOColor color) {
		if (color.getSubID() == 16) {
			Random r = new Random();
			pos1Color = OvOColor.resolveBySubID(r.nextInt(16));
		} else {
			pos1Color = color;
		}
	}

	public void setColorPos2(OvOColor normal, OvOColor alternate) {
		if (normal.getSubID() == 16) {
			Random r = new Random();
			pos2Color = OvOColor.resolveBySubID(r.nextInt(16));
			if (pos2Color.equals(pos1Color))
				setColorPos2(normal, alternate);
		} else {
			if (normal == pos1Color) {
				pos2Color = alternate;
			} else {
				pos2Color = normal;
			}
		}
	}

	public OvOColor getColorPos1() {
		return pos1Color;
	}

	public OvOColor getColorPos2() {
		return pos2Color;
	}

	
	public void join(ArrayList<Player> p1, ArrayList<Player> p2, String kit, int subID, UUID tournament, int groupID1, int groupID2) {
		
		this.used = true;
		this.tournament = true;
		this.tournamentID = tournament;
		this.groupID1 = groupID1;
		this.groupID2 = groupID2;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				join(p1, p2, kit, subID);
				
			}
		}.runTask(Main.ins);
	}
	
	public void join(ArrayList<Player> p1, ArrayList<Player> p2, String kit, int subID) {
		this.used = true;
		if (kit.contains(":")) {
			if (kit.split(":").length == 2) {
				try {
					subID = Integer.parseInt(kit.split(":")[1]);
				} catch (NumberFormatException e) {
				}
			}
			kit = kit.split(":")[0];
		}

		
		this.kit = kit;
		try {
			this.kitName = KitMgr.getKitName(UUID.fromString(kit));
		} catch (Exception e) {
			this.kitName = kit;
		}

		this.subID = subID;

		for (Player players : p1) {
			pos1.add(players.getUniqueId());
			players.teleport(locPos1);
			Main.ins.getOneVsOnePlayer(players).setArena(getName());
			Main.ins.getOneVsOnePlayer(players).setpState(PlayerState.INARENA);
			Main.ins.getOneVsOnePlayer(players).setInQueue(false);
			players.getInventory().clear();
			players.getInventory().setArmorContents(null);
			KitMgr.loadKit(players, getKit(), false, subID);
			KitMgr.loadKit(players, getKit(), true, subID);
			setColorPos1(OvOColor.resolveBySubID(Main.ins.database.getColor(players.getUniqueId(), true, false)));
		}
		
		for (Player players : p2) {
			pos2.add(players.getUniqueId());
			players.teleport(locPos2);
			Main.ins.getOneVsOnePlayer(players).setArena(getName());
			Main.ins.getOneVsOnePlayer(players).setpState(PlayerState.INARENA);
			Main.ins.getOneVsOnePlayer(players).setInQueue(false);
			players.getInventory().clear();
			players.getInventory().setArmorContents(null);
			KitMgr.loadKit(players, getKit(), false, subID);
			KitMgr.loadKit(players, getKit(), true, subID);
			setColorPos2(OvOColor.resolveBySubID(Main.ins.database.getColor(players.getUniqueId(), false, false)),
					OvOColor.resolveBySubID(Main.ins.database.getColor(players.getUniqueId(), false, true)));
		}
		
		
		
		

		if (getColorPos1() != null) {
			for (int i = 0; i < 4; i++) {
				for (Player players : p1) {
					if (players.getInventory().getArmorContents()[i].getType().toString().contains("LEATHER")) {
						LeatherArmorMeta meta = (LeatherArmorMeta) players.getInventory().getArmorContents()[i]
								.getItemMeta();
						meta.setColor(getColorPos1().getColor());
						
						players.getInventory().getArmorContents()[i].setItemMeta(meta);
						players.updateInventory();
					}
				}
			}
		}

		if (getColorPos2() != null) {
			for (int i = 0; i < 4; i++) {
				for (Player players : p2) {
					if (players.getInventory().getArmorContents()[i].getType().toString().contains("LEATHER")) {
						LeatherArmorMeta meta = (LeatherArmorMeta) players.getInventory().getArmorContents()[i]
								.getItemMeta();
						meta.setColor(getColorPos2().getColor());
						players.getInventory().getArmorContents()[i].setItemMeta(meta);
						players.updateInventory();
					}
				}
			}
		}

		for (Player players : p1) {
			try {
				KitMgr.sendKitInfos(UUID.fromString(getKit()), subID, players);
			} catch (IllegalArgumentException e) {
				KitMgr.sendKitInfos(getKit(), players);
			}
		}

		for (Player players : p2) {
			try {
				KitMgr.sendKitInfos(UUID.fromString(getKit()), subID, players);
			} catch (IllegalArgumentException e) {
				KitMgr.sendKitInfos(getKit(), players);
			}
		}

		String[] datas = null;

		try {
			datas = Main.ins.database.getRawKitSettings(UUID.fromString(getKit()), subID).split("");
		} catch (Exception e) {
			datas = Main.ins.database.getRawKitSettings(getKit()).split("");
		}

		for (int i = 0; i < datas.length; i++)
			if (datas[i].equalsIgnoreCase("1"))
				settings.add(KitPrefs.getPrefByID(i));

		for (Player players : p1) ChallangeMgr.removeComplete(players);
		for (Player players : p2) ChallangeMgr.removeComplete(players);

		if (isRanked() && getBestOf() == BestOfsPrefs.BESTOF1) {
			for (Player players : p1)
				Main.ins.utils.sendTitle(players, 10, 20 * 2, 10, "§6§lRANKED", "");
			for (Player players : p2)
				Main.ins.utils.sendTitle(players, 10, 20 * 2, 10, "§6§lRANKED", "");
		} else {
			setRanked(false);
		}

		if (getBestOf() != BestOfsPrefs.BESTOF1) {
			String nameP1 = "";
			String nameP2 = "";

			if (Main.ins.getOneVsOnePlayer(p1.get(0)).getTeamObj() != null) {
				nameP1 = Main.ins.getOneVsOnePlayer(p1.get(0)).getTeamObj().getTeamName(true);
			} else {
				nameP1 = p1.get(0).getDisplayName();
			}

			if (Main.ins.getOneVsOnePlayer(p2.get(0)).getTeamObj() != null) {
				nameP2 = Main.ins.getOneVsOnePlayer(p2.get(0)).getTeamObj().getTeamName(true);
			} else {
				nameP2 = p2.get(0).getDisplayName();
			}

			if (getBestOf() == BestOfsPrefs.BESTOF3) {
				for (Player players : p1)
					Main.ins.utils.sendTitle(players, 10, 20 * 2, 10, "§cBest of 3",
							"§7" + nameP1 + " §6" + getBestOfWinsP1() + " §8| §6" + getBestOfWinsP2() + " §7" + nameP2);

				for (Player players : p2)
					Main.ins.utils.sendTitle(players, 10, 20 * 2, 10, "§cBest of 3",
							"§7" + nameP1 + " §6" + getBestOfWinsP1() + " §8| §6" + getBestOfWinsP2() + " §7" + nameP2);

			} else if (getBestOf() == BestOfsPrefs.BESTOF5) {
				for (Player players : p1)
					Main.ins.utils.sendTitle(players, 10, 20 * 2, 10, "§cBest of 5",
							"§7" + nameP1 + " §6" + getBestOfWinsP1() + " §8| §6" + getBestOfWinsP2() + " §7" + nameP2);

				for (Player players : p2)
					Main.ins.utils.sendTitle(players, 10, 20 * 2, 10, "§cBest of 5",
							"§7" + nameP1 + " §6" + getBestOfWinsP1() + " §8| §6" + getBestOfWinsP2() + " §7" + nameP2);
			}
		}
		setMaxFightTime(Main.ins.maxFightTime);

		startScheduler();

	}

	public void removeArenaEntitys() {
		
		if(isWorldArena()) return;
		
		if(corner1 != null && corner1.getWorld() != null && corner1.getWorld().getEntities() != null) {
			for (Entity en : corner1.getWorld().getEntities()) {

				int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
				int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
				int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
				int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

				int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
				int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());

				Location Min = new Location(corner1.getWorld(), minX, minY, minZ);
				Location Max = new Location(corner1.getWorld(), maxX, maxY, maxZ);

				if (Main.ins.utils.checkRegion(en.getLocation(), Min, Max))
					if (!(en instanceof Player))
						en.remove();

			}
		}
		

	}

	public void startScheduler() {

		if (mainTask != null)
			mainTask.cancel();
		if (builderTask != null)
			builderTask.cancel();

		mainTask = new BukkitRunnable() {

			@SuppressWarnings("unchecked")
			public void run() {

				if (started) {
					
					timer++;
					
					int remainingTime = (maxFightTime+1 - timer)  ;
					
					if(timer != 1) remainingTime--;
					
					
					if (remainingTime <= 0) {

						if (isEnded())
							return;

						double minDistancePos1 = Integer.MAX_VALUE;
						double minDistancePos2 = Integer.MAX_VALUE;

						for (UUID players1 : pos1) {
							if (Bukkit.getPlayer(players1) != null) {
								if (Bukkit.getPlayer(players1).getLocation()
										.distance(getMiddleLoc()) < minDistancePos1) {
									minDistancePos1 = Bukkit.getPlayer(players1).getLocation().distance(getMiddleLoc());
								}
							}
						}

						for (UUID players2 : pos2) {
							if (Bukkit.getPlayer(players2) != null) {
								if (Bukkit.getPlayer(players2).getLocation()
										.distance(getMiddleLoc()) < minDistancePos2) {
									minDistancePos2 = Bukkit.getPlayer(players2).getLocation().distance(getMiddleLoc());
								}
							}
						}

						
						
						for (UUID uuids : (ArrayList<UUID>)pos1.clone()) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.CHICKEN_HURT, 1.0f, 1.0f);
								if(!(minDistancePos1 <= minDistancePos2)) {
									death(uuids);
								}
							}
						}

						for (UUID uuids : (ArrayList<UUID>)pos2) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.CHICKEN_HURT, 1.0f, 1.0f);
								if((minDistancePos1 <= minDistancePos2)) {
									death(uuids);
								}
							}
						}
						
//						endFight((minDistancePos1 <= minDistancePos2), pos1, pos2);
						stopScheduler(false);
						return;
					}
					
					if (remainingTime >= 60 && (remainingTime % 60 == 0)) {
						int remainingMins = 0;

						remainingMins = maxFightTime / 60 - (timer / 60);

						for (UUID players1 : pos1) {
							if (Bukkit.getPlayer(players1) != null) {
								if (remainingMins == 1) {
									Main.ins.utils.sendActionBar(Bukkit.getPlayer(players1),
											"§7Der Kampf endet in §6einer Minute!");
									Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players1), JSound.CLICK, 1,
											1);
								} else {
									Main.ins.utils.sendActionBar(Bukkit.getPlayer(players1),
											"§7Der Kampf endet in §6" + remainingMins + " Minuten!");
									Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players1), JSound.CLICK, 1,
											1);
								}
							}
						}
						for (UUID players2 : pos2) {
							if (Bukkit.getPlayer(players2) != null) {
								if (remainingMins == 1) {
									Main.ins.utils.sendActionBar(Bukkit.getPlayer(players2),
											"§7Der Kampf endet in §6einer Minute!");
									Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players2), JSound.CLICK, 1,
											1);
								} else {
									Main.ins.utils.sendActionBar(Bukkit.getPlayer(players2),
											"§7Der Kampf endet in §6" + remainingMins + " Minuten!");
									Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players2), JSound.CLICK, 1,
											1);
								}
							}
						}
					} else if (remainingTime < 60 && remainingTime > 0 && (remainingTime % 10 == 0 || remainingTime == 5
							|| remainingTime == 4 || remainingTime == 3 || remainingTime == 2)) {
						for (UUID players1 : pos1) {
							if (Bukkit.getPlayer(players1) != null) {

								Main.ins.utils.sendActionBar(Bukkit.getPlayer(players1),
										"§7Der Kampf endet in §6" + remainingTime + " Sekunden!");
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players1), JSound.CLICK, 1, 1);

							}
						}
						for (UUID players2 : pos2) {
							if (Bukkit.getPlayer(players2) != null) {

								Main.ins.utils.sendActionBar(Bukkit.getPlayer(players2),
										"§7Der Kampf endet in §6" + remainingTime + " Sekunden!");

								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players2), JSound.CLICK, 1, 1);
							}
						}
					} else if (remainingTime == 1) {
						for (UUID players1 : pos1) {
							if (Bukkit.getPlayer(players1) != null) {

								Main.ins.utils.sendActionBar(Bukkit.getPlayer(players1),
										"§7Der Kampf endet in §6einer Sekunde!");
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players1), JSound.CLICK, 1, 1);

							}
						}
						for (UUID players2 : pos2) {
							if (Bukkit.getPlayer(players2) != null) {

								Main.ins.utils.sendActionBar(Bukkit.getPlayer(players2),
										"§7Der Kampf endet in §6einer Sekunde!");
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players2), JSound.CLICK, 1, 1);

							}
						}
					} 

					
				}

				if (ready && !started) {

					if (startCountdown >= 2) {
						for (UUID uuids : pos1) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids),
										"§7Der Kampf startet in §6" + startCountdown + " Sekunden!");
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.ORB_PICKUP, 1.0f,
										1.0f);

								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);
							}
						}
						for (UUID uuids : pos2) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids),
										"§7Der Kampf startet in §6" + startCountdown + " Sekunden!");
								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.ORB_PICKUP, 1.0f,
										1.0f);

								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);
							}
						}
						startCountdown--;
					} else if (startCountdown == 1) {
						for (UUID uuids : pos1) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids),
										"§7Der Kampf startet in §6einer Sekunde!");

								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.ORB_PICKUP, 1.0f,
										1.0f);

								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);
							}
						}
						for (UUID uuids : pos2) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids),
										"§7Der Kampf startet in §6einer Sekunde!");

								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.ORB_PICKUP, 1.0f,
										1.0f);

								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);
							}
						}
						startCountdown--;
					} else {
						
						//Kitstats update!
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								Main.ins.database.setKitStats(getKit(), 1, getSubID(), Main.ins.database.getKitStats(getKit(), 1, getSubID())+1);
								Main.ins.database.setKitStats(getKit(), 2, getSubID(), Main.ins.database.getKitStats(getKit(), 2, getSubID())+1);
								Main.ins.database.setKitStats(getKit(), 3, getSubID(), Main.ins.database.getKitStats(getKit(), 3, getSubID())+1);
								
							}
						}.runTaskAsynchronously(Main.ins);
						//---
						
						
						for (UUID uuids : pos1) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids),
										"§6Der Kampf beginnt! Viel Glück!");

								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.LEVEL_UP, 1.0f,
										1.0f);

								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);

								if (settings.contains(KitPrefs.DOUBLEJUMP)) {

									new BukkitRunnable() {

										@Override
										public void run() {
											Bukkit.getPlayer(uuids).setAllowFlight(true);
										}
									}.runTask(Main.ins);

								}
							}
						}
						for (UUID uuids : pos2) {
							if (Bukkit.getPlayer(uuids) != null) {
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids),
										"§6Der Kampf beginnt! Viel Glück!");

								Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.LEVEL_UP, 1.0f,
										1.0f);

								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);

								if (settings.contains(KitPrefs.DOUBLEJUMP)) {

									new BukkitRunnable() {

										@Override
										public void run() {
											Bukkit.getPlayer(uuids).setAllowFlight(true);
										}
									}.runTask(Main.ins);

								}

							}
						}

						started = true;
						removeArenaEntitys();
					}

				}

			}
		};

		builderTask = new BukkitRunnable() {

			public void run() {
				if (!ready || !started) {
					for (UUID uuids : pos1) {
						if (Bukkit.getPlayer(uuids) != null) {
							if (Bukkit.getPlayer(uuids).getLocation().distance(locPos1) > 0.5) {
								Bukkit.getPlayer(uuids).teleport(locPos1);
								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);
							}
							if (!ready)
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids), "§6Arena wird vorbereitet...");
						}
					}
					for (UUID uuids : pos2) {
						if (Bukkit.getPlayer(uuids) != null) {
							if (Bukkit.getPlayer(uuids).getLocation().distance(locPos2) > 0.5) {
								Bukkit.getPlayer(uuids).teleport(locPos2);
								Bukkit.getPlayer(uuids).setHealth(Bukkit.getPlayer(uuids).getMaxHealth());
								Bukkit.getPlayer(uuids).setFoodLevel(22);
							}
							if (!ready)
								Main.ins.utils.sendActionBar(Bukkit.getPlayer(uuids), "§6Arena wird vorbereitet...");
						}
					}
				}

			}
		};

		mainTask.runTaskTimerAsynchronously(Main.ins, 0, 20);
		builderTask.runTaskTimerAsynchronously(Main.ins, 0, 1);

	}

	@SuppressWarnings("unchecked")
	public void death(UUID player) {

		if (ended) return;
			

		death.add(player);

		ArrayList<UUID> pos1Copy = (ArrayList<UUID>) pos1.clone();
		ArrayList<UUID> pos2Copy = (ArrayList<UUID>) pos2.clone();

		
		pos1Copy.remove(player);
		
		
		pos2Copy.remove(player);
		

		pos1Copy.removeAll(death);
		pos2Copy.removeAll(death);

		if (Bukkit.getPlayer(player) != null)
			Bukkit.getPlayer(player).setMaximumNoDamageTicks(10);

		for (UUID uuids : pos1) {
			if (Bukkit.getPlayer(uuids) != null) {
				Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.CHICKEN_HURT, 1.0f, 1.0f);
			}
		}

		for (UUID uuids : pos2) {
			if (Bukkit.getPlayer(uuids) != null) {
				Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuids), JSound.CHICKEN_HURT, 1.0f, 1.0f);

			}
		}

		if (pos1Copy.size() == 0 || pos2Copy.size() == 0) {
			
			stopScheduler(true);

			ArrayList<UUID> pos1C = (ArrayList<UUID>) pos1.clone();
			ArrayList<UUID> pos2C = (ArrayList<UUID>) pos2.clone();

			if (pos1Copy.size() == 0) {
				endFight(false, pos1C, pos2C);
				return;
			}
			if (pos2Copy.size() == 0) {
				endFight(true, pos1C, pos2C);
				return;
			}
		} else {
			
			if (Bukkit.getPlayer(player) != null) 
				SpectateMgr.spectate(Bukkit.getPlayer(player), getName(), false, true);
			
		}

	}

	
	//TODO HIER IST DIE ENDFIGHT METHODE
	@SuppressWarnings("unchecked")
	public void endFight(boolean p1Winner, ArrayList<UUID> p1, ArrayList<UUID> p2) {
		//Check if Arena was already ended...
		if (ended) return;
		setEnded(true);
		
		
		//Fight DB creation
		ArrayList<UUID> p1Copy = (ArrayList<UUID>) p1.clone();
		ArrayList<UUID> p2Copy = (ArrayList<UUID>) p2.clone();
		boolean wRanked = ranked;
		UUID wTournament =  tournamentID;
		OvOColor wP1Color = getColorPos1();
		OvOColor wP2Color = getColorPos2();
		int wDuration = getFightTimeSeconds();
		
		ArrayList<String> p1Names = new ArrayList<>();
		ArrayList<String> p2Names = new ArrayList<>();
		for(UUID uuid : p1Copy) 
			if(Bukkit.getPlayer(uuid) != null) p1Names.add(Bukkit.getPlayer(uuid).getName());
		for(UUID uuid : p2Copy) 
			if(Bukkit.getPlayer(uuid) != null) p2Names.add(Bukkit.getPlayer(uuid).getName());
		
		ArrayList<Integer> p1Kills = new ArrayList<>();
		ArrayList<Integer> p2Kills = new ArrayList<>();
		
		ArrayList<Float> health1 = new ArrayList<>();
		ArrayList<Float> health2 = new ArrayList<>();
		
		for(UUID uuid : p1Copy) {
			if(death.contains(uuid)) {
				health1.add(0.0f);
				continue;
			} else {
				if(Bukkit.getPlayer(uuid) != null) {
					health1.add((float) Bukkit.getPlayer(uuid).getHealth());
					continue;
				} else {
					health1.add(0.0f);
					continue;
				}
			}
			
		}
		
		for(UUID uuid : p2Copy) {
			if(death.contains(uuid)) {
				health2.add(0.0f);
				continue;
			} else {
				if(Bukkit.getPlayer(uuid) != null) {
					health2.add((float) Bukkit.getPlayer(uuid).getHealth());
					continue;
				} else {
					health2.add(0.0f);
					continue;
				}
			}
			
		}
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				String resolvedKitName;
				
				
				YamlConfiguration cfg = Main.ins.utils.getYaml("Layouts");
				
				String ymlItemID = cfg.getString("Layout." + getLayout() + ".ItemID");
				
				String name = "STONE";
				int subID = 0;
				
				if(ymlItemID.contains(":")) {
					if(ymlItemID.split(":").length >= 2) {
						name = ymlItemID.split(":")[0].toUpperCase();
						try {
							subID = Integer.parseInt(ymlItemID.split(":")[1]);
						} catch (NumberFormatException e) {}
						
						
					} else {
						name = ymlItemID.toUpperCase().replaceAll(":", "");
					}
				} else {
					name = ymlItemID.toUpperCase();
				}
				
				
				
				if(kit != null && Database.getKitType(kit) == 1) {
					resolvedKitName = Database.getUserName(UUID.fromString(kit));
				} else resolvedKitName = kit;
				
				
				
				
				
				Database.addFight(
						System.currentTimeMillis(), UUID.randomUUID(), 
						p1Copy, p2Copy, p1Names, p2Names, 
						p1Winner, wP1Color.toString(), wP2Color.toString(), 
						health1, health2,//TODO HERZEN ERGÄNZEN 
						getName(), getLayout(), wRanked, wTournament, 
						"", name, subID, resolvedKitName + ":" + subID, 
						wDuration, p1Kills, p2Kills);
					
					
			}
		}.runTaskAsynchronously(Main.ins);
		//End of FightsDB
		
		//Remove all Entities in the Arena
		removeArenaEntitys();
		
		
		boolean isT = tournament;
		boolean moreFights = false;
		
		
		ArrayList<UUID> spectatorList = (ArrayList<UUID>) getSpectators().clone();
		

		//Reset all Values
		String nextArena = null;
		String kit = null;
		int subID = 0;
		
		//For Best of
		if(!isT) {
			if (p1Winner) 
				setBestOfWinsP1(getBestOfWinsP1() + 1);
			 else 
				setBestOfWinsP2(getBestOfWinsP2() + 1);
			
			if (getBestOf() == BestOfsPrefs.BESTOF3 || getBestOf() == BestOfsPrefs.BESTOF5) {
				
				if (
						
					(((getBestOfWinsP1() < 2) && (getBestOfWinsP2() < 2)) && 
					getBestOf() == BestOfsPrefs.BESTOF3) || 
					((getBestOfWinsP1() < 3) && (getBestOfWinsP2() < 3) && 
					getBestOf() == BestOfsPrefs.BESTOF5)) {
					
					moreFights = true;
					nextArena = QueueManager.getRndmArena(getLayout());
					
					if (nextArena == null) 
						if(!isWorldArena()) nextArena = getName(); //TODO HIER WURDE KEINE FREIE NEUE ARENA GEFUNDEN!
					
						
					OneVsOneArena nextPlayedArena = Main.ins.getOneVsOneArena(nextArena);
					nextPlayedArena.setUsed(true);
					kit = getKit();
					subID = getSubID();
				}
			} 
			
		}
		
		
		for (UUID pl1 : p1) {
			if (p1Winner) {

				/* Stats bei Gewinn von pl1 */
				
				boolean tight = false;
				boolean eZ    =	false;
				
				
				if(Bukkit.getPlayer(pl1) != null) {
					tight = (Bukkit.getPlayer(pl1).getHealth() <= 1);
					eZ = (Bukkit.getPlayer(pl1).getHealth() >= 20);
				}					
				
				
					updateStats(pl1, true, tight, eZ);
				if (ranked) Main.ins.database.updateRankPoints(pl1, 1);
				/*-----*/

				if (!moreFights) {
					if(!isT) {
					 new BukkitRunnable() {

						@Override
						public void run() {
							resetPlayer(pl1, true);

						}
					 }.runTaskLater(Main.ins, Main.ins.timeInArenaWinner);
					
					
						if (getBestOf() == BestOfsPrefs.BESTOF3) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl1), 10, 20 * 3, 10, "§cBest of 3",
									"§aDu hast gewonnen!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl1), JSound.LEVEL_UP, 10, 1);
						} else if (getBestOf() == BestOfsPrefs.BESTOF5) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl1), 10, 20 * 3, 10, "§cBest of 5",
									"§aDu hast gewonnen!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl1), JSound.LEVEL_UP, 10, 1);
						}
					}
				}

				if (Bukkit.getPlayer(pl1) != null) {

					String p2Name = "";
					if (p2.size() > 0 && Bukkit.getPlayer(p2.get(0)) != null) {
						p2Name = Bukkit.getPlayer(p2.get(0)).getDisplayName();
					} else {
						p2Name = Bukkit.getOfflinePlayer(p2.get(0)).getName();
					}

					
					if(p1.size() == 1) {
						if(p2.size() == 1) 
							Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§aDu §7hast den Kampf gegen §c" + p2Name + " §agewonnen!");
						 else {
							 if(p2.size() > 1 && Bukkit.getPlayer(p2.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj() != null) 
								Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§aDu §7hast den Kampf gegen §c" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj().getTeamName(false) + " §agewonnen!");
							 else Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§aDu §7hast den Kampf gegen §c" + p2Name + " §agewonnen!");
						 }
							
					} else {
						if(p2.size() == 1)
							Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§aIhr §7habt den Kampf gegen §c" + p2Name + " §agewonnen!");
						 else 
							 if(p2.size() > 1 && Bukkit.getPlayer(p2.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj() != null) 
									Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§aIhr §7habt den Kampf gegen §c" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj().getTeamName(false) + " §agewonnen!");
								 else Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§aIhr §7habt den Kampf gegen §c" + p2Name + " §agewonnen!");
					}
					
				}
			} else {

				/* Stats bei Verlust von pl1 */
				updateStats(pl1, false, false, false);
				
				
				
				if (ranked)
					Main.ins.database.updateRankPoints(pl1, -1);
				/*-----*/

				if (!moreFights) {
					if(!isT) {
					 new BukkitRunnable() {

						@Override
						public void run() {
							resetPlayer(pl1, true);

						}
					 }.runTaskLater(Main.ins, Main.ins.timeInArenaLoser);
					
					
						if (getBestOf() == BestOfsPrefs.BESTOF3) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl1), 10, 20 * 3, 10, "§cBest of 3",
									"§cDu hast verloren!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl1), JSound.BLAZE_DEATH, 10, 2);
						} else if (getBestOf() == BestOfsPrefs.BESTOF5) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl1), 10, 20 * 3, 10, "§cBest of 5",
									"§cDu hast verloren!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl1), JSound.BLAZE_DEATH, 10, 2);
						}
					}
					
				}

				if (Bukkit.getPlayer(pl1) != null) {

					String p2Name = "";
					if (p2.size() > 0 && Bukkit.getPlayer(p2.get(0)) != null) {
						p2Name = Bukkit.getPlayer(p2.get(0)).getDisplayName();
					} else {
						p2Name = Bukkit.getOfflinePlayer(p2.get(0)).getName();
					}
					
					if(p1.size() == 1) {
						if(p2.size() == 1) 
							Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§cDu §7hast den Kampf gegen §a" + p2Name + " §cverloren!");
						 else {
							 if(p2.size() > 1 && Bukkit.getPlayer(p2.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj() != null) 
								Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§cDu §7hast den Kampf gegen §a" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj().getTeamName(false) + " §cverloren!");
							 else Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§cDu §7hast den Kampf gegen §a" + p2Name + " §cverloren!");
						 }
							
					} else {
						if(p2.size() == 1)
							Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§cIhr §7habt den Kampf gegen §a" + p2Name + " §cverloren!");
						 else 
							 if(p2.size() > 1 && Bukkit.getPlayer(p2.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj() != null) 
									Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§cIhr §7habt den Kampf gegen §a" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p2.get(0))).getTeamObj().getTeamName(false) + " §cverloren!");
								 else Bukkit.getPlayer(pl1).sendMessage(Main.ins.prefixBlue + "§cIhr §7hast den Kampf gegen §a" + p2Name + " §cverloren!");
					}

					

				}
			}

		}

		for (UUID pl2 : p2) {
			if (!p1Winner) {

				boolean tight = false;
				boolean eZ    =	false;
				
				
				if(Bukkit.getPlayer(pl2) != null) {
					tight = (Bukkit.getPlayer(pl2).getHealth() <= 1);
					eZ = (Bukkit.getPlayer(pl2).getHealth() >= 20);
				}					
				
				
				updateStats(pl2, true, tight, eZ);
				
				
				if (ranked)
					Main.ins.database.updateRankPoints(pl2, 1);
				/*-----*/

				if (!moreFights) {
					if(!isT) {
					 new BukkitRunnable() {

						@Override
						public void run() {
							resetPlayer(pl2, true);

						}
					 }.runTaskLater(Main.ins, Main.ins.timeInArenaWinner);
					
					
						if (getBestOf() == BestOfsPrefs.BESTOF3) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl2), 10, 20 * 3, 10, "§cBest of 3",
									"§aDu hast gewonnen!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl2), JSound.LEVEL_UP, 10, 1);
						} else if (getBestOf() == BestOfsPrefs.BESTOF5) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl2), 10, 20 * 3, 10, "§cBest of 5",
									"§aDu hast gewonnen!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl2), JSound.LEVEL_UP, 10, 1);
						}
					}
					

				}

				if (Bukkit.getPlayer(pl2) != null) {

					String p1Name = "";
					if (p1.size() > 0 && Bukkit.getPlayer(p1.get(0)) != null) {
						p1Name = Bukkit.getPlayer(p1.get(0)).getDisplayName();
					} else {
						p1Name = Bukkit.getOfflinePlayer(p1.get(0)).getName();
					}

					
					if(p2.size() == 1) {
						if(p1.size() == 1) 
							Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§aDu §7hast den Kampf gegen §c" + p1Name + " §agewonnen!");
						 else {
							 if(p1.size() > 1 && Bukkit.getPlayer(p1.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj() != null) 
								Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§aDu §7hast den Kampf gegen §c" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj().getTeamName(false) + " §agewonnen!");
							 else Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§aDu §7hast den Kampf gegen §c" + p1Name + " §agewonnen!");
						 }
							
					} else {
						if(p1.size() == 1)
							Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§aIhr §7habt den Kampf gegen §c" + p1Name + " §agewonnen!");
						 else 
							 if(p1.size() > 1 && Bukkit.getPlayer(p1.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj() != null) 
									Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§aIhr §7habt den Kampf gegen §c" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj().getTeamName(false) + " §agewonnen!");
								 else Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§aIhr §7habt den Kampf gegen §c" + p1Name + " §agewonnen!");
					}
					
				}
			} else {
				/* Stats bei Verlust von pl2 */
				updateStats(pl2, false, false, false);
				
				
//				
				if (ranked)
					Main.ins.database.updateRankPoints(pl2, -1);
				/*-----*/
				if (!moreFights) {
					if(!isT) {
					 new BukkitRunnable() {

						@Override
						public void run() {
							resetPlayer(pl2, true);

						}
					 }.runTaskLater(Main.ins, Main.ins.timeInArenaLoser);
					
						if (getBestOf() == BestOfsPrefs.BESTOF3) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl2), 10, 20 * 3, 10, "§cBest of 3",
									"§cDu hast verloren!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl2), JSound.BLAZE_DEATH, 10, 2);
						} else if (getBestOf() == BestOfsPrefs.BESTOF5) {
							Main.ins.utils.sendTitle(Bukkit.getPlayer(pl2), 10, 20 * 3, 10, "§cBest of 5",
									"§cDu hast verloren!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(pl2), JSound.BLAZE_DEATH, 10, 2);
						}
					}
					
				}

				if (Bukkit.getPlayer(pl2) != null) {

					String p1Name = "";
					if (p1.size() > 0 && Bukkit.getPlayer(p1.get(0)) != null) {
						p1Name = Bukkit.getPlayer(p1.get(0)).getDisplayName();
					} else {
						p1Name = Bukkit.getOfflinePlayer(p1.get(0)).getName();
					}

					if(p2.size() == 1) {
						if(p1.size() == 1) 
							Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§cDu §7hast den Kampf gegen §a" + p1Name + " §cverloren!");
						 else {
							 if(p1.size() > 1 && Bukkit.getPlayer(p1.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj() != null) 
								Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§cDu §7hast den Kampf gegen §a" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj().getTeamName(false) + " §cverloren!");
							 else Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§cDu §7hast den Kampf gegen §a" + p1Name + " §cverloren!");
						 }
							
					} else {
						if(p1.size() == 1)
							Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§cIhr §7habt den Kampf gegen §a" + p1Name + " §cverloren!");
						 else 
							 if(p1.size() > 1 && Bukkit.getPlayer(p1.get(0)) != null && Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj() != null) 
									Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§cIhr §7habt den Kampf gegen §a" + Main.ins.getOneVsOnePlayer(Bukkit.getPlayer(p1.get(0))).getTeamObj().getTeamName(false) + " §cverloren!");
								 else Bukkit.getPlayer(pl2).sendMessage(Main.ins.prefixBlue + "§cIhr §7hast den Kampf gegen §a" + p1Name + " §cverloren!");
					}
					
				}
			}

		}

		new BukkitRunnable() {

			@Override
			public void run() {
				
				for (UUID specs : spectatorList) {
					
					if (Bukkit.getPlayer(specs) != null) {
						
						if(Main.ins.getOneVsOnePlayer(specs).getPlayertournament() != null) {
							continue;
						}
						
						SpectateMgr.stopSpectate(Bukkit.getPlayer(specs), false, death.contains(specs));

						String p1Name = "";
						if (p1.size() > 0 && Bukkit.getPlayer(p1.get(0)) != null) {
							p1Name = Bukkit.getPlayer(p1.get(0)).getDisplayName();
						} else {
							p1Name = Bukkit.getOfflinePlayer(p1.get(0)).getName();
						}

						String p2Name = "";
						if (p2.size() > 0 && Bukkit.getPlayer(p2.get(0)) != null) {
							p2Name = Bukkit.getPlayer(p2.get(0)).getDisplayName();
						} else {
							p2Name = Bukkit.getOfflinePlayer(p2.get(0)).getName();
						}

						if (p1Winner) {
							Bukkit.getPlayer(specs).sendMessage(Main.ins.prefixBlue + "§a" + p1Name
									+ " §7hat den Kampf gegen §c" + p2Name + " §agewonnen!");
						} else {
							Bukkit.getPlayer(specs).sendMessage(Main.ins.prefixBlue + "§a" + p2Name
									+ " §7hat den Kampf gegen §c" + p1Name + " §agewonnen!");
						}

						SpectateMgr.stopSpectate(Bukkit.getPlayer(specs), false, death.contains(specs));

					}
				}
				for (OneVsOnePlayer players : Main.ins.getOneVsOnePlayersCopy().values()) {
					for (OneVsOnePlayer players1 : Main.ins.getOneVsOnePlayersCopy().values()) {
						if (Bukkit.getPlayer(players.getUUID()) != null && Bukkit.getPlayer(players1.getUUID()) != null) {
							
							if(players.getpState() != PlayerState.SPECTATOR)
								Bukkit.getPlayer(players1.getUUID()).showPlayer(Bukkit.getPlayer(players.getUUID()));
							
							if(players1.getpState() != PlayerState.SPECTATOR) 
								Bukkit.getPlayer(players.getUUID()).showPlayer(Bukkit.getPlayer(players1.getUUID()));
							
							
							
						}
					}
				}

			}
		}.runTask(Main.ins);

//		if (Main.ins.spawnFirework && !moreFights && !isT) {
//			Location edge1 = getBuildCorner1();
//			Location edge2 = getBuildCorner2();
//
//			Random r = new Random();
//
//			int a = r.nextInt(3) + 3;
//
//			int maxX = Math.max(edge1.getBlockX(), edge2.getBlockX());
//			int maxZ = Math.max(edge1.getBlockZ(), edge2.getBlockZ());
//			int minX = Math.min(edge1.getBlockX(), edge2.getBlockX());
//			int minZ = Math.min(edge1.getBlockZ(), edge2.getBlockZ());
//
//			int maxY = Math.max(edge1.getBlockY(), edge2.getBlockY());
//			int minY = Math.min(edge1.getBlockY(), edge2.getBlockY());
//
//			ArrayList<Location> locations = new ArrayList<>();
//
//			new BukkitRunnable() {
//				
//				@Override
//				public void run() {
//					for (int x = minX; x < maxX; x++) {
//						for (int z = minZ; z < maxZ; z++) {
//
//							int foundY = minY;
//							int checkY = minY;
//							while (checkY < maxY) {
//								if (new Location(edge1.getWorld(), x, foundY, z).getBlock().getType() != Material.AIR) {
//									foundY = checkY + 1;
//								}
//								checkY++;
//
//							}
//							if (foundY > maxY)
//								foundY = maxY - 2;
//
//							locations.add(new Location(edge1.getWorld(), x, foundY, z));
//						}
//					}
//				}
//			}.runTask(Main.ins);
//			
//
//			while (a > 0) {
//				a--;
//
//				new BukkitRunnable() {
//
//					@Override
//					public void run() {
//						
//						if(locations == null || locations.size() <= 0) return;
//						
//						Location loc = locations.get(r.nextInt(locations.size()));
//
//						Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
//						FireworkMeta fwm = fw.getFireworkMeta();
//
//						Builder builder = FireworkEffect.builder();
//
//						switch (r.nextInt(3) + 1) {
//						case 1:
//							builder.with(Type.BALL);
//							break;
//						case 2:
//							builder.with(Type.BALL_LARGE);
//							break;
//						case 3:
//							builder.with(Type.BURST);
//							break;
//						case 4:
//							builder.with(Type.STAR);
//							break;
//						default:
//							builder.with(Type.BALL);
//							break;
//						}
//
//						builder.withColor(getColor(r.nextInt(12) + 1));
//						builder.withFade(getColor(r.nextInt(12) + 1));
//
//						if (r.nextBoolean())
//							builder.withFlicker();
//						if (r.nextBoolean())
//							builder.withTrail();
//
//						fwm.addEffect(builder.build());
//						fwm.setPower(0);
//						fw.setFireworkMeta(fwm);
//
//					}
//				}.runTaskLater(Main.ins, r.nextInt(20 * 3 - 5));
//
//			}
//		}

		
		if(!isT) {
			final String nArena = nextArena;
			
			
			if(nArena == null && moreFights) {
				new BukkitRunnable() {
					
					@Override
					public void run() {
						for(UUID uuid : p1) {
							if(Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixRed + "§cDa keine freie Arena gefunden werden konnte, muss das Bestof abgebrochen werden!");
								Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixRed + "§cSollte dies öfters vorkommen, kontaktiere bitte einen Admin!");
							}
							resetPlayer(uuid, true);
						}
						for(UUID uuid : p2) {
							if(Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixRed + "§cDa keine freie Arena gefunden werden konnte, muss das Bestof abgebrochen werden!");
								Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixRed + "§cSollte dies öfters vorkommen, kontaktiere bitte einen Admin!");
							}
							resetPlayer(uuid, true);
						}
						reload();
					}
				}.runTask(Main.ins);
				
				return;
			}
			
			final String nKit = kit;
			final int nSubID = subID;
			final BestOfsPrefs nBOfType = getBestOf();
			final int pointsp1 = getBestOfWinsP1();
			final int pointsp2 = getBestOfWinsP2();

			final ArrayList<UUID> poses1 = (ArrayList<UUID>) p1.clone();
			final ArrayList<UUID> poses2 = (ArrayList<UUID>) p2.clone();

			if (!moreFights) {
				new BukkitRunnable() {

					@Override
					public void run() {
						removeArenaEntitys();
						reload();

					}
				}.runTaskLater(Main.ins, Main.ins.timeInArenaWinner);
			} else {

				int timer = 1;
				if (worldArena) {
					timer = 35;
					setLocked(true);
				}
				new BukkitRunnable() {

					@Override
					public void run() {
						removeArenaEntitys();
						reload();
						if (nArena.equalsIgnoreCase(getName()))
							setUsed(true);
					}
				}.runTaskLater(Main.ins, timer);
			}

			if (moreFights) {
				new BukkitRunnable() {

					@Override
					public void run() {

						boolean allOfflinep1 = true;
						boolean allOfflinep2 = true;

						for (UUID p1 : poses1) {
							if (Bukkit.getPlayer(p1) != null)
								allOfflinep1 = false;
							resetPlayer(p1, false);
						}
						for (UUID p2 : poses2) {
							if (Bukkit.getPlayer(p2) != null)
								allOfflinep2 = false;
							resetPlayer(p2, false);
						}

						if (allOfflinep1) {
							Main.ins.getOneVsOneArena(nArena).reload();
							for (UUID p2 : poses2) {
								if (Bukkit.getPlayer(p2) != null) {
									if (bestOf == BestOfsPrefs.BESTOF3) {
										Main.ins.utils.sendTitle(Bukkit.getPlayer(p2), 10, 20 * 3, 10, "§cBest of 3",
												"§cDu hast verloren!");
										Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(p2), JSound.BLAZE_DEATH,
												10, 2);
									} else if (bestOf == BestOfsPrefs.BESTOF5) {
										Main.ins.utils.sendTitle(Bukkit.getPlayer(p2), 10, 20 * 3, 10, "§cBest of 5",
												"§cDu hast verloren!");
										Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(p2), JSound.BLAZE_DEATH,
												10, 2);
									}
								}

								resetPlayer(p2, true);
							}
							return;
						}

						if (allOfflinep2) {
							Main.ins.getOneVsOneArena(nArena).reload();
							for (UUID p1 : poses1) {
								if (Bukkit.getPlayer(p1) != null) {
									if (bestOf == BestOfsPrefs.BESTOF3) {
										Main.ins.utils.sendTitle(Bukkit.getPlayer(p1), 10, 20 * 3, 10, "§cBest of 3",
												"§cDu hast verloren!");
										Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(p1), JSound.BLAZE_DEATH,
												10, 2);
									} else if (bestOf == BestOfsPrefs.BESTOF5) {
										Main.ins.utils.sendTitle(Bukkit.getPlayer(p1), 10, 20 * 3, 10, "§cBest of 5",
												"§cDu hast verloren!");
										Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(p1), JSound.BLAZE_DEATH,
												10, 2);
									}
								}

								resetPlayer(p1, true);
							}
							return;
						}

						OneVsOneArena nextPlayedArena = Main.ins.getOneVsOneArena(nArena);

						nextPlayedArena.setBestOf(nBOfType);
						nextPlayedArena.setBestOfWinsP1(pointsp1);
						nextPlayedArena.setBestOfWinsP2(pointsp2);

						ArrayList<Player> playersP1 = new ArrayList<>();
						ArrayList<Player> playersP2 = new ArrayList<>();
						for (UUID uuids : p1)
							if (Bukkit.getPlayer(uuids) != null)
								playersP1.add(Bukkit.getPlayer(uuids));

						for (UUID uuids : p2)
							if (Bukkit.getPlayer(uuids) != null)
								playersP2.add(Bukkit.getPlayer(uuids));

						nextPlayedArena.join(playersP1, playersP2, nKit, nSubID);

					}
				}.runTaskLater(Main.ins, 20);
			}
		}
		
		if(isT) {
			if(Main.ins.tournaments.get(tournamentID) != null)  {
			
				new BukkitRunnable() {
					
					@Override
					public void run() {
						Main.ins.tournaments.get(tournamentID).endFight(getName(), p1, p2, p1Winner, groupID1, groupID2);
						
						
					}
				}.runTaskLater(Main.ins, 3);
			} else {
				//TODO Spieler in Lobby teleportieren, da Turnier kaputt!
			}
		}

	}

//	private Color getColor(int i) {
//		if (i == 1)
//			return Color.AQUA;
//		if (i == 2)
//			return Color.BLUE;
//		if (i == 3)
//			return Color.YELLOW;
//		if (i == 4)
//			return Color.GREEN;
//		if (i == 5)
//			return Color.LIME;
//		if (i == 6)
//			return Color.MAROON;
//		if (i == 7)
//			return Color.NAVY;
//		if (i == 8)
//			return Color.OLIVE;
//		if (i == 9)
//			return Color.ORANGE;
//		if (i == 10)
//			return Color.PURPLE;
//		if (i == 11)
//			return Color.RED;
//		if (i == 12)
//			return Color.TEAL;
//
//		return Color.GRAY;
//	}

	public void resetPlayer(UUID player, boolean sendToLobby) {

		if (Bukkit.getPlayer(player) != null) {
			Player p = Bukkit.getPlayer(player);
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setMaxHealth(20);
			p.setHealth(p.getMaxHealth());
			p.setFoodLevel(20);
			p.setGameMode(GameMode.SURVIVAL);
			p.setAllowFlight(false);
			p.setFlying(false);
			p.setLevel(0);
			p.setExp(0);
			p.setMaximumNoDamageTicks(10);
			p.setFireTicks(0);

			p.setVelocity(new Vector(0, 0, 0));

			// Reset potion efects
			for (PotionEffect ee : p.getActivePotionEffects()) {
				p.addPotionEffect(new PotionEffect(ee.getType(), 0, 0), true);
			}

			OneVsOnePlayer oPlayer = Main.ins.getOneVsOnePlayer(player);
			oPlayer.setDoubleJumpUsed(false);

			if (sendToLobby) {
				Main.ins.utils.tpToLobby(p);
				
				Main.ins.utils.giveLobbyItems(p);

				if (oPlayer.wasInQueue && Main.ins.database.isQueuePrefEnabled(player, 0) == 1) {
					Main.ins.utils.sendTitle(p, 10, 20 * 5, 10, "§cSneake,",
							"§7wenn du nicht in die Warteschlange willst.");
					new BukkitRunnable() {

						@Override
						public void run() {
							if (oPlayer.isWasInQueue()) {
								if (Main.ins.getOneVsOnePlayer(player).getpState() == PlayerState.INLOBBY
										|| Main.ins.getOneVsOnePlayer(player).getpState() == PlayerState.INKITEDIT) {
									oPlayer.setWasInQueue(false);
									oPlayer.setInQueue(true);

									p.sendMessage(Main.ins.prefixBlue + "§7Du bist nun in der §6Warteschlange!");
									Main.ins.utils.getSoundMgr().playSound(p, JSound.ORB_PICKUP, 1f, 1f);
								}

							}

						}
					}.runTaskLater(Main.ins, 20 * 5 + 2);
				} else {
					oPlayer.setWasInQueue(false);
				}

				oPlayer.setArena(null);
				oPlayer.setpState(PlayerState.INLOBBY);
			}

		}

	}

	public void stopScheduler(boolean buildtask) {
		if (mainTask != null) mainTask.cancel();
			
		mainTask = null;

		if (buildtask) {
			if (builderTask != null)
				builderTask.cancel();
			builderTask = null;
		}

	}

	public void reload() {
		stopScheduler(true);
		resetData();
	}

	
	public void finishWorldReset() {
		reloadLocations();
		ready = true;
		ended = false;
		setLocked(false);
	}
	
	public void resetArena() {
		String layout = getLayout();
		this.ready = false;
		
		if(isWorldArena()) {
			setLocked(true);
			WorldResetMgr.startReset(getArenaWorldName(), "1vs1Worlds/Presets/" + layout, this);
			
		} else {
			if(layout != null) {
				BlockMapReset reset = new BlockMapReset(
						Main.ins.utils.getPos1Layout(layout).clone(),
						Main.ins.utils.getPos2Layout(layout).clone(), 
						resetLoc.clone()
						, name, 1000, false);
				this.reset = reset;
				
				reset.copy();
			} else {
				System.err.println("[1vs1] Layout für Arena " + name + " nicht gefunden!");
			}
			if(resetLoc != null) {
				
			} else {
				System.err.println("[1vs1] Resetloc für Arena " + name + " nicht gefunden!");
				
			}
			
			
			
		}
			
		
		
		
		
		
		stopScheduler(false);
		this.started = false;
		this.startCountdown = 3;
		this.bestOf = BestOfsPrefs.BESTOF1;
		this.bestOfWinsP1 = 0;
		this.bestOfWinsP2 = 0;
		this.timer = 0;
		this.ranked = false;
		this.kitName = "-";
		resetSpectators();
		clearDeath();
	}

	public void resetData() {
		pos1.clear();
		pos2.clear();

		locPos1 = null;
		locPos2 = null;

		corner1 = null;
		corner2 = null;

		buildCorner1 = null;
		buildCorner2 = null;

		startCountdown = 3;
		endMatchTimer = 300;

		started = false;
		ended = false;
		used = false;

		tournament = false;

		settings.clear();

		layout = "";

		middleLoc = null;
		resetLoc = null;

		reset = null;

		kit = "";
		subID = 0;

		reloadData();
		resetArena();
	}

	public void reloadData() {
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
		layout = cfg.getString("Arena." + name + ".Layout");
		
		worldArena = cfg.getBoolean("Arena." + name + ".WorldArena");
		reloadLocations();
		
	}

	@SuppressWarnings("static-access")
	public void reloadLocations() {
		
		if(isWorldArena()) {
			setLocked(true);
			YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(new File("1vs1Worlds/Presets/" + layout + "/data.yml"));
			
			double pos1X = cfg.getDouble("Data.Pos1.X");
			int pos1Y = cfg.getInt("Data.Pos1.Y");
			double pos1Z = cfg.getDouble("Data.Pos1.Z");
			float pos1Yaw = (float) cfg.getDouble("Data.Pos1.Yaw");
			float pos1Pitch = (float) cfg.getDouble("Data.Pos1.Pitch");
			
			double pos2X = cfg.getDouble("Data.Pos2.X");
			int pos2Y = cfg.getInt("Data.Pos2.Y");
			double pos2Z = cfg.getDouble("Data.Pos2.Z");
			float pos2Yaw = (float) cfg.getDouble("Data.Pos2.Yaw");
			float pos2Pitch = (float) cfg.getDouble("Data.Pos2.Pitch");
			
			double middleX = cfg.getDouble("Data.Middle.X");
			int middleY = cfg.getInt("Data.Middle.Y");
			double middleZ = cfg.getDouble("Data.Middle.Z");
			float middleYaw = (float) cfg.getDouble("Data.Middle.Yaw");
			float middlePitch = (float) cfg.getDouble("Data.Middle.Pitch");
			
			int buildCorner1X = cfg.getInt("Data.BuildCorner1.X"); 
			int buildCorner1Y = cfg.getInt("Data.BuildCorner1.Y"); 
			int buildCorner1Z = cfg.getInt("Data.BuildCorner1.Z"); 
			
			int buildCorner2X = cfg.getInt("Data.BuildCorner2.X"); 
			int buildCorner2Y = cfg.getInt("Data.BuildCorner2.Y"); 
			int buildCorner2Z = cfg.getInt("Data.BuildCorner2.Z"); 
			
			int corner1X = cfg.getInt("Data.corner1.X"); 
			int corner1Y = cfg.getInt("Data.corner1.Y"); 
			int corner1Z = cfg.getInt("Data.corner1.Z"); 
			
			int corner2X = cfg.getInt("Data.corner2.X"); 
			int corner2Y = cfg.getInt("Data.corner2.Y"); 
			int corner2Z = cfg.getInt("Data.corner2.Z"); 
			
			this.arenaWorldName = "1vs1Worlds/Arenas/" + name;
			
			locPos1 = new Location(Bukkit.getWorld(arenaWorldName), pos1X, pos1Y, pos1Z, pos1Yaw, pos1Pitch);
			
			locPos2 = new Location(Bukkit.getWorld(arenaWorldName), pos2X, pos2Y, pos2Z, pos2Yaw, pos2Pitch);
			
			middleLoc = new Location(Bukkit.getWorld(arenaWorldName), middleX, middleY, middleZ, middleYaw, middlePitch);
			
			buildCorner1 = new Location(Bukkit.getWorld(arenaWorldName), buildCorner1X, buildCorner1Y, buildCorner1Z);
			buildCorner2 = new Location(Bukkit.getWorld(arenaWorldName), buildCorner2X, buildCorner2Y, buildCorner2Z);
			
			corner1 = new Location(Bukkit.getWorld(arenaWorldName), corner1X, corner1Y, corner1Z);
			corner2 = new Location(Bukkit.getWorld(arenaWorldName), corner2X, corner2Y, corner2Z);
			
			
			
			return;
		}
		
		YamlConfiguration cfg = Main.ins.utils.getYaml("Arenas");
		
		
		int pos1X = cfg.getInt("Arena." + name + ".Pos1X");
		int pos1Y = cfg.getInt("Arena." + name + ".Pos1Y");
		int pos1Z = cfg.getInt("Arena." + name + ".Pos1Z");

		float pos1Yaw = (float) cfg.getDouble("Arena." + name + ".Pos1Yaw");
		float pos1Pitch = (float) cfg.getDouble("Arena." + name + ".Pos1Pitch");

		String world = cfg.getString("Arena." + name + ".Pos1World");

		int pos2X = cfg.getInt("Arena." + name + ".Pos2X");
		int pos2Y = cfg.getInt("Arena." + name + ".Pos2Y");
		int pos2Z = cfg.getInt("Arena." + name + ".Pos2Z");

		float pos2Yaw = (float) cfg.getDouble("Arena." + name + ".Pos2Yaw");
		float pos2Pitch = (float) cfg.getDouble("Arena." + name + ".Pos2Pitch");

		String world2 = cfg.getString("Arena." + name + ".Pos2World");

		int middleX = cfg.getInt("Arena." + name + ".MiddleX");
		int middleY = cfg.getInt("Arena." + name + ".MiddleY");
		int middleZ = cfg.getInt("Arena." + name + ".MiddleZ");

		String middleWorld = cfg.getString("Arena." + name + ".MiddleWorld");

		int resetX = cfg.getInt("Arena." + name + ".ResetX");
		int resetY = cfg.getInt("Arena." + name + ".ResetY");
		int resetZ = cfg.getInt("Arena." + name + ".ResetZ");

		String resetWorld = cfg.getString("Arena." + name + ".ResetWorld");

		if (world != null && Bukkit.getWorld(world) != null)
			locPos1 = new Location(Bukkit.getWorld(world), (double) pos1X + (double) 0.5, pos1Y,
					(double) pos1Z + (double) 0.5, pos1Yaw, pos1Pitch);
		if (world2 != null && Bukkit.getWorld(world2) != null)
			locPos2 = new Location(Bukkit.getWorld(world2), (double) pos2X + (double) 0.5, pos2Y,
					(double) pos2Z + (double) 0.5, pos2Yaw, pos2Pitch);
		if (middleWorld != null && Bukkit.getWorld(middleWorld) != null)
			middleLoc = new Location(Bukkit.getWorld(middleWorld), middleX, middleY, middleZ);
		if (resetWorld != null && Bukkit.getWorld(resetWorld) != null)
			resetLoc = new Location(Bukkit.getWorld(resetWorld), resetX, resetY, resetZ);

	}

	public void init() {
		ok = true;
		
		reloadData();
	}

	public String getLayout() {
		return layout;
	}

	/**
	 * @return the middleLoc
	 */
	public Location getMiddleLoc() {
		return middleLoc;
	}

	/**
	 * @param middleLoc
	 *            the middleLoc to set
	 */
	public void setMiddleLoc(Location middleLoc) {
		this.middleLoc = middleLoc;
	}

	/**
	 * @return the resetLoc
	 */
	public Location getResetLoc() {
		return resetLoc;
	}

	/**
	 * @param resetLoc
	 *            the resetLoc to set
	 */
	public void setResetLoc(Location resetLoc) {
		this.resetLoc = resetLoc;
	}

	/**
	 * @return the reset
	 */
	public BlockMapReset getReset() {
		return reset;
	}

	/**
	 * @param reset
	 *            the reset to set
	 */
	public void setReset(BlockMapReset reset) {
		this.reset = reset;
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @param ready
	 *            the ready to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * @return the pos1
	 */
	public ArrayList<UUID> getPos1() {
		return pos1;
	}

	/**
	 * @param pos1
	 *            the pos1 to set
	 */
	public void setPos1(ArrayList<UUID> pos1) {
		this.pos1 = pos1;
	}

	/**
	 * @return the pos2
	 */
	public ArrayList<UUID> getPos2() {
		return pos2;
	}

	/**
	 * @param pos2
	 *            the pos2 to set
	 */
	public void setPos2(ArrayList<UUID> pos2) {
		this.pos2 = pos2;
	}

	/**
	 * @return the locPos1
	 */
	public Location getLocPos1() {
		return locPos1;
	}

	/**
	 * @param locPos1
	 *            the locPos1 to set
	 */
	public void setLocPos1(Location locPos1) {
		this.locPos1 = locPos1;
	}

	/**
	 * @return the locPos2
	 */
	public Location getLocPos2() {
		return locPos2;
	}

	/**
	 * @param locPos2
	 *            the locPos2 to set
	 */
	public void setLocPos2(Location locPos2) {
		this.locPos2 = locPos2;
	}

	/**
	 * @return the corner1
	 */
	public Location getCorner1() {
		return corner1;
	}

	/**
	 * @param corner1
	 *            the corner1 to set
	 */
	public void setCorner1(Location corner1) {
		this.corner1 = corner1;
	}

	/**
	 * @return the corner2
	 */
	public Location getCorner2() {
		return corner2;
	}

	/**
	 * @param corner2
	 *            the corner2 to set
	 */
	public void setCorner2(Location corner2) {
		this.corner2 = corner2;
	}

	/**
	 * @return the buildCorner1
	 */
	public Location getBuildCorner1() {
		return buildCorner1;
	}

	/**
	 * @param buildCorner1
	 *            the buildCorner1 to set
	 */
	public void setBuildCorner1(Location buildCorner1) {
		this.buildCorner1 = buildCorner1;
	}

	/**
	 * @return the buildCorner2
	 */
	public Location getBuildCorner2() {
		return buildCorner2;
	}

	/**
	 * @param buildCorner2
	 *            the buildCorner2 to set
	 */
	public void setBuildCorner2(Location buildCorner2) {
		this.buildCorner2 = buildCorner2;
	}

	/**
	 * @return the startCountdown
	 */
	public int getStartCountdown() {
		return startCountdown;
	}

	/**
	 * @param startCountdown
	 *            the startCountdown to set
	 */
	public void setStartCountdown(int startCountdown) {
		this.startCountdown = startCountdown;
	}

	/**
	 * @return the endMatchTimer
	 */
	public int getEndMatchTimer() {
		return endMatchTimer;
	}

	/**
	 * @param endMatchTimer
	 *            the endMatchTimer to set
	 */
	public void setEndMatchTimer(int endMatchTimer) {
		this.endMatchTimer = endMatchTimer;
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * @param started
	 *            the started to set
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * @return the ended
	 */
	public boolean isEnded() {
		return ended;
	}

	/**
	 * @param ended
	 *            the ended to set
	 */
	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	/**
	 * @return the tournament
	 */
	public boolean isTournament() {
		return tournament;
	}

	/**
	 * @param tournament
	 *            the tournament to set
	 */
	public void setTournament(boolean tournament) {
		this.tournament = tournament;
	}

	/**
	 * @return the settings
	 */
	public ArrayList<KitPrefs> getSettings() {
		return settings;
	}

	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(ArrayList<KitPrefs> settings) {
		this.settings = settings;
	}

	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isUsed() {
		return used;
	}

	/**
	 * @return the kit
	 */
	public String getKit() {
		return kit;
	}

	/**
	 * @param kit
	 *            the kit to set
	 */
	public void setKit(String kit) {
		this.kit = kit;
	}

	/**
	 * @return the subID
	 */
	public int getSubID() {
		return subID;
	}

	/**
	 * @param subID
	 *            the subID to set
	 */
	public void setSubID(int subID) {
		this.subID = subID;
	}

	/**
	 * @return the ranked
	 */
	public boolean isRanked() {
		return ranked;
	}

	/**
	 * @param ranked
	 *            the ranked to set
	 */
	public void setRanked(boolean ranked) {
		this.ranked = ranked;
	}

	/**
	 * @return the bestOf
	 */
	public BestOfsPrefs getBestOf() {
		return bestOf;
	}

	/**
	 * @param bestOf
	 *            the bestOf to set
	 */
	public void setBestOf(BestOfsPrefs bestOf) {
		this.bestOf = bestOf;
	}

	/**
	 * @return the bestOfWinsP1
	 */
	public int getBestOfWinsP1() {
		return bestOfWinsP1;
	}

	/**
	 * @param bestOfWinsP1
	 *            the bestOfWinsP1 to set
	 */
	public void setBestOfWinsP1(int bestOfWinsP1) {
		this.bestOfWinsP1 = bestOfWinsP1;
	}

	/**
	 * @return the bestOfWinsP2
	 */
	public int getBestOfWinsP2() {
		return bestOfWinsP2;
	}

	/**
	 * @param bestOfWinsP2
	 *            the bestOfWinsP2 to set
	 */
	public void setBestOfWinsP2(int bestOfWinsP2) {
		this.bestOfWinsP2 = bestOfWinsP2;
	}

	public void addSpectator(UUID uuid) {
		if (!spectators.contains(uuid))
			spectators.add(uuid);
	}

	public boolean isSpectator(UUID uuid) {
		return spectators.contains(uuid);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<UUID> getSpectators() {
		return (ArrayList<UUID>) spectators.clone();
	}

	public void resetSpectators() {
		spectators.clear();
	}

	public void removeSpectator(UUID uuid) {
		spectators.remove(uuid);
	}

	public String getFormatedFightTime() {
		int time = timer;

		int mins = 0;
		int secs = 0;

		while (time > 59) {
			mins++;
			time -= 60;
		}

		secs = time;

		if (secs < 10)
			return mins + ":0" + secs;

		return mins + ":" + secs;

	}

	public int getFightTimeSeconds() {
		return timer;
	}

	public String getKitName() {
		return kitName;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<UUID> getDeathList() {
		return (ArrayList<UUID>) this.death.clone();
	}

	public void addDeath(UUID uuid) {
		if (!this.death.contains(uuid))
			this.death.add(uuid);
	}

	public void removeDeath(UUID uuid) {
		if (this.death.contains(uuid))
			this.death.remove(uuid);
	}

	public boolean isDeath(UUID uuid) {
		return this.death.contains(uuid);
	}

	public void clearDeath() {
		this.death.clear();
	}

	public void setMaxFightTime(int time) {
		this.maxFightTime = time;
	}

	public int getMaxFightTime() {
		return this.maxFightTime;
	}

	public boolean isWorldArena() {
		return this.worldArena;
	}
	
	public String getArenaWorldName() {
		return this.arenaWorldName;
	}
	
	public boolean isLocked() {
		return this.locked;
	}
	
	public void setLocked(boolean lock) {
		this.locked = lock;
	}
	
	 public boolean hasErrors() {
		 
		 if(locked) {
			 System.out.println("" + getName() + " ErrorType = 1");
			 return true;
		 }
		 if(!ok) {
			 System.out.println("" + getName() + " ErrorType = 2");
			 return true;
		 }
		 if(name == null) {
			 System.out.println("" + getName() + " ErrorType = 3");
			 return true;
		 }
		 if(locPos1 == null || locPos1.getWorld() == null) {
			if(locPos1 == null) System.out.println("" + getName() + " ErrorType = 4.1");
			 if(locPos1.getWorld() == null) System.out.println("" + getName() + " ErrorType = 4.2");
			 return true;
		 }
		 if(locPos2 == null || locPos2.getWorld() == null) {
			 if(locPos2 == null) System.out.println("" + getName() + " ErrorType = 5.1");
			 if(locPos2.getWorld() == null) System.out.println("" + getName() + " ErrorType = 5.2");
			 return true;
		 }
		 if(middleLoc == null || middleLoc.getWorld() == null) {
			 if(middleLoc == null) System.out.println("" + getName() + " ErrorType = 6.1");
			 if(middleLoc.getWorld() == null) System.out.println("" + getName() + " ErrorType = 6.2");
			 return true;
		 }
		 if(corner1 == null || corner2 == null) {
			 
			 if(corner1 == null) System.out.println("" + getName() + " ErrorType = 7.1");
			 if(corner2 == null) System.out.println("" + getName() + " ErrorType = 7.2");
			 return true;
		 }
		 if(corner1.getWorld() == null || corner2.getWorld() == null) {
			 if(corner1.getWorld() == null) System.out.println("" + getName() + " ErrorType = 8.1");
			 if(corner2.getWorld() == null) System.out.println("" + getName() + " ErrorType = 8.2");
			 return true;
		 }
		 if(buildCorner1 == null || buildCorner2 == null) {
			 if(buildCorner1 == null) System.out.println("" + getName() + " ErrorType = 9.1");
			 if(buildCorner2 == null) System.out.println("" + getName() + " ErrorType = 9.2");
			 return true;
		 }
		 if(buildCorner1.getWorld() == null || buildCorner2.getWorld() == null) {
			 if(buildCorner1.getWorld() == null) System.out.println("" + getName() + " ErrorType = 10.1");
			 if(buildCorner2.getWorld() == null) System.out.println("" + getName() + " ErrorType = 10.2");
			 return true;
		 }
		 
		 
		 return false;
	 }
	 
	 public void updateStats(UUID uuid, boolean won, boolean tight, boolean eZ) {
//		 if(true) return;
		 new BukkitRunnable() {
			
			@Override
			public void run() {
				
				float winsAll = Main.ins.database.getStats(uuid, 1, 2);
				float wins30d = Main.ins.database.getStats(uuid, 2, 2);	
				float wins24h = Main.ins.database.getStats(uuid, 3, 2);
				
				float lostAll = Main.ins.database.getStats(uuid, 1, 3);
				float lost30d = Main.ins.database.getStats(uuid, 2, 3);
				float lost24h = Main.ins.database.getStats(uuid, 3, 3);			
				
				
				Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 1, 1)+1), 1, 1);
				Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 2, 1)+1), 2, 1);
				Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 3, 1)+1), 3, 1);
				
				
				
				if(won) {
					winsAll++;
					wins30d++;
					wins24h++;
					Main.ins.database.setStats(uuid, winsAll, 1, 2);
					Main.ins.database.setStats(uuid, wins30d, 2, 2);
					Main.ins.database.setStats(uuid, wins24h, 3, 2);
					
					
					
					if(tight) {
						Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 1,  5)+1), 1, 5);
						Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 2,  5)+1), 2, 5);
						Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 3,  5)+1), 3, 5);
					}
					
					if(eZ) {
						Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 1,  4)+1), 1, 4);
						Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 2,  4)+1), 2, 4);
						Main.ins.database.setStats(uuid, (Main.ins.database.getStatsAsInt(uuid, 3,  4)+1), 3, 4);
					}
					
				} else {
					lostAll++;
					lost30d++;
					lost24h++;
					Main.ins.database.setStats(uuid, lostAll, 1, 3);
					Main.ins.database.setStats(uuid, lost30d, 2, 3);
					Main.ins.database.setStats(uuid, lost24h, 3, 3);
				}
				
				if(lostAll > 0) 
					Main.ins.database.setStats(uuid, (winsAll/lostAll), 1, 8);
				 else 
					Main.ins.database.setStats(uuid, (winsAll), 1, 8);
				
				if(lost30d > 0) 
					Main.ins.database.setStats(uuid, (wins30d/lost30d), 2, 8);
				 else 
					Main.ins.database.setStats(uuid, (wins30d), 2, 8);
				
				if(lost24h > 0) 
					Main.ins.database.setStats(uuid, (wins24h/lost24h), 3, 8);
				 else 
					Main.ins.database.setStats(uuid, (wins24h), 3, 8);
			
				
				
			}
		}.runTaskLaterAsynchronously(Main.ins,20*3);
	 }
	 
	 public void updateKD(UUID uuid, boolean kill) {
		 
		 new BukkitRunnable() {
				
				@Override
				public void run() {
					
					
					float killsAll = Main.ins.database.getStats(uuid, 1, 6);
					float kills30d = Main.ins.database.getStats(uuid, 2, 6);
					float kills24h = Main.ins.database.getStats(uuid, 3, 6);
					
					float deathAll = Main.ins.database.getStats(uuid, 1, 7);
					float death30d = Main.ins.database.getStats(uuid, 2, 7);
					float death24h = Main.ins.database.getStats(uuid, 3, 7);
					
					
					if(kill) {
						killsAll++;
						kills30d++;
						kills24h++;
						Main.ins.database.setStats(uuid, killsAll, 1, 6);
						Main.ins.database.setStats(uuid, kills30d, 2, 6);
						Main.ins.database.setStats(uuid, kills24h, 3, 6);
					} else {
						deathAll++;
						death30d++;
						death24h++;
						Main.ins.database.setStats(uuid, deathAll, 1, 7);
						Main.ins.database.setStats(uuid, death30d, 2, 7);
						Main.ins.database.setStats(uuid, death24h, 3, 7);
					}
					
					
					
					if(deathAll > 0) 
						Main.ins.database.setStats(uuid, (killsAll/deathAll), 1, 9);
					 else 
						Main.ins.database.setStats(uuid, (killsAll), 1, 9);
					
					if(death30d > 0) 
						Main.ins.database.setStats(uuid, (kills30d/death30d), 2, 9);
					 else 
						Main.ins.database.setStats(uuid, (kills30d), 2, 9);
					
					if(death24h > 0) 
						Main.ins.database.setStats(uuid, (kills24h/death24h), 3, 9);
					 else 
						Main.ins.database.setStats(uuid, (kills24h), 3, 9);
				
					
					
					
				}
			}.runTaskLaterAsynchronously(Main.ins,20*3);
	 }
	
}
