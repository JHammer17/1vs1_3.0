package de.onevsone.listener;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.methods.QueueManager;
import de.onevsone.objects.OneVsOnePlayer;

public class ChallangeMgr implements Listener {

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if(e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
			Player receiver = (Player) e.getEntity();
			Player sender = (Player) e.getDamager();
			
			OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
			OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
			
			if(senderPlayer.isIn1vs1() && receiverPlayer.isIn1vs1()) {
				
				if((sender.getItemInHand().equals(Main.ins.utils.getChallanger()) || sender.getItemInHand().equals(Main.ins.utils.getChallangerDisabled())) && 
				   senderPlayer.getpState() == PlayerState.INLOBBY && receiverPlayer.getpState() == PlayerState.INLOBBY) {
					/*Check, ob eine Beziehung besteht oder nicht*/
					if(!receiverPlayer.getChallanged().contains(sender.getUniqueId())) {
						/*Es besteht keine Beziehung => Anfrage wird gesendet oder zurückgezogen*/
						
						if(!receiverPlayer.getAcceptChallanges()) {
							/*Receiver aktzeptiert keine Challanges von anderen! => Ende*/
							e.setCancelled(true);
							sender.sendMessage(Main.ins.prefixRed + "§7" + receiver.getDisplayName() + " §7nimmt keine Herausforderungen an");
							return;
						}
						
						/*Check, ob eine Herausfoderung zurückgezogen werden soll*/
						if(!senderPlayer.getChallanged().contains(receiver.getUniqueId())) {
							/*Herausforderung soll gesendet werden*/
							
							
							
							if(senderPlayer.getTeamObj() != null && !senderPlayer.getTeamObj().getLeader().equals(senderPlayer.getUUID())) {
								sender.sendMessage(Main.ins.prefixRed + "§cDu bist nicht der Leiter deines Teams!");
								return;
							}
							
							
							if(receiverPlayer.getTeamObj() != null && !receiverPlayer.getTeamObj().getLeader().equals(receiverPlayer.getUUID())) {
								sender.sendMessage(Main.ins.prefixRed + "§c" + receiver.getDisplayName() + " ist nicht der Leiter seines Teams!");
								return;
							}
							
							sendChallange(sender, receiver);
							
							sender.sendMessage(Main.ins.prefixBlue + "Du hast §6" + receiver.getDisplayName() + " §7eine §6Herausforderung §7gesendet.");
							receiver.sendMessage(Main.ins.prefixBlue + "§6" + sender.getDisplayName() + " §7hat dir eine §6Herausforderung §7gesendet.");
							
							
							
						} else {
							/*Herausforderung soll zurückgezogen werden*/
							
							revokeChallange(sender, receiver);
							
							sender.sendMessage(Main.ins.prefixBlue + "Du hast die §6Herausforderung §7gegenüber §6" + receiver.getDisplayName() + " §czurückgezogen.");
							receiver.sendMessage(Main.ins.prefixBlue + "§6" + sender.getDisplayName() + " §7hat seine §6Herausforderung §czurückgezogen.");
						}
					} else {
						/*Es besteht eine Beziehung => Kampf wurde angenommen!*/
						
						if(senderPlayer.getTeamObj() != null && !senderPlayer.getTeamObj().getLeader().equals(senderPlayer.getUUID())) {
							sender.sendMessage(Main.ins.prefixRed + "§cDu bist nicht der Leiter deines Teams!");
							return;
						}
						
						
						if(receiverPlayer.getTeamObj() != null && !receiverPlayer.getTeamObj().getLeader().equals(receiverPlayer.getUUID())) {
							sender.sendMessage(Main.ins.prefixRed + "§c" + receiver.getDisplayName() + " ist nicht der Leiter seines Teams!");
							return;
						}
						acceptChallange(receiver, sender);
					}
					
				}
			}	
		}

	}
	
	@SuppressWarnings("unchecked")
	public static void removeComplete(Player p) {
		ArrayList<UUID> list = (ArrayList<UUID>) Main.ins.getOneVsOnePlayer(p).getChallanged().clone();
		
		for(UUID uuids : Main.ins.getOneVsOnePlayer(p).getChallangedBy()) {
			
			list.remove(p.getUniqueId());
			Main.ins.getOneVsOnePlayer(uuids).setChallanged((ArrayList<UUID>) list.clone());
		}
		 list = (ArrayList<UUID>) Main.ins.getOneVsOnePlayer(p).getChallangedBy().clone();
			
		for(UUID uuids : Main.ins.getOneVsOnePlayer(p).getChallanged()) {
			
			list.remove(p.getUniqueId());
			Main.ins.getOneVsOnePlayer(uuids).setChallangedBy((ArrayList<UUID>) list.clone());
		}
		Main.ins.getOneVsOnePlayer(p).resetChallanges();
		
	}
	
	public static void revokeChallange(Player sender, Player receiver) {
		OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
		OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
		
		ArrayList<UUID> list = senderPlayer.getChallanged();
		list.remove(receiver.getUniqueId());
		senderPlayer.setChallanged(list);
		list = receiverPlayer.getChallangedBy();
		list.remove(sender.getUniqueId());
		receiverPlayer.setChallangedBy(list);
		
	}
	
	public static void sendChallange(Player sender, Player receiver) {
		OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
		OneVsOnePlayer receiverPlayer = Main.ins.getOneVsOnePlayer(receiver);
		
		ArrayList<UUID> list = senderPlayer.getChallanged();
		list.add(receiver.getUniqueId());
		senderPlayer.setChallanged(list);
		list = receiverPlayer.getChallangedBy();
		list.add(sender.getUniqueId());
		receiverPlayer.setChallangedBy(list);
		
	}
	
	
	
	public static void toggleChallange(Player sender, Player receiver, boolean sendMessages) {
		OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(sender);
		
		
		ArrayList<UUID> list = senderPlayer.getChallanged();
		if(list.contains(receiver.getUniqueId())) {
			revokeChallange(sender, receiver);
			if(sendMessages) {
				sender.sendMessage(Main.ins.prefixBlue + "Du hast die §6Herausforderung §7gegenüber §6" + receiver.getDisplayName() + " §czurückgezogen.");
				receiver.sendMessage(Main.ins.prefixBlue + "§6" + sender.getDisplayName() + " §7hat seine §6Herausforderung §czurückgezogen.");
			}
		} else {
			
			if(!Main.ins.getOneVsOnePlayer(receiver).getAcceptChallanges()) {
				sender.sendMessage(Main.ins.prefixRed + "§7" + receiver.getDisplayName() + " §7nimmt keine Herausforderungen an");
				return;
			}
			
			sendChallange(sender, receiver);
			if(sendMessages) {
				sender.sendMessage(Main.ins.prefixBlue + "Du hast §6" + receiver.getDisplayName() + " §7eine §6Herausforderung §7gesendet.");
				receiver.sendMessage(Main.ins.prefixBlue + "§6" + sender.getDisplayName() + " §7hat dir eine §6Herausforderung §7gesendet.");
				
			}
		}
	}
	
	
	public static void acceptChallange(Player pos1, Player pos2) {
		QueueManager.tryJoin(pos1.getUniqueId(), pos2.getUniqueId(), false);
	}
	
	
}
