/**
 * 
 */
package de.onevsone.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.onevsone.Main;
import de.onevsone.arenas.SpectateMgr;
import de.onevsone.enums.PlayerState;
import de.onevsone.enums.TournamentState;
import de.onevsone.methods.QueueManager;
import de.onevsone.methods.SoundMgr.JSound;



/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 13:08:35					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class OneVsOneTournament {

	private ArrayList<UUID> participants = new ArrayList<>();
	private HashMap<Integer, TournamentGroup> groups = new HashMap<>();
	private HashMap<UUID, Integer> groupPlayer = new HashMap<>();
	
	private TournamentState state = TournamentState.WAITING;
	
	private ArrayList<UUID> out = new ArrayList<>();
	private ArrayList<Integer> outGroup = new ArrayList<>();
	
	private int startPlayers = 0;
	
	private ArrayList<String> tArenas = new ArrayList<>();
	private HashMap<Integer, ArrayList<TournamentRoundStats>> stats = new HashMap<>();
	private int round = 0;
	
	/*All Settings*/
	 /*Overall settings*/
	  /*Basic*/
	  private UUID owner;
	  private UUID tournamentID;
	  private String kit;
	  private String password;
	  /* [Basic] */
	  
	  private boolean open = false;
	  private boolean started = false;
	  private int timer = -1;
	
	  /*0 = Round Qualification | 1 = Timed Qualification | 2 = K.O. */
	  private int selectedType = 0;
	  /*----------*/
	
	  /*Other Settings*/
	  private int maxTeamSize = 1;
	  private int maxPlayers = 0;
	  private int startTimerSecs = 3;
	  /*[Other Settings]*/
	  
	 /*Phases*/
	  private boolean timedQualification = false;
	  /*NormalQualification*/
	  private int maxFightTimeNormalQ = 300;
	  private int maxRoundsNormalQ = 3;
	  private int maxFightsNormalQ = 3;
	  /*[NormalQualification]*/
	 
	  /*TimedQualification*/
	  private int maxFightTimeTimedQ = 120;
	  private int maxTimeTimedQ = 300;
	  /*[TimedQualification]*/
	 
	  
	  /*K.O*/
	  private int maxFightTimeKO = 180;
	  private int maxFightsKO = 1;
	  /*[K.O]*/
	 /*[Phases]*/
	/*---------*/
	
	
	public OneVsOneTournament(UUID owner, UUID tournamentID, String password, String kit) {
		this.owner = owner;
		this.tournamentID = tournamentID;
		
		this.kit = kit;
		this.password = password;
	}
	
	public void addParticipant(Player player, ArrayList<UUID> team) {
		participants.add(player.getUniqueId());
		for(UUID uuid : team) participants.add(uuid);
		
		TournamentGroup group = new TournamentGroup(player.getUniqueId(), team, groups.size()+1);
		
		groups.put(group.getNumber(), group);
		groupPlayer.put(player.getUniqueId(), group.getNumber());
		
		
		for(UUID uuid : team) {
			if(Bukkit.getPlayer(uuid) != null) {
				Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§6Dein Team ist einem Turnier beigetreten!");
			}
		}
		
		player.sendMessage(Main.ins.prefixTournament + "§6Dein Team ist einem Turnier beigetreten!");
		
		for(UUID uuid : participants) {
			if(Bukkit.getPlayer(uuid) != null) {
				Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§6" + player.getDisplayName() + " §7ist dem Turnier beigetreten!");
			}
		}
		
		for(UUID uuid : group.getAll()) {
			Main.ins.getOneVsOnePlayer(uuid).setPlayertournament(getTournamentID());
		}
		
	}
	
	public void startStartCounter() {
		if(this.state != TournamentState.WAITING) return; 
		timer = startTimerSecs;
		this.state = TournamentState.STARTING;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(started) {
					cancel();
					return;
				}
				
				if(timer%60 == 0 && timer > 60 && timer > 0) {
					for(UUID uuid : participants) 
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Das §6Turnier §7startet in §6" + timer/60 + " Minuten!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.CLICK, 1f, 0.1f);
						}
				} else if(timer%60 == 0 && timer == 60 && timer > 0) {
					for(UUID uuid : participants) 
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Das §6Turnier §7startet in §6einer Minute!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.CLICK, 1f, 0.1f);
						}
				} else if(timer%10 == 0 && timer < 60 && timer > 0) {
					for(UUID uuid : participants) 
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Das §6Turnier §7startet in §6" + timer + " Sekunden!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.CLICK, 1f, 0.1f);
						}
				} else if(timer <= 5 && timer != 1 && timer > 0) {
					for(UUID uuid : participants) 
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Das §6Turnier §7startet in §6" + timer + " Sekunden!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.CLICK, 1f, 0.1f);
						}
				} else if(timer == 1 && timer > 0) {
					for(UUID uuid : participants) 
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Das §6Turnier §7startet in §6einer Sekunde!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.CLICK, 1f, 0.1f);
						}
				} else if(timer <= 0) {
					cancel();
					for(UUID uuid : participants) 
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Das §6Turnier §7beginnt! §6Viel Glück!");
							Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.LEVEL_UP, 1f, 1f);
						}
					startTournament();
					return;
				}
				
				
				timer--;
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 20);
	}
	
	
	private void startTournament() {
		
		if(groups.size() <= 1 || groups.size() < Main.ins.minPlayersTournament) {
			for(UUID uuid : participants) {
				if(Bukkit.getPlayer(uuid) != null) {
					Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§cEs sind nicht genügend Spieler in dem Turnier! (Es werden mindestens " + Main.ins.minPlayersTournament + " Spieler benötigt!)");
					Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(uuid), JSound.ITEM_BREAK, 3, 0.1f);
				}
			}
			started = false;
			state = TournamentState.WAITING;
			return;
		}
		
		
		this.state = TournamentState.QUALIFICATIONPHASE;
		this.started = true;
		this.timer = getMaxTimeTimedQ();
		this.startPlayers = groups.size();
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				startNextRound();
			}
		}.runTask(Main.ins);
	}
	
	
	
	
	@SuppressWarnings({ "unchecked"})
	public void startNextRound() {
		
		HashMap<Integer, TournamentGroup> cGroups = (HashMap<Integer, TournamentGroup>) groups.clone();
		for(int id : outGroup) cGroups.remove(id);
		
		ArrayList<Integer> a = new ArrayList<>();
		for(int i : cGroups.keySet()) a.add(i); 
		
		
		
		if(getRemainingPlayers() <= 1) {
			sendMessageToAllPlayers(Main.ins.prefixTournament + "§6Das Turnier wurde beendet!");
			
			if(getRemainingPlayers() == 0) {
				sendMessageToAllPlayers(Main.ins.prefixTournament + "§cEs konnte kein Sieger festgestellt werden...");
				
				//Fehler => Turnier einfach löschen und alle Spieler in Lobby
			} else {
				sendMessageToAllPlayers(Main.ins.prefixTournament + "§aGewinner: §6" + cGroups.get(a.get(0)).getName());
				//Alles ok! => Gewinner senden und danach Turnier löschen
			}
			deleteTournament();
		 return;
		}
		
		this.round++;
		
		
		if(state == TournamentState.QUALIFICATIONPHASE) {
			if(!timedQualification && round > maxRoundsNormalQ) {
				sendMessageToAllPlayers(Main.ins.prefixTournament + "§7Die Quallifikation ist abgeschlossen!");
				
				int points = (maxRoundsNormalQ*maxFightsNormalQ)/2;
				
				for(TournamentGroup tGroups : groups.values()) {
					
					sendMessageToTGroup(tGroups.getNumber(), Main.ins.prefixTournament + "Ihr habt §6" + tGroups.qPoints + "/" + (maxRoundsNormalQ*maxFightsNormalQ) + " Kämpfe gewonnen");
					
					if(tGroups.qPoints < points) {
						out(tGroups.getNumber());
					} else {
						sendMessageToTGroup(tGroups.getNumber(), Main.ins.prefixTournament + "§aIhr seid weiter!");
					}
				}
				
				cGroups = (HashMap<Integer, TournamentGroup>) groups.clone();
				for(int id : outGroup) cGroups.remove(id);
				
				a = new ArrayList<>();
				for(int i : cGroups.keySet()) a.add(i); 
				
				
				state = TournamentState.KOPHASE;
			}
		}
		
		sendMessageToAllPlayers(Main.ins.prefixTournament + "§7Die §6" + round + ". Runde (" + state.getName() + ") §7beginnt!");
		sendMessageToAllPlayers(Main.ins.prefixTournament + "§7Es verbleiben §6" + getRemainingPlayers() + " Spieler.");
		
		
		Random r = new Random();
		
		
		
		//while(true) {
		while(!a.isEmpty()) {
			try {
				
				int id1 = r.nextInt(a.size());
				int pos1 = a.get(id1);
				a.remove(id1);
				
				if(a.isEmpty()) {
					TournamentGroup group = groups.get(pos1);
					for(UUID uuid : group.getAll()) {
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "Es konnte kein Gegner für dich gefunden werden.");
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§aDu wirst diese Runde Zuschauen!");
							
							new BukkitRunnable() {
								
								@Override
								public void run() {
									spectateRndmArena(uuid);
								}
							}.runTaskLater(Main.ins, 2);
							
						}
					}
					
					
					break;
				}
				
				int id2 = r.nextInt(a.size());
				int pos2 = a.get(id2);
				a.remove(id2);
				
				
				ArrayList<Player> pos1Players = new ArrayList<>();
				
				if(Bukkit.getPlayer(groups.get(pos1).getLeader()) != null) {
					pos1Players.add(Bukkit.getPlayer(groups.get(pos1).getLeader()));
					SpectateMgr.stopSpectate(Bukkit.getPlayer(groups.get(pos1).getLeader()), true, false);
					
				}
				for(UUID players : groups.get(pos1).getPlayers()) {
					if(Bukkit.getPlayer(players) != null) {
						pos1Players.add(Bukkit.getPlayer(players));
						
						SpectateMgr.stopSpectate(Bukkit.getPlayer(players), true, false);//TODO NEU!
					}
				}
				
				
				ArrayList<Player> pos2Players = new ArrayList<>();
				
				if(Bukkit.getPlayer(groups.get(pos2).getLeader()) != null) {
					pos2Players.add(Bukkit.getPlayer(groups.get(pos2).getLeader()));
					SpectateMgr.stopSpectate(Bukkit.getPlayer(groups.get(pos2).getLeader()), true, false);
					
				}
				for(UUID players : groups.get(pos2).getPlayers()) {
					if(Bukkit.getPlayer(players) != null) {
						pos2Players.add(Bukkit.getPlayer(players));
						
						SpectateMgr.stopSpectate(Bukkit.getPlayer(players), true, false);//TODO NEU!
					}
				}
				
				ArrayList<TournamentRoundStats> roundStats;
				
				if(stats.get(this.round) != null) roundStats = stats.get(this.round);
				 else roundStats = new ArrayList<>();
				
				roundStats.add(new TournamentRoundStats(pos1, pos2));
				roundStats.add(new TournamentRoundStats(pos2, pos1));
				
				stats.remove(this.round);
				
				stats.put(this.round, roundStats);
				
				String arena = QueueManager.getRndmArena(QueueManager.getRndmMap(pos1Players.get(0).getUniqueId(), pos2Players.get(0).getUniqueId(), false));
				
				
				
				Main.ins.getOneVsOneArena(arena).join(pos1Players, pos2Players, getKit(), 1, getTournamentID(), pos1, pos2);
				tArenas.add(arena);
				
				if(a.isEmpty()) break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		
		
		for(Integer group : outGroup) 
			for(UUID uuid : groups.get(group).getAll()) 
				if(Bukkit.getPlayer(uuid) != null) spectateRndmArena(uuid);
				
			
		
		
		
		
		
	}
	
	public void endFight(String arena, ArrayList<UUID> pos1, ArrayList<UUID> pos2, boolean pos1Winner, int groupID1, int groupID2) {
		if(tArenas.contains(arena)) tArenas.remove(arena);
		
		@SuppressWarnings("unchecked")
		ArrayList<UUID> specs = (ArrayList<UUID>) Main.ins.getOneVsOneArena(arena).getSpectators().clone();
		
		Main.ins.getOneVsOneArena(arena).resetData();
		
		int plays = 0;
		
		TournamentRoundStats stats = getStats(this.round, groupID1);
		if(stats != null) {
			if(pos1Winner) stats.setWins(stats.getWins()+1);
			if(!pos1Winner) stats.setLost(stats.getLost()+1);
			
			plays = stats.getWins();
		} else {
			plays = -1;
		}
		
		stats = getStats(this.round, groupID2);
		if(stats != null) {
			if(!pos1Winner) stats.setWins(stats.getWins()+1);
			if(pos1Winner) stats.setLost(stats.getLost()+1);
			if(plays != -1) plays += stats.getWins();
		} else {
			plays = -1;
		}
		
		if(plays == -1) {
			Bukkit.broadcastMessage("Ein Fehler ist aufgetreten!"); //TODO Verbessern!!!
			return;
		}
		
		int maxPlays = 3;
		
		if(state == TournamentState.QUALIFICATIONPHASE) {
//			Bukkit.broadcastMessage("§aTEST " + pos1Winner);
			if(pos1Winner) {
//				Bukkit.broadcastMessage("" + groupID1 + )
				groups.get(groupID1).setQPoints(groups.get(groupID1).getQPoints()+1);
			} else {
				groups.get(groupID2).setQPoints(groups.get(groupID2).getQPoints()+1);
			}
			
			
			if(isTimedQualification()) {
				maxPlays = 1;
			} else {
				maxPlays = getMaxFightsNormalQ();
			}
		} else if(state == TournamentState.KOPHASE) {
			maxPlays = getMaxFightsKO();
		}
		
		
		if(plays < maxPlays) {
				
				String nArena = QueueManager.getRndmArena(QueueManager.getRndmMap(pos1.get(0), pos2.get(0), false));
				
				if(nArena == null) nArena = arena;
				
				ArrayList<Player> pos1Players = new ArrayList<>();
				
				if(Bukkit.getPlayer(groups.get(groupID1).getLeader()) != null) {
					pos1Players.add(Bukkit.getPlayer(groups.get(groupID1).getLeader()));
				}
				for(UUID players : groups.get(groupID1).getPlayers()) {
					if(Bukkit.getPlayer(players) != null) {
						pos1Players.add(Bukkit.getPlayer(players));
					}
				}
				
				
				ArrayList<Player> pos2Players = new ArrayList<>();
				
				if(Bukkit.getPlayer(groups.get(groupID2).getLeader()) != null) {
					pos2Players.add(Bukkit.getPlayer(groups.get(groupID2).getLeader()));
				}
				for(UUID players : groups.get(groupID2).getPlayers()) {
					if(Bukkit.getPlayer(players) != null) {
						pos2Players.add(Bukkit.getPlayer(players));
					}
				}
				
				
				Main.ins.getOneVsOneArena(arena).join(pos1Players, pos2Players, getKit(), 1, getTournamentID(), groupID1, groupID2);
				tArenas.add(arena);
				
				
				
				for(UUID uuid : specs) {
					if(Bukkit.getPlayer(uuid) != null) {
						spectateRndmArena(uuid);
					}
				}
				
			} else {
				
				if(state == TournamentState.KOPHASE) {
					if(pos1Winner) {
						
						for(UUID uuid : groups.get(groupID2).getAll()) {
							if(Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§cDu bist ausgeschieden!");
							}
						}
						
						outGroup.add(groupID2);
					}
					else {
						
						for(UUID uuid : groups.get(groupID1).getAll()) {
							if(Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§cDu bist ausgeschieden!");
							}
						}
						
						outGroup.add(groupID1);
					}
				}
				
				
				if(tArenas.isEmpty()) {
					sendMessageToAllPlayers(Main.ins.prefixTournament + "§6Alle Kämpfe in dieser Runde wurden beendet!");
					
					//TODO Nachrichten hier bearbeiten!
					startNextRound();
				} else {
					
					for(UUID uuid : specs) {
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§aDa noch nicht alle Kämpfe beendet sind, wirst du eine andere Arena spectaten!");
							
							spectateRndmArena(uuid);
						}
					}
					for(UUID uuid : pos1) {
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§aDa noch nicht alle Kämpfe beendet sind, wirst du eine andere Arena spectaten!");
							
							spectateRndmArena(uuid);
						}
					}
					for(UUID uuid : pos2) {
						if(Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixTournament + "§aDa noch nicht alle Kämpfe beendet sind, wirst du eine andere Arena spectaten!");
							
							spectateRndmArena(uuid);//TODO Nachrichten hier bearbeiten!
						}
					}
				}
				
				
			}
	}
	
	
	
	
	public void setSelectedType(int type) {
		if(type < 0 || type > 2) return;
		selectedType = type;
	}
	
	public int getSelectedType() {
		return selectedType;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	public UUID getTournamentID() {
		return tournamentID;
	}

	public String getKit() {
		return kit;
	}

	public void setKit(String kit) {
		this.kit = kit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getMaxTeamSize() {
		return maxTeamSize;
	}
	
	public void setMaxTeamSize(int maxTeamSize) {
		this.maxTeamSize = maxTeamSize;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public int getStartTimerSecs() {
		return startTimerSecs;
	}
	
	public void setStartTimerSecs(int startTimerSecs) {
		this.startTimerSecs = startTimerSecs;
	}

	public boolean isTimedQualification() {
		return timedQualification;
	}

	public void setTimedQualification(boolean timedQualification) {
		this.timedQualification = timedQualification;
	}

	public int getMaxFightTimeKO() {
		return maxFightTimeKO;
	}

	public void setMaxFightTimeKO(int maxFightTimeKO) {
		this.maxFightTimeKO = maxFightTimeKO;
	}

	public int getMaxFightsKO() {
		return maxFightsKO;
	}

	public void setMaxFightsKO(int maxFightsKO) {
		this.maxFightsKO = maxFightsKO;
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

	public int getMaxFightTimeNormalQ() {
		return maxFightTimeNormalQ;
	}

	public void setMaxFightTimeNormalQ(int maxFightTimeNormalQ) {
		this.maxFightTimeNormalQ = maxFightTimeNormalQ;
	}

	public int getMaxRoundsNormalQ() {
		return maxRoundsNormalQ;
	}

	public void setMaxRoundsNormalQ(int maxRoundsNormalQ) {
		this.maxRoundsNormalQ = maxRoundsNormalQ;
	}

	public int getMaxFightsNormalQ() {
		return maxFightsNormalQ;
	}

	public void setMaxFightsNormalQ(int maxFightsNormalQ) {
		this.maxFightsNormalQ = maxFightsNormalQ;
	}

	public int getMaxFightTimeTimedQ() {
		return maxFightTimeTimedQ;
	}

	public void setMaxFightTimeTimedQ(int maxFightTimeTimedQ) {
		this.maxFightTimeTimedQ = maxFightTimeTimedQ;
	}

	public int getMaxTimeTimedQ() {
		return maxTimeTimedQ;
	}

	public void setMaxTimeTimedQ(int maxTimeTimedQ) {
		this.maxTimeTimedQ = maxTimeTimedQ;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public int getTimer() {
		return timer;
	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public ArrayList<UUID> getParticipants() {
		return participants;
	}
	
	public void setParticipants(ArrayList<UUID> participants) {
		this.participants = participants;
	}
	
	public ArrayList<UUID> getOut() {
		return out;
	}
	
	public void setOut(ArrayList<UUID> out) {
		this.out = out;
	}
	
	public HashMap<Integer, TournamentGroup> getGroups() {
		return groups;
	}
	
	public void setGroups(HashMap<Integer, TournamentGroup> groups) {
		this.groups = groups;
	}
	
	public HashMap<UUID, Integer> getGroupPlayer() {
		return groupPlayer;
	}
	
	public void setGroupPlayer(HashMap<UUID, Integer> groupPlayer) {
		this.groupPlayer = groupPlayer;
	}

	public TournamentState getState() {
		return state;
	}

	public void setState(TournamentState state) {
		this.state = state;
	}
	
	public int getStartPlayersAmount() {
		return startPlayers;
	}
	
	public class TournamentRoundStats {
		
		private int groupID;
		private int wins = 0;
		private int lost = 0;
		private int enemieGroupID;
		
		public TournamentRoundStats(int groupID, int enemieGroupID) {
			this.groupID = groupID;
		}
		
		public int getGroupID() {
			return this.groupID;
		}
		
		public int getEnemieGroupID() {
			return this.enemieGroupID;
		}
		
		public int getWins() {
			return this.wins;
		}
		
		public int getLost() {
			return this.lost;
		}
		
		public void setWins(int wins) {
			this.wins = wins;
		}
		
		public void setLost(int lost) {
			this.lost = lost;
		}
	}
	
	public TournamentRoundStats getStats(int round, int group) {
		if(stats.containsKey(round)) {
			ArrayList<TournamentRoundStats> a = stats.get(round);
			for(TournamentRoundStats s : a) 
				if(s.getGroupID() == group) return s;
		}
		return null;
	}
	
	public void sendMessageToAllPlayers(String msg) {
		
		@SuppressWarnings("unchecked")
		HashMap<Integer, TournamentGroup> cGroups = (HashMap<Integer, TournamentGroup>) groups.clone();
		
		
		for(TournamentGroup group : cGroups.values()) {
			for(UUID uuid : group.getAll()) {
				if(Bukkit.getPlayer(uuid) != null) {	
					Bukkit.getPlayer(uuid).sendMessage(msg);
				}
			}
		}
		
	}
	
	public int getRemainingPlayers() {
		return groups.size()-outGroup.size();
	}
	
	public void spectateRndmArena(UUID uuid) {
		if(uuid != null) {
			Random r = new Random();//TODO RANDOM!
			String arena = tArenas.get(r.nextInt(tArenas.size()));	
			SpectateMgr.stopSpectate(Bukkit.getPlayer(uuid), true, false);
			SpectateMgr.spectate(Bukkit.getPlayer(uuid), arena, true, false);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void deleteTournament() {
		for(TournamentGroup group : ((HashMap<Integer, TournamentGroup>)groups.clone()).values()) {
			for(UUID uuid : group.getAll()) {
				if(Bukkit.getPlayer(uuid) != null) {
					
						Player p = Bukkit.getPlayer(uuid);
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

						p.setVelocity(new Vector(0, 0, 0));

						// Reset potion efects
						for (PotionEffect ee : p.getActivePotionEffects()) {
							p.addPotionEffect(new PotionEffect(ee.getType(), 0, 0), true);
						}

						OneVsOnePlayer oPlayer = Main.ins.getOneVsOnePlayer(p);
						oPlayer.setDoubleJumpUsed(false);

					
						
						Main.ins.utils.tpToLobby(p);
						
						Main.ins.utils.giveLobbyItems(p);

						oPlayer.setArena(null);
						oPlayer.setpState(PlayerState.INLOBBY);
						oPlayer.setPlayertournament(null);
				}
			}
		}
		Main.ins.tournaments.remove(getTournamentID());
	}
	
	public void sendMessageToTGroup(int group, String msg) {
		if(!groups.containsKey(group)) return;
		for(UUID uuid : groups.get(group).getAll()) {
			if(Bukkit.getPlayer(uuid) != null) {
				Bukkit.getPlayer(uuid).sendMessage(msg);
			}
		}
	}
	
	public void out(int groupID) {
		outGroup.add(groupID);
		sendMessageToTGroup(groupID, Main.ins.prefixTournament + "§cDas hat leider nicht gereicht! Du bist ausgeschieden!");
	}
	
	
}
