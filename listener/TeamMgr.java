/**
 * 
 */
package de.onevsone.listener;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.objects.OneVsOnePlayer;
import de.onevsone.objects.OneVsOneTeam;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 28.03.2018 14:14:47					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class TeamMgr implements Listener {

	@EventHandler
	public void onRequest(PlayerInteractAtEntityEvent e) {
		if(e.getPlayer().getType() == EntityType.PLAYER && e.getRightClicked().getType() == EntityType.PLAYER) {
			Player receiver = (Player) e.getRightClicked();
			Player sender = (Player) e.getPlayer();
			
				OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
				OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
				if((sender.getInventory().getItemInHand().equals(Main.ins.utils.getChallanger()) || sender.getInventory().getItemInHand().equals(Main.ins.utils.getChallangerDisabled()))   && 
				   senderPlayer.getpState() == PlayerState.INLOBBY && 
				   receiverPlayer.getpState() == PlayerState.INLOBBY) {
					
				/*Check auf Beziehung zwischen Receiver und Sender*/
				if(!receiverPlayer.getTeamInvited().contains(sender.getUniqueId())) {
					/*Keine Beziehung => Einladung senden/zurückziehen*/

					if(!receiverPlayer.getAcceptChallanges()) {
						/*Receiver aktzeptiert keine Challanges von anderen! => Ende*/
						e.setCancelled(true);
						sender.sendMessage(Main.ins.prefixRed + "§7" + receiver.getDisplayName() + " §7nimmt keine Teameinladungen an");
						return;
					}
					toggleInvite(sender, receiver, true);
				} else {
					acceptTeam(receiver, sender , true);
				}
				
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void removeComplete(Player p) {
		ArrayList<UUID> list = (ArrayList<UUID>) Main.ins.getOneVsOnePlayer(p).getTeamInvited().clone();
		
		for(UUID uuids : Main.ins.getOneVsOnePlayer(p).getTeamInvitedBy()) {
			
			list.remove(p.getUniqueId());
			Main.ins.getOneVsOnePlayer(uuids).setTeamInvited((ArrayList<UUID>) list.clone());
		}
		 list = (ArrayList<UUID>) Main.ins.getOneVsOnePlayer(p).getTeamInvitedBy().clone();
			
		for(UUID uuids : Main.ins.getOneVsOnePlayer(p).getTeamInvited()) {
			
			list.remove(p.getUniqueId());
			Main.ins.getOneVsOnePlayer(uuids).setTeamInvitedBy((ArrayList<UUID>) list.clone());
		}
		Main.ins.getOneVsOnePlayer(p).resetTeamInvites();
		
	}
	
	public static void revokeTeamInvite(Player sender, Player receiver) {
		OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
		OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
		
		ArrayList<UUID> list = senderPlayer.getTeamInvited();
		list.remove(receiver.getUniqueId());
		senderPlayer.setTeamInvited(list);
		list = receiverPlayer.getTeamInvitedBy();
		list.remove(sender.getUniqueId());
		receiverPlayer.setTeamInvitedBy(list);
		
	}
	
	public static void sendTeamInvite(Player sender, Player receiver) {
		OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
		OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
		
		ArrayList<UUID> list = senderPlayer.getTeamInvited();
		list.add(receiver.getUniqueId());
		senderPlayer.setTeamInvited(list);
		list = receiverPlayer.getTeamInvitedBy();
		list.add(sender.getUniqueId());
		receiverPlayer.setTeamInvitedBy(list);
		
	}
	
	
	
	public static void toggleInvite(Player sender, Player receiver, boolean sendMessages) {
		OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
		OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
		
		if(senderPlayer.getTeamObj() != null && !senderPlayer.getTeamObj().getLeader().equals(senderPlayer.getUUID())) {
			sender.sendMessage(Main.ins.prefixRed + "§cDu bist nicht der Leiter deines Teams!");
			return;
		}
		
		
		if(receiverPlayer.getTeamObj() != null) {
			sender.sendMessage(Main.ins.prefixRed + "§c" + receiver.getDisplayName() + " hat bereits ein Team!");
			return;
		}
		
		
		
		ArrayList<UUID> list = senderPlayer.getTeamInvited();
		if(list.contains(receiver.getUniqueId())) {
			revokeTeamInvite(sender, receiver);
			if(sendMessages) {
				sender.sendMessage(Main.ins.prefixBlue + "Du hast die §6Teameinladung §7gegenüber §6" + receiver.getDisplayName() + " §czurückgezogen.");
				receiver.sendMessage(Main.ins.prefixBlue + "§6" + sender.getDisplayName() + " §7hat seine §6Teameinladung §czurückgezogen.");
			}
		} else {
			if(!Main.ins.getOneVsOnePlayer(receiver).getAcceptChallanges()) {
				sender.sendMessage(Main.ins.prefixRed + "§7" + receiver.getDisplayName() + " §7nimmt keine Herausforderungen an");
				return;
			}
			
			sendTeamInvite(sender, receiver);
			if(sendMessages) {
				sender.sendMessage(Main.ins.prefixBlue + "Du hast §6" + receiver.getDisplayName() + " §7eine §6Teameinladung §7gesendet.");
				receiver.sendMessage(Main.ins.prefixBlue + "§6" + sender.getDisplayName() + " §7hat dir eine §6Teameinladung §7gesendet.");
				
			}
		}
	}
	
	public static void acceptTeam(Player leader, Player member, boolean sendMessages) {
		revokeTeamInvite(leader, member);
		removeComplete(member);
		ChallangeMgr.removeComplete(member);
		
		
		if(sendMessages) {
			member.sendMessage(Main.ins.prefixBlue + "Du bist nun im Team von §6" + leader.getDisplayName());
			
		} 
		
		OneVsOnePlayer leaderPlayer = Main.ins.getOneVsOnePlayer(leader);
		
		
		OneVsOneTeam team = leaderPlayer.getTeamObj();
		if(team == null) {
			ArrayList<UUID> members = new ArrayList<>();
			members.add(member.getUniqueId());
			team = new OneVsOneTeam(leader, members);
		}
		
		if(!team.isMember(member.getUniqueId())) {
			team.addMember(member.getUniqueId());
		}
		
		for(UUID uuid : team.getAll()) {
			if(sendMessages) {
				if(Bukkit.getPlayer(uuid) != null)
				 Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixBlue + "§6" + member.getDisplayName() + " §7ist dem Team beigetreten!");
			}
			Main.ins.getOneVsOnePlayer(uuid).setTeamObj(team);
		}
	}

	public static void removeTeam(Player removed, boolean sendMessages) {
		removeComplete(removed);
		
		if(sendMessages) {
			removed.sendMessage(Main.ins.prefixBlue + "Du hast das Team verlassen!");
		} 
		
		OneVsOnePlayer member = Main.ins.getOneVsOnePlayer(removed);
		if(member.getTeamObj() != null) {
			
			for(UUID uuid : member.getTeamObj().getAll()) {
				if(sendMessages) {
					if(Bukkit.getPlayer(uuid) != null)
					 Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixBlue + "§6" + removed.getDisplayName() + " §7hat das Team verlassen!");
				}
				
				OneVsOneTeam mgr = Main.ins.getOneVsOnePlayer(uuid).getTeamObj();
				mgr.removeMember(removed.getUniqueId());
				
				Main.ins.getOneVsOnePlayer(uuid).setTeamObj(mgr);
			}
			
			member.setTeamObj(null);
		}
		
		
		
	}
	
	public static void destroyTeam(Player leader, boolean sendMessages) {
		
		
		OneVsOnePlayer member = Main.ins.getOneVsOnePlayer(leader);
		if(member.getTeamObj() != null) {
			
			for(UUID uuid : member.getTeamObj().getAll()) {
				if(sendMessages) {
					if(Bukkit.getPlayer(uuid) != null)
					 Bukkit.getPlayer(uuid).sendMessage(Main.ins.prefixBlue + "§7Das Team wurde aufgelöst!");
				}
				
				
				Main.ins.getOneVsOnePlayer(uuid).setTeamObj(null);
			}
			
			member.setTeamObj(null);
		}
		
		
		
	}
	
	
}
