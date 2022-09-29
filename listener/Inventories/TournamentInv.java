/**
 * 
 */
package de.onevsone.listener.Inventories;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.onevsone.Main;
import de.onevsone.methods.SoundMgr.JSound;
import de.onevsone.methods.Utils;
import de.onevsone.objects.OneVsOnePlayer;
import de.onevsone.objects.OneVsOneTournament;

/**
 * ###################################################### # @author JHammer17 #
 * # Erstellt am 31.03.2018 11:36:00 # # # # Alle Ihhalte dieser Klasse dürfen #
 * # frei verwendet oder verbreitet werden. # # Es wird keine Zustimmung von
 * JHammer17 benötigt. # # #
 * ######################################################
 */
public class TournamentInv implements Listener {

	public static void openTournamentMainInv(Player p, UUID uuid) {

		OneVsOneTournament tournament = Main.ins.tournaments.get(uuid);
		if (tournament == null) {
			tournament = new OneVsOneTournament(p.getUniqueId(), uuid, "",
					Main.ins.getOneVsOnePlayer(p).getKitLoaded());
			Main.ins.tournaments.put(uuid, tournament);
			p.sendMessage(Main.ins.prefixRed + "§cEin Fehler ist aufgetreten! Das Turnier wurde zurückgesetzt!");
		}

		Utils util = Main.ins.utils;

		Inventory inv = Bukkit.createInventory(null, 9 * 6, "Turnier Einstellungen§a");

		ItemStack empty = util.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§a", null);
		ItemStack active = util.createItem(Material.INK_SACK, 10, 1, "§a§lAktiviert", null);
		ItemStack inactive = util.createItem(Material.INK_SACK, 8, 1, "§c§lDeaktiviert", null);

		ItemStack qualificationRoundPhase = util.createItem(Material.EMERALD, 0, 1, "§6Runden Quallifikation", null);
		ItemStack qualificationTimedPhase = util.createItem(Material.WATCH, 0, 1, "§6Zeitliche Quallifikation", null);

		ItemStack koPhase = util.createItem(Material.EXPLOSIVE_MINECART, 0, 1, "§6K.O.-Phase", null);

		ItemStack password = util.createItem(Material.TRIPWIRE_HOOK, 0, 1, "§6Passwort: §7-", null);
		ItemStack kit = util.createItem(Material.SIGN, 0, 1, "§6Kit: §7" + p.getName(), null);

		ItemStack maxTeamSize = util.createItem(Material.INK_SACK, 3, 1,
				"§6Maximale Teamgröße: §7" + tournament.getMaxTeamSize(), null);

		String max = "Unendlich";
		if (tournament.getMaxPlayers() != 0)
			max = "" + tournament.getMaxPlayers();
		ItemStack maxPlayers = util.createItem(Material.SKULL_ITEM, 3, 1, "§6Maximale Spieler: §7" + max, null);

		ItemStack startTime = util.createItem(Material.WATCH, 0, 1,
				"§6Startzeit: §7" + tournament.getFormattedTime(tournament.getStartTimerSecs()), null);

		ItemStack add1 = util.createItem(Material.STONE_BUTTON, 0, 1, "§6+1", "\n§8Shift-Klick: +5");
		ItemStack remove1 = util.createItem(Material.STONE_BUTTON, 0, 1, "§6-1", "\n§8Shift-Klick: -5");

		ItemStack add10Secs = util.createItem(Material.STONE_BUTTON, 0, 1, "§6+0:10", "\n§8Shift-Klick: +0:30");
		ItemStack remove10Secs = util.createItem(Material.STONE_BUTTON, 0, 1, "§6-0:10", "\n§8Shift-Klick: -0:30");

		ItemStack startCountdown = util.createItem(Material.STAINED_GLASS_PANE, 5, 1, "§a§lTurnier starten", null);
		ItemStack openTournament = util.createItem(Material.STAINED_GLASS_PANE, 13, 1, "§2Turnier veröffentlichen", null);
		
		
		ItemStack qualificationLength = util.createItem(Material.WATCH, 0, 1,
				"§6Quallifikationsdauer: §7" + tournament.getFormattedTime(tournament.getMaxTimeTimedQ()), null);
		ItemStack maxRoundsQualification = util.createItem(Material.SAPLING, 0, 1,
				"§6Runden: §7" + tournament.getMaxRoundsNormalQ(), null);
		ItemStack maxFightsPerRound;
		ItemStack maxFightTime;

		/* Upper left corner */
		inv.setItem(0, qualificationRoundPhase);
		inv.setItem(3, qualificationTimedPhase);
		inv.setItem(9, koPhase);

		if (tournament.getSelectedType() != 0)
			inv.setItem(1, inactive);
		else
			inv.setItem(1, active);
		if (tournament.getSelectedType() != 1)
			inv.setItem(2, inactive);
		else
			inv.setItem(2, active);
		if (tournament.getSelectedType() != 2)
			inv.setItem(10, inactive);
		else
			inv.setItem(10, active);

		/* [Upper left corner] */

		/* Kit & Password */
		inv.setItem(5, password);
		inv.setItem(23, kit);
		/* [Kit & Password] */

		/* Upper left Corner */
		inv.setItem(6, add1);
		inv.setItem(7, add1);
		inv.setItem(8, add10Secs);

		inv.setItem(15, maxTeamSize);
		inv.setItem(16, maxPlayers);
		inv.setItem(17, startTime);

		inv.setItem(24, remove1);
		inv.setItem(25, remove1);
		inv.setItem(26, remove10Secs);
		/* [Upper left Corner] */

		/* Right Corner */
		
		if(tournament.isOpen()) {
			inv.setItem(42, startCountdown);
			inv.setItem(43, startCountdown);
			inv.setItem(44, startCountdown);
			inv.setItem(51, startCountdown);
			inv.setItem(52, startCountdown);
			inv.setItem(53, startCountdown);
		} else {
			inv.setItem(42, openTournament);
			inv.setItem(43, openTournament);
			inv.setItem(44, openTournament);
			inv.setItem(51, openTournament);
			inv.setItem(52, openTournament);
			inv.setItem(53, openTournament);
		}
		

		/* [Right Corner] */

		/* Variables Corner */

		if (tournament.getSelectedType() == 0) {

			maxFightTime = util.createItem(Material.WATCH, 0, 1,
					"§6Maximale Kampfzeit: §7" + tournament.getFormattedTime(tournament.getMaxFightTimeNormalQ()),
					null);
			maxFightsPerRound = util.createItem(Material.NETHER_STAR, 0, 1,
					"§6Kämpfe pro Runde: §7" + tournament.getMaxFightsNormalQ(), null);

			inv.setItem(28, add10Secs);
			inv.setItem(29, add1);

			inv.setItem(37, maxFightTime);
			inv.setItem(38, maxRoundsQualification);
			inv.setItem(39, maxFightsPerRound);

			inv.setItem(46, remove10Secs);
			inv.setItem(47, remove1);
			
			if(!tournament.isTimedQualification()) inv.setItem(27, active);
			else inv.setItem(27, inactive);
			
		} else if (tournament.getSelectedType() == 1) {

			maxFightTime = util.createItem(Material.WATCH, 0, 1,
					"§6Maximale Kampfzeit: §7" + tournament.getFormattedTime(tournament.getMaxFightTimeTimedQ()), null);

			inv.setItem(28, add10Secs);
			inv.setItem(30, add10Secs);

			inv.setItem(37, qualificationLength);
			inv.setItem(39, maxFightTime);

			inv.setItem(46, remove10Secs);
			inv.setItem(48, remove10Secs);
			
			if(tournament.isTimedQualification()) inv.setItem(27, active);
			else inv.setItem(27, inactive);
		} else if (tournament.getSelectedType() == 2) {
			maxFightTime = util.createItem(Material.WATCH, 0, 1,
					"§6Maximale Kampfzeit: §7" + tournament.getFormattedTime(tournament.getMaxFightTimeKO()), null);
			maxFightsPerRound = util.createItem(Material.NETHER_STAR, 0, 1,
					"§6Kämpfe pro Runde: §7" + tournament.getMaxFightsKO(), null);

			inv.setItem(28, add10Secs);

			inv.setItem(37, maxFightTime);
			inv.setItem(39, maxFightsPerRound);

			inv.setItem(46, remove10Secs);
		}

		/* [Variables Corner] */

		/* Empty */
		inv.setItem(4, empty);
		inv.setItem(11, empty);
		inv.setItem(12, empty);
		inv.setItem(13, empty);
		inv.setItem(14, empty);
		inv.setItem(18, empty);
		inv.setItem(19, empty);
		inv.setItem(20, empty);
		inv.setItem(21, empty);
		inv.setItem(22, empty);
		inv.setItem(32, empty);
		inv.setItem(33, empty);
		inv.setItem(34, empty);
		inv.setItem(35, empty);
		inv.setItem(41, empty);
		inv.setItem(50, empty);
		/* [Empty] */
		p.openInventory(inv);
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getInventory() != null && e.getWhoClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getWhoClicked();
			if (Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
				if (e.getInventory().getName().equalsIgnoreCase("Turnier Einstellungen§a")) {
					e.setCancelled(true);
					if (e.getClickedInventory() != null
							&& e.getClickedInventory().getName().equalsIgnoreCase("Turnier Einstellungen§a")) {

						int slot = e.getSlot();

						OneVsOneTournament tournament = Main.ins.tournaments
								.get(Main.ins.getOneVsOnePlayer(p).getPlayertournament());

						if (tournament == null) {
							p.sendMessage(Main.ins.prefixRed + "§cEin Fehler ist aufgetreten!");
							openTournamentMainInv(p, UUID.randomUUID());
							return;
						}

						if (slot == 0 || slot == 1) {
							tournament.setSelectedType(0);
						} else if (slot == 2 || slot == 3) {
							tournament.setSelectedType(1);
						} else if (slot == 9 || slot == 10) {
							tournament.setSelectedType(2);
						}

						if (slot == 6) {
							if (e.isShiftClick()) {
								tournament.setMaxTeamSize(tournament.getMaxTeamSize() + 5);
							} else {
								tournament.setMaxTeamSize(tournament.getMaxTeamSize() + 1);
							}
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
						} else if (slot == 24) {
							if (e.isShiftClick()) {
								if (tournament.getMaxTeamSize() - 5 >= 1)
									tournament.setMaxTeamSize(tournament.getMaxTeamSize() - 5);
								else
									tournament.setMaxTeamSize(1);

							} else {
								if (tournament.getMaxTeamSize() - 1 >= 1)
									tournament.setMaxTeamSize(tournament.getMaxTeamSize() - 1);
								else
									tournament.setMaxTeamSize(1);

							}
							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
						}

						if (slot == 7) {
							if (e.isShiftClick())
								tournament.setMaxPlayers(tournament.getMaxPlayers() + 5);
							else
								tournament.setMaxPlayers(tournament.getMaxPlayers() + 1);

							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
						} else if (slot == 25) {
							if (e.isShiftClick())
								if (tournament.getMaxPlayers() - 5 >= 0)
									tournament.setMaxPlayers(tournament.getMaxPlayers() - 5);
								else
									tournament.setMaxPlayers(0);
							else if (tournament.getMaxPlayers() - 1 >= 0)
								tournament.setMaxPlayers(tournament.getMaxPlayers() - 1);
							else
								tournament.setMaxPlayers(0);

							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
						}

						if (slot == 8) {
							if (e.isShiftClick())
								tournament.setStartTimerSecs(tournament.getStartTimerSecs() + 30);
							else
								tournament.setStartTimerSecs(tournament.getStartTimerSecs() + 10);

							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
						} else if (slot == 26) {
							if (e.isShiftClick())
								if (tournament.getStartTimerSecs() - 5 >= 0)
									tournament.setStartTimerSecs(tournament.getStartTimerSecs() - 30);
								else
									tournament.setStartTimerSecs(0);
							else if (tournament.getStartTimerSecs() - 1 >= 0)
								tournament.setStartTimerSecs(tournament.getStartTimerSecs() - 10);
							else
								tournament.setStartTimerSecs(0);

							Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
						}
						
						if (tournament.getSelectedType() == 0) {
							if (slot == 28) {
								if (e.isShiftClick())
									tournament.setMaxFightTimeNormalQ(tournament.getMaxFightTimeNormalQ() + 30);
								else
									tournament.setMaxFightTimeNormalQ(tournament.getMaxFightTimeNormalQ() + 10);
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							} else if (slot == 46) {
								if (e.isShiftClick()) {
									if (tournament.getMaxFightTimeNormalQ() - 30 >= 60)
										tournament.setMaxFightTimeNormalQ(tournament.getMaxFightTimeNormalQ() - 30);
									else
										tournament.setMaxFightsNormalQ(60);

								} else {
									if (tournament.getMaxFightTimeNormalQ() - 10 >= 60)
										tournament.setMaxFightTimeNormalQ(tournament.getMaxFightTimeNormalQ() - 10);
									else
										tournament.setMaxFightTimeNormalQ(60);

								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}
							if (slot == 29) {
								if (e.isShiftClick())
									tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() + 5);
								else
									tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() + 1);
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							} else if (slot == 47) {
								if (e.isShiftClick()) {
									if (tournament.getMaxRoundsNormalQ() - 5 >= 1)
										tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() - 5);
									else
										tournament.setMaxRoundsNormalQ(1);
								} else {
									if (tournament.getMaxRoundsNormalQ() - 1 >= 1)
										tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() - 1);
									else
										tournament.setMaxRoundsNormalQ(1);

								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}
							if (slot == 39) {
								if (tournament.getMaxFightsNormalQ() == 1) {
									tournament.setMaxFightsNormalQ(3);
								} else if (tournament.getMaxFightsNormalQ() == 3) {
									tournament.setMaxFightsNormalQ(5);
								} else {
									tournament.setMaxFightsNormalQ(1);
								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}
							
							if(slot == 27) {
								if(tournament.isTimedQualification()) 
									tournament.setTimedQualification(false);
								 else tournament.setTimedQualification(true);
							} 
							
						} else if (tournament.getSelectedType() == 1) {

							if (slot == 28) {
								if (e.isShiftClick())
									tournament.setMaxTimeTimedQ(tournament.getMaxTimeTimedQ() + 30);
								else
									tournament.setMaxTimeTimedQ(tournament.getMaxTimeTimedQ() + 10);
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							} else if (slot == 46) {
								if (e.isShiftClick()) {
									if (tournament.getMaxTimeTimedQ() - 30 >= 180)
										tournament.setMaxTimeTimedQ(tournament.getMaxTimeTimedQ() - 30);
									else
										tournament.setMaxTimeTimedQ(180);

								} else {
									if (tournament.getMaxTimeTimedQ() - 10 >= 180)
										tournament.setMaxTimeTimedQ(tournament.getMaxTimeTimedQ() - 10);
									else
										tournament.setMaxTimeTimedQ(180);

								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}

							if (slot == 30) {
								if (e.isShiftClick())
									tournament.setMaxFightTimeTimedQ(tournament.getMaxFightTimeTimedQ() + 30);
								else
									tournament.setMaxFightTimeTimedQ(tournament.getMaxFightTimeTimedQ() + 10);
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							} else if (slot == 48) {
								if (e.isShiftClick()) {
									if (tournament.getMaxFightTimeTimedQ() - 30 >= 180)
										tournament.setMaxFightTimeTimedQ(tournament.getMaxFightTimeTimedQ() - 30);
									else
										tournament.setMaxFightTimeTimedQ(180);

								} else {
									if (tournament.getMaxFightTimeTimedQ() - 10 >= 180)
										tournament.setMaxFightTimeTimedQ(tournament.getMaxFightTimeTimedQ() - 10);
									else
										tournament.setMaxFightTimeTimedQ(180);

								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}

							if(slot == 27) {
								if(tournament.isTimedQualification()) 
									tournament.setTimedQualification(false);
								 else tournament.setTimedQualification(true);
							} 
							
						} else if (tournament.getSelectedType() == 2) {
							if (slot == 28) {
								if (e.isShiftClick())
									tournament.setMaxFightTimeKO(tournament.getMaxFightTimeKO() + 30);
								else
									tournament.setMaxFightTimeKO(tournament.getMaxFightTimeKO() + 10);
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							} else if (slot == 46) {
								if (e.isShiftClick()) {
									if (tournament.getMaxFightTimeKO() - 30 >= 60)
										tournament.setMaxFightTimeKO(tournament.getMaxFightTimeKO() - 30);
									else
										tournament.setMaxFightTimeKO(60);

								} else {
									if (tournament.getMaxFightTimeKO() - 10 >= 60)
										tournament.setMaxFightTimeKO(tournament.getMaxFightTimeKO() - 10);
									else
										tournament.setMaxFightTimeKO(60);

								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}
							if (slot == 29) {
								if (e.isShiftClick())
									tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() + 5);
								else
									tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() + 1);
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							} else if (slot == 47) {
								if (e.isShiftClick()) {
									if (tournament.getMaxRoundsNormalQ() - 5 >= 1)
										tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() - 5);
									else
										tournament.setMaxRoundsNormalQ(1);
								} else {
									if (tournament.getMaxRoundsNormalQ() - 1 >= 1)
										tournament.setMaxRoundsNormalQ(tournament.getMaxRoundsNormalQ() - 1);
									else
										tournament.setMaxRoundsNormalQ(1);

								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}
							if (slot == 39) {
								if (tournament.getMaxFightsKO() == 1) {
									tournament.setMaxFightsKO(3);
								} else if (tournament.getMaxFightsKO() == 3) {
									tournament.setMaxFightsKO(5);
								} else {
									tournament.setMaxFightsKO(1);
								}
								Main.ins.utils.getSoundMgr().playSound(p, JSound.CLICK, 2.0F, 2F);
							}

						}
						
						
						
						if(slot == 42 || slot == 43 || slot == 44 ||
						   slot == 51 || slot == 52 || slot == 53) {
							
							if(!tournament.isOpen()) {
								for(OneVsOnePlayer players : Main.ins.getOneVsOnePlayersCopy().values()) {
									if(Bukkit.getPlayer(players.getUUID()) != null) {
										Main.ins.utils.getSoundMgr().playSound(Bukkit.getPlayer(players.getUUID()), JSound.ENDERDRAGON_GROWL, 1, 1);
										Bukkit.getPlayer(players.getUUID()).sendMessage(Main.ins.prefixTournament + "§6" + Bukkit.getPlayer(tournament.getOwner()).getName() + " §7hat ein Turnier erstellt! => §f/join " + Bukkit.getPlayer(tournament.getOwner()).getName());
									}
								}
								tournament.setOpen(true);
							} else {
								tournament.startStartCounter();
							}
							
						}
						
						
						openTournamentMainInv(p, tournament.getTournamentID());

					}
				}
			}
		}
	}

}
