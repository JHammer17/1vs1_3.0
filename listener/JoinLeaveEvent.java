package de.onevsone.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.arenas.SpectateMgr;
import de.onevsone.commands.MainCMD;
import de.onevsone.enums.PlayerState;

public class JoinLeaveEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!Main.ins.database.isUserExists(e.getPlayer().getUniqueId())) {
					Main.ins.database.addUser(e.getPlayer().getUniqueId(), e.getPlayer().getName(), "Player");
				} else {
					
					Main.ins.database.updateUserData(e.getPlayer());
				}
				
				if(!Main.ins.database.isUserExists(e.getPlayer().getUniqueId(), "Kit")) {
					Main.ins.database.addUser(e.getPlayer().getUniqueId(), e.getPlayer().getName(), "Kit");
				}
				
				if(!Main.ins.database.isUserExists(e.getPlayer().getUniqueId(), "Stats")) {
					Main.ins.database.addUser(e.getPlayer().getUniqueId(), e.getPlayer().getName(), "Stats");
				}
				
			}
		}.runTaskLaterAsynchronously(Main.ins, 2);
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.SPECTATOR) {
				SpectateMgr.stopSpectate(e.getPlayer(), false, false);
			}
		}
		
		if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INARENA) {
			Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(e.getPlayer()).getArena()).death(e.getPlayer().getUniqueId());
		}
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) &&
		   Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).isIn1vs1()) {
			
			MainCMD.toggle1vs1(e.getPlayer(), false, false);
		}
	}
	
	@EventHandler
	public void playerTeleportEvent(PlayerTeleportEvent e) {
		if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			if(Main.ins.utils.isMainThread()) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1));
			
		}
	}
	
}
