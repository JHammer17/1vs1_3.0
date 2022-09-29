/**
 * 
 */
package de.onevsone.objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 28.03.2018 13:39:38					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class OneVsOneTeam {

	private UUID leader;
	private ArrayList<UUID> members = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public OneVsOneTeam(Player leader, ArrayList<UUID> members) {
		this.leader = leader.getUniqueId();
		this.members = (ArrayList<UUID>) members.clone();
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public void addMember(UUID uuid) {
		if(!members.contains(uuid)) 
			members.add(uuid);
	}
	
	public void removeMember(UUID uuid) {
		if(members.contains(uuid)) 
			members.remove(uuid);
	}
	
	public boolean isMember(UUID uuid) {
		return members.contains(uuid);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UUID> getMemberList() {
		return (ArrayList<UUID>) members.clone();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UUID> getAll() {
		ArrayList<UUID> all = (ArrayList<UUID>) members.clone();
		all.add(leader);
		return all;
	}
	
	public String getTeamName(boolean upperCase) {
		if(getAll().size() > 2) {
			if(upperCase) {
				if(Bukkit.getPlayer(leader) != null) {
					return "Das Team von " + Bukkit.getPlayer(leader).getDisplayName();
				} else return "Das Team von -";
			} else {
				if(Bukkit.getPlayer(leader) != null) {
					return "das Team von " + Bukkit.getPlayer(leader).getDisplayName();
				} else return "das Team von -";
			}
			
			
		}
		StringBuilder builder = new StringBuilder();
		
		if(getAll().size() == 1) {
			if(Bukkit.getPlayer(getAll().get(0)) != null) {
				builder.append(Bukkit.getPlayer(getAll().get(0)).getDisplayName());
			} else {
				builder.append("-");
			}
		} else if(getAll().size() <= 0) {
			builder.append("-");
		} else {
			if(Bukkit.getPlayer(getAll().get(0)) != null) {
				builder.append(Bukkit.getPlayer(getAll().get(0)).getDisplayName()).append(" & ");
				if(Bukkit.getPlayer(getAll().get(1)) != null) {
					builder.append(Bukkit.getPlayer(getAll().get(1)).getDisplayName());
				} else {
					builder.append("-");
				}
			} else {
				builder.append("-");
			}
			
		}
		
		return builder.toString();
		
	}
	
	
	
	
	
	
}
