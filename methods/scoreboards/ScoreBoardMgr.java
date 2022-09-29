/**
 * 
 */
package de.onevsone.methods.scoreboards;

import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.objects.OneVsOnePlayer;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 29.03.2018 10:11:43					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class ScoreBoardMgr {

	LobbyScoreboard lbSb = new LobbyScoreboard();
	ArenaScoreboard arSb = new ArenaScoreboard();
	
	public ScoreBoardMgr() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(OneVsOnePlayer player : Main.ins.getOneVsOnePlayersCopy().values()) {
					if(player.getpState() == PlayerState.INLOBBY ||
					   player.getpState() == PlayerState.INKITEDIT) {
						lbSb.updateLobbyBoard(player.getPlayer());
					} else if(player.getpState() == PlayerState.INARENA) {
						arSb.updateArenaBoard(player.getPlayer(), player.getArena());
					} else if(player.getpState() == PlayerState.SPECTATOR) {
						arSb.updateArenaBoard(player.getPlayer(), player.getSpecator());
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 5);
		
		
	}
	 
	
}
