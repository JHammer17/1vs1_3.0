package de.onevsone.commands.Variable;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.listener.ChallangeMgr;
import de.onevsone.objects.OneVsOnePlayer;

public class ChallangeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("1vs1.Command.Challange")) {
				
				if(args.length == 1) {
					if(!Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
						p.sendMessage(Main.ins.prefixRed + "§cDu bist nicht im 1vs1-Modus!");
						return true;
					}
					if(Bukkit.getPlayer(args[0]) != null) {
						Player receiver = Bukkit.getPlayer(args[0]);
						
						if(Main.ins.isInOneVsOnePlayers(receiver.getUniqueId())) {
							OneVsOnePlayer pReceiver = Main.ins.getOneVsOnePlayer(receiver);
							if(Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INLOBBY) {
								if(pReceiver.getpState() == PlayerState.INLOBBY) {
									
									OneVsOnePlayer senderPlayer = Main.ins.getOneVsOnePlayer(p);
									OneVsOnePlayer receiverPlayer = pReceiver;
									
									
									if(senderPlayer.getTeamObj() != null && !senderPlayer.getTeamObj().getLeader().equals(senderPlayer.getUUID())) {
										sender.sendMessage(Main.ins.prefixRed + "§cDu bist nicht der Leiter deines Teams!");
										return true;
									}
									
									
									if(receiverPlayer.getTeamObj() != null && !receiverPlayer.getTeamObj().getLeader().equals(receiverPlayer.getUUID())) {
										sender.sendMessage(Main.ins.prefixRed + "§c" + receiver.getDisplayName() + " ist nicht der Leiter seines Teams!");
										return true;
									}
									
									
									if(!pReceiver.getChallanged().contains(p.getUniqueId())) {
										ChallangeMgr.toggleChallange(p, receiver, true);
									} else {
										ChallangeMgr.acceptChallange(receiver, p);
									}
									
									
									
								} else {
									p.sendMessage(Main.ins.prefixRed + "§cDieser Spieler ist nicht in der Lobby!");
								}
							} else {
								p.sendMessage(Main.ins.prefixRed + "§cDiesen Befehl darfst du hier nicht ausführen!");
							}
							
						} else {
							p.sendMessage(Main.ins.prefixRed + "Dieser Spieler ist nicht im 1vs1-Modus!");
						}
						
					} else {
						p.sendMessage(Main.ins.prefixRed + "§c" + args[0] + " wurde nicht gefunden!");
					}
				} else {
					p.sendMessage(Main.ins.prefixRed + "Nutze: /c [Spieler]");
				}
				
			} else {
				p.sendMessage(Main.ins.prefixRed + "Dazu hast du nicht die benötigten Rechte!");
			}
			
			
		} else {
			sender.sendMessage(Main.ins.prefixRed + "§cDu bist kein Spieler!");
		}
		return true;
	}

}
