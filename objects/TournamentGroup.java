/**
 * 
 */
package de.onevsone.objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 17:47:19					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class TournamentGroup {

	UUID leader;
	ArrayList<UUID> players = new ArrayList<>();
	int number = 0;
	
	int qPoints = 0;
	
	@SuppressWarnings("unchecked")
	public TournamentGroup(UUID leader, ArrayList<UUID> players, int number) {
		this.leader = leader;
		this.players = (ArrayList<UUID>) players.clone();
		this.number = number;
	}
	
	public UUID getLeader() {
		return leader;
	}

	public void setLeader(UUID leader) {
		this.leader = leader;
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<UUID> players) {
		this.players = players;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UUID> getAll() {
		ArrayList<UUID> all = (ArrayList<UUID>) players.clone();
		all.add(leader);
		return all;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
 	
	public String getName() {
		if(getAll().size() == 1) {
			if(Bukkit.getPlayer(leader) != null) return Bukkit.getPlayer(leader).getDisplayName();
		} else if(getAll().size() == 2) {
			if(Bukkit.getPlayer(leader) != null && getPlayers().size() > 0 && Bukkit.getPlayer(getPlayers().get(0)) != null) {
				return Bukkit.getPlayer(leader).getDisplayName() + " & " + Bukkit.getPlayer(getPlayers().get(0)).getDisplayName();
			}
			
		} else if(getAll().size() > 2) {
			if(Bukkit.getPlayer(leader) != null) return "Das Team von " + Bukkit.getPlayer(leader).getDisplayName();
		} 
		return "-";
	}
	
	public void setQPoints(int points) {
		this.qPoints = points;
	}
	
	public int getQPoints() {
		return this.qPoints;
	}
	
}
