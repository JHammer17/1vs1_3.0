package de.onevsone.arenas;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import de.onevsone.Main;
import de.onevsone.enums.KitPrefs;
import de.onevsone.enums.PlayerState;

public class KillMgr implements Listener {

	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		
		if(Main.ins.getOneVsOnePlayer(e.getEntity()).isIn1vs1()) {
			if(Main.ins.getOneVsOnePlayer(e.getEntity()).getpState() == PlayerState.INARENA) {
				
				if(Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(e.getEntity()).getArena()).getSettings().contains(KitPrefs.NOITEMDROPS)) {
					e.setKeepInventory(true);
				}
				
				e.setDeathMessage(null);
				e.setDroppedExp(0);
				
				e.setKeepInventory(true);
				e.setKeepLevel(true);
				
				e.getEntity().setHealth(20);
				
				if(e.getEntity().isDead()) e.getEntity().spigot().respawn();
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						String arena = Main.ins.getOneVsOnePlayer(e.getEntity()).getArena();
						
						if(e.getEntity().getKiller() != null) {
							Main.ins.getOneVsOneArena(arena).updateKD(e.getEntity().getKiller().getUniqueId(), true);
						}
						
						Main.ins.getOneVsOneArena(arena).updateKD(e.getEntity().getUniqueId(), false);
						Main.ins.getOneVsOneArena(arena).death(e.getEntity().getUniqueId());;
						
					}
				}.runTaskAsynchronously(Main.ins);
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(Main.ins.getOneVsOnePlayer(e.getPlayer()).isIn1vs1()) {
			if(Main.ins.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.INARENA) {
				e.setRespawnLocation(Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(e.getPlayer()).getArena()).getMiddleLoc());
			}
		}		
	}
	

	
	
}
